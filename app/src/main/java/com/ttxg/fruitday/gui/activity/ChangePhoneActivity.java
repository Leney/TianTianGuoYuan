package com.ttxg.fruitday.gui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
 * 验证原手机号
 * Created by lilijun on 2016/10/25.
 */
public class ChangePhoneActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 获取验证码tag
     */
    private final String GET_CODE_SMS = "sms/sendSms";
    /**
     * 校验原手机号 或者 更改新的绑定手机号
     */
    private final String SAFE_SET = "safe/safeSet";
    /**
     * 步骤展示图片
     */
    private ImageView stepImg;

    /**
     * 验证原手机号部分
     */
    private LinearLayout step1Lay;
    private TextView step1Phone;
    private EditText step1CodeInput;
    private CountDownTextView step1GetCodeBtn;
    private Button step1NextBtn;

    /**
     * 绑定新手机部分
     */
    private LinearLayout step2Lay;
    private EditText step2PhoneInput;
    private EditText step2CodeInput;
    private CountDownTextView step2GetCodeBtn;
    private Button step2BindingBtn;

    private ProgressDialog progressDialog;

    /**
     * 标识是否是检测原手机号  true=检测原手机号，false=提交绑定新的手机号
     */
    private boolean isCheckOldPhone = true;
    /**
     * 绑定的新的手机号
     */
    private String bindingNewPhone;

    /**
     * 绑定一个新的手机成功
     */
    public static int BIDING_NEW_PHONE_SUCCESS = 100019;

    /**
     * 校验原手机号时返回的token，需要在绑定新手机号的时候传入绑定新手机号参数
     */
    private String confirmToken;

    @Override
    protected void initView() {
        setTitleName(getResources().getString(R.string.confirm_old_phone));
        setCenterView(R.layout.activity_change_phone);
        stepImg = (ImageView) findViewById(R.id.change_phone_step_img);
        step1Lay = (LinearLayout) findViewById(R.id.confirm_old_phone_lay);
        step1Phone = (TextView) findViewById(R.id.confirm_old_phone_text);
        step1CodeInput = (EditText) findViewById(R.id.confirm_old_phone_code_input);
        step1GetCodeBtn = (CountDownTextView) findViewById(R.id.confirm_old_phone_get_code_btn);
        step1GetCodeBtn.setOnClickListener(this);
        step1NextBtn = (Button) findViewById(R.id.confirm_old_phone_next_btn);
        step1NextBtn.setOnClickListener(this);

        step2Lay = (LinearLayout) findViewById(R.id.binding_new_phone_lay);
        step2PhoneInput = (EditText) findViewById(R.id.binding_new_phone_input);
        step2CodeInput = (EditText) findViewById(R.id.binding_new_phone_code_input);
        step2GetCodeBtn = (CountDownTextView) findViewById(R.id.binding_new_phone_get_code_btn);
        step2GetCodeBtn.setOnClickListener(this);
        step2BindingBtn = (Button) findViewById(R.id.binding_new_phone_sure);
        step2BindingBtn.setOnClickListener(this);

        String phone = UserInfoManager.getInstance().getUserInfo().getPhone();
        String midPhone = phone.substring(3, 7);
        String result = getResources().getString(R.string.phone_num) + phone.replaceFirst(midPhone,
                "****");
        step1Phone.setText(result);
        showCenterView();
    }

    @Override
    protected void onLoadDataSuccess(String tag, JSONObject resultObject) {
        super.onLoadDataSuccess(tag, resultObject);
        if (TextUtils.equals(tag, SAFE_SET)) {
            if (isCheckOldPhone) {
                // 校验原手机成功
                progressDialog.dismiss();

                try {
                    confirmToken = resultObject.getString("token");
                } catch (Exception e) {
                    DLog.e("ChangePhoneActivity", "解析校验原手机号时返回的token异常#Exception:\n", e);
                }
                // 重新设置标题
                setTitleName(getResources().getString(R.string.binding_new_phone));
                // 更改步骤图
                stepImg.setImageResource(R.drawable.step_img2);
                // 隐藏第一步的视图，显示第二步的视图
                step1Lay.setVisibility(View.GONE);
                step2Lay.setVisibility(View.VISIBLE);
            } else {
                // 绑定新的手机号成功
                // 绑定手机号码成功
                progressDialog.dismiss();
                // 设置绑定手机号
                DLog.i("绑定的新手机号---->>" + bindingNewPhone);
                UserInfoManager.getInstance().getUserInfo().setPhone(bindingNewPhone);
                Util.showToast(ChangePhoneActivity.this, getResources().getString(R.string
                        .binding_phone_change_success));
                setResult(BIDING_NEW_PHONE_SUCCESS);
                finish();
            }
        }
    }

    @Override
    protected void onLoadDataError(String tag, int errorCode, String msg) {
        super.onLoadDataError(tag, errorCode, msg);
        if (TextUtils.equals(tag, SAFE_SET)) {
            if (isCheckOldPhone) {
                // 校验原手机失败
                progressDialog.dismiss();
                Util.showErrorMessage(ChangePhoneActivity.this, msg, getResources().getString
                        (R.string.confirm_old_phone_failed));
            } else {
                // 绑定新手机失败
                progressDialog.dismiss();
                Util.showErrorMessage(ChangePhoneActivity.this, msg, getResources().getString
                        (R.string.binding_failed));
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.confirm_old_phone_get_code_btn:
                // 验证原手机(第一步) 获取验证码
                step1GetCodeBtn.start(60);
                HashMap<String, Object> getCodeParams = new HashMap<>();
                getCodeParams.put("phone", UserInfoManager.getInstance().getUserInfo().getPhone());
                getCodeParams.put("action", Constants.OLD_PHONE_BINDING);
                loadDataGet(GET_CODE_SMS, getCodeParams);
                break;
            case R.id.confirm_old_phone_next_btn:
                // 验证原手机(第一步) 下一步 按钮
                String code = step1CodeInput.getText().toString().trim();
                if (TextUtils.isEmpty(code) || code.length() < 4) {
                    Util.showToast(ChangePhoneActivity.this, getResources().getString(R.string
                            .code_error));
                    return;
                }
                progressDialog = Util.showLoadingDialog(ChangePhoneActivity.this,
                        progressDialog);
                // 标识为验证原手机号
                isCheckOldPhone = true;

                try {
                    Map<String, String> checkPhoneParams = new HashMap<>();
                    JSONObject paramsObject = new JSONObject();
                    paramsObject.put("action", "3");
                    paramsObject.put("validateMobile", UserInfoManager.getInstance().getUserInfo
                            ().getPhone());
                    paramsObject.put("code", code);
                    paramsObject.put("extractPassword", "");
                    paramsObject.put("oldExtractPassword", "");
                    paramsObject.put("token", "");
                    String params = RSACrypt.encryptByPublicKey(paramsObject.toString().getBytes
                            (), Constants.RSA_PUBLIC_KEY);
                    checkPhoneParams.put("params", params);
                    loadDataPost(SAFE_SET, checkPhoneParams);
                } catch (Exception e) {
                    progressDialog.dismiss();
                    DLog.e("ChangePhoneActivity", "校验手机组建参数时发生异常#excetpion:", e);
                }
                // 隐藏键盘
                Util.hideInput(ChangePhoneActivity.this, step1CodeInput);
                break;
            case R.id.binding_new_phone_get_code_btn:
                // 绑定新手机号(第二步) 获取验证码
                bindingNewPhone = step2PhoneInput.getText().toString().trim();
                if (TextUtils.isEmpty(bindingNewPhone) || bindingNewPhone.length() != 11) {
                    // 手机号码位数不符合
                    Util.showToast(ChangePhoneActivity.this, getResources().getString(R.string
                            .phone_error));
                    return;
                }

                step2GetCodeBtn.start(60);
                HashMap<String, Object> getCodeParams2 = new HashMap<>();
                getCodeParams2.put("phone", bindingNewPhone);
                getCodeParams2.put("action", Constants.NEW_PHONE_BINDING);
                loadDataGet(GET_CODE_SMS, getCodeParams2);
                break;
            case R.id.binding_new_phone_sure:
                // 绑定新手机号(第二步) 绑定 按钮
                String phoneNum = step2PhoneInput.getText().toString().trim();
                if (TextUtils.isEmpty(phoneNum) || phoneNum.length() != 11) {
                    // 手机号码位数不符合
                    Util.showToast(ChangePhoneActivity.this, getResources().getString(R.string
                            .phone_error));
                    return;
                }

                if (!TextUtils.equals(phoneNum, bindingNewPhone)) {
                    // 获取短信验证码的手机号和输入框中的手机号不相同
                    Util.showToast(ChangePhoneActivity.this, getResources().getString(R.string
                            .phone_different));
                    return;
                }

                String code2 = step2CodeInput.getText().toString().trim();
                if (TextUtils.isEmpty(code2) || code2.length() < 4) {
                    Util.showToast(ChangePhoneActivity.this, getResources().getString(R.string
                            .code_error));
                    return;
                }
                progressDialog = Util.showLoadingDialog(ChangePhoneActivity.this, progressDialog);
                // 标识为提交绑定新的手机号
                isCheckOldPhone = false;
                try {
                    Map<String, String> bindingPhoneParams = new HashMap<>();
                    JSONObject paramsObject = new JSONObject();
                    paramsObject.put("action", "4");
                    paramsObject.put("validateMobile", bindingNewPhone);
                    paramsObject.put("code", code2);
                    paramsObject.put("token", confirmToken);
                    paramsObject.put("extractPassword", "");
                    paramsObject.put("oldExtractPassword", "");
                    String params = RSACrypt.encryptByPublicKey(paramsObject.toString().getBytes
                            (), Constants.RSA_PUBLIC_KEY);
                    bindingPhoneParams.put("params", params);
                    loadDataPost(SAFE_SET, bindingPhoneParams);
                } catch (Exception e) {
                    progressDialog.dismiss();
                    DLog.e("ChangePhoneActivity", "校验新手机组建参数时发生异常#excetpion:", e);
                }
                break;
        }
    }

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, ChangePhoneActivity.class);
        activity.startActivityForResult(intent, 10019);
    }
}
