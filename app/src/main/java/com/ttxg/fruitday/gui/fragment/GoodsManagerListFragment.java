package com.ttxg.fruitday.gui.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.gui.adapter.GoodsManagerAdapter;
import com.ttxg.fruitday.gui.view.WraperListView;
import com.ttxg.fruitday.model.GoodsShelf;
import com.ttxg.fruitday.model.MyGoods;
import com.ttxg.fruitday.util.Constants;
import com.ttxg.fruitday.util.ParseUtil;
import com.ttxg.fruitday.util.Util;
import com.ttxg.fruitday.util.log.DLog;

/**
 * 商品管理列表Fragment
 * Created by lilijun on 2016/9/29.
 */
public class GoodsManagerListFragment extends BaseFragment implements GoodsManagerAdapter.OnChangeGoodsShelfListener {
    /**
     * 获取商品列表接口
     */
    private final String GET_MY_GOODS_LIST = "store/myGoods";
    /**
     * 切换货架 | 停止经销接口
     */
    private final String CHANGE_OR_STOP_SALES = "store/updateFloorGoods";
    /**
     * 根据商品id查询货架信息接口
     */
    private final String GET_GOODS_SHELF_BY_GOODS_ID = "store/getFloorByStoreIdAndGoodsId";
    /**
     * 获取货架列表接口
     */
    private final String GET_GOODS_SHELF_LIST = "store/floorManage";
    /**
     * 搜索到的总件数
     */
    private TextView amountText;

    private WraperListView listView;

    private GoodsManagerAdapter adapter;

    /**
     * 列表显示给用户看的商品列表数据
     */
    private List<MyGoods> goodsList;

    /**
     * 真正的总的商品列表数据
     */
    private List<MyGoods> totalGoodsList;

    /**
     * 当前的排序规则
     */
    private int curOrderFilter = 2;

    private Map<String, Object> params;

    private boolean isFirstLoad = true;

    private final int LOAD_DATA_SIZE = 10;

    /**
     * 总共有多少件商品
     */
    private int curTotalNum = 0;
    private int curPage = 1;

    /**
     * 标识是否加载新的数据(全部重新加载)
     */
    private boolean isLoadNew = false;

    /**
     * 请稍后dialog
     */
    private ProgressDialog progressDialog;

    /**
     * 商品货架列表数据
     */
    private List<GoodsShelf> shelfList;
    /**
     * 商品货架名称列表数据
     */
    private List<String> shelfNameList;

    /**
     * 通过商品id查询货架信息的商品id
     */
    private int queryGoodsId = -1;

    /**
     * 查询到的单个商品的所属货架信息
     */
    private GoodsShelf qureryGoodsShelf;

    /**
     * 初始化
     *
     * @param orderFilter 查找的排序规则
     *                    (0=综合,1=团购价格升序,2=按团购价格降序,3=按销量降序,4=按评价降序)
     */
    public void init(int orderFilter) {
        this.curOrderFilter = orderFilter;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isFirstLoad) {
            params = new HashMap<>();
            params.put("order", curOrderFilter);
            params.put("start", curPage);
            params.put("limit", LOAD_DATA_SIZE);
            loadDataGet(GET_MY_GOODS_LIST, params);
            isFirstLoad = false;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        getActivity().registerReceiver(receiver, new IntentFilter(Constants
                .ACTION_STOP_SALES_GOODS_SUCCESS));
    }

