package com.joyproj.idwb;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.joyproj.idwb.data.UrlData;
import com.joyproj.idwb.data.UserData;
import com.joyproj.idwb.util.ImageUtil;
import com.joyproj.idwb.util.UploadUtil;

public class AvatarActivity extends AbstractPowerfulActivity implements
		View.OnClickListener {

    /* 请求码*/
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESULT_REQUEST_CODE = 2;	
    private static final String IMAGE_FILE_NAME = "faceImage.jpg";
	
	private String[] items = new String[] { "选择本地图片", "拍照" };
	
	ImageView imageAvatar;
	Button btnChange;
	Button btnSave;

	private Bitmap photo;
	private String uid;
	private String msg = "";
	
	@Override
	protected void doSthOnCreateView() {
		imageAvatar = (ImageView) findViewById(R.id.imageAvatar);
		btnChange = (Button) findViewById(R.id.btnChange);
		btnSave = (Button) findViewById(R.id.btnSave);
		//
		btnChange.setOnClickListener(this);
		btnSave.setOnClickListener(this);
		// 判断哪些可用哪些不可用
		if(!UserData.id.equals(uid)){
			btnChange.setVisibility(View.GONE);
			btnSave.setVisibility(View.GONE);
		}
		//
		initData();
	}

	/**
	 * 初始化
	 */
	private void initData() {
		handler.sendEmptyMessage(UI_WAIT);
		new Thread() {
			@Override
			public void run() {
				String url = UrlData.AVATAR + "?id=" + uid + "&raw=yes";
				ImageUtil.loadImgByNetwork(url, mainHandler, new ImageUtil.Own() {
					@Override
					public void ok(Drawable drawable) {
						imageAvatar.setImageDrawable(drawable);
						handler.sendEmptyMessage(UI_RESET);
					}
	
					@Override
					public void failed(Exception e) {
						Toast.makeText(AvatarActivity.this, "联网失败",
								Toast.LENGTH_SHORT).show();
						handler.sendEmptyMessage(UI_RESET);
					}
				});
			};
		}.start();
	}

	@Override
	protected CharSequence returnTitleText() {
		// TODO Auto-generated method stub
		return "头像";
	}

	@Override
	protected CharSequence returnRightText() {
		return "";
	}

	@Override
	protected void onRightClick(View v) {
	}

	@Override
	protected int returnChildResource() {
		return R.layout.act_child_avatar;
	}

	@Override
	protected void uiWait() {
	}

	@Override
	protected void uiReset() {
	}

	@Override
	protected void uiOther() {
	}

	@Override
	protected void beginOnCreateView() {
		uid = (String) getIntent().getCharSequenceExtra("uid");
	}

	@Override
	public void onClick(View v) {
		if(v == btnChange){
			showDialog();
		} else if(v == btnSave){
			uploadImage();
		}
	}
	
	/**
	 * 显示选择对话框
	 */
	private void showDialog() {

		new AlertDialog.Builder(this).setTitle("修改头像").setItems(items, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					Intent intentFromGallery = new Intent();
					intentFromGallery.setType("image/*"); // 设置文件类型
					intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
					startActivityForResult(intentFromGallery, IMAGE_REQUEST_CODE);
					break;
					
				case 1:
					Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					// 判断存储卡是否可以用，可用进行存储
						if (hasSdcard()) {
							File dir = new File(Environment.getExternalStorageDirectory() + "/idwb");
							if(!dir.exists()){
								dir.mkdirs();
							}
							intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT,
								Uri.fromFile(new File(dir,
									IMAGE_FILE_NAME)));
						}
						startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
						break;
					}
				}
			}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			}).show();
	}
	
	
	  
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    //结果码不等于操作取消时候
	  if (resultCode != RESULT_CANCELED) {
	      switch (requestCode) {
	      case IMAGE_REQUEST_CODE:
	          startPhotoZoom(data.getData());
	          break;
	      case CAMERA_REQUEST_CODE:
	          if (hasSdcard()) {
	              File tempFile = new File(Environment.getExternalStorageDirectory() + "/idwb/" + IMAGE_FILE_NAME);
	              startPhotoZoom(Uri.fromFile(tempFile));
	          } else {
	        	  Toast.makeText(AvatarActivity.this, "未找到存储卡，无法存储照片！", Toast.LENGTH_LONG).show();
	          }
	          break;
	      case RESULT_REQUEST_CODE:
	          if (data != null) {
	                  getImageToView(data);
	          }
	          break;
	      }
	  }
      super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 裁剪图片方法实现
     * 
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {

            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(uri, "image/*");
            // 设置裁剪
            intent.putExtra("crop", "true");
            // aspectX aspectY 是宽高的比例
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            // outputX outputY 是裁剪图片宽高
            intent.putExtra("outputX", 320);
            intent.putExtra("outputY", 320);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, 2);
    }

    /**
     * 保存裁剪之后的图片数据
     * 
     * @param picdata
     */
    private void getImageToView(Intent data) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                    photo = extras.getParcelable("data");
                    Drawable drawable = new BitmapDrawable(getResources(), photo);
                    imageAvatar.setImageDrawable(drawable);
            }
    }	
	
	/**
	 * 上传图片
	 */
    private void uploadImage(){
		if(photo == null){
			return;
		}
		final File file = saveToSD();
		handler.sendEmptyMessage(UI_BLUR);
		
    	new Thread(){
    		@Override
    		public void run() {
    			Map<String, String> args = new HashMap<String, String>();
    			args.put("id", UserData.id);
    			args.put("password", UserData.password);
    			try {
    				String res = UploadUtil.upload(UrlData.AVATAR_UPLOAD, file, args);
					handleRes(res);
				} catch (Exception e) {
					e.printStackTrace(System.err);
					mainHandler.post(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(AvatarActivity.this, "失败", Toast.LENGTH_SHORT).show();
							handler.sendEmptyMessage(UI_CLEAR);
						}
					});
				}
    		};
    	}.start();
    }
    
    /**
     * 
     */
    private void handleRes(String res){
    	if(res == null || "".equals(res.trim())){
    		return;
    	} else if("1".equals(res)){
    		msg = "成功";
    	} else if("2".equals(res)){
    		msg = "图片不能超过5M";
    	} else if("3".equals(res)){
    		msg = "未知错误";
    	}
		mainHandler.post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(AvatarActivity.this, msg, Toast.LENGTH_SHORT).show();
			}
		});
		handler.sendEmptyMessage(UI_CLEAR);    	
    }
    
    /**
     * 保存到SD卡
     */
    private File saveToSD(){
		// 将文件保存到SD卡
  	  final File f = new File(Environment.getExternalStorageDirectory() + "/idwb/", "tmp.jpg");
  	  if (f.exists()) {
  		  f.delete();
  	  }
  	  FileOutputStream out = null;
  	  try {
  		  out = new FileOutputStream(f);
  		  photo.compress(Bitmap.CompressFormat.JPEG, 90, out);      		   
  		  out.flush();
  	  } catch (Exception e) {
  		  throw new RuntimeException(e);
  	  } finally {
  			if(out != null){
  				try {
  					out.close();
  				} catch (IOException e) {
  					e.printStackTrace();
  				}
  			}
  		}
  	  return f;    	
    }
    
    
    /**
     * 检查是否存在SDCard
     * @return
     */
    public static boolean hasSdcard(){
            String state = Environment.getExternalStorageState();
            if(state.equals(Environment.MEDIA_MOUNTED)){
                    return true;
            }else{
                    return false;
            }
    }		
}
