package com.flea.modules.system.dao.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.flea.common.dao.impl.BaseDaoImpl;
import com.flea.modules.system.dao.LibraryImportErrorDataDao;
import com.flea.modules.system.pojo.LibraryImportErrorData;
@Repository
public class LibraryImportErrorDataDaoImpl extends BaseDaoImpl<LibraryImportErrorData, Long> implements LibraryImportErrorDataDao {

	
	@Override
	public int deleteById(int id) {
		String hql="delete from LibraryImportErrorData where user.id = "+id;
		Query query =getSession().createQuery(hql);
		return query.executeUpdate();
	}
}
