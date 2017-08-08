package com.sungkyul.project_lte.Community;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
// 여행 후기 글 작성 페이지
public class Trip_Board_Write extends ImageSelectHelperActivity implements View.OnClickListener{
    Button btnTripCancel, btn_addmdl, btn_Write, btnSetCountry, buttonimage; // 글 작성 취소, 모듈 작성, 글 작성, 국가 선택 버튼, 이미지선택 버튼
    EditText edt_explain, edt_place, edt_content, edt_title; // 장소 설명 입력, 장소 입력, 글 내용 입력, 글 제목 입력
    private ListView list_module; // 여행 후기 모듈 리스트 뷰
    private EditText fromDateEtxt; // 여행 시작 날짜
    private EditText toDateEtxt; // 여행 마지막 날짜

    private DatePickerDialog fromDatePickerDialog; // 여행 시작 날짜 선택
    private DatePickerDialog toDatePickerDialog; // 여행 마지막 날짜 선택

    private SimpleDateFormat dateFormatter;

    int count = 0;
    String tag1 = "insModule", res, tag2 = "updateTrip", email, trip_no, start, end, countryCode, no, tag3 = "delModule", countryname;
    Intent i;
    HttpClient httpclient;
    HttpPost httppost;
    HttpResponse response;
    List<NameValuePair> nameValuePairs; // php로 post할 변수들을 담은 배열
    ProgressDialog dialog = null; // 프로그래스 바 초기화
    List<Trip_Edit_Module_Item> list = new ArrayList<Trip_Edit_Module_Item>(); // 화면에 표시할 리스트 뷰에 나타낼 객체들을 담을 배열
    ListAdapter adapter = null; // 리스트 연결 어댑터

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_trip_board);

        list_module = (ListView)findViewById(R.id.list_Module);

        RbPreference pref = new RbPreference(this); // RbPreference 생성
        email = pref.getValue("email", null); // null값을 해당 email에 할당
        trip_no = pref.getValue("trip_write_no", null); // null값을 해당 trip_write_no에 할당

        View header = getLayoutInflater().inflate(R.layout.activity_write_trip_board_header, null, false);
        View footer = getLayoutInflater().inflate(R.layout.activity_write_trip_board_footer, null, false);

        list_module.addHeaderView(header); // 여행 후기 작성 모듈 List에 header 연결
        list_module.addFooterView(footer); // 여행 후기 작성 모듈 List에 footer 연결

        //btnComeCash = (Button)findViewById(R.id.btn_ComeCash);

        buttonimage = (Button)footer.findViewById(R.id.button1);
        btn_Write = (Button)footer.findViewById(R.id.btn_Write);
        btnTripCancel = (Button)footer.findViewById(R.id.btn_TripCancel);
        edt_explain = (EditText)footer.findViewById(R.id.edt_explain);
        edt_place = (EditText)footer.findViewById(R.id.edt_place);
        btn_addmdl = (Button)footer.findViewById(R.id.btn_addmdl);
        edt_content = (EditText)header.findViewById(R.id.edt_content);
        edt_title = (EditText)header.findViewById(R.id.edt_title);
        btnSetCountry = (Button)header.findViewById(R.id.btn_SetCountry);
        final Intent intent = new Intent(getApplicationContext(), CountrycodeActivity.class);

        firstmodule(); // 모듈 작성 예제 보여주는 메소드 호출

        btnSetCountry.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivityForResult(intent, 1);
            }
        });


        buttonimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //setImageSizeBoundary(400); // optional. default is 500.
                //setCropOption(1, 1);  // optional. default is no crop.
                //setCustomButtons(btnGallery, btnCamera, btnCancel); // you can set these buttons.
                startSelectImage();
            }
        });


        getSelectedImageFile(); // extract selected & saved image file.

        dateFormatter = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);

        fromDateEtxt = (EditText) header.findViewById(R.id.edt_start);
        fromDateEtxt.setInputType(InputType.TYPE_NULL);
        fromDateEtxt.requestFocus();

        toDateEtxt = (EditText) header.findViewById(R.id.edt_end);
        toDateEtxt.setInputType(InputType.TYPE_NULL);

        setDateTimeField(); // 여행 기간 설정하는 메소드 호출



        btnTripCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog diaBox = new AlertDialog.Builder(Trip_Board_Write.this)
                        .setTitle("작성 취소")
                        .setMessage("작성을 취소하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                Intent intent_insert_trip = new Intent(getApplicationContext(), Community_Main.class);
                                startActivity(intent_insert_trip);
                                finish();
                            }
                        })
                        .setNegativeButton("아니오", null)
                        .create();
                diaBox.show();
            }
        });
        btn_addmdl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = ProgressDialog.show(Trip_Board_Write.this, "", "잠시만 기다려주세요...", true);
                new Thread(new Runnable() {
                    public void run() {
                        insmodule(); // 모듈 추가하는 메소드
                    }
                }).start();
            }
        });

        btn_Write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("국가설정".equals(btnSetCountry.getText().toString())) {
                    Toast.makeText(Trip_Board_Write.this, "국가를 설정해주세요", Toast.LENGTH_SHORT).show();
                } else if (edt_title.getText().toString().isEmpty()) {
                    Toast.makeText(Trip_Board_Write.this, "제목을 입력해주세요", Toast.LENGTH_SHORT).show();
                } else if (edt_content.getText().toString().isEmpty()) {
                    Toast.makeText(Trip_Board_Write.this, "내용을 입력해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    if (count != 0)
                    {
                        dialog = ProgressDialog.show(Trip_Board_Write.this, "", "잠시만 기다려주세요...", true);
                        new Thread(new Runnable() {
                            public void run() {
                                updatetrip(); // 여행 후기 글 작성 완료하는 메소드
                            }
                        }).start();
                    } else
                    {
                        Toast.makeText(Trip_Board_Write.this, "모듈을 작성하지 않았습니다.", Toast.LENGTH_SHORT).show();
                    }
                }

            }

        });

        list_module.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                no = list.get(position - 1).getNo();
                AlertDialog diaBox = new AlertDialog.Builder(Trip_Board_Write.this)
                        .setTitle("삭제")
                        .setMessage("해당 모듈을 삭제하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                try {
                                    new Thread(new Runnable() {
                                        public void run() {
                                            list.remove(position - 1);
                                            list_module.clearChoices();
                                            delmodule(); // 글 모듈 삭제하는 메소드
                                        }
                                    }).start();
                                } catch (Exception e) {
                                    Toast.makeText(Trip_Board_Write.this, "모듈을 담을 수 없습니다.", Toast.LENGTH_SHORT).show();
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            countryCode = data.getStringExtra(CountrycodeActivity.RESULT_CONTRYCODE);
            countryname = data.getStringExtra(CountrycodeActivity.countryname);
            btnSetCountry.setText(countryCode);
            //Toast.makeText(getActivity(), "You selected countrycode: " + countryCode, Toast.LENGTH_LONG).show();
        }
    }
    // 여행 기간 설정하는 메소드
    private void setDateTimeField() {
        fromDateEtxt.setOnClickListener(this);
        toDateEtxt.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fromDateEtxt.setText(dateFormatter.format(newDate.getTime()));
                start = dateFormatter.format(newDate.getTime());
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                toDateEtxt.setText(dateFormatter.format(newDate.getTime()));
                end = dateFormatter.format(newDate.getTime());
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onClick(View view) {
        if(view == fromDateEtxt) {
            fromDatePickerDialog.show();
        } else if(view == toDateEtxt) {
            toDatePickerDialog.show();
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
            nameValuePairs.add(new BasicNameValuePair("tag", tag3));
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
                        Toast.makeText(Trip_Board_Write.this, "삭제 완료", Toast.LENGTH_SHORT).show();
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
    // 모듈 추가하는 메소드
    void insmodule() {
        try {
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost("http://211.221.245.121/LTE/service_user.php"); // http 통신 서버 주소
            // 데이터 추가
            nameValuePairs = new ArrayList<NameValuePair>(3);
            // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
            nameValuePairs.add(new BasicNameValuePair("tag", tag1));
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
                    String no = jobj.getString("MODULE_NO"); // MODULE_NO에 대한 객체 생성
                    String place = jobj.getString("PLACE"); // PLACE에 대한 객체 생성
                    String con = jobj.getString("CONTENT"); // CONTENT에 대한 객체 생성

                    Trip_Edit_Module_Item trip_edit_module_item = new Trip_Edit_Module_Item();
                    trip_edit_module_item.place = place;
                    trip_edit_module_item.content = con;
                    trip_edit_module_item.no = no;

                    list.add(trip_edit_module_item);
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        comadapter(); // ListView에 모듈 List 연결하는 메소드

                        edt_place.setText(null);
                        edt_explain.setText(null);

                        Toast.makeText(Trip_Board_Write.this, "추가 완료", Toast.LENGTH_SHORT).show();
                        count = count + 1;
                    }
                });
            } else {
                showAlert(); // 오류 다이얼로그 메소드 호출
            }
        } catch (Exception e) {
            dialog.dismiss(); // 프로그래스 바 사라짐
            e.printStackTrace();
            //System.out.println("Exception : " + e.getMessage());
        }
    }
    // 모듈 작성 예제 보여주는 메소드
    void firstmodule() {
        Trip_Edit_Module_Item trip_edit_module_item = new Trip_Edit_Module_Item();
        String place = "장소를 입력하는 부분";
        String con = "설명을 입력하는 부분";

        trip_edit_module_item.place = place;
        trip_edit_module_item.content = con;

        list.add(trip_edit_module_item);

        adapter = new Trip_Edit_Module_Adapter(Trip_Board_Write.this, R.layout.edit_trip_module_item_row, list);
        list_module.setAdapter(adapter);
    }
    // ListView에 모듈 List 연결하는 메소드
    void comadapter() {
        adapter = new Trip_Edit_Module_Adapter(Trip_Board_Write.this, R.layout.edit_trip_module_item_row, list);
        list_module.setAdapter(adapter);
    }
    // 글 작성 완료하는 메소드
    void updatetrip() {
        try {
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost("http://211.221.245.121/LTE/service_user.php"); // http 통신 서버 주소
            // 데이터 추가
            nameValuePairs = new ArrayList<NameValuePair>(5);
            // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
            nameValuePairs.add(new BasicNameValuePair("tag", tag2));
            nameValuePairs.add(new BasicNameValuePair("trip_no", trip_no));
            nameValuePairs.add(new BasicNameValuePair("trip_title", edt_title.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("start_date", start));
            nameValuePairs.add(new BasicNameValuePair("end_date", end));  // $Edittext_value = $_POST['Edittext_value'];
            nameValuePairs.add(new BasicNameValuePair("content", edt_content.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("iso", countryname));
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
                pref.put("trip_write_no", null); // 해당 trip_write_no에 null값 저장
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(Trip_Board_Write.this, "글이 등록되었습니다.", Toast.LENGTH_SHORT).show();
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
        Trip_Board_Write.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(Trip_Board_Write.this);
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

    /*void comadapter() {
        adapter = new SimpleAdapter(Trip_Board_Write.this, oslist,
                R.layout.module_item_row,
                new String[]{"PLACE", "CONTENT", "COMMENT_NO"}, new int[]{
                R.id.txtPlace, R.id.txtContent});
        list_module.setAdapter(adapter);
    }*/

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            //하드웨어 뒤로가기 버튼에 따른 이벤트 설정
            case KeyEvent.KEYCODE_BACK:
                Intent intent_community = new Intent(getApplicationContext(), Community_Main.class);
                startActivity(intent_community);
                finish();
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
