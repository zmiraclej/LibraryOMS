package com.flea.modules.customer.dao;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Repository;

import com.flea.common.dao.BaseDao;
import com.flea.common.pojo.User;
import com.flea.modules.customer.pojo.Customer;
import com.flea.modules.customer.pojo.CutomerLibrary;

/**
 * 客户DAO接口
 * @author Bruce
 * @version 2016-04-15
 */
@Repository
public interface CustomerDao extends BaseDao<Customer,Integer> {
	
	String findMaxLibraryCode();
	/**
	 * 
	 * @method:getId	查找对应的区域ID
	 * @Description:getId
	 * @author: HeTao
	 * @date:2016年8月18日 下午7:23:23
	 * @param:@param name
	 * @param:@param t
	 * @param:@return
	 * @return:Class<T>
	 */
	String getId(String name, Class<T> t);
	/**
	 * 
	 * @method:deleteLib	删除对应馆
	 * @Description:deleteLib
	 * @author: HeTao
	 * @date:2016年8月20日 下午5:07:33
	 * @param:@param id
	 * @return:void
	 */
	boolean deleteLib(Integer id);
	
	boolean  updateHallCodeById(String hallCode,Integer id);
	
	boolean updateModifyInfoById(Integer id);
	/**
	 * 客户代码是否已存在
	 */	
	boolean isExistCustomer(String customerCode);
}
