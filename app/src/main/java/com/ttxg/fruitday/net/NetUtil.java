package com.ttxg.fruitday.net;

import android.graphics.Bitmap;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import com.ttxg.fruitday.manager.NetManager;
import com.ttxg.fruitday.net.request.FormFile;
import com.ttxg.fruitday.net.request.PostUploadRequest;
import com.ttxg.fruitday.util.Constants;
import com.ttxg.fruitday.util.Util;
import com.ttxg.fruitday.util.log.DLog;


/**
 * Volley网络请求
 * Created by lilijun on 2016/8/11.
 */
public class NetUtil {
    /**
     * String类型的请求（Get方式，不带参数）
     *
     * @param tag
     */
    public static void requestStringGet(String tag, Response.Listener<String> listener, Response
            .ErrorListener errorListener) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.MAIN_URL +
                tag, listener, errorListener);
        startRequest(stringRequest);
    }

    /**
     * String类型的请求(Get方式，带参数)
     *
     * @param tag
     * @param parmMap
     */
    public static void requestStringGet(String tag, final Map<String, Object> parmMap, Response
            .Listener<String> listener, Response.ErrorListener errorListener) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Util.getURL(Constants
                .MAIN_URL + tag, parmMap), listener, errorListener);
        startRequest(stringRequest);
    }

    /**
     * String类型的请求（Post方式，不带参数）
     *
     * @param tag
     */
    public static void requestStringPost(String tag, Response.Listener<String> listener, Response
            .ErrorListener errorListener) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.MAIN_URL +
                tag, listener, errorListener);
        startRequest(stringRequest);
    }

    /**
     * String类型的请求(Post方式，带参数)
     *
     * @param tag
     * @param parmMap
     */
    public static void requestStringPost(String tag, final Map<String, String> parmMap, Response
            .Listener<String> listener, Response.ErrorListener errorListener) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.MAIN_URL +
                tag, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return parmMap;
            }
        };
        startRequest(stringRequest);
    }

    /**
     * 返回JsonObject类型的请求(请求参数没有json数据)
     *
     * @param tag
     */
    public static void requestJsonObject(String tag, Response.Listener<JSONObject> listener,
                                         Response.ErrorListener errorListener) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                Constants.MAIN_URL + tag, null, listener, errorListener) {
        };
        startRequest(jsonObjectRequest);
    }

    /**
     * 返回JsonObject类型的请求(请求参数json数据)
     *
     * @param tag
     */
    public static void requestJsonObject(String tag, JSONObject json, Response
            .Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                Constants.MAIN_URL + tag, json, listener, errorListener);
        startRequest(jsonObjectRequest);
    }

//    public static void requestJsonObjectByPost(String tag, Map<String, String> params, Response
//            .Listener<JSONObject> listener, Response.ErrorListener errorListener) {
//        JSONObject jsonObject = new JSONObject(params);
//        DLog.i("lilijun","jsonObject.toString()----->>"+jsonObject.toString());
//        JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST,
// Constants.MAIN_URL + tag,
//                jsonObject,
//                listener, errorListener) {
//            @Override
//            public Map<String, String> getHeaders() {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Accept", "application/json");
//                headers.put("Content-Type", "application/json; charset=UTF-8");
//                return headers;
//            }
//        };
////        Request<JSONObject> jsonRequest = new NormalPostRequest(Constants.MAIN_URL + tag,
// listener,errorListener,params);
//        startRequest(jsonRequest);
//    }


    /**
     * 返回JsonArray类型的请求（请求参数没有JsonArray数据）
     *
     * @param tag
     */
    public static void requestJsonArray(String tag, Response.Listener<JSONArray> listener,
                                        Response.ErrorListener errorListener) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, Constants
                .MAIN_URL + tag, null, listener, errorListener);
        startRequest(jsonArrayRequest);
    }

    /**
     * 返回JsonArray类型的请求（请求参数有JsonArray数据）
     *
     * @param tag
     * @param array
     */
    public static void requestJsonArray(String tag, JSONArray array, Response.Listener<JSONArray>
            listener, Response.ErrorListener errorListener) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, Constants
                .MAIN_URL + tag, array, listener, errorListener);
        startRequest(jsonArrayRequest);
    }

    /**
     * 加载图片请求
     *
     * @param url
     * @param maxWidth
     * @param maxHeight
     * @param bimapConfig
     * @param listener
     * @param errorListener
     */
    public static void reqeustImage(String url, int maxWidth, int maxHeight, Bitmap.Config
            bimapConfig, Response.Listener<Bitmap> listener, Response.ErrorListener errorListener) {
        ImageRequest imageRequest = new ImageRequest(url, listener, maxWidth, maxHeight,
                bimapConfig, errorListener);
        startRequest(imageRequest);
    }


    /**
     * String类型的请求（Get方式，纯url）
     */
    public static void requestUrl(String url, Response.Listener<String> listener, Response
            .ErrorListener errorListener) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, listener,
                errorListener);
        startRequest(stringRequest);
    }

    /**
     * 上传文件接口
     */
    public static void requestPostUploadRequest(String url, List<FormFile> files, final
    Map<String, String> parmMap, Response.Listener listener, Response.ErrorListener errorListener) {
        Request request = new PostUploadRequest(Constants.MAIN_URL + url, files, listener,
                errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return parmMap;
            }
        };
        startRequest(request);
    }

    /**
     * 开始请求
     *
     * @param request
     */
    private static void startRequest(Request request) {
        DLog.i("lilijun", "requestUrl------>>>" + request.getUrl());
        // 将请求加入队列
        NetManager.getInstance().addToRequestQueue(request);
//        // 开始发起请求
//        NetManager.getInstance().getRequestQueue().start();
    }

}
