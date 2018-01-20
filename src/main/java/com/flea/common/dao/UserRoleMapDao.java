package com.flea.common.dao;


import org.springframework.stereotype.Repository;

import com.flea.common.dao.BaseDao;
import com.flea.common.pojo.UserRoleMap;

/**
 * 用户和角色关联DAO接口
 * @author UserRoleMap
 * @version 2016-04-20
 */
@Repository
public interface UserRoleMapDao extends BaseDao<UserRoleMap,Integer> {
	
	void deleteByUserId(Integer userId);
	
	void deleteByRoleId(Integer roleId);
	
	Integer  findUserSumByRoleId(Integer roleId);
}
