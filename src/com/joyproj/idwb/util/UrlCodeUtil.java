package com.joyproj.idwb.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public final class UrlCodeUtil {

	private  UrlCodeUtil() {
	}
	
	public static String encode(String str){
		String res = null;
		try {
			res = URLEncoder.encode(str, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public static String decode(String str, String charset){
		String res = null;
		try {
			URLDecoder.decode(str, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return res;
	}
}
