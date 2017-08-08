package com.sungkyul.project_lte.MyPage;

/**
 * 나의 자유게시판 아이템 - 완료
 * Created by suhyu on 2016-05-24.
 */
// 내가 쓴 자유게시판 List Item
public class MyFreeBoardItem {
    public String num;      // 글 번호
    public String title;    // 글 제목
    public String name;     // 글 작성자
    public String date;     // 글 작성날짜
    public String look;     // 글 조회수

    /***
     * MyFreeItem 생성자 매개변수 x
     */
    public MyFreeBoardItem(){
        super();
    }

    /***
     * MyFreeItem 생성자 매개변수 o
     * @param num
     * @param title
     * @param name
     * @param date
     * @param look
     */
    public MyFreeBoardItem(String num, String title, String name, String date, String look){
        super();
        this.num = num;
        this.title = title;
        this.name = name;
        this.date = date;
        this.look = look;
        //this.like = like;
    }

    // 글 번호, 글 제목, 글 작성자, 글 작성날짜, 글 조회수에 해당하는 getter & setter
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
