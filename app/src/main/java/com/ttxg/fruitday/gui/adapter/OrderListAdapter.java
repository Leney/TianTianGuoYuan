package com.ttxg.fruitday.gui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.model.OrderListInfo;

/**
 * 订单列表Adapter
 * Created by lilijun on 2016/9/26.
 */
public class OrderListAdapter extends BaseAdapter {
    private List<OrderListInfo> orderList;

    private OnCancelOrderListener onCancelOrderLinstener;
    private OnDeleteOrderListener onDeleteOrderLinstener;
    private OnPayOrderListener onPayOrderLinstener;
    private OnConfirmTakeGoodsListener onConfirmTakeGoodsListener;
    private OnGoCommentListener onGoCommentListener;

    public OrderListAdapter(List<OrderListInfo> orderList) {
        this.orderList = orderList;
    }

    public void setOnCancelOrderLinstener(OnCancelOrderListener onCancelOrderLinstener) {
        this.onCancelOrderLinstener = onCancelOrderLinstener;
    }

    public void setOnDeleteOrderLinstener(OnDeleteOrderListener onDeleteOrderLinstener) {
        this.onDeleteOrderLinstener = onDeleteOrderLinstener;
    }

    public void setOnPayOrderLinstener(OnPayOrderListener onPayOrderLinstener) {
        this.onPayOrderLinstener = onPayOrderLinstener;
    }

    public void setOnConfirmTakeGoodsListener(OnConfirmTakeGoodsListener
                                                      onConfirmTakeGoodsListener) {
        this.onConfirmTakeGoodsListener = onConfirmTakeGoodsListener;
    }

    public void setOnGoCommentListener(OnGoCommentListener listener){
        this.onGoCommentListener = listener;
    }

    @Override
    public int getCount() {
        return orderList.size();
    }

    @Override
    public Object getItem(int i) {
        return orderList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        HolderView holderView;
        if (view == null) {
            view = View.inflate(viewGroup.getContext(), R.layout.order_list_adater, null);
            holderView = new HolderView();
            holderView.topLay = (LinearLayout) view.findViewById(R.id.order_list_adapter_top_lay);
            holderView.storeName = (TextView) view.findViewById(R.id.order_list_adapter_store_name);
            holderView.status = (TextView) view.findViewById(R.id.order_list_adapter_order_status);
            holderView.splitLine1 = view.findViewById(R.id.order_list_adapter_split_line);
            holderView.splitLine2 = view.findViewById(R.id.order_list_adapter_split_line2);
            holderView.splitLine3 = view.findViewById(R.id.order_list_adapter_bottom_split_line);
            holderView.goodsIcon = (SimpleDraweeView) view.findViewById(R.id
                    .order_list_adapter_goods_icon);
            holderView.goodsName = (TextView) view.findViewById(R.id.order_list_adapter_goods_name);
            holderView.buyNum = (TextView) view.findViewById(R.id.order_list_adapter_buy_num);
            holderView.price = (TextView) view.findViewById(R.id.order_list_adapter_goods_price);
            holderView.bottomLay = (LinearLayout) view.findViewById(R.id
                    .order_list_adapter_bottom_lay);
            holderView.payMoney = (TextView) view.findViewById(R.id
                    .order_list_adapter_real_total_price);
            holderView.cancelOrderBtn = (TextView) view.findViewById(R.id
                    .order_list_adapter_cancel_order);
            holderView.goPayBtn = (TextView) view.findViewById(R.id.order_list_adapter_go_pay);
            holderView.deleteOrderBtn = (TextView) view.findViewById(R.id
                    .order_list_adapter_delete_order);

//            holderView.checkLogistics = (TextView) view.findViewById(R.id
//                    .order_list_adapter_check_logistics);
            holderView.confirmTakeGoods = (TextView) view.findViewById(R.id
                    .order_list_adapter_confirm_take_goods);
            holderView.goComment = (TextView) view.findViewById(R.id.order_list_adapter_go_comment);

            holderView.cancelOrderBtn.setOnClickListener(cancelOrderClickListener);
            holderView.goPayBtn.setOnClickListener(goPayClickListener);
            holderView.deleteOrderBtn.setOnClickListener(deleteOrderClickListener);
            holderView.confirmTakeGoods.setOnClickListener(confirmTakeGoodsListener);
//            holderView.checkLogistics.setOnClickListener(checkLogisticsListener);
            holderView.goComment.setOnClickListener(goCommentListener);

            view.setTag(holderView);
        } else {
            holderView = (HolderView) view.getTag();
        }

        OrderListInfo orderInfo = (OrderListInfo) getItem(i);
        if (orderInfo.isFirstData()) {
            holderView.topLay.setVisibility(View.VISIBLE);
        } else {
            holderView.topLay.setVisibility(View.GONE);
        }
        if (orderInfo.isLastData()) {
            holderView.bottomLay.setVisibility(View.VISIBLE);
            holderView.splitLine3.setVisibility(View.VISIBLE);
            holderView.splitLine2.setVisibility(View.VISIBLE);
        } else {
            holderView.bottomLay.setVisibility(View.GONE);
            holderView.splitLine3.setVisibility(View.GONE);
            holderView.splitLine2.setVisibility(View.GONE);
        }

        holderView.storeName.setText(orderInfo.getStoreName());
        // 设置不同状态显示的不同视图
        setDifferentView(viewGroup.getContext(), holderView, orderInfo);

        holderView.goodsIcon.setImageURI(orderInfo.getGoodsIcon());
        holderView.goodsName.setText(orderInfo.getGoodsName());
        holderView.buyNum.setText(String.format(viewGroup.getResources().getString(R.string.num),
                orderInfo.getGoodsBuyNum() + ""));
        holderView.price.setText(String.format(viewGroup.getResources().getString(R.string
                .format_money), orderInfo.getGoodsPrice() + ""));
        holderView.payMoney.setText(String.format(viewGroup.getResources().getString(R.string
                .format_real_pay), orderInfo.getRealTotalPrice()));

        holderView.cancelOrderBtn.setTag(i);
        holderView.goPayBtn.setTag(i);
        holderView.deleteOrderBtn.setTag(i);
        holderView.confirmTakeGoods.setTag(i);
//        holderView.checkLogistics.setTag(i);
        holderView.goComment.setTag(orderInfo.getId());
        return view;
    }


