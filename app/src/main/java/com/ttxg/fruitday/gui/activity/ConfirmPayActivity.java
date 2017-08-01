package com.ttxg.fruitday.gui.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.gui.view.PasswordInputView;
import com.ttxg.fruitday.manager.UserInfoManager;
import com.ttxg.fruitday.model.OrderDetailInfo;
import com.ttxg.fruitday.model.OrderInfo;
import com.ttxg.fruitday.model.OrderListInfo;
import com.ttxg.fruitday.model.UserInfo;
import com.ttxg.fruitday.model.WXPrepareInfo;
import com.ttxg.fruitday.util.AliPayResult;
import com.ttxg.fruitday.util.Constants;
import com.ttxg.fruitday.util.ParseUtil;
import com.ttxg.fruitday.util.PreferencesUtils;
import com.ttxg.fruitday.util.Util;
import com.ttxg.fruitday.util.log.DLog;
import com.ttxg.fruitday.util.rsa.RSACrypt;


/**
 * 确认支付界面
 * Created by lilijun on 2016/9/27.
 */
public class ConfirmPayActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 通过订单序列号获取微信预支付信息
     */
    private final String GET_WX_PREPARE_BY_SERIAL_NUMBER = "wxpay/unifiedorderBySerialNumber";
    /**
     * 通过订单序列号获取微信预支付信息
     */
    private final String GET_WX_PREPARE_BY_ORDER_NO = "wxpay/unifiedorderByOrderNo";
    /**
     * 获取支付宝支付所需信息参数
     */
    private final String GET_ALI_PAY_PARMS = "alipay/pay";
    /**
     * 查询微信支付订单状态(微信支付前端返回支付成功，但需要到我们自己后台服务器查询是否支付成功)
     */
    private final String QUERY_WX_PAY_RESULT = "wxpay/tradeQuery";
    /**
     * 查询支付宝支付订单状态(支付宝支付前端返回支付成功，但需要到我们自己后台服务器查询是否支付成功)
     */
    private final String QUERY_ALI_PAY_RESULT = "alipay/tradeQuery";
    /**
     * 查询用户是否绑定了手机号
     */
    private final String QUERY_BINDING_PHONE = "safe/bind_phone";
    /**
     * 查询是否设置了提现密码
     */
    private final String QUERY_PAY_PWD = "safe/existsPassword";
    /**
     * 余额支付
     */
    private final String PAY_BY_BALANCE = "prepaid/doBalancePay";
    private OrderInfo orderInfo;
    private OrderListInfo orderListInfo;
    private OrderDetailInfo orderDetailInfo;

    private TextView orderNo;
    private TextView payMoney;
    private Button balancePayBtn;
    private Button chatPayBtn;
