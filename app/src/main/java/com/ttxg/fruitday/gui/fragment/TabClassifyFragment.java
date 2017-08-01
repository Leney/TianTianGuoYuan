package com.ttxg.fruitday.gui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.gui.adapter.ClassifyTitleAdapter;
import com.ttxg.fruitday.gui.view.BaseTitleView;
import com.ttxg.fruitday.gui.view.RefreshSimpleListView;
import com.ttxg.fruitday.model.Classify;
import com.ttxg.fruitday.util.ParseUtil;
import com.ttxg.fruitday.util.Util;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 分类Tab Fragment
 * Created by yb on 2016/8/12.
 */
public class TabClassifyFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    private static final String GET_CATEGORY = "goods/category";
    HashMap<String, Object> params = null;
    /**
     * 总分类的数据集合
     */
    private List<Classify> classifyList;
    private RefreshSimpleListView listView;
    private ClassifyTitleAdapter adapter;

    private ClassifyChildFragment classifyChildFragment;

    /**
     * 当前选择的itemPosition
     */
    private int curSelectItemPosition;

    private boolean isRefreshing = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView(RelativeLayout view) {
        params = new HashMap<>();
        classifyList = new ArrayList<>();

        BaseTitleView titleView = new BaseTitleView(getActivity());
        titleView.setBackImgVisible(false);
        RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(RelativeLayout
                .LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen
                .title_height));
        titleView.setTitleName(getResources().getString(R.string.classify_all));
        setAddView(titleView, titleParams);

        setCenterView(R.layout.fragment_classify);
        listView = (RefreshSimpleListView) view.findViewById(R.id.clssify_fragment_listview);
        adapter = new ClassifyTitleAdapter(classifyList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.setNextPageViewVisible(false);
        listView.setonRefreshListener(new RefreshSimpleListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefreshing = true;
                loadDataGet(GET_CATEGORY, params);
            }
        });

        classifyChildFragment = (ClassifyChildFragment) getActivity().getSupportFragmentManager()
                .findFragmentByTag("classify_child_fragment");

        params.put("pid", 0);
        loadDataGet(GET_CATEGORY, params);
    }

    @Override
    protected void onLoadDataSuccess(String tag, JSONObject resultObject) {
        super.onLoadDataSuccess(tag, resultObject);
        if (tag.equals(GET_CATEGORY)) {
            classifyList.clear();
            if (isRefreshing) {
                listView.onRefreshComplete();
                isRefreshing = false;
                classifyChildFragment.clearAll();
            }
            List<Classify> list = ParseUtil.parseClassifyList(resultObject);
            if (list != null && !list.isEmpty()) {
                classifyList.addAll(list);
                adapter.notifyDataSetChanged();
                curSelectItemPosition = 0;
                classifyChildFragment.changeClassify(classifyList.get(0).getId());
                showCenterView();
            } else {
                showErrorView();
            }
        }
    }

    @Override
    protected void onLoadDataError(String tag, int errorCode, String msg) {
        super.onLoadDataError(tag, errorCode, msg);
        if (TextUtils.equals(tag, GET_CATEGORY)) {
            if (isRefreshing) {
                listView.onRefreshComplete();
                isRefreshing = false;
                Util.showErrorMessage(getActivity(), msg, getResources().getString(R.string
                        .refresh_fail));
            } else {
                showErrorView();
            }
        }
    }

    @Override
    protected void tryAgain() {
        super.tryAgain();
        loadDataGet(GET_CATEGORY, params);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        classifyList.get(curSelectItemPosition).setSelected(false);
        classifyList.get(i - 1).setSelected(true);
        adapter.notifyDataSetChanged();
        curSelectItemPosition = i - 1;
        classifyChildFragment.changeClassify(classifyList.get(i - 1).getId());
    }
}
