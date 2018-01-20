/**
 * Copyright &copy; 2014-2015.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.flea.modules.ebook.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;



/**
 * @author Bruce Tie
 * @date 2015年1月16日 上午11:13:55 
 * @function
 */
public class HttpUtil {
	   public static final String UTF_8 = "UTF-8";
	  
	   private final static int TIMEOUT_CONNECTION = 20000;
	   private final static int TIMEOUT_SOCKET = 20000;
	   private final static String TAG="HttpUtil";
	   /**
	    * 为HttpClient设置参数
	    * @return
	    */
	   public static HttpClient getHttpClient() {
		   CloseableHttpClient httpClient = HttpClients.createDefault();
	      return httpClient;
	   }
	   /**
	    * 为HttpGet添加Get信息。
	    * @param url
	    * @return
	    */
	   public static HttpGet getHttpGet(String url) {
	      HttpGet httpGet = new HttpGet(url);
	      RequestConfig requestConfig = RequestConfig.custom()
	    		    .setConnectionRequestTimeout(TIMEOUT_CONNECTION)
	    		    .setConnectTimeout(TIMEOUT_CONNECTION)
	    		    .setSocketTimeout(TIMEOUT_CONNECTION)
	    		    .build();
	     // SSLSocketFactory.getSocketFactory().setHostnameVerifier(new AllowAllHostnameVerifier());  
	      httpGet.addHeader("User-Agent", "DZS-TEST");//伪装为浏览器,HttpClient访问豆瓣的API时不会返回500错误
	      httpGet.setConfig(requestConfig);
	      return httpGet;
	   }
	 
	   
	   
	   /**
	    * Get请求
	    * @param url
	    * @return
	    */
	   public static String http_get(String url) {
	  
	      HttpClient httpClient = null;
	      HttpGet httpGet = null;
	      String responseBody = "";
	      try {
	         httpClient = getHttpClient();
	         httpGet = getHttpGet(url);
	         HttpResponse response = httpClient.execute(httpGet);
	         int statusCode = response.getStatusLine().getStatusCode();
	         if (statusCode != HttpStatus.SC_OK) {
	            System.out.println("错误码" + statusCode);
	            return "404";
	         }
	         responseBody = EntityUtils.toString(response.getEntity(),"UTF-8");
	      } catch (IOException e) {
	    	 // throw new IOException();
	         // 发生网络异常
	         e.printStackTrace();
	  
	      } finally {
	         // 释放连接
	         httpClient.getConnectionManager().shutdown();
	         httpClient = null;
	      }
	      return responseBody;
	   }
	   /**
	    * 获取网络图片
	    * @param url
	    * @return
	    */
//	   public static Bitmap getNetBitmap(String url)  {
//	      HttpClient httpClient = null;
//	      HttpGet httpGet = null;
//	      Bitmap bitmap = null;
//	  
//	      try {
//	         httpClient = getHttpClient();
//	         httpGet = getHttpGet(url);
//	         HttpResponse response = httpClient.execute(httpGet);
//	         int statusCode = response.getStatusLine().getStatusCode();
//	         if (statusCode != HttpStatus.SC_OK) {
//	            Log.i(TAG, "错误码"+statusCode);
//	         }
//	         InputStream inStream = new ByteArrayInputStream(
//	                EntityUtils.toByteArray(response.getEntity()));
//	         bitmap = BitmapFactory.decodeStream(inStream);
//	         inStream.close();
//	      } catch (IOException e) {
//	         // 发生网络异常
//	         e.printStackTrace();
//	          
//	      } finally {
//	         // 释放连接
//	         httpClient.getConnectionManager().shutdown();
//	         httpClient = null;
//	      }
//	  
//	      return bitmap;
//	   }
	  
	}