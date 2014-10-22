package com.joyproj.idwb;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public abstract class AbstractTitleBarActivity extends AbstractActivity {

	TextView textBack;
	TextView textTitle;
	TextView textRight;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getResource());
		//
		initTitlebar();
		//
		doSthOnCreateView();
	}

	/**
	 * 初始化标题栏
	 */
	private void initTitlebar() {
		textBack = (TextView) findViewById(R.id.textBack);
		textTitle = (TextView) findViewById(R.id.textTitle);
		textRight = (TextView) findViewById(R.id.textRight);
		
		textBack.setText(setBack());
		textTitle.setText(setTitle());
		textRight.setText(setRight());
		
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
	protected abstract void doSthOnCreateView();
	
	/**
	 * 设置标题
	 * @return
	 */
	protected abstract CharSequence setTitle();
	
	/**
	 * 设置做左边文字
	 * @return
	 */
	protected abstract CharSequence setBack();
	
	/**
	 * 设置左边按钮点击事件
	 */
	protected abstract void onBackClick(View v);
	
	/**
	 * 设置右边按钮
	 * @return
	 */
	protected abstract CharSequence setRight();

	/**
	 * 设置右边按钮点击事件
	 * @param v 右边按钮
	 */
	protected abstract void onRightClick(View v);
	
}
