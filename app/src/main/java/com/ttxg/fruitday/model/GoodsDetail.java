package com.ttxg.fruitday.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品详情
 * Created by yb on 2016/8/24.
 */
public class GoodsDetail implements Serializable {
    private int id = -1;
    private String name;
    /** 商品编码*/
    private String code;
    /** 所属种类id*/
    private int categoryId;
    /** 详情图片路径集合*/
    private List<String> imgUrls;
    /** 销售数量*/
    private int salesNums;
    /** 库存量*/
    private int stock;
    /** 商品详情介绍的html*/
    private String introduce;
    /** 售卖商品价格*/
    private double price;
    /** 市场价(原价)*/
    private double marketPrice;
    /** 进价*/
    private double factoryPrice = 0.00d;
    /** 商品状态*/
    private int status;

    /** 商品配送类型*/
    private int sendType;
    /** 发货人id*/
    private int fhUserId;
    /** 是否包邮，默认包邮*/
    private boolean isTakeFreight = true;
    /** 当不包邮时，单个商品的邮费(当包邮时，值就为0.00)*/
    private double freight;
    /** 销售等级   1=只以新人价格销售，2=只以会员价格销售，3=既以非会员价格销售又以会员价格销售*/
    private int salesLevel;

//    /** sku商品多个小类型集合*/
//    private List<SkuInfo> skuInfoList = new ArrayList<>();

//    /**
//     * 如果商品只有一种类型时，默认的skuId
//     */
//    private int defaultSkuId;

    /**
     * 当商品没有类型信息时的默认SkuInfo
     */
    private SkuInfo defaultSkuInfo;

    /**
     * 类型1的名称
     */
    private String sku1Name;
    /**
     * 类型2的名称
     */
    private String sku2Name;
    /**
     * 类型1所包含的item值
     */
    private String[] sku1Values;
    /**
     * 类型2所包含的item值
     */
    private String[] sku2Values;

    /**
     * sku1 value 和 sku2Value所组合起来的SkuInfo Map 集合
     * (即：返回结果中 skulist 中的列表数据)
     */
    private Map<String,SkuInfo> skuTotalMap = new HashMap<>();

    /**
     * 标识是否有类型数据
     */
    private boolean isHasSkuInfos = false;

//    /** 商品所属店铺信息*/
//    private Shop shop;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public List<String> getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(List<String> imgUrls) {
        this.imgUrls = imgUrls;
    }

    public int getSalesNums() {
        return salesNums;
    }

    public void setSalesNums(int salesNums) {
        this.salesNums = salesNums;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
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

    public double getFactoryPrice() {
        return factoryPrice;
    }

    public void setFactoryPrice(double factoryPrice) {
        this.factoryPrice = factoryPrice;
    }

    public int getSendType() {
        return sendType;
    }

    public void setSendType(int sendType) {
        this.sendType = sendType;
    }

    public int getFhUserId() {
        return fhUserId;
    }

    public void setFhUserId(int fhUserId) {
        this.fhUserId = fhUserId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isTakeFreight() {
        return isTakeFreight;
    }

    public void setTakeFreight(boolean takeFreight) {
        isTakeFreight = takeFreight;
    }

    public double getFreight() {
        return freight;
    }

    public void setFreight(double freight) {
        this.freight = freight;
    }

//    public int getDefaultSkuId() {
//        return defaultSkuId;
//    }
//
//    public void setDefaultSkuId(int defaultSkuId) {
//        this.defaultSkuId = defaultSkuId;
//    }


    public SkuInfo getDefaultSkuInfo() {
        return defaultSkuInfo;
    }

    public void setDefaultSkuInfo(SkuInfo defaultSkuInfo) {
        this.defaultSkuInfo = defaultSkuInfo;
    }

    public String getSku1Name() {
        return sku1Name;
    }

    public void setSku1Name(String sku1Name) {
        this.sku1Name = sku1Name;
    }

    public String getSku2Name() {
        return sku2Name;
    }

    public void setSku2Name(String sku2Name) {
        this.sku2Name = sku2Name;
    }

    public String[] getSku1Values() {
        return sku1Values;
    }

    public void setSku1Values(String[] sku1Values) {
        this.sku1Values = sku1Values;
    }

    public String[] getSku2Values() {
        return sku2Values;
    }

    public void setSku2Values(String[] sku2Values) {
        this.sku2Values = sku2Values;
    }

    public Map<String, SkuInfo> getSkuTotalMap() {
        return skuTotalMap;
    }

    public boolean isHasSkuInfos() {
        return isHasSkuInfos;
    }

    public void setHasSkuInfos(boolean hasSkuInfos) {
        isHasSkuInfos = hasSkuInfos;
    }

    public int getSalesLevel() {
        return salesLevel;
    }

    public void setSalesLevel(int salesLevel) {
        this.salesLevel = salesLevel;
    }

    //    public List<SkuInfo> getSkuInfoList() {
//        return skuInfoList;
//    }

    //    public Shop getShop() {
//        return shop;
//    }
//
//    public void setShop(Shop shop) {
//        this.shop = shop;
//    }
}
