package com.ttxg.fruitday.gui.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.callback.OnChangeShoppingCartBuyNumLinstener;
import com.ttxg.fruitday.callback.OnDeleteShoppingCartGroupLinstener;
import com.ttxg.fruitday.callback.OnDeleteShoppingCartItemLinstener;
import com.ttxg.fruitday.gui.activity.AddNewAddressActivity;
import com.ttxg.fruitday.gui.activity.ConfirmOrderActivity;
import com.ttxg.fruitday.gui.activity.LoginActivity;
import com.ttxg.fruitday.gui.activity.MainActivity;
import com.ttxg.fruitday.gui.adapter.ShoppingCartAdapter;
import com.ttxg.fruitday.gui.view.BaseTitleView;
import com.ttxg.fruitday.gui.view.WraperExpandableListView;
import com.ttxg.fruitday.manager.UserInfoManager;
import com.ttxg.fruitday.model.Address;
import com.ttxg.fruitday.model.Shop;
import com.ttxg.fruitday.model.ShoppingCart;
import com.ttxg.fruitday.util.Arith;
import com.ttxg.fruitday.util.Constants;
import com.ttxg.fruitday.util.ParseUtil;
import com.ttxg.fruitday.util.Util;

/**
 * 购物车Tab Fragment
 * Created by lilijun on 2016/8/12.
 */
