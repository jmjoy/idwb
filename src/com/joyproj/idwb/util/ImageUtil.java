package com.joyproj.idwb.util;

import java.net.URL;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;

public final class ImageUtil {

	public static Drawable zoomDrawable(Drawable drawable, int w, int h) {
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		Bitmap oldbmp = drawableToBitmap(drawable);
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) w / width);
		float scaleHeight = ((float) h / height);
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,
				matrix, true);
		return new BitmapDrawable(null, newbmp);
	}

	public static Bitmap drawableToBitmap(Drawable drawable) {
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565;
		Bitmap bitmap = Bitmap.createBitmap(width, height, config);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas);
		return bitmap;
	}
	
	
	public static Drawable loadImgByNetwork(String urlStr, Handler handler, final Own own){
		Drawable drawable2 = null;
		try {
			final Drawable drawable = Drawable.createFromStream(new URL(urlStr).openStream(), null);
			drawable2 = drawable;
			handler.post(new Runnable() {
				@Override
				public void run() {
					own.ok(drawable);
				}
			});
		} catch (final Exception e) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					own.failed(e);
				}
			});
		}
		return drawable2;
	}
	
	public static interface Own{
		void failed(Exception e);
		void ok(Drawable drawable);
	}
}
