package com.ttxg.fruitday.gui.fragment;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.iflytek.voiceads.AdError;
import com.iflytek.voiceads.AdKeys;
import com.iflytek.voiceads.IFLYNativeAd;
import com.iflytek.voiceads.IFLYNativeListener;
import com.iflytek.voiceads.NativeADDataRef;
import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;
import com.ttxg.fruitday.R;
import com.ttxg.fruitday.gui.activity.GoodsDetailActivity;
import com.ttxg.fruitday.gui.activity.LoginActivity;
import com.ttxg.fruitday.gui.activity.MoreGoodsActivity;
import com.ttxg.fruitday.gui.activity.NewExclusiveActivity;
import com.ttxg.fruitday.gui.activity.SearchActivity;
import com.ttxg.fruitday.gui.activity.ToBeVipActivity;
import com.ttxg.fruitday.gui.activity.TopUpActivity;
import com.ttxg.fruitday.gui.activity.WebActivity;
import com.ttxg.fruitday.gui.adapter.BannerAdapter;
import com.ttxg.fruitday.gui.adapter.HomeListViewAdapter2;
import com.ttxg.fruitday.gui.view.RefreshListView;
import com.ttxg.fruitday.manager.UserInfoManager;
import com.ttxg.fruitday.model.AdItem;
import com.ttxg.fruitday.model.Goods;
import com.ttxg.fruitday.model.UserInfo;
import com.ttxg.fruitday.util.Constants;
import com.ttxg.fruitday.util.ParseUtil;
import com.ttxg.fruitday.util.PreferencesUtils;
import com.ttxg.fruitday.util.Util;
import com.ttxg.fruitday.util.log.DLog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 首页Tab Fragment
 * Created by yb on 2016/8/12.
 */
public class TabHomeFragment extends BaseFragment implements View.OnClickListener {
    public static final String TAG = "TabHomeFragment";
    /**
     * 请求首页的tag
     */
    private static String GET_HOME_DATA_TAG = "index/index";
    /**
     * 登陆tag
     */
    private final String LOGIN_USER = "supplier/login";

    //    private PullToRefreshLayout pullToRefreshLayout;
//    private RefreshExpandableListView2 listView;
    private RefreshListView listView;
    //    private HomeListViewAdapter adapter;
    private HomeListViewAdapter2 adapter;
    //    private List<HomeGroupItem> groupList;
    //    private List<List<Goods[]>> childList;
//    private List<List<Goods>> childList;
    private List<Goods> childList;
    private View headerView;

    private FrameLayout kdxfBannerLay;
    private SimpleDraweeView kdxfBannerImg;
    private TextView kdxfBannerAdMark;

    //    private SimpleDraweeView titleImg;
    private EditText searchInput;
    private Button searchBtn;

//    private TextView headerHotName;
//    private TextView headerHotMore;
//    private RelativeLayout headerHotTitleLay;
//    private LinearLayout headerHotItemLay;
//    /**
//     * 头部推荐标题数据
//     */
//    private RecommendTitle headerRecommendTitle;
//    /**
//     * 头部推荐商品列表数据集合
//     */
//    private List<Goods> headerRecommendList;

    /**
     * 轮播banner控件
     */
    private RollPagerView headerBannerPagerView;
    private BannerAdapter headerBannerAdapter;
//    /**
//     * 轮播消息控件
//     */
//    private CustomTextView customTextView;
    /**
     * 首次体验、消费充值、会员专卡、蜜蜂招募
     */
    private TextView[] headerAdTabs;
    /**
     * 顶部banner数据集合
     */
    private List<AdItem> headerBannerList;

//    /**
//     * 头部水平广告位
//     */
//    private HorizontalScrollView headerHorizontalScrollView;
//    /**
//     * 添加子视图的父类容器
//     */
//    private LinearLayout headerHoriziontalItemLay;
//    private List<AdItem> horizontalList;

//    /**
//     * 标题上logo图片路径
//     */
//    private String logoUrl;

