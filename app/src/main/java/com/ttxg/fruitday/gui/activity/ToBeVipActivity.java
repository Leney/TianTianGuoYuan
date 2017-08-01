package com.ttxg.fruitday.gui.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.gui.adapter.ToVipAdapter;
import com.ttxg.fruitday.gui.view.WraperListView;
import com.ttxg.fruitday.manager.UserInfoManager;
import com.ttxg.fruitday.model.WXPrepareInfo;
import com.ttxg.fruitday.util.Constants;
import com.ttxg.fruitday.util.ParseUtil;
import com.ttxg.fruitday.util.PreferencesUtils;
import com.ttxg.fruitday.util.Util;
import com.ttxg.fruitday.util.log.DLog;

/**
 * 成为vip界面
 * Created by lilijun on 2016/11/8.
 */
public class ToBeVipActivity extends BaseActivity {
    /**
     * 获取充值信息
     */
    private final String GET_TOP_UP_PRICE_LIST = "prepaid/vipRecharge";
    /**
     * 获取微信充值预支付信息
     */
    private final String TOP_UP_BY_CHAT = "wxpay/unifiedorderByWxRecharge";
    /**
     * 查询微信支付订单状态(微信支付前端返回支付成功，但需要到我们自己后台服务器查询是否支付成功)
     */
    private final String QUERY_WX_PAY_RESULT = "wxpay/tradeQuery";
    /**
     * 获取用户信息
     */
    private final String GET_LOGIN_USER_INFO = "user/getUserInfo";
    //    /**
//     * 充值成功的resultCode
//     */
//    public static final int TOP_UP_SUCCESS_RESULT_CODE = 100020;
    private List<String> imgUrls;
    private List<Double> topUpMoneys;
    private ToVipAdapter adapter;

    private ProgressDialog progressDialog;

    /**
     * 微信支付api
     */
    private IWXAPI wxApi;

//    /**
//     * 标识是否需要设置reslutCode
//     */
//    private boolean isForReslut = false;

