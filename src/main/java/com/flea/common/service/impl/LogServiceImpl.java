package com.flea.common.service.impl;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flea.common.dao.LogDao;
import com.flea.common.pojo.Log;
import com.flea.common.pojo.Page;
import com.flea.common.service.LogService;

/**
 * 系统日志Service
 * @author bruce
 * @version 2016-09-01
 */
 @Service
public class LogServiceImpl extends BaseServiceImpl<Log,Integer> implements LogService{

	@Autowired
	private LogDao logDao;
	
	
	@Autowired
	public  LogServiceImpl(LogDao logDao) {
		super(logDao);
		this.logDao = logDao;
	}
	
	@Override
	public Page<Log> find(Page<Log> page,Log log) {
		DetachedCriteria dc = logDao.createDetachedCriteria();
//		if(StringUtils.isNotEmpty(log.getName())){
//			dc.add(Restrictions.eq("name", log.getName()));
//		}
		return logDao.find(page, dc);
	}
	
}
