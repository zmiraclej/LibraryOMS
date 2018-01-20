package com.flea.modules.order.service.impl;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flea.common.Common;
import com.flea.common.pojo.Page;
import com.flea.common.service.BaseService;
import com.flea.common.service.impl.BaseServiceImpl;
import com.flea.modules.customer.dao.CustomerDao;
import com.flea.modules.customer.pojo.Customer;
import com.flea.modules.customer.pojo.CutomerLibrary;
import com.flea.modules.order.dao.CustomerAllotDao;
import com.flea.modules.order.pojo.CustomerAllot;
import com.flea.modules.order.pojo.OrdersDetail;
import com.flea.modules.order.service.CustomerAllotService;
import com.flea.modules.order.service.OrdersDetailService;
@Service
public class CustomerAllotServiceImpl extends BaseServiceImpl<CustomerAllot, Integer> implements CustomerAllotService {
	
	@Autowired
	private CustomerAllotDao customerAllotDao;
	
	
	/**
	 * 查询分页
	 * @param page
	 * @param search
	 * @param customer
	 * @return
	 */
   	public Page<CustomerAllot> find(Page<CustomerAllot> page,String search, CustomerAllot customer) {
		DetachedCriteria dc = customerAllotDao.createDetachedCriteria();
		if(StringUtils.isNotEmpty(customer.getName())){
			dc.add(Restrictions.eq("name", customer.getName()));
		}
		if(StringUtils.isNotBlank(search)){
			dc.add(Restrictions.or(Restrictions.like("name", "%"+search+"%"),Restrictions.like("hallCode", "%"+search+"%")));
		}
		dc.add(Restrictions.eq("isEffective", Common.FLAG_ACTIVATION));
		return customerAllotDao.find(page, dc);
   	}
}
