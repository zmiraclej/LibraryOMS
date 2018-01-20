package com.flea.common.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.flea.common.pojo.Role;

public interface RoleService  extends BaseService<Role,Integer> {
	//得到用户角色
	public List<Role> getRoleById(Integer id);
	
	public boolean valiRoleName(String roleName, Integer roleid);
	
	List<Role> findRolesBySearch(String search);
	
	List<Role>  findRolesByLevel(String level);
	
	JSONObject removeById(int id);
	
	List<Role> getRoleByOwnerId(Integer ownerId);
	
}
