package com.sungkyul.project_lte.MainNLogin;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.sungkyul.project_lte.Calculator.CalculatorActivity;
import com.sungkyul.project_lte.Community.Community_Main;
import com.sungkyul.project_lte.MyPage.CartActivity;
import com.sungkyul.project_lte.MyPage.Help;
import com.sungkyul.project_lte.MyPage.HelpAppVersion;
import com.sungkyul.project_lte.MyPage.My_Page;
import com.sungkyul.project_lte.R;
import com.sungkyul.project_lte.Etc.RbPreference;
import com.sungkyul.project_lte.SOS.SOSMain;
import com.sungkyul.project_lte.flight.FlightActivity;
import com.sungkyul.project_lte.game.GameStart;

/***
 * 메인 - 완료
 */
public class Main extends FragmentActivity implements NavigationView.OnNavigationItemSelectedListener, DialogInterface.OnKeyListener {

    // 계산기 버튼, 커뮤니티 버튼, SOS 버튼, 마이페이지 버튼, 로그인 버튼, 로그아웃 버튼, 내비게이션 로그인/로그아웃 버튼 선언
    Button btnCal, btnComm, btnSOS, btnLogin, btn_logout, btn_navLogin, btn_flight;
    String login;
    String email;
    MenuItem navLogin;   // 로그인, 이메일 선언
    TextView nav_nick, nav_name; //  로그인 여부, 이메일 보여줄 text 선언
    private Toolbar supportActionBar; // ToolBar 선언
    private String name;
    DrawerLayout drawer;
    RbPreference pref = new RbPreference(this); // 값을 가져올 RePreference class 생성

