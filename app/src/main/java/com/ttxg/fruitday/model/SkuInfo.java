package com.ttxg.fruitday.model;

import java.io.Serializable;

/**
 * 商品详情小类型数据信息
 * Created by lilijun on 2016/10/11.
 */
public class SkuInfo implements Serializable {
    /**
     * 小类型id
     */
    private int id;
    /**
     * 商品id
     */
    private int goodsId;
    /**
     * 种类类型名称1
     */
    private String skuName1;
    /**
     * 种类具体的描述值1
     */
    private String skuValue1;
    /**
     * 种类类型名称2
     */
    private String skuName2;
    /**
     * 种类具体的描述值2
     */
    private String skuValue2;
    /**
     * 库存数量
     */
    private int quantity;
    /**
     * 销售数量
     */
    private int salesNum;
    /**
     * 市场价格(原价)
     */
    private double marketPrice;
    /**
     * 售卖价格
     */
    private double price;
    /**
     * 出厂价
     */
    private double factoryPrice;
    /**
     * 新手价格
     */
    private double newbiePrice;
    /**
     * 会员价格
     */
    private double memberPrice;
    private String barCode;
    private boolean isRecommed;
    private boolean isSku;

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

    public String getSkuName1() {
        return skuName1;
    }

    public void setSkuName1(String skuName1) {
        this.skuName1 = skuName1;
    }

    public String getSkuValue1() {
        return skuValue1;
    }

    public void setSkuValue1(String skuValue1) {
        this.skuValue1 = skuValue1;
    }

    public String getSkuName2() {
        return skuName2;
    }

    public void setSkuName2(String skuName2) {
        this.skuName2 = skuName2;
    }

    public String getSkuValue2() {
        return skuValue2;
    }

    public void setSkuValue2(String skuValue2) {
        this.skuValue2 = skuValue2;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getSalesNum() {
        return salesNum;
    }

    public void setSalesNum(int salesNum) {
        this.salesNum = salesNum;
    }

    public double getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(double marketPrice) {
        this.marketPrice = marketPrice;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getFactoryPrice() {
        return factoryPrice;
    }

    public void setFactoryPrice(double factoryPrice) {
        this.factoryPrice = factoryPrice;
    }

    public double getNewbiePrice() {
        return newbiePrice;
    }

    public void setNewbiePrice(double newbiePrice) {
        this.newbiePrice = newbiePrice;
    }

    public double getMemberPrice() {
        return memberPrice;
    }

    public void setMemberPrice(double memberPrice) {
        this.memberPrice = memberPrice;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public boolean isRecommed() {
        return isRecommed;
    }

    public void setRecommed(boolean recommed) {
        isRecommed = recommed;
    }

    public boolean isSku() {
        return isSku;
    }

    public void setSku(boolean sku) {
        isSku = sku;
    }
}
