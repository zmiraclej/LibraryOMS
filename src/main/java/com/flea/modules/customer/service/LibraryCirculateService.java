package com.flea.modules.customer.service;


import com.flea.common.pojo.Page;
import com.flea.common.service.BaseService;
import com.flea.modules.customer.pojo.LibraryCirculate;

/**
 * 馆际流通Service
 * @author bruce
 * @version 2016-05-26
 */

public interface LibraryCirculateService extends BaseService<LibraryCirculate,Integer> {

   	Page<LibraryCirculate> find(Page<LibraryCirculate> page,LibraryCirculate libraryCirculate);
	
}
