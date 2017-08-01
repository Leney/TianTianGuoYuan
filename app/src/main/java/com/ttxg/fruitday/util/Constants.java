package com.ttxg.fruitday.util;

/**
 * 常量类
 * Created by lilijun on 2016/8/11.
 */
public class Constants {
    /****
     * 网络部分
     ****/
//    public static String MAIN_URL = "http://xuxinjian.iask.in/hwd/";
//    public static String MAIN_URL = "http://192.168.1.129:8080/hwd/";
//    public static String MAIN_URL = "http://192.168.191.1:8080/hwd/";

//    public static String MAIN_URL = "http://192.168.1.43:8080/yxs/";
//    public static String MAIN_URL = "http://192.168.191.1:8080/yxs/";
    public static String MAIN_URL = "";

    /**
     * 云邦appId
     */
    public static String YB_APP_ID = "76";


    /**
     * 微信后台分配的AppId
     */
//    public static final String WX_APP_ID = "wxd930ea5d5a258f4f";
    public static final String WX_APP_ID = "wxa9ce91980a596169";

    /**
     * 支付宝签约合作者身份ID
     */
//    public static final String PARTNER_ID = "2088421876713999";
    public static final String PARTNER_ID = "2088321048522522";
    /**
     * 支付宝商家账户
     */
//    public static final String SELLER = "hwd@huiwandian.cn";
    public static final String SELLER = "winbon@yeah.net";
    /**
     * 支付宝用户私钥
     */
//    public static final String RSA_PRIVATE =
//
// "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCd5g9S3sSKgY0zavKlDgKdPKCR23aN1HbRRUIXabjSJI1wEGF0zvRJ
// +sGhzldXXJfcc87nR5z02q9QHx99HWElCV
// /BWf3lzDReLm8P4N0knpdV2zEuQWZBd1nwduVejAnqdC8lZM4E9Sv4R2x04JdY+1VKpQ9cX4MXVo4io6sMnQIDAQAB";
//    public static final String RSA_PRIVATE =
// "MIICdAIBADANBgkqhkiG9w0BAQEFAASCAl4wggJaAgEAAoGBAJ3mD1LexIqBjTNq8qUOAp08oJHbdo3UdtFFQhdpuNIkjXAQYXTO9En6waHOV1dcl9xzzudHnPTar1AfH30dYSUJX8FZ/eXMNF4ubw/g3SSel1XbMS5BZkF3WfB25V6MCep0LyVkzgT1K/hHbHTgl1j7VUqlD1xfgxdWjiKjqwydAgMBAAECfybbdunt6HAtcZI1v4De8cXAJcO/Pm2o25Skdp9MZS9Rz3zEizcztup9yykGnnfhUX3vAfvoo7eeGYaeIiGoGT+hGlTPVbEoZ+XaDikvIChpf3Vjps1DeKZVVg1ALt+qVucOJlzByw3rFZQiL5cHbO8rMsIwj4RVE16pXZz5JuUCQQDJKwN4bjy6qW04LreyiwrpPscdzMPJPsy+T6ymmHWTsQHJHCNxL4Hc36UDz/CfrkAPNTjfZfptUgXRa9gWD3jXAkEAyO/SjZW/cqs5DtKUKRfScapaX3wFOheN5cu/ZbH95zooFIMWpcfp2shGMEAuDAWw65Y0xF1eUU65MOAoqwGzqwJAY55fW5YLkMwx/+5ynYHpZ1PlOsaZ9eCZd2zxLJa3Muom3jaG/pfuETjW+KpSmGCr7FwIy+guOVCy/OAQ4+vJhQJBAJ8Wwe23kkyHpDVIJGYkpkm4RrUyzDCiVYaLOo6Zzh00vmb/Iv8+uLivuGKbbdm258yesEiIZBSb76Py1kAOqOECQGxk4SmP5+jLxy1m11xDqGvL35zaxi6wQuMZ76dqPce5dQI09K9qpXDtYsDqP0MoxLsQIe/z5TGcoXHsj8h1QlU=";
//    public static final String RSA_PRIVATE =
// "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCd5g9S3sSKgY0zavKlDgKdPKCR23aN1HbRRUIXabjSJI1wEGF0zvRJ
// +sGhzldXXJfcc87nR5z02q9QHx99HWElCV
// /BWf3lzDReLm8P4N0knpdV2zEuQWZBd1nwduVejAnqdC8lZM4E9Sv4R2x04JdY+1VKpQ9cX4MXVo4io6sMnQIDAQAB";

//    public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMN" +
//            "+cu/Qq96nrMU0\n" +
//            "cqMg6o+vP+Hg6trLfoV1azVORrXbH/dfWGnSCb4UWYakbMfWp3JANf/FLGJ7ZX6Q\n" +
//            "1hPGTTgtX/DzYPTy3wsHcx3w3cMSZdRrJuCES7tNYcJEtbt/fVPk6GK5E3Hp3jRt\n" +
//            "L9uoE+Fbjji5UK8RB0xOOovRUpA9AgMBAAECgYEAkPc1l+vvZx6DmYD4+UqxZXcZ\n" +
//            "MhCtVy3LVn/pzg+Lk9M38yI6182u98rzUEqhd9cPOEOxR0cZULYcXyxb8zH7k74c\n" +
//            "WOOZyyoIF7NR4+6u8poKHNPAuBEE3u746WJAHnbo47RPN/awix27ILR7jOGNucCW\n" +
//            "hPr2LLdJDZg3JyesoKkCQQDxqHZcEM/prnTv55s0g9kQKNYmlnr2spaVjApdKopN\n" +
//            "Sab9gJzGcsz7DewCvEOosiZh2wnjmLarD1tkzt1VeD4jAkEAzxicMFNoGD+yC6cT\n" +
//            "RJ9FwacEXli6gGR0waeXq6cLRhM2apZkGGejX0chbSMATYpxX5NxKHJ4SmCjKg/e\n" +
//            "Z7BuHwJAEu1b39gdQLM+S2/7cwCEpsRcCYett9bC/pGKWkOzs1v6bU5+ePiS33L2\n" +
//            "sjwmnHbflLiALMhQPlOkl9+v7DrPuwJARkC6+0nQDinNZFaRGr5NE5xsxvSij9uE\n" +
//            "IA2SThCkqmjqFM5ftkeyDCg9FqDBAYztWNbTlD0AwXp231XyxIn9UQJAJ4cOR7xu\n" +
//            "wAJl2xcbxJ4Or+mcg9Cv8BWrVae+PoJCGdIpuFpJ3GgnA0/pJ57k69oWPzjG7uo3\n" +
//            "Pufc+/ZOtkmEVA==";
    /**
     * 支付宝appId
     */
    public static final String ALI_APP_ID = "2016072501662775";

