package com.sungkyul.project_lte.Community;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.sungkyul.project_lte.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Hanna on 2016-05-12.
 */
// 여행 후기 글 읽기 모듈 List 연결 Adapter
public class Trip_Read_Module_Adapter extends ArrayAdapter<Trip_Read_Module_Item> {
    Context context;
    int layoutResourceId;
    Trip_Read_Module_Item data[] = null;

    public Trip_Read_Module_Adapter(Context context, int layoutResourceId, List<Trip_Read_Module_Item> objects) {
        super(context, layoutResourceId, objects);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        TripReadModuleHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(R.layout.read_trip_module_item_row, null);

            holder = new TripReadModuleHolder();
            holder.txtPlace = (TextView)row.findViewById(R.id.txtPlace);
            holder.txtContent = (TextView)row.findViewById(R.id.txtContent);

            row.setTag(holder);
        }else{
            holder = (TripReadModuleHolder)row.getTag();
        }
        Trip_Read_Module_Item trip_read_module_item = getItem(position);
        holder.txtPlace.setText(trip_read_module_item.place);
        holder.txtContent.setText(trip_read_module_item.content);

        notifyDataSetChanged();

        return row;

    }

    static class TripReadModuleHolder
    {
        TextView txtPlace, txtContent;
    }
}
