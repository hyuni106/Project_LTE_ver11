package com.sungkyul.project_lte.MainNLogin;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sungkyul.project_lte.Etc.ByteLengthFilter;
import com.sungkyul.project_lte.R;

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
// 회원가입 페이지
public class Join extends Activity {
    private EditText editemail, editpw, editrepw, editnick; // 이메일 입력, 비밀번호 입력, 비밀번호 재입력, 닉네임 입력
    private Button btnjoin, btn_EmailCheck, btn_NickCheck; // 회원가입 완료 버튼, 이메일 중복확인 버튼, 닉네임 중복확인 버튼
    private TextView txt_Checkemail, txt_Checknick; // 이메일 중복확인 여부 메세지, 닉네임 중복확인 여부 메세지
    String tag1 = "register", tag2 = "checkNick", tag3 = "checkEmail", success, res;
    HttpClient httpclient;
    HttpPost httppost;
    HttpResponse response;
    List<NameValuePair> nameValuePairs; // php로 post할 변수들을 담은 배열
    ProgressDialog dialog = null; // 프로그래스 바 초기화

    int i = 0, j = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join);
        editemail = (EditText) findViewById(R.id.edit_Email);
        editpw = (EditText) findViewById(R.id.edit_PW);
        editrepw = (EditText) findViewById(R.id.edit_RePW);
        editnick = (EditText) findViewById(R.id.edit_Nick);
        btnjoin = (Button) findViewById(R.id.btn_Join);
        txt_Checkemail = (TextView) findViewById(R.id.txt_Checkemail);
        txt_Checknick = (TextView) findViewById(R.id.txt_Checknick);
        btn_EmailCheck = (Button) findViewById(R.id.btn_EmailCheck);
        btn_NickCheck = (Button) findViewById(R.id.btn_NickCheck);

        btn_NickCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editnick.getText().toString().isEmpty()) {
                    new Thread(new Runnable() {
                        public void run() {
                            checkNick(); // 닉네임 중복확인 메소드 호출
                        }
                    }).start();
                } else {
                    Toast.makeText(getApplicationContext(), "닉네임을 입력하세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_EmailCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editemail.getText().toString().isEmpty()) {
                    if (editemail.getText().toString().contains("@")) {
                        new Thread(new Runnable() {
                            public void run() {
                                checkEmail(); // 이메일 중복확인 메소드 호출
                            }
                        }).start();
                    }else{
                        Toast.makeText(getApplicationContext(), "이메일을 입력하세요", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "이메일을 입력하세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnjoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pw = editpw.getText().toString();
                String re_pw = editrepw.getText().toString();
                if (pw.equals(re_pw)) {
                    if (pw.length() > 5) {
                        dialog = ProgressDialog.show(Join.this, "", "Validating user...", true);
                        new Thread(new Runnable() {
                            public void run() {
                                register(); // 회원가입 완료하는 메소드 호출
                            }
                        }).start();
                    } else {
                        Toast.makeText(getApplicationContext(), "비밀번호는 6글자 이상부터 입력가능합니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "비밀번호와 재확인이 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        InputFilter[] filters = new InputFilter[] {new ByteLengthFilter(8, "KSC5601")};
        editnick.setFilters(filters);
    }
    // 회원가입 완료하는 메소드
    void register() {
        if (i == 1 && j == 1) {
            try {
                httpclient = new DefaultHttpClient();
                httppost = new HttpPost("http://hyuni106.dothome.co.kr/LTE/service_user.php"); // http 통신 서버 주소
                // 데이터 추가
                nameValuePairs = new ArrayList<NameValuePair>(3);
                // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
                nameValuePairs.add(new BasicNameValuePair("tag", tag1));
                nameValuePairs.add(new BasicNameValuePair("name", editnick.getText().toString().trim()));  // $Edittext_value = $_POST['Edittext_value'];
                nameValuePairs.add(new BasicNameValuePair("email", editemail.getText().toString().trim()));
                nameValuePairs.add(new BasicNameValuePair("password", editpw.getText().toString().trim()));
                httppost.setHeader("Content-type", "application/x-www-form-urlencoded; charset=utf-8");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));
                // http 통신 요청 실행
                //response = httpclient.execute(httppost);
                // edited by James from coderzheaven.. from here....
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                res = httpclient.execute(httppost, responseHandler); // http 통신 응답
                System.out.println("Response : " + res);

                JSONObject json = new JSONObject(res); // json 객체 생성
                success = json.getString("success");

                runOnUiThread(new Runnable() {
                    public void run() {
                        dialog.dismiss(); // 프로그래스 바 사라짐
                    }
                });

                if (success.equals("1")) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(Join.this, "가입 완료", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Intent send = new Intent(Join.this, Login.class);
                    startActivity(send);
                    finish();
                } else {
                    showAlert(); // 오류 다이얼로그 메소드 호출
                }
            } catch (Exception e) {
                dialog.dismiss(); // 프로그래스 바 사라짐
                System.out.println("Exception : " + e.getMessage());
            }
        } else {
            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(Join.this, "중복 검사를 완료해주세요.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    // 닉네임 중복확인 메소드
    void checkNick() {
        try {
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost("http://hyuni106.dothome.co.kr/LTE/service_user.php"); // http 통신 서버 주소
            // 데이터 추가
            nameValuePairs = new ArrayList<NameValuePair>(1);
            // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
            nameValuePairs.add(new BasicNameValuePair("tag", tag2));
            nameValuePairs.add(new BasicNameValuePair("name", editnick.getText().toString().trim()));  // $Edittext_value = $_POST['Edittext_value'];
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
                i = 0;
                String a = json.getString("user"); // json 배열 생성
                JSONArray json1 = new JSONArray(a);
                JSONObject jobj = json1.getJSONObject(0);
                final String nick = jobj.getString("name"); // name에 대한 객체 받음
                System.out.println(nick);
                runOnUiThread(new Runnable() {
                    public void run() {
                        txt_Checknick.setText(nick + " 이 이미 존재합니다.");
                    }
                });
            } else if (res.equals("0")) {
                i = 1;
                runOnUiThread(new Runnable() {
                    public void run() {
                        txt_Checknick.setText(editnick.getText().toString() + " 은(는) 사용 가능한 닉네임입니다.");
                    }
                });
            } else {
                showAlert(); // 오류 다이얼로그 메소드 호출
            }
        } catch (Exception e) {
            System.out.println("Exception : " + e.getMessage());
        }
    }
    // 이메일 중복확인 메소드
    void checkEmail() {
        try {
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost("http://hyuni106.dothome.co.kr/LTE/service_user.php"); // http 통신 서버 주소
            // 데이터 추가
            nameValuePairs = new ArrayList<NameValuePair>(1);
            // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
            nameValuePairs.add(new BasicNameValuePair("tag", tag3));
            nameValuePairs.add(new BasicNameValuePair("name", editemail.getText().toString().trim()));  // $Edittext_value = $_POST['Edittext_value'];
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
                j = 0;
                String a = json.getString("user"); // json 배열 생성
                JSONArray json1 = new JSONArray(a);
                JSONObject jobj = json1.getJSONObject(0);
                final String nick = jobj.getString("email"); // email에 대한 객체 받음
                System.out.println(nick);
                runOnUiThread(new Runnable() {
                    public void run() {
                        txt_Checkemail.setText(nick + " 이 이미 존재합니다.");
                    }
                });
            } else if (res.equals("0")) {
                j = 1;
                runOnUiThread(new Runnable() {
                    public void run() {
                        txt_Checkemail.setText(editemail.getText().toString() + " 은(는) 사용 가능한 이메일입니다.");
                    }
                });
            } else {
                showAlert(); // 오류 다이얼로그 메소드 호출
            }
        } catch (Exception e) {
            System.out.println("Exception : " + e.getMessage());
        }
    }
// 오류 다이얼로그 메소드
    public void showAlert() {
        Join.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(Join.this);
                builder.setTitle("Join Error.");
                builder.setMessage("가입이 불가능합니다.")
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
    //   뒤로가기 버튼 클릭 시 발생하는 메소드
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            //하드웨어 뒤로가기 버튼에 따른 이벤트 설정
            case KeyEvent.KEYCODE_BACK:
                Intent intent_main = new Intent(getApplicationContext(), Login.class);
                startActivity(intent_main);
                finish();
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}

