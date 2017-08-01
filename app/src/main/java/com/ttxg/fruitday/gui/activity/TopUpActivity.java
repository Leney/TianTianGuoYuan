package com.ttxg.fruitday.gui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.gui.adapter.FragmentViewPagerAdapter;
import com.ttxg.fruitday.gui.fragment.TopUpByCardFragment;
import com.ttxg.fruitday.gui.fragment.TopUpByWeiXinFragment;
import com.ttxg.fruitday.manager.UserInfoManager;

/**
 * 充值卡充值界面
 * Created by lilijun on 2016/10/24.
 */
public class TopUpActivity extends BaseActivity implements View.OnClickListener {
//    /**
//     * 充值卡充值
//     */
//    private final String TOP_UP_BY_CART = "prepaid/doRecharge";
//
//    /**
//     * 充值成功的resultCode
//     */
//    public static final int TOP_UP_SUCCESS_RESULT_CODE = 100016;
//    /**
//     * 卡号输入框，密码输入框
//     */
//    private EditText cartCodeInput, cartPwdInput;
//    /**
//     * 确认充值、取消
//     */
//    private Button sureBtn, cancelBtn;
//
//    private ProgressDialog progressDialog;

    /**
     * 充值成功的resultCode
     */
    public static final int TOP_UP_SUCCESS_RESULT_CODE = 100016;

    private TextView[] titles = new TextView[2];
    private ViewPager viewPager;

    private FragmentViewPagerAdapter adapter;

    private int curPagePosition = 0;

//    /**
//     * 判断是否需要返回ResultCode
//     */
//    private boolean isForResult = false;

    @Override
    protected void initView() {
        setTitleName(getResources().getString(R.string.top_up));
        setCenterView(R.layout.activity_top_up);

//        cartCodeInput = (EditText) findViewById(R.id.top_up_cart_code_input);
//        cartPwdInput = (EditText) findViewById(R.id.top_up_cart_pwd_input);
//        sureBtn = (Button) findViewById(R.id.top_up_confirm);
//        sureBtn.setOnClickListener(this);
//        cancelBtn = (Button) findViewById(R.id.top_up_cancel);
//        cancelBtn.setOnClickListener(this);
//        showCenterView();

        titles[0] = (TextView) findViewById(R.id.top_up_title_1);
        titles[1] = (TextView) findViewById(R.id.top_up_title_2);

        titles[0].setOnClickListener(this);
        titles[1].setOnClickListener(this);
        titles[0].setSelected(true);

        viewPager = (ViewPager) findViewById(R.id.top_up_viewPager);

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new TopUpByWeiXinFragment());
        fragmentList.add(new TopUpByCardFragment());
        adapter = new FragmentViewPagerAdapter(getSupportFragmentManager(),fragmentList);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                titles[curPagePosition].setSelected(false);
                titles[position].setSelected(true);
                curPagePosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        showCenterView();
    }

//    @Override
//    protected void onLoadDataSuccess(String tag, JSONObject resultObject) {
//        super.onLoadDataSuccess(tag, resultObject);
//        if (TextUtils.equals(tag, TOP_UP_BY_CART)) {
//            // 充值成功
//            progressDialog.dismiss();
//            Util.showToast(TopUpActivity.this, getResources().getString(R.string.top_up_success));
//            setResult(TOP_UP_SUCCESS_RESULT_CODE);
//            // 发送用户充值成功的消息，需要刷新用户的一些数据
//            EventBus.getDefault().post(Constants.NEED_REFRESH_ANY_NUM);
//            finish();
//        }
//    }
//
//    @Override
//    protected void onLoadDataError(String tag, int errorCode, String msg) {
//        super.onLoadDataError(tag, errorCode, msg);
//        if (TextUtils.equals(tag, TOP_UP_BY_CART)) {
//            // 充值失败
//            progressDialog.dismiss();
//            Util.showErrorMessage(TopUpActivity.this, msg, getResources().getString(R.string
//                    .top_up_failed));
//        }
//    }


//    public void setForResult(boolean forResult) {
//        isForResult = forResult;
//    }


//    @Override
//    public void finish() {
//        if(isForResult){
//            setResult(TOP_UP_SUCCESS_RESULT_CODE);
//        }
//        super.finish();
//    }

    @Override
    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.top_up_confirm:
//                // 确认充值
//                String cartCodeStr = cartCodeInput.getText().toString().trim();
//                if (TextUtils.isEmpty(cartCodeStr)) {
//                    Util.showToast(TopUpActivity.this, getResources().getString(R.string
//                            .please_input_cart_code));
//                    return;
//                }
//                String cartPwdStr = cartPwdInput.getText().toString().trim();
//                if (TextUtils.isEmpty(cartPwdStr)) {
//                    Util.showToast(TopUpActivity.this, getResources().getString(R.string
//                            .please_input_cart_pwd));
//                    return;
//                }
//                if (cartCodeStr.length() < 12) {
//                    Util.showToast(TopUpActivity.this, getResources().getString(R.string
//                            .please_input_cart_code_right));
//                    return;
//                }
//                if (cartPwdStr.length() < 8) {
//                    Util.showToast(TopUpActivity.this, getResources().getString(R.string
//                            .please_input_cart_pwd_right));
//                    return;
//                }
//                Map<String, String> topUpParams = new HashMap<>();
//                topUpParams.put("cardNum", cartCodeStr);
//                topUpParams.put("cardPass", cartPwdStr);
//                progressDialog = Util.showLoadingDialog(TopUpActivity.this, progressDialog);
//                loadDataPost(TOP_UP_BY_CART, topUpParams);
//                break;
//            case R.id.top_up_cancel:
//                // 取消
//                finish();
//                break;
//        }

        switch (view.getId()){
            case R.id.top_up_title_1:
                // 微信充值title
                viewPager.setCurrentItem(0);
                break;
            case R.id.top_up_title_2:
                // 充值卡充值title
                viewPager.setCurrentItem(1);
                break;
        }
    }

    public static void startActivity(Context context) {
        if(UserInfoManager.getInstance().isValidUserInfo()){
            Intent intent = new Intent(context, TopUpActivity.class);
            context.startActivity(intent);
        }else{
            LoginActivity.startActivity(context);
        }
    }
}
