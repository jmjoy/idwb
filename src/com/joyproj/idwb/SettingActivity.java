package com.joyproj.idwb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.joyproj.idwb.data.UserData;
import com.joyproj.idwb.helper.PoolHelper;

public class SettingActivity extends AbstractPowerfulActivity {

	Spinner spinnerWeibo;
	Spinner spinnerUser;
	
	Activity actMain;
	
	private ArrayAdapter<String> adapter;
	
	@Override
	protected CharSequence returnTitleText() {
		return "设置";
	}

	@Override
	protected CharSequence returnRightText() {
		return "确定 > ";
	}

	@Override
	protected void onRightClick(View v) {
		int weiboListrows = spinnerWeibo.getSelectedItemPosition() * 5 + 10;
		int userListrows = spinnerUser.getSelectedItemPosition() * 5 + 10;
		// 更新到sharedPreferences
		SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putInt("weiboListrows", weiboListrows);
		editor.putInt("perListrows", userListrows);
		// 成功提示
		if(editor.commit()){
			Toast.makeText(this, "设置成功", Toast.LENGTH_SHORT).show();
			UserData.weiboListrows = weiboListrows;
			UserData.perListrows = userListrows;
			finish();
		}
		// 失败提示
		else{
			Toast.makeText(this, "设置失败", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void doSthOnCreateView() {
		// 获取 MainActivity 对象
		actMain = (Activity) getIntent().getSerializableExtra("main");
		// 初始化
		spinnerWeibo = (Spinner) findViewById(R.id.spinnerWeibo);
		spinnerUser = (Spinner) findViewById(R.id.spinnerUser);
		
		String[] items = {"10", "15", "20", "25"}; 
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerWeibo.setAdapter(adapter);	
		spinnerUser.setAdapter(adapter);
		
		spinnerWeibo.setSelection((UserData.weiboListrows - 10) / 5);
		spinnerUser.setSelection((UserData.perListrows - 10) / 5);
	}

	@Override
	protected int returnChildResource() {
		return R.layout.act_child_setting;
	}

	@Override
	protected void uiWait() {
	}

	@Override
	protected void uiReset() {
	}

	/**
	 * 注销
	 * @param v
	 */
	public void logout(View v){
		// 清除登录信息
		SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putString("id", null);
		editor.putString("password", null);
		editor.commit();
		UserData.id = "";
		UserData.password = "";
		// 把主界面删除
		PoolHelper poolHelper = PoolHelper.getInstance();
		poolHelper.activity.finish();
		// 返回登录页面
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	protected void beginOnCreateView() {
	}

	@Override
	protected void uiOther() {
		// TODO Auto-generated method stub
		
	}
}
