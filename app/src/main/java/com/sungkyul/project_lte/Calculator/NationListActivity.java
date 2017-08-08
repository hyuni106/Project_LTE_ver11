package com.sungkyul.project_lte.Calculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.sungkyul.project_lte.R;

import java.util.ArrayList;
// 원화계산기 통화 설정 List
public class NationListActivity extends AppCompatActivity {

    ListView listView; // 통화 설정 국가 리스트 뷰
    NationAdapter nationAdapter; // 리스트 뷰에 리스트 값 연결 어댑터
    ArrayList<nationList_Item> nationArrayList; // 리스트에 들어갈 값 배열

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        listView = (ListView) findViewById(R.id.nationList);

        nationArrayList = new ArrayList<nationList_Item>();
        addItem(); // 아이템 추가 메소드 호출
        nationAdapter = new NationAdapter(NationListActivity.this, nationArrayList); // 어댑터 연결
        listView.setAdapter(nationAdapter); // 리스트뷰 - 어댑터

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                String exchange = nationArrayList.get(position).getExchange().toString();
                String exchangeUnit = nationArrayList.get(position).getExchangeUnit().toString();
                int flagImage = nationArrayList.get(position).getImg();
                // intent 설정  데이터 전달.
                Intent intent = new Intent(getApplicationContext(), CalculatorActivity.class);
                intent.putExtra("exchange", exchange); // 국가 통화코드 전달
                intent.putExtra("exchangeUnit", exchangeUnit); // 통화 기호 전달
                intent.putExtra("flagImage", flagImage); // 해당 국가 국기 이미지 전달
//               선택된 exchange 와 exchangeUnit을 보낸다
                setResult(1111, intent);
                finish();

            }
        });
    }
    // 아이템 추가 메소드
    public void addItem() {

        nationArrayList.add(new nationList_Item(R.drawable.usd, "미국  ", "USD", "$"));
        nationArrayList.add(new nationList_Item(R.drawable.eur, "유럽 연합  ", "EUR", "€"));
        nationArrayList.add(new nationList_Item(R.drawable.jpy, "일본  ", "JPY", "¥"));
        nationArrayList.add(new nationList_Item(R.drawable.cny, "중국  ", "CNY", "¥"));
        nationArrayList.add(new nationList_Item(R.drawable.hkd, "홍콩  ", "HKD", "HK$"));
        nationArrayList.add(new nationList_Item(R.drawable.twd, "대만  ", "TWD", "NT$"));
        nationArrayList.add(new nationList_Item(R.drawable.gbp, "영국  ", "GBP", "£"));
        nationArrayList.add(new nationList_Item(R.drawable.cad, "캐나다  ", "CAD", "C$"));
        nationArrayList.add(new nationList_Item(R.drawable.chf, "스위스", "CHF", "CHF"));
        nationArrayList.add(new nationList_Item(R.drawable.sek, "스웨덴  ", "SEK", "kr"));
        nationArrayList.add(new nationList_Item(R.drawable.aud, "호주  ", "AUD", "$"));
        nationArrayList.add(new nationList_Item(R.drawable.nzd, "뉴질랜드  ", "NZD", "$"));
        nationArrayList.add(new nationList_Item(R.drawable.czk, "체코  ", "CZK", "Kč"));
        nationArrayList.add(new nationList_Item(R.drawable.trye, "터키 ", "TRY", "TL"));
        nationArrayList.add(new nationList_Item(R.drawable.mnt, "몽골  ", "MNT", "₮"));
        nationArrayList.add(new nationList_Item(R.drawable.ils, "이스라엘  ", "ILS", "₪"));
        nationArrayList.add(new nationList_Item(R.drawable.dkk, "덴마크  ", "DKK", "kr"));
        nationArrayList.add(new nationList_Item(R.drawable.nok, "노르웨이 ", "NOK", "kr"));
        nationArrayList.add(new nationList_Item(R.drawable.sar, "사우디아라비아", "SAR ", "ر.س"));
        nationArrayList.add(new nationList_Item(R.drawable.kwd, "쿠웨이트  ", "KWD", "د.ك"));
        nationArrayList.add(new nationList_Item(R.drawable.bhd, "바레인  ", "BHD", ".د.ب"));
        nationArrayList.add(new nationList_Item(R.drawable.aed, "아랍에미리트", "AED", "د.إ"));
        nationArrayList.add(new nationList_Item(R.drawable.jod, "요르단  ", "JOD", "JOD"));
        nationArrayList.add(new nationList_Item(R.drawable.egp, "이집트  ", "EGP", "ج.م"));
        nationArrayList.add(new nationList_Item(R.drawable.thb, "태국  ", "THB ", "฿"));
        nationArrayList.add(new nationList_Item(R.drawable.sgd, "싱가포르", "SGD", "S$"));
        nationArrayList.add(new nationList_Item(R.drawable.myr, "말레이시아  ", "MYR", "RM"));
        nationArrayList.add(new nationList_Item(R.drawable.idr, "인도네시아  ", "IDR", "Rp"));
        nationArrayList.add(new nationList_Item(R.drawable.qar, "카타르", "QAR", "ر.ق"));
        nationArrayList.add(new nationList_Item(R.drawable.kzt, "카자흐스탄  ", "KZT", "KZT"));
        nationArrayList.add(new nationList_Item(R.drawable.bnd, "브루나이  ", "BND", "B$"));
        nationArrayList.add(new nationList_Item(R.drawable.inr, "인도  ", "INR", "Rs."));
        nationArrayList.add(new nationList_Item(R.drawable.pkr, "파키스탄", "PKR", "Rs."));
        nationArrayList.add(new nationList_Item(R.drawable.bdt, "방글라데시  ", "BDT", "Tk"));
        nationArrayList.add(new nationList_Item(R.drawable.php, "필리핀", "PHP", "₱"));
        nationArrayList.add(new nationList_Item(R.drawable.mxn, "멕시코  ", "MXN", "$"));
        nationArrayList.add(new nationList_Item(R.drawable.brl, "브라질  ", "BRL", "R$"));
        nationArrayList.add(new nationList_Item(R.drawable.vnd, "베트남  ", "VND", "₫"));
        nationArrayList.add(new nationList_Item(R.drawable.zar, "남아프리카", "ZAR", "R"));
        nationArrayList.add(new nationList_Item(R.drawable.rub, "러시아", "RUB", "руб"));
        nationArrayList.add(new nationList_Item(R.drawable.huf, "헝가리  ", "HUF", "Ft"));
        nationArrayList.add(new nationList_Item(R.drawable.pln, "폴란드", "PLN", "zł"));
    }
}
