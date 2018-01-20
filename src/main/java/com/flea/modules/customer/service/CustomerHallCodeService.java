package com.flea.modules.customer.service;


import com.flea.common.pojo.Page;
import com.flea.common.service.BaseService;
import com.flea.modules.customer.pojo.CustomerHallCode;

/**
 * 客户分配馆号Service
 * @author bruce
 * @version 2016-05-09
 */

public interface CustomerHallCodeService extends BaseService<CustomerHallCode,Integer> {

   	Page<CustomerHallCode> find(Page<CustomerHallCode> page,CustomerHallCode CustomerHallCode);
	
}
