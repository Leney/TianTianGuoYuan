package com.ttxg.fruitday.model;

/**
 * 云店铺列表实体类
 * Created by lilijun on 2016/9/1.
 */
public class Store {
    /** 店id*/
    private int id;
    /** 店持有人的用户id*/
    private int userId;
    /** 店名称*/
    private String name;
    /** 距离(单位米)*/
    private int distance;
    /** 距离(单位km)*/
    private String distanceKm;
    /** 店logo url*/
    private String logo;
    /** 类型*/
    private String classifyName;
    /** 标签tag*/
    private String tag;
    /** 是否是周边配送值(1=周边配送，非1=不是周边配送)*/
    private int sendType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(String distanceKm) {
        this.distanceKm = distanceKm;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getClassifyName() {
        return classifyName;
    }

    public void setClassifyName(String classifyName) {
        this.classifyName = classifyName;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getSendType() {
        return sendType;
    }

    public void setSendType(int sendType) {
        this.sendType = sendType;
    }
}
