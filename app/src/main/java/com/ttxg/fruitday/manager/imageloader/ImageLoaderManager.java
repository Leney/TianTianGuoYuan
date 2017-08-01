package com.ttxg.fruitday.manager.imageloader;

/**
 * Volley图片加载管理器
 * Created by yb on 2016/8/12.
 */
public class ImageLoaderManager {
//    private static ImageLoaderManager instance;
//    private ImageLoader.ImageListener listener;
//    /** 默认加载的图片资源*/
//    private int defaultRes = R.mipmap.ic_launcher;
//    /** 加载失败的图片资源*/
//    private int failedRes = R.mipmap.ic_launcher;
//    private ImageLoaderManager(){
//
//    }
//    public static ImageLoaderManager getInstance(){
//        if(instance == null){
//            synchronized (ImageLoaderManager.class){
//                instance = new ImageLoaderManager();
//            }
//        }
//        return  instance;
//    }

//    /**
//     * 使用默认的图片加载配置
//     * @param imageView
//     *          目标ImageView
//     * @param url
//     *          图片加载地址
//     */
//    public void loadImage(ImageView imageView,String url){
//        loadImage(imageView, url, defaultRes,failedRes,0,0);
//    }
//
//    /**
//     * 自定义图片加载配置(适配imageView的宽和高)
//     * @param imageView
//     *          目标ImageView
//     * @param url
//     *          图片加载地址
//     * @param defaultImgRes
//     *          默认显示的图片
//     * @param failedImgRes
//     *          加载失败时显示的图片
//     */
//    public void loadImage(ImageView imageView,String url,int defaultImgRes,int failedImgRes){
//        loadImage(imageView,url,defaultImgRes,failedImgRes,0,0);
//    }

//    /**
//     * 自定义加载图片配置(指定加载图片的宽和高)
//     * @param imageView
//     * @param url
//     * @param defaultImgRes
//     * @param failedImgRes
//     * @param width
//     * @param height
//     */
//    public void loadImage(ImageView imageView,String url,int defaultImgRes,int failedImgRes,int width,int height){
//        listener = ImageLoader.getImageListener(imageView,defaultImgRes,failedImgRes);
//        if(width == 0 && height == 0){
//            NetManager.getInstance().getImageLoader().get(url,listener);
//        }else {
//            NetManager.getInstance().getImageLoader().get(url,listener,width,height);
//        }
//    }
}
