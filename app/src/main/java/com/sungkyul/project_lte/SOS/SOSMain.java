package com.sungkyul.project_lte.SOS;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sungkyul.project_lte.Calculator.CalculatorActivity;
import com.sungkyul.project_lte.Community.Community_Main;
import com.sungkyul.project_lte.Etc.RbPreference;
import com.sungkyul.project_lte.MainNLogin.Login;
import com.sungkyul.project_lte.MainNLogin.Main;
import com.sungkyul.project_lte.MyPage.CartActivity;
import com.sungkyul.project_lte.MyPage.Help;
import com.sungkyul.project_lte.MyPage.HelpAppVersion;
import com.sungkyul.project_lte.MyPage.My_Page;
import com.sungkyul.project_lte.R;
import com.sungkyul.project_lte.flight.FlightActivity;
import com.sungkyul.project_lte.game.GameStart;

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
import java.util.List;
// SOS 메인 페이지
public class SOSMain extends Activity implements NavigationView.OnNavigationItemSelectedListener {

    EditText editNum, editCon, editGPS; // 수신자 번호, SOS 메세지, GPS 값 표시
    Button btnInfo; // 대사관 정보 페이지로 이동
    Context mContext;
    static String PHONE_NUMBER; // 발신자의 번호
    final static String MESSAGE = "http://maps.google.com/?q="; // 발신자의 위치를 나타낼 수 있는 지도 URL을 제공하기위해 뒤에 위도,경도 값 붙여서 메세지 발송
    private SensorManager mSensorManager; // 센서 값을 허용하기 위한 변수
    String num1, con1;

    HttpClient httpclient;
    HttpPost httppost;
    HttpResponse response;
    List<NameValuePair> nameValuePairs; // php로 post할 변수들을 담은 배열
    ProgressDialog dialog = null; // 프로그래스 바 초기화
    String res, tag = "retUserInfo", email, name, login;

