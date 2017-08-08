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
// 여행 계획 댓글 List 연결하는 Adapter
public class Plan_Comment_Adapter extends ArrayAdapter<Plan_Comment_Item> {
    Context context;
    int layoutResourceId;
    List<Plan_Comment_Item> list = new ArrayList<Plan_Comment_Item>();

    public Plan_Comment_Adapter(Context context, int layoutResourceId, List<Plan_Comment_Item> objects) {
        super(context, layoutResourceId, objects);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.list = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        Plan_CommentHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE );
            row = inflater.inflate(R.layout.plan_comment_item_row, parent, false);

            holder = new Plan_CommentHolder();
            holder.txtcommenter = (TextView)row.findViewById(R.id.tv_commenter);
            holder.txtcomment = (TextView)row.findViewById(R.id.tv_comment);

            row.setTag(holder);
        }else{
            holder = (Plan_CommentHolder)row.getTag();
        }

        Plan_Comment_Item plan_comment_item = getItem(position);
        holder.txtcommenter.setText(plan_comment_item.name);
        holder.txtcomment.setText(plan_comment_item.comment);

        return row;

    }
    static class Plan_CommentHolder
    {
        TextView txtcommenter, txtcomment;
        Button btn_delete;
    }
}
