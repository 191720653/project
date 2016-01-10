package com.zehao.scan;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.zehao.R;
import com.zehao.scan.camera.CameraManager;
import com.zehao.scan.decoding.CaptureActivityHandler;
import com.zehao.scan.decoding.InactivityTimer;
import com.zehao.scan.decoding.RGBLuminanceSource;
import com.zehao.scan.view.ViewfinderView;

/**
 * Initial the camera
 * 
 * @author Ryan.Tang
 */
public class ScansActivity extends Activity implements Callback {

	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;
	private ImageView imageView;
	private int RESULT_LOAD_IMAGE = 8;
//	private ProgressDialog progressDialog;
	private String picturePath;
	private Bitmap scanBitmap;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_capture);
		// ViewUtil.addTopView(getApplicationContext(), this,
		// R.string.scan_card);
		CameraManager.init(getApplication());
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);

		imageView = (ImageView) findViewById(R.id.help_id);
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent localIntent = new Intent();
				localIntent.setType("image/*");
				localIntent.setAction("android.intent.action.GET_CONTENT");
				startActivityForResult(localIntent, RESULT_LOAD_IMAGE);
			}
		});

//		Button mButtonBack = (Button) findViewById(R.id.button_back);
//		mButtonBack.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				MipcaActivityCapture.this.finish();
//
//			}
//		});
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;

	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		Log.d("asdfghjkl--inactivityTimer", (inactivityTimer==null) + "");
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	/**
	 * ����ɨ����
	 * 
	 * @param result
	 * @param barcode
	 */
	public void handleDecode(Result result, Bitmap barcode) {
		inactivityTimer.onActivity();
		Log.d("asdfghjkl--inactivityTimer", (inactivityTimer==null) + "");
		playBeepSoundAndVibrate();
		if (result == null || result.getText().equals("")) {
			Toast.makeText(ScansActivity.this, "Scan failed!",
					Toast.LENGTH_SHORT).show();
		} else {
			Intent resultIntent = new Intent(ScansActivity.this, ScanResultActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("result", result.getText());
			bundle.putParcelable("bitmap", barcode);
			resultIntent.putExtras(bundle);
			startActivity(resultIntent);
			finish();
		}
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
				&& null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			picturePath = cursor.getString(columnIndex);// 获取选中图片的路径
			cursor.close();

			// String picturePath contains the path of selected Image

			// progressDialog = new ProgressDialog(MipcaActivityCapture.this);
			// progressDialog.setMessage("正在扫描...");
			// progressDialog.setCancelable(false);
			// progressDialog.show();

			new Thread(new Runnable() {
				@Override
				public void run() {
					// scanningImage(picturePath);
					scanningImage(picturePath);
					Log.d("asdfghjkl--result", "结束！！！！！！！！！！！！！！！！！！！！");

					// if (result != null) {
					// Message m = handler.obtainMessage();
					// m.what = R.id.return_scan_result;
					// m.obj = result;
					// handler.sendMessage(m);
					// } else {
					// Message m = handler.obtainMessage();
					// m.what = R.id.restart_preview;
					// m.obj = "Scan failed!";
					// handler.sendMessage(m);
					// }
				}
			}).start();
		}
	}

	protected void scanningImage(String path) {
		if (TextUtils.isEmpty(path)) {
			handleDecode(null, null);
			// return null;
		}
		// DecodeHintType 和EncodeHintType
		Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
		hints.put(DecodeHintType.CHARACTER_SET, "utf-8"); // 设置二维码内容的编码
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true; // 先获取原大小
		scanBitmap = BitmapFactory.decodeFile(path, options);
		options.inJustDecodeBounds = false; // 获取新的大小

		int sampleSize = (int) (options.outHeight / (float) 200);

		if (sampleSize <= 0)
			sampleSize = 1;
		options.inSampleSize = sampleSize;
		scanBitmap = BitmapFactory.decodeFile(path, options);
		// scanBitmap.getWidth(), scanBitmap.getHeight(), new
		// int[scanBitmap.getWidth()*scanBitmap.getHeight()]
		RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap);
		BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
		QRCodeReader reader = new QRCodeReader();
		Result result = null;
		try {

			result = reader.decode(bitmap1, hints);

			Log.d("asdfghjkl", "进来扫描图片！");

			handleDecode(result, scanBitmap);

			// Toast.makeText(MipcaActivityCapture.this, "进来扫描图片！",
			// Toast.LENGTH_SHORT).show();

			// handleDecode(result, scanBitmap);

		} catch (NotFoundException e) {

			e.printStackTrace();

		} catch (ChecksumException e) {

			e.printStackTrace();

		} catch (FormatException e) {

			e.printStackTrace();

		}
		// return result;
	}

	// // 生成QR图
	// public Bitmap createImage(String str) {
	// try {
	// // 需要引入core包
	// QRCodeWriter writer = new QRCodeWriter();
	//
	// String text = str;
	//
	// Log.i("createImage", "生成的文本：" + text);
	// if (text == null || "".equals(text) || text.length() < 1) {
	// return null;
	// }
	//
	// // 把输入的文本转为二维码
	// BitMatrix martix = writer.encode(text, BarcodeFormat.QR_CODE,
	// 100, 100);
	//
	// System.out.println("w:" + martix.getWidth() + "h:"
	// + martix.getHeight());
	//
	// Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType,
	// String>();
	// hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
	// BitMatrix bitMatrix = new QRCodeWriter().encode(text,
	// BarcodeFormat.QR_CODE, 100, 100, hints);
	// int[] pixels = new int[100 * 100];
	// for (int y = 0; y < 100; y++) {
	// for (int x = 0; x < 100; x++) {
	// if (bitMatrix.get(x, y)) {
	// pixels[y * 100 + x] = 0xff000000;
	// } else {
	// pixels[y * 100 + x] = 0xffffffff;
	// }
	//
	// }
	// }
	//
	// Bitmap bitmap = Bitmap.createBitmap(100, 100,
	// Bitmap.Config.ARGB_8888);
	//
	// bitmap.setPixels(pixels, 0, 100, 0, 0, 100, 100);
	//
	// return bitmap;
	//
	// } catch (WriterException e) {
	// e.printStackTrace();
	// }
	// return null;
	// }
}