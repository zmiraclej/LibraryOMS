package com.flea.common;

/**
 * 
 * @author brucetie
 * @date 2014年5月27日 下午3:53:29
 * @version V1.0
 * @function
 */
public interface Constants {
	
	
	public static final Short FLAG_ACTIVATION = Short.valueOf((short) 1);
	/*删除标志*/
	public static final Short FLAG_DELETED = Short.valueOf((short) 0);
	/*未删除标志*/
	/*public static final Short FLAG_UNDELETED = Short.valueOf((short) 1);*/
	
	
	public static final String FLAG_ENABLE = "1";
	
	public static final String FLAG_DISABLE = "0";
	
	/*停用标志*/
	public static final String FLAG_STOPED= "2";
	
	/*审核标志*/
	public static final String FLAG_AUDITED = "3";
	
	public static final String BORROWER_MODEL = "19";
	
	
	/**
	 * 停用
	 */
	public static final Short FLAG_STOP = Short.valueOf((short) 0);
	
	public static final Integer FLAG_TOP= 1;
	
	
	public static final String DATE_FORMAT_FULL = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_FORMAT_YMD = "yyyy-MM-dd";
	public static final String COMPANY_LOGO = "app.logoPath";
	public static final String COMPANY_NAME = "app.companyName";
	public static final String DEFAULT_COMPANYNAME = "跳蚤平台信息科技有限公司";
	public static final String LOGIN_USER_SESSION = "login_user_session";
	public static final String SITEURL = "SITEURL";
	
	public static final String CODE_DICT_VAR_NAME = "_cd";
	
	public static final int PAGE_SIZE =10;
	
	/***************** session key *****************/
	public static final String CURRENT_USER = "CURRENT_USER";
	public static final String ENTER_COUNT = "ENTER_COUNT";
	public static final String NEED_CAPTCHA = "NEED_CAPTCHA";
	
	/**
	 * 默认密码长度
	 */
	public static final short USER_PASSWORD_LENGTH = 6;
	public static final String USER_ADMIN_NAME = "admin";
	

}