/**  
* @Package com.flea.common.dao.impl
* @Description: TODO
* @author bruce
* @date 2015年12月17日 上午10:20:06
* @version V1.0  
*/ 
package com.flea.modules.system.dao.impl;


import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.flea.common.dao.impl.BaseDaoImpl;
import com.flea.modules.system.dao.WarehouseDao;
import com.flea.modules.system.pojo.Warehouse;

/**
 * @author bruce
 * @2015年12月17日 上午10:20:06
 */
@Repository
public class WarehouseDaoImpl extends BaseDaoImpl<Warehouse, Long> implements
		WarehouseDao {

	/* (non-Javadoc)
	 * @see com.flea.common.dao.WarehouseDao#removeById(int)
	 */
	@Override
	public boolean removeById(int id) {
		  Session session = getSession();
		  Query query =session.createQuery("delete from Warehouse where id="+id);
		  if(query.executeUpdate()>0)return true;
		  return false;
	}

	
}
