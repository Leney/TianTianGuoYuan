package com.ttxg.fruitday.gui.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.ttxg.fruitday.gui.adapter.MyCommentAdapter;
import com.ttxg.fruitday.gui.view.WraperListView;
import com.ttxg.fruitday.model.MyComment;
import com.ttxg.fruitday.util.ParseUtil;
import com.ttxg.fruitday.util.Util;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的评论Fragment
 * Created by lilijun on 2016/10/13.
 */
public class MyCommentFragment extends BaseFragment {
    private final String GET_MY_COMMENT = "user/comment";
    private WraperListView listView;
    private MyCommentAdapter adapter;
    private List<MyComment> commentList;
    /**
     * 当前fragment的类型  0=好评，1=中评，2=差评
     */
    private int type = 1;

    private boolean isFirstLoad = true;

    private Map<String, Object> params;

    /**
     * 当前页数
     */
    private int curPage = 1;

    private final int LOAD_DATA_SIZE = 10;

    /**
     * 初始化
     *
     * @param type fragment的类型  1=好评，2=中评，3=差评
     */
    public void init(int type) {
        this.type = type;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isFirstLoad) {
            params = new HashMap<>();
            params.put("rateType", type);
            params.put("start", curPage);
            params.put("limit", LOAD_DATA_SIZE);
            loadDataGet(GET_MY_COMMENT, params);
            isFirstLoad = false;
        }
    }

    @Override
    protected void initView(RelativeLayout view) {

        commentList = new ArrayList<>();

        listView = new WraperListView(getActivity());
        setCenterView(listView);

        listView.setOnNextPageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                params.put("start", curPage + 1);
                loadDataGet(GET_MY_COMMENT, params);
            }
        });
        adapter = new MyCommentAdapter(commentList);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onLoadDataSuccess(String tag, JSONObject resultObject) {
        super.onLoadDataSuccess(tag, resultObject);
        if (TextUtils.equals(tag, GET_MY_COMMENT)) {
            // 获取评论数据成功
            List<MyComment> emptyList = ParseUtil.parseMyCommentList(resultObject);
            if (emptyList != null) {
                if (emptyList.size() < LOAD_DATA_SIZE) {
                    // 没有下一页数据了
                    listView.setNextPageViewVisible(false);
                } else {
                    listView.setNextPageViewVisible(true);
                }

                commentList.addAll(emptyList);
                adapter.notifyDataSetChanged();

                if (commentList.isEmpty()) {
                    showNoDataView();
                }else {
                    if (commentList.size() <= LOAD_DATA_SIZE) {
                        // 只在第一页加载成功的时候执行
                        showCenterView();
                    }
                    curPage ++;
                }
            } else {
                showErrorView();
            }
        }
    }

    @Override
    protected void onLoadDataError(String tag, int errorCode, String msg) {
        super.onLoadDataError(tag, errorCode, msg);
        if (TextUtils.equals(tag, GET_MY_COMMENT)) {
            // 获取评论数据失败
            Util.showErrorMessage(getActivity(), msg);
            showErrorView();
        }
    }

    @Override
    protected void tryAgain() {
        super.tryAgain();
        curPage = 1;
        params.put("start", curPage);
        loadDataGet(GET_MY_COMMENT, params);
    }
}
