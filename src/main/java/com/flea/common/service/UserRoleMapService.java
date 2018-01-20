package com.flea.common.service;


import com.flea.common.pojo.Page;
import com.flea.common.pojo.UserRoleMap;

/**
 * 用户和角色关联Service
 * @author UserRoleMap
 * @version 2016-04-20
 */

public interface UserRoleMapService extends BaseService<UserRoleMap,Integer> {

   	Page<UserRoleMap> find(Page<UserRoleMap> page,UserRoleMap userRoleMap);
	
    void deleteByUserId(Integer userId);
}
