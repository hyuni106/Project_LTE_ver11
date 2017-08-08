package com.sungkyul.project_lte.MyPage;

/**
 * Created by Hanna on 2016-05-12.
 */
// 여행 카트 List Item
public class Cart_Module_Item {
    public String place; // 모듈 장소
    public String content; // 모듈 장소 설명
    public String position; // 모듈 위치
    public String no;   //모듈 번호

    public Cart_Module_Item(){
        super();
    }
    // Cart_Module_Item 생성자 매개변수
    public Cart_Module_Item(String place, String content, String position, String no){
        super();
        this.place = place;
        this.content = content;
        this.position = position;
        this.no = no;
    }

    // 모듈 장소, 모듈 장소 설명, 모듈 위치, 모듈 번호에 해당하는 getter & setter
    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

}
