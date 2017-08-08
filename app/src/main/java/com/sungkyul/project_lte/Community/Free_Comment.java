package com.sungkyul.project_lte.Community;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
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

/**
 * Created by Hanna on 2016-06-03.
 */
// 자유게시판 댓글 페이지
public class Free_Comment extends AppCompatActivity {
    EditText edt_free_comment; // 댓글 입력
    Button btn_free_inscom; // 댓글 작성 버튼
    ListView free_list_comment; // 댓글 표시 리스트 뷰

    List<Free_Comment_Item> list = new ArrayList<Free_Comment_Item>(); // 화면에 표시할 리스트 뷰에 나타낼 객체들을 담을 배열
    ListAdapter adapter = null;
    String tag1 = "readFree", res, tag2 = "insComment", email, tag3 = "listFreeCom", login, writer, tag4 = "deleteFree", no, tag5="deleteFreeComment", name;
    HttpClient httpclient;
    HttpPost httppost;
    HttpResponse response;
    List<NameValuePair> nameValuePairs; // php로 post할 변수들을 담은 배열
    ProgressDialog dialog = null; // 프로그래스 바 사라짐
    String free_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_comment);
        free_list_comment = (ListView) findViewById(R.id.free_list_comment);
        edt_free_comment = (EditText)findViewById(R.id.edt_free_comment);
        btn_free_inscom = (Button)findViewById(R.id.btn_free_inscom);

        Intent i = getIntent();
        free_no = i.getStringExtra("no");

        RbPreference pref = new RbPreference(this); // RbPreference 생성
        email = pref.getValue("email", null); // null 값을 해당 email에 할당
        name = pref.getValue("name",null); // null 값을 해당 name에 할당
        login = pref.getValue("login", null); // null값을 해당 login에 할당

        dialog = ProgressDialog.show(Free_Comment.this, "", "잠시만 기다려주세요...", true);
        new Thread(new Runnable() {
            public void run() {
                setcomlist(); // 댓글 불러오는 메소드 호출
            }
        }).start();

        btn_free_inscom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(login != null) {
                    if(!edt_free_comment.getText().toString().equals("")) {
                        dialog = ProgressDialog.show(Free_Comment.this, "", "잠시만 기다려주세요...", true);
                        new Thread(new Runnable() {
                            public void run() {
                                inscomment(); // 댓글 작성하는 메소드 호출
                            }
                        }).start();
                    } else {
                        Toast.makeText(Free_Comment.this, "댓글을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Free_Comment.this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        free_list_comment.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                no = list.get(position).getNo();
                writer = list.get(position).getCommenter();
                if(email != null) {
                    if (email.equals(writer)) {
                        AlertDialog diaBox = new AlertDialog.Builder(Free_Comment.this)
                                .setTitle("삭제")
                                .setMessage("정말로 삭제하시겠습니까?")
                                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        list.remove(position);
                                        free_list_comment.clearChoices();
                                        new Thread(new Runnable() {
                                            public void run() {
                                                delcomment(); // 댓글 삭제하는 메소드 호출
                                            }
                                        }).start();
                                    }
                                })
                                .setNegativeButton("아니오", null)
                                .create();
                        diaBox.show();
                    } else {
                        Toast.makeText(Free_Comment.this, "자신의 댓글만 삭제가 가능합니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Free_Comment.this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }
    // ListView에 자유게시판 댓글 List 연결하는 메소드
    void comadapter() {
        adapter = new Free_Comment_Adapter(Free_Comment.this, R.layout.free_comment_item_row, list);
        free_list_comment.setAdapter(adapter);
    }
    // 댓글 불러오는 메소드
    void setcomlist() {
        try {
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost("http://211.221.245.121/LTE/service_user.php"); // http 통신 서버 주소
            // 데이터 추가
            nameValuePairs = new ArrayList<NameValuePair>(1);
            // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
            nameValuePairs.add(new BasicNameValuePair("tag", tag3));
            nameValuePairs.add(new BasicNameValuePair("free_no", free_no));
            httppost.setHeader("Content-type", "application/x-www-form-urlencoded; charset=utf-8");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));
            // http 통신 요청 실행

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
                String a = json.getString("free"); // jjson 배열 생성
                JSONArray json1 = new JSONArray(a);
                for (int i = 0; i < json1.length(); i++) {
                    JSONObject jobj = json1.getJSONObject(i);
                    String no = jobj.getString("COMMENT_NO"); // COMMENT_NO에 대한 객체 받음
                    String id = jobj.getString("USER_ID"); // USER_ID에 대한 객체 받음
                    String con = jobj.getString("CONTENT"); // CONTENT에 대한 객체 받음
                    String name = jobj.getString("name"); // name에 대한 객체 받음

                    Free_Comment_Item free_comment_item = new Free_Comment_Item();
                    free_comment_item.name = name;
                    free_comment_item.comment = con;
                    free_comment_item.no = no;
                    free_comment_item.commenter = id;

                    list.add(free_comment_item);
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        comadapter(); // 자유게시판 댓글 List 연결하는 메소드 호출
                    }
                });
            } else if (success.equals("0")) {
                Toast.makeText(Free_Comment.this, "댓글이 없습니다.", Toast.LENGTH_SHORT).show();
            } else {
                showAlert(); // 오류 다이얼로그 메소드 호출
            }
        } catch (Exception e) {
            dialog.dismiss(); // 프로그래스 바 사라짐
            System.out.println("Exception : " + e.getMessage());
        }
    }
    // 댓글 작성하는 메소드
    void inscomment() {
        try {
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost("http://211.221.245.121/LTE/service_user.php"); // http 통신 서버 주소
            // 데이터 추가
            nameValuePairs = new ArrayList<NameValuePair>(3);
            // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
            nameValuePairs.add(new BasicNameValuePair("tag", tag2));
            nameValuePairs.add(new BasicNameValuePair("free_no", free_no));
            nameValuePairs.add(new BasicNameValuePair("user_id", email)); // $Edittext_value = $_POST['Edittext_value'];
            nameValuePairs.add(new BasicNameValuePair("comment", edt_free_comment.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("name", name));
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
                String a = json.getString("free"); // json 배열 생성
                JSONArray json1 = new JSONArray(a);

                for (int i = 0; i < json1.length(); i++) {
                    JSONObject jobj = json1.getJSONObject(i);
                    String no = jobj.getString("COMMENT_NO"); // COMMENT_NO에 대한 객체 받음
                    String id = jobj.getString("USER_ID"); // USER_ID에 대한 객체 받음
                    String name = jobj.getString("name"); // name에 대한 객체 받음
                    String con = jobj.getString("CONTENT"); // CONTENT에 대한 객체 받음

                    Free_Comment_Item free_comment_item = new Free_Comment_Item();
                    free_comment_item.name = name;
                    free_comment_item.comment = con;
                    free_comment_item.no = no;
                    free_comment_item.commenter = id;

                    list.add(free_comment_item);
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        comadapter(); // 자유게시판 댓글 List 연결하는 메소드 호출
                        Toast.makeText(Free_Comment.this, "등록 완료", Toast.LENGTH_SHORT).show();
                        edt_free_comment.setText(null);
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
    // 댓글 삭제하는 메소드
    void delcomment() {
        try {
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost("http://211.221.245.121/LTE/service_user.php"); // http 통신 서버 주소
            // 데이터 추가
            nameValuePairs = new ArrayList<NameValuePair>(1);
            // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
            nameValuePairs.add(new BasicNameValuePair("tag", tag5));
            nameValuePairs.add(new BasicNameValuePair("comment_no", no));
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
                        Toast.makeText(Free_Comment.this, "삭제 완료", Toast.LENGTH_SHORT).show();
                        comadapter(); // 자유게시판 댓글 List 연결하는 메소드 호출
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
        Free_Comment.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(Free_Comment.this);
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
}
