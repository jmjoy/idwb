package com.joyproj.idwb;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.joyproj.idwb.fragment.HomeFragment;
import com.joyproj.idwb.fragment.MeFragment;
import com.joyproj.idwb.fragment.MsgFragment;
import com.joyproj.idwb.helper.PoolHelper;

public class MainActivity extends AbstractActivity implements View.OnClickListener{

	private int flag = 0;
	private long exitTime;
	
	LinearLayout linearHome;
	LinearLayout linearMsg;
	LinearLayout linearMe;

	ImageView imageHome;
	ImageView imageMsg;
	ImageView imageMe;	
	
	TextView textHome;
	TextView textMsg;
	TextView textMe;
	
	Drawable drawHome;
	Drawable drawMsg;
	Drawable drawMe;
	Drawable drawHomeLight;
	Drawable drawMsgLight;
	Drawable drawMeLight;
	
	HomeFragment homeFragment;
	MsgFragment msgFragment;
	MeFragment meFragment;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        
        initTab();
        
        changeFrag();
        
        // 把他放进单例池 哟
        PoolHelper poolHelper = PoolHelper.getInstance();
        poolHelper.activity = this;
    }
    
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
    	Intent intent;
        switch(item.getItemId()){
        	case R.id.action_settings:
        		intent = new Intent(this, SettingActivity.class);
        		startActivity(intent);
        		return true;
        		
        	case R.id.action_about:
        		intent = new Intent(this, AboutActivity.class);
        		startActivity(intent);
        		return true;
        		
        	case R.id.action_exit:
        		finish();
        		System.exit(0);
        		return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 切换fragment
     */
    private void changeFrag(){
    	FragmentTransaction transaction = getFragmentManager().beginTransaction();
    	Fragment fragment = null;
    	switch(flag){
    		case 0:
    			if(homeFragment == null){
    				homeFragment = new HomeFragment();
    			}
    			fragment = homeFragment;
    			break;
    			
    		case 1:
    			if(msgFragment == null){
    				msgFragment = new MsgFragment();
    			}
    			fragment = msgFragment;
    			break;
    			
    		case 2:
    			if(meFragment == null){
    				meFragment = new MeFragment();
    			}
    			fragment = meFragment;
    			break;
    			
    	}
    	transaction.replace(R.id.linearMain, fragment);
    	transaction.commit();
    }
    
    /**
     * 初始化选项卡
     */
	private void initTab() {
		
        linearHome = (LinearLayout) findViewById(R.id.linearHome);
        linearMsg = (LinearLayout) findViewById(R.id.linearMsg);
        linearMe = (LinearLayout) findViewById(R.id.linearMe);
        
        imageHome = (ImageView) findViewById(R.id.imageHome);
        imageMsg = (ImageView) findViewById(R.id.imageMsg);
        imageMe = (ImageView) findViewById(R.id.imageMe);
        
        textHome = (TextView) findViewById(R.id.textHome);
        textMsg = (TextView) findViewById(R.id.textMsg);
        textMe = (TextView) findViewById(R.id.textMe);
        
        drawHome = getResources().getDrawable(R.drawable.home);
        drawMsg = getResources().getDrawable(R.drawable.msg);
        drawMe = getResources().getDrawable(R.drawable.per);
        
        drawHomeLight = getResources().getDrawable(R.drawable.home_light);
        drawMsgLight = getResources().getDrawable(R.drawable.msg_light);
        drawMeLight = getResources().getDrawable(R.drawable.per_light);
        
        linearHome.setOnClickListener(this);
        linearMsg.setOnClickListener(this);
        linearMe.setOnClickListener(this);
	}

	/**
	 * 选项卡点击事件
	 */
	@Override
	public void onClick(View v) {
		if(v == linearHome){
			if(flag == 0){
				return;
			}
			flag = 0;
			resetNav();
			imageHome.setImageDrawable(drawHomeLight);
			textHome.setTextColor(0xff428bca);
			changeFrag();
		}
		else if(v == linearMsg){
			if(flag == 1){
				return;
			}
			flag = 1;
			resetNav();
			imageMsg.setImageDrawable(drawMsgLight);
			textMsg.setTextColor(0xff428bca);
			changeFrag();
		}
		else if(v == linearMe){
			if(flag == 2){
				return;
			}
			flag = 2;
			resetNav();
			imageMe.setImageDrawable(drawMeLight);
			textMe.setTextColor(0xff428bca);
			changeFrag();
		}
	}
    
	/**
	 * 重置选项卡所有选项样式
	 */
	private void resetNav(){
		
        imageHome.setImageDrawable(drawHome);
        imageMsg.setImageDrawable(drawMsg);
        imageMe.setImageDrawable(drawMe);
        
        textHome.setTextColor(0xff777777);
        textMsg.setTextColor(0xff777777);
        textMe.setTextColor(0xff777777);
	}
	
	/**
	 * 点两次返回退出程序
	 */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){   
            if((System.currentTimeMillis()-exitTime) > 1000){  
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();                                
                exitTime = System.currentTimeMillis();  
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    
}