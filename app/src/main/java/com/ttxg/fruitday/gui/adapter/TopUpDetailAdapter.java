package com.ttxg.fruitday.gui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.model.TopUpInfo;

/**
 * 充值详情Adapter
 * Created by lilijun on 2016/10/24.
 */
public class TopUpDetailAdapter extends BaseAdapter {
    private List<TopUpInfo> infoList;

    public TopUpDetailAdapter(List<TopUpInfo> infoList) {
        this.infoList = infoList;
    }

    @Override
    public int getCount() {
        return infoList.size();
    }

    @Override
    public Object getItem(int i) {
        return infoList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        HolderView holderView;
        if (view == null) {
            view = View.inflate(viewGroup.getContext(), R.layout.top_up_detail_adapter, null);
            holderView = new HolderView();
            holderView.tradeNo = (TextView) view.findViewById(R.id.top_up_detail_adapter_tradeNo);
            holderView.status = (TextView) view.findViewById(R.id.top_up_detail_adapter_status);
            holderView.money = (TextView) view.findViewById(R.id.top_up_detail_adapter_money);
            holderView.date = (TextView) view.findViewById(R.id.top_up_detail_adapter_date);
            view.setTag(holderView);
        } else {
            holderView = (HolderView) view.getTag();
        }
        TopUpInfo topUpInfo = (TopUpInfo) getItem(i);
        holderView.tradeNo.setText(topUpInfo.getTradeNo());
        holderView.money.setText(String.format(viewGroup.getResources().getString(R.string
                .format_money), topUpInfo.getMoney() + ""));
        holderView.date.setText(topUpInfo.getDate());
        if (topUpInfo.getStatus() == 1) {
            // 成功
            holderView.status.setText(viewGroup.getResources().getString(R.string.success));
            holderView.status.setSelected(true);
        } else {
            // 失败
            holderView.status.setText(viewGroup.getResources().getString(R.string.failed));
            holderView.status.setSelected(false);
        }
        return view;
    }

    private class HolderView {
        TextView tradeNo, status, money, date;
    }
}
