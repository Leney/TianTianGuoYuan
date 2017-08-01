package com.ttxg.fruitday.gui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.gui.activity.GoodsDetailActivity;
import com.ttxg.fruitday.gui.view.CenterLineTextView;
import com.ttxg.fruitday.model.PureGoods;

/**
 * 搜索列表Adapter
 * Created by lilijun on 2016/8/30.
 */
public class SearchAdapter extends BaseAdapter {
    private List<PureGoods[]> pureGoodsList;

    public SearchAdapter(List<PureGoods[]> pureGoodsList) {
        this.pureGoodsList = pureGoodsList;
    }

    @Override
    public int getCount() {
        return pureGoodsList.size();
    }

    @Override
    public Object getItem(int i) {
        return pureGoodsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        HolderView holderView = null;
        if(view ==  null){
            view = View.inflate(viewGroup.getContext(), R.layout.search_child_item,null);
            holderView = new HolderView();
            holderView.itemLay1 = (RelativeLayout) view.findViewById(R.id.search_child_lay1);
            holderView.itemLay2 = (RelativeLayout) view.findViewById(R.id.search_child_lay2);
            holderView.icon1 = (SimpleDraweeView) view.findViewById(R.id.search_chlid_icon1);
            holderView.icon2 = (SimpleDraweeView) view.findViewById(R.id.search_chlid_icon2);
            holderView.name1 = (TextView) view.findViewById(R.id.search_child_name1);
            holderView.name2 = (TextView) view.findViewById(R.id.search_child_name2);
            holderView.price1 = (TextView) view.findViewById(R.id.search_child_price1);
            holderView.price2 = (TextView) view.findViewById(R.id.search_child_price2);
            holderView.comment1 = (TextView) view.findViewById(R.id.search_child_comment1);
            holderView.comment2 = (TextView) view.findViewById(R.id.search_child_comment2);
            holderView.sales1 = (TextView) view.findViewById(R.id.search_child_sales1);
            holderView.sales2 = (TextView) view.findViewById(R.id.search_child_sales2);
            holderView.marketPrice1 = (CenterLineTextView) view.findViewById(R.id.search_child_market_price1);
            holderView.marketPrice2 = (CenterLineTextView) view.findViewById(R.id.search_child_market_price2);

            holderView.itemLay1.setOnClickListener(itemClickLinstener);
            holderView.itemLay2.setOnClickListener(itemClickLinstener);
            view.setTag(holderView);
        }else {
            holderView = (HolderView) view.getTag();
        }

        PureGoods[] childItem = (PureGoods[]) getItem(i);
        //左边子item的视图
        holderView.icon1.setImageURI(childItem[0].getIcon());
        holderView.name1.setText(childItem[0].getName());
        holderView.marketPrice1.setText(String.format(viewGroup.getResources().getString(R.string.format_money),childItem[0].getMarketPrice()+""));
        holderView.price1.setText(String.format(viewGroup.getResources().getString(R.string.format_money),childItem[0].getPrice()));
        holderView.comment1.setText(String.format(viewGroup.getResources().getString(R.string.format_comment),childItem[0].getEvaluateNums()));
        holderView.sales1.setText(String.format(viewGroup.getResources().getString(R.string.format_sales),childItem[0].getSalesNums()));
        holderView.itemLay1.setTag(childItem[0]);
        holderView.itemLay2.setVisibility(View.INVISIBLE);
        if(childItem[1] != null){
            //证明有下一个数据
            //右边子item的视图
            holderView.icon2.setImageURI(childItem[1].getIcon());
            holderView.name2.setText(childItem[1].getName());
            holderView.marketPrice2.setText(String.format(viewGroup.getResources().getString(R.string.format_money),childItem[1].getMarketPrice()+""));
            holderView.price2.setText(String.format(viewGroup.getResources().getString(R.string.format_money),childItem[1].getPrice()));
            holderView.comment2.setText(String.format(viewGroup.getResources().getString(R.string.format_comment),childItem[1].getEvaluateNums()));
            holderView.sales2.setText(String.format(viewGroup.getResources().getString(R.string.format_sales),childItem[1].getSalesNums()));
            holderView.itemLay2.setTag(childItem[1]);
            holderView.itemLay2.setVisibility(View.VISIBLE);
        }
        return view;
    }


    class HolderView{
        RelativeLayout itemLay1,itemLay2;
        SimpleDraweeView icon1,icon2;
        TextView name1,price1,comment1,sales1;
        TextView name2,price2,comment2,sales2;
        CenterLineTextView marketPrice1,marketPrice2;
    }

    private View.OnClickListener itemClickLinstener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            PureGoods goods = (PureGoods) view.getTag();
            GoodsDetailActivity.startActivity(view.getContext(),goods.getId());
        }
    };
}
