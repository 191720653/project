//package com.zehao.base;
//
//import com.zehao.R;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.Window;
//import android.widget.Button;
//import android.widget.LinearLayout;
//
///** * 继承于Activity用于以后方便管理 * * @author coder * */ 
//public class baseactivity2 extends Activity { //固定的菜单栏radiobutton 
//	private Button homeRadioButton; 
//	private Button priceRadioButton; 
//	private Button newsRadioButton; 
//	private Button calRadioButton; 
//	private LinearLayout ly_content; 
//	// 内容区域的布局 private View contentView; 
//	@Override 
//	protected void onCreate(Bundle savedInstanceState) { 
//		super.onCreate(savedInstanceState); 
//		requestWindowFeature(Window.FEATURE_NO_TITLE); 
//		setContentView(R.layout.main); 
//		ly_content = (LinearLayout) findViewById(ntent); 
//		homeRadioButton = (Button) findViewById(R.id.homeRadioButton); 
//		priceRadioButton = (Button) findViewById(R.id.priceRadioButton); 
//		newsRadioButton = (Button) findViewById(R.id.newsRadioButton); 
//		calRadioButton = (Button) findViewById(R.id.calRadioButton); 
//		homeRadioButton.setOnClickListener(new OnClickListener(){ 
//			public void onClick(View v) { 
//				Intent intent1 = new Intent(baseactivity2.this,HomeActivity.class); 
//				startActivity(intent1); } }); 
//		priceRadioButton.setOnClickListener(new OnClickListener(){ 
//			public void onClick(View v) { 
//				Intent intent2 = new Intent(baseactivity2.this,PriceActivity.class); 
//				startActivity(intent2); } }); 
//		calRadioButton.setOnClickListener(new OnClickListener(){ 
//			public void onClick(View v) { 
//				Intent intent3 = new Intent(baseactivity2.this,CalendarActivity.class); 
//				startActivity(intent3); } }); 
//		newsRadioButton.setOnClickListener(new OnClickListener(){ 
//			public void onClick(View v) { 
//				Intent intent4 = new Intent(baseactivity2.this,NewsActivity.class); 
//				startActivity(intent4); } }); 
//		} 
//	/*** * 设置内容区域 * * @param resId * 资源文件ID */ 
//	public void setContentLayout(int resId) { 
//		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		contentView = inflater.inflate(resId, null); 
//		LayoutParams layoutParams = new LayoutParams(); 
//		contentView.setLayoutParams(layoutParams); 
//		//contentView.setBackgroundDrawable(null); 
//		if (null != ly_content) { 
//			ly_content.addView(contentView); 
//		// 添加Activity到堆栈
//		AppManager.getAppManager().addActivity(this); 
//		Log.v("AppManager", "AppManager 添加actiivty！！"+this.getLocalClassName()); 
//		} 
//		} 
//	/*** * 设置内容区域 * * @param view * View对象 */
//	public void setContentLayout(View view) { 
//		if (null != ly_content) { 
//			ly_content.addView(view); 
//			} 
//		} 
//	/** * 得到内容的View * * @return */ 
//	public View getLyContentView() { 
//		return contentView; 
//		} 
//	public baseactivity2() {
//		
//	} 
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) { 
//		/** 
//		 * 1. 如果不分组，就自定义为Menu.NONE 
//		 * 2. id:　这个很重要：onOptionsItemSelected(MenuItem * item) 根据id来判断那个菜单被选中 
//		 * 3. 定义菜单的排列 
//		 * 3. 设置Title 
//		 * */ 
//		menu.add(Menu.NONE, 1, Menu.NONE, "退出"); 
//		menu.add(Menu.NONE, 2, Menu.NONE, "取消"); 
//		return super.onCreateOptionsMenu(menu); } 
//	@Override 
//	public boolean onOptionsItemSelected(MenuItem item) 
//	{ 
//		if(item.getItemId() == 1) { 
//		//finish(); 
//		//	System.exit(0); 
//		//退出程序  AppManager.getAppManager().AppExit(BaseActivity.this); 
//		} else if(item.getItemId() == 2){
//			
//		} 
//	return super.onOptionsItemSelected(item); 
//	} 
//	public void onClick(View v) {
//		
//	}
//		
//}

//AppManager:
//	　　public class AppManager { public static Activity context = null; private static Stack<Activity> activityStack; private static AppManager instance; private AppManager(){} /** * 单一实例 */ public static AppManager getAppManager(){ if(instance==null){ instance=new AppManager(); } return instance; } /** * 添加Activity到堆栈 */ public void addActivity(Activity activity){ if(activityStack==null){ activityStack=new Stack<Activity>(); } activityStack.add(activity); } /** * 获取当前Activity（堆栈中最后一个压入的） */ public Activity currentActivity(){ Activity activity=activityStack.lastElement(); return activity; } /** * 结束当前Activity（堆栈中最后一个压入的） */ public void finishActivity(){ Activity activity=activityStack.lastElement(); finishActivity(activity); } /** * 结束指定的Activity */ public void finishActivity(Activity activity){ if(activity!=null){ activityStack.remove(activity); activity.finish(); activity=null; } } /** * 结束指定类名的Activity */ public void finishActivity(Class< > cls){ for (Activity activity : activityStack) { if(activity.getClass().equals(cls) ){ finishActivity(activity); } } } /** * 结束所有Activity */ public void finishAllActivity(){ for (int i = 0, size = activityStack.size(); i < size; i++){ if (null != activityStack.get(i)){ activityStack.get(i).finish(); } } activityStack.clear(); } /** * 退出应用程序 */ public void AppExit(Context context) { try { finishAllActivity(); ActivityManager activityMgr= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE); activityMgr.restartPackage(context.getPackageName()); System.exit(0); } catch (Exception e) {	} } }


//例如HomeActivity继承baseActivity的关键代码：
//@Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        
//        //setContentView(R.layout.home);
//     //调用baseactivity的方法
//        setContentLayout(R.layout.home);
//}