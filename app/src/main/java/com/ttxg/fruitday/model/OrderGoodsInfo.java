package com.ttxg.fruitday.model;

import java.io.Serializable;

/**
 * 订单详情里面的单个商品信息对象
 * Created by lilijun on 2016/9/28.
 */
public class OrderGoodsInfo implements Serializable {
    /** 商品id*/
    private int id;
    /** 名称*/
    private String name;
    /** 单价*/
    private double price;
    /** 购买数量*/
    private int buyNum;
    /** 总价格(数量 * 单价)*/
    private double totalPrice;
    /** 最终需支付的金额*/
    private double realPrice;
    /** 商品图片地址*/
    private String icon;
    /** 商品类型信息*/
    private String skuValues;

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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(int buyNum) {
        this.buyNum = buyNum;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(double realPrice) {
        this.realPrice = realPrice;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSkuValues() {
        return skuValues;
    }

    public void setSkuValues(String skuValues) {
        this.skuValues = skuValues;
    }
}
