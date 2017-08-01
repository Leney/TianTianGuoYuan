package com.ttxg.fruitday.gui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.gui.adapter.DetailBannerAdapter;
import com.ttxg.fruitday.gui.fragment.CommentFragment;
import com.ttxg.fruitday.gui.view.CenterLineTextView;
import com.ttxg.fruitday.gui.view.RollPagerViewLab;
import com.ttxg.fruitday.gui.view.SelfAdaptionViewGroup;
import com.ttxg.fruitday.gui.view.WraperScrollView;
import com.ttxg.fruitday.manager.UserInfoManager;
import com.ttxg.fruitday.model.Address;
import com.ttxg.fruitday.model.Comment;
import com.ttxg.fruitday.model.GoodsDetail;
import com.ttxg.fruitday.model.Shop;
import com.ttxg.fruitday.model.SkuInfo;
import com.ttxg.fruitday.util.Arith;
import com.ttxg.fruitday.util.Constants;
import com.ttxg.fruitday.util.ParseUtil;
import com.ttxg.fruitday.util.Util;
import com.ttxg.fruitday.util.log.DLog;

/**
 * 商品详情界面
 * Created by yb on 2016/8/23.
 */
public class GoodsDetailActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 获取商品详情信息tag
     */
    private String detailTag = "goods/detail/";
    /**
     * 添加到购物车tag
     */
    private static final String ADD_TO_SHOPPING_CART = "cart/addGoodsToCart";
//    /**
//     * 加入小店接口
//     */
//    private final String ADD_TO_STORE = "goods/distribute";
    /**
     * 查询收货地址列表
     */
    private final String QUREY_ADDRESS = "address/queryAddress";
    /**
     * 查询用户信息
     */
    private final String GET_USER_INFO = "user/getUserInfo";
    /**
     * 获取到的跳转商品的id
     */
    private int intentGoodsId;
    /**
     * 获取到的跳转storeId
     */
    private int intentStoreId;

    /**
     * 商品详情信息
     */
    private GoodsDetail goodsDetail;
    /**
     * 店铺信息
     */
    private Shop shop;

    private RollPagerViewLab pagerView;
    private DetailBannerAdapter bannerAdapter;
//    private ImageView vipLab;

    /**
     * 根ScrollView
     */
    private WraperScrollView baseScrollView;
//    /**
//     * 商品名称，出售价格，原价，销售数量，运费，购买数量，库存数量，商品店铺名称，商品店铺类型
//     */
//    private TextView goodsName, priceLeft, marketPriceLeft, salesNum, postage, buyNum, stockNum,
//            shopName, shopType;
    /**
     * 商品名称，销售数量，运费，购买数量，库存数量
     */
    private TextView goodsName, salesNum, postage, buyNum, stockNum;

    private TextView priceLeft, priceRight;
    private CenterLineTextView marketPriceLeft, marketPriceRight;
    /**
     * 商铺信息部分
     */
    private RelativeLayout shopInfoLay;
    /**
     * 减数量，加数量
     */
    private ImageView reduceBuyNum, addBuyNum;
//    /**
//     * 店铺logo
//     */
//    private SimpleDraweeView shopLogo;
    /**
     * 商品介绍和商品评价tab
     */
    private RadioGroup tabGroup;
    /**
     * 商品介绍tab，商品评价tab
     */
    private RadioButton introduceTab, commendsTab;
//    /**
//     * 显示商品介绍和商品评价的viewpager
//     */
//    private ViewPager tabViewPager;
//    /**
//     * viewpager adapter
//     */
//    private ViewPagerAdapter pagerAdapter;
    /**
     * ViewPager显示是视图集合
     */
    private List<View> viewList;
    /**
     * 显示详情webview
     */
    private WebView introduceWebview;
    /**
     * 暂无商品详情,暂无商品评价信息
     */
//    private TextView introduceNoAny, commendNoAny;
    private TextView introduceNoAny;
    /**
     * 好评、中评、差评的RadioGroup
     */
    private RadioGroup commendsGroup;
//    /**
//     * 评论列表的listView
//     */
//    private ListView commendListView;
//    /**
//     * 评论列表适配器
//     */
//    private CommentAdapter commentAdapter;
//    /**
//     * 显示在列表中的评论数据源
//     */
//    private List<Comment> commentList;
//    /**
//     * 好评数据集合、中评数据集合、差评数据集合
//     */
//    private List<Comment> goodCommentList, normalCommentList, lowCommentList;

//    /**
//     * 当前viewpager的高度
//     */
//    private int curViewpagerHeight = 0;
//    /**
//     * viewPager的LayoutParams对象
//     */
//    private ViewGroup.LayoutParams viewPagerParams = null;

//    /**
//     * 评论fragment放置区域
//     */
//    private FrameLayout commentFragmentLay;


    /**
     * 好评、中评、差评 fragment数组
     */
    private CommentFragment[] commentFragments;
//    /** 中评fragment*/
//    private CommentFragment lowCommentFragment;
//    /** 差评fragment*/
//    private CommentFragment normalCommentFragment;

    private FragmentManager fragmentManager;

    /**
     * 加入购物车按钮，立即购买按钮，加入小店按钮
     */
    private Button addShoppingCartBtn, buyNowBtn, addStoreBtn;

    /**
     * 总邮费
     */
    private double totalFreight = 0d;

//    /**
//     * 当前评论显示的position
//     */
//    private int curCommentPosition = 0;

    private static final int RESUME_SCROLL_VIEW = 1;

    private Map<String, Object> params;

    /**
     * 请稍后dialog
     */
    private ProgressDialog progressDialog;

    /**
     * 类型1如果有多种款式时用户选中的款式position
     */
    private int curSku1Position = 0;
    /**
     * 类型2如果有多种款式时用户选中的款式position
     */
    private int curSku2Position = 0;
    /**
     * 类型1分类的类型名称
     */
    private TextView sku1TypeName;

    /**
     * 类型1分类子Values显示部分
     */
    private SelfAdaptionViewGroup sku1ValuesLay;

    /**
     * 类型2分类的类型名称
     */
    private TextView sku2TypeName;
    /**
     * 类型2分类子Values显示部分
     */
    private SelfAdaptionViewGroup sku2ValuesLay;

    /**
     * sku分割线
     */
    private View skuSplitLine;

    /**
     * sku类型中间分割线
     */
    private View skuMidSplitLine;

    /**
     * 标示是否是 “立即购买”
     */
    private boolean isBuyNow = false;

//    /**
//     * 标志是否添加过到购物车
//     */
//    private boolean isAddedToShoppingCart = false;

    /**
     * 标识当前显示的评论fragment
     * 0=好评，1=中评，2=差评
     */
    private int curCommentPosition = 0;

    /**
     * 各个tab切换之前ScrollView的滑动距离
     * [0]=商品介绍，[1]=商品评价，[2]=好评，[3]=中评，[4]=差评
     */
