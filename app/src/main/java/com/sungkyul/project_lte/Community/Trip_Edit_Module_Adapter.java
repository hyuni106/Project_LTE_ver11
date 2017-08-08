package com.sungkyul.project_lte.Community;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sungkyul.project_lte.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Hanna on 2016-05-12.
 */
// 여행 후기 수정 모듈 List 연결 Adapter
public class Trip_Edit_Module_Adapter extends ArrayAdapter<Trip_Edit_Module_Item> {
    Context context;
    int layoutResourceId;
    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
    List<Trip_Edit_Module_Item> list = new ArrayList<Trip_Edit_Module_Item>();

    public Trip_Edit_Module_Adapter(Context context, int layoutResourceId, List<Trip_Edit_Module_Item> objects) {
        super(context, layoutResourceId, objects);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.list = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        TripEditModuleHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            //row = inflater.inflate(layoutResourceId, parent, false);
            row = inflater.inflate(R.layout.edit_trip_module_item_row, null);

            holder = new TripEditModuleHolder();
            holder.txtPlace = (TextView)row.findViewById(R.id.txtPlace);
            holder.txtContent = (TextView)row.findViewById(R.id.txtContent);


            row.setTag(holder);
        }else{
            holder = (TripEditModuleHolder)row.getTag();
        }
        Trip_Edit_Module_Item trip_edit_module_item = getItem(position);
        holder.txtPlace.setText(trip_edit_module_item.place);
        holder.txtContent.setText(trip_edit_module_item.content);

        notifyDataSetChanged();

        return row;

    }
    static class TripEditModuleHolder
    {
        TextView txtPlace, txtContent;
    }
}
