package com.sungkyul.project_lte.MyPage;

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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.sungkyul.project_lte.Community.Free_Board_Read;
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

/**
 * 나의 자유게시판 - 완료
 * Created by WonHo on 2016-05-23.
 */
// 내가 쓴 자유게시판 List
public class MyFreeBoardList extends Fragment {
    private ListView list_my_free_board;  // 내가 작성한 자유게시판 리스트 뷰

    String tag = "mylistFree", res, success, email;
    HttpClient httpclient;
    HttpPost httppost;
    HttpResponse response;
    List<NameValuePair> nameValuePairs; // php로 post할 변수들을 담은 배열
    ProgressDialog dialog = null; // 프로그래스 바 초기화
    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>(); // 화면에 표시할 리스트 뷰에 나타낼 객체들을 담을 배열

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_my_free_board_list, container, false);  // View 선언 및 레이아웃 연동

        list_my_free_board = (ListView)view.findViewById(R.id.list_MyFree);     // 리스트뷰 선언

        RbPreference pref = new RbPreference(getActivity()); // RbPreference 생성
        email = pref.getValue("email", null); // null값을 email에 할당
        oslist = new ArrayList<HashMap<String, String>>();

        dialog = ProgressDialog.show(getActivity(), "", "로딩중...", true);
        new Thread(new Runnable() {
            public void run() {
                setmyfreelist(); // 내가 작성한 자유게시판 List 출력하는 메소드 호출
            }
        }).start();

        list_my_free_board.setOnItemClickListener(MyFreeBoardRead);  // 자유게시글에 해당 글 클릭 이벤트 처리
        return view;        // View 반환

    }

    /***
     * 자유게시글에 해당 글 클릭 이벤트 처리
     */
    private AdapterView.OnItemClickListener MyFreeBoardRead = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String no = oslist.get(position).get("FREE_NO");
            Intent intent_read_free = new Intent(getActivity(),Free_Board_Read.class);
            intent_read_free.putExtra("no", no);
            startActivity(intent_read_free);
            getActivity().finish();                // 현 액티비티 종료
        }
    };
    // 내가 작성한 자유게시판 List 출력하는 메소드
    void setmyfreelist() {
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
                String a = json.getString("free"); // json 배열 생성
                JSONArray json1 = new JSONArray(a);
                for(int i=0; i<json1.length(); i++) {
                    JSONObject jobj = json1.getJSONObject(i);

                    String no = jobj.getString("FREE_NO"); // FREE_NO에 대한 객체 받음
                    String title = jobj.getString("FREE_TITLE"); // FREE_TITLE에 대한 객체 받음
                    String user = jobj.getString("name"); // USER_ID에 대한 객체 받음
                    String write_date = jobj.getString("WRITE_DATE"); // WRITE_DATE에 대한 객체 받음
                    String hit = jobj.getString("HIT_COUNT"); // HIT_COUNT에 대한 객체 받음

                    HashMap<String, String> map = new HashMap<String, String>();

                    map.put("FREE_NO", no);
                    map.put("FREE_TITLE", title);
                    map.put("name", user);
                    map.put("WRITE_DATE", write_date);
                    map.put("HIT_COUNT", hit);

                    oslist.add(map);
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        ListAdapter adapter = new SimpleAdapter(getActivity(), oslist,
                                R.layout.free_board_item_row,
                                new String[] { "FREE_NO","FREE_TITLE", "name", "WRITE_DATE", "HIT_COUNT" }, new int[] {
                                R.id.txtNum,R.id.txtTitle, R.id.txtName, R.id.txtDate, R.id.txtLook});
                        list_my_free_board.setAdapter(adapter);
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
}
