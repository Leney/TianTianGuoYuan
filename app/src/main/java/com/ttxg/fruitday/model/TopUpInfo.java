package com.ttxg.fruitday.model;

/**
 * 充值信息info
 * Created by lilijun on 2016/10/24.
 */
public class TopUpInfo {
    /**
     * 充值流水id
     */
    private int id;
    /**
     * 充值方式:1=微信充值,2=充值卡充值
     */
    private int type;
    /**
     * 充值流水号
     */
    private String tradeNo;
    /**
     * 充值金额
     */
    private double money;
    /**
     * 充值时间戳
     */
    private long timestamp;
    /**
     * 充值时间
     */
    private String date;
    /**
     * 充值状态 1=充值成功,0=充值失败
     */
    private int status;
    /**
     * 充值卡id
     */
    private int cartId;
    /**
     * 充值卡号
     */
    private String cartNo;
    /**
     * 充值账户的id
     */
    private int accountId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public String getCartNo() {
        return cartNo;
    }

    public void setCartNo(String cartNo) {
        this.cartNo = cartNo;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }
}
