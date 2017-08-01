package com.ttxg.fruitday.gui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.ttxg.fruitday.R;
import com.ttxg.fruitday.gui.activity.SearchActivity;
import com.ttxg.fruitday.model.Classify;

import java.util.List;

/**
 * 首页列表的Adatper
 * Created by liijun on 2016/8/22.
 */
public class ClassifyChildAdapter extends BaseExpandableListAdapter {
    /**
     * 商品类型
     */
    public static final int GOODS_TYPE = 1;
    /**
     * 商品广告类型
     */
    public static final int AD_TYPE = 2;
    private List<String> groupList;
    private List<List<Classify[]>> childList;

    public ClassifyChildAdapter(List<String> groupList, List<List<Classify[]>> childList) {
        this.groupList = groupList;
        this.childList = childList;
    }

    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return childList.get(i).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        GroupHolderView groupHolderView = null;
        if (view == null) {
            view = View.inflate(viewGroup.getContext(), R.layout.classify_child_group_item, null);
            groupHolderView = new GroupHolderView(view);
            view.setTag(groupHolderView);
        } else {
            groupHolderView = (GroupHolderView) view.getTag();
        }
        String name = groupList.get(i);
        groupHolderView.name.setText(name);
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean b, View view, ViewGroup viewGroup) {
        ChildHolderView childHolderView = null;
        if (view == null) {
            // 商品类型
            view = View.inflate(viewGroup.getContext(), R.layout.classify_child_child_item, null);
            childHolderView = new ChildHolderView(view);
            view.setTag(childHolderView);
        } else {
            childHolderView = (ChildHolderView) view.getTag();
        }
        Classify[] childItem = (Classify[]) getChild(groupPosition, childPosition);
        //左边子item的视图
        childHolderView.icon1.setImageURI(childItem[0].getLogo());
        childHolderView.name1.setText(childItem[0].getName());
        childHolderView.lay1.setTag(childItem[0]);
        if (childItem[1] != null) {
            //证明有下一个数据
            childHolderView.icon2.setImageURI(childItem[1].getLogo());
            childHolderView.name2.setText(childItem[1].getName());
            childHolderView.lay2.setVisibility(View.VISIBLE);
            childHolderView.lay2.setTag(childItem[1]);
            if(childItem[2] != null){
                //证明有第三个数据
                childHolderView.icon3.setImageURI(childItem[2].getLogo());
                childHolderView.name3.setText(childItem[2].getName());
                childHolderView.lay3.setVisibility(View.VISIBLE);
                childHolderView.lay3.setTag(childItem[2]);
            }else {
                childHolderView.lay3.setVisibility(View.INVISIBLE);
            }
        }else {
            childHolderView.lay2.setVisibility(View.INVISIBLE);
            childHolderView.lay3.setVisibility(View.INVISIBLE);
        }
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }


    class GroupHolderView {
        public TextView name;

        public void init(View baseView) {
            name = (TextView) baseView.findViewById(R.id.classify_child_group_name);
        }

        public GroupHolderView(View baseView) {
            init(baseView);
        }
    }

    class ChildHolderView {
        public LinearLayout lay1, lay2, lay3;
        public SimpleDraweeView icon1, icon2, icon3;
        public TextView name1, name2, name3;

        public void init(View baseView) {
            icon1 = (SimpleDraweeView) baseView.findViewById(R.id.classify_chlid_icon1);
            icon2 = (SimpleDraweeView) baseView.findViewById(R.id.classify_chlid_icon2);
            icon3 = (SimpleDraweeView) baseView.findViewById(R.id.classify_chlid_icon3);

            name1 = (TextView) baseView.findViewById(R.id.classify_chlid_name1);
            name2 = (TextView) baseView.findViewById(R.id.classify_chlid_name2);
            name3 = (TextView) baseView.findViewById(R.id.classify_chlid_name3);

            lay1 = (LinearLayout) baseView.findViewById(R.id.classify_chlid_lay1);
            lay2 = (LinearLayout) baseView.findViewById(R.id.classify_chlid_lay2);
            lay3 = (LinearLayout) baseView.findViewById(R.id.classify_chlid_lay3);

            lay1.setOnClickListener(childItemClickListener);
            lay2.setOnClickListener(childItemClickListener);
            lay3.setOnClickListener(childItemClickListener);
        }

        public ChildHolderView(View baseView) {
            init(baseView);
        }
    }

    /**
     * 类型item 点击事件
     */
    private View.OnClickListener childItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Classify classify = (Classify) view.getTag();
            SearchActivity.startActivity(view.getContext(),classify.getId());
        }
    };
}
