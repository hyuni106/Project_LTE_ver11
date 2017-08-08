package com.sungkyul.project_lte.Community;

/**
 * Created by Hanna on 2016-05-20.
 */
// 댓글 List Item
public class Comment_Item {
    public String commenter; // 댓글 작성자
    public String comment; // 댓글 내용
    public String no; // 댓글 번호

    public Comment_Item(){
        super();
    }
    // Comment_Item 생성자 매개변수
    public Comment_Item(String commenter, String comment){
        super();
        this.commenter = commenter;
        this.comment = comment;
    }

    // 댓글 번호, 댓글 작성자, 댓글 내용에 해당하는 getter&setter
    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getCommenter() {
        return commenter;
    }

    public void setCommenter(String commenter) {
        this.commenter = commenter;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
