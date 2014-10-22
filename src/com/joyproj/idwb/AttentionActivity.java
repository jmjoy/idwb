package com.joyproj.idwb;

import com.joyproj.idwb.fragment.UserListFragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.View;

public class AttentionActivity extends AbstractSearchableActivity {

	private CharSequence title;
	private String uid;
	
	@Override
	protected void doNextCreateView() {
		//
		editInput.setHint("在" + title + "中搜索");
		// 这个是初始化 AttentionList
		doNextFromClickSearch(null);
	}

	@Override
	protected void doNextFromClickSearch(View v) {
		// 创建那个Fragment
		FragmentManager fragmentManager = getFragmentManager();  
	    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
	    Fragment userFrag = new UserListFragment();
	    fragmentTransaction.replace(R.id.replace, userFrag);
	    fragmentTransaction.commit();
	}

	@Override
	protected void beginOnCreateView() {
		title = getIntent().getCharSequenceExtra("title");
		uid = (String) getIntent().getCharSequenceExtra("uid");
		url = (String) getIntent().getCharSequenceExtra("url");
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
		return R.layout.act_attend;
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


}
