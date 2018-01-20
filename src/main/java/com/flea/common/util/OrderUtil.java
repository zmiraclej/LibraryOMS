package com.flea.common.util;

import java.math.BigInteger;
import java.util.regex.Pattern;

import com.flea.common.dao.BaseDao;
import com.flea.modules.order.dao.impl.OrdersDaoImpl;
/** 
 * @ClassName: OrderUtil 
 * @author : Wuhua 
 * @Description: 订单号生成工具 
 * 
 */  
public class OrderUtil {

	private final static Integer orderLength = 11;
	
	public static boolean validateOrder(String order) {
		
		if(null == order || orderLength < order.length()) {
			return false;
		}
		if (Pattern.compile("[0-9]*").matcher(order).matches()) { 
			return true;
		}
		return false;
	}
	
	public static boolean validateOrder(int order) {
		return false;
	}
	
	public static Long makeOrder(int customerID, BaseDao baseDao) {
		BaseDao baseDaos = baseDao;
		BigInteger orderNext = (BigInteger) baseDao.getBySQL("select AUTO_INCREMENT from INFORMATION_SCHEMA.TABLES where TABLE_NAME='ebook_orders' limit 1", null);
		String newString = String.format("%09d", orderNext);
		newString =1+newString; //渠道号
		return Long.parseLong(newString);
	}

}
