package com.joyproj.idwb.fragment;

import com.joyproj.idwb.R;

import android.view.View;

public class RegisterFragment extends AbstractPowerfulFragment {

	@Override
	protected int getResource() {
		return R.layout.frag_register;
	}

	@Override
	protected void doSthOnCreateView(View view) {
	}

	@Override
	protected CharSequence setTitle() {
		return "×¢²á";
	}

	@Override
	protected CharSequence setBack() {
		return "< ·µ»Ø";
	}

	@Override
	protected void onBackClick(View v) {
		getActivity().finish();
	}	
	
	@Override
	protected void onRightClick(View v) {
	}

}
