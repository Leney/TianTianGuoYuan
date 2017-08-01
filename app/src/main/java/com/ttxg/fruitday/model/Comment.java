package com.ttxg.fruitday.model;

/**
 * 评论model
 * Created by yb on 2016/8/25.
 */
public class Comment {
    /** 好评类型*/
    public static final int COMMENT_TYPE_GOOD = 1;
    /** 中评类型*/
    public static final int COMMENT_TYPE_NORMAL = COMMENT_TYPE_GOOD + 1;
    /** 差评类型*/
    public static final int COMMENT_TYPE_LOW = COMMENT_TYPE_NORMAL + 1;

    /** 评论类型 1=好评，2=中评，3=差评*/
    private int type;
    /** 评论类型*/
    private String content;
    /** 评论时间*/
    private String date;
    /** 评论星级*/
    private int star;
    /** 评论用户名*/
    private String userName;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
