package com.ttxg.fruitday.gui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.gui.view.BaseTitleView;
import com.ttxg.fruitday.manager.UserInfoManager;
import com.ttxg.fruitday.net.NetErrorHelper;
import com.ttxg.fruitday.net.NetUtil;
import com.ttxg.fruitday.util.Constants;
import com.ttxg.fruitday.util.log.DLog;

/**
 * Activity 基类
 * Created by yb on 2016/8/18.
 */
public abstract class BaseActivity extends AppCompatActivity {
    public static final String TAG = "BaseActivity";
    private BaseTitleView titleView;
    private ProgressBar loadingView;
    private RelativeLayout centerLay;
    private TextView errorView;
    /**
     * 子视图有可能增加视图的区域(不想让loadingView,ErrorView覆盖的)
     */
    private RelativeLayout childAddLay;
    /**
     * 没有数据的视图
     */
    private RelativeLayout noDataView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        titleView = (BaseTitleView) findViewById(R.id.base_activity_title);
        loadingView = (ProgressBar) findViewById(R.id.base_activity_loading);
        centerLay = (RelativeLayout) findViewById(R.id.base_activity_center);
        errorView = (TextView) findViewById(R.id.base_activity_error);
        errorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tryAgain();
            }
        });
        childAddLay = (RelativeLayout) findViewById(R.id.base_activity_add_lay);
        noDataView = (RelativeLayout) findViewById(R.id.base_activity_no_data);
        noDataView.setVisibility(View.GONE);
        showLoadingView();
        initView();
    }

    /**
     * 设置中间试图
     *
     * @param layoutId
     */
    protected void setCenterView(int layoutId) {
        View view = View.inflate(this, layoutId, null);
        setCenterView(view);
    }

    /**
     * 设置中间试图
     *
     * @param view
     */
    protected void setCenterView(View view) {
        setCenterView(view, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams
                .MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT));
    }

    /**
     * 设置中间试图
     *
     * @param view
     * @param params
     */
    protected void setCenterView(View view, ViewGroup.LayoutParams params) {
        centerLay.addView(view, params);
    }

    /**
     * 设置顶部子视图需添加的视图
     *
     * @param layoutId
     */
    protected void setAddView(int layoutId) {
        View view = View.inflate(this, layoutId, null);
    }

    /**
     * 设置顶部子视图需添加的视图
     */
    protected void setAddView(View view) {
        setAddView(view, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT));
    }

    /**
     * 设置顶部子视图是否显示
     * @param visible
     */
    protected void setAddViewVisible(boolean visible){
        childAddLay.setVisibility(visible ? View.VISIBLE:View.GONE);
    }

    /**
     * 设置无数据时的视图
     *
     * @param layoutId
     */
    protected void addNoDataView(int layoutId) {
        addNoDataView(View.inflate(this, layoutId, null));
    }

    /**
     * 设置无数据时的视图
     *
     * @param view
     */
    protected void addNoDataView(View view) {
        addNoDataView(view, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams
                .WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT));
    }

    /**
     * 设置无数据时的视图
     */
    protected void addNoDataView(View view, ViewGroup.LayoutParams params) {
        noDataView.removeAllViews();
        noDataView.addView(view, params);
//        childAddLay.setVisibility(View.VISIBLE);
    }

    /**
     * 设置顶部子视图需添加的视图
     */
    protected void setAddView(View view, ViewGroup.LayoutParams params) {
        childAddLay.addView(view, params);
        childAddLay.setVisibility(View.VISIBLE);
    }

    /**
     * 初始化视图
     */
    protected abstract void initView();

    /**
     * 获取网络数据(Get方式)
     */
    protected void loadDataGet(final String tag, Map<String, Object> parmMap) {
        if (parmMap == null) {
//            NetUtil.requestStringGet(tag, new Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
//                    doRequestSuccess(tag, response);
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    int errorCode = NetErrorHelper.getCode(error);
//                    DLog.i(TAG, "errorCode-------->>>" + errorCode);
//                    onLoadDataError(tag, errorCode, "");
//                }
//            });

            parmMap = new HashMap<>();
        }
        parmMap.put("appId", Constants.YB_APP_ID);
        NetUtil.requestStringGet(tag, parmMap, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                doRequestSuccess(tag, response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                int errorCode = NetErrorHelper.getCode(error);
                DLog.i(TAG, "errorCode-------->>>" + errorCode);
                onLoadDataError(tag, errorCode, "");
            }
        });
    }

    /**
     * 获取网络数据(Post方式)
     */
    protected void loadDataPost(final String tag, Map<String, String> parmMap) {
        if (parmMap == null) {
//            NetUtil.requestStringPost(tag, new Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
//                    doRequestSuccess(tag, response);
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    int errorCode = NetErrorHelper.getCode(error);
//                    DLog.i(TAG, "errorCode-------->>>" + errorCode);
//                    onLoadDataError(tag, errorCode, "");
//                }
//            });
            parmMap = new HashMap<>();
        }
        parmMap.put("appId", Constants.YB_APP_ID);
        NetUtil.requestStringPost(tag, parmMap, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                doRequestSuccess(tag, response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                int errorCode = NetErrorHelper.getCode(error);
                DLog.i(TAG, "errorCode-------->>>" + errorCode);
                onLoadDataError(tag, errorCode, "");
            }
        });
    }

    /**
     * 处理网络请求成功的事情(仅仅只是和服务器通信成功，但还是会出现其它功能逻辑的错误的)
     */
    private void doRequestSuccess(String tag, String response) {
        DLog.i(TAG, "response-------->>>" + response);
        int code = -1;
        String errorMsg = "";
        JSONObject baseObject = null;
        try {
            JSONObject dataObject = new JSONObject(response);
            code = dataObject.getInt("code");
            if (code == 0) {
                // 获取数据成功
                // 返回需要解析的JsonObject对象
                if (!dataObject.isNull("map")) {
                    baseObject = dataObject.getJSONObject("map");
                }
            } else {
                errorMsg = dataObject.getString("message").trim();
            }
        } catch (Exception e) {
            DLog.e(TAG, "getResponseJson()#exception:\n", e);
            code = -2;
            baseObject = null;
        }
        if (code == 0) {
            // 成功
            onLoadDataSuccess(tag, baseObject);
        } else {
            // 失败
            if (code == Constants.LOGIN_TIME_OUT) {
                // 设置用户为未登录状态
                UserInfoManager.getInstance().clearUserInfo();
            }
            onLoadDataError(tag, code, errorMsg);
        }
    }

    /**
     * 加载网络数据成功，UI线程操作
     *
     * @param tag          请求的接口标识
     * @param resultObject 返回的结果
     */
    protected void onLoadDataSuccess(String tag, JSONObject resultObject) {
    }

    /**
     * 加载网络数据失败
     *
     * @param tag       请求的接口标识
     * @param errorCode 错误标识码
     */
    protected void onLoadDataError(String tag, int errorCode, String msg) {

    }

    /**
     * 如果第一次加载失败，点击错误视图重新加载所调用的函数
     */
    protected void tryAgain() {
        showLoadingView();
    }

    /**
     * 显示加载失败视图
     */
    protected void showErrorView() {
        errorView.setVisibility(View.VISIBLE);
//        loadingView.setVisibilyView(false);
        loadingView.setVisibility(View.GONE);
        centerLay.setVisibility(View.GONE);
        noDataView.setVisibility(View.GONE);
    }

    /**
     * 显示正文视图
     */
    protected void showCenterView() {
        centerLay.setVisibility(View.VISIBLE);
//        loadingView.setVisibilyView(false);
        loadingView.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
        noDataView.setVisibility(View.GONE);
    }

    /**
     * 显示正在加载视图
     */
    protected void showLoadingView() {
//        loadingView.setVisibilyView(true);
        loadingView.setVisibility(View.VISIBLE);
        centerLay.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
        noDataView.setVisibility(View.GONE);
    }

    /**
     * 显示没有数据视图
     */
    protected void showNoDataView() {
        loadingView.setVisibility(View.GONE);
        centerLay.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
        noDataView.setVisibility(View.VISIBLE);
    }

//    /**
//     * 设置没有数据时显示的文本
//     *
//     * @param msg
//     */
//    protected void setNoDataViewMsg(String msg) {
//        noDataView.setText(msg);
//    }

    /**
     * 设置标题
     *
     * @param title
     */
    protected void setTitleName(String title) {
        titleView.setTitleName(title);
    }

    /**
     * 设置标题是否显示
     *
     * @param show
     */
    protected void setTitleVisible(boolean show) {
        titleView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    /**
     * 设置标题返回图标的点击事件
     *
     * @param listener
     */
    protected void setTitleBackLinstener(View.OnClickListener listener) {
        titleView.setOnClickListener(listener);
    }


//    /**
//     * 解析服务器返回的基本数据
//     * @param result
//     * @return
//     */
//    private int parseBaseData(String result,JSONObject baseObject){
//        int code = -1;
//        try {
//            JSONObject dataObject = new JSONObject(result);
//            code = dataObject.getInt("code");
//            if(code == 0){
//                // 获取数据成功
//                // 返回需要解析的JsonObject对象
//                baseObject = dataObject.getJSONObject("map");
//            }
//        } catch (Exception e) {
//            DLog.e(TAG,"getResponseJson()#exception:" + e);
//        }
//        return code;
//    }

}
