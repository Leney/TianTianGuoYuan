package com.ttxg.fruitday.gui.fragment;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.gui.activity.LoginActivity;
import com.ttxg.fruitday.model.WXPrepareInfo;
import com.ttxg.fruitday.util.Constants;
import com.ttxg.fruitday.util.ParseUtil;
import com.ttxg.fruitday.util.PreferencesUtils;
import com.ttxg.fruitday.util.Util;
import com.ttxg.fruitday.util.log.DLog;

/**
 * 微信充值Fragment
 * Created by lilijun on 2016/11/7.
 */
public class TopUpByWeiXinFragment extends BaseFragment implements View.OnClickListener {
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
    /**
     * 微信充值4个价格选项
     */
    private FrameLayout[] priceItemLays = new FrameLayout[4];
    /**
     * 价格选项被选中的视图
     */
    private View[] priceSelectViews = new View[4];
    /**
     * 4个价格选项所对应的价格
     */
    private double[] itemPrices = new double[4];
    private EditText priceInput;
    private Button sureBtn, cancelBtn;
    /**
     * 当前选中的充值价格position
     */
    private int curSelectPosition;

    private ProgressDialog progressDialog;

    /**
     * 微信支付api
     */
    private IWXAPI wxApi;

    @Override
    protected void initView(RelativeLayout view) {
        setCenterView(R.layout.top_up_by_weixin_fragment);

        itemPrices[0] = 198;
        itemPrices[1] = 1980;
        itemPrices[2] = 6000;
        itemPrices[3] = 12000;

        priceItemLays[0] = (FrameLayout) view.findViewById(R.id.top_up_wei_xin_item_0_lay);
        priceItemLays[1] = (FrameLayout) view.findViewById(R.id.top_up_wei_xin_item_1_lay);
        priceItemLays[2] = (FrameLayout) view.findViewById(R.id.top_up_wei_xin_item_2_lay);
        priceItemLays[3] = (FrameLayout) view.findViewById(R.id.top_up_wei_xin_item_3_lay);
        priceItemLays[0].setOnClickListener(this);
        priceItemLays[1].setOnClickListener(this);
        priceItemLays[2].setOnClickListener(this);
        priceItemLays[3].setOnClickListener(this);
        priceItemLays[0].setSelected(true);

        priceSelectViews[0] = view.findViewById(R.id.top_up_wei_xin_item_0_select_view);
        priceSelectViews[1] = view.findViewById(R.id.top_up_wei_xin_item_1_select_view);
        priceSelectViews[2] = view.findViewById(R.id.top_up_wei_xin_item_2_select_view);
        priceSelectViews[3] = view.findViewById(R.id.top_up_wei_xin_item_3_select_view);

        priceInput = (EditText) view.findViewById(R.id.top_up_wei_xin_input_price);
        sureBtn = (Button) view.findViewById(R.id.top_up_wei_xin_confirm_btn);
        sureBtn.setOnClickListener(this);
        cancelBtn = (Button) view.findViewById(R.id.top_up_wei_xin_cancel_btn);
        cancelBtn.setOnClickListener(this);

        // 初始化微信支付api
        wxApi = WXAPIFactory.createWXAPI(getActivity(), Constants.WX_APP_ID, true);
        wxApi.registerApp(Constants.WX_APP_ID);

        showCenterView();

        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(Constants
                .ACTION_PAY_SUCCESS));
    }

    @Override
    protected void onLoadDataSuccess(String tag, JSONObject resultObject) {
        super.onLoadDataSuccess(tag, resultObject);
        if (TextUtils.equals(tag, TOP_UP_BY_CHAT)) {
            // 获取微信预支付信息成功
            // 获取微信预支付订单信息成功
            WXPrepareInfo prepareInfo = new WXPrepareInfo();
            String prepaidTradeNo = ParseUtil.parseWxPrepareInfo(resultObject, prepareInfo);
            if (TextUtils.isEmpty(prepaidTradeNo)) {
                progressDialog.dismiss();
                Util.showToast(getActivity(), getResources().getString(R.string
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
                    Util.showToast(getActivity(), getResources().getString(R.string
                            .top_up_success));
//                    getActivity().setResult(TopUpActivity.TOP_UP_SUCCESS_RESULT_CODE);
//                    // 发送用户充值成功的消息，需要刷新用户的一些数据
//                    EventBus.getDefault().post(Constants.NEED_REFRESH_ANY_NUM);
//                    getActivity().finish();
                } else {
                    // 不能查询到订单支付的信息，支付失败
                    Util.showToast(getActivity(), getResources().getString(R.string.pay_failed));
                }
//                ((TopUpActivity)getActivity()).setForResult(true);
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
        if (TextUtils.equals(tag, TOP_UP_BY_CHAT)) {
            // 获取微信预支付订单信息失败
            progressDialog.dismiss();
            Util.showErrorMessage(getActivity(), msg, getResources().getString(R.string
                    .get_wxprepareinfo_failed));
            if (errorCode == Constants.LOGIN_TIME_OUT) {
                LoginActivity.startActivity(getActivity());
            }
        } else if (TextUtils.equals(tag, QUERY_WX_PAY_RESULT)) {
            // 查询支付订单状态失败
            progressDialog.dismiss();
            // 查询订单支付信息时出现了异常(有可能是网络，服务器等原因，支付也可能失败了)
            // 同样返回显示充值成功
            Util.showToast(getActivity(), getResources().getString(R.string
                    .top_up_success));
//            getActivity().setResult(TopUpActivity.TOP_UP_SUCCESS_RESULT_CODE);
//            // 发送用户充值成功的消息，需要刷新用户的一些数据
//            EventBus.getDefault().post(Constants.NEED_REFRESH_ANY_NUM);
//            getActivity().finish();
//            ((TopUpActivity)getActivity()).setForResult(true);
            // 去获取用户信息
            loadDataGet(GET_LOGIN_USER_INFO, null);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_up_wei_xin_confirm_btn:
                // 确认充值
                if (!wxApi.isWXAppInstalled() || !wxApi.isWXAppSupportAPI()) {
                    // 未安装微信
                    Util.showToast(getActivity(), getResources().getString(R.string
                            .no_weiXin));
                    return;
                }
                String inputPrice = priceInput.getText().toString().trim();
                double price;
                if (TextUtils.isEmpty(inputPrice)) {
                    // 用户没有输入金额
                    // 则去获取默认的选择的价格
                    price = itemPrices[curSelectPosition];
                } else {
                    // 用户有输入金额
                    price = Double.parseDouble(inputPrice);
                }
                progressDialog = Util.showLoadingDialog(getActivity(), progressDialog);
                Map<String, String> prepareParams = new HashMap<>();
                prepareParams.put("prepaidPrice", price + "");
                loadDataPost(TOP_UP_BY_CHAT, prepareParams);
                break;
            case R.id.top_up_wei_xin_cancel_btn:
                // 取消
                getActivity().finish();
                break;
            case R.id.top_up_wei_xin_item_0_lay:
                // 198元
                if (curSelectPosition == 0) {
                    return;
                }
                priceItemLays[curSelectPosition].setSelected(false);
                priceSelectViews[curSelectPosition].setVisibility(View.GONE);
                curSelectPosition = 0;
                priceItemLays[curSelectPosition].setSelected(true);
                priceSelectViews[curSelectPosition].setVisibility(View.VISIBLE);
                break;
            case R.id.top_up_wei_xin_item_1_lay:
                // 1980元
                priceItemLays[curSelectPosition].setSelected(false);
                priceSelectViews[curSelectPosition].setVisibility(View.GONE);
                curSelectPosition = 1;
                priceItemLays[curSelectPosition].setSelected(true);
                priceSelectViews[curSelectPosition].setVisibility(View.VISIBLE);
                break;
            case R.id.top_up_wei_xin_item_2_lay:
                // 6000元
                priceItemLays[curSelectPosition].setSelected(false);
                priceSelectViews[curSelectPosition].setVisibility(View.GONE);
                curSelectPosition = 2;
                priceItemLays[curSelectPosition].setSelected(true);
                priceSelectViews[curSelectPosition].setVisibility(View.VISIBLE);
                break;
            case R.id.top_up_wei_xin_item_3_lay:
                // 12000元
                priceItemLays[curSelectPosition].setSelected(false);
                priceSelectViews[curSelectPosition].setVisibility(View.GONE);
                curSelectPosition = 3;
                priceItemLays[curSelectPosition].setSelected(true);
                priceSelectViews[curSelectPosition].setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (progressDialog == null) {
            return;
        }
        progressDialog.dismiss();
    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(broadcastReceiver);
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
//                    progressDialog.show();
                    Util.showLoadingDialog(context, progressDialog);
                }
            }
        }
    };
}
