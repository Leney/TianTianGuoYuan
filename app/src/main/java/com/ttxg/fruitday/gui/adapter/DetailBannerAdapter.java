package com.ttxg.fruitday.gui.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;

import java.util.List;

import com.ttxg.fruitday.util.log.DLog;

/**
 * 商品详情 轮播Adapter
 * Created by yb on 2016/8/19.
 */
public class DetailBannerAdapter extends LoopPagerAdapter {
    private List<String> bannerItems;

    public DetailBannerAdapter(RollPagerView viewPager, List<String> list) {
        super(viewPager);
        this.bannerItems = list;
    }

    @Override
    public View getView(ViewGroup container, int position) {
        String imgUrl = bannerItems.get(position);
        DLog.i("lilijun","imgUrl------>>>"+imgUrl);
        SimpleDraweeView bannerImg = new SimpleDraweeView(container.getContext());
        GenericDraweeHierarchy hierarchy = bannerImg.getHierarchy();
        hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.CENTER_INSIDE);
//        bannerImg.setLayoutParams(
//                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        bannerImg.setImageURI(imgUrl);
        return bannerImg;
    }

    @Override
    public int getRealCount() {
        return bannerItems.size();
    }
}
