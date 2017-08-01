package com.ttxg.fruitday.model;

/**
 * 首页推荐标题
 * Created by lilijun on 2016/8/23.
 */
public class RecommendTitle {
    private int id;
    private String name;
    private int type;
    private String date;

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
