package com.ttxg.fruitday.gui.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ExpandableListView;

/**
 * ExpandableListView 自定义封装类
 * 点击groupitem 可收缩
 * 
 * @author lilijun
 */
public class WraperExpandableListView2 extends ExpandableListView
{

	// /** ~到底了~ 视图 */
	// private FrameLayout endLay = null;

	private Context mContext = null;

	public WraperExpandableListView2(Context context, AttributeSet attrs,
									 int defStyleAttr, int defStyleRes)
	{
		super(context, attrs, defStyleAttr, defStyleRes);
		mContext = context;
		init(context);
	}

	public WraperExpandableListView2(Context context, AttributeSet attrs,
									 int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		mContext = context;
		init(context);
	}

	public WraperExpandableListView2(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		mContext = context;
		init(context);
		// initEndLay(context);
	}

	public WraperExpandableListView2(Context context)
	{
		super(context);
		mContext = context;
		init(context);
	}

	private void init(Context context)
	{
		// 禁止列表到底或者在顶部能继续拉动
		setOverScrollMode(View.OVER_SCROLL_NEVER);
		setDivider(null);
		setChildDivider(null);
		setDividerHeight(0);
		setCacheColorHint(Color.parseColor("#00000000"));
		setFadingEdgeLength(0);
		// 去掉箭头
		setGroupIndicator(null);
	}
}
