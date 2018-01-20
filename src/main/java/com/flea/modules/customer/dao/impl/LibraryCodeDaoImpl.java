package com.flea.modules.customer.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.flea.common.dao.BaseDao;
import com.flea.common.dao.impl.BaseDaoImpl;
import com.flea.modules.customer.dao.LibraryCirculateDao;
import com.flea.modules.customer.dao.LibraryCodeDao;
import com.flea.modules.customer.pojo.CustomerContact;
import com.flea.modules.customer.pojo.LibraryCirculate;
import com.flea.modules.customer.pojo.LibraryCode;
@Repository
public class LibraryCodeDaoImpl extends BaseDaoImpl<LibraryCode,Integer> implements LibraryCodeDao {

	@Override
	public LibraryCode getUnUsedLibraryCodeByLevel(Integer level) {
		// TODO Auto-generated method stub
		List params = new ArrayList();
		params.add(params);
		System.out.println("select");
		System.out.println("this.getSession()" + this.getSession());;
		LibraryCode	libraryCode = this.getListByHQL("from LibraryCode where level = " + level + " and status = 0  ", 1 , null).get(0);
				//(LibraryCode)this.getOneBySQL("select * from lib_code where level = ? and status = 0 ", params);
		//("select * from lib_code where level = ? and status = 0 limit 1", params);
		return libraryCode;
	}
}