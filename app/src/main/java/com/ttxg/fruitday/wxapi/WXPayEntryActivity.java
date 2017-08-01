package com.ttxg.fruitday.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.util.Constants;
import com.ttxg.fruitday.util.PreferencesUtils;
import com.ttxg.fruitday.util.Util;
import com.ttxg.fruitday.util.log.DLog;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "WXPayEntryActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);

        api = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        DLog.i(TAG, "onPayFinish, errCode = " + resp.errCode);
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {

            switch (resp.errCode) {
                case 0:
                    // 支付成功
                    DLog.i("resp.transaction------>>>" + resp.transaction);
                    Intent intent = new Intent(Constants.ACTION_PAY_SUCCESS);
                    intent.putExtra("payType", Constants.WX_PAY_TYPE);
                    intent.putExtra("orderNo", PreferencesUtils.getString(Constants
                            .WX_PAY_ORDER_NO));
                    sendBroadcast(intent);
                    finish();
                    break;
                case -1:
                    // 支付出现异常(可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等)
                    Util.showErrorMessage(WXPayEntryActivity.this, resp.errStr, getResources()
                            .getString(R.string.pay_failed));
                    finish();
                    break;
                case -2:
                    // 用户取消支付(用户不支付了，点击取消，返回APP)
                    finish();
                    break;
                default:
                    // 支付出现异常
                    Util.showErrorMessage(WXPayEntryActivity.this, resp.errStr, getResources()
                            .getString(R.string.pay_failed));
                    finish();
                    break;
            }
        }
    }
}