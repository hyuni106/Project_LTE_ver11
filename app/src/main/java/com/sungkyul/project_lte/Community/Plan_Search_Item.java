package com.sungkyul.project_lte.Community;

/**
 * Created by suhyu on 2016-06-04.
 */
// 여행 계획 검색 List Item
public class Plan_Search_Item {
    public String num; // 글 번호
    public String title; // 글 제목
    public String name; // 글 작성자
    public String date; // 글 작성일
    public String look; // 글 조회수
    public String like; // 글 추천수
    public String country; // 글 국가

    public Plan_Search_Item(){
        super();
    }

    // Plan_Search_Item 생성자 매개변수
    public Plan_Search_Item(String num, String title, String name, String date, String look, String like, String country){
        super();
        this.num = num;
        this.title = title;
        this.name = name;
        this.date = date;
        this.look = look;
        this.like = like;
        this.country = country;
    }
    // 글 국가, 글 작성일, 글 추천수, 글 조회수, 글 작성자, 글 번호, 글 제목에 해당하는 getter&setter
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getLook() {
        return look;
    }

    public void setLook(String look) {
        this.look = look;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
