package com.ttxg.fruitday.gui.fragment;

import android.view.View;
import android.widget.RelativeLayout;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.gui.adapter.GoodsGridViewAdapter;
import com.ttxg.fruitday.gui.view.WraperGridView;
import com.ttxg.fruitday.model.PureGoods;
import com.ttxg.fruitday.util.ParseUtil;

/**
 * 邮递到家fragment
 * Created by lilijun on 2016/9/7.
 */
public class StoreGoodsListFragment extends BaseFragment {
    private String GET_STORE_GOODS_LIST = "cloud/store/";
    private HashMap<String,Object> params;
    private int curPage = 1;
    private final int LOAD_DATA_SIZE = 20;

    private WraperGridView gridView;
    private GoodsGridViewAdapter adapter;
    private List<PureGoods> goodsList;

    /**
     * 是否是第一次加载
     */
    private boolean isFirstLoad = true;

    /**
     *
     * @param userId 云店铺用户的userId
     * @param tab 所属tab标识值 1=邮递到家，2=周边闪送，3=门店服务，4=友情推荐
     */
    public void init(int userId,int tab){
        params = new HashMap<>();
        goodsList = new ArrayList<>();
        GET_STORE_GOODS_LIST += userId + "/goodsByTab";
        params.put("userId",userId);
        params.put("tab",tab);
        params.put("start",curPage);
        params.put("limit",LOAD_DATA_SIZE);
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser && isFirstLoad){
            loadDataGet(GET_STORE_GOODS_LIST,params);
            isFirstLoad = false;
        }
    }

    @Override
    protected void initView(RelativeLayout view) {
        gridView = new WraperGridView(getActivity());
        gridView.setNumColumns(2);
        gridView.setVerticalSpacing((int) getResources().getDimension(R.dimen.gridview_padding));
        gridView.setHorizontalSpacing((int) getResources().getDimension(R.dimen.gridview_padding));
        gridView.setVerticalScrollBarEnabled(false);
        setCenterView(gridView);
        adapter = new GoodsGridViewAdapter(goodsList);
        gridView.setAdapter(adapter);

        gridView.setOnNextPageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadDataGet(GET_STORE_GOODS_LIST,params);
            }
        });
    }

    @Override
    protected void onLoadDataSuccess(String tag, JSONObject resultObject) {
        super.onLoadDataSuccess(tag, resultObject);
        if(tag.equals(GET_STORE_GOODS_LIST)){
            List<PureGoods> tempList = ParseUtil.parsePureGoodsList(resultObject);
            if(goodsList.isEmpty()){
                // 首次加载成功
                if(tempList == null){
                    // 首次加载失败
                    showErrorView();
                }else {
                    if(tempList.isEmpty()){
                        showNoDataView();
                    }else {
                        goodsList.addAll(tempList);
                        adapter.notifyDataSetChanged();
                        if(tempList.size() < LOAD_DATA_SIZE){
                            // 没有下一页了
                            gridView.setNextPageViewVisible(false);
                        }else {
                            // 还有下一页
                            gridView.setNextPageViewVisible(true);
                            curPage ++;
                        }
                        showCenterView();
                    }
                }
            }else {
                // 加载下一页成功
                if(tempList == null){
                    // 加载下一页失败
                    gridView.setNextPageViewLoadFailed();
                    gridView.setNextPageViewVisible(true);
                }else {
                    if(tempList.isEmpty() || tempList.size() < LOAD_DATA_SIZE){
                        // 没有下一页了
                        gridView.setNextPageViewVisible(false);
                    }else {
                        // 还有下一页
                        gridView.setNextPageViewVisible(true);
                        curPage ++;
                    }
                    goodsList.addAll(tempList);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    protected void onLoadDataError(String tag, int errorCode, String msg) {
        super.onLoadDataError(tag, errorCode, msg);
        if(tag.equals(GET_STORE_GOODS_LIST)){
            if(goodsList.isEmpty()){
                // 首次加载失败
                showErrorView();
            }else {
                // 加载下一页失败
                gridView.setNextPageViewLoadFailed();
                gridView.setNextPageViewVisible(true);
            }
        }
    }

    @Override
    protected void tryAgain() {
        super.tryAgain();
        loadDataGet(GET_STORE_GOODS_LIST,params);
    }
}
