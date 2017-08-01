package com.ttxg.fruitday.gui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.callback.OnDeleteAddressListener;
import com.ttxg.fruitday.callback.OnEditAddressListener;
import com.ttxg.fruitday.callback.OnSetDefaultAddressListener;
import com.ttxg.fruitday.model.Address;

/**
 * 地址信息Adapter
 * Created by lilijun on 2016/9/19.
 */
public class AddressAdapter extends BaseAdapter {
    private List<Address> addressList;
    private OnDeleteAddressListener deleteAddressListener;
    private OnSetDefaultAddressListener changeDefaultAddressListener;
    private OnEditAddressListener editAddressListener;

    public AddressAdapter(List<Address> addressList) {
        this.addressList = addressList;
    }

    public void setDeleteClickListener(OnDeleteAddressListener deleteClickListener) {
        this.deleteAddressListener = deleteClickListener;
    }

    public void setChangeDefaultAddressListener(OnSetDefaultAddressListener
                                                        changeDefaultAddressListener) {
        this.changeDefaultAddressListener = changeDefaultAddressListener;
    }

    public void setEditAddressListener(OnEditAddressListener editAddressListener) {
        this.editAddressListener = editAddressListener;
    }

    @Override
    public int getCount() {
        return addressList.size();
    }

    @Override
    public Object getItem(int i) {
        return addressList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        HolderView holderView;
        if (view == null) {
            view = View.inflate(viewGroup.getContext(), R.layout.address_list_adapter, null);
            holderView = new HolderView();
            holderView.name = (TextView) view.findViewById(R.id.address_adapter_name);
            holderView.telephone = (TextView) view.findViewById(R.id.address_adapter_telephone);
            holderView.setDefaultAddressLay = (LinearLayout) view.findViewById(R.id
                    .address_adapter_set_default_address_lay);
            holderView.setDefaultAddressLay.setOnClickListener(onSetDefaultAddressClickListener);
            holderView.defaultPointImg = (ImageView) view.findViewById(R.id.address_adapter_default_point_img);
            holderView.address = (TextView) view.findViewById(R.id.address_adapter_address_detail);
            holderView.editBtn = (TextView) view.findViewById(R.id.address_adapter_edit_btn);
            holderView.deleteBtn = (TextView) view.findViewById(R.id.address_adapter_delete_btn);
            holderView.editBtn.setOnClickListener(editClickListener);
            holderView.deleteBtn.setOnClickListener(deleteClickListener);
            view.setTag(holderView);
        } else {
            holderView = (HolderView) view.getTag();
        }
        Address address = (Address) getItem(i);
        holderView.name.setText(address.getName());
        holderView.telephone.setText(address.getMobile());
        holderView.address.setText(address.getProvince() + "," + address.getCity() + "," +
                address.getTown() + address.getDetailAddress());
        if(address.isReceiveDefault()){
            holderView.defaultPointImg.setImageResource(R.drawable.logistics_status2);
        }else {
            holderView.defaultPointImg.setImageResource(R.drawable.logistics_status1);
        }

        holderView.editBtn.setTag(address);
        holderView.editBtn.setTag(R.id.address_adapter_edit_btn,i);
        holderView.deleteBtn.setTag(address);
        holderView.setDefaultAddressLay.setTag(address);
        return view;
    }

    class HolderView {
        TextView name, telephone, address;
        LinearLayout setDefaultAddressLay;
        ImageView defaultPointImg;
        TextView editBtn, deleteBtn;
    }

    /**
     * 设置默认地址点击事件
     */
    private View.OnClickListener onSetDefaultAddressClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Address address = (Address) view.getTag();
            if (!address.isReceiveDefault()) {
                // 如果不是默认地址才回调
                if (changeDefaultAddressListener != null) {
                    changeDefaultAddressListener.onSetDefaultAddressListener(address);
                }
            }
        }
    };

    /**
     * 编辑按钮点击事件
     */
    private View.OnClickListener editClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(editAddressListener != null){
                Address address = (Address) view.getTag();
                int position = (int) view.getTag(R.id.address_adapter_edit_btn);
                editAddressListener.onEditAddress(address,position);
            }
        }
    };

    /**
     * 删除点击事件
     */
    private View.OnClickListener deleteClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (deleteAddressListener != null) {
                Address address = (Address) view.getTag();
                deleteAddressListener.onDeleteListener(address);
            }
        }
    };
}
