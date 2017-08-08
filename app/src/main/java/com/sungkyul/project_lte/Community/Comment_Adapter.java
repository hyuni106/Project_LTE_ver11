package com.sungkyul.project_lte.Community;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.sungkyul.project_lte.R;

/**
 * Created by Hanna on 2016-05-12.
 */
// 댓글 List 연결 Adapter
public class Comment_Adapter extends ArrayAdapter<Comment_Item> {
    Context context;
    int layoutResourceId;
    Comment_Item data[] = null;

    public Comment_Adapter(Context context, int layoutResourceId, Comment_Item[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        CommentHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE );
            //row = inflater.inflate(layoutResourceId, parent, false);
            row = inflater.inflate(R.layout.comment_item_row, parent, false);

            holder = new CommentHolder();
            holder.txtcommenter = (TextView)row.findViewById(R.id.tv_commenter);
            holder.txtcomment = (TextView)row.findViewById(R.id.tv_comment);

            row.setTag(holder);
        }else{
            holder = (CommentHolder)row.getTag();
        }
        Comment_Item comment = data[position];
        holder.txtcommenter.setText(comment.commenter);
        holder.txtcomment.setText(comment.comment);

        return row;

    }
    static class CommentHolder
    {
        TextView txtcommenter, txtcomment;
        Button btn_delete;
    }
}
