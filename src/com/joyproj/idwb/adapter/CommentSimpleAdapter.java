package com.joyproj.idwb.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.joyproj.idwb.AbstractActivity;
import com.joyproj.idwb.data.UrlData;
import com.joyproj.idwb.util.EmotionUtil;
import com.joyproj.idwb.util.ImageUtil;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class CommentSimpleAdapter extends SimpleAdapter {

	AbstractActivity act;
	
	private Map<String, Drawable> mapAvatar = new HashMap<String, Drawable>();
	
	public CommentSimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
		super(context, data, resource, from, to);
		this.act = (AbstractActivity) context;
	}

	@Override
	public void setViewImage(final ImageView v, final String value) {
		super.setViewImage(v, value); 
		// 设置头像
 		if(mapAvatar.containsKey(value)){
			v.setImageDrawable(mapAvatar.get(value));
		}
		else{
			new Thread(){
				@Override
				public void run() {
					mapAvatar.put(value, loadAvatar(v, value));
				};
			}.start();
		} 
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
	
	/**
	 * 录取头像
	 * @param imageAvatar
	 * @param id
	 */
	private Drawable loadAvatar(final ImageView imageAvatar, String id){
		String url = UrlData.AVATAR + "?id=" + id;
		return ImageUtil.loadImgByNetwork(url, act.getMainHandler(), new ImageUtil.Own() {
			@Override
			public void ok(Drawable drawable) {
				imageAvatar.setImageDrawable(drawable);
			}
			@Override
			public void failed(Exception e) {
				e.printStackTrace(System.err);
			}
		});
	}		
	
}
