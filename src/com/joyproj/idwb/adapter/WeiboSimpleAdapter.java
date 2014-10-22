package com.joyproj.idwb.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.joyproj.idwb.AbstractActivity;
import com.joyproj.idwb.R;
import com.joyproj.idwb.data.UrlData;
import com.joyproj.idwb.data.UserData;
import com.joyproj.idwb.helper.HttpHelper;
import com.joyproj.idwb.util.EmotionUtil;
import com.joyproj.idwb.util.ImageUtil;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class WeiboSimpleAdapter extends SimpleAdapter {

	private AbstractActivity act;
	private Handler mainHandler;
	private LayoutInflater inflater;
	private int resource;
	
	private Map<Integer, Boolean> mapPraise = new HashMap<Integer, Boolean>();
	private Map<Integer, Integer> mapPraiseNum = new HashMap<Integer, Integer>();
	
	/**
	 * 记录头像的列表, 下次不要联网搜了!
	 */
	private Map<String, Drawable> mapAvatar = new HashMap<String, Drawable>();
	
	public WeiboSimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
		super(context, data, resource, from, to);
		this.act = (AbstractActivity) context;
		this.mainHandler = act.getMainHandler();
		this.inflater = LayoutInflater.from(context);
		this.resource = resource;
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
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		// 获取View
		TextView textId = (TextView) view.findViewById(R.id.textId);
		TextView textWid = (TextView) view.findViewById(R.id.textWid);
		View linearPraise = view.findViewById(R.id.linearPraise);
		ImageView imagePraise = (ImageView) view.findViewById(R.id.imagePraise);
		TextView textPraise = (TextView) view.findViewById(R.id.textPraise);
		TextView textPraised = (TextView) view.findViewById(R.id.textPraised);
		// 设置图标
		if(mapPraise.get(position) == null){
			mapPraise.put(position, "1".equals(textPraised.getText().toString()));
		}
		if(mapPraise.get(position)){
			imagePraise.setImageDrawable(act.getResources().getDrawable(R.drawable.praise_light));
		} else{
			imagePraise.setImageDrawable(act.getResources().getDrawable(R.drawable.praise));
		}
		// 设置赞的数目
		if(mapPraiseNum.get(position) == null){
			mapPraiseNum.put(position, Integer.parseInt(textPraise.getText().toString()));
		} else{
			textPraise.setText("" + mapPraiseNum.get(position));
		}
		// 获取参数
		String id = textId.getText().toString();
		String wid = textWid.getText().toString();
		// 设置监听器
		onClickPraise(linearPraise, imagePraise, textPraise, wid, position, textPraised);
		//
		return view;
	}
	
	private void onClickPraise(final View linearPraise, final ImageView imagePraise, final TextView textPraise, final String wid, final int position, final TextView textPraised){
		linearPraise.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// UI : 暂停
				linearPraise.setAlpha(0.5f);
				linearPraise.setEnabled(false);
				// 联网请求
				new Thread(){
					@Override
					public void run() {
						HttpHelper helper = new HttpHelper(UrlData.PRAISE, act);
						String args = "id=" + UserData.id + "&password=" + UserData.password + "&wid=" + wid;
						String res = helper.post(args);
						handleRes(res, linearPraise);
						// UI : 恢复
						mainHandler.post(new Runnable() {
							@Override
							public void run() {
								linearPraise.setAlpha(1f);
								linearPraise.setEnabled(true);
							}
						});
					};
				}.start();
			}
			
			/**
			 * 处理结果
			 * @param res
			 * @param linearPraise
			 */
			private void handleRes(final String res, View linearPraise){
				if(res == null || "".equals(res)){
					return;
				}
				mainHandler.post(new Runnable() {
					@Override
					public void run() {
						if("1".equals(res)){
							mapPraise.put(position, true);
							imagePraise.setImageDrawable(act.getResources().getDrawable(R.drawable.praise_light));
							mapPraiseNum.put(position, mapPraiseNum.get(position) + 1);
						}
						else if("2".equals(res)){
							mapPraise.put(position, false);
							imagePraise.setImageDrawable(act.getResources().getDrawable(R.drawable.praise));
							mapPraiseNum.put(position, mapPraiseNum.get(position) - 1);
						}
						textPraise.setText(mapPraiseNum.get(position) + "");
					}
				});
			}
		});
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
