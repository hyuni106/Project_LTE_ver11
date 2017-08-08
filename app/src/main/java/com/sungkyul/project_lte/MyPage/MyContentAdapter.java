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
 * Created by WonHo on 2016-05-23.
 */
// 내가 쓴 글 페이지 Adapter
public class MyContentAdapter extends FragmentPagerAdapter {
    // 텝 갯수
    final private int PAGE_COUNT = 3;
    // 텝 명명
    String[] pageTitle = {"나의 여행후기", "나의 여행계획", "나의 자유게시판"};

    // 배열로 페이지 선언.
    Fragment[] item = new Fragment[]{new MyTripBoardList(), new MyPlanBoardList(), new MyFreeBoardList() };
    Context context;

    public MyContentAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {

            case 0:
                MyTripBoardList mTripList = new MyTripBoardList();
                return mTripList;
            case 1:
                MyPlanBoardList mPlanList = new MyPlanBoardList();
                return mPlanList;
            case 2:
                MyFreeBoardList mFreeList = new MyFreeBoardList();
                return  mFreeList;

        }
        return null;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    public View getTabView(int position) {
        // 텝의 customizing 모양
        View view = LayoutInflater.from(context).inflate(R.layout.custom_tab , null);

        // textView
        TextView txtView = (TextView) view.findViewById(R.id.txtTab);
        txtView.setText(pageTitle[position]);

        return view;
    }
}
