package com.ttxg.fruitday.callback;

import com.ttxg.fruitday.model.Address;

/**
 * 地址编辑回调接口
 * Created by lilijun on 2016/9/23.
 */
public interface OnEditAddressListener {
    void onEditAddress(Address address,int position);
}
