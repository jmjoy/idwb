package com.joyproj.idwb.data;

public interface UrlData {
	
	/**
	 * 网站头
	 */
//	String APP = "http://joyproj.com/app/index.php";
	String APP = "http://jmjoyftp.hk15132.wsdns.cc/app/index.php";
	
	/**
	 * 加载页面
	 */
	String SPLASH = APP;
	
	/**
	 * 注册请求
	 */
	String REGISTER =  APP + "/Register/index";
	
	/**
	 * 登录请求
	 */
	String LOGIN = APP + "/Login/index";
	
	/**
	 * 请求自己的微博
	 */
	String WEIBO_ONE = APP + "/Weibo/one";
	
	/**
	 * 请求所有微博
	 */
	String WEIBO_ALL = APP + "/Weibo/all";
	
	/**
	 * 发布微博
	 */
	String WRITE = APP + "/Write/index";
	
	/**
	 * 请求基础个人信息
	 */
	String ME = APP + "/Me/index";

	/**
	 * 请求关注
	 */
	String ATTENTION = APP + "/Attention/index";
	
	/**
	 * 取消关注
	 */
	String ATTENTION_CANCEL = APP + "/Attention/cancel";
	
	/**
	 * 关键字寻找用户
	 */
	String USER_SEARCH = APP + "/User/index";
	
	/**
	 * 获取我的关注
	 */
	String ATTENTION_LIST = APP + "/Attention/getAttention";
	
	/**
	 * 获取我的粉丝
	 */
	String FANS_LIST = APP + "/Attention/getFans";
	
	/**
	 * 获取某个人的主页信息
	 */
	String INFO = APP + "/Info/index";
	
	/**
	 * 获取某个人的主页信息
	 */
	String MODIFY = APP + "/Info/modify";
	
	/**
	 * 获取头像
	 */
	String AVATAR = APP + "/Image/avatar";
	
	/**
	 * 上传头像
	 */
	String AVATAR_UPLOAD = APP + "/Upload/avatar";
	
	/**
	 * 点赞
	 */
	String PRAISE = APP + "/Weibo/praise";
	
}
