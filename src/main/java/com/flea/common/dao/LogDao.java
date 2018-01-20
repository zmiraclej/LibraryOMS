package com.flea.common.dao;

import org.springframework.stereotype.Repository;

import com.flea.common.dao.BaseDao;
import com.flea.common.pojo.Log;

/**
 * 系统日志DAO接口
 * @author bruce
 * @version 2016-09-01
 */
@Repository
public interface LogDao extends BaseDao<Log,Integer> {
	
}
