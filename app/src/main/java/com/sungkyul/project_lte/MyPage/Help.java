package com.sungkyul.project_lte.MyPage;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;

import com.sungkyul.project_lte.Calculator.CalculatorActivity;
import com.sungkyul.project_lte.Community.Community_Main;
import com.sungkyul.project_lte.MainNLogin.Main;
import com.sungkyul.project_lte.R;
import com.sungkyul.project_lte.Etc.RbPreference;
import com.sungkyul.project_lte.SOS.SOSMain;


/**
 * 도움말 - 완료
 * Created by WonHo on 2016-05-24.
 */
public class Help extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        tabInit();  // 뷰페이저 설정
    }

    void tabInit() {
        // 첫 시작 View Pager 선언
        ViewPager contentPager = (ViewPager) findViewById(R.id.fragHelp);

        // 어뎁터 가져오기
        HelpAdapter helpAdapter = new HelpAdapter(getSupportFragmentManager(), Help.this);
        contentPager.setAdapter(helpAdapter);

        // 메인 layout
        TabLayout tabLayout = (TabLayout) findViewById(R.id.helpTabView);

        // viewPager 와 tabLayout을 연결
        tabLayout.setupWithViewPager(contentPager);

        // 모든 view를 메인 layout에 설정
        for (int i = 0; i < tabLayout.getTabCount(); i++) {

            TabLayout.Tab tabName = tabLayout.getTabAt(i);

            tabName.setCustomView(helpAdapter.getTabView(i));
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            //하드웨어 뒤로가기 버튼에 따른 이벤트 설정
            case KeyEvent.KEYCODE_BACK:
                RbPreference pref = new RbPreference(this); // RbPreference 생성
                String activity_no = pref.getValue("activity", null);
                if(activity_no.equals("1")) {
                    Intent send = new Intent(Help.this, CalculatorActivity.class);
                    startActivity(send);
                    finish();
                } else if(activity_no.equals("2")) {
                    Intent send = new Intent(Help.this, SOSMain.class);
                    startActivity(send);
                    finish();
                } else if(activity_no.equals("3")) {
                    Intent send = new Intent(Help.this, Community_Main.class);
                    startActivity(send);
                    finish();
                } else if(activity_no.equals("0")){
                    Intent send = new Intent(Help.this, Main.class);
                    startActivity(send);
                    finish();
                } else {
                    Intent send = new Intent(Help.this, My_Page.class);
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