package com.joyproj.idwb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.joyproj.idwb.data.UrlData;
import com.joyproj.idwb.data.UserData;
import com.joyproj.idwb.helper.HttpHelper;
import com.joyproj.idwb.util.EmotionUtil;
import com.joyproj.idwb.util.UrlCodeUtil;

public class WriteActivity extends AbstractPowerfulActivity implements TextWatcher, AdapterView.OnItemClickListener{

	EditText editInput;
	TextView textWordSum;
	GridView gridEmotions;

	/**
	 * 主线程
	 */
	@Override
	protected void doSthOnCreateView() {
		// 初始化
		editInput = (EditText) findViewById(R.id.editInput);
		textWordSum = (TextView) findViewById(R.id.textWordSum);
		gridEmotions = (GridView) findViewById(R.id.gridEmotions);		
		// 表情框架初始化
		SimpleAdapter adapter = new SimpleAdapter(this, returnEmotionData(), R.layout.pic, new String[]{"emotion"}, new int[]{R.id.imageEmotion});
		gridEmotions.setAdapter(adapter);
		// 增加监听器
		editInput.addTextChangedListener(this);
		gridEmotions.setOnItemClickListener(this);
	}

	/**
	 * 发表微博
	 */
	@Override
	protected void onRightClick(View v) {
		// 如果微博为空, 不予理会
		final String rawContent = editInput.getText().toString();
		if("".equals(rawContent)){
			return;
		}
		// UI : 等待
		handler.sendEmptyMessage(UI_OTHER);
		// 新线程提交数据
		new Thread(){
			@Override
			public void run() {
				HttpHelper httpUtil = new HttpHelper(UrlData.WRITE, WriteActivity.this);
				String content = UrlCodeUtil.encode(rawContent);
				String args = "id=" + UserData.id + "&password=" + UserData.password + "&content=" + content;
				String res = httpUtil.post(args);		
				handleRes(res);
			};
		}.start();

	}	

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
		String str = editInput.getText().toString() + EmotionUtil.EMOTION_MAP[pos];
		if(str.length() >= 200){
			return;
		}
		editInput.setText(EmotionUtil.formatContent(str, this));
		editInput.requestFocus();
		editInput.setSelection(str.length());
	}
	
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// 改变字数显示
		textWordSum.setText(s.length() + " / 200");
	}
	
	/**
	 * 处理返回结果
	 */
	private void handleRes(String res){
		if("1".equals(res)){
			handler.post(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(WriteActivity.this, "发布成功!", Toast.LENGTH_SHORT).show();
					finish();					
				}
			});
		}
		else{
			handler.post(new Runnable() {
				@Override
				public void run() {
					base.setAlpha(1f);
					progressBar.setVisibility(View.GONE);					
					textRight.setClickable(true);
					editInput.setEnabled(true);
					gridEmotions.setClickable(true);					
				}
			});
		}
	}	
	
	@Override
	protected CharSequence returnTitleText() {
		return "写微博";
	}

	@Override
	protected CharSequence returnRightText() {
		return "发表 > ";
	}	
	
	@Override
	protected int returnChildResource() {
		return R.layout.act_child_write;
	}

	@Override
	protected void uiWait() {
	}

	@Override
	protected void uiReset() {
	}

	/**
	 * 在gridview中添加表情组
	 */
	private List<Map<String, Object>> returnEmotionData(){
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		try {
			Class clazz = Class.forName("com.joyproj.idwb.R$drawable");
			for(int i = 1; i <= 50; i++){
				Map<String, Object> map = new HashMap<String, Object>();
				String index = (i < 10) ? ("0" + i) : ("" + i);
				map.put("emotion", clazz.getField("emotion_" + index).getInt(null));
				data.add(map);
			}
		} 
		catch (Exception e) {
			System.out.println(e);
		}
		return data;
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	@Override
	public void afterTextChanged(Editable s) {
	}

	@Override
	protected void beginOnCreateView() {
	}

	@Override
	protected void uiOther() {
		base.setAlpha(0.5f);
		progressBar.setVisibility(View.VISIBLE);
		textRight.setClickable(false);
		editInput.setEnabled(false);
		gridEmotions.setClickable(false);		
	}
	
}
