package com.joyproj.idwb.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joyproj.idwb.AbstractActivity;
import com.joyproj.idwb.AbstractSearchableActivity;
import com.joyproj.idwb.R;
import com.joyproj.idwb.adapter.UserSimpleAdapter;
import com.joyproj.idwb.data.DataDealer;
import com.joyproj.idwb.data.UserData;
import com.joyproj.idwb.widget.AutoListView;

public class UserListFragment extends AbstractFragment {

	AbstractSearchableActivity act;
	AutoListView autoSearch;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.frag_search, container, false);
		//
		act = (AbstractSearchableActivity) getActivity();
		// 初始化AutoSearch
		autoSearch = (AutoListView) v.findViewById(R.id.autoSearch);
		//
		initAutoSearch();
		//
		return v;
	}
	
	/**
	 * 初始化AutoListView
	 */
	private void initAutoSearch(){
		// 联网
		String[] from = {"id", "avatar", "name", "mood", "attend"};
		int[] to = {R.id.textId, R.id.imageAvatar, R.id.textName, R.id.textMood, R.id.toggleAttention};
		DataDealer dataDealer = new DataDealer((AbstractActivity) getActivity(), autoSearch, R.layout.user, UserSimpleAdapter.class, from, to);
		dataDealer.setHttpDetail(act.getUrl(), act.getPostArgs(), UserData.perListrows, new DataDealer.Own() {
			@Override
			public Object[] dealRes(Object[] fields) {
				return new Object[]{fields[0], fields[0], fields[2], fields[3], (fields[4] == null) ? "0" : "1"};
			}
		});
		dataDealer.work();
	}
	
}