package com.sungkyul.project_lte.SOS;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.sungkyul.project_lte.Etc.CustomExpListView;
import com.sungkyul.project_lte.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParentLevelAdapter extends BaseExpandableListAdapter {
    private final Context mContext;
    private final List<String> mListDataHeader; // 1 LEVEL
    private final Map<String, List<String>> mListData_SecondLevel_Map; // 2 LEVEL
    private final Map<String, List<String>> mListData_ThirdLevel_Map; // 3 LEVEL
    // ParentLevelAdapter 생성자
    public ParentLevelAdapter(Context mContext, List<String> mListDataHeader) {
        this.mContext = mContext;
        this.mListDataHeader = new ArrayList<>();
        this.mListDataHeader.addAll(mListDataHeader); // addAll(mListDataHeader) 통째로 복사
        // SECOND LEVEL
        String[] mItemHeaders; // SECOND LEVEL
        mListData_SecondLevel_Map = new HashMap<>();
        int parentCount = mListDataHeader.size();
        for (int i = 0; i < parentCount; i++) {
            String content = mListDataHeader.get(i);
            switch (content) { // 국가(1 LEVEL)에 따라 대사관 정보(2 LEVEL)를 얻을 수 있음
                case "일본":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_1);
                    break;
                case "뉴질랜드":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_2);
                    break;
                case "통가":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_3);
                    break;
                case "사모아":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_4);
                    break;
                case "중국":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_5);
                    break;
                case "라오스":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_6);
                    break;
                case "호주":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_7);
                    break;
                case "인도":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_8);
                    break;
                case "방글라데시":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_9);
                    break;
                case "브루나이":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_10);
                    break;
                case "싱가포르":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_11);
                    break;
                case "아프가니스탄":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_12);
                    break;
                case "인도네시아":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_13);
                    break;
                case "파키스탄":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_14);
                    break;
                case "피지":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_15);
                    break;
                case "나우루":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_16);
                    break;
                case "마이크로네시아":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_17);
                    break;
                case "마샬군도":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_18);
                    break;
                case "키리바시":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_19);
                    break;
                case "투발루":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_20);
                    break;
                case "부탄":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_21);
                    break;
                case "필리핀":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_22);
                    break;
                case "팔라우":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_23);
                    break;
                case "네팔":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_24);
                    break;
                case "동티모르":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_25);
                    break;
                case "미얀마연방공화국":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_26);
                    break;
                case "베트남":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_27);
                    break;
                case "스리랑카":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_28);
                    break;
                case "캄보디아":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_29);
                    break;
                case "태국":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_30);
                    break;
                case "대만":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_31);
                    break;
                case "파푸아뉴기니":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_32);
                    break;
                case "솔로몬아일랜드":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_33);
                    break;
                case "몽골":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_34);
                    break;
                case "말레이시아":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_35);
                    break;
                case "과테말라":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_36);
                    break;
                case "니카라과":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_37);
                    break;
                case "도미니카공화국":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_38);
                    break;
                case "멕시코":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_39);
                    break;
                case "베네수엘라":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_40);
                    break;
                case "볼리비아":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_41);
                    break;
                case "아르헨티나":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_42);
                    break;
                case "엘살바도르":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_43);
                    break;
                case "온두라스":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_44);
                    break;
                case "칠레":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_45);
                    break;
                case "코스타리카":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_46);
                    break;
                case "페루":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_47);
                    break;
                case "가이아나":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_48);
                    break;
                case "도미니카연방":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_49);
                    break;
                case "바하마":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_50);
                    break;
                case "자메이카":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_51);
                    break;
                case "세인트루시아":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_52);
                    break;
                case "세인트킷츠네비스":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_53);
                    break;
                case "앤티가바부다":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_54);
                    break;
                case "브라질":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_55);
                    break;
                case "에콰도르":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_56);
                    break;
                case "우루과이":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_57);
                    break;
                case "캐나다":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_58);
                    break;
                case "콜롬비아":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_69);
                    break;
                case "주트리니다드토바고":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_60);
                    break;
                case "파나마":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_61);
                    break;
                case "그레나다":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_62);
                    break;
                case "바베이도스":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_63);
                    break;
                case "세인트빈센트그레나딘":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_64);
                    break;
                case "벨리즈":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_65);
                    break;
                case "수리남":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_66);
                    break;
                case "아이티":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_67);
                    break;
                case "쿠바":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_68);
                    break;
                case "미국":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_69);
                    break;
                case "네덜란드":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_70);
                    break;
                case "덴마크":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_71);
                    break;
                case "스페인":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_72);
                    break;
                case "루마니아":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_73);
                    break;
                case "벨기에":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_74);
                    break;
                case "세르비아":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_75);
                    break;
                case "스위스":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_76);
                    break;
                case "슬로바키아":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_77);
                    break;
                case "아제르바이잔":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_78);
                    break;
                case "영국":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_79);
                    break;
                case "우즈베키스탄":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_80);
                    break;
                case "크로아티아":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_81);
                    break;
                case "타지키스탄":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_82);
                    break;
                case "투르크메니스탄":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_83);
                    break;
                case "폴란드":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_84);
                    break;
                case "카자흐스탄":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_85);
                    break;
                case "룩셈부르크":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_86);
                    break;
                case "리히텐슈타인":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_87);
                    break;
                case "모나코":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_88);
                    break;
                case "몰도바":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_89);
                    break;
                case "보스니아-헤르체고비나":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_90);
                    break;
                case "산마리노":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_91);
                    break;
                case "아르메니아":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_92);
                    break;
                case "알바니아":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_93);
                    break;
                case "조지아":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_94);
                    break;
                case "그리스":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_95);
                    break;
                case "노르웨이":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_96);
                    break;
                case "독일":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_97);
                    break;
                case "러시아":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_98);
                    break;
                case "벨라루스":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_99);
                    break;
                case "불가리아":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_100);
                    break;
                case "스웨덴":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_101);
                    break;
                case "아일랜드":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_102);
                    break;
                case "오스트리아":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_103);
                    break;
                case "우크라이나":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_104);
                    break;
                case "키르기스공화국":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_105);
                    break;
                case "터키":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_106);
                    break;
                case "포르투갈":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_107);
                    break;
                case "프랑스":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_108);
                    break;
                case "핀란드":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_109);
                    break;
                case "헝가리":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_110);
                    break;
                case "이탈리아":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_111);
                    break;
                case "체코":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_112);
                    break;
                case "라트비아":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_113);
                    break;
                case "리투아니아":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_114);
                    break;
                case "마케도니아":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_115);
                    break;
                case "몬테네그로":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_116);
                    break;
                case "몰타":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_117);
                    break;
                case "키프로스":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_118);
                    break;
                case "슬로베니아":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_119);
                    break;
                case "아이슬란드":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_120);
                    break;
                case "안도라":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_121);
                    break;
                case "에스토니아":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_122);
                    break;
                case "코소보":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_123);
                    break;
                case "가나":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_124);
                    break;
                case "나이지리아":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_125);
                    break;
                case "레바논":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_126);
                    break;
                case "리비아":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_127);
                    break;
                case "모잠비크":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_128);
                    break;
                case "사우디아라비아":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_129);
                    break;
                case "수단":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_130);
                    break;
                case "알제리":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_131);
                    break;
                case "에티오피아":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_132);
                    break;
                case "오만":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_133);
                    break;
                case "우간다":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_134);
                    break;
                case "이란":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_135);
                    break;
                case "이집트":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_136);
                    break;
                case "짐바브웨":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_137);
                    break;
                case "카타르":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_138);
                    break;
                case "코트디부아르":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_139);
                    break;
                case "쿠웨이트":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_140);
                    break;
                case "튀니지":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_141);
                    break;
                case "감비아":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_142);
                    break;
                case "기니비사우":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_143);
                    break;
                case "나미비아":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_144);
                    break;
                case "레소토":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_145);
                    break;
                case "말라위":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_146);
                    break;
                case "모리셔스":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_147);
                    break;
                case "베냉":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_148);
                    break;
                case "부룬디":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_149);
                    break;
                case "상투메프린시페":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_150);
                    break;
                case "소말리아":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_151);
                    break;
                case "시리아":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_152);
                    break;
                case "에리트리아":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_153);
                    break;
                case "적도기니":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_154);
                    break;
                case "지부티":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_155);
                    break;
                case "카보베르데":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_156);
                    break;
                case "콩고":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_157);
                    break;
                case "가봉":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_158);
                    break;
                case "남아프리카공화국":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_159);
                    break;
                case "르완다":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_160);
                    break;
                case "모로코":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_161);
                    break;
                case "바레인":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_162);
                    break;
                case "세네갈":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_163);
                    break;
                case "아랍에미리트":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_164);
                    break;
                case "앙골라":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_165);
                    break;
                case "예멘":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_166);
                    break;
                case "요르단":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_167);
                    break;
                case "이라크":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_168);
                    break;
                case "이스라엘":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_169);
                    break;
                case "카메룬":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_170);
                    break;
                case "케냐":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_171);
                    break;
                case "콩고민주공화국":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_172);
                    break;
                case "탄자니아":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_173);
                    break;
                case "팔레스타인":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_174);
                    break;
                case "기니":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_175);
                    break;
                case "니제르":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_176);
                    break;
                case "라이베리아":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_177);
                    break;
                case "마다가스카르":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_178);
                    break;
                case "말리":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_179);
                    break;
                case "모리타니":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_180);
                    break;
                case "보츠와나":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_181);
                    break;
                case "부르키나파소":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_182);
                    break;
                case "세이셸":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_183);
                    break;
                case "스와질랜드":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_184);
                    break;
                case "시에라리온":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_185);
                    break;
                case "잠비아":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_186);
                    break;
                case "중앙아프리카공화국":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_187);
                    break;
                case "차드공화국":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_188);
                    break;
                case "코모로":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_189);
                    break;
                case "토고":
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_190);
                    break;



                default:
                    mItemHeaders = mContext.getResources().getStringArray(R.array.array_1);
            }
            mListData_SecondLevel_Map.put(mListDataHeader.get(i), Arrays.asList(mItemHeaders));
        }

        // THIRD LEVEL
        String[] mItemChildOfChild;
        List<String> listChild;
        mListData_ThirdLevel_Map = new HashMap<>();
        for (Object o : mListData_SecondLevel_Map.entrySet()) {
            //  mListData_SecondLevel_Map에 저장된 값을 entry의 형태로 Set에 저장하여 봔환
            Map.Entry entry = (Map.Entry) o;
            Object object = entry.getValue();
            if (object instanceof List) {
                List<String> stringList = new ArrayList<>();
                Collections.addAll(stringList, (String[]) ((List) object).toArray());
                for (int i = 0; i < stringList.size(); i++) {
                       /* mItemChildOfChild = mContext.getResources().getStringArray(R.array.array_1_1);
                        listChild = Arrays.asList(mItemChildOfChild);
                        mListData_ThirdLevel_Map.put(stringList.get(i), listChild);*/
                }
            }
        }



    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // groupPosition과 childPosition을 통해 childList의 원소를 얻어옴
        return childPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) { // ChildList의 ID로 long 형 값을 반환
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        // ChildList의 View. 위 ParentList의 View를 얻을 때와 비슷하게 Layout 연결 후, layout 내 TextView를 연결
        final CustomExpListView secondLevelExpListView = new CustomExpListView(this.mContext);
        String parentNode = (String) getGroup(groupPosition);
        secondLevelExpListView.setAdapter(new SecondLevelAdapter(this.mContext, mListData_SecondLevel_Map.get(parentNode), mListData_ThirdLevel_Map));
        secondLevelExpListView.setGroupIndicator(null);
//        secondLevelExpListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
//            int previousGroup = -1;
//            @Override
//            public void onGroupExpand(int groupPosition) {
//                if (groupPosition != previousGroup)
//                    secondLevelExpListView.collapseGroup(previousGroup);
//                previousGroup = groupPosition;
//            }
//        });
        return secondLevelExpListView;
    }

    @Override
    public int getChildrenCount(int groupPosition) { // ChildList의 크기를 int 형으로 반환
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) { // ParentList의 position을 받아 해당 TextView에 반영될 Object를 반환
        return this.mListDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() { // ParentList의 원소 개수를 반환
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
            // ParentList의 layout 연결. root로 argument 중 parent를 받으며 root로 고정하지는 않음
            convertView = layoutInflater.inflate(R.layout.drawer_list_group, parent, false);
        }
        // ParentList의 Layout 연결 후, 해당 layout 내 TextView를 연결
        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setTextColor(Color.BLACK);
        lblListHeader.setText(headerTitle);
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