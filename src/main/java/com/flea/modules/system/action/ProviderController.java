package com.flea.modules.system.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.flea.common.pojo.Page;
import com.flea.common.util.JsonUtil;
import com.flea.modules.system.pojo.Provider;
import com.flea.modules.system.service.ProviderService;

/**
 * 供应商管理Controller
 * @author Bruce
 * @version 2016-01-07
 */
@Controller
@RequestMapping(value = "common/provider")
public class ProviderController  {

	@Autowired
	private ProviderService providerService;
	
	
	
	@RequestMapping(value = {"list", ""})
	public ModelAndView list(Provider provider, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Provider> page=providerService.find(new Page<Provider>(request,response), provider);
        model.addAttribute("page",page);
        model.addAttribute("provider", provider);
		return new ModelAndView("system/providerList");
	}

	@RequestMapping(value = "form")
	public String form(Provider provider, Model model) {
		if(provider.getId()!=null)
		provider = providerService.findProvider$Region(provider.getId());
		model.addAttribute("provider", provider);
		return "system/providerForm";
	}

	@RequestMapping(value = "save")
	public ModelAndView save(Provider provider, Model model) {
		providerService.saveOne(provider);
		return new ModelAndView("redirect:list.html");
	}
	
	@RequestMapping(value = "del/{id}",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject delete(@PathVariable  Integer id) {
	return JsonUtil.createSuccessJson(providerService.deleteById(id));
	}

}
