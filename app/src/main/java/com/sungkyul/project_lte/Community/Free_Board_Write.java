package com.sungkyul.project_lte.Community;

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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
// 자유게시판 글 작성 페이지
public class Free_Board_Write extends AppCompatActivity {
    Button btnFreeOK, btnFreeCancel; // 작성 완료, 작성 취소 버튼
    EditText edt_freetitle, edt_freecontent; // 글 제목 입력, 글 내용 입력
    String tag = "insFree", res, email, code = "a", name;
    HttpClient httpclient;
    HttpPost httppost;
    HttpResponse response;
    List<NameValuePair> nameValuePairs; // php로 post할 변수들을 담은 배열
    ProgressDialog dialog = null; // 프로그래스 바 초기화
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_free_board);
        btnFreeOK = (Button)findViewById(R.id.btn_FreeOK);
        btnFreeCancel = (Button)findViewById(R.id.btn_FreeCancel);
        edt_freecontent = (EditText)findViewById(R.id.edt_freecontent);
        edt_freetitle = (EditText)findViewById(R.id.edt_freetitle);

        RbPreference pref = new RbPreference(this); // RbPreference 생성
        email = pref.getValue("email", null); // email 변수에 로그인 시 저장된 email 값 할당
        name = pref.getValue("name", null); // name 변수에 로그인 시 저장된 name 값 할당

        btnFreeOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = ProgressDialog.show(Free_Board_Write.this, "", "잠시만 기다려주세요...", true);
                new Thread(new Runnable() {
                    public void run() {
                        writefree(); // 자유게시판 글 작성 완료 메소드 호출
                    }
                }).start();
            }
        });
        btnFreeCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog diaBox = new AlertDialog.Builder(Free_Board_Write.this)
                        .setTitle("작성 취소")
                        .setMessage("작성을 취소하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                Intent intent_free_cancel = new Intent(getApplicationContext(), Community_Main.class);
                                startActivity(intent_free_cancel);
                                finish();
                            }
                        })
                        .setNegativeButton("아니오", null)
                        .create();
                diaBox.show();
            }
        });
    }
    // 자유게시판 글 작성 완료 메소드
    void writefree() {
        try {
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost("http://211.221.245.121/LTE/service_user.php"); // http 통신 서버 주소
            // 데이터 추가
            nameValuePairs = new ArrayList<NameValuePair>(4);
            // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
            nameValuePairs.add(new BasicNameValuePair("tag", tag));
            nameValuePairs.add(new BasicNameValuePair("user_id", email));
            nameValuePairs.add(new BasicNameValuePair("name", name));
            nameValuePairs.add(new BasicNameValuePair("free_title", edt_freetitle.getText().toString()));  // $Edittext_value = $_POST['Edittext_value'];
            nameValuePairs.add(new BasicNameValuePair("content", edt_freecontent.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("code", code));
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
            String res = json.getString("success");


            if (res.equals("1")) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(Free_Board_Write.this, "글이 등록되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                Intent intent_free_ok = new Intent(getApplicationContext(),Community_Main.class);
                startActivity(intent_free_ok);
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
        Free_Board_Write.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(Free_Board_Write.this);
                builder.setTitle("Error.");
                builder.setMessage("작성이 불가능합니다.")
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
    // 뒤로가기 버튼 클릭 시 발생하는 이벤트 메소드
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            //하드웨어 뒤로가기 버튼에 따른 이벤트 설정
            case KeyEvent.KEYCODE_BACK:
                Intent intent_main = new Intent(getApplicationContext(), Community_Main.class);
                startActivity(intent_main);
                finish();
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
