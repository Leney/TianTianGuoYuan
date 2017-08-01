package com.ttxg.fruitday.gui.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 倒计时控件
 * Created by lilijun on 2016/9/8.
 */
public class CountDownTextView extends TextView {
    private final int COUNT_DOWN_MSG = 1;
    /** 倒计时总次数 */
    private int totalCount = 60;
    /** 默认的显示文本*/
    private String defaultText;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == COUNT_DOWN_MSG){
                if(--totalCount < 0){
                    // 倒计时完成
                    // 设置按钮可点击
                    setEnabled(true);
                    // 还原默认的显示文本
                    setText(defaultText);
                }else {
                    // 倒计时未完成
                    setText(totalCount + "s");
                    sendEmptyMessageDelayed(COUNT_DOWN_MSG,1000);
                }
            }
        }
    };

    public CountDownTextView(Context context) {
        super(context);
    }

    public CountDownTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CountDownTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CountDownTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    // 开始倒计时
    public void start(int totalCount){
        this.totalCount = totalCount;
        this.defaultText = getText().toString();
        // 开始计时 设置为不可点击
        setEnabled(false);
        setText(totalCount+"s");
        handler.sendEmptyMessageDelayed(COUNT_DOWN_MSG,1000);
    }
}
