/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.flea.modules.customer.action;

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
import com.flea.modules.customer.pojo.LibraryCirculate;
import com.flea.modules.customer.service.LibraryCirculateService;

/**
 * 馆际流通Controller
 * @author bruce
 * @version 2016-05-26
 */
@Controller
@RequestMapping(value = "customer/libraryCirculate")
public class LibraryCirculateController  {

	@Autowired
	private LibraryCirculateService libraryCirculateService;
	
	
	
	@RequestMapping(value = {"list", ""})
	public ModelAndView list(LibraryCirculate libraryCirculate, HttpServletRequest request, HttpServletResponse response, Model model) {
        
		/*Page<LibraryCirculate> page=libraryCirculateService.find(new Page<LibraryCirculate>(request,response), libraryCirculate);
        model.addAttribute("page",page);
        model.addAttribute("libraryCirculate",libraryCirculate);*/
		
		
		
        return new ModelAndView("system/providerList");
	}

	@RequestMapping(value = "form")
	public String form(LibraryCirculate libraryCirculate, Model model) {
		model.addAttribute("libraryCirculate", libraryCirculate);
		return "customer/libraryCirculateForm";
	}

	@RequestMapping(value = "save")
	public ModelAndView save(LibraryCirculate libraryCirculate, Model model) {
		return new ModelAndView("redirect:list.html");
	}
	
	@RequestMapping(value = "del")
	@ResponseBody
	public JSONObject delete(Integer id, RedirectAttributes redirectAttributes) {
	return JsonUtil.createSuccessJson(true);
	}

}
