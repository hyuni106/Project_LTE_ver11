package com.sungkyul.project_lte.MyPage;

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
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.sungkyul.project_lte.Calculator.CalculatorActivity;
import com.sungkyul.project_lte.Community.Community_Main;
import com.sungkyul.project_lte.Community.Trip_Read_Module_Adapter;
import com.sungkyul.project_lte.Community.Trip_Read_Module_Item;
import com.sungkyul.project_lte.MainNLogin.Main;
import com.sungkyul.project_lte.Community.Plan_Board_Write;
import com.sungkyul.project_lte.R;
import com.sungkyul.project_lte.Etc.RbPreference;
import com.sungkyul.project_lte.SOS.SOSMain;

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

/**
 * Created by Hanna on 2016-05-26.
 */
// 여행 카트 List 페이지
public class CartActivity extends AppCompatActivity {
    private ListView cart_module; // 카트 모듈 보여주는 리스트뷰
    Button btn_CartSave; // 여행 계획 작성 페이지로 이동하는 버튼
    TextView txt_country, txt_nomod; // 카트에 담긴 모듈들의 국가 표시, 모듈이 없을 시 나타나는 문자

    String tag="setCart", email, res, tag2 = "insPlan", code = "b", no, name, firstcountry, tag3 = "delModuleCart", no_cart = "0";
    HttpClient httpclient;
    HttpPost httppost;
    HttpResponse response;
    List<NameValuePair> nameValuePairs; // php로 post할 변수들을 담은 배열
    ProgressDialog dialog = null; // 프로그래스 바 초기화
    ListAdapter adapter = null;
    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>(); // 화면에 표시할 리스트 뷰에 나타낼 객체들을 담을 배열
    RbPreference pref = new RbPreference(this); // RbPreference 생성
    List<Cart_Module_Item> list = new ArrayList<Cart_Module_Item>(); // 화면에 표시할 리스트뷰에 나타낼 객체들을 담을 리스트

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cart_module = (ListView) findViewById(R.id.list_CartModule);
        txt_nomod = (TextView)findViewById(R.id.txt_nomod);

        email = pref.getValue("email", null); // null값을 email에 할당
        name = pref.getValue("name", null); // null값을 name에 할당

        View header = getLayoutInflater().inflate(R.layout.activity_cart_header, null, false);
        View footer = getLayoutInflater().inflate(R.layout.activity_cart_footer, null, false);

        cart_module.addHeaderView(header); // 여행 카트 리스트에 header 연결
        cart_module.addFooterView(footer); // 여행 카트 리스트에 footer 연결

        btn_CartSave = (Button) footer.findViewById(R.id.btn_CartSave);
        txt_country = (TextView)header.findViewById(R.id.txt_country);
        btn_CartSave.setFocusable(false);

        dialog = ProgressDialog.show(CartActivity.this, "", "잠시만 기다려주세요...", true);
        new Thread(new Runnable() {
            public void run() {
                setcart(); // 여행 카트 모듈 List 출력하는 메소드
            }
        }).start();

