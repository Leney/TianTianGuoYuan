package com.ttxg.fruitday.model;

import com.iflytek.voiceads.IFLYNativeAd;
import com.iflytek.voiceads.NativeADDataRef;

import java.io.Serializable;

/**
 * 首页类别子商品的信息Item
 * Created by yb on 2016/8/22.
 */
public class Goods implements Serializable{

    /** 商品类型*/
    public static final int GOODS_TYPE = 1;
    /** 商品广告类型*/
    public static final int AD_TYPE = 2;
    /** 热销推荐类型*/
    public static final int HOT_TYPE = 3;

    /** 商品类型*/
    private int type;

    /**
     * 非纯图片商品属性
     */
    /** 商品id*/
    private int id;
    private int homeDetailId;
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
    /** 销售等级   1=只以新人价格销售，2=只以会员价格销售，3=既以非会员价格销售又以会员价格销售*/
    private int salesLevel;
    /** 会员价*/
    private double memberPrice;
    /** 新人价*/
    private double newbiePrice;

    /** 是广告类型时，广告对象*/
    private AdItem adItem;

    /** 标识是否填充过广告*/
    private boolean isFullAd;

//    /** 标识是否上报曝光过*/
//    private boolean isAdExposured;

    /** 科大讯飞信息流广告数据*/
    private NativeADDataRef infoAd;

    /** 科大讯飞信息流广告调度器*/
    private IFLYNativeAd kdxfInfoAdControl;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHomeDetailId() {
        return homeDetailId;
    }

    public void setHomeDetailId(int homeDetailId) {
        this.homeDetailId = homeDetailId;
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

    public AdItem getAdItem() {
        return adItem;
    }

    public void setAdItem(AdItem adItem) {
        this.adItem = adItem;
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

    public boolean isFullAd() {
        return isFullAd;
    }

    public void setFullAd(boolean fullAd) {
        isFullAd = fullAd;
    }

    public NativeADDataRef getInfoAd() {
        return infoAd;
    }

    public void setInfoAd(NativeADDataRef infoAd) {
        this.infoAd = infoAd;
    }

    public IFLYNativeAd getKdxfInfoAdControl() {
        return kdxfInfoAdControl;
    }

    public void setKdxfInfoAdControl(IFLYNativeAd kdxfInfoAdControl) {
        this.kdxfInfoAdControl = kdxfInfoAdControl;
    }

//    public boolean isAdExposured() {
//        return isAdExposured;
//    }
//
//    public void setAdExposured(boolean adExposured) {
//        isAdExposured = adExposured;
//    }
}
