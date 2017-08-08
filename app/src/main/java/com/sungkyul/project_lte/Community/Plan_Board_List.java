package com.sungkyul.project_lte.Community;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

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

import static com.google.android.gms.internal.zzir.runOnUiThread;
// 여행 계획 List 페이지
public class Plan_Board_List extends Fragment {
    Button  btn_set_plan_country, btn_searchPlan; // 국가 설정 버튼, 검색 버튼
    EditText edt_search; // 검색 단어 입력
    private ListView list_plan_board; // 여행 계획 게시판 리스트 뷰

    String tag = "listPlan", res, success, email, no, login, countryname;
    HttpClient httpclient;
    HttpPost httppost;
    HttpResponse response;
    List<NameValuePair> nameValuePairs; // php로 post할 변수들을 담은 배열
    ProgressDialog dialog = null; // 프로그래스 바 초기화
    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>(); // 화면에 표시할 리스트 뷰에 나타낼 객체들을 담을 배열

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_plan_board_list, container, false);
        //plan_write = (Button)view.findViewById(R.id.btn_PlanWrite);
        btn_set_plan_country = (Button)view.findViewById(R.id.btn_SetPlanCountry);
        btn_searchPlan = (Button) view.findViewById(R.id.btn_searchPlan);
        edt_search = (EditText)view.findViewById(R.id.edt_search);
        final Intent intent = new Intent(getActivity(), CountrycodeActivity.class);

        RbPreference pref = new RbPreference(getActivity()); // RbPreference 생성
        email = pref.getValue("email", null); // null값을 해당 email에 할당
        login = pref.getValue("login", null); // null값을 해당 login에 할당

        btn_set_plan_country.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivityForResult(intent, 1);
            }
        });

        list_plan_board = (ListView)view.findViewById(R.id.listView_PlanBoard);

        dialog = ProgressDialog.show(getActivity(), "", "로딩중...", true);
        new Thread(new Runnable() {
            public void run() {
                setplanlist(); // 여행 계획 List 출력하는 메소드 호출
            }
        }).start();

        btn_searchPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = edt_search.getText().toString();
                Intent intent_read_search = new Intent(getActivity(), Plan_Search_List.class);
                intent_read_search.putExtra("search", "1");
                intent_read_search.putExtra("word", word);
                startActivity(intent_read_search);
                getActivity().finish();
            }
        });

        list_plan_board.setOnItemClickListener(PlanBoardRead);
        return view;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            String countryCode = data.getStringExtra(CountrycodeActivity.RESULT_CONTRYCODE);
            countryname = data.getStringExtra(CountrycodeActivity.countryname);
            Intent intent_search_country = new Intent(getActivity(), Plan_Search_List.class);
            intent_search_country.putExtra("search", "2");
            intent_search_country.putExtra("country", countryname);
            startActivity(intent_search_country);
            getActivity().finish();
        }
    }
    private AdapterView.OnItemClickListener PlanBoardRead = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String no = oslist.get(position).get("TRAVEL_NO");
            String user_id = oslist.get(position).get("USER_ID");
            Intent intent_read_trip = new Intent(getActivity(),Plan_Board_Read.class);
            intent_read_trip.putExtra("no", no);
            intent_read_trip.putExtra("user_id", user_id);
            intent_read_trip.putExtra("activity", "1");
            startActivity(intent_read_trip);
            getActivity().finish();
        }
    };
    // 여행 계획 List 출력하는 메소드
    void setplanlist() {
        try {
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost("http://211.221.245.121/LTE/service_user.php"); // http 통신 서버 주소
            // 데이터 추가
            nameValuePairs = new ArrayList<NameValuePair>(1);
            // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
            nameValuePairs.add(new BasicNameValuePair("tag", tag));
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
                String a = json.getString("plan"); // json 배열 생성
                JSONArray json1 = new JSONArray(a);
                for(int i=0; i<json1.length(); i++) {
                    JSONObject jobj = json1.getJSONObject(i);

                    String no = jobj.getString("TRAVEL_NO"); // TRAVEL_NO에 대한 객체 받음
                    String title = jobj.getString("TRAVEL_TITLE"); // TRAVEL_TITLE에 대한 객체 받음
                    String name = jobj.getString("name"); // name에 대한 객체 받음
                    String write_date = jobj.getString("WRITE_DATE"); // WRITE_DATE에 대한 객체 받음
                    String hit = jobj.getString("HIT_COUNT"); // HIT_COUNT에 대한 객체 받음
                    String like = jobj.getString("RECOMMEND"); // RECOMMENT에 대한 객체 받음
                    String user = jobj.getString("USER_ID"); // USER_ID에 대한 객체 받음
                    String country = jobj.getString("TRAVEL_COUR_CD"); // TRAVEL_COUR_CD에 대한 객체 받음

                    HashMap<String, String> map = new HashMap<String, String>();

                    map.put("TRAVEL_NO", no);
                    map.put("TRAVEL_TITLE", title);
                    map.put("name", name);
                    map.put("WRITE_DATE", write_date);
                    map.put("HIT_COUNT", hit);
                    map.put("RECOMMEND", like);
                    map.put("USER_ID", user);
                    map.put("TRAVEL_COUR_CD", country);

                    oslist.add(map);
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        ListAdapter adapter = new SimpleAdapter(getActivity(), oslist,
                                R.layout.plan_board_item_row,
                                new String[]{"TRAVEL_NO", "TRAVEL_TITLE", "name", "WRITE_DATE", "HIT_COUNT", "RECOMMEND", "TRAVEL_COUR_CD", "USER_ID"}, new int[]{
                                R.id.txtNum, R.id.txtTitle, R.id.txtName, R.id.txtDate, R.id.txtLook, R.id.txtLike, R.id.txtNation});
                        list_plan_board.setAdapter(adapter);
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
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Join Error.");
                builder.setMessage("검색이 불가능합니다.")
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