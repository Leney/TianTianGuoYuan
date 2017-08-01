package com.ttxg.fruitday.util;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.Map;

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.callback.OnLocationLinstener;
import com.ttxg.fruitday.manager.NetManager;
import com.ttxg.fruitday.util.log.DLog;


/**
 * 工具类
 * Created by yb on 2016/8/16.
 */
public class Util {
    private static final String TAG = "Util";

    /**
     * 清除图片加载器Fresco的缓存
     */
    public static void clearFrescoCache() {
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        imagePipeline.clearMemoryCaches();
        imagePipeline.clearDiskCaches();
        imagePipeline.clearCaches();
    }

    /**
     * 清除Volley的缓存数据
     */
    public static void clearVolleyCache() {
        NetManager.getInstance().getRequestQueue().getCache().clear();
    }

    /**
     * 获取应用缓存大小
     *
     * @param context
     * @return
     * @throws Exception
     */
    public static String getTotalCacheSize(Context context) throws Exception {
        long cacheSize = getFolderSize(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cacheSize += getFolderSize(context.getExternalCacheDir());
        }
        if (!TextUtils.isEmpty(DLog.logDirPath)) {
            cacheSize += getFolderSize(new File(DLog.logDirPath));
        }
        return getFormatSize(cacheSize);
    }

    /**
     * 清空应用所有的缓存文件
     * TODO 需要过滤不希望清除的文件
     *
     * @param context
     */
    public static void clearAllCache(Context context) {
        deleteDir(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteDir(context.getExternalCacheDir());
        }
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    // 获取文件
    //Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
    //Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

//    /**
//     * 格式化单位
//     *
//     * @param size
//     * @return
//     */
//    public static String getFormatSize(double size) {
//        double kiloByte = size / 1024;
//        if (kiloByte < 1) {
////            return size + "Byte";
//            return "0K";
//        }
//
//        double megaByte = kiloByte / 1024;
//        if (megaByte < 1) {
//            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
//            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
//                    .toPlainString() + "KB";
//        }
//
//        double gigaByte = megaByte / 1024;
//        if (gigaByte < 1) {
//            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
//            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
//                    .toPlainString() + "MB";
//        }
//
//        double teraBytes = gigaByte / 1024;
//        if (teraBytes < 1) {
//            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
//            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
//                    .toPlainString() + "GB";
//        }
//        BigDecimal result4 = new BigDecimal(teraBytes);
//        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
//                + "TB";
//    }

    /**
     * 文件大小单位转换
     *
     * @param size
     * @return
     */
    public static String getFormatSize(long size) {
        DecimalFormat df = new DecimalFormat("###.##");
        float f = ((float) size / (float) (1024 * 1024));

        if (f < 1.0) {
            float f2 = ((float) size / (float) (1024));

            return df.format(new Float(f2).doubleValue()) + "KB";

        } else {
            return df.format(new Float(f).doubleValue()) + "MB";
        }

    }


    public static String getURL(String mainUrl, Map<String, Object> parms) {
        StringBuffer sb = new StringBuffer(mainUrl);
        if (parms != null && !parms.isEmpty() && parms.size() > 0) {
            sb.append("?");
            int length = 1;
            for (Map.Entry<String, Object> entry : parms.entrySet()) {
                sb.append(entry.getKey());
                sb.append("=");
                sb.append(entry.getValue());
                if (length != parms.size()) {
                    sb.append("&");
                }
                length++;
            }
        }
//        String url = sb.toString();
        String url = "";
        try {
            url = new String(sb.toString().getBytes(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        DLog.i("lilijun", "请求地址---url--->>" + url);
        return url;
    }


    /**
     * 隐藏软键盘
     *
     * @param inputEdit
     */
    public static void hideInput(Context context, EditText inputEdit) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context
                .INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(inputEdit.getWindowToken(), InputMethodManager
                            .HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 将米转成km
     *
     * @param distance
     * @return
     */
    public static String formatDistance(int distance) {
        DecimalFormat df = new DecimalFormat(".##");
        double format = distance / 1000.0;
        return df.format(format) + "km";
    }


    /**
     * 获取定位信息（经纬度）
     *
     * @param context
     * @return
     */
    public static void getLocation(Context context, final OnLocationLinstener linstener) {
        // 获取位置管理服务
        LocationManager locationManager;
        String serviceName = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) context.getSystemService(serviceName);
        // 查找到服务信息
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE); // 高精度
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW); // 低功耗
        String provider = locationManager.getBestProvider(criteria, true); // 获取GPS信息
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED) {
                Location location = locationManager.getLastKnownLocation(provider); // 通过GPS获取位置
                if (location != null) {
                    double[] locations = new double[2];
                    // 纬度
                    locations[0] = location.getLatitude();
                    // 经度
                    locations[1] = location.getLongitude();

                    linstener.onLocationSuccess(locations);
                } else {
                    linstener.onLocationFailed();
                }
//                // 设置监听*器，自动更新的最小时间为间隔N秒(1秒为1*1000，这样写主要为了方便)或最小位移变化超过N米
//                locationManager.requestLocationUpdates(provider, 100 * 1000, 500,
// locationListener);
            } else {
                // 没有权限
                linstener.noPermisstion();
            }
        } else {
            Location location = locationManager.getLastKnownLocation(provider); // 通过GPS获取位置
            if (location != null) {
                double[] locations = new double[2];
                // 纬度
                locations[0] = location.getLatitude();
                // 经度
                locations[1] = location.getLongitude();
                linstener.onLocationSuccess(locations);
            } else {
                linstener.onLocationFailed();
            }
//            // 设置监听*器，自动更新的最小时间为间隔N秒(1秒为1*1000，这样写主要为了方便)或最小位移变化超过N米
//            locationManager.requestLocationUpdates(provider, 100 * 1000, 500,locationListener);
        }
    }


    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示网络处理发生错误时的消息
     *
     * @param msg            服务器返回的错误消息
     * @param defaultMessage 默认的错误消息
     */
    public static void showErrorMessage(Context context, String msg, String defaultMessage) {
        if (TextUtils.isEmpty(msg)) {
            showToast(context, defaultMessage);
        } else {
            showToast(context, msg);
        }
    }

