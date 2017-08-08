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
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sungkyul.project_lte.Etc.RbPreference;
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
// 여행 후기 글 수정 페이지
public class Trip_Board_Edit extends AppCompatActivity {
    Button btn_edit_tripok, btn_edit_tripcancel, btn_addmdl; // 글 수정 완료, 글 수정 취소, 모듈 추가 버튼
    EditText edt_place, edt_explain, edt_title, edt_content; // 장소, 장소설명, 글 제목, 일반글 EditText
    TextView txt_cour_cd; // 해당 글의 설정 국가 출력 TextView
    private ListView trip_edit_module; // 여행 후기 모듈 리스트

    String tag3 = "insModuleEdit", res, trip_no, email, tag1 = "readTrip", tag2 = "listTripModule", cour_cd, no, tag4 = "delModule", place, content, limit, tag5 = "cancelTripEdit", tag6 = "editTrip";
    HttpClient httpclient;
    HttpPost httppost;
    HttpResponse response;
    List<NameValuePair> nameValuePairs; // php로 post할 변수들을 담은 배열
    ProgressDialog dialog = null; // 프로그래스 바 초기화
    ListAdapter adapter = null;
    List<Trip_Edit_Module_Item> list = new ArrayList<Trip_Edit_Module_Item>(); // 화면에 표시할 리스트 뷰에 나타낼 객체들을 담을 배열

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_trip_board);
        trip_edit_module = (ListView) findViewById(R.id.list_EditTripModule);

        Intent i = getIntent();
        trip_no = i.getStringExtra("no");

        RbPreference pref = new RbPreference(this); // RbPreference 생성
        email = pref.getValue("email", null); // null값을 해당 email을 할당

        View header = getLayoutInflater().inflate(R.layout.activity_edit_trip_board_header, null, false);
        View footer = getLayoutInflater().inflate(R.layout.activity_edit_trip_board_footer, null, false);

        trip_edit_module.addHeaderView(header); // 여행 후기 모듈 List에 header 연결
        trip_edit_module.addFooterView(footer); // 여행 후기 모듈 List에 footer 연결

        btn_edit_tripok = (Button) footer.findViewById(R.id.btn_EditTripOK);
        btn_edit_tripcancel = (Button) footer.findViewById(R.id.btn_EditTripCancel);
        btn_addmdl = (Button) footer.findViewById(R.id.btn_addmdl);
        edt_place = (EditText) footer.findViewById(R.id.edt_place);
        edt_explain = (EditText) footer.findViewById(R.id.edt_explain);
        edt_title = (EditText) header.findViewById(R.id.edt_title);
        edt_content = (EditText) header.findViewById(R.id.edt_content);
        txt_cour_cd = (TextView) header.findViewById(R.id.txt_cour_cd);

        dialog = ProgressDialog.show(Trip_Board_Edit.this, "", "잠시만 기다려주세요...", true);
        new Thread(new Runnable() {
            public void run() {
                settrip(); // 글 불러오는 메소드 호출
                setmodule(); // 모듈 불러오는 메소드 호출
            }
        }).start();

        btn_edit_tripok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = ProgressDialog.show(Trip_Board_Edit.this, "", "잠시만 기다려주세요...", true);
                new Thread(new Runnable() {
                    public void run() {
                        edittrip(); // 글 수정 완료하는 메소드 호출
                    }
                }).start();
            }
        });
        // 글 수정 취소 버튼 클릭
        btn_edit_tripcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog diaBox = new AlertDialog.Builder(Trip_Board_Edit.this)
                        .setTitle("수정 취소")
                        .setMessage("수정을 취소하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog = ProgressDialog.show(Trip_Board_Edit.this, "", "잠시만 기다려주세요...", true);
                                new Thread(new Runnable() {
                                    public void run() {
                                        cancel_edit(); // 수정시 작성 모듈 삭제
                                    }
                                }).start();
                            }
                        })
                        .setNegativeButton("아니오", null)
                        .create();
                diaBox.show();

                /*limit = Integer.toString(count);
                dialog = ProgressDialog.show(Trip_Board_Edit.this, "", "잠시만 기다려주세요...", true);
                new Thread(new Runnable() {
                    public void run() {
                        cancel_edit();
                    }
                }).start();*/
            }
        });
        btn_addmdl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = ProgressDialog.show(Trip_Board_Edit.this, "", "잠시만 기다려주세요...", true);
                new Thread(new Runnable() {
                    public void run() {
                        insmodule(); // 모듈 추가하는 메소드 호출
                    }
                }).start();
            }
        });
        // 모듈 롱클릭 시 해당 모듈 삭제 여부 확인
        trip_edit_module.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                no = list.get(position - 1).getNo();
                AlertDialog diaBox = new AlertDialog.Builder(Trip_Board_Edit.this)
                        .setTitle("삭제")
                        .setMessage("해당 모듈을 삭제하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                try {
                                    new Thread(new Runnable() {
                                        public void run() {
                                            list.remove(position - 1);
                                            trip_edit_module.clearChoices();
                                            delmodule(); // 모듈 삭제하는 메소드 호출
                                        }
                                    }).start();
                                } catch (Exception e) {
                                    Toast.makeText(Trip_Board_Edit.this, "모듈을 삭제할 수 없습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("아니오", null)
                        .create();
                diaBox.show();
                return false;
            }
        });
    }
    // ListView에 모듈 List 연결하는 메소드
    void comadapter() {
        adapter = new Trip_Edit_Module_Adapter(Trip_Board_Edit.this, R.layout.edit_trip_module_item_row, list);
        trip_edit_module.setAdapter(adapter);
    }
    // 글 수정 작성 취소 메소드
    void cancel_edit() {
        try {
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost("http://211.221.245.121/LTE/service_user.php"); // http 통신 서버 주소
            // 데이터 추가
            nameValuePairs = new ArrayList<NameValuePair>(1);
            // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
            nameValuePairs.add(new BasicNameValuePair("tag", tag5));
            nameValuePairs.add(new BasicNameValuePair("trip_no", trip_no));
            httppost.setHeader("Content-type", "application/x-www-form-urlencoded; charset=utf-8");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));
            // http 통신 요청 실행
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            res = httpclient.execute(httppost, responseHandler); // http 통신 응답
            System.out.println("Response : " + res);

            JSONObject json = new JSONObject(res); // json 객체 생성
            String success = json.getString("success");

            if (success.equals("1")) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        dialog.dismiss(); // 프로그래스 바 사라짐
                        Toast.makeText(Trip_Board_Edit.this, "작성이 취소되었습니다.", Toast.LENGTH_SHORT).show();
                        comadapter(); // ListView에 모듈 List 연결하는 메소드
                    }
                });
                Intent intent_edit_tripcancel = new Intent(getApplicationContext(), Trip_Board_Read.class);
                intent_edit_tripcancel.putExtra("no", trip_no);
                startActivity(intent_edit_tripcancel);
                finish();
            } else {
                showAlert(); // 오류 다이얼로그 메소드 호출
            }
        } catch (Exception e) {
            dialog.dismiss(); // 프로그래스 바 사라짐
            System.out.println("Exception : " + e.getMessage());
        }
    }
    // 글 수정 완료하는 메소드
    void edittrip() {
        try {
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost("http://211.221.245.121/LTE/service_user.php"); // http 서버 주소
            // 데이터 추가
            nameValuePairs = new ArrayList<NameValuePair>(5);
            // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
            nameValuePairs.add(new BasicNameValuePair("tag", tag6));
            nameValuePairs.add(new BasicNameValuePair("trip_no", trip_no));
            nameValuePairs.add(new BasicNameValuePair("trip_title", edt_title.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("content", edt_content.getText().toString()));
            httppost.setHeader("Content-type", "application/x-www-form-urlencoded; charset=utf-8");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));
            // http 통신 요청 실행
            // edited by James from coderzheaven.. from here....
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            res = httpclient.execute(httppost, responseHandler);
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
                pref.put("trip_write_no", null); // 해당 trip_write_no 값에 null 저장
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(Trip_Board_Edit.this, "글이 등록되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                Intent intent_edit_tripok = new Intent(getApplicationContext(), Trip_Board_Read.class);
                intent_edit_tripok.putExtra("no", trip_no);
                startActivity(intent_edit_tripok);
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
    // 모듈 삭제하는 메소드
    void delmodule() {
        try {
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost("http://211.221.245.121/LTE/service_user.php"); // http 통신 서버 주소
            // 데이터 추가
            nameValuePairs = new ArrayList<NameValuePair>(1);
            // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
            nameValuePairs.add(new BasicNameValuePair("tag", tag4));
            nameValuePairs.add(new BasicNameValuePair("module_no", no));
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
                        Toast.makeText(Trip_Board_Edit.this, "삭제 완료", Toast.LENGTH_SHORT).show();
                        comadapter(); // ListView에 모듈 List 연결하는 메소드
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
    // 작성되어있는 모듈 불러오는 메소드
    void setmodule() {
        try {
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost("http://211.221.245.121/LTE/service_user.php"); // http 통신 서버 주소
            // 데이터 추가
            nameValuePairs = new ArrayList<NameValuePair>(1);
            // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
            nameValuePairs.add(new BasicNameValuePair("tag", tag2));
            nameValuePairs.add(new BasicNameValuePair("trip_no", trip_no));
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
                String a = json.getString("trip"); // json 배열 생성
                JSONArray json1 = new JSONArray(a);
                for (int i = 0; i < json1.length(); i++) {
                    JSONObject jobj = json1.getJSONObject(i);
                    String no = jobj.getString("MODULE_NO"); // MODULE_NO에 대한 객체 받음
                    String place = jobj.getString("PLACE"); // PLACE에 대한 객체 받음
                    String con = jobj.getString("CONTENT"); // CONTENT에 대한 객체 받음

                    Trip_Edit_Module_Item trip_edit_module_item = new Trip_Edit_Module_Item();
                    trip_edit_module_item.place = place;
                    trip_edit_module_item.content = con;
                    trip_edit_module_item.no = no;

                    list.add(trip_edit_module_item);

                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        comadapter(); // ListView에 모듈 List 연결하는 메소드 호출
                    }
                });
            } else if (success.equals("0")) {
                Toast.makeText(Trip_Board_Edit.this, "댓글이 없습니다.", Toast.LENGTH_SHORT).show();
            } else {
                showAlert(); // 오류 다이얼로그 메소드 호출
            }
        } catch (Exception e) {
            dialog.dismiss(); // 프로그래스 바 사라짐
            System.out.println("Exception : " + e.getMessage());
        }
    }
    // 여행 후기 글 불러오는 메소드
    void settrip() {
        try {
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost("http://211.221.245.121/LTE/service_user.php"); // http 통신 서버 주소
            // 데이터 추가
            nameValuePairs = new ArrayList<NameValuePair>(1);
            // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
            nameValuePairs.add(new BasicNameValuePair("tag", tag1));
            nameValuePairs.add(new BasicNameValuePair("trip_no", trip_no));  // $Edittext_value = $_POST['Edittext_value'];
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
                String a = json.getString("trip"); // json 배열 생성
                JSONArray json1 = new JSONArray(a);
                JSONObject jobj = json1.getJSONObject(0);
                final String title = jobj.getString("TRAVEL_TITLE"); // TRAVEL_TITLE에 대한 객체 받음
                final String content = jobj.getString("CONTENT"); // CONTENT에 대한 객체 받음
                final String iso = jobj.getString("TRAVEL_COUR_CD"); // TRAVEL_COUR_CD에 대한 객체 받음
                cour_cd = jobj.getString("TRAVEL_COUR_CD"); // TRAVEL_COUR_CD에 대한 객체 받음
                runOnUiThread(new Runnable() {
                    public void run() {
                        edt_title.setText(title);
                        edt_content.setText(content);
                        txt_cour_cd.setText(iso);
                    }
                });
            } else if (res.equals("0")) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(Trip_Board_Edit.this, "글을 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                showAlert(); // 오류 다이얼로그 메소드 호출
            }
        } catch (Exception e) {
            System.out.println("Exception : " + e.getMessage());
        }
    }
    // 모듈 추가하는 메소드
    void insmodule() {
        try {
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost("http://211.221.245.121/LTE/service_user.php"); // http 통신 서버 주소
            // 데이터 추가
            nameValuePairs = new ArrayList<NameValuePair>(3);
            // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
            nameValuePairs.add(new BasicNameValuePair("tag", tag3));
            nameValuePairs.add(new BasicNameValuePair("trip_no", trip_no));
            nameValuePairs.add(new BasicNameValuePair("user_id", email));
            nameValuePairs.add(new BasicNameValuePair("place", edt_place.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("content", edt_explain.getText().toString())); // $Edittext_value = $_POST['Edittext_value'];
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
                for (int i = 0; i < json1.length(); i++) {
                    JSONObject jobj = json1.getJSONObject(i);
                    String no = jobj.getString("MODULE_NO"); // MODULE_NO에 대한 객체 받음
                    String place = jobj.getString("PLACE"); // PLACE에 대한 객체 받음
                    String con = jobj.getString("CONTENT"); // CONTENT에 대한 객체 받음

                    Trip_Edit_Module_Item trip_edit_module_item = new Trip_Edit_Module_Item();
                    trip_edit_module_item.place = place;
                    trip_edit_module_item.content = con;
                    trip_edit_module_item.no = no;

                    list.add(trip_edit_module_item);

                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        comadapter(); // ListView에 모듈 List 연결하는 메소드 호출

                        edt_place.setText(null);
                        edt_explain.setText(null);

                        Toast.makeText(Trip_Board_Edit.this, "추가 완료", Toast.LENGTH_SHORT).show();
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
    // 오류 다이얼로그 메소드
    public void showAlert() {
        Trip_Board_Edit.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(Trip_Board_Edit.this);
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
                Intent intent_community = new Intent(getApplicationContext(), Trip_Board_Read.class);
                intent_community.putExtra("no", trip_no);
                startActivity(intent_community);
                finish();
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
