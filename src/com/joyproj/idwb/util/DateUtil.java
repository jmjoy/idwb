package com.joyproj.idwb.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @Author SunnyCoffee
 * @Date 2014-1-28
 * @version 1.0
 * @Desc π§æﬂ¿‡
 */
public final class DateUtil {

	public static String getCurrentTime(String format) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
		String currentTime = sdf.format(date);
		return currentTime;
	}

	public static String getCurrentTime() {
		return getCurrentTime("yyyy-MM-dd  HH:mm:ss");
	}
	
	public static String format(String time){
		long t = Long.parseLong(time);
		return format(t);
	}
	
	public static String format(long time){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
		return sdf.format(new Date(time * 1000));
	}
}
