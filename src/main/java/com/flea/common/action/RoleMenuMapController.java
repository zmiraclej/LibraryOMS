/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.flea.common.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.servlet.ModelAndView;
import com.flea.common.util.JsonUtil;
import com.flea.common.pojo.Page;
import com.flea.common.pojo.RoleMenuMap;
import com.flea.common.service.RoleMenuMapService;

/**
 * 角色和菜单关联Controller
 * @author bruce
 * @version 2016-04-26
 */
@Controller
@RequestMapping(value = "common/roleMenuMap")
public class RoleMenuMapController  {

	@Autowired
	private RoleMenuMapService roleMenuMapService;
	
	
	
	@RequestMapping(value = {"list", ""})
	public ModelAndView list(RoleMenuMap roleMenuMap, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<RoleMenuMap> page=roleMenuMapService.find(new Page<RoleMenuMap>(request,response), roleMenuMap);
        model.addAttribute("page",page);
        model.addAttribute("roleMenuMap",roleMenuMap);
		return new ModelAndView("system/providerList");
	}

	@RequestMapping(value = "form")
	public String form(RoleMenuMap roleMenuMap, Model model) {
		model.addAttribute("roleMenuMap", roleMenuMap);
		return "common/roleMenuMapForm";
	}

	@RequestMapping(value = "save")
	public ModelAndView save(RoleMenuMap roleMenuMap, Model model) {
		roleMenuMapService.saveOne(roleMenuMap);
		return new ModelAndView("redirect:list.html");
	}
	
	@RequestMapping(value = "del")
	@ResponseBody
	public JSONObject delete(Integer id, RedirectAttributes redirectAttributes) {
	return JsonUtil.createSuccessJson(roleMenuMapService.deleteById(id));
	}

}
