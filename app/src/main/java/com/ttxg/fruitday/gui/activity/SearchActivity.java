package com.ttxg.fruitday.gui.activity;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.gui.adapter.GoodsGridViewAdapter;
import com.ttxg.fruitday.gui.view.WraperGridView;
import com.ttxg.fruitday.model.PureGoods;
import com.ttxg.fruitday.util.ParseUtil;
import com.ttxg.fruitday.util.Util;

/**
 * 搜索商品界面
 * Created by lilijun on 2016/8/30.
 */
public class SearchActivity extends BaseActivity implements View.OnClickListener {

    private static final String SEARCH_GOODS_LIST = "goods/search";
    private HashMap<String,Object> params;

    private WraperGridView gridView;
    private GoodsGridViewAdapter adapter;
    private List<PureGoods> goodsList;

    private ImageView backImg;
    private EditText inputEdit;
    private Button searchBtn;
    /**
     * 综合,价格，销量，评价 文本
     */
    private TextView filterSynthesize,filterPrice,filterSales,filterComment;

    /**
     *价格，销量，评价 图标
     */
    private ImageView filterPriceImg,filterSalesImg,filterCommentImg;

    /**
     * 价格、销量、评价 lay
     */
    private LinearLayout priceLay,salesLay,commentLay;

    /**
     * 搜索到的总件数
     */
    private TextView amountText;

    /**
     * 跳转时可能携带过来的类别Id
     */
    private int classifyId = -1;

    /**
     * 跳转时可能携带过来的搜索关键字
     */
    private String keyword = "";

    /**
     * 当前显示数据的页数
     */
    private int curPage = 1;

    /**
     * 请求数据的条数(注意：在实际显示的时候 ListView 条数为10，因为一个listView item 显示了两条数据)
     */
    private static final int pageSize = 20;

    /**
     * 搜索的类型（0=综合,1=团购价格升序,2=按团购价格降序,3=按销量降序,4=按评价降序）
     */
    private int orderType = 3;

    /**
     * 当搜索类型为价格类型时，是否为价格升序类型(默认以团购价格降序)
     */
    private boolean isPriceUpOrderType = false;

    /**
     * 标记是否是一次新的请求，而非加载下一页
     */
    private boolean isNewLoad = true;

    @Override
    protected void initView() {
        setTitleVisible(false);
        setCenterView(R.layout.activity_search);
        View topView = View.inflate(this,R.layout.search_activity_top_lay,null);
        setAddView(topView);

        params = new HashMap<>();
        goodsList = new ArrayList<>();
        params.put("keyword","");
        params.put("order",orderType);
        params.put("source","index");
        params.put("start",curPage);
        params.put("limit",pageSize);


        gridView = (WraperGridView) findViewById(R.id.search_goods_gridview);
        adapter = new GoodsGridViewAdapter(goodsList);
        gridView.setAdapter(adapter);
        gridView.setOnNextPageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 加载下一页
                gridView.setNextPageLoading();
                loadNextPage();
            }
        });

        backImg = (ImageView) topView.findViewById(R.id.search_goods_back_img);
        backImg.setOnClickListener(this);
        inputEdit = (EditText) topView.findViewById(R.id.search_goods_input_edit);
        searchBtn = (Button) topView.findViewById(R.id.search_goods_search_btn);
        searchBtn.setOnClickListener(this);
        inputEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                if (actionId== EditorInfo.IME_ACTION_SEARCH ||(event!=null&&event.getKeyCode()== KeyEvent.KEYCODE_ENTER))
                {
                    searchByKeyword();
                    return true;
                }
                return false;
            }
        });
        filterSynthesize = (TextView) topView.findViewById(R.id.search_order_synthesize_text);
        filterPrice = (TextView) topView.findViewById(R.id.search_order_price_text);
        filterSales = (TextView) topView.findViewById(R.id.search_order_sales_text);
        filterComment = (TextView) topView.findViewById(R.id.search_order_comment_text);
        filterPriceImg = (ImageView) topView.findViewById(R.id.search_order_price_img);
        filterSalesImg = (ImageView) topView.findViewById(R.id.search_order_sales_img);
        filterCommentImg = (ImageView) topView.findViewById(R.id.search_order_comment_img);
        priceLay = (LinearLayout) topView.findViewById(R.id.search_goods_price_lay);
        salesLay = (LinearLayout) topView.findViewById(R.id.search_goods_sales_lay);
        commentLay = (LinearLayout) topView.findViewById(R.id.search_goods_comment_lay);
        filterSynthesize.setOnClickListener(this);
        priceLay.setOnClickListener(this);
        salesLay.setOnClickListener(this);
        commentLay.setOnClickListener(this);
        amountText = (TextView) topView.findViewById(R.id.search_goods_amount_text);

        getIntentData();

        loadDataGet(SEARCH_GOODS_LIST,params);
