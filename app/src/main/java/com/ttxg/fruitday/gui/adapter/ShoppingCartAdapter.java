package com.ttxg.fruitday.gui.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.callback.OnChangeShoppingCartBuyNumLinstener;
import com.ttxg.fruitday.callback.OnDeleteShoppingCartGroupLinstener;
import com.ttxg.fruitday.callback.OnDeleteShoppingCartItemLinstener;
import com.ttxg.fruitday.model.Shop;
import com.ttxg.fruitday.model.ShoppingCart;

/**
 * 购物车Adapter
 * Created by lilijun on 2016/9/14.
 */
public class ShoppingCartAdapter extends BaseExpandableListAdapter {
    private ArrayList<Shop> groupList;
    private ArrayList<ArrayList<ShoppingCart>> childList;

    /** 删除了子条目商品的监听器对象*/
    private OnDeleteShoppingCartItemLinstener deleteChildItemLinstener;

    /** 删除了整组商店的所有商品的监听器对象*/
    private OnDeleteShoppingCartGroupLinstener deleteGroupLinstner;

    /** 更改了购买数量监听器*/
    private OnChangeShoppingCartBuyNumLinstener changeShoppingCartBuyNumLinstener;

    public ShoppingCartAdapter(ArrayList<Shop> groupList, ArrayList<ArrayList<ShoppingCart>> childList) {
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
            view = View.inflate(viewGroup.getContext(), R.layout.shopping_cart_group_adater, null);
            holderView = new GroupHolderView();
            holderView.shopName = (TextView) view.findViewById(R.id.shopping_cart_group_name);
            holderView.deleteBtn = (TextView) view.findViewById(R.id.shopping_cart_group_delete);
            holderView.deleteBtn.setOnClickListener(deleteGroupClickListener);
            view.setTag(holderView);
        } else {
            holderView = (GroupHolderView) view.getTag();
        }
        Shop shop = (Shop) getGroup(i);
        holderView.shopName.setText(shop.getName());
        holderView.deleteBtn.setTag(i);
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        ChildHolderView holderView;
        if (view == null) {
            view = View.inflate(viewGroup.getContext(), R.layout.shopping_cart_child_adater, null);
            holderView = new ChildHolderView();
            holderView.icon = (SimpleDraweeView) view.findViewById(R.id.shopping_cart_child_icon);
            holderView.goodsName = (TextView) view.findViewById(R.id
                    .shopping_cart_child_goods_name);
            holderView.skuInfo = (TextView) view.findViewById(R.id.shopping_cart_sku_info);
            holderView.price = (TextView) view.findViewById(R.id.shopping_cart_child_price);
            holderView.buyNum = (TextView) view.findViewById(R.id.shopping_cart_child_buy_nums);

            holderView.reduceBtn = (ImageView) view.findViewById(R.id
                    .shopping_cart_child_reduce_btn);
            holderView.reduceBtn.setOnClickListener(reduceOnClickListener);

            holderView.addBtn = (ImageView) view.findViewById(R.id.shopping_cart_child_add_btn);
            holderView.addBtn.setOnClickListener(addOnClickListener);

            holderView.deleteBtn = (ImageView) view.findViewById(R.id.shopping_cart_child_delete_btn);
            holderView.deleteBtn.setOnClickListener(deleteChildClickListener);
            view.setTag(holderView);
        } else {
            holderView = (ChildHolderView) view.getTag();
        }
        ShoppingCart cart = (ShoppingCart) getChild(i, i1);
        holderView.icon.setImageURI(cart.getGoodsIcon());
        holderView.goodsName.setText(cart.getGoodsName());
        holderView.price.setText(String.format(viewGroup.getResources().getString(R.string
                .format_money), cart.getBuyPrice() + ""));
        holderView.buyNum.setText(cart.getBuyNum() + "");

        if(!TextUtils.isEmpty(cart.getSku1Name().trim())){
            String typeStr = cart.getSku1Name() + ": " + cart.getSku1Value();
            if(!TextUtils.isEmpty(cart.getSku2Name().trim())){
                typeStr += "  " + cart.getSku2Name() + ": " +cart.getSku2Value();
            }
            typeStr = "(" + typeStr + ")";
            holderView.skuInfo.setText(typeStr);
            holderView.skuInfo.setVisibility(View.VISIBLE);
        }else {
            holderView.skuInfo.setVisibility(View.GONE);
        }

        int[] positions = new int[2];
        positions[0] = i;
        positions[1] = i1;

        holderView.reduceBtn.setTag(positions);
        holderView.addBtn.setTag(positions);
        holderView.deleteBtn.setTag(positions);
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    /**
     * 减号点击事件
     */
    private View.OnClickListener reduceOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(changeShoppingCartBuyNumLinstener != null){
                int postions[] = (int[]) view.getTag();
                changeShoppingCartBuyNumLinstener.onBuyNumChange(false,postions[0],postions[1]);
            }
        }
    };

    /**
     * 加号点击事件
     */
    private View.OnClickListener addOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(changeShoppingCartBuyNumLinstener != null){
                int postions[] = (int[]) view.getTag();
                changeShoppingCartBuyNumLinstener.onBuyNumChange(true,postions[0],postions[1]);
            }
        }
    };

    /**
     * 子条目删除按钮点击事件
     */
    private View.OnClickListener deleteChildClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(deleteChildItemLinstener != null){
                int[] positions = (int[]) view.getTag();
                deleteChildItemLinstener.onDeleteItem(positions[0],positions[1]);
            }
//            ShoppingCart cart = (ShoppingCart) view.getTag();
//            if(childList.get(positions[0]).size() <= 1){
//                // 是这个组下面最后一个数据了，则需要将整组的数据也删除掉
//                groupList.remove(positions[0]);
//                childList.remove(positions[0]);
//            }else {
//                childList.get(positions[0]).remove(positions[1]);
//            }
//            notifyDataSetChanged();
        }
    };

    /**
     * group店铺删除按钮点击事件
     */
    private View.OnClickListener deleteGroupClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(deleteGroupLinstner != null){
                int position = (int) view.getTag();
                deleteGroupLinstner.onDeleteGroup(position);
            }
        }
    };


    /**
     * 设置删除了子条目单个商品的监听器
     * @param linstener
     */
    public void setOnDeleteChildItemLinstener(OnDeleteShoppingCartItemLinstener linstener){
        this.deleteChildItemLinstener = linstener;
    }

    /**
     * 设置删除了子条目单个商品的监听器
     * @param linstener
     */
    public void setOnDeleteGroupLinstener(OnDeleteShoppingCartGroupLinstener linstener){
        this.deleteGroupLinstner = linstener;
    }

    /**
     * 设置更改了购买商品的数量监听器
     * @param linstener
     */
    public void setOnChangedBuyNumLinstener(OnChangeShoppingCartBuyNumLinstener linstener){
        this.changeShoppingCartBuyNumLinstener = linstener;
    }



    class GroupHolderView {
        TextView shopName, deleteBtn;
    }

    class ChildHolderView {
        SimpleDraweeView icon;
        TextView goodsName,skuInfo, price, buyNum;
        ImageView reduceBtn, addBtn, deleteBtn;
    }
}
