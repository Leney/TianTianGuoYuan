package com.ttxg.fruitday.gui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.manager.UserInfoManager;
import com.ttxg.fruitday.util.Util;
import com.ttxg.fruitday.util.log.DLog;

/**
 * 安全设置界面
 * Created by lilijun on 2016/10/25.
 */
public class SafetySettingActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 查询用户是否绑定了手机号
     */
    private final String QUERY_BINDING_PHONE = "safe/bind_phone";
    /**
     * 查询是否设置了密码
     */
    private final String QUERY_EXISTS_PWD = "safe/existsPassword";
    //    private LinearLayout bindingLay, modifyLoginPwdLay,settingWithdrawPwdLay,
    // forgetWithdrawPwdLay,
//            settingPayPwdLay, forgetPayPwdLay;
    private LinearLayout bindingLay, modifyLoginPwdLay, settingPayPwdLay, forgetPayPwdLay;
    //    private LinearLayout notifySettingLay;
    private TextView bindingPhone;

    private ProgressDialog progressDialog;

    /**
     * 查询是否设置了密码的类型 1=登录密码，2=提现密码，3：余额支付密码
     */
    private int queryPwdType = 2;

    @Override
    protected void initView() {
        setTitleName(getResources().getString(R.string.safety_setting));
        setCenterView(R.layout.activity_safety_setting);

        bindingLay = (LinearLayout) findViewById(R.id.safety_setting_binding_phone_lay);
        bindingLay.setOnClickListener(this);
        modifyLoginPwdLay = (LinearLayout) findViewById(R.id.safety_setting_modify_login_pwd_lay);
        modifyLoginPwdLay.setOnClickListener(this);
//        settingWithdrawPwdLay = (LinearLayout) findViewById(R.id.safety_setting_withdraw_pwd_lay);
//        settingWithdrawPwdLay.setOnClickListener(this);
//        forgetWithdrawPwdLay = (LinearLayout) findViewById(R.id
//                .safety_setting_forget_withdraw_pwd_lay);
//        forgetWithdrawPwdLay.setOnClickListener(this);
        settingPayPwdLay = (LinearLayout) findViewById(R.id.safety_setting_pay_pwd_lay);
        settingPayPwdLay.setOnClickListener(this);
        forgetPayPwdLay = (LinearLayout) findViewById(R.id.safety_setting_forget_pay_pwd_lay);
        forgetPayPwdLay.setOnClickListener(this);
//        notifySettingLay = (LinearLayout) findViewById(R.id.safety_setting_notify_setting_lay);
//        notifySettingLay.setOnClickListener(this);
        bindingPhone = (TextView) findViewById(R.id.safety_setting_binding_phone);

        if (TextUtils.isEmpty(UserInfoManager.getInstance().getUserInfo().getPhone())) {
            // 如果用户绑定的手机号是空的
            // 则需要手动的去查询用户是否绑定了手机
            loadDataGet(QUERY_BINDING_PHONE, null);
        } else {
            // 有绑定手机号
            String phone = UserInfoManager.getInstance().getUserInfo().getPhone();
            String midPhone = phone.substring(3, 7);
            String formatPhone = phone.replaceFirst(midPhone, "****");
            bindingPhone.setText(String.format(getResources().getString(R.string
                    .format_already_binding_phone), formatPhone));
            showCenterView();
        }
    }

    @Override
    protected void onLoadDataSuccess(String tag, JSONObject resultObject) {
        super.onLoadDataSuccess(tag, resultObject);
        if (TextUtils.equals(tag, QUERY_BINDING_PHONE)) {
            // 查询用户是否绑定手机成功
            String phone = "";
            try {
                phone = resultObject.getString("validateMobile");
                UserInfoManager.getInstance().getUserInfo().setPhone(phone);
            } catch (Exception e) {
                DLog.e("SafetySettingActivity", "查询绑定手机解析出现异常#exception：\n", e);
            }
            if (TextUtils.isEmpty(phone)) {
                // 没有绑定手机号
                bindingPhone.setText(getResources().getString(R.string.not_binding));
                Util.showToast(SafetySettingActivity.this, getResources().getString(R.string
                        .not_binding2));
            } else {
                // 有绑定手机号
                String midPhone = phone.substring(3, 7);
                String formatPhone = phone.replaceFirst(midPhone, "****");
                bindingPhone.setText(String.format(getResources().getString(R.string
                        .format_already_binding_phone), formatPhone));
            }
            showCenterView();
        } else if (TextUtils.equals(tag, QUERY_EXISTS_PWD)) {
            progressDialog.dismiss();
            if (queryPwdType == 2) {
                // 查询是否设置了提现密码成功
                int exists = 0;
                try {
                    exists = resultObject.getInt("exists");
                } catch (Exception e) {
                    DLog.e("SafetySettingActivity", "解析是否设置了提现密码成功时异常#Exception:\n", e);
                }
                UserInfoManager.getInstance().getUserInfo().setWithdrawPwd(exists == 1);
                if (exists == 0) {
                    // 没有设置提现密码
                    // 跳转到设置提现密码界面
                    SettingWithdrawPwdActivity.startActivity(SafetySettingActivity.this);
                } else {
                    // 有设置提现密码
                    // 跳转到修改提现密码界面
                    ModifyWithdrawPwdActivity.startActivity(SafetySettingActivity.this);
                }
            } else if (queryPwdType == 3) {
                // 查询是否设置了支付密码成功
                int exists = 0;
                try {
                    exists = resultObject.getInt("exists");
                } catch (Exception e) {
                    DLog.e("SafetySettingActivity", "解析是否设置了支付密码成功时异常#Exception:\n", e);
                }
                UserInfoManager.getInstance().getUserInfo().setPayPwd(exists == 1);
                if (exists == 0) {
                    // 没有设置支付密码
                    // 跳转到设置支付密码界面
                    SettingPayPwdActivity.startActivity(SafetySettingActivity.this);
                } else {
                    // 有设置支付密码
                    // 跳转到修改支付密码界面
                    ModifyPayPwdActivity.startActivity(SafetySettingActivity.this);
                }
            }
        }
    }

    @Override
    protected void onLoadDataError(String tag, int errorCode, String msg) {
        super.onLoadDataError(tag, errorCode, msg);
        if (TextUtils.equals(tag, QUERY_BINDING_PHONE)) {
            // 查询用户是否绑定手机失败
            showErrorView();
        } else if (TextUtils.equals(tag, QUERY_EXISTS_PWD)) {
            // 查询是否设置了提现密码失败
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == BindingPhoneActivity.BINDING_SUCCESS_RESULT || resultCode ==
                ChangePhoneActivity.BIDING_NEW_PHONE_SUCCESS) {
            // 绑定成功
            // 有绑定手机号
            String phone = UserInfoManager.getInstance().getUserInfo().getPhone();
            String midPhone = phone.substring(3, 7);
            String formatPhone = phone.replaceFirst(midPhone, "****");
            bindingPhone.setText(String.format(getResources().getString(R.string
                    .format_already_binding_phone), formatPhone));
            showCenterView();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.safety_setting_binding_phone_lay:
                // 绑定手机
                if (TextUtils.isEmpty(UserInfoManager.getInstance().getUserInfo().getPhone())) {
                    // 没有绑定手机，跳转到去绑定
                    BindingPhoneActivity.startActivity(SafetySettingActivity.this);
                } else {
                    // 绑定了手机，跳转到更改绑定手机界面
                    ChangePhoneActivity.startActivity(SafetySettingActivity.this);
                }
                break;
            case R.id.safety_setting_modify_login_pwd_lay:
                // 修改登录密码
                ModifyLoginPwdActivity.startActivity(SafetySettingActivity.this);
                break;
//            case R.id.safety_setting_withdraw_pwd_lay:
//                // 设置提现密码
//                if (!UserInfoManager.getInstance().getUserInfo().isWithdrawPwd()) {
//                    // 没有设置提现密码
//                    // 查询是否设置了提现密码
//                    progressDialog = Util.showLoadingDialog(SafetySettingActivity.this,
//                            progressDialog);
//                    Map<String, Object> params = new HashMap<>();
//                    // 查询类型 1=登录密码，2=提现密码，3：余额支付密码
//                    queryPwdType = 2;
//                    params.put("type", queryPwdType);
//                    loadDataGet(QUERY_EXISTS_PWD, params);
//                } else {
//                    // 有设置提现密码
//                    // 跳转到修改提现密码界面
//                    ModifyWithdrawPwdActivity.startActivity(SafetySettingActivity.this);
//                }
//                break;
//            case R.id.safety_setting_forget_withdraw_pwd_lay:
//                // 忘记提现密码
//                if (TextUtils.isEmpty(UserInfoManager.getInstance().getUserInfo().getPhone())) {
//                    // 没有绑定手机，查询是否绑定了手机
//                    loadDataGet(QUERY_BINDING_PHONE, null);
//                } else {
//                    // 绑定了手机，跳转到忘记密码界面
//                    ResetWithdrawPwdActivity.startActivity(SafetySettingActivity.this);
//                }
//                break;
            case R.id.safety_setting_pay_pwd_lay:
                // 设置支付密码
                if (!UserInfoManager.getInstance().getUserInfo().isPayPwd()) {
                    // 没有设置支付密码
                    // 查询是否设置了支付密码
                    progressDialog = Util.showLoadingDialog(SafetySettingActivity.this,
                            progressDialog);
                    Map<String, Object> params = new HashMap<>();
                    // 查询类型 1=登录密码，2=提现密码，3：余额支付密码
                    queryPwdType = 3;
                    params.put("type", queryPwdType);
                    loadDataGet(QUERY_EXISTS_PWD, params);
                } else {
                    // 有设置支付密码
                    // 跳转到修改支付密码界面
                    ModifyPayPwdActivity.startActivity(SafetySettingActivity.this);
                }
                break;
            case R.id.safety_setting_forget_pay_pwd_lay:
                // 忘记支付密码
                ResetPayPwdActivity.startActivity(SafetySettingActivity.this);
                break;
//            case R.id.safety_setting_notify_setting_lay:
//                // 消息提醒设置
//                break;
        }
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, SafetySettingActivity.class);
        context.startActivity(intent);
    }
}
