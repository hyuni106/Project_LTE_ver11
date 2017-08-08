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
// 자유게시판 댓글 List 연결 Adapter
public class Free_Comment_Adapter extends ArrayAdapter<Free_Comment_Item> {
    Context context;
    int layoutResourceId;
    List<Free_Comment_Item> list = new ArrayList<Free_Comment_Item>();

    public Free_Comment_Adapter(Context context, int layoutResourceId, List<Free_Comment_Item> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.list = data;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        Free_CommentHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE );
            row = inflater.inflate(R.layout.free_comment_item_row, parent, false);

            holder = new Free_CommentHolder();
            holder.txtcommenter = (TextView)row.findViewById(R.id.tv_commenter);
            holder.txtcomment = (TextView)row.findViewById(R.id.tv_comment);

            row.setTag(holder);
        }else{
            holder = (Free_CommentHolder)row.getTag();
        }
        Free_Comment_Item free_comment_item = getItem(position);
        holder.txtcommenter.setText(free_comment_item.name);
        holder.txtcomment.setText(free_comment_item.comment);

        return row;

    }
    static class Free_CommentHolder
    {
        TextView txtcommenter, txtcomment;
        Button btn_delete;
    }
}
