package com.flea.common.service.impl;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flea.common.dao.UserRoleMapDao;
import com.flea.common.pojo.Page;
import com.flea.common.pojo.UserRoleMap;
import com.flea.common.service.UserRoleMapService;

/**
 * 用户和角色关联Service
 * @author UserRoleMap
 * @version 2016-04-20
 */
 @Service
public class UserRoleMapServiceImpl extends BaseServiceImpl<UserRoleMap,Integer> implements UserRoleMapService{

	@Autowired
	private UserRoleMapDao userRoleMapDao;
	
	
	@Autowired
	public  UserRoleMapServiceImpl(UserRoleMapDao userRoleMapDao) {
		super(userRoleMapDao);
		this.userRoleMapDao = userRoleMapDao;
	}
	
	@Override
	public Page<UserRoleMap> find(Page<UserRoleMap> page,UserRoleMap userRoleMap) {
		DetachedCriteria dc = userRoleMapDao.createDetachedCriteria();
//		if(StringUtils.isNotEmpty(userRoleMap.getName())){
//			dc.add(Restrictions.eq("name", userRoleMap.getName()));
//		}
		return userRoleMapDao.find(page, dc);
	}

	/* (non-Javadoc)
	 * @see com.flea.common.service.UserRoleMapService#deleteByRoleId(java.lang.Integer)
	 */
	@Override
	public void deleteByUserId(Integer userId) {
		userRoleMapDao.deleteByUserId(userId);
	}
	
}
