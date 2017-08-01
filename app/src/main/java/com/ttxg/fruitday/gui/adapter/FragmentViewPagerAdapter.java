package com.ttxg.fruitday.gui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * ViewPager 的 FragmentAdapter
 * Created by lilijun on 2016/9/1.
 */
public class FragmentViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;
    public FragmentViewPagerAdapter(FragmentManager fm,List<Fragment> list) {
        super(fm);
        this.fragmentList = list;
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
    }
}
