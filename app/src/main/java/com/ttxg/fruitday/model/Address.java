package com.ttxg.fruitday.model;

import java.io.Serializable;

/**
 * 地址model
 * Created by lilijun on 2016/9/19.
 */
public class Address implements Serializable{
    /** 地址id*/
    private int id;
    /** 收货人/发货人名称*/
    private String name;
    /** 地址详情*/
    private String detailAddress;
    /** 手机号码*/
    private String mobile;
    /** 邮政编码*/
    private String postcode;
    /** 是否是默认收货地址*/
    private boolean isReceiveDefault;
    /** 是否是默认发货地址*/
    private boolean isSendDefault;
    /** 用户id*/
    private int userId;
    /** 创建地址的时间*/
    private String createTime;


    /** 区域地址信息**/
    /** 区域地址id*/
    private int locationId;
    /** 国家*/
    private String country;
    /** 省份*/
    private String province;
    /** 市级名称*/
    private String city;
    /** 城镇名称(区)*/
    private String town;

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

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public boolean isReceiveDefault() {
        return isReceiveDefault;
    }

    public void setReceiveDefault(boolean receiveDefault) {
        isReceiveDefault = receiveDefault;
    }

    public boolean isSendDefault() {
        return isSendDefault;
    }

    public void setSendDefault(boolean sendDefault) {
        isSendDefault = sendDefault;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }
}
