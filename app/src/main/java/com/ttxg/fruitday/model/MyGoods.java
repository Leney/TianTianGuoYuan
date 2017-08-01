package com.ttxg.fruitday.model;

/**
 * 纯商品model
 * Created by lilijun on 2016/8/30.
 */
public class MyGoods {
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
    /** 类别id*/
    private int categoryId;
    /** 供应商id*/
    private int supplierId;
    /** 所属商店的id*/
    private int storeId;
    /** 所属商店的名称*/
    private String storeName;

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

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}
