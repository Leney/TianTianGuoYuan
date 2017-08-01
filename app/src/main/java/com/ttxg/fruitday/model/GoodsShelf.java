package com.ttxg.fruitday.model;

/**
 * 货架model
 * Created by lilijun on 2016/10/8.
 */
public class GoodsShelf {
    /** 货架id*/
    private int id;
    /** 货架名称*/
    private String name;
    /** 货架是否显示*/
    private boolean isShow;
    /** 创建时间*/
    private String createTime;
    /** 货架上总共有多少件商品*/
    private int goodsNum;
    /** 是否是默认货架 (服务器给到的数据：1=true,0=false)*/
    private boolean isDefault;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(int goodsNum) {
        this.goodsNum = goodsNum;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }
}
