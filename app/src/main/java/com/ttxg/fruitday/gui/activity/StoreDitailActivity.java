package com.ttxg.fruitday.gui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.gui.adapter.FragmentViewPagerAdapter;
import com.ttxg.fruitday.gui.fragment.StoreGoodsListFragment;
import com.ttxg.fruitday.model.StoreDetail;
import com.ttxg.fruitday.util.ParseUtil;

/**
 * 云店详情界面
 * Created by lilijun on 2016/9/6.
 */
public class StoreDitailActivity extends BaseActivity implements View.OnClickListener {
    private String GET_STORE_DETAIL = "cloud/store/";
    private int userId = -1;
    private SimpleDraweeView storeLogo;
    private TextView storeName;
    private TextView telephone;
    /**
     * 位置
     */
    private TextView adress;
    /**
     * 本店说明
     */
    private TextView discrible;
    /**
     * 门店服务标签
     */
    private TextView labService;
    /**
     * 周边闪送标签
     */
    private TextView labSendAround;
    /**
     * viewpager tab布局部分
     */
    private LinearLayout tabLay1, tabLay2, tabLay3, tabLay4;
    /**
     * viewpager tab名称
     */
    private TextView tabName1, tabName2, tabName3, tabName4;
    /**
     * viewpager tab指示部分
     */
    private ImageView tabPoint1, tabPoint2, tabPoint3, tabPoint4;
    private ViewPager viewPager;
    private FragmentViewPagerAdapter adapter;
    private List<Fragment> fragmentList;
    private StoreDetail storeDetail;

    private StoreGoodsListFragment postHomeFragment;
    private StoreGoodsListFragment sendAroundFragment;
    private StoreGoodsListFragment storeServiceFragment;
    private StoreGoodsListFragment friendCommendFragment;

