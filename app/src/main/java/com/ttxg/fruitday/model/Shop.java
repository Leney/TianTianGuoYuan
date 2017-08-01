package com.ttxg.fruitday.model;

import java.io.Serializable;

/**
 * 店铺信息
 * Created by yb on 2016/8/24.
 */
public class Shop implements Serializable {
    /** 店铺id*/
    private int id = -1;
    /** 店铺所属用户的用户id*/
    private int userId;
    /** 店铺名称*/
    private String name;
    /** 店铺电话*/
    private String telephone;
    /** 店铺logo*/
    private String logoUrl;
    /** 店铺类型 1=供货商，2/3=经销商，其他=普通用户*/
    private int storeType;

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

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public int getStoreType() {
        return storeType;
    }

    public void setStoreType(int storeType) {
        this.storeType = storeType;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
