package com.ttxg.fruitday.gui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.iflytek.voiceads.AdError;
import com.iflytek.voiceads.AdKeys;
import com.iflytek.voiceads.IFLYNativeAd;
import com.iflytek.voiceads.IFLYNativeListener;
import com.iflytek.voiceads.NativeADDataRef;
import com.ttxg.fruitday.R;

import java.util.List;

/**
 * 全屏广告界面
 * Created by llj on 2017/4/5.
 */
public class FullScreenAdActivity extends Activity {

    private SimpleDraweeView adImg;

    private TextView closeBtn, adMark;

    /**
     * 超时次数
     */
    private int totalCount = 3;

    private final int COUNT_DOWN_MSG = 1;

    /**
     * 标识是否请求请成功
     */
    private boolean isSuccess = false;

//    /**
//     * 标识是否点击跳转了
//     */
//    private boolean isClick = false;

    /**
     * 标识是否已经请求广告超时
     */
    private boolean isTimeOut = false;

    /**
     * 科大讯飞全屏广告
     */
    private IFLYNativeAd interstitialAd;

//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if (msg.what == COUNT_DOWN_MSG) {
//                if (isSuccess) {
//                    handler.removeMessages(COUNT_DOWN_MSG);
//                    return;
//                }
//                if (--totalCount < 0) {
//                    // 倒计时完成(请求超时)
//                    handler.removeMessages(COUNT_DOWN_MSG);
//                    Log.e("llj", "倒计时完成！！！");
//                    isTimeOut = true;
//                    // 打开主页
//                    MainActivity.startActivity(FullScreenAdActivity.this);
//                    finish();
//                } else {
//                    // 倒计时未完成
//                    sendEmptyMessageDelayed(COUNT_DOWN_MSG, 1000);
//                }
//            }
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_full_screen_layout);

        adImg = (SimpleDraweeView) findViewById(R.id.full_screen_ad_img);
//        adImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final AdInfo adInfo = (AdInfo) view.getTag();
//                if (adInfo != null) {
//                    AdUtils.doClick(FullScreenAdActivity.this, adInfo, adImg.downX, adImg.downY,
//                            adImg.upX, adImg.upY, new AdUtils.OnAdClickListener() {
//                                @Override
//                                public void onClick(String type) {
//                                    if (TextUtils.equals(type, AdInfo.AD_TYPE_REDIRECT)) {
//                                        // 跳转链接类型，什么都不做
//                                        countDownTextView.stop();
//                                        countDownTextView.setOnDoneListner(null);
//                                        if (TextUtils.isEmpty(adInfo.getLandingUrl())) {
//                                            MainActivity.startActivity(FullScreenAdActivity.this);
//                                        }
//                                        finish();
//                                    } else if (TextUtils.equals(type, AdInfo.AD_TYPE_DOWNLOAD)) {
//                                        // 下载类型
//                                        // 跳转到主页
//                                        countDownTextView.stop();
//                                        countDownTextView.setOnDoneListner(null);
//                                        MainActivity.startActivity(FullScreenAdActivity.this);
//                                        finish();
//                                    } else if (TextUtils.equals(type, AdInfo.AD_TYPE_BRAND)) {
//                                        // 品牌类型
//                                        countDownTextView.stop();
//                                        countDownTextView.setOnDoneListner(null);
//                                        finish();
//                                    }
//                                }
//                            });
////                    isClick = true;
//                }
//            }
//        });

        closeBtn = (TextView) findViewById(R.id.full_screen_close_ad_img);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                countDownTextView.stop();
//                finish();
                // 打开主页
                MainActivity.startActivity(FullScreenAdActivity.this);
                finish();
            }
        });
//        countDownTextView = (CountDownTextView2) findViewById(R.id.full_screen_ad_count_down);
//        countDownTextView.setOnDoneListner(new CountDownTextView2.OnCountDownDoneListener() {
//            @Override
//            public void onDone() {
//                // 倒计时完成
////                finish();
//                Log.e("llj", "广告展示倒计时完成！！！");
////                if(isClick){
////                    finish();
////                    return;
////                }
//                // 跳转到主页
//                MainActivity.startActivity(FullScreenAdActivity.this);
//                finish();
//            }
//        });

        adMark = (TextView) findViewById(R.id.full_screen_ad_mark);

//        String userAgent = LeplayPreferences.getInstance(FullScreenAdActivity.this).getUserAgent();
//        if (TextUtils.isEmpty(userAgent)) {
//            userAgent = new WebView(FullScreenAdActivity.this).getSettings().getUserAgentString();
//            LeplayPreferences.getInstance(FullScreenAdActivity.this).setUserAgent(userAgent);
//            DLog.i("llj", "转义之前 userAgent----->>" + userAgent);
//            String escapeUserAgent = StringEscapeUtils.escapeJava(userAgent);
//            DLog.i("llj", "转义之之后 escapeUserAgent----->>" + escapeUserAgent);
//            DataCollectionConstant.userAgent = escapeUserAgent;
//        } else {
//            DataCollectionConstant.userAgent = userAgent;
//            DLog.i("llj", "userAgent不为空----->>" + userAgent);
//        }

        // 请求全屏数据
        requestFullScreenAds();
