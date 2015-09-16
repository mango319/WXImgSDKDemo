package com.qq.wx.img.demo;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.qq.wx.img.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;


public class ResultActivity extends Activity {

	//关于结果
	private String mResUrl;
	private String mResMD5;
	private String mResPicDesc;
	
	private Bitmap mResBitmap;
			
	public Handler handler = new Handler(new Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			if (1 == msg.what) {
				setImgView();
			}
			return false;
		}
	});
			
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle bundle=getIntent().getExtras();
		if (true == bundle.getBoolean("ret")) {
			mResUrl = bundle.getString("url");
			mResMD5 = bundle.getString("md5");
			mResPicDesc = bundle.getString("picDesc");
			resultUI(true);
		}
		else {
			resultUI(false);
		}
	}
	
	public void resultUI(boolean isFound) {
		setContentView(R.layout.result_demo);
		TextView md5tv = (TextView) findViewById(R.id.recmd5);
		TextView picDesctv = (TextView) findViewById(R.id.recPicDesc);
		
		if (isFound) {
			new Thread(getBitMapThread).start();
		
			md5tv.setText("MD5: " + mResMD5);
			picDesctv.setText("picDesc: " + mResPicDesc);
		}
		else {
			TextView resTv = (TextView) findViewById(R.id.resText);
			resTv.setText("未找到对应图片");
			md5tv.setText("MD5: ");
			picDesctv.setText("picDesc:");
		}
	}
	
	protected void onResume(){
		super.onResume();
		
		 if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
			  setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			 }
	}
	
	public void setImgView() {
		if (mResBitmap != null) {
			ImageView imgv = (ImageView) findViewById(R.id.resImg);
			imgv.setImageBitmap(mResBitmap);
		}
	}
	public Bitmap getBitMap(String url) { 
		URL myFileUrl = null; 
		Bitmap bitmap = null; 
		try { 
			myFileUrl = new URL(url); 
		} catch (MalformedURLException e) { 
			e.printStackTrace(); 
		} 
		try { 
			HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection(); 
			conn.setDoInput(true); 
			conn.connect(); 
			InputStream is = conn.getInputStream(); 
			bitmap = BitmapFactory.decodeStream(is); 
			is.close(); 
		} catch (IOException e) { 
			e.printStackTrace(); 
		} 
		return bitmap; 
	} 

	Runnable getBitMapThread = new Runnable(){
	    @Override
	    public void run() {
	        //
	        // TODO: http request.
	        //
	    	if (mResUrl != null) {
	    		mResBitmap = getBitMap(mResUrl);
	    	}
	        Message msg = new Message();
	        msg.what = 1;
	        handler.sendMessage(msg);
	    }
	};
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if(keyCode == KeyEvent.KEYCODE_BACK) {
	    	Intent it = new Intent(this, MainActivity.class);
	   		startActivity(it);
	        // 监控返回键
	    	finish();
	    }
	    return super.onKeyDown(keyCode, event);
	}
}


