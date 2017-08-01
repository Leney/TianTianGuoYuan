package com.ttxg.fruitday.gui.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.gui.activity.AboutActivity;
import com.ttxg.fruitday.gui.activity.AddressListActivity;
import com.ttxg.fruitday.gui.activity.BindingPhoneActivity;
import com.ttxg.fruitday.gui.activity.LoginActivity;
import com.ttxg.fruitday.gui.activity.ModifyPersonageInfoActivity;
import com.ttxg.fruitday.gui.activity.MyIntegralActivity;
import com.ttxg.fruitday.gui.activity.OrderListActivity;
import com.ttxg.fruitday.gui.activity.RemainingActivity;
import com.ttxg.fruitday.gui.activity.SafetySettingActivity;
import com.ttxg.fruitday.gui.activity.ToBeVipActivity;
import com.ttxg.fruitday.gui.adapter.UserCenterAdapter;
import com.ttxg.fruitday.gui.view.BaseTitleView;
import com.ttxg.fruitday.gui.view.WraperGridView;
import com.ttxg.fruitday.manager.UserInfoManager;
import com.ttxg.fruitday.model.UserCenterMenu;
import com.ttxg.fruitday.model.UserInfo;
import com.ttxg.fruitday.util.Constants;
import com.ttxg.fruitday.util.ParseUtil;
import com.ttxg.fruitday.util.Util;
import com.ttxg.fruitday.util.log.DLog;


/**
 * 用户中心Tab Fragment
 * Created by lilijun on 2016/8/12.
 */
public class TabUserCenterFragment extends BaseFragment implements View.OnClickListener,
        AdapterView.OnItemClickListener {
    /**
     * 根据用户类型获取到的用户中心中的菜单信息
     */
    private final String GET_USER_CENTER = "user/center";
    /**
     * 查询的各类订单数量信息
     */
    private final String GET_ANY_NUM = "user/getDataStatistics";
    /**
     * 查询用户是否绑定了手机号
     */
    private final String QUERY_BINDING_PHONE = "safe/bind_phone";
    private BaseTitleView titleView;
    private WraperGridView gridView;
    private UserCenterAdapter adapter;
    private List<UserCenterMenu> menuList;
    private View headerView;
    private SimpleDraweeView userIcon;
    private TextView userName;
    private ImageView vipIcon;
    private LinearLayout userInfoLay;
//    /**
//     * 今日收入，累计收入，今日徒弟数
//     */
//    private TextView todayIncome, totalIncome, apprenticeNum;
    /**
     * 待付款部分，待发货部分，待收货部分，待评价部分
     */
    private FrameLayout payLay, sendGoodsLay, takeGoodsLay, commentLay;
    /**
     * 待付款数量，待发货数量，待收货数量，待评价数量
     */
    private TextView payNum, sendGoodsNum, takeGoodsNum, commentNum;

    private ProgressDialog progressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }


    @Override
    protected void initView(RelativeLayout view) {
        titleView = new BaseTitleView(getActivity());
        titleView.setTitleName(getResources().getString(R.string.tab5));
        titleView.setBackImgVisible(false);
        titleView.setRightImgRes(R.drawable.setting_icon);
        titleView.setRightImgClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 跳转到设置界面
                AboutActivity.startActivity(getActivity());
            }
        });
        setAddView(titleView, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams
                .MATCH_PARENT, (int) getResources().getDimension(R.dimen.title_height)));

        menuList = new ArrayList<>();

        setCenterView(R.layout.fragment_user_center);
        gridView = (WraperGridView) view.findViewById(R.id.user_center_gridview);
        adapter = new UserCenterAdapter(menuList);

        headerView = View.inflate(getActivity(), R.layout.user_center_header, null);
        userIcon = (SimpleDraweeView) headerView.findViewById(R.id.user_center_icon);
        userName = (TextView) headerView.findViewById(R.id.user_center_nick_name);
        vipIcon = (ImageView) headerView.findViewById(R.id.user_center_vip_icon);
        vipIcon.setVisibility(View.INVISIBLE);
        userInfoLay = (LinearLayout) headerView.findViewById(R.id.user_center_info_lay);
        userInfoLay.setOnClickListener(this);
