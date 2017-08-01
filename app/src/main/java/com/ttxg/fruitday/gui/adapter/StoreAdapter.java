package com.ttxg.fruitday.gui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.model.Store;

/**
 * 云店铺Adapter
 * Created by lilijun on 2016/9/1.
 */
public class StoreAdapter extends BaseAdapter {
    List<Store> storeList;

    public StoreAdapter(List<Store> storeList) {
        this.storeList = storeList;
    }

    @Override
    public int getCount() {
        return storeList.size();
    }

    @Override
    public Object getItem(int i) {
        return storeList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        HolderView holderView = null;
        if(view == null){
            view = View.inflate(viewGroup.getContext(), R.layout.store_item_adapter,null);
            holderView = new HolderView();
            holderView.logo = (SimpleDraweeView) view.findViewById(R.id.store_item_logo);
            holderView.name = (TextView) view.findViewById(R.id.store_item_name);
            holderView.classifyName = (TextView) view.findViewById(R.id.store_item_clssify_name);
            holderView.tag = (TextView) view.findViewById(R.id.store_item_tag);
            holderView.distance = (TextView) view.findViewById(R.id.store_item_distance);
            holderView.sendType = (TextView) view.findViewById(R.id.store_item_send_type);
            view.setTag(holderView);
        }else {
            holderView = (HolderView) view.getTag();
        }
        Store store = (Store) getItem(i);
        holderView.logo.setImageURI(store.getLogo());
        holderView.name.setText(store.getName());
        holderView.classifyName.setText(store.getClassifyName());
        holderView.tag.setText(store.getTag());
        holderView.distance.setText(store.getDistanceKm());
        if(store.getSendType() == 1){
            holderView.sendType.setVisibility(View.VISIBLE);
        }else {
            holderView.sendType.setVisibility(View.INVISIBLE);
        }
        return view;
    }

    class HolderView{
        SimpleDraweeView logo;
        TextView name,classifyName,tag,distance,sendType;
    }
}
