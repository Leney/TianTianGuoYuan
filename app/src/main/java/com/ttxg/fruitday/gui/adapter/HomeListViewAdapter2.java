package com.ttxg.fruitday.gui.adapter;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.ttxg.fruitday.R;
import com.ttxg.fruitday.gui.activity.GoodsDetailActivity;
import com.ttxg.fruitday.gui.view.AdRelativeLayout;
import com.ttxg.fruitday.gui.view.CenterLineTextView;
import com.ttxg.fruitday.gui.view.SimpleDraweeViewLab;
import com.ttxg.fruitday.model.Goods;
import com.ttxg.fruitday.util.log.DLog;

import java.util.List;

/**
 * 首页列表的Adatper
 * Created by liijun on 2016/8/22.
 */
public class HomeListViewAdapter2 extends BaseAdapter {
    /**
     * 商品类型
     */
    public static final int GOODS_TYPE = 1;
    /**
     * 商品广告类型
     */
    public static final int AD_TYPE = 2;
    private List<Goods> childList;


    private OnRequestAdListener listener;

    public void setOnRequestAdListener(OnRequestAdListener listener) {
        this.listener = listener;
    }

    public HomeListViewAdapter2(List<Goods> childList) {
        this.childList = childList;
    }



    @Override
    public Object getItem(int position) {
        return childList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return childList.size();
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        if (childList.get(position).getType() == Goods.GOODS_TYPE) {
            return GOODS_TYPE;
        } else {
            return AD_TYPE;
        }
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ChildHotGoodHolderView hotGoodHolderView = null;
        AdViewHolder adViewHolder = null;
        int type = getItemViewType(position);
        if (view == null) {
            if (type == GOODS_TYPE) {
                view = View.inflate(viewGroup.getContext(), R.layout.hot_goods_item, null);
                hotGoodHolderView = new ChildHotGoodHolderView(view);
                view.setTag(hotGoodHolderView);
            } else {
                // 初始化广告视图
                view = View.inflate(viewGroup.getContext(),R.layout.ad_item,null);
                adViewHolder = new AdViewHolder(view);
                view.setTag(adViewHolder);
            }
        }else {
            if (type == GOODS_TYPE) {
                hotGoodHolderView = (ChildHotGoodHolderView) view.getTag();
            } else {
                // 广告视图
                adViewHolder = (AdViewHolder) view.getTag();
            }
        }

        Goods childItem = (Goods) getItem(position);
        if (type == GOODS_TYPE) {
            hotGoodHolderView.hotImg.setImageURI(childItem.getIcon());
            hotGoodHolderView.hotName.setText(childItem.getName());
            if (childItem.getSalesLevel() == 1) {
                // 售卖新人价，显示市场价和新人价
                hotGoodHolderView.leftPrice.setVisibility(View.INVISIBLE);
                hotGoodHolderView.rightPrice.setVisibility(View.VISIBLE);
                hotGoodHolderView.leftMarketPrice.setVisibility(View.VISIBLE);
                hotGoodHolderView.rightMarketPrice.setVisibility(View.INVISIBLE);
                hotGoodHolderView.hotImg.showLab(false);

                hotGoodHolderView.rightPrice.setText(String.format(viewGroup.getResources()
                        .getString(R.string
                                .format_new_money), childItem.getNewbiePrice() + ""));
                hotGoodHolderView.leftMarketPrice.setText(String.format(viewGroup.getResources()
                        .getString(R.string
                                .format_money), childItem.getMarketPrice() + ""));
            } else if (childItem.getSalesLevel() == 2) {
                // 只显示售卖会员价
                hotGoodHolderView.leftPrice.setVisibility(View.VISIBLE);
                hotGoodHolderView.rightPrice.setVisibility(View.GONE);
                hotGoodHolderView.leftMarketPrice.setVisibility(View.GONE);
                hotGoodHolderView.rightMarketPrice.setVisibility(View.GONE);
                hotGoodHolderView.hotImg.showLab(true);

                hotGoodHolderView.leftPrice.setText(String.format(viewGroup.getResources()
                        .getString(R.string
                                .format_vip_money), childItem.getMemberPrice() + ""));
            } else if (childItem.getSalesLevel() == 3) {
                // 显示非会员价和会员价
                hotGoodHolderView.leftPrice.setVisibility(View.VISIBLE);
                hotGoodHolderView.rightPrice.setVisibility(View.VISIBLE);
                hotGoodHolderView.leftMarketPrice.setVisibility(View.GONE);
                hotGoodHolderView.rightMarketPrice.setVisibility(View.GONE);
                hotGoodHolderView.hotImg.showLab(false);

                hotGoodHolderView.leftPrice.setText(String.format(viewGroup.getResources()
                        .getString(R.string
                                .format_money), childItem.getPrice() + ""));
                hotGoodHolderView.rightPrice.setText(String.format(viewGroup.getResources()
                        .getString(R.string
                                .format_vip_money), childItem.getMemberPrice() + ""));
            } else {
                // 显示非会员价和市场价
                hotGoodHolderView.leftPrice.setVisibility(View.VISIBLE);
                hotGoodHolderView.rightPrice.setVisibility(View.GONE);
                hotGoodHolderView.leftMarketPrice.setVisibility(View.GONE);
                hotGoodHolderView.rightMarketPrice.setVisibility(View.VISIBLE);
                hotGoodHolderView.hotImg.showLab(false);

                hotGoodHolderView.leftPrice.setText(String.format(viewGroup.getResources()
                        .getString(R.string
                                .format_money), childItem.getPrice() + ""));
                hotGoodHolderView.rightMarketPrice.setText(String.format(viewGroup.getResources()
                        .getString(R.string
                                .format_money), childItem.getMarketPrice() + ""));
            }
            hotGoodHolderView.hotLay.setTag(childItem);
        } else {
            // 广告视图
            adViewHolder.itemLay.setTag(childItem);
            if(!childItem.isFullAd()){
                // 未填充过广告  则回调  去信息流广告
                if(listener != null){
                    listener.onRequestAd(position,adViewHolder.itemLay);
                }
            }else {
                // 广告已经被填充过
                // 设置科大讯飞广告请求控制器
                adViewHolder.itemLay.setNativeAd(childItem.getKdxfInfoAdControl());


//                if(!childItem.isAdExposured()){
//                    // 还未上报曝光  也就是第一次显示广告
//                    // 进行上报曝光
//                    childItem.getInfoAd().onExposured(adViewHolder.itemLay);
//                }
                // 显示广告视图
                if(!TextUtils.isEmpty(childItem.getInfoAd().getIcon())){
                    adViewHolder.icon.setImageURI(childItem.getInfoAd().getIcon());
                }else if (!TextUtils.isEmpty(childItem.getInfoAd().getImage())){
                    adViewHolder.icon.setImageURI(childItem.getInfoAd().getImage());
                }
                adViewHolder.mainTitle.setText(childItem.getInfoAd().getTitle());
                adViewHolder.describle.setText(childItem.getInfoAd().getSubTitle());
                if(!TextUtils.isEmpty(childItem.getInfoAd().getAdSourceMark())){
                    adViewHolder.adMark.setText(childItem.getInfoAd().getAdSourceMark() + "|广告" );
                }
            }
        }
        return view;
    }

