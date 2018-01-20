package com.flea.common.util;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class RequestContextHolderUtil {
	public static HttpServletRequest getRequest()
	  {
	    HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
	    request.getSession().getServletContext();
	    return request;
	  }

	  public static HttpSession getSession()
	  {
	    HttpSession session = getRequest().getSession();
	    return session;
	  }

	  public static ServletContext getServletContext()
	  {
	    ServletContext servletContext = getSession().getServletContext();
	    return servletContext;
	  }
	  
}
