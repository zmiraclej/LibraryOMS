package com.flea.modules.customer.service;

import java.util.List;

import com.flea.common.pojo.Area;
import com.flea.common.pojo.City;
import com.flea.common.pojo.Province;
import com.flea.common.service.BaseService;
import com.flea.modules.customer.pojo.Customer;
import com.flea.modules.customer.pojo.CustomerTypeCode;


/**
 * 客户分级地区查询
 * @author gouxl
 * @version 2016-05-15
 */

public interface CustomerTypeCodeService extends BaseService<CustomerTypeCode, Integer> {

	/**
	 * 查询客户省份列表
	 * @param customerType 客户级别
	 * @return
	 */
	public List<Province> listProvince(Integer customerType);
	
	/**
	 * 修改查询客户省份列表
	 * @param customerType 客户级别
	 * @param pCode 省级代码
	 * @return
	 */
	public List<Province> listEditProvince(Integer customerType, String pCode);
	
	/**
	 * 查询客户城市列表
	 * @param provinceCode 省code
	 * @param customerType 客户级别
	 * @return
	 */
	public List<City> listCity(String provinceCode, Integer customerType);
	
	/**
	 * 编辑查询客户区域
	 * @param provinceCode
	 * @param customerType
	 * @param cityCode
	 * @return
	 */
	public List<City> listEditCity(String provinceCode, Integer customerType, String cityCode);
	
	//查询客户地区列表
	public List<Area> listArea(String cityCode);
	
	/**
	 * 保存关联关系信息
	 * @param customerId
	 */
	void saveOne(Customer customer);
	
	/**
	 * 编辑客户信息,修改级联关系表
	 * @param customer
	 * @return
	 */
	void updateOne(Customer customer);
	
	/**
	 * 查询省市级是否存在
	 * @param customer
	 * @return
	 */
	CustomerTypeCode findCustomerTypeCode(Customer customer);
	
	
}
