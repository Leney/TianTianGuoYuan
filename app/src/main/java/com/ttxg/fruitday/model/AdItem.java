package com.ttxg.fruitday.model;

/**
 * 广告 model类
 * Created by yb on 2016/8/19.
 */
public class AdItem {
    /** 楼层商品更多类型*/
    public static final int TYPE_GROUP_MORE = 1;
    /** 会员专卡页类型*/
    public static final int TYPE_VIP_SPC = 2;
    /** 消费充值页类型*/
    public static final int TYPE_TOP_UP = 3;
    /** 商品详情类型*/
    public static final int TYPE_GOODS_DETAIL = 4;
    /** 类目商品页类型*/
    public static final int TYPE_CLASSIFY_GOODS = 5;
    /** 普通静态页(Web)类型*/
    public static final int TYPE_WEB = 6;

    private int id;
    private String icon;
    /** banner跳转的类型*/
    private int type = -1;
    /** banner名称描述*/
    private String name;
    /** banner 日期*/
    private String date;
    /** 跳转的路径（当类型为TYPE_WEB时有效）*/
    private String skipUrl;
    /** 商品id*/
    private int goodsId;
    /** 类型id*/
    private int storeId;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSkipUrl() {
        return skipUrl;
    }

    public void setSkipUrl(String skipUrl) {
        this.skipUrl = skipUrl;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }
}
