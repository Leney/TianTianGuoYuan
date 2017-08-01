package com.ttxg.fruitday.gui.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.gui.adapter.GoodsListAdapter;
import com.ttxg.fruitday.gui.view.WraperListView;
import com.ttxg.fruitday.model.PureGoods;
import com.ttxg.fruitday.util.ParseUtil;

/**
 * 新人专享界面
 * Created by lilijun on 2016/12/9.
 */
public class NewExclusiveActivity extends BaseActivity {
    private final String GET_NEW_LIST = "goods/newbie";
    private Map<String, Object> params;
    private int curPage = 1;
    private final int LOAD_DATA_SIZE = 10;
    private WraperListView listView;
    private GoodsListAdapter adapter;
    private List<PureGoods> goodsList;

    private SimpleDraweeView headerIcon;

    @Override
    protected void initView() {
        setTitleName(getResources().getString(R.string.new_exclusive));
        goodsList = new ArrayList<>();
        params = new HashMap<>();
        params.put("start", curPage);
        params.put("limit", LOAD_DATA_SIZE);

        listView = new WraperListView(NewExclusiveActivity.this);
        listView.setOnNextPageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                params.put("start", curPage);
                loadDataGet(GET_NEW_LIST, params);
            }
        });
        View headerView = View.inflate(NewExclusiveActivity.this,R.layout.new_exclusive_banner,null);
        headerIcon = (SimpleDraweeView) headerView.findViewById(R.id.new_exclusive_header_icon);
        listView.addHeaderView(headerView);

        adapter = new GoodsListAdapter(goodsList);
        setCenterView(listView);
        loadDataGet(GET_NEW_LIST, params);
    }

    @Override
    protected void onLoadDataSuccess(String tag, JSONObject resultObject) {
        super.onLoadDataSuccess(tag, resultObject);
        if (TextUtils.equals(tag, GET_NEW_LIST)) {
            List<PureGoods> list = new ArrayList<>();
            String bannerUrl = ParseUtil.parseNewExclusGoodsList(resultObject,list);
            if (list == null) {
                if (curPage == 1) {
                    // 首次加载就失败了
                    showErrorView();
                } else {
                    // 加载下一页失败了
                    listView.setNextPageViewLoadFailed();
                }
                return;
            }
            if (list.isEmpty()) {
                if (curPage == 1) {
                    // 首次加载没有数据
                    showNoDataView();
                } else {
                    // 加载下一页数据为空，没有下一页数据了
                    listView.setNextPageViewVisible(false);
                }
                return;
            }

            if (list.size() < LOAD_DATA_SIZE) {
                listView.setNextPageViewVisible(false);
            }

            goodsList.addAll(list);
            if (curPage == 1) {
                listView.setAdapter(adapter);
                headerIcon.setImageURI(bannerUrl);
                showCenterView();
            } else {
                adapter.notifyDataSetChanged();
            }
            curPage++;
        }
    }

    @Override
    protected void onLoadDataError(String tag, int errorCode, String msg) {
        super.onLoadDataError(tag, errorCode, msg);
        if (TextUtils.equals(tag, GET_NEW_LIST)) {
            if (curPage == 1) {
                // 首次加载就失败了
                showErrorView();
            } else {
                // 加载下一页失败了
                listView.setNextPageViewLoadFailed();
            }
        }
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, NewExclusiveActivity.class);
        context.startActivity(intent);
    }
}
