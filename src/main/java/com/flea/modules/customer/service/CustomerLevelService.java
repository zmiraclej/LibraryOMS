package com.flea.modules.customer.service;

import com.flea.common.service.BaseService;
import com.flea.modules.customer.pojo.Customer;
import com.flea.modules.customer.pojo.CustomerLevel;
/**
 * 客户等级分类Service
 * @author bruce
 * @version 2016-05-09
 */
public interface CustomerLevelService extends BaseService<CustomerLevel,Integer>{

	/**
	 * 获取客户分级等级
	 * @param customer
	 * @return 
	 */
	public Integer getLevelByCustomer(Customer customer);

	/**
	 * 获取客户分级对象
	 * @param customer
	 * @return 
	 */
	public CustomerLevel getCustomerLevelByCustomer(Customer customer);

}
