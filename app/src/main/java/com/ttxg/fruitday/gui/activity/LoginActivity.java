package com.ttxg.fruitday.gui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.HashMap;

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.gui.view.CountDownTextView;
import com.ttxg.fruitday.util.Constants;
import com.ttxg.fruitday.util.ParseUtil;
import com.ttxg.fruitday.util.PreferencesUtils;
import com.ttxg.fruitday.util.Util;
import com.ttxg.fruitday.util.log.DLog;
import com.ttxg.fruitday.util.rsa.RSACrypt;

/**
 * 登陆界面
 * Created by lilijun on 2016/9/7.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 获取验证码tag
     */
    private final String GET_CODE_SMS = "sms/sendSms";
    /**
     * 登陆tag
     */
    private final String LOGIN_USER = "supplier/login";
    /**
     * 返回按钮
     */
    private ImageView backImg;
    /**
     * 注册按钮
     */
    private TextView registerBtn;
    /**
     * 账号密码登陆，
     */
    private TextView loginByPwd;
    /**
     * 手机快捷登陆
     */
    private TextView loginByFast;
    /**
     * 获取验证码按钮
     */
    private CountDownTextView getCodeBtn;
    /**
     * 手机号码输入框，密码输入框，验证码输入框
     */
    private TextView phoneInput, pwdInput, codeInput;
//    /**
//     * 密码方式登陆输入部分，手机号快捷登陆输入部分
//     */
//    private LinearLayout pwdInputLay, codeInputLay;
    /**
     * 找回密码
     */
    private TextView findBackPwd;
    /**
     * 登陆按钮
     */
    private Button loginBtn;
