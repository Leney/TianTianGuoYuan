package com.ttxg.fruitday.gui.fragment;

import android.app.ProgressDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.gui.activity.LoginActivity;
import com.ttxg.fruitday.util.Constants;
import com.ttxg.fruitday.util.ParseUtil;
import com.ttxg.fruitday.util.Util;

/**
 * 充值卡充值fragment
 * Created by lilijun on 2016/11/7.
 */
public class TopUpByCardFragment extends BaseFragment implements View.OnClickListener {
    /**
     * 充值卡充值
     */
    private final String TOP_UP_BY_CART = "prepaid/doRecharge";
    /**
     * 获取用户信息
     */
    private final String GET_LOGIN_USER_INFO = "user/getUserInfo";

    /**
     * 卡号输入框，密码输入框
     */
    private EditText cartCodeInput, cartPwdInput;
    /**
     * 确认充值、取消
     */
    private Button sureBtn, cancelBtn;

    private ProgressDialog progressDialog;

    @Override
    protected void initView(RelativeLayout view) {
        setCenterView(R.layout.top_up_by_card_fragment);

        cartCodeInput = (EditText) view.findViewById(R.id.top_up_cart_code_input);
        cartPwdInput = (EditText) view.findViewById(R.id.top_up_cart_pwd_input);
        sureBtn = (Button) view.findViewById(R.id.top_up_confirm);
        sureBtn.setOnClickListener(this);
        cancelBtn = (Button) view.findViewById(R.id.top_up_cancel);
        cancelBtn.setOnClickListener(this);
        showCenterView();
    }

    @Override
    protected void onLoadDataSuccess(String tag, JSONObject resultObject) {
        super.onLoadDataSuccess(tag, resultObject);
        if (TextUtils.equals(tag, TOP_UP_BY_CART)) {
            // 充值成功
            progressDialog.dismiss();
            Util.showToast(getActivity(), getResources().getString(R.string.top_up_success));
//            getActivity().setResult(TopUpActivity.TOP_UP_SUCCESS_RESULT_CODE);
//            // 发送用户充值成功的消息，需要刷新用户的一些数据
//            EventBus.getDefault().post(Constants.NEED_REFRESH_ANY_NUM);
//            getActivity().finish();
//            ((TopUpActivity)getActivity()).setForResult(true);
            cartCodeInput.setText("");
            cartPwdInput.setText("");
            // 去获取用户信息
            loadDataGet(GET_LOGIN_USER_INFO,null);
        }else if (TextUtils.equals(tag,GET_LOGIN_USER_INFO)){
            // 获取用户信息成功
            ParseUtil.parseLoginUserInfo(resultObject);
        }
    }

    @Override
    protected void onLoadDataError(String tag, int errorCode, String msg) {
        super.onLoadDataError(tag, errorCode, msg);
        if (TextUtils.equals(tag, TOP_UP_BY_CART)) {
            // 充值失败
            progressDialog.dismiss();
            Util.showErrorMessage(getActivity(), msg, getResources().getString(R.string
                    .top_up_failed));
            if (errorCode == Constants.LOGIN_TIME_OUT) {
                LoginActivity.startActivity(getActivity());
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_up_confirm:
                // 确认充值
                String cartCodeStr = cartCodeInput.getText().toString().trim();
                if (TextUtils.isEmpty(cartCodeStr)) {
                    Util.showToast(getActivity(), getResources().getString(R.string
                            .please_input_cart_code));
                    return;
                }
                String cartPwdStr = cartPwdInput.getText().toString().trim();
                if (TextUtils.isEmpty(cartPwdStr)) {
                    Util.showToast(getActivity(), getResources().getString(R.string
                            .please_input_cart_pwd));
                    return;
                }
                if (cartCodeStr.length() < 12) {
                    Util.showToast(getActivity(), getResources().getString(R.string
                            .please_input_cart_code_right));
                    return;
                }
                if (cartPwdStr.length() < 8) {
                    Util.showToast(getActivity(), getResources().getString(R.string
                            .please_input_cart_pwd_right));
                    return;
                }
                Map<String, String> topUpParams = new HashMap<>();
                topUpParams.put("cardNum", cartCodeStr);
                topUpParams.put("cardPass", cartPwdStr);
                progressDialog = Util.showLoadingDialog(getActivity(), progressDialog);
                loadDataPost(TOP_UP_BY_CART, topUpParams);
                break;
            case R.id.top_up_cancel:
                // 取消
                getActivity().finish();
                break;
        }
    }
}
