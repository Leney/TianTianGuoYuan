package com.ttxg.fruitday.gui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;

import com.ttxg.fruitday.util.log.DLog;


/**
 * 详情评论特定ListView
 * Created by lilijun on 2016/8/26.
 */
public class DetailCommentListView extends WraperListView implements AbsListView.OnScrollListener {
    private float startY = 0;//按下时y值
    private int mTouchSlop;//系统值

    /**
     * 标识是否滑动到顶部
     */
    private boolean isTop;

    /**
     * 标识是否滑动到底部
     */
    private boolean isBottom;

    public DetailCommentListView(Context context) {
        super(context);
        setOnScrollListener(this);
    }

    public DetailCommentListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnScrollListener(this);
    }

    public DetailCommentListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnScrollListener(this);
    }

    public DetailCommentListView(Context context, AttributeSet attrs, int defStyleAttr, int
            defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setOnScrollListener(this);
    }


    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int
            totalItemCount) {
        if (firstVisibleItem == 0) {
            View firstVisibleItemView = getChildAt(0);
            if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
                DLog.i("##### 滚动到顶部 #####");
                isTop = true;
                return;
            }
        } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
            View lastVisibleItemView = getChildAt(getChildCount() - 1);
            if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == getHeight()) {
                DLog.i("##### 滚动到底部 ######");
                isBottom = true;
                return;
            }
        }
        isTop = false;
        isBottom = false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startY = ev.getY();
                // 自己处理，不交给父类容器处理
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(ev.getY() - startY) > mTouchSlop) {
                    if (ev.getY() - startY >= 0) {
                        // 下滑
                        DLog.i("下滑!!");
                        if (isTop) {
                            // 自己不处理，交给父类容器处理
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }else {
                            // 自己处理，不交给父类容器处理
                            getParent().requestDisallowInterceptTouchEvent(true);
                        }
                    } else {
                        // 上滑
                        DLog.i("上滑---!!");
                        if(isBottom){
                            // 自己不处理，交给父类容器处理
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }else {
                            // 自己处理，不交给父类容器处理
                            getParent().requestDisallowInterceptTouchEvent(true);
                        }
                    }
                }
                startY = ev.getY();
                break;
        }
        return super.onTouchEvent(ev);
    }
}