//    /**
//     * 第三方账号登陆：qq,微信，新浪，支付宝
//     */
//    private ImageView qqImg, weixinImg, sinaImg, zfbImg;

    /**
     * 是否是用账号密码方式登陆，true=账号密码登陆  false=手机快捷登陆
     */
    private boolean isLoginByPwd = true;

    /**
     * 当使用账号密码登陆方式时保存的传到服务器的登陆信息的加密数据
     */
    private String loginInfoByPwd;

    /**
     * 正在登陆dialog
     */
    private ProgressDialog progressDialog;

    @Override
    protected void initView() {
        setTitleVisible(false);
        setCenterView(R.layout.activity_login);

        backImg = (ImageView) findViewById(R.id.login_title_back_img);
        backImg.setOnClickListener(this);
        registerBtn = (TextView) findViewById(R.id.login_title_register);
        registerBtn.setOnClickListener(this);
        loginByPwd = (TextView) findViewById(R.id.login_login_by_pwd);
        loginByPwd.setSelected(true);
        loginByPwd.setOnClickListener(this);
        loginByFast = (TextView) findViewById(R.id.login_login_by_phone);
        loginByFast.setSelected(false);
        loginByFast.setOnClickListener(this);

        getCodeBtn = (CountDownTextView) findViewById(R.id.login_get_code_btn);
        getCodeBtn.setOnClickListener(this);
        getCodeBtn.setVisibility(View.INVISIBLE);

        phoneInput = (TextView) findViewById(R.id.login_input_phone);
        pwdInput = (TextView) findViewById(R.id.login_input_pwd);
        codeInput = (TextView) findViewById(R.id.login_input_code);
//        pwdInputLay = (LinearLayout) findViewById(R.id.login_login_pwd_lay);
//        pwdInputLay.setVisibility(View.VISIBLE);
//        codeInputLay = (LinearLayout) findViewById(R.id.login_login_code_lay);
//        codeInputLay.setVisibility(View.GONE);
        pwdInput.setVisibility(View.VISIBLE);
        codeInput.setVisibility(View.GONE);

        findBackPwd = (TextView) findViewById(R.id.login_find_back_pwd);
        findBackPwd.setOnClickListener(this);
        loginBtn = (Button) findViewById(R.id.login_login_btn);
        loginBtn.setOnClickListener(this);
//        qqImg = (ImageView) findViewById(R.id.login_by_qq);
//        qqImg.setOnClickListener(this);
//        weixinImg = (ImageView) findViewById(R.id.login_by_weixin);
//        weixinImg.setOnClickListener(this);
//        sinaImg = (ImageView) findViewById(R.id.login_by_sina);
//        sinaImg.setOnClickListener(this);
//        zfbImg = (ImageView) findViewById(R.id.login_by_zhifubao);
//        zfbImg.setOnClickListener(this);

        showCenterView();
    }

    @Override
    protected void onLoadDataSuccess(String tag, JSONObject resultObject) {
        super.onLoadDataSuccess(tag, resultObject);
        if (TextUtils.equals(tag, LOGIN_USER)) {
            progressDialog.dismiss();
            Util.showToast(LoginActivity.this, getResources().getString(R.string.login_success));
            ParseUtil.parseLoginUserInfoResult(resultObject);
            // 将通过账号密码登陆传递给服务器的加密数据保存到SharedPreferences里面
            PreferencesUtils.putString(Constants.LOGIN_INFO_BY_PWD, loginInfoByPwd);
            finish();
        }
    }

    @Override
    protected void onLoadDataError(String tag, int errorCode, String msg) {
        super.onLoadDataError(tag, errorCode, msg);
        if (TextUtils.equals(tag, LOGIN_USER)) {
            progressDialog.dismiss();
            Util.showErrorMessage(LoginActivity.this, msg, getResources().getString(R.string
                    .login_failed));
            // 置空SharedPreferences里面通过账号密码登陆传递给服务器的加密数据
            PreferencesUtils.putString(Constants.LOGIN_INFO_BY_PWD, "");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_title_back_img:
                // 标题返回按钮
                finish();
                break;
            case R.id.login_title_register:
                // 标题注册
                RegisterActivity.startActivity(LoginActivity.this);
                break;
            case R.id.login_login_by_pwd:
                // 账号密码方式登陆
                isLoginByPwd = true;
                loginByPwd.setSelected(true);
                loginByFast.setSelected(false);
                getCodeBtn.setVisibility(View.INVISIBLE);
//                pwdInputLay.setVisibility(View.VISIBLE);
//                codeInputLay.setVisibility(View.GONE);
                pwdInput.setVisibility(View.VISIBLE);
                codeInput.setVisibility(View.GONE);
                break;
            case R.id.login_login_by_phone:
                // 手机快捷登陆
                isLoginByPwd = false;
                loginByFast.setSelected(true);
                loginByPwd.setSelected(false);
                getCodeBtn.setVisibility(View.VISIBLE);
//                pwdInputLay.setVisibility(View.GONE);
//                codeInputLay.setVisibility(View.VISIBLE);
                pwdInput.setVisibility(View.GONE);
                codeInput.setVisibility(View.VISIBLE);
                break;
            case R.id.login_get_code_btn:
                // 快捷登陆方式获取验证码按钮
                String phoneNum = phoneInput.getText().toString().trim();
                if (TextUtils.isEmpty(phoneNum) || phoneNum.length() != 11) {
                    // 手机号码位数不符合
                    Util.showToast(LoginActivity.this, getResources().getString(R.string
                            .phone_error));
                    return;
                }
                getCodeBtn.start(60);
                HashMap<String, Object> getCodeParams = new HashMap<>();
                getCodeParams.put("phone", phoneNum);
                getCodeParams.put("action", Constants.SIGNIN);
                loadDataGet(GET_CODE_SMS, getCodeParams);
                break;
            case R.id.login_login_btn:
                // 登陆按钮
                login();
                break;
            case R.id.login_find_back_pwd:
                // 找回密码
                ResetLoginPwdActivity.startActivity(LoginActivity.this);
                break;
//            case R.id.login_by_qq:
//                // QQ图标
//                break;
//            case R.id.login_by_weixin:
//                // 微信图标
//                break;
//            case R.id.login_by_sina:
//                // 新浪图标
//                break;
//            case R.id.login_by_zhifubao:
//                // 支付宝图标
//                break;
        }
    }

    /**
     * 登陆
     */
    private void login() {
        if (isLoginByPwd) {
            // 账号密码登陆
            String phone = phoneInput.getText().toString().trim();
            if (TextUtils.isEmpty(phone) || phone.length() != 11) {
                // 手机号码位数不符合
                Util.showToast(LoginActivity.this, getResources().getString(R.string.phone_error));
                return;
            }
            String password = pwdInput.getText().toString().trim();
            if (TextUtils.isEmpty(password) || password.length() < 6 || password.length() > 20) {
                Util.showToast(LoginActivity.this, getResources().getString(R.string
                        .password_set_hint));
                return;
            }

            try {
                JSONObject loginObject = new JSONObject();
                loginObject.put("account", phone);
                loginObject.put("password", password);
                loginObject.put("loginType", 1);
                String parms = RSACrypt.encryptByPublicKey(loginObject.toString().getBytes(),
                        Constants.RSA_PUBLIC_KEY);
                HashMap<String, String> loginParams = new HashMap<>();
                loginParams.put("parms", parms);

                loginInfoByPwd = parms;
                progressDialog = Util.showLoadingDialog(LoginActivity.this, progressDialog);

                loadDataPost(LOGIN_USER, loginParams);
            } catch (Exception e) {
                DLog.e("LoginActivity", "登陆时组建参数时发生异常#excetpion:", e);
                Util.showToast(LoginActivity.this, getResources().getString(R.string.login_failed));
            }
        } else {
            // 手机快捷登陆
            String phone = phoneInput.getText().toString().trim();
            if (TextUtils.isEmpty(phone) || phone.length() != 11) {
                // 手机号码位数不符合
                Util.showToast(LoginActivity.this, getResources().getString(R.string.phone_error));
                return;
            }
            String code = codeInput.getText().toString().trim();
            if (TextUtils.isEmpty(code) || code.length() < 4) {
                Util.showToast(LoginActivity.this, getResources().getString(R.string.code_error));
                return;
            }

            try {
                JSONObject loginObject = new JSONObject();
                loginObject.put("account", phone);
                loginObject.put("code", code);
                loginObject.put("loginType", 2);
                String parms = RSACrypt.encryptByPublicKey(loginObject.toString().getBytes(),
                        Constants.RSA_PUBLIC_KEY);
                HashMap<String, String> loginParams = new HashMap<>();
                progressDialog = Util.showLoadingDialog(LoginActivity.this, progressDialog);
                loginParams.put("parms", parms);
                loadDataPost(LOGIN_USER, loginParams);
            } catch (Exception e) {
                DLog.e("LoginActivity", "登陆时组建参数时发生异常#excetpion:", e);
                Util.showToast(LoginActivity.this, getResources().getString(R.string.login_failed));
            }
        }
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
}
