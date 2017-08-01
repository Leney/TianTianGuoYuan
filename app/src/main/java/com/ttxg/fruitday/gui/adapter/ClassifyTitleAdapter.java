package com.ttxg.fruitday.gui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.model.Classify;

/**
 * 总分类列表标题adapter
 * Created by lilijun on 2016/8/29.
 */
public class ClassifyTitleAdapter extends BaseAdapter {
    private List<Classify> classifyList;

    public ClassifyTitleAdapter(List<Classify> classifyList) {
        this.classifyList = classifyList;
    }

    @Override
    public int getCount() {
        return classifyList.size();
    }

    @Override
    public Object getItem(int i) {
        return classifyList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        HolderView holderView = null;
        if(view == null){
            view = View.inflate(viewGroup.getContext(), R.layout.classify_title_adapter,null);
            holderView = new HolderView();
            holderView.titleLay = (LinearLayout) view.findViewById(R.id.classify_title_lay);
            holderView.selectLine = view.findViewById(R.id.classify_title_select_view);
            holderView.rightLine = view.findViewById(R.id.classify_title_right_line);
            holderView.name = (TextView) view.findViewById(R.id.classify_title_name);
            view.setTag(holderView);
        }else {
            holderView = (HolderView) view.getTag();
        }
        Classify classify = (Classify) getItem(i);
        if(classify.isSelected()){
            holderView.selectLine.setVisibility(View.VISIBLE);
            holderView.rightLine.setVisibility(View.INVISIBLE);
            holderView.name.setSelected(true);
            holderView.titleLay.setSelected(true);
        }else {
            holderView.selectLine.setVisibility(View.INVISIBLE);
            holderView.rightLine.setVisibility(View.VISIBLE);
            holderView.name.setSelected(false);
            holderView.titleLay.setSelected(false);
        }
        holderView.name.setText(classify.getName());
        return view;
    }

    class HolderView{
        View selectLine,rightLine;
        TextView name;
        LinearLayout titleLay;
    }
}