//    private Button zhifubaoPayBtn;

    private ProgressDialog progressDialog;

    /**
     * 微信支付api
     */
    private IWXAPI wxApi;

    /**
     * 当前订单信息有效的标识值 0=orderInfo,1=orderListInfo,2=orderDetailInfo
     */
    private int curOrderInfoPosition = 0;

    private static final int SDK_PAY_FLAG = 1;

    /**
     * 当前支付的订单的订单号(有可能是订单序列号或者订单号)
     */
    private String curPayOrderNo;

    /**
     * 实际支付金额
     */
    private double payRealMoney;
    /**
     * 需支付金额的格式化字符串，带有“￥”单位
     */
    private String payRealMoneyFormat;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            if (msg.what == SDK_PAY_FLAG) {
                // 支付宝支付信息返回
                @SuppressWarnings("unchecked")
                AliPayResult payResult = new AliPayResult((Map<String, String>) msg.obj);
                /**
                 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                 */
                String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                String resultStatus = payResult.getResultStatus();
                DLog.i("resultStatus---->>>" + resultStatus);
                switch (resultStatus) {
                    case "9000":
                        // 支付成功
                        // 检查支付宝支付的订单信息
                        Map<String, String> checkOrderParams = new HashMap<>();
                        checkOrderParams.put("out_trade_no", curPayOrderNo);
                        loadDataPost(QUERY_ALI_PAY_RESULT, checkOrderParams);
//                        progressDialog.show();
                        Util.showLoadingDialog(ConfirmPayActivity.this, progressDialog);
                        break;
                    case "8000":
                        // 支付结果确认中
                        Util.showToast(ConfirmPayActivity.this, getResources().getString(R.string
                                .ali_pay_checking));
                        break;
                    case "4000":
                        // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                        Util.showToast(ConfirmPayActivity.this, getResources().getString(R.string
                                .no_installed_ali));
                        break;
                    default:
                        Util.showErrorMessage(ConfirmPayActivity.this, payResult.getMemo(),
                                getResources
                                        ().getString(R.string.pay_failed));
                        break;
                }
            }
        }
    };

    @Override
    protected void initView() {
        setTitleName(getResources().getString(R.string.confirm_pay));
        orderInfo = (OrderInfo) getIntent().getSerializableExtra("orderInfo");
        orderListInfo = (OrderListInfo) getIntent().getSerializableExtra("orderListInfo");
        orderDetailInfo = (OrderDetailInfo) getIntent().getSerializableExtra("orderDetailInfo");
        if (orderInfo == null && orderListInfo == null && orderDetailInfo == null) {
            showNoDataView();
            return;
        }

        setCenterView(R.layout.activity_confirm_pay);
        orderNo = (TextView) findViewById(R.id.confirm_pay_order_num);
        payMoney = (TextView) findViewById(R.id.confirm_pay_money);
        balancePayBtn = (Button) findViewById(R.id.confirm_pay_by_balance_btn);
        chatPayBtn = (Button) findViewById(R.id.confirm_pay_by_chat_btn);
//        zhifubaoPayBtn = (Button) findViewById(R.id.confirm_pay_by_zhifubao_btn);

        balancePayBtn.setOnClickListener(this);
        chatPayBtn.setOnClickListener(this);
//        zhifubaoPayBtn.setOnClickListener(this);

        setData();
        showCenterView();


        // 初始化微信支付api
        wxApi = WXAPIFactory.createWXAPI(ConfirmPayActivity.this, Constants.WX_APP_ID, true);
        wxApi.registerApp(Constants.WX_APP_ID);

        registerReceiver(broadcastReceiver, new IntentFilter(Constants.ACTION_PAY_SUCCESS));
    }

    private void setData() {
        if (orderInfo != null) {
            orderNo.setText(orderInfo.getSerialNumber());
            payRealMoney = orderInfo.getRealPayment();
            payRealMoneyFormat = String.format(getResources().getString(R.string.format_money),
                    payRealMoney + "");
            curPayOrderNo = orderInfo.getSerialNumber();
            curOrderInfoPosition = 0;
        } else {
            if (orderListInfo != null) {
                orderNo.setText(orderListInfo.getOrderNo());
                payRealMoney = orderListInfo.getRealTotalPrice();
                payRealMoneyFormat = String.format(getResources().getString(R.string.format_money),
                        payRealMoney + "");
                curPayOrderNo = orderListInfo.getOrderNo();
                curOrderInfoPosition = 1;
            } else {
                if (orderDetailInfo != null) {
                    orderNo.setText(orderDetailInfo.getOrderNo());
                    payRealMoney = orderDetailInfo.getRealTotalPrice();
                    payRealMoneyFormat = String.format(getResources().getString(R.string
                            .format_money), payRealMoney + "");
                    curPayOrderNo = orderDetailInfo.getOrderNo();
                    curOrderInfoPosition = 2;
                }
            }
        }
        payMoney.setText(payRealMoneyFormat);
    }

    @Override
    protected void onLoadDataSuccess(String tag, JSONObject resultObject) {
        super.onLoadDataSuccess(tag, resultObject);
        if (TextUtils.equals(tag, GET_WX_PREPARE_BY_SERIAL_NUMBER) || TextUtils.equals(tag,
                GET_WX_PREPARE_BY_ORDER_NO)) {
            // 获取微信预支付订单信息成功
            WXPrepareInfo prepareInfo = ParseUtil.parseWxPrepareInfo(resultObject);
            if (prepareInfo == null) {
                progressDialog.dismiss();
                Util.showToast(ConfirmPayActivity.this, getResources().getString(R.string
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
                PreferencesUtils.putString(Constants.WX_PAY_ORDER_NO, curPayOrderNo);
                wxApi.sendReq(req);
            }
        } else if (TextUtils.equals(tag, GET_ALI_PAY_PARMS)) {
            // 获取支付宝支付所需参数成功
            final String payParams = ParseUtil.parseAliPayInfo(resultObject);
            progressDialog.dismiss();
            if (payParams == null || TextUtils.isEmpty(payParams)) {
                Util.showToast(ConfirmPayActivity.this, getResources().getString(R.string
                        .get_ali_pay_failed));
                return;
            }
            Runnable payRunnable = new Runnable() {
                @Override
                public void run() {
                    // 构造PayTask 对象
                    PayTask alipay = new PayTask(ConfirmPayActivity.this);
                    Map<String, String> result = alipay.payV2(payParams, true);
                    DLog.i("支付宝支付返回数据---->>" + result.toString());

                    Message msg = new Message();
                    msg.what = SDK_PAY_FLAG;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }
            };
            // 必须异步调用
            Thread payThread = new Thread(payRunnable);
            payThread.start();
        } else if (TextUtils.equals(tag, QUERY_WX_PAY_RESULT)) {
            // 查询微信支付订单状态成功
            progressDialog.dismiss();
            try {
                String state = resultObject.getString("trade_state");
                if (TextUtils.equals(state, "SUCCESS")) {
                    // 能查询到支付订单信息，微信支付成功
                    paySuccess(Constants.WX_PAY_TYPE, resultObject.getString("out_trade_no"));
                } else {
                    // 不能查询到订单支付的信息，支付失败
                    Util.showToast(ConfirmPayActivity.this, getResources()
                            .getString(R.string.pay_failed));
                }
            } catch (Exception e) {
                DLog.e("ConfirmPayActivity", "解析查询微信支付订单状态信息异常#exception:\n", e);
            }
        } else if (TextUtils.equals(tag, QUERY_ALI_PAY_RESULT)) {
            // 查询支付宝支付订单状态成功
            progressDialog.dismiss();
            try {
                String state = resultObject.getString("trade_state");
                if (TextUtils.equals(state, "SUCCESS")) {
                    // 能查询到支付订单信息，支付宝支付成功
                    paySuccess(Constants.ALI_PAY_TYPE, resultObject.getString("out_trade_no"));
                } else {
                    // 不能查询到订单支付的信息，支付失败
                    Util.showToast(ConfirmPayActivity.this, getResources()
                            .getString(R.string.pay_failed));
                }
            } catch (Exception e) {
                DLog.e("ConfirmPayActivity", "解析查询支付宝支付订单状态信息异常#exception:\n", e);
            }
        } else if (TextUtils.equals(tag, QUERY_BINDING_PHONE)) {
            // 查询用户是否绑定手机成功
            String phone = "";
            try {
                phone = resultObject.getString("validateMobile");
                UserInfoManager.getInstance().getUserInfo().setPhone(phone);
            } catch (Exception e) {
                DLog.e("ConfirmPayActivity", "查询绑定手机解析出现异常#exception：\n", e);
            }
            if (TextUtils.isEmpty(phone)) {
                // 没有绑定手机号,跳转到绑定手机的界面
                progressDialog.dismiss();
                Util.showToast(ConfirmPayActivity.this, getResources().getString(R.string
                        .not_binding_tips));
                BindingPhoneActivity.startActivity(ConfirmPayActivity.this);
            } else {
                // 有绑定手机号,再去查询是否有设置了提现密码
                Map<String, Object> params = new HashMap<>();
                // 查询类型 1=登录密码，2=提现密码,3=支付密码
                params.put("type", "3");
                loadDataGet(QUERY_PAY_PWD, params);
            }
        } else if (TextUtils.equals(tag, QUERY_PAY_PWD)) {
            // 查询是否设置了支付密码成功
            progressDialog.dismiss();
            int exists = 0;
            try {
                exists = resultObject.getInt("exists");
            } catch (Exception e) {
                DLog.e("ConfirmPayActivity", "解析是否设置了支付密码成功时异常#Exception:\n", e);
            }
            UserInfoManager.getInstance().getUserInfo().setWithdrawPwd(exists == 1);
            if (exists == 0) {
                // 没有设置支付密码
                // 跳转到设置支付密码界面
                SettingPayPwdActivity.startActivity(ConfirmPayActivity.this);
            } else {
                // 有设置提现密码
                // 弹出余额支付输入密码界面
                showBalancePayDialog();
            }
        } else if (TextUtils.equals(tag, PAY_BY_BALANCE)) {
            // 余额支付成功
            progressDialog.dismiss();
            paySuccess(Constants.BALANCE_PAY_TYPE, curPayOrderNo);
            Util.showToast(ConfirmPayActivity.this, getResources().getString(R.string.pay_success));
        }
    }

    @Override
    protected void onLoadDataError(String tag, int errorCode, String msg) {
        super.onLoadDataError(tag, errorCode, msg);
        if (TextUtils.equals(tag, GET_WX_PREPARE_BY_SERIAL_NUMBER) || TextUtils.equals(tag,
                GET_WX_PREPARE_BY_ORDER_NO)) {
            // 获取微信预支付订单信息失败
            progressDialog.dismiss();
            Util.showErrorMessage(ConfirmPayActivity.this, msg, getResources().getString(R.string
                    .get_wxprepareinfo_failed));
            if (errorCode == Constants.LOGIN_TIME_OUT) {
                LoginActivity.startActivity(ConfirmPayActivity.this);
            }
        } else if (TextUtils.equals(tag, GET_ALI_PAY_PARMS)) {
            // 获取支付宝支付所需参数失败
            progressDialog.dismiss();
            Util.showErrorMessage(ConfirmPayActivity.this, msg, getResources().getString(R.string
                    .get_ali_pay_failed));
            if (errorCode == Constants.LOGIN_TIME_OUT) {
                LoginActivity.startActivity(ConfirmPayActivity.this);
            }
        } else if (TextUtils.equals(tag, QUERY_WX_PAY_RESULT) || TextUtils.equals(tag,
                QUERY_ALI_PAY_RESULT)) {
            // 查询支付订单状态失败(微信支付和支付宝)
            progressDialog.dismiss();
            // 查询订单支付信息时出现了异常(有可能是网络，服务器等原因，支付也可能失败了)
            // 发送广播让订单列表等界面重新去获取订单信息
            checkPayInfoFailed();
        } else if (TextUtils.equals(tag, QUERY_BINDING_PHONE)) {
            // 查询用户是否绑定手机失败
            progressDialog.dismiss();
            Util.showErrorMessage(ConfirmPayActivity.this, msg, getResources().getString(R.string
                    .get_user_info_failed));
        } else if (TextUtils.equals(tag, QUERY_PAY_PWD)) {
            // 查询是否设置了支付密码失败
            progressDialog.dismiss();
            Util.showErrorMessage(ConfirmPayActivity.this, msg, getResources().getString(R.string
                    .get_user_info_failed));
        } else if (TextUtils.equals(tag, PAY_BY_BALANCE)) {
            // 余额支付失败
            progressDialog.dismiss();
            Util.showErrorMessage(ConfirmPayActivity.this, msg, getResources().getString(R.string
                    .pay_failed));
            if (errorCode == Constants.LOGIN_TIME_OUT) {
                LoginActivity.startActivity(ConfirmPayActivity.this);
            }
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

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.confirm_pay_by_chat_btn:
                // 微信支付
                // 获取微信预支付所需要的信息(预支付id等)
                if (!wxApi.isWXAppInstalled() || !wxApi.isWXAppSupportAPI()) {
                    // 未安装微信
                    Util.showToast(ConfirmPayActivity.this, getResources().getString(R.string
                            .no_weiXin));
                    return;
                }
                getWxPrepareId();
                break;
//            case R.id.confirm_pay_by_zhifubao_btn:
//                // 支付宝支付
//                getAliPayParams();
//                break;
            case R.id.confirm_pay_by_balance_btn:
                // 余额支付
                UserInfo userInfo = UserInfoManager.getInstance().getUserInfo();
                if (TextUtils.isEmpty(userInfo.getPhone())) {
                    // 没有绑定手机，查询是否绑定了手机
                    loadDataGet(QUERY_BINDING_PHONE, null);
                    progressDialog = Util.showLoadingDialog(ConfirmPayActivity.this,
                            progressDialog);
                } else {
                    if (userInfo.isPayPwd()) {
                        // 有设置提现支付密码
                        // 弹出余额支付输入密码界面
                        showBalancePayDialog();
                    } else {
                        // 没有设置提现支付密码，去查询用户是否设置了提现支付密码
                        progressDialog = Util.showLoadingDialog(ConfirmPayActivity.this,
                                progressDialog);
                        Map<String, Object> params = new HashMap<>();
                        // 查询类型 1=登录密码，2=提现密码,3=余额支付密码
                        params.put("type", "3");
                        loadDataGet(QUERY_PAY_PWD, params);
                    }
                }

                break;
        }
    }

    /**
     * 获取微信预支付订单等信息
     */
    private void getWxPrepareId() {
        Map<String, String> prepareParams = new HashMap<>();
        prepareParams.put("appName", getResources().getString(R.string.app_name));
        prepareParams.put("action", "下单");
        progressDialog = Util.showLoadingDialog(ConfirmPayActivity.this, progressDialog);
        switch (curOrderInfoPosition) {
            case 0:
                curPayOrderNo = orderInfo.getSerialNumber();
                prepareParams.put("serialNumber", curPayOrderNo);
                loadDataPost(GET_WX_PREPARE_BY_SERIAL_NUMBER, prepareParams);
                break;
            case 1:
                curPayOrderNo = orderListInfo.getOrderNo();
                prepareParams.put("orderNo", curPayOrderNo);
                loadDataPost(GET_WX_PREPARE_BY_ORDER_NO, prepareParams);
                break;
            case 2:
                curPayOrderNo = orderDetailInfo.getOrderNo();
                prepareParams.put("orderNo", curPayOrderNo);
                loadDataPost(GET_WX_PREPARE_BY_ORDER_NO, prepareParams);
                break;
        }
    }


    /**
     * 获取支付宝支付所需参数
     */
    private void getAliPayParams() {
        Map<String, String> aliParams = new HashMap<>();
        progressDialog = Util.showLoadingDialog(ConfirmPayActivity.this, progressDialog);
        switch (curOrderInfoPosition) {
            case 0:
                curPayOrderNo = orderInfo.getSerialNumber();
                aliParams.put("out_trade_no", curPayOrderNo);
                aliParams.put("total_amount", orderInfo.getRealPayment() + "");
                aliParams.put("subject", orderInfo.getSerialNumber() + "");
                break;
            case 1:
                curPayOrderNo = orderListInfo.getOrderNo();
                aliParams.put("out_trade_no", curPayOrderNo);
                aliParams.put("total_amount", orderListInfo.getRealTotalPrice() + "");
                aliParams.put("subject", orderListInfo.getOrderNo() + "");
                break;
            case 2:
                curPayOrderNo = orderDetailInfo.getOrderNo();
                aliParams.put("out_trade_no", curPayOrderNo);
                aliParams.put("total_amount", orderDetailInfo.getRealTotalPrice() + "");
                aliParams.put("subject", orderDetailInfo.getOrderNo() + "");
                break;
        }

        DLog.i("curPayOrderNo------>>>" + curPayOrderNo);
        loadDataPost(GET_ALI_PAY_PARMS, aliParams);
    }


    /**
     * 真正的支付成功处理
     */
    private void paySuccess(int payType, String orderNo) {
        // 发送真正支付成功的广播
        Intent intent = new Intent(Constants.ACTION_GET_PAY_ORDER_INFO_SUCCESS);
        intent.putExtra("payType", payType);
        intent.putExtra("orderNo", orderNo);
        sendBroadcast(intent);

        // 设置待收货数量+1
        UserInfoManager.getInstance().getUserInfo().setPaiedOrderNum(UserInfoManager.getInstance
                ().getUserInfo().getPaiedOrderNum() + 1);
        EventBus.getDefault().post(Constants.REFRESH_USER_INFO_VIEW);

//        // 通知再次去获取订单数量等信息
//        EventBus.getDefault().post(Constants.NEED_REFRESH_ANY_NUM);

        Util.showToast(ConfirmPayActivity.this, getResources().getString(R.string
                .pay_success));
        finish();
    }

    /**
     * 查询订单支付信息时出现了异常(有可能是网络，服务器等原因，支付也可能失败了)
     */
    private void checkPayInfoFailed() {
        // 发送广播让订单列表等界面重新去获取订单信息
        Intent intent = new Intent(Constants.ACTION_GET_PAY_ORDER_INFO_SUCCESS_REGET_DATA);
        sendBroadcast(intent);
        // 通知再次去获取订单数量等信息
        EventBus.getDefault().post(Constants.NEED_REFRESH_ANY_NUM);
        finish();
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (TextUtils.equals(Constants.ACTION_PAY_SUCCESS, intent.getAction())) {
                // 接收到支付成功的广播
                int payType = intent.getIntExtra("payType", -1);
                String orderNo = intent.getStringExtra("orderNo");
                if (payType == -1) {
                    return;
                }
                switch (payType) {
                    case Constants.WX_PAY_TYPE:
                        // 微信支付成功
                        Map<String, String> checkOrderParams = new HashMap<>();
                        checkOrderParams.put("out_trade_no", orderNo);
                        loadDataPost(QUERY_WX_PAY_RESULT, checkOrderParams);
//                        progressDialog.show();
                        Util.showLoadingDialog(ConfirmPayActivity.this, progressDialog);
                        break;
                    case Constants.ALI_PAY_TYPE:
                        // 支付宝支付成功
                        // 在这里就不做任何操作了  因为上面有支付宝handler接收到了支付成功的消息 已经处理了
                        break;
                    case Constants.BALANCE_PAY_TYPE:
                        // 余额支付成功
                        break;
                }
            }
        }
    };

    /**
     * 显示余额支付的dialog
     */
    private void showBalancePayDialog() {
        final Dialog dialog = new Dialog(ConfirmPayActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.balace_pay_dialog);
        TextView price = (TextView) dialog.findViewById(R.id.balance_pay_price);
        price.setText(payRealMoneyFormat);
        final PasswordInputView pwdInput = (PasswordInputView) dialog.findViewById(R.id
                .balance_pay_pwd_input);
        Button payBtn = (Button) dialog.findViewById(R.id.balance_pay_now_btn);
        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //进行余额支付，提交密码等
                String payPwdStr = pwdInput.getText().toString();
                if (TextUtils.isEmpty(payPwdStr)) {
                    Util.showToast(ConfirmPayActivity.this, getResources().getString(R.string
                            .please_input_pay_pwd));
                    return;
                }
                if (payPwdStr.length() != 6) {
                    Util.showToast(ConfirmPayActivity.this, getResources().getString(R.string
                            .please_input_pay_pwd_right));
                    return;
                }
                dialog.dismiss();
                progressDialog = Util.showLoadingDialog(ConfirmPayActivity.this, progressDialog);
                try {
                    Map<String, String> payParams = new HashMap<>();
                    JSONObject paramsObject = new JSONObject();
                    DLog.i("curPayOrderNo---->>" + curPayOrderNo);
                    DLog.i("payPwdStr---->>" + payPwdStr);
                    paramsObject.put("serialNumber", curPayOrderNo);
                    paramsObject.put("payPassword", payPwdStr);
                    String params = RSACrypt.encryptByPublicKey(paramsObject.toString().getBytes
                            (), Constants.RSA_PUBLIC_KEY);
                    payParams.put("params", params);
                    loadDataPost(PAY_BY_BALANCE, payParams);
                } catch (Exception e) {
                    progressDialog.dismiss();
                    DLog.e("ConfirmPayActivity", "余额支付组建参数时发生异常#excetpion:", e);
                }
            }
        });
        dialog.show();
    }

    public static void startActivity(Context context, OrderInfo orderInfo) {
        Intent intent = new Intent(context, ConfirmPayActivity.class);
        intent.putExtra("orderInfo", orderInfo);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, OrderListInfo orderListInfo) {
        Intent intent = new Intent(context, ConfirmPayActivity.class);
        intent.putExtra("orderListInfo", orderListInfo);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, OrderDetailInfo orderDetailInfo) {
        Intent intent = new Intent(context, ConfirmPayActivity.class);
        intent.putExtra("orderDetailInfo", orderDetailInfo);
        context.startActivity(intent);
    }
}
