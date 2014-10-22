package com.joyproj.idwb.fragment;

import com.joyproj.idwb.R;

import android.view.View;

public class MsgFragment extends AbstractPowerfulFragment {

	@Override
	protected int getResource() {
		return R.layout.frag_msg;
	}

	@Override
	protected void doSthOnCreateView(View view) {
		imageRight.setImageResource(R.drawable.refresh);
	}

	@Override
	protected CharSequence setTitle() {
		return "ÏûÏ¢";
	}
	
	@Override
	protected void onRightClick(View v) {
		// TODO Auto-generated method stub

	}

}
