package com.ttxg.fruitday.model;

/**
 * 纯商品model
 * Created by lilijun on 2016/8/30.
 */
public class PureGoods {
    /** 商品id*/
    private int id;
    /** 商品名称*/
    private String name;
    /** 商品图片url*/
    private String icon;
    /** 商品价格(当前价格)*/
    private double price;
    /** 市场价格(原价)*/
    private double marketPrice;
    /** 商品进价*/
    private double bidPrice;
    /** 评价数量*/
    private int evaluateNums;
    /** 销售数量*/
    private int salesNums;
    /** 库存数量*/
    private int stockNums;
    /** 销售等级   1=只以新人价格销售，2=只以会员价格销售，3=既以非会员价格销售又以会员价格销售*/
    private int salesLevel;
    /** 会员价*/
    private double memberPrice;
    /** 新人价*/
    private double newbiePrice;

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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(double marketPrice) {
        this.marketPrice = marketPrice;
    }

    public double getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(double bidPrice) {
        this.bidPrice = bidPrice;
    }

    public int getEvaluateNums() {
        return evaluateNums;
    }

    public void setEvaluateNums(int evaluateNums) {
        this.evaluateNums = evaluateNums;
    }

    public int getSalesNums() {
        return salesNums;
    }

    public void setSalesNums(int salesNums) {
        this.salesNums = salesNums;
    }

    public int getStockNums() {
        return stockNums;
    }

    public void setStockNums(int stockNums) {
        this.stockNums = stockNums;
    }

    public int getSalesLevel() {
        return salesLevel;
    }

    public void setSalesLevel(int salesLevel) {
        this.salesLevel = salesLevel;
    }

    public double getMemberPrice() {
        return memberPrice;
    }

    public void setMemberPrice(double memberPrice) {
        this.memberPrice = memberPrice;
    }

    public double getNewbiePrice() {
        return newbiePrice;
    }

    public void setNewbiePrice(double newbiePrice) {
        this.newbiePrice = newbiePrice;
    }
}
