package com.ttxg.fruitday.net.request;

import android.graphics.Bitmap;

import java.io.File;

/**
 * 上传文件的封装类
 * Created by lilijun on 2016/11/10.
 */
public class FormFile {
    //参数的名称
    private String paramName;
    //文件名
    private String mFileName;
    //文件的 mime(相当于上传文件的类型)，需要根据文档查询
    private String mMime;
//    //需要上传的图片资源，因为这里测试为了方便起见，直接把 bigmap 传进来，真正在项目中一般不会这般做，而是把图片的路径传过来，在这里对图片进行二进制转换
//    private Bitmap mBitmap ;

    private byte[] bytes;

//    private File file;

//    /** 标识是否是文件格式  true=文件类型，false=bitmap类型*/
//    private boolean isFile;
    //    public FormFile(Bitmap mBitmap) {
//        this.mBitmap = mBitmap;
//    }
    public FormFile(String paramName, String mFileName,String mMime, File file) {
        this.paramName = paramName;
        this.mFileName = mFileName;
        this.mMime = mMime;
//        this.file = file;
//        this.isFile = true;
    }

    public FormFile(String paramName, String mFileName, String mMime, Bitmap mBitmap) {
        this.paramName = paramName;
        this.mFileName = mFileName;
        this.mMime = mMime;
//        this.mBitmap = mBitmap;
//        this.isFile = false;
    }

    public FormFile(String paramName, String mFileName, String mMime, byte[] bytes) {
        this.paramName = paramName;
        this.mFileName = mFileName;
        this.mMime = mMime;
        this.bytes = bytes;
//        this.isFile = false;
    }

    public String getName() {
        return paramName;
//        return isFile ? file.getName() : paramName;
    }

    public String getFileName() {
        return mFileName;
//        return isFile ? file.getName() : mFileName;
    }
//    //对图片进行二进制转换
//    public byte[] getValue() {
//        ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
//        mBitmap.compress(Bitmap.CompressFormat.JPEG,100,bos) ;
//        return bos.toByteArray();
//    }

    public byte[] getValue() {
//        return isFile ? getBytes(file) : getBitmapBytes(mBitmap);
        return bytes;
    }

    //因为我知道是 png 文件，所以直接根据文档查的
    public String getMime() {
//        return "image/jpg";
        return mMime;
    }

//    /**
//     * 获得指定文件的byte数组
//     */
//    private byte[] getBytes(File file) {
//        byte[] buffer = null;
//        try {
//            FileInputStream fis = new FileInputStream(file);
//            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
//            byte[] b = new byte[1000];
//            int n;
//            while ((n = fis.read(b)) != -1) {
//                bos.write(b, 0, n);
//            }
//            fis.close();
//            bos.close();
//            buffer = bos.toByteArray();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return buffer;
//    }

//    /**
//     * 将Bitmap转换为数组
//     * @param bmp
//     * @return
//     */
//    private byte[] getBitmapBytes(Bitmap bmp) {
//        if (bmp == null) return null;
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        return baos.toByteArray();
//    }
}
