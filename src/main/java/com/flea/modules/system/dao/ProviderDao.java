package com.flea.modules.system.dao;

import org.springframework.stereotype.Repository;

import com.flea.common.dao.BaseDao;
import com.flea.modules.system.pojo.Provider;

/**
 * 供应商管理DAO接口
 * @author Bruce
 * @version 2016-01-07
 */
@Repository
public interface ProviderDao extends BaseDao<Provider,Integer> {
	
}