    ImageView image1, image2, image3;
    ViewFlipper vFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view); // 보여줄 레이아웃 설정

        // 계산기 버튼, 커뮤니티 버튼, SOS 버튼, 마이페이지 버튼, 로그인 버튼, 로그아웃 버튼 등 을 xml과 연동
        btnCal = (Button) findViewById(R.id.btn_calculator);
        btnComm = (Button) findViewById(R.id.btn_community);
        btnSOS = (Button) findViewById(R.id.btn_sos);
        btn_flight = (Button) findViewById(R.id.btn_flight);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btn_logout = (Button) findViewById(R.id.btn_logout);

        //ViewFlipper
        image1 = (ImageView) findViewById(R.id.image1);
        image2 = (ImageView) findViewById(R.id.image2);
        image3 = (ImageView) findViewById(R.id.image3);
        vFlipper = (ViewFlipper) findViewById(R.id.viewFlipper1);
        vFlipper.startFlipping();
        vFlipper.setFlipInterval(4000);

        image1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.discoverhongkong.com/kr/plan-your-trip/travel-kit/guides.jsp"));
                startActivity(intent);
            }
        });

        image2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.tourtaiwan.or.kr/download_list.asp"));
                startActivity(intent);
            }
        });

        image3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.welcometojapan.or.kr/board/ebook/"));
                startActivity(intent);
            }
        });

        // 툴바 적용
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //tabInit();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);  // 네비게이션 드로우를 적용하기 위해 DrawerLayout 선언 및 xml과 연동
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
        login();    // 로그인 메소드 호출


        if (login != null) {    // 로그인시
//            check_login.setText(email + " 님 환영합니다.");   // 아이디 값을 check_login 변수를 통해 보여줌
            btnLogin.setVisibility(View.INVISIBLE); // 로그인 미표시
            btn_logout.setVisibility(View.VISIBLE); // 로그아웃 표시
            btn_navLogin.setText("로그아웃");
            nav_name.setText(email);
            nav_nick.setText(name);

        } else {    // 비로그인시
//            check_login.setText("비로그인 유저입니다.");
            btn_navLogin.setText("로그인");
            nav_name.setText("로그인이 필요합니다.");
            nav_nick.setText("");
        }

        /***
         *  네비게이션 로그인/로그아웃 버튼
         */
        btn_navLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (btn_navLogin.getText().toString() == "로그아웃") {
                    // 로그인인 경우
                    AlertDialog.Builder alert = new AlertDialog.Builder(Main.this);
                    alert.setTitle("로그아웃");
                    alert.setMessage("로그아웃 하시겠습니까?");
                    alert.setNegativeButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            droppreference();   // 로그아웃 메소드 호출
                            Toast.makeText(Main.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                            btn_logout.setVisibility(View.INVISIBLE);          // 로그아웃 미표시
                            btnLogin.setVisibility(View.VISIBLE);               // 로그인 표시
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

        /***
         * SOS 버튼 이벤트 처리
         */
        btnSOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (login != null) {    // 로그인시
                    // SOS 액티비티로 이동할 intent 선언
                    Intent intent_sosmain = new Intent(getApplicationContext(), SOSMain.class);
                    startActivity(intent_sosmain);  // SOSMain 액티비티로 이동
                    finish();
                } else {                // 비로그인시
                    Toast.makeText(Main.this, "로그인이 필요합니다.", Toast.LENGTH_LONG).show(); // 로그인 필요 메시지 출력
                    // 로그인 액티비티로 이동할 intent 선언
                    Intent intent_login = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent_login); // 로그인 액티비티로 이동
                    finish();                    // 현 액티비티 종료
                }
            }
        });
        /***
         * 로그인 버튼 이벤트 처리
         */
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref.put("activity", "0");
                // 로그인 액티비티로 이동할 intent 선언
                Intent intent_login = new Intent(getApplicationContext(), Login.class);
                startActivity(intent_login);    // 로그인 액티비티로 이동
                finish();                       // 현 액티비티 종료
            }
        });
        /***
         * 환율 계산기 버튼 이벤트 처리
         */
        btnCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 환율 계산기 액티비티로 이동할 intent 선언
                Intent intent_cal = new Intent(getApplicationContext(), CalculatorActivity.class);
                startActivity(intent_cal);  // 환율 계산기 액티비티로 이동
                finish();
            }
        });
        /***
         *  여행 커뮤니티 버튼 이벤트 처리
         */
        btnComm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // 커뮤니티 액티비티로 이동할 intent 선언
                Intent intent_community = new Intent(getApplicationContext(), Community_Main.class);
                startActivity(intent_community);    //커뮤니티 액티비티로 이동
                finish();                           // 현 액티비티 종료
            }
        });
        /***
         *  운항정보 버튼 이벤트 처리
         */
        btn_flight.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // 커뮤니티 액티비티로 이동할 intent 선언
                Intent intent_flight = new Intent(getApplicationContext(), FlightActivity.class);
                startActivity(intent_flight);    //커뮤니티 액티비티로 이동
                finish();                           // 현 액티비티 종료
            }
        });
        /***
         * 로그아웃 버튼 이벤트 처리
         */
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                droppreference();   // 로그아웃 메소드 호출
                Toast.makeText(Main.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                pref.put("login", null);
                btn_logout.setVisibility(View.INVISIBLE);          // 로그아웃 미표시
                btnLogin.setVisibility(View.VISIBLE);               // 로그인 표시
                btn_navLogin.setText("로그인");
                navLogin.setTitle("로그인");
                nav_name.setText("로그인이 필요합니다");
                nav_nick.setText("닉네임");
            }
        });

    }
    /***
     * 뒤로가기 클릭 메소드
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout); // 네비게이션 선언 및 xml 연동
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

    /***
     * 네비게이션 목차에 해당되는 아이템 클릭시 처리 이벤트
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.btnNavLogin) {
            if (btn_navLogin.getText().toString() == "로그아웃") {
                // 로그인인 경우
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("로그아웃");
                alert.setMessage("로그아웃 하시겠습니까?");
                alert.setNegativeButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 로그아웃 정보 필요
                        // Login으로 이동
                        droppreference();   // 로그아웃 메소드 호출
//                        check_login.setText("비로그인 유저입니다.");      // check_login 을 로그인 유저에서 비로그인 유저로 표시
                        Toast.makeText(Main.this, "빠이", Toast.LENGTH_SHORT).show();
                        btn_logout.setVisibility(View.INVISIBLE);          // 로그아웃 미표시
                        btnLogin.setVisibility(View.VISIBLE);               // 로그인 표시
                        btn_navLogin.setText("로그인");
                        nav_name.setText("로그인이 필요합니다");
                        nav_nick.setText("");
                    }
                });
                alert.setPositiveButton("아니오", null);
                alert.show();
            } else {
                Toast.makeText(Main.this, "로그인으로 이동", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
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
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(Main.this);
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
        } else if (id == R.id.nav_cart) {
            // 여행 카트
            if (login != null) {
                Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(intent);
                finish();
            } else {
                // 로그인인 필요한 경우
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
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
            if (login != null) {
                Intent intent = new Intent(getApplicationContext(), SOSMain.class);
                startActivity(intent);
                finish();
            } else {
                // 로그인인 필요한 경우
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(Main.this);
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
        } else if (id == R.id.nav_appversion) {
            Intent intent = new Intent(getApplicationContext(), HelpAppVersion.class);
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /***
     * 로그인 값 가져올 메소드
     */
    public void login() {
        login = pref.getValue("login", null);      // pref로부터 login값 가져와 login에 할당
        email = pref.getValue("email", null);      // pref로부터 email값 가져와 email에 할당
        name = pref.getValue("name", null); // pref로부터 name값 가져와 name에 할당
        pref.put("activity", "0");
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

    /***
     * 키 눌림 이벤트 메소드
     *
     * @param keyCode
     * @param event
     * @return
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            //하드웨어 뒤로가기 버튼에 따른 이벤트 설정
            case KeyEvent.KEYCODE_BACK:
                AlertDialog.Builder alert = new AlertDialog.Builder(this);  // Alert 창 출력
                alert.setTitle("프로그램 종료");
                alert.setMessage("프로그램을 종료 하시겠습니까?");
                alert.setPositiveButton("예", new DialogInterface.OnClickListener() { // 예 버튼 누를시 이벤트처리
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 프로세스 종료.
                        droppreference(); // 로그아웃 메소드 호출
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                });
                alert.setNegativeButton("아니오", null);   // 아니오 누를시 취소
                alert.show();
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