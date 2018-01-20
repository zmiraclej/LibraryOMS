package com.flea.common.service;


import com.flea.common.pojo.Page;
import com.flea.common.pojo.RoleMenuMap;

/**
 * 角色和菜单关联Service
 * @author bruce
 * @version 2016-04-26
 */

public interface RoleMenuMapService extends BaseService<RoleMenuMap,Integer> {

   	Page<RoleMenuMap> find(Page<RoleMenuMap> page,RoleMenuMap roleMenuMap);
   	
   	RoleMenuMap findByRoleId$MenuId(Integer roleId,Integer menuId);
   	
   	
   	void deleteByRoleId(Integer roleId);
	
}
