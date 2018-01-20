package com.flea.modules.customer.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.flea.common.dao.BaseDao;
import com.flea.modules.customer.pojo.CustomerTypeCode;

/**
 * 客户DAO接口
 * 客户分级地区查询
 * @author gouxl
 * @version 2016-05-15
 */
@Repository
public interface CustomerTypeCodeDao extends BaseDao<CustomerTypeCode, Integer> {
	/**
	 * 查询客户分级是否存在该省
	 * @param customerType
	 * @return
	 */
	List<CustomerTypeCode> listProvince(Integer customerType);
	
	/**
	 * 查询客户分级是否存在该市
	 * @param customerType
	 * @return
	 */
	List<CustomerTypeCode> listCity(Integer customerType);
	
	/**
	 * 查询
	 * @param customerId
	 * @return
	 */
	CustomerTypeCode findOne(Integer customerId);
	
	/**
	 * 通过地区代码和客户级别id查询是否已经存在
	 * @param provinceCode
	 * @param customerLevelId
	 * @return
	 */
	CustomerTypeCode findCustomerTypeCode(String code, Integer customerLevelId);
	
}