//        showCenterView();
    }

    private void getIntentData(){
        classifyId = getIntent().getIntExtra("classifyId",-1);
        keyword = getIntent().getStringExtra("keyword");
        if(classifyId != -1){
            // 通过类别id进行搜索
            params.put("cateId",classifyId);
        }
        if(keyword != null && !"".equals(keyword)){
            // 通过关键字进行搜索
            try {
                String tempKeyword = URLEncoder.encode(keyword, "utf-8");
                params.put("keyword",tempKeyword);
                inputEdit.setText(keyword);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onLoadDataSuccess(String tag, JSONObject resultObject) {
        super.onLoadDataSuccess(tag, resultObject);
        if(tag.equals(SEARCH_GOODS_LIST)){
            setSearchResult(resultObject);
            if(goodsList.isEmpty()){
                showNoDataView();
            }else {
                showCenterView();
            }
        }
    }

    @Override
    protected void onLoadDataError(String tag, int errorCode, String msg) {
        super.onLoadDataError(tag, errorCode, msg);
        if(tag.equals(SEARCH_GOODS_LIST)){
            if(isNewLoad){
                // 第一次就加载失败
                showErrorView();
            }else {
                // 下一页加载失败
                gridView.setNextPageViewLoadFailed();
            }
        }
    }

    @Override
    protected void tryAgain() {
        super.tryAgain();
        loadDataGet(SEARCH_GOODS_LIST,params);
    }

    private void setSearchResult(JSONObject resultObject){
        if(isNewLoad){
            // 是一次重新加载新的数据
            goodsList.clear();
            int[] results = ParseUtil.parsePureGoodsList(resultObject,goodsList);
            if(results != null){
                curPage = results[0];
                orderType = results[1];
                setOrderType(orderType);
                amountText.setText(results[2]+"");
                if(goodsList.size() >= results[2]){
                    // 已经加载完所有的数据了
                    gridView.setNextPageViewVisible(false);
                    isNewLoad = true;
                }else {
                    // 还有下一页数据
                    gridView.setNextPageLoadNext();
                }
                isNewLoad = false;
            }
            adapter.notifyDataSetChanged();
            // 回滚到顶部
//            gridView.setSelectionAfterHeaderView();
            gridView.setSelection(0);
        }else {
            // 加载下一页
            adapter.notifyDataSetChanged();
            int[] results = ParseUtil.parsePureGoodsList(resultObject,goodsList);
            if(results != null){
                curPage = results[0];
                orderType = results[1];
                if(goodsList.size() >= results[2]){
                    // 已经加载完所有的数据了
                    gridView.setNextPageViewVisible(false);
                }else {
                    // 还有下一页数据
                    gridView.setNextPageLoadNext();
                }
            }
        }
    }


    /**
     * 改变搜索类型order
     * @param type
     */
    private void changeOrderTypeSearch(int type){
        orderType = type;
        curPage = 1;
        params.put("order",orderType);
        params.put("start",curPage);
        loadDataGet(SEARCH_GOODS_LIST,params);
        showLoadingView();
    }

    /**
     * 点击搜索按钮 以关键字搜索
     */
    private void searchByKeyword(){
        orderType = 3;
        String inputKeyword = inputEdit.getText().toString().trim();
        if(!"".equals(inputKeyword)){
            try {
                String tempKeyword = URLEncoder.encode(inputKeyword, "utf-8");
                params.put("keyword",tempKeyword);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }else {
            params.put("keyword","");
        }
        params.put("order",orderType);
        params.put("source","index");
        params.put("start",1);
        params.put("limit",pageSize);
        if(params.containsKey("cateId")){
            params.remove("cateId");
        }
        isPriceUpOrderType = false;
        isNewLoad = true;
        showLoadingView();
        loadDataGet(SEARCH_GOODS_LIST,params);

        // 隐藏键盘
        Util.hideInput(SearchActivity.this,inputEdit);
    }

    /**
     * 加载下一页
     */
    private void loadNextPage(){
        params.put("start",curPage + 1);
        loadDataGet(SEARCH_GOODS_LIST,params);
    }


    private void setOrderType(int orderType){
        // 0=综合,1=团购价格升序,2=按团购价格降序,3=按销量降序,4=按评价降序
        switch (orderType){
            case 0:
                // 综合
                filterSynthesize.setSelected(true);
                filterPrice.setSelected(false);
                filterSales.setSelected(false);
                filterComment.setSelected(false);

                filterPriceImg.setImageResource(R.drawable.icon_filter1);
                filterSalesImg.setImageResource(R.drawable.icon_filter4);
                filterCommentImg.setImageResource(R.drawable.icon_filter4);
                break;
            case 1:
                // 团购价格升序
                filterSynthesize.setSelected(false);
                filterPrice.setSelected(true);
                filterSales.setSelected(false);
                filterComment.setSelected(false);

                filterPriceImg.setImageResource(R.drawable.icon_filter2);
                filterSalesImg.setImageResource(R.drawable.icon_filter4);
                filterCommentImg.setImageResource(R.drawable.icon_filter4);
                break;
            case 2:
                // 团购价格降序
                filterSynthesize.setSelected(false);
                filterPrice.setSelected(true);
                filterSales.setSelected(false);
                filterComment.setSelected(false);

                filterPriceImg.setImageResource(R.drawable.icon_filter3);
                filterSalesImg.setImageResource(R.drawable.icon_filter4);
                filterCommentImg.setImageResource(R.drawable.icon_filter4);
                break;
            case 3:
                // 销量降序
                filterSynthesize.setSelected(false);
                filterPrice.setSelected(false);
                filterSales.setSelected(true);
                filterComment.setSelected(false);

                filterPriceImg.setImageResource(R.drawable.icon_filter1);
                filterSalesImg.setImageResource(R.drawable.icon_filter5);
                filterCommentImg.setImageResource(R.drawable.icon_filter4);
                break;
            case 4:
                // 评价降序
                filterSynthesize.setSelected(false);
                filterPrice.setSelected(false);
                filterSales.setSelected(false);
                filterComment.setSelected(true);

                filterPriceImg.setImageResource(R.drawable.icon_filter1);
                filterSalesImg.setImageResource(R.drawable.icon_filter4);
                filterCommentImg.setImageResource(R.drawable.icon_filter5);
                break;
        }
    }


    /**
     * 通过类别id搜索商品列表
     * @param classifyId
     */
    public static void startActivity(Context context,int classifyId){
        Intent intent = new Intent(context,SearchActivity.class);
        intent.putExtra("classifyId",classifyId);
        context.startActivity(intent);
    }
    /**
     * 通过关键字搜索商品列表
     * @param keyword
     */
    public static void startActivity(Context context,String keyword){
        Intent intent = new Intent(context,SearchActivity.class);
        intent.putExtra("keyword",keyword);
        context.startActivity(intent);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.search_goods_back_img:
                // 返回按钮
                finish();
                break;
            case R.id.search_goods_search_btn:
                // 搜索按钮
                searchByKeyword();
                break;
            case R.id.search_order_synthesize_text:
                // 综合
                if(orderType == 0){
                    return;
                }
                isNewLoad = true;
                gridView.setNextPageViewVisible(true);
                setOrderType(0);
                changeOrderTypeSearch(0);
                isPriceUpOrderType = false;
                break;
            case R.id.search_goods_price_lay:
                // 价格
                if(isPriceUpOrderType){
                    // 团购价格升序
                    isNewLoad = true;
                    gridView.setNextPageViewVisible(true);
                    setOrderType(1);
                    changeOrderTypeSearch(1);
                    isPriceUpOrderType = false;
                }else {
                    //团购价格降序
                    isNewLoad = true;
                    gridView.setNextPageViewVisible(true);
                    setOrderType(2);
                    changeOrderTypeSearch(2);
                    isPriceUpOrderType = true;
                }
                break;
            case R.id.search_goods_sales_lay:
                // 销量降序
                if(orderType == 3){
                    return;
                }
                isNewLoad = true;
                gridView.setNextPageViewVisible(true);
                setOrderType(3);
                changeOrderTypeSearch(3);
                isPriceUpOrderType = false;
                break;
            case R.id.search_goods_comment_lay:
                // 评价降序
                if(orderType == 4){
                    return;
                }
                isNewLoad = true;
                gridView.setNextPageViewVisible(true);
                setOrderType(4);
                changeOrderTypeSearch(4);
                isPriceUpOrderType = false;
                break;
        }
    }
}
