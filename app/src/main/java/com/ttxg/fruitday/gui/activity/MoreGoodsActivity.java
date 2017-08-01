package com.ttxg.fruitday.gui.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.gui.adapter.GoodsGridViewAdapter;
import com.ttxg.fruitday.gui.view.WraperGridView;
import com.ttxg.fruitday.model.HomeGroupItem;
import com.ttxg.fruitday.model.PureGoods;
import com.ttxg.fruitday.util.ParseUtil;

/**
 * 更多商品界面
 * Created by lilijun on 2016/8/30.
 */
public class MoreGoodsActivity extends BaseActivity implements View.OnClickListener {

    private static final String GET_MORE_GOODS_LIST = "index/storeyMoreGoods";
    private HashMap<String,Object> params;

    private WraperGridView gridView;
    private GoodsGridViewAdapter adapter;
    private List<PureGoods> goodsList;

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
     * 跳转时可能携带过来的商品类别Id
     */
    private int classifyId = -1;

    /**
     * 热销商品更多时携带过来的id
     */
    private int hotGoodsId = -1;

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
    private int orderType = 0;

    /**
     * 当搜索类型为价格类型时，是否为价格升序类型(默认以团购价格降序)
     */
    private boolean isPriceUpOrderType = false;

    /**
     * 标记是否是一次新的请求，而非加载下一页
     */
    private boolean isNewLoad = true;

    /**
     * 标题名称
     */
    private String title;

    @Override
    protected void initView() {
        setCenterView(R.layout.activity_more_goods);
        View topView = View.inflate(this,R.layout.more_activity_top_lay,null);
        setAddView(topView);

        params = new HashMap<>();
        goodsList = new ArrayList<>();
        params.put("order",orderType);
        params.put("start",curPage);
        params.put("limit",pageSize);


        gridView = (WraperGridView) findViewById(R.id.more_goods_gridview);
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

        loadDataGet(GET_MORE_GOODS_LIST,params);
//        showCenterView();
    }

    private void getIntentData(){
        classifyId = getIntent().getIntExtra("storeyId",-1);
        if(classifyId != -1){
            // 通过类别id进行搜索
            params.put("storeyId",classifyId);
        }
        hotGoodsId = getIntent().getIntExtra("hotGoods",-1);
        if(hotGoodsId != -1){
            params.put("hotGoods",hotGoodsId);
        }

        title = getIntent().getStringExtra("titleName");
        setTitleName(title);
    }


    @Override
    protected void onLoadDataSuccess(String tag, JSONObject resultObject) {
        super.onLoadDataSuccess(tag, resultObject);
        if(tag.equals(GET_MORE_GOODS_LIST)){
            setResult(resultObject);
            showCenterView();
            curPage++;
        }
    }

    @Override
    protected void onLoadDataError(String tag, int errorCode, String msg) {
        super.onLoadDataError(tag, errorCode, msg);
        if(tag.equals(GET_MORE_GOODS_LIST)){
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
        loadDataGet(GET_MORE_GOODS_LIST,params);
    }

    private void setResult(JSONObject resultObject){
        if(isNewLoad){
            // 是一次重新加载新的数据
            goodsList.clear();
            Object[] results = ParseUtil.parseMoreGoodsList(resultObject,goodsList);
            if(results != null){
                curPage = (int) results[0];
                orderType = (int) results[1];
                setOrderType(orderType);
                amountText.setText(results[2].toString());
                if(goodsList.size() >= (int) results[2]){
                    // 已经加载完所有的数据了
                    gridView.setNextPageViewVisible(false);
                    isNewLoad = true;
                }else {
                    // 还有下一页数据
                    gridView.setNextPageLoadNext();
                }
                isNewLoad = false;
                if(TextUtils.isEmpty(title) && results[3] != null){
                    // 设置标题
                    setTitleName(results[3].toString());
                }
            }
            adapter.notifyDataSetChanged();
            // 回滚到顶部
//            gridView.setSelectionAfterHeaderView();
            gridView.setSelection(0);
        }else {
            // 加载下一页
            adapter.notifyDataSetChanged();
            Object[] results = ParseUtil.parseMoreGoodsList(resultObject,goodsList);
            if(results != null){
                curPage = (int) results[0];
                orderType = (int) results[1];
                if(goodsList.size() >= (int) results[2]){
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
        loadDataGet(GET_MORE_GOODS_LIST,params);
        showLoadingView();
    }

    /**
     * 加载下一页
     */
    private void loadNextPage(){
        params.put("start",curPage);
        loadDataGet(GET_MORE_GOODS_LIST,params);
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
     * 热销商品更多
     * @param item
     */
    public static void startActivityByRecommendTitle(Context context,HomeGroupItem item){
        Intent intent = new Intent(context,MoreGoodsActivity.class);
        intent.putExtra("hotGoods",item.getId());
        intent.putExtra("titleName",item.getName());
        context.startActivity(intent);
    }
    /**
     * 其它商品更多
     * @param item
     */
    public static void startActivityByClassify(Context context,HomeGroupItem item){
        Intent intent = new Intent(context,MoreGoodsActivity.class);
        intent.putExtra("storeyId",item.getId());
        intent.putExtra("titleName",item.getName());
        context.startActivity(intent);
    }

    /**
     * 未携带标题名称的方式
     * @param context
     * @param storeId
     */
    public static void startActivityByStoreId(Context context,int storeId){
        Intent intent = new Intent(context,MoreGoodsActivity.class);
        intent.putExtra("storeyId",storeId);
        context.startActivity(intent);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
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
