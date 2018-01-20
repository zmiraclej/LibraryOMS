package com.flea.common;

import com.flea.modules.system.util.DepartmentUtil;





public class Common implements Constants {
	public static final String CODE_DICT_VAR_NAME = "_cd";
	
	/* 系统功能根目录ID描述字符串，注意值保持和config配置文件中设置同步*/
	public static final String FUNCTION_ROOT_ID = "functionRootId";
	/* 系统部门根目录ID描述字符串，注意值保持和config配置文件中设置同步*/	
	public static final String DEPARTMENT_ROOT_ID = "departmentRootId";
	/*系统超级管理员角色根目录ID描述字符串，注意值保持和config配置文件中设置同步*/	
	public static final String SUPERMANAGER_ROLE_ID = "superManagerRoleId";	
	/*系统超级管理员ID描述字符串，注意值保持和config配置文件中设置同步*/	
	public static final String SUPERMANAGER_ID = "superManagerId";

	/*上传文件目录，注意值保持和config配置文件中设置同步*/
	public static final String UPLOAD_DIRECTORY = "uploadDirectory";	
	/*上传临时文件目录，注意值保持和config配置文件中设置同步*/
	public static final String UPLOAD_TEMP_DIRECTORY = "uploadTempDirectory";
	/*导入系统的模板文件目录，注意值保持和config配置文件中设置同步*/
	public static final String  TEMPLATE_FILE_DIRECTORY = "templateFileDirectory";
	
	public static final String  ROLE_FIRST_LEVLE="0";
	
	public static final String  ROLE_SECOND_LEVLE="1";
	
	/**
	 * 登陆用户map
	 */
	public static final String LOGIN_USER_MAP = "login_user_map";
	

	/**
	 * 默认密码
	 */
	public static final String USER_DEFAULT_PASSWORD = "123456";
	/**
	 * 默认密码长度
	 */
	public static final short USER_PASSWORD_LENGTH = 6;
	public static final String USER_ADMIN_NAME = "admin";
	/*前台当前结算账户*/
	public static final String  CURRENT_CARD = "current_card";
	
	public static final String CURRENT_DEPT = "current_dept";
	/*前台当前结算账户*/
	public static final Integer  PAGE_SIZE =10;
	
	public static final String DEFAULT_AREA = "DEFAULT_AREA";
	/**
	 * 距离 全国
	 */
	public static final String VISIT_AREA_COUNTRY = "visit_area_country";
	/**
	 * 距离 同市
	 */
	public static final String  VISIT_AREA_CITY = "visit_area_city";
	/**
	 * 距离 同省
	 */
	public static final String  VISIT_AREA_PROVINCE = "visit_area_province";
	/**距离 区域
	 * 
	 */
	public static final String  VISIT_AREA_DISTRICT = "visit_area_district";
	public static final String  SHOW_TYPE_LIST = "list";
	public static final String SHOW_TYPE_GRID = "grid";
	
	public static final String PAGE_DEPT = "p_dept";
	
	/**
	 * 云图书平台占用平台编号
	 */
	public static final String TZPT_OCCUPY_DEPCODE = "YTSG";
	/**
	 * 云图书平台占用平台编号
	 */
	public static final int TZPT_OCCUPY_DEPCODE_NUMBER = DepartmentUtil.depcodeToNumber(TZPT_OCCUPY_DEPCODE);
	/**
	 * 当前应用为初始化状态
	 */
	public static final byte APP_STATUS_INIT = 0;

	
	/**
	 * 跳蚤平台占用平台编号
	 */
	public static final String BEGIN_HALL_CODE = "AABB";
	
}
