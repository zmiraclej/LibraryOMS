package com.flea.common.service;



import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.flea.common.pojo.Menu;
import com.flea.common.pojo.Page;

/**
 * 菜单管理Service
 * @author Bruce
 * @version 2016-01-12
 */

public interface MenuService extends BaseService<Menu,Integer> {

   	Page<Menu> find(Page<Menu> page,Menu menu);
   	
	List<Map<String,Object>> findMapByPid(Integer pid);
	
	List<Menu> getMenuList();
	
	
	Menu findMenu$Parent(Integer id);
	
	JSONArray getMenuTreeJson(Integer roleId);
}
