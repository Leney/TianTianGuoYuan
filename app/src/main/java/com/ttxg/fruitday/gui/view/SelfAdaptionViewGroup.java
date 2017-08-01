package com.ttxg.fruitday.gui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * 自适应宽高度的ViewGroup
 * Created by yb on 2016/10/12.
 */
public class SelfAdaptionViewGroup extends ViewGroup {

    //    private int width = 0;
    public SelfAdaptionViewGroup(Context context) {
        super(context);
        //ViewGroup不需要绘制任何内容，所以设置这个标记，系统会做相应的优化
        setWillNotDraw(true);
    }

    public SelfAdaptionViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        //ViewGroup不需要绘制任何内容，所以设置这个标记，系统会做相应的优化
        setWillNotDraw(true);
    }

    public SelfAdaptionViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //ViewGroup不需要绘制任何内容，所以设置这个标记，系统会做相应的优化
        setWillNotDraw(true);
    }


//    /**
//     * 设置本ViewGroup的LayoutParams
//     */
//    @Override
//    public LayoutParams generateLayoutParams(AttributeSet attrs) {
//        return new MarginLayoutParams(getContext(), attrs);
//    }

    /**
     * 计算所有的子View的宽度和高度，然后根据子View的计算结果设置本ViewGroup的宽度和高度
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 获得此ViewGroup所属容器为其推荐的计算模式以及宽和高(因为此ViewGroup也可能被其它容器所包裹)
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        // 计算出所有的子View的宽和高
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int childCount = getChildCount();
        MarginLayoutParams childParams = null;
        // 当前已经布局完成的剩余的宽度
        int curWidth = sizeWidth;

        //测量出来的宽和高
        int width = 0;
        int height = 0;
        // 当前一行完成的宽度
        int lineWidth = 0;

        // 一行中最高的子控件高度
        int lineMaxHeight = 0;
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            childParams = (MarginLayoutParams) childView.getLayoutParams();
            // childView本身的实际宽度
            int childAllWidth = childView.getMeasuredWidth() + childParams.leftMargin + childParams.rightMargin;
            int childAllHeight = childView.getMeasuredHeight() + childParams.topMargin + childParams.bottomMargin;
            if(i == 0){
//                lineMaxHeight = childAllHeight;
                height += childAllHeight;
                lineMaxHeight = height;
            }
            if (childAllWidth <= curWidth) {
                // 可以容纳下当前子View
                //减去当前子View所占用掉整行的宽度
                curWidth -= childAllWidth;
                lineWidth += childAllWidth;
//                lineMaxHeight = Math.max(lineMaxHeight,childAllHeight);
//                if(i == 0){
//                    height = lineMaxHeight;
//                }
            } else {
                // 当前行剩余的宽度不能容纳下当前子View了，则换行
                //取最大的宽度值
                width = Math.max(lineWidth, width);
//                height += lineMaxHeight;
                height += childAllHeight;
                Log.e("lilijun","onMeasure,height----->>>"+height);
                lineWidth = 0;
                lineMaxHeight += childAllHeight;
                curWidth = width - childAllWidth;
            }
            width = Math.max(lineWidth, width);
//            height = Math.max(childAllHeight, height);
        }
        /**
         //         * 如果是wrap_content设置为我们计算的值
         //         * 否则：直接设置为父容器计算的值
         //         */
        setMeasuredDimension((widthMode == MeasureSpec.EXACTLY) ? sizeWidth
                : width, (heightMode == MeasureSpec.EXACTLY) ? sizeHeight
                : height);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.i("lilijun", "onLayout()!!!");
        int childCount = getChildCount();
        int childWidth = 0;
        int childHeight = 0;
        MarginLayoutParams childParams = null;
        // 当前已经布局完成的剩余的宽度
        int width = getWidth();
        int curWidth = width;
        // 当前行新布局子view的左边起始位置
        int curLeft = 0;
        // 当前行新布局子view的top起始位置
        int curTop = 0;
        int curRight = 0;
        int curBottom = 0;

        // 上一行的高度
        int lastLineHeight = 0;

        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            childWidth = childView.getMeasuredWidth();
            childHeight = childView.getMeasuredHeight();
            childParams = (MarginLayoutParams) childView.getLayoutParams();
            // childView本身的实际宽度
            int childAllWidth = childWidth + childParams.leftMargin + childParams.rightMargin;
            // childView本身的实际高度
            int childAllHeight = childHeight + childParams.topMargin + childParams.bottomMargin;
            if (childAllWidth <= curWidth) {
                // 可以容纳下当前子View
                // 得到此行的最高的控件高度
                lastLineHeight = Math.max(lastLineHeight, childAllHeight);
                curRight += childAllWidth;
                curBottom = curTop + childAllHeight;
                childView.layout(curLeft, curTop, curRight, curBottom);
                //减去当前子View所占用掉整行的宽度
                curWidth -= childAllWidth;
                curLeft += childAllWidth;
            } else {
                // 当前行剩余的宽度不能容纳下当前子View了，则换行
                curLeft = 0;
                //再加上上一行的高度
                curRight = childAllWidth;
                curTop += lastLineHeight;
                curBottom = curTop + childAllHeight;
                childView.layout(curLeft, curTop, curRight, curBottom);
                curWidth = width - childAllWidth;
                curLeft += childAllWidth;
                lastLineHeight = childAllHeight;
            }

        }

    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    /**
     * 继承自margin，支持子视图android:layout_margin属性
     */
    public static class LayoutParams extends MarginLayoutParams {
        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }
        public LayoutParams(int width, int height) {
            super(width, height);
        }
        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super(source);
        }
    }
}
