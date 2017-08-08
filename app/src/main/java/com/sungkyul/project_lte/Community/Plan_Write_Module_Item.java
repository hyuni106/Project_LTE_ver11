package com.sungkyul.project_lte.Community;

/**
 * Created by Hanna on 2016-05-12.
 */
// 여행 계획 글 작성 모듈 List Item
public class Plan_Write_Module_Item {
    public String place; // 모듈 장소
    public String content; // 모듈 장소 설명
    public String position; // 모듈 위치

    public Plan_Write_Module_Item(){
        super();
    }

    // Plan_Write_Module_Item 생성자 매개변수
    public Plan_Write_Module_Item(String place, String content, String position){
        super();
        this.place = place;
        this.content = content;
        this.position = position;

    }

    // 모듈 장소, 모듈 장소 설명, 모듈 위치에 해당하는 getter&setter
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
