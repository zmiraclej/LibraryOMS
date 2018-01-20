/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.flea.modules.customer.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.servlet.ModelAndView;
import com.flea.common.util.JsonUtil;
import com.flea.common.pojo.Page;
import com.flea.modules.customer.pojo.CustomerHallCode;
import com.flea.modules.customer.service.CustomerHallCodeService;

/**
 * 客户分配馆号Controller
 * @author bruce
 * @version 2016-05-09
 */
@Controller
@RequestMapping(value = "customer/CustomerHallCode")
public class CustomerHallCodeController  {

	@Autowired
	private CustomerHallCodeService CustomerHallCodeService;
	
	
	
	@RequestMapping(value = {"list", ""})
	public ModelAndView list(CustomerHallCode CustomerHallCode, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<CustomerHallCode> page=CustomerHallCodeService.find(new Page<CustomerHallCode>(request,response), CustomerHallCode);
        model.addAttribute("page",page);
        model.addAttribute("CustomerHallCode",CustomerHallCode);
		return new ModelAndView("system/providerList");
		
	}

	@RequestMapping(value = "form")
	public String form(CustomerHallCode CustomerHallCode, Model model) {
		model.addAttribute("CustomerHallCode", CustomerHallCode);
		return "customer/CustomerHallCodeForm";
	}

	@RequestMapping(value = "save")
	public ModelAndView save(CustomerHallCode CustomerHallCode, Model model) {
		CustomerHallCodeService.saveOne(CustomerHallCode);
		return new ModelAndView("redirect:list.html");
	}
	
	@RequestMapping(value = "del")
	@ResponseBody
	public JSONObject delete(Integer id, RedirectAttributes redirectAttributes) {
	return JsonUtil.createSuccessJson(CustomerHallCodeService.deleteById(id));
	}

}
