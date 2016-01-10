package com.zehao.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
/*
 * 自定义ViewPager类，允许嵌套的可滚动视图滚动
 */
public class MineViewPager extends ViewPager{

	public MineViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public MineViewPager(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public MineViewPager(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	/**
	 * 重写ViewPager的canScroll方法，允许嵌套的可滚动视图滚动
	 */
	@Override
	protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
	   if(v != this && v instanceof ViewPager) {
	      return true;
	   }
	   return super.canScroll(v, checkV, dx, x, y);
	}

}
