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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.sungkyul.project_lte.MyPage.CartActivity;
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
import java.util.HashMap;
import java.util.List;
// 여행 계획 작성 페이지
public class Plan_Board_Write extends AppCompatActivity {
    Button btnComePlan, btnPlanOK, btnPlanCancel; // 작성 완료, 작성 취소 버튼
    EditText edt_title, edt_content; // 제목, 글 작성 EditText
    ListView plan_write_module; // 카트에서 불러온 모듈을 보여주는 ListView
    TextView txt_country; // 계획한 국가를 보여주는 TextView

    String tag="setCart", email, res, tag2 = "updatePlan", code = "b", no, plan_no, country;
    HttpClient httpclient;
    HttpPost httppost;
    HttpResponse response;
    List<NameValuePair> nameValuePairs; // php로 post할 변수들을 담은 배열
    ProgressDialog dialog = null; // 프로그래스 바 사라짐
    ListAdapter adapter = null;
    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>(); // 화면에 표시할 리스트 뷰에 나타낼 객체들을 담을 배열
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_plan_board);
        //btnComePlan = (Button)findViewById(R.id.btn_ComePlan);
        oslist = new ArrayList<HashMap<String, String>>();

        RbPreference pref = new RbPreference(this); // RbPreference 생성
        email = pref.getValue("email", null); // null값을 해당 email에 할당
        plan_no = pref.getValue("plan_write_no", null); // null값을 해당 plan_write_no에 할당

        Intent i = getIntent();
        country = i.getStringExtra("country");

        plan_write_module = (ListView)findViewById(R.id.list_WritePlanModule);

        View header = getLayoutInflater().inflate(R.layout.activity_write_plan_board_header, null, false);
        View footer = getLayoutInflater().inflate(R.layout.activity_write_plan_board_footer, null, false);

        plan_write_module.addHeaderView(header); // 여행 계획 작성 모듈 List에 header 연결
        plan_write_module.addFooterView(footer); // 여행 계획 작성 모듈 List에 footer 연결


        btnPlanOK = (Button)footer.findViewById(R.id.btn_PlanOK);
        btnPlanCancel = (Button)footer.findViewById(R.id.btn_PlanCancel);
        edt_title = (EditText)header.findViewById(R.id.edt_title);
        edt_content = (EditText)header.findViewById(R.id.edt_content);
        txt_country = (TextView)header.findViewById(R.id.txt_country);

        txt_country.setText(country);

        dialog = ProgressDialog.show(Plan_Board_Write.this, "", "잠시만 기다려주세요...", true);
        new Thread(new Runnable() {
            public void run() {
                setcart(); // 여행 카트에 담았던 모듈 불러오는 메소드 호출
            }
        }).start();


        btnPlanOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = ProgressDialog.show(Plan_Board_Write.this, "", "잠시만 기다려주세요...", true);
                new Thread(new Runnable() {
                    public void run() {
                        updateplan(); // 여행 계획 글 작성 완료하는 메소드 호출
                    }
                }).start();
            }
        });
        btnPlanCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog diaBox = new AlertDialog.Builder(Plan_Board_Write.this)
                        .setTitle("작성 취소")
                        .setMessage("작성을 취소하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                Intent intent_plan_cancel = new Intent(getApplicationContext(), CartActivity.class);
                                startActivity(intent_plan_cancel);
                                finish();
                            }
                        })
                        .setNegativeButton("아니오", null)
                        .create();
                diaBox.show();
            }
        });
    }
    // ListView에 모듈 List 연결하는 메소드 호출
    void comadapter() {
        adapter = new SimpleAdapter(Plan_Board_Write.this, oslist,
                R.layout.write_plan_module_item_row,
                new String[]{"PLACE", "CONTENT", "CART_NO"}, new int[]{
                R.id.txtPlace, R.id.txtContent});
        plan_write_module.setAdapter(adapter);
    }
    // 여행 카트에 담았던 모듈 불러오는 메소드
    void setcart() {
        try {
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost("http://211.221.245.121/LTE/service_user.php"); // http 통신 서버 주소
            // 데이터 추가
            nameValuePairs = new ArrayList<NameValuePair>(1);
            // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
            nameValuePairs.add(new BasicNameValuePair("tag", tag));
            nameValuePairs.add(new BasicNameValuePair("email", email));
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
                String a = json.getString("cart"); // json 배열 생성
                JSONArray json1 = new JSONArray(a);
                for (int i = 0; i < json1.length(); i++) {
                    JSONObject jobj = json1.getJSONObject(i);
                    String no = jobj.getString("CART_NO"); // CART_NO에 대한 객체 받음
                    String place = jobj.getString("PLACE"); // PLACE에 대한 객체 받음
                    String con = jobj.getString("CONTENT"); // CONTENT에 대한 객체 받음

                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("CART_NO", no);
                    map.put("PLACE", place);
                    map.put("CONTENT", con);

                    oslist.add(map);
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        comadapter(); // ListView에 모듈 List 연결하는 메소드 호출
                    }
                });
            } else if (success.equals("0")) {
                Toast.makeText(Plan_Board_Write.this, "댓글이 없습니다.", Toast.LENGTH_SHORT).show();
            } else {
                showAlert(); // 오류 다이얼로그 메소드 호출
            }
        } catch (Exception e) {
            dialog.dismiss(); // 프로그래스 바 사라짐
            System.out.println("Exception : " + e.getMessage());
        }
    }
    // 여행 계획 글 작성 완료하는 메소드
    void updateplan() {
        try {
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost("http://211.221.245.121/LTE/service_user.php"); // http 통신 서버 주소
            // 데이터 추가
            nameValuePairs = new ArrayList<NameValuePair>(5);
            // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
            nameValuePairs.add(new BasicNameValuePair("tag", tag2));
            nameValuePairs.add(new BasicNameValuePair("email", email));
            nameValuePairs.add(new BasicNameValuePair("plan_no", plan_no));// $Edittext_value = $_POST['Edittext_value'];
            nameValuePairs.add(new BasicNameValuePair("plan_title", edt_title.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("content", edt_content.getText().toString()));
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
                RbPreference pref = new RbPreference(this); // RbPreference 생성
                pref.put("plan_write_no", null); // 해당 plan_write_no값에 null 저장
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(Plan_Board_Write.this, "글이 등록되었습니다.", Toast.LENGTH_SHORT).show();
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
            //System.out.println("Exception : " + e.getMessage());
            e.printStackTrace();
        }
    }
    // 오류 다이얼로그 메소드
    public void showAlert() {
        Plan_Board_Write.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(Plan_Board_Write.this);
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

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            //하드웨어 뒤로가기 버튼에 따른 이벤트 설정
            case KeyEvent.KEYCODE_BACK:
                Intent intent_main = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(intent_main);
                finish();
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
