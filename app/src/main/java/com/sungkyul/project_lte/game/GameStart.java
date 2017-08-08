package com.sungkyul.project_lte.game;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.sungkyul.project_lte.MainNLogin.Main;
import com.sungkyul.project_lte.R;

/**
 * Created by Hanna on 2016-10-06.
 */
public class GameStart extends Activity{
    Button easy, normal, hard, game_help;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        easy = (Button)findViewById(R.id.easy);
        normal = (Button)findViewById(R.id.normal);
        hard = (Button)findViewById(R.id.hard);
        game_help = (Button)findViewById(R.id.game_help);

        easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GameEasy.class);
                startActivity(intent);
                finish();
            }
        });
        normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GameNormal.class);
                startActivity(intent);
                finish();
            }
        });
        hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GameHard.class);
                startActivity(intent);
                finish();
            }
        });
        game_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GameHelp.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            //하드웨어 뒤로가기 버튼에 따른 이벤트 설정
            case KeyEvent.KEYCODE_BACK:
                Intent intent_main = new Intent(getApplicationContext(), Main.class);
                startActivity(intent_main);
                finish();
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
