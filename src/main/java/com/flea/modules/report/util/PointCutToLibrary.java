package com.flea.modules.report.util;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class PointCutToLibrary {
	@Pointcut("execution(* com.flea.modules.news.service.impl.ActivityServiceImpl.*(..))")
	public void tt (){}
	@Before("tt()")
	public void isLib() {
		System.err.println("-----------------------------------------------I'm here in");
	}
}
