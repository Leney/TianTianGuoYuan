package com.ttxg.fruitday.callback;

/**
 * 删除了购物车的数据回调接口
 * Created by lilijun on 2016/9/14.
 */
public interface OnDeleteShoppingCartItemLinstener {
    void onDeleteItem(int groupPosition,int childPosition);
}
