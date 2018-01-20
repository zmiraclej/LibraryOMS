package com.flea.common.util;

import java.security.MessageDigest;
import java.util.Random;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;

import com.taobao.api.ApiException;



public class PasswordHelper {
	public final static String MD5(String s) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};       
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    } 
	
	 //生成随机数字和字母,  
    public static String getStringRandom(int length) {  
        String val = "";  
        Random random = new Random();  
        String chars = "abcdefghjkmnpqrstvwxyz";
        
        //参数length，表示生成几位随机数  
//        for(int i = 0; i < length; i++) {  
//            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";  
//            //输出字母还是数字  
//            if( "char".equalsIgnoreCase(charOrNum) ) {  
//                //输出是大写字母还是小写字母  
////              int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;  
//                val += chars.charAt(random.nextInt(24)); 
//            } else if( "num".equalsIgnoreCase(charOrNum) ) {  
//                val += String.valueOf(random.nextInt(10));  
//            }  
//        }  
        //密码设置小写字母加三位数字
        for (int i = 0; i < length - 3; i++) {
        	val += chars.charAt(random.nextInt(22));
		}
        for (int i = 0; i < 3; i++) {
        	val += String.valueOf(random.nextInt(10)); 
		}
        return val;  
    }  
    
    public static void sendPassword(String hallCode,String phone,String passwd) {
		String content ="{hallCode:'"+hallCode+"',name:'admin',passwd:'"+passwd+"'}";
		try {
			SendMessages.sendMessages(phone,content,"SMS_13191460");
		} catch (ApiException e) {
			e.printStackTrace();
		}
	 	Session currentSession = SecurityUtils.getSubject().getSession();
	 	currentSession.setAttribute(hallCode+"userPassword","");
	}
    
}
