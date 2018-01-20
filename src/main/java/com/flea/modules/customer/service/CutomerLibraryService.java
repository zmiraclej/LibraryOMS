package com.flea.modules.customer.service;


import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.flea.common.pojo.Page;
import com.flea.common.service.BaseService;
import com.flea.modules.customer.pojo.CutomerLibrary;

/**
 * 分配馆号Service
 * @author Bruce
 * @version 2016-04-15
 */

public interface CutomerLibraryService extends BaseService<CutomerLibrary,Integer> {
	
	/**
	 * 查询列表
	 * @param page
	 * @param search
	 * @param cutomerLibrary
	 * @return
	 */
   	Page<CutomerLibrary> find(Page<CutomerLibrary> page,String search,CutomerLibrary cutomerLibrary);
   	
   	/**
   	 * 图书馆新增地区查询
   	 * @param customerId
   	 * @return
   	 */
   	JSONObject findLibraryAreasByCustomerId(Integer customerId);
   	
   	/**
   	 * 图书馆新增馆别查询
   	 * @param customerId
   	 * @return
   	 */
   	JSONArray findLevelByAreaCode(Integer areaCode);
   	
   	JSONObject  findLibraryCodeById(Integer Id);
   	
   	List<CutomerLibrary>  findListByCustomerId(Integer customerId);

   	/**
   	 * 图书馆某批次未使用馆号
   	 * @param id  system_customer_library 批次表ID
   	 * @return
   	 */
	JSONObject findUnusedLibraryCodeById(Integer id);
	
   	/**
   	 * 客户增加图书馆批次并预分配馆号 需要事务处理
   	 * @param id  system_customer_library 批次表ID
   	 * @return
   	 */
	public void addCustomerLibraryAndUpdateLibraryCode(CutomerLibrary customerLibrary);
	/**
   	 * 根据批次给客户分配馆号
   	 * @param id  system_customer_library 批次表ID
   	 * @return
   	 */	
	public void allotLibraryCode(CutomerLibrary customerLibrary);
}
