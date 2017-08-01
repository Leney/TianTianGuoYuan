package com.ttxg.fruitday.manager;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.Volley;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.DefaultHttpClient;


/**
 * 网络管理类
 * Created by yb on 2016/8/11.
 */
public class NetManager {
    public static final String TAG = "NetManager";
    private static NetManager instance;

    private RequestQueue requestQueue;
//    private ImageLoader imageLoader;

//    private static AbstractHttpClient mHttpClient;

    private RetryPolicy retryPolicy;

    private NetManager() {

    }

    public static NetManager getInstance() {
        if (instance == null) {
            synchronized (NetManager.class) {
                instance = new NetManager();
            }
        }
        return instance;
    }

    public void init(Context context) {
//        mHttpClient = new DefaultHttpClient();
//        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
//        requestQueue = Volley.newRequestQueue(context, new HttpClientStack(mHttpClient));
//        imageLoader = new ImageLoader(requestQueue, new BitmapLruCache());
//        retryPolicy = new DefaultRetryPolicy(50000, DefaultRetryPolicy
//                .DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        DefaultHttpClient httpclient = new DefaultHttpClient();
        CookieStore cookieStore = new PreferencesCookieStore(context);
        httpclient.setCookieStore(cookieStore);
        HttpStack httpStack = new HttpClientStack(httpclient);
        requestQueue = Volley.newRequestQueue(context,httpStack);

//        imageLoader = new ImageLoader(requestQueue, new BitmapLruCache());
        retryPolicy = new DefaultRetryPolicy(50000, DefaultRetryPolicy
                .DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

//    public ImageLoader getImageLoader() {
//        return imageLoader;
//    }

    public <T> void addToReqeustQueue(Request<T> request, String tag) {
        // 设置标志
        request.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

//        /* 解决重复请求问题 */
//        request.setRetryPolicy(new DefaultRetryPolicy(5 * 1000,0,1.0f){
//            //Volley默认尝试两次，MAX=1,count=0;count<=MAX;count++;count=2时，
//            // 表示当前已经重复请求两次，就不会再第三次重复请求，从而屏蔽掉Volley的自动重复请求功能；
//            @Override
//            public int getCurrentRetryCount() {
////                return super.getCurrentRetryCount();
//                return 2;
//            }
//        });
        request.setRetryPolicy(retryPolicy);
        requestQueue.add(request);
    }

    public <T> void addToRequestQueue(Request<T> req) {
//        // 设置默认的tag标志
//        req.setTag(TAG);
////        req.setRetryPolicy(new DefaultRetryPolicy(5 * 1000,1,1.0f));
//        requestQueue.add(req);
        addToReqeustQueue(req, "");
    }

    /**
     * 关闭所有请求
     *
     * @param tag 标志
     */
    public void cancleRequests(Object tag) {
        if (requestQueue != null) {
            requestQueue.cancelAll(tag);
        }
    }
}
