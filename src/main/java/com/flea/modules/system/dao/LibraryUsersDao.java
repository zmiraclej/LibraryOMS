package com.flea.modules.system.dao;

import com.flea.common.dao.BaseDao;
import com.flea.modules.system.pojo.LibraryUser;
/**
 * 
 * @ClassName: LibraryUsersDao
 * @Description:用户图书馆持久层
 * @author QL
 * @date 2017年3月28日 上午11:37:51
 */

public interface LibraryUsersDao  extends BaseDao<LibraryUser, Long>{
	//通过图书馆号更新图书馆的用户状态
	int updateLibraryUser(String libraryHallCode); 

}
