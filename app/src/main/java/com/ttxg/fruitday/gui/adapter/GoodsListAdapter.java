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
import com.ttxg.fruitday.util.log.DLog;

/**
 * 纯列表数据的Adapter
 * Created by lilijun on 2016/12/9.
 */
public class GoodsListAdapter extends BaseAdapter {
    private List<PureGoods> goodsList;

    public GoodsListAdapter(List<PureGoods> goodsList) {
        this.goodsList = goodsList;
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
        HolderView holderView = null;
        if (view == null) {
            view = View.inflate(viewGroup.getContext(), R.layout.hot_goods_item, null);
            holderView = new HolderView(view);
            view.setTag(holderView);
        } else {
            holderView = (HolderView) view.getTag();
        }
        PureGoods goods = (PureGoods) getItem(i);
        holderView.hotImg.setImageURI(goods.getIcon());
        holderView.hotName.setText(goods.getName());
        if (goods.getSalesLevel() == 1) {
            // 售卖新人价，显示市场价和新人价
            holderView.leftPrice.setVisibility(View.INVISIBLE);
            holderView.rightPrice.setVisibility(View.VISIBLE);
            holderView.leftMarketPrice.setVisibility(View.VISIBLE);
            holderView.rightMarketPrice.setVisibility(View.INVISIBLE);
            holderView.hotImg.showLab(false);

            holderView.rightPrice.setText(String.format(viewGroup.getResources()
                    .getString(R.string
                            .format_new_money), goods.getNewbiePrice() + ""));
            holderView.leftMarketPrice.setText(String.format(viewGroup.getResources()
                    .getString(R.string
                            .format_money), goods.getMarketPrice() + ""));
        } else if (goods.getSalesLevel() == 2) {
            // 只显示售卖会员价
            holderView.leftPrice.setVisibility(View.VISIBLE);
            holderView.rightPrice.setVisibility(View.GONE);
            holderView.leftMarketPrice.setVisibility(View.GONE);
            holderView.rightMarketPrice.setVisibility(View.GONE);
            holderView.hotImg.showLab(true);

            holderView.leftPrice.setText(String.format(viewGroup.getResources()
                    .getString(R.string
                            .format_vip_money), goods.getMemberPrice() + ""));
        } else if (goods.getSalesLevel() == 3) {
            // 显示非会员价和会员价
            holderView.leftPrice.setVisibility(View.VISIBLE);
            holderView.rightPrice.setVisibility(View.VISIBLE);
            holderView.leftMarketPrice.setVisibility(View.GONE);
            holderView.rightMarketPrice.setVisibility(View.GONE);
            holderView.hotImg.showLab(false);

            holderView.leftPrice.setText(String.format(viewGroup.getResources()
                    .getString(R.string
                            .format_money), goods.getPrice() + ""));
            holderView.rightPrice.setText(String.format(viewGroup.getResources()
                    .getString(R.string
                            .format_vip_money), goods.getMemberPrice() + ""));
        } else {
            // 显示非会员价和市场价
            holderView.leftPrice.setVisibility(View.VISIBLE);
            holderView.rightPrice.setVisibility(View.GONE);
            holderView.leftMarketPrice.setVisibility(View.GONE);
            holderView.rightMarketPrice.setVisibility(View.VISIBLE);
            holderView.hotImg.showLab(false);

            holderView.leftPrice.setText(String.format(viewGroup.getResources()
                    .getString(R.string
                            .format_money), goods.getPrice()));
            holderView.rightMarketPrice.setText(String.format(viewGroup.getResources()
                    .getString(R.string
                            .format_money), goods.getMarketPrice() + ""));
        }
        holderView.hotLay.setTag(goods);
        return view;
    }


    class HolderView {
        public RelativeLayout hotLay;
        public SimpleDraweeViewLab hotImg;
        public TextView hotName;
        public TextView leftPrice;
        public TextView rightPrice;
        public CenterLineTextView leftMarketPrice;
        public CenterLineTextView rightMarketPrice;

        public void init(View baseView) {
            hotLay = (RelativeLayout) baseView.findViewById(R.id.hot_item_lay);
            hotImg = (SimpleDraweeViewLab) baseView.findViewById(R.id.hot_item_img);
            hotName = (TextView) baseView.findViewById(R.id.hot_item_goods_name);
            leftPrice = (TextView) baseView.findViewById(R.id.hot_item_price_left);
            rightPrice = (TextView) baseView.findViewById(R.id.hot_item_price_right);
            leftMarketPrice = (CenterLineTextView) baseView.findViewById(R.id
                    .hot_item_market_price_left);
            rightMarketPrice = (CenterLineTextView) baseView.findViewById(R.id
                    .hot_item_market_price_right);
            hotLay.setOnClickListener(childItemClickListener);
        }

        public HolderView(View baseView) {
            init(baseView);
        }
    }

    /**
     * 商品类型item 点击事件
     */
    private View.OnClickListener childItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            PureGoods goods = (PureGoods) view.getTag();
            DLog.i("lilijun", "商品类型item点击，goods.getName()---->>>" + goods.getName());
            DLog.i("lilijun", "商品类型item点击，goods.getId()---->>>" + goods.getId());
            GoodsDetailActivity.startActivity(view.getContext(), goods.getId());
        }
    };
}
