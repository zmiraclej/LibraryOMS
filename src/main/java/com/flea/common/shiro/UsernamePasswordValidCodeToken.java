package com.flea.common.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * @author Administrator
 * 保存验证码得token
 */
public class UsernamePasswordValidCodeToken extends UsernamePasswordToken{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2524724495669953759L;
	//用于存储用户输入的校验码  
	private String hallCode;
	private String validCode;
	public UsernamePasswordValidCodeToken(String hallCode,String username,String password,String validCode,boolean rememberMe,String host) {
		// TODO Auto-generated constructor stub
		super(username,password,rememberMe,host); 
		this.hallCode = hallCode;
        this.validCode=validCode;  
	}
	public String getValidCode() {
		return validCode;
	}
	public void setValidCode(String validCode) {
		this.validCode = validCode;
	}
	public String getHallCode() {
		return hallCode;
	}
	public void setHallCode(String hallCode) {
		this.hallCode = hallCode;
	}
	
}