package com.sungkyul.project_lte.MyPage;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.sungkyul.project_lte.Community.Plan_Board_Read;
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

/***
 * Created by WonHo on 2016-05-23.
 */
// 내가 세운 계획 List
public class MyPlanList extends AppCompatActivity {
    private ListView Myplanlist; // 내가 세운 계획을 보여주는 리스트 뷰

    String tag = "mylistPlanAll", res, success, email, name, login;
    HttpClient httpclient;
    HttpPost httppost;
    HttpResponse response;
    List<NameValuePair> nameValuePairs; // php로 post할 변수들을 담은 배열
    ProgressDialog dialog = null; // 프로그래스 바 초기화
    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>(); // 화면에 표시할 리스트 뷰에 나타낼 객체들을 담을 배열
    RbPreference pref = new RbPreference(this); // 값을 가져올 RePreference class 생성

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_plan_list);

        oslist = new ArrayList<HashMap<String, String>>();
        Myplanlist = (ListView) findViewById(R.id.myPlanList);
        email = pref.getValue("email", null);      // pref로부터 email값 가져와 email에 할당
        dialog = ProgressDialog.show(MyPlanList.this, "", "로딩중...", true);
        new Thread(new Runnable() {
            public void run() {
                setmyplanlist(); // 내가 세운 계획 List 출력해주는 메소드 호출
            }
        }).start();

        Myplanlist.setOnItemClickListener(MyPlanRead);
    }

    /***
     * 메뉴바 생성
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }



    private AdapterView.OnItemClickListener MyPlanRead = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String no = oslist.get(position).get("TRAVEL_NO");
            Intent intent_read_free = new Intent(getApplicationContext(), Plan_Board_Read.class);
            intent_read_free.putExtra("no", no);
            intent_read_free.putExtra("user_id", email);
            intent_read_free.putExtra("activity", "2");
            startActivity(intent_read_free);
            finish();               // 현 액티비티 종료
        }
    };
    // 내가 세운 계획 List 출력해주는 메소드
    void setmyplanlist() {
        try {
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost("http://211.221.245.121/LTE/service_user.php"); // http 통신 서버 주소
            // 데이터 추가
            nameValuePairs = new ArrayList<NameValuePair>(2);
            // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
            nameValuePairs.add(new BasicNameValuePair("tag", tag));
            nameValuePairs.add(new BasicNameValuePair("user_id", email));
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

                    String no = jobj.getString("TRAVEL_NO"); // TRAVEL_NO에 대한 객체 받음
                    String title = jobj.getString("TRAVEL_TITLE"); // TRAVEL_TITLE에 대한 객체 받음
                    String user = jobj.getString("name"); // USER_ID에 대한 객체 받음
                    String write_date = jobj.getString("WRITE_DATE"); // WRITE_DATE에 대한 객체 받음
                    String hit = jobj.getString("HIT_COUNT"); // HIT_COUNT에 대한 객체 받음
                    String recommend = jobj.getString("RECOMMEND");
                    String iso = jobj.getString("TRAVEL_COUR_CD"); // TRAVEL_COUR_CD에 대한 객체 받음

                    HashMap<String, String> map = new HashMap<String, String>();

                    map.put("TRAVEL_NO", no);
                    map.put("TRAVEL_TITLE", title);
                    map.put("USER_ID", user);
                    map.put("WRITE_DATE", write_date);
                    map.put("HIT_COUNT", hit);
                    map.put("RECOMMEND", recommend);
                    map.put("TRAVEL_COUR_CD", iso);

                    oslist.add(map);
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        ListAdapter adapter = new SimpleAdapter(MyPlanList.this, oslist,
                                R.layout.trip_board_item_row,
                                new String[]{"TRAVEL_NO", "TRAVEL_TITLE", "USER_ID", "WRITE_DATE", "HIT_COUNT", "RECOMMEND", "TRAVEL_COUR_CD"}, new int[]{
                                R.id.txtNum, R.id.txtTitle, R.id.txtName, R.id.txtDate, R.id.txtLook, R.id.txtLike, R.id.txtNation});
                        Myplanlist.setAdapter(adapter);
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
        runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyPlanList.this);
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
