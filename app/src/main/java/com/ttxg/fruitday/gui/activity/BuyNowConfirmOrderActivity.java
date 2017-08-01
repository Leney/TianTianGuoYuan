package com.ttxg.fruitday.gui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.gui.adapter.ConfirmOrderAdapter;
import com.ttxg.fruitday.gui.view.WraperExpandableListView;
import com.ttxg.fruitday.manager.UserInfoManager;
import com.ttxg.fruitday.model.Address;
import com.ttxg.fruitday.model.GoodsDetail;
import com.ttxg.fruitday.model.OrderInfo;
import com.ttxg.fruitday.model.Shop;
import com.ttxg.fruitday.model.ShoppingCart;
import com.ttxg.fruitday.util.Arith;
import com.ttxg.fruitday.util.Constants;
import com.ttxg.fruitday.util.ParseUtil;
import com.ttxg.fruitday.util.Util;
import com.ttxg.fruitday.util.log.DLog;


/**
 * 单个商品立即购买，确认订单界面
 * Created by yb on 2016/9/28.
 */
public class BuyNowConfirmOrderActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 立即购买
     */
    private final String BUY_NOW = "order/buynow";

    /**
     * 提交订单(创建订单)
     */
    private final String CREATE_ORDER = "order/createOrder2";

    private WraperExpandableListView listView;

    private ConfirmOrderAdapter adapter;

    /**
     * 应付总金额
     */
    private TextView totalPrice;
    /**
     * 商品总数
     */
    private TextView goodsCount;
    /**
     * 提交订单按钮
     */
    private Button submitOrderBtn;

    /**
     * header中显示的默认收货地址
     */
    private TextView headerAddress;

    /**
     * 商品总额
     */
    private TextView footerGoodsTotalPrice;
    /**
     * 运费总额
     */
    private TextView footerPostToatalPrice;
    /**
     * 给卖家留言编辑框
     */
    private EditText footerLeaveMessageInput;

    private List<Shop> groupList;
    private List<List<ShoppingCart>> childList;
    /**
     * 商品总价格
     */
    private double totalGoodsPrice;
    /**
     * 邮费总价格
     */
    private double postPrice;
    /**
     * 商品总件数
     */
    private int goodsNum;

    /**
     * 当前的收货地址信息
     */
    private Address curAddress;

    /**
     * 请稍候dialog
     */
    private ProgressDialog progressDialog;

    /**
     * 从商品详情页传过来的参数
     */
    private Shop shop;
    private GoodsDetail goodsDetail;
//    private String buyCount = "1";
//    private int curSku1Position;
//    private int curSku2Position;
//    private double totalFreight;

    private Map<String, String> buyParams;

    @Override
    protected void initView() {

        EventBus.getDefault().register(this);

        groupList = new ArrayList<>();
        childList = new ArrayList<>();
        curAddress = new Address();
        buyParams = new HashMap<>();

        shop = (Shop) getIntent().getSerializableExtra("shop");
        goodsDetail = (GoodsDetail) getIntent().getSerializableExtra("goodsDetail");
        buyParams = (Map<String, String>) getIntent().getSerializableExtra("buyParams");
//        buyCount = getIntent().getStringExtra("buyCount");
//        if (TextUtils.isEmpty(buyCount)) {
//            buyCount = "1";
//        }
//        curSku1Position = getIntent().getIntExtra("curSku1Position", 0);
//        curSku2Position = getIntent().getIntExtra("curSku2Position", 0);
//        totalFreight = getIntent().getDoubleExtra("totalFreight", 0.0d);


        setTitleName(getResources().getString(R.string.confirm_order));
        setCenterView(R.layout.activity_confirm_order);
        listView = (WraperExpandableListView) findViewById(R.id.confirm_order_listiew);
        totalPrice = (TextView) findViewById(R.id.confirm_order_total_price);
        goodsCount = (TextView) findViewById(R.id.confirm_order_good_count);
        submitOrderBtn = (Button) findViewById(R.id.confirm_order_submit_btn);
        submitOrderBtn.setOnClickListener(this);

        View headerView = View.inflate(BuyNowConfirmOrderActivity.this, R.layout
                .confirm_order_header_view, null);
        headerAddress = (TextView) headerView.findViewById(R.id.confirm_order_header_address_info);
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 跳转到收货地址列表
                AddressListActivity.startActivityForResult(BuyNowConfirmOrderActivity.this, true,
                        1006);
            }
        });

        View footerView = View.inflate(BuyNowConfirmOrderActivity.this, R.layout
                .confirm_order_footer_view, null);
        footerGoodsTotalPrice = (TextView) footerView.findViewById(R.id
                .confirm_order_footer_goods_total_price);
        footerPostToatalPrice = (TextView) footerView.findViewById(R.id
                .confirm_order_footer_post_total_price);
        footerLeaveMessageInput = (EditText) footerView.findViewById(R.id
                .confirm_order_footer_leave_msg);

        listView.addHeaderView(headerView, null, false);
        listView.addFooterView(footerView, null, false);

        adapter = new ConfirmOrderAdapter(groupList, childList);
        listView.setAdapter(adapter);

