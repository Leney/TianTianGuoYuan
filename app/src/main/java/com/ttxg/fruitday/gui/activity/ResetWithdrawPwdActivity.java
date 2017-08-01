package com.ttxg.fruitday.gui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.gui.view.CountDownTextView;
import com.ttxg.fruitday.manager.UserInfoManager;
import com.ttxg.fruitday.util.Constants;
import com.ttxg.fruitday.util.Util;
import com.ttxg.fruitday.util.log.DLog;
import com.ttxg.fruitday.util.rsa.RSACrypt;

/**
 * 忘记提现密码界面
 * Created by lilijun on 2016/10/25.
 */
public class ResetWithdrawPwdActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 获取验证码tag
     */
    private final String GET_CODE_SMS = "sms/sendSms";
    /**
     * 重置提现密码
     */
    private final String RESET_WITHDRAW_PWD = "safe/safeSet";
    private TextView phoneText;
    private EditText codeInput, newPwdInput, newPwdConfirmInput;
    private CountDownTextView getCodeBtn;
    private Button sureBtn, cancelBtn;

    private ProgressDialog progressDialog;

    @Override
    protected void initView() {
        setTitleName(getResources().getString(R.string.forget_withdraw_pwd));
        setCenterView(R.layout.activity_forget_pwd);
        phoneText = (TextView) findViewById(R.id.forget_pwd_phone);
        codeInput = (EditText) findViewById(R.id.forget_pwd_code_input);
        newPwdInput = (EditText) findViewById(R.id.forget_pwd_new_pwd_input);
        newPwdConfirmInput = (EditText) findViewById(R.id.forget_pwd_new_pwd_confirm_input);

        getCodeBtn = (CountDownTextView) findViewById(R.id.forget_pwd_get_code_btn);
        getCodeBtn.setOnClickListener(this);
        sureBtn = (Button) findViewById(R.id.forget_pwd_sure);
        sureBtn.setOnClickListener(this);
        cancelBtn = (Button) findViewById(R.id.forget_pwd_cancel);
        cancelBtn.setOnClickListener(this);

        phoneText.setText(UserInfoManager.getInstance().getUserInfo().getPhone());
        showCenterView();
    }

    @Override
    protected void onLoadDataSuccess(String tag, JSONObject resultObject) {
        super.onLoadDataSuccess(tag, resultObject);
        if (TextUtils.equals(tag, RESET_WITHDRAW_PWD)) {
            // 修改提现密码成功
            progressDialog.dismiss();
            UserInfoManager.getInstance().getUserInfo().setWithdrawPwd(true);
            Util.showToast(ResetWithdrawPwdActivity.this, getResources().getString(R.string
                    .reset_withdraw_pwd_success));
            finish();
        }
    }

    @Override
    protected void onLoadDataError(String tag, int errorCode, String msg) {
        super.onLoadDataError(tag, errorCode, msg);
        if (TextUtils.equals(tag, RESET_WITHDRAW_PWD)) {
            // 修改提现密码失败
            progressDialog.dismiss();
            Util.showErrorMessage(ResetWithdrawPwdActivity.this, msg, getResources().getString
                    (R.string.reset_withdraw_pwd_failed));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.forget_pwd_get_code_btn:
                // 获取验证码
                getCodeBtn.start(60);
                HashMap<String, Object> getCodeParams = new HashMap<>();
                getCodeParams.put("phone", UserInfoManager.getInstance().getUserInfo().getPhone());
                getCodeParams.put("action", Constants.SAFE_PWD_FORGET);
                loadDataGet(GET_CODE_SMS, getCodeParams);
                break;
            case R.id.forget_pwd_sure:
                // 确定
                String code = codeInput.getText().toString().trim();
                if (TextUtils.isEmpty(code) || code.length() < 4) {
                    Util.showToast(ResetWithdrawPwdActivity.this, getResources().getString(R.string
                            .code_error));
                    return;
                }

                String newPwd = newPwdInput.getText().toString().trim();
                if (newPwd.length() < 6) {
                    Util.showToast(ResetWithdrawPwdActivity.this, getResources().getString(R
                            .string.set_withdraw_pwd_rule_tips));
                    return;
                }
                String newPwdConfirm = newPwdConfirmInput.getText().toString().trim();
                if (!TextUtils.equals(newPwd, newPwdConfirm)) {
                    Util.showToast(ResetWithdrawPwdActivity.this, getResources().getString(R
                            .string.password_twice_different));
                    return;
                }
                progressDialog = Util.showLoadingDialog(ResetWithdrawPwdActivity.this,
                        progressDialog);

                try {
                    Map<String, String> modifyParams = new HashMap<>();
                    JSONObject paramsObject = new JSONObject();
                    paramsObject.put("action", "2");
                    paramsObject.put("extractPassword", newPwd);
                    paramsObject.put("oldExtractPassword", "");
                    paramsObject.put("validateMobile", UserInfoManager.getInstance().getUserInfo
                            ().getPhone());
                    paramsObject.put("code", code);
                    paramsObject.put("token", "");
                    String params = RSACrypt.encryptByPublicKey(paramsObject.toString().getBytes
                            (), Constants.RSA_PUBLIC_KEY);
                    modifyParams.put("params", params);
                    loadDataPost(RESET_WITHDRAW_PWD, modifyParams);
                } catch (Exception e) {
                    progressDialog.dismiss();
                    DLog.e("ResetWithdrawPwdActivity", "重置提现密码组建参数时发生异常#excetpion:", e);
                }
                break;
            case R.id.forget_pwd_cancel:
                // 取消
                finish();
                break;
        }
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ResetWithdrawPwdActivity.class);
        context.startActivity(intent);
    }
}
