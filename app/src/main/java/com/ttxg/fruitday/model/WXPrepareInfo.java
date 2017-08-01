package com.ttxg.fruitday.model;

/**
 * 微信预支付订单的信息model
 * Created by lilijun on 2016/10/14.
 */
public class WXPrepareInfo {
    /** 预支付id*/
    private String prepayId;
    /** 商户号*/
    private String partnerId;
    /** 随机字符串*/
    private String nonceStr;
    /** 时间戳*/
    private String timeStamp;
    private String packageValue;
    /** 签名*/
    private String sign;

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getPackageValue() {
        return packageValue;
    }

    public void setPackageValue(String packageValue) {
        this.packageValue = packageValue;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
