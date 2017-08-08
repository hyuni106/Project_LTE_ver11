package com.sungkyul.project_lte.MyPage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sungkyul.project_lte.Calculator.CalculatorActivity;
import com.sungkyul.project_lte.Community.Community_Main;
import com.sungkyul.project_lte.MainNLogin.Main;
import com.sungkyul.project_lte.R;
import com.sungkyul.project_lte.Etc.RbPreference;
import com.sungkyul.project_lte.SOS.SOSMain;

/**
 * Created by Hanna on 2016-04-21.
 */
// 마이페이지
public class My_Page extends AppCompatActivity {
    Button btnSetsos, btnChangePW, btnChangeNick, btn_UserOut,btn_MyContent,btn_MyPlan, btn_Help;
    // SOS설정, 비밀번호 변경, 닉네임 변경, 회원탈퇴, 내가 작성한 글, 내가 세운 계획, 도움말 버튼
    TextView txt_userid;
    String email;
    RbPreference pref = new RbPreference(this); // 값 가져오기 위한 RbPreference class 생성

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_page);

        btnSetsos = (Button)findViewById(R.id.btn_SetSos);
        btn_MyContent = (Button)findViewById(R.id.btn_MyContent);
        btn_MyPlan= (Button)findViewById(R.id.btn_MyPlan);
        btnChangePW = (Button)findViewById(R.id.btn_ChangePW);
        btnChangeNick = (Button)findViewById(R.id.btn_ChangeNick);
        btn_UserOut = (Button)findViewById(R.id.btn_UserOut);
        btn_Help = (Button)findViewById(R.id.btn_Help);
        txt_userid = (TextView)findViewById(R.id.txt_userid);

        email = pref.getValue("email", null);
        txt_userid.setText(email);

        btnChangeNick.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent_changeNick = new Intent(getApplicationContext(), Change_NickName.class);
                startActivity(intent_changeNick);
                finish();
            }
        });
        btn_MyContent.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent_myContent = new Intent(getApplicationContext(), MyContentList.class);
                startActivity(intent_myContent);
                finish();
            }
        });
        btn_MyPlan.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent_myPlan = new Intent(getApplicationContext(), MyPlanList.class);
                startActivity(intent_myPlan);
                finish();
            }
        });
        btnSetsos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_setsos = new Intent(getApplicationContext(), Set_SOS.class);
                startActivity(intent_setsos);
                finish();
            }
        });

        btnChangePW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_changePW = new Intent(getApplicationContext(), Change_Password.class);
                startActivity(intent_changePW);
                finish();
            }
        });

        btn_UserOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_dropUser = new Intent(getApplicationContext(), Drop_User.class);
                startActivity(intent_dropUser);
                finish();
            }
        });

        btn_Help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_help = new Intent(getApplicationContext(), Help.class);
                startActivity(intent_help);
                finish();
            }
        });

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            //하드웨어 뒤로가기 버튼에 따른 이벤트 설정
            case KeyEvent.KEYCODE_BACK:
                String activity_no = pref.getValue("activity", null);
                if(activity_no.equals("1")) {
                    Intent send = new Intent(My_Page.this, CalculatorActivity.class);
                    startActivity(send);
                    finish();
                } else if(activity_no.equals("2")) {
                    Intent send = new Intent(My_Page.this, SOSMain.class);
                    startActivity(send);
                    finish();
                } else if(activity_no.equals("3")) {
                    Intent send = new Intent(My_Page.this, Community_Main.class);
                    startActivity(send);
                    finish();
                } else {
                    Intent send = new Intent(My_Page.this, Main.class);
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
