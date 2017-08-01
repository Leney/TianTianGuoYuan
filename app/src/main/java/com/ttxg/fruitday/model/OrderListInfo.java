package com.ttxg.fruitday.model;

import java.io.Serializable;

/**
 * 订单列表info对像
 * Created by lilijun on 2016/9/26.
 */
public class OrderListInfo implements Serializable {

    /** 待支付 = 1 */
    public static final int ORDER_STATUS_NEW = 1;
    /** 已付款 = 2 */
    public static final int ORDER_STATUS_PAID = 2;
    /** 已发货 = 3 */
    public static final int ORDER_STATUS_SENT = 3;
    /** 客户签收 = 4*/
    public static final int ORDER_STATUS_SIGN = 4;
    /** 已取消 = 5 */
    public static final int ORDER_STATUS_CANCEL = 5;


    /** 订单id*/
    private int id;
    /** 订单类型(1=我的订单,2=直销订单,4=分销订单)*/
    private int begType = 1;
    /** 订单号*/
    private String orderNo;
    /** 订单序列号*/
    private String serialNumber;
    /** 订单状态 (1待支付,未支付;2已支付,待发货;3,已发货,待收货,4,已收货,完成交易;5,已取消)*/
    private int status;
    /** 此订单所需邮费*/
    private double postage;
    /** 此订单所有商品总价格*/
    private double goodsTotalPrice;
    /** 此订单实际真实需要付款的金额(邮费+商品总价格) */
    private double realTotalPrice;
    /** 商家名称*/
    private String storeName;
    /** 店铺id*/
    private int storeId;
    /** 商家的用户id*/
    private int storeUserId;
    /** 商家用户类型*/
    private int storeUserType;
    /** 订单创建时间*/
    private String createTime;
    /** 是否评价过*/
    private boolean isCommented;
    /** 改价状态(2.确认改价,3取消改价,1.已改价;0.未改价[ 默认 ] )*/
    private int priceModifyStatus;
    /** 支付方式 (1=在线支付,2=货到付款)*/
    private int payType;
    /** 取货方式*/
    private int takeType;
    /** 物流类型(1快递，2EMS,3平邮等)*/
    private int postType;

    /** 商品id*/
    private int goodsId;
    /** 商品icon*/
    private String goodsIcon;
    /** 商品名称*/
    private String goodsName;
    /** 购买商品的数量*/
    private int goodsBuyNum;
    /** 商品价格*/
    private double goodsPrice;


    /** 为了好显示界面  锁添加的属性**/
    /** 是否是第一条数据*/
    private boolean isFirstData;
    /** 是否是最后一条数据*/
    private boolean isLastData;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBegType() {
        return begType;
    }

    public void setBegType(int begType) {
        this.begType = begType;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getPostage() {
        return postage;
    }

    public void setPostage(double postage) {
        this.postage = postage;
    }

    public double getGoodsTotalPrice() {
        return goodsTotalPrice;
    }

    public void setGoodsTotalPrice(double goodsTotalPrice) {
        this.goodsTotalPrice = goodsTotalPrice;
    }

    public double getRealTotalPrice() {
        return realTotalPrice;
    }

    public void setRealTotalPrice(double realTotalPrice) {
        this.realTotalPrice = realTotalPrice;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getStoreUserId() {
        return storeUserId;
    }

    public void setStoreUserId(int storeUserId) {
        this.storeUserId = storeUserId;
    }

    public int getStoreUserType() {
        return storeUserType;
    }

    public void setStoreUserType(int storeUserType) {
        this.storeUserType = storeUserType;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public boolean isCommented() {
        return isCommented;
    }

    public void setCommented(boolean commented) {
        isCommented = commented;
    }

    public int getPriceModifyStatus() {
        return priceModifyStatus;
    }

    public void setPriceModifyStatus(int priceModifyStatus) {
        this.priceModifyStatus = priceModifyStatus;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public int getTakeType() {
        return takeType;
    }

    public void setTakeType(int takeType) {
        this.takeType = takeType;
    }

    public int getPostType() {
        return postType;
    }

    public void setPostType(int postType) {
        this.postType = postType;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsIcon() {
        return goodsIcon;
    }

    public void setGoodsIcon(String goodsIcon) {
        this.goodsIcon = goodsIcon;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public int getGoodsBuyNum() {
        return goodsBuyNum;
    }

    public void setGoodsBuyNum(int goodsBuyNum) {
        this.goodsBuyNum = goodsBuyNum;
    }

    public double getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(double goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public boolean isFirstData() {
        return isFirstData;
    }

    public void setFirstData(boolean firstData) {
        isFirstData = firstData;
    }

    public boolean isLastData() {
        return isLastData;
    }

    public void setLastData(boolean lastData) {
        isLastData = lastData;
    }
}
