package com.flea.modules.system.dao;

import com.flea.common.dao.BaseDao;
import com.flea.modules.system.pojo.LibrarysUpdateStatus;
 /**
  * 
  * @ClassName: LibrarysUpdateStatusDao
  * @Description:图书馆用户新增权限的更新状态
  * @author QL
  * @date 2017年5月8日 上午10:21:47
  */

public interface LibrarysUpdateStatusDao extends BaseDao<LibrarysUpdateStatus,Integer>{
	//当图书馆为修改驳回的时候，把更新过的数据该为原来的数据
	boolean updateLibrarys(Integer libraryId);
}
