package com.sungkyul.project_lte.MyPage;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.sungkyul.project_lte.MainNLogin.Main;
import com.sungkyul.project_lte.R;

/**
 * 도움말 앱 버전 - 완료
 * Created by WonHo on 2016-05-24.
 */
public class HelpAppVersion extends Activity {   // Fragment 상속
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_version);

    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            //하드웨어 뒤로가기 버튼에 따른 이벤트 설정
            case KeyEvent.KEYCODE_BACK:
                Intent intent_main = new Intent(HelpAppVersion.this, Main.class);
                startActivity(intent_main);
                finish();
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

}
