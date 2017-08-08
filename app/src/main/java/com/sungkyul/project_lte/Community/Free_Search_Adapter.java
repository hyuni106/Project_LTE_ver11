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
 * Created by suhyu on 2016-05-23.
 */
// 자유게시판 검색 List 연결 Adapter
public class Free_Search_Adapter extends ArrayAdapter<Free_Search_Item> {
    Context context;
    int layoutResourceId;
    List<Free_Search_Item> list = new ArrayList<Free_Search_Item>();

    public Free_Search_Adapter(Context context, int layoutResourceId, List<Free_Search_Item> objects) {
        super(context, layoutResourceId, objects);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.list = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        FreeSearchHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(R.layout.free_search_item_row, null);

            holder = new FreeSearchHolder();
            holder.txtnum = (TextView)row.findViewById(R.id.txtNum);
            holder.txttitle = (TextView)row.findViewById(R.id.txtTitle);
            holder.txtname = (TextView)row.findViewById(R.id.txtName);
            holder.txtdate = (TextView)row.findViewById(R.id.txtDate);
            holder.txtlook = (TextView)row.findViewById(R.id.txtLook);
            //holder.txtlike = (TextView)row.findViewById(R.id.txtLike);

            row.setTag(holder);
        }else{
            holder = (FreeSearchHolder)row.getTag();
        }
        Free_Search_Item free_search_item = getItem(position);
        holder.txtnum.setText(free_search_item.num);
        holder.txttitle.setText(free_search_item.title);
        holder.txtname.setText(free_search_item.name);
        holder.txtdate.setText(free_search_item.date);
        holder.txtlook.setText(free_search_item.look);
        //holder.txtlike.setText(free_search_item.like);

        return row;

    }
    static class FreeSearchHolder
    {
        TextView txtnum, txttitle, txtname, txtdate, txtlook;
    }
}