//        todayIncome = (TextView) headerView.findViewById(R.id.user_center_today_income);
//        totalIncome = (TextView) headerView.findViewById(R.id.user_center_total_income);
//        apprenticeNum = (TextView) headerView.findViewById(R.id.user_center_today_apprentice_num);
        payLay = (FrameLayout) headerView.findViewById(R.id.user_center_pay_lay);
        payLay.setOnClickListener(this);
        sendGoodsLay = (FrameLayout) headerView.findViewById(R.id.user_center_send_goods_lay);
        sendGoodsLay.setOnClickListener(this);
        takeGoodsLay = (FrameLayout) headerView.findViewById(R.id.user_center_take_goods_lay);
        takeGoodsLay.setOnClickListener(this);
        commentLay = (FrameLayout) headerView.findViewById(R.id.user_center_comment_lay);
        commentLay.setOnClickListener(this);
        payNum = (TextView) headerView.findViewById(R.id.user_center_pay_num);
        sendGoodsNum = (TextView) headerView.findViewById(R.id.user_center_send_goods_num);
        takeGoodsNum = (TextView) headerView.findViewById(R.id.user_center_take_goods_num);
        commentNum = (TextView) headerView.findViewById(R.id.user_center_comment_num);

        gridView.addHeaderView(headerView);
        gridView.setAdapter(adapter);
        gridView.setNextPageViewVisible(false);
        gridView.setOnItemClickListener(this);

        loadDataGet(GET_USER_CENTER, null);
    }

    @Override
    protected void onLoadDataSuccess(String tag, JSONObject resultObject) {
        super.onLoadDataSuccess(tag, resultObject);
        if (TextUtils.equals(tag, GET_USER_CENTER)) {
            // 获取菜单信息数据成功
            ParseUtil.parseUserCenterMenuResult(resultObject, menuList);
            if (!menuList.isEmpty()) {
                adapter.notifyDataSetChanged();
                showCenterView();
                // 去获取用户各种信息数据
                loadDataGet(GET_ANY_NUM, null);
            } else {
                showErrorView();
            }
        } else if (TextUtils.equals(tag, GET_ANY_NUM)) {
            // 获取各类订单数量成功
            ParseUtil.parseUserInfo(resultObject);
            setUserInfoData();
        } else if (TextUtils.equals(tag, QUERY_BINDING_PHONE)) {
            // 查询用户是否绑定手机成功
            progressDialog.dismiss();
            String phone = "";
            try {
                phone = resultObject.getString("validateMobile");
                UserInfoManager.getInstance().getUserInfo().setPhone(phone);
            } catch (Exception e) {
                DLog.e("TabUserCenterFragment", "查询绑定手机解析出现异常#exception：\n", e);
            }
            if (TextUtils.isEmpty(phone)) {
                // 没有绑定手机号
                Util.showToast(getActivity(), getResources().getString(R.string
                        .not_binding2));
                BindingPhoneActivity.startActivity(getActivity());
            } else {
                // 有绑定手机号，跳转到安全设置界面
                SafetySettingActivity.startActivity(getActivity());
            }
        }
    }

    @Override
    protected void onLoadDataError(String tag, int errorCode, String msg) {
        super.onLoadDataError(tag, errorCode, msg);
        if (TextUtils.equals(tag, GET_USER_CENTER)) {
            // 获取菜单信息失败
            showErrorView();
            Util.showErrorMessage(getActivity(), msg, getResources().getString(R.string
                    .get_user_info_failed));
        } else if (TextUtils.equals(tag, GET_ANY_NUM)) {
            // 获取各类订单数量失败
            DLog.i("lilijun", "重新获取数量信息失败！！！！");
        } else if (TextUtils.equals(tag, QUERY_BINDING_PHONE)) {
            // 查询用户是否绑定手机失败
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
        loadDataGet(GET_USER_CENTER, null);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe
    public void onEventMessage(String msg) {
//        if (TextUtils.equals(msg, Constants.CANCEL_ORDER_SUCCESS) || TextUtils.equals(msg,
//                Constants.CANCEL_ORDER_SUCCESS_FROM_ALL) || TextUtils.equals(msg,
//                Constants.SUBMIT_ORDER_SUCCESS)) {
//            // 去获取用户各种信息数据
//            loadDataGet(GET_ANY_NUM, null);
//        }
        if (TextUtils.equals(msg, Constants.NEED_REFRESH_ANY_NUM)) {
//            try {
//                // 延迟1s去获取数据
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            DLog.i("lilijun", "重新去获取订单数量等信息！！！");
            // 去获取用户各种信息数据
            loadDataGet(GET_ANY_NUM, null);
        } else if (TextUtils.equals(msg, Constants.REFRESH_USER_INFO_VIEW) || TextUtils.equals(msg,
                Constants.SUBMIT_ORDER_SUCCESS)) {
            // 重新刷新用户视图
            setUserInfoData();
        }
    }

    private void setUserInfoData() {
        UserInfo userInfo = UserInfoManager.getInstance().getUserInfo();
        userIcon.setImageURI(userInfo.getIcon());
        // 优先显示name,其次nickName,最后userId
//        userName.setText(
//                TextUtils.isEmpty(userInfo.getName())
//                        ? TextUtils.isEmpty(userInfo.getNickName())
//                        ? String.format(getResources().getString(R.string.format_welcome),
//                        userInfo.getId())
//                        : String.format(getResources().getString(R.string.format_welcome),
//                        userInfo.getNickName())
//                        : String.format(getResources().getString(R.string.format_welcome),
//                        userInfo.getName()));
        userName.setText(
                TextUtils.isEmpty(userInfo.getNickName())
                        ? TextUtils.isEmpty(userInfo.getName())
                        ? String.format(getResources().getString(R.string.format_welcome),
                        userInfo.getId())
                        : String.format(getResources().getString(R.string.format_welcome),
                        userInfo.getName())
                        : String.format(getResources().getString(R.string.format_welcome),
                        userInfo.getNickName()));

        // 设置是否显示vip icon
        vipIcon.setVisibility(userInfo.getVipLevel() < 1 ? View.INVISIBLE : View.VISIBLE);


//        todayIncome.setText(String.format(getResources().getString(R.string.format_today_income),
//                userInfo.getTodayIncome() + ""));
//        totalIncome.setText(String.format(getResources().getString(R.string.format_total_income),
//                userInfo.getTotalIncome() + ""));
//        apprenticeNum.setText(String.format(getResources().getString(R.string
//                .format_today_apprentice_num), userInfo.getTodayApprenticeNum() + ""));

        if (userInfo.getNoPayOrderNum() <= 9) {
            // 数量小于9
            payNum.setText(userInfo.getNoPayOrderNum() + "");
            payNum.setBackgroundResource(R.drawable.circle_red_shape);
        } else {
            // 数量大于9
            payNum.setText("");
            payNum.setBackgroundResource(R.drawable.more_num_point);
        }
        payNum.setVisibility(userInfo.getNoPayOrderNum() > 0 ? View.VISIBLE : View.GONE);

        DLog.i("lilijun", "待发货数量------>>>" + userInfo.getPaiedOrderNum());
        if (userInfo.getPaiedOrderNum() <= 9) {
            // 数量小于9
            sendGoodsNum.setText(userInfo.getPaiedOrderNum() + "");
            sendGoodsNum.setBackgroundResource(R.drawable.circle_red_shape);
        } else {
            // 数量大于9
            sendGoodsNum.setText("");
            sendGoodsNum.setBackgroundResource(R.drawable.more_num_point);
        }
        sendGoodsNum.setVisibility(userInfo.getPaiedOrderNum() > 0 ? View.VISIBLE : View.GONE);

        if (userInfo.getUnTakeOverNum() <= 9) {
            // 数量小于9
            takeGoodsNum.setText(userInfo.getUnTakeOverNum() + "");
            takeGoodsNum.setBackgroundResource(R.drawable.circle_red_shape);
        } else {
            // 数量大于9
            takeGoodsNum.setText("");
            takeGoodsNum.setBackgroundResource(R.drawable.more_num_point);
        }
        takeGoodsNum.setVisibility(userInfo.getUnTakeOverNum() > 0 ? View.VISIBLE : View.GONE);

        if (userInfo.getUnCommentNum() <= 9) {
            // 数量小于9
            commentNum.setText(userInfo.getUnCommentNum() + "");
            commentNum.setBackgroundResource(R.drawable.circle_red_shape);
        } else {
            // 数量大于9
            commentNum.setText("");
            commentNum.setBackgroundResource(R.drawable.more_num_point);
        }
        commentNum.setVisibility(userInfo.getUnCommentNum() > 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ModifyPersonageInfoActivity.MODIFY_USER_INFO_SUCCESS_RESULT_CODE) {
            // 修改个人资料成功
            setUserInfoData();
        }
    }

    @Override
    public void onClick(View view) {
        if (!UserInfoManager.getInstance().isValidUserInfo()) {
            Util.showToast(getActivity(), getResources().getString(R.string.login_time_out_or_no));
            LoginActivity.startActivity(getActivity());
            return;
        }
        switch (view.getId()) {
            case R.id.user_center_info_lay:
                // 用户信息显示部分(头像，昵称)  点击进入个人信息设置界面
                ModifyPersonageInfoActivity.startActivity(getActivity());
                break;
            case R.id.user_center_pay_lay:
                // 待付款
                OrderListActivity.startActivity(getActivity(), 1);
                break;
            case R.id.user_center_send_goods_lay:
                // 待发货
                OrderListActivity.startActivity(getActivity(), 2);
                break;
            case R.id.user_center_take_goods_lay:
                // 待收货
                OrderListActivity.startActivity(getActivity(), 3);
                break;
            case R.id.user_center_comment_lay:
                // 待评价
                OrderListActivity.startActivity(getActivity(), 4);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switchMenu(menuList.get(i));
    }

    /**
     * 处理根据不同tag跳转到不同的界面
     *
     * @param menu
     */
    private void switchMenu(UserCenterMenu menu) {
        if (!UserInfoManager.getInstance().isValidUserInfo()) {
            Util.showToast(getActivity(), getResources().getString(R.string.login_time_out_or_no));
            LoginActivity.startActivity(getActivity());
            return;
        }
        switch (menu.getTag()) {
            case UserCenterMenu.USER_BALANCE:
                // 余额
                RemainingActivity.startActivity(getActivity());
                break;
            case UserCenterMenu.USER_POINT:
                // 我的积分
                MyIntegralActivity.startActivity(getActivity());
                break;
//            case UserCenterMenu.USER_PRODUCT:
//                // 商品管理
//                GoodsManagerListActivity.startActivity(getActivity());
//                break;
            case UserCenterMenu.USER_SAFE:
                // 安全设置
                if (TextUtils.isEmpty(UserInfoManager.getInstance().getUserInfo().getPhone())) {
                    // 没有绑定手机，查询是否绑定了手机
                    loadDataGet(QUERY_BINDING_PHONE, null);
                    progressDialog = Util.showLoadingDialog(getActivity(), progressDialog);
                } else {
                    // 绑定了手机，跳转到安全设置界面
                    SafetySettingActivity.startActivity(getActivity());
                }
                break;
            case UserCenterMenu.USER_ADDRESS:
                // 地址管理
                AddressListActivity.startActivity(getActivity());
                break;
            case UserCenterMenu.USER_VIP:
                // 成为会员
                ToBeVipActivity.startActivity(getActivity());
                break;
            case UserCenterMenu.USER_COUPONS:
                // 优惠卷
                break;
            case UserCenterMenu.USER_SERVICE:
                // 客服
                try {
                    Uri uri = Uri.parse("tel:4000596069");
                    Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                    startActivity(intent);
                } catch (Exception e) {
                    Util.showToast(getActivity(), getResources().getString(R.string.can_not_call));
                }
                break;
//            case UserCenterMenu.USER_COMMENT:
//                // 我的评论
//                MyCommentActivity.startActivity(getActivity());
//                break;
            default:
                Util.showToast(getActivity(), getResources().getString(R.string.no_open));
                break;
        }
    }
}