    /**
     * 标记当前是否是在下拉刷新
     */
    private boolean isRefreshing = false;


    /**
     * 科大讯飞横幅Banner广告
     */
    private IFLYNativeAd bannerAd;

    /**
     * 科大讯飞插屏广告
     */
    private IFLYNativeAd interstitialAd;

//    /**
//     * 科大讯飞全屏广告
//     */
//    private IFLYFullScreenAd fullScreenAd;

//    /**
//     * 科大讯飞信息流广告
//     */
//    private IFLYNativeAd nativeAd;


    /**
     * 标识值次数
     */
    private int onResumeTime = 1;

    /**
     * 插屏dialog
     */
    private Dialog adDialog;
    // 插屏视图对象
    private SimpleDraweeView adImg,adIcon;
    private ImageView closeAdImg;
    private TextView adMark;
    private FrameLayout tabAdLay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EventBus.getDefault().register(this);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        getActivity().registerReceiver(receiver, filter);
    }

    @Override
    protected void initView(RelativeLayout view) {

//        getKDXFFullAd();
        setCenterView(R.layout.fragment_home);

//        groupList = new ArrayList<>();
        childList = new ArrayList<>();
        listView = (RefreshListView) view.findViewById(R.id.home_list_view);
        listView.setNextPageViewVisible(false);
//        adapter = new HomeListViewAdapter(groupList, childList);
        adapter = new HomeListViewAdapter2(childList);
        adapter.setOnRequestAdListener(new HomeListViewAdapter2.OnRequestAdListener() {
            @Override
            public void onRequestAd(int position,View view) {
                // 请求数据流广告
                getKDXFInfoAd(position,view);
            }
        });
//        pullToRefreshLayout = (PullToRefreshLayout) view.findViewById(R.id.home_refresh_layout);
//        pullToRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                DLog.i("lilijun", "下拉刷新！！！");
//                isRefreshing = true;
//                String loginInfoByPwd = PreferencesUtils.getString(Constants
//                        .LOGIN_INFO_BY_PWD);
//                if (!TextUtils.isEmpty(loginInfoByPwd)) {
//                    // 有通过账号密码登陆过的信息,则去登陆(每次进入都需要登陆)
//                    HashMap<String, String> loginParams = new HashMap<>();
//                    loginParams.put("parms", loginInfoByPwd);
//                    loadDataPost(LOGIN_USER, loginParams);
//                } else {
//                    loadDataGet(GET_HOME_DATA_TAG, null);
//                }
//            }
//        });

        // 标题部分
//        titleImg = (SimpleDraweeView) view.findViewById(R.id.home_title_img);
        searchInput = (EditText) view.findViewById(R.id.home_title_input_edit);
        searchBtn = (Button) view.findViewById(R.id.home_title_search_btn);
        searchBtn.setOnClickListener(this);
        searchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event
                        .getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    // 标题搜索按钮
                    // 隐藏键盘
                    Util.hideInput(getActivity(), searchInput);
                    SearchActivity.startActivity(getActivity(), searchInput.getText().toString());
                    return true;
                }
                return false;
            }
        });

        initHeaderView();
        listView.addHeaderView(headerView);
        listView.setAdapter(adapter);
