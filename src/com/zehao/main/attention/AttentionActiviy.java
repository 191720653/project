package com.zehao.main.attention;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.Menu;

import com.zehao.R;
import com.zehao.base.TabBarFragmentActivity;
import com.zehao.fragment.Page1;
import com.zehao.fragment.Page2;
import com.zehao.fragment.Page3;
import com.zehao.view.MineViewPager;
import com.zehao.view.SlideTabView;

public class AttentionActiviy extends TabBarFragmentActivity {

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
	 * Third Fragment
	 */
	private Page3 thirdFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// setContentView(R.layout.activity_main);
		// 调用TabBarBaseActivity的方法
		setContentLayout(R.layout.attention_act_layout);

		setRadio(2);

		longToastHandler("This is the Attention Activity!");

		MineViewPager pager = (MineViewPager) findViewById(R.id.pager);
		slideText = (SlideTabView) findViewById(R.id.tabText);
		pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
		slideText.setViewPager(pager);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
