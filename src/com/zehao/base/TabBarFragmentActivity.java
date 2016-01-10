package com.zehao.base;

import com.zehao.R;
import com.zehao.main.Fiveth;
import com.zehao.main.Forth;
import com.zehao.main.MainActivity;
import com.zehao.main.attention.AttentionActiviy;
import com.zehao.scan.ScansActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;

/**
 * < 继承于BaseFragmentActivity，固定底部菜单栏 >
 * 
 * @ClassName: TabBarFragmentActivity
 * @author pc-hao
 * @date 2015年4月27日 上午9:56:55
 * @version V 1.0
 */
public class TabBarFragmentActivity extends BaseFragmentActivity {

	private RadioButton radioFirst;
	private RadioButton radioSecond;
	private RadioButton radioThird;
	private RadioButton radioForth;
	private RadioButton radioFiveth;

	// 用于放置添加子视图
	private LinearLayout ly_content;

	// 内容区域的布局
	private View contentView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_tab);
		ly_content = (LinearLayout) findViewById(R.id.content);
		radioFirst = (RadioButton) findViewById(R.id.radio_first);
		radioSecond = (RadioButton) findViewById(R.id.radio_second);
		radioThird = (RadioButton) findViewById(R.id.radio_third);
		radioForth = (RadioButton) findViewById(R.id.radio_forth);
		radioFiveth = (RadioButton) findViewById(R.id.radio_fiveth);
		radioFirst.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				goActivityAndFinish(MainActivity.class);
			}
		});
		radioSecond.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				goActivityAndFinish(AttentionActiviy.class);
			}
		});
		radioThird.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				goActivityAndFinish(ScansActivity.class);
			}
		});
		radioForth.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				goActivityAndFinish(Forth.class);
			}
		});
		radioFiveth.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				goActivityAndFinish(Fiveth.class);
			}
		});
	}

	/**
	 * 暴露给子类设置底部导航栏选中按钮
	 * 
	 * @param index
	 */
	public void setRadio(int index) {
		switch (index) {
		case 1: {
			radioFirst.setChecked(Boolean.TRUE);
			break;
		}
		case 2: {
			radioSecond.setChecked(Boolean.TRUE);
			break;
		}
		case 3: {
			radioThird.setChecked(Boolean.TRUE);
			break;
		}
		case 4: {
			radioForth.setChecked(Boolean.TRUE);
			break;
		}
		case 5: {
			radioFiveth.setChecked(Boolean.TRUE);
			break;
		}
		default: {
			break;
		}
		}
	}

	/**
	 * 设置内容区域
	 * 
	 * @param resId
	 *            资源文件ID
	 */
	public void setContentLayout(int resId) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		contentView = inflater.inflate(resId, null);
		// LayoutParams layoutParams = new LayoutParams();
		// contentView.setLayoutParams(layoutParams);
		// contentView.setBackgroundDrawable(null);
		if (null != ly_content) {
			ly_content.addView(contentView);
			// 添加Activity到堆栈
			// AppManager.getAppManager().addActivity(this);
			// Log.v("AppManager",
			// "AppManager 添加actiivty！！" + this.getLocalClassName());
		}
	}

	/**
	 * 设置内容区域
	 * 
	 * @param view
	 *            View对象
	 */
	public void setContentLayout(View view) {
		if (null != ly_content) {
			ly_content.addView(view);
		}
	}

	/**
	 * 得到添加的内容的View
	 * 
	 * @return
	 */
	public View getLyContentView() {
		return contentView;
	}

	public TabBarFragmentActivity() {

	}

}
