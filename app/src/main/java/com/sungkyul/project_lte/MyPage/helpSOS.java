package com.sungkyul.project_lte.MyPage;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sungkyul.project_lte.R;

/**
 * Created by WonHo on 2016-06-03.
 */
// 도움말 - SOS
public class helpSOS extends Fragment {
    ImageView helpLocationSet,helpSend,helpSosSet,helpEmbassy;
    // 위치 설정, 문자 발송, sos 설정, 대사관 정보 이미지
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.help_sos, container, false);

        helpLocationSet = (ImageView)view.findViewById(R.id.helpLocationSet);
        helpSosSet = (ImageView)view.findViewById(R.id.helpSosSet);
        helpSend= (ImageView)view.findViewById(R.id.helpSend);
        helpEmbassy= (ImageView)view.findViewById(R.id.helpEmbassy);

        return view;

    }
}