    class ChildHotGoodHolderView {
        public RelativeLayout hotLay;
        public SimpleDraweeViewLab hotImg;
        public TextView hotName;
        public TextView leftPrice;
        public TextView rightPrice;
        public CenterLineTextView leftMarketPrice;
        public CenterLineTextView rightMarketPrice;

        public void init(View baseView) {
            hotLay = (RelativeLayout) baseView.findViewById(R.id.hot_item_lay);
            hotImg = (SimpleDraweeViewLab) baseView.findViewById(R.id.hot_item_img);
            hotName = (TextView) baseView.findViewById(R.id.hot_item_goods_name);
            leftPrice = (TextView) baseView.findViewById(R.id.hot_item_price_left);
            rightPrice = (TextView) baseView.findViewById(R.id.hot_item_price_right);
            leftMarketPrice = (CenterLineTextView) baseView.findViewById(R.id
                    .hot_item_market_price_left);
            rightMarketPrice = (CenterLineTextView) baseView.findViewById(R.id
                    .hot_item_market_price_right);
            hotLay.setOnClickListener(childItemClickListener);
        }
        public ChildHotGoodHolderView(View baseView) {
            init(baseView);
        }
    }

    public class AdViewHolder {

        public SimpleDraweeView icon;
        /**
         * 主标题
         */
        public TextView mainTitle;

