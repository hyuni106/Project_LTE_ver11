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
import android.widget.TextView;
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
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
// 자유게시판 글 읽기 페이지
public class Free_Board_Read extends AppCompatActivity {
    Button btn_edit_free, btn_delFree, btn_CommentFree; // 글 수정, 글 삭제, 댓글 버튼
    TextView txt_title, txt_user, txt_date, txt_hit, txt_content; // 제목, 작성자, 작성일, 조회수, 글 표시
    String tag1 = "readFree", free_no, res, email, tag3 = "listFreeCom", login, writer, tag4 = "deleteFree", no;
    HttpClient httpclient;
    HttpPost httppost;
    HttpResponse response;
    List<NameValuePair> nameValuePairs; // php로 post할 변수들을 담은 배열
    ProgressDialog dialog = null; // 프로그래스 바 초기화

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_free_board);
        btn_edit_free = (Button) findViewById(R.id.btn_EditFree);
        btn_CommentFree = (Button) findViewById(R.id.btn_CommentFree);
        txt_title = (TextView) findViewById(R.id.txt_title);
        txt_user = (TextView) findViewById(R.id.txt_user);
        txt_date = (TextView) findViewById(R.id.txt_date);
        txt_hit = (TextView) findViewById(R.id.txt_hit);
        txt_content = (TextView) findViewById(R.id.txt_content);
        btn_delFree = (Button) findViewById(R.id.btn_delFree);

        Intent i = getIntent();
        free_no = i.getStringExtra("no");

        RbPreference pref = new RbPreference(this); // RbPreference 생성
        email = pref.getValue("email", null); // null값을 해당 email에 할당
        login = pref.getValue("login", null); // null값을 해당 login에 할당

        dialog = ProgressDialog.show(Free_Board_Read.this, "", "잠시만 기다려주세요...", true);
        new Thread(new Runnable() {
            public void run() {
                setfree(); // 자유게시판 글 불러오는 메소드
            }
        }).start();

        btn_edit_free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_edit_free = new Intent(getApplicationContext(), Free_Board_Edit.class);
                intent_edit_free.putExtra("no", free_no);
                startActivity(intent_edit_free);
            }
        });

        btn_CommentFree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_free_comment = new Intent(getApplicationContext(), Free_Comment.class);
                intent_free_comment.putExtra("no", free_no);
                startActivity(intent_free_comment);
            }
        });

        btn_delFree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = ProgressDialog.show(Free_Board_Read.this, "", "삭제중입니다...", true);
                new Thread(new Runnable() {
                    public void run() {
                        delFree(); // 글 삭제하는 메소드 호출
                    }
                }).start();
            }
        });
    }

    // 작성자 비교하는 메소드
    void writer_check() {
        if (email != null) {
            if (email.equals(writer)) {
                btn_edit_free.setVisibility(View.VISIBLE);
                btn_delFree.setVisibility(View.VISIBLE);
            }
        }
    }
    // 자유게시판 글 불러오는 메소드
    void setfree() {
        try {
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost("http://211.221.245.121/LTE/service_user.php"); // http 통신 서버 주소
            // 데이터 추가
            nameValuePairs = new ArrayList<NameValuePair>(1);
            // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
            nameValuePairs.add(new BasicNameValuePair("tag", tag1));
            nameValuePairs.add(new BasicNameValuePair("free_no", free_no));  // $Edittext_value = $_POST['Edittext_value'];
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

            runOnUiThread(new Runnable() {
                public void run() {
                    dialog.dismiss(); // 프로그래스 바 사라짐
                }
            });

            if (res.equals("1")) {
                String a = json.getString("free"); // json 배열 생성
                JSONArray json1 = new JSONArray(a);
                JSONObject jobj = json1.getJSONObject(0);
                final String title = jobj.getString("FREE_TITLE"); // FREE_TITLE에 대한 객체 받음
                final String user = jobj.getString("name"); // name에 대한 객체 받음
                writer = jobj.getString("USER_ID"); // USER_ID에 대한 객체 받음
                final String date = jobj.getString("WRITE_DATE"); // WRITE_DATE에 대한 객체 받음
                final String content = jobj.getString("CONTENT"); // CONTENT에 대한 객체 받음
                final String hit = jobj.getString("HIT_COUNT"); // HIT_COUNT에 대한 객체 받음
                runOnUiThread(new Runnable() {
                    public void run() {
                        writer_check(); // 작성자 비교하는 메소드 호출
                        txt_title.setText(title);
                        txt_user.setText(user);
                        txt_date.setText(date);
                        txt_hit.setText(hit);
                        txt_content.setText(content);
                    }
                });
            } else if (res.equals("0")) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(Free_Board_Read.this, "글을 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
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
    // 글 삭제하는 메소드
    void delFree() {
        try {
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost("http://211.221.245.121/LTE/service_user.php"); // http 통신 서버 주소
            // 데이터 추가
            nameValuePairs = new ArrayList<NameValuePair>(3);
            // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
            nameValuePairs.add(new BasicNameValuePair("tag", tag4));
            nameValuePairs.add(new BasicNameValuePair("free_no", free_no));
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
                        Toast.makeText(Free_Board_Read.this, "삭제 완료", Toast.LENGTH_SHORT).show();
                    }
                });
                Intent intent_community = new Intent(getApplicationContext(), Community_Main.class);
                startActivity(intent_community);
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
        Free_Board_Read.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(Free_Board_Read.this);
                builder.setTitle("Error.");
                builder.setMessage("오류입니다.")
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
                Intent intent_community = new Intent(getApplicationContext(), Community_Main.class);
                startActivity(intent_community);
                finish();
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}