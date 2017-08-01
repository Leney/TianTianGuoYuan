package com.ttxg.fruitday.gui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ttxg.fruitday.R;


/**
 * 再包一层可添加FooterView 和 HeaderView的GridView
 * Created by yb on 2016/9/1.
 */
public class WraperGridView extends GridViewWithHeaderAndFooter {
    /**
     * footerView 显示区域
     */
    private RelativeLayout footerLay;
    /**
     * 下一页的视图
     */
    private TextView nextPageView;

    private float downX, downY;

//    /**
//     * 差值
//     */
//    private int delValue = 20;
//
//    /**
//     * 标记是否需要拦截父类touch
//     */
//    private boolean flag = false;

    public WraperGridView(Context context) {
        super(context);
        init(context);
    }

    public WraperGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WraperGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        // 禁止列表到底或者在顶部能继续拉动
        setOverScrollMode(View.OVER_SCROLL_NEVER);
        setSelector(R.drawable.transparent);
        footerLay = new RelativeLayout(context);
        LayoutParams footerParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        footerLay.setLayoutParams(footerParams);

        nextPageView = new TextView(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                (int) getResources().getDimension(R.dimen.comment_footer_height));
        nextPageView.setLayoutParams(params);
        nextPageView.setGravity(Gravity.CENTER);
        nextPageView.setText(context.getResources().getString(R.string.load_next_page));
        footerLay.addView(nextPageView);
        addFooterView(footerLay);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                downX = ev.getX();
//                downY = ev.getY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                float moveX = ev.getX();
//                float moveY = ev.getY();
//
//                float delX = Math.abs(downX - moveX);
//                float delY = Math.abs(downY - moveY);
//                if (Math.abs(downX - moveX) > 20) {
//                    // 左右滑动 允许父类拦截了
//                    getParent().requestDisallowInterceptTouchEvent(false);
////                    flag = true;
//                } else {
//                    // 上下滑动 不允许父类拦截
//                    getParent().requestDisallowInterceptTouchEvent(true);
////                    flag = false;
//                }
//
////                if (Math.abs(downX - moveX) > Math.abs(downY - moveY)) {
////                    // 左右滑动 允许父类拦截了
////                    getParent().requestDisallowInterceptTouchEvent(false);
////                } else {
////                    // 上下滑动 不允许父类拦截
////                    getParent().requestDisallowInterceptTouchEvent(true);
////                }
//                break;
////            case MotionEvent.ACTION_UP:
////                if(flag){
////                    // 不允许父类拦截,自己处理
////                    getParent().requestDisallowInterceptTouchEvent(true);
////                }else {
////                    // 允许父类拦截，父类处理
////                    getParent().requestDisallowInterceptTouchEvent(false);
////                }
////                flag =false;
////                break;
//        }
        // 上下滑动 不允许父类拦截
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 设置添加FooterView
     *
     * @param view
     */
    public void setFooterView(View view) {
        footerLay.addView(view, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
    }

    /**
     * 设置加载下一页数据视图是否显示
     *
     * @param visible
     */
    public void setNextPageViewVisible(boolean visible) {
        nextPageView.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    /**
     * 设置加载下一页数据的点击回调事件
     *
     * @param listener
     */
    public void setOnNextPageClickListener(OnClickListener listener) {
        nextPageView.setOnClickListener(listener);
    }

    /**
     * 设置下一页加载失败的视图
     */
    public void setNextPageViewLoadFailed() {
        nextPageView.setText(getResources().getString(R.string.load_next_error));
    }

    /**
     * 设置下一页正在加载的视图
     */
    public void setNextPageLoading() {
        nextPageView.setText(getResources().getString(R.string.load_next_loading));
    }

    /**
     * 设置下一页 加载下一页 的视图
     */
    public void setNextPageLoadNext() {
        nextPageView.setText(getResources().getString(R.string.load_next_page));
    }
}
