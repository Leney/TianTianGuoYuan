package com.ttxg.fruitday.model;

/**
 * 分类item
 * Created by yb on 2016/8/29.
 */
public class Classify {
    /** 分类id*/
    private int id;
    /** 父类型的类别id*/
    private int pId;
    /** 分类名称*/
    private String name;
    /** logo路径*/
    private String logo;

    /** 是否为选中状态(主要是为了在总类别好显示)*/
    private boolean isSelected;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
