package com.sungkyul.project_lte.Community;

/**
 * Created by Hanna on 2016-05-12.
 */
// 여행 후기 List Item
public class Trip_Board_Item {
    public String num; // 글 번호
    public String title; // 글 제목
    public String name; // 글 작성자
    public String date; // 글 작성일
    public String look; // 글 조회수
    public String like; // 글 추천수
    public Trip_Board_Item(){
        super();
    }

    // Trip_Board_Item 생성자 매개변수
    public Trip_Board_Item(String num, String title, String name, String date, String look, String like){
        super();
        this.num = num;
        this.title = title;
        this.name = name;
        this.date = date;
        this.look = look;
        this.like = like;
    }


}
