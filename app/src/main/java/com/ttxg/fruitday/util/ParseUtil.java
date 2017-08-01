package com.ttxg.fruitday.util;

import android.text.TextUtils;

import com.ttxg.fruitday.manager.UserInfoManager;
import com.ttxg.fruitday.model.AccountInfo;
import com.ttxg.fruitday.model.AdItem;
import com.ttxg.fruitday.model.Address;
import com.ttxg.fruitday.model.Classify;
import com.ttxg.fruitday.model.Comment;
import com.ttxg.fruitday.model.Goods;
import com.ttxg.fruitday.model.GoodsDetail;
import com.ttxg.fruitday.model.GoodsShelf;
import com.ttxg.fruitday.model.HomeGroupItem;
import com.ttxg.fruitday.model.MyComment;
import com.ttxg.fruitday.model.MyGoods;
import com.ttxg.fruitday.model.OrderDetailInfo;
import com.ttxg.fruitday.model.OrderGoodsInfo;
import com.ttxg.fruitday.model.OrderInfo;
import com.ttxg.fruitday.model.OrderListInfo;
import com.ttxg.fruitday.model.PureGoods;
import com.ttxg.fruitday.model.Shop;
import com.ttxg.fruitday.model.ShoppingCart;
import com.ttxg.fruitday.model.SkuInfo;
import com.ttxg.fruitday.model.SpinnerMode;
import com.ttxg.fruitday.model.Store;
import com.ttxg.fruitday.model.StoreDetail;
import com.ttxg.fruitday.model.TopUpInfo;
import com.ttxg.fruitday.model.UserCenterMenu;
import com.ttxg.fruitday.model.UserInfo;
import com.ttxg.fruitday.model.WXPrepareInfo;
import com.ttxg.fruitday.util.log.DLog;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 解析数据帮助类
 * Created by yb on 2016/8/12.
 */
public class ParseUtil {
    private static final String TAG = "ParseUtil";

    /**
     * 解析首页数据
     *
     * @param resultObject     网络返回结果
     * @param groupList        商品分类列表集合
     * @param childList        商品分类具体数据集合
     * @param headerBannerList 头部轮播banner数据集合
     */
    public static void parseHomeData(
            JSONObject resultObject,
            List<HomeGroupItem> groupList,
            List<List<Goods[]>> childList,
            List<AdItem> headerBannerList) {
        try {
            //得到商品列表
            if (resultObject.has("title")) {
                JSONObject titleObject = resultObject.getJSONObject("title");
                HomeGroupItem groupItem = new HomeGroupItem();
                groupItem.setId(titleObject.getInt("id"));
                groupItem.setType(HomeGroupItem.HOT_TYPE);
                groupItem.setName(titleObject.getString("name"));
                groupItem.setDate(titleObject.getString("addtime"));
                groupList.add(groupItem);

                JSONArray recommendArray = resultObject.getJSONArray("hotGoodsList");
                int recommendLength = recommendArray.length();
                List<Goods[]> hotItems = new ArrayList<>();
                for (int i = 0; i < recommendLength; i++) {
                    JSONObject recommendObject = recommendArray.getJSONObject(i);
                    Goods childItem = parseGoods(recommendObject);
                    if (childItem != null) {
                        childItem.setType(Goods.HOT_TYPE);
                        Goods[] childItemArray = new Goods[1];
                        childItemArray[0] = childItem;
                        hotItems.add(childItemArray);
                    }
                }
                childList.add(hotItems);
            } else {
                DLog.i("没有title!@!!!");
            }

            JSONArray itemArray = resultObject.getJSONArray("storeyDataList");
            int length = itemArray.length();
            for (int i = 0; i < length; i++) {
                HomeGroupItem groupItem = new HomeGroupItem();
                JSONObject classifyObject = itemArray.getJSONObject(i);
                groupItem.setId(classifyObject.getInt("id"));
                groupItem.setDate(classifyObject.getString("addtime"));
                groupItem.setName(classifyObject.getString("storeyName"));
                groupList.add(groupItem);
                JSONArray goodsArray;
                if (classifyObject.isNull("goodsList")) {
                    goodsArray = new JSONArray();
                } else {
                    goodsArray = classifyObject.getJSONArray("goodsList");
                }
                List<Goods[]> childItems = new ArrayList<>();


                JSONArray adArray;
                if (classifyObject.isNull("detailList")) {
                    adArray = new JSONArray();
                } else {
                    adArray = classifyObject.getJSONArray("detailList");
                }
                int adLength = adArray.length();
                for (int k = 0; k < adLength; k++) {
                    Goods[] childItemAdArray = new Goods[1];
                    Goods childItem = new Goods();
                    JSONObject adObject = adArray.getJSONObject(k);
                    AdItem adItem = parseAdItem(adObject);
                    if (adItem != null) {
                        childItem.setType(Goods.AD_TYPE);
                        childItem.setAdItem(adItem);
                        childItemAdArray[0] = childItem;
                        childItems.add(childItemAdArray);
                    }
                }

                int goodsLength = goodsArray.length();
                Goods[] childItemArray = null;
                for (int j = 0; j < goodsLength; j++) {
                    JSONObject goodsObject = goodsArray.getJSONObject(j);
                    Goods childItem = null;
                    if (j % 2 == 0) {
                        childItemArray = new Goods[2];
                        childItem = parseGoods(goodsObject);
                        if (childItem != null) {
                            childItem.setType(Goods.GOODS_TYPE);
                            childItemArray[0] = childItem;
                            if (j >= goodsLength - 1) {
                                //后面没有数据了
                                childItems.add(childItemArray);
                            }
                        }
                    } else {
                        childItem = parseGoods(goodsObject);
                        if (childItem != null) {
                            childItem.setType(Goods.GOODS_TYPE);
                            childItemArray[1] = childItem;
                            childItems.add(childItemArray);
                        }
                    }

                }
                childList.add(childItems);
            }

            // 解析顶部banner数据
            JSONArray bannerArray = resultObject.getJSONArray("bannerList");
            int bannerLength = bannerArray.length();
            for (int i = 0; i < bannerLength; i++) {
                JSONObject bannerObject = bannerArray.getJSONObject(i);
                AdItem adItem = parseAdItem(bannerObject);
                if (adItem != null) {
                    headerBannerList.add(adItem);
                }
            }
        } catch (Exception e) {
            DLog.e(TAG, "解析首页返回数据时发生异常#exception：", e);
        }
    }

    /**
     * 解析首页数据
     *
     * @param resultObject     网络返回结果
     * @param groupList        商品分类列表集合
     * @param childList        商品分类具体数据集合
     * @param headerBannerList 头部轮播banner数据集合
     */
    public static void parseHomeData2(
            JSONObject resultObject,
            List<HomeGroupItem> groupList,
            List<List<Goods>> childList,
            List<AdItem> headerBannerList) {
        try {
            //得到商品列表
            if (resultObject.has("title")) {
                JSONObject titleObject = resultObject.getJSONObject("title");
                HomeGroupItem groupItem = new HomeGroupItem();
                groupItem.setId(titleObject.getInt("id"));
                groupItem.setType(HomeGroupItem.HOT_TYPE);
                groupItem.setName(titleObject.getString("name"));
                groupItem.setDate(titleObject.getString("addtime"));
                groupList.add(groupItem);

                JSONArray recommendArray = resultObject.getJSONArray("hotGoodsList");
                int recommendLength = recommendArray.length();
                List<Goods> hotItems = new ArrayList<>();
                for (int i = 0; i < recommendLength; i++) {
                    JSONObject recommendObject = recommendArray.getJSONObject(i);
                    Goods childItem = parseGoods(recommendObject);

                    if (childItem != null) {
                        childItem.setType(Goods.GOODS_TYPE);
                        hotItems.add(childItem);
//                        Goods[] childItemArray = new Goods[1];
//                        childItemArray[0] = childItem;
//                        hotItems.add(childItemArray);
                    }

                    if(i % 3 == 0){
                        Goods adItem = new Goods();
                        adItem.setType(Goods.AD_TYPE);
                        adItem.setFullAd(false);
                        hotItems.add(adItem);
                    }
                }
                childList.add(hotItems);
            } else {
                DLog.i("没有title!@!!!");
            }

            JSONArray itemArray = resultObject.getJSONArray("storeyDataList");
            int length = itemArray.length();
            for (int i = 0; i < length; i++) {
                HomeGroupItem groupItem = new HomeGroupItem();
                JSONObject classifyObject = itemArray.getJSONObject(i);
                groupItem.setId(classifyObject.getInt("id"));
                groupItem.setDate(classifyObject.getString("addtime"));
                groupItem.setName(classifyObject.getString("storeyName"));
                groupList.add(groupItem);
                JSONArray goodsArray;
                if (classifyObject.isNull("goodsList")) {
                    goodsArray = new JSONArray();
                } else {
                    goodsArray = classifyObject.getJSONArray("goodsList");
                }


//                List<Goods[]> childItems = new ArrayList<>();
//                JSONArray adArray;
//                if (classifyObject.isNull("detailList")) {
//                    adArray = new JSONArray();
//                } else {
//                    adArray = classifyObject.getJSONArray("detailList");
//                }
//                int adLength = adArray.length();
//                for (int k = 0; k < adLength; k++) {
//                    Goods[] childItemAdArray = new Goods[1];
//                    Goods childItem = new Goods();
//                    JSONObject adObject = adArray.getJSONObject(k);
//                    AdItem adItem = parseAdItem(adObject);
//                    if (adItem != null) {
//                        childItem.setType(Goods.AD_TYPE);
//                        childItem.setAdItem(adItem);
//                        childItemAdArray[0] = childItem;
//                        childItems.add(childItemAdArray);
//                    }
//                }

                List<Goods> childItems = new ArrayList<>();
                int goodsLength = goodsArray.length();
//                Goods[] childItemArray = null;
                for (int j = 0; j < goodsLength; j++) {
                    JSONObject goodsObject = goodsArray.getJSONObject(j);
                    Goods childItem = parseGoods(goodsObject);
                    if(childItem != null){
                        childItems.add(childItem);
                    }

                    if(j % 3 == 0){
                        Goods adItem = new Goods();
                        adItem.setType(Goods.AD_TYPE);
                        adItem.setFullAd(false);
                        childItems.add(adItem);
                    }
//                    if (j % 2 == 0) {
//                        childItemArray = new Goods[2];
//                        childItem = parseGoods(goodsObject);
//                        if (childItem != null) {
//                            childItem.setType(Goods.GOODS_TYPE);
//                            childItemArray[0] = childItem;
//                            if (j >= goodsLength - 1) {
//                                //后面没有数据了
//                                childItems.add(childItemArray);
//                            }
//                        }
//                    } else {
//                        childItem = parseGoods(goodsObject);
//                        if (childItem != null) {
//                            childItem.setType(Goods.GOODS_TYPE);
//                            childItemArray[1] = childItem;
//                            childItems.add(childItemArray);
//                        }
//                    }

                }
                childList.add(childItems);
            }

            // 解析顶部banner数据
            JSONArray bannerArray = resultObject.getJSONArray("bannerList");
            int bannerLength = bannerArray.length();
            for (int i = 0; i < bannerLength; i++) {
                JSONObject bannerObject = bannerArray.getJSONObject(i);
                AdItem adItem = parseAdItem(bannerObject);
                if (adItem != null) {
                    headerBannerList.add(adItem);
                }
            }
        } catch (Exception e) {
            DLog.e(TAG, "解析首页返回数据时发生异常#exception：", e);
        }
    }

