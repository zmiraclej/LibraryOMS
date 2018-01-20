package com.flea.modules.system.dao.impl;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.flea.common.dao.impl.BaseDaoImpl;
import com.flea.modules.system.dao.LibraryUsersDao;
import com.flea.modules.system.pojo.LibraryUser;
/**
 * 
 * @ClassName: LibraryUsersDaoImpl
 * @Description:更改用户图书馆的状态
 * @author QL
 * @date 2017年3月28日 上午11:37:01
 */
@Repository
public class LibraryUsersDaoImpl extends BaseDaoImpl<LibraryUser, Long> implements LibraryUsersDao{
	@Override
	public int updateLibraryUser(String libraryHallCode) {
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("update librarys_users set isEffective = 1  where libraryHallCode = ?");
		SQLQuery query = getSession().createSQLQuery(sqlBuffer.toString());
		query.setString(0, libraryHallCode);
		return query.executeUpdate();
	}

}
