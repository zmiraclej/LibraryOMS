package com.flea.modules.customer.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flea.common.pojo.Page;
import com.flea.common.service.impl.BaseServiceImpl;
import com.flea.modules.customer.dao.CustomerHallCodeDao;
import com.flea.modules.customer.pojo.CustomerHallCode;
import com.flea.modules.customer.service.CustomerHallCodeService;

/**
 * 客户分配馆号Service
 * @author bruce
 * @version 2016-05-09
 */
 @Service
public class CustomerHallCodeServiceImpl extends BaseServiceImpl<CustomerHallCode,Integer> implements CustomerHallCodeService{

	@Autowired
	private CustomerHallCodeDao CustomerHallCodeDao;
	
	
	@Autowired
	public  CustomerHallCodeServiceImpl(CustomerHallCodeDao CustomerHallCodeDao) {
		super(CustomerHallCodeDao);
		this.CustomerHallCodeDao = CustomerHallCodeDao;
	}
	
	@Override
	public Page<CustomerHallCode> find(Page<CustomerHallCode> page,CustomerHallCode CustomerHallCode) {
		return null;
	}
	
}