        /**
         * 描述(副标题)
         */
        public TextView describle;
        /**
         * 广告标记
         */
        public TextView adMark;

        public AdRelativeLayout itemLay;

        public void init(View baseView) {
            icon = (SimpleDraweeView) baseView.findViewById(R.id.ad_list_icon);
            mainTitle = (TextView) baseView.findViewById(R.id.ad_list_main_tilte);
            describle = (TextView) baseView.findViewById(R.id.ad_list_sec_title);
            adMark = (TextView) baseView.findViewById(R.id.ad_list_ad_mark);
            itemLay = (AdRelativeLayout) baseView.findViewById(R.id.soft_list_item_lay);
            itemLay.setOnClickListener(adItemClickListner);
        }

        public AdViewHolder(View baseView) {
            init(baseView);
        }

    }

    /**
     * 广告条目点击事件
     */
    private View.OnClickListener adItemClickListner = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.i("llj","信息流广告点击！！！");
            Goods goods = (Goods) view.getTag();
            if (goods == null || goods.getInfoAd() == null) {
                return;
            }

            // 处理点击
            boolean state = goods.getInfoAd().onClicked(view);
            Log.i("llj","信息流广告处理点击，state--->>>"+state);
//            if(TextUtils.equals(goods.getInfoAd().getAdtype(),"AD_DOWNLOAD")){
//                // 下载类型广告
//            }else if (TextUtils.equals(goods.getInfoAd().getAdtype(),"AD_REDIRECT")){
//                // 跳转类型的广告
//            }

//            AdRelativeLayout adRelativeLayout = (AdRelativeLayout) view;
//            DLog.i("llj","downX---->>"+adRelativeLayout.downX);
//            DLog.i("llj","downY---->>"+adRelativeLayout.downY);
//            DLog.i("llj","upX---->>"+adRelativeLayout.upX);
//            DLog.i("llj","upY---->>"+adRelativeLayout.upY);
//            if(!goods.getInfoAd().onClicked(adRelativeLayout)){
//                goods.getKdxfInfoAdControl().setParameter(AdKeys.CLICK_POS_DX,adRelativeLayout.downX + "");
//                goods.getKdxfInfoAdControl().setParameter(AdKeys.CLICK_POS_DY,adRelativeLayout.downY + "");
//                goods.getKdxfInfoAdControl().setParameter(AdKeys.CLICK_POS_UX,adRelativeLayout.upX + "");
//                goods.getKdxfInfoAdControl().setParameter(AdKeys.CLICK_POS_UY,adRelativeLayout.upY + "");
//                goods.getInfoAd().onClicked(adRelativeLayout);
//            }
        }
    };

    /**
     * 商品类型item 点击事件
     */
    private View.OnClickListener childItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Goods goods = (Goods) view.getTag();
            DLog.i("lilijun", "商品类型item点击，goods.getName()---->>>" + goods.getName());
            DLog.i("lilijun", "商品类型item点击，goods.getId()---->>>" + goods.getId());
            GoodsDetailActivity.startActivity(view.getContext(), goods.getId());
        }
    };


    /**
     * 请求广告的监听器
     */
    public interface OnRequestAdListener {
        void onRequestAd(int position,View view);
    }
}
