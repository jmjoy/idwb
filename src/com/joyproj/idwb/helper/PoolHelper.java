package com.joyproj.idwb.helper;

import android.app.Activity;

/**
 * 单例池
 * @author JM_Joy
 *
 */
public class PoolHelper {

	// 获取 MainActivity 对象
	public Activity activity;
	
	private static PoolHelper instance = new PoolHelper();
	
	private PoolHelper(){
	}
	
	public static PoolHelper getInstance(){
		return instance;
	}
}
