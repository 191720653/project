package com.zehao.fragment;

import com.zehao.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class Page1 extends Fragment implements OnClickListener{
	
	/** 
     * 设置图片轮播的回调 
     * 
     */  
    public interface MyPageChangeListener  
    {  
        void onFOneBtnClick();  
    }  

//	TextView textView;
//	SlideShowImage showView;
	View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.page1, container, false);
//		textView = (TextView)view.findViewById(R.id.textView1);
//		textView.setOnClickListener(this);
//		showView = (SlideShowImage)view.findViewById(R.id.slideshowView);
//		showView.setOnClickListener(this);
		return view;

	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		System.out.println("---------------"+v.getId());
	}

}
