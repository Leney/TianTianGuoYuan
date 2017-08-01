package com.ttxg.fruitday.gui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.gui.activity.GoodsDetailActivity;
import com.ttxg.fruitday.gui.view.CenterLineTextView;
import com.ttxg.fruitday.gui.view.SimpleDraweeViewLab;
import com.ttxg.fruitday.model.PureGoods;

/**
 * 搜索列表Adapter
 * Created by lilijun on 2016/8/30.
 */
public class GoodsGridViewAdapter extends BaseAdapter {
    private List<PureGoods> pureGoodsList;

    public GoodsGridViewAdapter(List<PureGoods> pureGoodsList) {
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
        if (view == null) {
            view = View.inflate(viewGroup.getContext(), R.layout.gridview_child_item, null);
            holderView = new HolderView();
            holderView.itemLay = (RelativeLayout) view.findViewById(R.id.search_child_lay1);
            holderView.icon = (SimpleDraweeViewLab) view.findViewById(R.id.search_chlid_icon1);
//            holderView.vipLab = (ImageView) view.findViewById(R.id.search_child_vip_lab);
            holderView.name = (TextView) view.findViewById(R.id.search_child_name1);
            holderView.price1 = (TextView) view.findViewById(R.id.search_child_price1);
            holderView.price2 = (TextView) view.findViewById(R.id.search_child_price2);
            holderView.comment = (TextView) view.findViewById(R.id.search_child_comment1);
            holderView.sales = (TextView) view.findViewById(R.id.search_child_sales1);
            holderView.marketPrice1 = (CenterLineTextView) view.findViewById(R.id
                    .search_child_market_price1);
            holderView.marketPrice2 = (CenterLineTextView) view.findViewById(R.id
                    .search_child_market_price2);
            holderView.itemLay.setOnClickListener(itemClickLinstener);
            view.setTag(holderView);
        } else {
            holderView = (HolderView) view.getTag();
        }

        PureGoods childItem = (PureGoods) getItem(i);
        holderView.icon.setImageURI(childItem.getIcon());
        holderView.name.setText(childItem.getName());
//        holderView.marketPrice1.setText(String.format(viewGroup.getResources().getString(R.string
//                .format_money), childItem.getMarketPrice() + ""));
//        holderView.price1.setText(String.format(viewGroup.getResources().getString(R.string
//                .format_money), childItem.getPrice()));
        holderView.comment.setText(String.format(viewGroup.getResources().getString(R.string
                .format_comment), childItem.getEvaluateNums()));
        holderView.sales.setText(String.format(viewGroup.getResources().getString(R.string
                .format_sales), childItem.getSalesNums()));

        if (childItem.getSalesLevel() == 1) {
            // 售卖新人价，显示市场价和新人价
            holderView.price1.setVisibility(View.INVISIBLE);
            holderView.price2.setVisibility(View.VISIBLE);
            holderView.marketPrice1.setVisibility(View.INVISIBLE);
            holderView.marketPrice2.setVisibility(View.INVISIBLE);
//            holderView.vipLab.setVisibility(View.INVISIBLE);
            holderView.icon.showLab(false);

            holderView.price2.setText(String.format(viewGroup.getResources()
                    .getString(R.string.format_new_money), childItem.getNewbiePrice() + ""));
            holderView.marketPrice1.setText(String.format(viewGroup.getResources()
                    .getString(R.string.format_money), childItem.getMarketPrice() + ""));
        } else if (childItem.getSalesLevel() == 2) {
            // 只显示售卖会员价
            holderView.price1.setVisibility(View.VISIBLE);
            holderView.price2.setVisibility(View.INVISIBLE);
            holderView.marketPrice1.setVisibility(View.INVISIBLE);
            holderView.marketPrice2.setVisibility(View.INVISIBLE);
//            holderView.vipLab.setVisibility(View.VISIBLE);
            holderView.icon.showLab(true);

            holderView.price1.setText(String.format(viewGroup.getResources()
                    .getString(R.string.format_money), childItem.getMemberPrice() + ""));
        } else if (childItem.getSalesLevel() == 3) {
            // 显示非会员价和会员价
            holderView.price1.setVisibility(View.VISIBLE);
            holderView.price2.setVisibility(View.VISIBLE);
            holderView.marketPrice1.setVisibility(View.INVISIBLE);
            holderView.marketPrice2.setVisibility(View.INVISIBLE);
//            holderView.vipLab.setVisibility(View.INVISIBLE);
            holderView.icon.showLab(false);

            holderView.price1.setText(String.format(viewGroup.getResources()
                    .getString(R.string.format_money), childItem.getPrice() + ""));
            holderView.price2.setText(String.format(viewGroup.getResources()
                    .getString(R.string.format_vip_money), childItem.getMemberPrice() + ""));
        } else {
            // 显示非会员价和市场价
            holderView.price1.setVisibility(View.VISIBLE);
            holderView.price2.setVisibility(View.INVISIBLE);
            holderView.marketPrice1.setVisibility(View.INVISIBLE);
            holderView.marketPrice2.setVisibility(View.VISIBLE);
//            holderView.vipLab.setVisibility(View.INVISIBLE);
            holderView.icon.showLab(false);

            holderView.price1.setText(String.format(viewGroup.getResources()
                    .getString(R.string.format_money), childItem.getPrice()));
            holderView.marketPrice2.setText(String.format(viewGroup.getResources()
                    .getString(R.string.format_money), childItem.getMarketPrice() + ""));
        }
        holderView.itemLay.setTag(R.id.search_child_lay1,childItem);
        return view;
    }


    class HolderView {
        RelativeLayout itemLay;
        SimpleDraweeViewLab icon;
//        ImageView vipLab;
        TextView name, price1, price2, comment, sales;
        CenterLineTextView marketPrice1, marketPrice2;
    }

    private View.OnClickListener itemClickLinstener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            PureGoods goods = (PureGoods) view.getTag(R.id.search_child_lay1);
            GoodsDetailActivity.startActivity(view.getContext(), goods.getId());
        }
    };
}
