package com.ttxg.fruitday.gui.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.gui.adapter.GoodsShelfAdapter;
import com.ttxg.fruitday.gui.view.WraperListView;
import com.ttxg.fruitday.model.GoodsShelf;
import com.ttxg.fruitday.util.Constants;
import com.ttxg.fruitday.util.ParseUtil;
import com.ttxg.fruitday.util.Util;

/**
 * 货架Framgent
 * Created by lilijun on 2016/10/8.
 */
public class GoodsShelfFragment extends BaseFragment implements View.OnClickListener,
        GoodsShelfAdapter.OnDeleteShelfListener, GoodsShelfAdapter.OnEditShelfListener {
    /**
     * 获取货架列表接口
     */
    private final String GET_GOODS_SHELF_LIST = "store/floorManage";
    /**
     * 添加新的货架
     */
    private final String CREATE_NEW_SHELF = "store/addFloorDefine";
    /**
     * 删除货架
     */
    private final String DELETE_GOODS_SHELF = "store/delFloorDefine";
    /**
     * 编辑货架名称
     */
    private final String EDIT_GOODS_SHELF = "store/editFloorDefine";
    private WraperListView listView;
    private GoodsShelfAdapter adapter;
    private List<GoodsShelf> shelfList;
    private TextView createNewShelfBtn;

    private boolean isFirstLoad = true;

    /**
     * 请稍后dialog
     */
    private ProgressDialog progressDialog;

    /**
     * 用户正在更改的货架id（删除或者编辑）
     */
    private int changeShelfId = -1;

    /**
     * 编辑货架名称时所新输入的货架名称
     */
    private String editNewShelfName = "";

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isFirstLoad) {
            loadDataGet(GET_GOODS_SHELF_LIST, null);
            isFirstLoad = false;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
//        getActivity().registerReceiver(receiver, new IntentFilter(Constants
//                .ACTION_STOP_SALES_GOODS_SUCCESS));
    }

    @Override
    protected void initView(RelativeLayout view) {
        setCenterView(R.layout.fragment_goods_shelf_list);
        listView = (WraperListView) view.findViewById(R.id.goods_shelf_list_listView);
        createNewShelfBtn = (TextView) view.findViewById(R.id.goods_shelf_list_create_shelf_btn);
        createNewShelfBtn.setOnClickListener(this);

        shelfList = new ArrayList<>();
        adapter = new GoodsShelfAdapter(shelfList);
        listView.setAdapter(adapter);
        listView.setNextPageViewVisible(false);
        adapter.setOnDeleteShelfListener(this);
        adapter.setOnEditShelfListener(this);
    }

    @Override
    protected void onLoadDataSuccess(String tag, JSONObject resultObject) {
        super.onLoadDataSuccess(tag, resultObject);
        if (TextUtils.equals(tag, GET_GOODS_SHELF_LIST)) {
            // 获取货架信息成功
            List<GoodsShelf> emptyList = ParseUtil.parseGoodsShelfList(resultObject);
            if (emptyList == null) {
                showErrorView();
                return;
            }
            shelfList.clear();
            shelfList.addAll(emptyList);
            adapter.notifyDataSetChanged();
            showCenterView();
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            // 发送货架信息加载成功的消息
            EventBus.getDefault().post(shelfList);
        } else if (TextUtils.equals(tag, CREATE_NEW_SHELF)) {
            // 创建新的货架成功(再去获取货架列表数据)
            loadDataGet(GET_GOODS_SHELF_LIST, null);
        } else if ((TextUtils.equals(tag, DELETE_GOODS_SHELF))) {
            // 删除货架成功
            if (changeShelfId != -1) {
                for (GoodsShelf shelf : shelfList) {
                    if (changeShelfId == shelf.getId()) {
                        shelfList.remove(shelf);
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
                Util.showToast(getActivity(), getResources().getString(R.string
                        .delete_shelf_success));
            }
            progressDialog.dismiss();
        } else if (TextUtils.equals(tag, EDIT_GOODS_SHELF)) {
            // 编辑货架名称成功
            Util.showToast(getActivity(), getResources().getString(R.string
                    .edit_shelf_success));
            for (GoodsShelf shelf : shelfList) {
                if (shelf.getId() == changeShelfId) {
                    shelf.setName(editNewShelfName);
                    break;
                }
            }
            adapter.notifyDataSetChanged();
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onLoadDataError(String tag, int errorCode, String msg) {
        super.onLoadDataError(tag, errorCode, msg);
        if (TextUtils.equals(tag, GET_GOODS_SHELF_LIST)) {
            // 获取货架信息失败
            showErrorView();
            Util.showErrorMessage(getActivity(), msg);
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        } else if (TextUtils.equals(tag, CREATE_NEW_SHELF)) {
            // 创建新的货架失败
            Util.showErrorMessage(getActivity(), msg, getResources().getString(R.string
                    .create_new_shelf_failed));
            progressDialog.dismiss();
        } else if ((TextUtils.equals(tag, DELETE_GOODS_SHELF))) {
            // 删除货架失败
            progressDialog.dismiss();
            Util.showErrorMessage(getActivity(), msg, getResources().getString(R.string
                    .delete_shelf_failed));
        } else if (TextUtils.equals(tag, EDIT_GOODS_SHELF)) {
            // 编辑货架名称失败
            progressDialog.dismiss();
            Util.showErrorMessage(getActivity(), msg, getResources().getString(R.string
                    .edit_shelf_failed));
        }
    }

    @Override
    protected void tryAgain() {
        super.tryAgain();
        loadDataGet(GET_GOODS_SHELF_LIST, null);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.goods_shelf_list_create_shelf_btn:
                // 新建货架
                createNewShelfDialog();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
//        getActivity().unregisterReceiver(receiver);
    }


    @Subscribe
    public void onEventMessage(String msg) {
        if (TextUtils.equals(msg, Constants.STORE_GOODS_HAVE_MODIFY)) {
            tryAgain();
        }
    }

//    @Subscribe
//    public void onEventMessage(int goodsId){
//        // 接收停止分销某商品成功的消息
//        tryAgain();
//    }

//    /**
//     * 局部广播类
//     */
//    private BroadcastReceiver receiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (TextUtils.equals(intent.getAction(), Constants.ACTION_STOP_SALES_GOODS_SUCCESS)) {
//                // 接收到停止某件商品成功的消息
//                tryAgain();
//            }
//        }
//    };

    @Override
    public void onDelete(final GoodsShelf goodsShelf) {
        // 删除货架按钮点击回调事件
        Util.showAlertDialog(getActivity(), getResources().getString(R.string.warm_prompt),
                getResources().getString(R.string.delete_shelf_confirm_message), getResources()
                        .getString(R.string.sure), getResources().getString(R.string.cancel), new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // 确定  按钮点击事件
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("floorId", goodsShelf.getId() + "");
                                dialogInterface.dismiss();
                                progressDialog = Util.showLoadingDialog(getActivity(),progressDialog);
                                changeShelfId = goodsShelf.getId();
                                loadDataPost(DELETE_GOODS_SHELF, params);
                            }
                        }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 取消 按钮点击事件
                        dialogInterface.dismiss();
                    }
                });
    }

    @Override
    public void onEdit(GoodsShelf goodsShelf) {
        // 编辑货架按钮点击回调事件
        editShelfDialog(goodsShelf);
    }


    /**
     * 创建新的货架dialog
     */
    private void createNewShelfDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.create_new_shelf_dialog);
        final EditText nameInput = (EditText) dialog.findViewById(R.id.create_new_shelf_name_input);
        TextView sureBtn = (TextView) dialog.findViewById(R.id.create_new_shelf_sure_btn);
        TextView cancelBtn = (TextView) dialog.findViewById(R.id.create_new_shelf_cancel_btn);
        sureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String shelfName = nameInput.getText().toString().trim();
                if (TextUtils.isEmpty(shelfName)) {
                    Util.showToast(getActivity(), getResources().getString(R.string
                            .please_input_new_shelf_name));
                    return;
                }
                Map<String, String> createParams = new HashMap<>();
                createParams.put("floorName", shelfName);

                loadDataPost(CREATE_NEW_SHELF, createParams);
                dialog.dismiss();
                progressDialog = Util.showLoadingDialog(getActivity(),progressDialog);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    /**
     * 编辑货架dialog
     */
    private void editShelfDialog(final GoodsShelf shelf) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.edit_shelf_dialog);
        final EditText nameInput = (EditText) dialog.findViewById(R.id.edit_shelf_name_input);
        TextView sureBtn = (TextView) dialog.findViewById(R.id.edit_shelf_sure_btn);
        TextView cancelBtn = (TextView) dialog.findViewById(R.id.edit_shelf_cancel_btn);
        nameInput.setText(shelf.getName());
        sureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String shelfName = nameInput.getText().toString().trim();
                if (TextUtils.isEmpty(shelfName)) {
                    Util.showToast(getActivity(), getResources().getString(R.string
                            .please_input_new_shelf_name));
                    return;
                }
                if (TextUtils.equals(shelf.getName(), shelfName)) {
                    // 名字没有改动
                    Util.showToast(getActivity(), getResources().getString(R.string
                            .new_name_and_old_name_same));
                    return;
                }
                Map<String, String> createParams = new HashMap<>();
                createParams.put("floorId", shelf.getId() + "");
                createParams.put("floorName", shelfName);

                changeShelfId = shelf.getId();
                editNewShelfName = shelfName;
                loadDataPost(EDIT_GOODS_SHELF, createParams);
                dialog.dismiss();
                progressDialog = Util.showLoadingDialog(getActivity(),progressDialog);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
