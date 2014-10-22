package com.joyproj.idwb.util;

import java.io.File;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

/**
 * Example how to use multipart/form encoded POST request.
 */
public class UploadUtil {

//    public static String uploadFile(String url, File file, Map<String, String> args) throws Exception {
//    	
//    	String res = null;
//    	
//        CloseableHttpClient httpclient = HttpClients.createDefault();
//        try {
//            HttpPost httppost = new HttpPost(url);
//
//            FileBody bin = new FileBody(file);
//            
//            MultipartEntityBuilder builder = MultipartEntityBuilder.create()
//            		.addPart("file", bin);
//            
//            // 循环获取参数
//            for(Map.Entry<String, String> entry : args.entrySet()){
//            	builder.addPart(entry.getKey(), new StringBody(entry.getValue(), ContentType.TEXT_PLAIN));
//            }
//            
//            HttpEntity reqEntity = builder.build();
//
//            httppost.setEntity(reqEntity);
//
////            System.out.println("executing request " + httppost.getRequestLine());
//            CloseableHttpResponse response = httpclient.execute(httppost);
//            try {
////                System.out.println("----------------------------------------");
////                System.out.println(response.getStatusLine());
//                HttpEntity resEntity = response.getEntity();
//                if (resEntity != null) {
////                    System.out.println("Response content length: " + resEntity.getContentLength());
//                    BufferedInputStream inStream = new BufferedInputStream(resEntity.getContent());
//                    byte[] b = new byte[1];
//                    inStream.read(b);
////                    System.out.println(new String(b));
//                    res = new String(b);
//                }
////                EntityUtils.consume(resEntity);
//            } finally {
//                response.close();
//            }
//        } finally {
//            httpclient.close();
//        }
//        //
//		return res;
//    }
	
	
	public static String upload(String urlServer, File file, Map<String, String> args) throws Exception {
	      HttpClient httpclient = new DefaultHttpClient();
	      //设置通信协议版本
	      httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
	       
	      //File path= Environment.getExternalStorageDirectory(); //取得SD卡的路径
	       
	      //String pathToOurFile = path.getPath()+File.separator+"ak.txt"; //uploadfile
	      //String urlServer = "http://192.168.1.88/test/upload.php"; 
	       
	      HttpPost httppost = new HttpPost(urlServer);
	   
	      MultipartEntity mpEntity = new MultipartEntity(); //文件传输
	      ContentBody cbFile = new FileBody(file);
	      mpEntity.addPart("file", cbFile); // <input type="file" name="userfile" />  对应的
	      
		  // 循环获取参数
		  for(Map.Entry<String, String> entry : args.entrySet()){
			  mpEntity.addPart(entry.getKey(), new StringBody(entry.getValue()));
		  }	      
	      
	      httppost.setEntity(mpEntity);
//	      System.out.println("executing request " + httppost.getRequestLine());
	       
	      HttpResponse response = httpclient.execute(httppost);
	      HttpEntity resEntity = response.getEntity();
	   
//	      System.out.println(response.getStatusLine());//通信Ok
	      String res="";
	      
	      if (resEntity != null) {
	        //System.out.println(EntityUtils.toString(resEntity,"utf-8"));
	        res = EntityUtils.toString(resEntity,"utf-8");
	      }
	      
	      if (resEntity != null) {
	        resEntity.consumeContent();
	      }
	      httpclient.getConnectionManager().shutdown();
	      
	      return res;
	    }		
	

}