/**  
* @Package com.flea.common.action
* @Description: TODO
* @author bruce
* @date 2016年9月12日 下午2:55:41
* @version V1.0  
*/ 
package com.flea.common.action;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bruce
 * @2016年9月12日 下午2:55:41
 */
public abstract class BaseAction {
	
	/**
	 * 日志对象
	 */
	protected Logger log = LoggerFactory.getLogger(getClass());
			
	/**
	 * 发送json。使用UTF-8编码。
	 * 
	 * @param response
	 *            HttpServletResponse
	 * @param text
	 *            发送的字符串
	 */
	public  void renderJson(HttpServletResponse response, String text) {
		response.setContentType("text/html");  
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		try {
			response.getWriter().print(text);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
