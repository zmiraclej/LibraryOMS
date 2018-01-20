package com.flea.modules.system.util;

import java.util.Random;

import org.apache.commons.lang.StringUtils;

public class DepartmentUtil {
	/**
	 * 将指定的自然数转换为26进制表示。映射关系：[0-26] ->[A-Z]。
	 * @param n 自然数（如果无效，则返回空字符串）。
	 * @return 26进制表示。
	 */
	public static String numberToDepcode(int n,int bit){
		String s = "";
		while (bit>0){
			int m = (n % 26);
			s = (char)(m + 65) + s;
			n = ((n - m) / 26);
			bit--;
		}
		return s;
	}
	public static int depcodeToNumber(String str){
		if (StringUtils.isBlank(str)) {
			return 0;
		}
		int n = 0;
		char[] chars = str.toCharArray();
		for(int i = chars.length - 1,j=1; i >= 0;i--){
			char c = chars[i];
			if (c < 'A' || c > 'Z') return 0;
			n += ((int)c - 65)*j;
			j *=26;
		}
		return n;
	}
	
	public static String createUserPassword(int lenght) {
		String str = "";
		Random random = new Random();
		for (int i = 0; i < lenght; i++) {
			int r = random.nextInt(62);
			if(r<10){
				str+=r;
			}else if(r>=10&&r<36){
				str+=(char)(r+55);
			}else{
				str+=(char)(r+61);
			}
		}
		return str;
	}

	
	   /**
     * 得到两点间的距离 米
     * 
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return
     */ 
    public static double getDistanceOfMeter(double lat1, double lng1, 
            double lat2, double lng2) { 
        double radLat1 = rad(lat1); 
        double radLat2 = rad(lat2); 
        double a = radLat1 - radLat2; 
        double b = rad(lng1) - rad(lng2); 
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) 
                + Math.cos(radLat1) * Math.cos(radLat2) 
                * Math.pow(Math.sin(b / 2), 2))); 
        s = s * EARTH_RADIUS; 
        s = Math.round(s * 10000) / 10; 
        return s; 
    } 
       
    private static double rad(double d) { 
        return d * Math.PI / 180.0; 
    } 
    
    /**
     * 地球半径：6378.137KM
     */ 
    private static double EARTH_RADIUS = 6378.137;  
    
    
   /**
    * 字符串反转 
    * @param libCode
    * @return
    */
   public static String reverseCode (String libCode) {
	   	String reverseCode = "";
		for(int i = libCode.length()-1;i>=0;i--) {
		   	  char vChar = libCode.charAt(i);
		   	  reverseCode += vChar;
		}
		return reverseCode;
	   
   }
}
