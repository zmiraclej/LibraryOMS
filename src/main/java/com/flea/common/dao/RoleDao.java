package com.flea.common.dao;

import java.util.List;

import com.flea.common.pojo.Role;

public interface RoleDao extends BaseDao<Role, Long>{

	public List<Role> findByUserId(Integer userId);
	
	List<Role> findRoleByName$Id(String roleName, Integer roleid);

	List<Role> findRolesBySearch(String search);
	
	List<Role>  findRolesByLevel(String level);
	
	List<Role> getRoleByOwnerId(Integer ownerId);
	
}
