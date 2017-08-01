package com.ttxg.fruitday.gui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.gui.adapter.FragmentViewPagerAdapter;
import com.ttxg.fruitday.gui.fragment.GoodsManagerListFragment;
import com.ttxg.fruitday.gui.fragment.GoodsShelfFragment;

/**
 * 商品管理列表
 * Created by lilijun on 2016/9/29.
 */
public class GoodsManagerListActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     * 价格，销量，库存，货架 文本
     */
    private TextView filterPrice,filterSales,filterStock,filterGoodsShelf;

    /**
     *价格，销量，库存 图标
     */
    private ImageView filterPriceImg,filterSalesImg,filterStockImg;

    /**
     * 价格、销量、库存 lay
     */
    private LinearLayout priceLay,salesLay,stockLay;

    private ViewPager viewPager;

    private ImageView backImg;

    private TextView titleName;

    private TextView choiceGoodsBtn;

    private FragmentViewPagerAdapter adapter;

    private List<Fragment> fragmentList;

    /** 价格Fragment,销量Fragment,库存Fragment*/
    private GoodsManagerListFragment priceFragment,salesFragment,stockFragment;

    /** 货架Fragment*/
    private GoodsShelfFragment goodsShelfFragment;

    /**
     * 标识是否是按照价格降序，默认为true
     */
    private boolean isPriceDownOrder = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_manager_list);

        fragmentList = new ArrayList<>();

        backImg = (ImageView) findViewById(R.id.goods_manager_title_back_img);
        backImg.setOnClickListener(this);
        titleName = (TextView) findViewById(R.id.goods_manager_title_name);
        choiceGoodsBtn = (TextView) findViewById(R.id.goods_manager_title_choice_goods_btn);
        choiceGoodsBtn.setOnClickListener(this);

        titleName.setText(getResources().getString(R.string.goods_manager));

        filterPrice = (TextView) findViewById(R.id.goods_manager_price_text);
        filterSales = (TextView) findViewById(R.id.goods_manager_sales_text);
        filterStock = (TextView) findViewById(R.id.goods_manager_stock_text);
        filterGoodsShelf = (TextView) findViewById(R.id.goods_manager_goods_shelf_text);
        filterGoodsShelf.setOnClickListener(this);
        filterPriceImg = (ImageView) findViewById(R.id.goods_manager_price_img);
        filterSalesImg = (ImageView) findViewById(R.id.goods_manager_sales_img);
        filterStockImg = (ImageView) findViewById(R.id.goods_manager_stock_img);
        priceLay = (LinearLayout) findViewById(R.id.goods_manager_price_lay);
        priceLay.setOnClickListener(this);
        salesLay = (LinearLayout) findViewById(R.id.goods_manager_sales_lay);
        salesLay.setOnClickListener(this);
        stockLay = (LinearLayout) findViewById(R.id.goods_manager_stock_lay);
        stockLay.setOnClickListener(this);
        viewPager = (ViewPager) findViewById(R.id.goods_manager_viewpager);
        adapter = new FragmentViewPagerAdapter(getSupportFragmentManager(),fragmentList);

        priceFragment = new GoodsManagerListFragment();
        priceFragment.init(2);

        salesFragment = new GoodsManagerListFragment();
        salesFragment.init(3);

        stockFragment = new GoodsManagerListFragment();
        stockFragment.init(4);

        goodsShelfFragment = new GoodsShelfFragment();

        fragmentList.add(priceFragment);
        fragmentList.add(salesFragment);
        fragmentList.add(stockFragment);
        fragmentList.add(goodsShelfFragment);

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setFilter(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setFilter(0);
    }

    /**
     * 设置正常切换默认的过滤条件的视图显示
     * @param position
     */
    private void setFilter(int position){
        switch (position){
            case 0:
                // 价格
                filterPrice.setSelected(true);
                if(isPriceDownOrder){
                    // 价格降序
                    filterPriceImg.setImageResource(R.drawable.icon_filter3);
                }else {
                    // 价格升序
                    filterPriceImg.setImageResource(R.drawable.icon_filter2);
                }

                filterSales.setSelected(false);
                filterSalesImg.setImageResource(R.drawable.icon_filter4);
                filterStock.setSelected(false);
                filterStockImg.setImageResource(R.drawable.icon_filter4);
                filterGoodsShelf.setSelected(false);
                break;
            case 1:
                // 销量
                filterPrice.setSelected(false);
                filterPriceImg.setImageResource(R.drawable.icon_filter1);
                filterSales.setSelected(true);
                filterSalesImg.setImageResource(R.drawable.icon_filter5);
                filterStock.setSelected(false);
                filterStockImg.setImageResource(R.drawable.icon_filter4);
                filterGoodsShelf.setSelected(false);
                break;
            case 2:
                // 库存
                filterPrice.setSelected(false);
                filterPriceImg.setImageResource(R.drawable.icon_filter1);
                filterSales.setSelected(false);
                filterSalesImg.setImageResource(R.drawable.icon_filter4);
                filterStock.setSelected(true);
                filterStockImg.setImageResource(R.drawable.icon_filter5);
                filterGoodsShelf.setSelected(false);
                break;
            case 3:
                // 货架
                filterPrice.setSelected(false);
                filterPriceImg.setImageResource(R.drawable.icon_filter1);
                filterSales.setSelected(false);
                filterSalesImg.setImageResource(R.drawable.icon_filter4);
                filterStock.setSelected(false);
                filterStockImg.setImageResource(R.drawable.icon_filter4);
                filterGoodsShelf.setSelected(true);
                break;
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.goods_manager_price_lay:
                // 价格区域
                if(viewPager.getCurrentItem() == 0){
                    // 当前viewpager已经在价格界面
                    if(isPriceDownOrder){
                        // 如果当前是按照价格降序，则重新加载价格升序
                        priceFragment.loadNewType(1);
                        isPriceDownOrder = false;
                    }else {
                        priceFragment.loadNewType(2);
                        isPriceDownOrder = true;
                    }
                }else {
                    viewPager.setCurrentItem(0);
                }
                setFilter(0);
                break;
            case R.id.goods_manager_sales_lay:
                // 销量区域
                viewPager.setCurrentItem(1);
                setFilter(1);
                break;
            case R.id.goods_manager_stock_lay:
                // 库存区域
                viewPager.setCurrentItem(2);
                setFilter(2);
                break;
            case R.id.goods_manager_goods_shelf_text:
                // 货架
                viewPager.setCurrentItem(3);
                setFilter(3);
                break;
            case R.id.goods_manager_title_back_img:
                // 标题返回按钮
                finish();
                break;
            case R.id.goods_manager_title_choice_goods_btn:
                // 选择商品按钮
                ChoiceGoodsActivity.startActivity(GoodsManagerListActivity.this);
                break;
        }
    }

    public static void startActivity(Context context){
        Intent intent = new Intent(context,GoodsManagerListActivity.class);
        context.startActivity(intent);
    }
}
