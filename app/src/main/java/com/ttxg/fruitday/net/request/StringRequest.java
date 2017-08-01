package com.ttxg.fruitday.net.request;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.ttxg.fruitday.util.Util;

/**
 * 重写StringRequest，保持会话
 * Created by lilijun on 2016/12/1.
 */
public class StringRequest extends com.android.volley.toolbox.StringRequest {
    /**
     * @param method
     * @param url           //     * @param params
     *                      //     *            A {@link HashMap} to post with the request. Null
     *                      is allowed
     *                      //     *            and indicates no parameters will be posted along
     *                      with request.
     * @param listener
     * @param errorListener
     */
    public StringRequest(int method, String url, Response.Listener<String> listener,
                         Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    /* (non-Javadoc)
     * @see com.android.volley.toolbox.StringRequest#parseNetworkResponse(com.android.volley
     * .NetworkResponse)
     */
    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        // since we don't know which of the two underlying network vehicles
        // will Volley use, we have to handle and store session cookies manually
        Util.checkSessionCookie(response.headers);
        return super.parseNetworkResponse(response);
    }

    /* (non-Javadoc)
     * @see com.android.volley.Request#getHeaders()
     */
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = super.getHeaders();
        if (headers == null
                || headers.equals(Collections.emptyMap())) {
            headers = new HashMap<>();
        }
        Util.addSessionCookie(headers);
        return headers;
    }
}
