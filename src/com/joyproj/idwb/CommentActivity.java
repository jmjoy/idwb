package com.joyproj.idwb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.joyproj.idwb.util.EmotionUtil;

public class CommentActivity extends AbstractPowerfulActivity implements
		AdapterView.OnItemClickListener {

	ImageView imageAvatar;
	TextView textName;
	TextView textTime;
	TextView textContent;
	GridView gridEmotions;
	EditText editComment;
	View scrollEmotion;

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
		editComment = (EditText) findViewById(R.id.editComment);
		gridEmotions = (GridView) findViewById(R.id.gridEmotions);
		scrollEmotion = findViewById(R.id.scrollEmotion);
		// 初始化
		imageAvatar.setImageBitmap(avatar);
		textName.setText(name);
		textTime.setText(time);
		textContent.setText(EmotionUtil.formatContent(content, this));
		// 表情框架初始化
		SimpleAdapter adapter = new SimpleAdapter(this, returnEmotionData(),
				R.layout.pic, new String[] { "emotion" },
				new int[] { R.id.imageEmotion });
		gridEmotions.setAdapter(adapter);
		// 监听器
		gridEmotions.setOnItemClickListener(this);
	}

	/**
	 * 在gridview中添加表情组
	 */
	private List<Map<String, Object>> returnEmotionData() {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		try {
			Class clazz = Class.forName("com.joyproj.idwb.R$drawable");
			for (int i = 1; i <= 50; i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				String index = (i < 10) ? ("0" + i) : ("" + i);
				map.put("emotion",
						clazz.getField("emotion_" + index).getInt(null));
				data.add(map);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return data;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
		String str = editComment.getText().toString() + EmotionUtil.EMOTION_MAP[pos];
		if (str.length() >= 50) {
			return;
		}
		editComment.setText(EmotionUtil.formatContent(str, this));
		editComment.requestFocus();
		editComment.setSelection(str.length());
	}

	/**
	 * 按下表情按钮
	 * @param v
	 */
	public void onClickBtnEmotion(View v){
		int visibility =  scrollEmotion.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
		scrollEmotion.setVisibility(visibility);
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
