package com.ttxg.fruitday.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单详情对象
 * Created by lilijun on 2016/9/28.
 */
public class OrderDetailInfo implements Serializable {
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
    /** 订单关闭时间*/
    private String closeTime;
    /** 发货时间*/
    private String consignTime;
    /** 签收时间*/
    private String signTime;
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
    /** 买家留言*/
    private String buyerMessage;

    /**用户地址信息**/
    private Address address = new Address();

    /** 商品信息列表*/
    private List<OrderGoodsInfo> goodsList = new ArrayList<>();


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

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public String getConsignTime() {
        return consignTime;
    }

    public void setConsignTime(String consignTime) {
        this.consignTime = consignTime;
    }

    public String getSignTime() {
        return signTime;
    }

    public void setSignTime(String signTime) {
        this.signTime = signTime;
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

    public String getBuyerMessage() {
        return buyerMessage;
    }

    public void setBuyerMessage(String buyerMessage) {
        this.buyerMessage = buyerMessage;
    }

    public Address getAddress() {
        return address;
    }

    public List<OrderGoodsInfo> getGoodsList() {
        return goodsList;
    }
}
