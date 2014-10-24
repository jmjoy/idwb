package com.joyproj.idwb;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

public abstract class AbstractPowerfulActivity extends AbstractActivity {

	protected static final int UI_WAIT = 1;
	protected static final int UI_RESET = 2;
	protected static final int UI_BLUR = 3;
	protected static final int UI_CLEAR = 4;
	protected static final int UI_OTHER = 5;
	protected static final int UI_OTHER2 = 6;
	
	protected TextView textBack;
	protected TextView textTitle;
	protected TextView textRight;
	protected ProgressBar progressBar;
	protected ViewGroup container;
	protected View base;
	
	protected Handler handler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 
		beginOnCreateView();
		// 设置布局文件
		setContentView(R.layout.act_powerful);
		// 获取所有View
		textBack = (TextView) findViewById(R.id.textBack);
		textTitle = (TextView) findViewById(R.id.textTitle);
		textRight = (TextView) findViewById(R.id.textRight);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		container = (ViewGroup) findViewById(R.id.container);
		base = findViewById(R.id.base);
		// 设置TitleBar
		textBack.setText(returnBackText());
		textTitle.setText(returnTitleText());
		textRight.setText(returnRightText());
		// 设置监听器
		textBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackClick(v);
			}
		});
		textRight.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onRightClick(v);
			}
		});
		//  动态加载主界面内容
		View child = getLayoutInflater().inflate(returnChildResource(), null);
		container.addView(child);
		// 创建Handler
		handler = returnHandler();
		// 后续
		doSthOnCreateView();
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
						container.setVisibility(View.GONE);
						progressBar.setVisibility(View.VISIBLE);
						uiWait();
						break;
						
					case UI_RESET:
						container.setVisibility(View.VISIBLE);
						progressBar.setVisibility(View.GONE);
						uiReset();
						break;
						
					case UI_BLUR:
						base.setAlpha(0.5f);
						progressBar.setVisibility(View.VISIBLE);
						uiWait();
						break;
						
					case UI_CLEAR:
						base.setAlpha(1f);
						progressBar.setVisibility(View.GONE);
						uiReset();
						break;
						
					case UI_OTHER:
						uiOther();
						break;
						
					case UI_OTHER2:
						uiOther2();
						break;
				}
			}
		};
	}

	protected CharSequence returnBackText() {
		return "< 返回";
	}
	
	protected abstract CharSequence returnTitleText();
	
	protected abstract CharSequence returnRightText();
	
	protected void onBackClick(View v){
		finish();
	}
	
	protected abstract void onRightClick(View v);
	
	protected abstract void doSthOnCreateView();
	
	protected abstract int returnChildResource();
	
	protected abstract void uiWait();
	
	protected abstract void uiReset();
	
	protected abstract void uiOther();
	
	protected void uiOther2(){
	}
	
	protected abstract void beginOnCreateView();
	
}
