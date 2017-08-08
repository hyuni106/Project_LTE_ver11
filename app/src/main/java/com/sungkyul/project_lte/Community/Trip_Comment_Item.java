package com.sungkyul.project_lte.Community;

/**
 * Created by suhyu on 2016-05-27.
 */
// 여행 후기 댓글 List Item
public class Trip_Comment_Item {
    public String commenter; // 댓글 작성자 이메일
    public String comment; // 댓글 내용
    public String no; // 댓글 번호
    public String name; // 댓글 작성자 닉네임

    public Trip_Comment_Item(){
        super();
    }

    public Trip_Comment_Item(String commenter, String comment){
        super();
        this.commenter = commenter;
        this.comment = comment;
    }

    // 댓글 작성자 닉네임, 댓글 번호, 댓글 작성자 이메일, 댓글 내용에 해당하는 getter&setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

