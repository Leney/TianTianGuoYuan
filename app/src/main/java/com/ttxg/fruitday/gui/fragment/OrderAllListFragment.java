package com.ttxg.fruitday.gui.fragment;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.gui.activity.CommentOrderGoodsActivity;
import com.ttxg.fruitday.gui.activity.ConfirmPayActivity;
import com.ttxg.fruitday.gui.activity.LoginActivity;
import com.ttxg.fruitday.gui.activity.OrderDetailActivity;
import com.ttxg.fruitday.gui.adapter.OrderListAdapter;
import com.ttxg.fruitday.gui.view.RefreshListView;
import com.ttxg.fruitday.manager.UserInfoManager;
import com.ttxg.fruitday.model.OrderListInfo;
import com.ttxg.fruitday.model.UserInfo;
import com.ttxg.fruitday.util.Constants;
import com.ttxg.fruitday.util.ParseUtil;
import com.ttxg.fruitday.util.Util;

/**
 * 全部 订单信息列表Fragment
 * Created by lilijun on 2016/9/26.
 */
public class OrderAllListFragment extends BaseFragment implements OrderListAdapter
        .OnCancelOrderListener, OrderListAdapter.OnDeleteOrderListener, OrderListAdapter
        .OnPayOrderListener, OrderListAdapter.OnConfirmTakeGoodsListener, OrderListAdapter
        .OnGoCommentListener {
    /**
     * 查询订单列表
     */
    private final String SEARCH_ORDER_LIST = "order/findFrontOrders";
    /**
     * 更改订单状态
     */
    private final String CHANGE_ORDER_STATUS = "order/setOrderInfoOrderStatus";

    private RefreshListView listView;

    private OrderListAdapter adapter;

    private List<OrderListInfo> orderList;

    private Map<String, Object> params;

    private int curPage = 1;

//    /**
//     * 总共有多少页数
//     */
//    private int totalPage = 0;

    /**
     * 当前Fragment是否是第一次加载显示
     */
    private boolean isFirstLoad = true;

    /**
     * 加载一页的数据长度
     */
    private final int LOAD_DATA_SIZE = 10;

    /**
     * 请稍后dialog
     */
    private ProgressDialog progressDialog;

    /**
     * 更改订单状态的position
     */
    private int changeStatusPosition = -1;

    /**
     * 更改订单状态标识 0=取消订单，1=删除订单，2=确认收货
     */
    private int changeOrderStatus = -1;

    /**
     * 跳转到详情界面时的
     */
    private int skipDetialPosition = -1;

    /**
     * 是否正在刷新
     */
    private boolean isRefreshing = false;

    public void init() {
        orderList = new ArrayList<>();

        params = new HashMap<>();
        // begType:订单类型。1=我的订单，2=直销订单，4=分销订单
        params.put("begType", 1);
        params.put("orderStatus", 0);
        params.put("start", curPage);
        params.put("limit", LOAD_DATA_SIZE);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && isFirstLoad) {
            // 只在第一次显示视图时去加载网络数据
            isFirstLoad = false;
            loadDataGet(SEARCH_ORDER_LIST, params);
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        IntentFilter filter = new IntentFilter(Constants.ACTION_GET_PAY_ORDER_INFO_SUCCESS);
        filter.addAction(Constants.ACTION_GET_PAY_ORDER_INFO_SUCCESS_REGET_DATA);
        getActivity().registerReceiver(receiver, filter);
    }

    @Override
    protected void initView(RelativeLayout view) {
        listView = new RefreshListView(getActivity());
        setCenterView(listView);
        adapter = new OrderListAdapter(orderList);
        adapter.setOnCancelOrderLinstener(this);
        adapter.setOnDeleteOrderLinstener(this);
        adapter.setOnPayOrderLinstener(this);
        adapter.setOnConfirmTakeGoodsListener(this);
        adapter.setOnGoCommentListener(this);
        listView.setAdapter(adapter);
        listView.setOnNextPageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 加载下一页
                params.put("start", curPage + 1);
                loadDataGet(SEARCH_ORDER_LIST, params);
                listView.setNextPageLoading();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // 跳转到订单详情界面
                OrderDetailActivity.startActivity(getActivity(), orderList.get(i - 1), 10011);
            }
        });
        listView.setonRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefreshing = true;
                params.put("start", 1);
                loadDataGet(SEARCH_ORDER_LIST, params);
            }
        });
    }

    @Override
    protected void onLoadDataSuccess(String tag, JSONObject resultObject) {
        super.onLoadDataSuccess(tag, resultObject);
        if (TextUtils.equals(tag, SEARCH_ORDER_LIST)) {
            // 获取订单列表成功
            int dataSize = 0;
            if (isRefreshing) {
                // 下拉刷新成功
                List<OrderListInfo> emptyList = new ArrayList<>();
                int[] result = ParseUtil.parseOrderList(resultObject, emptyList);
                if (result == null) {
                    // 刷新失败
                    Util.showToast(getActivity(), getResources().getString(R.string.refresh_fail));
                } else {
                    // 刷新成功
                    orderList.clear();
                    orderList.addAll(emptyList);
                    curPage = result[0];
                    dataSize = result[1];
                }
                listView.onRefreshComplete();
                isRefreshing = false;
            } else {
                // 不是下拉刷新成功
                int[] result = ParseUtil.parseOrderList(resultObject, orderList);
                if (result == null) {
                    // 加载失败
                    showErrorView();
                } else {
                    // 加载成功
                    curPage = result[0];
                    dataSize = result[1];
                }
            }
            if (!orderList.isEmpty()) {
                adapter.notifyDataSetChanged();
                showCenterView();
            } else {
                // 没有订单数据
                showNoDataView();
            }

            if (dataSize < LOAD_DATA_SIZE) {
                // 没有下一页数据
                listView.setNextPageViewVisible(false);
            } else {
                // 还有下一页数据
                listView.setNextPageViewVisible(true);
                listView.setNextPageLoadNext();
            }
        } else if (TextUtils.equals(tag, CHANGE_ORDER_STATUS)) {
            // 更改订单状态成功
            if (changeOrderStatus == 0) {
                // 取消订单成功
                // 就是在全部的tab中取消的订单
                // 设置状态为 取消订单状态
                orderList.get(changeStatusPosition).setStatus(OrderListInfo
                        .ORDER_STATUS_CANCEL);
                // 将待付款的订单数量减去1
                UserInfo userInfo = UserInfoManager.getInstance().getUserInfo();
                if (userInfo != null) {
                    userInfo.setNoPayOrderNum(userInfo.getNoPayOrderNum() - 1);
                }
                EventBus.getDefault().post(Constants.REFRESH_USER_INFO_VIEW);

                EventBus.getDefault().post(new String[]{Constants
                        .CANCEL_ORDER_SUCCESS_FROM_ALL, orderList.get(changeStatusPosition)
                        .getId() + ""});
//                EventBus.getDefault().post(Constants.NEED_REFRESH_ANY_NUM);
            } else if (changeOrderStatus == 1) {
                // 删除订单成功
                // 遍历删除本页的数据
                OrderListInfo orderListInfo = orderList.get(changeStatusPosition);
                if (orderListInfo != null) {
                    // 遍历一次数据、删除具有相同order id的数据
                    Iterator<OrderListInfo> iterator = orderList.iterator();
                    while (iterator.hasNext()) {
                        OrderListInfo tempInfo = iterator.next();
                        if (tempInfo.getId() == orderListInfo.getId()) {
                            iterator.remove();
                        }
                    }
                }
            } else if (changeOrderStatus == 2) {
                // 确认收货成功
                OrderListInfo orderListInfo = orderList.get(changeStatusPosition);
                if (orderListInfo != null) {
                    // 发送确认收货成功的广播
                    Intent intent = new Intent(Constants.ACTION_CONFIRM_TAKE_GOODS_SUCCESS);
                    ArrayList<OrderListInfo> confirmGoodsList = new ArrayList<>();
                    // 遍历一次数据、删除具有相同order id的数据
                    for (OrderListInfo info : orderList) {
                        if (info.getId() == orderListInfo.getId()) {
                            info.setStatus(OrderListInfo.ORDER_STATUS_SIGN);
                            confirmGoodsList.add(info);
                        }
                    }
                    intent.putExtra("confirm_take_goods_list", confirmGoodsList);
                    getActivity().sendBroadcast(intent);
                }
            }
            if (orderList.isEmpty()) {
                showNoDataView();
            }
            adapter.notifyDataSetChanged();
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onLoadDataError(String tag, int errorCode, String msg) {
        super.onLoadDataError(tag, errorCode, msg);
        if (TextUtils.equals(tag, SEARCH_ORDER_LIST)) {
            // 获取订单列表失败
            if (isRefreshing) {
                // 刷新失败
                listView.onRefreshComplete();
                isRefreshing = false;
                Util.showErrorMessage(getActivity(), msg, getResources().getString(R.string
                        .refresh_fail));
            } else {
                if (curPage == 1) {
                    // 加载第一页数据就失败了
                    if (errorCode == Constants.LOGIN_TIME_OUT) {
                        // 登录信息超时
                        Util.showErrorMessage(getActivity(), msg, getResources().getString(R.string
                                .get_order_list_failed));
                        LoginActivity.startActivity(getActivity());
                    }
                    showErrorView();
                } else {
                    // 加载下一页数据失败
                    listView.setNextPageViewLoadFailed();
                }
            }
        } else if (TextUtils.equals(tag, CHANGE_ORDER_STATUS)) {
            // 更改订单状态失败
            if (changeOrderStatus == 0) {
                // 取消订单失败
                Util.showErrorMessage(getActivity(), msg, getResources().getString(R
                        .string.cancel_order_failed));
            } else if (changeOrderStatus == 1) {
                // 删除订单失败
                Util.showErrorMessage(getActivity(), msg, getResources().getString(R
                        .string.delete_failed));
            } else if (changeOrderStatus == 2) {
                // 确认收货失败
                Util.showErrorMessage(getActivity(), msg, getResources().getString(R
                        .string.take_goods_failed));
            }
            progressDialog.dismiss();
        }
    }

    @Override
    protected void tryAgain() {
        super.tryAgain();
        curPage = 1;
        params.put("start", curPage);
        loadDataGet(SEARCH_ORDER_LIST, params);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        getActivity().unregisterReceiver(receiver);
        super.onDestroy();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (!isFirstLoad && resultCode == OrderDetailActivity.DELETE_ORDER_RESULT_CODE) {
            // 删除了订单
            int orderId = data.getIntExtra("orderId", -1);
            if (orderId != -1) {
                // 遍历一次数据、删除具有相同order id的数据
                Iterator<OrderListInfo> iterator = orderList.iterator();
                while (iterator.hasNext()) {
                    OrderListInfo tempInfo = iterator.next();
                    if (tempInfo.getId() == orderId) {
                        iterator.remove();
                    }
                }
                if (orderList.isEmpty()) {
                    showNoDataView();
                }
                adapter.notifyDataSetChanged();
            }
        } else if (resultCode == CommentOrderGoodsActivity.COMMENT_SUCCESS_RESULT_CODE) {
            // 评价成功返回
            if (isFirstLoad) {
                // 没有加载过本界面 就直接返回 后续不处理
                return;
            }
            int orderId = data.getIntExtra("orderId", -1);
            if (orderId != -1) {
                // 遍历一次数据、删除具有相同order id的数据
                Iterator<OrderListInfo> iterator = orderList.iterator();
                while (iterator.hasNext()) {
                    OrderListInfo tempInfo = iterator.next();
                    if (tempInfo.getId() == orderId) {
                        iterator.remove();
                    }
                }
                if (orderList.isEmpty()) {
                    showNoDataView();
                }
                adapter.notifyDataSetChanged();
            }
        }
    }


//    @Subscribe
//    public void onEventMessage(String msg) {
//        if (TextUtils.equals(msg, Constants.CANCEL_ORDER_SUCCESS)) {
//            // 是取消订单(从其他tab页发送过来的消息)
//            showLoadingView();
//            orderList.clear();
//            curPage = 1;
//            params.put("start", curPage);
//            // 从第一页开始加载
//            loadDataGet(SEARCH_ORDER_LIST, params);
//        }
//    }

    /**
     * @param infos [0]=具体消息的类型，[1]=订单id
     */
    @Subscribe
    public void onEventMessage(String[] infos) {
        if (infos != null) {
            if (TextUtils.equals(infos[0],
                    Constants.CANCEL_ORDER_SUCCESS_FROM_DETAIL) || TextUtils.equals(infos[0],
                    Constants.CANCEL_ORDER_SUCCESS)) {
                // 从订单详情发送过来的订单取消成功的消息
                int orderId = Integer.parseInt(infos[1]);
                // 遍历一次数据、更改具有相同orderId数据的状态为已取消
                for (OrderListInfo orderInfo : orderList) {
                    if (orderInfo.getId() == orderId) {
                        orderInfo.setStatus(OrderListInfo.ORDER_STATUS_CANCEL);
                    }
                }
                adapter.notifyDataSetChanged();
            }
//            else if (TextUtils.equals(infos[0],
//                    Constants.CANCEL_ORDER_SUCCESS_FROM_DETAIL)) {
//                // 从订单详情发送删除订单成功的消息
//                int orderId = Integer.parseInt(infos[1]);
//                // 遍历一次数据、删除具有相同order id的数据
//                Iterator<OrderListInfo> iterator = orderList.iterator();
//                while (iterator.hasNext()) {
//                    OrderListInfo tempInfo = iterator.next();
//                    if (tempInfo.getId() == orderId) {
//                        iterator.remove();
//                    }
//                }
//                if (orderList.isEmpty()) {
//                    showNoDataView();
//                }
//                adapter.notifyDataSetChanged();
//            }
            else if (TextUtils.equals(infos[0], Constants.CONFIRM_TAKE_GOODS_SUCCESS) ||
                    TextUtils.equals(infos[0], Constants.CONFIRM_TAKE_GOODS_SUCCESS_FROM_DETAIL)) {
                // 确认收货成功
                int orderId = Integer.parseInt(infos[1]);
                // 遍历一次数据、更改具有相同orderId数据的状态为已收货
                for (OrderListInfo orderInfo : orderList) {
                    if (orderInfo.getId() == orderId) {
                        orderInfo.setStatus(OrderListInfo.ORDER_STATUS_SIGN);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 搜索订单
     *
     * @param message 搜索条件
     */
    public void searchOrder(String message) {
        showLoadingView();
        orderList.clear();
        curPage = 1;
        params.put("conditions", message);
        params.put("start", curPage);
        loadDataGet(SEARCH_ORDER_LIST, params);
    }

    @Override
    public void onCancelOrder(final int position) {
        // 取消订单
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getResources().getString(R.string.confirm_cancel_order));
        builder.setTitle(getResources().getString(R.string.warm_prompt));
        builder.setPositiveButton(getResources().getString(R.string.sure), new DialogInterface
                .OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 确定点击事件
                OrderListInfo orderListInfo = orderList.get(position);
                if (orderListInfo == null) {
                    Util.showToast(getActivity(), getResources().getString(R.string
                            .cancel_order_failed));
                    dialog.dismiss();
                    return;
                }
                changeStatusPosition = position;
                // 标识是取消订单
                changeOrderStatus = 0;
                Map<String, String> params = new HashMap<>();
                params.put("id", orderListInfo.getId() + "");
                params.put("status", OrderListInfo.ORDER_STATUS_CANCEL + "");
//                params.put("isRate", orderListInfo.isCommented() + "");
//                params.put("isRemove", "false");
//                params.put("priceModifyStatus", orderListInfo.getPriceModifyStatus() + "");
                loadDataPost(CHANGE_ORDER_STATUS, params);
                dialog.dismiss();
                progressDialog = Util.showLoadingDialog(getActivity(), progressDialog);
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface
                .OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 取消点击事件
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public void onDeleteOrder(final int position) {
        // 删除订单
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getResources().getString(R.string.confirm_delete_order));
        builder.setTitle(getResources().getString(R.string.warm_prompt));
        builder.setPositiveButton(getResources().getString(R.string.sure), new DialogInterface
                .OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 确定点击事件
                OrderListInfo orderListInfo = orderList.get(position);
                if (orderListInfo == null) {
                    Util.showToast(getActivity(), getResources().getString(R.string
                            .cancel_order_failed));
                    dialog.dismiss();
                    return;
                }
                changeStatusPosition = position;
                // 标识是删除订单
                changeOrderStatus = 1;
                Map<String, String> params = new HashMap<>();
                params.put("id", orderListInfo.getId() + "");
//                params.put("status", orderListInfo.getStatus() + "");
//                params.put("isRate", orderListInfo.isCommented() + "");
                params.put("isRemove", "true");
//                params.put("priceModifyStatus", orderListInfo.getPriceModifyStatus() + "");
                loadDataPost(CHANGE_ORDER_STATUS, params);
                dialog.dismiss();
                progressDialog = Util.showLoadingDialog(getActivity(), progressDialog);
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface
                .OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 取消点击事件
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public void onPayOrder(int position) {
        // 去付款
        ConfirmPayActivity.startActivity(getActivity(), orderList.get(position));
    }

    @Override
    public void onConfirm(final int position) {
        // 确认收货
        Util.showAlertDialog(getActivity(), getResources().getString(R.string.warm_prompt),
                getResources().getString(R.string.confirm_take_goods_msg), getResources()
                        .getString(R.string.cancel), getResources().getString(R.string.sure), new
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
                        OrderListInfo orderListInfo = orderList.get(position);
                        if (orderListInfo == null) {
                            Util.showToast(getActivity(), getResources().getString(R.string
                                    .take_goods_failed));
                            dialogInterface.dismiss();
                            return;
                        }
                        changeStatusPosition = position;
                        // 标识是确认收货
                        changeOrderStatus = 2;
                        Map<String, String> params = new HashMap<>();
                        params.put("id", orderListInfo.getId() + "");
                        params.put("status", "4");
//                params.put("isRate", orderListInfo.isCommented() + "");
//                params.put("isRemove", "true");
//                params.put("priceModifyStatus", orderListInfo.getPriceModifyStatus() + "");
                        loadDataPost(CHANGE_ORDER_STATUS, params);
                        dialogInterface.dismiss();
                        progressDialog = Util.showLoadingDialog(getActivity(), progressDialog);
                    }
                });
    }

    @Override
    public void onGoComment(ArrayList<OrderListInfo> orderList) {
        // 去评论
        CommentOrderGoodsActivity.startActivity(getActivity(), orderList);
    }


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (TextUtils.equals(Constants.ACTION_GET_PAY_ORDER_INFO_SUCCESS, intent.getAction())) {
                // 接收到支付成功的广播
                int payType = intent.getIntExtra("payType", -1);
                String orderNo = intent.getStringExtra("orderNo");
                if (payType == -1 || TextUtils.isEmpty(orderNo)) {
                    return;
                }
//                switch (payType){
//                    case Constants.WX_PAY_TYPE:
//                        // 微信支付成功
//                        finish();
//                        break;
//                    case Constants.ALI_PAY_TYPE:
//                        // 支付宝支付成功
//                        // 在这里就不做任何操作了  因为上面有支付宝handler接收到了支付成功的消息 已经处理了
//                        break;
//                    case Constants.BALANCE_PAY_TYPE:
//                        // 余额支付成功
//                        break;
//                }

                // 支付成功
                // 遍历一次数据、更改具有相同orderId数据的状态为已收货
                for (OrderListInfo orderInfo : orderList) {
                    if (TextUtils.equals(orderNo, orderInfo.getOrderNo())) {
                        // 将状态值改为已付款
                        orderInfo.setStatus(OrderListInfo.ORDER_STATUS_PAID);
                    }
                }
                adapter.notifyDataSetChanged();
            } else if (TextUtils.equals(Constants.ACTION_GET_PAY_ORDER_INFO_SUCCESS_REGET_DATA,
                    intent.getAction())) {
                // 接收到前端支付成功但到服务器去检查订单支付情况时发生了异常
                // 重新去获取订单列表信息
                tryAgain();
            }
        }
    };
}
