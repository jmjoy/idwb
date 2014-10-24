package com.joyproj.idwb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.joyproj.idwb.adapter.CommentSimpleAdapter;
import com.joyproj.idwb.data.DataDealer;
import com.joyproj.idwb.data.UrlData;
import com.joyproj.idwb.data.UserData;
import com.joyproj.idwb.helper.HttpHelper;
import com.joyproj.idwb.util.DateUtil;
import com.joyproj.idwb.util.EmotionUtil;
import com.joyproj.idwb.util.UrlCodeUtil;
import com.joyproj.idwb.widget.AutoListView;

public class CommentActivity extends AbstractPowerfulActivity implements
		AdapterView.OnItemClickListener {

	ImageView imageAvatar;
	TextView textName;
	TextView textWid;
	TextView textTime;
	TextView textContent;
	GridView gridEmotions;
	EditText editComment;
	AutoListView listComment;
	View scrollEmotion;
	FrameLayout frameComment;

	private Bitmap avatar;
	private String name;
	private String wid;
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
		textWid = (TextView) findViewById(R.id.textWid);
		textName = (TextView) findViewById(R.id.textName);
		textTime = (TextView) findViewById(R.id.textTime);
		textContent = (TextView) findViewById(R.id.textContent);
		editComment = (EditText) findViewById(R.id.editComment);
		gridEmotions = (GridView) findViewById(R.id.gridEmotions);
		listComment = (AutoListView) findViewById(R.id.listComment);
		scrollEmotion = findViewById(R.id.scrollEmotion);
		frameComment = (FrameLayout) findViewById(R.id.frameComment);
		// 初始化
		imageAvatar.setImageBitmap(avatar);
		textName.setText(name);
		textTime.setText(time);
		textWid.setText(wid);
		textContent.setText(EmotionUtil.formatContent(content, this));
		// 表情框架初始化
		SimpleAdapter adapter = new SimpleAdapter(this, returnEmotionData(),
				R.layout.pic, new String[] { "emotion" },
				new int[] { R.id.imageEmotion });
		gridEmotions.setAdapter(adapter);
		// 监听器
		gridEmotions.setOnItemClickListener(this);
		//
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		initData();
	} 

	/**
	 * 初始化评论
	 */
	private void initData() {
		String[] from = {"id", "avatar", "name", "ctime", "content"};
		int[] to = {R.id.textId, R.id.imageAvatar, R.id.textName, R.id.textTime, R.id.textContent};
		DataDealer dealer = new DataDealer(this, listComment, R.layout.comment, CommentSimpleAdapter.class, from, to);
		String args = "wid=" + wid;
		dealer.setHttpDetail(UrlData.COMMENT_LIST, args, UserData.weiboListrows, new DataDealer.Own() {
			@Override
			public Object[] dealRes(Object[] fields) {
				return new Object[]{fields[0], R.drawable.avatar_2, fields[2], DateUtil.format((String) fields[3]), "*" + fields[4]};
			}
		});
		dealer.work();
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
	
	/**
	 * 按下发布按钮
	 * @param v
	 */
	public void onClickBtnComment(View v){
		final String comment = editComment.getText().toString();
		final String wid = textWid.getText().toString();
		if(comment == null || "".equals(comment.trim())){
			return;
		}
		// UI : 等待
		handler.sendEmptyMessage(UI_BLUR);
		// 联网提交
		new Thread(){
			@Override
			public void run() {
				HttpHelper helper = new HttpHelper(UrlData.COMMENT, CommentActivity.this);
				String args = "id=" + UserData.id + "&password=" + UserData.password + "&wid=" + wid + "&comment=" + UrlCodeUtil.encode(comment);
				String res = helper.post(args);
				handleRes(res);
				handler.sendEmptyMessage(UI_CLEAR);
			};
		}.start();
	}
	
	private void handleRes(String res){
		if("1".equals(res)){
			handler.sendEmptyMessage(UI_OTHER);
		}else{
			handler.sendEmptyMessage(UI_OTHER2);
		}
	}
	
	@Override
	protected int returnChildResource() {
		return R.layout.act_child_comment;
	}

	@Override
	protected void uiWait() {
		scrollEmotion.setVisibility(View.GONE);
		disableEnableControls(false, frameComment);
	}
	
	@Override
	protected void uiReset() {
		disableEnableControls(true, frameComment);		
	}

	@Override
	protected void uiOther() {
		Toast.makeText(CommentActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
		editComment.setText("");
	}	
	
	@Override
	protected void uiOther2() {
		Toast.makeText(CommentActivity.this, "评论失败", Toast.LENGTH_SHORT).show();		
	}

	/**
	 * 控制所有的组件的enable
	 * @param enable
	 * @param vg
	 */
	private void disableEnableControls(boolean enable, ViewGroup vg){
	    for (int i = 0; i < vg.getChildCount(); i++){
	       View child = vg.getChildAt(i);
	       child.setEnabled(enable);
	       if (child instanceof ViewGroup){ 
	          disableEnableControls(enable, (ViewGroup)child);
	       }
	    }
	}	

	@Override
	protected void beginOnCreateView() {
		Intent intent = getIntent();
		wid = intent.getStringExtra("wid");
		name = intent.getStringExtra("name");
		time = intent.getStringExtra("time");
		content = intent.getStringExtra("content");
		//
		byte[] b = intent.getByteArrayExtra("avatar");
		if(b != null){
			avatar = BitmapFactory.decodeByteArray(b, 0, b.length);
		}
	}

}
