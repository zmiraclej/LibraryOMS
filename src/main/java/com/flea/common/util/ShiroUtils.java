/**
 * Copyright &copy; 2014-2015.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.flea.common.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;

import com.flea.common.Common;
import com.flea.common.Constants;
import com.flea.common.pojo.Role;
import com.flea.common.pojo.User;


/**
 * @author Bruce Tie
 * @date 2015年3月24日 下午5:11:29 
 * @function
 */
public class ShiroUtils {

	/**
	 * @功能: 获取当前登录用户
	 * @作者: Bruce Tie
	 * @创建日期: 2014年5月21日 下午7:54:41
	 */
	public static User getCurrentUser() {
		return (User) SecurityUtils.getSubject().getSession().getAttribute(Constants.CURRENT_USER);
	}
	
	public static String getCurrentUserRoleLevel(User user) {
		Role role =  user.getRoles().get(0);
		return role.getLevel();
	}
	
	
	/**
	 * 通过SessionId踢用户
	 * @param sessionId
	 */
	public static void userLogout(Serializable sessionId){
		org.apache.shiro.mgt.SecurityManager securityManager = SecurityUtils.getSecurityManager();
		Subject.Builder builder = new Subject.Builder(securityManager);
		builder.sessionId(sessionId);
		Subject subject = builder.buildSubject();
		if (null != subject) {
			try {
				subject.logout();
			} catch (SessionException e) {
			}
		}
	}
	
	/**
	 * 取出sessionId
	 * @param sessionId
	 */
	public static String getConextMapSessionId(HttpSession httpSession){
		User user =getCurrentUser();
		Map<Integer, Serializable> sessionMap = (Map<Integer, Serializable>)httpSession.getServletContext().getAttribute(Common.LOGIN_USER_MAP);
		if(sessionMap==null) sessionMap = new HashMap<Integer, Serializable>();
		Serializable userSessionId = sessionMap.get(user.getId());
		return userSessionId!=null?userSessionId.toString():"";
	}
	
	/**
	 * 设置盘点重复登录sessionMap<userId,sessionId>
	 * @param sessionId
	 */
	public static void setConextMapSessionId(HttpSession httpSession){
		User user =getCurrentUser();
		Map<Integer, Serializable> sessionMap = (Map<Integer, Serializable>)httpSession.getServletContext().getAttribute(Common.LOGIN_USER_MAP);
		if(sessionMap==null) sessionMap = new HashMap<Integer, Serializable>();
		sessionMap.put(user.getId(),httpSession.getId());
		httpSession.getServletContext().setAttribute(Common.LOGIN_USER_MAP, sessionMap);
	}
	
	/**
	 * 获得用户远程地址
	 */
	public static String getRemoteAddr(HttpServletRequest request){
		String remoteAddr = request.getHeader("X-Real-IP");
        if (StringUtils.isNotBlank(remoteAddr)) {
        	remoteAddr = request.getHeader("X-Forwarded-For");
        }else if (StringUtils.isNotBlank(remoteAddr)) {
        	remoteAddr = request.getHeader("Proxy-Client-IP");
        }else if (StringUtils.isNotBlank(remoteAddr)) {
        	remoteAddr = request.getHeader("WL-Proxy-Client-IP");
        }
        return remoteAddr != null ? remoteAddr : request.getRemoteAddr();
	}
	
	
}
