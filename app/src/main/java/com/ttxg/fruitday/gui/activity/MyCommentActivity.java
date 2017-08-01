package com.ttxg.fruitday.gui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.gui.adapter.FragmentViewPagerAdapter;
import com.ttxg.fruitday.gui.fragment.MyCommentFragment;

/**
 * 我的评论界面
 * Created by lilijun on 2016/10/13.
 */
public class MyCommentActivity extends BaseActivity {

    private RadioGroup commentGroup;

    private RadioButton[] commentTabs;

    private ViewPager viewPager;

    @Override
    protected void initView() {
        setTitleName(getResources().getString(R.string.my_comment));
        setCenterView(R.layout.activity_my_comment);
        commentGroup = (RadioGroup) findViewById(R.id.my_comment_radioGroup);
        commentGroup.setOnCheckedChangeListener(onCheckedChangeListener);

        commentTabs = new RadioButton[3];
        commentTabs[0] = (RadioButton) findViewById(R.id.my_comment_good);
        commentTabs[1] = (RadioButton) findViewById(R.id.my_comment_normal);
        commentTabs[2] = (RadioButton) findViewById(R.id.my_comment_low);

        viewPager = (ViewPager) findViewById(R.id.my_comment_viewPager);

        List<Fragment> commentFragmentList = new ArrayList<>();

        MyCommentFragment goodsCommentFragment = new MyCommentFragment();
        goodsCommentFragment.init(1);

        MyCommentFragment normalCommentFragment = new MyCommentFragment();
        normalCommentFragment.init(2);

        MyCommentFragment lowCommentFragment = new MyCommentFragment();
        lowCommentFragment.init(3);

        commentFragmentList.add(goodsCommentFragment);
        commentFragmentList.add(normalCommentFragment);
        commentFragmentList.add(lowCommentFragment);

        FragmentViewPagerAdapter pagerAdapter = new FragmentViewPagerAdapter
                (getSupportFragmentManager(), commentFragmentList);

        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(onPageChangeListener);

        showCenterView();
    }

    private RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup
            .OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            switch (i) {
                case R.id.my_comment_good:
                    // 好评
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.my_comment_normal:
                    // 中评
                    viewPager.setCurrentItem(1);
                    break;
                case R.id.my_comment_low:
                    // 差评
                    viewPager.setCurrentItem(2);
                    break;
            }
        }
    };


    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }
        @Override
        public void onPageSelected(int position) {
            viewPager.setCurrentItem(position);
            commentTabs[position].setChecked(true);
        }
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    public static void startActivity(Context context){
        Intent intent = new Intent(context,MyCommentActivity.class);
        context.startActivity(intent);
    }
}
