package com.flea.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.aliyun.oss.OSSClient;


public class OSSUtils {
	private static String endpoint = "http://oss-cn-shenzhen.aliyuncs.com/";
	private static String accessKeyId = "LTAIThXpXhtBPoTR";
	private static String accessKeySecret = "3wXqIyrD6kmxm6ahYfJuJWvTxffyJc";
	private static String bucketName = "yuntoss";
	private static String path = "images/";

	
	public  void upFile(InputStream inputStream,String targetFile){
		
		OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
//		inputStream = new URL(urlFile).openStream();
		ossClient.putObject(bucketName, path+targetFile, inputStream);
		ossClient.shutdown();
		
	}
	
	/**
	 * 电子书图片上传
	 * @param inputStream
	 * @param targetFile
	 */
	public  void upEbookFile(InputStream inputStream, String targetFile){
		OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
//		inputStream = new URL(urlFile).openStream();
		ossClient.putObject(bucketName, path+"pic/"+targetFile, inputStream);
		ossClient.shutdown();
	}
	/**
	 * 
	 * @Description:咨询管理ueditor图片上传
	 * @param inputStream
	 * @param targetFile    
	 * @author QL 
	 * @date 2017年3月16日 下午7:37:01
	 */
	public  void uploadNewImages(InputStream inputStream,String targetFile){
		OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
//		inputStream = new URL(urlFile).openStream();
		ossClient.putObject(bucketName, path + "newsImages/" + targetFile, inputStream);
		ossClient.shutdown();
	}
	
	/**
	 * 字符串切分为字符
	 * @param source
	 * @param index 倒数第几位
	 * @return
	 */
	public static String splitByIndex(String source,int index){
		return source.substring(source.length()-index, source.length()-index+1);
	}
	
	public static String getSubCatalogByIsbn(String isbn){
		return splitByIndex(isbn,2)+"/"+splitByIndex(isbn,1)+"/";
	}
	
	public static void main(String[] args)  {
		OSSUtils ossUtils = new OSSUtils();
		File file = new File("d:/liutao.png");
		InputStream inputStream;
		try {
			inputStream = new FileInputStream(file);
			ossUtils.upFile(inputStream, "newsImages/201609/"+file.getName());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	
	}

}
