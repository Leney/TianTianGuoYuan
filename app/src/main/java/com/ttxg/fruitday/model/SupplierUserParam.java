package com.ttxg.fruitday.model;

import java.io.Serializable;

public class SupplierUserParam implements Serializable{
	private static final long serialVersionUID = 1L;
	private String mobile;			//注册手机号码
	private Integer captchaInput;		//验证码
	private String setPassword;		//注册密码
	private String storeName;		//店铺名称
	private String openid;			//注册openid
	private Integer	userType;		//注册类型
	private String enterpriseId;	//邀请码
	private Integer pid;			//分销商关系id
	private Integer appId;			//来源公众号
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Integer getCaptchaInput() {
		return captchaInput;
	}
	public void setCaptchaInput(Integer captchaInput) {
		this.captchaInput = captchaInput;
	}
	public String getSetPassword() {
		return setPassword;
	}
	public void setSetPassword(String setPassword) {
		this.setPassword = setPassword;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public Integer getUserType() {
		return userType;
	}
	public void setUserType(Integer userType) {
		this.userType = userType;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getEnterpriseId() {
		return enterpriseId;
	}
	public void setEnterpriseId(String enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	public Integer getPid() {
		return pid;
	}
	public void setPid(Integer pid) {
		this.pid = pid;
	}
	public Integer getAppId() {
		return appId;
	}
	public void setAppId(Integer appId) {
		this.appId = appId;
	}
	
}