package com.ttxg.fruitday.gui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.gui.adapter.FragmentViewPagerAdapter;
import com.ttxg.fruitday.gui.fragment.OrderAllListFragment;
import com.ttxg.fruitday.gui.fragment.OrderListFragment;

/**
 * 订单信息列表界面
 * Created by yb on 2016/9/26.
 */
public class OrderListActivity extends BaseActivity implements View.OnClickListener {
    /**
     * [0]=全部，[1]=待付款，[2]=待发货，[3]=待收货，[4]=待评价
     */
    private TextView[] tabs = new TextView[5];
    private ViewPager viewPager;
    private FragmentViewPagerAdapter adapter;
    private List<Fragment> fragmentList;
    /**
     * 当前显示的tab类型  默认显示 “全部” 0=全部,1=待付款，2=待发货，3=待签收，4=交易完成
     */
    private int curStatus = 0;

    /**
     * 搜索框
     */
    private EditText searchInput;

    private OrderAllListFragment allFragment;
    private OrderListFragment waitPayFragment, waitSendFragment, waitTakeFragment,
            waitCommentFragment;

    @Override
    protected void initView() {

        curStatus = getIntent().getIntExtra("status", 0);

        setCenterView(R.layout.activity_order_list);


        searchInput = (EditText) findViewById(R.id.order_list_search_input);
        ImageView deleteBtn = (ImageView) findViewById(R.id.order_list_search_delete);
        deleteBtn.setOnClickListener(this);
        ImageView searchBtn = (ImageView) findViewById(R.id.order_list_search_btn);
        searchBtn.setOnClickListener(this);


        tabs[0] = (TextView) findViewById(R.id.order_list_all_table);
        tabs[1] = (TextView) findViewById(R.id.order_list_wait_pay_table);
        tabs[2] = (TextView) findViewById(R.id.order_list_wait_send_table);
        tabs[3] = (TextView) findViewById(R.id.order_list_wait_take_table);
        tabs[4] = (TextView) findViewById(R.id.order_list_wait_comment_table);
        tabs[0].setOnClickListener(this);
        tabs[1].setOnClickListener(this);
        tabs[2].setOnClickListener(this);
        tabs[3].setOnClickListener(this);
        tabs[4].setOnClickListener(this);
        viewPager = (ViewPager) findViewById(R.id.order_list_viewpager);
        viewPager.setOffscreenPageLimit(2);
        fragmentList = new ArrayList<>();
        allFragment = new OrderAllListFragment();
        allFragment.init();
        waitPayFragment = new OrderListFragment();
        waitPayFragment.init(1);
        waitSendFragment = new OrderListFragment();
        waitSendFragment.init(2);
        waitTakeFragment = new OrderListFragment();
        waitTakeFragment.init(3);
        waitCommentFragment = new OrderListFragment();
        waitCommentFragment.init(4);
        fragmentList.add(allFragment);
        fragmentList.add(waitPayFragment);
        fragmentList.add(waitSendFragment);
        fragmentList.add(waitTakeFragment);
        fragmentList.add(waitCommentFragment);
        adapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(curStatus);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int
                    positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                curStatus = position;
                setCurStatus();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setCurStatus();
        showCenterView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        allFragment.onActivityResult(requestCode,resultCode,data);
        waitPayFragment.onActivityResult(requestCode,resultCode,data);
        waitSendFragment.onActivityResult(requestCode,resultCode,data);
        waitTakeFragment.onActivityResult(requestCode,resultCode,data);
        waitCommentFragment.onActivityResult(requestCode,resultCode,data);
    }

    /**
     * 设置当前状态参数
     */
    private void setCurStatus() {
        switch (curStatus) {
            case 0:
                // 全部
                setTitleName(getResources().getString(R.string.all));
                tabs[0].setSelected(true);
                tabs[1].setSelected(false);
                tabs[2].setSelected(false);
                tabs[3].setSelected(false);
                tabs[4].setSelected(false);
                break;
            case 1:
                // 待付款
                setTitleName(getResources().getString(R.string.not_pay));
                tabs[0].setSelected(false);
                tabs[1].setSelected(true);
                tabs[2].setSelected(false);
                tabs[3].setSelected(false);
                tabs[4].setSelected(false);
                break;
            case 2:
                // 待发货
                setTitleName(getResources().getString(R.string.not_send_goods));
                tabs[0].setSelected(false);
                tabs[1].setSelected(false);
                tabs[2].setSelected(true);
                tabs[3].setSelected(false);
                tabs[4].setSelected(false);
                break;
            case 3:
                // 待收货
                setTitleName(getResources().getString(R.string.not_take_goods));
                tabs[0].setSelected(false);
                tabs[1].setSelected(false);
                tabs[2].setSelected(false);
                tabs[3].setSelected(true);
                tabs[4].setSelected(false);
                break;
            case 4:
                // 待评价
                setTitleName(getResources().getString(R.string.not_comment));
                tabs[0].setSelected(false);
                tabs[1].setSelected(false);
                tabs[2].setSelected(false);
                tabs[3].setSelected(false);
                tabs[4].setSelected(true);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.order_list_all_table:
                // 全部
                curStatus = 0;
                setCurStatus();
                viewPager.setCurrentItem(curStatus);
                break;
            case R.id.order_list_wait_pay_table:
                // 待付款
                curStatus = 1;
                setCurStatus();
                viewPager.setCurrentItem(curStatus);
                break;
            case R.id.order_list_wait_send_table:
                // 待发货
                curStatus = 2;
                setCurStatus();
                viewPager.setCurrentItem(curStatus);
                break;
            case R.id.order_list_wait_take_table:
                // 待收货
                curStatus = 3;
                setCurStatus();
                viewPager.setCurrentItem(curStatus);
                break;
            case R.id.order_list_wait_comment_table:
                // 待评价
                curStatus = 4;
                setCurStatus();
                viewPager.setCurrentItem(curStatus);
                break;
            case R.id.order_list_search_delete:
                // 搜索框内的删除按钮
                searchInput.setText("");
                break;
            case R.id.order_list_search_btn:
                // 搜索按钮
                searchOrder();
                break;
        }
    }

    /**
     * 搜索相应tab的订单
     */
    private void searchOrder() {
        String searchStr = searchInput.getText().toString().trim();
//        if (TextUtils.isEmpty(searchStr)) {
//            Util.showToast(OrderListActivity.this, getResources().getString(R.string
//                    .input_search_text));
//            return;
//        }
        switch (curStatus) {
            case 0:
                // 全部
                allFragment.searchOrder(searchStr);
                break;
            case 1:
                // 待付款
                waitPayFragment.searchOrder(searchStr);
                break;
            case 2:
                // 待发货
                waitSendFragment.searchOrder(searchStr);
                break;
            case 3:
                // 待收货
                waitTakeFragment.searchOrder(searchStr);
                break;
            case 4:
                // 待评价
                waitCommentFragment.searchOrder(searchStr);
                break;
        }
    }

    /**
     * 启动订单列表界面
     *
     * @param context
     * @param status  跳转过来最先显示的fragment 0=全部,1=待付款，2=待发货，3=待签收，4=交易完成
     */
    public static void startActivity(Context context, int status) {
        Intent intent = new Intent(context, OrderListActivity.class);
        intent.putExtra("status", status);
        context.startActivity(intent);
    }
}
