package com.sungkyul.project_lte.Etc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.sungkyul.project_lte.MainNLogin.Main;
import com.sungkyul.project_lte.R;

/**
 * 로딩 화면 - 완료
 * Created by WonHo on 2016-05-26.
 */
public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);     // 레이아웃 설정
        Handler hd = new Handler();           // 핸들러 생성
        hd.postDelayed(new Runnable() {         // 스레드 실행
            @Override
            public void run() {
                startActivity(new Intent(Splash.this, Main.class));  // Main 액티비티로 이동
                finish();       // 3 초후 이미지를 닫아버림
            }
        }, 3000);
    }

}