//    private int[] scrollYs = new int[5];
    private int curScrollY = 0;

    /**
     * 加入购物车时的参数
     */
    private HashMap<String, String> addCartParams = new HashMap<>();
    /**
     * 立即购买时的参数
     */
    private HashMap<String, String> buyNowParams = new HashMap<>();

    private final MyHandler handler = new MyHandler(this);

    /**
     * 加入购物车的handler消息
     */
    private static final int ADD_TO_SHOPPING_CART_MSG = 1;
    /**
     * 立即购买的handler消息
     */
    private static final int BUY_NOW_MSG = ADD_TO_SHOPPING_CART_MSG + 1;

    /**
     * 标识是否是已经显示过视图，只是重新设置数据
     */
    private boolean isResetData = false;

    @Override
    protected void initView() {

        // 注册EventBus
        EventBus.getDefault().register(this);

        setTitleName(getResources().getString(R.string.goods_details));
        intentGoodsId = getIntent().getIntExtra("goodsId", -1);
        intentStoreId = getIntent().getIntExtra("storeId", -1);


        goodsDetail = new GoodsDetail();
        shop = new Shop();
        params = new HashMap<>();
        setCenterView(R.layout.activity_goods_detail);

//        commentList = new ArrayList<>();
//        goodCommentList = new ArrayList<>();
//        normalCommentList = new ArrayList<>();
//        lowCommentList = new ArrayList<>();
        commentFragments = new CommentFragment[3];

        baseScrollView = (WraperScrollView) findViewById(R.id.goods_detail_scrollview);
        pagerView = (RollPagerViewLab) findViewById(R.id.goods_detail_banner);
//        vipLab = (ImageView) findViewById(R.id.goods_detail_vip_lab);
        goodsName = (TextView) findViewById(R.id.goods_detail_name);
        priceLeft = (TextView) findViewById(R.id.goods_detail_price_left);
        priceRight = (TextView) findViewById(R.id.goods_detail_price_right);
        marketPriceLeft = (CenterLineTextView) findViewById(R.id.goods_detail_market_price_left);
        marketPriceRight = (CenterLineTextView) findViewById(R.id.goods_detail_market_price_right);
        salesNum = (TextView) findViewById(R.id.goods_detail_sales_nums);
        postage = (TextView) findViewById(R.id.goods_detail_postage);
        reduceBuyNum = (ImageView) findViewById(R.id.goods_detail_reduce_btn);
        reduceBuyNum.setOnClickListener(this);
        addBuyNum = (ImageView) findViewById(R.id.goods_detail_add_btn);
        addBuyNum.setOnClickListener(this);
        buyNum = (TextView) findViewById(R.id.goods_detail_buy_nums);
        stockNum = (TextView) findViewById(R.id.goods_detail_stock);
//        shopName = (TextView) findViewById(R.id.goods_detail_shop_name);
//        shopType = (TextView) findViewById(R.id.goods_detail_shop_type);
//        shopInfoLay = (RelativeLayout) findViewById(R.id.goods_detail_shop_info_lay);
//        shopInfoLay.setOnClickListener(this);
//        shopLogo = (SimpleDraweeView) findViewById(R.id.goods_detail_shop_logo);
        tabGroup = (RadioGroup) findViewById(R.id.goods_detail_tab_group);
        tabGroup.setOnCheckedChangeListener(detailsChangeListener);
        introduceTab = (RadioButton) findViewById(R.id.goods_detail_introduce_tab);
        commendsTab = (RadioButton) findViewById(R.id.goods_detail_commend_tab);

        addShoppingCartBtn = (Button) findViewById(R.id.goods_detail_add_shopping_cart_btn);
        addShoppingCartBtn.setOnClickListener(this);
        buyNowBtn = (Button) findViewById(R.id.goods_detail_buy_now_btn);
        buyNowBtn.setOnClickListener(this);
        addStoreBtn = (Button) findViewById(R.id.goods_detail_become_vip_btn);
        addStoreBtn.setOnClickListener(this);

        sku1TypeName = (TextView) findViewById(R.id.goods_detail_sku1_type_name);
        skuSplitLine = findViewById(R.id.goods_detail_sku_split_line);
        skuMidSplitLine = findViewById(R.id.goods_detail_sku_mid_split_line);
        sku1ValuesLay = (SelfAdaptionViewGroup) findViewById(R.id.goods_detail_sku1_items_lay);
        sku2TypeName = (TextView) findViewById(R.id.goods_detail_sku2_type_name);
        sku2ValuesLay = (SelfAdaptionViewGroup) findViewById(R.id.goods_detail_sku2_items_lay);


//        initViewPager();
        initDetailAndComment();

        if (intentGoodsId != -1) {
            detailTag += intentGoodsId;
        }
        if (intentStoreId != -1) {
            params.put("storeId", intentStoreId);
        }

        loadDataGet(detailTag, params);
    }

