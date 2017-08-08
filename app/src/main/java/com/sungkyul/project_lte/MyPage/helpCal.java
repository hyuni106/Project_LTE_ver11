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
// 도움말 - 원화계산기
public class helpCal extends Fragment {

    ImageView helpCalNation, helpCal, helpCalEnd; // 통화 설정, 계산기, 계산 결과 이미지
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.help_cal, container, false);

        helpCalNation = (ImageView) view.findViewById(R.id.helpCalNation);
        helpCal= (ImageView) view.findViewById(R.id.helpCal);
        helpCalEnd = (ImageView) view.findViewById(R.id.helpCalEnd);

        return view;
    }

}