//        handler.sendEmptyMessageDelayed(COUNT_DOWN_MSG, 1000);


//        adInfo = (AdInfo) getIntent().getSerializableExtra("adInfo");
//        if (adInfo == null) {
//            finish();
//        }

//        if (!TextUtils.isEmpty(adInfo.getImageUrl())) {
//            ImageLoaderManager.getInstance().displayImage(adInfo.getImageUrl(), adImg,
//                    DisplayUtil.getScreenShortImageLoaderOptions());
//        } else if (!TextUtils.isEmpty(adInfo.getRightIconUrl())) {
//            ImageLoaderManager.getInstance().displayImage(adInfo.getRightIconUrl(), adImg,
//                    DisplayUtil.getScreenShortImageLoaderOptions());
//        } else if (!TextUtils.isEmpty(adInfo.getIcon())) {
//            ImageLoaderManager.getInstance().displayImage(adInfo.getIcon(), adImg, DisplayUtil
//                    .getScreenShortImageLoaderOptions());
//        }
//        adImg.setTag(adInfo);
//
//        if (adInfo.getImprUrls() != null) {
//            int length = adInfo.getImprUrls().length;
//            for (int i = 0; i < length; i++) {
//                // 加载完成  上报
//                NetUtil.requestUrl(adInfo.getImprUrls()[i], new
//                        Callback() {
//                            @Override
//                            public void onResponse(Response response) throws
//                                    IOException {
//                                DLog.i("llj", "请求一个url成功!!!!");
//                                String result = response.body().string();
//                                DLog.i("llj", "result---->>>" + result);
//                            }
//
//                            @Override
//                            public void onFailure(Request request,
//                                                  IOException e) {
//                                DLog.e("llj", "请求一个url失败!!!");
//                            }
//                        });
//            }
//        }
//        countDownTextView.start(5);
    }

    @Override
    public void onBackPressed() {
        MainActivity.startActivity(FullScreenAdActivity.this);
        FullScreenAdActivity.this.finish();
//        super.onBackPressed();
    }

//    @Override
//    public void finish() {
//        handler.removeMessages(COUNT_DOWN_MSG);
//        countDownTextView.stop();
//        countDownTextView.setOnDoneListner(null);
//        super.finish();
//    }

    /**
     * 请求全屏广告数据
     */
    private void requestFullScreenAds() {
        interstitialAd = new IFLYNativeAd(FullScreenAdActivity.this, "6B8B6CDB6C2C2314172024006E00D888", new IFLYNativeListener() {
            @Override
            public void onAdFailed(AdError adError) {
                // 广告请求失败
                Log.e("llj", "调用讯飞全屏广告失败！！！  errorCode---->>>" + adError.getErrorCode() + "\n 错误描述------>>>" + adError.getErrorDescription());
            }

            @Override
            public void onADLoaded(List<NativeADDataRef> lst) {
                // 广告请求成功
                Log.i("llj", "请求原生全屏广告成功---lst == null----->>>" + (lst == null));

                // 设置信息流广告数据
                if (lst != null && !lst.isEmpty()) {
                    Log.i("llj", "请求原生全屏广告成功---lst.size----->>>" + lst.size());
                    // 因为信息流广告每次只能返回一条信息流广告  所以只需要第一条数据
//                    childList.get(groupPosition).get(childPosition).setInfoAd(lst.get(0));

                    final NativeADDataRef adInfo = lst.get(0);

                    Log.i("llj","adInfo.getImage()------->>>"+adInfo.getImage());
                    adImg.setImageURI(adInfo.getImage());
                    adMark.setText(adInfo.getAdSourceMark() + "|" + getResources().getString(R.string.ad));
                    // 曝光
                    adInfo.onExposured(adImg);
                    adImg.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            switch (event.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    interstitialAd.setParameter(AdKeys.CLICK_POS_DX, event.getX() + "");
                                    interstitialAd.setParameter(AdKeys.CLICK_POS_DY, event.getY() + "");
                                    break;
                                case MotionEvent.ACTION_UP:
                                    interstitialAd.setParameter(AdKeys.CLICK_POS_UX, event.getX() + "");
                                    interstitialAd.setParameter(AdKeys.CLICK_POS_UY, event.getY() + "");
                                    break;
                                default:
                                    break;
                            }
                            return false;
                        }
                    });

                    adImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            adInfo.onClicked(view);
//                            FullScreenAdActivity.this.finish();
                        }
                    });
                }
            }

            @Override
            public void onCancel() {
                // 下载类广告， 下载提示框取消
            }

            @Override
            public void onConfirm() {
                // 下载类广告， 下载提示框确认
            }
        });
        int count = 1; // 一次拉取的广告条数：范围 1-30（ 目前仅支持每次请求一条）
        interstitialAd.loadAd(count);
    }

//    public static void startAcitivty(Context context, AdInfo adInfo) {
//        Intent intent = new Intent(context, FullScreenAdActivity.class);
//        intent.putExtra("adInfo", adInfo);
//        context.startActivity(intent);
//    }
}
