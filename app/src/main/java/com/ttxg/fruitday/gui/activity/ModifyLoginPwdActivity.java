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
 * 修改登录密码
 * Created by lilijun on 2016/10/28.
 */
public class ModifyLoginPwdActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 修改提现密码
     */
    private final String MODIFY_LOGIN_PWD = "safe/resetLoginPassword";
    private EditText oldPwdInput, newPwdInput, newPwdConfirmInput;
    private Button sureBtn, cancelBtn;
    private ProgressDialog progressDialog;

    @Override
    protected void initView() {
        setTitleName(getResources().getString(R.string.modify_login_pwd));
        setCenterView(R.layout.activity_modify_login_pwd);
        oldPwdInput = (EditText) findViewById(R.id.modify_login_old_pwd_input);
        newPwdInput = (EditText) findViewById(R.id.modify_login_new_pwd_input);
        newPwdConfirmInput = (EditText) findViewById(R.id.modify_login_new_pwd_confirm_input);
        sureBtn = (Button) findViewById(R.id.modify_login_pwd_sure_btn);
        sureBtn.setOnClickListener(this);
        cancelBtn = (Button) findViewById(R.id.modify_login_pwd_cancel_btn);
        cancelBtn.setOnClickListener(this);
        showCenterView();
    }

    @Override
    protected void onLoadDataSuccess(String tag, JSONObject resultObject) {
        super.onLoadDataSuccess(tag, resultObject);
        if (TextUtils.equals(tag, MODIFY_LOGIN_PWD)) {
            // 修改登录密码成功
            progressDialog.dismiss();
            Util.showToast(ModifyLoginPwdActivity.this, getResources().getString(R.string
                    .modify_login_pwd_success));
            finish();
        }
    }

    @Override
    protected void onLoadDataError(String tag, int errorCode, String msg) {
        super.onLoadDataError(tag, errorCode, msg);
        if (TextUtils.equals(tag, MODIFY_LOGIN_PWD)) {
            // 修改登录密码失败
            progressDialog.dismiss();
            Util.showErrorMessage(ModifyLoginPwdActivity.this, msg, getResources().getString
                    (R.string.modify_login_pwd_failed));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.modify_login_pwd_sure_btn:
                // 确定
                String oldPwd = oldPwdInput.getText().toString().trim();
                if (oldPwd.length() < 6) {
                    Util.showToast(ModifyLoginPwdActivity.this, getResources().getString(R
                            .string.set_login_pwd_rule_old_tips));
                    return;
                }

                String newPwd = newPwdInput.getText().toString().trim();
                if (newPwd.length() < 6) {
                    Util.showToast(ModifyLoginPwdActivity.this, getResources().getString(R
                            .string.set_login_pwd_rule_tips));
                    return;
                }
                String newPwdConfirm = newPwdConfirmInput.getText().toString().trim();
                if (!TextUtils.equals(newPwd, newPwdConfirm)) {
                    Util.showToast(ModifyLoginPwdActivity.this, getResources().getString(R
                            .string.password_twice_different));
                    return;
                }
                progressDialog = Util.showLoadingDialog(ModifyLoginPwdActivity.this,
                        progressDialog);
                try {
                    Map<String, String> resetParams = new HashMap<>();
                    JSONObject paramsObject = new JSONObject();
                    paramsObject.put("account", UserInfoManager.getInstance().getUserInfo()
                            .getPhone());
                    paramsObject.put("code", "");
                    paramsObject.put("oldLoginPassword", oldPwd);
                    paramsObject.put("newLoginPassword", newPwd);
                    paramsObject.put("resetType", "1");
                    String params = RSACrypt.encryptByPublicKey(paramsObject.toString().getBytes()
                            , Constants.RSA_PUBLIC_KEY);
                    resetParams.put("params", params);
                    loadDataPost(MODIFY_LOGIN_PWD, resetParams);
                } catch (Exception e) {
                    DLog.e("RegisterActivity", "注册组建参数时发生异常#excetpion:", e);
                    Util.showToast(ModifyLoginPwdActivity.this, getResources().getString(R.string
                            .reset_login_pwd_failed));
                    progressDialog.dismiss();
                }

                break;
            case R.id.modify_login_pwd_cancel_btn:
                // 取消
                finish();
                break;
        }
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ModifyLoginPwdActivity.class);
        context.startActivity(intent);
    }
}
