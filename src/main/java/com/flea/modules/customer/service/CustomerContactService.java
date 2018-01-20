package com.flea.modules.customer.service;


import com.flea.common.pojo.Page;
import com.flea.common.service.BaseService;
import com.flea.modules.customer.pojo.CustomerContact;

/**
 * 联系人Service
 * @author bruce
 * @version 2016-08-01
 */

public interface CustomerContactService extends BaseService<CustomerContact,Integer> {

   	Page<CustomerContact> find(Page<CustomerContact> page,CustomerContact customerContact);
	
}
