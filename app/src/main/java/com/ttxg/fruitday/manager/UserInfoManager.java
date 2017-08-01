package com.ttxg.fruitday.manager;

import android.content.Context;

import com.ttxg.fruitday.model.UserInfo;

/**
 * 用户信息管理类
 * Created by lilijun on 2016/9/8.
 */
public class UserInfoManager {
    private static UserInfoManager instance;
    private UserInfo userInfo;
    private UserInfoManager(){}
    public void init(Context context){
//        UserInfo cacheUserInfo = (UserInfo) Util.getCacheDataFromFile(context, Constants.USER_INFO_CANCHE_NAME);
//        if(cacheUserInfo != null){
//            userInfo = cacheUserInfo;
//        }
        userInfo = new UserInfo();
    }
    public static UserInfoManager getInstance(){
        if(instance == null){
            synchronized (UserInfoManager.class){
                instance = new UserInfoManager();
            }
        }
        return instance;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public boolean isValidUserInfo(){
        if(userInfo.getId() == -1){
            // 无效用户
            return false;
        }
        return true;
    }

    /**
     * 清除用户登录信息
     */
    public void clearUserInfo(){
        userInfo = null;
        userInfo = new UserInfo();
    }
}
