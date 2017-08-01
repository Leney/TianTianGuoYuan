package com.ttxg.fruitday.gui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.facebook.drawee.generic.GenericDraweeHierarchy;

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.util.log.DLog;

/**
 * 自定义添加左上角标签的SimpleDraweeView控件
 * Created by yb on 2016/11/14.
 */
public class SimpleDraweeViewLab extends com.facebook.drawee.view.SimpleDraweeView {
    /**
     * 标识是否显示标签
     */
    private boolean isShowLab = false;
    public SimpleDraweeViewLab(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
    }

    public SimpleDraweeViewLab(Context context) {
        super(context);
    }

    public SimpleDraweeViewLab(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SimpleDraweeViewLab(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SimpleDraweeViewLab(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context){

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(isShowLab){
            DLog.i("绘制lab");
            Paint paint = new Paint();
            Bitmap bmp= BitmapFactory.decodeResource(getResources(), R.drawable.vip_lab);
            canvas.drawBitmap(bmp,0,0,paint);
        }
    }

    public void showLab(boolean isShow){
        this.isShowLab = isShow;
        if(isShowLab){
            postInvalidate();
        }
    }
}
