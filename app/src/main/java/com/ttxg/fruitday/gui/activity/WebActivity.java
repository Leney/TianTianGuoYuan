package com.ttxg.fruitday.gui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.util.log.DLog;

/**
 * 网页加载Activity
 * Created by lilijun on 2016/12/9.
 */
public class WebActivity extends BaseActivity {

    private WebView webView;
    private String url;
    private ProgressBar bar;

    @Override
    protected void initView() {
        url = getIntent().getStringExtra("url");
        String title = getIntent().getStringExtra("title");
        if(TextUtils.isEmpty(title)){
            title =getResources().getString(R.string.web_page);
        }
        setTitleName(title);

        View view = View.inflate(WebActivity.this, R.layout.web_progress_bar, null);
        bar = (ProgressBar) view.findViewById(R.id.web_progress);
        setAddView(view);
        webView = new WebView(WebActivity.this);
        WebSettings ws = webView.getSettings();
        ws.setDefaultTextEncodingName("uft-8");
        ws.setLoadWithOverviewMode(true);
        ws.setJavaScriptEnabled(true);
        ws.setPluginState(WebSettings.PluginState.ON);
        ws.setDomStorageEnabled(true);
        ws.setSupportMultipleWindows(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm
                    .TEXT_AUTOSIZING);
        } else {
            ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }
        setCenterView(webView);
        if(!url.startsWith("http://")){
            url = "http://"+url;
        }
        webView.loadUrl(url);
        DLog.i("url------>>>"+url);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    // 加载完成
                    setAddViewVisible(false);
                    showCenterView();
                } else {
                    // 加载中
                    bar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                webView.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request,
                                        WebResourceError error) {
                super.onReceivedError(view, request, error);
            }
        });
    }

    @Override
    protected void tryAgain() {
        super.tryAgain();
        webView.reload();
    }

    @Override
    public void onBackPressed() {
        if(webView != null && webView.canGoBack()){
            webView.goBack();// 返回前一个页面
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public void finish() {
        try {
            // 反射方法 暂停webView
            webView.getClass().getMethod("onPause").invoke(webView,(Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.setVisibility(View.GONE);
            webView.removeAllViews();
            webView.destroy();
            webView = null;
        }
    }

    public static void startActivity(Context context, String url, String title) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }
}
