package com.ttxg.fruitday.gui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;

import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.util.Constants;
import com.ttxg.fruitday.util.Util;
import com.ttxg.fruitday.util.download.DownloadManagerUtil;
import com.ttxg.fruitday.util.log.DLog;

/**
 * 关于设置界面
 * Created by lilijun on 2016/11/18.
 */
public class AboutActivity extends BaseActivity implements View.OnClickListener {
    private final String CHECK_UPDATE_VERSION = "version/checkUpdate";
    private TextView clearCacheBtn;
    private ProgressDialog progressDialog;
    private String versionName;
    private int versionCode;

    private static final int GET_CACHE_FILE_SIZE_SUCCESS = 1;
    private static final int CLEAR_CACHE_SUCCESS = 2;

    private final MyHandler handler = new MyHandler(this);

    @Override
    protected void initView() {
        setTitleName(getResources().getString(R.string.about));
        setCenterView(R.layout.activity_about);
        TextView checkBtn = (TextView) findViewById(R.id.about_check_version_update_btn);
        checkBtn.setOnClickListener(this);
        clearCacheBtn = (TextView) findViewById(R.id.about_clear_cache_btn);
        getVersionCodeAndName();

        TextView version = (TextView) findViewById(R.id.about_version);
        version.setText(String.format(getResources().getString(R.string.format_version),
                versionName));
        showCenterView();

        new Thread(getCacheSizeRunnable).start();
    }

    @Override
    protected void onLoadDataSuccess(String tag, JSONObject resultObject) {
        super.onLoadDataSuccess(tag, resultObject);
        if (TextUtils.equals(tag, CHECK_UPDATE_VERSION)) {
            // 检查更新版本成功
            DLog.i("lilijun", "检查更新版本成功！！！");
            progressDialog.dismiss();
            try {
                if (resultObject.has("newVersion")) {
                    // 有版本更新
//                    PreferencesUtils.putBoolean(Constants.HAS_VERSION_UPDATE, true);
                    JSONObject newVersionObject = resultObject.getJSONObject("newVersion");
                    // 应用名称
                    String title = newVersionObject.getString("appName");
                    // 版本名称
                    String versionName = newVersionObject.getString("version");
                    // 新版特性
                    String describle = newVersionObject.getString("properties");
                    // 更新包下载地址
                    String downloadUrl = newVersionObject.getString("downloadUrl");
                    // 组合消息
                    String message = title + getResources().getString(R.string
                            .update_version) + versionName + "\n\n" + describle;
                    showUpdateVersionDialog(title, message, downloadUrl);
                } else {
                    // 已经是最新版本
                    Util.showErrorMessage(AboutActivity.this, getResources().getString(R.string
                            .check_update_newest));
                    DLog.i("lilijun", "检查更新版本成功！！！但当前没有版本更新！");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onLoadDataError(String tag, int errorCode, String msg) {
        super.onLoadDataError(tag, errorCode, msg);
        if (TextUtils.equals(tag, CHECK_UPDATE_VERSION)) {
            progressDialog.dismiss();
            Util.showErrorMessage(AboutActivity.this, msg, getResources().getString(R.string
                    .check_update_failed));
        }
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.about_check_version_update_btn:
                // 检查更新
                Map<String, Object> params = new HashMap<>();
                params.put("versionCode", versionCode);
                params.put("appId", Constants.YB_APP_ID);
                //1=app，2=pad
                params.put("deviceType", 1);
                progressDialog = Util.showLoadingDialog(AboutActivity.this, progressDialog);
                loadDataGet(CHECK_UPDATE_VERSION, params);
                break;
            case R.id.about_clear_cache_btn:
                // 清除缓存
                // 清除图片缓存
                progressDialog = Util.showLoadingDialog(AboutActivity.this, progressDialog);
                new Thread(clearCacheRunnable).start();
                break;
        }
    }

    /**
     * 获取缓存文件大小Runnable对象
     */
    private Runnable getCacheSizeRunnable = new Runnable() {
        @Override
        public void run() {
            File cacheFile = new File(Environment
                    .getExternalStorageDirectory(), "iyunbang");
            // 获取app自定义缓存文件夹里的大小
            long size = Util.getFileSize(cacheFile);
            // 获取Fresco缓存大小
            size += Fresco.getImagePipelineFactory().getMainDiskStorageCache().getSize();
            if (size < 0) {
                size = 0;
            }
            String sizeStr = Util.convertFileSize(size);
            Message msg = new Message();
            msg.what = GET_CACHE_FILE_SIZE_SUCCESS;
            Bundle bundle = new Bundle();
            bundle.putLong("size", size);
            bundle.putString("sizeStr", sizeStr);
            msg.setData(bundle);
            handler.sendMessage(msg);
        }
    };

    /**
     * 删除缓存文件Runnable对象
     */
    private Runnable clearCacheRunnable = new Runnable() {
        @Override
        public void run() {

            File cacheFile = new File(Environment
                    .getExternalStorageDirectory(), "iyunbang");
            Util.deleteAllFiles(cacheFile);
            Fresco.getImagePipeline().clearCaches();
            handler.sendEmptyMessage(CLEAR_CACHE_SUCCESS);
        }
    };


    private void showUpdateVersionDialog(String title, String message, final String downloadUrl) {
        Util.showAlertDialog(AboutActivity.this, title, message, getResources().getString(R
                        .string.update_now), getResources().getString(R.string.update_later), new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // 立即更新
                                DownloadManagerUtil.getInstance().startDownload(downloadUrl);
                                dialogInterface.dismiss();
                            }
                        },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 暂不更新
                        dialogInterface.dismiss();
                    }
                });
    }

    /**
     * 获取版本号和名称
     */
    private void getVersionCodeAndName() {
        try {
            PackageManager pm = AboutActivity.this.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(AboutActivity.this.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
            versionCode = pi.versionCode;
            versionName = pi.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class MyHandler extends Handler {
        private final WeakReference<AboutActivity> mActivity;

        private MyHandler(AboutActivity mActivity) {
            this.mActivity = new WeakReference<>(mActivity);
        }

        @Override
        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
            AboutActivity aboutActivity = mActivity.get();
            if (aboutActivity != null) {
                if (msg.what == GET_CACHE_FILE_SIZE_SUCCESS) {
                    // 获取缓存文件大小成功
                    long size = msg.getData().getLong("size", 0);
                    String sizeStr = msg.getData().getString("sizeStr");
                    aboutActivity.clearCacheBtn.setText(String.format(aboutActivity.getResources
                            ().getString(R.string
                            .format_clear_cache), sizeStr));
                    if (size != 0) {
                        aboutActivity.clearCacheBtn.setOnClickListener(aboutActivity);
                    }
                } else if (msg.what == CLEAR_CACHE_SUCCESS) {
                    // 清除缓存成功
                    aboutActivity.progressDialog.dismiss();
                    Util.showToast(aboutActivity, aboutActivity.getResources().getString(R.string
                            .clear_cache_success));
                    aboutActivity.clearCacheBtn.setText(aboutActivity.getResources().getString(R
                            .string.cleared));
                    aboutActivity.clearCacheBtn.setClickable(false);
                }
            }
        }
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, AboutActivity.class);
        context.startActivity(intent);
    }
}
