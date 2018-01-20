package com.flea.modules.system.service;

import com.flea.common.pojo.Page;
import com.flea.common.service.BaseService;
import com.flea.modules.system.pojo.LibraryCustomer;

public interface LibraryCustomerService extends BaseService<LibraryCustomer, Integer> {

	Page<LibraryCustomer> findLibCustomer(Page<LibraryCustomer> page,String search, LibraryCustomer library);
	/**
	 * 
	 * @Description:(查询未审核的图书馆)
	 * @param page
	 * @param library
	 * @param search
	 * @param libraryStatus
	 * @return    
	 * Page<LibraryCustomer>    返回类型
	 * @throws
	 * @author QL 
	 * @date 2017年3月27日 下午4:20:53
	 */
	Page<LibraryCustomer> findLibCustomerExamine(Page<LibraryCustomer> page, LibraryCustomer library, String search);
}
