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

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.gui.view.CountDownTextView;
import com.ttxg.fruitday.util.Constants;
import com.ttxg.fruitday.util.Util;
import com.ttxg.fruitday.util.log.DLog;
import com.ttxg.fruitday.util.rsa.RSACrypt;

/**
 * 注册界面
 * Created by lilijun on 2016/9/7.
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener{
    /** 返回按钮*/
    private ImageView backImg;
    /** 获取验证码tag*/
    private final String GET_CODE_SMS = "sms/sendSms";
    /** 注册tag*/
    private final String REGISTER_USER = "supplier/addSupplierUser";
    private CountDownTextView getCodeBtn;
    /** 手机号码输入框、验证码输入框、密码输入框、确认密码*/
    private EditText phoneInput,codeInput,pwdInput,affirmPwdInput;
    /** 注册按钮*/
    private Button registerBtn;
//    /** 用户协议是否同意的checkBox*/
//    private CheckBox userDealCheckBox;
//    /** 《用户使用协议》  按钮*/
//    private TextView userDealBtn;

    /** 获取验证码参数信息*/
    private HashMap<String,Object> getCodeParams;
    /** 注册的参数信息*/
    private HashMap<String,String> registerParams;

    /** 正在注册dialog*/
    private ProgressDialog progressDialog;

    /**
     * 获取验证码时的手机号
     */
    private String getCodePhone;
    @Override
    protected void initView() {
        setTitleVisible(false);


        getCodeParams = new HashMap<>();
        registerParams = new HashMap<>();

        setCenterView(R.layout.activity_register);

        backImg = (ImageView) findViewById(R.id.register_title_back_img);
        backImg.setOnClickListener(this);
        getCodeBtn = (CountDownTextView) findViewById(R.id.register_get_code_btn);
        getCodeBtn.setOnClickListener(this);
        phoneInput = (EditText) findViewById(R.id.register_input_phone);
        codeInput = (EditText) findViewById(R.id.register_input_code);
        pwdInput = (EditText) findViewById(R.id.register_input_pwd);
        affirmPwdInput = (EditText) findViewById(R.id.register_input_pwd_affirm);
        registerBtn = (Button) findViewById(R.id.register_register_btn);
        registerBtn.setOnClickListener(this);
//        userDealCheckBox = (CheckBox) findViewById(R.id.register_user_deal_checkbox);
//        userDealBtn = (TextView) findViewById(R.id.register_user_deal);
//        userDealBtn.setOnClickListener(this);

        showCenterView();
    }

    @Override
    protected void onLoadDataSuccess(String tag, JSONObject resultObject) {
        super.onLoadDataSuccess(tag, resultObject);
        if(TextUtils.equals(tag,REGISTER_USER)){
            // 注册信息返回
            progressDialog.dismiss();
            Util.showToast(RegisterActivity.this,getResources().getString(R.string.register_success));
            finish();
        }
    }

    @Override
    protected void onLoadDataError(String tag, int errorCode, String msg) {
        super.onLoadDataError(tag, errorCode, msg);
        if(TextUtils.equals(tag,REGISTER_USER)){
            // 注册失败
            progressDialog.dismiss();
            Util.showErrorMessage(RegisterActivity.this, msg, getResources().getString(R.string
                    .register_failed));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.register_title_back_img:
                // 标题返回按钮
                finish();
                break;
            case R.id.register_get_code_btn:
                // 获取验证码
                getCodePhone = phoneInput.getText().toString().trim();
                if(TextUtils.isEmpty(getCodePhone) || getCodePhone.length() != 11){
                    // 手机号码位数不符合
                    Util.showToast(RegisterActivity.this,getResources().getString(R.string.phone_error));
                    return;
                }
                getCodeBtn.start(60);
                getCodeParams.put("phone",getCodePhone);
                getCodeParams.put("action", Constants.SIGNUP);
                loadDataGet(GET_CODE_SMS,getCodeParams);
                break;
//            case R.id.register_user_deal:
//                // 《用户使用协议》
//                break;
            case R.id.register_register_btn:
                // 注册按钮
                String phone = phoneInput.getText().toString().trim();
                if(TextUtils.isEmpty(phone) || phone.length() != 11){
                    // 手机号码位数不符合
                    Util.showToast(RegisterActivity.this,getResources().getString(R.string.phone_error));
                    return;
                }

                if (!TextUtils.equals(getCodePhone, phone)) {
                    // 获取验证码和输入的手机号不一致
                    Util.showToast(RegisterActivity.this, getResources().getString(R.string
                            .get_code_phone_different_input_phone));
                    return;
                }

                String code = codeInput.getText().toString().trim();
                if(TextUtils.isEmpty(code) || code.length() < 4){
                    Util.showToast(RegisterActivity.this,getResources().getString(R.string.code_error));
                    return;
                }
                String password = pwdInput.getText().toString().trim();
                if(TextUtils.isEmpty(password) || password.length() < 6 || password.length() > 20){
                    Util.showToast(RegisterActivity.this,getResources().getString(R.string.password_set_hint));
                    return;
                }
                String passwordAffirm = affirmPwdInput.getText().toString().trim();
                if(TextUtils.isEmpty(passwordAffirm) || !passwordAffirm.equals(password)){
                    Util.showToast(RegisterActivity.this,getResources().getString(R.string.password_twice_different));
                    return;
                }

//                if(!userDealCheckBox.isChecked()){
//                    Util.showToast(RegisterActivity.this,getResources().getString(R.string.agress_user_deal));
//                    return;
//                }

                try {
                    JSONObject paramsObject = new JSONObject();
                    paramsObject.put("mobile",phone);
                    paramsObject.put("captchaInput",code);
                    paramsObject.put("setPassword",password);
                    String parms = RSACrypt.encryptByPublicKey(paramsObject.toString().getBytes(),Constants.RSA_PUBLIC_KEY);
                    registerParams.put("parms",parms);
                    progressDialog = Util.showLoadingDialog(RegisterActivity.this,progressDialog);
                    loadDataPost(REGISTER_USER,registerParams);
                } catch (Exception e) {
                    DLog.e("RegisterActivity","注册组建参数时发生异常#excetpion:",e);
                    Util.showToast(RegisterActivity.this,getResources().getString(R.string.register_failed));
                }
                break;
        }
    }

    public static void startActivity(Context context){
        Intent intent = new Intent(context,RegisterActivity.class);
        context.startActivity(intent);
    }
}
