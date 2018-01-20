package com.flea.common.service;


import com.flea.common.pojo.Page;
import com.flea.common.service.BaseService;
import com.flea.common.pojo.Log;

/**
 * 系统日志Service
 * @author bruce
 * @version 2016-09-01
 */

public interface LogService extends BaseService<Log,Integer> {

   	Page<Log> find(Page<Log> page,Log log);
	
}
