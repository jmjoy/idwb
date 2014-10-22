package com.joyproj.idwb;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

public abstract class AbstractProgressbarActivity extends AbstractActivity {
	
	protected ProgressBar progressbar;
	protected View viewBase;
	
	protected static final int UI_WAIT = 0;
	protected static final int UI_RESET = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 
		setContentView(setResource());
		// 
		progressbar = (ProgressBar) findViewById(R.id.progressBar);
		viewBase = findViewById(R.id.viewBase);
		//
		doSthOnCreateView();
	}

	/**
	 * 设置布局文件
	 * @return
	 */
	protected abstract int setResource();
	
	/**
	 * 后续
	 */
	protected abstract void doSthOnCreateView();
	
}
