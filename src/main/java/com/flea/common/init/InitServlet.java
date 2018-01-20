package com.flea.common.init;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class InitServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3369987794093216994L;
	
	
	private static final Logger logger = Logger.getRootLogger();
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		try {
//			WebApplicationContext app = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());
//			SystemAreasServiceImpl areaService = app.getBean(SystemAreasServiceImpl.class);
//			logger.info("系统初始化开始.....................");
//			areaService.initArea();
			logger.info("初始化字典表.....................");
			logger.info("系统初始化结束.....................");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("----------");
		resp.setContentType("text/html");

		PrintWriter out = resp.getWriter();

		out.println("<HTML>");

		out.println("<HEAD><TITLE>Hello World</TITLE></HEAD>");

		out.println("<BODY>");

		out.println("<BIG>Hello World</BIG>");

		out.println("</BODY></HTML>");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("----11111111111-----");
		super.doPost(req, resp);
	}
	
	
}