    /**
     * 显示网络处理发生错误时的消息
     *
     * @param msg 服务器返回的错误消息
     */
    public static void showErrorMessage(Context context, String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        showToast(context, msg);
    }

    /**
     * 通过错误码，显示相应的错误提示信息
     *
     * @param errorCode
     */
    public static void toastMsgByErrorCode(Context context, int errorCode) {
        switch (errorCode) {
            case 1001:
                // 用户不存在
                showToast(context, context.getResources().getString(R.string.no_user));
                break;
            case 1002:
                // 用户名称或密码不正确
                showToast(context, context.getResources().getString(R.string
                        .login_name_or_pwd_wrong));
                break;
            case 1003:
                // 用户已过期
                showToast(context, context.getResources().getString(R.string.user_passed));
                break;
            case 1004:
                // 用户状态异常
                showToast(context, context.getResources().getString(R.string.user_exception));
                break;
            case 1005:
                // 用户已注册
                showToast(context, context.getResources().getString(R.string.already_have_user));
                break;
            case 2001:
                // 类目不存在
                break;
            case 2002:
                // 类目已存在
                break;
            case 2003:
                // 同级别类目名称不能相同
                break;
            case 3001:
                // 商品新增失败
                break;
            case 3002:
                // 商品不存在
                break;
            case 3003:
                // 商品已存在
                break;
            case 3004:
                // 商品有效期已过
                break;
            case 3005:
                // 已是最左位置
                break;
            case 3009:
                // 商品库存不足
                break;
            case 9902:
                // 输入参数为空
                break;
            case 9903:
                // 会话已过期
                break;
            case -1001:
                // 网络错误，无网络
                break;
            case -1002:
                // 无法连接到服务器
                break;
            case -1003:
                // 服务器报错
                break;
        }
    }


    /**
     * 显示弹出警告框
     *
     * @param context
     * @param title
     * @param message
     * @param positiveName
     * @param negativeName
     * @param positiveClickListener
     * @param negativeListener
     */
    public static void showAlertDialog(Context context, String title, String message, String
            positiveName, String negativeName, DialogInterface.OnClickListener
                                                 positiveClickListener, DialogInterface
            .OnClickListener negativeListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setPositiveButton(positiveName, positiveClickListener);
        builder.setNegativeButton(negativeName, negativeListener);
        builder.create().show();
    }

    /**
     * 显示请稍候。。的dialog
     * @param context
     * @param progressDialog
     * @return
     */
    public static ProgressDialog showLoadingDialog(Context context,ProgressDialog progressDialog){
        if (progressDialog == null) {
            progressDialog = ProgressDialog.show(context, null,
                    context.getResources().getString(R.string.please_waitting), true, true);
        }
        progressDialog.show();
        return progressDialog;
    }