//    /**
//     * 初始化viewpager部分
//     */
//    private void initViewPager() {
////        viewList = new ArrayList<>();
////        tabViewPager = (ViewPager) findViewById(R.id.goods_detail_viewpager);
////        tabViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
////            @Override
////            public void onPageScrolled(int position, float positionOffset, int
////                    positionOffsetPixels) {
////
////            }
////
////            @Override
////            public void onPageSelected(int position) {
////                if (position == 0) {
////                    introduceTab.setChecked(true);
////                } else {
////                    commendsTab.setChecked(true);
////                }
////            }
////
////            @Override
////            public void onPageScrollStateChanged(int state) {
////                switch (state) {
////                    case ViewPager.SCROLL_STATE_DRAGGING:
////                        // viewpager正在滑动，手指还未松开！！
////                        break;
////                    case ViewPager.SCROLL_STATE_SETTLING:
////                        // viewpager回弹时，手指已经松开，正在回弹或者正在滑动到下一界面
////                        break;
////                    case ViewPager.SCROLL_STATE_IDLE:
////                        // viewpager已经设置完毕
//////                        baseScrollView.scrollTo(0,baseScrollView.getScrollY());
////                        break;
////                }
////            }
////        });
//
//
//        View tab1 = View.inflate(this, R.layout.goods_detail_webview, null);
//        introduceWebview = (WraperWebView) tab1.findViewById(R.id.goods_detail_introduce_webview);
//        introduceNoAny = (TextView) tab1.findViewById(R.id.goods_detail_introduce_no_any);
//
//        View tab2 = View.inflate(this, R.layout.goods_detail_no_any, null);
////        commendNoAny = (TextView) tab2.findViewById(R.id.goods_detail_commends_no_any);
//        commendsGroup = (RadioGroup) tab2.findViewById(R.id.goods_detail_commend_radiogroup);
//        commendsGroup.setOnCheckedChangeListener(commendTypeChangeListener);
////        commendListView = (ListView) tab2.findViewById(R.id.goods_detail_commend_listview);
////        commentAdapter = new CommentAdapter(commentList);
////        commendListView.setAdapter(commentAdapter);
////        commentFragmentLay = (FrameLayout) tab2.findViewById(R.id
//// .goods_detail_comment_fragments_lay);
//        fragmentManager = getSupportFragmentManager();
//        commentFragments[0] = (CommentFragment) fragmentManager.getFragments().get(0);
//        commentFragments[1] = (CommentFragment) fragmentManager.getFragments().get(1);
//        commentFragments[2] = (CommentFragment) fragmentManager.getFragments().get(2);
//        // 显示好评framgetn
//        fragmentManager.beginTransaction()
//                .hide(commentFragments[1])
//                .hide(commentFragments[2])
//                .show(commentFragments[0])
//                .commitAllowingStateLoss();
//        curCommentPosition = 0;
////        commentFragments[1] = new CommentFragment();
////        commentFragments[2] = new CommentFragment();
////        FragmentTransaction transaction = fragmentManager.beginTransaction();
////        transaction.add(R.id.goods_detail_comment_fragments_lay,
//// commentFragments[curCommentPosition]);
////        transaction.commitAllowingStateLoss();
//
//
////        viewList.add(tab1);
////        viewList.add(tab2);
////        introduceWebview.setInitialScale(94);
//
////        introduceWebview.setWebViewClient(new WebViewClient() {
////            @Override
////            public void onPageFinished(WebView view, String url) {
////                try {
////                    Thread.sleep(1000);
////                } catch (InterruptedException e) {
////                    e.printStackTrace();
////                }
////                int contentHeight = introduceWebview.getContentHeight();
////                DLog.i("lilijun", "webView加载完成！！contentHeight---->>>" + contentHeight);
////                if (contentHeight != 0) {
////                    // 取最大高度值
////                    curViewpagerHeight = Math.max(curViewpagerHeight,contentHeight);
////                    viewPagerParams.height = curViewpagerHeight;
////                    tabViewPager.setLayoutParams(viewPagerParams);
////                }
////            }
////        });
//        introduceWebview.getSettings().setDefaultTextEncodingName("utf-8");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            introduceWebview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm
//                    .TEXT_AUTOSIZING);
//        } else {
//            introduceWebview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
//        }
//
////        pagerAdapter = new ViewPagerAdapter(viewList);
////        tabViewPager.setAdapter(pagerAdapter);
////        viewPagerParams = tabViewPager.getLayoutParams();
//    }

    private void initDetailAndComment() {
        introduceWebview = (WebView) findViewById(R.id.goods_detail_introduce_webview);
        introduceNoAny = (TextView) findViewById(R.id.goods_detail_introduce_no_any);

        commendsGroup = (RadioGroup) findViewById(R.id.goods_detail_commend_radiogroup);
        commendsGroup.setOnCheckedChangeListener(commendTypeChangeListener);
        commendsGroup.setVisibility(View.GONE);

        fragmentManager = getSupportFragmentManager();
        commentFragments[0] = (CommentFragment) fragmentManager.getFragments().get(0);
        commentFragments[1] = (CommentFragment) fragmentManager.getFragments().get(1);
        commentFragments[2] = (CommentFragment) fragmentManager.getFragments().get(2);
        // 隐藏所有的评论fragment
        fragmentManager.beginTransaction()
                .hide(commentFragments[1])
                .hide(commentFragments[2])
                .hide(commentFragments[0])
                .commitAllowingStateLoss();
        curCommentPosition = 0;

//        introduceWebview.getSettings().setDefaultTextEncodingName("utf-8");
        WebSettings ws = introduceWebview.getSettings();
        ws.setDefaultTextEncodingName("uft-8");
//        ws.setBuiltInZoomControls(true);// 隐藏缩放按钮
        // ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);// 排版适应屏幕

//        ws.setUseWideViewPort(true);// 可任意比例缩放
        ws.setLoadWithOverviewMode(true);//
        // setUseWideViewPort方法设置webview推荐使用的窗口。setLoadWithOverviewMode方法是设置webview加载的页面的模式。

//        ws.setSavePassword(true);
//        ws.setSaveFormData(true);// 保存表单数据
        ws.setJavaScriptEnabled(true);
        ws.setPluginState(WebSettings.PluginState.ON);
//        ws.setGeolocationEnabled(true);// 启用地理定位
//        ws.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");// 设置定位的数据库路径
        ws.setDomStorageEnabled(true);
        ws.setSupportMultipleWindows(true);// 新加

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm
                    .TEXT_AUTOSIZING);
        } else {
            ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }
    }

    @Override
    protected void onLoadDataSuccess(String tag, JSONObject resultObject) {
        super.onLoadDataSuccess(tag, resultObject);
        if (TextUtils.equals(tag, detailTag)) {
            // 获取详情信息成功
            ParseUtil.parseGoodsDetail(resultObject, goodsDetail, shop);
            if (!isResetData) {
                // 首次进入加载
                if (goodsDetail.getId() != -1 && shop.getId() != -1) {
                    initData();
                    showCenterView();
                } else {
                    showErrorView();
                }
            } else {
                // 重新设置数据即可
                if (goodsDetail.getId() != -1 && shop.getId() != -1) {
                    resetViewData();
                    showCenterView();
                } else {
                    showErrorView();
                }
            }
        } else if (TextUtils.equals(tag, ADD_TO_SHOPPING_CART)) {
            // 加入购物车成功
            progressDialog.dismiss();
//            // 标志已经添加到购物车
//            isAddedToShoppingCart = true;
            if (!isBuyNow) {
                // 不是 “立即购买” 仅仅是加入购物车
                Util.showToast(GoodsDetailActivity.this, getResources().getString(R.string
                        .add_shopping_cart_success));
            } else {
                // 是 “立即购买”
                ConfirmOrderActivity.startActivity(GoodsDetailActivity.this);
            }
            EventBus.getDefault().post(Constants.ADD_SHOPPING_CART_SUCCESS);
        }
//        else if (TextUtils.equals(tag, ADD_TO_STORE)) {
//            // 加入小店成功
//            progressDialog.dismiss();
//            Util.showToast(GoodsDetailActivity.this, getResources().getString(R.string
//                    .add_to_store_success));
//        }
        else if (TextUtils.equals(tag, QUREY_ADDRESS)) {
            // 查询用户是否有收货地址成功
            List<Address> emptyList = ParseUtil.parseAddressListResult(resultObject);
            progressDialog.dismiss();
            if (emptyList != null && !emptyList.isEmpty()) {
                // 用户有默认收货地址,处理 立即购买 功能逻辑
//                if (isAddedToShoppingCart) {
//                    // 已经添加过一次到购物车了  就直接跳转到确认订单的界面去
//                    ConfirmOrderActivity.startActivity(GoodsDetailActivity.this);
//                } else {
//                    // 没有添加过到购物车，就先加入购物车再跳转到确认订单界面
//                    isBuyNow = true;
//                    progressDialog = Util.showLoadingDialog(GoodsDetailActivity.this,
//                            progressDialog);
//                    addShoppingCart(ADD_TO_SHOPPING_CART);
//                }
//                BuyNowConfirmOrderActivity.startActivity(GoodsDetailActivity.this, shop,
//                        goodsDetail, buyNum.getText().toString(), curSku1Position,
// curSku2Position,
//                        totalFreight);

//                BuyNowConfirmOrderActivity.startActivity(GoodsDetailActivity.this, shop,
//                        goodsDetail, getShoppingCartParams());
                handler.post(buyNowRunnable);
            } else {
                // 用户没有默认收货地址,跳转到新添加收货地址界面
                Util.showToast(GoodsDetailActivity.this, getResources().getString(R.string
                        .no_receive_address_default));
                AddNewAddressActivity.startActivity(GoodsDetailActivity.this);
            }
        } else if (TextUtils.equals(tag, GET_USER_INFO)) {
            // 查询用户信息成功
            ParseUtil.parseLoginUserInfo(resultObject);
            if (UserInfoManager.getInstance().getUserInfo().getVipLevel() > 0) {
                // 用户够资格加入买，则立即购买
                if (UserInfoManager.getInstance().getUserInfo().getDefaultReceiveAddress() ==
                        null) {
                    // 本地用户信息中没有收货地址, 去查询是否有默认收货地址
                    progressDialog = Util.showLoadingDialog(GoodsDetailActivity.this,
                            progressDialog);
                    loadDataGet(QUREY_ADDRESS, null);
                } else {
                    // 本地用户信息中有默认收货地址，则去做 立即购买 相关操作
//                    BuyNowConfirmOrderActivity.startActivity(GoodsDetailActivity.this, shop,
//                            goodsDetail, getShoppingCartParams());
                    handler.post(buyNowRunnable);
                }
            } else {
                // 用户不够资格买，跳转到充值界面
                progressDialog.dismiss();
                Util.showToast(GoodsDetailActivity.this, getResources().getString(R.string
                        .not_vip));
                TopUpActivity.startActivity(GoodsDetailActivity.this);
            }
        }
    }

    @Override
    protected void onLoadDataError(String tag, int errorCode, String msg) {
        super.onLoadDataError(tag, errorCode, msg);
        if (TextUtils.equals(tag, detailTag)) {
            // 获取详情数据失败
            showErrorView();
            Util.showErrorMessage(GoodsDetailActivity.this, msg, getResources().getString(R
                    .string.get_goods_details_error));
        } else if (TextUtils.equals(tag, ADD_TO_SHOPPING_CART)) {
            // 加入购物车失败
            progressDialog.dismiss();
            Util.showErrorMessage(GoodsDetailActivity.this, msg, getResources().getString(R.string
                    .add_shopping_cart_failed));
            if (errorCode == Constants.LOGIN_TIME_OUT) {
                // 登录信息过期，跳转到登录界面
                LoginActivity.startActivity(GoodsDetailActivity.this);
            }
        }
//        else if (TextUtils.equals(tag, ADD_TO_STORE)) {
//            // 加入小店失败
//            progressDialog.dismiss();
//            Util.showErrorMessage(GoodsDetailActivity.this, msg, getResources().getString(R.string
//                    .add_to_store_failed));
//        }
        else if (TextUtils.equals(tag, QUREY_ADDRESS)) {
            // 查询用户是否有收货地址失败
            progressDialog.dismiss();
            Util.showErrorMessage(GoodsDetailActivity.this, msg, getResources().getString(R.string
                    .get_user_info_failed));
        } else if (TextUtils.equals(tag, GET_USER_INFO)) {
            // 查询用户信息失败
            progressDialog.dismiss();
            Util.showErrorMessage(GoodsDetailActivity.this, msg, getResources().getString(R.string
                    .get_user_info_failed));
        }
    }

    @Override
    protected void tryAgain() {
        super.tryAgain();
        loadDataGet(detailTag, params);
    }

    @Override
    public void finish() {
        try {
            // 反射方法 暂停webView
            introduceWebview.getClass().getMethod("onPause").invoke(introduceWebview,
                    (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.finish();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        if (introduceWebview != null) {
            introduceWebview.setVisibility(View.GONE);
            introduceWebview.removeAllViews();
            introduceWebview.destroy();
        }
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Subscribe
    public void onMessageEvent(String message) {
        // 接收到EventBus发送的消息
        if (TextUtils.equals(Constants.LOGIN_SUCCESS_MSG, message)) {
            // 登陆成功(此处用登陆的EventBus是因为用户可能在用户未登陆成功时进入商品详情)
//            loadDataGet(detailTag, params);
            isResetData = true;
            tryAgain();
        }
    }

    private void initData() {
        bannerAdapter = new DetailBannerAdapter(pagerView, goodsDetail.getImgUrls());
        pagerView.setAdapter(bannerAdapter);
        goodsName.setText(goodsDetail.getName());
//        priceLeft.setText(String.format(getResources().getString(R.string.format_money), " " +
//                goodsDetail.getPrice()));
//        marketPriceLeft.setText(String.format(getResources().getString(R.string
//                        .format_money_market),goodsDetail.getMarketPrice() + ""));
        salesNum.setText(String.format(getResources().getString(R.string.format_sales),
                goodsDetail.getSalesNums() + ""));
        postage.setText(goodsDetail.isTakeFreight() ? getResources().getString(R.string
                .take_factoryPrice) : String.format(getResources().getString(R.string
                .format_factoryPrice), goodsDetail.getFreight()));
        buyNum.setText("1");
        stockNum.setText(String.format(getResources().getString(R.string.format_stock),
                goodsDetail.getStock() + ""));

        setViewBySalesLevel(goodsDetail.getDefaultSkuInfo());
//        shopName.setText(shop.getName());
//        shopLogo.setImageURI(shop.getLogoUrl());
//        switch (shop.getStoreType()) {
//            case 1:
//                //供货商
//                shopType.setText(getResources().getString(R.string.shop_type1));
//                break;
//            case 2:
//            case 3:
//                //经销商
//                shopType.setText(getResources().getString(R.string.shop_type2));
//                break;
//            default:
//                //其他
//                shopType.setText(getResources().getString(R.string.shop_type_other));
//                break;
//        }
        if (!TextUtils.isEmpty(goodsDetail.getIntroduce().trim())) {
            introduceWebview.setVisibility(View.VISIBLE);
            introduceNoAny.setVisibility(View.GONE);
//            introduceWebview.loadDataWithBaseURL("", goodsDetail.getIntroduce(), "text/html",
// "utf-8", "");
//            introduceWebview.loadDataWithBaseURL("",getHtmlData(goodsDetail.getIntroduce()),
// "text/html", "utf-8", "");
            introduceWebview.loadData(getHtmlData(goodsDetail.getIntroduce()), "text/html; " +
                    "charset=utf-8", "utf-8");
        } else {
            introduceWebview.setVisibility(View.GONE);
            introduceNoAny.setVisibility(View.VISIBLE);
        }

        // 设置评论加载类型
        commentFragments[0].setIdAndType(intentGoodsId, Comment.COMMENT_TYPE_GOOD);
        commentFragments[1].setIdAndType(intentGoodsId, Comment.COMMENT_TYPE_NORMAL);
        commentFragments[2].setIdAndType(intentGoodsId, Comment.COMMENT_TYPE_LOW);
        // 开始第一次加载好评数据
        commentFragments[0].loadCommentOut();

//        if(goodCommentList.isEmpty()){
        // 没有好评数据
//            commendNoAny.setText(getResources().getString(R.string.no_good_comments));
//            commendNoAny.setVisibility(View.VISIBLE);
//        }else {
        // 有好评数据
//            commentList.addAll(goodCommentList);
//            commendNoAny.setVisibility(View.GONE);
//            commentAdapter.notifyDataSetChanged();
//            commentFragments[0].setCommentList(goodCommentList);
//        }

        if (goodsDetail.getSku1Values() != null) {
            DLog.i("有类型1的数据！");
            // 有类型1的数据
            setSku1LayVisible(true);
            // 设置总的类型名称
            sku1TypeName.setText(goodsDetail.getSku1Name());
            // 显示类型1Value视图
            addSku1ValuesItem();
        } else {
            setSku1LayVisible(false);
        }

        if (goodsDetail.getSku2Values() != null) {
            DLog.i("有类型2的数据！");
            setSku2LayVisible(true);
            sku2TypeName.setVisibility(View.VISIBLE);
            sku2TypeName.setText(goodsDetail.getSku2Name());
            addSku2ValuesItem();
        } else {
            setSku2LayVisible(false);
        }

//        if (goodsDetail.getSkuTotalMap().size() <= 1) {
//            setSku1LayVisible(false);
//        } else {
//            setSku1LayVisible(true);
//            // 设置总的类型名称
//            sku1TypeName.setText(goodsDetail.getSku1Name());
//            // 显示小类型视图
//            addSku1ValuesItem();
//        }
    }


    /**
     * 当从详情页加载成功之后需要再次重新加载数据时设置的视图数据
     */
    private void resetViewData() {
        salesNum.setText(String.format(getResources().getString(R.string.format_sales),
                goodsDetail.getSalesNums() + ""));
        postage.setText(goodsDetail.isTakeFreight() ? getResources().getString(R.string
                .take_factoryPrice) : String.format(getResources().getString(R.string
                .format_factoryPrice), goodsDetail.getFreight()));
        stockNum.setText(String.format(getResources().getString(R.string.format_stock),
                goodsDetail.getStock() + ""));
//        setViewBySalesLevel(goodsDetail.getDefaultSkuInfo());
    }

    /**
     * 设置类型1视图显示
     *
     * @param visible
     */
    private void setSku1LayVisible(boolean visible) {
        if (visible) {
            sku1TypeName.setVisibility(View.VISIBLE);
            sku1ValuesLay.setVisibility(View.VISIBLE);
            skuSplitLine.setVisibility(View.VISIBLE);
        } else {
            sku1TypeName.setVisibility(View.GONE);
            sku1ValuesLay.setVisibility(View.GONE);
            skuSplitLine.setVisibility(View.GONE);
        }
    }

    /**
     * 设置类型2视图显示
     *
     * @param visible
     */
    private void setSku2LayVisible(boolean visible) {
        if (visible) {
            skuMidSplitLine.setVisibility(View.VISIBLE);
            sku2TypeName.setVisibility(View.VISIBLE);
            sku2ValuesLay.setVisibility(View.VISIBLE);
        } else {
            skuMidSplitLine.setVisibility(View.GONE);
            sku2TypeName.setVisibility(View.GONE);
            sku2ValuesLay.setVisibility(View.GONE);
        }
    }

    /**
     * 显示类型1款式的视图
     */
    private void addSku1ValuesItem() {
        if (goodsDetail.getSku1Values() == null || goodsDetail.getSku1Values().length == 0) {
            setSku1LayVisible(false);
            return;
        }
        sku1ValuesLay.removeAllViews();
        int length = goodsDetail.getSku1Values().length;
        for (int i = 0; i < length; i++) {
//            SkuInfo skuInfo = goodsDetail.getSkuInfoList().get(i);
            View view = View.inflate(GoodsDetailActivity.this, R.layout.goods_detail_sku_item,
                    null);
            final FrameLayout itemLay = (FrameLayout) view.findViewById(R.id
                    .goods_detail_sku_item_lay);
            TextView skuName = (TextView) view.findViewById(R.id.goods_detail_sku_item_name);
            View selectView = view.findViewById(R.id.goods_detail_sku_select_view);
            itemLay.setTag(i);
            itemLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!itemLay.isSelected()) {
                        // 设置上一次选中的item选中状态为false
                        ((LinearLayout) sku1ValuesLay.getChildAt(curSku1Position)).getChildAt(1)
                                .setSelected(false);
                        // 隐藏原来选中的标示图片
                        ((FrameLayout) ((LinearLayout) sku1ValuesLay.getChildAt(curSku1Position))
                                .getChildAt(1)).getChildAt(1)
                                .setVisibility(View.GONE);
                        itemLay.setSelected(true);
                        // 显示新的选中的标示图片
                        itemLay.getChildAt(1).setVisibility(View.VISIBLE);

                        curSku1Position = (int) itemLay.getTag();

                        // 设置相应的数据,价格、库存等
                        SkuInfo skuInfo = null;
                        if (goodsDetail.getSku1Values() != null && curSku1Position < goodsDetail
                                .getSku1Values().length) {
                            // 获SkuInfo的key值
                            String key = goodsDetail.getSku1Values()[curSku1Position];
                            if (goodsDetail.getSku2Values() != null && curSku2Position <
                                    goodsDetail.getSku2Values().length) {
                                // 获取类型2的当前选中的position
                                key += goodsDetail.getSku2Values()[curSku2Position];
                            }
                            // 获取到SkuInfo
                            skuInfo = goodsDetail.getSkuTotalMap().get(key);
                        }
                        if (skuInfo == null) {
                            DLog.i("设置skuInfo信息为空！！");
                            return;
                        }
                        DLog.i("设置skuInfo信息！！");
//                        priceLeft.setText(String.format(getResources().getString(R.string
//                                .format_money), " " +
//                                skuInfo.getPrice()));
//                        marketPriceLeft.setText(String.format(getResources().getString(R.string
//                                        .format_money_market),
//                                skuInfo.getMarketPrice() + ""));
//                        salesNum.setText(String.format(getResources().getString(R.string
//                                        .format_sales),
//                                skuInfo.getSalesNum() + ""));
//                        stockNum.setText(String.format(getResources().getString(R.string
//                                        .format_stock),
//                                skuInfo.getQuantity() + ""));
                        setViewBySalesLevel(skuInfo);
                    }
                }
            });
            skuName.setText(goodsDetail.getSku1Values()[i]);
            if (curSku1Position == i) {
                itemLay.setSelected(true);
                selectView.setVisibility(View.VISIBLE);
            } else {
                itemLay.setSelected(false);
                selectView.setVisibility(View.GONE);
            }
            sku1ValuesLay.addView(view);
        }
    }

    /**
     * 显示类型2款式的视图
     */
    private void addSku2ValuesItem() {
        if (goodsDetail.getSku2Values() == null || goodsDetail.getSku2Values().length == 0) {
            setSku1LayVisible(false);
            return;
        }
        sku2ValuesLay.removeAllViews();
        int length = goodsDetail.getSku2Values().length;
        for (int i = 0; i < length; i++) {
//            SkuInfo skuInfo = goodsDetail.getSkuInfoList().get(i);
            View view = View.inflate(GoodsDetailActivity.this, R.layout.goods_detail_sku_item,
                    null);
            final FrameLayout itemLay = (FrameLayout) view.findViewById(R.id
                    .goods_detail_sku_item_lay);
            TextView skuName = (TextView) view.findViewById(R.id.goods_detail_sku_item_name);
            View selectView = view.findViewById(R.id.goods_detail_sku_select_view);
            itemLay.setTag(i);
            itemLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!itemLay.isSelected()) {
                        // 设置上一次选中的item选中状态为false
                        ((LinearLayout) sku2ValuesLay.getChildAt(curSku2Position)).getChildAt(1)
                                .setSelected(false);
                        // 隐藏原来选中的标示图片
                        ((FrameLayout) ((LinearLayout) sku2ValuesLay.getChildAt(curSku2Position))
                                .getChildAt(1)).getChildAt(1)
                                .setVisibility(View.GONE);
                        itemLay.setSelected(true);
                        // 显示新的选中的标示图片
                        itemLay.getChildAt(1).setVisibility(View.VISIBLE);

                        curSku2Position = (int) itemLay.getTag();

                        // 设置相应的数据,价格、库存等
                        SkuInfo skuInfo = null;
                        if (goodsDetail.getSku1Values() != null && curSku1Position < goodsDetail
                                .getSku1Values().length) {
                            // 获SkuInfo的key值
                            String key = goodsDetail.getSku1Values()[curSku1Position];
                            if (goodsDetail.getSku2Values() != null && curSku2Position <
                                    goodsDetail.getSku2Values().length) {
                                // 获取类型2的当前选中的position
                                key += goodsDetail.getSku2Values()[curSku2Position];
                            }
                            // 获取到SkuInfo
                            skuInfo = goodsDetail.getSkuTotalMap().get(key);
                        }
                        if (skuInfo == null) {
                            DLog.i("设置skuInfo 2 信息为空！！");
                            return;
                        }
                        DLog.i("设置skuInfo 2信息！！");
//                            SkuInfo skuInfo2 = goodsDetail.getSkuTotalMap().get(curSku1Position);
//                        priceLeft.setText(String.format(getResources().getString(R.string
//                                .format_money), " " +
//                                skuInfo.getPrice()));
//                        marketPriceLeft.setText(String.format(getResources().getString(R.string
//                                        .format_money_market),
//                                skuInfo.getMarketPrice() + ""));
//                        salesNum.setText(String.format(getResources().getString(R.string
//                                        .format_sales),
//                                skuInfo.getSalesNum() + ""));
//                        stockNum.setText(String.format(getResources().getString(R.string
//                                        .format_stock),
//                                skuInfo.getQuantity() + ""));
                        setViewBySalesLevel(skuInfo);
                    }
                }
            });
            skuName.setText(goodsDetail.getSku2Values()[i]);
            if (curSku2Position == i) {
                itemLay.setSelected(true);
                selectView.setVisibility(View.VISIBLE);
            } else {
                itemLay.setSelected(false);
                selectView.setVisibility(View.GONE);
            }
            sku2ValuesLay.addView(view);
        }
    }

    /**
     * 根据salesLevel显示不同的视图或数据
     *
     * @param skuInfo
     */
    private void setViewBySalesLevel(SkuInfo skuInfo) {
        switch (goodsDetail.getSalesLevel()) {
            case 1:
                // 售卖新人价，显示市场价和新人价
                priceLeft.setVisibility(View.GONE);
                priceRight.setVisibility(View.VISIBLE);
                marketPriceLeft.setVisibility(View.VISIBLE);
                marketPriceRight.setVisibility(View.GONE);
//                vipLab.setVisibility(View.GONE);

                marketPriceLeft.setText(String.format(getResources().getString(R.string
                        .format_money), " " + skuInfo.getPrice()));
                priceRight.setText(String.format(getResources().getString(R.string
                        .format_new_money), " " + skuInfo.getNewbiePrice()));

                // 底部只显示立即购买按钮
                buyNowBtn.setVisibility(View.VISIBLE);
                addShoppingCartBtn.setVisibility(View.GONE);
                addStoreBtn.setVisibility(View.GONE);
                break;
            case 2:
                // 只显示售卖会员价
                priceLeft.setVisibility(View.VISIBLE);
                priceRight.setVisibility(View.GONE);
                marketPriceLeft.setVisibility(View.GONE);
                marketPriceRight.setVisibility(View.GONE);
//                vipLab.setVisibility(View.VISIBLE);
                pagerView.showLab();

                priceLeft.setText(String.format(getResources().getString(R.string
                        .format_vip_money), " " + skuInfo.getMemberPrice()));

                // 底部显示所有按钮
                buyNowBtn.setVisibility(View.VISIBLE);
                addShoppingCartBtn.setVisibility(View.VISIBLE);
                addStoreBtn.setVisibility(View.VISIBLE);
                break;
            case 3:
                // 显示非会员价和会员价
                priceLeft.setVisibility(View.VISIBLE);
                priceRight.setVisibility(View.VISIBLE);
                marketPriceLeft.setVisibility(View.GONE);
                marketPriceRight.setVisibility(View.GONE);
//                vipLab.setVisibility(View.GONE);

                priceLeft.setText(String.format(getResources().getString(R.string
                        .format_money), " " + skuInfo.getPrice()));
                priceRight.setText(String.format(getResources().getString(R.string
                        .format_vip_money), " " + skuInfo.getMemberPrice()));

                // 底部显示所有按钮
                buyNowBtn.setVisibility(View.VISIBLE);
                addShoppingCartBtn.setVisibility(View.VISIBLE);
                addStoreBtn.setVisibility(View.VISIBLE);
                break;
            default:
                // 显示非会员价和市场价
                priceLeft.setVisibility(View.VISIBLE);
                priceRight.setVisibility(View.GONE);
                marketPriceLeft.setVisibility(View.GONE);
                marketPriceRight.setVisibility(View.VISIBLE);
//                vipLab.setVisibility(View.GONE);

                priceLeft.setText(String.format(getResources().getString(R.string
                        .format_money), " " + skuInfo.getPrice()));
                marketPriceRight.setText(String.format(getResources().getString(R.string
                        .format_money), " " + skuInfo.getMarketPrice()));

                // 底部显示所有按钮
                buyNowBtn.setVisibility(View.VISIBLE);
                addShoppingCartBtn.setVisibility(View.VISIBLE);
                addStoreBtn.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * 商品介绍、商品评价tab切换监听器
     */
    private RadioGroup.OnCheckedChangeListener detailsChangeListener = new RadioGroup
            .OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            if (i == R.id.goods_detail_introduce_tab) {
                // 商品介绍tab
//                tabViewPager.setCurrentItem(0);
                // 记录商品评价切换之前baseScrollView的滚动距离
                curScrollY = baseScrollView.getCurScrollY();
                if (!TextUtils.isEmpty(goodsDetail.getIntroduce().trim())) {
                    introduceWebview.setVisibility(View.VISIBLE);
                    introduceNoAny.setVisibility(View.GONE);
                    try {
                        // 反射方法 恢复webView
                        introduceWebview.getClass().getMethod("onResume").invoke(introduceWebview,
                                (Object[]) null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    introduceWebview.setVisibility(View.GONE);
                    introduceNoAny.setVisibility(View.VISIBLE);
                }

                // 隐藏评论所有的fragment
                fragmentManager.beginTransaction()
                        .hide(commentFragments[1])
                        .hide(commentFragments[2])
                        .hide(commentFragments[0])
                        .commitAllowingStateLoss();
                commendsGroup.setVisibility(View.GONE);

                // 滚动到上次切换之前的地方
                baseScrollView.setResetScrollY(curScrollY);
            } else {
                // 商品评价tab
//                tabViewPager.setCurrentItem(1);

                // 记录商品介绍切换之前baseScrollView的滚动距离
                curScrollY = baseScrollView.getCurScrollY();

                introduceWebview.setVisibility(View.GONE);
                introduceNoAny.setVisibility(View.GONE);
                commendsGroup.setVisibility(View.VISIBLE);

                try {
                    // 反射方法 暂停webView
                    introduceWebview.getClass().getMethod("onPause").invoke(introduceWebview,
                            (Object[]) null);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                switch (curCommentPosition) {
                    case 0:
                        // 显示好评fragment
                        fragmentManager.beginTransaction()
                                .hide(commentFragments[1])
                                .hide(commentFragments[2])
                                .show(commentFragments[0])
                                .commitAllowingStateLoss();
                        curCommentPosition = 0;
                        break;
                    case 1:
                        // 显示中评fragment
                        fragmentManager.beginTransaction()
                                .hide(commentFragments[0])
                                .hide(commentFragments[2])
                                .show(commentFragments[1])
                                .commitAllowingStateLoss();
                        commentFragments[1].loadCommentOut();
                        curCommentPosition = 1;
                        break;
                    case 2:
                        // 显示差评fragment
                        fragmentManager.beginTransaction()
                                .hide(commentFragments[0])
                                .hide(commentFragments[1])
                                .show(commentFragments[2])
                                .commitAllowingStateLoss();
                        commentFragments[2].loadCommentOut();
                        curCommentPosition = 2;
                        break;
                }
//                fragmentManager.beginTransaction()
//                        .hide(commentFragments[1])
//                        .hide(commentFragments[2])
//                        .show(commentFragments[0])
//                        .commitAllowingStateLoss();

                // 滚动到上次切换之前的地方
                baseScrollView.setResetScrollY(curScrollY);
            }
        }
    };

    /**
     * 好评、中评、差评tab切换监听器
     */
    private RadioGroup.OnCheckedChangeListener commendTypeChangeListener = new RadioGroup
            .OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            curScrollY = baseScrollView.getCurScrollY();
            switch (i) {
                case R.id.goods_detail_commend_good:
                    // 好评
                    // 显示好评fragment
                    fragmentManager.beginTransaction()
                            .hide(commentFragments[1])
                            .hide(commentFragments[2])
                            .show(commentFragments[0])
                            .commitAllowingStateLoss();
                    curCommentPosition = 0;
                    // 滚动到上次切换之前的地方
                    baseScrollView.setResetScrollY(curScrollY);
                    break;
                case R.id.goods_detail_commend_normal:
                    // 中评
                    // 显示中评fragment
                    fragmentManager.beginTransaction()
                            .hide(commentFragments[0])
                            .hide(commentFragments[2])
                            .show(commentFragments[1])
                            .commitAllowingStateLoss();
                    commentFragments[1].loadCommentOut();
                    curCommentPosition = 1;
                    // 滚动到上次切换之前的地方
                    baseScrollView.setResetScrollY(curScrollY);
                    break;
                case R.id.goods_detail_commend_low:
                    // 差评
                    // 显示差评fragment
                    fragmentManager.beginTransaction()
                            .hide(commentFragments[0])
                            .hide(commentFragments[1])
                            .show(commentFragments[2])
                            .commitAllowingStateLoss();
                    commentFragments[2].loadCommentOut();
                    curCommentPosition = 2;
                    // 滚动到上次切换之前的地方
                    baseScrollView.setResetScrollY(curScrollY);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.goods_detail_reduce_btn:
                // 减去购买数量
                int curNum = Integer.parseInt(buyNum.getText().toString().trim());
                if (curNum <= 1) {
                    // 不能再减了 最少购买1件商品
                    return;
                }
                buyNum.setText(--curNum + "");
                if (!goodsDetail.isTakeFreight()) {
                    // 不包邮
                    postage.setText(String.format(getResources().getString(R.string
                            .format_factoryPrice), Arith.mul(curNum, goodsDetail.getFreight())));
                }
                break;
            case R.id.goods_detail_add_btn:
                // 增加购买数量
                int curNum2 = Integer.parseInt(buyNum.getText().toString().trim());
                if (curNum2 >= goodsDetail.getStock()) {
                    // 如果当前购买数量超过了库存量  则不允许再添加了
                    return;
                }
                buyNum.setText(++curNum2 + "");
                if (!goodsDetail.isTakeFreight()) {
                    // 不包邮
                    postage.setText(String.format(getResources().getString(R.string
                            .format_factoryPrice), Arith.mul(curNum2, goodsDetail.getFreight())));
                }
                break;
//            case R.id.goods_detail_shop_info_lay:
//                // 进入店铺
//                StoreDitailActivity.startActivity(GoodsDetailActivity.this, shop
//                        .getUserId(), shop.getName());
//                break;
            case R.id.goods_detail_add_shopping_cart_btn:
                // 加入购物车
                if (!UserInfoManager.getInstance().isValidUserInfo()) {
                    // 用户未登录，跳转至登录界面
                    LoginActivity.startActivity(GoodsDetailActivity.this);
                    Util.showToast(GoodsDetailActivity.this, getResources().getString(R.string
                            .login_time_out_or_no));
                    return;
                }
                if (goodsDetail.getSalesLevel() == 2) {
                    // 会员专属商品(只卖会员)
                    if (UserInfoManager.getInstance().getUserInfo().getVipLevel() > 0) {
                        // 用户够资格加入买，则加入购物车
                        isBuyNow = false;
                        progressDialog = Util.showLoadingDialog(GoodsDetailActivity.this,
                                progressDialog);
                        handler.post(addShoppingCartRunnable);
//                        addShoppingCart(ADD_TO_SHOPPING_CART);
                    } else {
                        // 用户不够资格买，则跳转到充值界面
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Util.showToast(GoodsDetailActivity.this, getResources().getString(R.string
                                .not_vip));
                        TopUpActivity.startActivity(GoodsDetailActivity.this);
                    }
                } else {
                    // 非会员专属商品
                    isBuyNow = false;
                    progressDialog = Util.showLoadingDialog(GoodsDetailActivity.this,
                            progressDialog);
                    handler.post(addShoppingCartRunnable);
//                    addShoppingCart(ADD_TO_SHOPPING_CART);
                }
                break;
            case R.id.goods_detail_buy_now_btn:
                // 立即购买
                if (!UserInfoManager.getInstance().isValidUserInfo()) {
                    // 用户未登录
                    LoginActivity.startActivity(GoodsDetailActivity.this);
                    Util.showToast(GoodsDetailActivity.this, getResources().getString(R.string
                            .login_time_out_or_no));
                    return;
                }

                if (goodsDetail.getSalesLevel() == 2) {
                    // 会员专属商品(只卖会员)
                    if (UserInfoManager.getInstance().getUserInfo().getVipLevel() > 0) {
                        // 用户够资格加入买，则立即购买
                        if (UserInfoManager.getInstance().getUserInfo().getDefaultReceiveAddress
                                () == null) {
                            // 本地用户信息中没有收货地址, 去查询是否有默认收货地址
                            progressDialog = Util.showLoadingDialog(GoodsDetailActivity.this,
                                    progressDialog);
                            loadDataGet(QUREY_ADDRESS, null);
                        } else {
                            // 本地用户信息中有默认收货地址，则去做 立即购买 相关操作
//                            BuyNowConfirmOrderActivity.startActivity(GoodsDetailActivity.this,
// shop,
//                                    goodsDetail, getShoppingCartParams());
                            handler.post(buyNowRunnable);
                        }
                    } else {
                        // 用户不够资格买，查询一下用户当前的信息数据
                        progressDialog = Util.showLoadingDialog(GoodsDetailActivity.this,
                                progressDialog);
                        loadDataGet(GET_USER_INFO, null);
//                        Util.showToast(GoodsDetailActivity.this, getResources().getString(R.string
//                                .not_vip));
//                        TopUpActivity.startActivity(GoodsDetailActivity.this);
                        return;
                    }
                } else {
                    // 非会员专属商品
                    if (UserInfoManager.getInstance().getUserInfo().getDefaultReceiveAddress() ==
                            null) {
                        // 本地用户信息中没有收货地址, 去查询是否有默认收货地址
                        progressDialog = Util.showLoadingDialog(GoodsDetailActivity.this,
                                progressDialog);
                        loadDataGet(QUREY_ADDRESS, null);
                    } else {
                        // 本地用户信息中有默认收货地址，则去做 立即购买 相关操作
//                        BuyNowConfirmOrderActivity.startActivity(GoodsDetailActivity.this, shop,
//                                goodsDetail, getShoppingCartParams());
                        handler.post(buyNowRunnable);
                    }
                }
                break;
            case R.id.goods_detail_become_vip_btn:
                // 成为会员
//                Map<String, String> addParms = new HashMap<>();
//                addParms.put("source_store_id", shop.getId() + "");
//                addParms.put("goodsId", goodsDetail.getId() + "");
//                progressDialog = Util.showLoadingDialog(GoodsDetailActivity.this, progressDialog);
//                loadDataPost(ADD_TO_STORE, addParms);
                ToBeVipActivity.startActivity(GoodsDetailActivity.this);
                break;
        }
    }

//    /**
//     * 添加商品到购物车 或者 立即购买
//     *
//     * @param tag 接口名称
//     */
//
//    private void addShoppingCart(String tag) {
//        loadDataPost(tag, getShoppingCartParams());
//    }

    /**
     * 获取加入购物车所需数据参数
     *
     * @return
     */
    private void getShoppingCartParams(HashMap<String, String> addShoppingCartParams) {
        addShoppingCartParams.clear();
        addShoppingCartParams.put("store_user_id", shop.getUserId() + "");
        addShoppingCartParams.put("store_name", shop.getName());
        addShoppingCartParams.put("store_id", shop.getId() + "");
        addShoppingCartParams.put("userType", shop.getStoreType() + "");
        addShoppingCartParams.put("goods_id", goodsDetail.getId() + "");
        DLog.i("goodsDetail.getSendType()----->>" + goodsDetail.getSendType());
        addShoppingCartParams.put("goodsType", Util.getStringByObject(goodsDetail.getSendType()));
        addShoppingCartParams.put("goodsName", goodsDetail.getName());
        addShoppingCartParams.put("fh_user_id", goodsDetail.getFhUserId() + "");
        addShoppingCartParams.put("group_buy_price", goodsDetail.getPrice() + "");
        addShoppingCartParams.put("shop_market_price", goodsDetail.getMarketPrice() + "");
        addShoppingCartParams.put("ex_factory_price", goodsDetail.getFactoryPrice() + "");
        addShoppingCartParams.put("sales_num", goodsDetail.getSalesNums() + "");
        addShoppingCartParams.put("buyCount", buyNum.getText().toString());
        addShoppingCartParams.put("quantity", goodsDetail.getStock() + "");
        if (goodsDetail.getImgUrls() != null && !goodsDetail.getImgUrls().isEmpty()) {
            addShoppingCartParams.put("pic_server_url1", goodsDetail.getImgUrls().get(0));
        } else {
            addShoppingCartParams.put("pic_server_url1", "");
        }
        addShoppingCartParams.put("isfreeShipping", goodsDetail.isTakeFreight() + "");
        addShoppingCartParams.put("postage", totalFreight + "");
        addShoppingCartParams.put("singlePostage", goodsDetail.getFreight() + "");

        if (goodsDetail.getSku1Values() != null && curSku1Position < goodsDetail
                .getSku1Values().length) {
            // 类型1有数据
            // 获SkuInfo的key值
            String key = goodsDetail.getSku1Values()[curSku1Position];
            if (goodsDetail.getSku2Values() != null && curSku2Position <
                    goodsDetail.getSku2Values().length) {
                // 获取类型2的当前选中的position
                key += goodsDetail.getSku2Values()[curSku2Position];
            }
            // 获取到SkuInfo
            SkuInfo skuInfo = goodsDetail.getSkuTotalMap().get(key);
            if (skuInfo != null) {
                addShoppingCartParams.put("sku_id", skuInfo.getId() + "");
                addShoppingCartParams.put("sku1Name", skuInfo.getSkuName1());
                addShoppingCartParams.put("sku1Value", skuInfo.getSkuValue1());
                addShoppingCartParams.put("sku2Name", skuInfo.getSkuName2());
                addShoppingCartParams.put("sku2Value", skuInfo.getSkuValue2());
            }
        } else {
            // 没有类型数据，没有多个款式
            addShoppingCartParams.put("sku_id", goodsDetail.getDefaultSkuInfo().getId() + "");
            addShoppingCartParams.put("sku1Name", "");
            addShoppingCartParams.put("sku1Value", "");
            addShoppingCartParams.put("sku2Name", "");
            addShoppingCartParams.put("sku2Value", "");
        }
//        if (goodsDetail.getSkuInfoList().size() <= 1) {
//            // 没有多个款式
//            addShoppingCartParams.put("sku_id", goodsDetail.getDefaultSkuId() + "");
//            addShoppingCartParams.put("sku1Name", "");
//            addShoppingCartParams.put("sku1Value", "");
//            addShoppingCartParams.put("sku2Name", "");
//            addShoppingCartParams.put("sku2Value", "");
//        } else {
//            SkuInfo skuInfo = goodsDetail.getSkuInfoList().get(curSku1Position);
//            addShoppingCartParams.put("sku_id", skuInfo.getId() + "");
//            addShoppingCartParams.put("sku1Name", skuInfo.getSkuName1());
//            addShoppingCartParams.put("sku1Value", skuInfo.getSkuValue1());
//            addShoppingCartParams.put("sku2Name", skuInfo.getSkuName2());
//            addShoppingCartParams.put("sku2Value", skuInfo.getSkuValue2());
//        }
//        return addCartParams;
    }


    public static void startActivity(Context context, int id) {
        Intent intent = new Intent(context, GoodsDetailActivity.class);
        intent.putExtra("goodsId", id);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, int id, int storeId) {
        Intent intent = new Intent(context, GoodsDetailActivity.class);
        intent.putExtra("goodsId", id);
        intent.putExtra("storeId", storeId);
        context.startActivity(intent);
    }


//    private void changePage(Fragment fragment)
//    {
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.hide(commentFragments[curCommentPosition]);
//        if (!fragment.isAdded())
//        {
//            // 如果没有被添加过
//            transaction.add(R.id.main_tab_fragment_lay, fragment);
//        } else if (fragment.isHidden())
//        {
//            transaction.show(fragment);
//        }
//        transaction.commitAllowingStateLoss();
//    }

    private String getHtmlData(String bodyHTML) {
//        String head = "<head>" +
//                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, " +
//                "user-scalable=no\"> " +
//                "<style>img{max-width: 100%; width:auto; height:auto;}</style>" +
//                "</head>";
        String head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, " +
                "user-scalable=no\"> " +
                "<style>p{text-align:center} img{max-width: 100%;display:inline-block;width:auto;" +
                " height:auto;}</style>" +
                "</head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }


    /**
     * 加入购物车的Runnable对象
     */
    private Runnable addShoppingCartRunnable = new Runnable() {
        @Override
        public void run() {
            getShoppingCartParams(addCartParams);
            handler.sendEmptyMessage(ADD_TO_SHOPPING_CART_MSG);
        }
    };

    /**
     * 立即购买的Runnable对象
     */
    private Runnable buyNowRunnable = new Runnable() {
        @Override
        public void run() {
            getShoppingCartParams(buyNowParams);
            DLog.i("商品名称--1--->>" + buyNowParams.get("goodsName"));
            handler.sendEmptyMessage(BUY_NOW_MSG);
        }
    };


    private static class MyHandler extends Handler {
        private final WeakReference<GoodsDetailActivity> mActivity;

        private MyHandler(GoodsDetailActivity mActivity) {
            this.mActivity = new WeakReference<>(mActivity);
        }

        @Override
        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
            GoodsDetailActivity goodsDetailActivity = mActivity.get();
            if (goodsDetailActivity != null) {
                if (msg.what == ADD_TO_SHOPPING_CART_MSG) {
                    // 加入购物车
                    goodsDetailActivity.loadDataPost(ADD_TO_SHOPPING_CART, goodsDetailActivity
                            .addCartParams);
                } else if (msg.what == BUY_NOW_MSG) {
                    // 立即购买
                    DLog.i("商品名称--2--->>" + goodsDetailActivity.buyNowParams.get("goodsName"));
                    BuyNowConfirmOrderActivity.startActivity(goodsDetailActivity,
                            goodsDetailActivity.shop, goodsDetailActivity.goodsDetail,
                            goodsDetailActivity.buyNowParams);
                }
            }
        }
    }
}
