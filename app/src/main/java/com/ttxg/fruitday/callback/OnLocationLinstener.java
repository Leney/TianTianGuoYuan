package com.ttxg.fruitday.callback;

/**
 * 定位的接口
 * Created by lilijun on 2016/9/2.
 */
public interface OnLocationLinstener {
    /** 定位成功*/
    void onLocationSuccess(double[] locationDetail);
    /** 定位失败*/
    void onLocationFailed();
    /** 没有权限*/
    void noPermisstion();
}
