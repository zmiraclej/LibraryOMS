package com.flea.common.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.flea.common.dao.BaseDao;
import com.flea.common.dao.RoleMenuMapDao;
import com.flea.common.pojo.RoleMenuMap;

/**
 * 角色和菜单关联DAO接口
 * @author bruce
 * @version 2016-04-26
 */
@Repository
public class RoleMenuMapDaoImpl extends BaseDaoImpl<RoleMenuMap,Integer> implements RoleMenuMapDao{

	/* (non-Javadoc)
	 * @see com.flea.common.dao.RoleMenuMapDao#deleteByRoleId(java.lang.Integer)
	 */
	@Override
	public void deleteByRoleId(Integer roleId) {
		String hqlString= "delete RoleMenuMap where roleId=?";
		List<Object> list = new ArrayList<Object>();
		list.add(roleId);
		this.queryHql(hqlString, list);
	}
	
}
