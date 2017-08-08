package com.sungkyul.project_lte.Community;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

// 자유게시판 글 수정 페이지
public class Free_Board_Edit extends AppCompatActivity {
    Button btn_edit_freeok, btn_edit_freecancel; // 수정 완료, 수정 취소 버튼
    EditText edt_edtitle, edt_edtcontent; // 제목 입력, 글 입력
    String tag1 = "readFree", tag2 = "edtFree", res, email, free_no;
    HttpClient httpclient;
    HttpPost httppost;
    HttpResponse response;
    List<NameValuePair> nameValuePairs; // php로 post할 변수들을 담은 배열
    ProgressDialog dialog = null; // 프로그래스 바 초기화

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_free_board);

        btn_edit_freeok = (Button) findViewById(R.id.btn_EditFreeOK);
        btn_edit_freecancel = (Button) findViewById(R.id.btn_EditFreeCancel);
        edt_edtitle = (EditText) findViewById(R.id.edt_edtitle);
        edt_edtcontent = (EditText) findViewById(R.id.edt_edtcontent);

        Intent i = getIntent();
        free_no = i.getStringExtra("no");

        RbPreference pref = new RbPreference(this); // RbPreference 생성
        email = pref.getValue("email", null); // null 값을 해당 email에 할당

        dialog = ProgressDialog.show(Free_Board_Edit.this, "", "잠시만 기다려주세요...", true);
        new Thread(new Runnable() {
            public void run() {
                readFree(); // 자유게시판 글 불러오는 메소드 호출
            }
        }).start();

        btn_edit_freeok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = ProgressDialog.show(Free_Board_Edit.this, "", "수정중입니다...", true);
                new Thread(new Runnable() {
                    public void run() {
                        editfree(); // 자유게시판 글 수정 완료 메소드
                    }
                }).start();
            }
        });
        btn_edit_freecancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog diaBox = new AlertDialog.Builder(Free_Board_Edit.this)
                        .setTitle("수정 취소")
                        .setMessage("수정을 취소하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                Intent intent_edit_freecancel = new Intent(getApplicationContext(), Free_Board_Read.class);
                                intent_edit_freecancel.putExtra("no", free_no);
                                startActivity(intent_edit_freecancel);
                                finish();
                            }
                        })
                        .setNegativeButton("아니오", null)
                        .create();
                diaBox.show();
            }
        });
    }

    // 자유게시판 글 불러오는 메소드
    void readFree() {
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
                String a = json.getString("free"); // json 배열 생성
                JSONArray json1 = new JSONArray(a);
                JSONObject jobj = json1.getJSONObject(0);
                final String title = jobj.getString("FREE_TITLE"); // FREE_TITLE에 대한 객체 받음
                final String content = jobj.getString("CONTENT"); // CONTENT에 대한 객체 받음
                runOnUiThread(new Runnable() {
                    public void run() {
                        edt_edtitle.setText(title);
                        edt_edtcontent.setText(content);
                    }
                });
            } else if (res.equals("0")) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(Free_Board_Edit.this, "글을 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
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

    // 자유게시판 글 수정 완료 메소드
    void editfree() {
        try {
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost("http://211.221.245.121/LTE/service_user.php"); // http 통신 서버 주소
            // 데이터 추가
            nameValuePairs = new ArrayList<NameValuePair>(2);
            // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
            nameValuePairs.add(new BasicNameValuePair("tag", tag2));
            nameValuePairs.add(new BasicNameValuePair("free_no", free_no));
            nameValuePairs.add(new BasicNameValuePair("free_title", edt_edtitle.getText().toString()));  // $Edittext_value = $_POST['Edittext_value'];
            nameValuePairs.add(new BasicNameValuePair("content", edt_edtcontent.getText().toString()));
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
                        Toast.makeText(Free_Board_Edit.this, "글이 수정되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                Intent intent_edit_freeok = new Intent(getApplicationContext(), Free_Board_Read.class);
                intent_edit_freeok.putExtra("no", free_no);
                startActivity(intent_edit_freeok);
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
        Free_Board_Edit.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(Free_Board_Edit.this);
                builder.setTitle("Error.");
                builder.setMessage("수정이 불가능합니다.")
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
}
