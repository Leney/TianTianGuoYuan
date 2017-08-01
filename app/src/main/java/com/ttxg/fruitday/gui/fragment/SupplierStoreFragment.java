package com.ttxg.fruitday.gui.fragment;

import android.widget.RelativeLayout;

import com.ttxg.fruitday.R;


/**
 * 供应商店铺Fragment
 * Created by lilijun on 2016/9/8.
 */
public class SupplierStoreFragment extends BaseFragment {
    @Override
    protected void initView(RelativeLayout view) {
        setCenterView(R.layout.fragment_supplier_store);
        showCenterView();
    }
}
