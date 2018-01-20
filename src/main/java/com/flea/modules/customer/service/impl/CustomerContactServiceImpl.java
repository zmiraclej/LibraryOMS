package com.flea.modules.customer.service.impl;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flea.common.pojo.Page;
import com.flea.common.service.impl.BaseServiceImpl;
import com.flea.modules.customer.dao.CustomerContactDao;
import com.flea.modules.customer.pojo.CustomerContact;
import com.flea.modules.customer.service.CustomerContactService;

/**
 * 联系人Service
 * @author bruce
 * @version 2016-08-01
 */
 @Service
public class CustomerContactServiceImpl extends BaseServiceImpl<CustomerContact,Integer> implements CustomerContactService{

	@Autowired
	private CustomerContactDao customerContactDao;
	
	
	@Autowired
	public  CustomerContactServiceImpl(CustomerContactDao customerContactDao) {
		super(customerContactDao);
		this.customerContactDao = customerContactDao;
	}
	
	@Override
	public Page<CustomerContact> find(Page<CustomerContact> page,CustomerContact customerContact) {
		DetachedCriteria dc = customerContactDao.createDetachedCriteria();
		return customerContactDao.find(page, dc);
	}
	
}
