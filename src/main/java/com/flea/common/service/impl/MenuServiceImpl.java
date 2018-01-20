package com.flea.common.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.flea.common.dao.MenuDao;
import com.flea.common.dao.RoleDao;
import com.flea.common.pojo.Menu;
import com.flea.common.pojo.Page;
import com.flea.common.pojo.Role;
import com.flea.common.pojo.User;
import com.flea.common.service.MenuService;
import com.flea.common.util.AppUtils;
import com.flea.common.util.ShiroUtils;
import com.google.common.collect.Lists;

/**
 * 菜单管理Service
 * @author Bruce
 * @version 2016-01-12
 */
 @Service
public class MenuServiceImpl extends BaseServiceImpl<Menu,Integer> implements MenuService{

	@Autowired
	private MenuDao menuDao;
	
	
	@Autowired
	public  MenuServiceImpl(MenuDao menuDao) {
		super(menuDao);
		this.menuDao = menuDao;
	}
	
	@Override
	public Page<Menu> find(Page<Menu> page,Menu menu) {
		DetachedCriteria dc = menuDao.createDetachedCriteria();
		if(StringUtils.isNotEmpty(menu.getName())){
			dc.add(Restrictions.eq("name", menu.getName()));
		}
		return menuDao.find(page, dc);
	}

	/* (non-Javadoc)
	 * @see com.flea.common.service.MenuService#findMapByPid(java.lang.Integer)
	 */
	@Override
	public List<Map<String, Object>> findMapByPid(Integer pid) {
		List<Menu> regions = menuDao.findByPid(pid);
		List<Map<String,Object>> nodes = new ArrayList<Map<String,Object>>();
		for(Menu menu:regions){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("pid", pid);
			map.put("id",menu.getId());
			map.put("name",menu.getName());
			List<Menu> seeAreas = menuDao.findByPid(pid);
			if (seeAreas.size()>0) {
				map.put("isParent", "true");
			}
			nodes.add(map);
		}
		return nodes;
	}

	/* (non-Javadoc)
	 * @see com.flea.common.service.MenuService#getMenuList()
	 */
	@Override
	public List<Menu> getMenuList() {
		List<Menu> menuList= Lists.newArrayList();
		User user = ShiroUtils.getCurrentUser();
		//if (user.isAdmin()){
			menuList = menuDao.findAll();
//		}else{
//			menuList = menuDao.findByUserId(user.getId());
//		}
		return menuList;
	}

	/* (non-Javadoc)
	 * @see com.flea.common.service.MenuService#findMenu$Parent(java.lang.Integer)
	 */
	@Override
	public Menu findMenu$Parent(Integer id) {
		Menu menu = menuDao.getOne(id);
		String regionStr = AppUtils.MenuAndParentName(menu.getParent(),"\\");
		menu.setMenuAssist(regionStr);
		return menu;
	}
	
	
	
	@javax.annotation.Resource
	private RoleDao roleDao;
	@Override
	public JSONArray getMenuTreeJson(Integer roleId){
		HashSet<Integer> roleMenuIds = null;
		if (roleId!=null) {
			Role role = roleDao.getOne(roleId);
			Set<Menu> roleMenus = role.getMenus();
			if(roleMenus!=null){
				roleMenuIds = new HashSet<Integer>();
				for (Menu roleMenu : roleMenus) {
					roleMenuIds.add(roleMenu.getId());
				}
			}
		}
		List<Menu> Menus =  menuDao.findByPid(null);
		return MenusToTreeJson(Menus, roleMenuIds);
	}
	public JSONArray MenusToTreeJson(List<Menu> Menus,HashSet<Integer> roleMenuIds) {
		JSONArray jsonArray = new JSONArray();
		for (Menu Menu : Menus) {
			JSONObject jo = new JSONObject();
			jo.put("name", Menu.getName());
			jo.put("id", Menu.getId());
			if(roleMenuIds!=null&&roleMenuIds.contains(Menu.getId())){
				jo.put("checked",true);
			}
			List<Menu> childrens = Menu.getChildList();
			if(childrens!=null&&childrens.size()>0){
				jo.put("children", MenusToTreeJson(childrens, roleMenuIds));
			}else{
				jo.put("leaf", true);
			}
			jsonArray.add(jo);
		}
		return jsonArray;
	}
	
	
}
