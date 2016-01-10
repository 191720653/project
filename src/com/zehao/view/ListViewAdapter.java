package com.zehao.view;

import java.util.List;
import java.util.Map;

import com.zehao.R;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter {
	private Context context; // 运行上下文
	private List<Map<String, Object>> listItems; // 商品信息集合
	private LayoutInflater listContainer; // 视图容器

	public final class ListItemView { // 自定义控件集合
		public ImageView image;
		public TextView title;
		public TextView info;
		public Button detail;
	}

	public ListViewAdapter(Context context, List<Map<String, Object>> listItems) {
		this.context = context;
		listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
		this.listItems = listItems;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return listItems.size();
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return listItems.get(arg0).get("info");
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * 显示物品详情
	 * 
	 * @param clickID
	 */
	private void showDetailInfo(int clickID) {
		new AlertDialog.Builder(context)
				.setTitle("物品详情：" + listItems.get(clickID).get("info"))
				.setMessage(listItems.get(clickID).get("detail").toString())
				.setPositiveButton("确定", null).show();
	}
	
	public void updateView(List<Map<String, Object>> listItems) {
		this.listItems = listItems;
		this.notifyDataSetChanged();
	}

	/**
	 * ListView Item设置
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
//		Log.e("method", "getView");
		final int selectID = position;
		// 自定义视图
		ListItemView listItemView = null;
		if (convertView == null) {
			listItemView = new ListItemView();
			// 获取list_item布局文件的视图
			convertView = listContainer.inflate(R.layout.list_item, null);
			// 获取控件对象
			listItemView.image = (ImageView) convertView
					.findViewById(R.id.imageItem);
			listItemView.title = (TextView) convertView
					.findViewById(R.id.titleItem);
			listItemView.info = (TextView) convertView
					.findViewById(R.id.infoItem);
			listItemView.detail = (Button) convertView
					.findViewById(R.id.detailItem);
			// 设置控件集到convertView
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}
		// Log.d("image", (String) listItems.get(position).get("title")); //测试
		// Log.e("image", (String) listItems.get(position).get("info"));

		// 设置文字和图片
		listItemView.image.setBackgroundResource((Integer) listItems.get(
				position).get("image"));
		listItemView.title.setText((String) listItems.get(position)
				.get("title"));
		listItemView.info.setText((String) listItems.get(position).get("info"));
		listItemView.detail.setText("活动详情");
		// 注册按钮点击时间爱你
		listItemView.detail.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 显示物品详情
				showDetailInfo(selectID);
			}
		});

		return convertView;
	}
}
