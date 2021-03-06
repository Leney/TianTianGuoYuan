package com.ttxg.fruitday.gui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.manager.UserInfoManager;
import com.ttxg.fruitday.util.Constants;
import com.ttxg.fruitday.util.Util;
import com.ttxg.fruitday.util.log.DLog;
import com.ttxg.fruitday.util.rsa.RSACrypt;

/**
 * 设置提现密码界面
 * Created by lilijun on 2016/10/25.
 */
public class SettingWithdrawPwdActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 设置提现密码
     */
    private final String SET_WITHDRAW_PWD = "safe/safeSet";
    private EditText pwdInput, pwdConfirmInput;
    private Button sureBtn, cancelBtn;

    private ProgressDialog progressDialog;

    @Override
    protected void initView() {
        setTitleName(getResources().getString(R.string.setting_withdraw_pwd));
        setCenterView(R.layout.activity_set_withdraw_pwd);
        pwdInput = (EditText) findViewById(R.id.set_withdraw_pwd_input);
        pwdConfirmInput = (EditText) findViewById(R.id.set_withdraw_pwd_confirm_input);
        sureBtn = (Button) findViewById(R.id.set_withdraw_pwd_sure_btn);
        sureBtn.setOnClickListener(this);
        cancelBtn = (Button) findViewById(R.id.set_withdraw_pwd_cancel_btn);
        cancelBtn.setOnClickListener(this);
        showCenterView();
    }

    @Override
    protected void onLoadDataSuccess(String tag, JSONObject resultObject) {
        super.onLoadDataSuccess(tag, resultObject);
        if (TextUtils.equals(tag, SET_WITHDRAW_PWD)) {
            // 设置提现密码成功
            progressDialog.dismiss();
            UserInfoManager.getInstance().getUserInfo().setWithdrawPwd(true);
            Util.showToast(SettingWithdrawPwdActivity.this, getResources().getString(R.string
                    .setting_withdraw_pwd_success));
            finish();
        }
    }

    @Override
    protected void onLoadDataError(String tag, int errorCode, String msg) {
        super.onLoadDataError(tag, errorCode, msg);
        if (TextUtils.equals(tag, SET_WITHDRAW_PWD)) {
            // 设置提现密码失败
            progressDialog.dismiss();
            Util.showErrorMessage(SettingWithdrawPwdActivity.this, msg, getResources().getString
                    (R.string.setting_withdraw_pwd_failed));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.set_withdraw_pwd_sure_btn:
                // 确认
                String pwd = pwdInput.getText().toString().trim();
                if (pwd.length() < 6) {
                    Util.showToast(SettingWithdrawPwdActivity.this, getResources().getString(R
                            .string.set_withdraw_pwd_rule_tips));
                    return;
                }
                String pwdConfirm = pwdConfirmInput.getText().toString().trim();
                if (!TextUtils.equals(pwd, pwdConfirm)) {
                    Util.showToast(SettingWithdrawPwdActivity.this, getResources().getString(R
                            .string.password_twice_different));
                    return;
                }
                progressDialog = Util.showLoadingDialog(SettingWithdrawPwdActivity.this,
                        progressDialog);

                try {
                    Map<String, String> modifyParams = new HashMap<>();
                    JSONObject paramsObject = new JSONObject();
                    paramsObject.put("action", "1");
                    paramsObject.put("extractPassword", pwd);
                    paramsObject.put("oldExtractPassword", "");
                    paramsObject.put("validateMobile", "");
                    paramsObject.put("code", "");
                    paramsObject.put("token", "");
                    String params = RSACrypt.encryptByPublicKey(paramsObject.toString().getBytes
                            (), Constants.RSA_PUBLIC_KEY);
                    modifyParams.put("params", params);
                    loadDataPost(SET_WITHDRAW_PWD, modifyParams);
                } catch (Exception e) {
                    progressDialog.dismiss();
                    DLog.e("SettingWithdrawPwdActivity", "修改提现密码组建参数时发生异常#excetpion:", e);
                }
                break;
            case R.id.set_withdraw_pwd_cancel_btn:
                // 取消
                finish();
                break;
        }
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, SettingWithdrawPwdActivity.class);
        context.startActivity(intent);
    }
}
