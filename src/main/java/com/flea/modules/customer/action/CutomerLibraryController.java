package com.flea.modules.customer.action;


import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.flea.common.pojo.Area;
import com.flea.common.pojo.City;
import com.flea.common.pojo.Page;
import com.flea.common.pojo.Province;
import com.flea.common.pojo.User;
import com.flea.common.service.RoleService;
import com.flea.common.service.SystemAreasService;
import com.flea.common.util.FileUtils;
import com.flea.common.util.JsonUtil;
import com.flea.common.util.ModelAndViewUtils;
import com.flea.common.util.ShiroUtils;
import com.flea.modules.customer.pojo.Customer;
import com.flea.modules.customer.pojo.CutomerLibrary;
import com.flea.modules.customer.service.CustomerService;
import com.flea.modules.customer.service.CutomerLibraryService;
import com.flea.modules.system.util.PublicDataUtils;

/**
 * 分配馆号Controller
 * @author Bruce
 * @version 2016-04-15
 */
@Controller
@RequestMapping(value = "cms/customerLibrary")
public class CutomerLibraryController  {

	@Autowired
	private CutomerLibraryService cutomerLibraryService;
	@Autowired
	private RoleService roleService;
	@Resource
	private SystemAreasService areaService;
	@Autowired
	private CustomerService customerService;
	
	
	@RequestMapping(value = {"list", ""})
	public ModelAndView list(CutomerLibrary cutomerLibrary,String search, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<CutomerLibrary> page=cutomerLibraryService.find(new Page<CutomerLibrary>(request,response),search,cutomerLibrary);
        model.addAttribute("page",page);
        model.addAttribute("cutomerLibrary",cutomerLibrary);
        model.addAttribute("search",search);
        return new ModelAndView("customer/customer_list");
	}

	@RequestMapping(value = "findByCustomerId/{customerId}")
	public String findListByCutomerId(@PathVariable Integer customerId,Model model) {
		List<CutomerLibrary> libraries =cutomerLibraryService.findListByCustomerId(customerId);
		model.addAttribute("libraries", libraries);
		model.addAttribute("action", "edit");
		return "customer/library_list";
	}
	
	
	@RequestMapping(value = "get")
	@ResponseBody
	public JSONObject get(Integer id, RedirectAttributes redirectAttributes) {
		CutomerLibrary customerLibrary = cutomerLibraryService.getOne(id);
		String province = customerLibrary.getProvince().getCode();
		String city = customerLibrary.getCity().getCode();
		String area = customerLibrary.getArea().getCode();
		
		JSONObject  json = new JSONObject();
		json.put("provice", province);
		json.put("city", city);
		json.put("area", area);
		List<City> areaList = areaService.listCity(province);
		Map<String, String> cityMap = new HashMap<String,String>();
		for(City c:areaList){
			cityMap.put(c.getCode(), c.getName());	
		}
		List<Area> list = areaService.listArea(city);
		Map<String, String> areaMap = new HashMap<String,String>();
		for(Area c:list){
			areaMap.put(c.getCode(), c.getName());
		}
		json.put("citylist", cityMap);
		json.put("arealist", areaMap);
		json.put("usedCodeNumber", customerLibrary.getUsedCodeNumber());
		json.put("attachementFile", customerLibrary.getAttachementFile());
		json.put("libraryLevel", customerLibrary.getLibraryLevel());
		json.put("libraryNumber", customerLibrary.getLibraryNumber());
		json.put("assinCode", customerLibrary.getAssignCode());
		json.put("chargeStandard", customerLibrary.getChargeStandard());
		json.put("chargeMoney", customerLibrary.getChargeMoney());
		json.put("chargeStartDate", customerLibrary.getChargeStartDate());
		json.put("chargeEndDate", customerLibrary.getChargeEndDate());
		json.put("contractStartDate", customerLibrary.getContractStartDate());
		json.put("contractEndDate", customerLibrary.getContractEndDate());
		return json;
	}
	
