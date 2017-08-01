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
 * 修改提现密码界面
 * Created by lilijun on 2016/10/25.
 */
public class ModifyWithdrawPwdActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 修改提现密码
     */
    private final String MODIFY_WITHDRAW_PWD = "safe/safeSet";
    private EditText oldPwdInput, newPwdInput, newPwdConfirmInput;
    private Button sureBtn, cancelBtn;
    private ProgressDialog progressDialog;

    @Override
    protected void initView() {
        setTitleName(getResources().getString(R.string.modify_withdraw_pwd));
        setCenterView(R.layout.activity_modify_withdraw_pwd);
        oldPwdInput = (EditText) findViewById(R.id.modify_withdraw_old_pwd_input);
        newPwdInput = (EditText) findViewById(R.id.modify_withdraw_new_pwd_input);
        newPwdConfirmInput = (EditText) findViewById(R.id.modify_withdraw_new_pwd_confirm_input);
        sureBtn = (Button) findViewById(R.id.modify_withdraw_pwd_sure_btn);
        sureBtn.setOnClickListener(this);
        cancelBtn = (Button) findViewById(R.id.modify_withdraw_pwd_cancel_btn);
        cancelBtn.setOnClickListener(this);
        showCenterView();
    }

    @Override
    protected void onLoadDataSuccess(String tag, JSONObject resultObject) {
        super.onLoadDataSuccess(tag, resultObject);
        if (TextUtils.equals(tag, MODIFY_WITHDRAW_PWD)) {
            // 修改提现密码成功
            progressDialog.dismiss();
            UserInfoManager.getInstance().getUserInfo().setWithdrawPwd(true);
            Util.showToast(ModifyWithdrawPwdActivity.this, getResources().getString(R.string
                    .modify_withdraw_pwd_success));
            finish();
        }
    }

    @Override
    protected void onLoadDataError(String tag, int errorCode, String msg) {
        super.onLoadDataError(tag, errorCode, msg);
        if (TextUtils.equals(tag, MODIFY_WITHDRAW_PWD)) {
            // 修改提现密码失败
            progressDialog.dismiss();
            Util.showErrorMessage(ModifyWithdrawPwdActivity.this, msg, getResources().getString
                    (R.string.modify_withdraw_pwd_failed));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.modify_withdraw_pwd_sure_btn:
                // 确定
                String oldPwd = oldPwdInput.getText().toString().trim();
                if (oldPwd.length() < 6) {
                    Util.showToast(ModifyWithdrawPwdActivity.this, getResources().getString(R
                            .string.set_withdraw_pwd_rule_old_tips));
                    return;
                }

                String newPwd = newPwdInput.getText().toString().trim();
                if (newPwd.length() < 6) {
                    Util.showToast(ModifyWithdrawPwdActivity.this, getResources().getString(R
                            .string.set_withdraw_pwd_rule_tips));
                    return;
                }
                String newPwdConfirm = newPwdConfirmInput.getText().toString().trim();
                if (!TextUtils.equals(newPwd, newPwdConfirm)) {
                    Util.showToast(ModifyWithdrawPwdActivity.this, getResources().getString(R
                            .string.password_twice_different));
                    return;
                }
                progressDialog = Util.showLoadingDialog(ModifyWithdrawPwdActivity.this,
                        progressDialog);
                try {
                    Map<String, String> modifyParams = new HashMap<>();
                    JSONObject paramsObject = new JSONObject();
                    paramsObject.put("action", "1");
                    paramsObject.put("extractPassword", newPwd);
                    paramsObject.put("oldExtractPassword", oldPwd);
                    paramsObject.put("validateMobile", "");
                    paramsObject.put("code", "");
                    paramsObject.put("token", "");
                    String params = RSACrypt.encryptByPublicKey(paramsObject.toString().getBytes
                            (), Constants.RSA_PUBLIC_KEY);
                    modifyParams.put("params", params);
                    loadDataPost(MODIFY_WITHDRAW_PWD, modifyParams);
                } catch (Exception e) {
                    progressDialog.dismiss();
                    DLog.e("ModifyWithdrawPwdActivity", "修改提现密码组建参数时发生异常#excetpion:", e);
                }
                break;
            case R.id.modify_withdraw_pwd_cancel_btn:
                // 取消
                finish();
                break;
        }
    }

    public static void startActivity(Context context){
        Intent intent = new Intent(context,ModifyWithdrawPwdActivity.class);
        context.startActivity(intent);
    }
}