    /**
     * 解析首页数据
     *
     * @param resultObject     网络返回结果
     * @param childList        商品分类具体数据集合
     * @param headerBannerList 头部轮播banner数据集合
     */
    public static void parseHomeData3(
            JSONObject resultObject,
            List<Goods> childList,
            List<AdItem> headerBannerList) {
        try {
            //得到商品列表
            if (resultObject.has("title")) {
//                JSONObject titleObject = resultObject.getJSONObject("title");
//                HomeGroupItem groupItem = new HomeGroupItem();
//                groupItem.setId(titleObject.getInt("id"));
//                groupItem.setType(HomeGroupItem.HOT_TYPE);
//                groupItem.setName(titleObject.getString("name"));
//                groupItem.setDate(titleObject.getString("addtime"));
//                groupList.add(groupItem);

                JSONArray recommendArray = resultObject.getJSONArray("hotGoodsList");
                int recommendLength = recommendArray.length();
//                List<Goods> hotItems = new ArrayList<>();
                for (int i = 0; i < recommendLength; i++) {
                    JSONObject recommendObject = recommendArray.getJSONObject(i);
                    Goods childItem = parseGoods(recommendObject);

                    if (childItem != null) {
                        childItem.setType(Goods.GOODS_TYPE);
                        childList.add(childItem);
                    }

                    if(i % 3 == 0){
                        Goods adItem = new Goods();
                        adItem.setType(Goods.AD_TYPE);
                        adItem.setFullAd(false);
                        childList.add(adItem);
                    }
                }
//                childList.add(hotItems);
            } else {
                DLog.i("没有title!@!!!");
            }

            JSONArray itemArray = resultObject.getJSONArray("storeyDataList");
            int length = itemArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject classifyObject = itemArray.getJSONObject(i);
                JSONArray goodsArray;
                if (classifyObject.isNull("goodsList")) {
                    goodsArray = new JSONArray();
                } else {
                    goodsArray = classifyObject.getJSONArray("goodsList");
                }


                int goodsLength = goodsArray.length();
//                Goods[] childItemArray = null;
                for (int j = 0; j < goodsLength; j++) {
                    JSONObject goodsObject = goodsArray.getJSONObject(j);
                    Goods childItem = parseGoods(goodsObject);
                    if(childItem != null){
                        childItem.setType(Goods.GOODS_TYPE);
                        childList.add(childItem);
                    }

                    if(j % 3 == 0){
                        Goods adItem = new Goods();
                        adItem.setType(Goods.AD_TYPE);
                        adItem.setFullAd(false);
                        childList.add(adItem);
                    }

                }
//                childList.add(childItems);
            }

            // 解析顶部banner数据
            JSONArray bannerArray = resultObject.getJSONArray("bannerList");
            int bannerLength = bannerArray.length();
            for (int i = 0; i < bannerLength; i++) {
                JSONObject bannerObject = bannerArray.getJSONObject(i);
                AdItem adItem = parseAdItem(bannerObject);
                if (adItem != null) {
                    headerBannerList.add(adItem);
                }
            }
        } catch (Exception e) {
            DLog.e(TAG, "解析首页返回数据时发生异常#exception：", e);
        }
    }

    /**
     * 解析单个商品信息
     */
    private static Goods parseGoods(JSONObject object) {
        try {
            Goods goods = new Goods();
            goods.setName(object.getString("goodsName"));
            goods.setId(object.getInt("id"));
//            String homeDetailId = object.getString("homeDetailId");
//            if(homeDetailId != null){
//                goods.setHomeDetailId(Integer.parseInt(homeDetailId));
//            }
            goods.setIcon(object.getString("goodsUrl"));
            goods.setPrice(getJsonDoubleValue("price", 0.0, object));
            goods.setMarketPrice(object.getDouble("marketPrice"));
            goods.setBidPrice(object.getDouble("factoryPrice"));

            goods.setEvaluateNums(getJsonIntValue("evaluateNums", 0, object));
            goods.setSalesNums(getJsonIntValue("salesNum", 0, object));

            goods.setSalesLevel(getJsonIntValue("saleLevel", 0, object));
            goods.setMemberPrice(getJsonDoubleValue("memberPrice", 0.0, object));
            goods.setNewbiePrice(getJsonDoubleValue("newbiePrice", 0.0, object));
            return goods;
        } catch (Exception e) {
            DLog.e(TAG, "解析商品出现异常#exception:\n", e);
        }
        return null;
    }

    /**
     * 解析单个广告item
     */
    private static AdItem parseAdItem(JSONObject object) {
        try {
            AdItem adItem = new AdItem();
            adItem.setDate(object.getString("addtime"));
            adItem.setId(object.getInt("id"));
            adItem.setIcon(object.getString("adpic"));
            adItem.setSkipUrl(object.getString("adurl"));
            adItem.setType(getJsonIntValue("pageType",-1,object));
            adItem.setGoodsId(getJsonIntValue("goodsid", -1, object));
            adItem.setStoreId(getJsonIntValue("storeyId", -1, object));
            adItem.setName(getJsonStringValue("name",object));
            return adItem;
        } catch (Exception e) {
            DLog.e(TAG, "解析AdItem出现异常#exception:\n", e);
        }
        return null;
    }

    /**
     * 解析商品详情数据
     *
     * @param resultObject 返回结果jsonObject
     * @return GoodsDetail
     */
