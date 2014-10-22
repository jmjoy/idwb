package com.joyproj.idwb.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.joyproj.idwb.AbstractActivity;
import com.joyproj.idwb.AttentionActivity;
import com.joyproj.idwb.AvatarActivity;
import com.joyproj.idwb.InfoActivity;
import com.joyproj.idwb.R;
import com.joyproj.idwb.SearchActivity;
import com.joyproj.idwb.WeiboListActivity;
import com.joyproj.idwb.data.UrlData;
import com.joyproj.idwb.data.UserData;
import com.joyproj.idwb.helper.HttpHelper;
import com.joyproj.idwb.helper.ImageUploadHelper;
import com.joyproj.idwb.util.ImageUtil;

public class MeFragment extends AbstractPowerfulFragment implements View.OnClickListener{

	TextView textName;
	TextView textMood;
	TextView textFans;
	TextView textAttention;
	TextView textWeibo;
	ImageView imageAvatar;
	View relaInfo;
	View linearFans;
	View linearAttention;
	View linearWeibo;
	View linearSearch;
	
	String[] userInfo;
	
	@Override
	protected int getResource() {
		return R.layout.frag_me;
	}

	@Override
	protected void doSthOnCreateView(View view) {
		//
		textName = (TextView) view.findViewById(R.id.textName);
		textMood = (TextView) view.findViewById(R.id.textMood);
		textFans = (TextView) view.findViewById(R.id.textFans);
		textAttention = (TextView) view.findViewById(R.id.textAttention);
		textWeibo = (TextView) view.findViewById(R.id.textWeibo);
		imageAvatar = (ImageView) view.findViewById(R.id.imageAvatar);
		relaInfo = view.findViewById(R.id.relaInfo);
		linearFans = view.findViewById(R.id.linearFans);
		linearAttention = view.findViewById(R.id.linearAttention);
		linearWeibo = view.findViewById(R.id.linearWeibo);
		linearSearch = view.findViewById(R.id.lineaSearch);
		// 设置监听器
		relaInfo.setOnClickListener(this);
		linearFans.setOnClickListener(this);
		linearAttention.setOnClickListener(this);
		linearWeibo.setOnClickListener(this);
		linearSearch.setOnClickListener(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		// 联网设置数据
		initData();		
	}
	
	/**
	 * UI_OTHER
	 */
	@Override
	protected void uiOther() {
		textName.setText(userInfo[0]);
		textMood.setText(userInfo[1]);
		textFans.setText(userInfo[2]);
		textAttention.setText(userInfo[3]);
		textWeibo.setText(userInfo[4]);
	}
	
	/**
	 * 联网设置数据
	 */
	private void initData() {
		handler.sendEmptyMessage(UI_WAIT);
		new Thread(){
			@Override
			public void run() {
				//
				loadAvatar();
				//
				HttpHelper httpHelper = new HttpHelper(UrlData.ME, (AbstractActivity) getActivity());
				String args = "id=" + UserData.id + "&password=" + UserData.password;
				String  res = httpHelper.post(args);
				if(res != null && !"".equals(res)){
					// 分割信息
					userInfo = res.split("\\|");
					if(userInfo.length == 5){
						handler.sendEmptyMessage(UI_OTHER);
					}					
				}
				handler.sendEmptyMessage(UI_RESET);
			};
		}.start();
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		if(v == linearSearch){
			intent = new Intent(getActivity(), SearchActivity.class);
		}
		else if(v == linearFans){
			intent = new Intent(getActivity(), AttentionActivity.class);
			intent.putExtra("title", "我的粉丝");
			intent.putExtra("uid", UserData.id);			
			intent.putExtra("url", UrlData.FANS_LIST);
		}
		else if(v == linearAttention){
			intent = new Intent(getActivity(), AttentionActivity.class);
			intent.putExtra("title", "我的关注");
			intent.putExtra("uid", UserData.id);			
			intent.putExtra("url", UrlData.ATTENTION_LIST);
		}
		else if(v == linearWeibo){
			intent = new Intent(getActivity(), WeiboListActivity.class);
			intent.putExtra("title", "我的微博");
			intent.putExtra("uid", UserData.id);
		}
		else if(v == relaInfo){
			intent = new Intent(getActivity(), InfoActivity.class);
			intent.putExtra("title", "我的主页");
			intent.putExtra("uid", UserData.id);
		}
		getActivity().startActivity(intent);
	}	
	
	/**
	 * 读取头像
	 */
	private void loadAvatar(){
		String url = UrlData.AVATAR + "?id=" + UserData.id;
		ImageUtil.loadImgByNetwork(url, handler, new ImageUtil.Own() {
			@Override
			public void ok(Drawable drawable) {
				imageAvatar.setImageDrawable(drawable);
			}

			@Override
			public void failed(Exception e) {
			}
		});		
	}
	
	@Override
	protected CharSequence setTitle() {
		return "我";
	}
	
	@Override
	protected void onRightClick(View v) {
	}

}
