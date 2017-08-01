package com.ttxg.fruitday.util.rsa;

public class Constants {

	// 网络协议
	public static final String SERVICE_PROTOCOL = "http://";
	// 网络IP地址
	// "semtec-ss1.weedoor.com:8080";
	public static final String SERVICE_IP = SERVICE_PROTOCOL +  "112.74.128.37:8080";
	// 应用路径
	public static final String CONTENT_NAME = SERVICE_IP + "/smartcommunity";
	// 用户路径
	public static final String CONTENT_USER = CONTENT_NAME + "/user";
	// 城市路径
	public static final String CONTENT_CITY = CONTENT_USER + "/city/all";
	// 电话号码
	public static final String CONTENT_CELLPHONE = CONTENT_USER + "/cellphone/";
	// 新用户申请
	public static final String CONTENT_NEW_USER_APPLY = CONTENT_USER + "/applyfor";
	// 验证码
	public static final String CONTENT_VERIFYCODE = "/verifycode";
	// 验证码校验
	public static final String CONTENT_CHECK = CONTENT_USER + CONTENT_VERIFYCODE + "/check";
	// 城市ID
	public static final String CONTENT_CITYAREACODE = CONTENT_USER + "/cityareacode/";
	// 社区
	public static final String CONTENT_COMMUNITIES = "/communities";
	// 社区ID
	public static final String CONTENT_COMMUNITYID = CONTENT_USER + "/communityid/";
	// 楼栋
	public static final String CONTENT_BLOCKS = "/blocks";
	// 楼栋ID
	public static final String CONTENT_BLOCKID = CONTENT_USER + "/blockid/";
	// 单元
	public static final String CONTENT_BUILDINGS = "/buildings";
	// 单元ID
	public static final String CONTENT_BUILDID = CONTENT_USER + "/buildid/";
	// 房间号
	public static final String CONTENT_HOUSES = "/houses";
	// 注册
	public static final String CONTENT_NEW = CONTENT_USER + "/new";
	// 登录后台
	public static final String CONTENT_LOGIN = CONTENT_USER + "/login";
	// 登出后台
	public static final String CONTENT_LOGOUT = CONTENT_USER + "/logout";
	// 修改密码
	public static final String CONTENT_MODIFY = CONTENT_USER + "/password/modify";
	// 找回密码
	public static final String CONTENT_FORGET = CONTENT_USER + "/password/forget";
	// 待业主审批
	public static final String CONTENT_REQUEST = CONTENT_USER + "/setting/list2approved";
	// 审批同意
	public static final String CONTENT_REQUEST_APPROVED = CONTENT_USER + "/setting/approved";
	// 审批拒绝
	public static final String CONTENT_REQUEST_REJECTED = CONTENT_USER + "/setting/rejected";
	// 查看家庭成员接口
	public static final String CONTENT_GOVERN_FAMILY = CONTENT_USER + "/setting/familylist";
	// 删除家庭成员接口
	public static final String CONTENT_GOVERN_DELETE = CONTENT_USER + "/setting/familylist/delete";
	// 设置、获取个人信息接口
	public static final String CONTENT_USERINFO = CONTENT_USER + "/setting/userinfo";
	// 设置是否手机接听模式
	public static final String CONTENT_PHONECALLTAG = CONTENT_USER + "/setting/phonecalltag";
	// 获取用户可用门口机
	public static final String CONTENT_GET_CLOCK = CONTENT_NAME + "/facility/publiclock/get";
	// 版本升级
	public static final String CONTENT_UPDATE = CONTENT_NAME + "/apps/sysparam/paramvalue/AndroidVer";

	// 获取 帮助页面数据
	public static final String CONTENT_HELPICON = CONTENT_NAME + "/apps/sysparam/paramvalue/HelpIco";

	// minefragment 我的 需要加 cellphone
	public static final String MINEFRAGMENT_URL = "http://api.heyzhima.com/webapp/me.html?tel=";

	// 用户注册接口 需要加 cellphone
	public static final String REGIST_URL = "http://api.heyzhima.com/webapi/userSubmit?tel=";

	// 开门后调的webview 需要加 cellphone
	public static final String OPENED_URL = "http://h5.bama77.com/zlp/index.html?tel=";

	public static final String RSA_APP_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCbj4yK43Qf0IYgYtzRIQ3CqCNbKlRGsXTxOe+9Fv7D2snocggT91Vl1aQni04pPUJVTxxeD1iH41K+oF/2oBvEO6HlEfWVlZYfrnXVM0zYF0Y0eB5tkzVgfu8ZAUqMKnWKcJsPFOVGtpgrX2e6MSgCcYa5/k86lxe3zJ7iS/v2SwIDAQAB";

}
