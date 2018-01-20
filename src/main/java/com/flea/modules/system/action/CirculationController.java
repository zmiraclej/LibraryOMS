package com.flea.modules.system.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONObject;
import com.flea.common.Common;
import com.flea.common.pojo.Page;
import com.flea.common.pojo.User;
import com.flea.common.util.JsonUtil;
import com.flea.common.util.ShiroUtils;
import com.flea.modules.customer.pojo.Customer;
import com.flea.modules.customer.service.CustomerService;
import com.flea.modules.system.pojo.Circulation;
import com.flea.modules.system.pojo.Library;
import com.flea.modules.system.pojo.vo.LibraryCirculation;
import com.flea.modules.system.service.CirculationAuditService;
import com.flea.modules.system.service.CirculationService;
import com.flea.modules.system.service.LibraryService;
import com.flea.modules.system.util.PublicDataUtils;

/**
 * 馆际流通Controller
 * @author bruce
 * @version 2016-08-18
 */
@Controller
@RequestMapping(value = "system/circulation")
public class CirculationController  {


	@Autowired
	private LibraryService libraryService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private CirculationService circulationService;
	@Autowired
	private CirculationAuditService circulationAuditService;
	
	
	@RequestMapping(value = {"list", ""})
	public ModelAndView list(Circulation circulation,String search,HttpServletRequest request, HttpServletResponse response, Model model,String add) {
        Page<LibraryCirculation> page=circulationService.find(new Page<LibraryCirculation>(request,response), circulation,search);
        ModelAndView mav = new ModelAndView("library/circulate/guanjiliutong_set");
        User user =  ShiroUtils.getCurrentUser();
	    String roleLevel =	ShiroUtils.getCurrentUserRoleLevel(user);
		Integer  customerId = 0;
	    if(roleLevel.equals(Common.ROLE_SECOND_LEVLE)){
	    	customerId = user.getCustomer().getId();
	    }
    	int max = circulationService.getMaxScope(customerId)+1;
		List<Library> list = new ArrayList<Library>();
		for (int i = 0; i < max; i++) {
			Library li = new Library();
			//把数字更改字符串。。
			li.setName("范围"+PublicDataUtils.changeNumber((i+1)));
			list.add(li);
		}
		mav.addObject("add",add);
		mav.addObject("search",search);
		mav.addObject("max",list);
		mav.addObject("page",page);
		return mav;
	}
	
	@RequestMapping(value = {"cross"})
	public ModelAndView cross(Circulation circulation,String search,HttpServletRequest request, HttpServletResponse response, Model model,String add) {
        Page<LibraryCirculation> page=circulationService.find(new Page<LibraryCirculation>(request,response), circulation,search);
        Map<String,Integer> auditMap =  circulationAuditService.getAduitCount();
        ModelAndView mav = new ModelAndView("library/circulate/guanjiliutong_set2");
        model.addAttribute("add",add);
        model.addAttribute("search",search);
        model.addAttribute("sumMap",auditMap);
		mav.addObject("page",page);
		return mav;
	}

	@RequestMapping(value = "form")
	public String form(Circulation circulation, Model model) {
		model.addAttribute("circulation", circulation);
		return "system/circulationForm";
	}

	@RequestMapping(value = "save")
	public ModelAndView save(Circulation circulation, Model model) {
		circulationService.saveOne(circulation);
		return new ModelAndView("redirect:list.html");
	}
	
	@RequestMapping(value = "del")
	@ResponseBody
	public JSONObject delete(Integer id, RedirectAttributes redirectAttributes) {
	return JsonUtil.createSuccessJson(circulationService.deleteById(id));
	}
	
	@RequestMapping(value = "scope")
	@ResponseBody
	public List<String> scope(String hallCode) {
		Customer customer= customerService.findByCustomerCode(hallCode);
		if(customer==null)return null;
	   	int max = circulationService.getMaxScope(customer.getId());
	   	List<String> list = new ArrayList<String>();
	   	for(int i=1;i<=max;i++){
	   		list.add("范围"+PublicDataUtils.changeNumber((i)));
	   	}
	   	return list;
	}
	
	
	@RequestMapping(value="/circulate",method=RequestMethod.POST)
	@ResponseBody
	public String circulate(String action,String checkedId,int scope,HttpServletRequest req) throws IOException{
		boolean flag = circulationService.updateLibraryCirculation(action, checkedId, scope);
		if(flag)
		return "location.reload()";
		else
		return "false";
	}

}
