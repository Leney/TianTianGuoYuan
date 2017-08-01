package com.ttxg.fruitday.model;

/**
 * 用户账户info
 * Created by lilijun on 2016/10/24.
 */
public class AccountInfo {
    /**
     * 充值账户id
     */
    private int id;
    /**
     * 用户id
     */
    private int userId;
    /**
     * 账户充值累计金额
     */
    private double totalTopUpMoney;
    /**
     * 账户可用余额
     */
    private double remainingMoney;
    /**
     * 账户创建时间
     */
    private String createTime;

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

    public double getTotalTopUpMoney() {
        return totalTopUpMoney;
    }

    public void setTotalTopUpMoney(double totalTopUpMoney) {
        this.totalTopUpMoney = totalTopUpMoney;
    }

    public double getRemainingMoney() {
        return remainingMoney;
    }

    public void setRemainingMoney(double remainingMoney) {
        this.remainingMoney = remainingMoney;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
