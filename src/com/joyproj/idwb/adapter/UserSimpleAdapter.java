package com.joyproj.idwb.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.joyproj.idwb.AbstractActivity;
import com.joyproj.idwb.AvatarActivity;
import com.joyproj.idwb.InfoActivity;
import com.joyproj.idwb.R;
import com.joyproj.idwb.data.UrlData;
import com.joyproj.idwb.data.UserData;
import com.joyproj.idwb.helper.HttpHelper;
import com.joyproj.idwb.util.ImageUtil;

public class UserSimpleAdapter extends SimpleAdapter{

	private Handler mainHandler;
	private AbstractActivity activity;
	
	/**
	 * 记录头像的列表, 下次不要联网搜了!
	 */
	private Map<String, Drawable> mapAvatar = new HashMap<String, Drawable>();	
	
	public UserSimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
		super(context, data, resource, from, to);
		this.activity =  ((AbstractActivity) context);
		this.mainHandler = activity.getMainHandler();
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		// 获取listView的子元素
		final View view = super.getView(position, convertView, parent);
		// 获取
		final ToggleButton toggleAttend = (ToggleButton) view.findViewById(R.id.toggleAttention);
		final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
		// 判断关注状态
		boolean b = "1".equals(toggleAttend.getText().toString());
		toggleAttend.setChecked(b);
		// 通过隐藏的TextView获取搜索到的用户的id  
		TextView textId = (TextView) view.findViewById(R.id.textId);
		final String id = textId.getText().toString();
		// 设置 ToggleButton 点击事件
		toggleAttend.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				onToggleAttendClick(toggleAttend, progressBar, id, position);
			}	
		});
		// 设置view 点击事件
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(activity, InfoActivity.class);
				intent.putExtra("title", "他/她的主页");
				intent.putExtra("uid", id);
				activity.startActivity(intent);
			}
		});
		//
		return view;
	}
	
	/**
	 * ToggleButton 点击事件
	 * @param position 
	 * @param v
	 */
	private void onToggleAttendClick(final ToggleButton toggleAttend, final ProgressBar progressBar, final String id, final int position){
		// UI 等待
		progressBar.setVisibility(View.VISIBLE);
		toggleAttend.setEnabled(false);
		// 联网设置关注状态
		new Thread(){
			@Override
			public void run() {
				HttpHelper httpHelper = null;
				if(toggleAttend.isChecked()){
					httpHelper = new HttpHelper(UrlData.ATTENTION, activity);
				}
				else{
					httpHelper = new HttpHelper(UrlData.ATTENTION_CANCEL, activity);
				}
				String args = "id=" + UserData.id + "&password=" + UserData.password + "&aid=" + id;
				final String res = httpHelper.post(args);
				// 处理结果
				mainHandler.post(new Runnable() {
					@Override
					public void run() {
						// 失败 , 成功就不理会了
						if(!"1".equals(res)){
							toggleAttend.setChecked(!toggleAttend.isChecked());
							Toast.makeText(activity, "失败!", Toast.LENGTH_SHORT).show();
						}
						// UI : 恢复
						toggleAttend.setEnabled(true);
						progressBar.setVisibility(View.GONE);
					}
				});
			};
		}.start();
	}
	
	/**
	 * 录取头像
	 * @param imageAvatar
	 * @param id
	 */
	private Drawable loadAvatar(final ImageView imageAvatar, String id){
		String url = UrlData.AVATAR + "?id=" + id;
		return ImageUtil.loadImgByNetwork(url, mainHandler, new ImageUtil.Own() {
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
