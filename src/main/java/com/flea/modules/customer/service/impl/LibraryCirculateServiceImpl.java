package com.flea.modules.customer.service.impl;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flea.common.pojo.Page;
import com.flea.common.service.impl.BaseServiceImpl;
import com.flea.modules.customer.dao.LibraryCirculateDao;
import com.flea.modules.customer.pojo.LibraryCirculate;
import com.flea.modules.customer.service.LibraryCirculateService;

/**
 * 馆际流通Service
 * @author bruce
 * @version 2016-05-26
 */
 @Service
public class LibraryCirculateServiceImpl extends BaseServiceImpl<LibraryCirculate,Integer> implements LibraryCirculateService{

	@Autowired
	private LibraryCirculateDao libraryCirculateDao;
	
	
	@Autowired
	public  LibraryCirculateServiceImpl(LibraryCirculateDao libraryCirculateDao) {
		super(libraryCirculateDao);
		this.libraryCirculateDao = libraryCirculateDao;
	}
	
	@Override
	public Page<LibraryCirculate> find(Page<LibraryCirculate> page,LibraryCirculate libraryCirculate) {
		DetachedCriteria dc = libraryCirculateDao.createDetachedCriteria();
		
		return libraryCirculateDao.find(page, dc);
	}
	
}
