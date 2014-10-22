package com.joyproj.idwb;

import com.joyproj.idwb.data.UrlData;
import com.joyproj.idwb.helper.HttpHelper;
import com.joyproj.idwb.util.UrlCodeUtil;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AbstractProgressbarActivity {

	private static final int VAL_SAME_OK = 100;
	private static final int VAL_SAME_NAME = -100;
	private static final int VAL_SAME_EMAIL = -101;
	private static final int VAL_SAME_UNKNOW = -102;
	
	
	EditText inputName;
	EditText inputEmail;
	EditText inputPassword;
	EditText inputRepassword;
	Button btnRegister;
	
	private Handler handler;
	
	@Override
	protected int setResource() {
		return R.layout.act_register;
	}

	@Override
	protected void doSthOnCreateView() {
		inputName = (EditText) findViewById(R.id.inputName);
		inputEmail = (EditText) findViewById(R.id.inputEmail);
		inputPassword = (EditText) findViewById(R.id.inputPassword);
		inputRepassword = (EditText) findViewById(R.id.inputRepassword);
		btnRegister = (Button) findViewById(R.id.btnRegister);	
		// 设置UI的Handler
		handler = returnHandler();
	}		
	
	/**
	 * 注册按钮点击事件
	 * @param v
	 */
	public void register(View v){
		// 获取用户输入
		final String name = inputName.getText().toString();
		final String email = inputEmail.getText().toString();
		final String password = inputPassword.getText().toString();
		String repassword = inputRepassword.getText().toString();		
		// 检验合法性
		if(!validate(name, email, password, repassword)){
			return;
		}
		// UI 等待状态
		handler.sendEmptyMessage(UI_WAIT);
		// 联网注册
		new Thread(){
			@Override
			public void run() {
				String args = "name=" + UrlCodeUtil.encode(name) + "&email=" + email + "&password=" + password;
				HttpHelper httpUtil = new HttpHelper(UrlData.REGISTER, RegisterActivity.this);
				String res = httpUtil.post(args);
				System.out.println(res);
				// 检测注册成功与否
				resHandle(res);
				// UI重置 
				handler.sendEmptyMessage(UI_RESET);
			};
		}.start();
	}
	
	/**
	 * 检测传入参数合法性
	 */
	private boolean validate(String name, String email, String password, String repassword) {
		if(!name.matches("^[\\u4E00-\\u9FA5\\w\\-]{3,8}$")){
			Toast.makeText(this, "用户名必须是3-8非特殊字符", Toast.LENGTH_LONG).show();
			return false;
		}
		if(!email.matches("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$")){
			Toast.makeText(this, "邮箱不合法", Toast.LENGTH_LONG).show();
			return false;
		}
		if(!password.matches("^\\S{4,12}$")){
			Toast.makeText(this, "密码必须是4-12个字符", Toast.LENGTH_LONG).show();
			return false;
		}
		if(!password.equals(repassword)){
			Toast.makeText(this, "两次密码不一致", Toast.LENGTH_LONG).show();
			return false;	
		}
		return true;
	}	
	
	/**
	 * 根据返回结果做出处理
	 * 1.ok		-1.用户名重复	-2.邮箱重复		-3.未知
	 * @param res
	 */
	private void resHandle(String res){
		if(res == null || "".equals(res)){
			return;
		}
		if(res.equals("1")){
			handler.sendEmptyMessage(VAL_SAME_OK);
			finish();
		}
		else if(res.equals("-1")){
			handler.sendEmptyMessage(VAL_SAME_NAME);
		}
		else if(res.equals("-2")){
			handler.sendEmptyMessage(VAL_SAME_EMAIL);
		}
		else if(res.equals("-3")){
			handler.sendEmptyMessage(VAL_SAME_UNKNOW);
		}
	}
	
	/**
	 * 返回一个控制UI的Handler
	 * @return
	 */
	private Handler returnHandler() {
		return new Handler(){
			@Override
			public void handleMessage(Message msg){
				switch (msg.what) {
					case UI_WAIT:
						viewBase.setAlpha(0.5f);
						progressbar.setVisibility(View.VISIBLE);
						inputName.setEnabled(false);
						inputEmail.setEnabled(false);
						inputPassword.setEnabled(false);
						inputRepassword.setEnabled(false);
						btnRegister.setEnabled(false);
						break;
						
					case UI_RESET:
						viewBase.setAlpha(1f);
						progressbar.setVisibility(View.GONE);
						inputName.setEnabled(true);
						inputEmail.setEnabled(true);
						inputPassword.setEnabled(true);
						inputRepassword.setEnabled(true);
						btnRegister.setEnabled(true);
						break;
						
					case VAL_SAME_OK:
						Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
						break;
						
					case VAL_SAME_NAME:
						Toast.makeText(RegisterActivity.this, "sorry, 用户名已注册!", Toast.LENGTH_LONG).show();
						break;
						
					case VAL_SAME_EMAIL:
						Toast.makeText(RegisterActivity.this, "sorry, 邮箱已注册!", Toast.LENGTH_LONG).show();						
						break;
						
					case VAL_SAME_UNKNOW:
						Toast.makeText(RegisterActivity.this, "太恐怖了, 未知错误!", Toast.LENGTH_LONG).show();						
						break;
				}
			}
		};
	}	
	
}
