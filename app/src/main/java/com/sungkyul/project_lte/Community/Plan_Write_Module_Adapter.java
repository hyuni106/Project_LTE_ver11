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
// 여행 계획 글 작성 모듈 List 연결 Adapter
public class Plan_Write_Module_Adapter extends ArrayAdapter<Plan_Write_Module_Item> {
    Context context;
    int layoutResourceId;
    Plan_Write_Module_Item data[] = null;

    public Plan_Write_Module_Adapter(Context context, int layoutResourceId, Plan_Write_Module_Item[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        PlanWriteModuleHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new PlanWriteModuleHolder();
            holder.txtPlace = (TextView)row.findViewById(R.id.txtPlace);
            holder.txtContent = (TextView)row.findViewById(R.id.txtContent);


            row.setTag(holder);
        }else{
            holder = (PlanWriteModuleHolder)row.getTag();
        }
        Plan_Write_Module_Item plan_write_module_item = data[position];
        holder.txtPlace.setText(plan_write_module_item.place);
        holder.txtContent.setText(plan_write_module_item.content);


        return row;

    }
    static class PlanWriteModuleHolder
    {
        TextView txtPlace, txtContent;
    }
}
