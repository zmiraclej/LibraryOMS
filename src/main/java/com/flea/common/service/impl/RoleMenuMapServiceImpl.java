package com.flea.common.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.flea.common.pojo.Page;
import com.flea.common.pojo.RoleMenuMap;
import com.flea.common.service.RoleMenuMapService;
import com.flea.common.dao.RoleMenuMapDao;

/**
 * 角色和菜单关联Service
 * @author bruce
 * @version 2016-04-26
 */
 @Service
public class RoleMenuMapServiceImpl extends BaseServiceImpl<RoleMenuMap,Integer> implements RoleMenuMapService{

	@Autowired
	private RoleMenuMapDao roleMenuMapDao;
	
	
	@Autowired
	public  RoleMenuMapServiceImpl(RoleMenuMapDao roleMenuMapDao) {
		super(roleMenuMapDao);
		this.roleMenuMapDao = roleMenuMapDao;
	}
	
	@Override
	public Page<RoleMenuMap> find(Page<RoleMenuMap> page,RoleMenuMap roleMenuMap) {
		return roleMenuMapDao.find(page, null);
	}

	/* (non-Javadoc)
	 * @see com.flea.common.service.RoleMenuMapService#findByRoleId$MenuId(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public RoleMenuMap findByRoleId$MenuId(Integer roleId, Integer menuId) {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.flea.common.service.RoleMenuMapService#deleteByRoleId(java.lang.Integer)
	 */
	@Override
	public void deleteByRoleId(Integer roleId) {
		 roleMenuMapDao.deleteByRoleId(roleId);;
	}
	
}
