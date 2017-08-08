package com.sungkyul.project_lte.Calculator;

/**
 * Created by WonHo on 2016-05-10.
 */
// 원화계산기 통화 설정 List Item
public class nationList_Item {
    private int img; // 국가 이미지
    private String nation; // 국가명
    private String exchange; // 통화 코드
    private String exchangeUnit; // 통화 기호

    // nationList_Item 생성자 매개변수
    public nationList_Item(int img, String nation, String exchange, String exchangeUnit) {
        this.img = img;
        this.nation = nation;
        this.exchange = exchange;
        this.exchangeUnit = exchangeUnit;
    }

    // 국가 이미지, 국가명, 통화 코드, 통화 기호에 해당하는 getter&setter
    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getExchangeUnit() {
        return exchangeUnit;
    }

    public void setExchangeUnit(String exchangeUnit) {
        this.exchangeUnit = exchangeUnit;
    }
}
