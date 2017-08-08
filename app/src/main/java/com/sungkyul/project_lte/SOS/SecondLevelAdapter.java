package com.sungkyul.project_lte.SOS;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.sungkyul.project_lte.R;

import java.util.List;
import java.util.Map;

public class SecondLevelAdapter extends BaseExpandableListAdapter {
    private final Context mContext;
    private final List<String> mListDataHeader; // 2 LEVEL
    private final Map<String, List<String>> mListDataChild; // 3 LEVEL
    // SecondLevelAdapter 생성자
    public SecondLevelAdapter(Context mContext, List<String> mListDataHeader, Map<String, List<String>> mListDataChild) {
        this.mContext = mContext;
        this.mListDataHeader = mListDataHeader;
        this.mListDataChild = mListDataChild;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // groupPosition과 childPosition을 통해 childList의 원소를 얻어옴
        return this.mListDataChild.get(this.mListDataHeader.get(groupPosition))
                .get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) { // ChildList의 ID로 long형 값을 반환
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        // ChildList의 View. Layout 연결 후, layout 내 TextView를 연결
        final String childText = (String) getChild(groupPosition, childPosition);
        if (convertView == null) {
            // inflater를 이용하여 사용할 layout을 가져옴
            LayoutInflater layoutInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // ThirdList의 layout 연결. root로 argument 중 SecondList를 받으며 root로 고정하지는 않음
            convertView = layoutInflater.inflate(R.layout.drawer_list_item, parent, false);
        }
        //SecondList의 Layout 연결 후, 해당 layout 내 TextView를 연결
        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);
        txtListChild.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) { // ChildList의 크기를 int 형으로 반환
        try {
            return this.mListDataChild.get(this.mListDataHeader.get(groupPosition)).size();
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public Object getGroup(int groupPosition) { // ParentList의 position을 받아 해당 TextView에 반영될 Object를 반환
        return this.mListDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {  // ParentList의 원소 개수를 반환
        return this.mListDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) { // ParentList의 position을 받아 long값으로 반환
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) { // ParentList의 View
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            // inflater를 이용하여 사용할 layout을 가져옴
            LayoutInflater layoutInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // SecondList의 layout 연결. root로 argument 중 Parent를 받으며 root로 고정하지는 않음
            convertView = layoutInflater.inflate(R.layout.drawer_list_group_second, parent, false);
        }
        // ParentList의 Layout 연결 후, 해당 layout 내 TextView를 연결
        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setText(headerTitle);
        lblListHeader.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        lblListHeader.setTextColor(Color.GRAY);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        // stable ID인지 boolean 값으로 반환
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // 선택여부를 boolean 값으로 반환
        return true;
    }
}
