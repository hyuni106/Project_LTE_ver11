package com.sungkyul.project_lte.flight;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.TextView;

/*
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
*/
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.sungkyul.project_lte.R;

/**
 * Created by Hanna on 2016-09-25.
 */
public class Flight_Result extends AppCompatActivity {

    TextView result_airline, result_flight, flight_intime, flight_change_time, airport, gate, check_in, status;
    String airline, airportId, flightId, remark, scheduleTime, gatenumber, changeTime, chkinrange;
    String sel_airline, sel_airport, sel_time, sel_flight;
    String st_h, st_m, ct_h, ct_m;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_result);

        result_airline = (TextView) findViewById(R.id.result_airline);
        result_flight = (TextView) findViewById(R.id.result_flight);
        flight_intime = (TextView) findViewById(R.id.flight_intime);
        flight_change_time = (TextView) findViewById(R.id.flight_change_time);
        airport = (TextView) findViewById(R.id.airport);
        gate = (TextView) findViewById(R.id.gate);
        check_in = (TextView) findViewById(R.id.check_in);
        status = (TextView) findViewById(R.id.status);

        Intent i = getIntent();
        sel_airline = i.getStringExtra("airline");
        sel_airport = i.getStringExtra("airport");
        sel_time = i.getStringExtra("hour") +i.getStringExtra("minute");
        sel_flight = i.getStringExtra("flight");

        new Thread(new Runnable() {
            public void run() {
                search_flight();
            }
        }).start();

    }

    void search_flight() {
        try {
            StringBuilder urlBuilder = new StringBuilder("http://openapi.airport.kr/openapi/service/StatusOfPassengerDeparturesKR/getPassengerDeparturesKR"); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=hZS4IUl0EcSaKWhTOsolJ2TAFU4aZwHB66%2F3D9uyVzckl7o%2BHVG1keXWt5G1cvHzMlscia4A69XEzFa%2FtUBPyQ%3D%3D"); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("from_time","UTF-8") + "=" + URLEncoder.encode(sel_time, "UTF-8")); /*조회시간(부터)*/
            urlBuilder.append("&" + URLEncoder.encode("to_time","UTF-8") + "=" + URLEncoder.encode(sel_time, "UTF-8")); /*조회시간(까지)*/
            //urlBuilder.append("&" + URLEncoder.encode("airport","UTF-8") + "=" + URLEncoder.encode(sel_airport, "UTF-8")); /*출발지공항(IATA)*/
            urlBuilder.append("&" + URLEncoder.encode("flight_id","UTF-8") + "=" + URLEncoder.encode(sel_flight, "UTF-8")); /*편명*/
            urlBuilder.append("&" + URLEncoder.encode("airline","UTF-8") + "=" + URLEncoder.encode(sel_airline, "UTF-8")); /*항공사 (IATA)*/
            urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("999", "UTF-8")); /*검색건수*/
            urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지 번호*/
            URL url = new URL(urlBuilder.toString());
            InputStream is = url.openStream();  //url위치로 입력스트림 연결

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8"));  //inputstream 으로부터 xml 입력받기

            String tag;

            xpp.next();
            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        //buffer.append("start XML parsing...\n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        tag = xpp.getName();    //테그 이름 얻어오기

                        if (tag.equals("item")) ;// 첫번째 검색결과
                        else if (tag.equals("airline")) {
                            xpp.next();
                            airline = xpp.getText();
                        } else if (tag.equals("airport")) {
                            xpp.next();
                            airportId = xpp.getText();
                            //airport.setText(xpp.getText());
                        } else if (tag.equals("flightId")) {
                            xpp.next();
                            flightId = xpp.getText();
                            //result_flight.setText(xpp.getText());
                        } else if (tag.equals("remark")) {
                            xpp.next();
                            remark = xpp.getText();
                            //status.setText(xpp.getText());
                        } else if (tag.equals("scheduleDateTime")) {
                            xpp.next();
                            scheduleTime = xpp.getText();
                            st_h = scheduleTime.substring(0, 2);
                            st_m = scheduleTime.substring(2);
                            scheduleTime = st_h + " : " + st_m;
                            //flight_intime.setText(xpp.getText());
                        } else if(tag.equals("gatenumber")) {
                            xpp.next();
                            gatenumber = xpp.getText();
                        } else if(tag.equals("estimatedDateTime")){
                            xpp.next();
                            changeTime = xpp.getText();
                            ct_h = changeTime.substring(0, 2);
                            ct_m = changeTime.substring(2);
                            changeTime = ct_h + " : " + ct_m;
                        } else if(tag.equals("chkinrange")) {
                            xpp.next();
                            chkinrange = xpp.getText();
                        }

                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        break;
                }

                runOnUiThread(new Runnable() {
                    public void run() {
                        result_airline.setText(airline);
                        airport.setText(airportId);
                        result_flight.setText(flightId);
                        flight_intime.setText(scheduleTime);
                        gate.setText(gatenumber);
                        flight_change_time.setText(changeTime);
                        check_in.setText(chkinrange);
                        if(remark != null) {
                            status.setText(remark);
                        } else {
                            status.setText("탑승 시간이 아닙니다.");
                        }
                    }
                });

                eventType = xpp.next();
            }
        } catch (Exception e) {
            System.out.println("Exception : " + e.getMessage());
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            //하드웨어 뒤로가기 버튼에 따른 이벤트 설정
            case KeyEvent.KEYCODE_BACK:
                Intent intent_main = new Intent(Flight_Result.this, FlightActivity.class);
                startActivity(intent_main);
                finish();
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
