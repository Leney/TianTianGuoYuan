package com.ttxg.fruitday.gui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.callback.OnDeleteAddressListener;
import com.ttxg.fruitday.callback.OnEditAddressListener;
import com.ttxg.fruitday.callback.OnSetDefaultAddressListener;
import com.ttxg.fruitday.gui.adapter.AddressAdapter;
import com.ttxg.fruitday.gui.view.RefreshListView;
import com.ttxg.fruitday.manager.UserInfoManager;
import com.ttxg.fruitday.model.Address;
import com.ttxg.fruitday.util.ParseUtil;
import com.ttxg.fruitday.util.Util;

/**
 * 地址列表界面
 * Created by lilijun on 2016/9/19.
 */
public class AddressListActivity extends BaseActivity implements OnDeleteAddressListener,
        OnSetDefaultAddressListener, OnEditAddressListener {
    public static final int CHOICE_ADDRESS_ITEM_RESULT_CODE = 10010;
    /**
     * 查询收货地址列表
     */
    private final String QUREY_ADDRESS = "address/queryAddress";
    /**
     * 删除地址
     */
    private final String DELETE_ADDRESS = "address/removeAddress";
    /**
     * 设置默认地址
     */
    private final String SET_DEFAULT_ADDRESS = "address/setReceiveAddress";

    private RefreshListView listView;
    private AddressAdapter adapter;
    private TextView addAddressBtn;
    private List<Address> addressList;
    private LinearLayout noDataLay;
    /**
     * 请稍后dialog
     */
    private ProgressDialog progressDialog;

    /**
     * 点击编辑按钮跳转到编辑地址界面时的列表position
     */
    private int editPosition = 0;

    /**
     * 判断是否是从确认订单界面跳转过来的
     */
    private boolean isFromConfirmOrder = false;

    /**
     * 是否正在刷新
     */
    private boolean isRefreshing = false;


//    @Override
//    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
//        super.onCreate(savedInstanceState, persistentState);
//        EventBus.getDefault().register(this);
//    }

    @Override
    protected void initView() {
        addressList = new ArrayList<>();
        setTitleName(getResources().getString(R.string.receive_address));
        setCenterView(R.layout.activity_adress_list);
        listView = (RefreshListView) findViewById(R.id.address_list_listview);
        listView.setNextPageViewVisible(false);
        adapter = new AddressAdapter(addressList);
        adapter.setDeleteClickListener(this);
        adapter.setChangeDefaultAddressListener(this);
        adapter.setEditAddressListener(this);
        listView.setAdapter(adapter);
        addAddressBtn = (TextView) findViewById(R.id.address_list_add_btn);
        addAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                AddNewAddressActivity.startActivity(AddressListActivity.this);
                AddNewAddressActivity.startActivityForResult(AddressListActivity.this, 1001);
            }
        });

        listView.setonRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefreshing = true;
                loadDataGet(QUREY_ADDRESS, null);
            }
        });
        noDataLay = (LinearLayout) findViewById(R.id.address_list_no_data_lay);
        isFromConfirmOrder = getIntent().getBooleanExtra("isFromConfirmOrder", false);

        if (isFromConfirmOrder) {
            // 如果是从确认订单界面跳转过来的，则设置listView的item点击事件
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    // 判断是否是默认地址
                    Address address = addressList.get(i - 1);
                    if (address.isReceiveDefault()) {
                        // 默认是收货地址
                        // 直接finish掉界面
                        finish();
                        return;
                    }
                    // 如果不是默认收货地址，则去设置为默认收货地址
                    Map<String, String> params = new HashMap<>();
                    params.put("id", address.getId() + "");
                    progressDialog = Util.showLoadingDialog(AddressListActivity.this,
                            progressDialog);
                    loadDataPost(SET_DEFAULT_ADDRESS, params);
                }
            });
        }
        loadDataGet(QUREY_ADDRESS, null);
    }

    @Override
    protected void onLoadDataSuccess(String tag, JSONObject resultObject) {
        super.onLoadDataSuccess(tag, resultObject);
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        if (TextUtils.equals(tag, QUREY_ADDRESS)) {
            List<Address> emptyList = ParseUtil.parseAddressListResult(resultObject);
            if (emptyList != null && !emptyList.isEmpty()) {
                addressList.clear();
                addressList.addAll(emptyList);
            } else {
                addressList.clear();
            }
            if (addressList.isEmpty()) {
                // 设置用户的默认收货信息为空
                UserInfoManager.getInstance().getUserInfo().setDefaultReceiveAddress(null);
                noDataLay.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
            } else {
                noDataLay.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
            }
            if (isRefreshing) {
                listView.onRefreshComplete();
                isRefreshing = false;
            }
            adapter.notifyDataSetChanged();
            showCenterView();
//            if(progressDialog != null){
//                progressDialog.dismiss();
//            }
//            else {
//                showNoDataView();
//            }
        } else if (TextUtils.equals(tag, DELETE_ADDRESS)) {
            // 删除地址
            Util.showToast(AddressListActivity.this, getResources().getString(R.string
                    .delete_success));
            loadDataGet(QUREY_ADDRESS, null);
        } else if (TextUtils.equals(tag, SET_DEFAULT_ADDRESS)) {
            // 设置默认地址
            Util.showToast(AddressListActivity.this, getResources().getString(R.string
                    .set_success));
            if (isFromConfirmOrder) {
                // 是从确认订单界面跳转过来的
//                Intent data = new Intent();
//                Address address = addressList.get(i);
//                data.putExtra("address", address);
//                setResult(CHOICE_ADDRESS_ITEM_RESULT_CODE, data);
                setResult(CHOICE_ADDRESS_ITEM_RESULT_CODE);
                finish();
                return;
            }
            loadDataGet(QUREY_ADDRESS, null);
        }
    }

    @Override
    protected void onLoadDataError(String tag, int errorCode, String msg) {
        super.onLoadDataError(tag, errorCode, msg);
        if (TextUtils.equals(tag, QUREY_ADDRESS)) {
            if (isRefreshing) {
                listView.onRefreshComplete();
                isRefreshing = false;
                Util.showErrorMessage(AddressListActivity.this, msg, getResources().getString(R
                        .string.refresh_fail));
            } else {
                showErrorView();
            }
        } else if (TextUtils.equals(tag, DELETE_ADDRESS)) {
            Util.showToast(AddressListActivity.this, getResources().getString(R.string
                    .delete_failed));
        } else if (TextUtils.equals(tag, SET_DEFAULT_ADDRESS)) {
            Util.showToast(AddressListActivity.this, getResources().getString(R.string
                    .set_default_address_failed));
        }
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == AddNewAddressActivity.ADD_ADDRESS_SUCCESS_RESULT_CODE) {
            loadDataGet(QUREY_ADDRESS, null);
        } else if (resultCode == EditAddressActivity.UPDATE_SUCCESS_RESULT_CODE) {
            // 从编辑界面返回(编辑地址成功)
            Address address = (Address) data.getSerializableExtra("address");
            addressList.get(editPosition).setProvince(address.getProvince());
            addressList.get(editPosition).setCity(address.getCity());
            addressList.get(editPosition).setTown(address.getTown());
            addressList.get(editPosition).setMobile(address.getMobile());
            addressList.get(editPosition).setName(address.getName());
            addressList.get(editPosition).setDetailAddress(address.getDetailAddress());
            addressList.get(editPosition).setPostcode(address.getPostcode());
            addressList.get(editPosition).setLocationId(address.getLocationId());
            adapter.notifyDataSetChanged();
        }
    }

