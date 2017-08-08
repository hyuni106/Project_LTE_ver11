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
// 내가 작성한 여행 계획 List 연결 Adapter
public class MyPlanBoardAdapter extends ArrayAdapter<MyPlanBoardItem> {
    Context context;
    int layoutResourceId;
    MyPlanBoardItem data[] = null;

    public MyPlanBoardAdapter(Context context, int layoutResourceId, MyPlanBoardItem[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        MyPlanBoardHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new MyPlanBoardHolder();
            holder.txtnum = (TextView)row.findViewById(R.id.txtNum);
            holder.txttitle = (TextView)row.findViewById(R.id.txtTitle);
            holder.txtname = (TextView)row.findViewById(R.id.txtName);
            holder.txtdate = (TextView)row.findViewById(R.id.txtDate);
            holder.txtlook = (TextView)row.findViewById(R.id.txtLook);
            holder.txtlike = (TextView)row.findViewById(R.id.txtLike);

            row.setTag(holder);
        }else{
            holder = (MyPlanBoardHolder)row.getTag();
        }
        MyPlanBoardItem myPlanBoardItem = data[position];
        holder.txtnum.setText(myPlanBoardItem.num);
        holder.txttitle.setText(myPlanBoardItem.title);
        holder.txtname.setText(myPlanBoardItem.name);
        holder.txtdate.setText(myPlanBoardItem.date);
        holder.txtlook.setText(myPlanBoardItem.look);
        holder.txtlike.setText(myPlanBoardItem.like);

        return row;

    }
    static class MyPlanBoardHolder
    {
        TextView txtnum, txttitle, txtname, txtdate, txtlook, txtlike;
    }
}
