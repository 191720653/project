package com.zehao.view;

import java.lang.reflect.Field;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.zehao.R;
import com.zehao.transformer.DepthPageTransformer;
import com.zehao.util.FixedSpeedScroller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

@SuppressLint("HandlerLeak")
public class ImageSlideView extends FrameLayout {

	/**
	 * ViewPager
	 */
	private ViewPager viewPager;

	/**
	 * 装点点的ImageView数组
	 */
	private ImageView[] tips;

	/**
	 * 装文字说明的TextView
	 */
	private TextView textView;

	/**
	 * 装ImageView数组
	 */
	private ImageView[][] mImageViews;

	/**
	 * 存放从服务器获取循环播放的图片的URL
	 */
	private String[] imageUrl = null;

	/**
	 * 存放从服务器获取循环播放的文字资源
	 */
	private String[] textIdArray;

	/**
	 * 图片自动切换时间
	 */
	private static final int PHOTO_CHANGE_TIME = 5000;

	/**
	 * 更换图片消息标志
	 */
	private static final int MSG_CHANGE_PHOTO = 1;

	private Handler mHandler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			switch (msg.what) {
			case MSG_CHANGE_PHOTO:
				int index = viewPager.getCurrentItem();
				viewPager.setCurrentItem(index + 1);
				mHandler.sendEmptyMessageDelayed(MSG_CHANGE_PHOTO,
						PHOTO_CHANGE_TIME);
				break;
			default:
				break;
			}
			super.dispatchMessage(msg);
		}
	};

	private Context context;

	public ImageSlideView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public ImageSlideView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public ImageSlideView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		this.context = context;

		initImageLoader(context);

		initData();
	}

	@SuppressLint("ClickableViewAccessibility")
	public void initUI() {

		if (imageUrl == null || imageUrl.length == 0)
			return;

		LayoutInflater.from(context).inflate(R.layout.image_slide_view_layuot,
				this, true);

		ViewGroup group = (ViewGroup) findViewById(R.id.viewGroup);
		viewPager = (ViewPager) findViewById(R.id.viewPager);

		textView = (TextView) findViewById(R.id.slide_textView);

		// 将点点加入到ViewGroup中
		tips = new ImageView[imageUrl.length];

		if (imageUrl.length <= 1)
			group.setVisibility(View.GONE);
		for (int i = 0; i < tips.length; i++) {
			ImageView imageView = new ImageView(context);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					15, 15);
			params.leftMargin = 8;
			params.rightMargin = 8;
			tips[i] = imageView;
			if (i == 0) {
				tips[i].setBackgroundResource(R.drawable.index_focus);
			} else {
				tips[i].setBackgroundResource(R.drawable.index_unfocus);
			}

			group.addView(imageView, params);
		}

		mImageViews = new ImageView[2][];
		// 将图片装载到数组中,其中一组类似缓冲，防止图片少时出现黑色图片，即显示不出来
		mImageViews[0] = new ImageView[imageUrl.length];
		mImageViews[1] = new ImageView[imageUrl.length];

		for (int i = 0; i < mImageViews.length; i++) {
			for (int j = 0; j < mImageViews[i].length; j++) {
				ImageView imageView = new ImageView(context);
				imageView.setTag(imageUrl[j]);
				imageView.setScaleType(ScaleType.FIT_XY);
				mImageViews[i][j] = imageView;
				Log.i("TwoActivity_WY", i + "," + j + "\t");
			}
		}

		controlViewPagerSpeed();
		// 设置过渡效果
		viewPager.setPageTransformer(true, new DepthPageTransformer());
		// 设置Adapter
		viewPager.setAdapter(new MyAdapter());
		// 设置监听，主要是设置点点的背景
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// Toast.makeText(context, "here " + arg0,
				// Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// Toast.makeText(context, "there " + arg0,
				// Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onPageSelected(int arg0) {
				setImageBackground(arg0 % imageUrl.length);
				// Toast.makeText(context, "end " + arg0,
				// Toast.LENGTH_SHORT).show();
			}
		});

		viewPager.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (imageUrl.length == 0 || imageUrl.length == 1) {
					return true;
				} else {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN: {
						mHandler.removeMessages(MSG_CHANGE_PHOTO);
						break;
					}
					case MotionEvent.ACTION_UP: {
						mHandler.sendEmptyMessageDelayed(MSG_CHANGE_PHOTO,
								PHOTO_CHANGE_TIME);
						break;
					}
					}
					return false;
				}
			}
		});

		// 设置ViewPager的默认项, 设置为长度的50倍，这样子开始就能往左滑动
		viewPager.setCurrentItem((imageUrl.length) * 50);
		mHandler.sendEmptyMessageDelayed(MSG_CHANGE_PHOTO, PHOTO_CHANGE_TIME);
	}

	// 初始化Viewpager的滑动速度
	private FixedSpeedScroller mScroller = null;

	/**
	 * 放慢viewpager的滑动速度
	 */
	private void controlViewPagerSpeed() {
		try {
			Field mField;

			mField = ViewPager.class.getDeclaredField("mScroller");
			mField.setAccessible(true);

			mScroller = new FixedSpeedScroller(context,
					new AccelerateInterpolator());
			mScroller.setmDuration(500); // 2000ms
			mField.set(viewPager, mScroller);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			if (imageUrl.length == 1)
				((ViewPager) container).removeView(mImageViews[position
						/ imageUrl.length % 2][0]);
			else
				((ViewPager) container).removeView(mImageViews[position
						/ imageUrl.length % 2][position % imageUrl.length]);
		}

		/**
		 * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
		 */
		@Override
		public Object instantiateItem(View container, int position) {
			if (imageUrl.length == 1)
				return mImageViews[position / imageUrl.length % 2][0];
			else {

				ImageView imageView = mImageViews[position / imageUrl.length
						% 2][position % imageUrl.length];
				DisplayImageOptions options = new DisplayImageOptions.Builder()
				// .showImageOnLoading(R.drawable.ic_stub) //加载图片时的图片
				// .showImageForEmptyUri(R.drawable.abcd) //没有图片资源时的默认图片
				// .showImageOnFail(R.drawable.abcd) //加载失败时的图片
						.cacheInMemory(true) // 启用内存缓存
						.cacheOnDisc(true) // 启用外存缓存
						// .considerExifParams(true) //启用EXIF和JPEG图像格式
						// .displayer(new RoundedBitmapDisplayer(10))
						// //设置显示风格这里是圆角矩形
						.build();

				imageLoader.displayImage(imageView.getTag() + "", imageView,
						options);
				((ViewPager) container).addView(mImageViews[position
						/ imageUrl.length % 2][position % imageUrl.length], 0);
			}
			return mImageViews[position / imageUrl.length % 2][position
					% imageUrl.length];
		}

	}

	/**
	 * 设置选中的tip的背景
	 * 
	 * @param selectItemsIndex
	 */
	private void setImageBackground(int selectItemsIndex) {
		for (int i = 0; i < tips.length; i++) {
			if (i == selectItemsIndex) {
				textView.setText(textIdArray[i]);
				tips[i].setBackgroundResource(R.drawable.index_focus);
			} else {
				tips[i].setBackgroundResource(R.drawable.index_unfocus);
			}
		}
	}

	// 开源组件获取图片
	// 这里使用Android的开源框架universal-image-loader读取服务器的图片
	private ImageLoader imageLoader = ImageLoader.getInstance();

	/**
	 * 初始化相关Data
	 */
	private void initData() {
		// 异步任务获取图片
		new GetListTask().execute("");
	}

	/**
	 * 异步任务,获取数据
	 * 
	 */
	class GetListTask extends AsyncTask<String, Integer, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			try {
				// 这里一般调用 服务端 接口获取一组轮播图片10.10.1.43:8080

				imageUrl = new String[] {
						"http://pic1.win4000.com/wallpaper/8/5338e59a85497.jpg",
						"http://pic1.win4000.com/wallpaper/8/5338e59d4f530.jpg",
						"http://pic1.win4000.com/wallpaper/8/5338e59fd3603.jpg",
						"http://pic1.win4000.com/wallpaper/8/5338e5a2e99f5.jpg",
						"http://pic1.win4000.com/wallpaper/8/5338e5a5a7701.jpg",
						"http://pic1.win4000.com/wallpaper/8/5338e5a87a8a4.jpg",
						"http://pic1.win4000.com/wallpaper/a/52c624f239316.jpg" };

				textIdArray = new String[] { "对酒当歌", "人生几何", "宁静致远", "淡泊明志",
						"脚踏实地", "匍匐前进", "任重道远" };
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (result) {
				initUI();
			}
		}
	}

	/**
	 * ImageLoader 图片组件初始化
	 * 
	 * @param context
	 */
	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs().build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

}
