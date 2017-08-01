package com.ttxg.fruitday.gui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import com.ttxg.fruitday.R;

/**
 * 成为会员列表Adapter
 * Created by lilijun on 2016/11/8.
 */
public class ToVipAdapter extends BaseAdapter {
    private List<String> imgUrls;

    public ToVipAdapter(List<String> imgUrls) {
        this.imgUrls = imgUrls;
    }

    @Override
    public int getCount() {
        return imgUrls.size();
    }

    @Override
    public Object getItem(int i) {
        return imgUrls.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        HolderView holderView = null;
        if (view == null) {
            holderView = new HolderView();
            view = View.inflate(viewGroup.getContext(), R.layout.to_be_vip_adapter, null);
            holderView.icon = (SimpleDraweeView) view.findViewById(R.id.to_be_vip_adapter_icon);
            view.setTag(holderView);
        }else {
            holderView = (HolderView) view.getTag();
        }
        holderView.icon.setImageURI((String) getItem(i));
        return view;
    }

    private class HolderView {
        SimpleDraweeView icon;
    }
}
