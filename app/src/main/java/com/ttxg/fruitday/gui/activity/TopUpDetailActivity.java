package com.ttxg.fruitday.gui.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.gui.adapter.TopUpDetailAdapter;
import com.ttxg.fruitday.gui.view.WraperListView;
import com.ttxg.fruitday.model.TopUpInfo;
import com.ttxg.fruitday.util.ParseUtil;

/**
 * 充值明细界面
 * Created by lilijun on 2016/10/24.
 */
public class TopUpDetailActivity extends BaseActivity {
    /**
     * 获取充值明细列表
     */
    private final String GET_TOP_UP_DETAIL = "prepaid/detail";

    private WraperListView listView;
    private TopUpDetailAdapter adapter;
    private List<TopUpInfo> infoList;
    /**
     * 当前页
     */
    private int curPage = 1;

    private final int LOAD_DATA_SIZE = 10;

    private Map<String,Object> params;
    @Override
    protected void initView() {

        setTitleName(getResources().getString(R.string.top_up_detail));
        infoList = new ArrayList<>();

        params = new HashMap<>();
        params.put("start",curPage);
        params.put("limit",LOAD_DATA_SIZE);

        listView = new WraperListView(TopUpDetailActivity.this);
        adapter = new TopUpDetailAdapter(infoList);
        setCenterView(listView);
        listView.setOnNextPageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 加载下一页
                params.put("start",curPage);
                loadDataGet(GET_TOP_UP_DETAIL,params);
            }
        });
        listView.setAdapter(adapter);
        loadDataGet(GET_TOP_UP_DETAIL,params);
    }

    @Override
    protected void onLoadDataSuccess(String tag, JSONObject resultObject) {
        super.onLoadDataSuccess(tag, resultObject);
        if(TextUtils.equals(tag,GET_TOP_UP_DETAIL)){
            // 获取充值详情成功
            List<TopUpInfo> emptyList = ParseUtil.parseTopUpInfoList(resultObject);
            if(emptyList == null){
                if(curPage == 1){
                    // 加载第一页就失败了
                    showErrorView();
                }else {
                    // 加载下一页失败了
                    listView.setNextPageViewLoadFailed();
                }
                return;
            }

            infoList.addAll(emptyList);
            if(infoList.isEmpty()){
                // 没有充值记录
                showNoDataView();
            }else {
                // 有充值记录
                if(curPage == 1){
                    // 第一次加载完成，显示中间视图
                    showCenterView();
                }
                adapter.notifyDataSetChanged();
            }

            if(emptyList.size() < LOAD_DATA_SIZE){
                // 没有下一页了
                listView.setNextPageViewVisible(false);
            }else {
                // 还有下一页
                listView.setNextPageViewVisible(true);
                listView.setNextPageLoadNext();
                // 加一页
                curPage ++;
            }
        }
    }

    @Override
    protected void onLoadDataError(String tag, int errorCode, String msg) {
        super.onLoadDataError(tag, errorCode, msg);
        if(TextUtils.equals(tag,GET_TOP_UP_DETAIL)){
            // 获取充值详情失败
            if(curPage == 1){
                // 加载第一页就失败了
                showErrorView();
            }else {
                // 加载下一页失败了
                listView.setNextPageViewLoadFailed();
            }
        }
    }

    public static void startActivity(Context context){
        Intent intent = new Intent(context,TopUpDetailActivity.class);
        context.startActivity(intent);
    }
}
