package com.sungkyul.project_lte.Calculator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sungkyul.project_lte.MyPage.CartActivity;
import com.sungkyul.project_lte.Community.Community_Main;
import com.sungkyul.project_lte.flight.FlightActivity;
import com.sungkyul.project_lte.game.GameStart;
import com.sungkyul.project_lte.MyPage.Help;
import com.sungkyul.project_lte.MyPage.HelpAppVersion;
import com.sungkyul.project_lte.MainNLogin.Login;
import com.sungkyul.project_lte.MainNLogin.Main;
import com.sungkyul.project_lte.MyPage.My_Page;
import com.sungkyul.project_lte.R;
import com.sungkyul.project_lte.Etc.RbPreference;
import com.sungkyul.project_lte.SOS.SOSMain;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by WonHo on 2016-05-10.
 */
// 원화계산기
public class CalculatorActivity extends Activity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener, OnKeyListener, DialogInterface.OnKeyListener {

    private InputMethodManager imm;
    Button btnExchange, btn_CalChange; // 통화 설정 버튼, 현지화 to 원화 계산 버튼
    LinearLayout llExchange; // 리니어 레이아웃
    EditText editCurrencyMoney, editUserExchange; // 현지화 입력, 사용자 환율 입력
    TextView txtOurCurrencyMoney; // 원화 금액 표시
    Double currencyMoney, userExchange, sum;
    ImageView exFlagImage; // 통화 설정 국가의 국기 표시
    //환율 php
    String tag1 = "setusercur", tag2 = "cal", iso, krw = "KRW", email, tag3="retUserInfo";
    HttpPost httppost;
    HttpResponse response;
    HttpClient httpclient;
    ArrayList<NameValuePair> nameValuePairs; // php로 post할 변수들을 담은 배열
    ProgressDialog dialog = null; // 프로그래스 바 초기화
    String res, login;

    TextView nav_nick, nav_name; //  로그인 여부, 이메일 보여줄 text 선언
    Button btn_navLogin; // 내비게이션 로그인/로그아웃 버튼
    private Toolbar supportActionBar; // ToolBar 선언
    private String name;
    MenuItem navLogin;   // 로그인, 이메일 선언
    DrawerLayout drawer;
    RbPreference pref = new RbPreference(this); // 값 가져오기 위한 RbPreference class 생성

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

        init(); // 변수 초기화 메소드 호출
        login(); // login 값 가져오는 메소드 호출
        log();  // login 여부 확인 메소드 호출

        if(login != null) {
            Toast.makeText(CalculatorActivity.this, "로그인 유저입니다.", Toast.LENGTH_SHORT).show();
            dialog = ProgressDialog.show(CalculatorActivity.this, "", "잠시만 기다려주세요...", true);
            new Thread(new Runnable() {
                public void run() {
                    setcur(); // 설정해놓은 사용자 환율 값 가져오는 메소드 호출
                }
            }).start();
        } else {
            Toast.makeText(CalculatorActivity.this, "비로그인 유저입니다.", Toast.LENGTH_SHORT).show();
        }

