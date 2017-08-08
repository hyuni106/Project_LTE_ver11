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
// 도움말 - 마이페이지
public class helpEtc extends Fragment {
    ImageView helpMyPage, helpMyPlan,helpSosSet,helpChangeNick,helpChangePw;
    // 마이페이지, 내가 세운 계획, SOS 설정, 닉네임 변경, 비밀번호 변경 이미지
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.help_etc, container, false);

        helpMyPage = (ImageView)view.findViewById(R.id.helpMyPage);
        helpMyPlan= (ImageView)view.findViewById(R.id.helpMyPlan);
        helpSosSet= (ImageView)view.findViewById(R.id.helpSosSet);
        helpChangeNick= (ImageView)view.findViewById(R.id.helpChangeNick);
        helpChangePw= (ImageView)view.findViewById(R.id.helpChangePw);

        return view;
    }
}
