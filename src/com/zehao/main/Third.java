package com.zehao.main;

import com.zehao.R;
import com.zehao.base.TabBarFragmentActivity;
import com.zehao.fragment.Page1;
import com.zehao.fragment.Page2;
import com.zehao.fragment.Page3;
import com.zehao.view.MineViewPager;
import com.zehao.view.SlideTabView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;

public class Third extends TabBarFragmentActivity {
	
	/**
	 * Get the displayMetrics of the phone
	 */
	private DisplayMetrics displayMetrics;
	
	/**
	 * SlideText
	 */
	private SlideTabView slideText;

	/**
	 * First Fragment
	 */
	private Page1 firstFragment;

	/**
	 * Second Fragment
	 */
	private Page2 secondFragment;

	/**
	 *  Third Fragment
	 */
	private Page3 thirdFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// setContentView(R.layout.activity_main);
		// 调用TabBarBaseActivity的方法
		setContentLayout(R.layout.third);

		setRadio(3);

		longToastHandler("This is the Third,Third,Third Activity!");
		
		
		MineViewPager pager = (MineViewPager) findViewById(R.id.pager);
		slideText = (SlideTabView) findViewById(R.id.tabText);
		pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
		slideText.setViewPager(pager);
		initSlideText();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/**
	 * Init the attributes in SlideText
	 */
	private void initSlideText() {
		
		//Get the displayMetrics of the phone
		displayMetrics = getResources().getDisplayMetrics();
		
		// 设置Tab是自动填充满屏幕的
//		slideText.setShouldExpand(true);
		// 设置Tab的分割线是透明的
//		slideText.setDividerColor(Color.TRANSPARENT);
//		// 设置Tab底部线的高度
//		slideText.setUnderlineHeight((int) TypedValue.applyDimension(
//				TypedValue.COMPLEX_UNIT_DIP, 1, displayMetrics));
//		// 设置Tab Indicator的高度
//		slideText.setIndicatorHeight((int) TypedValue.applyDimension(
//				TypedValue.COMPLEX_UNIT_DIP, 4, displayMetrics));
//		// 设置Tab标题文字的大小
//		slideText.setTextSize((int) TypedValue.applyDimension(
//				TypedValue.COMPLEX_UNIT_SP, 16, displayMetrics));
//		// 设置Tab Indicator的颜色
//		slideText.setIndicatorColor(Color.parseColor("#45c01a"));
//		// 设置选中Tab文字的颜色 (这是我自定义的一个方法)
//		slideText.setSelectedTextColor(Color.parseColor("#45c01a"));
//		// 取消点击Tab时的背景色
//		slideText.setTabBackground(0);
	}

	public class MyPagerAdapter extends FragmentPagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		private final String[] titles = { "参加", "关注", "历史" };

		@Override
		public CharSequence getPageTitle(int position) {
			return titles[position];
		}

		@Override
		public int getCount() {
			return titles.length;
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				if (firstFragment == null) {
					firstFragment = new Page1();
				}
				return firstFragment;
			case 1:
				if (secondFragment == null) {
					secondFragment = new Page2();
				}
				return secondFragment;
			case 2:
				if (thirdFragment == null) {
					thirdFragment = new Page3();
				}
				return thirdFragment;
			default:
				return null;
			}
		}

	}

}