    /**
     * 根据status设置不同的视图显示部分
     */
    private void setDifferentView(Context context, HolderView holderView, OrderListInfo orderInfo) {
        switch (orderInfo.getStatus()) {
            case 1:
                // 待支付
                holderView.status.setVisibility(View.VISIBLE);
                holderView.status.setText(context.getResources().getString(R.string.not_pay));
                holderView.deleteOrderBtn.setVisibility(View.GONE);
                holderView.cancelOrderBtn.setVisibility(View.VISIBLE);
                holderView.goPayBtn.setVisibility(View.VISIBLE);

//                holderView.checkLogistics.setVisibility(View.GONE);
                holderView.confirmTakeGoods.setVisibility(View.GONE);
                holderView.goComment.setVisibility(View.GONE);
                break;
            case 2:
                // 待发货
                holderView.status.setVisibility(View.VISIBLE);
                holderView.status.setText(context.getResources().getString(R.string
                        .not_send_goods));

                holderView.deleteOrderBtn.setVisibility(View.GONE);
                holderView.cancelOrderBtn.setVisibility(View.GONE);
                holderView.goPayBtn.setVisibility(View.GONE);

//                holderView.checkLogistics.setVisibility(View.GONE);
                holderView.confirmTakeGoods.setVisibility(View.GONE);
                holderView.goComment.setVisibility(View.GONE);
                break;
            case 3:
                // 待收货
                holderView.status.setVisibility(View.VISIBLE);
                holderView.status.setText(context.getResources().getString(R.string
                        .not_take_goods));
                holderView.deleteOrderBtn.setVisibility(View.GONE);
                holderView.cancelOrderBtn.setVisibility(View.GONE);
                holderView.goPayBtn.setVisibility(View.GONE);

//                holderView.checkLogistics.setVisibility(View.VISIBLE);
                holderView.confirmTakeGoods.setVisibility(View.VISIBLE);
                holderView.goComment.setVisibility(View.GONE);
                break;
            case 4:
                // 已收货（待评价）
                holderView.status.setVisibility(View.VISIBLE);
//                if (!orderInfo.isCommented()) {
//                    // 待评价
//                    holderView.status.setText(context.getResources().getString(R.string
//                } else {
//                    // 已经评价,显示已收货
//                    holderView.status.setText(context.getResources().getString(R.string
//                            .already_take_goods));
//                }
//                            .not_comment));

                // 交易成功
                holderView.status.setText(context.getResources().getString(R.string
                        .deal));

                holderView.deleteOrderBtn.setVisibility(View.GONE);
                holderView.cancelOrderBtn.setVisibility(View.GONE);
                holderView.goPayBtn.setVisibility(View.GONE);
//                holderView.checkLogistics.setVisibility(View.VISIBLE);
                holderView.confirmTakeGoods.setVisibility(View.GONE);
                if(orderInfo.isCommented()){
                    holderView.goComment.setVisibility(View.GONE);
                }else {
                    holderView.goComment.setVisibility(View.VISIBLE);
                }
                break;
            case 5:
                // 订单已取消
                holderView.status.setVisibility(View.VISIBLE);
                holderView.status.setText(context.getResources().getString(R.string.cancel_order2));
                holderView.deleteOrderBtn.setVisibility(View.VISIBLE);
                holderView.cancelOrderBtn.setVisibility(View.GONE);
                holderView.goPayBtn.setVisibility(View.GONE);
//                holderView.checkLogistics.setVisibility(View.GONE);
                holderView.confirmTakeGoods.setVisibility(View.GONE);
                holderView.goComment.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 取消订单 按钮点击事件
     */
    private View.OnClickListener cancelOrderClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (onCancelOrderLinstener != null) {
                onCancelOrderLinstener.onCancelOrder((int) view.getTag());
            }
        }
    };

