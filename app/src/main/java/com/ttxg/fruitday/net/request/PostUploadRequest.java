package com.ttxg.fruitday.net.request;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 上传文件的Request
 * Created by lilijun on 2016/11/10.
 */
public class PostUploadRequest extends Request<String> {
    /**
     * 正确数据的时候回掉用
     */
//    private Response.ErrorListener mListener;
    private Response.Listener mListener;
    /*请求 数据通过参数的形式传入*/
    private List<FormFile> mListItem;

    private String BOUNDARY = "--------------520-13-14"; //数据分隔线
    private String MULTIPART_FORM_DATA = "multipart/form-data";

    public PostUploadRequest(String url, List<FormFile> listItem, Response.Listener listener,
                             Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        this.mListener = listener;
        setShouldCache(false);
        mListItem = listItem;
//        //设置请求的响应事件，因为文件上传需要较长的时间，所以在这里加大了，设为5秒
//        setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    /**
     * 这里开始解析数据
     *
     * @param response Response from the network
     * @return
     */
    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            String mString =
                    new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(mString,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    /**
     * 回调正确的数据
     *
     * @param response The parsed response returned by
     */
    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        if (mListItem == null || mListItem.size() == 0) {
            return super.getBody();
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int N = mListItem.size();
        FormFile formFile;
        for (int i = 0; i < N; i++) {
            formFile = mListItem.get(i);
            StringBuffer sb = new StringBuffer();
            /*第一行*/
            //`"--" + BOUNDARY + "\r\n"`
            sb.append("--" + BOUNDARY);
            sb.append("\r\n");
            /*第二行*/
            //Content-Disposition: form-data; name="参数的名称"; filename="上传的文件名" + "\r\n"
            sb.append("Content-Disposition: form-data;");
            sb.append(" name=\"");
            sb.append(formFile.getName());
            sb.append("\"");
            sb.append("; filename=\"");
            sb.append(formFile.getFileName());
            sb.append("\"");
            sb.append("\r\n");
            /*第三行*/
            //Content-Type: 文件的 mime 类型 + "\r\n"
            sb.append("Content-Type: ");
            sb.append(formFile.getMime());
            sb.append("\r\n");
            /*第四行*/
            //"\r\n"
            sb.append("\r\n");
            try {
                bos.write(sb.toString().getBytes("utf-8"));
                /*第五行*/
                //文件的二进制数据 + "\r\n"
                bos.write(formFile.getValue());
                bos.write("\r\n".getBytes("utf-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        /*结尾行*/
        //`"--" + BOUNDARY + "--" + "\r\n"`
        String endLine = "--" + BOUNDARY + "--" + "\r\n";
        try {
            bos.write(endLine.toString().getBytes("utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bos.toByteArray();
    }

    //Content-Type: multipart/form-data; boundary=----------8888888888888
    @Override
    public String getBodyContentType() {
        return MULTIPART_FORM_DATA + "; boundary=" + BOUNDARY;
    }
}
