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
// 자유게시판 연결 List
public class Free_Search_List extends AppCompatActivity {
    TextView txt_searchWord; // 검색 단어 표시
    Button btn_back; // 뒤로가기 버튼
    private ListView list_free_search; // 검색 단어에 맞는 글들 보여주는 List
    String tag = "searchFree", word, res;
    HttpClient httpclient;
    HttpPost httppost;
    HttpResponse response;
    List<NameValuePair> nameValuePairs; // php로 post할 변수들을 담은 배열
    ProgressDialog dialog = null; // 프로그래스 바 초기화
    List<Free_Search_Item> list = new ArrayList<Free_Search_Item>(); // 화면에 표시할 리스트 뷰에 나타낼 객체들을 담을 배열

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_search_list);
        btn_back = (Button)findViewById(R.id.btn_back);
        txt_searchWord = (TextView)findViewById(R.id.txt_searchWord);
        list_free_search = (ListView)findViewById(R.id.listView_FreeBoard);

        Intent i = getIntent();
        word = i.getStringExtra("word");

        txt_searchWord.setText(word);

        dialog = ProgressDialog.show(Free_Search_List.this, "", "로딩중...", true);
        new Thread(new Runnable() {
            public void run() {
                searchFree(); // 검색 결과 List로 보여주는 메소드 호출
            }
        }).start();

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_Free_List = new Intent(getApplicationContext(), Community_Main.class);
                startActivity(intent_Free_List);
                finish();
            }
        });

        list_free_search.setOnItemClickListener(FreeBoardRead);
    }

    private AdapterView.OnItemClickListener FreeBoardRead = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String no = list.get(position).getNum();
            Intent intent_read_free = new Intent(Free_Search_List.this, Free_Board_Read.class);
            intent_read_free.putExtra("no", no);
            startActivity(intent_read_free);
            finish();
        }
    };
    // 검색 결과 List로 보여주는 메소드
    void searchFree() {
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
                String a = json.getString("free"); // json 배열 생성
                JSONArray json1 = new JSONArray(a);
                for(int i=0; i<json1.length(); i++) {
                    JSONObject jobj = json1.getJSONObject(i);

                    String no = jobj.getString("FREE_NO"); // FREE_NO에 대한 객체 받음
                    String title = jobj.getString("FREE_TITLE"); // FREE_TITLE에 대한 객체 받음
                    String user = jobj.getString("name"); // USER_ID에 대한 객체 받음
                    String write_date = jobj.getString("WRITE_DATE"); // WRITE_DATE에 대한 객체 받음
                    String hit = jobj.getString("HIT_COUNT"); // HIT_COUNT에 대한 객체 받음

                    Free_Search_Item free_search_item = new Free_Search_Item();
                    free_search_item.num = no;
                    free_search_item.title = title;
                    free_search_item.name = user;
                    free_search_item.date = write_date;
                    free_search_item.look = hit;

                    list.add(free_search_item);
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        ListAdapter adapter = new Free_Search_Adapter(Free_Search_List.this, R.layout.free_search_item_row, list);
                        list_free_search.setAdapter(adapter);
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
        Free_Search_List.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(Free_Search_List.this);
                builder.setTitle("Join Error.");
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
