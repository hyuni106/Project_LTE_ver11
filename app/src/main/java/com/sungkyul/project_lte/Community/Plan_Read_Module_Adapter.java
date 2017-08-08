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
import java.util.List;

/**
 * Created by Hanna on 2016-05-12.
 */
// 여행 계획 읽기 모듈 List 연결 Adapter
public class Plan_Read_Module_Adapter extends ArrayAdapter<Plan_Read_Module_Item> {
    Context context;
    int layoutResourceId;
    List<Plan_Read_Module_Item> list = new ArrayList<Plan_Read_Module_Item>();

    public Plan_Read_Module_Adapter(Context context, int layoutResourceId, List<Plan_Read_Module_Item> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.list = data;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        PlanReadModuleHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(R.layout.read_plan_module_item_row, null);

            holder = new PlanReadModuleHolder();
            holder.txtPlace = (TextView)row.findViewById(R.id.txtPlace);
            holder.txtContent = (TextView)row.findViewById(R.id.txtContent);


            row.setTag(holder);
        }else{
            holder = (PlanReadModuleHolder)row.getTag();
        }
        Plan_Read_Module_Item plan_read_module_item = getItem(position);
        holder.txtPlace.setText(plan_read_module_item.place);
        holder.txtContent.setText(plan_read_module_item.content);

        notifyDataSetChanged();

        return row;
    }
    static class PlanReadModuleHolder
    {
        TextView txtPlace, txtContent;
    }
}
