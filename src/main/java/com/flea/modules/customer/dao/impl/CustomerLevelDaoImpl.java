package com.flea.modules.customer.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.flea.common.dao.impl.BaseDaoImpl;
import com.flea.modules.customer.dao.CustomerLevelDao;
import com.flea.modules.customer.pojo.Customer;
import com.flea.modules.customer.pojo.CustomerLevel;
@Repository
public class CustomerLevelDaoImpl extends BaseDaoImpl<CustomerLevel,Integer> implements CustomerLevelDao  {

	@Override
	public List<CustomerLevel> getUnsedCustomerLevelList() {
		List<CustomerLevel> list = this.getListByHQL("From CustomerLevel Where isUsed = 0 ", null);
		return list;
	}

	@Override
	public List<CustomerLevel> getUnsedCustomerLevelList(int customerID) {
		//客户类型可用列表 剔除已使用的国家级类型
		List params = new ArrayList();
		params.add(customerID);
		List<CustomerLevel> list = this.getListByHQL("From CustomerLevel Where isUsed = 0 or customerID = ? ", params);
		return list;
	}
}
