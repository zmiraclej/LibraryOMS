/**  
* @Package com.flea.common.shiro
* @Description: TODO
* @author bruce
* @date 2016年6月17日 下午6:20:52
* @version V1.0  
*/ 
package com.flea.common.shiro;

import java.io.Serializable;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;


import com.flea.common.Common;
import com.flea.common.Constants;
import com.flea.common.pojo.User;



/**
 * 创建、销毁session
 * @author bruce
 * @2016年6月17日 下午6:20:52
 */
public class KickOutSessionListener implements HttpSessionListener  {
	

	/* 
	 * 将创建的session放入map,以便在重复登录的时候取出销毁
	 * @see javax.servlet.http.HttpSessionListener#sessionCreated(javax.servlet.http.HttpSessionEvent)
	 */
	@Override
	public void sessionCreated(HttpSessionEvent httpSessionEvent) {
		 MySessionContext.AddSession(httpSessionEvent.getSession());
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSessionListener#sessionDestroyed(javax.servlet.http.HttpSessionEvent)
	 */
	@Override
	public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
		  HttpSession session = httpSessionEvent.getSession();
	    User user =(User)session.getAttribute(Constants.CURRENT_USER);
		Map<String, Serializable> sessionMap = (Map<String, Serializable>)session.getServletContext().getAttribute(Common.LOGIN_USER_MAP);
		if(sessionMap==null||user==null)return;
		Serializable  sessionId = sessionMap.get(user.getId());
		if(sessionId!=null){
			sessionMap.remove(user.getId());
		}
	}	

	

}
