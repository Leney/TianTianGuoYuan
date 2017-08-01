package com.ttxg.fruitday.gui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.model.Comment;

/**
 * 详情评论列表Adapter
 * Created by lilijun on 2016/8/25.
 */
public class CommentAdapter extends BaseAdapter {
    private List<Comment> commentList;

    public CommentAdapter(List<Comment> commentList) {
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
            view = View.inflate(viewGroup.getContext(), R.layout.comment_item,null);
            holderView = new HolderView();
            holderView.userName = (TextView) view.findViewById(R.id.comment_user_name);
            holderView.content = (TextView) view.findViewById(R.id.comment_content);
            holderView.date = (TextView) view.findViewById(R.id.comment_date);
            holderView.star = (RatingBar) view.findViewById(R.id.comment_star);
            view.setTag(holderView);
        }else {
            holderView = (HolderView) view.getTag();
        }
        Comment comment = (Comment) getItem(i);
        holderView.userName.setText(comment.getUserName());
        holderView.content.setText(comment.getContent());
        holderView.date.setText(comment.getDate());
        holderView.star.setRating(comment.getStar());
        return view;
    }

    class HolderView{
        TextView userName,content,date;
        RatingBar star;
    }
}
