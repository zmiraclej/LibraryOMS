package com.flea.modules.customer.service;


import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.flea.common.pojo.Page;
import com.flea.common.service.BaseService;
import com.flea.modules.customer.pojo.Customer;
import com.flea.modules.customer.pojo.CustomerLevel;
import com.flea.modules.customer.pojo.CutomerLibrary;


/**
 * 客户Service
 * @author Bruce
 * @version 2016-04-15
 */

public interface CustomerService extends BaseService<Customer,Integer> {

	/**
	 * 查询分页
	 * @param page
	 * @param search
	 * @param customer
	 * @return
	 */
	public Page<Customer> find(Page<Customer> page,String search, Customer customer);
   	
   	/**
   	 * 客户管理分配馆号
   	 * @param s_code
   	 * @param prevCode
   	 * @param number
   	 * @param modify
   	 * @param pn
   	 * @return
   	 */
   	public Map<String, String> assignLibraryCode(String s_code,String prevCode,Integer number,Boolean modify);

	/**
	 * 
	 * @method:saveCustomer	保存客户的资料
	 * @Description:getCustomer
	 * @param cl 
	 * @date:2016年8月18日 上午11:45:19
	 * @param:@param customerInfo
	 * @param:@return
	 * @return:Customer
	 */
	public boolean saveCustomer(Customer customer, CutomerLibrary cl);

	/**
	 * 
	 * @method:deleteLib	删除对应馆
	 * @Description:deleteLib
	 * @author: HeTao
	 * @date:2016年8月20日 下午5:07:33
	 * @param:@param id
	 * @return:void
	 */
	public boolean deleteLib(Integer id);
	
	public Customer  findByCustomerCode(String hallCode);
	
	public boolean  updateHallCodeById(String hallCode,Integer id);
	
	/**
	 * 更新记录日期和操作员
	 * @method:updateModifyInfoById 
	 * @Description: 
	 */	
	public boolean updateModifyInfoById(Integer id);
	
	/**
	 * 
	 * @method:makeCustomerCode 处理1-3级客户，设置客户代码
	 * @Description: 根据客户分级，设置客户代码
	 */
	public String makeCustomerCode(Customer customer);
	/**
	 * 
	 * @method:makeCustomerCode 处理4级客户，自动建馆再设置客户代码
	 * @Description: 处理4级客户，自动建馆再设置客户代码
	 */	
	public void makeCustomerCodeLevelFour(Customer customer);

	/**
	 * 获取未占用客户分类列表
	 * @return
	 */
	public List<CustomerLevel> getCustomerLevelList();
	/**
	 * 获取未占用客户分类列表 ,排除客户自己
	 * @return
	 */
	public List<CustomerLevel> getCustomerLevelList(int customerID);
	/**
	 * 
	 * saveOrUpdateCustomer	保存或更新客户的资料 
	 */
	public boolean saveOrUpdateCustomer(Customer customer, CutomerLibrary cl);
	/**
	 * 更新客户后台用户账号
	 * @param customer
	 */
	public void updateUserListByCustomer(Customer customer);
	/**
	 * 客户可建馆区间
	 * @param customer
	 */
	public List<String> getVisibleLibraryRange(Customer customer);

}
