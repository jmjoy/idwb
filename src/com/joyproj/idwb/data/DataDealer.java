package com.joyproj.idwb.data;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.SimpleAdapter;

import com.joyproj.idwb.AbstractActivity;
import com.joyproj.idwb.helper.HttpHelper;
import com.joyproj.idwb.widget.AutoListView;

public class DataDealer implements AutoListView.OnRefreshListener, AutoListView.OnLoadListener{

	public static final int LIMIT_WEIBO = 1;
	public static final int LIMIT_USER = 2;
	
	private String strUrl;
	private String args;
	private AbstractActivity activity;
	private int resource;
	private Class<? extends SimpleAdapter> clazz;
	private String[] from;
	private int[] to;
	private AutoListView autolist;
	private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
	private SimpleAdapter adapter;
	private Handler handler;
	
	private int page = 1;
	private int limitFlag = 0;
	private Own own;
	
	/**
	 * 构造函数
	 * @param activity
	 * @param autolist
	 * @param resource
	 * @param clazz
	 * @param from
	 * @param to
	 */
	public DataDealer(AbstractActivity activity, AutoListView autolist, int resource, Class<? extends SimpleAdapter> clazz, String[] from, int[] to) {
		this.activity = activity;
		this.resource = resource;
		this.clazz = clazz;
		this.from = from;
		this.to = to;
		this.autolist = autolist;
	}

	/**
	 * 设置Http提交和处理的详细信息, 注意不要加上page和limit参数
	 * @param strUrl post的地址
	 * @param args 会动态加上page参数, 所以不用传递page
	 * @param own 用于接收必须调用者才能生成的方法
	 */
	public void setHttpDetail(String strUrl, String args, Own own){
		this.strUrl = strUrl;
		this.args = args;
		this.own = own;
	}
	
	/**
	 * 设置Http提交和处理的详细信息, 注意不要加上id, password, page和limit参数
	 * @param strUrl post的地址
	 * @param args 如果不传参数 请填 ""
	 * @param own 用于接收必须调用者才能生成的方法
	 */
	public void setHttpDetail(String strUrl, String args, int limitFlag, Own own){
		this.strUrl = strUrl;
		this.args = args;
		this.limitFlag = limitFlag;
		this.own = own;
	}
	
	/**
	 * 设置limit的模式
	 * @param flag 可选 LIMIT_WEIBO , LIMIT_USER
	 */
	public void setLimitMod(int limitFlag){
		this.limitFlag = limitFlag;
	}
	
	/**
	 * 开始工作!
	 */
	public void work(){
		// 反射获取 SimpleAdapter 实例
		try {
			Constructor<? extends SimpleAdapter> construct = clazz.getConstructor(Context.class, List.class, int.class, String[].class, int[].class);
			adapter = construct.newInstance(activity, data, resource, from, to);
		} catch (Exception e) {
			e.printStackTrace();
		}
		autolist.setAdapter(adapter);
		autolist.setOnRefreshListener(this);
		autolist.setOnLoadListener(this);
		handler = returnHandler();
		initData();
	}
	
	/**
	 * 处理数据
	 * @return
	 */
	public List<Map<String, Object>> getData() {
		List<Map<String, Object>> reslist = new ArrayList<Map<String, Object>>();
		// 联网获取搜索到的东西
		HttpHelper httpHelper = new HttpHelper(strUrl, activity);
		// 按照Limit模式设置limit应该加什么东西
		String limit = null;
		switch(limitFlag){
			case LIMIT_USER:
				limit = UserData.perListrows + "";
				break;
			case LIMIT_WEIBO:
				limit =UserData.weiboListrows + "";
				break;
			default:
				limit = UserData.DEFAULT_LISTROWS + "";
				break;
		}
		// 拼接post参数
		if(args != null && !"".equals(args)){
			args += "&";
		}
		String resStr = httpHelper.post(args + "id=" + UserData.id + "&password=" + UserData.password  + "&page=" + page + "&limit=" + limit);
		// 如果没东西为空
		if(resStr == null || "".equals(resStr)){
			return reslist;
		}
		//  吗的这里经常要调试
//		System.err.println(resStr);
		// 转换json成list 
		try {
			JSONArray jsonArray = new JSONArray(resStr);
			for(int i = 0; i < jsonArray.length(); i++){
				JSONObject jsonObj = (JSONObject) jsonArray.get(i);
				Map<String, Object> map = new HashMap<String, Object>();
				// 这个牛逼 把从网络获取的字段信息返回给调用者去处理
				Object[] fields = new Object[from.length];
				for(int ii = 0; ii < from.length; ii++){
					if(jsonObj.isNull(from[ii])){
						fields[ii] = null;
					} else{
						fields[ii] = jsonObj.get(from[ii]);
					}
				}
				Object[] dealedRes = own.dealRes(fields);
				for(int ii = 0; ii < from.length; ii++){
					map.put(from[ii], dealedRes[ii]);
				}
				reslist.add(map);
			}
		} catch (JSONException e) {
			System.out.println(e);
		}
		return reslist;
	}	
	
	private void loadData(final int what) {
		// 从服务器获取数据
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Message msg = handler.obtainMessage();
				msg.what = what;
				msg.obj = getData();
				handler.sendMessage(msg);
			}
		}).start();
	}

	private void initData() {
		loadData(AutoListView.REFRESH);
	}	
	
	@Override
	public void onRefresh() {
		page = 1;
		loadData(AutoListView.REFRESH);
	}

	@Override
	public void onLoad() {
		page++;
		loadData(AutoListView.LOAD);
	}

	/**
	 * 设置Handler
	 * @return
	 */
	private Handler returnHandler(){
		return new Handler() {
			@Override
			public void handleMessage(Message msg) {
				List<Map<String, Object>> result = (List<Map<String, Object>>) msg.obj;
				switch (msg.what) {
					case AutoListView.REFRESH:
						autolist.onRefreshComplete();
						data.clear();
						data.addAll(result);
						break;
					case AutoListView.LOAD:
						autolist.onLoadComplete();
						data.addAll(result);
						break;
				}
				autolist.setResultSize(result.size());
				adapter.notifyDataSetChanged();
			};
		};		
	}
	
	/**
	 * DataDealer 用来传递方法的
	 * @author JM_Joy
	 *
	 */
	public interface Own{
		/**
		 * 传递本来的从网络请求到的参数, 顺序和from的顺序一直
		 * @param fields 如果from中的元素没有对应的返回值, 则对于的fields的元素为空
		 * @return 处理后的数组
		 */
		Object[] dealRes(Object[] fields);
	}
}
