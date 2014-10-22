package com.joyproj.idwb.fragment;

import com.joyproj.idwb.AbstractActivity;

import android.app.Activity;
import android.app.Fragment;

public abstract class AbstractFragment extends Fragment{

	AbstractActivity activity;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = (AbstractActivity) activity;
	}
}
