package com.ttxg.fruitday.gui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
 * 绑定手机号界面
 * Created by lilijun on 2016/10/24.
 */
public class BindingPhoneActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 获取验证码tag
     */
    private final String GET_CODE_SMS = "sms/sendSms";
    /**
     * 提交绑定手机号码
     */
    private final String BINDING_PHONE = "safe/safeSet";

    /**
     * 绑定成功的resultCode
     */
    public static final int BINDING_SUCCESS_RESULT = 100017;

    private EditText phoneInput, codeInput;
    private CountDownTextView getCodeBtn;
    private Button sureBtn, cancelBtn;
    /**
     * 获取验证码时的手机号
     */
    private String phoneNum;

    private ProgressDialog progressDialog;

//    /** 标识是否需要返回resultCode，即跳转到此页的界面需要一个绑定结果*/
//    private boolean isNeedResultCode;

    @Override
    protected void initView() {
        setTitleName(getResources().getString(R.string.binding_phone));
        setCenterView(R.layout.activity_binding_phone);

        phoneInput = (EditText) findViewById(R.id.binding_phone_input_phone);
        codeInput = (EditText) findViewById(R.id.binding_phone_input_code);
        getCodeBtn = (CountDownTextView) findViewById(R.id.binding_phone_get_code_btn);
        getCodeBtn.setOnClickListener(this);
        sureBtn = (Button) findViewById(R.id.binding_phone_sure);
        sureBtn.setOnClickListener(this);
        cancelBtn = (Button) findViewById(R.id.binding_phone_cancel);
        cancelBtn.setOnClickListener(this);

        showCenterView();
    }

    @Override
    protected void onLoadDataSuccess(String tag, JSONObject resultObject) {
        super.onLoadDataSuccess(tag, resultObject);
        if (TextUtils.equals(tag, BINDING_PHONE)) {
            // 绑定手机号码成功
            progressDialog.dismiss();
            // 设置绑定手机号
            UserInfoManager.getInstance().getUserInfo().setPhone(phoneNum);
            Util.showToast(BindingPhoneActivity.this, getResources().getString(R.string
                    .binding_phone_success));
            // 是否需要一个绑定成功的返回结果
            setResult(BINDING_SUCCESS_RESULT);
            finish();
        }
    }

    @Override
    protected void onLoadDataError(String tag, int errorCode, String msg) {
        super.onLoadDataError(tag, errorCode, msg);
        if (TextUtils.equals(tag, BINDING_PHONE)) {
            // 绑定手机号码失败
            progressDialog.dismiss();
            Util.showErrorMessage(BindingPhoneActivity.this, msg, getResources().getString(R
                    .string.binding_failed));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.binding_phone_get_code_btn:
                // 获取验证码
                phoneNum = phoneInput.getText().toString().trim();
                if (TextUtils.isEmpty(phoneNum) || phoneNum.length() != 11) {
                    // 手机号码位数不符合
                    Util.showToast(BindingPhoneActivity.this, getResources().getString(R.string
                            .phone_error));
                    return;
                }
                getCodeBtn.start(60);
                HashMap<String, Object> getCodeParams = new HashMap<>();
                getCodeParams.put("phone", phoneNum);
                getCodeParams.put("action", Constants.NEW_PHONE_BINDING);
                loadDataGet(GET_CODE_SMS, getCodeParams);
                break;
            case R.id.binding_phone_sure:
                // 确定
                String phone = phoneInput.getText().toString().trim();
                if (TextUtils.isEmpty(phone) || phone.length() != 11) {
                    // 手机号码位数不符合
                    Util.showToast(BindingPhoneActivity.this, getResources().getString(R.string
                            .phone_error));
                    return;
                }

                if (!TextUtils.equals(phoneNum, phone)) {
                    // 获取短信验证码的手机号和输入框中的手机号不相同
                    Util.showToast(BindingPhoneActivity.this, getResources().getString(R.string
                            .phone_different));
                    return;
                }

                String code = codeInput.getText().toString().trim();
                if (TextUtils.isEmpty(code) || code.length() < 4) {
                    Util.showToast(BindingPhoneActivity.this, getResources().getString(R.string
                            .code_error));
                    return;
                }

                progressDialog = Util.showLoadingDialog(BindingPhoneActivity.this, progressDialog);

                try {
                    Map<String, String> bindingParams = new HashMap<>();
                    JSONObject paramsObject = new JSONObject();
                    paramsObject.put("action", "4");
                    paramsObject.put("validateMobile", phoneNum);
                    paramsObject.put("code", code);
                    paramsObject.put("extractPassword", "");
                    paramsObject.put("oldExtractPassword", "");
                    paramsObject.put("token", "");
                    String params = RSACrypt.encryptByPublicKey(paramsObject.toString().getBytes
                            (), Constants.RSA_PUBLIC_KEY);
                    bindingParams.put("params", params);
                    loadDataPost(BINDING_PHONE, bindingParams);
                } catch (Exception e) {
                    progressDialog.dismiss();
                    DLog.e("BindingPhoneActivity", "校验手机组建参数时发生异常#excetpion:", e);
                }
                break;
            case R.id.binding_phone_cancel:
                // 取消
                finish();
                break;
        }
    }


    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, BindingPhoneActivity.class);
        activity.startActivityForResult(intent, 10017);
    }
}
