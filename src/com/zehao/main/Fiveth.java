package com.zehao.main;

import com.zehao.R;
import com.zehao.base.TabBarBaseActivity;

import android.os.Bundle;
import android.view.Menu;

public class Fiveth extends TabBarBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// setContentView(R.layout.activity_main);
		// 调用TabBarBaseActivity的方法
		setContentLayout(R.layout.user_info_layout);

		setRadio(5);

		longToastHandler("This is the Fiveth,Fiveth,Fiveth Activity!");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
