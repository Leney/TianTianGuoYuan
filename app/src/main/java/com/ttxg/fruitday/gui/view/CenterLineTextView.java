package com.ttxg.fruitday.gui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import com.ttxg.fruitday.R;


/**
 * 中间画线的TextView
 * Created by yb on 2016/8/23.
 */
public class CenterLineTextView extends TextView {
    private Paint mPaint;
    public CenterLineTextView(Context context) {
        super(context);
        init();
    }

    public CenterLineTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CenterLineTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CenterLineTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        mPaint = new Paint();
        mPaint.setStrokeWidth(getResources().getDimension(R.dimen.center_line_height));
//        mPaint.setColor(Color.GRAY);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(getTextColors().getDefaultColor());
        canvas.drawLine(0,getMeasuredHeight()/2,getMeasuredWidth(),getMeasuredHeight()/2,mPaint);
    }
}
