package com.zehao.login;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.zehao.R;
import com.zehao.bean.WeatherInfo;
import com.zehao.common.AppConstant;
import com.zehao.common.Constant;
import com.zehao.http.HttpCLient;
import com.zehao.main.MainActivity;
import com.zehao.register.RegisterActivity;
import com.zehao.view.CircleImageButton;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class LoginActivity extends Activity {

	private JSONObject jsonObject = null;
	private EditText et_account, et_password;
	private TextView tv_register;
	private CheckBox cb_rem_password, cb_auto_login;
	private Button btn_login;
	private CircleImageButton btnQuit;
	private String loginNameValue, passwordValue;
	private SharedPreferences sp;
	private ProgressDialog progressDialog;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			progressDialog.dismiss();
		}
	};

	@SuppressLint("WorldReadableFiles")
	@SuppressWarnings({ "deprecation" })
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);

		// ConnectivityManager
		// cwjManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		// Log.d("check_internet",
		// cwjManager.getActiveNetworkInfo().isAvailable()+"");

		sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);
		et_account = (EditText) findViewById(R.id.et_account);
		et_password = (EditText) findViewById(R.id.et_password);
		tv_register = (TextView) findViewById(R.id.tv_register);
		cb_rem_password = (CheckBox) findViewById(R.id.cb_rem_password);
		cb_auto_login = (CheckBox) findViewById(R.id.cb_auto_login);
		btn_login = (Button) findViewById(R.id.btn_login);
		btnQuit = (CircleImageButton) findViewById(R.id.user_icon);

		btnQuit.setImageDrawable(getResources().getDrawable(R.drawable.user_icons));
		
		if (sp.getBoolean("ISCHECK", false)) {

			cb_rem_password.setChecked(true);
			// String name = sp.getString("USER_NAME", "");
			// String pw = sp.getString("PASSWORD", "");

			if (sp.getBoolean("AUTO_ISCHECK", false)) {

				cb_auto_login.setChecked(true);

				Intent intent = new Intent(LoginActivity.this,
						MainActivity.class);
				LoginActivity.this.startActivity(intent);

			}
		}

		tv_register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				Intent intent = new Intent(LoginActivity.this,
						RegisterActivity.class);
				LoginActivity.this.startActivity(intent);
			}
		});

		btn_login.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				loginNameValue = et_account.getText().toString();
				passwordValue = et_password.getText().toString();

				if (loginNameValue.length() != 11 || passwordValue.length() < 6) {
					Toast.makeText(LoginActivity.this, "请填写好用户名以及密码！",
							Toast.LENGTH_LONG).show();
				} else {

					progressDialog = ProgressDialog.show(LoginActivity.this,
							"登录", "正在联网登录,请稍候......");

					try {
						jsonObject = new JSONObject();
						jsonObject.put(Constant.LOGIN_NAME, loginNameValue);
						jsonObject.put(Constant.LOGIN_PASSWORD, passwordValue);

						finish();
						Intent intent = new Intent(
								LoginActivity.this,
								MainActivity.class);
						LoginActivity.this
								.startActivity(intent);
//						HttpCLient.get(AppConstant.WEATHER_URL, null, new AsyncHttpResponseHandler() {
//							
//							@Override
//							public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
//								// TODO Auto-generated method stub
//								Toast.makeText(
//										LoginActivity.this,new String(arg2)
//												+ arg1.toString(),
//										Toast.LENGTH_SHORT)
//										.show();
//								Log.e(Constant.LOG_TAG, "ON_SUCCESS：" + new String(arg2));
//								Log.e(Constant.LOG_TAG, "ON_SUCCESS：" + arg1.toString());
//								Gson gson = new Gson();
//								JsonObject json = new JsonParser().parse(new String(arg2)).getAsJsonObject();
//								WeatherInfo weatherInfo = gson.fromJson(json.get("weatherinfo"), WeatherInfo.class);
//								Log.e(Constant.LOG_TAG, "ON_SUCCESS：" + weatherInfo.toString());
//							}
//							
//							@Override
//							public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
//								// TODO Auto-generated method stub
//								Toast.makeText(LoginActivity.this,
//										"onFailure", Toast.LENGTH_LONG)
//										.show();
//								Log.e(Constant.LOG_TAG, "ON_FAILURE......");
//							}
//
//							@Override
//							public void onFinish() {
//								// TODO Auto-generated method stub
//								Toast.makeText(LoginActivity.this,
//										"onFinish", Toast.LENGTH_LONG)
//										.show();
//								Message message = handler
//										.obtainMessage();
//								message.what = 1;
//								handler.sendMessage(message);
//								Log.e(Constant.LOG_TAG, "ON_FINISH......");
//							}
//							
//							
//						});
						
						/*HttpCLient.postJson(LoginActivity.this, "login",
								new StringEntity(jsonObject.toString()),
								new AsyncHttpResponseHandler() {

									public void onFailure(int arg0,
											Header[] arg1, byte[] arg2,
											Throwable arg3) {
										// TODO Auto-generated method stub
										Message message = handler
												.obtainMessage();
										message.what = 1;
										handler.sendMessage(message);
										Toast.makeText(LoginActivity.this,
												"onFailure", Toast.LENGTH_LONG)
												.show();
									}

									public void onSuccess(int arg0,
											Header[] arg1, byte[] arg2) {
										// TODO Auto-generated method stub
										Message message = handler
												.obtainMessage();
										message.what = 1;
										handler.sendMessage(message);
										Log.d("login_information", "code："
												+ arg0 + " onSuccess："
												+ new String(arg2));
										JSONObject result = null;
										try {
											result = new JSONObject(new String(
													arg2));
											if (result.getString("error")
													.equals("false")) {
												Toast.makeText(
														LoginActivity.this,
														result.getString("message")
																+ new String(
																		arg2),
														Toast.LENGTH_SHORT)
														.show();
												if (cb_rem_password.isChecked()) {
													Editor editor = sp.edit();
													editor.putString(
															"USER_NAME",
															loginNameValue);
													editor.putString(
															"PASSWORD",
															passwordValue);
													editor.commit();
												}
												finish();
												Intent intent = new Intent(
														LoginActivity.this,
														MainActivity.class);
												LoginActivity.this
														.startActivity(intent);
											} else {
												Toast.makeText(
														LoginActivity.this,
														result.getString("message")
																+ new String(
																		arg2),
														Toast.LENGTH_SHORT)
														.show();
											}
										} catch (JSONException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}

									public void onFinish() {// 完成后调用，失败，成功，都要调用
										// TODO Auto-generated method stub
										Toast.makeText(LoginActivity.this,
												"onFinish", Toast.LENGTH_LONG)
												.show();
									}
								});*/
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						Toast.makeText(LoginActivity.this, "登录失败，请检查网络连接状态！",
								Toast.LENGTH_LONG).show();
					}
				}

			}
		});

		cb_rem_password
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (cb_rem_password.isChecked()) {

							System.out.println("记住密码");
							sp.edit().putBoolean("ISCHECK", true).commit();

						} else {

							System.out.println("不记住密码");
							sp.edit().putBoolean("ISCHECK", false).commit();

						}

					}
				});
		cb_auto_login.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (cb_auto_login.isChecked()) {
					System.out.println("自动登录");
					sp.edit().putBoolean("AUTO_ISCHECK", true).commit();

				} else {
					System.out.println("不自动登录");
					sp.edit().putBoolean("AUTO_ISCHECK", false).commit();
				}
			}
		});

		btnQuit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

}