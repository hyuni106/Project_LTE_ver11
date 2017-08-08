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
// 도움말 - 여행 커뮤니티
public class helpCom extends Fragment {
    ImageView helpFree, helpTrip, helpPlan; // 자유게시판, 여행 후기 게시판, 여행 계획 게시판 이미지
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.help_com, container, false);

        helpFree = (ImageView) view.findViewById(R.id.helpFree);
        helpTrip= (ImageView) view.findViewById(R.id.helpTrip);
        helpPlan = (ImageView) view.findViewById(R.id.helpPlan);

        return view;
    }

}
