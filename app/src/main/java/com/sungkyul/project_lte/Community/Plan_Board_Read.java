package com.sungkyul.project_lte.Community;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sungkyul.project_lte.MyPage.MyContentList;
import com.sungkyul.project_lte.MyPage.MyPlanList;
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
// 여행 계획 글 읽기
public class Plan_Board_Read extends AppCompatActivity {
    private ListView plan_read_module; // 여행 계획 모듈 리스트 뷰
    TextView txt_title, txt_like, txt_writer, txt_write_date, txt_hit, txt_content, txt_country; // 글 제목, 추천수, 작성자, 작성일, 조회수, 일반글
    Button btn_delPlan, btn_commentPlan, btn_recommend; // 계획 삭제, 댓글, 추천 버튼

    String tag1 = "readPlan", plan_no, res, tag2 = "listPlanModule", user_id, writer, no, tag3 = "deletePlan", place, content, email, tag4="addCart", login, cour_cd, tag5 = "recommend", activity;
    HttpClient httpclient;
    HttpPost httppost;
    HttpResponse response;
    List<NameValuePair> nameValuePairs; // php로 post할 변수들을 담은 배열
    ProgressDialog dialog = null; // 프로그래스 바 초기화
    ListAdapter adapter = null;
    List<Plan_Read_Module_Item> list = new ArrayList<Plan_Read_Module_Item>(); // 화면에 표시할 리스트 뷰에 나타낼 객체들을 담을 배열

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_plan_board);

        plan_read_module = (ListView)findViewById(R.id.list_ReadPlanModule);

        Intent i = getIntent();
        plan_no = i.getStringExtra("no");
        user_id = i.getStringExtra("user_id");
        activity = i.getStringExtra("activity");

        RbPreference pref = new RbPreference(this); // RbPreference 생성
        email = pref.getValue("email", null); // null값을 해당 email에 할당
        login = pref.getValue("login", null); // null값을 해당 login에 할당

        View header = getLayoutInflater().inflate(R.layout.activity_read_plan_board_header, null, false);
        View footer = getLayoutInflater().inflate(R.layout.activity_read_plan_board_footer, null, false);

        plan_read_module.addHeaderView(header); // 여행 계획 모듈 List에 header 연결
        plan_read_module.addFooterView(footer); // 여행 계획 모듈 List에 footer 연결

        txt_writer = (TextView)header.findViewById(R.id.txt_writer);
        txt_write_date = (TextView)header.findViewById(R.id.txt_write_date);
        txt_hit = (TextView)header.findViewById(R.id.txt_hit);
        txt_content = (TextView)header.findViewById(R.id.txt_content);
        txt_title = (TextView)header.findViewById(R.id.txt_title);
        txt_like = (TextView)header.findViewById(R.id.txt_like);
        btn_delPlan = (Button)footer.findViewById(R.id.btn_delPlan);
        btn_commentPlan = (Button)footer.findViewById(R.id.btn_commentPlan);
        btn_recommend = (Button)footer.findViewById(R.id.btn_recommend);
        txt_country = (TextView)header.findViewById(R.id.txt_country);

        dialog = ProgressDialog.show(Plan_Board_Read.this, "", "잠시만 기다려주세요...", true);
        new Thread(new Runnable() {
            public void run() {
                setplan(); // 여행 계획 글 불러오는 메소드 호출
                setmodule(); // 여행 계획 모듈 불러오는 메소드 호출
            }
        }).start();

        btn_delPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = ProgressDialog.show(Plan_Board_Read.this, "", "삭제중입니다...", true);
                new Thread(new Runnable() {
                    public void run() {
                        delPlan(); // 여행 계획 글 삭제하는 메소드 호출
                    }
                }).start();
            }
        });

        btn_commentPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_trip_comment = new Intent(getApplicationContext(), Plan_Comment.class);
                intent_trip_comment.putExtra("no", plan_no);
                startActivity(intent_trip_comment);
            }
        });

        btn_recommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(login != null) {
                    new Thread(new Runnable() {
                        public void run() {
                            recommend(); // 여행 계획 글 추천하는 메소드 호출
                        }
                    }).start();
                } else {
                    Toast.makeText(Plan_Board_Read.this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        plan_read_module.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if (login != null) {
                    no = list.get(position - 1).getNo();
                    place = list.get(position - 1).getPlace();
                    content = list.get(position - 1).getContent();
                    AlertDialog diaBox = new AlertDialog.Builder(Plan_Board_Read.this)
                            .setTitle("카트 담기")
                            .setMessage("해당 모듈을 카트에 담으시겠습니까?")
                            .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    try {
                                        new Thread(new Runnable() {
                                            public void run() {
                                                addcart(); // 여행 카트에 모듈 추가하는 메소드 호출
                                            }
                                        }).start();
                                    } catch (Exception e) {
                                        Toast.makeText(Plan_Board_Read.this, "모듈을 담을 수 없습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setNegativeButton("아니오", null)
                            .create();
                    diaBox.show();
                } else {
                    Toast.makeText(Plan_Board_Read.this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }
    // 작성자 비교 메소드
    void writer_check() {
        if (email != null) {
            if (email.equals(writer)) {
                btn_delPlan.setVisibility(View.VISIBLE);
            }
        }
    }
    // ListView에 모듈 List 연결하는 메소드
    void comadapter() {
        adapter = new Plan_Read_Module_Adapter(Plan_Board_Read.this, R.layout.read_plan_module_item_row, list);
        plan_read_module.setAdapter(adapter);
    }
    // 여행 계획 글 추천하는 메소드
    void recommend() {
        try {
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost("http://211.221.245.121/LTE/service_user.php"); // http 통신 서버 주소
            // 데이터 추가
            nameValuePairs = new ArrayList<NameValuePair>(1);
            // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
            nameValuePairs.add(new BasicNameValuePair("tag", tag5));    // $Edittext_value = $_POST['Edittext_value'];
            nameValuePairs.add(new BasicNameValuePair("trip_no", plan_no)); //original_travel_no
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
                        Toast.makeText(Plan_Board_Read.this, "해당 게시물을 추천하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                showAlert(); // 오류 다이얼로그 메소드 호출
            }
        } catch (Exception e) {
            dialog.dismiss(); // 프로그래스 바 사라짐
            e.printStackTrace();
        }
    }
    // 카트에 모듈 추가하는 메소드
    void addcart() {
        try {
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost("http://211.221.245.121/LTE/service_user.php"); // http 통신 서버 주소
            // 데이터 추가
            nameValuePairs = new ArrayList<NameValuePair>(6);
            // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
            nameValuePairs.add(new BasicNameValuePair("tag", tag4));    // $Edittext_value = $_POST['Edittext_value'];
            nameValuePairs.add(new BasicNameValuePair("trip_no", plan_no)); //original_travel_no
            nameValuePairs.add(new BasicNameValuePair("email", email));   //email
            nameValuePairs.add(new BasicNameValuePair("module_no", no));    //original_module_no
            nameValuePairs.add(new BasicNameValuePair("place", place));
            nameValuePairs.add(new BasicNameValuePair("content", content));
            nameValuePairs.add(new BasicNameValuePair("iso", cour_cd));
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
                        Toast.makeText(Plan_Board_Read.this, "추가 완료", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                showAlert(); // 오류 다이얼로그 메소드 호출
            }
        } catch (Exception e) {
            dialog.dismiss(); // 프로그래스 바 사라짐
            e.printStackTrace();
        }
    }
    // 여행 계획 삭제하는 메소드
    void delPlan() {
        try {
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost("http://211.221.245.121/LTE/service_user.php"); // http 통신 서버 주소
            // 데이터 추가
            nameValuePairs = new ArrayList<NameValuePair>(3);
            // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
            nameValuePairs.add(new BasicNameValuePair("tag", tag3));
            nameValuePairs.add(new BasicNameValuePair("plan_no", plan_no));
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
                        Toast.makeText(Plan_Board_Read.this, "삭제 완료", Toast.LENGTH_SHORT).show();
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
    // 여행 계획 모듈 불러오는 메소드
    void setmodule() {
        try {
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost("http://211.221.245.121/LTE/service_user.php"); // http 통신 서버 주소
            // 데이터 추가
            nameValuePairs = new ArrayList<NameValuePair>(1);
            // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
            nameValuePairs.add(new BasicNameValuePair("tag", tag2));
            nameValuePairs.add(new BasicNameValuePair("plan_no", plan_no));
            nameValuePairs.add(new BasicNameValuePair("user_id", user_id));
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
                String a = json.getString("plan"); // json 배열 생성
                JSONArray json1 = new JSONArray(a);
                for (int i = 0; i < json1.length(); i++) {
                    JSONObject jobj = json1.getJSONObject(i);
                    String no = jobj.getString("CART_NO"); // CART_NO에 대한 객체 받음
                    String place = jobj.getString("PLACE"); // PLACE에 대한 객체 받음
                    String con = jobj.getString("CONTENT"); // CONTENT에 대한 객체 받음

                    Plan_Read_Module_Item plan_read_module_item = new Plan_Read_Module_Item();
                    plan_read_module_item.place = place;
                    plan_read_module_item.content = con;
                    plan_read_module_item.no = no;

                    list.add(plan_read_module_item);
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        comadapter(); // ListView에 모듈 List 연결하는 메소드 호출
                    }
                });
            } else if (success.equals("0")) {
                Toast.makeText(Plan_Board_Read.this, "댓글이 없습니다.", Toast.LENGTH_SHORT).show();
            } else {
                showAlert(); // 오류 다이얼로그 메소드 호출
            }
        } catch (Exception e) {
            dialog.dismiss(); // 프로그래스 바 사라짐
            System.out.println("Exception : " + e.getMessage());
        }
    }
    // 여행 계획 글 불러오는 메소드
    void setplan() {
        try {
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost("http://211.221.245.121/LTE/service_user.php"); // http 통신 서버 주소
            // 데이터 추가
            nameValuePairs = new ArrayList<NameValuePair>(1);
            // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
            nameValuePairs.add(new BasicNameValuePair("tag", tag1));
            nameValuePairs.add(new BasicNameValuePair("plan_no", plan_no));  // $Edittext_value = $_POST['Edittext_value'];
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
                String a = json.getString("plan"); // json 배열 생성
                JSONArray json1 = new JSONArray(a);
                JSONObject jobj = json1.getJSONObject(0);
                final String title = jobj.getString("TRAVEL_TITLE"); // TRAVEL_TITLE에 대한 객체 받음
                final String user = jobj.getString("name"); // name에 대한 객체 받음
                writer = jobj.getString("USER_ID"); // USER_ID에 대한 객체 받음
                final String date = jobj.getString("WRITE_DATE"); // WRITE_DATE에 대한 객체 받음
                final String content = jobj.getString("CONTENT"); // CONTENT에 대한 객체 받음
                final String hit = jobj.getString("HIT_COUNT"); // HIT_COUNT에 대한 객체 받음
                final String like = jobj.getString("RECOMMEND"); // RECOMMEND에 대한 객체 받음
                cour_cd = jobj.getString("TRAVEL_COUR_CD"); // TRAVEL_COUR_CD에 대한 객체 받음
                runOnUiThread(new Runnable() {
                    public void run() {
                        writer_check(); // 작성자 비교 메소드 호출
                        txt_title.setText(title);
                        txt_writer.setText(user);
                        txt_write_date.setText(date);
                        txt_hit.setText(hit);
                        txt_content.setText(content);
                        txt_like.setText(like);
                        txt_country.setText(cour_cd);
                    }
                });
            } else if (res.equals("0")) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(Plan_Board_Read.this, "글을 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
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
        Plan_Board_Read.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(Plan_Board_Read.this);
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
                if(activity.equals("2")) {
                    Intent intent_community = new Intent(getApplicationContext(), MyPlanList.class);
                    startActivity(intent_community);
                    finish();
                } else if(activity.equals("3")) {
                    Intent intent_community = new Intent(getApplicationContext(), MyContentList.class);
                    startActivity(intent_community);
                    finish();
                } else {
                    Intent intent_community = new Intent(getApplicationContext(), Community_Main.class);
                    startActivity(intent_community);
                    finish();
                }
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
