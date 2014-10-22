package com.joyproj.idwb;

import com.joyproj.idwb.data.UrlData;
import com.joyproj.idwb.data.UserData;
import com.joyproj.idwb.helper.HttpHelper;
import com.joyproj.idwb.util.MD5Util;
import com.joyproj.idwb.util.UrlCodeUtil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 他妈的为什么就是不能够完全退出进程呢???
 * 
 * @author JM_Joy
 *
 */
public class LoginActivity extends AbstractProgressbarActivity implements View.OnClickListener{

	private static final int VAL_FAILED = -100;
	
	EditText inputName;
	EditText inputPassword;
	TextView textgotoRegister;
	Button btnLogin;
	
	Handler handler;
	
	@Override
	protected int setResource() {
		return R.layout.act_login;
	}

	@Override
	protected void doSthOnCreateView() {
		// 初始化组件
		textgotoRegister = (TextView) findViewById(R.id.textgotoRegister);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		inputName = (EditText) findViewById(R.id.inputName);
		inputPassword = (EditText) findViewById(R.id.inputPassword);
		// 设置监听器
		textgotoRegister.setOnClickListener(this);
		btnLogin.setOnClickListener(this);
		// 设置Handler
		handler = returnHandler();
	}
	
	@Override
	public void onClick(View v) {
		// 点击去注册吧
		if(v == textgotoRegister){
			// 跳转到注册界面
			Intent intent = new Intent(this, RegisterActivity.class);
			startActivity(intent);
		}
		// 点击登录按钮
		else if(v == btnLogin){
			login();
		}
	}	
	
	/**
	 * 登录请求
	 */
	private void login(){
		final String name = inputName.getText().toString().trim();
		final String password = inputPassword.getText().toString().trim();
		if("".equals(name) || "".equals(password)){
			return;
		}
		// UI : 等待状态
		handler.sendEmptyMessage(UI_WAIT);
		// 联网提交登录信息
		new Thread(){
			@Override
			public void run() {
				// 加密密码
				String md5psd = MD5Util.md5(password);
				String args = "name=" + UrlCodeUtil.encode(name) + "&password=" + md5psd;
				// 联网提交数据
				HttpHelper httpUtil = new HttpHelper(UrlData.LOGIN, LoginActivity.this);
				String res = httpUtil.post(args);
				// 处理登录是否成功
				handleLoginRes(res, name, md5psd);
				// UI : 恢复
				handler.sendEmptyMessage(UI_RESET);
			};
		}.start();
	}
	
	/**
	 * 处理返回结果
	 * 1.登录成功  0.用户名或密码错误
	 * @param res
	 * @param md5psd 
	 * @param name 
	 */
	private void handleLoginRes(String res, String name, String md5psd) {
		if(res == null){
			return;
		}
		else if("0".equals(res)){
			handler.sendEmptyMessage(VAL_FAILED);
			return;
		}
		else{
			//实例化SharedPreferences对象（第一步） 
			SharedPreferences mySharedPreferences= getSharedPreferences("user", Context.MODE_PRIVATE); 
			//实例化SharedPreferences.Editor对象（第二步） 
			SharedPreferences.Editor editor = mySharedPreferences.edit(); 
			//用putString的方法保存数据 
			editor.putString("id", res);
			editor.putString("password", md5psd);
			//提交当前数据 
			editor.commit();
			// 储存到静态变量
			UserData.id = res;
			UserData.password = md5psd;
			// 转到主界面
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			finish();
		}
	}
	
	/**
	 * 返回一个控制UI的Handler
	 * @return
	 */
	private Handler returnHandler() {
		return new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
					case UI_WAIT:
						viewBase.setAlpha(0.5f);
						progressbar.setVisibility(View.VISIBLE);
						inputName.setEnabled(false);
						inputPassword.setEnabled(false);
						break;
						
					case UI_RESET:
						viewBase.setAlpha(1f);
						progressbar.setVisibility(View.GONE);
						inputName.setEnabled(true);
						inputPassword.setEnabled(true);
						break;
					case VAL_FAILED:
						Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_LONG).show();
						break;
				}
			}
		};
	}
	
}
