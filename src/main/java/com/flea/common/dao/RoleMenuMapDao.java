package com.flea.common.dao;

import org.springframework.stereotype.Repository;

import com.flea.common.dao.BaseDao;
import com.flea.common.pojo.RoleMenuMap;

/**
 * 角色和菜单关联DAO接口
 * @author bruce
 * @version 2016-04-26
 */
@Repository
public interface RoleMenuMapDao extends BaseDao<RoleMenuMap,Integer> {
	
	void deleteByRoleId(Integer roleId);
}
