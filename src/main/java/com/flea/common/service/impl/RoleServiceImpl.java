package com.flea.common.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.flea.common.dao.RoleDao;
import com.flea.common.dao.RoleMenuMapDao;
import com.flea.common.dao.UserRoleMapDao;
import com.flea.common.pojo.Role;
import com.flea.common.pojo.RoleMenuMap;
import com.flea.common.pojo.User;
import com.flea.common.service.RoleService;
import com.flea.common.util.ShiroUtils;
@Service
public class RoleServiceImpl extends BaseServiceImpl<Role, Integer>  implements RoleService{
	
	private RoleDao roleDao;
	
	@Autowired
	private UserRoleMapDao userRoleMapDao;
	
	@Autowired
	private RoleMenuMapDao roleMenuMapDao;
	
	
	@Autowired
	public RoleServiceImpl(RoleDao roleDao) {
		super(roleDao);
		this.roleDao = roleDao;
	}


	@Override
	public List<Role> getRoleById(Integer id) {
		return roleDao.findByUserId(id);
	}
	@Override
	public JSONObject removeById(int id) {
	  Integer sum = userRoleMapDao.findUserSumByRoleId(id);
	  JSONObject json = new JSONObject();
		if(sum>0){
			json.put("sum", sum);
			json.put("success", false);
			return json;
		}
		boolean flag =roleDao.deleteById(id)>0?true:false;
		if(flag){
			userRoleMapDao.deleteByRoleId(id);
		}
		json.put("sum", 0);
		json.put("success", flag);
		return json;
	}

	@Override
	public void updateOne(Role t) {
		Role old = roleDao.getOne(t.getId());
		old.getOwner();
		t.setOwner(old.getOwner());
		roleDao.saveOrUpdateOne(t);
	}

	/* (non-Javadoc)
	 * @see com.flea.common.service.RoleService#valiRoleName(java.lang.String, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public boolean valiRoleName(String roleName, Integer roleid) {
		if (StringUtils.isBlank(roleName))return false;
		List<Role> roles = roleDao.findRoleByName$Id(roleName, roleid);
		if(roles.size()>0)return false;
		return true;
	}



	/* (non-Javadoc)
	 * @see com.flea.common.service.RoleService#findRolesBySearch(java.lang.String)
	 */
	@Override
	public List<Role> findRolesBySearch(String search) {
		return roleDao.findRolesBySearch(search);
	}


	/* (non-Javadoc)
	 * @see com.flea.common.service.RoleService#findRolesByLevel(java.lang.Integer)
	 */
	@Override
	public List<Role> findRolesByLevel(String level) {
		return roleDao.findRolesByLevel(level);
	}


	/* (non-Javadoc)
	 * @see com.flea.common.service.RoleService#getRoleByOwnerId(java.lang.Integer)
	 */
	@Override
	public List<Role> getRoleByOwnerId(Integer ownerId) {
		return roleDao.getRoleByOwnerId(ownerId);
	}

	public  void saveRole(Role role,String menuIds) {
		User user = ShiroUtils.getCurrentUser();
		role.setOwner(user.getId());
		roleDao.saveOne(role);
		String[] menus = menuIds.split(",");
		for(String menuId:menus){
			RoleMenuMap roleMenu =new RoleMenuMap();
			roleMenu.setMenuId(Integer.parseInt(menuId));
			roleMenu.setRoleId(role.getId());
			roleMenuMapDao.saveOne(roleMenu);
		}
		JSONObject json = new JSONObject();
		json.put("succcess", true);
	}
	
	
}
