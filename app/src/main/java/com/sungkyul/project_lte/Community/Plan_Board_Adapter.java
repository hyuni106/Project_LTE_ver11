package com.sungkyul.project_lte.Community;

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
// 여행 계획 게시판 List 연결 Adapter
public class Plan_Board_Adapter extends ArrayAdapter<Plan_Board_Item> {
    Context context;
    int layoutResourceId;
    Plan_Board_Item data[] = null;

    public Plan_Board_Adapter(Context context, int layoutResourceId, Plan_Board_Item[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        PlanBoardHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new PlanBoardHolder();
            holder.txtnum = (TextView)row.findViewById(R.id.txtNum);
            holder.txttitle = (TextView)row.findViewById(R.id.txtTitle);
            holder.txtname = (TextView)row.findViewById(R.id.txtName);
            holder.txtdate = (TextView)row.findViewById(R.id.txtDate);
            holder.txtlook = (TextView)row.findViewById(R.id.txtLook);
            holder.txtlike = (TextView)row.findViewById(R.id.txtLike);

            row.setTag(holder);
        }else{
            holder = (PlanBoardHolder)row.getTag();
        }
        Plan_Board_Item planBoard = data[position];
        holder.txtnum.setText(planBoard.num);
        holder.txttitle.setText(planBoard.title);
        holder.txtname.setText(planBoard.name);
        holder.txtdate.setText(planBoard.date);
        holder.txtlook.setText(planBoard.look);
        holder.txtlike.setText(planBoard.like);

        return row;

    }
    static class PlanBoardHolder
    {
        TextView txtnum, txttitle, txtname, txtdate, txtlook, txtlike;
    }
}