        btn_CartSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (no_cart.equals("1")) {
                    Toast.makeText(CartActivity.this, "모듈을 담은 후 이용해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    dialog = ProgressDialog.show(CartActivity.this, "", "로딩중...", true);
                    new Thread(new Runnable() {
                        public void run() {
                            writeplan(); // 여행 카트에 담아놨던 모듈을 여행 계획 작성페이지로 보내는 메소드 호출
                        }
                    }).start();
                }
            }
        });

        // 모듈 롱 클릭
        cart_module.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                no = list.get(position - 1).getNo();
                AlertDialog diaBox = new AlertDialog.Builder(CartActivity.this)
                        .setTitle("삭제")
                        .setMessage("해당 모듈을 삭제하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                try {
                                    new Thread(new Runnable() {
                                        public void run() {
                                            delmodule(); // 카트에서 모듈 삭제하는 메소드 호출
                                        }
                                    }).start();
                                } catch (Exception e) {
                                    Toast.makeText(CartActivity.this, "모듈을 지울 수 없습니다. 잠시 후 시도해주세요.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("아니오", null)
                        .create();
                diaBox.show();
                return false;
            }
        });
        /*cart_module.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                no = oslist.get(position-1).get("CART_NO");
                AlertDialog diaBox = new AlertDialog.Builder(CartActivity.this)
                        .setTitle("삭제")
                        .setMessage("해당 모듈을 삭제하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                try {
                                    new Thread(new Runnable() {
                                        public void run() {
                                            list.get(position - 1).getNo();
                                            cart_module.clearChoices();
                                            delmodule(); // 여행 카트 모듈 삭제하는 메소드 호출
                                        }
                                    }).start();
                                } catch (Exception e) {
                                    Toast.makeText(CartActivity.this, "모듈을 지울 수 없습니다. 잠시 후 시도해주세요.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("아니오", null)
                        .create();
                diaBox.show();
                return false;
            }
        });*/
        //btn_CartSave.setOnClickListener(mClickListener);
    }

    // 여행카트 ListView에 모듈 List연결해주는 메소드
    void comadapter() {
        adapter = new Cart_Module_Adapter(CartActivity.this, R.layout.module_item_row, list);
        cart_module.setAdapter(adapter);
    }
    // 여행 카트 모듈 삭제하는 메소드
    void delmodule() {
        try {
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost("http://211.221.245.121/LTE/service_user.php"); // http 통신 서버 주소
            // 데이터 추가
            nameValuePairs = new ArrayList<NameValuePair>(1);
            // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
            nameValuePairs.add(new BasicNameValuePair("tag", tag3));
            nameValuePairs.add(new BasicNameValuePair("cart_no", no));
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
                        Toast.makeText(CartActivity.this, "삭제 완료", Toast.LENGTH_SHORT).show();
                        comadapter(); // 여행카트 ListView에 모듈 List연결해주는 메소드
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
    // 여행 카트에 담아놨던 모듈을 여행 계획 작성페이지로 보내는 메소드
    void writeplan() {
        try {
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost("http://211.221.245.121/LTE/service_user.php"); // http 통신 서버 주소
            // 데이터 추가
            nameValuePairs = new ArrayList<NameValuePair>(4);
            // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
            nameValuePairs.add(new BasicNameValuePair("tag", tag2));
            nameValuePairs.add(new BasicNameValuePair("user_id", email));
            nameValuePairs.add(new BasicNameValuePair("name", name));
            nameValuePairs.add(new BasicNameValuePair("code", code));
            nameValuePairs.add(new BasicNameValuePair("country", firstcountry));
            httppost.setHeader("Content-type", "application/x-www-form-urlencoded; charset=utf-8");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));
            //Execute HTTP Post Request

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
            String a = json.getString("plan"); // json 배열 생성
            JSONArray json1 = new JSONArray(a);
            JSONObject jobj = json1.getJSONObject(0);
            no = jobj.getString("TRAVEL_NO"); // TRAVEL_NO에 대한 객체 받음
            RbPreference pref = new RbPreference(this); // RbPreference 생성
            pref.put("plan_write_no", no); // 해당 plan_write_no 값에 no 저장
            if (res.equals("1")) {
                Intent intent = new Intent(getApplicationContext(), Plan_Board_Write.class);
                intent.putExtra("country", firstcountry);
                startActivity(intent);
                finish();
            } else {
                showAlert(); // 오류 다이얼로그 메소드 호출
            }
        } catch (Exception e) {
            dialog.dismiss(); // 프로그래스 바 사라짐
            System.out.println("Exception : " + e.getMessage());
        }
    }
    // 여행 카트 모듈 List 출력하는 메소드
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
            //Execute HTTP Post Request

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
            String error = json.getString("error");

            if (success.equals("1")) {
                String a = json.getString("cart"); // json 배열 생성
                JSONArray json1 = new JSONArray(a);
                JSONObject country = json1.getJSONObject(0);
                firstcountry = country.getString("MOD_COUR_CD"); // MOD_COUR_CD에 대한 객체 받음
                for (int i = 0; i < json1.length(); i++) {
                    JSONObject jobj = json1.getJSONObject(i);
                    String no = jobj.getString("CART_NO"); // CART_NO에 대한 객체 받음
                    String place = jobj.getString("PLACE"); // PLACE에 대한 객체 받음
                    String con = jobj.getString("CONTENT"); // CONTENT에 대한 객체 받음

                    Cart_Module_Item cart_module_item = new Cart_Module_Item();
                    cart_module_item.place = place;
                    cart_module_item.content = con;
                    cart_module_item.position = no;

                    list.add(cart_module_item);
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        txt_country.setText(firstcountry);
                        comadapter(); // 여행카트 ListView에 모듈 List 연결해주는 메소드
                    }
                });
            } else if (error.equals("1")) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(CartActivity.this, "모듈이 없습니다.", Toast.LENGTH_SHORT).show();
                        no_cart = "1";
                        /*cart_module.setVisibility(View.GONE);
                        txt_nomod.setVisibility(View.VISIBLE);*/
                        comadapter();
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
        CartActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
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
                String activity_no = pref.getValue("activity", null);
                if(activity_no.equals("1")) {
                    Intent send = new Intent(CartActivity.this, CalculatorActivity.class);
                    startActivity(send);
                    finish();
                } else if(activity_no.equals("2")) {
                    Intent send = new Intent(CartActivity.this, SOSMain.class);
                    startActivity(send);
                    finish();
                } else if(activity_no.equals("3")) {
                    Intent send = new Intent(CartActivity.this, Community_Main.class);
                    startActivity(send);
                    finish();
                } else {
                    Intent send = new Intent(CartActivity.this, Main.class);
                    startActivity(send);
                    finish();
                }
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
