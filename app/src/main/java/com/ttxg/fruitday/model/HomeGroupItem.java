package com.ttxg.fruitday.model;

import java.io.Serializable;

/**
 * 商品所属类别信息Item
 * Created by yb on 2016/8/22.
 */
public class HomeGroupItem implements Serializable{
    /** 标识热销推荐类型*/
    public static int HOT_TYPE = 1;
    /** 类别id*/
    private int id;
    /** 类别名称*/
    private String name;
    /** 添加类别的日期时间*/
    private String date;
    /** 商品类型*/
    private int type = 0;

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
