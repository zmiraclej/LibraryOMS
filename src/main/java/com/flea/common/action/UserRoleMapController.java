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
import com.flea.common.pojo.UserRoleMap;
import com.flea.common.service.UserRoleMapService;

/**
 * 用户和角色关联Controller
 * @author UserRoleMap
 * @version 2016-04-20
 */
@Controller
@RequestMapping(value = "common/userRoleMap")
public class UserRoleMapController  {

	@Autowired
	private UserRoleMapService userRoleMapService;
	
	
	
	@RequestMapping(value = {"list", ""})
	public ModelAndView list(UserRoleMap userRoleMap, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<UserRoleMap> page=userRoleMapService.find(new Page<UserRoleMap>(request,response), userRoleMap);
        model.addAttribute("page",page);
        model.addAttribute("userRoleMap",userRoleMap);
		return new ModelAndView("system/providerList");
		
//        model.addAttribute("list", list);
//		return new ModelAndView("library/list");
	}

	@RequestMapping(value = "form")
	public String form(UserRoleMap userRoleMap, Model model) {
		model.addAttribute("userRoleMap", userRoleMap);
		return "common/userRoleMapForm";
	}

	@RequestMapping(value = "save")
	public ModelAndView save(UserRoleMap userRoleMap, Model model) {
		userRoleMapService.saveOne(userRoleMap);
		return new ModelAndView("redirect:list.html");
	}
	
	@RequestMapping(value = "del")
	@ResponseBody
	public JSONObject delete(Integer id, RedirectAttributes redirectAttributes) {
	return JsonUtil.createSuccessJson(userRoleMapService.deleteById(id));
	}

}
