package com.joyproj.idwb.helper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.joyproj.idwb.AbstractActivity;

import android.widget.Toast;

public final class HttpHelper {

	private int timeout = 7000;
	
	private HttpURLConnection connection;
	private URL url;
	private BufferedReader inReader;
	private DataOutputStream outStream;
	private AbstractActivity activity;
	
	/**
	 * 
	 * @param activity
	 */
	public HttpHelper(AbstractActivity activity){
		this.activity = activity;
	}

	/**
	 * 
	 * @param urlStr
	 * @param activity
	 */
	public HttpHelper(String urlStr, AbstractActivity activity){
		setURL(urlStr);
		this.activity = activity;
	}
	
	/**
	 * 设置超时
	 * @param timeout
	 */
	public void setTimeout(int timeout){
		this.timeout = timeout;
	}
	
	/**
	 * 设置URL
	 * @param urlstr
	 */
	public void setURL(String urlstr){
		try {
			url = new URL(urlstr);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public String get() {
		return request("GET", null);
	}
	
	/**
	 * 
	 * @param args
	 * @return
	 */
	public String post(String args){
		return request("POST", args);
	}
	
	/**
	 * 
	 * @param method
	 * @param args
	 * @return
	 */
	private String request(String method, String args){
        String result = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setConnectTimeout(timeout);
            connection.setDoInput(true);
            if("POST".equals(method)){
            	connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty("Charset", "utf-8");
                outStream = new DataOutputStream(connection.getOutputStream());
                outStream.writeBytes(args);
                outStream.flush();
            }
            connection.connect();
            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK){
            	throw new HttpConnectFailedException();
            }
            inReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer strBuffer = new StringBuffer();
            String line = null;
            while ((line = inReader.readLine()) != null) {
                strBuffer.append(line);
            }
            result = strBuffer.toString();
        } catch (Exception e) {
        	e.printStackTrace(System.err);
        	activity.getMainHandler().post(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(activity, "网络连接失败", Toast.LENGTH_LONG).show();
				}
			});
        } finally {
        	try{
        		if(outStream != null){
        			outStream.close();
        		}
        	} catch (IOException e) {
				e.printStackTrace();
			}
        	finally{
            	try{
            		if(inReader != null){
            			inReader.close();
            		}
            	} catch (IOException e) {
    				e.printStackTrace();
    			}
            	finally{
    	            if (connection != null) {
    	            	connection.disconnect();
    	            }        		
            	}        		
        	}
        }
        return result;
	}

	/**
	 * 
	 * @author JM_Joy
	 *
	 */
	public final class HttpConnectFailedException extends Exception {
	}
}
