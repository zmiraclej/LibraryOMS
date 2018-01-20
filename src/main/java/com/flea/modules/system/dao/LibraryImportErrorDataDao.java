package com.flea.modules.system.dao;


import com.flea.common.dao.BaseDao;
import com.flea.modules.system.pojo.LibraryImportErrorData;

public interface LibraryImportErrorDataDao extends BaseDao<LibraryImportErrorData, Long>{

	int deleteById(int id);
	
}
