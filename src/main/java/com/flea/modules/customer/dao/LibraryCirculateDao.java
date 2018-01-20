package com.flea.modules.customer.dao;

import org.springframework.stereotype.Repository;

import com.flea.common.dao.BaseDao;
import com.flea.modules.customer.pojo.LibraryCirculate;

/**
 * 馆际流通DAO接口
 * @author bruce
 * @version 2016-05-26
 */
@Repository
public interface LibraryCirculateDao extends BaseDao<LibraryCirculate,Integer> {
	
}
