package com.ttxg.fruitday.net.request;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by yb on 2016/9/13.
 */
public class NormalPostRequest extends Request<JSONObject> {
//    private Map<String, String> mMap;
//    private Response.Listener<JSONObject> mListener;
//    public NormalPostRequest(String url, Response.Listener<JSONObject> listener, Response
// .ErrorListener errorListener, Map<String, String> map) {
//        super(Request.Method.POST, url, errorListener);
//        mListener = listener;
//        mMap = map;
//    }
//
//    //mMap是已经按照前面的方式,设置了参数的实例
//    @Override
//    protected Map<String, String> getParams() throws AuthFailureError {
//        return mMap;
//    }
//    @Override
//    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
//        try {
//            String jsonString = new String(response.data, HttpHeaderParser.parseCharset
// (response.headers));
//            return Response.success(new JSONObject(jsonString),HttpHeaderParser
// .parseCacheHeaders(response));
//        } catch (UnsupportedEncodingException e) {
//            return Response.error(new ParseError(e));
//        } catch (JSONException je) {
//            return Response.error(new ParseError(je));
//        }
//    }
//
//    @Override
//    protected void deliverResponse(JSONObject response) {
//        mListener.onResponse(response);
//    }

    private Map<String, String> mMap;
    private Response.Listener<JSONObject> mListener;


    public NormalPostRequest(String url, Response.Listener<JSONObject> listener, Response
            .ErrorListener errorListener, Map map) {
        super(Request.Method.POST, url, errorListener);
        mListener = listener;
        mMap = map;

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mMap;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString =
                    new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        mListener.onResponse(response);

    }
}