//        listView.setonRefreshListener(new RefreshExpandableListView2.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                isRefreshing = true;
//                loadDataGet(GET_HOME_DATA_TAG, null);
//            }
//        });

        listView.setonRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefreshing = true;
                loadDataGet(GET_HOME_DATA_TAG, null);
            }
        });
        String loginInfoByPwd = PreferencesUtils.getString(Constants
                .LOGIN_INFO_BY_PWD);
        if (!TextUtils.isEmpty(loginInfoByPwd)) {
            // 有通过账号密码登陆过的信息,则去登陆(每次进入都需要登陆)
            HashMap<String, String> loginParams = new HashMap<>();
            loginParams.put("parms", loginInfoByPwd);
            loadDataPost(LOGIN_USER, loginParams);
        } else {
            loadDataGet(GET_HOME_DATA_TAG, null);
        }

        // 初始化插屏dialog
        adDialog = new Dialog(getActivity());
        adDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        adDialog.setContentView(R.layout.ad_table_layout);
        adDialog.getWindow().setWindowAnimations(R.style.dialogWindowAnim);

        adImg = (SimpleDraweeView) adDialog.findViewById(R.id.show_ad_img);

        adMark = (TextView) adDialog.findViewById(R.id.show_ad_mark);
        closeAdImg = (ImageView) adDialog.findViewById(R.id.close_ad_img);
        closeAdImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adDialog.dismiss();
            }
        });
        adIcon = (SimpleDraweeView) adDialog.findViewById(R.id.show_ad_icon);
        tabAdLay = (FrameLayout) adDialog.findViewById(R.id.show_ad_lay);
    }

    private void initHeaderView() {
//        headerRecommendTitle = new RecommendTitle();
//        headerRecommendList = new ArrayList<>();
        headerBannerList = new ArrayList<>();
//        horizontalList = new ArrayList<>();
        headerAdTabs = new TextView[3];
        headerView = View.inflate(getActivity(), R.layout.hot_header_item, null);

//        // 热门推荐
//        headerHotTitleLay = (RelativeLayout) headerView.findViewById(R.id
//                .home_header_hot_title_lay);
//        headerHotName = (TextView) headerView.findViewById(R.id.home_header_hot_name);
//        headerHotMore = (TextView) headerView.findViewById(R.id.home_header_hot_more);
//        headerHotMore.setOnClickListener(this);
//        headerHotItemLay = (LinearLayout) headerView.findViewById(R.id.home_header_hot_goods_lay);

        //banner
        headerBannerPagerView = (RollPagerView) headerView.findViewById(R.id.home_banner);
        headerBannerAdapter = new BannerAdapter(headerBannerPagerView, headerBannerList);
        headerBannerPagerView.setAdapter(headerBannerAdapter);
        headerBannerPagerView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // 顶部banner item点击事件
                doBannerItemClickListener(headerBannerList.get(position));
            }
        });

//        headerAdTabs[0] = (TextView) headerView.findViewById(R.id.home_tab_ad_0);
//        headerAdTabs[1] = (TextView) headerView.findViewById(R.id.home_tab_ad_1);
//        headerAdTabs[2] = (TextView) headerView.findViewById(R.id.home_tab_ad_2);
//        headerAdTabs[3] = (TextView) headerView.findViewById(R.id.home_tab_ad_3);

        headerAdTabs[0] = (TextView) headerView.findViewById(R.id.home_tab_ad_0);
        headerAdTabs[1] = (TextView) headerView.findViewById(R.id.home_tab_ad_1);
        headerAdTabs[2] = (TextView) headerView.findViewById(R.id.home_tab_ad_2);
        headerAdTabs[0].setOnClickListener(this);
        headerAdTabs[1].setOnClickListener(this);
        headerAdTabs[2].setOnClickListener(this);

        kdxfBannerLay = (FrameLayout) headerView.findViewById(R.id.home_kdxf_banner_lay);
        kdxfBannerLay.setVisibility(View.GONE);
        kdxfBannerImg = (SimpleDraweeView) headerView.findViewById(R.id.home_kdxf_banner_img);
        kdxfBannerAdMark = (TextView) headerView.findViewById(R.id.home_kdxf_banner_ad_mark);

//        kdxfBannerImg.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        bannerAd.setParameter(AdKeys.CLICK_POS_DX, event.getX() + "");
//                        bannerAd.setParameter(AdKeys.CLICK_POS_DY, event.getY() + "");
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        bannerAd.setParameter(AdKeys.CLICK_POS_UX, event.getX() + "");
//                        bannerAd.setParameter(AdKeys.CLICK_POS_UY, event.getY() + "");
//                        break;
//                    default:
//                        break;
//                }
//                return false;
//            }
//        });
//        headerAdTabs[3].setOnClickListener(this);

