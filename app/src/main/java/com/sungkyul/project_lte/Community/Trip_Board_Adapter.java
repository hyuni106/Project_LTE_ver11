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
// 여행 후기 List 연결 Adapter
public class Trip_Board_Adapter extends ArrayAdapter<Trip_Board_Item> {
    Context context;
    int layoutResourceId;
    Trip_Board_Item data[] = null;

    public Trip_Board_Adapter(Context context, int layoutResourceId, Trip_Board_Item[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        TripBoardHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new TripBoardHolder();
            holder.txtnum = (TextView)row.findViewById(R.id.txtNum);
            holder.txttitle = (TextView)row.findViewById(R.id.txtTitle);
            holder.txtname = (TextView)row.findViewById(R.id.txtName);
            holder.txtdate = (TextView)row.findViewById(R.id.txtDate);
            holder.txtlook = (TextView)row.findViewById(R.id.txtLook);
            holder.txtlike = (TextView)row.findViewById(R.id.txtLike);

            row.setTag(holder);
        }else{
            holder = (TripBoardHolder)row.getTag();
        }
        Trip_Board_Item tripBoard = data[position];
        holder.txtnum.setText(tripBoard.num);
        holder.txttitle.setText(tripBoard.title);
        holder.txtname.setText(tripBoard.name);
        holder.txtdate.setText(tripBoard.date);
        holder.txtlook.setText(tripBoard.look);
        holder.txtlike.setText(tripBoard.like);

        return row;

    }
    static class TripBoardHolder
    {
        TextView txtnum, txttitle, txtname, txtdate, txtlook, txtlike;
    }
}