    @Override
    protected void initView(RelativeLayout view) {
        goodsList = new ArrayList<>();
        totalGoodsList = new ArrayList<>();
        shelfList = new ArrayList<>();
        shelfNameList = new ArrayList<>();

        setCenterView(R.layout.fragment_goods_manager_list);
        amountText = (TextView) view.findViewById(R.id.goods_manager_amount_text);
        listView = (WraperListView) view.findViewById(R.id.goods_manager_listView);
        listView.setOnNextPageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                params.put("start", curPage + 1);
                loadDataGet(GET_MY_GOODS_LIST, params);
            }
        });
        adapter = new GoodsManagerAdapter(goodsList);
        listView.setAdapter(adapter);
        adapter.setOnChangeGoodsShelfListener(this);
    }

    @Override
    protected void onLoadDataSuccess(String tag, JSONObject resultObject) {
        super.onLoadDataSuccess(tag, resultObject);
        if (TextUtils.equals(tag, GET_MY_GOODS_LIST)) {
            if (isLoadNew) {
                curPage = 1;
                goodsList.clear();
                totalGoodsList.clear();
                isLoadNew = false;
                curTotalNum = 0;
            }
            // 获取商品数据成功
            int totalNum = ParseUtil.parseMyGoodsList(resultObject, goodsList, totalGoodsList);
            if (goodsList.isEmpty()) {
                showNoDataView();
            } else {
                curTotalNum = totalNum;
                if (curPage == 1) {
                    // 首次加载数据成功
                    if (totalGoodsList.size() < LOAD_DATA_SIZE) {
                        // 加载第一页就已经加载完了所有数据
                        listView.setNextPageViewVisible(false);
                    } else {
                        // 还有下一页数据
                        listView.setNextPageViewVisible(true);
                        curPage++;
                    }
                    showCenterView();
                } else {
                    //是加载下一页数据成功了
                    if (totalGoodsList.size() % LOAD_DATA_SIZE != 0) {
                        // 证明没有下一页数据了，数据已加载完
                        listView.setNextPageViewVisible(false);
                    } else {
                        // 还有下一页数据
                        listView.setNextPageLoadNext();
                        curPage++;
                    }
                }
            }
            adapter.notifyDataSetChanged();
            amountText.setText(curTotalNum + "");
        } else if (TextUtils.equals(tag, GET_GOODS_SHELF_BY_GOODS_ID)) {
            // 根据商品id查询其货架信息成功
            GoodsShelf goodsShelf = ParseUtil.parseGoodsShelf(resultObject);
            if (goodsShelf != null) {
                qureryGoodsShelf = goodsShelf;
                if (shelfList.isEmpty()) {
                    //商品货架列表中没有数据，证明用户未加载货架的fragment  则需要去获取商户的货架列表信息数据
                    DLog.i("去获取货架列表数据！！！");
                    loadDataGet(GET_GOODS_SHELF_LIST, null);
                } else {
                    // 商品货架列表中有数据  则直接显示dialog
                    progressDialog.dismiss();
                    showChangeGoodsShelfDialog(queryGoodsId, qureryGoodsShelf.getName());
                }
            } else {
                progressDialog.dismiss();
                Util.showToast(getActivity(), getResources().getString(R.string
                        .get_goods_shelf_failed));
            }
        } else if (TextUtils.equals(tag, GET_GOODS_SHELF_LIST)) {
            // 获取用户货架列表信息成功
            List<GoodsShelf> emptyList = ParseUtil.parseGoodsShelfList(resultObject);
            progressDialog.dismiss();
            if (emptyList == null || emptyList.isEmpty()) {
                Util.showToast(getActivity(), getResources().getString(R.string
                        .get_user_goods_shelf_list_failed));
            } else {
                shelfList.clear();
                shelfNameList.clear();
                shelfList.addAll(emptyList);
                for (GoodsShelf shelf : this.shelfList) {
                    shelfNameList.add(shelf.getName());
                }
                showChangeGoodsShelfDialog(queryGoodsId, qureryGoodsShelf.getName());
            }
        } else if (TextUtils.equals(tag, CHANGE_OR_STOP_SALES)) {
            // 切换或停止经销成功
            try {
                boolean isChangeShelfSuccess = TextUtils.equals("editFloor", resultObject
                        .getString("flag"));
                if (isChangeShelfSuccess) {
                    // 切换货架成功
                    Util.showToast(getActivity(), getResources().getString(R.string
                            .change_goods_shelf_success));
                } else {
                    // 停止经销成功
                    int goodsId = resultObject.getInt("goodsId");
                    DLog.i("发送停止经销成功的消息！！");
                    Intent intent = new Intent(Constants.ACTION_STOP_SALES_GOODS_SUCCESS);
                    intent.putExtra("goodsId", goodsId);
                    getActivity().sendBroadcast(intent);
//                    EventBus.getDefault().post(goodsId);
                    Util.showToast(getActivity(), getResources().getString(R.string
                            .stop_sales_success));
                }
                EventBus.getDefault().post(Constants.STORE_GOODS_HAVE_MODIFY);
            } catch (Exception e) {
                DLog.e(TAG, "解析更换货架或者停止经销失败#exception:\n", e);
                Util.showToast(getActivity(), getResources().getString(R.string.operation_failed));
                return;
            }
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onLoadDataError(String tag, int errorCode, String msg) {
        super.onLoadDataError(tag, errorCode, msg);
        if (TextUtils.equals(tag, GET_MY_GOODS_LIST)) {
            // 获取商品数据失败
            if (goodsList.isEmpty()) {
                // 加载第一页失败
                showErrorView();
            } else {
                // 加载下一页失败
                listView.setNextPageViewLoadFailed();
            }
        } else if (TextUtils.equals(tag, GET_GOODS_SHELF_BY_GOODS_ID)) {
            // 根据商品id查询其货架信息失败
            Util.showErrorMessage(getActivity(), msg, getResources().getString(R.string
                    .get_goods_shelf_failed));
            progressDialog.dismiss();
        } else if (TextUtils.equals(tag, GET_GOODS_SHELF_LIST)) {
            // 获取用户货架列表信息失败
            Util.showErrorMessage(getActivity(), msg, getResources().getString(R.string
                    .get_user_goods_shelf_list_failed));
            progressDialog.dismiss();
        } else if (TextUtils.equals(tag, CHANGE_OR_STOP_SALES)) {
            // 更换货架或者停止经销失败
            Util.showErrorMessage(getActivity(), msg, getResources().getString(R.string
                    .operation_failed));
            progressDialog.dismiss();
        }
    }

    /**
     * 根据排序条件加载新的数据
     *
     * @param type
     */
    public void loadNewType(int type) {
        isLoadNew = true;
        curPage = 1;
        curTotalNum = 0;
        curOrderFilter = type;
        tryAgain();
    }

    @Override
    protected void tryAgain() {
        super.tryAgain();
        params.put("order", curOrderFilter);
        params.put("start", curPage);
        params.put("limit", LOAD_DATA_SIZE);
        loadDataGet(GET_MY_GOODS_LIST, params);
    }

    @Subscribe
    public void onEventMessage(MyGoods goods) {
        // 接收加入小店成功的数据
        if (goodsList.size() < LOAD_DATA_SIZE) {
            // 只有一页数据
            goodsList.add(goods);
            adapter.notifyDataSetChanged();
            showCenterView();
        }
    }

    @Subscribe
    public void onEventMessage(List<GoodsShelf> shelfList) {
        // 接收获取商品货架列表的数据
        this.shelfList.clear();
        this.shelfNameList.clear();
        this.shelfList.addAll(shelfList);
        for (GoodsShelf shelf : this.shelfList) {
            shelfNameList.add(shelf.getName());
        }
    }

//    @Subscribe
//    public void onEventMessage(int goodsId) {
//        // 接收停止分销某商品成功的消息
//        DLog.i("接收到停止分销成功的消息 goodsId-->>>" + goodsId);
//        for (MyGoods goods : goodsList) {
//            DLog.i("遍历 goodsId---->>" + goods.getId());
//            if (goods.getId() == goodsId) {
//                // 移除停止经销的数据
//                goodsList.remove(goods);
//                break;
//            }
//        }
//        amountText.setText(--curTotalNum + "");
//        adapter.notifyDataSetChanged();
//    }


    /**
     * 局部广播类
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (TextUtils.equals(intent.getAction(), Constants.ACTION_STOP_SALES_GOODS_SUCCESS)) {
                // 接收到停止某件商品成功的消息
                int goodsId = intent.getIntExtra("goodsId", -1);
                if (goodsId != -1) {
                    for (MyGoods goods : goodsList) {
                        if (goods.getId() == goodsId) {
                            // 移除停止经销的数据
                            goodsList.remove(goods);
                            break;
                        }
                    }
                    amountText.setText(--curTotalNum + "");
                    adapter.notifyDataSetChanged();
                }
            }
        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        getActivity().unregisterReceiver(receiver);
    }

    @Override
    public void onChange(MyGoods goods) {
        // 更改货架的按钮点击事件
        progressDialog = Util.showLoadingDialog(getActivity(),progressDialog);
        Map<String, Object> params = new HashMap<>();
        queryGoodsId = goods.getId();
        params.put("goodsId", goods.getId());
        loadDataGet(GET_GOODS_SHELF_BY_GOODS_ID, params);
    }


    /**
     * 显示更改货架的dialog
     *
     * @param goodsId      商品id
     * @param curShelfName 当前商品所属的货架名称
     */
    private void showChangeGoodsShelfDialog(final int goodsId, String curShelfName) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.change_goods_shelf_dialog);
        final Spinner spinner = (Spinner) dialog.findViewById(R.id
                .change_goods_shelf_dialog_spinner);
        ArrayAdapter spinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout
                .spinner_item_lay, shelfNameList);
        // 设置样式
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        int curShelfPosition = 0;
        int length = shelfNameList.size();
        for (int i = 0; i < length; i++) {
            String name = shelfNameList.get(i);
            if (TextUtils.equals(curShelfName, name)) {
                // 说明是当前商品的货架名称
                curShelfPosition = i;
                break;
            }
            DLog.i("货架名称------>>>" + name);
        }
        // 设置spinner的选择项
        spinner.setSelection(curShelfPosition);
        TextView sureBtn = (TextView) dialog.findViewById(R.id.change_goods_shelf_dialog_sure_btn);
        TextView stopSalesBtn = (TextView) dialog.findViewById(R.id
                .change_goods_shelf_dialog_stop_sales_btn);
        sureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 更换货架
                GoodsShelf goodsShelf = shelfList.get(spinner.getSelectedItemPosition());
                if (goodsShelf != null) {
                    Map<String, String> params = new HashMap<>();
                    params.put("goodsId", goodsId + "");
                    params.put("flag", "editFloor");
                    params.put("newfloorId", goodsShelf.getId() + "");
                    loadDataPost(CHANGE_OR_STOP_SALES, params);
                    dialog.dismiss();
                    progressDialog = Util.showLoadingDialog(getActivity(),progressDialog);
                }
            }
        });

        stopSalesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 停止分销
                Map<String, String> params = new HashMap<>();
                params.put("goodsId", goodsId + "");
                params.put("flag", "stopDistribute");
                loadDataPost(CHANGE_OR_STOP_SALES, params);
                dialog.dismiss();
                progressDialog = Util.showLoadingDialog(getActivity(),progressDialog);
            }
        });
        dialog.show();
    }
}
