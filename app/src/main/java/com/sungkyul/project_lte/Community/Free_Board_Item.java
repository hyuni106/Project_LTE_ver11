package com.sungkyul.project_lte.Community;

/**
 * Created by Hanna on 2016-05-12.
 */
// 자유게시판 List Item
public class Free_Board_Item {
    public String num; // 글 번호
    public String title; // 글 제목
    public String name; // 글 작성자 닉네임
    public String date; // 글 작성일
    public String look; // 글 조회수
    //public String like;
    public String user_id; // 글 작성자 이메일

    public Free_Board_Item(){
        super();
    }
    // Free_Board_Item 생성자 매개변수
    public Free_Board_Item(String num, String title, String name, String date, String look){
        super();
        this.num = num;
        this.title = title;
        this.name = name;
        this.date = date;
        this.look = look;
        //this.like = like;
    }
    // 글 작성자 이메일, 글 작성일, 글 조회수, 글 작성자 닉네임, 글 번호, 글 제목에 해당하는 getter&setter
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