    @Override
    protected void initView() {
        setTitleName(getResources().getString(R.string.become_vip));
        imgUrls = new ArrayList<>();
        topUpMoneys = new ArrayList<>();
        WraperListView listView = new WraperListView(ToBeVipActivity.this);
        setCenterView(listView);
        adapter = new ToVipAdapter(imgUrls);
        listView.setAdapter(adapter);
        listView.setNextPageViewVisible(false);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!wxApi.isWXAppInstalled() || !wxApi.isWXAppSupportAPI()) {
                    // 未安装微信
                    Util.showToast(ToBeVipActivity.this, getResources().getString(R.string
                            .no_weiXin));
                    return;
                }
                progressDialog = Util.showLoadingDialog(ToBeVipActivity.this, progressDialog);
                Map<String, String> prepareParams = new HashMap<>();
                prepareParams.put("prepaidPrice", topUpMoneys.get(i) + "");
                loadDataPost(TOP_UP_BY_CHAT, prepareParams);
            }
        });
        loadDataGet(GET_TOP_UP_PRICE_LIST, null);

        // 初始化微信支付api
        wxApi = WXAPIFactory.createWXAPI(ToBeVipActivity.this, Constants.WX_APP_ID, true);
        wxApi.registerApp(Constants.WX_APP_ID);

        registerReceiver(broadcastReceiver, new IntentFilter(Constants.ACTION_PAY_SUCCESS));
    }

    @Override
    protected void onLoadDataSuccess(String tag, JSONObject resultObject) {
        super.onLoadDataSuccess(tag, resultObject);
        if (TextUtils.equals(tag, GET_TOP_UP_PRICE_LIST)) {
            // 获取充值信息成功
            imgUrls.clear();
            topUpMoneys.clear();
            try {
                JSONArray levelArray = resultObject.getJSONArray("levels");
                int length = levelArray.length();
                for (int i = 0; i < length; i++) {
                    JSONObject infoObject = levelArray.getJSONObject(i);
                    imgUrls.add(infoObject.getString("imgUrl"));
                    topUpMoneys.add(infoObject.getDouble("prepaidPrice"));
                }
            } catch (Exception e) {
                DLog.e("ToBeVipActivity", "解析充值信息列表数据异常#Exception:\n", e);
            }
            if (imgUrls.isEmpty() || topUpMoneys.isEmpty()) {
                showNoDataView();
            } else {
                adapter.notifyDataSetChanged();
                showCenterView();
            }
        } else if (TextUtils.equals(tag, TOP_UP_BY_CHAT)) {
            // 获取微信预支付信息成功
            // 获取微信预支付订单信息成功
            WXPrepareInfo prepareInfo = new WXPrepareInfo();
            String prepaidTradeNo = ParseUtil.parseWxPrepareInfo(resultObject, prepareInfo);
            if (TextUtils.isEmpty(prepaidTradeNo)) {
                progressDialog.dismiss();
                Util.showToast(ToBeVipActivity.this, getResources().getString(R.string
                        .get_wxprepareinfo_failed));
                return;
            }
            if (wxApi != null) {
                PayReq req = new PayReq();

                req.appId = Constants.WX_APP_ID;// 微信开放平台审核通过的应用APPID
                // 微信支付分配的商户号
                req.partnerId = prepareInfo.getPartnerId();
                // 预支付订单号
                req.prepayId = prepareInfo.getPrepayId();
                // 随机字符串
                req.nonceStr = prepareInfo.getNonceStr();
                // 时间戳
                req.timeStamp = prepareInfo.getTimeStamp();
                // 固定值Sign=WXPay，可以直接写死，服务器返回的也是这个固定值
                req.packageValue = prepareInfo.getPackageValue();
                // 签名
                req.sign = prepareInfo.getSign();

                // 保存微信支付的订单号
                PreferencesUtils.putString(Constants.WX_PAY_ORDER_NO, prepaidTradeNo);
                wxApi.sendReq(req);
            }
        } else if (TextUtils.equals(tag, QUERY_WX_PAY_RESULT)) {
            // 查询微信支付订单状态成功
            progressDialog.dismiss();
            try {
                String state = resultObject.getString("trade_state");
                if (TextUtils.equals(state, "SUCCESS")) {
                    // 能查询到支付订单信息，微信支付成功
                    DLog.i("能查询到支付订单信息，微信支付成功");
                    Util.showToast(ToBeVipActivity.this, getResources().getString(R.string
                            .top_up_success));
//                    setResult(TopUpActivity.TOP_UP_SUCCESS_RESULT_CODE);
//                    // 发送用户充值成功的消息，需要刷新用户的一些数据
//                    EventBus.getDefault().post(Constants.NEED_REFRESH_ANY_NUM);
//                    finish();
                } else {
                    // 不能查询到订单支付的信息，支付失败
                    Util.showToast(ToBeVipActivity.this, getResources().getString(R.string
                            .pay_failed));
                }
//                isForReslut = true;
                // 去获取用户信息
                loadDataGet(GET_LOGIN_USER_INFO, null);
            } catch (Exception e) {
                DLog.e("TopUpByWeiXinFragment", "解析查询微信支付订单状态信息异常#exception:\n", e);
            }
        } else if (TextUtils.equals(tag, GET_LOGIN_USER_INFO)) {
            // 获取用户信息成功
            ParseUtil.parseLoginUserInfo(resultObject);
        }
    }

    @Override
    protected void onLoadDataError(String tag, int errorCode, String msg) {
        super.onLoadDataError(tag, errorCode, msg);
        if (TextUtils.equals(tag, GET_TOP_UP_PRICE_LIST)) {
            // 获取充值信息失败
            Util.showErrorMessage(ToBeVipActivity.this, msg, getResources().getString(R.string
                    .get_top_up_info_failed));
            if (errorCode == Constants.LOGIN_TIME_OUT) {
                LoginActivity.startActivity(ToBeVipActivity.this);
                // 只要是登录信息过期了  就finish掉界面
                finish();
                return;
            }
            showErrorView();
        } else if (TextUtils.equals(tag, TOP_UP_BY_CHAT)) {
            // 获取微信预支付订单信息失败
            progressDialog.dismiss();
            Util.showErrorMessage(ToBeVipActivity.this, msg, getResources().getString(R.string
                    .get_wxprepareinfo_failed));
        } else if (TextUtils.equals(tag, QUERY_WX_PAY_RESULT)) {
            // 查询支付订单状态失败
            progressDialog.dismiss();
            // 查询订单支付信息时出现了异常(有可能是网络，服务器等原因，支付也可能失败了)
            // 同样返回显示充值成功
            Util.showToast(ToBeVipActivity.this, getResources().getString(R.string
                    .top_up_success));
//            setResult(TopUpActivity.TOP_UP_SUCCESS_RESULT_CODE);
//            // 发送用户充值成功的消息，需要刷新用户的一些数据
//            EventBus.getDefault().post(Constants.NEED_REFRESH_ANY_NUM);
//            finish();
//            isForReslut = true;
            // 去获取用户信息
            loadDataGet(GET_LOGIN_USER_INFO, null);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (progressDialog == null) {
            return;
        }
        progressDialog.dismiss();
    }

//    @Override
//    public void finish() {
//        if(isForReslut){
//            setResult(TOP_UP_SUCCESS_RESULT_CODE);
//        }
//        super.finish();
//    }

    @Override
    public void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (TextUtils.equals(Constants.ACTION_PAY_SUCCESS, intent.getAction())) {
                // 接收到支付成功的广播
                int payType = intent.getIntExtra("payType", -1);
                String orderNo = intent.getStringExtra("orderNo");
                DLog.i("查询微信订单状态订单号----->>" + orderNo);
                if (payType == Constants.WX_PAY_TYPE) {
                    // 微信支付成功
                    Map<String, String> checkOrderParams = new HashMap<>();
                    checkOrderParams.put("out_trade_no", orderNo);
                    loadDataPost(QUERY_WX_PAY_RESULT, checkOrderParams);
                    Util.showLoadingDialog(context, progressDialog);
                }
            }
        }
    };

    public static void startActivity(Context context) {
        if (UserInfoManager.getInstance().isValidUserInfo()) {
            Intent intent = new Intent(context, ToBeVipActivity.class);
            context.startActivity(intent);
        } else {
            LoginActivity.startActivity(context);
        }
    }
}
