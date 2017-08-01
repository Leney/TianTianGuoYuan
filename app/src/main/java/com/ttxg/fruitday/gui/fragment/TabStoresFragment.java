package com.ttxg.fruitday.gui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.gui.activity.LoginActivity;
import com.ttxg.fruitday.gui.adapter.FragmentViewPagerAdapter;
import com.ttxg.fruitday.manager.UserInfoManager;

/**
 * 云店Fragment
 * Created by lilijun on 2016/9/1.
 */
public class TabStoresFragment extends Fragment implements View.OnClickListener{
    private ViewPager viewPager;
    private FragmentPagerAdapter pagerAdapter;
    private List<Fragment> fragmentList;

    private SimpleDraweeView userIcon;
    /** 云店铺，供应商店铺*/
    private TextView cloudStoreText,prividerStoreText;
    /** 搜索按钮*/
    private ImageView searchImg;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentList = new ArrayList<>();
        fragmentList.add(new CloudStoreFragment());
        fragmentList.add(new SupplierStoreFragment());
        pagerAdapter = new FragmentViewPagerAdapter(getActivity().getSupportFragmentManager(),fragmentList);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View baseView = inflater.inflate(R.layout.fragment_stroes,container,false);
        userIcon = (SimpleDraweeView) baseView.findViewById(R.id.stores_fragment_user_icon);
        userIcon.setOnClickListener(this);
        cloudStoreText = (TextView) baseView.findViewById(R.id.stores_fragment_cloud_store_text);
        cloudStoreText.setOnClickListener(this);
        prividerStoreText = (TextView) baseView.findViewById(R.id.stores_fragment_provider_store_text);
        prividerStoreText.setOnClickListener(this);
        searchImg = (ImageView) baseView.findViewById(R.id.stores_fragment_search_img);
        viewPager = (ViewPager) baseView.findViewById(R.id.stores_fragment_viewpager);
        viewPager.setAdapter(pagerAdapter);
        cloudStoreText.setSelected(true);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0){
                    cloudStoreText.setSelected(true);
                    prividerStoreText.setSelected(false);
                }else if(position == 1){
                    prividerStoreText.setSelected(true);
                    cloudStoreText.setSelected(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return baseView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.stores_fragment_cloud_store_text:
                // 云店铺 title
                cloudStoreText.setSelected(true);
                prividerStoreText.setSelected(false);
                viewPager.setCurrentItem(0);
                break;
            case R.id.stores_fragment_provider_store_text:
                // 供应商店铺 title
                prividerStoreText.setSelected(true);
                cloudStoreText.setSelected(false);
                viewPager.setCurrentItem(1);
                break;
            case R.id.stores_fragment_user_icon:
                // 我的小店图标点击事件
                if(!UserInfoManager.getInstance().isValidUserInfo()){
                    LoginActivity.startActivity(getActivity());
                }
                break;
        }
    }
}
