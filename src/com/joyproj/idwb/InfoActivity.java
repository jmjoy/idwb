package com.joyproj.idwb;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.joyproj.idwb.data.UrlData;
import com.joyproj.idwb.data.UserData;
import com.joyproj.idwb.helper.HttpHelper;
import com.joyproj.idwb.util.ImageUtil;

public class InfoActivity extends AbstractPowerfulActivity implements View.OnClickListener{

	private CharSequence title;
	private String uid;
	
	private String[] resArr;
	
	ImageView imageAvatar;
	TextView textName;
	TextView textMood;
	TextView textFans;
	TextView textAttention;
	TextView textWeibo;
	ToggleButton toggleAttend;
	TextView textCtime;
	TextView textSex;	
	TextView textBirth;
	TextView textResidence;
	TextView textIntro;
	View relaAttend;
	View linearFans;
	View linearAttention;
	View linearWeibo;
	
	@Override
	protected void doSthOnCreateView() {
		// 获取
		imageAvatar = (ImageView) findViewById(R.id.imageAvatar);
		textName = (TextView) findViewById(R.id.textName);
		textMood = (TextView) findViewById(R.id.textMood);
		textFans = (TextView) findViewById(R.id.textFans);
		textAttention = (TextView) findViewById(R.id.textAttention);
		textWeibo = (TextView) findViewById(R.id.textWeibo);
		toggleAttend =  (ToggleButton) findViewById(R.id.toggleAttend);
		textCtime = (TextView) findViewById(R.id.textCtime);
		textSex = (TextView) findViewById(R.id.textSex);
		textBirth = (TextView) findViewById(R.id.textBirth);
		textResidence = (TextView) findViewById(R.id.textResidence);
		textIntro = (TextView) findViewById(R.id.textIntro);
		linearFans = findViewById(R.id.linearFans);
		linearAttention = findViewById(R.id.linearAttention);
		linearFans = findViewById(R.id.linearFans);
		linearWeibo = findViewById(R.id.linearWeibo);
		//
		relaAttend = findViewById(R.id.relaAttend);
//		if(UserData.id.equals(uid)){
			relaAttend.setVisibility(View.GONE);
//		}
		// 监听器
		imageAvatar.setOnClickListener(this);
		linearFans.setOnClickListener(this);
		linearAttention.setOnClickListener(this);
		linearWeibo.setOnClickListener(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		//
		initData();
	}
	
	/**
	 * 初始数据
	 */
	private void initData() {
		handler.sendEmptyMessage(UI_WAIT);
		new Thread(){
			@Override
			public void run() {
				//
				loadAvatar();
				//
				HttpHelper httpHelper = new HttpHelper(UrlData.INFO, InfoActivity.this);
				String args = "id=" + UserData.id + "&password=" + UserData.password + "&uid=" + uid;
				String res = httpHelper.post(args);
				// 处理结果
				handleRes(res);
				handler.sendEmptyMessage(UI_RESET);
			};
		}.start();
	}

	/**
	 * 
	 */
	private void loadAvatar(){
		String url = UrlData.AVATAR + "?id=" + uid;
		ImageUtil.loadImgByNetwork(url, mainHandler, new ImageUtil.Own() {
			@Override
			public void ok(Drawable drawable) {
				imageAvatar.setImageDrawable(drawable);
			}

			@Override
			public void failed(Exception e) {
			}
		});
	}
	
	/**
	 * 处理结果
	 * @param res
	 */
	private void handleRes(String res){
		if(res == null || "".equals(res)){
			return;
		}
		resArr = res.split("\\!\\@\\*\\&\\^\\%");
		
		System.err.println(resArr.length);
		
		if(resArr.length != 11){
			return;
		}
		handler.sendEmptyMessage(UI_OTHER);
	}
	
	@Override
	protected void uiOther() {
		// name,ctime,mood,sex,birth,residence,intro,
		// fas_num,attention_num,weibo_num,attend
		//
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy年MM月dd日");
		//
		textName.setText(resArr[0]);
		//
		long ctime = Long.parseLong(resArr[1]) * 1000;
		textCtime.setText(sdf.format(new Date(ctime)));
		//
		textMood.setText(resArr[2]);
		//
		if("0".equals(resArr[3])){
			textSex.setText("保密");
		}else if("1".equals(resArr[3])){
			textSex.setText("男");
		}else if("2".equals(resArr[3])){
			textSex.setText("女");
		}
		//
		long birth = Long.parseLong(resArr[4]) * 1000;
		textBirth.setText(sdf.format(new Date(birth)));
		//
		textResidence.setText(resArr[5]);
		//
		textIntro.setText(resArr[6]);
		//
		textFans.setText(resArr[7]);
		//
		textAttention.setText(resArr[8]);
		//
		textWeibo.setText(resArr[9]);
		//
		if("1".equals(resArr[10])){
			toggleAttend.setChecked(true);
		} else{
			toggleAttend.setChecked(false);			
		}
	}	
	
	@Override
	public void onClick(View v) {
		if(v == linearFans){
			// TODO 粉丝
		}else if(v == linearAttention){
			// TODO 关注	
		}else if(v == linearWeibo){
			// TODO 微博
		}else if(v == imageAvatar){
			Intent intent = new Intent(this, AvatarActivity.class);
			intent.putExtra("uid", uid);
			startActivity(intent);
		}
	}		
	
	@Override
	protected void beginOnCreateView() {
		title = getIntent().getCharSequenceExtra("title");
		uid = (String) getIntent().getCharSequenceExtra("uid");
	}
	
	@Override
	protected CharSequence returnTitleText() {
		return title;
	}

	@Override
	protected CharSequence returnRightText() {
		if(UserData.id.equals(uid)){
			return "修改 > ";
		}
		return "";
	}

	@Override
	protected void onRightClick(View v) {
		if(!UserData.id.equals(uid)){
			return;
		}
		Intent intent = new Intent(this, ModifyActivity.class);
		// name,ctime,mood,sex,birth,residence,intro,
		// fas_num,attention_num,weibo_num,attend
		// mood 2 sex 3 birth 4 residence 5 intro 6
		intent.putExtra("mood", resArr[2]);
		intent.putExtra("sex", resArr[3]);
		intent.putExtra("birth", resArr[4]);
		intent.putExtra("residence", resArr[5]);
		intent.putExtra("intro", resArr[6]);
		startActivity(intent);
	}

	@Override
	protected int returnChildResource() {
		return R.layout.act_child_info;
	}

	@Override
	protected void uiWait() {
		textRight.setVisibility(View.GONE);
	}

	@Override
	protected void uiReset() {
		textRight.setVisibility(View.VISIBLE);		
	}

}
