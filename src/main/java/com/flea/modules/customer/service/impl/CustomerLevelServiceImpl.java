package com.flea.modules.customer.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.flea.common.service.impl.BaseServiceImpl;
import com.flea.modules.customer.dao.CustomerLevelDao;
import com.flea.modules.customer.pojo.Customer;
import com.flea.modules.customer.pojo.CustomerLevel;
import com.flea.modules.customer.service.CustomerLevelService;
/**
 * 客户等级Service
 * @author bruce
 * @version 2016-05-09
 */
@Service
public class CustomerLevelServiceImpl extends BaseServiceImpl<CustomerLevel,Integer> implements CustomerLevelService {

	@Autowired
	private CustomerLevelDao customerLevelDao;
	/**
	 * 获取客户分级等级
	 */
	@Override
	public Integer getLevelByCustomer(Customer customer) {
		int id = customer.getLevelId();
		CustomerLevel customerLevel = customerLevelDao.getOne(id);
		return customerLevel.getLevel();
	}
	
	public CustomerLevel getCustomerLevelByCustomer(Customer customer) {
		int id = customer.getLevelId();
		CustomerLevel customerLevel = customerLevelDao.getOne(id);
		return customerLevel;
	}
}
