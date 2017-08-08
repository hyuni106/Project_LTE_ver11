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
// 자유게시판 List 연결 Adapter
public class Free_Board_Adapter extends ArrayAdapter<Free_Board_Item> {
    Context context;
    int layoutResourceId;
    List<Free_Board_Item> list = new ArrayList<Free_Board_Item>();

    public Free_Board_Adapter(Context context, int layoutResourceId, List<Free_Board_Item> objects) {
        super(context, layoutResourceId, objects);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.list = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        FreeBoardHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(R.layout.free_board_item_row, null);

            holder = new FreeBoardHolder();
            holder.txtnum = (TextView) row.findViewById(R.id.txtNum);
            holder.txttitle = (TextView) row.findViewById(R.id.txtTitle);
            holder.txtname = (TextView) row.findViewById(R.id.txtName);
            holder.txtdate = (TextView) row.findViewById(R.id.txtDate);
            holder.txtlook = (TextView) row.findViewById(R.id.txtLook);
            //holder.txtlike = (TextView)row.findViewById(R.id.txtLike);

            row.setTag(holder);
        } else {
            holder = (FreeBoardHolder) row.getTag();
        }
        Free_Board_Item free_board_item = getItem(position);
        holder.txtnum.setText(free_board_item.num);
        holder.txttitle.setText(free_board_item.title);
        holder.txtname.setText(free_board_item.name);
        holder.txtdate.setText(free_board_item.date);
        holder.txtlook.setText(free_board_item.look);
        //holder.txtlike.setText(free_board_item.like);

        notifyDataSetChanged();


        return row;

    }

    static class FreeBoardHolder {
        TextView txtnum, txttitle, txtname, txtdate, txtlook;
    }
}
