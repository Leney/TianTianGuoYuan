package com.ttxg.fruitday.model;

/**
 * 提交订单商品评论时的单个数据model
 * Created by lilijun on 2016/10/10.
 */
public class OrderComment {
    /**
     * 商品id
     */
    private int goodsId;
    /**
     * 评价类型,好评、中评、差评
     */
    private int rateType;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 评价内容
     */
    private String commentContent;
    /**
     * 评论的星级
     */
    private int star;

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public int getRateType() {
        return rateType;
    }

    public void setRateType(int rateType) {
        this.rateType = rateType;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }
}
