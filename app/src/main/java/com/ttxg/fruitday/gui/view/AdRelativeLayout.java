package com.ttxg.fruitday.gui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.iflytek.voiceads.AdKeys;
import com.iflytek.voiceads.IFLYNativeAd;

/**
 * Created by yb on 2017/4/7.
 */
public class AdRelativeLayout extends RelativeLayout {
//    public float downX,downY,upX,upY;
    /**
     * 讯飞信息流广告请求控制器
     */
    public IFLYNativeAd nativeAd;
    public AdRelativeLayout(Context context) {
        super(context);
    }

    public AdRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AdRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AdRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setNativeAd(IFLYNativeAd nativeAd) {
        this.nativeAd = nativeAd;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
////        if(event.getAction() == MotionEvent.ACTION_DOWN){
//            if(nativeAd != null){
//                nativeAd.setParameter(AdKeys.CLICK_POS_DX, event.getX()+"");
//                nativeAd.setParameter(AdKeys.CLICK_POS_DY, event.getY() + "");
//
//            }
////            downX = event.getX();
////            downY = event.getY();
//        }else if (event.getAction() == MotionEvent.ACTION_UP){
//            nativeAd.setParameter(AdKeys.CLICK_POS_UX, event.getX() + "");
//            nativeAd.setParameter(AdKeys.CLICK_POS_UY, event.getY() + "");
////            upX = event.getX();
////            upY = event.getY();
//        }
//        return super.onTouchEvent(event);

        if(nativeAd != null){
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    nativeAd.setParameter(AdKeys.CLICK_POS_DX, event.getX() + "");
                    nativeAd.setParameter(AdKeys.CLICK_POS_DY, event.getY() + "");
                    break;
                case MotionEvent.ACTION_UP:
                    nativeAd.setParameter(AdKeys.CLICK_POS_UX, event.getX() + "");
                    nativeAd.setParameter(AdKeys.CLICK_POS_UY, event.getY() + "");
                    break;
                default:
                    break;
            }
        }
        return super.onTouchEvent(event);
    }
}