    TextView nav_nick, nav_name; //  로그인 여부, 이메일 보여줄 text 선언
    Button btn_navLogin; // 내비게이션 로그인/로그아웃 버튼
    private Toolbar supportActionBar; // ToolBar 선언
    MenuItem navLogin;   // 로그인, 이메일 선언
    DrawerLayout drawer;
    Toolbar toolbar;
    RbPreference pref = new RbPreference(this); // 값 가져오기 위한 RbPreference class 생성

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sosmain);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        PHONE_NUMBER = tMgr.getLine1Number(); // getLine1Number 메소드가 전화번호를 반환

        mContext = this;
        btnInfo = (Button) findViewById(R.id.btnInfo);
        editCon = (EditText) findViewById(R.id.editCon);
        editNum = (EditText) findViewById(R.id.editNum);
        editGPS = (EditText) findViewById(R.id.editGPS);

        setting();  // sos 설정 값 불러오는 메소드 호출
        navi(); // 내비게이션 연결 메소드 호출
        login(); // login 값 가져오는 메소드 호출
        log(); // login 여부 확인 메소드 호출

        num1 = editNum.getText().toString();
        con1 = editCon.getText().toString();
        startLocationService(); // 위치 요청 메소드 호출

        btnInfo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // 대사관 정보 헤이지로 이동
                Intent mIntent1 = new Intent(getApplicationContext(), EmbassyInfo.class);
                startActivity(mIntent1);
                finish();
            }
        });
    }
    // 내비게이션 연결 메소드
    void navi() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_sos);  // 네비게이션 드로우를 적용하기 위해 DrawerLayout 선언 및 xml과 연동

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
                    android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(SOSMain.this);
                    alert.setTitle("로그아웃");
                    alert.setMessage("로그아웃 하시겠습니까?");
                    alert.setNegativeButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 로그아웃 정보 필요
                            droppreference();   // 로그아웃 메소드 호출
                            Toast.makeText(SOSMain.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
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

    public void setting() {
        dialog = ProgressDialog.show(SOSMain.this, "", "잠시만 기다려주세요...", true);
        new Thread(new Runnable() {
            public void run() {
                setsos(); // sos 설정 값 가져오는 메소드 호출
            }
        }).start();

    }

    /***
     * 로그인 값 가져올 메소드
     */
    void login() {
        login = pref.getValue("login", null);      // pref로부터 login값 가져와 login에 할당
        email = pref.getValue("email", null);      // pref로부터 email값 가져와 login에 할당
        name = pref.getValue("name", null);
        pref.put("activity", "2");
    }
    // 로그인 여부 판단 메소드
    public void log() {
        if (login != null) {    // 로그인시
            btn_navLogin.setText("로그아웃");
            nav_name.setText(email);
            nav_nick.setText(name);

        } else {    // 비로그인시
            btn_navLogin.setText("로그인");
            nav_name.setText("로그인이 필요합니다.");
            nav_nick.setText("닉네임");
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_sos); // 네비게이션 선언 및 xml 연동
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
            Intent intent = new Intent(getApplicationContext(), My_Page.class);
            startActivity(intent);
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
            Toast.makeText(SOSMain.this, "해당 위치에 있습니다", Toast.LENGTH_SHORT).show();
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_sos);
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
    // 문자 발송 버튼 onClick 속성
    public void sendSMS(View v) {
        num1 = editNum.getText().toString();
        con1 = editCon.getText().toString();
        if (0 < num1.length()) {
            if (0 < con1.length() && con1.length() < 25) {
                sendSMS(num1, con1);
            } else if (con1.equals("")) {
                Toast.makeText(this, "내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "글자 수를 줄여주세요.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
        }
    }

    //문자 발송 메소드
    public void sendSMS(String smsNumber, String smsText) {
        PendingIntent sentIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_SENT_ACTION"), 0);
        PendingIntent deliveredIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_DELIVERED_ACTION"), 0);
        /**
         * SMS
         * When the SMS massage has been sent
         */
        // SMS 전송 성공, 실패 여부를 확인하기 위한 BroadcastReceiver
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(mContext, "문자 전송 완료", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(mContext, "전송 실패", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(mContext, "서비스 지역이 아닙니다.", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(mContext, "무선(Radio)가 꺼져있습니다.", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(mContext, "PDU Null", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter("SMS_SENT_ACTION"));
        /**
         * SMS
         * When the SMS massage has been delivered
         */
        // SMS 수신 이벤트
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(mContext, "SMS 도착 완료", Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(mContext, "SMS 도착 실패", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter("SMS_DELIVERED_ACTION"));
        String gps1 = editGPS.getText().toString();
        SmsManager mSmsManager = SmsManager.getDefault(); // SmsManager : 안드로이드에서 SMS 전송을 위해 제공되는 객체, getDefault() 호출하여 객체 생성
        mSmsManager.sendTextMessage(smsNumber, null, smsText + "\n" + MESSAGE + gps1, sentIntent, deliveredIntent);
    }
    // 위치 요청 메소드
    public EditText startLocationService() {
        // 위치 관리자 객체 참조
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // 리스너 객체 생성
        GPSListener gpsListener = new GPSListener();
        long minTime = 10000;// 위치 갱신하는데 필요한 최소 시간 간격
        float minDistance = 0; // 위치를 갱신하는데 필요한 최소 거리


        try {
            // GPS 기반 위치 요청
            manager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    minTime,
                    minDistance,
                    gpsListener);

            // 네트워크 기반 위치 요청
            manager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    minTime,
                    minDistance,
                    gpsListener);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class GPSListener implements LocationListener {
        /**
         * 위치 정보가 확인되었을 때 호출되는 메소드
         */
        public void onLocationChanged(Location location) {
            // Location 객체로부터 위도(latitude)와 경도(longitude)를 비롯한 위치 정보 구함
            Double latitude = location.getLatitude();
            Double longitude = location.getLongitude();

            String msg = latitude + "," + longitude;
            editGPS.setText(msg); // 위도, 경도 값을 EditText에 표시
        }

        public void onProviderDisabled(String provider) {
            // 위치 공급자가 사용 불가능해질 때 호출
        }

        public void onProviderEnabled(String provider) {
            // 위치 공급자가 사용 가능해질 때 호출
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            // 위치 공급자의 상태가 바뀔 때 호출
        }
    }
    // SOS 설정 값 가져오는 메소드
    void setsos() {
        try {
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost("http://hyuni106.dothome.co.kr/LTE/service_user.php"); // http 통신 서버 주소
            // 데이터 추가
            nameValuePairs = new ArrayList<NameValuePair>(1);
            // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
            nameValuePairs.add(new BasicNameValuePair("tag", tag));
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
                final String contack = jobj.getString("sos_contact");// sos_contact에 대한 객체 받음
                final String message = jobj.getString("sos_message"); // sos_message에 대한 객체 받음
                if (!contack.equals("") && !message.equals("")) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            editNum.setText(contack);
                            editCon.setText(message);
                        }
                    });
                } else if (!contack.equals("") && message.equals("")) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            editNum.setText(contack);
                            editCon.setText("도움을 요청드립니다.");
                        }
                    });
                } else {
                    Toast.makeText(mContext, "마이페이지에서 설정해주세요", Toast.LENGTH_SHORT).show();
                    editCon.setText("도움을 요청드립니다.");
                }
            } else {
                showAlert(); // 오류 다이얼로그 메소드 호출
            }
        } catch (Exception e) {
            dialog.dismiss(); // 프로그래스 바 사라짐
            System.out.println("Exception : " + e.getMessage());
        }
    }
    // 오류 다이얼로그 메소드
    public void showAlert() {
        SOSMain.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(SOSMain.this);
                builder.setTitle("미설정");
                builder.setMessage("마이페이지 > SOS 설정을 해주세요.")
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
}






