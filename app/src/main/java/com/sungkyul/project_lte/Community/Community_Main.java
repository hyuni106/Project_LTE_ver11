package com.sungkyul.project_lte.Community;

import android.app.AlertDialog;
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
import com.sungkyul.project_lte.flight.FlightActivity;
import com.sungkyul.project_lte.game.GameStart;
import com.sungkyul.project_lte.MyPage.Help;
import com.sungkyul.project_lte.MainNLogin.Login;
import com.sungkyul.project_lte.MainNLogin.Main;
import com.sungkyul.project_lte.MyPage.CartActivity;
import com.sungkyul.project_lte.MyPage.My_Page;
import com.sungkyul.project_lte.R;
import com.sungkyul.project_lte.Etc.RbPreference;
import com.sungkyul.project_lte.SOS.SOSMain;

// 커뮤니티 메인 페이지
public class Community_Main extends ActionBarActivity implements NavigationView.OnNavigationItemSelectedListener, DialogInterface.OnKeyListener {

    TextView nav_nick, nav_name; //  로그인 여부, 이메일 보여줄 text 선언
    Button btn_navLogin; // 내비게이션 로그인/로그아웃 버튼
    private Toolbar supportActionBar; // ToolBar 선언
    private String name, email, login;
    MenuItem navLogin;   // 로그인, 이메일 선언
    DrawerLayout drawer;
    Toolbar toolbar;
    RbPreference pref = new RbPreference(this); // RbPreference 생성

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_main);
        // 툴바 적용
        tabInit(); // 초기화 메소드 호출
        login(); // 로그인 값 가져오는 메소드 호출
        navi(); // 내비게이션 연결 메소드 호출

    }
    // 내비게이션 연결 메소드
    void navi() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_com);  // 네비게이션 드로우를 적용하기 위해 DrawerLayout 선언 및 xml과 연동

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

        if(login != null) {
            btn_navLogin.setText("로그아웃");
            nav_name.setText(email);
            nav_nick.setText(name);
        } else {
            btn_navLogin.setText("로그인");
            nav_name.setText("로그인이 필요합니다");
            nav_nick.setText("닉네임");
        }

        /***
         *  네비게이션 로그인/로그아웃 버튼
         */
        btn_navLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (btn_navLogin.getText().toString() == "로그아웃") {
                    // 로그인인 경우
                    android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(Community_Main.this);
                    alert.setTitle("로그아웃");
                    alert.setMessage("로그아웃 하시겠습니까?");
                    alert.setNegativeButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 로그아웃 정보 필요
                            droppreference();   // 로그아웃 메소드 호출
                            Toast.makeText(Community_Main.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                            btn_navLogin.setText("로그인");
                            nav_name.setText("로그인이 필요합니다");
                            nav_nick.setText("닉네임");
                            drawer.closeDrawer(GravityCompat.START);        // 네비게이션 창을 닫음
                        }
                    });
                    alert.setPositiveButton("아니오", null);
                    alert.show();
                } else {

                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    /***
     * 로그인 값 가져올 메소드
     */
    private void login() {
        login = pref.getValue("login", null);      // pref로부터 login값 가져와 login에 할당
        email = pref.getValue("email", null);      // pref로부터 email값 가져와 login에 할당
        name = pref.getValue("name", null);
        pref.put("activity", "3");
    }

    // 초기화 메소드
    void tabInit() {
        // 첫 시작 View Pager 선언
        ViewPager viewpager = (ViewPager) findViewById(R.id.viewPager);

        // 어뎁터 가져오기
        ViewAdapter viewAdapter = new ViewAdapter(getSupportFragmentManager(), Community_Main.this);

        // 뷰페이저 어댑터 설정
        viewpager.setAdapter(viewAdapter);

        // 메인 layout
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabView);

        // viewPager 와 tabLayout을 연결
        tabLayout.setupWithViewPager(viewpager);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {

            TabLayout.Tab tabName = tabLayout.getTabAt(i);

            tabName.setCustomView(viewAdapter.getTabView(i));
        }
    }

    /***
     * 네비게이션에서 뒤로가기 클릭 메소드
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_com); // 네비게이션 선언 및 xml 연동
        if (drawer.isDrawerOpen(GravityCompat.START)) {     // 네비게이션이 켜져있는 경우
            drawer.closeDrawer(GravityCompat.START);        // 네비게이션 창을 닫음
        } else {
            super.onBackPressed();                          // 뒤로가기 이벤트
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

    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_mypage) {
            // 마이페이지
            if (login != null) {
                Intent intent = new Intent(getApplicationContext(), My_Page.class);
                startActivity(intent);
                finish();
            } else {
                // 로그인인 필요한 경우
                android.support.v7.app.AlertDialog.Builder alert_confirm = new  android.support.v7.app.AlertDialog.Builder(Community_Main.this);
                alert_confirm.setMessage("로그인이 필요합니다 로그인 하시겠습니까?").setCancelable(false).setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 'YES'
                                Intent intent = new Intent(getApplicationContext(), Login.class);
                                startActivity(intent);
                                finish();
                            }
                        }).setNegativeButton("아니오", null);

                android.support.v7.app.AlertDialog alert = alert_confirm.create();
                alert.setOnKeyListener(this);
                alert.show();
            }
        } else if (id == R.id.nav_cart) {
            // 여행 카트
            if (login != null) {
                Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(intent);
                finish();
            } else {
                // 로그인인 필요한 경우
                android.support.v7.app.AlertDialog.Builder alert = new   android.support.v7.app.AlertDialog.Builder(this);
                alert.setTitle("로그인");
                alert.setMessage("로그인이 필요합니다. 로그인 하시겠습니까?");
                alert.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 로그인
                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        startActivity(intent);
                        finish();
                    }
                }).setNegativeButton("아니오", null);
                alert.setOnKeyListener(this);
                alert.show();
            }
        } else if (id == R.id.nav_free) {
            // 여행 커뮤니티
            Toast.makeText(Community_Main.this, "현재 위치에 있습니다.", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_cal) {
            // 계산기
            Intent intent = new Intent(getApplicationContext(), CalculatorActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_sos) {
            // SOS
            if (login != null) {
                Intent intent = new Intent(getApplicationContext(), SOSMain.class);
                startActivity(intent);
                finish();
            } else {
                // 로그인인 필요한 경우
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(Community_Main.this);
                alert_confirm.setMessage("로그인이 필요합니다 로그인 하시겠습니까?").setCancelable(false).setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 'YES'
                                Intent intent = new Intent(getApplicationContext(), Login.class);
                                startActivity(intent);
                                finish();
                            }
                        }).setNegativeButton("아니오", null);

                AlertDialog alert = alert_confirm.create();
                alert.setOnKeyListener(this);
                alert.show();
            }
        } else if (id == R.id.nav_help) {
            Intent intent = new Intent(getApplicationContext(), Help.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_flight) {
            Intent intent = new Intent(getApplicationContext(), FlightActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_game) {
            Intent intent = new Intent(getApplicationContext(), GameStart.class);
            startActivity(intent);
            finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_com);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /***
     * 로그아웃 메소드
     * 해당 값에 null로 채우는 메소드
     */
    public void droppreference() {
        pref.put("email", null);                    // 해당 email 값에 null 저장
        pref.put("name", null);                    // 해당 name 값에 null 저장
        pref.put("login", null);                    // 해당 login 값에 null 저장
        login = pref.getValue("login", null);       // null값을 login에 할당
        email = pref.getValue("email", null);       // null값을 email에 할당
    }

    // 뒤로가기 버튼 클릭 시 발생하는 이벤트 메소드
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            //하드웨어 뒤로가기 버튼에 따른 이벤트 설정
            case KeyEvent.KEYCODE_BACK:
                Intent intent_main = new Intent(getApplicationContext(), Main.class);
                startActivity(intent_main);
                finish();
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    /***
     * alert 창 시 back버튼
     *
     * @param dialog
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            dialog.dismiss();
            return true;
        }
        return false;
    }

}