//        getBuyNowParams();
        loadDataPost(BUY_NOW, buyParams);
    }

    @Override
    protected void onLoadDataSuccess(String tag, JSONObject resultObject) {
        super.onLoadDataSuccess(tag, resultObject);
        if (TextUtils.equals(tag, BUY_NOW)) {
            // 请求订单结算成功
            double[] resluts = ParseUtil.parseConfirmOrderResult(resultObject, groupList,
                    childList, curAddress);
            DLog.i("curAddress.getId()------>>" + curAddress.getId());
            if (resluts != null) {
                if (curAddress.getId() != 0) {
                    // 成功 有设置默认收货地址
                    postPrice = resluts[0];
                    totalGoodsPrice = resluts[1];
                    goodsNum = (int) resluts[2];
                    DLog.i("商品总价格---totalGoodsPrice-->>" + totalGoodsPrice);
                    DLog.i("邮费总价格---postPrice-->>" + postPrice);
                    setDefaultAddress();
                    setFooterInfo();
                    adapter.notifyDataSetChanged();
                    // 展开所有item
                    for (int i = 0; i < adapter.getGroupCount(); i++) {
                        listView.expandGroup(i);
                    }
                    showCenterView();
                } else {
                    // 成功 但沒有设置默认收货地址
                    showErrorView();
                    Util.showToast(BuyNowConfirmOrderActivity.this, getResources().getString(R
                            .string
                            .no_receive_address_default));
                    AddNewAddressActivity.startActivity(BuyNowConfirmOrderActivity.this);
                }
            } else {
                // 失败
                showErrorView();
            }
        } else if ((TextUtils.equals(tag, CREATE_ORDER))) {
            // 创建订单成功
            progressDialog.dismiss();
            OrderInfo orderInfo = ParseUtil.parseOrderInfo(resultObject);
            if (orderInfo == null) {
                DLog.i("lilijun", "创建订单失败，解析数据失败！！！");
                Util.showToast(BuyNowConfirmOrderActivity.this, getResources().getString(R.string
                        .create_order_failed));
            } else {
                DLog.i("lilijun", "创建订单成功！！！");
                // 直接将未付款数量加1就好了
                UserInfoManager.getInstance().getUserInfo().setNoPayOrderNum(UserInfoManager
                        .getInstance().getUserInfo().getNoPayOrderNum() + 1);
                EventBus.getDefault().post(Constants.SUBMIT_ORDER_SUCCESS);
                CreateOrderSuccessActivity.startActivity(BuyNowConfirmOrderActivity.this,
                        orderInfo);
                // 销毁确认订单的界面
                finish();
            }
        }
    }

    @Override
    protected void onLoadDataError(String tag, int errorCode, String msg) {
        super.onLoadDataError(tag, errorCode, msg);
        if (TextUtils.equals(tag, BUY_NOW)) {
            // 请求订单结算失败
//            showErrorView();
            Util.showErrorMessage(BuyNowConfirmOrderActivity.this, msg, getResources().getString(R
                    .string.get_confirm_order_failed));
            finish();
        } else if ((TextUtils.equals(tag, CREATE_ORDER))) {
            // 创建订单失败
            DLog.i("lilijun", "创建订单失败！！！");
            progressDialog.dismiss();
            Util.showErrorMessage(BuyNowConfirmOrderActivity.this, msg, getResources().getString(R
                    .string.create_order_failed));
        }
    }

    @Override
    protected void tryAgain() {
        super.tryAgain();
        groupList.clear();
        childList.clear();
        curAddress = null;
        curAddress = new Address();
        loadDataPost(BUY_NOW, buyParams);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEventMessage(Address address) {
        curAddress = address;
        setDefaultAddress();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == AddressListActivity.CHOICE_ADDRESS_ITEM_RESULT_CODE) {
//            curAddress = (Address) data.getSerializableExtra("address");
//            setDefaultAddress();
            // 再次去获取一次结算数据
            tryAgain();
        }
    }

    /**
     * 设置默认地址信息
     */
    private void setDefaultAddress() {
        StringBuilder builder = new StringBuilder();
        builder.append(curAddress.getName());
        builder.append(",");
        builder.append(curAddress.getMobile());
        builder.append("\n");
        builder.append(curAddress.getProvince());
        builder.append(curAddress.getCity());
        builder.append(curAddress.getTown());
        builder.append(curAddress.getDetailAddress());
        headerAddress.setText(builder.toString());
    }

    /**
     * 设置商品价格和邮费价格
     */
    private void setFooterInfo() {
        footerGoodsTotalPrice.setText(String.format(getResources().getString(R.string
                .format_money), totalGoodsPrice + ""));
        footerPostToatalPrice.setText(String.format(getResources().getString(R.string
                .format_money), postPrice + ""));
        totalPrice.setText(String.format(getResources().getString(R.string
                .format_money), Arith.add(totalGoodsPrice, postPrice) + ""));
        goodsCount.setText(String.format(getResources().getString(R.string.format_count),
                goodsNum + ""));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.confirm_order_submit_btn:
                // 提交订单
                submitOrder();
                break;
        }
    }

    /**
     * 提交订单
     */
    private void submitOrder() {
        progressDialog = Util.showLoadingDialog(BuyNowConfirmOrderActivity.this, progressDialog);
        Map<String, String> param = new HashMap<>();
        param.put("receiveName", curAddress.getName());
        param.put("receiveProvince", curAddress.getProvince());
        param.put("receiveCity", curAddress.getCity());
        param.put("receiveArea", curAddress.getTown());
        param.put("receiveAddress", curAddress.getDetailAddress());
        param.put("receiveMobile", curAddress.getMobile());
        param.put("buyerMessage", footerLeaveMessageInput.getText().toString().trim());
        if (postPrice <= 0.0d) {
            // 包邮
            param.put("isfreeShipping", true + "");
        } else {
            // 不包邮
            param.put("isfreeShipping", false + "");
        }
        param.put("postage", postPrice + "");
        param.put("pay_style", "1");
        param.put("takeType", "2");
        param.put("totalPrice", totalGoodsPrice + "");
        param.put("reductionPrice", "0");

        loadDataPost(CREATE_ORDER, param);
    }


