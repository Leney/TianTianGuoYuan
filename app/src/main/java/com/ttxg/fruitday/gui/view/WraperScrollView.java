package com.ttxg.fruitday.gui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * ScrollView封装类
 * Created by yb on 2016/11/17.
 */
public class WraperScrollView extends ScrollView {
    /**
     * 当前滚动到的Y值
     */
    private int curScrollY = 0;
    /**
     * 标识是否需要重设滚动距离
     */
    private boolean isResetScroll;
    /**
     * 重设的Y值滚动距离
     */
    private int resetScrollY;

    public WraperScrollView(Context context) {
        super(context);
    }

    public WraperScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WraperScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public WraperScrollView(Context context, AttributeSet attrs, int defStyleAttr, int
            defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        this.curScrollY = t;
        if (isResetScroll) {
            isResetScroll = false;
            scrollTo(0, resetScrollY);
        }
    }

    public int getCurScrollY() {
        return this.curScrollY;
    }

    public void setResetScrollY(int scrollY) {
        this.isResetScroll = true;
        this.resetScrollY = scrollY;
    }
}
