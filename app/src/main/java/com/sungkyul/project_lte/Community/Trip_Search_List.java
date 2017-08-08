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
// 여행 후기 게시판 댓글 페이지
public class Trip_Search_List extends AppCompatActivity {
    TextView txt_searchWord; // 검색 단어 표시
    Button btn_back; // 뒤로가기 버튼
    private ListView list_plan_search; // 검색 단어에 맞는 글 목록 출력 리스트 뷰
    String tag = "searchTrip", word, res, search, country, tag2 = "searchCountryTrip";
    HttpClient httpclient;
    HttpPost httppost;
    HttpResponse response;
    List<NameValuePair> nameValuePairs; // php로 post할 변수들을 담은 배열
    ProgressDialog dialog = null; // 프로그래스 바 초기화
    List<Plan_Search_Item> list = new ArrayList<Plan_Search_Item>(); // 화면에 표시할 리스트 뷰에 나타낼 객체들을 담을 배열

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip__search__list);
        btn_back = (Button)findViewById(R.id.btn_back);
        txt_searchWord = (TextView)findViewById(R.id.txt_searchWord);
        list_plan_search = (ListView)findViewById(R.id.listView_FreeBoard);

        Intent i = getIntent();
        search = i.getStringExtra("search");

        if(search.equals("1")) {
            word = i.getStringExtra("word");
            txt_searchWord.setText(word);
            dialog = ProgressDialog.show(Trip_Search_List.this, "", "로딩중...", true);
            new Thread(new Runnable() {
                public void run() {
                    searchPlan(); // 입력한 단어로 검색하는 메소드 호출
                }
            }).start();
        } else if(search.equals("2")) {
            country = i.getStringExtra("country");
            txt_searchWord.setText(country);
            dialog = ProgressDialog.show(Trip_Search_List.this, "", "로딩중...", true);
            new Thread(new Runnable() {
                public void run() {
                    searchCountry(); // 선택한 국가로 검색하는 메소드 호출
                }
            }).start();
        }


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_Free_List = new Intent(getApplicationContext(), Community_Main.class);
                startActivity(intent_Free_List);
                finish();
            }
        });

        list_plan_search.setOnItemClickListener(FreeBoardRead);
    }
    // 리스트에서 글 클릭하면 해당 글 페이지로 이동
    private AdapterView.OnItemClickListener FreeBoardRead = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String no = list.get(position).getNum();
            Intent intent_read_free = new Intent(Trip_Search_List.this, Trip_Board_Read.class);
            intent_read_free.putExtra("no", no);
            startActivity(intent_read_free);
            finish();
        }
    };
    // 선택한 국가로 검색하는 메소드
    void searchCountry() {
        try {
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost("http://211.221.245.121/LTE/service_user.php"); // http 통신 서버 주소
            // 데이터 추가
            nameValuePairs = new ArrayList<NameValuePair>(2);
            // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
            nameValuePairs.add(new BasicNameValuePair("tag", tag2));
            nameValuePairs.add(new BasicNameValuePair("country", country));
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
                String a = json.getString("trip"); // json 배열 생성
                JSONArray json1 = new JSONArray(a);
                for(int i=0; i<json1.length(); i++) {
                    JSONObject jobj = json1.getJSONObject(i);

                    String no = jobj.getString("TRAVEL_NO"); // TRAVEL_NO에 대한 객체 받음
                    String title = jobj.getString("TRAVEL_TITLE"); // TRAVEL_TITLE에 대한 객체 받음
                    String user = jobj.getString("name"); // name에 대한 객체 받음
                    String write_date = jobj.getString("WRITE_DATE"); // WRITE_DATE에 대한 객체 받음
                    String hit = jobj.getString("HIT_COUNT"); // HIT_COUNT에 대한 객체 받음
                    String country = jobj.getString("TRAVEL_COUR_CD"); // TRAVEL_COUR_CD에 대한 객체 받음
                    String recommend = jobj.getString("RECOMMEND");

                    Plan_Search_Item plan_search_item = new Plan_Search_Item();
                    plan_search_item.num = no;
                    plan_search_item.title = title;
                    plan_search_item.name = user;
                    plan_search_item.date = write_date;
                    plan_search_item.look = hit;
                    plan_search_item.country = country;
                    plan_search_item.like = recommend;

                    list.add(plan_search_item);
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        ListAdapter adapter = new Plan_Search_Adapter(Trip_Search_List.this, R.layout.plan_search_item_row, list);
                        list_plan_search.setAdapter(adapter);
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
    // 입력한 단어로 검색하는 메소드
    void searchPlan() {
        try {
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost("http://211.221.245.121/LTE/service_user.php"); // http 통신 서버 주소
            // 데이터 추가
            nameValuePairs = new ArrayList<NameValuePair>(2);
            // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
            nameValuePairs.add(new BasicNameValuePair("tag", tag));
            nameValuePairs.add(new BasicNameValuePair("word", word.trim()));
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
                String a = json.getString("trip"); // json 배열 생성
                JSONArray json1 = new JSONArray(a);
                for(int i=0; i<json1.length(); i++) {
                    JSONObject jobj = json1.getJSONObject(i);

                    String no = jobj.getString("TRAVEL_NO"); // TRAVEL_NO에 대한 객체 받음
                    String title = jobj.getString("TRAVEL_TITLE"); // TRAVEL_TITLE에 대한 객체 받음
                    String user = jobj.getString("name"); // name에 대한 객체 받음
                    String write_date = jobj.getString("WRITE_DATE"); // WRITE_DATE에 대한 객체 받음
                    String hit = jobj.getString("HIT_COUNT"); // HIT_COUNT에 대한 객체 받음
                    String country = jobj.getString("TRAVEL_COUR_CD"); // TRAVEL_COUR_CD에 대한 객체 받음
                    String recommend = jobj.getString("RECOMMEND");

                    Plan_Search_Item plan_search_item = new Plan_Search_Item();
                    plan_search_item.num = no;
                    plan_search_item.title = title;
                    plan_search_item.name = user;
                    plan_search_item.date = write_date;
                    plan_search_item.look = hit;
                    plan_search_item.country = country;
                    plan_search_item.like = recommend;

                    list.add(plan_search_item);
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        ListAdapter adapter = new Plan_Search_Adapter(Trip_Search_List.this, R.layout.free_search_item_row, list);
                        list_plan_search.setAdapter(adapter);
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
        Trip_Search_List.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(Trip_Search_List.this);
                builder.setTitle("Error.");
                builder.setMessage("검색 결과가 없습니다.")
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
                Intent intent_Free_List = new Intent(getApplicationContext(), Community_Main.class);
                startActivity(intent_Free_List);
                finish();
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
