package com.joyproj.idwb.helper;

import android.graphics.Bitmap;
import com.joyproj.idwb.AbstractActivity;

public class ImageUploadHelper {

	private Bitmap bitmap;
	private String urlStr;
	private AbstractActivity activity;
	
	/**
	 * 
	 * @param bitmap
	 * @param url
	 */
	public ImageUploadHelper(Bitmap bitmap, String urlStr, AbstractActivity activity) {
		this.bitmap = bitmap;
		this.urlStr = urlStr;
		this.activity = activity;
	}
	
	public void uploadAvatar(){
		
	}
	
}
