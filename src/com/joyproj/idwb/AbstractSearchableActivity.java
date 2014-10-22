package com.joyproj.idwb;

import com.joyproj.idwb.util.UrlCodeUtil;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public abstract class AbstractSearchableActivity extends AbstractPowerfulActivity {

	protected EditText editInput;
	
	// 传递给子Fragment的东西
	private String postArgs = "";
	protected String url;
	
	@Override
	protected void doSthOnCreateView() {
		editInput = (EditText) findViewById(R.id.editInput);
		doNextCreateView();
	}
	
	/**
	 * 点击Search按钮的时候
	 * @param v
	 */
	public void clickSearch(View v){
		// 获取输入的关键词
		String searchWord = UrlCodeUtil.encode(editInput.getText().toString());
		if(searchWord != null && !"".equals(searchWord)){
			postArgs = "search=" + searchWord;
		}
		// 隐藏输入法
		InputMethodManager imm = ( InputMethodManager ) v.getContext( ).getSystemService( Context.INPUT_METHOD_SERVICE );     
        if ( imm.isActive( ) ) {     
            imm.hideSoftInputFromWindow( v.getApplicationWindowToken( ) , 0 );   
        }
        doNextFromClickSearch(v);
	}

	/**
	 * 返回搜索关键字
	 * @return 搜索关键字
	 */
	public String getPostArgs(){
		return postArgs;
	}	
	
	/**
	 * 
	 */
	public String getUrl(){
		return url;
	}
	
	/**
	 * 名字没有取好
	 */
	protected abstract void doNextCreateView();
	
	/**
	 * 点击搜索按钮 搜索词获得了 然后呢
	 */
	protected abstract void doNextFromClickSearch(View v);
	
}
