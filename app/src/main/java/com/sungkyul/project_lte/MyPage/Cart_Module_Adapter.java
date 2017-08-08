package com.sungkyul.project_lte.MyPage;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sungkyul.project_lte.Community.Trip_Read_Module_Item;
import com.sungkyul.project_lte.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Hanna on 2016-05-12.
 */
// 여행 카트 List 연결 Adapter
public class Cart_Module_Adapter extends ArrayAdapter<Cart_Module_Item> {
    Context context;
    int layoutResourceId;
    Cart_Module_Item data[] = null;
    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();

/*    public Cart_Module_Adapter(Context context, int layoutResourceId, Cart_Module_Item[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }*/

    public Cart_Module_Adapter(Context context, int layoutResourceId, List<Cart_Module_Item> objects) {
        super(context, layoutResourceId, objects);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        CartModuleHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new CartModuleHolder();
            holder.txtPlace = (TextView)row.findViewById(R.id.txtPlace);
            holder.txtContent = (TextView)row.findViewById(R.id.txtContent);


            row.setTag(holder);
        }else{
            holder = (CartModuleHolder)row.getTag();
        }
        Cart_Module_Item trip_read_module_item = getItem(position);
        holder.txtPlace.setText(trip_read_module_item.place);
        holder.txtContent.setText(trip_read_module_item.content);

        notifyDataSetChanged();

        return row;

    }
    static class CartModuleHolder
    {
        TextView txtPlace, txtContent;
    }
}
