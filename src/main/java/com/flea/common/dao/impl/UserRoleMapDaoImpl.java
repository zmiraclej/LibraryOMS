package com.flea.common.dao.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.flea.common.dao.UserRoleMapDao;
import com.flea.common.pojo.UserRoleMap;

/**
 * 用户和角色关联DAO接口
 * @author UserRoleMap
 * @version 2016-04-20
 */
@Repository
public class UserRoleMapDaoImpl extends BaseDaoImpl<UserRoleMap,Integer> implements UserRoleMapDao{

	/* (non-Javadoc)
	 * @see com.flea.common.dao.UserRoleMapDao#deleteByUserId(java.lang.Integer)
	 */
	@Override
	public void deleteByUserId(Integer userId) {
		String hqlString= "delete UserRoleMap where userId=?";
		List<Object> list = new ArrayList<Object>();
		list.add(userId);
		this.queryHql(hqlString, list);
		
	}

	/* (non-Javadoc)
	 * @see com.flea.common.dao.UserRoleMapDao#deleteByRoleId(java.lang.Integer)
	 */
	@Override
	public void deleteByRoleId(Integer roleId) {
		String hqlString= "delete UserRoleMap where roleId=?";
		List<Object> list = new ArrayList<Object>();
		list.add(roleId);
		this.queryHql(hqlString, list);
	}

	/* (non-Javadoc)
	 * @see com.flea.common.dao.UserRoleMapDao#findUserSumByRoleId(java.lang.Integer)
	 */
	@Override
	public Integer findUserSumByRoleId(Integer roleId) {
//		String sqlString ="SELECT COUNT(*) from system_user_role ur LEFT JOIN system_user u  on ur.userId = u.id WHERE ur.roleId = '"+roleId+"' AND u.isEffective =1;";
		String sqlString ="SELECT COUNT(*) from system_user_role ur LEFT JOIN system_user u  on ur.userId = u.id WHERE ur.roleId = '"+roleId+"' ;";
		SQLQuery query = getSession().createSQLQuery(sqlString);
		return ((BigInteger)query.uniqueResult()).intValue();
	}
	
}
