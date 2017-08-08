package com.sungkyul.project_lte.MyPage;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sungkyul.project_lte.R;

/**
 * 나의 자유게시판 어댑터 - 완료
 * Created by suhyu on 2016-05-24.
 */
// 내가 쓴 자유게시판 List 연결 Adapter
public class MyFreeBoardAdapter extends ArrayAdapter<MyFreeBoardItem> {
    Context context;
    int layoutResourceId;   // layout
    MyFreeBoardItem data[] = null;

    /***
     * 나의 자유게시판 어댑터 생성자
     * @param context
     * @param layoutResourceId
     * @param data
     */
    public MyFreeBoardAdapter(Context context, int layoutResourceId, MyFreeBoardItem[] data) {
        // context, layoutResourceId, Free_Search_Item[] 를 매개변수로 받아 선언된 변수에 할당
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    /***
     * 리스트뷰에서 아이템과 xml을 연결하여 화면에 표시해주는 부분 (중요)
     * convertView <--  item.xml을 불러와야함 (약간의 절차 필요)
     * Context의 생성자를 통해 받은것
     * LayoutInflater 클래스는 다른 클래스에서 xml을 가져올 수 있음
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;  // View에 converView 할당
        MyFreeBoardHolder holder = null; // holder 선언

        if (row == null) {      // View의 값이 null 이라면

            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new MyFreeBoardHolder();    // holder 생성
            holder.txtnum = (TextView) row.findViewById(R.id.txtNum);       // 글 번호 선언 및 View의 할당
            holder.txttitle = (TextView) row.findViewById(R.id.txtTitle);   // 글 제목 선언 및 View의 할당
            holder.txtname = (TextView) row.findViewById(R.id.txtName);     //  글 작성자 선언 및 View의 할당
            holder.txtdate = (TextView) row.findViewById(R.id.txtDate);     // 글 작성날짜 선언 및 View의 할당
            holder.txtlook = (TextView) row.findViewById(R.id.txtLook);     // 글 조회수 선언 및 View의 할당
            //holder.txtlike = (TextView)row.findViewById(R.id.txtLike);

            row.setTag(holder);     // holder에 할당 받은 값을을 View에 setTag
        } else {    //View가 null이 아닌 경우
            holder = (MyFreeBoardHolder) row.getTag();   // holder 값을 getTag
        }

        MyFreeBoardItem myFreeBoardItem = data[position];    // 자유게시판 아이템의 해당 위치를 myFreeBoardItem로 선언
        holder.txtnum.setText(myFreeBoardItem.num);          // 해당 position의 글 번호 값 설정
        holder.txttitle.setText(myFreeBoardItem.title);      // 해당 position의 글 제목 값 설정
        holder.txtname.setText(myFreeBoardItem.name);      // 해당 position의 글 작성자 값 설정
        holder.txtdate.setText(myFreeBoardItem.date);      // 해당 position의 글 작성날짜 값 설정
        holder.txtlook.setText(myFreeBoardItem.look);      // 해당 position의 글 조회수 값 설정
        //holder.txtlike.setText(freeBoard.like);

        return row;

    }

    /***
     * LayoutInflater를 통한 xml 불러오는 작업은 생각보다 무거운 작업
     * Holder 패턴이라는 패턴을 통해 최적화하는 방법
     * FreeSearchHolder class
     */
    static class MyFreeBoardHolder {
        TextView txtnum, txttitle, txtname, txtdate, txtlook;
    }
}
