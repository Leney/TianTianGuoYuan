package com.ttxg.fruitday.gui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.model.GoodsShelf;

/**
 * 货架适配器
 * Created by yb on 2016/10/8.
 */
public class GoodsShelfAdapter extends BaseAdapter {
    private List<GoodsShelf> shelfList;

    private OnDeleteShelfListener onDeleteShelfListener;

    private OnEditShelfListener onEditShelfListener;

    public GoodsShelfAdapter(List<GoodsShelf> shelfList) {
        this.shelfList = shelfList;
    }

    public void setOnDeleteShelfListener(OnDeleteShelfListener onDeleteShelfListener) {
        this.onDeleteShelfListener = onDeleteShelfListener;
    }

    public void setOnEditShelfListener(OnEditShelfListener onEditShelfListener) {
        this.onEditShelfListener = onEditShelfListener;
    }

    @Override
    public int getCount() {
        return shelfList.size();
    }

    @Override
    public Object getItem(int i) {
        return shelfList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        HolderView holderView;
        if (view == null) {
            view = View.inflate(viewGroup.getContext(), R.layout.goods_shelf_adapter, null);
            holderView = new HolderView();
            holderView.name = (TextView) view.findViewById(R.id.goods_shelf_adapter_name);
            holderView.goodsNum = (TextView) view.findViewById(R.id.goods_shelf_adapter_goods_nums);
            holderView.delete = (ImageView) view.findViewById(R.id.goods_shelf_adapter_delete_btn);
            holderView.edit = (ImageView) view.findViewById(R.id.goods_shelf_adapter_edit_btn);

            holderView.delete.setOnClickListener(deleteShelfListener);
            holderView.edit.setOnClickListener(editShelfListener);
            view.setTag(holderView);
        } else {
            holderView = (HolderView) view.getTag();
        }
        GoodsShelf shelf = (GoodsShelf) getItem(i);
        holderView.name.setText(shelf.getName());
        holderView.goodsNum.setText(String.format(viewGroup.getResources().getString(R.string
                .format_goods_shelf_nums), shelf.getGoodsNum()));
        holderView.delete.setVisibility(shelf.isDefault() ? View.GONE : View.VISIBLE);

        holderView.delete.setTag(shelf);
        holderView.edit.setTag(shelf);
        return view;
    }


    private class HolderView {
        TextView name, goodsNum;
        ImageView delete, edit;
    }


    /**
     * 删除货架的点击事件对象
     */
    private View.OnClickListener deleteShelfListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (onDeleteShelfListener != null) {
                onDeleteShelfListener.onDelete((GoodsShelf) view.getTag());
            }
        }
    };

    /**
     * 编辑货架的点击事件对象
     */
    private View.OnClickListener editShelfListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(onEditShelfListener != null){
                onEditShelfListener.onEdit((GoodsShelf) view.getTag());
            }
        }
    };


    public interface OnDeleteShelfListener {
        void onDelete(GoodsShelf goodsShelf);
    }

    public interface OnEditShelfListener {
        void onEdit(GoodsShelf goodsShelf);
    }

}
