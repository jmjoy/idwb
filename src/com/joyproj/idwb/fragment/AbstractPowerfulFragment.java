package com.joyproj.idwb.fragment;

import com.joyproj.idwb.R;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public abstract class AbstractPowerfulFragment extends AbstractFragment{

	protected static final int UI_WAIT = 1;
	protected static final int UI_RESET = 2;	
	protected static final int UI_OTHER = 3;
	
	protected TextView textBack;
	protected TextView textTitle;
	protected ImageView imageRight;
	protected ProgressBar progressBar;
	protected View base;	
	
	protected Handler handler;	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//
		View view = inflater.inflate(getResource(), container, false);
		// 
		progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
		base = view.findViewById(R.id.base);
		//
		initTitlebar(view);
		// 初始化Handler
		handler = returnHandler();
		//
		doSthOnCreateView(view);
		return view;
	} 

	/**
	 * 初始化标题栏
	 */
	private void initTitlebar(View view) {
		textBack = (TextView) view.findViewById(R.id.textBack);
		textTitle = (TextView) view.findViewById(R.id.textTitle);
		imageRight = (ImageView) view.findViewById(R.id.imageRight);
		
		textBack.setText(setBack());
		textTitle.setText(setTitle());
		
		textBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackClick(v);
			}
		});
		imageRight.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onRightClick(v);
			}
		});
	}

	/**
	 * 设置做左边文字
	 * @return
	 */
	protected CharSequence setBack(){
		return getString(R.string.app_name);
	}
	
	/**
	 * 设置左边按钮点击事件
	 */
	protected void onBackClick(View v){
	}
	
	/**
	 * 设置资源文件
	 * @return
	 */
	protected abstract int getResource();
	
	/**
	 * 
	 * @param view
	 */
	protected abstract void doSthOnCreateView(View view);
	
	/**
	 * 设置标题
	 * @return
	 */
	protected abstract CharSequence setTitle();	
	
	/**
	 * 设置右边按钮点击事件
	 * @param v 右边按钮
	 */
	protected abstract void onRightClick(View v);

	/**
	 * 选择性重写 用于handler发送UI_OTHRT消息时执行的动作
	 */
	protected void uiOther(){
	}
	
	/**
	 * 创建Handler
	 * @return
	 */
	private Handler returnHandler(){
		return new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
					case UI_WAIT:
						base.setVisibility(View.GONE);
						progressBar.setVisibility(View.VISIBLE);
						break;
						
					case UI_RESET:
						base.setVisibility(View.VISIBLE);
						progressBar.setVisibility(View.GONE);
						break;
						
					case UI_OTHER:
						uiOther();
						break;
				}
			}
		};
	}
	
	
}
