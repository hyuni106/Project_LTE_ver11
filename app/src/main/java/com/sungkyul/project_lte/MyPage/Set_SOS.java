package com.sungkyul.project_lte.MyPage;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sungkyul.project_lte.Etc.RbPreference;
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

/**
 * Created by Hanna on 2016-04-21.
 */
// SOS 설정 페이지
public class Set_SOS extends AppCompatActivity{
    EditText editSOSNum, editSOSCon; // SOS 수신번호, SOS 내용
    Button btnUpdateNum, btnUpdateCon; // SOS 번호 저장, SOS 내용 저장
    String tag1 = "number", tag2 = "message", email, res, success, tag3="retUserInfo";
    HttpClient httpclient;
    HttpPost httppost;
    HttpResponse response;
    List<NameValuePair> nameValuePairs; // php로 post할 변수들을 담은 배열
    ProgressDialog dialog = null; // 프로그래스 바 사라짐

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_sos);

        editSOSNum = (EditText)findViewById(R.id.edit_SOSPhone);
        editSOSCon = (EditText)findViewById(R.id.edit_SOScon);
        btnUpdateNum = (Button)findViewById(R.id.btn_UpdatePhone);
        btnUpdateCon = (Button)findViewById(R.id.btn_UpdateCon);

        RbPreference pref = new RbPreference(this);
        email = pref.getValue("email", null);

        dialog = ProgressDialog.show(Set_SOS.this, "", "잠시만 기다려주세요...", true);
        new Thread(new Runnable() {
            public void run() {
                setsos(); // SOS 설정 값 불러오는 메소드 호출
            }
        }).start();

        btnUpdateNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = ProgressDialog.show(Set_SOS.this, "",
                        "저장중...", true);
                new Thread(new Runnable() {
                    public void run() {
                        setsosnum(); // SOS 수신번호 수정 완료 메소드 호출
                    }
                }).start();
            }
        });

        btnUpdateCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String context = editSOSCon.getText().toString();
                if(context.length()<25){
                    dialog = ProgressDialog.show(Set_SOS.this, "",
                            "저장중...", true);
                    new Thread(new Runnable() {
                        public void run() {
                            setsosmsg(); // SOS 내용 수정 완료 메소드 호출
                        }
                    }).start();
                }else {
                    Toast.makeText(getApplicationContext(),"글자 수가 너무 많아요.",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
    // SOS 설정 값 불러오는 메소드
    void setsos() {
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
                final String contack = jobj.getString("sos_contact"); // sos_contact에 대한 객체 받음
                final String message = jobj.getString("sos_message"); // sos_message에 대한 객체 받음
                System.out.println(contack);
                System.out.println(message);
                runOnUiThread(new Runnable() {
                    public void run() {
                        editSOSNum.setText(contack);
                        editSOSCon.setText(message);
                    }
                });
            } else if (res.equals("0")) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(Set_SOS.this, "설정 값이 없습니다.", Toast.LENGTH_SHORT).show();
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
    // SOS 수신번호 설정 완료 메소드
    void setsosnum() {
        try {
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost("http://hyuni106.dothome.co.kr/LTE/service_user.php"); // http 통신 서버 주소
            // 데이터 추가
            nameValuePairs = new ArrayList<NameValuePair>(2);
            // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
            nameValuePairs.add(new BasicNameValuePair("tag", tag1.trim()));
            nameValuePairs.add(new BasicNameValuePair("email", email.trim())); // $Edittext_value = $_POST['Edittext_value'];
            nameValuePairs.add(new BasicNameValuePair("sosnum", editSOSNum.getText().toString().trim()));
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
            success = json.getString("success");

            if (success.equals("1")) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(Set_SOS.this, "설정 완료", Toast.LENGTH_SHORT).show();
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
    // SOS 내용 수정 완료 메소드 호출
    void setsosmsg() {
        try {
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost("http://hyuni106.dothome.co.kr/LTE/service_user.php"); // http 통신 서버 주소
            // 데이터 추가
            nameValuePairs = new ArrayList<NameValuePair>(2);
            // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
            nameValuePairs.add(new BasicNameValuePair("tag", tag2));
            nameValuePairs.add(new BasicNameValuePair("email", email)); // $Edittext_value = $_POST['Edittext_value'];
            nameValuePairs.add(new BasicNameValuePair("sosmsg", editSOSCon.getText().toString().trim()));
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
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(Set_SOS.this, "설정 완료", Toast.LENGTH_SHORT).show();
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
    // 오류 다이얼로그 메소드
    public void showAlert() {
        Set_SOS.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(Set_SOS.this);
                builder.setTitle("Join Error.");
                builder.setMessage("설정이 불가능합니다.")
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
