/**  
* @Package com.flea.modules.system.util
* @Description: TODO
* @author bruce
* @date 2016年4月26日 上午11:43:19
* @version V1.0  
*/ 
package com.flea.modules.system.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.flea.common.Common;
import com.flea.common.pojo.User;
import com.flea.common.util.ShiroUtils;

/**
 * @author bruce
 * @2016年4月26日 上午11:43:19
 */
public class PublicDataUtils {

	
	public static  List<String> getLibraryLevels(){
		List<String> list = new ArrayList<String>();
		list.add("高校馆");
		list.add("高校馆I");
		list.add("高校馆II");
		list.add("高校馆III");
		list.add("公共图书馆");
		list.add("公共图书馆I");
		list.add("公共图书馆II");
		list.add("公共图书馆III");
		list.add("公共图书馆IV");
		list.add("社会馆");
		list.add("中小学馆");
		list.add("中小学馆I");
		list.add("中小学馆II");
		list.add("中小学馆III");
		list.add("社区书屋");
		list.add("农家书屋");
		list.add("体验馆");
		return list;
	}
	
	public static  List<Map<String, String>> getNewsType(){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		Map<String, String>  map = new HashMap<String, String>();
		User user = ShiroUtils.getCurrentUser();
		String role = ShiroUtils.getCurrentUserRoleLevel(user);
		if(Common.ROLE_FIRST_LEVLE.equals(role)){
			map.put("key", "1");
			map.put("val", "平台资讯");
			list.add(map);
		}
		map = new HashMap<String, String>();
		map.put("key", "2");
		map.put("val", "单位资讯");
		list.add(map);
		
		map = new HashMap<String, String>();
		map.put("key", "3");
		map.put("val", "图书馆资讯");
		list.add(map);
		return list;
	}
	
	public static  List<Map<String, String>> getNoticeType(){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		Map<String, String>  map = new HashMap<String, String>();
		map = new HashMap<String, String>();
		map.put("key", "4");
		map.put("val", "消息");
		list.add(map);
		return list;
	}
	
	public static  Map<String, String> getNewsSource(){
		Map<String, String>  map = new HashMap<String, String>();
		map.put("1", "云图书馆");
		map.put("2", "客户名称");
		map.put("3", "图书馆名称");
		return map;
	}
	
	public static String changeNumber(int number) {
		String str = String.valueOf(number);
		String strNum = "一二三四五六七八九";
		//大于十的数字转化
		if(number > 9)
		{
			String cto = "十";
			if(number < 20){
				if(number == 10){
					return cto;
				}else{
					//十位数的转化
					return cto+=unitNumber(String.valueOf(number-10),strNum);
				}
			}else{
				switch(number){
				case 20:
					return "二十";
				case 30:
					return "三十";
				case 40:
					return "四十";
				case 50:
					return "五十";
				default:
					//多位数的转化
					cto = unitNumber(String.valueOf(number).substring(0,1),strNum)+cto;
					cto += unitNumber(String.valueOf(number).substring(1,2),strNum);
					return cto;
				}
				
			}
			
		}
		//小于10的数字转化
		return unitNumber(str, strNum);
	}
	//正对单个数字的转化
	public static String unitNumber(String str,String strNum) {
		if("9".equals(str)){
			return "九";
		}
		for (int i = 0; i < str.length(); i++) {
			for (int j = 0; j < strNum.length(); j++) {
				if(String.valueOf(str.charAt(i)).equals(String.valueOf(j))){
					str = str.replace(str.charAt(i),strNum.charAt(j-1));
				}
			}
		}
		return str;
	}
	
}
