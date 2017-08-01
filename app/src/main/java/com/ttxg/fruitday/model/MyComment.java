package com.ttxg.fruitday.model;

/**
 * 我的评论列表model
 * Created by lilijun on 2016/10/13.
 */
public class MyComment {
    /**
     * 评价id
     */
    private int id;
    private int goodsId;
    /**
     * 商品名称
     */
    private String goodsName;
    private String goodsIcon;
    private double goodPrice;
    /**
     * 评论的类型   1=好评，2=中评，3=差评
     */
    private int commentType;
    /**
     * 评论内容
     */
    private String content;
    /**
     * 评论时间
     */
    private String commentDate;
    private int rateAspect;
    /**
     * 评论的星级
     */
    private int star;

    private int userId;
    private String userName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsIcon() {
        return goodsIcon;
    }

    public void setGoodsIcon(String goodsIcon) {
        this.goodsIcon = goodsIcon;
    }

    public double getGoodPrice() {
        return goodPrice;
    }

    public void setGoodPrice(double goodPrice) {
        this.goodPrice = goodPrice;
    }

    public int getCommentType() {
        return commentType;
    }

    public void setCommentType(int commentType) {
        this.commentType = commentType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }

    public int getRateAspect() {
        return rateAspect;
    }

    public void setRateAspect(int rateAspect) {
        this.rateAspect = rateAspect;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
