package com.ttxg.fruitday.gui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.model.SpinnerMode;

/**
 * 下拉菜单Adapter
 * Created by lilijun on 2016/9/1.
 */
public class SpinnerAdapter extends BaseAdapter {
    private List<SpinnerMode> modeList;

    public SpinnerAdapter(List<SpinnerMode> modeList) {
        this.modeList = modeList;
    }

    @Override
    public int getCount() {
        return modeList.size();
    }

    @Override
    public Object getItem(int i) {
        return modeList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView name;
        if(view == null){
            view = View.inflate(viewGroup.getContext(), R.layout.spinner_item_lay,null);
            name = (TextView) view.findViewById(R.id.spinner_content);
            view.setTag(name);
        }else {
            name = (TextView) view.getTag();
        }
        name.setText(((SpinnerMode)getItem(i)).getName());
        return view;
    }
}