    /**
     * 支付宝异步通知地址
     */
    public static final String NOTIFY_URL = "http://xuxinjian.iask.in/hwd/alipay/notify";

    /**
     * 请求发送短信的类型
     */
    /**
     * 注册
     */
    public static final String SIGNUP = "signup";
    /**
     * 登陆
     */
    public static final String SIGNIN = "signin";
    /**
     * 找回密码
     */
    public static final String FIND_PWD = "find_pwd";
//    /**
//     * 变更手机号码
//     */
//    public static final String EDIT_PHONE = "edit_phone";
//    /**
//     * 安全设置-密码设置
//     */
//    public static final String SAFE_SET = "safe_set";
    /**
     * 提现密码找回
     */
    public static final String SAFE_PWD_FORGET = "safe_pwd_forget";
    /**
     * 解绑-验证原手机号
     */
    public static final String OLD_PHONE_BINDING = "old_phone_binding";
    /**
     * 绑定-新手机号绑定
     */
    public static final String NEW_PHONE_BINDING = "new_phone_binding";
//    /**
//     * 提现验证手机号
//     */
//    public static final String SET_CASH_ACCOUNT = "set_cash_account";

    /**
     * 保存微信支付时的订单号  小配置的key
     */
    public static final String WX_PAY_ORDER_NO = "wx_pay_order_no";

    /** 修改余额支付密码 */
    public static final String EDIT_PAY_PWD = "edit_pay_pwd";

    /** 忘记余额支付密码 */
    public static final String FORGOT_PAY_PWD = "forgot_pay_pwd";


    /**
     * RSA 公钥
     */
    public static final String RSA_PUBLIC_KEY =
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCN6sMYMveuC25Eo78WRNrnOu0FzeJsBkHJy6YjLJIRlwCbn0UDiTKbnZhU98nw/GtdJYnzgEp9eSeGDrCsfec3BEyBerSiVEKSvwwW4E7QoHpeG3jYU5puPrk/wh7HFucA7cVrvEB6g6ja5iaM7+w8zaN38ytx0LEvZDB6o36YswIDAQAB";
//    public static String RSA_PUBLIC_KEY = "";

    /**
     * 存储在SharedPreferences里面的key值部分
     */
    /**
     * 以帐号密码方式登录时保存的登录加密数据
     */
    public static final String LOGIN_INFO_BY_PWD = "login_info_by_pwd";
    /**
     * 进入app的次数
     */
    public static final String COME_IN_TIEMS = "come_in_times";
    /**
     * 是否检到过有版本更新
     */
    public static final String HAS_VERSION_UPDATE = "has_version_update";
    /**
     * 如果有版本更新，忽略提醒的次数
     */
    public static final int INGRORN_REMIND_TIME = 10;
//    /**
//     * 上次登录时的SessionID
//     */
//    public static final String SEESION_ID = "user_session_id";


