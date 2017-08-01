package com.ttxg.fruitday.gui.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;

import java.util.List;

import com.ttxg.fruitday.model.AdItem;

/**
 * Banner 轮播Adapter
 * Created by yb on 2016/8/19.
 */
public class BannerAdapter extends LoopPagerAdapter {
    private List<AdItem> bannerItems;

    public BannerAdapter(RollPagerView viewPager, List<AdItem> list) {
        super(viewPager);
        this.bannerItems = list;
    }

    @Override
    public View getView(ViewGroup container, int position) {
        AdItem item = bannerItems.get(position);
        SimpleDraweeView bannerImg = new SimpleDraweeView(container.getContext());
        GenericDraweeHierarchy hierarchy = bannerImg.getHierarchy();
        hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.FIT_XY);
        bannerImg.setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        bannerImg.setImageURI(item.getIcon());
        return bannerImg;
    }

    @Override
    public int getRealCount() {
        return bannerItems.size();
    }
}
