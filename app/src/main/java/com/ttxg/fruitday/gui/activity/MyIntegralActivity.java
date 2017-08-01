package com.ttxg.fruitday.gui.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.manager.UserInfoManager;
import com.ttxg.fruitday.util.log.DLog;

/**
 * 我的积分
 * Created by lilijun on 2016/10/26.
 */
public class MyIntegralActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 查询用户积分
     */
    private final String QUERY_USER_INTEGRAL = "user/point/index";
    private TextView integralValue;
    private LinearLayout integralDetailLay;

    @Override
    protected void initView() {
        setTitleName(getResources().getString(R.string.my_integral));
        setCenterView(R.layout.acitivty_my_integral);
        integralValue = (TextView) findViewById(R.id.my_integral_value);
        integralDetailLay = (LinearLayout) findViewById(R.id.my_integral_detail_lay);
        integralDetailLay.setOnClickListener(this);

        loadDataGet(QUERY_USER_INTEGRAL, null);
    }

    @Override
    protected void onLoadDataSuccess(String tag, JSONObject resultObject) {
        super.onLoadDataSuccess(tag, resultObject);
        if (TextUtils.equals(tag, QUERY_USER_INTEGRAL)) {
            // 查询积分成功
            try {
                JSONObject integralObject = resultObject.getJSONObject("point");
                UserInfoManager.getInstance().getUserInfo().setTotalIntegral(integralObject
                        .getInt("totalPoint"));
                int remainingIntegral = integralObject.getInt("usablePoint");
                UserInfoManager.getInstance().getUserInfo().setRemainingIntegral(remainingIntegral);
                integralValue.setText(remainingIntegral + "");
                showCenterView();
            } catch (Exception e) {
                DLog.e("MyIntegralActivity", "解析用户积分发生异常#Exception:\n", e);
            }
        }
    }

    @Override
    protected void onLoadDataError(String tag, int errorCode, String msg) {
        super.onLoadDataError(tag, errorCode, msg);
        if (TextUtils.equals(tag, QUERY_USER_INTEGRAL)) {
            // 查询积分失败
            showErrorView();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.my_integral_detail_lay:
                // 积分明细
                break;
        }
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MyIntegralActivity.class);
        context.startActivity(intent);
    }
}
