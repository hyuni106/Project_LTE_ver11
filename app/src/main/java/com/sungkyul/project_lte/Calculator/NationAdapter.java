package com.sungkyul.project_lte.Calculator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sungkyul.project_lte.R;

import java.util.ArrayList;

/**
 * Created by WonHo on 2016-05-10.
 */
// 원화계산기 통화 설정 List 연결 Adapter
public class NationAdapter extends BaseAdapter {
    Context context;
    ArrayList<nationList_Item> nationArrayList; // 리스트 뷰에 들어갈 값 배열
    ViewHolder viewholder;

    public NationAdapter(Context context, ArrayList<nationList_Item> nationArrayList) {
        this.context = context;
        this.nationArrayList = nationArrayList;
    }

    /***
     * 리스트뷰가 몇개의 아이템을 가지고 있는지 알려주는 함수
     * arrayList의 size만큼 가지고 있으므로 이걸 반환
     *
     * @return
     */
    @Override
    public int getCount() {
        return this.nationArrayList.size();
    }

    /***
     * 현재 어떤 아이템인지를 알려주는 부분
     * arrayList에 저장되있는 객체중 position에 해당되는 아이템을 가져옴
     *
     * @param position
     * @return
     */
    @Override
    public Object getItem(int position) {
        return nationArrayList.get(position);
    }

    /***
     * 현재 어떤 표지션인지 알려주는 부분
     *
     * @param position
     * @return
     */
    @Override
    public long getItemId(int position) {
        return position;
    }


    /***
     * 리스트뷰에서 아이템과 xml을 연결하여 화면에 표시해주는 부분 (중요)
     * getView에서 반복문 실행된다고
     * convertView <--  item.xml을 불러와야함 (약간의 절차 필요)
     * Context의 생성자를 통해 받은것
     * LayoutInflater 클래스는 다른 클래스에서 xml을 가져올 수 있음
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            convertView = LayoutInflater.from(context).inflate(R.layout.nation_list_item2, null);
            viewholder = new ViewHolder();

            viewholder.imgFlag = (ImageView) convertView.findViewById(R.id.flagImage);
            viewholder.txtNation = (TextView) convertView.findViewById(R.id.txtNaton);
            viewholder.txtExchange = (TextView) convertView.findViewById(R.id.txtExchange);
            viewholder.txtExchangeUnit = (TextView) convertView.findViewById(R.id.txtExchangeUnit);

            convertView.setTag(viewholder);
        } else {
            viewholder = (ViewHolder)convertView.getTag();
        }
//        Glide.with(context).load(nationArrayList.get(position).getImg()).into(viewholder.imgFlag); 사용방법 알아올것
        viewholder.imgFlag.setBackgroundResource(nationArrayList.get(position).getImg());
        viewholder.txtNation.setText(nationArrayList.get(position).getNation());
        viewholder.txtExchange.setText(nationArrayList.get(position).getExchange());
        viewholder.txtExchangeUnit.setText(nationArrayList.get(position).getExchangeUnit());

        return convertView;
    }

    /***
     * LayoutInflater를 통한 xml 불러오는 작업은 생각보다 무거운 작업
     * Holder 패턴이라는 패턴을 통해 최적화하는 방법
     * inner 클래스 ( = 내부 클래스)
     */
    class ViewHolder {
        ImageView imgFlag;
        TextView txtNation;
        TextView txtExchange;
        TextView txtExchangeUnit;
    }
}