	@RequestMapping(value = "form/{cId}")
	public String form(@PathVariable Integer cId,CutomerLibrary cutomerLibrary, Model model) {
		List<CutomerLibrary> libraries =cutomerLibraryService.findListByCustomerId(cId);
		Customer  customer = customerService.getOne(cId);
		//可建馆区间
		List<String> visibleLibraryList = customerService.getVisibleLibraryRange(customer);
		model.addAttribute("visibleLibraryList", visibleLibraryList);
		model.addAttribute("customer", customer);
		model.addAttribute("libraries", libraries);
		model.addAttribute("cutomerLibrary", cutomerLibrary);
		model.addAttribute("customerId", cId);
		return "customer/customer_library";
	}

	
	@RequestMapping(value = "edit/{id}")
	public ModelAndView edit(@PathVariable Integer id,Model model) {
		ModelAndView mav = ModelAndViewUtils.createAddFormModelAndView("customer/customer_edit");
		CutomerLibrary customerLibrary = cutomerLibraryService.getOne(id);
		Customer  customer = customerLibrary.getCustomer();
		List<Province> provices = areaService.listProvince();
		List<String> levelList = PublicDataUtils.getLibraryLevels();
		model.addAttribute("provices", provices);
		model.addAttribute("levels", levelList);
		model.addAttribute("customer", customer);
		setCDeptAndRole(mav);
		return mav;
	}
	
	public void setCDeptAndRole(ModelAndView mav){
		User loginUser = ShiroUtils.getCurrentUser();
		if (loginUser.isAdmin()) {
			mav.addObject("roles",roleService.findAll());
		}else{
			mav.addObject("roles", loginUser.getRoles());
		}
	}
	
	/**
	 * 保存客户的图书馆批次资料
	 * @param cutomerLibrary
	 * @param model
	 * @param name
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "save")
	public void  save(CutomerLibrary cutomerLibrary, Model model,
			String name, HttpServletRequest request,
		HttpServletResponse response) throws IOException {
		String fileName = FileUtils.uploadFile(request, response,"uploads");
		
		if(StringUtils.isNotEmpty(fileName)){
			cutomerLibrary.setAttachementFile(fileName);
		}
		JSONObject json = new JSONObject();
		if (cutomerLibrary.getLibraryId() == null) {
			//新增批次
			cutomerLibrary.setCreateTime(new Date());
			//cutomerLibraryService.saveOne(cutomerLibrary);
			cutomerLibraryService.addCustomerLibraryAndUpdateLibraryCode(cutomerLibrary);
			Customer customer =	customerService.getOne(cutomerLibrary.getCustomer().getId());
			customerService.updateModifyInfoById(customer.getId());
			
			if(StringUtils.isBlank(customer.getHallCode())){
				String startCode = cutomerLibrary.getStartCode();	
				customerService.updateHallCodeById(startCode, customer.getId());
				json.put("hallCode",cutomerLibrary.getStartCode());
			}else{
				json.put("hallCode",customer.getHallCode());
			}
		} else {
			//修改批次
			Customer customer =	customerService.getOne(cutomerLibrary.getCustomer().getId());
			customerService.updateModifyInfoById(customer.getId());
			json.put("hallCode",customer.getHallCode());
			cutomerLibraryService.updateOne(cutomerLibrary);
		}
		json.put("libraryId", cutomerLibrary.getLibraryId());
		json.put("customerId", cutomerLibrary.getCustomer().getId());
		// json 格式 {"customerId":392,"hallCode":"京文","libraryId":1123}
		response.setContentType("text/html");  
		response.getWriter().write(json.toJSONString());
	}
	
	@RequestMapping(value = "del/{id}")
	@ResponseBody
	public JSONObject delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
	return JsonUtil.createSuccessJson(cutomerLibraryService.deleteById(id));
	}
	
	@RequestMapping(value = "findByArea")
	@ResponseBody
	public JSONArray findByArea(Integer area) {
		return 	cutomerLibraryService.findLevelByAreaCode(area);
	}
	
	@RequestMapping(value = "findCodesById")
	@ResponseBody
	public JSONObject findLibraryCodeById(Integer id) {
		return cutomerLibraryService.findLibraryCodeById(id);
	}

	/**
	 * 
	 * @param id system_customer_library
	 * @return
	 */
	@RequestMapping(value = "findUnusedLibraryCodeById")
	@ResponseBody
	public JSONObject findUnusedLibraryCodeById(Integer id) {
		return cutomerLibraryService.findUnusedLibraryCodeById(id);
	}
}
