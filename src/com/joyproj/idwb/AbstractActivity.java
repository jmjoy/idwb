package com.joyproj.idwb;

import android.app.Activity;
import android.os.Handler;

public abstract class AbstractActivity extends Activity {
	
	protected Handler mainHandler = new Handler();

	/**
	 * 
	 * @return
	 */
	public Handler getMainHandler() {
		return mainHandler;
	}
	
}
