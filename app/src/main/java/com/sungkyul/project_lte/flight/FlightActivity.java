package com.sungkyul.project_lte.flight;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.sungkyul.project_lte.Calculator.CalculatorActivity;
import com.sungkyul.project_lte.Community.Community_Main;
import com.sungkyul.project_lte.MainNLogin.Main;
import com.sungkyul.project_lte.R;
import com.sungkyul.project_lte.Etc.RbPreference;
import com.sungkyul.project_lte.SOS.SOSMain;

/**
 * Created by Hanna on 2016-09-25.
 */
public class FlightActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText flight;
    Spinner spin_airport, spin_airline, spin_hour, spin_minute;
    Button search_flight;
    String airportId, airlineId, sel_hour, sel_minute;
    String[] hour, minute;
    RbPreference pref = new RbPreference(this); // 값 가져오기 위한 RbPreference class 생성

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight);

        spin_airport = (Spinner) findViewById(R.id.spin_airport);
        flight = (EditText) findViewById(R.id.flight);
        spin_airline = (Spinner) findViewById(R.id.spin_airline);
        search_flight = (Button) findViewById(R.id.search_flight);
        spin_hour = (Spinner) findViewById(R.id.hour);
        spin_minute = (Spinner) findViewById(R.id.minute);

        final String[] airline = getResources().getStringArray(R.array.airline);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, airline);
        spin_airline.setAdapter(adapter);

        String[] airport = getResources().getStringArray(R.array.airport);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, airport);
        spin_airport.setAdapter(adapter1);

        hour = getResources().getStringArray(R.array.hour);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, hour);
        spin_hour.setAdapter(adapter2);

        minute = getResources().getStringArray(R.array.minute);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, minute);
        spin_minute.setAdapter(adapter3);

        spin_airport.setOnItemSelectedListener(this);
        spin_airline.setOnItemSelectedListener(this);
        spin_hour.setOnItemSelectedListener(this);
        spin_minute.setOnItemSelectedListener(this);

        search_flight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Flight_Result.class);
                intent.putExtra("airline", airlineId);
                intent.putExtra("airport", airportId);
                intent.putExtra("hour", sel_hour);
                intent.putExtra("minute", sel_minute);
                intent.putExtra("flight", flight.getText().toString());
                startActivity(intent);
                finish();
            }
        });

    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        spin_airport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        airportId = "BKK";
                        break;
                    case 1:
                        airportId = "CAN";
                        break;
                    case 2:
                        airportId = "CJU";
                        break;
                    case 3:
                        airportId = "FCO";
                        break;
                    case 4:
                        airportId = "FUK";
                        break;
                    case 5:
                        airportId = "GMP";
                        break;
                    case 6:
                        airportId = "HND";
                        break;
                    case 7:
                        airportId = "HKG";
                        break;
                    case 8:
                        airportId = "HRB";
                        break;
                    case 9:
                        airportId = "IAD";
                        break;
                    case 10:
                        airportId = "JFK";
                        break;
                    case 11:
                        airportId = "KHH";
                        break;
                    case 12:
                        airportId = "KIX";
                        break;
                    case 13:
                        airportId = "KKJ";
                        break;
                    case 14:
                        airportId = "NRT";
                        break;
                    case 15:
                        airportId = "LHR";
                        break;
                    case 16:
                        airportId = "PEK";
                        break;
                    case 17:
                        airportId = "PUS";
                        break;
                    case 18:
                        airportId = "PVG";
                        break;
                    case 19:
                        airportId = "SIN";
                        break;
                    case 20:
                        airportId = "TAE";
                        break;
                    case 21:
                        airportId = "TPE";
                        break;
                    case 22:
                        airportId = "ICN";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spin_airline.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        airlineId = "7C";
                        break;
                    case 1:
                        airlineId = "BA";
                        break;
                    case 2:
                        airlineId = "BR";
                        break;
                    case 3:
                        airlineId = "CA";
                        break;
                    case 4:
                        airlineId = "CI";
                        break;
                    case 5:
                        airlineId = "FM";
                        break;
                    case 6:
                        airlineId = "JL";
                        break;
                    case 7:
                        airlineId = "KE";
                        break;
                    case 8:
                        airlineId = "KJ";
                        break;
                    case 9:
                        airlineId = "LD";
                        break;
                    case 10:
                        airlineId = "LJ";
                        break;
                    case 11:
                        airlineId = "MM";
                        break;
                    case 12:
                        airlineId = "MU";
                        break;
                    case 13:
                        airlineId = "OZ";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spin_hour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sel_hour = String.valueOf(spin_hour.getSelectedItem());
                Toast.makeText(FlightActivity.this, "선택 값 = " + sel_hour, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                sel_hour = "00";
            }
        });

        spin_minute.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sel_minute = String.valueOf(spin_minute.getSelectedItem());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                sel_minute = "00";
            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        airportId = "";
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            //하드웨어 뒤로가기 버튼에 따른 이벤트 설정
            case KeyEvent.KEYCODE_BACK:
                String activity_no = pref.getValue("activity", null);
                if(activity_no.equals("1")) {
                    Intent send = new Intent(FlightActivity.this, CalculatorActivity.class);
                    startActivity(send);
                    finish();
                } else if(activity_no.equals("2")) {
                    Intent send = new Intent(FlightActivity.this, SOSMain.class);
                    startActivity(send);
                    finish();
                } else if(activity_no.equals("3")) {
                    Intent send = new Intent(FlightActivity.this, Community_Main.class);
                    startActivity(send);
                    finish();
                } else {
                    Intent send = new Intent(FlightActivity.this, Main.class);
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
