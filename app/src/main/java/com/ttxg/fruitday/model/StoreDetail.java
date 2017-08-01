package com.ttxg.fruitday.model;

/**
 * 云店铺详情信息
 * Created by lilijun on 2016/9/6.
 */
public class StoreDetail {
    private int id;
    private String name;
    private String ower;
    private String adress;
    private String discrible;
    private String phone;
    private String logo;
    private int userId;
    /** 纬度*/
    private double lat;
    /** 经度*/
    private double lng;
    /** 是否有 周边闪送 标签*/
    private boolean isSendAroundLab;
    /** 是否有 门店服务 标签*/
    private boolean isStoreServiceLab;

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

    public String getOwer() {
        return ower;
    }

    public void setOwer(String ower) {
        this.ower = ower;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getDiscrible() {
        return discrible;
    }

    public void setDiscrible(String discrible) {
        this.discrible = discrible;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public boolean isSendAroundLab() {
        return isSendAroundLab;
    }

    public void setSendAroundLab(boolean sendAroundLab) {
        isSendAroundLab = sendAroundLab;
    }

    public boolean isStoreServicelab() {
        return isStoreServiceLab;
    }

    public void setStoreServicelab(boolean storeServiceLab) {
        isStoreServiceLab = storeServiceLab;
    }
}
