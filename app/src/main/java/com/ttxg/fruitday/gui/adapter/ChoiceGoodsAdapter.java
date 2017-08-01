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
 * 选择商品列表Adapter
 * Created by lilijun on 2016/9/29.
 */
public class ChoiceGoodsAdapter extends BaseAdapter {
    private List<MyGoods> goodsList;

    private OnAddToStoreListener onAddToStoreListener;

    public ChoiceGoodsAdapter(List<MyGoods> goodsList) {
        this.goodsList = goodsList;
    }

    public void setOnAddToStoreListener(OnAddToStoreListener onAddToStoreListener) {
        this.onAddToStoreListener = onAddToStoreListener;
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
            view = View.inflate(viewGroup.getContext(), R.layout.choice_goods_list_adapter, null);
            holderView = new HolderView();
            holderView.icon = (SimpleDraweeView) view.findViewById(R.id.choice_goods_adapter_icon);
            holderView.name = (TextView) view.findViewById(R.id.choice_goods_adapter_name);
            holderView.price = (TextView) view.findViewById(R.id.choice_goods_adapter_price);
            holderView.marketPrice = (TextView) view.findViewById(R.id
                    .choice_goods_adapter_old_price);
            holderView.stockNum = (TextView) view.findViewById(R.id
                    .choice_goods_adapter_stock_num);
            holderView.salesNum = (TextView) view.findViewById(R.id
                    .choice_goods_adapter_sales_num);
            holderView.addToStore = (TextView) view.findViewById(R.id
                    .choice_goods_adapter_store_name);
            holderView.checkDetail = (TextView) view.findViewById(R.id
                    .choice_goods_adapter_go_detail_btn);
            holderView.addToStore = (TextView) view.findViewById(R.id
                    .choice_goods_adapter_add_to_store_btn);
            holderView.storeName = (TextView) view.findViewById(R.id
                    .choice_goods_adapter_store_name);
            holderView.checkDetail.setOnClickListener(goodsDetailsListener);
            holderView.addToStore.setOnClickListener(addToStoreListener);
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
        holderView.storeName.setText(goods.getStoreName());


        holderView.checkDetail.setTag(goods);
        holderView.addToStore.setTag(goods);
        return view;
    }

    class HolderView {
        SimpleDraweeView icon;
        TextView name, price, marketPrice, stockNum, salesNum, storeName;
        TextView checkDetail, addToStore;
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
     * 添加到小店的点击事件
     */
    private View.OnClickListener addToStoreListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (onAddToStoreListener != null) {
                onAddToStoreListener.onAdd((MyGoods) view.getTag());
            }
        }
    };

    /**
     * 加入小店的回调接口
     */
    public interface OnAddToStoreListener {
        void onAdd(MyGoods goods);
    }
}
