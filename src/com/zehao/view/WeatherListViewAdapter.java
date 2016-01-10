package com.zehao.view;

import java.util.List;
import java.util.Map;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zehao.R;
import com.zehao.bean.WeatherInfo;
import com.zehao.common.AppConstant;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
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

public class WeatherListViewAdapter extends BaseAdapter {
	private Context context; // 运行上下文
	private List<WeatherInfo> listItems; // 天气信息集合
	private LayoutInflater listContainer; // 视图容器

	public final class ListItemView { // 存放自定义控件集合
		public ImageView image;
		public TextView title;
		public TextView info;
		public Button detail;
	}

	public WeatherListViewAdapter(Context context, List<WeatherInfo> listItems) {
		this.context = context;
		listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
		this.listItems = listItems;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return listItems.size();
	}

	public WeatherInfo getItem(int arg0) {
		// TODO Auto-generated method stub
		return listItems.get(arg0);
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * 显示天气详情
	 * 
	 * @param clickID
	 */
	private void showDetailInfo(int clickID) {
		new AlertDialog.Builder(context)
				.setTitle(listItems.get(clickID).getCity() + "天气详情：")
				.setMessage(listItems.get(clickID).toString())
				.setPositiveButton("确定", null).show();
	}
	
	public void updateView(List<WeatherInfo> listItems) {
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
		// 显示图片的配置
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher)
				.cacheInMemory(true)
				.cacheOnDisc(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();
		String url = AppConstant.WEATHER_IMG_56x40_URL + listItems.get(position).getImg1() + ".gif";
		ImageLoader.getInstance().displayImage(url, listItemView.image, options);
		
		listItemView.title.setText((String) listItems.get(position).getCity());
		StringBuffer temp = new StringBuffer();
		temp.append((String) listItems.get(position).getTemp1());
		temp.append((String) listItems.get(position).getWeather1());
		temp.append((String) listItems.get(position).getWind1());
		temp.append((String) listItems.get(position).getFl1());
		listItemView.info.setText(temp.toString());
		listItemView.detail.setText("天气详情");
		// 注册按钮点击事件
		listItemView.detail.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 显示天气详情
				showDetailInfo(selectID);
			}
		});

		return convertView;
	}
}
