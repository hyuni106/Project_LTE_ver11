package com.sungkyul.project_lte.MyPage;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sungkyul.project_lte.R;

// 내가 세운 계획 List 연결 Adapter
public class MyPlanAdapter extends ArrayAdapter<MyPlanItem> {
    Context context;
    int layoutResourceId;
    MyPlanItem data[] = null;

    public MyPlanAdapter(Context context, int layoutResourceId, MyPlanItem[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        MyPlanHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new MyPlanHolder();
            holder.txtnum = (TextView)row.findViewById(R.id.txtNum);
            holder.txttitle = (TextView)row.findViewById(R.id.txtTitle);
            holder.txtname = (TextView)row.findViewById(R.id.txtName);
            holder.txtdate = (TextView)row.findViewById(R.id.txtDate);
            holder.txtlook = (TextView)row.findViewById(R.id.txtLook);
            holder.txtlike = (TextView)row.findViewById(R.id.txtLike);

            row.setTag(holder);
        }else{
            holder = (MyPlanHolder)row.getTag();
        }
        MyPlanItem myPlanItem = data[position];
        holder.txtnum.setText(myPlanItem.num);
        holder.txttitle.setText(myPlanItem.title);
        holder.txtname.setText(myPlanItem.name);
        holder.txtdate.setText(myPlanItem.date);
        holder.txtlook.setText(myPlanItem.look);
        holder.txtlike.setText(myPlanItem.like);

        return row;

    }
    static class MyPlanHolder
    {
        TextView txtnum, txttitle, txtname, txtdate, txtlook, txtlike;
    }
}