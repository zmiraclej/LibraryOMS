package com.flea.common.tag;

import java.util.List;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

public class ContentHide implements TemplateMethodModelEx{

	@Override
	public Object exec(List arguments) throws TemplateModelException {
		int length = Integer.parseInt(arguments.get(1).toString());
		Object co = arguments.get(0);
		if (co==null) {
			return arguments.get(0);
		}
		String content = arguments.get(0).toString();
		content = getStrByLength(content, length);
		return content;
	}
	/**
	    * 功能：根据限制长度截取字符串（字符串中包括汉字，一个汉字等于两个字符）
	    * @param strParameter 要截取的字符串
	    * @param limitLength 截取的长度
	    * @return 截取后的字符串
	    */
	public String getStrByLength(String strParameter , int limitLength)
	{
		
	    String return_str=strParameter;//返回的字符串
	    int temp_int=0;//将汉字转换成两个字符后的字符串长度
	    int cut_int=0;//对原始字符串截取的长度
	    char[] chars=strParameter.toCharArray();//将字符串转换成字符数组
	    for (int i = 0; i < chars.length; i++) {
	    	int clength = Character.valueOf(chars[i]).toString().getBytes().length;
	    	if (clength>1) {
	    		temp_int+=2;
			}else{
				temp_int++;
			}
	    	cut_int++;
	    	if (temp_int >= limitLength) {
	    		 return return_str.substring(0,cut_int)+"...";
			}
		}
	    return return_str;
	}
}
