package com.sungkyul.project_lte.Community;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.sungkyul.project_lte.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hanna on 2016-05-13.
 */
// 여행 커뮤니티 국가 설정 Activity
public class CountrycodeActivity extends ListActivity {

    public static String RESULT_CONTRYCODE = "countrycode";
    public static String countryname = "countryname";
    public String[] countrynames, countrycodes; // 국가 명, 국가 코드 String 배열
    private TypedArray imgs; // 국기 이미지 배열
    private List<Country> countryList; // 국가 리스트

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populateCountryList(); // 국가 List에 배열 값을 넣는 메소드 호출
        ArrayAdapter<Country> adapter = new CountryListArrayAdapter(this, countryList);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Country c = countryList.get(position);
                Intent returnIntent = new Intent();
                returnIntent.putExtra(RESULT_CONTRYCODE, c.getCode());
                returnIntent.putExtra(countryname, c.getName());
                setResult(RESULT_OK, returnIntent);
                imgs.recycle(); //recycle images
                finish();
            }
        });
    }
    // 국가 List에 배열 값을 넣는 메소드
    private void populateCountryList() {
        countryList = new ArrayList<Country>();
        countrynames = getResources().getStringArray(R.array.country_names);
        countrycodes = getResources().getStringArray(R.array.country_codes);
        imgs = getResources().obtainTypedArray(R.array.country_flags);
        for(int i = 0; i < countrycodes.length; i++){
            countryList.add(new Country(countrynames[i], countrycodes[i], imgs.getDrawable(i)));
        }
    }

    public class Country {
        private String name;
        private String code;
        private Drawable flag;
        public Country(String name, String code, Drawable flag){
            this.name = name;
            this.code = code;
            this.flag = flag;
        }
        public String getName() {
            return name;
        }
        public Drawable getFlag() {
            return flag;
        }
        public String getCode() {
            return code;
        }
    }
}