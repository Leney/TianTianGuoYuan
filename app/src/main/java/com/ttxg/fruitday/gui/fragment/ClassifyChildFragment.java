package com.ttxg.fruitday.gui.fragment;

import android.text.TextUtils;
import android.widget.RelativeLayout;

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.gui.adapter.ClassifyChildAdapter;
import com.ttxg.fruitday.gui.view.RefreshExpandableListView;
import com.ttxg.fruitday.model.Classify;
import com.ttxg.fruitday.util.ParseUtil;
import com.ttxg.fruitday.util.Util;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 子分类的fragment
 * Created by lilijun on 2016/8/29.
 */
public class ClassifyChildFragment extends BaseFragment {
    private static final String GET_CATEGORY = "goods/category";
    HashMap<String, Object> params = null;
    //    private PullToRefreshLayout pullToRefreshLayout;
    private RefreshExpandableListView listView;
    private ClassifyChildAdapter adapter;
    /**
     * 保存所有已经网络加载过的类别Group数据
     */
    private HashMap<Integer, List<String>> groupsMap;

    private HashMap<Integer, List<List<Classify[]>>> childsMap;

    /**
     * 当前显示的group数据集合
     */
    private List<String> curGroupList;
    /**
     * 当前显示的child数据集合
     */
    private List<List<Classify[]>> curChildList;

    /**
     * 当前显示类别的父id(即：当前类别id)
     */
    private int curPId;

    /**
     * 是否正在刷新
     */
    private boolean isRefreshing = false;

    @Override
    protected void initView(RelativeLayout view) {
        params = new HashMap<>();
        groupsMap = new HashMap<>();
        childsMap = new HashMap<>();
        curGroupList = new ArrayList<>();
        curChildList = new ArrayList<>();

        setCenterView(R.layout.fragment_classify_child);
        listView = (RefreshExpandableListView) view.findViewById(R.id.classify_child_list_view);
//        listView = new WraperExpandableListView(getActivity());
//        listView.setLayoutParams(new RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.MATCH_PARENT,
//                RelativeLayout.LayoutParams.MATCH_PARENT));
//        listView.setBackgroundResource(R.drawable.white);
//        setCenterView(listView);
//        pullToRefreshLayout = (PullToRefreshLayout) view.findViewById(R.id
//                .classify_child_pull_to_refresh_lay);
        adapter = new ClassifyChildAdapter(curGroupList, curChildList);
        listView.setAdapter(adapter);

        listView.setonRefreshListener(new RefreshExpandableListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefreshing = true;
                params.put("pid", curPId);
                loadDataGet(GET_CATEGORY, params);
            }
        });
        params.put("pid", 0);
    }

    /**
     * 切换或加载类别数据
     *
     * @param id
     */
    public void changeClassify(int id) {
        if (curPId == id) {
            return;
        }
        if (groupsMap.containsKey(id)) {
            // 之前有加载过(已有数据)
            curGroupList.clear();
            curChildList.clear();
            curGroupList.addAll(groupsMap.get(id));
            curChildList.addAll(childsMap.get(id));
            adapter.notifyDataSetChanged();
            // 展开所有item
            for (int i = 0; i < adapter.getGroupCount(); i++) {
                listView.expandGroup(i);
            }
            showCenterView();
        } else {
            // 之前没加载过  从网络获取数据
            params.remove("pid");
            params.put("pid", id);
            loadDataGet(GET_CATEGORY, params);
            showLoadingView();
        }
        curPId = id;
    }

    @Override
    protected void onLoadDataSuccess(String tag, JSONObject resultObject) {
        super.onLoadDataSuccess(tag, resultObject);
        if (TextUtils.equals(tag, GET_CATEGORY)) {
            List<String> groupTempList = new ArrayList<>();
            List<List<Classify[]>> childTempList = new ArrayList<>();
            ParseUtil.parseClassifyChildList(resultObject, groupTempList, childTempList);
//            if(!groupTempList.isEmpty() && !childTempList.isEmpty()){
            // 有数据
            curGroupList.clear();
            curChildList.clear();

            curGroupList.addAll(groupTempList);
            curChildList.addAll(childTempList);

            groupsMap.put(curPId, groupTempList);
            childsMap.put(curPId, childTempList);
            // 展开所有item
            for (int i = 0; i < adapter.getGroupCount(); i++) {
                listView.expandGroup(i);
            }
            adapter.notifyDataSetChanged();
            showCenterView();
            if (isRefreshing) {
                listView.onRefreshComplete();
                isRefreshing = false;
            }
            if (curGroupList.isEmpty() || curChildList.isEmpty()) {
                // 没数据
                showNoDataView();
            }
        }
    }

    @Override
    protected void onLoadDataError(String tag, int errorCode, String msg) {
        super.onLoadDataError(tag, errorCode, msg);
        if (TextUtils.equals(tag, GET_CATEGORY)) {
            // 失败
            if (isRefreshing) {
                // 刷新失败
                listView.onRefreshComplete();
                isRefreshing = false;
                Util.showErrorMessage(getActivity(), msg, getResources().getString(R.string
                        .refresh_fail));
            } else {
                // 首次获取数据失败
                showErrorView();
            }
        }
    }

    @Override
    protected void tryAgain() {
        super.tryAgain();
        loadDataGet(GET_CATEGORY, params);
    }

    /**
     * 清除所有的数据
     */
    public void clearAll() {
        groupsMap.clear();
        childsMap.clear();
        curGroupList.clear();
        curChildList.clear();
        curPId = 0;
        params.remove("pid");
        params.put("pid", curPId);
        showLoadingView();
    }
}