//    public static GoodsDetail parseGoodsDetail(JSONObject resultObject, List<Comment>
// goodCommentList,List<Comment> normalCommentList,List<Comment> lowCommentList){
    public static void parseGoodsDetail(JSONObject resultObject, GoodsDetail goodsDetail, Shop
            shop) {
//        goodsDetail = new GoodsDetail();
//        shop = new Shop();
        List<String> imgUrls = new ArrayList<>();
        try {
            JSONObject goodsObject = resultObject.getJSONObject("goods");
            DLog.i("goodsObject.toString()----->>>" + goodsObject.toString());
            goodsDetail.setId(goodsObject.getInt("id"));
//            if (goodsObject.has("sku_id_no")) {
//                goodsDetail.setDefaultSkuId(goodsObject.getInt("sku_id_no"));
//            } else {
//                goodsDetail.setDefaultSkuId(-1);
//            }
            goodsDetail.setCategoryId(getJsonIntValue("categoryId", -1, goodsObject));
            goodsDetail.setCode(goodsObject.getString("goodsCode"));
            goodsDetail.setName(goodsObject.getString("goodsName"));
            goodsDetail.setSalesNums(getJsonIntValue("salesNumSum", 0, goodsObject));
            //库存数量
            goodsDetail.setStock(goodsObject.getInt("quantitySum"));
            goodsDetail.setStatus(goodsObject.getInt("status"));

            // 销售价格等级
            goodsDetail.setSalesLevel(getJsonIntValue("saleLevel", 0, goodsObject));

            if (resultObject.has("goodsType")) {
                // 配送类型
                goodsDetail.setSendType(resultObject.getInt("goodsType"));
            } else {
                DLog.i("lilijun", "没有goodsType key!!!");
            }

            if (resultObject.has("success")) {
                // 是否包邮
                goodsDetail.setTakeFreight(resultObject.getBoolean("success"));
                if (!goodsDetail.isTakeFreight()) {
                    // 如果不包邮，则去获取单个邮费价格
                    goodsDetail.setFreight(resultObject.getDouble("freight"));
                } else {
                    // 如果包邮则设置为0.00
                    goodsDetail.setFreight(0.00);
                }
            } else {
                DLog.i("lilijun", "没有包邮信息！！！");
            }
            // 发货人userId
            goodsDetail.setFhUserId(resultObject.getInt("fh_user_id"));

            // 解析组合起来的skuInfo集合数据
            JSONArray skuArray = goodsObject.getJSONArray("skulist");
            int skuLength = skuArray.length();
            for (int i = 0; i < skuLength; i++) {
                JSONObject skuObject = skuArray.getJSONObject(i);
                SkuInfo skuInfo = new SkuInfo();
                skuInfo.setId(skuObject.getInt("id"));
                skuInfo.setSkuName1(skuObject.getString("sku1Name"));
                skuInfo.setSkuValue1(skuObject.getString("sku1Value"));
                skuInfo.setSkuName2(skuObject.getString("sku2Name"));
                skuInfo.setSkuValue2(skuObject.getString("sku2Value"));
                skuInfo.setQuantity(skuObject.getInt("quantity"));
                skuInfo.setSalesNum(skuObject.getInt("salesNum"));
                skuInfo.setMarketPrice(getJsonDoubleValue("shopMarketPrice", 0.0d, skuObject));
                skuInfo.setPrice(getJsonDoubleValue("groupBuyPrice", 0.0d, skuObject));
                skuInfo.setFactoryPrice(getJsonDoubleValue("exFactoryPrice", 0.0d, skuObject));
                skuInfo.setNewbiePrice(getJsonDoubleValue("newbiePrice", 0.0d, skuObject));
                skuInfo.setMemberPrice(getJsonDoubleValue("memberPrice", 0.0d, skuObject));
                skuInfo.setBarCode(skuObject.getString("barCode"));
                skuInfo.setRecommed(skuObject.getBoolean("isRecommend"));
                skuInfo.setGoodsId(skuObject.getInt("goodsId"));
                skuInfo.setSku(skuObject.getBoolean("isSku"));

                if (i == 0) {
                    goodsDetail.setDefaultSkuInfo(skuInfo);
                    goodsDetail.setHasSkuInfos(skuInfo.isSku());
                }

                // 键值是sku1Value 和 sku2Value的组合
                String key = skuInfo.getSkuValue1() + skuInfo.getSkuValue2();
                DLog.i("解析出来key------->>>" + key);
                // 添加组合的skuInfo
                goodsDetail.getSkuTotalMap().put(key, skuInfo);
//                goodsDetail.getSkuInfoList().add(skuInfo);
            }

            if (goodsDetail.isHasSkuInfos()) {
                // 解析类型1(sku1)的数据
                JSONArray sku1Array = resultObject.getJSONArray("sku1list");
                int sku1Length = sku1Array.length();
                DLog.i("sku1Length-------->>>" + sku1Length);
                DLog.i("sku1Array.toString()-------->>>" + sku1Array.toString());
                // 有多少条数据，就证明类型1就有多少条子value
                String[] sku1Values = new String[sku1Length];
                for (int i = 0; i < sku1Length; i++) {
                    JSONObject sku1Object = sku1Array.getJSONObject(i);
                    if (TextUtils.isEmpty(goodsDetail.getSku1Name())) {
                        // sku1Name只取第一条数据的值就行了，因为后面的都是一样的
                        goodsDetail.setSku1Name(sku1Object.getString("sku1Name"));
                        if (TextUtils.isEmpty(goodsDetail.getSku1Name()) || TextUtils.equals
                                (goodsDetail.getSku1Name(), "null")) {
                            // 设置没有sku1信息
                            goodsDetail.setSku1Name("");
                            sku1Values = null;
                            break;
                        }
                    }
                    sku1Values[i] = sku1Object.getString("sku1Value");
                }
                goodsDetail.setSku1Values(sku1Values);

                // 解析类型2(sku2)的数据
                JSONArray sku2Array = resultObject.getJSONArray("sku2list");
                int sku2Length = sku2Array.length();
                // 有多少条数据，就证明类型1就有多少条子value
                String[] sku2Values = new String[sku2Length];
                for (int i = 0; i < sku2Length; i++) {
                    JSONObject sku2Object = sku2Array.getJSONObject(i);
                    if (TextUtils.isEmpty(goodsDetail.getSku2Name())) {
                        // sku2Name只取第一条数据的值就行了，因为后面的都是一样的
                        goodsDetail.setSku2Name(sku2Object.getString("sku2Name"));
                        if (TextUtils.isEmpty(goodsDetail.getSku2Name()) || TextUtils.equals
                                (goodsDetail.getSku2Name(), "null")) {
                            // 设置没有sku2信息
                            DLog.i("没有sku2信息  解析时");
                            goodsDetail.setSku2Name("");
                            sku2Values = null;
                            break;
                        }
                    }
                    sku2Values[i] = sku2Object.getString("sku2Value");
                    DLog.i("解析出来的sku2Values[" + i + "]--------->>>" + sku2Values[i]);
                }
                goodsDetail.setSku2Values(sku2Values);
            }

            JSONObject detailObject = resultObject.getJSONObject("detail");

            //html 介绍
            goodsDetail.setIntroduce(detailObject.getString("detail"));
            if (goodsDetail.getDefaultSkuInfo() != null) {
                // 如果有默认的SkuInfo信息  则直接赋值默认的SkuInfo的价格信息
                goodsDetail.setFactoryPrice(goodsDetail.getDefaultSkuInfo().getFactoryPrice());
                goodsDetail.setMarketPrice(goodsDetail.getDefaultSkuInfo().getMarketPrice());
                goodsDetail.setPrice(goodsDetail.getDefaultSkuInfo().getPrice());
            } else {
                // 如果没有则去解析价格信息
                goodsDetail.setFactoryPrice(resultObject.getDouble("ex_factory_price"));
                goodsDetail.setMarketPrice(resultObject.getDouble("shop_market_price"));
                goodsDetail.setPrice(resultObject.getDouble("group_buy_price"));
            }

            //店铺信息
            JSONObject shopObject = resultObject.getJSONObject("store");
            shop.setName(shopObject.getString("storeName"));
            shop.setId(shopObject.getInt("id"));
            shop.setLogoUrl(shopObject.getString("storeLogo"));
            shop.setUserId(shopObject.getInt("userId"));
            shop.setStoreType(resultObject.getInt("userType"));

            shop.setTelephone(shopObject.getString("storeTelephone"));

            String img1 = goodsObject.getString("picServerUrl1");
            if (img1 != null && !"".equals(img1.trim()) && !"null".equals(img1.trim())) {
                imgUrls.add(img1);
            }
            String img2 = goodsObject.getString("picServerUrl2");
            if (img2 != null && !"".equals(img2.trim()) && !"null".equals(img2.trim())) {
                imgUrls.add(img2);
            }
            String img3 = goodsObject.getString("picServerUrl3");
            if (img3 != null && !"".equals(img3.trim()) && !"null".equals(img3.trim())) {
                imgUrls.add(img3);
            }
            String img4 = goodsObject.getString("picServerUrl4");
            if (img4 != null && !"".equals(img4.trim()) && !"null".equals(img4.trim())) {
                imgUrls.add(img4);
            }
            String img5 = goodsObject.getString("picServerUrl5");
            if (img5 != null && !"".equals(img5.trim()) && !"null".equals(img5.trim())) {
                imgUrls.add(img5);
            }

//            // 解析评论信息
//            //好评
//            JSONArray goodCommentArray = resultObject.getJSONArray("goodsRateList1");
//            int length1 = goodCommentArray.length();
//            for (int i = 0; i < length1; i++) {
//                JSONObject commentObject = goodCommentArray.getJSONObject(i);
//                Comment comment = new Comment();
//                comment.setContent(commentObject.getString("content"));
//                comment.setDate(commentObject.getString("addTime"));
//                comment.setStar(commentObject.getInt("evaluation"));
//                comment.setType(commentObject.getInt("rateType"));
//                comment.setUserName(commentObject.getString("user"));
//                goodCommentList.add(comment);
//            }

//            // 中评
//            JSONArray normalCommentArray = resultObject.getJSONArray("goodsRateList2");
//            int length2 = normalCommentArray.length();
//            for (int i = 0; i < length2; i++) {
//                JSONObject commentObject = normalCommentArray.getJSONObject(i);
//                Comment comment = new Comment();
//                comment.setContent(commentObject.getString("content"));
//                comment.setDate(commentObject.getString("addTime"));
//                comment.setStar(commentObject.getInt("evaluation"));
//                comment.setType(commentObject.getInt("rateType"));
//                comment.setUserName(commentObject.getString("user"));
//                normalCommentList.add(comment);
//            }

//            // 差评
//            JSONArray lowCommentArray = resultObject.getJSONArray("goodsRateList3");
//            int length3 = lowCommentArray.length();
//            for (int i = 0; i < length3; i++) {
//                JSONObject commentObject = lowCommentArray.getJSONObject(i);
//                Comment comment = new Comment();
//                comment.setContent(commentObject.getString("content"));
//                comment.setDate(commentObject.getString("addTime"));
//                comment.setStar(commentObject.getInt("evaluation"));
//                comment.setType(commentObject.getInt("rateType"));
//                comment.setUserName(commentObject.getString("user"));
//                lowCommentList.add(comment);
//            }

            goodsDetail.setImgUrls(imgUrls);
//            goodsDetail.setShop(shop);
//            return goodsDetail;
        } catch (Exception e) {
            DLog.e(TAG, "解析GoodsDetail出现异常#exception:\n", e);
        }
//        return null;
    }

    /**
     * 解析评论列表
     *
     * @param resultObject
     * @return
     */
    public static List<Comment> parseCommentList(JSONObject resultObject) {
        try {
            List<Comment> commentList = new ArrayList<>();
            JSONArray commentArray = resultObject.getJSONArray("goodsRateList");
            int length = commentArray.length();
//            // test
//            Comment commentTest = null;
            for (int i = 0; i < length; i++) {
                Comment comment = new Comment();
//                commentTest = comment;
                JSONObject commentObject = commentArray.getJSONObject(i);
                comment.setContent(commentObject.getString("content"));
                comment.setDate(commentObject.getString("addTime"));
                comment.setStar(commentObject.getInt("evaluation"));
                comment.setType(commentObject.getInt("rateType"));
                comment.setUserName(commentObject.getString("user"));
                commentList.add(comment);
            }
//            for (int i = 0;i < 30;i++){
//                commentList.add(commentTest);
//            }
            return commentList;
        } catch (Exception e) {
            DLog.e(TAG, "parseCommentList()#exception:\n", e);
        }
        return null;
    }


    /**
     * 解析总类别列表
     *
     * @param resultObject
     * @return
     */
    public static List<Classify> parseClassifyList(JSONObject resultObject) {
        try {
            List<Classify> classifyList = new ArrayList<>();
            JSONArray classifyArray = resultObject.getJSONArray("categoryList");
            int length = classifyArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject classifyObject = classifyArray.getJSONObject(i);
                Classify classify = new Classify();
                classify.setId(classifyObject.getInt("id"));
                classify.setName(classifyObject.getString("cateName"));
                classify.setpId(classifyObject.getInt("pid"));
                classify.setLogo(classifyObject.getString("logo"));
                if (i == 0) {
                    classify.setSelected(true);
                } else {
                    classify.setSelected(false);
                }
                classifyList.add(classify);
            }
            return classifyList;
        } catch (Exception e) {
            DLog.e(TAG, "parseClassifyList()#exception:\n", e);
        }
        return null;
    }

    /**
     * 解析子类别列表
     *
     * @param resultObject
     * @return
     */
    public static void parseClassifyChildList(JSONObject resultObject, List<String> groupList,
                                              List<List<Classify[]>> childList) {
        try {
            JSONArray classifyArray = resultObject.getJSONArray("categoryViews2");
            int length = classifyArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject classifyObject = classifyArray.getJSONObject(i);
                groupList.add(classifyObject.getString("cateName"));
                List<Classify[]> childClassifyList = new ArrayList<>();

                JSONArray childClassifyArray = classifyObject.getJSONArray("categoryViews");
                int length2 = childClassifyArray.length();
                Classify[] childClassify = null;
                for (int j = 0; j < length2; j++) {
                    JSONObject classifyChildObject = childClassifyArray.getJSONObject(j);

                    if (j % 3 == 0) {
                        // 新的一组数据
                        childClassify = new Classify[3];
                        Classify classify = new Classify();
                        classify.setId(classifyChildObject.getInt("id"));
                        classify.setName(classifyChildObject.getString("cateName"));
                        classify.setpId(classifyChildObject.getInt("pid"));
                        classify.setLogo(classifyChildObject.getString("logo"));
                        childClassify[0] = classify;
                        if (j >= length2 - 1) {
                            //后面没有数据了
                            childClassifyList.add(childClassify);
                        }
                    } else {
                        if (j % 3 == 1) {
                            // 第二个数据
                            Classify classify = new Classify();
                            classify.setId(classifyChildObject.getInt("id"));
                            classify.setName(classifyChildObject.getString("cateName"));
                            classify.setpId(classifyChildObject.getInt("pid"));
                            classify.setLogo(classifyChildObject.getString("logo"));
                            childClassify[1] = classify;
                            if (j >= length2 - 1) {
                                //后面没有数据了
                                childClassifyList.add(childClassify);
                            }
                        } else if (j % 3 == 2) {
                            // 第三个数据
                            Classify classify = new Classify();
                            classify.setId(classifyChildObject.getInt("id"));
                            classify.setName(classifyChildObject.getString("cateName"));
                            classify.setpId(classifyChildObject.getInt("pid"));
                            classify.setLogo(classifyChildObject.getString("logo"));
                            childClassify[2] = classify;
                            childClassifyList.add(childClassify);
                        }
                    }
                }
                childList.add(childClassifyList);
            }
        } catch (Exception e) {
            DLog.e(TAG, "parseClassifyChildList()#exception:\n", e);
        }
    }