//    @Override
//    protected void onDestroy() {
//        EventBus.getDefault().unregister(this);
//        super.onDestroy();
//    }

    @Override
    protected void tryAgain() {
        super.tryAgain();
        loadDataGet(QUREY_ADDRESS, null);
    }

//    @Subscribe
//    public void onEventMessage(String msg){
//        DLog.i("lilijun","重新编辑了地址信息，再次取获取地址列表信息！！！");
//        if(TextUtils.equals(msg, Constants.UPDATE_ADDRESS_SUCCESS)){
//            loadDataGet(QUREY_ADDRESS, null);
//        }
//    }

    @Override
    public void onDeleteListener(final Address address) {
        // 删除地址的监听回调方法
        AlertDialog.Builder builder = new AlertDialog.Builder(AddressListActivity.this);
        builder.setMessage(getResources().getString(R.string.confirm_delete_address));
        builder.setTitle(getResources().getString(R.string.warm_prompt));
        builder.setPositiveButton(getResources().getString(R.string.sure), new DialogInterface
                .OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 确定点击事件
                Map<String, String> params = new HashMap<>();
                params.put("id", address.getId() + "");
                loadDataPost(DELETE_ADDRESS, params);
                dialog.dismiss();
                progressDialog = Util.showLoadingDialog(AddressListActivity.this, progressDialog);
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface
                .OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 取消点击事件
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public void onSetDefaultAddressListener(Address address) {
        // 设置默认地址的监听回调方法
        Map<String, String> params = new HashMap<>();
        params.put("id", address.getId() + "");
        progressDialog = Util.showLoadingDialog(AddressListActivity.this, progressDialog);
        loadDataPost(SET_DEFAULT_ADDRESS, params);
    }


    @Override
    public void onEditAddress(Address address, int position) {
        // 编辑地址监听的回调方法
//            EditAddressActivity.startActivity(view.getContext(), address);
        editPosition = position;
        EditAddressActivity.startActivityForResult(AddressListActivity.this, address, 1007);
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, AddressListActivity.class);
        context.startActivity(intent);
    }

    public static void startActivityForResult(Activity context, boolean isFromConfirmOrder, int
            requestCode) {
        Intent intent = new Intent(context, AddressListActivity.class);
        intent.putExtra("isFromConfirmOrder", isFromConfirmOrder);
        context.startActivityForResult(intent, requestCode);
    }
}
