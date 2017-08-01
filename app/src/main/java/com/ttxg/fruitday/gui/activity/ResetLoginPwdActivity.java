package com.ttxg.fruitday.gui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.gui.view.CountDownTextView;
import com.ttxg.fruitday.util.Constants;
import com.ttxg.fruitday.util.Util;
import com.ttxg.fruitday.util.log.DLog;
import com.ttxg.fruitday.util.rsa.RSACrypt;

/**
 * 忘记登陆密码界面
 * Created by lilijun on 2016/10/28.
 */
public class ResetLoginPwdActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 获取验证码tag
     */
    private final String GET_CODE_SMS = "sms/sendSms";
    /**
     * 重置登陆密码
     */
    private final String RESET_LOGIN_PWD = "safe/resetLoginPassword";
    private EditText phoneInput, codeInput, newPwdInput, confirmPwdInput;
    private CountDownTextView getCodeBtn;
    private Button sureBtn;
    /** 返回按钮*/
    private ImageView backImg;

    /**
     * 获取验证码时的手机号
     */
    private String getCodePhone;

    private ProgressDialog progressDialog;

    @Override
    protected void initView() {
        setTitleVisible(false);
        setCenterView(R.layout.activity_reset_login_pwd);
        phoneInput = (EditText) findViewById(R.id.reset_login_pwd_input_phone);
        codeInput = (EditText) findViewById(R.id.reset_login_pwd_input_code);
        newPwdInput = (EditText) findViewById(R.id.reset_login_pwd_input_pwd);
        confirmPwdInput = (EditText) findViewById(R.id.reset_login_pwd_input_pwd_affirm);
        getCodeBtn = (CountDownTextView) findViewById(R.id.reset_login_pwd_get_code_btn);
        getCodeBtn.setOnClickListener(this);
        sureBtn = (Button) findViewById(R.id.reset_login_pwd_sure_btn);
        sureBtn.setOnClickListener(this);
        backImg = (ImageView) findViewById(R.id.reset_login_pwd_title_back_img);
        backImg.setOnClickListener(this);
        showCenterView();
    }

    @Override
    protected void onLoadDataSuccess(String tag, JSONObject resultObject) {
        super.onLoadDataSuccess(tag, resultObject);
        if (TextUtils.equals(tag, RESET_LOGIN_PWD)) {
            // 重置登陆密码成功
            progressDialog.dismiss();
            Util.showToast(ResetLoginPwdActivity.this, getResources().getString(R.string
                    .reset_login_pwd_success));
            finish();
        }
    }

    @Override
    protected void onLoadDataError(String tag, int errorCode, String msg) {
        super.onLoadDataError(tag, errorCode, msg);
        if (TextUtils.equals(tag, RESET_LOGIN_PWD)) {
            // 重置登陆密码失败
            progressDialog.dismiss();
            Util.showErrorMessage(ResetLoginPwdActivity.this, msg, getResources().getString(R.string
                    .reset_login_pwd_failed));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.reset_login_pwd_title_back_img:
                // 返回按钮
                finish();
                break;
            case R.id.reset_login_pwd_get_code_btn:
                // 获取验证码
                getCodePhone = phoneInput.getText().toString().trim();
                if (TextUtils.isEmpty(getCodePhone) || getCodePhone.length() != 11) {
                    // 手机号码位数不符合
                    Util.showToast(ResetLoginPwdActivity.this, getResources().getString(R.string
                            .phone_error));
                    return;
                }
                Map<String, Object> getCodeParams = new HashMap<>();
                getCodeParams.put("phone", getCodePhone);
                getCodeParams.put("action", Constants.FIND_PWD);
                getCodeBtn.start(60);
                loadDataGet(GET_CODE_SMS, getCodeParams);
                break;
            case R.id.reset_login_pwd_sure_btn:
                // 确定按钮
                String phone = phoneInput.getText().toString().trim();
                if (TextUtils.isEmpty(phone) || phone.length() != 11) {
                    // 手机号码位数不符合
                    Util.showToast(ResetLoginPwdActivity.this, getResources().getString(R.string
                            .phone_error));
                    return;
                }

                if (!TextUtils.equals(getCodePhone, phone)) {
                    // 获取验证码和输入的手机号不一致
                    Util.showToast(ResetLoginPwdActivity.this, getResources().getString(R.string
                            .get_code_phone_different_input_phone));
                    return;
                }

                String code = codeInput.getText().toString().trim();
                if (TextUtils.isEmpty(code) || code.length() < 4) {
                    Util.showToast(ResetLoginPwdActivity.this, getResources().getString(R.string
                            .code_error));
                    return;
                }
                String password = newPwdInput.getText().toString().trim();
                if (TextUtils.isEmpty(password) || password.length() < 6 || password.length() >
                        20) {
                    Util.showToast(ResetLoginPwdActivity.this, getResources().getString(R.string
                            .password_set_hint));
                    return;
                }
                String passwordAffirm = confirmPwdInput.getText().toString().trim();
                if (TextUtils.isEmpty(passwordAffirm) || !passwordAffirm.equals(password)) {
                    Util.showToast(ResetLoginPwdActivity.this, getResources().getString(R.string
                            .password_twice_different));
                    return;
                }

                try {
                    Map<String, String> resetParams = new HashMap<>();
                    JSONObject paramsObject = new JSONObject();
                    paramsObject.put("account", phone);
                    paramsObject.put("code", code);
                    paramsObject.put("oldLoginPassword", "");
                    paramsObject.put("newLoginPassword", password);
                    paramsObject.put("resetType", "2");
                    String params = RSACrypt.encryptByPublicKey(paramsObject.toString().getBytes()
                            , Constants.RSA_PUBLIC_KEY);
                    resetParams.put("params", params);
                    progressDialog = Util.showLoadingDialog(ResetLoginPwdActivity.this,
                            progressDialog);
                    loadDataPost(RESET_LOGIN_PWD, resetParams);
                } catch (Exception e) {
                    DLog.e("RegisterActivity", "注册组建参数时发生异常#excetpion:", e);
                    Util.showToast(ResetLoginPwdActivity.this, getResources().getString(R.string
                            .reset_login_pwd_failed));
                }
                break;
        }
    }


    public static void startActivity(Context context){
        Intent intent = new Intent(context,ResetLoginPwdActivity.class);
        context.startActivity(intent);
    }
}
