package com.sungkyul.project_lte.Community;

/**
 * Created by Hanna on 2016-05-12.
 */
// 여행 후기 수정 모듈 List Item
public class Trip_Edit_Module_Item {
    public String place; // 모듈 장소
    public String content; // 모듈 장소 설명
    public String position; // 모듈 위치
    public String no; // 모듈 번호

    public Trip_Edit_Module_Item(){
        super();
    }

    // Trip_Edit_Module_Item 생성자 매개변수
    public Trip_Edit_Module_Item(String place, String content, String position){
        super();
        this.place = place;
        this.content = content;
        this.position = position;

    }

    // 모듈 번호, 모듈 장소, 모듈 장소 설명, 모듈 위치에 해당하는 getter&setter
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
