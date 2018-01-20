package com.flea.modules.system.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.flea.common.dao.impl.BaseDaoImpl;
import com.flea.modules.system.dao.RegionDao;
import com.flea.modules.system.pojo.Region;

/**
 * 区域管理DAO接口
 * @author Bruce
 * @version 2016-01-06
 */
@Repository
public class RegionDaoImpl extends BaseDaoImpl<Region,Integer> implements RegionDao{

	/* (non-Javadoc)
	 * @see com.flea.common.dao.RegionDao#findByPid(java.lang.String)
	 */
	@Override
	public List<Region> findByPid(Integer pid) {
		String hql = "from Region ";
		if(pid!=0)
			hql+="where parent.id="+pid;
		else {
			hql+="where parent.id is null";
		}
		Query query = getSession().createQuery(hql);
		query.setCacheable(true);
		return query.list();
	}
	
}
