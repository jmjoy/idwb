package com.joyproj.idwb.adapter;

import java.util.List;
import java.util.Map;

import com.joyproj.idwb.AbstractActivity;
import com.joyproj.idwb.util.EmotionUtil;

import android.content.Context;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class CommentSimpleAdapter extends SimpleAdapter {

	AbstractActivity act;
	
	public CommentSimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
		super(context, data, resource, from, to);
		this.act = (AbstractActivity) context;
	}

	@Override
	public void setViewText(TextView v, String text) {
		// *号作为是微博内容的标记
		if(text.length() <= 0 || text.charAt(0) != '*'){
			v.setText(text);
			return;
		}
		text = text.substring(1);
		text = text.replaceAll("\\s", " ");
		v.setText(EmotionUtil.formatContent(text, act));
	}	
	
}
