package com.ttxg.fruitday.model;

/**
 * 用户中心下方菜单数据model
 * Created by lilijun on 2016/9/9.
 */
public class UserCenterMenu {

    /** 成为会员 */
    public static final String USER_VIP = "user_vip";

    /** 余额 */
    public static final String USER_BALANCE = "user_balance";

    /** 我的积分 */
    public static final String USER_POINT = "user_point";

    /** 安全设置 */
    public static final String USER_SAFE = "user_safe";

    /** 地址管理 */
    public static final String USER_ADDRESS = "user_address";

    /** 优惠券 */
    public static final String USER_COUPONS = "user_coupons";

    /** 客服 */
    public static final String USER_SERVICE = "user_service";


    /** menu 唯一标识, 通过此tag确定跳转的界面或者逻辑操作*/
    private String tag;
    /** menu 图标路径*/
    private String icon;
    /** menu 名称*/
    private String name;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
