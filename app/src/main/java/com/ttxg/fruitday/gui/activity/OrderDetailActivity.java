package com.ttxg.fruitday.gui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.manager.UserInfoManager;
import com.ttxg.fruitday.model.OrderDetailInfo;
import com.ttxg.fruitday.model.OrderGoodsInfo;
import com.ttxg.fruitday.model.OrderListInfo;
import com.ttxg.fruitday.model.UserInfo;
import com.ttxg.fruitday.util.Constants;
import com.ttxg.fruitday.util.ParseUtil;
import com.ttxg.fruitday.util.Util;
import com.ttxg.fruitday.util.log.DLog;

/**
 * 订单详情界面
 * Created by lilijun on 2016/9/28.
 */
public class OrderDetailActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 获取订单详情
     */
    private final String GET_ORDER_DETAIL = "order/findFrontOrderInfo";
    /**
     * 更改订单状态
     */
    private final String CHANGE_ORDER_STATUS = "order/setOrderInfoOrderStatus";

    /**
     * 删除了订单的resultCode
     */
    public static final int DELETE_ORDER_RESULT_CODE = 100011;

    private OrderListInfo orderListInfo;
    private TextView address;
    private TextView storeName;
    private LinearLayout goodsLay;
    private TextView postPrice;
    private TextView realPayMoney;
    private TextView orderNo;
    private TextView createTime;
    private TextView closeTime;
    /**
     * 发货时间
     */
    private TextView conSignTime;
    /**
     * 签收时间
     */
    private TextView signTime;
    private TextView cancelOrderBtn, goPayBtn, deleteOrderBtn, confirmTakeGoodsBtn, goCommentBtn;

    /**
     * 订单详情
     */
    private OrderDetailInfo orderDetailInfo;

    private Map<String, Object> params;

    /**
     * 请稍后dialog
     */
    private ProgressDialog progressDialog;

    /**
     * 更改订单状态标识 0=取消订单，1=删除订单，2=确认收货
     */
    private int changeOrderStatus = -1;

    @Override
    protected void initView() {
        orderListInfo = (OrderListInfo) getIntent().getSerializableExtra("orderListInfo");
        if (orderListInfo != null) {
            setTitle();

            params = new HashMap<>();
            params.put("orderId", orderListInfo.getId());
            params.put("begType", orderListInfo.getBegType());
        }
        setCenterView(R.layout.activity_order_detail);

        address = (TextView) findViewById(R.id.order_detail_address_info);
        storeName = (TextView) findViewById(R.id.order_detail_store_name);
        goodsLay = (LinearLayout) findViewById(R.id.order_detail_show_goods_lay);
        postPrice = (TextView) findViewById(R.id.order_detail_post_price);
        realPayMoney = (TextView) findViewById(R.id.order_detail_real_pay_money);
        orderNo = (TextView) findViewById(R.id.order_detail_order_no);
        createTime = (TextView) findViewById(R.id.order_detail_create_time);
        closeTime = (TextView) findViewById(R.id.order_detail_close_time);
        conSignTime = (TextView) findViewById(R.id.order_detail_consign_time);
        signTime = (TextView) findViewById(R.id.order_detail_sign_time);
        cancelOrderBtn = (TextView) findViewById(R.id.order_detail_cancel_order);
        cancelOrderBtn.setOnClickListener(this);
        goPayBtn = (TextView) findViewById(R.id.order_detail_go_pay);
        goPayBtn.setOnClickListener(this);
        deleteOrderBtn = (TextView) findViewById(R.id.order_detail_delete_order);
        deleteOrderBtn.setOnClickListener(this);
        confirmTakeGoodsBtn = (TextView) findViewById(R.id.order_detail_confirm_take_goods);
        confirmTakeGoodsBtn.setOnClickListener(this);
        goCommentBtn = (TextView) findViewById(R.id.order_detail_go_comment);
        goCommentBtn.setOnClickListener(this);
        loadDataGet(GET_ORDER_DETAIL, params);

        // 注册支付成功的广播
        IntentFilter filter = new IntentFilter(Constants.ACTION_GET_PAY_ORDER_INFO_SUCCESS);
        registerReceiver(receiver, filter);
    }


    @Override
    protected void onLoadDataSuccess(String tag, JSONObject resultObject) {
        super.onLoadDataSuccess(tag, resultObject);
        if (TextUtils.equals(tag, GET_ORDER_DETAIL)) {
            // 获取订单详情成功
            orderDetailInfo = ParseUtil.parseOrderDetailInfo(resultObject);
            if (orderDetailInfo != null) {
                setData();
                showGoodsView();
                showCenterView();
            }
        } else if (TextUtils.equals(tag, CHANGE_ORDER_STATUS)) {
            // 更改订单状态成功
            progressDialog.dismiss();
            DLog.i("确认收货成功----changeOrderStatus-->>>" + changeOrderStatus);
            if (changeOrderStatus == 0) {
                // 取消订单成功
                // 更改状态为取消
                orderDetailInfo.setStatus(5);
                // 更改当前时间为关闭订单时间
                java.text.DateFormat format1 = new java.text.SimpleDateFormat(
                        "yyyy-MM-dd hh:mm:ss");
                String formatTime = format1.format(new Date(System.currentTimeMillis()));
                orderDetailInfo.setCloseTime(formatTime);
                setCancelOrderView();

                // 将待付款的订单数量减去1
                UserInfo userInfo = UserInfoManager.getInstance().getUserInfo();
                if (userInfo != null) {
                    userInfo.setNoPayOrderNum(userInfo.getNoPayOrderNum() - 1);
                }
                // 发送订单取消成功的消息
                EventBus.getDefault().post(new String[]{Constants
                        .CANCEL_ORDER_SUCCESS_FROM_DETAIL, orderDetailInfo.getId() + ""});
                //                EventBus.getDefault().post(Constants.NEED_REFRESH_ANY_NUM);
                EventBus.getDefault().post(Constants.REFRESH_USER_INFO_VIEW);
            } else if (changeOrderStatus == 1) {
                // 删除订单成功
//                if (isFromAllTab) {
//                    // 从“全部”tab 跳转到此界面的
//                    EventBus.getDefault().post(Constants
// .DELETE_ORDER_SUCCESS_FROM_DETAIL_FOR_ALL);
//                } else {
//                    EventBus.getDefault().post(Constants.DELETE_ORDER_SUCCESS_FROM_DETAIL);
//                }
                if (orderListInfo != null) {
                    Intent intent = new Intent();
                    intent.putExtra("orderId", orderListInfo.getId());
                    setResult(DELETE_ORDER_RESULT_CODE, intent);
                }
                finish();
            } else if (changeOrderStatus == 2) {
                // 确认收货成功
                setAlreadyTakeGoodsOrderView();

                UserInfo userInfo = UserInfoManager.getInstance().getUserInfo();
                if (userInfo != null) {
                    // 将待收货的订单数量减去1
                    userInfo.setUnTakeOverNum(userInfo.getUnTakeOverNum() - 1);
                    // 将待评价的订单数量加1
                    userInfo.setUnCommentNum(userInfo.getUnCommentNum() + 1);
                }
                // 发送确认收货成功的消息
                EventBus.getDefault().post(new String[]{Constants
                        .CONFIRM_TAKE_GOODS_SUCCESS_FROM_DETAIL, orderDetailInfo.getId() + ""});
//                EventBus.getDefault().post(Constants.NEED_REFRESH_ANY_NUM);
                EventBus.getDefault().post(Constants.REFRESH_USER_INFO_VIEW);
            }
        }
    }

    @Override
    protected void onLoadDataError(String tag, int errorCode, String msg) {
        super.onLoadDataError(tag, errorCode, msg);
        if (TextUtils.equals(tag, GET_ORDER_DETAIL)) {
            // 获取订单详情失败
            showErrorView();
        } else if (TextUtils.equals(tag, CHANGE_ORDER_STATUS)) {
            if (changeOrderStatus == 0) {
                // 取消订单失败
                Util.showErrorMessage(OrderDetailActivity.this, msg, getResources().getString(R
                        .string.cancel_order_failed));
            } else if (changeOrderStatus == 1) {
                // 删除订单失败
                Util.showErrorMessage(OrderDetailActivity.this, msg, getResources().getString(R
                        .string.delete_failed));
            } else if (changeOrderStatus == 2) {
                // 确认收货失败
                Util.showErrorMessage(OrderDetailActivity.this, msg, getResources().getString(R
                        .string.take_goods_failed));
            }
            progressDialog.dismiss();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == CommentOrderGoodsActivity.COMMENT_SUCCESS_RESULT_CODE) {
            setResult(resultCode, data);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private void setTitle() {
        switch (orderListInfo.getStatus()) {
            case OrderListInfo.ORDER_STATUS_NEW:
                // 待支付
                setTitleName(getResources().getString(R.string.order_detail1));
                break;
            case OrderListInfo.ORDER_STATUS_PAID:
                // 待发货
                setTitleName(getResources().getString(R.string.order_detail2));
                break;
            case OrderListInfo.ORDER_STATUS_SENT:
                // 待收货
                setTitleName(getResources().getString(R.string.order_detail3));
                break;
            case OrderListInfo.ORDER_STATUS_SIGN:
                // 已收货
                setTitleName(getResources().getString(R.string.order_detail4));
                break;
            case OrderListInfo.ORDER_STATUS_CANCEL:
                // 已取消(交易关闭)
                setTitleName(getResources().getString(R.string.order_detail5));
                break;
        }
    }


    /**
     * 设置订单数据显示
     */
    private void setData() {
        StringBuilder addressBuilder = new StringBuilder(orderDetailInfo.getAddress().getName());
        addressBuilder.append(",");
        addressBuilder.append(orderDetailInfo.getAddress().getMobile());
        addressBuilder.append(",");
        addressBuilder.append(orderDetailInfo.getAddress().getProvince());
        addressBuilder.append(orderDetailInfo.getAddress().getCity());
        addressBuilder.append(orderDetailInfo.getAddress().getTown());
        addressBuilder.append(orderDetailInfo.getAddress().getDetailAddress());
        address.setText(addressBuilder.toString());
        storeName.setText(orderDetailInfo.getStoreName());
        postPrice.setText(String.format(getResources().getString(R.string.format_money),
                orderDetailInfo.getPostage() + ""));
        realPayMoney.setText(String.format(getResources().getString(R.string.format_money),
                orderDetailInfo.getRealTotalPrice() + ""));
        orderNo.setText(String.format(getResources().getString(R.string.format_order_no),
                orderDetailInfo.getOrderNo()));
        createTime.setText(String.format(getResources().getString(R.string
                .format_create_order_time), orderDetailInfo.getCreateTime()));
        conSignTime.setText(String.format(getResources().getString(R.string
                .format_consign_time), orderDetailInfo.getConsignTime()));
        signTime.setText(String.format(getResources().getString(R.string
                .format_sign_time), orderDetailInfo.getSignTime()));
        DLog.i("orderDetailInfo.getStatus()----->>>" + orderDetailInfo.getStatus());
        DLog.i("是否评价过----->>>" + orderDetailInfo.isCommented());
        switch (orderDetailInfo.getStatus()) {
            case 1:
                // 待支付
                setWaitPayOrderView();
                break;
            case 2:
                // 待发货
                setWaitSendGoodsOrderView();
                break;
            case 3:
                // 待收货
                setWaitTakeGoodsOrderView();
                break;
            case 4:
                // 已收货
                setAlreadyTakeGoodsOrderView();
                break;
            case 5:
                // 订单为取消状态
                setCancelOrderView();
                break;
        }
    }

    /**
     * 取消订单状态时的视图显示
     */
    private void setCancelOrderView() {
        closeTime.setText(String.format(getResources().getString(R.string
                        .format_close_order_time),
                orderDetailInfo.getCloseTime()));
        closeTime.setVisibility(View.VISIBLE);
        cancelOrderBtn.setVisibility(View.GONE);
        goPayBtn.setVisibility(View.GONE);
        deleteOrderBtn.setVisibility(View.VISIBLE);

        confirmTakeGoodsBtn.setVisibility(View.GONE);
        goCommentBtn.setVisibility(View.GONE);
    }

    /**
     * 待支付状态是的视图显示
     */
    private void setWaitPayOrderView() {
        closeTime.setVisibility(View.GONE);
        cancelOrderBtn.setVisibility(View.VISIBLE);
        goPayBtn.setVisibility(View.VISIBLE);
        deleteOrderBtn.setVisibility(View.GONE);

        confirmTakeGoodsBtn.setVisibility(View.GONE);
        goCommentBtn.setVisibility(View.GONE);
        conSignTime.setVisibility(View.GONE);
        signTime.setVisibility(View.GONE);
    }

    /**
     * 待发货
     */
    private void setWaitSendGoodsOrderView() {
        closeTime.setVisibility(View.GONE);
        cancelOrderBtn.setVisibility(View.GONE);
        deleteOrderBtn.setVisibility(View.GONE);
        goPayBtn.setVisibility(View.GONE);
        confirmTakeGoodsBtn.setVisibility(View.GONE);
        goCommentBtn.setVisibility(View.GONE);
        conSignTime.setVisibility(View.GONE);
        signTime.setVisibility(View.GONE);
    }

    /**
     * 待收货
     */
    private void setWaitTakeGoodsOrderView() {
        closeTime.setVisibility(View.GONE);
        cancelOrderBtn.setVisibility(View.GONE);
        deleteOrderBtn.setVisibility(View.GONE);
        goPayBtn.setVisibility(View.GONE);
        confirmTakeGoodsBtn.setVisibility(View.VISIBLE);
        goCommentBtn.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(orderDetailInfo.getConsignTime())) {
            conSignTime.setVisibility(View.VISIBLE);
            signTime.setVisibility(View.VISIBLE);
        } else {
            conSignTime.setVisibility(View.GONE);
            signTime.setVisibility(View.GONE);
        }
    }

    /**
     * 已收货(待评价)
     */
    private void setAlreadyTakeGoodsOrderView() {
        if (!orderDetailInfo.isCommented()) {
            // 没有评价过
            closeTime.setVisibility(View.GONE);
            cancelOrderBtn.setVisibility(View.GONE);
            deleteOrderBtn.setVisibility(View.GONE);
            goPayBtn.setVisibility(View.GONE);
            confirmTakeGoodsBtn.setVisibility(View.GONE);
            goCommentBtn.setVisibility(View.VISIBLE);
        } else {
            // 已评价过
            closeTime.setVisibility(View.GONE);
            cancelOrderBtn.setVisibility(View.GONE);
            deleteOrderBtn.setVisibility(View.GONE);
            goPayBtn.setVisibility(View.GONE);
            confirmTakeGoodsBtn.setVisibility(View.GONE);
            goCommentBtn.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(orderDetailInfo.getSignTime())) {
            conSignTime.setVisibility(View.VISIBLE);
            signTime.setVisibility(View.VISIBLE);
        } else {
            conSignTime.setVisibility(View.GONE);
            signTime.setVisibility(View.GONE);
        }
    }


    /**
     * 显示商品信息部分
     */
    private void showGoodsView() {
        for (OrderGoodsInfo goodsInfo : orderDetailInfo.getGoodsList()) {
            View goodsView = View.inflate(OrderDetailActivity.this, R.layout
                    .order_detail_goods_item, null);
            SimpleDraweeView icon = (SimpleDraweeView) goodsView.findViewById(R.id
                    .order_detail_goods_icon);
            TextView name = (TextView) goodsView.findViewById(R.id.order_detail_goods_name);
            TextView skuValues = (TextView) goodsView.findViewById(R.id.order_detail_sku_info);
            TextView buyNum = (TextView) goodsView.findViewById(R.id.order_detail_buy_num);
            TextView price = (TextView) goodsView.findViewById(R.id.order_detail_goods_price);

            icon.setImageURI(goodsInfo.getIcon());
            name.setText(goodsInfo.getName());
            buyNum.setText(String.format(getResources().getString(R.string.num),
                    goodsInfo.getBuyNum() + ""));
            price.setText(String.format(getResources().getString(R.string.format_money),
                    goodsInfo.getTotalPrice() + ""));
            if (TextUtils.isEmpty(goodsInfo.getSkuValues())) {
                skuValues.setVisibility(View.INVISIBLE);
            } else {
                skuValues.setVisibility(View.VISIBLE);
                skuValues.setText("(" + goodsInfo.getSkuValues() + ")");
            }
            goodsLay.addView(goodsView);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.order_detail_cancel_order:
                // 取消订单
                AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailActivity.this);
                builder.setMessage(getResources().getString(R.string.confirm_cancel_order));
                builder.setTitle(getResources().getString(R.string.warm_prompt));
                builder.setPositiveButton(getResources().getString(R.string.sure), new
                        DialogInterface
                                .OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 确定点击事件
                                // 标识是取消订单
                                changeOrderStatus = 0;
                                Map<String, String> params = new HashMap<>();
                                params.put("id", orderDetailInfo.getId() + "");
                                params.put("status", OrderListInfo.ORDER_STATUS_CANCEL + "");
                                loadDataPost(CHANGE_ORDER_STATUS, params);
                                dialog.dismiss();
                                progressDialog = Util.showLoadingDialog(OrderDetailActivity.this,
                                        progressDialog);
                            }
                        });
                builder.setNegativeButton(getResources().getString(R.string.cancel), new
                        DialogInterface
                                .OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 取消点击事件
                                dialog.dismiss();
                            }
                        });
                builder.create().show();
                break;
            case R.id.order_detail_go_pay:
                // 去付款
                ConfirmPayActivity.startActivity(OrderDetailActivity.this, orderDetailInfo);
                break;
            case R.id.order_detail_delete_order:
                // 删除订单
                AlertDialog.Builder deleteBuilder = new AlertDialog.Builder(OrderDetailActivity
                        .this);
                deleteBuilder.setMessage(getResources().getString(R.string.confirm_delete_order));
                deleteBuilder.setTitle(getResources().getString(R.string.warm_prompt));
                deleteBuilder.setPositiveButton(getResources().getString(R.string.sure), new
                        DialogInterface
                                .OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 确定点击事件
                                // 标识是删除订单
                                changeOrderStatus = 1;
                                Map<String, String> params = new HashMap<>();
                                params.put("id", orderListInfo.getId() + "");
                                params.put("isRemove", "true");
                                loadDataPost(CHANGE_ORDER_STATUS, params);
                                dialog.dismiss();
                                progressDialog = Util.showLoadingDialog(OrderDetailActivity.this,
                                        progressDialog);
                            }
                        });
                deleteBuilder.setNegativeButton(getResources().getString(R.string.cancel), new
                        DialogInterface
                                .OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 取消点击事件
                                dialog.dismiss();
                            }
                        });
                deleteBuilder.create().show();
                break;
            case R.id.order_detail_confirm_take_goods:
                // 确认收货
                Util.showAlertDialog(OrderDetailActivity.this, getResources().getString(R.string
                                .warm_prompt),
                        getResources().getString(R.string.confirm_take_goods_msg), getResources()
                                .getString(R.string.cancel), getResources().getString(R.string
                                .sure), new
                                DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // 取消
                                        dialogInterface.dismiss();
                                    }
                                }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // 确定
                                // 标识是确认收货
                                changeOrderStatus = 2;
                                Map<String, String> params = new HashMap<>();
                                params.put("id", orderListInfo.getId() + "");
                                params.put("status", "4");
                                loadDataPost(CHANGE_ORDER_STATUS, params);
                                dialogInterface.dismiss();
                                progressDialog = Util.showLoadingDialog(OrderDetailActivity.this,
                                        progressDialog);
                            }
                        });
                break;
            case R.id.order_detail_go_comment:
                // 去评价
                CommentOrderGoodsActivity.startActivity(OrderDetailActivity.this, orderDetailInfo);
                break;
        }
    }


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (TextUtils.equals(intent.getAction(), Constants.ACTION_GET_PAY_ORDER_INFO_SUCCESS)) {
                // 接收到支付成功的广播
                finish();
            }
        }
    };

    public static void startActivity(Activity context, OrderListInfo orderListInfo, int
            requestCode) {
        Intent intent = new Intent(context, OrderDetailActivity.class);
        intent.putExtra("orderListInfo", orderListInfo);
        context.startActivityForResult(intent, requestCode);
    }
}