//    /**
//     * 解析纯的商品数据列表集合
//     * @param resultObject
//     * @return [0]=pageSize,[1]=按照的搜索类型方式 orderType（0=综合,1=团购价格升序,2=按团购价格降序,3=按销量降序,4=按评价降序）
//     *          [2]=搜索到的商品数量总数，[3]=返回的真正的数据长度
//     */
//    public static int[] parsePureGoodsList(JSONObject resultObject,List<PureGoods[]> goodsList){
//        try {
//            int[] results = new int[4];
//            JSONObject dataObject = resultObject.getJSONObject("goodsList");
//            results[0] = dataObject.getInt("pageNum");
//            results[1] = resultObject.getInt("order");
//            results[2] = dataObject.getInt("total");
//            int listSize = 0;
//
//            JSONArray goodsArray = dataObject.getJSONArray("list");
//            int length = goodsArray.length();
//            PureGoods[] pureGoodses = null;
//            for (int i = 0; i < length; i++) {
//                JSONObject goodsObject = goodsArray.getJSONObject(i);
//                JSONObject skuObject = goodsObject.getJSONObject("goodsSku");
//
//                PureGoods goods = new PureGoods();
//                goods.setName(goodsObject.getString("goods_name"));
//                goods.setId(goodsObject.getInt("id"));
//                goods.setIcon(goodsObject.getString("pic_server_url1"));
//                goods.setEvaluateNums(goodsObject.getString("evaluate_num"));
//                goods.setMarketPrice(skuObject.getDouble("shopMarketPrice"));
//                goods.setPrice(skuObject.getDouble("groupBuyPrice"));
//                goods.setMarketPrice(skuObject.getDouble("shopMarketPrice"));
//                goods.setSalesNums(skuObject.getInt("salesNum"));
//
//                listSize ++;
//                if(i % 2 == 0){
//                    // 新的一组item数据
//                    pureGoodses = new PureGoods[2];
//                    pureGoodses[0] = goods;
//                    if(i >= length -1){
//                        goodsList.add(pureGoodses);
//                        results[3] = listSize;
//                    }
//                }else {
//                    pureGoodses[1] = goods;
//                    goodsList.add(pureGoodses);
//                    results[3] = listSize;
//                }
//            }
//            return results;
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    /**
     * 解析纯的商品数据列表集合
     *
     * @param resultObject
     * @return [0]=pageSize,[1]=按照的搜索类型方式 orderType（0=综合,1=团购价格升序,2=按团购价格降序,3=按销量降序,4=按评价降序）
     * [2]=搜索到的商品数量总数
     */
    public static int[] parsePureGoodsList(JSONObject resultObject, List<PureGoods> goodsList) {
        try {
            int[] results = new int[3];
            JSONObject dataObject = resultObject.getJSONObject("goodsList");
            results[0] = dataObject.getInt("pageNum");
            results[1] = resultObject.getInt("order");
            results[2] = dataObject.getInt("total");

            JSONArray goodsArray = dataObject.getJSONArray("list");
            int length = goodsArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject goodsObject = goodsArray.getJSONObject(i);
                JSONObject skuObject = goodsObject.getJSONObject("goodsSku");
                DLog.i("goodsObject.toString()------>>>" + goodsObject.toString());
                DLog.i("skuObject.toString()------>>>" + skuObject.toString());

                PureGoods goods = new PureGoods();
                goods.setName(goodsObject.getString("goods_name"));
                goods.setId(goodsObject.getInt("id"));
                goods.setIcon(goodsObject.getString("pic_server_url1"));
                goods.setEvaluateNums(getJsonIntValue("evaluate_num", 0, goodsObject));
                goods.setMarketPrice(skuObject.getDouble("shopMarketPrice"));
                goods.setPrice(getJsonDoubleValue("groupBuyPrice", 0.0d, skuObject));
                goods.setSalesNums(getJsonIntValue("salesNum", 0, skuObject));
                goods.setSalesLevel(getJsonIntValue("sale_level", 0, goodsObject));
                goods.setMemberPrice(getJsonDoubleValue("memberPrice", 0.0, skuObject));
                goods.setNewbiePrice(getJsonDoubleValue("newbiePrice", 0.0, skuObject));
                goodsList.add(goods);
            }
            return results;
        } catch (Exception e) {
            DLog.e(TAG, "parsePureGoodsList()#exception:\n", e);
        }
        return null;
    }


    /**
     * 解析新人专享商品列表数据
     * @param resultObject
     */
    public static String parseNewExclusGoodsList(JSONObject resultObject,List<PureGoods> goodsList){
        JSONObject dataObject = null;
        String bannerImg = "";
        try {
            bannerImg = resultObject.getString("banner");
            dataObject = resultObject.getJSONObject("goodsList");
            JSONArray goodsArray = dataObject.getJSONArray("list");
            int length = goodsArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject goodsObject = goodsArray.getJSONObject(i);
                JSONObject skuObject = goodsObject.getJSONObject("goodsSku");
                PureGoods goods = new PureGoods();
                goods.setName(goodsObject.getString("goods_name"));
                goods.setId(goodsObject.getInt("id"));
                goods.setIcon(goodsObject.getString("pic_server_url1"));
                goods.setEvaluateNums(getJsonIntValue("evaluate_num", 0, goodsObject));
                goods.setMarketPrice(skuObject.getDouble("shopMarketPrice"));
                goods.setPrice(getJsonDoubleValue("groupBuyPrice", 0.0d, skuObject));
                goods.setSalesNums(getJsonIntValue("salesNum", 0, skuObject));
                goods.setSalesLevel(getJsonIntValue("sale_level", 0, goodsObject));
                goods.setMemberPrice(getJsonDoubleValue("memberPrice", 0.0, skuObject));
                goods.setNewbiePrice(getJsonDoubleValue("newbiePrice", 0.0, skuObject));
                goodsList.add(goods);
            }
            return bannerImg;
        } catch (Exception e) {
            DLog.e(TAG, "parseNewExclusGoodsList()#exception:\n", e);
            goodsList = null;
        }
        return "";
    }


    /**
     * 解析纯的商品数据列表集合
     *
     * @param resultObject
     * @return [0]=pageSize,[1]=按照的搜索类型方式 orderType（0=综合,1=团购价格升序,2=按团购价格降序,3=按销量降序,4=按评价降序）
     * [2]=总的条数，[3]=楼层名称
     */
    public static Object[] parseMoreGoodsList(JSONObject resultObject, List<PureGoods> goodsList) {
        try {
            Object[] results = new Object[4];
            JSONObject dataObject = resultObject.getJSONObject("page");
            results[0] = dataObject.getInt("pageNum");
            results[1] = resultObject.getInt("order");
            results[2] = dataObject.getInt("total");

            JSONArray goodsArray = dataObject.getJSONArray("list");
            int length = goodsArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject goodsObject = goodsArray.getJSONObject(i);
//                JSONObject skuObject = goodsObject.getJSONObject("goodsSku");
                PureGoods goods = new PureGoods();
                goods.setName(goodsObject.getString("goodsName"));
                goods.setId(goodsObject.getInt("id"));
                goods.setIcon(goodsObject.getString("goodsUrl"));
                goods.setMarketPrice(goodsObject.getDouble("marketPrice"));
                goods.setPrice(getJsonDoubleValue("price", 0.0d, goodsObject));
                goods.setEvaluateNums(getJsonIntValue("evaluateNums", 0, goodsObject));
                goods.setSalesNums(getJsonIntValue("salesNum", 0, goodsObject));
                goods.setSalesLevel(getJsonIntValue("saleLevel", 0, goodsObject));
                goods.setNewbiePrice(getJsonDoubleValue("newbiePrice", 0.0d, goodsObject));
                goods.setMemberPrice(getJsonDoubleValue("memberPrice", 0.0d, goodsObject));
                goodsList.add(goods);
            }

            if(resultObject.has("storey")){
                // 解析楼层名称
                JSONObject storeyObject = resultObject.getJSONObject("storey");
                results[3] = storeyObject.getString("storeyName");
            }

            return results;
        } catch (Exception e) {
            DLog.e(TAG, "parseMoreGoodsList()#exception:\n", e);
        }
        return null;
    }

    /**
     * 解析云店铺列表数据
     *
     * @param resultObject
     * @return 是否解析数据成功
     */
    public static boolean parseStoreList(JSONObject resultObject, List<Store> storeList,
                                         List<SpinnerMode> classifyValueList) {
        try {
            JSONObject dataObject = resultObject.getJSONObject("cloudPage");
            JSONArray storeArray = dataObject.getJSONArray("list");
            int length = storeArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject itemObject = storeArray.getJSONObject(i);
                Store store = new Store();
                store.setName(itemObject.getString("storeName"));
                store.setClassifyName(itemObject.getString("industryName"));
                store.setDistance(itemObject.getInt("distance"));
                store.setDistanceKm(Util.formatDistance(store.getDistance()));
                store.setId(itemObject.getInt("storeId"));
                store.setLogo(itemObject.getString("listPic"));
                store.setSendType(itemObject.getInt("sendTypeShow"));
                store.setTag(itemObject.getString("tagName"));
                store.setUserId(itemObject.getInt("userId"));
                storeList.add(store);
            }

            if (classifyValueList != null) {
                // 类别为空就不解析了
                JSONArray classifyValueArray = resultObject.getJSONArray("siList");
                int length2 = classifyValueArray.length();
                for (int i = 0; i < length2; i++) {
                    JSONObject classifyValueObject = classifyValueArray.getJSONObject(i);
                    SpinnerMode spinnerMode = new SpinnerMode();
                    spinnerMode.setName(classifyValueObject.getString("name"));
                    spinnerMode.setId(classifyValueObject.getInt("id"));
                    spinnerMode.setDiscription(classifyValueObject.getString("description"));
                    classifyValueList.add(spinnerMode);
                }
            }
            return true;
        } catch (Exception e) {
            DLog.e(TAG, "parseStoreList()#exception:\n", e);
        }
        return false;
    }


    /**
     * 解析返回的定位地址信息
     */
    public static String parseLocationAdress(String result) {
        try {
            JSONObject dataObject = new JSONObject(result);
            String status = dataObject.getString("status");
            if ("OK".equals(status)) {
                // 状态是成功
                JSONArray resultArray = dataObject.getJSONArray("results");
                if (resultArray.length() != 0) {
                    // 只拿第一个JsonObject
                    JSONObject adressObject = resultArray.getJSONObject(0);
                    String adress = adressObject.getString("formatted_address");
                    int position = adress.indexOf(" ");
                    if (position != -1) {
                        adress = adress.substring(0, position);
                    }
                    return adress;
                }
            } else {
                // 状态是失败
                return "";
            }
        } catch (Exception e) {
            DLog.e(TAG, "parseLocationAdress()#exception:\n", e);
        }
        return "";
    }

    /**
     * 解析云店铺详情信息
     *
     * @param resultObject
     * @return
     */
    public static StoreDetail parseStoreDetail(JSONObject resultObject) {
        try {
            StoreDetail storeDetail = new StoreDetail();
            storeDetail.setSendAroundLab(resultObject.getInt("label2") == 1);
            storeDetail.setStoreServicelab(resultObject.getInt("label3") == 1);

            JSONObject storeObject = resultObject.getJSONObject("store");
            storeDetail.setId(storeObject.getInt("id"));
            storeDetail.setName(storeObject.getString("storeName"));
            storeDetail.setLogo(storeObject.getString("storeLogo"));
            storeDetail.setAdress(storeObject.getString("storeAddress"));
            storeDetail.setDiscrible(storeObject.getString("storeInfo"));
            storeDetail.setLat(getJsonDoubleValue("storeLat", 0.00d, storeObject));
            storeDetail.setLng(getJsonDoubleValue("storeLng", 0.00d, storeObject));
//            storeDetail.setLat(storeObject.getDouble("storeLat"));
//            storeDetail.setLng(storeObject.getDouble("storeLng"));
            storeDetail.setOwer(storeObject.getString("storeOwer"));
            storeDetail.setUserId(storeObject.getInt("userId"));
            storeDetail.setPhone(storeObject.getString("storeTelephone"));
            return storeDetail;
        } catch (Exception e) {
            DLog.e(TAG, "parseStoreDetail()#exception:\n", e);
        }
        return null;
    }

    /**
     * 解析纯商品列表数据集合
     *
     * @param resultObject
     * @return
     */
    public static List<PureGoods> parsePureGoodsList(JSONObject resultObject) {
        try {
            List<PureGoods> list = new ArrayList<>();
            JSONObject dataObject = resultObject.getJSONObject("goodsList");
            JSONArray goodsArray = dataObject.getJSONArray("list");
            int length = goodsArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject goodsObject = goodsArray.getJSONObject(i);
                JSONObject skuObject = goodsObject.getJSONObject("goodsSku");

                PureGoods goods = new PureGoods();
                goods.setName(goodsObject.getString("goods_name"));
                goods.setId(goodsObject.getInt("id"));
                goods.setIcon(goodsObject.getString("pic_server_url1"));

                goods.setMarketPrice(skuObject.getDouble("shopMarketPrice"));
                goods.setPrice(getJsonDoubleValue("groupBuyPrice", 0.0, skuObject));
                goods.setMarketPrice(skuObject.getDouble("shopMarketPrice"));
                goods.setEvaluateNums(getJsonIntValue("evaluate_num", 0, goodsObject));
                goods.setSalesNums(getJsonIntValue("salesNum", 0, skuObject));
                list.add(goods);
            }
            return list;
        } catch (Exception e) {
            DLog.e(TAG, "parseStoreDetail()#exception:\n", e);
        }
        return null;
    }


    /**
     * 解析登陆成功之后得到的用户信息结果
     *
     * @param resultObject
     */
    public static void parseLoginUserInfoResult(JSONObject resultObject) {
        try {
            JSONObject userObject = resultObject.getJSONObject("user");
            UserInfo userInfo = UserInfoManager.getInstance().getUserInfo();
            userInfo.setId(userObject.getInt("userId"));
            userInfo.setType(userObject.getInt("userType"));
            userInfo.setStoreId(getJsonIntValue("storeId", -1, userObject));
            userInfo.setSex(getJsonIntValue("sex", 0, userObject));
            userInfo.setIcon(userObject.getString("headimgurl"));
            userInfo.setCloudUser(userObject.getBoolean("isCloudUser"));
            userInfo.setNickName(getJsonStringValue("nickname", userObject));
            userInfo.setCompanyName(getJsonStringValue("companyName", userObject));
            userInfo.setName(getJsonStringValue("contactName", userObject));
            userInfo.setVipLevel(userObject.getInt("membershipType"));

            // 发送登陆成功的消息
            EventBus.getDefault().post(Constants.LOGIN_SUCCESS_MSG);
//            // 保存用户信息到缓存
//            Util.saveCachDataToFile(context,Constants.USER_INFO_CANCHE_NAME,userInfo);
        } catch (Exception e) {
            DLog.e(TAG, "parseLoginUserInfoResult()#exception:\n", e);
        }
    }

    /**
     * 解析查询用户信息成功之后得到的用户信息结果
     *
     * @param resultObject
     */
    public static void parseLoginUserInfo(JSONObject resultObject) {
        try {
            JSONObject userObject = resultObject.getJSONObject("user");
            UserInfo userInfo = UserInfoManager.getInstance().getUserInfo();
            userInfo.setId(userObject.getInt("userId"));
            userInfo.setType(userObject.getInt("userType"));
            userInfo.setStoreId(getJsonIntValue("storeId", -1, userObject));
            userInfo.setSex(getJsonIntValue("sex", 0, userObject));
            userInfo.setIcon(userObject.getString("headimgurl"));
            userInfo.setCloudUser(userObject.getBoolean("isCloudUser"));
            userInfo.setNickName(getJsonStringValue("nickname", userObject));
            userInfo.setCompanyName(getJsonStringValue("companyName", userObject));
            userInfo.setName(getJsonStringValue("contactName", userObject));
            userInfo.setVipLevel(userObject.getInt("membershipType"));
            DLog.i("会员等级------->>>" + userInfo.getVipLevel());
            // 发送重新刷新用户视图的消息
            EventBus.getDefault().post(Constants.REFRESH_USER_INFO_VIEW);
//            // 发送登陆成功的消息
//            EventBus.getDefault().post(Constants.LOGIN_SUCCESS_MSG);
//            // 保存用户信息到缓存
//            Util.saveCachDataToFile(context,Constants.USER_INFO_CANCHE_NAME,userInfo);
        } catch (Exception e) {
            DLog.e(TAG, "parseLoginUserInfo()#exception:\n", e);
        }
    }

    /**
     * 解析用户中心返回数据
     *
     * @param resultObject
     * @param menuList
     */
    public static void parseUserCenterMenuResult(JSONObject resultObject, List<UserCenterMenu>
            menuList) {
        try {
            // 解析menu部分
            JSONArray menuArray = resultObject.getJSONArray("menuList");
            int length = menuArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject menuObject = menuArray.getJSONObject(i);
                UserCenterMenu menu = new UserCenterMenu();
                menu.setIcon(menuObject.getString("menuImageURL"));
                menu.setName(menuObject.getString("menuName"));
                menu.setTag(menuObject.getString("menuLabel"));
                menuList.add(menu);
            }

//            // 保存用户信息到缓存
//            Util.saveCachDataToFile(context,Constants.USER_INFO_CANCHE_NAME,userInfo);
        } catch (Exception e) {
            DLog.e(TAG, "parseUserCenterMenuResult()#exception:\n", e);
        }
    }

    /**
     * 解析用户信息
     *
     * @param resultObject
     */
    public static void parseUserInfo(JSONObject resultObject) {
        // 解析用户信息部分
        try {
            JSONObject userObject = resultObject.getJSONObject("storeHome");
            UserInfo userInfo = UserInfoManager.getInstance().getUserInfo();
            userInfo.setId(userObject.getInt("p_user_id"));
            userInfo.setType(userObject.getInt("p_user_type"));
            userInfo.setTodayIncome(userObject.getDouble("p_today_income"));
            userInfo.setTotalIncome(userObject.getDouble("p_accumulate_income"));
            userInfo.setTodayApprenticeNum(userObject.getInt("p_today_apprentice"));
            userInfo.setNoPayOrderNum(userObject.getInt("p_nopayorder_nums"));
            userInfo.setPaiedOrderNum(userObject.getInt("p_paidorder_nums"));
            userInfo.setUnTakeOverNum(userObject.getInt("p_inboundorder_nums"));
            userInfo.setUnCommentNum(userObject.getInt("p_evaluateorder_nums"));
            userInfo.setGoodsNum(userObject.getInt("p_goods_nums"));
            userInfo.setTotalFunds(userObject.getDouble("p_funds_total"));
            DLog.i("总额/余额----->>>" + userInfo.getTotalFunds());
            userInfo.setDirectOrderNum(userObject.getInt("p_direct_orders"));
            userInfo.setDistributorNum(userObject.getInt("p_distributor_nums"));
            userInfo.setDistributorOrederNum(userObject.getInt("p_distributor_orders"));
            userInfo.setDealerNum(userObject.getInt("p_dealer_nums"));
            userInfo.setDealerOrderNum(getJsonIntValue("p_dealer_orders", 0, userObject));
            userInfo.setCustomerNum(userObject.getInt("p_customer_nums"));
            userInfo.setCommenterNum(userObject.getInt("p_customer_nums"));
        } catch (Exception e) {
            DLog.e(TAG, "parseUserInfo()#exception:\n", e);
        }
    }

    /**
     * 解析购物车返回结果
     *
     * @param resultObject
     * @param groupList
     * @param childList
     * @return 合计商品总价格
     */
    public static double parseShoppingCartResult(JSONObject resultObject, List<Shop> groupList,
                                                 ArrayList<ArrayList<ShoppingCart>> childList) {
        double totalPrice = 0.0d;
        try {
//            double postPrice = 0.00d;
//            double goodsCount = 0.0d;
            LinkedHashMap<Integer, ArrayList<ShoppingCart>> cartListMap = new LinkedHashMap<>();

            JSONArray cartArray = resultObject.getJSONArray("carts");
            totalPrice = getJsonDoubleValue("total", 0.0d, resultObject);
            int length = cartArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject cartObject = cartArray.getJSONObject(i);
                ShoppingCart cart = parseShoppingCartInfo(cartObject);

                int storeId = cartObject.getInt("store_id");
                ArrayList<ShoppingCart> cartList;
                if (!cartListMap.containsKey(storeId)) {
                    Shop shop = new Shop();
                    shop.setId(cartObject.getInt("store_id"));
                    shop.setUserId(cartObject.getInt("store_user_id"));
                    shop.setName(cartObject.getString("store_name"));
                    shop.setStoreType(cartObject.getInt("user_type"));
                    groupList.add(shop);
                    cartList = new ArrayList<>();
                    cartListMap.put(storeId, cartList);
                } else {
                    cartList = cartListMap.get(storeId);
                }
//                if (cart.getBuyNum() > 1) {
////                    totalPrice += cart.getBuyNum() * cart.getBuyPrice();
//                    totalPrice = Arith.add(totalPrice, Arith.mul(cart.getBuyNum(), cart
//                            .getBuyPrice()));
////                    if (!cart.isFreeShipping()) {
////                        // 如果不包邮
////                        postPrice = Arith.add(postPrice, Arith.mul(cart.getBuyNum(), cart
////                                .getSinglePostage()));
////                    }
//                } else {
////                    totalPrice += cart.getBuyPrice();
//                    totalPrice = Arith.add(totalPrice, cart.getBuyPrice());
////                    if (!cart.isFreeShipping()) {
////                        // 如果不包邮
////                        postPrice = Arith.add(postPrice, cart.getSinglePostage());
////                    }
//                }
//                goodsCount = Arith.add(goodsCount, cart.getBuyNum());
                cartList.add(cart);
            }

//            for (int i = 0; i < groupList.size(); i++) {
//                DLog.i("lilijun","每个组别item的商店名称-------->>>"+groupList.get(i).getName());
//            }
            // 再去循环一次map 得到childList
            for (Map.Entry<Integer, ArrayList<ShoppingCart>> entry : cartListMap.entrySet()) {
                List<ShoppingCart> list = entry.getValue();
//                DLog.i("lilijun","每个子item的第一条数据商品名称-------->>>"+list.get(0).getGoodsName());
                childList.add(entry.getValue());
            }
//            return new double[]{totalPrice, postPrice, goodsCount};
        } catch (Exception e) {
            DLog.e(TAG, "parseShoppingCartResult()#exception:\n", e);
        }
        return totalPrice;
    }

    /**
     * 解析单个ShoppingCart
     *
     * @return
     */
    private static ShoppingCart parseShoppingCartInfo(JSONObject cartObject) throws JSONException {
        ShoppingCart cart = new ShoppingCart();
        cart.setStoreId(cartObject.getInt("store_id"));
        cart.setStoreUserId(cartObject.getInt("store_user_id"));
        cart.setStoreName(cartObject.getString("store_name"));
        cart.setStoreType(cartObject.getInt("user_type"));
        cart.setStoreUserName(cartObject.getString("store_username"));

        cart.setGoodsId(cartObject.getInt("goods_id"));
        cart.setGoodsStatus(getJsonIntValue("status", 0, cartObject));
        cart.setGoodsName(cartObject.getString("goods_name"));
        cart.setGoodsIcon(cartObject.getString("pic_server_url1"));
        cart.setGoodsType(getJsonIntValue("goodsType", -1, cartObject));
        cart.setFhUserId(cartObject.getInt("fh_user_id"));
        cart.setPromoCode(getJsonIntValue("promo_code", -1, cartObject));
        cart.setFinalPrice(getJsonDoubleValue("final_price", 0.00d, cartObject));
        // 进货价
        cart.setStockPrice(getJsonDoubleValue("goods_stock_price", 0.00d, cartObject));
        // 购买价
        cart.setBuyPrice(cartObject.getDouble("group_buy_price"));
        // 市场价
        cart.setMarketPrice(cartObject.getDouble("shop_market_price"));
        // 出厂价
        cart.setFactoryPrice(cartObject.getDouble("ex_factory_price"));
        cart.setSalesNum(cartObject.getInt("sales_num"));
        cart.setBuyNum(cartObject.getInt("buyCount"));
        // 库存
        cart.setQuantityNum(cartObject.getInt("quantity"));
        // 总价
        cart.setTotalPrice(cartObject.getInt("total_price"));

        cart.setSkuId(cartObject.getInt("sku_id"));
        cart.setSku1Name(cartObject.getString("sku1_name"));
        cart.setSku1Value(cartObject.getString("sku1_value"));
        cart.setSku2Name(cartObject.getString("sku2_name"));
        cart.setSku2Value(cartObject.getString("sku2_value"));

        // 是否包邮
        cart.setFreeShipping(cartObject.getBoolean("isfree_shipping"));
        // 邮费总价
        cart.setPostage(cartObject.getDouble("postage"));
        // 单个商品的邮费
        cart.setSinglePostage(cartObject.getDouble("singlePostage"));

        DLog.i("解析出一条商品信息---->>>" + cart.getGoodsName());
        return cart;
    }

    /**
     * 解析地区信息
     *
     * @param parseKey
     * @param resultObject
     * @return
     */
    public static List<String> parseAddressInfos(String parseKey, JSONObject resultObject) {
        try {
            List<String> list = new ArrayList<>();
            // 是否需要前端把省份、市的信息写死
            boolean isLock = false;
            JSONArray locationArray = resultObject.getJSONArray("locationlist");
            int length = locationArray.length();
            if (resultObject.has("lock")) {
                isLock = resultObject.getBoolean("lock");
            }
            if (isLock) {
                // 需要前端写死
                if (length > 0) {
                    // 只解析第一条
                    list.add(locationArray.getJSONObject(0).getString(parseKey));
                }
            } else {
                // 不需要前端写死
                for (int i = 0; i < length; i++) {
                    list.add(locationArray.getJSONObject(i).getString(parseKey));
                }
            }
            return list;
        } catch (Exception e) {
            DLog.e(TAG, "parseAddressInfos()#exception:\n", e);
        }
        return null;
    }

    /**
     * 解析最终地区信息及对应的后台id
     *
     * @param resultObject
     * @param locationIdList spinner最终选择好的id信息集合数据
     * @return
     */
    public static List<String> parseAddressInfosAndIds(JSONObject resultObject, List<Integer>
            locationIdList) {
        try {
            List<String> list = new ArrayList<>();
            JSONArray locationArray = resultObject.getJSONArray("locationlist");
            int length = locationArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject locationObject = locationArray.getJSONObject(i);
                list.add(locationObject.getString("district"));
                locationIdList.add(locationObject.getInt("id"));
            }
            return list;
        } catch (Exception e) {
            DLog.e(TAG, "parseAddressInfos()#exception:\n", e);
        }
        return null;
    }


    /**
     * 解析地址列表数据
     *
     * @param resultObject
     * @return
     */
    public static List<Address> parseAddressListResult(JSONObject resultObject) {
        try {
            List<Address> list = new ArrayList<>();
            JSONArray addressArray = resultObject.getJSONArray("addressList");
            int length = addressArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject addressObject = addressArray.getJSONObject(i);
                Address address = new Address();
                parseAddressInfo(addressObject, address);
                list.add(address);
            }
            return list;
        } catch (Exception e) {
            DLog.e(TAG, "parseAddressListResult()#exception:\n", e);
        }
        return null;
    }

    /**
     * 解析单个地址信息
     *
     * @param addressObject
     * @return
     * @throws JSONException
     */
    private static void parseAddressInfo(JSONObject addressObject, Address address) throws
            JSONException {
        address.setId(addressObject.getInt("id"));
        address.setName(addressObject.getString("name"));
        address.setDetailAddress(addressObject.getString("address"));
        address.setMobile(addressObject.getString("mobile"));
        address.setPostcode(addressObject.getString("senderzip"));
        address.setReceiveDefault(addressObject.getBoolean("isReceiveDefault"));
        address.setSendDefault(addressObject.getBoolean("isSendDefault"));
        address.setUserId(addressObject.getInt("userId"));
        address.setCreateTime(addressObject.getString("createTime"));

        JSONObject locationObject = addressObject.getJSONObject("locationInfo");
        address.setLocationId(locationObject.getInt("id"));
        address.setCountry(locationObject.getString("country"));
        address.setProvince(locationObject.getString("province"));
        address.setCity(locationObject.getString("city"));
        address.setTown(locationObject.getString("district"));

        if (address.isReceiveDefault()) {
            // 设置用户的默认收货地址信息
            UserInfoManager.getInstance().getUserInfo().setDefaultReceiveAddress(address);
        }
    }

    /**
     * 解析单个订单信息
     *
     * @param resultObject
     * @return
     */
    public static OrderInfo parseOrderInfo(JSONObject resultObject) {
        try {
            OrderInfo orderInfo = new OrderInfo();
            JSONObject orderObject = resultObject.getJSONObject("orderInfo");
            orderInfo.setId(getJsonIntValue("id", -1, orderObject));
            orderInfo.setSerialNumber(orderObject.getString("serialNumber"));
            orderInfo.setOrderno(orderObject.getString("orderno"));
            orderInfo.setCreateTime(orderObject.getString("createTime"));
            orderInfo.setOrderType(orderObject.getInt("orderType"));
            orderInfo.setTotalPrice(orderObject.getDouble("totalPrice"));
            orderInfo.setRealPayment(orderObject.getDouble("realPayment"));
            orderInfo.setReductionPrice(orderObject.getDouble("reductionPrice"));
            orderInfo.setIsfreeShipping(orderObject.getBoolean("isfreeShipping"));
            orderInfo.setPostage(orderObject.getDouble("postage"));
            orderInfo.setBuyersIp(orderObject.getString("buyersIp"));
            orderInfo.setReceiveName(orderObject.getString("receiveName"));
            orderInfo.setReceiveProvince(orderObject.getString("receiveProvince"));
            orderInfo.setReceiveCity(orderObject.getString("receiveCity"));
            orderInfo.setReceiveArea(orderObject.getString("receiveArea"));
            orderInfo.setReceiveAddress(orderObject.getString("receiveAddress"));
            orderInfo.setReceiveMobile(orderObject.getString("receiveMobile"));
            orderInfo.setPayStyle(orderObject.getInt("payStyle"));
            orderInfo.setTakeType(orderObject.getInt("takeType"));
            return orderInfo;
        } catch (Exception e) {
            DLog.e(TAG, "parseOrderInfo()#exception:\n", e);
        }
        return null;
    }

    /**
     * 解析订单列表
     *
     * @param resultObject
     * @return [0]=当前页数,[1]=获取的数据条数(指的是订单个数，而非商品个数)
     */
    public static int[] parseOrderList(JSONObject resultObject, List<OrderListInfo> orderList) {
        try {
            int[] result = new int[2];
            int dataSize = 0;
            int begType = resultObject.getInt("begType");
            JSONObject orderListObject = resultObject.getJSONObject("orderlist");
            JSONArray orderArray = orderListObject.getJSONArray("list");
            int length = orderArray.length();
            for (int i = 0; i < length; i++) {
                dataSize++;
                JSONObject orderObject = orderArray.getJSONObject(i);
                JSONArray goodsArray = orderObject.getJSONArray("frontOrderGoodsView");
                int goodsLength = goodsArray.length();
                for (int j = 0; j < goodsLength; j++) {
                    OrderListInfo orderInfo = new OrderListInfo();
                    orderInfo.setBegType(begType);
                    if (j == 0) {
                        // 是第一条数据
                        orderInfo.setFirstData(true);
                    } else {
                        orderInfo.setFirstData(false);
                    }
                    if (j == goodsLength - 1) {
                        // 是最后一条数据
                        orderInfo.setLastData(true);
                    } else {
                        orderInfo.setLastData(false);
                    }
                    orderInfo.setId(orderObject.getInt("id"));
                    orderInfo.setOrderNo(orderObject.getString("orderno"));
//                    orderInfo.setSerialNumber(orderObject.getString("serialNumber"));
                    orderInfo.setStatus(orderObject.getInt("orderStatus"));
                    orderInfo.setPostage(orderObject.getDouble("postage"));
                    orderInfo.setGoodsTotalPrice(orderObject.getDouble("totalPrice"));
                    orderInfo.setRealTotalPrice(orderObject.getDouble("realPayment"));
                    orderInfo.setStoreName(orderObject.getString("salesOutlets"));
                    orderInfo.setStoreId(orderObject.getInt("xsStoreId"));
                    orderInfo.setStoreUserId(orderObject.getInt("store_userId"));
                    orderInfo.setStoreUserType(orderObject.getInt("store_user_type"));
                    orderInfo.setCreateTime(orderObject.getString("createTime"));
                    // 是否评论过此订单
                    orderInfo.setCommented(orderObject.getBoolean("israte"));
                    orderInfo.setPriceModifyStatus(orderObject.getInt("priceModifyStatus"));
                    orderInfo.setPayType(orderObject.getInt("payStyle"));
                    orderInfo.setTakeType(orderObject.getInt("takeType"));
                    // 物流类型
                    orderInfo.setPostType(orderObject.getInt("logisticsType"));


                    // 商品部分
                    JSONObject goodsObject = goodsArray.getJSONObject(j);
//                    orderInfo.setGoodsId(goodsObject.getInt("id"));
                    orderInfo.setGoodsId(goodsObject.getInt("goodsId"));
                    orderInfo.setGoodsIcon(goodsObject.getString("goodsPicurl"));
                    orderInfo.setGoodsName(goodsObject.getString("goodsName"));
                    orderInfo.setGoodsBuyNum(goodsObject.getInt("quantity"));
                    orderInfo.setGoodsPrice(goodsObject.getDouble("goodsPrice"));

                    orderList.add(orderInfo);
                }
            }
            result[0] = orderListObject.getInt("pageNum");
            result[1] = dataSize;
//            result[1] = orderListObject.getInt("total");
            return result;
        } catch (Exception e) {
            DLog.e(TAG, "parseOrderList()#exception:\n", e);
        }
        return null;
    }


    /**
     * 解析订单详情数据
     *
     * @param resultObject
     * @return
     */
    public static OrderDetailInfo parseOrderDetailInfo(JSONObject resultObject) {
        try {
            OrderDetailInfo orderDetailInfo = new OrderDetailInfo();
            JSONObject orderObject = resultObject.getJSONObject("orderInfo");
            orderDetailInfo.setBegType(resultObject.getInt("begType"));
            orderDetailInfo.setId(orderObject.getInt("id"));
            orderDetailInfo.setOrderNo(orderObject.getString("orderno"));
            orderDetailInfo.setSerialNumber(orderObject.getString("serialNumber"));
            orderDetailInfo.setStatus(orderObject.getInt("orderStatus"));
            orderDetailInfo.setPostage(orderObject.getDouble("postage"));
            orderDetailInfo.setGoodsTotalPrice(orderObject.getDouble("totalPrice"));
            orderDetailInfo.setRealTotalPrice(orderObject.getDouble("realPayment"));
            orderDetailInfo.setStoreName(orderObject.getString("salesOutlets"));
            orderDetailInfo.setStoreId(orderObject.getInt("xsStoreId"));
            orderDetailInfo.setStoreUserId(orderObject.getInt("store_userId"));
            orderDetailInfo.setStoreUserType(orderObject.getInt("store_user_type"));
            orderDetailInfo.setCreateTime(orderObject.getString("createTime"));
            orderDetailInfo.setCloseTime(orderObject.getString("colseTime"));
            orderDetailInfo.setConsignTime(getJsonStringValue("consignTime", orderObject));
            orderDetailInfo.setSignTime(getJsonStringValue("signTime", orderObject));
            // 是否评论过此订单
            orderDetailInfo.setCommented(orderObject.getBoolean("israte"));
            orderDetailInfo.setPriceModifyStatus(orderObject.getInt("priceModifyStatus"));
            orderDetailInfo.setPayType(orderObject.getInt("payStyle"));
            orderDetailInfo.setTakeType(orderObject.getInt("takeType"));
            // 物流类型
            orderDetailInfo.setPostType(orderObject.getInt("logisticsType"));
            orderDetailInfo.setBuyerMessage(orderObject.getString("buyerMessage"));

            /** 地址信息部分**/
            orderDetailInfo.getAddress().setName(orderObject.getString("receiveName"));
            orderDetailInfo.getAddress().setProvince(orderObject.getString("receiveProvince"));
            orderDetailInfo.getAddress().setCity(orderObject.getString("receiveCity"));
            orderDetailInfo.getAddress().setTown(orderObject.getString("receiveArea"));
            orderDetailInfo.getAddress().setDetailAddress(orderObject.getString("receiveAddress"));
            orderDetailInfo.getAddress().setMobile(orderObject.getString("receiveMobile"));

            JSONArray goodsArray = orderObject.getJSONArray("goodsDetails");
            int length = goodsArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject goodsObject = goodsArray.getJSONObject(i);
                OrderGoodsInfo goods = new OrderGoodsInfo();
                goods.setId(goodsObject.getInt("goodsId"));
                goods.setIcon(goodsObject.getString("goodsPicurl"));
                goods.setName(goodsObject.getString("goodsName"));
                goods.setBuyNum(goodsObject.getInt("quantity"));
                goods.setPrice(goodsObject.getDouble("goodsPrice"));
                goods.setTotalPrice(goodsObject.getDouble("totalPrice"));
                goods.setSkuValues(getJsonStringValue("skupropertys", goodsObject));
                orderDetailInfo.getGoodsList().add(goods);
            }
            return orderDetailInfo;
        } catch (Exception e) {
            DLog.e(TAG, "parseOrderDetailInfo()#exception:\n", e);
        }
        return null;
    }


    /**
     * 解析我的商品管理列表数据
     *
     * @param resultObject
     * @param goodsList
     * @param totalGoodsList
     * @return 所有的商品有多少条数据(不是一页返回了多少条数据)
     */
    public static int parseMyGoodsList(JSONObject resultObject, List<MyGoods> goodsList,
                                       List<MyGoods> totalGoodsList) {
        try {
            JSONObject jsonObject = resultObject.getJSONObject("goodsList");
            JSONArray goodsArray = jsonObject.getJSONArray("list");
            int length = goodsArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject goodsObject = goodsArray.getJSONObject(i);
                JSONObject skuObject = goodsObject.getJSONObject("goodsSku");

                MyGoods myGoods = new MyGoods();
                myGoods.setId(skuObject.getInt("id"));
                myGoods.setStockNums(skuObject.getInt("quantity"));
                myGoods.setMarketPrice(skuObject.getDouble("shopMarketPrice"));
                myGoods.setPrice(getJsonDoubleValue("groupBuyPrice", 0.0d, skuObject));
                myGoods.setSalesNums(skuObject.getInt("salesNum"));
                myGoods.setName(goodsObject.getString("goods_name"));
                myGoods.setIcon(goodsObject.getString("pic_server_url1"));
                myGoods.setCategoryId(goodsObject.getInt("category_id"));
                myGoods.setEvaluateNums(getJsonIntValue("evaluate_num", 0, goodsObject));
                myGoods.setSupplierId(goodsObject.getInt("supplier_id"));
                myGoods.setStoreId(goodsObject.getInt("store_id"));
                myGoods.setStoreName(goodsObject.getString("store_name"));
                goodsList.add(myGoods);
                totalGoodsList.add(myGoods);
            }
            return jsonObject.getInt("total");
        } catch (Exception e) {
            DLog.e(TAG, "parseMyGoodsList()#exception:\n", e);
        }
        return -1;
    }

    /**
     * 解析我的商品管理列表数据
     *
     * @param resultObject
     * @param goodsList
     * @return 所有的商品有多少条数据(不是一页返回了多少条数据)
     */
    public static int parseMyGoodsList(JSONObject resultObject, List<MyGoods> goodsList) {
        try {
            JSONObject jsonObject = resultObject.getJSONObject("goodsList");
            JSONArray goodsArray = jsonObject.getJSONArray("list");
            int length = goodsArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject goodsObject = goodsArray.getJSONObject(i);
                JSONObject skuObject = goodsObject.getJSONObject("goodsSku");

                MyGoods myGoods = new MyGoods();
                myGoods.setId(skuObject.getInt("id"));
                myGoods.setStockNums(skuObject.getInt("quantity"));
                myGoods.setMarketPrice(skuObject.getDouble("shopMarketPrice"));
                myGoods.setPrice(getJsonDoubleValue("groupBuyPrice", 0.0d, skuObject));
                myGoods.setSalesNums(skuObject.getInt("salesNum"));
                myGoods.setName(goodsObject.getString("goods_name"));
                myGoods.setIcon(goodsObject.getString("pic_server_url1"));
                myGoods.setCategoryId(goodsObject.getInt("category_id"));
                myGoods.setEvaluateNums(getJsonIntValue("evaluate_num", 0, goodsObject));
                myGoods.setSupplierId(goodsObject.getInt("supplier_id"));
                myGoods.setStoreId(goodsObject.getInt("store_id"));
                myGoods.setStoreName(goodsObject.getString("store_name"));
                goodsList.add(myGoods);
            }
            return jsonObject.getInt("total");
        } catch (Exception e) {
            DLog.e(TAG, "parseMyGoodsList()#exception:\n", e);
        }
        return -1;
    }

    /**
     * 解析货架列表结果
     *
     * @param resultObject
     * @return
     */
    public static List<GoodsShelf> parseGoodsShelfList(JSONObject resultObject) {
        try {
            List<GoodsShelf> list = new ArrayList<>();
            JSONArray shelfArray = resultObject.getJSONArray("storeFloors");
            int length = shelfArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject shelfObject = shelfArray.getJSONObject(i);
                GoodsShelf goodsShelf = new GoodsShelf();
                goodsShelf.setId(shelfObject.getInt("id"));
                goodsShelf.setName(shelfObject.getString("floorName"));
                goodsShelf.setCreateTime(shelfObject.getString("addTime"));
                goodsShelf.setDefault(getJsonIntValue("defaultFloor", 0, shelfObject) == 1);
                goodsShelf.setShow(getJsonIntValue("whetherShow", 0, shelfObject) == 1);
                if (shelfObject.isNull("goodsdetailview")) {
                    goodsShelf.setGoodsNum(0);
                } else {
                    JSONArray goodsArray = shelfObject.getJSONArray("goodsdetailview");
                    goodsShelf.setGoodsNum(goodsArray.length());
                }
                list.add(goodsShelf);
            }
            return list;
        } catch (Exception e) {
            DLog.e(TAG, "parseGoodsShelfList()#exception:\n", e);
        }
        return null;
    }

    /**
     * 解析根据商品id查询到的货架信息
     *
     * @param resultObject
     * @return
     */
    public static GoodsShelf parseGoodsShelf(JSONObject resultObject) {
        try {
            JSONObject shelfObject = resultObject.getJSONObject("storeFloor");
            GoodsShelf goodsShelf = new GoodsShelf();
            goodsShelf.setId(shelfObject.getInt("id"));
            goodsShelf.setName(shelfObject.getString("floorName"));
            goodsShelf.setCreateTime(shelfObject.getString("addTime"));
            goodsShelf.setDefault(getJsonIntValue("defaultFloor", 0, shelfObject) == 1);
            goodsShelf.setShow(getJsonIntValue("whetherShow", 0, shelfObject) == 1);
            if (shelfObject.isNull("goodsdetailview")) {
                goodsShelf.setGoodsNum(0);
            } else {
                JSONArray goodsArray = shelfObject.getJSONArray("goodsdetailview");
                goodsShelf.setGoodsNum(goodsArray.length());
            }
            return goodsShelf;
        } catch (Exception e) {
            DLog.e(TAG, "parseGoodsShelf()#exception:\n", e);
        }
        return null;
    }

    /**
     * 解析我的评论列表数据
     *
     * @param resultObject
     */
    public static List<MyComment> parseMyCommentList(JSONObject resultObject) {
        try {
            List<MyComment> list = new ArrayList<>();
            JSONObject rateObject = resultObject.getJSONObject("myrateList");
            JSONArray commentArray = rateObject.getJSONArray("list");
            int length = commentArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject commentObject = commentArray.getJSONObject(i);
                MyComment myComment = new MyComment();
                myComment.setId(getJsonIntValue("rateId", -1, commentObject));
                myComment.setGoodsName(commentObject.getString("goodsName"));
                myComment.setGoodsIcon(commentObject.getString("goodsPicUrl"));
                myComment.setGoodsId(commentObject.getInt("goodsId"));
                myComment.setGoodPrice(commentObject.getDouble("goodsPrice"));
                myComment.setCommentType(commentObject.getInt("rateType"));
                myComment.setContent(commentObject.getString("content"));
                myComment.setCommentDate(commentObject.getString("addTime"));
                myComment.setRateAspect(commentObject.getInt("rateAspect"));
                myComment.setStar(commentObject.getInt("evaluation"));
                myComment.setUserId(commentObject.getInt("userId"));
                myComment.setUserName(commentObject.getString("userName"));
                list.add(myComment);
            }
            return list;
        } catch (Exception e) {
            DLog.e(TAG, "parseMyCommentList()#exception:\n", e);
        }
        return null;
    }


    /**
     * 解析确认订单(订单结算) 返回的结果
     *
     * @param resultObject
     * @return [0]=邮费总额，[1]=商品总额,[2]=购买的商品总的件数
     */
    public static double[] parseConfirmOrderResult(JSONObject resultObject, List<Shop> groupList,
                                                   List<List<ShoppingCart>> childList, Address
                                                           address) {
        double[] results = new double[3];
        try {
            LinkedHashMap<Integer, List<ShoppingCart>> cartListMap = new LinkedHashMap<>();

            // 解析收货地址
            JSONObject addressObject = resultObject.getJSONObject("location");
            parseAddressInfo(addressObject, address);

            // 解析店铺信息
            JSONArray shopArray = resultObject.getJSONArray("stores");
            int shopLength = shopArray.length();
            for (int i = 0; i < shopLength; i++) {
                JSONObject shopObject = shopArray.getJSONObject(i);
                Shop shop = new Shop();
                shop.setId(shopObject.getInt("store_id"));
                shop.setUserId(shopObject.getInt("store_user_id"));
                shop.setName(shopObject.getString("store_name"));
                shop.setStoreType(shopObject.getInt("user_type"));
                groupList.add(shop);
                // 有多少商店信息就有多少个List列表
                cartListMap.put(shop.getId(), new ArrayList<ShoppingCart>());
            }
            // 解析购物车商品信息
            JSONArray cartArray = resultObject.getJSONArray("carts");
            int cartLength = cartArray.length();
            for (int i = 0; i < cartLength; i++) {
                JSONObject cartObject = cartArray.getJSONObject(i);
                ShoppingCart cart = parseShoppingCartInfo(cartObject);
                List<ShoppingCart> cartList = cartListMap.get(cart.getStoreId());
                if (cartList != null) {
                    cartList.add(cart);
                }
            }

            // 再去循环一次map 得到childList
            for (Map.Entry<Integer, List<ShoppingCart>> entry : cartListMap.entrySet()) {
                List<ShoppingCart> list = entry.getValue();
                childList.add(entry.getValue());
            }
            // 邮费总额
            results[0] = resultObject.getDouble("total_postage");
            // 商品总额
            results[1] = resultObject.getDouble("total_price");
            // 购买商品总件数
            results[2] = resultObject.getDouble("buyCount");
            return results;
        } catch (Exception e) {
            DLog.e(TAG, "parseMyCommentList()#exception:\n", e);
        }
        return null;
    }


    /**
     * 解析微信预支付的信息
     *
     * @param reslutObject
     * @return
     */
    public static WXPrepareInfo parseWxPrepareInfo(JSONObject reslutObject) {
        try {
            WXPrepareInfo prepareInfo = new WXPrepareInfo();
            // 预付订单id
            prepareInfo.setPrepayId(reslutObject.getString("prepayid"));
            // 商户号
            prepareInfo.setPartnerId(reslutObject.getString("partnerid"));
            prepareInfo.setPackageValue(reslutObject.getString("package"));
            prepareInfo.setNonceStr(reslutObject.getString("noncestr"));
            prepareInfo.setTimeStamp(reslutObject.getString("timestamp"));
            prepareInfo.setSign(reslutObject.getString("sign"));
            return prepareInfo;
        } catch (Exception e) {
            DLog.e(TAG, "parseWxPrepareInfo()#exception:\n", e);
        }
        return null;
    }

    /**
     * 解析微信充值获取预支付信息
     *
     * @param reslutObject
     * @return
     */
    public static String parseWxPrepareInfo(JSONObject reslutObject, WXPrepareInfo prepareInfo) {
        try {
//            prepareInfo = new WXPrepareInfo();
            // 预付订单id
            prepareInfo.setPrepayId(reslutObject.getString("prepayid"));
            // 商户号
            prepareInfo.setPartnerId(reslutObject.getString("partnerid"));
            prepareInfo.setPackageValue(reslutObject.getString("package"));
            prepareInfo.setNonceStr(reslutObject.getString("noncestr"));
            prepareInfo.setTimeStamp(reslutObject.getString("timestamp"));
            prepareInfo.setSign(reslutObject.getString("sign"));
            // 返回流水号
            return reslutObject.getString("prepaidTradeNo");
        } catch (Exception e) {
            DLog.e(TAG, "parseWxPrepareInfo()#exception:\n", e);
        }
        return null;
    }

    /**
     * 解析支付宝支付所需信息
     *
     * @param reslutObject
     * @return
     */
    public static String parseAliPayInfo(JSONObject reslutObject) {
        try {
            return reslutObject.getString("payParams");
        } catch (Exception e) {
            DLog.e(TAG, "parseAliPayInfo()#exception:\n", e);
        }
        return null;
    }

    /**
     * 解析用户账户信息
     *
     * @param resultObject
     * @return
     */
    public static AccountInfo parseAccountInfo(JSONObject resultObject) {
        try {
            JSONObject accountObject = resultObject.getJSONObject("account");
            AccountInfo accountInfo = new AccountInfo();
            accountInfo.setId(accountObject.getInt("accountId"));
            accountInfo.setUserId(accountObject.getInt("userId"));
            accountInfo.setTotalTopUpMoney(accountObject.getDouble("totalBalance"));
            accountInfo.setRemainingMoney(accountObject.getDouble("usableBalance"));
            accountInfo.setCreateTime(accountObject.getString("createTime"));
            return accountInfo;
        } catch (Exception e) {
            DLog.e(TAG, "parseAccountInfo()#exception:\n", e);
        }
        return null;
    }


    /**
     * 解析充值详情列表
     *
     * @param resultObject
     * @return
     */
    public static List<TopUpInfo> parseTopUpInfoList(JSONObject resultObject) {
        List<TopUpInfo> list = new ArrayList<>();
        try {
            JSONObject billObject = resultObject.getJSONObject("billList");
            JSONArray infoArray = billObject.getJSONArray("list");
            int length = infoArray.length();
//            java.text.DateFormat format1 = new java.text.SimpleDateFormat(
//                    "yyyy-MM-dd hh:mm:ss");
//            String name = format1.format(new Date(System.currentTimeMillis()))
//                    + ".log";
            DateFormat dateFormat = DateFormat.getDateTimeInstance();
            for (int i = 0; i < length; i++) {
                JSONObject infoObject = infoArray.getJSONObject(i);
                TopUpInfo topUpInfo = new TopUpInfo();
                topUpInfo.setId(infoObject.getInt("prepaidBillId"));
                topUpInfo.setType(infoObject.getInt("prepaidType"));
                topUpInfo.setTradeNo(infoObject.getString("prepaidTradeNo"));
                topUpInfo.setMoney(infoObject.getDouble("prepaidPrice"));
                topUpInfo.setTimestamp(infoObject.getLong("prepaidTime"));
                topUpInfo.setDate(dateFormat.format(topUpInfo.getTimestamp()));
                topUpInfo.setStatus(infoObject.getInt("prepaidStatus"));
                topUpInfo.setCartId(getJsonIntValue("cardId", -1, infoObject));
                topUpInfo.setCartNo(infoObject.getString("cardNum"));
                topUpInfo.setAccountId(infoObject.getInt("accountId"));
                list.add(topUpInfo);
            }
        } catch (Exception e) {
            list = null;
            DLog.e(TAG, "parseTopUpInfoList()#exception:\n", e);
        }
        return list;
    }

    /**
     * 获取json字符串数据,防止为null
     *
     * @param key
     * @param jsonObject
     * @return
     * @throws JSONException
     */
    private static String getJsonStringValue(String key, JSONObject jsonObject) throws
            JSONException {
        return jsonObject.isNull(key) ? "" : jsonObject.getString(key);
//        return "null".equals(jsonObject.getString(key).trim()) ? "" : jsonObject.getString(key);
    }

    /**
     * 获取json字符串数据,防止为null时转换int异常
     *
     * @param key
     * @param jsonObject
     * @param defaultValue
     * @return
     * @throws JSONException
     */
    private static int getJsonIntValue(String key, int defaultValue, JSONObject jsonObject)
            throws JSONException {
        return jsonObject.isNull(key) ? defaultValue : jsonObject.getInt(key);
//        return "null".equals(jsonObject.getString(key).trim()) ? defaultValue : jsonObject
// .getInt(key);
    }

    /**
     * 获取json字符串数据,防止为null时转换double异常
     *
     * @param key
     * @param jsonObject
     * @param defaultValue
     * @return
     * @throws JSONException
     */
    private static double getJsonDoubleValue(String key, double defaultValue, JSONObject
            jsonObject) throws JSONException {
        return jsonObject.isNull(key) ? defaultValue : jsonObject.getDouble(key);
//        return "null".equals(jsonObject.getString(key).trim()) ? defaultValue : jsonObject
// .getDouble(key);
    }

}
