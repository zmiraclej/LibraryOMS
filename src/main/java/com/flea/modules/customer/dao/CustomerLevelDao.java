package com.flea.modules.customer.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.flea.common.dao.BaseDao;
import com.flea.modules.customer.pojo.CustomerLevel;
import com.flea.modules.customer.pojo.CutomerLibrary;
@Repository
public interface CustomerLevelDao extends BaseDao<CustomerLevel,Integer> {
	
	/**
	 * 获取未占用客户分类列表
	 * @return
	 */
	public List<CustomerLevel> getUnsedCustomerLevelList();
	
	/**
	 * 获取未占用客户分类列表 ,排除客户自己
	 * @return
	 */
	public List<CustomerLevel> getUnsedCustomerLevelList(int customerID);
}