//    /**
//     * 获取加入购物车所需数据参数
//     *
//     * @return
//     */
//    private void getBuyNowParams() {
//        buyParams.put("store_user_id", shop.getUserId() + "");
//        buyParams.put("store_name", shop.getName());
//        buyParams.put("store_id", shop.getId() + "");
//        buyParams.put("userType", shop.getStoreType() + "");
//        buyParams.put("goods_id", goodsDetail.getId() + "");
//        DLog.i("goodsDetail.getSendType()----->>" + goodsDetail.getSendType());
//        buyParams.put("goodsType", Util.getStringByObject(goodsDetail.getSendType()));
//        buyParams.put("goodsName", goodsDetail.getName());
//        buyParams.put("fh_user_id", goodsDetail.getFhUserId() + "");
//        buyParams.put("group_buy_price", goodsDetail.getPrice() + "");
//        buyParams.put("shop_market_price", goodsDetail.getMarketPrice() + "");
//        buyParams.put("ex_factory_price", goodsDetail.getFactoryPrice() + "");
//        buyParams.put("sales_num", goodsDetail.getSalesNums() + "");
//        buyParams.put("buyCount", buyCount + "");
//        buyParams.put("quantity", goodsDetail.getStock() + "");
//        if (goodsDetail.getImgUrls() != null && !goodsDetail.getImgUrls().isEmpty()) {
//            buyParams.put("pic_server_url1", goodsDetail.getImgUrls().get(0));
//        } else {
//            buyParams.put("pic_server_url1", "");
//        }
//
//
//        buyParams.put("isfreeShipping", goodsDetail.isTakeFreight() + "");
//        buyParams.put("postage", totalFreight + "");
//        buyParams.put("singlePostage", goodsDetail.getFreight() + "");
//
//        if (goodsDetail.getSku1Values() != null && curSku1Position < goodsDetail
//                .getSku1Values().length) {
//            // 类型1有数据
//            // 获SkuInfo的key值
//            String key = goodsDetail.getSku1Values()[curSku1Position];
//            if (goodsDetail.getSku2Values() != null && curSku2Position <
//                    goodsDetail.getSku2Values().length) {
//                // 获取类型2的当前选中的position
//                key += goodsDetail.getSku2Values()[curSku2Position];
//            }
//            // 获取到SkuInfo
//            SkuInfo skuInfo = goodsDetail.getSkuTotalMap().get(key);
//            if (skuInfo != null) {
//                buyParams.put("sku_id", skuInfo.getId() + "");
//                buyParams.put("sku1Name", skuInfo.getSkuName1());
//                buyParams.put("sku1Value", skuInfo.getSkuValue1());
//                buyParams.put("sku2Name", skuInfo.getSkuName2());
//                buyParams.put("sku2Value", skuInfo.getSkuValue2());
//            }
//        } else {
//            // 没有类型数据，没有多个款式
//            buyParams.put("sku_id", goodsDetail.getDefaultSkuInfo().getId() + "");
//            buyParams.put("sku1Name", "");
//            buyParams.put("sku1Value", "");
//            buyParams.put("sku2Name", "");
//            buyParams.put("sku2Value", "");
//        }
////        if (goodsDetail.getSkuInfoList().size() <= 1) {
////            // 没有多个款式
////            buyParams.put("sku_id", goodsDetail.getDefaultSkuId() + "");
////            buyParams.put("sku1Name", "");
////            buyParams.put("sku1Value", "");
////            buyParams.put("sku2Name", "");
////            buyParams.put("sku2Value", "");
////        } else {
////            SkuInfo skuInfo = goodsDetail.getSkuInfoList().get(curSku1Position);
////            buyParams.put("sku_id", skuInfo.getId() + "");
////            buyParams.put("sku1Name", skuInfo.getSkuName1());
////            buyParams.put("sku1Value", skuInfo.getSkuValue1());
////            buyParams.put("sku2Name", skuInfo.getSkuName2());
////            buyParams.put("sku2Value", skuInfo.getSkuValue2());
////        }
//    }

