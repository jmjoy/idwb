package com.joyproj.idwb;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.joyproj.idwb.data.UrlData;
import com.joyproj.idwb.data.UserData;
import com.joyproj.idwb.helper.HttpHelper;
import com.joyproj.idwb.util.UrlCodeUtil;

import android.app.DatePickerDialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class ModifyActivity extends AbstractPowerfulActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener{

	EditText editMood;
	RadioGroup radioGroupSex;
	EditText editBirth;
	EditText editResidence;
	EditText editIntro;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
	
	private long timestamp;
	private int year;
	private int month;
	private int day;
	
	@Override
	protected CharSequence returnTitleText() {
		return "修改";
	}

	@Override
	protected CharSequence returnRightText() {
		return "确定 > ";
	}

	@Override
	protected void doSthOnCreateView() {
		// 获取
		editMood = (EditText) findViewById(R.id.editMood);
		radioGroupSex = (RadioGroup) findViewById(R.id.radioGroupSex);
		editBirth = (EditText) findViewById(R.id.editBirth);
		editResidence = (EditText) findViewById(R.id.editResidence);
		editIntro = (EditText) findViewById(R.id.editIntro);
		// intent
		String mood = getIntent().getStringExtra("mood");
		String sex = getIntent().getStringExtra("sex");
		String birth = getIntent().getStringExtra("birth");
		String residence = getIntent().getStringExtra("residence");
		String intro = getIntent().getStringExtra("intro");
		// 设置text
		editMood.setText(mood);
		if("1".equals(sex)){
			radioGroupSex.check(R.id.radio1);			
		}else if("2".equals(sex)){
			radioGroupSex.check(R.id.radio2);				
		}else{
			radioGroupSex.check(R.id.radio0);
		}
		timestamp = Long.parseLong(birth) * 1000;
		editBirth.setText(sdf.format(new Date(timestamp)));
		editResidence.setText(residence);
		editIntro.setText(intro);
		//
		editBirth.setOnClickListener(this);
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		this.year = year;
		this.month = monthOfYear + 1;
		this.day = dayOfMonth;
		String format = year + "年" + month + "月" + day + "日";
		try {
			this.timestamp = sdf.parse(format).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		editBirth.setText(format);
	}

	@Override
	public void onClick(View v) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(timestamp));
		new DatePickerDialog(this, this, 
                calendar.get(Calendar.YEAR), 
                calendar.get(Calendar.MONTH), 
                calendar.get(Calendar.DAY_OF_MONTH)).show();
	}	
	
	@Override
	protected void onRightClick(View v) {
		// UI 模糊 你懂的
		handler.sendEmptyMessage(UI_BLUR);
		new Thread(){
			@Override
			public void run() {
				// 获取输入
//				String mood = repalceSpecialChars(editMood.getText().toString());
//				String sex = repalceSpecialChars(returnSex());
//				String residence = repalceSpecialChars(editResidence.getText().toString());
//				String intro = repalceSpecialChars(editIntro.getText().toString());
				String mood = editMood.getText().toString();
				String sex = returnSex();
				String residence = editResidence.getText().toString();
				String intro = editIntro.getText().toString();
				String args = "id=" + UserData.id + "&password=" + UserData.password + 
								"&mood=" + UrlCodeUtil.encode(mood) + "&sex=" + sex + 
								"&birth=" + (timestamp / 1000) + "&residence=" + UrlCodeUtil.encode(residence) + 
								"&intro=" + UrlCodeUtil.encode(intro);
				// 联网请求
				HttpHelper httpHelper = new HttpHelper(UrlData.MODIFY, ModifyActivity.this);
				String res = httpHelper.post(args);
				// 处理
				if("1".equals(res)){
					handler.post(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(ModifyActivity.this, "成功", Toast.LENGTH_SHORT).show();
						}
					});
					finish();
				}
				handler.sendEmptyMessage(UI_CLEAR);
			};
		}.start();
	}
	
	private String returnSex(){
		int id = radioGroupSex.getCheckedRadioButtonId();
		String sex = "";
		switch(id){
			case R.id.radio0:
				sex = "0";
				break;
			case R.id.radio1:
				sex = "1";
				break;
			case R.id.radio2:
				sex = "2";
				break;
		}
		return sex;
	}
	
	@Override
	protected void uiWait() {
		setAllEnabled(false, editMood, radioGroupSex, editBirth, editResidence, editIntro);
	}

	@Override
	protected void uiReset() {
		setAllEnabled(true, editMood, radioGroupSex, editBirth, editResidence, editIntro);		
	}
	
	private void setAllEnabled(boolean status, View ...views){
		for(int i = 0; i < views.length; i++){
			views[i].setEnabled(status);
		}
	}
	
//	/**
//	 * 转换特殊字符
//	 * @return
//	 */ 
//	private String repalceSpecialChars(String str){
//		str = str.replaceAll("&", "%aI#@");
//		str = str.replaceAll("=", "#JJ#@a");
//		return str;
//	}
	
	@Override
	protected int returnChildResource() {
		return R.layout.act_child_modify;
	}

	@Override
	protected void uiOther() {
	}

	@Override
	protected void beginOnCreateView() {
	}

	
}
