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
import com.ttxg.fruitday.gui.view.PasswordInputView;
import com.ttxg.fruitday.manager.UserInfoManager;
import com.ttxg.fruitday.util.Constants;
import com.ttxg.fruitday.util.Util;
import com.ttxg.fruitday.util.log.DLog;
import com.ttxg.fruitday.util.rsa.RSACrypt;

/**
 * 忘记支付密码界面
 * Created by lilijun on 2016/10/27.
 */
public class ResetPayPwdActivity extends BaseActivity implements View
        .OnClickListener {
    /**
     * 获取验证码tag
     */
    private final String GET_CODE_SMS = "sms/sendSms";
    /**
     * 忘记支付密码
     */
    private final String RESET_PAY_PWD = "safe/forgotPayPassword";
    private TextView phoneText;
    private EditText codeInput;
    private CountDownTextView getCodeBtn;
    private Button submitBtn;

    private PasswordInputView payPwdInput, confrimPayPwdInput;

    private ProgressDialog progressDialog;

    @Override
    protected void initView() {
        setTitleName(getResources().getString(R.string.forget_pay_pwd));
        setCenterView(R.layout.activity_reset_pay_pwd);
        phoneText = (TextView) findViewById(R.id.reset_pay_pwd_phone_text);
        codeInput = (EditText) findViewById(R.id.reset_pay_pwd_code_input);
        getCodeBtn = (CountDownTextView) findViewById(R.id.reset_pay_pwd_get_code_btn);
        getCodeBtn.setOnClickListener(this);
        submitBtn = (Button) findViewById(R.id.reset_pay_pwd_submit_btn);
        submitBtn.setOnClickListener(this);
        payPwdInput = (PasswordInputView) findViewById(R.id.reset_pay_pwd_input);
        confrimPayPwdInput = (PasswordInputView) findViewById(R.id.reset_pay_pwd_again_input);

        String phone = UserInfoManager.getInstance().getUserInfo().getPhone();
        String midPhone = phone.substring(3, 7);
        String result = getResources().getString(R.string.phone_num) + phone.replaceFirst(midPhone,
                "****");
        phoneText.setText(result);
        showCenterView();
    }

    @Override
    protected void onLoadDataSuccess(String tag, JSONObject resultObject) {
        super.onLoadDataSuccess(tag, resultObject);
        if (TextUtils.equals(tag, RESET_PAY_PWD)) {
            // 设置新的支付密码成功
            progressDialog.dismiss();
            UserInfoManager.getInstance().getUserInfo().setPayPwd(true);
            Util.showToast(ResetPayPwdActivity.this, getResources().getString(R.string
                    .set_pay_pwd_success));
            finish();
        }
    }

    @Override
    protected void onLoadDataError(String tag, int errorCode, String msg) {
        super.onLoadDataError(tag, errorCode, msg);
        if (TextUtils.equals(tag, RESET_PAY_PWD)) {
            // 设置支付密码失败
            progressDialog.dismiss();
            Util.showErrorMessage(ResetPayPwdActivity.this, msg, getResources().getString(R
                    .string.set_pay_pwd_failed));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.reset_pay_pwd_get_code_btn:
                // 获取验证码
                getCodeBtn.start(60);
                HashMap<String, Object> getCodeParams = new HashMap<>();
                getCodeParams.put("phone", UserInfoManager.getInstance().getUserInfo().getPhone());
                getCodeParams.put("action", Constants.FORGOT_PAY_PWD);
                loadDataGet(GET_CODE_SMS, getCodeParams);
                break;
            case R.id.reset_pay_pwd_submit_btn:
                // 提交
                String code = codeInput.getText().toString().trim();
                if (TextUtils.isEmpty(code) || code.length() < 4) {
                    Util.showToast(ResetPayPwdActivity.this, getResources()
                            .getString(R.string.code_error));
                    return;
                }

                String payPwd = payPwdInput.getText().toString().trim();
                if (TextUtils.isEmpty(payPwd) || payPwd.length() != 6) {
                    Util.showToast(ResetPayPwdActivity.this, getResources()
                            .getString(R.string.pay_pwd_rule));
                    return;
                }

                String confirmPayPwd = confrimPayPwdInput.getText().toString().trim();
                if (!TextUtils.equals(payPwd, confirmPayPwd)) {
                    // 两次密码不一致
                    Util.showToast(ResetPayPwdActivity.this, getResources()
                            .getString(R.string.password_twice_different));
                    return;

                }

                progressDialog = Util.showLoadingDialog(ResetPayPwdActivity.this,
                        progressDialog);
                try {
                    Map<String, String> setParams = new HashMap<>();
                    JSONObject paramsObject = new JSONObject();
                    paramsObject.put("newPayPassword", payPwd);
                    paramsObject.put("code", code);
                    paramsObject.put("validateMobile", UserInfoManager.getInstance().getUserInfo
                            ().getPhone());
                    String params = RSACrypt.encryptByPublicKey(paramsObject.toString().getBytes
                            (), Constants.RSA_PUBLIC_KEY);
                    setParams.put("params", params);
                    loadDataPost(RESET_PAY_PWD, setParams);
                } catch (Exception e) {
                    progressDialog.dismiss();
                    DLog.e("ResetPayPwdActivity", "重置支付密码组建参数时发生异常#excetpion:", e);
                }
                break;
        }
    }

    public static void startActivity(Context context){
        Intent intent = new Intent(context,ResetPayPwdActivity.class);
        context.startActivity(intent);
    }
}
