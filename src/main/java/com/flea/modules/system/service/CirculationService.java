package com.flea.modules.system.service;


import java.util.List;

import com.flea.common.pojo.Page;
import com.flea.common.service.BaseService;
import com.flea.modules.system.pojo.Circulation;
import com.flea.modules.system.pojo.vo.LibraryCirculation;

/**
 * 馆际流通Service
 * @author bruce
 * @version 2016-08-18
 */

public interface CirculationService extends BaseService<Circulation,Integer> {

	Page<LibraryCirculation> find(Page<LibraryCirculation> page,Circulation circulation,String search);
	
	int getMaxScope(Integer  customerId);
	
	boolean updateLibraryCirculation(String action,String allId,int jieshu);
	
	List<Circulation> findByCustomerIdAndScope(Integer customerId,Integer scope);
	
}
