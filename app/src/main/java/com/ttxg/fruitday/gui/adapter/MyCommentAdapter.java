package com.ttxg.fruitday.gui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.model.MyComment;

/**
 * 我的评论列表Adapter
 * Created by lilijun on 2016/10/13.
 */
public class MyCommentAdapter extends BaseAdapter {
    private List<MyComment> commentList;

    public MyCommentAdapter(List<MyComment> commentList) {
        this.commentList = commentList;
    }

    @Override
    public int getCount() {
        return commentList.size();
    }

    @Override
    public Object getItem(int i) {
        return commentList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        HolderView holderView = null;
        if(view == null){
            view = View.inflate(viewGroup.getContext(), R.layout.my_comment_adapter,null);
            holderView = new HolderView();
            holderView.userName = (TextView) view.findViewById(R.id.my_comment_adapter_user_name);
            holderView.star = (RatingBar) view.findViewById(R.id.my_comment_adapter_star);
            holderView.goodsIcon = (SimpleDraweeView) view.findViewById(R.id.my_comment_adapter_goods_icon);
            holderView.goodsName = (TextView) view.findViewById(R.id.my_comment_adapter_goods_name);
            holderView.content = (TextView) view.findViewById(R.id.my_comment_adapter_content);
            holderView.date = (TextView) view.findViewById(R.id.my_comment_adapter_date);
            view.setTag(holderView);
        }else {
            holderView = (HolderView) view.getTag();
        }
        MyComment comment = (MyComment) getItem(i);
        holderView.userName.setText(comment.getUserName());
        holderView.star.setRating(comment.getStar());
        holderView.goodsIcon.setImageURI(comment.getGoodsIcon());
        holderView.goodsName.setText(comment.getGoodsName());
        holderView.content.setText(comment.getContent());
        holderView.date.setText(comment.getCommentDate());
        return view;
    }


    private class HolderView {
        TextView userName;
        RatingBar star;
        SimpleDraweeView goodsIcon;
        TextView goodsName;
        TextView content;
        TextView date;
    }
}