    /**
     * EventBus 消息类型部分
     */
    /**
     * 登陆成功
     */
    public static final String LOGIN_SUCCESS_MSG = "login_success";
//    /**
//     * 登陆失败
//     */
//    public static final String LOGIN_FAILED_MSG = "login_failed";
    /**
     * 加入购物车成功
     */
    public static final String ADD_SHOPPING_CART_SUCCESS = "add_shopping_cart_success";
    /**
     * 提交订单成功
     */
    public static final String SUBMIT_ORDER_SUCCESS = "submit_order_success";
//    /**
//     * 添加收货地址成功
//     */
//    public static final String ADD_ADDRESS_SUCCESS = "add_address_success";
//    /**
//     * 编辑更新收货地址成功
//     */
//    public static final String UPDATE_ADDRESS_SUCCESS = "update_address_success";
    /**
     * 取消订单成功
     */
    public static final String CANCEL_ORDER_SUCCESS = "cancel_order_success";
    /**
     * 确认收货成功
     */
    public static final String CONFIRM_TAKE_GOODS_SUCCESS = "confirm_take_goods_success";
    /**
     * 从 “全部” 页取消订单成功
     */
    public static final String CANCEL_ORDER_SUCCESS_FROM_ALL = "cancel_order_success_from_all";
    /**
     * 从订单详情页取消订单成功
     */
    public static final String CANCEL_ORDER_SUCCESS_FROM_DETAIL =
            "cancel_order_success_from_detail";
    /**
     * 从订单详情页确认收货成功
     */
    public static final String CONFIRM_TAKE_GOODS_SUCCESS_FROM_DETAIL =
            "confirm_take_goods_success_from_detail";
    /**
     * 需要重新获取用户各种数据的消息(比如待付款数量等等，通知用户中心Fragment)
     */
    public static final String NEED_REFRESH_ANY_NUM = "need_refresh_any_num";
    /**
     * 货架商品发生了变化
     */
    public static final String STORE_GOODS_HAVE_MODIFY = "store_goods_have_modify";
//    /**
//     * 用户充值成功的消息
//     */
//    public static final String MSG_USER_TOP_UP_SUCCESS = "msg_user_top_up_success";
    /**
     * 重新刷新用户显示消息
     */
    public static final String REFRESH_USER_INFO_VIEW = "refresh_user_info_view";
//    /**
//     * 支付成功
//     */
//    public static final String WX_PAY_SUCCESS = "wx_pay_success";
//
//    /**
//     * 支付成功(总的通知)
//     */
//    public static final String PAY_SUCCESS = "pay_success";

//    /**
//     * 从订单详情页删除订单成功,通知 非“全部”tab
//     */
//    public static final String DELETE_ORDER_SUCCESS_FROM_DETAIL =
//            "delete_order_success_from_detail";
//    /**
//     * 从订单详情页删除订单成功，通知“全部”tab
//     */
//    public static final String DELETE_ORDER_SUCCESS_FROM_DETAIL_FOR_ALL =
//            "delete_order_success_from_detail_for_all";

    /**
     * 微信支付成功时的类型标识
     */
    public static final int WX_PAY_TYPE = 0;
    /**
     * 支付宝支付成功时的类型标识
     */
    public static final int ALI_PAY_TYPE = 1;
    /**
     * 余额支付成功时的类型标识
     */
    public static final int BALANCE_PAY_TYPE = 2;

    /**
     * 广播Action部分
     */
    /**
     * 停止经销某件商品成功的广播
     */
    public static final String ACTION_STOP_SALES_GOODS_SUCCESS = "com.inyunbang.action" +
            ".ACTION_STOP_SALES_GOODS_SUCCESS";
    /**
     * 确认收货成功的广播
     */
    public static final String ACTION_CONFIRM_TAKE_GOODS_SUCCESS = "com.inyunbang.action" +
            ".ACTION_CONFIRM_TAKE_GOODS_SUCCESS";
    /**
     * 前端支付成功(总的通知,微信和支付宝，未校验订单的)
     */
    public static final String ACTION_PAY_SUCCESS = "com.inyunbang.action.ACTION_PAY_SUCCESS";
    /**
     * 当前端支付完成之后，到我们后台服务器去查询支付信息，能查询到订单支付成功的广播
     */
    public static final String ACTION_GET_PAY_ORDER_INFO_SUCCESS = "com.inyunbang.action" +
            ".ACTION_GET_PAY_ORDER_INFO_SUCCESS";
    /**
     * 当前端支付完成之后，到我们后台服务器去查询支付信息，出现异常需要订单列表等显示订单信息的地方重新获取数据
     */
    public static final String ACTION_GET_PAY_ORDER_INFO_SUCCESS_REGET_DATA = "com.inyunbang" +
            ".action" +
            ".ACTION_GET_PAY_ORDER_INFO_SUCCESS_REGET_DATA";

    /**
     * 其他部分
     */
//    /**
//     * 用户登陆信息缓存文件名
//     */
//    public static final String USER_INFO_CANCHE_NAME = "user_info_canche";


    /**
     * 网络请求错误代码
     */
    /** 登录超时*/
    public static int LOGIN_TIME_OUT = 9903;


    /**
     * 保持会话Cookie相关
     */
    public static final String SET_COOKIE_KEY = "Set-Cookie";
    public static final String COOKIE_KEY = "Cookie";
    //    private static final String SESSION_COOKIE = "sessionid";
    public static final String SESSION_COOKIE = "JSESSIONID";

}
