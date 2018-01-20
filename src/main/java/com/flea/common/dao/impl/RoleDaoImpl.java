package com.flea.common.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.flea.common.Common;
import com.flea.common.dao.RoleDao;
import com.flea.common.pojo.Role;
import com.flea.common.pojo.User;
import com.flea.common.util.Config;
import com.flea.common.util.ShiroUtils;
@Repository
public class RoleDaoImpl extends BaseDaoImpl<Role, Long> implements RoleDao{

	@Override
	public List<Role> findByUserId(Integer userId) {
		String hql = "select role from Role as role inner join role.users as u where u.id=?";
		Query query = getSession().createQuery(hql);
		query.setParameter(0, userId);
		return query.list();
	}

	/* (non-Javadoc)
	 * @see com.flea.common.dao.RoleDao#findRoleByName$Id(java.lang.String, java.lang.Integer)
	 */
	@Override
	public List<Role> findRoleByName$Id(String roleName, Integer roleid) {
		User user = ShiroUtils.getCurrentUser();
		String hql ="from Role where name='"+roleName+"'  and isEffective="+Common.FLAG_ACTIVATION;
		if(roleid!=null){
			hql += " and id!="+roleid;
		}
		hql+=" and owner="+user.getId();
		return getListByHQL(hql, null);
	}


	/* (non-Javadoc)
	 * @see com.flea.common.dao.RoleDao#findRolesBySearch(java.lang.String)
	 */
	@Override
	public List<Role> findRolesBySearch(String search) {
		List<Object> values  = new ArrayList<Object>();
		values.add(Common.FLAG_ACTIVATION);
		String hql = "from Role where 1=1";
		if(StringUtils.isNotBlank(search)){
			if("一级".equals(search)){
				hql+=" and level='0' ";
			}else if("二级".equals(search)){
				hql+=" and level='1'  ";
			}else {
				hql+= " and (name like '%"+search+"%' or description like '%"+search+"%')  ";
			}
		}
		hql +=" and  isEffective="+Common.FLAG_ACTIVATION;
		User user = ShiroUtils.getCurrentUser();
		String level = ShiroUtils.getCurrentUserRoleLevel(user);
		System.out.println(user.getRoles().get(0));
//		if(level.equals(Common.ROLE_FIRST_LEVLE) && 1 != Integer.parseInt(user.getRemark())){
//			hql+=" and (level='0' or id = 3) and (owner="+user.getId()+" or owner is null or id = 3 or owner is not null )";
//		}else 
//		if (level.equals(Common.ROLE_SECOND_LEVLE)){
//			hql+=" and level='1' and (customerId = "+ user.getCustomer().getId()+")";
//		}else {
//			hql+=" and level='1' and (owner="+user.getId()+" or id = 3 )";
//		}
		if(level.equals(Common.ROLE_FIRST_LEVLE)){
			hql+=" and (level='0' or id = 3) and (owner="+user.getId()+" or owner is null or id = 3 or owner is not null )";
		}else {
			hql+=" and level='1' and (customerId = "+ user.getCustomer().getId()+")";
		}
		
		return getListByHQL(hql,null);
	}

	/* (non-Javadoc)
	 * @see com.flea.common.dao.RoleDao#findRolesByLevel(java.lang.Integer)
	 */
	/**
	 * 用户新增，当前用户能够新增的角色权限
	 */
	@Override
	public List<Role> findRolesByLevel(String level) {
		User user = ShiroUtils.getCurrentUser();
		String hql = "from Role where isEffective="+Common.FLAG_ACTIVATION +" and level='"+level+"' and (owner="+user.getId()+" or owner is not null or owner is null ) ";
		return getListByHQL(hql, null);
	}

	/* (non-Javadoc)
	 * @see com.flea.common.dao.RoleDao#getRoleByOwnerId(java.lang.Integer)
	 */
	@Override
	public List<Role> getRoleByOwnerId(Integer ownerId) {
		String hql = "from Role where isEffective="+Common.FLAG_ACTIVATION;
		User user = ShiroUtils.getCurrentUser();
		String level = ShiroUtils.getCurrentUserRoleLevel(user);
//		if(level.equals(Common.ROLE_SECOND_LEVLE)&& 1 != Integer.parseInt(user.getRemark())){
//			hql +=" and (owner="+ownerId +" or id="+Config.getProperty("cRoleId")+")";
//		}else 
		if (level.equals(Common.ROLE_SECOND_LEVLE)){
			hql+=" and level='1' and (customerId = "+ user.getCustomer().getId()+")";
		}else {
			hql +=" and owner="+ownerId; 
		}
		return getListByHQL(hql, null);
	}
	
	
	
	
}
