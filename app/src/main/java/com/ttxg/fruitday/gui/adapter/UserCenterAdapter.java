package com.ttxg.fruitday.gui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.model.UserCenterMenu;

/**
 * 用户中心下方menu adapter
 * Created by lilijun on 2016/9/9.
 */
public class UserCenterAdapter extends BaseAdapter {
    List<UserCenterMenu> menuList;

    public UserCenterAdapter(List<UserCenterMenu> menuList) {
        this.menuList = menuList;
    }

    @Override
    public int getCount() {
        return menuList.size();
    }

    @Override
    public Object getItem(int i) {
        return menuList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        HolderView holderView = null;
        if(view == null){
            holderView = new HolderView();
            view = View.inflate(viewGroup.getContext(), R.layout.user_center_menu_adapter,null);
            holderView.icon = (SimpleDraweeView) view.findViewById(R.id.user_center_adapter_icon);
            holderView.name = (TextView) view.findViewById(R.id.user_center_adapter_name);
            holderView.num = (TextView) view.findViewById(R.id.user_center_adapter_num);
            view.setTag(holderView);
        }else {
            holderView = (HolderView) view.getTag();
        }
        UserCenterMenu menu = (UserCenterMenu) getItem(i);
//        holderView.num.setVisibility(View.GONE);
        holderView.icon.setImageURI(menu.getIcon());
        holderView.name.setText(menu.getName());
        holderView.num.setVisibility(isNumVisible(menu.getTag()) ? View.VISIBLE : View.GONE);
        return view;
    }


    class HolderView {
        SimpleDraweeView icon;
        TextView name;
        TextView num;
    }

    private boolean isNumVisible(String tag){
        switch (tag){
//            case UserCenterMenu.USER_ORDER:
//                // 经销订单
//                return UserInfoManager.getInstance().getUserInfo().getDealerOrderNum() != 0;
            default:
                return false;
        }
    }
}
