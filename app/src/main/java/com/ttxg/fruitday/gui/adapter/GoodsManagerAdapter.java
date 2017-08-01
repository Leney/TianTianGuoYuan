package com.ttxg.fruitday.gui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.gui.activity.GoodsDetailActivity;
import com.ttxg.fruitday.model.MyGoods;

/**
 * 商品管理列表Adapter
 * Created by lilijun on 2016/9/29.
 */
public class GoodsManagerAdapter extends BaseAdapter {
    private List<MyGoods> goodsList;

    private OnChangeGoodsShelfListener onChangeGoodsShelfListener;

    public GoodsManagerAdapter(List<MyGoods> goodsList) {
        this.goodsList = goodsList;
    }


    public void setOnChangeGoodsShelfListener(OnChangeGoodsShelfListener
                                                      onChangeGoodsShelfListener) {
        this.onChangeGoodsShelfListener = onChangeGoodsShelfListener;
    }

    @Override
    public int getCount() {
        return goodsList.size();
    }

    @Override
    public Object getItem(int i) {
        return goodsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        HolderView holderView;
        if (view == null) {
            view = View.inflate(viewGroup.getContext(), R.layout.goods_manager_list_adapter, null);
            holderView = new HolderView();
            holderView.icon = (SimpleDraweeView) view.findViewById(R.id.goods_manager_adapter_icon);
            holderView.name = (TextView) view.findViewById(R.id.goods_manager_adapter_name);
            holderView.price = (TextView) view.findViewById(R.id.goods_manager_adapter_price);
            holderView.marketPrice = (TextView) view.findViewById(R.id
                    .goods_manager_adapter_old_price);
            holderView.stockNum = (TextView) view.findViewById(R.id
                    .goods_manager_adapter_stock_num);
            holderView.salesNum = (TextView) view.findViewById(R.id
                    .goods_manager_adapter_sales_num);
            holderView.checkDetal = (TextView) view.findViewById(R.id.goods_manager_adapter_detail);
            holderView.changeGoodsShelf = (TextView) view.findViewById(R.id
                    .goods_manager_adapter_change_goods_shelf);
            holderView.shareGoods = (TextView) view.findViewById(R.id
                    .goods_manager_adapter_share_goods);

            holderView.checkDetal.setOnClickListener(goodsDetailsListener);
            holderView.changeGoodsShelf.setOnClickListener(changeGoodsShelfListener);
            holderView.shareGoods.setOnClickListener(shareGoodsListener);
            view.setTag(holderView);
        } else {
            holderView = (HolderView) view.getTag();
        }
        MyGoods goods = (MyGoods) getItem(i);
        holderView.icon.setImageURI(goods.getIcon());
        holderView.name.setText(goods.getName());
        holderView.price.setText(String.format(viewGroup.getResources().getString(R.string
                .format_money), goods.getPrice() + ""));
        holderView.marketPrice.setText(String.format(viewGroup.getResources().getString(R.string
                .format_money), goods.getMarketPrice() + ""));
        holderView.stockNum.setText(String.format(viewGroup.getResources().getString(R.string
                .format_stock), goods.getStockNums() + ""));
        holderView.salesNum.setText(String.format(viewGroup.getResources().getString(R.string
                .format_sales), goods.getSalesNums() + ""));

        holderView.checkDetal.setTag(goods);
        holderView.changeGoodsShelf.setTag(goods);
        holderView.shareGoods.setTag(goods);
        return view;
    }

    class HolderView {
        SimpleDraweeView icon;
        TextView name, price, marketPrice, stockNum, salesNum;
        TextView checkDetal, changeGoodsShelf, shareGoods;
    }

    /**
     * 查看商品详情点击事件
     */
    private View.OnClickListener goodsDetailsListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            MyGoods goods = (MyGoods) view.getTag();
            GoodsDetailActivity.startActivity(view.getContext(), goods.getId(), goods.getStoreId());
        }
    };

    /**
     * 更改货架点击事件
     */
    private View.OnClickListener changeGoodsShelfListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (onChangeGoodsShelfListener != null) {
                onChangeGoodsShelfListener.onChange((MyGoods) view.getTag());
            }
        }
    };

    /**
     * 分享商品点击事件
     */
    private View.OnClickListener shareGoodsListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // TODO share goods
            MyGoods goods = (MyGoods) view.getTag();
        }
    };

    /**
     * 改变商品货架的回调接口
     */
    public interface OnChangeGoodsShelfListener {
        void onChange(MyGoods goods);
    }


}
