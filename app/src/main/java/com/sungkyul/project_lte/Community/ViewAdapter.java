package com.sungkyul.project_lte.Community;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.sungkyul.project_lte.Community.Free_Board_List;
import com.sungkyul.project_lte.Community.Plan_Board_List;
import com.sungkyul.project_lte.Community.Trip_Board_List;
import com.sungkyul.project_lte.R;

/**
 * Created by WonHo on 2016-04-08.
 */
// 여행 커뮤니티 Fragment
public class ViewAdapter extends FragmentPagerAdapter {

    // 텝 갯수
    final private int PAGE_COUNT = 3;
    // 텝 명명
    String[] pageTitle = {"여행후기", "여행계획", "자유게시판"};

    //배열로 페이지 선언.
    Fragment[] item = new Fragment[]{new Trip_Board_List(), new Plan_Board_List(), new Free_Board_List()};


    Context context ;

    public ViewAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context =context;
    }

    @Override
    public Fragment getItem(int position) {
        return item[position];
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
