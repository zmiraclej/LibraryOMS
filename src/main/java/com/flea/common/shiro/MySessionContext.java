/**  
* @Package com.flea.common.shiro
* @Description: TODO
* @author bruce
* @date 2016年6月24日 下午6:50:57
* @version V1.0  
*/ 
package com.flea.common.shiro;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

/**
 * session管理
 * @author bruce
 * @2016年6月24日 下午6:50:57
 */
public class MySessionContext {

	 private static HashMap<String,HttpSession> mymap = new HashMap<String,HttpSession>();
	    public static synchronized void AddSession(HttpSession session) {
	        if (session != null) {
	            mymap.put(session.getId(), session);
	        }
	    }
	    public static synchronized void DelSession(HttpSession session) {
	        if (session != null) {
	            mymap.remove(session.getId());
	        }
	    }
	    public static synchronized HttpSession getSession(String session_id) {
	        if (session_id == null)
	        return null;
	        return (HttpSession) mymap.get(session_id);
	    }
}