    /**
     * 删除订单 按钮点击事件
     */
    private View.OnClickListener deleteOrderClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (onDeleteOrderLinstener != null) {
                onDeleteOrderLinstener.onDeleteOrder((int) view.getTag());
            }
        }
    };

    /**
     * 去付款 按钮点击事件
     */
    private View.OnClickListener goPayClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (onPayOrderLinstener != null) {
                onPayOrderLinstener.onPayOrder((Integer) view.getTag());
            }
        }
    };


    /**
     *  确认收货 按钮点击事件
     */
    private View.OnClickListener confirmTakeGoodsListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(onConfirmTakeGoodsListener != null){
                onConfirmTakeGoodsListener.onConfirm((Integer) view.getTag());
            }
        }
    };


    /**
     *  查看物流 按钮点击事件
     */
    private View.OnClickListener checkLogisticsListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // TODO 跳转查看物流界面
        }
    };

    /**
     *  去评价 按钮点击事件
     */
    private View.OnClickListener goCommentListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // 跳转评价界面
            if(onGoCommentListener != null){
                ArrayList<OrderListInfo> list = new ArrayList<>();
                int orderId = (int) view.getTag();
                for (OrderListInfo info :orderList) {
                    if(orderId == info.getId()){
                        list.add(info);
                    }
                }
                onGoCommentListener.onGoComment(list);
            }
        }
    };

    class HolderView {
        /**
         * 顶部显示店铺信息显示区域
         */
        LinearLayout topLay;
        /**
         * 店铺名称，订单状态
         */
        TextView storeName, status;
        /**
         * 第一条上下分割线,第二条上下分割线,第三条上下分割线
         */
        View splitLine1, splitLine2, splitLine3;
        SimpleDraweeView goodsIcon;
        TextView goodsName;
        TextView buyNum;
        TextView price;
        /**
         * 底部显示应付金额和按钮区域
         */
        LinearLayout bottomLay;
        /**
         * 应付金额
         */
        TextView payMoney;
        /**
         * 取消订单
         */
        TextView cancelOrderBtn;
        /**
         * 去付款
         */
        TextView goPayBtn;
        /**
         * 删除订单
         */
        TextView deleteOrderBtn;


//        /**
//         * 查看物流
//         */
//        TextView checkLogistics;
        /**
         * 确认收货
         */
        TextView confirmTakeGoods;
        /**
         * 去评价
         */
        TextView goComment;
    }


    /**
     * 取消订单 接口
     */
    public interface OnCancelOrderListener {
        void onCancelOrder(int position);
    }

    /**
     * 删除订单 接口
     */
    public interface OnDeleteOrderListener {
        void onDeleteOrder(int position);
    }

    /**
     * 去付款 接口
     */
    public interface OnPayOrderListener {
        void onPayOrder(int position);
    }

    /**
     * 确认收货 接口
     */
    public interface OnConfirmTakeGoodsListener{
        void onConfirm(int position);
    }

    /**
     * 去评论接口
     */
    public interface OnGoCommentListener{
        void onGoComment(ArrayList<OrderListInfo> orderList);
    }
}
