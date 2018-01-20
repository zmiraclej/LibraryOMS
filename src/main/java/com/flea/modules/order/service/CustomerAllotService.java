package com.flea.modules.order.service;

import java.util.Map;

import com.flea.common.pojo.Page;
import com.flea.common.service.BaseService;
import com.flea.modules.customer.pojo.Customer;
import com.flea.modules.customer.pojo.CutomerLibrary;
import com.flea.modules.order.pojo.CustomerAllot;

public interface CustomerAllotService extends BaseService<CustomerAllot,Integer> {
	/**
	 * 查询分页
	 * @param page
	 * @param search
	 * @param customer
	 * @return
	 */
   	Page<CustomerAllot> find(Page<CustomerAllot> page,String search, CustomerAllot customer);
}