//    /**
//     * 跳转到本Activity
//     */
//    public static void startActivity(Context context, Shop shop, GoodsDetail goodsDetail, String
//            buyCount, int curSku1Position, int curSku2Position, double totalFreight) {
//        if (UserInfoManager.getInstance().isValidUserInfo()) {
//            // 有登陆信息,跳转到立即购买确认订单界面
//            Intent intent = new Intent(context, BuyNowConfirmOrderActivity.class);
//            intent.putExtra("shop", shop);
//            intent.putExtra("goodsDetail", goodsDetail);
//            intent.putExtra("buyCount", buyCount);
//            intent.putExtra("curSku1Position", curSku1Position);
//            intent.putExtra("curSku2Position", curSku2Position);
//            intent.putExtra("totalFreight", totalFreight);
//            context.startActivity(intent);
//        } else {
//            // 没有登陆，跳转到登陆界面
//            LoginActivity.startActivity(context);
//        }
//    }

    /**
     * 跳转到本Activity
     */
    public static void startActivity(Context context, Shop shop, GoodsDetail goodsDetail,
                                     HashMap<String, String> buyParams) {
        if (UserInfoManager.getInstance().isValidUserInfo()) {
            // 有登陆信息,跳转到立即购买确认订单界面
            Intent intent = new Intent(context, BuyNowConfirmOrderActivity.class);
            intent.putExtra("shop", shop);
            intent.putExtra("goodsDetail", goodsDetail);
            intent.putExtra("buyParams", buyParams);
            context.startActivity(intent);
        } else {
            // 没有登陆，跳转到登陆界面
            LoginActivity.startActivity(context);
        }
    }
}
