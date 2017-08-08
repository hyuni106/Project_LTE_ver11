package com.sungkyul.project_lte.MainNLogin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.sungkyul.project_lte.Calculator.CalculatorActivity;
import com.sungkyul.project_lte.Community.Community_Main;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
// 로그인 페이지
public class Login extends AppCompatActivity {

    Button btnLogin, join; // 로그인 버튼, 회원가입 버튼
    EditText email, pass; // 이메일 입력, 비밀번호 입력
    CheckBox chbox_auto; // 자동로그인 여부 확인 체크박스
    HttpPost httppost;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs; // php로 post할 변수들을 담은 배열
    ProgressDialog dialog = null; // 프로그래스 바 초기화
    String id, tag = "login", auto, auto_id, auto_pw;
    String res;
    RbPreference pref = new RbPreference(this); // RbPreference 생성

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        btnLogin = (Button) findViewById(R.id.Button01);
        email = (EditText) findViewById(R.id.username);
        pass = (EditText) findViewById(R.id.password);
        join = (Button)findViewById(R.id.Button02);
        chbox_auto = (CheckBox)findViewById(R.id.chbox_auto);

        auto = pref.getValue("auto", null); // null값을 auto에 할당
        auto_id = pref.getValue("auto_id", null); // null값을 auto_id에 할당
        auto_pw = pref.getValue("auto_pw", null); // null값을 auto_pw에 할당

        // 자동로그인 여부 확인
        if(auto != null) {
            chbox_auto.setChecked(true);
            email.setText(auto_id);
            pass.setText(auto_pw);
        } else {
            chbox_auto.setChecked(false);
            email.setText(null);
        }

        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "이메일을 입력하세요", Toast.LENGTH_SHORT).show();
                } else if (pass.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
                } else {
                    dialog = ProgressDialog.show(Login.this, "", "잠시만 기다려주세요", true);
                    new Thread(new Runnable() {
                        public void run() {
                            login(); // 로그인 메소드 호출
                        }
                    }).start();
                    Editable edtable = email.getText();
                    id = edtable.toString();
                }
            }
        });

        join.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent send = new Intent(Login.this,Join.class);
                startActivity(send);
                finish();
            }
        });
    }
    // 로그인 메소드
    void login() {
        try {
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost("http://hyuni106.dothome.co.kr/LTE/service_user.php"); // http 통신 서버 주소
            // 데이터 추가
            nameValuePairs = new ArrayList<NameValuePair>(2);
            // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
            nameValuePairs.add(new BasicNameValuePair("tag", tag));
            nameValuePairs.add(new BasicNameValuePair("email", email.getText().toString().trim()));  // $Edittext_value = $_POST['Edittext_value'];
            nameValuePairs.add(new BasicNameValuePair("password", pass.getText().toString().trim()));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));
            // http 통신 요청 실행
            response = httpclient.execute(httppost);
            // edited by James from coderzheaven.. from here....
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            res = httpclient.execute(httppost, responseHandler); // http 통신 응답
            System.out.println("Response : " + res);

            JSONObject json = new JSONObject(res); // json 객체 생성
            String res = json.getString("success");

            if (res.equals("1")) {
                String a = json.getString("user"); // json 배열 생성
                JSONObject json2 = new JSONObject(a);
                String name = json2.getString("name"); // name에 대한 객체 받음
                String email = json2.getString("email"); // email에 대한 객체 받음
                String delete = json2.getString("delete_yn"); // delete_vn에 대한 객체 받음
                pref.put("email", email); // 해당 email 값에 email 저장
                pref.put("name", name); // 해당 name 값에 name 저장
                pref.put("login", "1"); // 해당 login 값에 1 저장
                pref.put("auto", null); // 해당 auto 값에 null 저장
                if(chbox_auto.isChecked()) {
                    pref.put("auto", "1"); // 해당 auto 값에 1 저장
                    pref.put("auto_id", email); // 해당 auto_id 값에 email 저장
                    pref.put("auto_pw", pass.getText().toString().trim());
                }
                if(delete.equals("1")) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            dialog.dismiss(); // 프로그래스 바 사라짐
                            Toast.makeText(Login.this, "탈퇴한 회원입니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            dialog.dismiss(); // 프로그래스 바 사라짐
                            Toast.makeText(Login.this, "로그인 되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    String activity_no = pref.getValue("activity", null);
                    if(activity_no.equals("1")) {
                        Intent send = new Intent(Login.this, CalculatorActivity.class);
                        startActivity(send);
                        finish();
                    } else if(activity_no.equals("2")) {
                        Intent send = new Intent(Login.this, SOSMain.class);
                        startActivity(send);
                        finish();
                    } else if(activity_no.equals("3")) {
                        Intent send = new Intent(Login.this, Community_Main.class);
                        startActivity(send);
                        finish();
                    } else {
                        Intent send = new Intent(Login.this, Main.class);
                        startActivity(send);
                        finish();
                    }
                }
            } else {
                dialog.dismiss(); // 프로그래스 바 사라짐
                showAlert(); // 오류 다이얼로그 메소드 호출
            }
        } catch (Exception e) {
            dialog.dismiss(); // 프로그래스 바 사라짐
            System.out.println("Exception : " + e.getMessage());
        }
    }
    // 오류 다이얼로그 메소드
    public void showAlert() {
        Login.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
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
    // 뒤로가기 버튼 클릭 시 발생하는 메소드
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
