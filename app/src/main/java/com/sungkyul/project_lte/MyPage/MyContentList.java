package com.sungkyul.project_lte.MyPage;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sungkyul.project_lte.Calculator.CalculatorActivity;
import com.sungkyul.project_lte.Community.Community_Main;
import com.sungkyul.project_lte.MainNLogin.Login;
import com.sungkyul.project_lte.R;
import com.sungkyul.project_lte.Etc.RbPreference;
import com.sungkyul.project_lte.SOS.SOSMain;

/**
 * Created by WonHo on 2016-05-23.
 */
// 내가 쓴 글
public class MyContentList extends ActionBarActivity implements NavigationView.OnNavigationItemSelectedListener {

    TextView nav_nick, nav_name; //  로그인 여부, 이메일 보여줄 text 선언
    Button btn_navLogin; // 내비게이션 로그인/로그아웃 버튼
    private Toolbar supportActionBar; // ToolBar 선언
    MenuItem navLogin;   // 로그인, 이메일 선언
    DrawerLayout drawer;
    Toolbar toolbar;
    String email, name, login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycontent_list);
        navi(); // 내비게이션 연결 메소드 호출
        tabInit(); // 초기화 메소드 호출
        login(); // 로그인 값 가져올 메소드 호출
        log(); // 로그인 여부 메소드 호출
    }
    // 로그인 여부 메소드
    private void log() {
        if (login != null) {    // 로그인시
            btn_navLogin.setText("로그아웃");
            nav_name.setText(email);
            nav_nick.setText(name);

        } else {    // 비로그인시
            btn_navLogin.setText("로그인");
            nav_name.setText("로그인이 필요합니다.");
            nav_nick.setText("");
        }
    }
    /***
     * 로그인 값 가져올 메소드
     */
    private void login() {
        RbPreference pref = new RbPreference(this); // 값 가져오기 위한 RbPreference class 생성
        login = pref.getValue("login", null);      // pref로부터 login값 가져와 login에 할당
        email = pref.getValue("email", null);      // pref로부터 email값 가져와 email에 할당
        name = pref.getValue("name", null); // pref로부터 name값 가져와 name에 할당
    }
    // 내비게이션 연결 메소드
    void navi() {
            drawer = (DrawerLayout) findViewById(R.id.drawer_mycontent);  // 네비게이션 드로우를 적용하기 위해 DrawerLayout 선언 및 xml과 연동

            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(                // 액션바 선언 및 할당
                    this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState(); // 액션바를 indicator할 수 있도록 지원하는 메소드

            final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view); // 해당 네비게이션의 모습 선언 및 xml과 연동
            navigationView.setNavigationItemSelectedListener(this);                        // 네비게이션 이벤트 처리

            View header = navigationView.getHeaderView(0);
        /*View view=navigationView.inflateHeaderView(R.layout.nav_header_main);*/
            navLogin = navigationView.getMenu().getItem(0);
            nav_nick = (TextView) header.findViewById(R.id.nav_nick);
            nav_name = (TextView) header.findViewById(R.id.nav_email);
            btn_navLogin = (Button) header.findViewById(R.id.btnNavLogin);
            /***
             *  네비게이션 로그인/로그아웃 버튼
             */
            btn_navLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (btn_navLogin.getText().toString() == "로그아웃") {
                        // 로그인인 경우
                        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(MyContentList.this);
                        alert.setTitle("로그아웃");
                        alert.setMessage("로그아웃 하시겠습니까?");
                        alert.setNegativeButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 로그아웃 정보 필요
                                // Login으로 이동
                                droppreference();   // 로그아웃 메소드 호출
                                Toast.makeText(MyContentList.this, "빠이", Toast.LENGTH_SHORT).show();
                                btn_navLogin.setText("로그인");
                                nav_name.setText("로그인이 필요합니다");
                                nav_nick.setText("닉네임");
                                drawer.closeDrawer(GravityCompat.START);        // 네비게이션 창을 닫음
                            }
                        });
                        alert.setPositiveButton("아니오", null);
                        alert.show();
                    } else {
                        Toast.makeText(MyContentList.this, "로그인으로 이동", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
    // 초기화 메소드
    void tabInit() {
        // 첫 시작 View Pager 선언
        ViewPager contentPager = (ViewPager) findViewById(R.id.contentPager);

        // 어뎁터 가져오기
        MyContentAdapter myContentAdapter = new MyContentAdapter(getSupportFragmentManager(), MyContentList.this);
        contentPager.setAdapter(myContentAdapter);

        // 메인 layout
        TabLayout tabLayout = (TabLayout) findViewById(R.id.contentTab);

        // viewPager 와 tabLayout을 연결
        tabLayout.setupWithViewPager(contentPager);

        for (int i=0; i<tabLayout.getTabCount(); i++) {

            TabLayout.Tab tabName = tabLayout.getTabAt(i);

            tabName.setCustomView(myContentAdapter.getTabView(i));
        }
    }
    /***
     * 메뉴바 생성
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /***
     * 생성된 메뉴바를 액션바로 할당
     *
     * @param supportActionBar
     */
    public void setSupportActionBar(Toolbar supportActionBar) {
        this.supportActionBar = supportActionBar;
    }

    /***
     * 네비게이션에서 뒤로가기 클릭 메소드
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_mycontent); // 네비게이션 선언 및 xml 연동
        if (drawer.isDrawerOpen(GravityCompat.START)) {     // 네비게이션이 켜져있는 경우
            drawer.closeDrawer(GravityCompat.START);        // 네비게이션 창을 닫음
        } else {
            super.onBackPressed();                          // 뒤로가기 이벤트
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_mypage) {
            // 마이페이지
            finish();
        } else if (id == R.id.nav_cart) {
            // 여행 카트
            Intent intent = new Intent(getApplicationContext(), CartActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_free) {
            // 여행 커뮤니티
            Intent intent = new Intent(getApplicationContext(), Community_Main.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_cal) {
            // 계산기
            Intent intent = new Intent(getApplicationContext(), CalculatorActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_sos) {
            // SOS
            Intent intent = new Intent(getApplicationContext(), SOSMain.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_help) {
            Intent intent = new Intent(getApplicationContext(), Help.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_appversion) {
            Intent intent = new Intent(getApplicationContext(), HelpAppVersion.class);
            startActivity(intent);
            finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_mycontent);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /***
     * 로그아웃 메소드
     * 해당 값에 null로 채우는 메소드
     */
    public void droppreference() {
        RbPreference pref = new RbPreference(this); // RbPreference 생성
        pref.put("email", null);                    // 해당 email 값에 null 저장
        pref.put("name", null);                    // 해당 name 값에 null 저장
        pref.put("login", null);                    // 해당 login 값에 null 저장
        login = pref.getValue("login", null);       // null값을 login에 할당
        email = pref.getValue("email", null);       // null값을 email에 할당
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            //하드웨어 뒤로가기 버튼에 따른 이벤트 설정
            case KeyEvent.KEYCODE_BACK:
                Intent intent_main = new Intent(getApplicationContext(), My_Page.class);
                startActivity(intent_main);
                finish();
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}