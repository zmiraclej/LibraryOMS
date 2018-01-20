/**
 * Copyright &copy; 2014-2015.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.flea.common.action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.flea.common.Common;
import com.flea.common.pojo.Menu;
import com.flea.common.pojo.Role;
import com.flea.common.pojo.RoleMenuMap;
import com.flea.common.pojo.User;
import com.flea.common.service.MenuService;
import com.flea.common.service.RoleMenuMapService;
import com.flea.common.service.RoleService;
import com.flea.common.service.UserService;
import com.flea.common.shiro.UserRealm;
import com.flea.common.util.CacheUtils;
import com.flea.common.util.ModelAndViewUtils;
import com.flea.common.util.ShiroUtils;
import com.flea.modules.customer.pojo.Customer;

/**
 * 角色管理控制器
 * @author Bruce Tie
 * @date 2015年3月16日 下午3:44:34 
 * @function
 */

@Controller
@RequestMapping(value="/cms/role")
public class RoleController {

	@Resource
	private RoleMenuMapService roleMenuService;
	
	/**
	 * 角色新增页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/get.do",method=RequestMethod.GET)
	public ModelAndView get(HttpServletRequest  request) {
		ModelAndView modelView = new ModelAndView("common/role_add");
		User user = ShiroUtils.getCurrentUser();
		String level = ShiroUtils.getCurrentUserRoleLevel(user);
		
		Integer roleid = user.getRoles().get(0).getId();
		Role role = roleService.getOne(roleid);
		Set<Menu> menuSets=role.getMenus();
		List<Integer>  menuIds = new ArrayList<Integer>();
		for (Menu menu : menuSets) {
			Integer menuId = menu.getId();
			menuIds.add(menuId);
		}
		modelView.addObject("menusId", JSONObject.toJSONString(menuIds));
		modelView.addObject("erole", role);
		
		
		modelView.addObject("level", level);
		return modelView;
	}
	
	/**
	 * 角色新增
	 * @param role
	 * @param menuIds
	 * @return
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject add(Role role,String menuIds){
		User user = ShiroUtils.getCurrentUser();
		role.setOwner(user.getId());
		//设置customerId
		Customer customer = user.getCustomer();
		Integer customerId = 0;
		if(customer != null){
			customerId = customer.getId();
		}
		role.setCustomerId(customerId);
		roleService.saveOne(role);
		String[] menus = menuIds.split(",");
		for(String menuId:menus){
			RoleMenuMap roleMenu =new RoleMenuMap();
			roleMenu.setMenuId(Integer.parseInt(menuId));
			roleMenu.setRoleId(role.getId());
			roleMenuService.saveOne(roleMenu);
		}
		JSONObject json = new JSONObject();
		json.put("succcess", true);
		return json;
	}
	
	/**
	 * 角色编辑页面
	 * @param roleid
	 * @return
	 */
	@RequestMapping(value="/edit/{roleid}")
	public ModelAndView edit(@PathVariable Integer roleid) {
		ModelAndView mav = ModelAndViewUtils.createEditFormModelAndView("common/role_edit");
		User user = ShiroUtils.getCurrentUser();
		System.out.println(user.getRoles().get(0).getId());
		Role role = roleService.getOne(roleid);
		Set<Menu> menuSets=role.getMenus();
		List<Integer>  menuIds = new ArrayList<Integer>();
		for (Menu menu : menuSets) {
			Integer menuId = menu.getId();
			menuIds.add(menuId);
		}
		mav.addObject("menusId", JSONObject.toJSONString(menuIds));
		mav.addObject("erole", role);
		return mav;
	}
	
	/**
	 * 角色编辑保存
	 * @param role
	 * @param menuIds
	 * @return
	 */
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject edit(Role role,String menuIds){
		Role  newRole= roleService.getOne(role.getId());
		role.setUsers(newRole.getUsers());
		Set<Menu>  menuSet = new HashSet<Menu>();
		if(StringUtils.isNotEmpty(menuIds)){
			String[] menus = menuIds.split(",");
			for(String menuId:menus){
				Menu menu = new Menu();
				menu.setId(Integer.parseInt(menuId));
				menuSet.add(menu);
			}
		}
		role.setMenus(menuSet);
		role.setIsEffective(Common.FLAG_ACTIVATION);
		//设置customerId
		User user = ShiroUtils.getCurrentUser();
		Customer customer = user.getCustomer();
		Integer customerId = 0;
		if(customer != null){
			customerId = customer.getId();
		}
		role.setCustomerId(customerId);
		roleService.updateOne(role);
		UserRealm  realm = new UserRealm();
		realm.clearAllCachedAuthorizationInfo();
		JSONObject json = new JSONObject();
		json.put("succcess", true);
		return json;
	}
	
	/**
	 * 列表
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/list.do",method=RequestMethod.GET)
	public ModelAndView list(String search) {
		ModelAndView modelView = new ModelAndView("common/role_list");
		List<Role> roles =roleService.findRolesBySearch(search);
		
		User user = ShiroUtils.getCurrentUser();
		modelView.addObject("user", user);
		
		modelView.addObject("list", roles);
		modelView.addObject("search", search);
		return modelView;
	}
	
	/**
	 * 关于本系统
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/system.do", method=RequestMethod.GET)
	public ModelAndView sysAbout() {
		ModelAndView modelView = new ModelAndView("common/systemAbout");
		return modelView;
	}
	
	/**
	 * 菜单保存
	 * @param role
	 * @return
	 */
	@RequestMapping(value = "/save.do",method=RequestMethod.POST)
	public ModelAndView save(Role role) {
		ModelAndView modelView = new ModelAndView("redirect:/cms/role/list.do");
		if(role !=null){
			User user = ShiroUtils.getCurrentUser();
			role.setOwner(user.getId());
			roleService.saveOne(role);
		}
		return modelView;
	}
	
	
	@RequestMapping(value="/valiRoleName")
	@ResponseBody
	public boolean valiRoleName(String roleName,Integer roleid) {
		return roleService.valiRoleName(roleName,roleid);
	}
	
	
	/**
	 * 
	* @功能: 删除角色
	* @作者: Bruce Tie
	* @创建日期: 2015年3月30日  下午4:11:31
	 */
	@RequestMapping(value = "del/{roleId}",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject delete(@PathVariable Integer roleId,HttpServletRequest request) {
		return roleService.removeById(roleId);
	}
	
	@Resource
	private MenuService permissionService;
	@ResponseBody
	@RequestMapping(value="/getPermissionTree")
	public JSONArray getPermissionTree(Integer roleId) {
		return permissionService.getMenuTreeJson(roleId);
	}
	
	@Resource
	private UserService userService;
	
	@Resource
	private RoleService roleService;
	
	
}
