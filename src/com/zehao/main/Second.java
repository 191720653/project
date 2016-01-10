package com.zehao.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zehao.R;
import com.zehao.base.TabBarBaseActivity;
import com.zehao.view.ListViewAdapter;
import com.zehao.view.RefreshableView;
import com.zehao.view.RefreshableView.PullToRefreshListener;
import com.zehao.view.SelfListView;
import com.zehao.view.SelfListView.OnLoadListener;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class Second extends TabBarBaseActivity implements OnLoadListener{
	
	RefreshableView refreshableView;
//	ListView listView;
	SelfListView selfListView;
	ArrayAdapter<String> adapter;
	String[] items = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L" };
	
	private ListViewAdapter listViewAdapter;
	private Integer[] imgeIDs = { R.drawable.setbackgroud,
			R.drawable.circscreen, R.drawable.setfont, R.drawable.text32,
			R.drawable.umd32, R.drawable.viewbookmark };
	private String[] goodsNames = { "蛋糕", "礼物", "邮票", "爱心", "鼠标", "音乐CD" };
	private String[] goodsDetails = { "蛋糕：好好吃。蛋糕：好好吃。蛋糕：好好吃。蛋糕：好好吃。蛋糕：好好吃。", 
			"礼物：礼轻情重。礼物：礼轻情重。礼物：礼轻情重。礼物：礼轻情重。礼物：礼轻情重。礼物：礼轻情重。", 
			"邮票：环游世界。邮票：环游世界。邮票：环游世界。邮票：环游世界。邮票：环游世界。邮票：环游世界。",
			"爱心：世界都有爱。爱心：世界都有爱。爱心：世界都有爱。爱心：世界都有爱。爱心：世界都有爱。爱心：世界都有爱。", 
			"鼠标：反应敏捷。鼠标：反应敏捷。鼠标：反应敏捷。鼠标：反应敏捷。鼠标：反应敏捷。鼠标：反应敏捷。", 
			"音乐CD：酷我音乐。音乐CD：酷我音乐。音乐CD：酷我音乐。音乐CD：酷我音乐。音乐CD：酷我音乐。音乐CD：酷我音乐。" }; 
	private int temp = 5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// setContentView(R.layout.activity_main);
		// 调用TabBarBaseActivity的方法
		setContentLayout(R.layout.activity_main);
		
		setRadio(2);

		longToastHandler("This is the Second,Second,Second Activity!");
		
		refreshableView = (RefreshableView) findViewById(R.id.refreshable_view);
		selfListView = (SelfListView) findViewById(R.id.list_view);
//		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
//		selfListView.setAdapter(listViewAdapter);
		selfListView.setOnLoadListener(this);
		refreshableView.setOnRefreshListener(new PullToRefreshListener() {
			@Override
			public void onRefresh() {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				refreshableView.finishRefreshing();
			}
		}, 0);
		
		showView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	/**
	 * 加载适配器
	 */
	private void showView() {
		if (listViewAdapter == null) {
			listViewAdapter = new ListViewAdapter(this, getListItems());
			selfListView.setAdapter(listViewAdapter);
		} else {
			Toast.makeText(this, "update......", Toast.LENGTH_LONG).show();
			listViewAdapter.updateView(addListItems());
		}
//		if (paginationAdapter == null) {
//			paginationAdapter = new DemoAdapter(this, datas);
//			paginationLv.setAdapter(paginationAdapter);
//		} else {
//			paginationAdapter.updateView(datas);
//		}
	}
	
	/**
	 * 初始化商品信息
	 */
	private List<Map<String, Object>> getListItems() {
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < goodsNames.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("image", imgeIDs[i]); // 图片资源
			map.put("title", "物品名称："); // 物品标题
			map.put("info", goodsNames[i]); // 物品名称
			map.put("detail", goodsDetails[i]); // 物品详情
			listItems.add(map);
		}
		return listItems;
	}
	
	private List<Map<String, Object>> addListItems() {
		
		String goodsList = "";
		new AlertDialog.Builder(Second.this).setTitle("购物清单：")
				.setMessage("你好，你选择了如下商品：\n" + listViewAdapter.getCount())
				.setPositiveButton("确定", null).show();
		
		List<Map<String, Object>> listItems = getListItems();
		for (int i = 1; i <= temp; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("image", imgeIDs[temp%5]); // 图片资源
			map.put("title", "增加的-物品名称：" + temp); // 物品标题
			map.put("info", goodsNames[temp%5]); // 物品名称
			map.put("detail", goodsDetails[temp%5]); // 物品详情
			listItems.add(map);
		}
		temp = temp + 5;
		return listItems;
	}

	@Override
	public void onLoad() {
		// 为了显示效果，采用延迟加载
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
//				initLoadData();
				showView();
				selfListView.loadComplete();
			}
		}, 3000);
	}

}
