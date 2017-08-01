package com.ttxg.fruitday.gui.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.model.Shop;
import com.ttxg.fruitday.model.ShoppingCart;

/**
 * 确认订单Adapter
 * Created by lilijun on 2016/9/14.
 */
public class ConfirmOrderAdapter extends BaseExpandableListAdapter {
    private List<Shop> groupList;
    private List<List<ShoppingCart>> childList;

    public ConfirmOrderAdapter(List<Shop> groupList, List<List<ShoppingCart>>
            childList) {
        this.groupList = groupList;
        this.childList = childList;
    }

    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return childList.get(i).size();
    }

    @Override
    public Object getGroup(int i) {
        return groupList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return childList.get(i).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        GroupHolderView holderView;
        if (view == null) {
            view = View.inflate(viewGroup.getContext(), R.layout.confirm_order_group_adater, null);
            holderView = new GroupHolderView();
            holderView.shopName = (TextView) view.findViewById(R.id
                    .confirm_order_adapter_group_name);
            view.setTag(holderView);
        } else {
            holderView = (GroupHolderView) view.getTag();
        }
        Shop shop = (Shop) getGroup(i);
        holderView.shopName.setText(shop.getName());
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        ChildHolderView holderView;
        if (view == null) {
            view = View.inflate(viewGroup.getContext(), R.layout.confirm_order_child_adater, null);
            holderView = new ChildHolderView();
            holderView.icon = (SimpleDraweeView) view.findViewById(R.id.confirm_order_child_icon);
            holderView.goodsName = (TextView) view.findViewById(R.id
                    .confirm_order_child_goods_name);
            holderView.skuInfo = (TextView) view.findViewById(R.id.confirm_order_sku_info);
            holderView.price = (TextView) view.findViewById(R.id.confirm_order_child_price);
            holderView.buyNum = (TextView) view.findViewById(R.id.confirm_order_child_buy_num);

            view.setTag(holderView);
        } else {
            holderView = (ChildHolderView) view.getTag();
        }
        ShoppingCart cart = (ShoppingCart) getChild(i, i1);
        holderView.icon.setImageURI(cart.getGoodsIcon());
        holderView.goodsName.setText(cart.getGoodsName());
        holderView.price.setText(String.format(viewGroup.getResources().getString(R.string
                .format_money), cart.getBuyPrice() + ""));
        holderView.buyNum.setText(String.format(viewGroup.getResources().getString(R.string.num),
                cart.getBuyNum()));
        if(!TextUtils.isEmpty(cart.getSku1Name().trim())){
            String typeStr = cart.getSku1Name() + ": " + cart.getSku1Value();
            if(!TextUtils.isEmpty(cart.getSku2Name().trim())){
                typeStr += "    " + cart.getSku2Name() + ": " + cart.getSku2Value();
            }
            holderView.skuInfo.setText(typeStr);
            holderView.skuInfo.setVisibility(View.VISIBLE);
        }else {
            holderView.skuInfo.setVisibility(View.INVISIBLE);
        }
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }


    class GroupHolderView {
        TextView shopName;
    }

    class ChildHolderView {
        SimpleDraweeView icon;
        TextView goodsName, skuInfo,price, buyNum;
    }
}
