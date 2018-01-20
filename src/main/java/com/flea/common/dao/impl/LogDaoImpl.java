package com.flea.common.dao.impl;

import org.springframework.stereotype.Repository;

import com.flea.common.dao.impl.BaseDaoImpl;
import com.flea.common.dao.LogDao;
import com.flea.common.pojo.Log;

/**
 * 系统日志DAO接口
 * @author bruce
 * @version 2016-09-01
 */
@Repository
public class LogDaoImpl extends BaseDaoImpl<Log,Integer> implements LogDao{
	
}
