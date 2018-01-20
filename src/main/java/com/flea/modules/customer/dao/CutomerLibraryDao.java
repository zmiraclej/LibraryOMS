package com.flea.modules.customer.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONArray;
import com.flea.common.dao.BaseDao;
import com.flea.common.pojo.Page;
import com.flea.modules.customer.pojo.Customer;
import com.flea.modules.customer.pojo.CutomerLibrary;

/**
 * 分配馆号DAO接口
 * @author Bruce
 * @version 2016-04-15
 */
@Repository
public interface CutomerLibraryDao extends BaseDao<CutomerLibrary,Integer> {

	
	/**
	 * 
	 * @param page
	 * @param search
	 * @return
	 */
	Page<CutomerLibrary> findListBySearch(Page<CutomerLibrary> page,String search);
	
	List<CutomerLibrary> findByCustomerId(Integer customerId);
	
	
	JSONArray findLevelByAreaCode(Integer area,Integer userId);
	
	
	void updateUsedCodeById(Integer id);
	
	List<CutomerLibrary> findListByCustomerId(Integer customerId);

	String getAllCustomerLibrary(Customer customer);
}
