package com.sungkyul.project_lte.MyPage;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sungkyul.project_lte.Etc.ByteLengthFilter;
import com.sungkyul.project_lte.R;
import com.sungkyul.project_lte.Etc.RbPreference;

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

/**
 * Created by Hanna on 2016-05-12.
 */
// 닉네임 변경 페이지
public class Change_NickName extends AppCompatActivity {
    EditText edit_NewNick; // 새로운 닉네임 입력
    Button btn_UpdateNick, btn_Checknick; // 닉네임 수정버튼, 닉네임 중복확인 버튼
    TextView txt_Checknick; // 닉네임 중복여부 메세지
    String new_nick;

    HttpPost httppost;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs; // php로 post할 변수들을 담은 배열
    ProgressDialog dialog = null; // 프로그래스 바 초기화
    String id, tag1 = "checkNick", tag2="changeNick", tag3="retUserInfo", email;
    String res;
    int i = 0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_nickname);
        btn_UpdateNick = (Button) findViewById(R.id.btn_UpdateNick);
        edit_NewNick = (EditText) findViewById(R.id.edit_NewNick);
        txt_Checknick = (TextView) findViewById(R.id.txt_Checknick);
        btn_Checknick = (Button) findViewById(R.id.btn_Checknick);

        RbPreference pref = new RbPreference(this); // RbPreference 생성
        email = pref.getValue("email", null); // null 값을 email에 할당

        dialog = ProgressDialog.show(Change_NickName.this, "", "잠시만 기다려주세요...", true);
        new Thread(new Runnable() {
            public void run() {
                setnick(); // 설정해놨던 닉네임 값 불러오는 메소드 호출
            }
        }).start();

        edit_NewNick.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == event.KEYCODE_ENTER) {
                    return true;
                }
                return false;
            }
        });

        btn_Checknick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new_nick = edit_NewNick.getText().toString();
                new Thread(new Runnable() {
                    public void run() {
                        checkNick(); // 닉네임 중복확인 메소드 호출
                    }
                }).start();
            }
        });

        btn_UpdateNick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new_nick = edit_NewNick.getText().toString();
                if (new_nick.equals("")) {
                    Toast.makeText(getApplicationContext(), "새로운 닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(new Runnable() {
                        public void run() {
                            changeNick(); // 닉네임 변경완료하는 메소드 호출
                        }
                    }).start();
                }
            }
        });

        InputFilter[] filters = new InputFilter[] {new ByteLengthFilter(8, "KSC5601")};
        edit_NewNick.setFilters(filters);
    }
    // 설정해놨던 닉네임 값 불러오기
    void setnick() {
        try {
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost("http://hyuni106.dothome.co.kr/LTE/service_user.php"); // http 통신 서버 주소
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
                final String name = jobj.getString("name"); // name에 대한 객체 받음
                runOnUiThread(new Runnable() {
                    public void run() {
                        edit_NewNick.setText(name);
                    }
                });
            } else if (res.equals("0")) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(Change_NickName.this, "설정 값이 없습니다.", Toast.LENGTH_SHORT).show();
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
    // 닉네임 중복확인 메소드
    void checkNick() {
        try {
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost("http://hyuni106.dothome.co.kr/LTE/service_user.php"); // http 통신 서버 주소
            // 데이터 추가
            nameValuePairs = new ArrayList<NameValuePair>(1);
            // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
            nameValuePairs.add(new BasicNameValuePair("tag", tag1));
            nameValuePairs.add(new BasicNameValuePair("name", new_nick.trim()));  // $Edittext_value = $_POST['Edittext_value'];
            httppost.setHeader("Content-type", "application/x-www-form-urlencoded; charset=utf-8");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));
            // http 통신 요청 실행
            response = httpclient.execute(httppost);
            // edited by James from coderzheaven.. from here....
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            res = httpclient.execute(httppost, responseHandler); // http 통신 응답
            System.out.println("Response : " + res);

            /*runOnUiThread(new Runnable() {
                public void run() {
                    dialog.dismiss();
                }
            });*/

            JSONObject json = new JSONObject(res); // json 객체 생성
            String res = json.getString("success");


            if (res.equals("1")) {
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
                        txt_Checknick.setText(new_nick + " 은(는) 사용 가능한 닉네임입니다.");
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
    // 닉네임 변경완료하는 메소드
    void changeNick() {
        try {
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost("http://hyuni106.dothome.co.kr/LTE/service_user.php"); // http 통신 서버 주소
            // 데이터 추가
            nameValuePairs = new ArrayList<NameValuePair>(2);
            // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
            nameValuePairs.add(new BasicNameValuePair("tag", tag2));
            nameValuePairs.add(new BasicNameValuePair("email", email));  // $Edittext_value = $_POST['Edittext_value'];
            nameValuePairs.add(new BasicNameValuePair("name", new_nick.trim()));
            httppost.setHeader("Content-type", "application/x-www-form-urlencoded; charset=utf-8");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));
            // http 통신 요청 실행
            //response = httpclient.execute(httppost);
            // edited by James from coderzheaven.. from here....
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            res = httpclient.execute(httppost, responseHandler); // http 통신 응답
            System.out.println("Response : " + res);

            JSONObject json = new JSONObject(res); // json 객체 생성
            String res = json.getString("success");

            if (res.equals("1")) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(Change_NickName.this, "변경 완료", Toast.LENGTH_SHORT).show();
                    }
                });
                Intent send = new Intent(Change_NickName.this, My_Page.class);
                startActivity(send);
                finish();
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
        Change_NickName.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(Change_NickName.this);
                builder.setTitle("Error.");
                builder.setMessage("Error.")
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