package com.ttxg.fruitday.gui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;


/**
 * 包装webView
 * Created by yb on 2016/8/26.
 */
public class WraperWebView extends WebView {
    private float downX, downY;

    private boolean isDown = true;

    public WraperWebView(Context context) {
        super(context);
    }

    public WraperWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WraperWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public WraperWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public WraperWebView(Context context, AttributeSet attrs, int defStyleAttr, boolean
            privateBrowsing) {
        super(context, attrs, defStyleAttr, privateBrowsing);
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                downX = event.getX();
//                downY = event.getY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                float moveX = event.getX();
//                float moveY = event.getY();
//                if(Math.abs(downX - moveX) < Math.abs(downY - moveY)){
//                    // 上下滑动 不允许父类拦截
//                    requestDisallowInterceptTouchEvent(true);
//                    if(downY - moveY > 0) {
//                        // 手指向上滑动(查看webView里面的内容) 不允许父类拦截
//                        requestDisallowInterceptTouchEvent(true);
//                    }else {
//                        // 手指向下滑动(有可能webView已经滑到顶了 需要判断)
//                        if(getScrollY() == 0){
//                            // 滑到顶了 允许父类拦截了
//                            requestDisallowInterceptTouchEvent(false);
//                        }else {
//                            // 没有滑到定 不允许父类拦截
//                            requestDisallowInterceptTouchEvent(true);
//                        }
//                    }
//                }else {
//                    // 左右滑动 允许父类拦截了
//                    requestDisallowInterceptTouchEvent(false);
//                }
//                break;
//        }
//        return super.onTouchEvent(event);
//    }


//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
////        switch (ev.getAction()){
////            case MotionEvent.ACTION_DOWN:
////                downX = ev.getX();
////                downY = ev.getY();
////                break;
////            case MotionEvent.ACTION_MOVE:
////                float moveX = ev.getX();
////                float moveY = ev.getY();
//////                if(Math.abs(downX - moveX) < Math.abs(downY - moveY)){
//////                    // 上下滑动 不允许父类拦截
////////                    requestDisallowInterceptTouchEvent(true);
//////                    if(downY - moveY > 0) {
//////                        // 手指向上滑动(查看webView里面的内容) 不允许父类拦截
//////                        getParent().requestDisallowInterceptTouchEvent(true);
//////                    }else {
//////                        // 手指向下滑动(有可能webView已经滑到顶了 需要判断)
//////                        if(getScrollY() == 0){
//////                            // 滑到顶了 允许父类拦截了
//////                            getParent().requestDisallowInterceptTouchEvent(false);
//////                        }else {
//////                            // 没有滑到定 不允许父类拦截
//////                            getParent().requestDisallowInterceptTouchEvent(true);
//////                        }
//////                    }
//////                }else {
//////                    // 左右滑动 允许父类拦截了
//////                    getParent().requestDisallowInterceptTouchEvent(false);
//////                }
//////                if(Math.abs(downX - moveX) > Math.abs(downY - moveY)){
//////                    // 左右滑动 允许父类拦截了
//////                    getParent().requestDisallowInterceptTouchEvent(false);
//////                }else {
//////                    // 上下滑动 不允许父类拦截
//////                    getParent().requestDisallowInterceptTouchEvent(true);
//////                }
////        }
//        // 上下滑动 不允许父类拦截
//        getParent().requestDisallowInterceptTouchEvent(true);
//        return super.dispatchTouchEvent(ev);
//    }


//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        DLog.i("dispatchTouchEvent() 事件分发——getScrollY()------>>>>" + getScrollY());
//        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//            DLog.i("dispatchTouchEvent() 事件分发—按下—getScrollY()------>>>>" + getScrollY());
//            isDown = true;
//            downX = ev.getX();
//            downY = ev.getY();
//        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
//            if (isDown) {
//                if (downY > ev.getY()) {
//                    // 向下翻看WebView内容
//                    DLog.e("dispatchTouchEvent() 事件分发—向下翻看WebView内容—getScrollY()------>>>>" +
//                            getScrollY());
//                    // 上下滑动 不允许父类拦截
//                    getParent().requestDisallowInterceptTouchEvent(true);
//                } else {
//                    // 想上翻看WebView内容，有可能到WebView顶部
//                    if (getScrollY() == 0) {
//                        // 滑动中到顶了
//                        DLog.e("dispatchTouchEvent() 事件分发—到顶—getScrollY()------>>>>" + getScrollY
//                                ());
//                        getParent().requestDisallowInterceptTouchEvent(false);
////                        return true;
//                    }else {
//                        getParent().requestDisallowInterceptTouchEvent(true);
//                    }
//                }
//            }
//        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
//            DLog.d("dispatchTouchEvent() 事件分发—抬起—getScrollY()------>>>>" + getScrollY());
//            isDown = false;
//        }
////        // 上下滑动 不允许父类拦截
////        getParent().requestDisallowInterceptTouchEvent(true);
//        return super.dispatchTouchEvent(ev);
//    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
//        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//            isDown = true;
//            downX = ev.getX();
//            downY = ev.getY();
//        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
//            if (isDown) {
//                if (downY > ev.getY()) {
//                    // 向下翻看WebView内容
//                    // 上下滑动 不允许父类拦截
//                    getParent().requestDisallowInterceptTouchEvent(true);
//                } else {
//                    // 想上翻看WebView内容，有可能到WebView顶部
//                    if (getScrollY() == 0) {
//                        // 滑动中到顶了
//                        getParent().requestDisallowInterceptTouchEvent(false);
//                        return false;
//                    }else {
//                        getParent().requestDisallowInterceptTouchEvent(true);
//                    }
//                }
//            }
//        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
//            isDown = false;
//        }
//        return super.onTouchEvent(ev);
        return false;
    }
}