public class TabShoppingCartFragment extends BaseFragment implements View.OnClickListener,
        OnDeleteShoppingCartGroupLinstener, OnDeleteShoppingCartItemLinstener,
        OnChangeShoppingCartBuyNumLinstener {
    private final String GET_SHOPPING_CART_LIST = "cart/index";
    /**
     * 购买商品数量加1
     */
    private final String ADD_BUY_NUM = "cart/increaseCartGoods";
    /**
     * 购买商品数量减1
     */
    private final String REDUCE_BUY_NUM = "cart/minusCartGoods";
    /**
     * 删除单个商品
     */
    private final String DELETE_SINGLE_GOODS = "cart/deleteCartGoods";
    /**
     * 删除单个商品
     */
    private final String DELETE_STORE_ALL_GOODS = "cart/deleteStoreCartGoods";
    /**
     * 查询收货地址列表
     */
    private final String QUREY_ADDRESS = "address/queryAddress";

    private ArrayList<Shop> groupList;
    private ArrayList<ArrayList<ShoppingCart>> childList;
    private WraperExpandableListView listView;
    private ShoppingCartAdapter adapter;
    private TextView totalPrice;
    private Button goPayBtn;

    /**
     * 商品总价格
     */
    private double allPrice;
//    /**
//     * 邮费总价格
//     */
//    private double postPrice;
//    /**
//     * 商品件数
//     */
//    private int goodsCount;
    /**
     * 请稍后dialog
     */
    private ProgressDialog progressDialog;

    /**
     * 当购买数量发生改变、删除商品时，商品的所在列表中的位置
     */
    private int changeGroupPosition, changeChildPosition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initView(RelativeLayout view) {
        groupList = new ArrayList<>();
        childList = new ArrayList<>();

        BaseTitleView titleView = new BaseTitleView(getActivity());
        titleView.setBackImgVisible(false);
        RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(RelativeLayout
                .LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen
                .title_height));
        titleView.setTitleName(getResources().getString(R.string.shopping_cart));
        setAddView(titleView, titleParams);

        setCenterView(R.layout.fragment_shopping_cart);
        listView = (WraperExpandableListView) view.findViewById(R.id
                .shopping_cart_expandable_listview);
        listView.addHeaderView(View.inflate(getActivity(), R.layout.shopping_header_lay, null));
        totalPrice = (TextView) view.findViewById(R.id.shopping_cart_total_price);
        goPayBtn = (Button) view.findViewById(R.id.shopping_cart_go_pay_btn);
        goPayBtn.setOnClickListener(this);


        View view1 = View.inflate(getActivity(), R.layout.shoppingcart_empty_lay, null);
        Button goShoppingBtn = (Button) view1.findViewById(R.id
                .shopping_cart_empty_go_shopping_btn);
        goShoppingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).changeTab(0);
            }
        });
        addNoDataView(view1);

        adapter = new ShoppingCartAdapter(groupList, childList);
        adapter.setOnChangedBuyNumLinstener(this);
        adapter.setOnDeleteChildItemLinstener(this);
        adapter.setOnDeleteGroupLinstener(this);
        listView.setAdapter(adapter);
        loadDataGet(GET_SHOPPING_CART_LIST, null);
    }

    @Override
    protected void onLoadDataSuccess(String tag, JSONObject resultObject) {
        super.onLoadDataSuccess(tag, resultObject);
        if (TextUtils.equals(tag, GET_SHOPPING_CART_LIST)) {
            // 获取购物车详情列表
            groupList.clear();
            childList.clear();
            allPrice = ParseUtil.parseShoppingCartResult(resultObject, groupList, childList);
//            if (result != null) {
//                allPrice = result[0];
//                postPrice = result[1];
//                goodsCount = (int) result[2];
//            }
            if (groupList.isEmpty() || childList.isEmpty()) {
                // 购物车没有数据
                showNoDataView();
                totalPrice.setText(String.format(getResources().getString(R.string.format_money2)
                        , "0.00"));
            } else {
                adapter.notifyDataSetChanged();
                // 展开所有item
                for (int i = 0; i < adapter.getGroupCount(); i++) {
                    listView.expandGroup(i);
                }
                totalPrice.setText(String.format(getResources().getString(R.string.format_money2)
                        , allPrice + ""));
                showCenterView();
            }
        } else if (TextUtils.equals(tag, ADD_BUY_NUM)) {
            // 某个单个商品购买数量加1
            ShoppingCart cart = childList.get(changeGroupPosition).get(changeChildPosition);
            cart.setBuyNum(cart.getBuyNum() + 1);
            adapter.notifyDataSetChanged();
//            allPrice += cart.getBuyPrice();
            allPrice = Arith.add(allPrice, cart.getBuyPrice());
//            if (!cart.isFreeShipping()) {
//                // 如果不包邮
//                postPrice = Arith.add(postPrice, cart.getSinglePostage());
//            }
//            goodsCount++;
            totalPrice.setText(String.format(getResources().getString(R.string.format_money2)
                    , allPrice + ""));
            progressDialog.dismiss();
        } else if (TextUtils.equals(tag, REDUCE_BUY_NUM)) {
            // 某个单个商品购买数量减1
            ShoppingCart cart = childList.get(changeGroupPosition).get(changeChildPosition);
            cart.setBuyNum(cart.getBuyNum() - 1);
            adapter.notifyDataSetChanged();
//            allPrice -= cart.getBuyPrice();
            allPrice = Arith.sub(allPrice, cart.getBuyPrice());
//            if (!cart.isFreeShipping()) {
//                // 如果不包邮
//                postPrice = Arith.sub(postPrice, cart.getSinglePostage());
//            }
//            goodsCount--;
            if (allPrice < 0) {
                allPrice = 0.0d;
            }
//            if (postPrice < 0) {
//                postPrice = 0.0d;
//            }
//            if (goodsCount < 0) {
//                goodsCount = 0;
//            }

            totalPrice.setText(String.format(getResources().getString(R.string.format_money2)
                    , allPrice + ""));
            progressDialog.dismiss();
        } else if (TextUtils.equals(tag, DELETE_SINGLE_GOODS)) {
            // 删除单个商品
            ShoppingCart cart = childList.get(changeGroupPosition).get(changeChildPosition);
            // 减去合计中的价格
//            allPrice -= cart.getBuyNum() * cart.getBuyPrice();
            allPrice = Arith.sub(allPrice, Arith.mul(cart.getBuyNum(), cart.getBuyPrice()));
//            if (!cart.isFreeShipping()) {
//                // 如果不包邮
//                postPrice = Arith.sub(postPrice, Arith.mul(cart.getBuyNum(), cart
//                        .getSinglePostage()));
//            }
//            goodsCount--;
            if (allPrice < 0) {
                allPrice = 0.0d;
            }
//            if (postPrice < 0) {
//                postPrice = 0.0d;
//            }
//            if (goodsCount < 0) {
//                goodsCount = 0;
//            }

            if (childList.get(changeGroupPosition).size() <= 1) {
                // 是这个组下面最后一个数据了，则需要将整组的数据也删除掉
                groupList.remove(changeGroupPosition);
                childList.remove(changeGroupPosition);
            } else {
                childList.get(changeGroupPosition).remove(changeChildPosition);
            }
            if (groupList.isEmpty() || childList.isEmpty()) {
                // 购物车没有数据了
                showNoDataView();
                allPrice = 0.00d;
            } else {
                adapter.notifyDataSetChanged();
            }
            totalPrice.setText(String.format(getResources().getString(R.string.format_money2)
                    , allPrice + ""));
            progressDialog.dismiss();
        } else if (TextUtils.equals(tag, DELETE_STORE_ALL_GOODS)) {
            // 删除店铺以及店铺下的商品
            for (ShoppingCart cart : childList.get(changeGroupPosition)) {
                // 去除合计中的价格
//                allPrice -= cart.getBuyNum() * cart.getBuyPrice();
                allPrice = Arith.sub(allPrice, Arith.mul(cart.getBuyNum(), cart.getBuyPrice()));
//                if (!cart.isFreeShipping()) {
//                    // 如果不包邮
//                    postPrice = Arith.sub(postPrice, Arith.mul(cart.getBuyNum(), cart
//                            .getSinglePostage()));
//                }
//                goodsCount -= cart.getBuyNum();
            }
            if (allPrice < 0) {
                allPrice = 0.0d;
            }
//            if (postPrice < 0) {
//                postPrice = 0.0d;
//            }
//            if (goodsCount < 0) {
//                goodsCount = 0;
//            }
            groupList.remove(changeGroupPosition);
            childList.remove(changeGroupPosition);
            if (groupList.isEmpty() || childList.isEmpty()) {
                // 购物车没有数据了
                showNoDataView();
                allPrice = 0.00d;
            } else {
                adapter.notifyDataSetChanged();
            }
            totalPrice.setText(String.format(getResources().getString(R.string.format_money2)
                    , allPrice + ""));
            progressDialog.dismiss();
        } else if (TextUtils.equals(tag, QUREY_ADDRESS)) {
            // 查询用户是否有收货地址成功
            List<Address> emptyList = ParseUtil.parseAddressListResult(resultObject);
            progressDialog.dismiss();
            if (emptyList != null && !emptyList.isEmpty()) {
                // 用户有默认收货地址，跳转到订单确认界面
                ConfirmOrderActivity.startActivity(getActivity());
            } else {
                // 用户没有默认收货地址,跳转到新添加收货地址界面
                Util.showToast(getActivity(), getResources().getString(R.string
                        .no_receive_address_default));
                AddNewAddressActivity.startActivity(getActivity());
            }
        }
    }

    @Override
    protected void onLoadDataError(String tag, int errorCode, String msg) {
        super.onLoadDataError(tag, errorCode, msg);
        if (TextUtils.equals(tag, GET_SHOPPING_CART_LIST)) {
            // 获取购物车详情列表
            showErrorView();
            Util.showErrorMessage(getActivity(), msg, getResources().getString(R.string
                    .get_shopping_cart_goods_failed));
        } else if (TextUtils.equals(tag, ADD_BUY_NUM) || TextUtils.equals(tag, REDUCE_BUY_NUM)) {
            // 某个单个商品购买数量加1 或者 减1
            progressDialog.dismiss();
            Util.showErrorMessage(getActivity(), msg, getResources().getString(R.string
                    .change_buy_num_failed));
        } else if (TextUtils.equals(tag, DELETE_SINGLE_GOODS) || TextUtils.equals(tag,
                DELETE_STORE_ALL_GOODS)) {
            // 删除单个商品 或者 删除店铺及店铺下所有商品
            progressDialog.dismiss();
            Util.showErrorMessage(getActivity(), msg, getResources().getString(R.string
                    .delete_goods_failed));
        } else if (TextUtils.equals(tag, QUREY_ADDRESS)) {
            // 查询用户是否有收货地址失败
            progressDialog.dismiss();
            Util.showErrorMessage(getActivity(), msg, getResources().getString(R.string
                    .get_user_info_failed));
        }
        if (errorCode == Constants.LOGIN_TIME_OUT) {
            LoginActivity.startActivity(getActivity());
        }
    }

    @Override
    protected void tryAgain() {
        super.tryAgain();
        loadDataGet(GET_SHOPPING_CART_LIST, null);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    /**
     * 接收到添加了新的商品到购物车的消息，去获取购物车列表数据
     *
     * @param msg
     */
    @Subscribe
    public void onMessageEvent(String msg) {
        if (TextUtils.equals(msg, Constants.ADD_SHOPPING_CART_SUCCESS) || TextUtils.equals(msg,
                Constants.SUBMIT_ORDER_SUCCESS)) {
            // 再次去获取购物车数据
            loadDataGet(GET_SHOPPING_CART_LIST, null);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.shopping_cart_go_pay_btn:
                // 去结算  按钮
                if (UserInfoManager.getInstance().isValidUserInfo()) {
                    // 用户有登录
                    if (UserInfoManager.getInstance().getUserInfo().getDefaultReceiveAddress() ==
                            null) {
                        // 用户没有收货地址, 去查询是否有默认收货地址
                        progressDialog = Util.showLoadingDialog(getActivity(), progressDialog);
                        loadDataGet(QUREY_ADDRESS, null);
                    } else {
                        // 用户有收货地址,跳转到订单确认界面
                        ConfirmOrderActivity.startActivity(getActivity());
                    }
                } else {
                    // 用户未登录，用户需要登录
                    LoginActivity.startActivity(getActivity());
                }
                break;
        }
    }

    @Override
    public void onBuyNumChange(boolean isAdd, int groupPosition, int childPosition) {
//        if(isAdd){
//            // 购买数量加1
//            allPrice += price;
//        }else {
//            // 购买数量减1
//            allPrice -= price;
//        }
//        totalPrice.setText(String.format(getResources().getString(R.string.format_money2),
// allPrice + ""));
        ShoppingCart cart = childList.get(groupPosition).get(childPosition);
        if (cart == null) {
            Util.showToast(getActivity(), getResources().getString(R.string.change_buy_num_failed));
            return;
        }

        changeGroupPosition = groupPosition;
        changeChildPosition = childPosition;

        Map<String, String> params = new HashMap<>();
        params.put("store_user_id", cart.getStoreUserId() + "");
        params.put("store_id", cart.getStoreId() + "");
        params.put("goods_id", cart.getGoodsId() + "");
        params.put("sku_id", cart.getSkuId() + "");
        if (isAdd) {
            if (cart.getBuyNum() >= cart.getQuantityNum()) {
                // 已经达到库存值了
                return;
            }
            loadDataPost(ADD_BUY_NUM, params);
        } else {
            if (cart.getBuyNum() <= 1) {
                // 至少得买一件商品
                return;
            }
            loadDataPost(REDUCE_BUY_NUM, params);
        }
        progressDialog = Util.showLoadingDialog(getActivity(), progressDialog);
    }

    /**
     * 删除了整个店铺里面的所有商品，调用接口
     *
     * @param position 组的position
     */
    @Override
    public void onDeleteGroup(final int position) {

        changeGroupPosition = position;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getResources().getString(R.string.confirm_delete_group));
        builder.setTitle(getResources().getString(R.string.warm_prompt));
        builder.setPositiveButton(getResources().getString(R.string.sure), new DialogInterface
                .OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 确定点击事件
                Shop shop = groupList.get(changeGroupPosition);
                if (shop == null) {
                    Util.showToast(getActivity(), getResources().getString(R.string
                            .delete_goods_failed));
                    return;
                }
                Map<String, String> params = new HashMap<>();
                params.put("store_id", shop.getId() + "");
                loadDataPost(DELETE_STORE_ALL_GOODS, params);
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

    /**
     * 删除了单个商品，调用接口
     *
     * @param groupPosition 组position
     * @param childPosition 组下子item的position
     */
    @Override
    public void onDeleteItem(int groupPosition, int childPosition) {
        changeGroupPosition = groupPosition;
        changeChildPosition = childPosition;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getResources().getString(R.string.confirm_delete_goods));
        builder.setTitle(getResources().getString(R.string.warm_prompt));
        builder.setPositiveButton(getResources().getString(R.string.sure), new DialogInterface
                .OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 确定点击事件
                ShoppingCart cart = childList.get(changeGroupPosition).get(changeChildPosition);
                if (cart == null) {
                    Util.showToast(getActivity(), getResources().getString(R.string
                            .delete_goods_failed));
                    dialog.dismiss();
                    return;
                }
                Map<String, String> params = new HashMap<>();
                params.put("store_user_id", cart.getStoreUserId() + "");
                params.put("store_id", cart.getStoreId() + "");
                params.put("goods_id", cart.getGoodsId() + "");
                params.put("sku_id", cart.getSkuId() + "");
                loadDataPost(DELETE_SINGLE_GOODS, params);
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
}
