package com.ttxg.fruitday.model;

import java.io.Serializable;

/**
 * 用户信息类
 * Created by lilijun on 2016/9/8.
 */
public class UserInfo implements Serializable{
    /** 用户id*/
    private int id = -1;
    /** 上级的用户id*/
    private int blongUserId;
    /** 性别 1=男，非1=女*/
    private int sex;
    /** 用户类型*/
    private int type;
    /** 用户头像地址*/
    private String icon;
    /** 用户店铺id*/
    private int storeId;
    /** 是否是云店铺用户*/
    private boolean isCloudUser;
    /** 用户姓名*/
    private String name;
    /** 用户昵称*/
    private String nickName;
    /** 公司名称*/
    private String companyName;
    /** 用户电话,用户绑定的手机号*/
    private String phone;
    /** 用户今日收入*/
    private double todayIncome;
    /** 用户累计收入*/
    private double totalIncome;
    /** 用户今日收徒个数*/
    private int todayApprenticeNum;
    /** 未付款订单数量*/
    private int noPayOrderNum;
    /** 已付款订单数量*/
    private int paiedOrderNum;
    /** 待收货订单数量*/
    private int unTakeOverNum;
    /** 待评价订单数量*/
    private int unCommentNum;
    /** 用户商品数量*/
    private int goodsNum;
    /** 用户资金总额*/
    private double totalFunds;
    /** 用户直销订单数量*/
    private int directOrderNum;
    /** 用户经销订单数量*/
    private int dealerOrderNum;
    /** 用户分销订单数量*/
    private int distributorOrederNum;
    /** 用户拥有经销商数量*/
    private int dealerNum;
    /** 用户拥有分销商数量*/
    private int distributorNum;
    /** 用户拥有的客户数量*/
    private int customerNum;
    /** 用户拥有的评论数量*/
    private int commenterNum;
    /** 是否设置了提现密码*/
    private boolean isWithdrawPwd;
    /** 是否设置了支付密码*/
    private boolean isPayPwd;
    /** 用户的总积分*/
    private int totalIntegral;
    /** 用户当前可用的积分(可用积分)*/
    private int remainingIntegral;
    /** 用户的默认收货地址*/
    private Address defaultReceiveAddress;
    /** 会员等级 0=普通会员(普通用户)，1=铁杆会员，2=白银会员，3=黄金会员，4=白金会员*/
    private int vipLevel;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBlongUserId() {
        return blongUserId;
    }

    public void setBlongUserId(int blongUserId) {
        this.blongUserId = blongUserId;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public boolean isCloudUser() {
        return isCloudUser;
    }

    public void setCloudUser(boolean cloudUser) {
        isCloudUser = cloudUser;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getTodayIncome() {
        return todayIncome;
    }

    public void setTodayIncome(double todayIncome) {
        this.todayIncome = todayIncome;
    }

    public double getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(double totalIncome) {
        this.totalIncome = totalIncome;
    }

    public int getTodayApprenticeNum() {
        return todayApprenticeNum;
    }

    public void setTodayApprenticeNum(int todayApprenticeNum) {
        if (todayApprenticeNum < 0) {
            todayApprenticeNum = 0;
        }
        this.todayApprenticeNum = todayApprenticeNum;
    }

    public int getNoPayOrderNum() {
        return noPayOrderNum;
    }

    public void setNoPayOrderNum(int noPayOrderNum) {
        if (noPayOrderNum < 0) {
            noPayOrderNum = 0;
        }
        this.noPayOrderNum = noPayOrderNum;
    }

    public int getPaiedOrderNum() {
        return paiedOrderNum;
    }

    public void setPaiedOrderNum(int paiedOrderNum) {
        if (paiedOrderNum < 0) {
            paiedOrderNum = 0;
        }
        this.paiedOrderNum = paiedOrderNum;
    }

    public int getUnTakeOverNum() {
        return unTakeOverNum;
    }

    public void setUnTakeOverNum(int unTakeOverNum) {
        if (unTakeOverNum < 0) {
            unTakeOverNum = 0;
        }
        this.unTakeOverNum = unTakeOverNum;
    }

    public int getUnCommentNum() {
        return unCommentNum;
    }

    public void setUnCommentNum(int unCommentNum) {
        if (unCommentNum < 0) {
            unCommentNum = 0;
        }
        this.unCommentNum = unCommentNum;
    }

    public int getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(int goodsNum) {
        if (goodsNum < 0) {
            goodsNum = 0;
        }
        this.goodsNum = goodsNum;
    }

    public double getTotalFunds() {
        return totalFunds;
    }

    public void setTotalFunds(double totalFunds) {
        this.totalFunds = totalFunds;
    }

    public int getDirectOrderNum() {
        return directOrderNum;
    }

    public void setDirectOrderNum(int directOrderNum) {
        if (directOrderNum < 0) {
            directOrderNum = 0;
        }
        this.directOrderNum = directOrderNum;
    }

    public int getDealerOrderNum() {
        return dealerOrderNum;
    }

    public void setDealerOrderNum(int dealerOrderNum) {
        if (dealerOrderNum < 0) {
            dealerOrderNum = 0;
        }
        this.dealerOrderNum = dealerOrderNum;
    }

    public int getDistributorOrederNum() {
        return distributorOrederNum;
    }

    public void setDistributorOrederNum(int distributorOrederNum) {
        if (distributorOrederNum < 0) {
            distributorOrederNum = 0;
        }
        this.distributorOrederNum = distributorOrederNum;
    }

    public int getDealerNum() {
        return dealerNum;
    }

    public void setDealerNum(int dealerNum) {
        if (dealerNum < 0) {
            dealerNum = 0;
        }
        this.dealerNum = dealerNum;
    }

    public int getDistributorNum() {
        return distributorNum;
    }

    public void setDistributorNum(int distributorNum) {
        if (distributorNum < 0) {
            distributorNum = 0;
        }
        this.distributorNum = distributorNum;
    }

    public int getCustomerNum() {
        return customerNum;
    }

    public void setCustomerNum(int customerNum) {
        if (customerNum < 0) {
            customerNum = 0;
        }
        this.customerNum = customerNum;
    }

    public int getCommenterNum() {
        return commenterNum;
    }

    public void setCommenterNum(int commenterNum) {
        if (commenterNum < 0) {
            commenterNum = 0;
        }
        this.commenterNum = commenterNum;
    }

    public boolean isWithdrawPwd() {
        return isWithdrawPwd;
    }

    public void setWithdrawPwd(boolean withdrawPwd) {
        isWithdrawPwd = withdrawPwd;
    }

    public boolean isPayPwd() {
        return isPayPwd;
    }

    public void setPayPwd(boolean payPwd) {
        isPayPwd = payPwd;
    }

    public int getTotalIntegral() {
        return totalIntegral;
    }

    public void setTotalIntegral(int totalIntegral) {
        this.totalIntegral = totalIntegral;
    }

    public int getRemainingIntegral() {
        return remainingIntegral;
    }

    public void setRemainingIntegral(int remainingIntegral) {
        if (remainingIntegral < 0) {
            remainingIntegral = 0;
        }
        this.remainingIntegral = remainingIntegral;
    }

    public Address getDefaultReceiveAddress() {
        return defaultReceiveAddress;
    }

    public void setDefaultReceiveAddress(Address defaultReceiveAddress) {
        this.defaultReceiveAddress = defaultReceiveAddress;
    }

    public int getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(int vipLevel) {
        this.vipLevel = vipLevel;
    }
}
