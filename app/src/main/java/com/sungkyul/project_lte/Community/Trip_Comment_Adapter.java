package com.sungkyul.project_lte.Community;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.sungkyul.project_lte.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suhyu on 2016-05-27.
 */
// 여행 후기 댓글 List 연결해주는 Adapter
public class Trip_Comment_Adapter extends ArrayAdapter<Trip_Comment_Item> {
    Context context;
    int layoutResourceId;
    List<Trip_Comment_Item> list = new ArrayList<Trip_Comment_Item>();

    public Trip_Comment_Adapter(Context context, int layoutResourceId, List<Trip_Comment_Item> objects) {
        super(context, layoutResourceId, objects);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.list = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        Trip_CommentHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE );
            //row = inflater.inflate(layoutResourceId, parent, false);
            row = inflater.inflate(R.layout.trip_comment_item_row, parent, false);

            holder = new Trip_CommentHolder();
            holder.txtcommenter = (TextView)row.findViewById(R.id.tv_commenter);
            holder.txtcomment = (TextView)row.findViewById(R.id.tv_comment);

            row.setTag(holder);
        }else{
            holder = (Trip_CommentHolder)row.getTag();
        }
        Trip_Comment_Item trip_comment_item = getItem(position);
        holder.txtcommenter.setText(trip_comment_item.name);
        holder.txtcomment.setText(trip_comment_item.comment);

        notifyDataSetChanged();

        return row;

    }
    static class Trip_CommentHolder
    {
        TextView txtcommenter, txtcomment;
        Button btn_delete;
    }
}
