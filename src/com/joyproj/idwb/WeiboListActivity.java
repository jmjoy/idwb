package com.joyproj.idwb;

import com.joyproj.idwb.adapter.WeiboSimpleAdapter;
import com.joyproj.idwb.data.DataDealer;
import com.joyproj.idwb.data.UrlData;
import com.joyproj.idwb.util.DateUtil;
import com.joyproj.idwb.widget.AutoListView;

import android.view.View;

public class WeiboListActivity extends AbstractPowerfulActivity {

	AutoListView autolist;
	
	/**
	 * 标题
	 */
	CharSequence title;
	
	/**
	 * 搜谁的微博
	 */
	String uid;
	
	@Override
	protected void doSthOnCreateView() {
		autolist = (AutoListView) findViewById(R.id.autolist);
		
		String[] from = {"avatar", "name", "ctime",  "content", "id"};
		int[] to = {R.id.weibo_avatar, R.id.weibo_name, R.id.weibo_time, R.id.weibo_content, R.id.textId};
		DataDealer dataDealer = new DataDealer(this, autolist, R.layout.weibo, WeiboSimpleAdapter.class, from, to);
		dataDealer.setHttpDetail(UrlData.WEIBO_ONE, "", DataDealer.LIMIT_WEIBO, new DataDealer.Own() {
			@Override
			public Object[] dealRes(Object[] fields) {
				// 其中 * 号作为微博内容的标记, 需要转换表情
				return new Object[]{fields[4], fields[1],  DateUtil.format((String) fields[2]), "*" + fields[3], fields[4]};
			} 
		}); 
		dataDealer.work();
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
		return "";
	}

	@Override
	protected void onRightClick(View v) {
	}

	@Override
	protected int returnChildResource() {
		return R.layout.act_child_weibolist;
	}

	@Override
	protected void uiWait() {
	}

	@Override
	protected void uiReset() {
	}

	@Override
	protected void uiOther() {
		// TODO Auto-generated method stub
		
	}

}
