package com.sungkyul.project_lte.MyPage;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.sungkyul.project_lte.R;


/**
 * 도움말 어댑터(연결) - 완료
 * Created by WonHo on 2016-05-23.
 */
public class HelpAdapter extends FragmentPagerAdapter {

    final private int PAGE_COUNT = 4;                  // 텝 갯수
    String[] pageTitle = {"원화계산기", "SOS", "   여행   커뮤니티", "기타"};      // 텝 명명

    // 배열로 페이지 선언.
    Fragment[] item = new Fragment[]{new helpCal(), new helpSOS(), new helpCom(), new helpEtc() };
    Context context;

    /**
     * fragmentManager와 context 를 받은 생성자
     * @param fm
     * @param context
     */
    public HelpAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    /***
     * 각 해당 도움말 탭에 존재하는 액티비티이다.
     * position을 매개변수로 받으며 첫 번째는 사용방법, 두 번째는 앱 버전을 보여줌
     * @param position
     * @return
     */
    @Override
    public Fragment getItem(int position) {
        switch (position) {

            case 0:
                helpCal helpCal = new helpCal();
                return helpCal;
            case 1:
                helpSOS helpSOS = new helpSOS();
                return helpSOS;
            case 2:
                helpCom helpCom = new helpCom();
                return helpCom;
            case 3:
                helpEtc helpEtc = new helpEtc();
                return helpEtc;


        }
        return null;
    }

    /***
     * fragment의 총 갯수를 return
     * @return
     */
    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    /***
     *  main TabView에 모습을 할당 및 글씨 설정
     * @param position
     * @return
     */
    public View getTabView(int position) {
        // 텝의 customizing 모양
        View view = LayoutInflater.from(context).inflate(R.layout.custom_tab , null);   // tab으로 보여줄 View 선언 및 할당.

        // textView
        TextView txtView = (TextView) view.findViewById(R.id.txtTab);   // 탭 제목
        txtView.setText(pageTitle[position]);                           // 각 위치에 탭 제목 설정

        return view;    // 설정된 view를 리턴
    }
}
