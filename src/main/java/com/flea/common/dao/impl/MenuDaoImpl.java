package com.flea.common.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.flea.common.dao.BaseDao;
import com.flea.common.dao.MenuDao;
import com.flea.common.pojo.Menu;
import com.google.common.collect.Lists;

/**
 * 菜单管理DAO接口
 * @author Bruce
 * @version 2016-01-12
 */
@Repository
public class MenuDaoImpl extends BaseDaoImpl<Menu,Integer> implements MenuDao{

	/* (non-Javadoc)
	 * @see com.flea.common.dao.MenuDao#findByPid(java.lang.Integer)
	 */
	@Override
	public List<Menu> findByPid(Integer pid) {
		String hql = "from Menu ";
		if(pid!=null)
			hql+="where parent.id="+pid;
		else {
			hql+="where parent.id is null";
		}
		Query query = getSession().createQuery(hql);
		query.setCacheable(true);
		return query.list();
	}

	/* (non-Javadoc)
	 * @see com.flea.common.dao.MenuDao#findByUserId(java.lang.String)
	 */
	@Override
	public List<Menu> findByUserId(Integer userId) {
		String hql ="select distinct m from Menu m, Role r, User u where m in elements (r.menus) and r in elements (u.roles)" +
				" and m.delFlag=:p1 and r.delFlag=:p1 and u.delFlag=:p1 and u.id=:p2" + // or (m.user.id=:p2  and m.delFlag=:p1)" + 
				" order by m.sort";
		List<Object> paramList=Lists.newArrayList();
		paramList.add(Menu.DEL_FLAG_NORMAL);
		paramList.add(userId);
		return getListByHQL(hql, paramList);
	}
	
}
