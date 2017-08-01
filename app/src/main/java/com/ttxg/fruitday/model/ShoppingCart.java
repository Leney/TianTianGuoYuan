package com.ttxg.fruitday.model;

import java.io.Serializable;

/**
 * 购物车model
 * Created by lilijun on 2016/9/14.
 */
public class ShoppingCart implements Serializable {
    /** 店铺所属用户的id*/
    private int storeUserId;
    /** 店铺id*/
    private int storeId;
    /** 店铺名称*/
    private String storeName;
    /** 店铺店主的名称*/
    private String storeUserName;
    /** 店铺店主的用户类型*/
    private int storeType;
    /** 商品id*/
    private int goodsId;
    /** 商品名称*/
    private String goodsName;
    /** 商品图片地址*/
    private String goodsIcon;
    /** 商品类型*/
    private int goodsType;
    /** 商品状态*/
    private int goodsStatus;
    /** 发货的用户id*/
    private int fhUserId;
    /** 优惠码*/
    private int promoCode;
    /** 最终成交价格*/
    private double finalPrice;
    /** 购买价格*/
    private double buyPrice;
    /** 市场价格*/
    private double marketPrice;
    /** 总价格(购买数量 * 单价)*/
    private double totalPrice;
    /** 出厂价*/
    private double factoryPrice;
    /** 进货价*/
    private double stockPrice;
    /** 销售数量*/
    private int salesNum;
    /** 购买数量*/
    private int buyNum;
    /** 库存数量*/
    private int quantityNum;

    /** 是否包邮*/
    private boolean isFreeShipping;
    /** 所需邮费(商品数量 * 单件商品邮费价格)*/
    private double postage;
    /** 单价商品邮费价格*/
    private double singlePostage;


    /** sku id*/
    private int skuId;
    /** sku1 name*/
    private String sku1Name;
    /** sku1 value*/
    private String sku1Value;
    /** sku2 name*/
    private String sku2Name;
    /** sku2 value*/
    private String sku2Value;

    public int getStoreUserId() {
        return storeUserId;
    }

    public void setStoreUserId(int storeUserId) {
        this.storeUserId = storeUserId;
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

    public String getStoreUserName() {
        return storeUserName;
    }

    public void setStoreUserName(String storeUserName) {
        this.storeUserName = storeUserName;
    }

    public int getStoreType() {
        return storeType;
    }

    public void setStoreType(int storeType) {
        this.storeType = storeType;
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

    public int getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(int goodsType) {
        this.goodsType = goodsType;
    }

    public int getFhUserId() {
        return fhUserId;
    }

    public void setFhUserId(int fhUserId) {
        this.fhUserId = fhUserId;
    }

    public int getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(int promoCode) {
        this.promoCode = promoCode;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(double finalPrice) {
        this.finalPrice = finalPrice;
    }

    public double getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(double marketPrice) {
        this.marketPrice = marketPrice;
    }

    public double getFactoryPrice() {
        return factoryPrice;
    }

    public void setFactoryPrice(double factoryPrice) {
        this.factoryPrice = factoryPrice;
    }

    public int getSalesNum() {
        return salesNum;
    }

    public void setSalesNum(int salesNum) {
        this.salesNum = salesNum;
    }

    public int getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(int buyNum) {
        this.buyNum = buyNum;
    }

    public int getQuantityNum() {
        return quantityNum;
    }

    public void setQuantityNum(int quantityNum) {
        this.quantityNum = quantityNum;
    }

    public boolean isFreeShipping() {
        return isFreeShipping;
    }

    public void setFreeShipping(boolean freeShipping) {
        isFreeShipping = freeShipping;
    }

    public double getPostage() {
        return postage;
    }

    public void setPostage(double postage) {
        this.postage = postage;
    }

    public double getSinglePostage() {
        return singlePostage;
    }

    public void setSinglePostage(double singlePostage) {
        this.singlePostage = singlePostage;
    }

    public int getSkuId() {
        return skuId;
    }

    public void setSkuId(int skuId) {
        this.skuId = skuId;
    }

    public String getSku1Name() {
        return sku1Name;
    }

    public void setSku1Name(String sku1Name) {
        this.sku1Name = sku1Name;
    }

    public String getSku1Value() {
        return sku1Value;
    }

    public void setSku1Value(String sku1Value) {
        this.sku1Value = sku1Value;
    }

    public String getSku2Name() {
        return sku2Name;
    }

    public void setSku2Name(String sku2Name) {
        this.sku2Name = sku2Name;
    }

    public String getSku2Value() {
        return sku2Value;
    }

    public void setSku2Value(String sku2Value) {
        this.sku2Value = sku2Value;
    }

    public int getGoodsStatus() {
        return goodsStatus;
    }

    public void setGoodsStatus(int goodsStatus) {
        this.goodsStatus = goodsStatus;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getStockPrice() {
        return stockPrice;
    }

    public void setStockPrice(double stockPrice) {
        this.stockPrice = stockPrice;
    }
}
