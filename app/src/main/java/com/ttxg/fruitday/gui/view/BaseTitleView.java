package com.ttxg.fruitday.gui.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ttxg.fruitday.R;


/**
 * 基本Title控件
 * Created by yb on 2016/8/23.
 */
public class BaseTitleView extends FrameLayout {
    private ImageView backImg;
    private TextView titleName;
    private ImageView rightImg;
    public BaseTitleView(Context context) {
        super(context);
        init();
    }

    public BaseTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public BaseTitleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        View baseTitleView = View.inflate(getContext(), R.layout.base_title_view,null);
        backImg = (ImageView) baseTitleView.findViewById(R.id.base_title_back_img);
        titleName = (TextView) baseTitleView.findViewById(R.id.base_title_name);
        rightImg = (ImageView) baseTitleView.findViewById(R.id.base_title_right_img);
        backImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity)getContext()).finish();
            }
        });
        addView(baseTitleView);
    }

    /**
     * 设置标题名称
     * @param title
     */
    public void setTitleName(String title){
        titleName.setText(title);
    }

    /**
     * 设置返回按钮是否显示
     * @param visible
     */
    public void setBackImgVisible(boolean visible){
        backImg.setVisibility(visible ? View.VISIBLE:View.GONE);
    }

    /**
     * 设置右边图片的资源
     * @param sourceId
     */
    public void setRightImgRes(int sourceId){
        rightImg.setVisibility(VISIBLE);
        rightImg.setImageResource(sourceId);
    }

    /**
     * 设置右边的图片点击事件
     * @param listener
     */
    public void setRightImgClickListener(OnClickListener listener){
        rightImg.setOnClickListener(listener);
    }
}
