package com.flea.modules.customer.dao;

import org.springframework.stereotype.Repository;

import com.flea.common.dao.BaseDao;
import com.flea.modules.customer.pojo.CustomerContact;
import com.flea.modules.customer.pojo.LibraryCode;
@Repository
public interface LibraryCodeDao extends BaseDao<LibraryCode,Integer> {

	/**
	 * 
	 * @Description: 根据图书馆等级返回馆号
	 */
	public LibraryCode getUnUsedLibraryCodeByLevel(Integer level);
}
