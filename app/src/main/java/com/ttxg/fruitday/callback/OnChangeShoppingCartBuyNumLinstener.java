package com.ttxg.fruitday.callback;

/**
 * 改变购物车中更改购买数量的回调接口
 * Created by yb on 2016/9/14.
 */
public interface OnChangeShoppingCartBuyNumLinstener {
    /**
     * 数量更改
     * @param isAdd true=数目增加1,否则减1
     * @param groupPosition 所在组的position
     * @param childPosition 所在组下的子item的position
     */
    void onBuyNumChange(boolean isAdd,int groupPosition,int childPosition);
}
