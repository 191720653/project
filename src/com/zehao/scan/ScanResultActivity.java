package com.zehao.scan;

import com.zehao.R;
import com.zehao.base.BaseActivity;
import com.zehao.main.MainActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class ScanResultActivity extends BaseActivity {

	private TextView mTextView;
	private ImageView mImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		setContentView(R.layout.scan_activity);

		mTextView = (TextView) findViewById(R.id.result);
		mImageView = (ImageView) findViewById(R.id.qrcode_bitmap);

		Bundle bundle = this.getIntent().getExtras();
		mTextView.setText(bundle.getString("result"));
		mImageView.setImageBitmap((Bitmap) bundle.getParcelable("bitmap"));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			Intent intent = new Intent(ScanResultActivity.this,
					MainActivity.class);
			ScanResultActivity.this.startActivity(intent);
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
