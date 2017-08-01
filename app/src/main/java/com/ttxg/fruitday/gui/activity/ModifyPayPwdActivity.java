package com.ttxg.fruitday.gui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.gui.view.PasswordInputView;
import com.ttxg.fruitday.manager.UserInfoManager;
import com.ttxg.fruitday.util.Constants;
import com.ttxg.fruitday.util.Util;
import com.ttxg.fruitday.util.log.DLog;
import com.ttxg.fruitday.util.rsa.RSACrypt;

/**
 * 更改支付密码界面
 * Created by lilijun on 2016/10/27.
 */
public class ModifyPayPwdActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 修改支付密码
     */
    private final String MODIFY_PAY_PWD = "safe/editPayPassword";
    private PasswordInputView oldPwdInput, newPwdInput, newPwdConfirmInput;
    private Button submitBtn;
    private ProgressDialog progressDialog;

    @Override
    protected void initView() {
        setTitleName(getResources().getString(R.string.modify_pay_pwd));
        setCenterView(R.layout.activity_modify_pay_pwd);
        oldPwdInput = (PasswordInputView) findViewById(R.id.modify_pay_pwd_old_input);
        newPwdInput = (PasswordInputView) findViewById(R.id.modify_pay_pwd_new_input);
        newPwdConfirmInput = (PasswordInputView) findViewById(R.id
                .modify_pay_pwd_new_confirm_input);
        submitBtn = (Button) findViewById(R.id.modify_pay_pwd_submit_btn);
        submitBtn.setOnClickListener(this);

        showCenterView();
    }

    @Override
    protected void onLoadDataSuccess(String tag, JSONObject resultObject) {
        super.onLoadDataSuccess(tag, resultObject);
        if (TextUtils.equals(tag, MODIFY_PAY_PWD)) {
            // 设置新的支付密码成功
            progressDialog.dismiss();
            UserInfoManager.getInstance().getUserInfo().setPayPwd(true);
            Util.showToast(ModifyPayPwdActivity.this, getResources().getString(R.string
                    .set_pay_pwd_success));
            finish();
        }
    }

    @Override
    protected void onLoadDataError(String tag, int errorCode, String msg) {
        super.onLoadDataError(tag, errorCode, msg);
        if (TextUtils.equals(tag, MODIFY_PAY_PWD)) {
            // 设置支付密码失败
            progressDialog.dismiss();
            Util.showErrorMessage(ModifyPayPwdActivity.this, msg, getResources().getString(R
                    .string.set_pay_pwd_failed));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.modify_pay_pwd_submit_btn:
                // 提交
                String oldPayPwd = oldPwdInput.getText().toString().trim();
                if (TextUtils.isEmpty(oldPayPwd) || oldPayPwd.length() != 6) {
                    Util.showToast(ModifyPayPwdActivity.this, getResources()
                            .getString(R.string.input_old_pwd_error));
                    return;
                }

                String newPayPwd = newPwdInput.getText().toString().trim();
                if (TextUtils.isEmpty(newPayPwd) || newPayPwd.length() != 6) {
                    Util.showToast(ModifyPayPwdActivity.this, getResources()
                            .getString(R.string.input_new_pwd_error));
                    return;
                }

                String confirmNewPayPwd = newPwdConfirmInput.getText().toString().trim();
                if (!TextUtils.equals(newPayPwd, confirmNewPayPwd)) {
                    // 两次新密码不一致
                    Util.showToast(ModifyPayPwdActivity.this, getResources()
                            .getString(R.string.input_new_pwd_twice_dif));
                    return;

                }

                progressDialog = Util.showLoadingDialog(ModifyPayPwdActivity.this,
                        progressDialog);

                try {
                    Map<String, String> setParams = new HashMap<>();
                    JSONObject paramsObject = new JSONObject();
                    paramsObject.put("newPayPassword", newPayPwd);
                    paramsObject.put("oldPayPassword", oldPayPwd);
                    paramsObject.put("validateMobile", UserInfoManager.getInstance().getUserInfo
                            ().getPhone());
                    String params = RSACrypt.encryptByPublicKey(paramsObject.toString().getBytes
                            (), Constants.RSA_PUBLIC_KEY);
                    setParams.put("params", params);
                    loadDataPost(MODIFY_PAY_PWD, setParams);
                } catch (Exception e) {
                    progressDialog.dismiss();
                    DLog.e("ModifyPayPwdActivity", "修改支付密码组建参数时发生异常#excetpion:", e);
                }
                break;
        }
    }


    public static void startActivity(Context context){
        Intent intent = new Intent(context,ModifyPayPwdActivity.class);
        context.startActivity(intent);
    }
}