    @Override
    protected void initView() {
        setTitleName(getIntent().getStringExtra("name"));
        fragmentList = new ArrayList<>();
        setCenterView(R.layout.activity_store_detail);
        storeLogo = (SimpleDraweeView) findViewById(R.id.store_detail_logo);
        storeName = (TextView) findViewById(R.id.store_detail_name);
        telephone = (TextView) findViewById(R.id.store_detail_tel);
        adress = (TextView) findViewById(R.id.store_detail_location);
        discrible = (TextView) findViewById(R.id.store_detail_describle);
        labService = (TextView) findViewById(R.id.store_detail_lab1);
        labSendAround = (TextView) findViewById(R.id.store_detail_lab2);

        tabLay1 = (LinearLayout) findViewById(R.id.store_detail_tab_lay1);
        tabLay1.setOnClickListener(this);
        tabLay2 = (LinearLayout) findViewById(R.id.store_detail_tab_lay2);
        tabLay2.setOnClickListener(this);
        tabLay3 = (LinearLayout) findViewById(R.id.store_detail_tab_lay3);
        tabLay3.setOnClickListener(this);
        tabLay4 = (LinearLayout) findViewById(R.id.store_detail_tab_lay4);
        tabLay4.setOnClickListener(this);

        tabName1 = (TextView) findViewById(R.id.store_detail_tab_name1);
        tabName2 = (TextView) findViewById(R.id.store_detail_tab_name2);
        tabName3 = (TextView) findViewById(R.id.store_detail_tab_name3);
        tabName4 = (TextView) findViewById(R.id.store_detail_tab_name4);

        tabPoint1 = (ImageView) findViewById(R.id.store_detail_tab_point1);
        tabPoint2 = (ImageView) findViewById(R.id.store_detail_tab_point2);
        tabPoint3 = (ImageView) findViewById(R.id.store_detail_tab_point3);
        tabPoint4 = (ImageView) findViewById(R.id.store_detail_tab_point4);
        viewPager = (ViewPager) findViewById(R.id.store_detail_viewpager);
        adapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), fragmentList);

        userId = getIntent().getIntExtra("userId", -1);
        GET_STORE_DETAIL += userId;

        postHomeFragment = new StoreGoodsListFragment();
        postHomeFragment.init(userId,1);

        sendAroundFragment = new StoreGoodsListFragment();
        sendAroundFragment.init(userId,2);

        storeServiceFragment = new StoreGoodsListFragment();
        storeServiceFragment.init(userId,3);

        friendCommendFragment = new StoreGoodsListFragment();
        friendCommendFragment.init(userId,4);

        fragmentList.add(postHomeFragment);
        fragmentList.add(sendAroundFragment);
        fragmentList.add(storeServiceFragment);
        fragmentList.add(friendCommendFragment);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(pageChangeListener);

        loadDataGet(GET_STORE_DETAIL, null);
    }

    @Override
    protected void onLoadDataSuccess(String tag, JSONObject resultObject) {
        super.onLoadDataSuccess(tag, resultObject);
        storeDetail = ParseUtil.parseStoreDetail(resultObject);
        if (storeDetail != null) {
            setStoreDetailInfo();
            showCenterView();
        } else {
            showErrorView();
        }
    }

    @Override
    protected void onLoadDataError(String tag, int errorCode, String msg) {
        super.onLoadDataError(tag, errorCode, msg);
    }

    @Override
    protected void tryAgain() {
        super.tryAgain();
        loadDataGet(GET_STORE_DETAIL, null);
    }

    /**
     * viewpage 界面切换监听
     */
    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            setViewPagerTab(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    private void setStoreDetailInfo() {
        setTitleName(storeDetail.getName());
        storeLogo.setImageURI(storeDetail.getLogo());
        storeName.setText(storeDetail.getName());
        telephone.setText(storeDetail.getPhone());
        adress.setText(storeDetail.getAdress());
        discrible.setText(String.format(getResources().getString(R.string.format_store_discrible), storeDetail.getDiscrible()));
        labSendAround.setVisibility(storeDetail.isSendAroundLab() ? View.VISIBLE : View.GONE);
        labService.setVisibility(storeDetail.isStoreServicelab() ? View.VISIBLE : View.GONE);
        setViewPagerTab(0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.store_detail_tab_lay1:
                // 邮递到家
                setViewPagerTab(0);
                viewPager.setCurrentItem(0);
                break;
            case R.id.store_detail_tab_lay2:
                // 周边闪送
                setViewPagerTab(1);
                viewPager.setCurrentItem(1);
                break;
            case R.id.store_detail_tab_lay3:
                // 门店服务
                setViewPagerTab(2);
                viewPager.setCurrentItem(2);
                break;
            case R.id.store_detail_tab_lay4:
                // 友情推荐
                setViewPagerTab(3);
                viewPager.setCurrentItem(3);
                break;
        }
    }

    private void setViewPagerTab(int position) {
        switch (position) {
            case 0:
                // 邮递到家
                tabName1.setSelected(true);
                tabPoint1.setVisibility(View.VISIBLE);

                tabName2.setSelected(false);
                tabPoint2.setVisibility(View.INVISIBLE);

                tabName3.setSelected(false);
                tabPoint3.setVisibility(View.INVISIBLE);

                tabName4.setSelected(false);
                tabPoint4.setVisibility(View.INVISIBLE);
                break;
            case 1:
                // 周边闪送
                tabName1.setSelected(false);
                tabPoint1.setVisibility(View.INVISIBLE);

                tabName2.setSelected(true);
                tabPoint2.setVisibility(View.VISIBLE);

                tabName3.setSelected(false);
                tabPoint3.setVisibility(View.INVISIBLE);

                tabName4.setSelected(false);
                tabPoint4.setVisibility(View.INVISIBLE);
                break;
            case 2:
                // 门店服务
                tabName1.setSelected(false);
                tabPoint1.setVisibility(View.INVISIBLE);

                tabName2.setSelected(false);
                tabPoint2.setVisibility(View.INVISIBLE);

                tabName3.setSelected(true);
                tabPoint3.setVisibility(View.VISIBLE);

                tabName4.setSelected(false);
                tabPoint4.setVisibility(View.INVISIBLE);
                break;
            case 3:
                // 友情推荐
                tabName1.setSelected(false);
                tabPoint1.setVisibility(View.INVISIBLE);

                tabName2.setSelected(false);
                tabPoint2.setVisibility(View.INVISIBLE);

                tabName3.setSelected(false);
                tabPoint3.setVisibility(View.INVISIBLE);

                tabName4.setSelected(true);
                tabPoint4.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * @param context
     * @param userId  云店拥有账户的用户id
     */
    public static void startActivity(Context context, int userId, String name) {
        Intent intent = new Intent(context, StoreDitailActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("userId", userId);
        context.startActivity(intent);
    }
}
