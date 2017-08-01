package com.ttxg.fruitday.gui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.gui.adapter.ChoiceGoodsAdapter;
import com.ttxg.fruitday.gui.view.WraperListView;
import com.ttxg.fruitday.model.MyGoods;
import com.ttxg.fruitday.util.Constants;
import com.ttxg.fruitday.util.ParseUtil;
import com.ttxg.fruitday.util.Util;

/**
 * 选择商品列表界面
 * Created by lilijun on 2016/9/30.
 */
public class ChoiceGoodsActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 获取选择商品列表接口
     */
    private final String GET_CHOICE_GOODS = "store/selectGoods";
    /**
     * 加入小店接口
     */
    private final String ADD_TO_STORE = "goods/distribute";
    private WraperListView listView;
    private ChoiceGoodsAdapter adapter;
    private View headerView;
    private List<MyGoods> goodsList;

    private Spinner searchSpinner;
    private EditText searchInput;
    private ImageView searchBtn;
    /**
     * 总共搜索到多少件商品
     */
    private TextView amountText;

    private Map<String, Object> params;

    /**
     * 过滤类型条件id (1=根据类目过滤,2=根据商品名称过滤,3=根据店铺名称过滤)
     */
    private int curSelectId = 1;

    /**
     * 当前页
     */
    private int curPage = 1;

    /**
     * 搜索到的商品总数
     */
    private int curTotalNum = 0;

    private final int LOAD_DATA_SIZE = 10;

    /**
     * 请稍后dialog
     */
    private ProgressDialog progressDialog;

    /**
     * 加入小店成功的MyGoods对象
     */
    private MyGoods curAddMyGoods;

    @Override
    protected void initView() {

        params = new HashMap<>();
        goodsList = new ArrayList<>();


        setTitleName(getResources().getString(R.string.choice_goods));

        listView = new WraperListView(ChoiceGoodsActivity.this);
        setCenterView(listView);
        headerView = View.inflate(ChoiceGoodsActivity.this, R.layout.choice_goods_header_lay, null);
        setAddView(headerView);

        adapter = new ChoiceGoodsAdapter(goodsList);
        listView.setAdapter(adapter);
        listView.setOnNextPageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                params.put("start", curPage + 1);
                loadDataGet(GET_CHOICE_GOODS, params);
                listView.setNextPageLoading();
            }
        });

        adapter.setOnAddToStoreListener(new ChoiceGoodsAdapter.OnAddToStoreListener() {
            @Override
            public void onAdd(MyGoods goods) {
                Map<String, String> addParms = new HashMap<>();
                addParms.put("source_store_id", goods.getStoreId() + "");
                addParms.put("goodsId", goods.getId() + "");
                progressDialog = Util.showLoadingDialog(ChoiceGoodsActivity.this, progressDialog);
                curAddMyGoods = goods;
                loadDataPost(ADD_TO_STORE, addParms);
            }
        });


        searchSpinner = (Spinner) headerView.findViewById(R.id.choice_goods_spinner);
        searchInput = (EditText) headerView.findViewById(R.id.choice_goods_search_input);
        searchBtn = (ImageView) headerView.findViewById(R.id.choice_goods_search_btn);
        searchBtn.setOnClickListener(this);
        amountText = (TextView) headerView.findViewById(R.id.choice_goods_amount_text);

        searchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                curSelectId = i + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        params.put("selectid", curSelectId);
        params.put("start", curPage);
        params.put("keyword", "");
        params.put("limit", LOAD_DATA_SIZE);
        loadDataGet(GET_CHOICE_GOODS, params);
    }

    @Override
    protected void onLoadDataSuccess(String tag, JSONObject resultObject) {
        super.onLoadDataSuccess(tag, resultObject);
        if (TextUtils.equals(tag, GET_CHOICE_GOODS)) {
            // 获取商品数据成功
            if (curPage == 1) {
                // 第一页加载数据返回
                curTotalNum = 0;
                goodsList.clear();
            }
            int totalNum = ParseUtil.parseMyGoodsList(resultObject, goodsList);
            if (goodsList.isEmpty()) {
                showNoDataView();
                curTotalNum = 0;
            } else {
                if (curTotalNum == 0) {
                    // 第一次
                    curTotalNum = totalNum;
                }
                if (curPage == 1) {
                    // 首次加载数据成功
                    if (goodsList.size() < LOAD_DATA_SIZE) {
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
                    if (goodsList.size() % LOAD_DATA_SIZE != 0) {
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
        } else if (TextUtils.equals(tag, ADD_TO_STORE)) {
            // 加入小店成功
            progressDialog.dismiss();
            Util.showToast(ChoiceGoodsActivity.this, getResources().getString(R.string
                    .add_to_store_success));
            // 发送将商品加入小店成功的消息
            EventBus.getDefault().post(curAddMyGoods);
            EventBus.getDefault().post(Constants.STORE_GOODS_HAVE_MODIFY);
        }
    }

    @Override
    protected void onLoadDataError(String tag, int errorCode, String msg) {
        super.onLoadDataError(tag, errorCode, msg);
        if (TextUtils.equals(tag, GET_CHOICE_GOODS)) {
            // 获取商品数据失败
            Util.showErrorMessage(ChoiceGoodsActivity.this, msg, getResources().getString(R
                    .string.get_choice_goods_failed));
            if (errorCode == -1) {
                finish();
                return;
            }
            showErrorView();
        } else if (TextUtils.equals(tag, ADD_TO_STORE)) {
            // 加入小店失败
            progressDialog.dismiss();
            Util.showErrorMessage(ChoiceGoodsActivity.this, msg, getResources().getString(R.string
                    .add_to_store_failed));
        }
    }

    @Override
    protected void tryAgain() {
        super.tryAgain();
        curPage = 1;
        curSelectId = 1;
        params.put("selectid", curSelectId);
        params.put("start", curPage);
        params.put("keyword", "");
        params.put("limit", LOAD_DATA_SIZE);
        loadDataGet(GET_CHOICE_GOODS, params);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.choice_goods_search_btn:
                // 搜索按钮
                curPage = 1;
                params.put("selectid", curSelectId);
                params.put("keyword", searchInput.getText().toString().trim());
                params.put("start", curPage);
                loadDataGet(GET_CHOICE_GOODS, params);
                showLoadingView();
                break;
        }
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ChoiceGoodsActivity.class);
        context.startActivity(intent);
    }
}
