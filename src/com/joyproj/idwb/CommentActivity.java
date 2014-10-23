package com.joyproj.idwb;

import com.joyproj.idwb.util.EmotionUtil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class CommentActivity extends AbstractPowerfulActivity {

	ImageView imageAvatar;
	TextView textName;
	TextView textTime;
	TextView textContent;
	
	private Bitmap avatar;
	private String name;
	private String time;
	private String content;
	
	@Override
	protected CharSequence returnTitleText() {
		return "微博";
	}

	@Override
	protected CharSequence returnRightText() {
		return "";
	}

	@Override
	protected void onRightClick(View v) {
	}

	@Override
	protected void doSthOnCreateView() {
		// 获取
		imageAvatar = (ImageView) findViewById(R.id.imageAvatar);
		textName = (TextView) findViewById(R.id.textName);
		textTime = (TextView) findViewById(R.id.textTime);
		textContent = (TextView) findViewById(R.id.textContent);
		// 初始化
		imageAvatar.setImageBitmap(avatar);
		textName.setText(name);
		textTime.setText(time);
		textContent.setText(EmotionUtil.formatContent(content, this));
	}

	@Override
	protected int returnChildResource() {
		return R.layout.act_child_comment;
	}

	@Override
	protected void uiWait() {
	}

	@Override
	protected void uiReset() {
	}

	@Override
	protected void uiOther() {
	}

	@Override
	protected void beginOnCreateView() {
		Intent intent = getIntent();
		name = intent.getStringExtra("name");
		time = intent.getStringExtra("time");
		content = intent.getStringExtra("content");
		//
		byte[] b = intent.getByteArrayExtra("avatar");
		avatar = BitmapFactory.decodeByteArray(b, 0, b.length);
	}

}