//        customTextView = (CustomTextView) headerView.findViewById(R.id.home_header_show_message);

        //horizontalScrollView
//        headerHorizontalScrollView = (HorizontalScrollView) headerView.findViewById(R.id
//                .home_header_horizontalScrollView);
//        headerHoriziontalItemLay = (LinearLayout) headerView.findViewById(R.id
//                .home_header_horizontal_layout);
    }

    @Override
    protected void onLoadDataSuccess(String tag, JSONObject resultObject) {
        super.onLoadDataSuccess(tag, resultObject);
        if (TextUtils.equals(tag, GET_HOME_DATA_TAG)) {
            // 首页数据加载成功
//            groupList.clear();
            childList.clear();
//            headerRecommendList.clear();
            headerBannerList.clear();
//            horizontalList.clear();
//            headerHotItemLay.removeAllViews();
//            logoUrl = ParseUtil.parseHomeData(resultObject, groupList, childList,
//                    headerRecommendTitle, headerRecommendList, headerBannerList, horizontalList);
//            ParseUtil.parseHomeData3(resultObject, groupList, childList, headerBannerList);
            ParseUtil.parseHomeData3(resultObject, childList, headerBannerList);
            if (isRefreshing) {
                listView.onRefreshComplete();
                isRefreshing = false;
            }
            refreshView();

            /**
             * 获取Banner横幅广告数据
             */
            getKDXFBannerAd();
//            if (isRefreshing) {
//                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.REFRESH_SUCCEED);
//                isRefreshing = false;
//            }
        } else if (TextUtils.equals(tag, LOGIN_USER)) {
            DLog.i("lilijun", "登录成功！！！！@！@！@！");
            // 登陆成功
            ParseUtil.parseLoginUserInfoResult(resultObject);
            // 加载首页数据
            loadDataGet(GET_HOME_DATA_TAG, null);
        }
    }

    @Override
    protected void onLoadDataError(String tag, int errorCode, String msg) {
        super.onLoadDataError(tag, errorCode, msg);
        DLog.i("tag----->>" + tag);
        if (TextUtils.equals(tag, GET_HOME_DATA_TAG)) {
            // 首页数据加载失败
            if (!isRefreshing) {
                // 不是下拉刷新，是首次进入加载数据失败
                showErrorView();
            } else {
                // 下拉刷新失败
                listView.onRefreshComplete();
                isRefreshing = false;
                Util.showErrorMessage(getActivity(), msg, getResources().getString(R.string
                        .refresh_fail));
            }
        } else if (TextUtils.equals(tag, LOGIN_USER)) {
            // 登陆失败
            // 重置UserInfo 信息
            DLog.i("lilijun", "登录失败！！！！@！@！@！");
            UserInfoManager.getInstance().setUserInfo(new UserInfo());
            // 加载首页数据
            loadDataGet(GET_HOME_DATA_TAG, null);
        }
    }

    @Override
    protected void tryAgain() {
        super.tryAgain();
        String loginInfoByPwd = PreferencesUtils.getString(Constants
                .LOGIN_INFO_BY_PWD);
        if (!TextUtils.isEmpty(loginInfoByPwd)) {
            // 有通过账号密码登陆过的信息,则去登陆(每次进入都需要登陆)
            HashMap<String, String> loginParams = new HashMap<>();
            loginParams.put("parms", loginInfoByPwd);
            loadDataPost(LOGIN_USER, loginParams);
        } else {
            loadDataGet(GET_HOME_DATA_TAG, null);
        }
    }


    private void refreshView() {
//        titleImg.setImageURI(logoUrl);
        adapter.notifyDataSetChanged();
        // 展开所有item
//        for (int i = 0; i < adapter.getGroupCount(); i++) {
//            listView.expandGroup(i);
//        }
//        addRecommendView();
        //banner
        if (headerBannerList.isEmpty()) {
            headerBannerPagerView.setVisibility(View.GONE);
        } else {
            headerBannerPagerView.setVisibility(View.VISIBLE);
            headerBannerAdapter.notifyDataSetChanged();
        }
        showCenterView();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("llj","TabHomeFragment，onResume()");
        if (onResumeTime % 2 == 0) {
            // 请求科大讯飞插屏广告
            getKDXFTableAd();
        }
        onResumeTime ++;
    }

    @Override
    public void onDestroy() {
//        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    /**
     * 请求科大讯飞banner广告
     */
    private void getKDXFBannerAd() {
        bannerAd = new IFLYNativeAd(getActivity(), "D1FC42EFBA10BEEB3EB0CD9299D24397", new IFLYNativeListener() {
            @Override
            public void onAdFailed(AdError adError) {
                // 广告请求失败
                Log.e("llj", "调用讯飞原生Banner广告失败！！！  errorCode---->>>" + adError.getErrorCode() + "\n 错误描述------>>>" + adError.getErrorDescription());
            }

            @Override
            public void onADLoaded(List<NativeADDataRef> lst) {
                // 广告请求成功
                Log.i("llj", "请求原生Banner广告成功---lst == null----->>>" + (lst == null));

                // 设置信息流广告数据
                if (lst != null && !lst.isEmpty()) {
                    Log.i("llj", "请求原生Banner广告成功---lst.size----->>>" + lst.size());
                    // 因为信息流广告每次只能返回一条信息流广告  所以只需要第一条数据
//                    childList.get(groupPosition).get(childPosition).setInfoAd(lst.get(0));

                    final NativeADDataRef adInfo = lst.get(0);

                    kdxfBannerLay.setVisibility(View.VISIBLE);
                    kdxfBannerImg.setImageURI(adInfo.getImage());
                    kdxfBannerAdMark.setText(adInfo.getAdSourceMark() + "|" + getResources().getString(R.string.ad));
                    // 曝光
                    adInfo.onExposured(kdxfBannerImg);
                    kdxfBannerImg.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            switch (event.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    bannerAd.setParameter(AdKeys.CLICK_POS_DX, event.getX() + "");
                                    bannerAd.setParameter(AdKeys.CLICK_POS_DY, event.getY() + "");
                                    break;
                                case MotionEvent.ACTION_UP:
                                    bannerAd.setParameter(AdKeys.CLICK_POS_UX, event.getX() + "");
                                    bannerAd.setParameter(AdKeys.CLICK_POS_UY, event.getY() + "");
                                    break;
                                default:
                                    break;
                            }
                            return false;
                        }
                    });
                    kdxfBannerImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            adInfo.onClicked(v);
//                            getKDXFBannerAd();
                        }
                    });
                }
            }

            @Override
            public void onCancel() {
                // 下载类广告， 下载提示框取消
            }

            @Override
            public void onConfirm() {
                // 下载类广告， 下载提示框确认
            }
        });
        int count = 1; // 一次拉取的广告条数：范围 1-30（ 目前仅支持每次请求一条）
        bannerAd.loadAd(count);
    }


    /**
     * 获取科大讯飞插屏广告
     */
    private void getKDXFTableAd() {
        //创建插屏广告： adId：开发者在广告平台(http://www.voiceads.cn/)申请的广告位 ID
        Log.i("llj","开始请求插屏广告！！");
        interstitialAd = new IFLYNativeAd(getActivity(), "0AB1E0073A73DC82104E3EDEADB99DC4", new IFLYNativeListener() {
            @Override
            public void onAdFailed(AdError adError) {
                // 广告请求失败
                Log.e("llj", "调用讯飞原生插屏广告失败！！！  errorCode---->>>" + adError.getErrorCode() + "\n 错误描述------>>>" + adError.getErrorDescription());
            }

            @Override
            public void onADLoaded(List<NativeADDataRef> lst) {
                // 广告请求成功
                Log.i("llj", "请求原生插屏广告成功---lst == null----->>>" + (lst == null));

                // 设置信息流广告数据
                if (lst != null && !lst.isEmpty()) {
                    Log.i("llj", "请求原生插屏广告成功---lst.size----->>>" + lst.size());
                    // 因为信息流广告每次只能返回一条信息流广告  所以只需要第一条数据
//                    childList.get(groupPosition).get(childPosition).setInfoAd(lst.get(0));

                    final NativeADDataRef adInfo = lst.get(0);

                    adImg.setImageURI(adInfo.getImage());
                    adMark.setText(adInfo.getAdSourceMark() + "|" + getResources().getString(R.string.ad));
                    adIcon.setImageURI(adInfo.getIcon());

                    adImg.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            switch (event.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    interstitialAd.setParameter(AdKeys.CLICK_POS_DX, event.getX() + "");
                                    interstitialAd.setParameter(AdKeys.CLICK_POS_DY, event.getY() + "");
                                    break;
                                case MotionEvent.ACTION_UP:
                                    interstitialAd.setParameter(AdKeys.CLICK_POS_UX, event.getX() + "");
                                    interstitialAd.setParameter(AdKeys.CLICK_POS_UY, event.getY() + "");
                                    break;
                                default:
                                    break;
                            }
                            return false;
                        }
                    });

                    adImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            adInfo.onClicked(view);
                            adDialog.dismiss();
                        }
                    });

                    Log.i("llj","插屏广告显示！！！！");
                    adDialog.show();

                    // 曝光
                    boolean state = adInfo.onExposured(tabAdLay);
                    Log.i("llj","插屏广告曝光,state------>>>"+state);
                }
            }

            @Override
            public void onCancel() {
                // 下载类广告， 下载提示框取消
            }

            @Override
            public void onConfirm() {
                // 下载类广告， 下载提示框确认
            }
        });
        int count = 1; // 一次拉取的广告条数：范围 1-30（ 目前仅支持每次请求一条）
        interstitialAd.loadAd(count);

    }

    /**
     * 请求科大讯飞信息流广告
     */
    private void getKDXFInfoAd(final int position,final View exposurView) {
        //创建原生广告： adId：开发者在广告平台(http://www.voiceads.cn/)申请的广告位 ID
        final IFLYNativeAd nativeAd = new IFLYNativeAd(getActivity(), "21978C28DA35C15274AD1F522AFAA5CA", new IFLYNativeListener() {
            @Override
            public void onAdFailed(AdError adError) {
                // 广告请求失败
                Log.e("llj", "调用讯飞信息流广告失败！！！  errorCode---->>>" + adError.getErrorCode() + "\n 错误描述------>>>" + adError.getErrorDescription());
                exposurView.setVisibility(View.GONE);
            }

            @Override
            public void onADLoaded(List<NativeADDataRef> lst) {
                // 广告请求成功
                Log.i("llj", "请求信息流广告成功---lst == null----->>>" + (lst == null));
                exposurView.setVisibility(View.VISIBLE);
                // 设置信息流广告数据
                if (!lst.isEmpty()) {
                    Log.i("llj", "请求信息流广告成功---lst.size----->>>" + lst.size());
                    // 因为信息流广告每次只能返回一条信息流广告  所以只需要第一条数据
//                    childList.get(groupPosition).get(childPosition).setInfoAd(lst.get(0));
                    childList.get(position).setInfoAd(lst.get(0));
                    // 设置为已经填充了
                    childList.get(position).setFullAd(true);
//                    // 设置为还未曝光上报
//                    childList.get(position).setAdExposured(false);
                    // 曝光
                    lst.get(0).onExposured(exposurView);
                    // 刷新视图
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancel() {
                // 下载类广告， 下载提示框取消
            }

            @Override
            public void onConfirm() {
                // 下载类广告， 下载提示框确认
            }
        });
        int count = 1; // 一次拉取的广告条数：范围 1-30（ 目前仅支持每次请求一条）
        nativeAd.loadAd(count);

        // 设置信息流调度器
//        childList.get(groupPosition).get(childPosition).setKdxfInfoAdControl(nativeAd);
        childList.get(position).setKdxfInfoAdControl(nativeAd);
    }

//    /**
//     * 科大讯飞请求信息流广告监听器
//     */
//    private IFLYNativeListener kdxfInfoAdListener = new IFLYNativeListener() {
//        @Override
//        public void onAdFailed(AdError adError) {
//            // 广告请求失败
//            Log.e("llj", "调用讯飞信息流广告失败！！！  errorCode---->>>" + adError.getErrorCode() + "\n 错误描述------>>>" + adError.getErrorDescription());
//        }
//
//        @Override
//        public void onADLoaded(List<NativeADDataRef> lst) {
//            // 广告请求成功
//            // TODO 显示并刷新列表视图
//        }
//
//        @Override
//        public void onCancel() {
//            // 下载类广告， 下载提示框取消
//        }
//
//        @Override
//        public void onConfirm() {
//            // 下载类广告， 下载提示框确认
//        }
//    };


    /**
     * 顶部banner item点击处理
     */

    private void doBannerItemClickListener(AdItem adItem) {
        switch (adItem.getType()) {
            case AdItem.TYPE_GROUP_MORE:
                // 楼层更多
                DLog.i("跳转至楼层更多！！");
                MoreGoodsActivity.startActivityByStoreId(getActivity(), adItem.getStoreId());
                break;
            case AdItem.TYPE_VIP_SPC:
                // 会员专卡
                DLog.i("跳转至会员专卡！！");
                ToBeVipActivity.startActivity(getActivity());
                break;
            case AdItem.TYPE_TOP_UP:
                // 消费充值
                DLog.i("跳转至消费充值！！");
                TopUpActivity.startActivity(getActivity());
                break;
            case AdItem.TYPE_GOODS_DETAIL:
                // 商品详情
                DLog.i("跳转至商品详情！！");
                GoodsDetailActivity.startActivity(getActivity(), adItem.getGoodsId());
                break;
            case AdItem.TYPE_CLASSIFY_GOODS:
                // 类目商品
                DLog.i("跳转至类目商品！！");
                SearchActivity.startActivity(getActivity(), adItem.getStoreId());
                break;
            case AdItem.TYPE_WEB:
                // 网页web
                DLog.i("跳转至网页web！！");
                if (TextUtils.equals(adItem.getSkipUrl().trim(), "#")) {
                    Util.showToast(getActivity(), getResources().getString(R.string.invalid_url));
                    return;
                }
                WebActivity.startActivity(getActivity(), adItem.getSkipUrl(), adItem.getName());
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_title_search_btn:
                // 标题搜索按钮
                // 隐藏键盘
                Util.hideInput(getActivity(), searchInput);
                SearchActivity.startActivity(getActivity(), searchInput.getText().toString());
                break;
//            case R.id.home_header_hot_more:
//                // 热推商品更多
//                MoreGoodsActivity.startActivityByRecommendTitle(getActivity(),
//                        headerRecommendTitle);
//                break;
            case R.id.home_tab_ad_0:
                // 新人专区
                NewExclusiveActivity.startActivity(getActivity());
                break;
            case R.id.home_tab_ad_1:
                // 消费充值
                TopUpActivity.startActivity(getActivity());
                break;
            case R.id.home_tab_ad_2:
                // 会员专卡
                if (UserInfoManager.getInstance().isValidUserInfo()) {
                    ToBeVipActivity.startActivity(getActivity());
                } else {
                    LoginActivity.startActivity(getActivity());
                }
                break;
//            case R.id.home_tab_ad_3:
//                // 蜜蜂招募
//                break;
        }
    }

//    @Subscribe
//    public void onEvent(String msg){
//        Log.i("lilijun","接收到发送过来的消息-onEvent---->>"+msg);
//    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_TIME_TICK)) {
                DLog.e("llj", "监听到一分钟时间变化！！！");
                getKDXFBannerAd();
            }
        }
    };
}
