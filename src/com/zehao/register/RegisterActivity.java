package com.zehao.register;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.zehao.R;
import com.zehao.common.Constant;
import com.zehao.http.HttpCLient;
import com.zehao.login.LoginActivity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
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
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint({ "HandlerLeak", "WorldReadableFiles" })
public class RegisterActivity extends Activity {

	private Button register;
	private EditText et_account, et_password;
	private SharedPreferences sp;
	private ProgressDialog progressDialog;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			progressDialog.dismiss();
		}
	};

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		setContentView(R.layout.register);
		
		sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);

		register = (Button) findViewById(R.id.register_button);
		et_account = (EditText) findViewById(R.id.register_et_account);
		et_password = (EditText) findViewById(R.id.register_et_password);
		register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (et_account.length() != 11) {
					Toast.makeText(RegisterActivity.this, "请输入正确的手机号码！",
							Toast.LENGTH_SHORT).show();
				} else if (et_password.length() < 6) {
					Toast.makeText(RegisterActivity.this, "密码不能少于6位！",
							Toast.LENGTH_SHORT).show();
				} else {

					progressDialog = ProgressDialog.show(RegisterActivity.this,
							"注册", "正在联网注册,请稍候......");

					try {

						JSONObject json = new JSONObject();

						json.put(Constant.LOGIN_NAME, et_account.getText());
						json.put(Constant.LOGIN_PASSWORD, et_password.getText());

						HttpCLient.postJson(RegisterActivity.this, "register",
								new StringEntity(json.toString()),
								new AsyncHttpResponseHandler() {

									@Override
									public void onFailure(int arg0,
											Header[] arg1, byte[] arg2,
											Throwable arg3) {
										// TODO Auto-generated method stub
										Message message = handler
												.obtainMessage();
										message.what = 1;
										handler.sendMessage(message);
										Toast.makeText(RegisterActivity.this,
												"onFailure", Toast.LENGTH_LONG)
												.show();
									}

									@Override
									public void onSuccess(int arg0,
											Header[] arg1, byte[] arg2) {
										// TODO Auto-generated method stub
										Message message = handler
												.obtainMessage();
										message.what = 1;
										handler.sendMessage(message);
										Log.d("register_information", "code："
												+ arg0 + " onSuccess："
												+ new String(arg2));
										JSONObject result = null;
										try {
											result = new JSONObject(new String(
													arg2));
											if (result.getString("error")
													.equals("false")) {
												Toast.makeText(
														RegisterActivity.this,
														result.getString("message")
																+ new String(
																		arg2),
														Toast.LENGTH_LONG)
														.show();
												Editor editor = sp.edit();
												editor.putString(
														"USER_NAME",
														et_account.getText().toString());
												editor.putString(
														"PASSWORD",
														et_password.getText().toString());
												editor.commit();
												finish();
												Intent intent = new Intent(
														RegisterActivity.this,
														LoginActivity.class);
												RegisterActivity.this
														.startActivity(intent);
											} else {
												Toast.makeText(
														RegisterActivity.this,
														result.getString("message")
																+ new String(
																		arg2),
														Toast.LENGTH_LONG)
														.show();
											}
										} catch (JSONException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}

									@Override
									public void onFinish() {
										// TODO Auto-generated method stub
										Toast.makeText(RegisterActivity.this,
												"onFinish", Toast.LENGTH_LONG)
												.show();
									}
								});
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			Intent intent = new Intent(RegisterActivity.this,
					LoginActivity.class);
			RegisterActivity.this.startActivity(intent);
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
