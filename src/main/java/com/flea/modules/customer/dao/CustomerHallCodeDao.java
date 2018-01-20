package com.flea.modules.customer.dao;

import org.springframework.stereotype.Repository;

import com.flea.common.dao.BaseDao;
import com.flea.modules.customer.pojo.CustomerHallCode;

/**
 * 客户分配馆号DAO接口
 * @author bruce
 * @version 2016-05-09
 */
@Repository
public interface CustomerHallCodeDao extends BaseDao<CustomerHallCode,Integer> {
	
}