    /**
     * 保存缓存数据到文件
     */
    public static boolean saveCachDataToFile(Context context, String fileName, Object object) {
        if (object == null) {
            return false;
        }
        try {
            // 需要一个文件输出流和对象输出流；文件输出流用于将字节输出到文件，对象输出流用于将对象输出为字节
            FileOutputStream fout = context.openFileOutput(fileName + ".ser",
                    Activity.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fout);
            out.writeObject(object);
            out.close();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "保存缓存文件时发生异常#Exception:\n", e);
        }
        return false;
    }

    /**
     * 从文件里面获取缓存数据
     *
     * @param context
     * @param fileName
     * @return
     */
    public static Object getCacheDataFromFile(Context context, String fileName) {
        Object object = null;
        try {
            FileInputStream fin = context.openFileInput(fileName + ".ser");
            ObjectInputStream in = new ObjectInputStream(fin);
            object = in.readObject();
        } catch (Exception e) {
            Log.e(TAG, "获取缓存文件对象时发生异常#Exception:\n", e);
        }
        return object;
    }

    /**
     * 将Object对象转成String
     *
     * @param object
     * @return
     */
    public static String getStringByObject(Object object) {
        return object == null ? "" : object.toString();
    }


    //获取版本号
    public static int getVersionCode(Context context){
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        }catch(PackageManager.NameNotFoundException e){
            return 0;
        }
    }

    /**
     * 文件大小单位转换
     * @param size
     * @return
     */
    public static String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;
        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else
            return String.format("%d B", size);
    }

    /**
     * 计算文件或者文件夹的大小 ，单位 MB
     * @param file 要计算的文件或者文件夹 ， 类型：java.io.File
     * @return 大小，单位：MB
     */
    public static long getFileSize(File file) {
        //判断文件是否存在
        if (file.exists()) {
            //如果是目录则递归计算其内容的总大小，如果是文件则直接返回其大小
            if (!file.isFile()) {
                //获取文件大小
                File[] fl = file.listFiles();
                long ss = 0L;
                for (File f : fl)
                    ss += getFileSize(f);
                return ss;
            } else {
                long ss = file.length();
                return ss;
            }
        } else {
            System.out.println("文件或者文件夹不存在，请检查路径是否正确！");
            return 0L;
        }
    }

    /**
     * 删除文件夹下所有文件
     * @param file
     */
    public static void deleteAllFiles(File file){
        if(!file.exists()){
            return;
        }
        if(file.isFile() || file.list().length ==0)
        {
            file.delete();
        }else{
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                deleteAllFiles(files[i]);
                files[i].delete();
            }
            if(file.exists())         //如果文件本身就是目录 ，就要删除目录
                file.delete();
        }
    }


    /**
     * 检查session 将得到seesion保存到本地
     * Checks the response headers for session cookie and saves it
     * if it finds it.
     *
     * @param headers Response Headers.
     */
    public static void checkSessionCookie(Map<String, String> headers) {
        if (headers.containsKey(Constants.SET_COOKIE_KEY) && headers.get(Constants
                .SET_COOKIE_KEY).startsWith(Constants.SESSION_COOKIE)) {
            String cookie = headers.get(Constants.SET_COOKIE_KEY);
            if (cookie.length() > 0) {
                String[] splitCookie = cookie.split(";");
                String[] splitSessionId = splitCookie[0].split("=");
                cookie = splitSessionId[1];
                DLog.i("lilijun", "checkSessionCookie----cookie--->>>" + cookie);
                // 将获取到的SessionId保存至本地
                PreferencesUtils.putString(Constants.SESSION_COOKIE, cookie);
            }
        }
    }

    /**
     * 组织网络请求session数据，设置本地保存好的sessionid,保持会话
     * Adds session cookie to headers if exists.
     *
     * @param headers
     */
    public static void addSessionCookie(Map<String, String> headers) {
        // 获取本地保存的sessionId
        String sessionId = PreferencesUtils.getString(Constants.SESSION_COOKIE, "");
        DLog.i("lilijun", "addSessionCookie----sessionId--->>>" + sessionId);
        if (sessionId.length() > 0) {
            // 组织headers消息，保持会话
            StringBuilder builder = new StringBuilder();
            builder.append(Constants.SESSION_COOKIE);
            builder.append("=");
            builder.append(sessionId);
            if (headers.containsKey(Constants.COOKIE_KEY)) {
                builder.append("; ");
                builder.append(headers.get(Constants.COOKIE_KEY));
            }
            headers.put(Constants.COOKIE_KEY, builder.toString());
        }
    }


}
