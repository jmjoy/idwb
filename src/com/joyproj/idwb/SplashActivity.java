package com.joyproj.idwb;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.joyproj.idwb.data.UrlData;
import com.joyproj.idwb.data.UserData;
import com.joyproj.idwb.helper.HttpHelper;

public class SplashActivity extends AbstractActivity {

	SharedPreferences sharedPreferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_splash);
		// 基础的
		sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
		// 获取本地配置
		getConfig();
		// 
		next();
	}

	/**
	 * 
	 */
	private void getConfig(){
		// id和密码
		UserData.id = sharedPreferences.getString("id", "");
		UserData.password = sharedPreferences.getString("password", "");
		// 设置每页显示条目数
		UserData.weiboListrows = sharedPreferences.getInt("weiboListrows", UserData.DEFAULT_LISTROWS);
		UserData.perListrows = sharedPreferences.getInt("perListrows", UserData.DEFAULT_LISTROWS);		
	}
	
	/**
	 * 
	 */
	private void next() {
		new Thread(){
			@Override
			public void run() {
				// 先睡一会
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				// 没有登录信息 跳转到登录页面
				if("".equals(UserData.id) || "".equals(UserData.password)){
					Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
					startActivity(intent);
				}
				else{
					// 联网检验登录信息
					String args = "id=" + UserData.id + "&password=" + UserData.password;
					HttpHelper httpUtil = new HttpHelper(UrlData.SPLASH, SplashActivity.this);
					String res = httpUtil.post(args);
					// 登录成功, 跳转到主界面
					if("1".equals(res)){
						Intent intent = new Intent(SplashActivity.this, MainActivity.class);
						startActivity(intent);
					}					
				}
				//
				finish();
			};
		}.start();
	}
	

}
