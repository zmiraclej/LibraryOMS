package com.flea.modules.customer.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.flea.common.dao.BaseDao;
import com.flea.common.pojo.User;
import com.flea.modules.customer.pojo.CustomerContact;

/**
 * 联系人DAO接口
 * @author bruce
 * @version 2016-08-01
 */
@Repository
public interface CustomerContactDao extends BaseDao<CustomerContact,Integer> {
	
	List<CustomerContact> findByCustomerId(Integer customerId);
}
