package com.ttxg.fruitday.util.download;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;

import java.io.File;

import com.ttxg.fruitday.util.log.DLog;

/**
 * 下载管理器
 * Created by lilijun on 2016/11/11.
 */
public class DownloadManagerUtil {
    private static DownloadManagerUtil instance;
    private Context context;
    /**
     * 创建一个下载任务所生成的downloadId
     */
    private long downloadId;
    private DownloadManager downloadManager = null;

    /**
     * apk文件的保存路径
     */
    private String apkPath = "";

    private DownloadManagerUtil() {
    }

    public static DownloadManagerUtil getInstance() {
        if (instance == null) {
            synchronized (DownloadManagerUtil.class) {
                instance = new DownloadManagerUtil();
            }
        }
        return instance;
    }

    public void init(Context context) {
        this.context = context;
        // 注册下载完成广播
        IntentFilter downloadFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
//        // 点击通知栏中的下载通知
//        downloadFilter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED);
        context.registerReceiver(downloadReceiver, downloadFilter);
    }

    /**
     * 注销监听
     */
    public void unRegisterReceiver() {
        context.unregisterReceiver(downloadReceiver);
    }

    /**
     * 开始下载
     *
     * @param uri
     */
    public void startDownload(String uri) {
        downloadManager = (DownloadManager) context.getSystemService(Context
                .DOWNLOAD_SERVICE);
        DownloadManager.Request req = new DownloadManager.Request(Uri.parse(uri));
        req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        //req.setAllowedOverRoaming(false);
        req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        //设置文件的保存的位置[三种方式]
        //第一种
        //file:///storage/emulated/0/Android/data/your-package/files/Download/YunXianSheng.apk
//        apkPath = Environment.DIRECTORY_DOWNLOADS + "/YunXianSheng.apk";
//        apkPath = "/storage/emulated/0/Android/data/cn.ybgoo.gou/files/Download/YunXianSheng.apk";
        File cacheFile = new File(Environment.getExternalStorageDirectory(), "iyunbang");
        if(!cacheFile.exists()){
            if (!cacheFile.mkdirs())
            {
                DLog.i("创建文件夹失败！！！");
            }
        }
        apkPath = Environment.getExternalStorageDirectory() + File.separator +
                "iyunbang" + File.separator + "YunXianSheng.apk";
        File file = new File(apkPath);
        if (file.exists()) {
            if(file.delete()){
                DLog.i("删除apk文件成功！！");
            }
        }
//        req.setDestinationInExternalFilesDir(context, "iyunbang", "YunXianSheng.apk");
        //第二种
        //file:///storage/emulated/0/Download/update.apk
        //req.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "update.apk");
        //第三种 自定义文件路径
        req.setDestinationUri(Uri.fromFile(file));

        // 设置一些基本显示信息
        req.setTitle("YunXianSheng_apk");
        req.setDescription("YunXianSheng_apk");
        req.setMimeType("application/vnd.android.package-archive");

        //加入下载队列
        downloadId = downloadManager.enqueue(req);

        //long downloadId = dm.enqueue(req);
        //Log.d("DownloadManager", downloadId + "");
        //dm.openDownloadedFile()
    }

    public long getDownloadId() {
        return downloadId;
    }


//    /**
//     * 打开安装apk文件
//     * @param context
//     * @param file
//     */
//    private void openApkFile(Context context,File file) {
//        Log.e("OpenFile", file.getName());
//        Intent intent = new Intent();
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setAction(android.content.Intent.ACTION_VIEW);
//        intent.setDataAndType(Uri.fromFile(file),
//                "application/vnd.android.package-archive");
//        context.startActivity(intent);
//    }


    private void installFile(long id) {
//        Intent install = new Intent(Intent.ACTION_VIEW);
//        Uri downloadFileUri = downloadManager.getUriForDownloadedFile(id);
//        DLog.i("downloadFileUri---------->>>" + downloadFileUri);
//        DLog.i("apkPath---------->>>" + apkPath);
//        Uri uri = Uri.parse("file://" + downloadFileUri);
//        install.setDataAndType(uri, "application/vnd.android.package-archive");
//        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(install);
        File file = new File(apkPath);
        if(!file.exists()){
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 下载完成广播监听
     */
    private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (downloadId == id) {
                    // 是我们创建的下载任务id
                    installFile(id);
                }
            }
//            else if (intent.getAction().equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)) {
//                Log.i("lilijun", "接收到点击下载通知栏广播");
//                String extraID = DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS;
//                long[] references = intent.getLongArrayExtra(extraID);
//                for (long reference : references)
//                    if (reference == downloadId) {
//                        // 点击的通知栏是我们
//                        // 是我们创建的下载任务id
//                        try {
//                            downloadManager.openDownloadedFile(downloadId);
//                        } catch (Exception e) {
//                            Toast.makeText(context, context.getResources().getString(R.string
//                                    .no_apk_file), Toast.LENGTH_SHORT).show();
//                            e.printStackTrace();
//                        }
//                    }
//            }
        }
    };
}
