package com.ttxg.fruitday.gui.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.model.AccountInfo;
import com.ttxg.fruitday.util.ParseUtil;
import com.ttxg.fruitday.util.log.DLog;

/**
 * 余额Activity
 * Created by lilijun on 2016/10/24.
 */
public class RemainingActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 获取余额
     */
    private final String GET_REMAINNING = "prepaid/index";

    /**
     * 可用金额(余额)
     */
    private TextView remainingMoney;

    /**
     * 充值
     */
    private LinearLayout topUpLay;

    /**
     * 充值明细
     */
    private LinearLayout topUpDetailLay;

    /**
     * 用户账户信息
     */
    private AccountInfo accountInfo;
    @Override
    protected void initView() {
        setTitleName(getResources().getString(R.string.remaining));
        setCenterView(R.layout.acitivty_remainning);
        remainingMoney = (TextView) findViewById(R.id.remaining_value);
        topUpLay = (LinearLayout) findViewById(R.id.remaining_top_up_lay);
        topUpLay.setOnClickListener(this);
        topUpDetailLay = (LinearLayout) findViewById(R.id.remaining_top_up_detail_lay);
        topUpDetailLay.setOnClickListener(this);

        loadDataGet(GET_REMAINNING,null);
    }

    @Override
    protected void onLoadDataSuccess(String tag, JSONObject resultObject) {
        super.onLoadDataSuccess(tag, resultObject);
        if(TextUtils.equals(tag,GET_REMAINNING)){
            // 获取用户余额成功
            accountInfo = ParseUtil.parseAccountInfo(resultObject);
            if(accountInfo == null){
                showErrorView();
                return;
            }
            remainingMoney.setText(accountInfo.getRemainingMoney() + "");
            showCenterView();
        }
    }

    @Override
    protected void onLoadDataError(String tag, int errorCode, String msg) {
        super.onLoadDataError(tag, errorCode, msg);
        if(TextUtils.equals(tag,GET_REMAINNING)){
            // 获取用户余额失败
            showErrorView();
        }
    }

    @Override
    protected void tryAgain() {
        super.tryAgain();
        loadDataGet(GET_REMAINNING,null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == TopUpActivity.TOP_UP_SUCCESS_RESULT_CODE){
            // 充值成功
            // 再次去查询用户账户信息
            DLog.i("充值成功，再次去获取余额信息！！！");
            tryAgain();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.remaining_top_up_lay:
                // 充值
                TopUpActivity.startActivity(RemainingActivity.this);
                break;
            case R.id.remaining_top_up_detail_lay:
                // 充值明细
                TopUpDetailActivity.startActivity(RemainingActivity.this);
                break;
        }
    }

    public static void startActivity(Context context){
        Intent intent = new Intent(context,RemainingActivity.class);
        context.startActivity(intent);
    }
}