        btnExchange.setOnClickListener(this); // exchnage 이벤트
        llExchange.setOnClickListener(this); // exchnage 이벤트
        btn_CalChange.setOnClickListener(this);
        editCurrencyMoney.setOnKeyListener(this);


    }

    // 로그인 값 가져오는 메소드
    public void login() {
        login = pref.getValue("login", null);      // pref로부터 login값 가져와 login에 할당
        email = pref.getValue("email", null);      // pref로부터 email값 가져와 email에 할당
        name = pref.getValue("name", null); // pref로부터 name값 가져와 name에 할당
        pref.put("activity", "1");
    }

    // 변수 초기화 메소드
    public void init() {
        editCurrencyMoney = (EditText) findViewById(R.id.editCurrencyMoney);
        editUserExchange = (EditText) findViewById(R.id.editUserExchange);
        txtOurCurrencyMoney = (TextView) findViewById(R.id.txtOurCurrencyMoney);
        btnExchange = (Button) findViewById(R.id.btnExchange);
        btn_CalChange = (Button) findViewById(R.id.btnCalChange);
        llExchange = (LinearLayout) findViewById(R.id.llExchange);
        exFlagImage = (ImageView) findViewById(R.id.exflagImage);
        login = pref.getValue("login", null); // pref로부터 login값 가져와 login에 할당
        email = pref.getValue("email", null); // pref로부터 email값 가져와 email에 할당

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
                    android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(CalculatorActivity.this);
                    alert.setTitle("로그아웃");
                    alert.setMessage("로그아웃 하시겠습니까?");
                    alert.setNegativeButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 로그아웃 정보 필요
                            // Login으로 이동
                            droppreference();   // 로그아웃 메소드 호출
                            Toast.makeText(CalculatorActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
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

    // 로그인 여부 확인 메소드
    public void log() {
        if (login != null) {
            Toast.makeText(CalculatorActivity.this, "로그인 유저입니다.", Toast.LENGTH_SHORT).show();
            navLogin.setTitle("로그아웃");
            nav_name.setText(email);
            nav_nick.setText(name);
        } else {    // 비로그인시
            navLogin.setTitle("로그인");
            nav_name.setText("로그인이 필요합니다.");
            nav_nick.setText("닉네임");
        }
    }

    /***
     * 네비게이션에서 뒤로가기 클릭 메소드
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_cal); // 네비게이션 선언 및 xml 연동
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
    @Override
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
                android.support.v7.app.AlertDialog.Builder alert_confirm = new android.support.v7.app.AlertDialog.Builder(CalculatorActivity.this);
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
                android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(this);
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
            /*Intent intent = new Intent(getApplicationContext(), CalculatorActivity.class);
            startActivity(intent);*/
            Toast.makeText(CalculatorActivity.this, "현재 위치에 있습니다.", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_sos) {
            // SOS
            if (login != null) {
                Intent intent = new Intent(getApplicationContext(), SOSMain.class);
                startActivity(intent);
                finish();
            } else {
                // 로그인인 필요한 경우
                android.support.v7.app.AlertDialog.Builder alert_confirm = new android.support.v7.app.AlertDialog.Builder(CalculatorActivity.this);
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
        } else if (id == R.id.nav_help) { // 도움말
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_cal);
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
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llExchange:
            case R.id.btnExchange: { // 통화 설정 버튼
                Intent intent = new Intent(this, NationListActivity.class);
                startActivityForResult(intent, 1111);
                editUserExchange.setText("");
                break;
            }
            case R.id.btnCalChange: { // 계산 버튼
                if ("통화 설정".equals(btnExchange.getText().toString())) {
                    Toast.makeText(CalculatorActivity.this, "통화를 설정해주세요", Toast.LENGTH_SHORT).show();
                } else if (editCurrencyMoney.getText().toString().isEmpty()) {
                    Toast.makeText(CalculatorActivity.this, "값을 입력해주세요", Toast.LENGTH_SHORT).show();
                } else if (!editCurrencyMoney.getText().toString().isEmpty() && !editUserExchange.getText().toString().isEmpty()) {
                    if(login != null) {
                        dialog = ProgressDialog.show(CalculatorActivity.this, "", "잠시만 기다려주세요...", true);
                        new Thread(new Runnable() {
                            public void run() {
                                setusrcur(); // 계산 결과 출력, 사용자 환율 저장하는 메소드 호출
                            }
                        }).start();
                    } else {
                        currencyMoney = Double.parseDouble(editCurrencyMoney.getText().toString());
                        userExchange = Double.parseDouble(editUserExchange.getText().toString());
                        sum = Double.valueOf(String.format("%.2f", currencyMoney * userExchange));
                        txtOurCurrencyMoney.setText(sum + " 원");
                    }
                } else {
                    dialog = ProgressDialog.show(CalculatorActivity.this, "", "잠시만 기다려주세요...", true);
                    // 서버에 받아올 각나라의 환율 부분
                    new Thread(new Runnable() {
                        public void run() {
                            cal(); // 환율 데이터 받아오는 메소드 호출
                        }
                    }).start();
                }
                break;
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1111) {
            btnExchange.setText(data.getStringExtra("exchange") + "(" + data.getStringExtra("exchangeUnit") + ")"); // 값 받기
            exFlagImage.setImageResource(data.getExtras().getInt("flagImage"));
            iso = data.getStringExtra("exchange");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    // 설정해놓은 사용자 환율 값 가져오는 메소드
    void setcur() {
        try {
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost("http://211.221.245.121/LTE/service_user.php"); // http 통신 서버 주소
            // 데이터 추가
            nameValuePairs = new ArrayList<NameValuePair>(1);
            // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
            nameValuePairs.add(new BasicNameValuePair("tag", tag3));
            nameValuePairs.add(new BasicNameValuePair("email", email));  // $Edittext_value = $_POST['Edittext_value'];
            httppost.setHeader("Content-type", "application/x-www-form-urlencoded; charset=utf-8");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));
            // http 통신 요청 실행
            response = httpclient.execute(httppost);
            // edited by James from coderzheaven.. from here....
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            res = httpclient.execute(httppost, responseHandler); // http 통신 응답
            System.out.println("Response : " + res);

            runOnUiThread(new Runnable() {
                public void run() {
                    dialog.dismiss(); // 프로그래스 바 사라짐
                }
            });

            JSONObject json = new JSONObject(res); // json 객체 생성
            String res = json.getString("success");


            if (res.equals("1")) {
                String a = json.getString("user"); // json 배열 생성
                JSONArray json1 = new JSONArray(a);
                JSONObject jobj = json1.getJSONObject(0);
                final String cur = jobj.getString("user_cur_rt"); // user_cur_rt에 대한 객체 받음
                if (!cur.equals("")) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(CalculatorActivity.this, "이전 설정 값을 불러옵니다.", Toast.LENGTH_SHORT).show();
                            editUserExchange.setText(cur);
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(CalculatorActivity.this, "설정 값이 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                showAlert(); // 오류 다이얼로그 메소드 호출
            }
        } catch (Exception e) {
            dialog.dismiss(); // 프로그래스 바 사라짐
            System.out.println("Exception : " + e.getMessage());
        }
    }
    // 계산 결과 출력, 사용자 환율 저장하는 메소드
    void setusrcur() {
        try {
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost("http://211.221.245.121/LTE/service_user.php"); // http 통신 서버 주소
            // 데이터 추가
            nameValuePairs = new ArrayList<NameValuePair>(3);
            // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
            nameValuePairs.add(new BasicNameValuePair("tag", tag1));
            nameValuePairs.add(new BasicNameValuePair("email", email));
            nameValuePairs.add(new BasicNameValuePair("iso", iso));
            nameValuePairs.add(new BasicNameValuePair("user_cur", editUserExchange.getText().toString().trim()));
            httppost.setHeader("Content-type", "application/x-www-form-urlencoded; charset=utf-8");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));
            // http 통신 요청 실행
            //response = httpclient.execute(httppost);
            // edited by James from coderzheaven.. from here....
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            res = httpclient.execute(httppost, responseHandler); // http 통신 응답
            System.out.println("Response : " + res);
            runOnUiThread(new Runnable() {
                public void run() {
                    dialog.dismiss(); // 프로그래스 바 사라짐
                }
            });

            JSONObject json = new JSONObject(res); // json 객체 생성
            String success = json.getString("success");

            if (success.equals("1")) {
                currencyMoney = Double.parseDouble(editCurrencyMoney.getText().toString());
                userExchange = Double.parseDouble(editUserExchange.getText().toString());
                sum = Double.valueOf(String.format("%.2f", currencyMoney * userExchange));
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(CalculatorActivity.this, "사용자 환율이 저장됩니다.", Toast.LENGTH_SHORT).show();
                        txtOurCurrencyMoney.setText(sum + " 원");
                    }
                });
            } else {
                showAlert(); // 오류 다이얼로그 메소드 호출
            }
        } catch (Exception e) {
            dialog.dismiss(); // 프로그래스 바 사라짐
            System.out.println("Exception : " + e.getMessage());
        }
    }
    // 환율 데이터 받아오는 메소드
    public void cal() {
        try {
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost("http://211.221.245.121/LTE/service_user.php"); // http 통신 서버 주소
            // 데이터 추가
            nameValuePairs = new ArrayList<NameValuePair>(2);
            // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
            nameValuePairs.add(new BasicNameValuePair("tag", tag2));
            nameValuePairs.add(new BasicNameValuePair("iso", iso));  // $Edittext_value = $_POST['Edittext_value'];
            nameValuePairs.add(new BasicNameValuePair("krw", krw));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));
            // http 통신 요청 실행
            response = httpclient.execute(httppost);
            // edited by James from coderzheaven.. from here....
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            res = httpclient.execute(httppost, responseHandler); // http 통신 응답
            System.out.println("Response : " + res);

            JSONObject json = new JSONObject(res); // json 객체 생성
            String a = json.getString("query"); // json 배열 생성
            JSONObject json2 = new JSONObject(a); // json 객체 생성
            String count = json2.getString("count"); // json 배열 생성
            String b = json2.getString("results"); // json 배열 생성
            JSONObject json3 = new JSONObject(b); // json 객체 생성
            String c = json3.getString("rate"); // json 배열 생성
            JSONObject json4 = new JSONObject(c); // json 객체 생성
            String d = json4.getString("Rate"); // json 배열 생성
            Double rate = Double.parseDouble(d);

            currencyMoney = Double.parseDouble(editCurrencyMoney.getText().toString());
            Double aa = currencyMoney * rate;
            final Double sum = Double.parseDouble(String.format("%.2f", aa));
            Log.e("run", "run");
            if (count.equals("1")) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        dialog.dismiss(); // 프로그래스 바 사라짐
                        txtOurCurrencyMoney.setText(sum + " 원");
                    }
                });

            } else {
                dialog.dismiss(); // 프로그래스 바 사라짐
                showAlert(); // 오류 다이얼로그 메소드 호출
            }
        } catch (Exception e) {
            System.out.println("Exception : " + e.getMessage());
        }
    }
    // 오류 다이얼로그 메소드
    public void showAlert() {
        CalculatorActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(CalculatorActivity.this);
                builder.setTitle("Login Error.");
                builder.setMessage("User not Found.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && KeyEvent.ACTION_DOWN == event.getAction()) {

            if ("통화 설정".equals(btnExchange.getText().toString())) {
                Toast.makeText(CalculatorActivity.this, "통화를 설정해주세요", Toast.LENGTH_SHORT).show();

                // 현지화 값이 입력되어있지 않은 경우
            } else if (editCurrencyMoney.getText().toString().isEmpty()) {
                Toast.makeText(CalculatorActivity.this, "값을 입력해주세요", Toast.LENGTH_SHORT).show();

                // 사용자 환율이 입력되어있는 경우
            } else if (!editCurrencyMoney.getText().toString().isEmpty() && !editUserExchange.getText().toString().isEmpty()) {
                if (login != null) {
                    dialog = ProgressDialog.show(CalculatorActivity.this, "", "잠시만 기다려주세요...", true);
                    new Thread(new Runnable() {
                        public void run() {
                            setusrcur(); // 계산 결과 출력, 사용자 환율 저장하는 메소드
                        }
                    }).start();
                } else {
                    // 둘다 입력되어 있는 경우
                    currencyMoney = Double.parseDouble(editCurrencyMoney.getText().toString());
                    userExchange = Double.parseDouble(editUserExchange.getText().toString());
                    sum = Double.valueOf(String.format("%.2f", currencyMoney * userExchange));
                    txtOurCurrencyMoney.setText(sum + " 원");
                }
            } else {
                dialog = ProgressDialog.show(CalculatorActivity.this, "", "잠시만 기다려주세요...", true);
                // 서버에 받아올 각나라의 환율 부분 및 계산
                new Thread(new Runnable() {
                    public void run() {
                        cal();  // 환율 데이터 받아오는 메소드를 호출
                    }
                }).start();

                imm.hideSoftInputFromWindow(editCurrencyMoney.getWindowToken(), 0);
            }
        } /*else if (keyCode == KeyEvent.KEYCODE_BACK) {
            dialog.dismiss();
            return true;
        }*/
        return false;
    }
    /***
     * alert 창 시 back버튼
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

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            //하드웨어 뒤로가기 버튼에 따른 이벤트 설정
            case KeyEvent.KEYCODE_BACK:
                Intent intent_main = new Intent(CalculatorActivity.this, Main.class);
                startActivity(intent_main);
                finish();
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
