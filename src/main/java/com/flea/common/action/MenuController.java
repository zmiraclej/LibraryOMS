/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.flea.common.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONObject;
import com.flea.common.pojo.Menu;
import com.flea.common.pojo.Page;
import com.flea.common.service.MenuService;
import com.flea.common.util.JsonUtil;
import com.google.common.collect.Lists;

/**
 * 菜单管理Controller
 * @author Bruce
 * @version 2016-01-12
 */
@Controller
@RequestMapping(value = "common/menu")
public class MenuController  {

	@Autowired
	private MenuService menuService;
	
	@RequestMapping(value = {"list", ""})
	public ModelAndView list(Menu menu, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<Menu> list = Lists.newArrayList();
		List<Menu> sourcelist = menuService.getMenuList();
		Menu.sortList(list, sourcelist, 1);
        model.addAttribute("list", list);
		return  new ModelAndView("common/menuList");
	}
	
	@RequestMapping(value="/getMenuNodes")
	@ResponseBody
	public List<Map<String, Object>> getGrea(Integer pid){
		if (pid==0||"".equals(pid)) {
			pid=1;
		}
		List<Map<String, Object>> nodes = menuService.findMapByPid(pid);
		return nodes;
	}

	@RequestMapping(value = "form")
	public String form(Menu menu, Model model) {
//		if (menu.getParent()==null||menu.getParent().getId()==null){
//			menu.setParent(new Menu(1));
//		}
		//menu.setParent(systemService.getMenu(menu.getParent().getId()));
		if(menu.getId()!=null)
		menu = menuService.findMenu$Parent(menu.getId());
		model.addAttribute("menu", menu);
		return "common/menuForm";
	}

	@RequestMapping(value = "save")
	public ModelAndView save(Menu menu, Model model) {
		if(menu.getId()==null)
			menuService.saveOne(menu);
		else 
			menuService.updateOne(menu);
		return new ModelAndView("redirect:list.html");
	}
	
	@RequestMapping(value = "del/{id}")
	@ResponseBody
	public JSONObject delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
	return JsonUtil.createSuccessJson(menuService.deleteById(id));
	}

}
