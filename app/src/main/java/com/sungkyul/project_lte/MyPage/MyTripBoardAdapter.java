package com.sungkyul.project_lte.MyPage;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sungkyul.project_lte.R;

/**
 * Created by Hanna on 2016-05-12.
 */
// 내가 작성한 여행 후기 게시판 List 연결 Adapter
public class MyTripBoardAdapter extends ArrayAdapter<MyTripBoardItem> {
    Context context;
    int layoutResourceId;
    MyTripBoardItem data[] = null;

    public MyTripBoardAdapter(Context context, int layoutResourceId, MyTripBoardItem[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        MyTripBoardHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new MyTripBoardHolder();
            holder.txtnum = (TextView)row.findViewById(R.id.txtNum);
            holder.txttitle = (TextView)row.findViewById(R.id.txtTitle);
            holder.txtname = (TextView)row.findViewById(R.id.txtName);
            holder.txtdate = (TextView)row.findViewById(R.id.txtDate);
            holder.txtlook = (TextView)row.findViewById(R.id.txtLook);
            holder.txtlike = (TextView)row.findViewById(R.id.txtLike);

            row.setTag(holder);
        }else{
            holder = (MyTripBoardHolder)row.getTag();
        }
        MyTripBoardItem myTripBoardItem = data[position];
        holder.txtnum.setText(myTripBoardItem.num);
        holder.txttitle.setText(myTripBoardItem.title);
        holder.txtname.setText(myTripBoardItem.name);
        holder.txtdate.setText(myTripBoardItem.date);
        holder.txtlook.setText(myTripBoardItem.look);
        holder.txtlike.setText(myTripBoardItem.like);

        return row;

    }
    static class MyTripBoardHolder
    {
        TextView txtnum, txttitle, txtname, txtdate, txtlook, txtlike;
    }
}
