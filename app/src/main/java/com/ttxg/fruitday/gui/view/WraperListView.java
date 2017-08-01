package com.ttxg.fruitday.gui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ttxg.fruitday.R;


/**
 * 简单封装ListView
 * Created by lilijun on 2016/8/26.
 */
public class WraperListView extends ListView {
    /**
     * footerView 显示区域
     */
    private RelativeLayout footerLay;
    /**
     * 下一页的视图
     */
    private TextView nextPageView;
    public WraperListView(Context context) {
        super(context);
        init(context);
    }

    public WraperListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WraperListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public WraperListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context){
        // 禁止列表到底或者在顶部能继续拉动
        setOverScrollMode(View.OVER_SCROLL_NEVER);
        setDividerHeight(0);
        setDivider(getResources().getDrawable(R.drawable.transparent));
        setSelector(R.drawable.transparent);

        footerLay = new RelativeLayout(context);
        LayoutParams footerParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        footerLay.setLayoutParams(footerParams);

        nextPageView = new TextView(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                (int)getResources().getDimension(R.dimen.comment_footer_height));
        nextPageView.setLayoutParams(params);
        nextPageView.setGravity(Gravity.CENTER);
        nextPageView.setText(context.getResources().getString(R.string.load_next_page));
        footerLay.addView(nextPageView);
        addFooterView(footerLay);
    }

//    /**
//     * 设置footerView 是否显示
//     *
//     * @param visible
//     */
//    public void setFooterLayVisible(boolean visible) {
//        footerLay.setVisibility(visible ? View.VISIBLE : View.GONE);
//    }

    /**
     * 设置添加FooterView
     * @param view
     */
    public void setFooterView(View view) {
        footerLay.addView(view, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
    }

    /**
     * 设置加载下一页数据视图是否显示
     * @param visible
     */
    public void setNextPageViewVisible(boolean visible){
        nextPageView.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    /**
     * 设置加载下一页数据的点击回调事件
     * @param listener
     */
    public void setOnNextPageClickListener(OnClickListener listener){
        nextPageView.setOnClickListener(listener);
    }

    /**
     * 设置下一页加载失败的视图
     */
    public void setNextPageViewLoadFailed(){
        nextPageView.setText(getResources().getString(R.string.load_next_error));
        nextPageView.setClickable(true);
    }

    /**
     * 设置下一页正在加载的视图
     */
    public void setNextPageLoading(){
        nextPageView.setText(getResources().getString(R.string.load_next_loading));
        nextPageView.setClickable(false);
    }

    /**
     * 设置下一页 加载下一页 的视图
     */
    public void setNextPageLoadNext(){
        nextPageView.setText(getResources().getString(R.string.load_next_page));
        nextPageView.setClickable(true);
    }
}
