package com.flea.modules.customer.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.util.SystemOutLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONObject;
import com.flea.common.Common;
import com.flea.common.action.BaseAction;
import com.flea.common.pojo.Page;
import com.flea.common.pojo.Province;
import com.flea.common.pojo.Role;
import com.flea.common.pojo.User;
import com.flea.common.service.RoleService;
import com.flea.common.service.SystemAreasService;
import com.flea.common.service.UserRoleMapService;
import com.flea.common.util.CacheUtils;
import com.flea.common.util.Config;
import com.flea.common.util.JsonUtil;
import com.flea.common.util.ModelAndViewUtils;
import com.flea.common.util.ShiroUtils;
import com.flea.modules.customer.pojo.Customer;
import com.flea.modules.customer.pojo.CustomerContact;
import com.flea.modules.customer.pojo.CustomerLevel;
import com.flea.modules.customer.pojo.CutomerLibrary;
import com.flea.modules.customer.service.CustomerLevelService;
import com.flea.modules.customer.service.CustomerService;
import com.flea.modules.report.pojo.BorrowerReport;
import com.flea.modules.report.pojo.vo.QueryCriteria;
import com.flea.modules.report.service.VLibraryBookService;
import com.flea.modules.system.util.PublicDataUtils;

/**
 * 客户Controller
 * 
 * @author Bruce
 * @version 2016-04-15
 */
@Controller
@RequestMapping(value = "cms/customer")
public class CustomerController extends BaseAction {

	@Autowired
	private CustomerService customerService;
	@Autowired
	private RoleService roleService;
	@Resource
	private SystemAreasService areaService;
	@Resource
	private VLibraryBookService vLibraryBookService;
	@Resource
	private UserRoleMapService userRoleService;
	@Resource
	private CustomerLevelService customerLevelService;

	@RequestMapping(value = { "list" })
	public ModelAndView list(Customer customer, String search, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		Page<Customer> page = customerService.find(new Page<Customer>(request, response), search, customer);
		model.addAttribute("page", page);
		model.addAttribute("customer", customer);
		model.addAttribute("search", search);
		return new ModelAndView("customer/customer_list");
	}

	/**
	 * 客户新增页面
	 */
	@RequestMapping(value = "add")
	public ModelAndView add(QueryCriteria query, Customer customer, Model model, String save) {
		ModelAndView mav = ModelAndViewUtils.createAddFormModelAndView("customer/customer_add");
		CacheUtils.put("startCode", "");
		CacheUtils.put("endCode", "");
		User user = new User();
		double minQuota = 1000, maxQuota = 1000;
		CustomerContact contact = new CustomerContact();
		// 新增
		List<CustomerLevel> customerLevelList = customerService.getCustomerLevelList();
		model.addAttribute("quota", String.valueOf(minQuota).replace(".0", "") + "-" + String.valueOf(maxQuota).replace(".0", ""));
		model.addAttribute("user", user);
		model.addAttribute("sendTime", user.getPassword() == null ? 0 : 1);
		model.addAttribute("contact", contact);
		model.addAttribute("customer", customer);
		model.addAttribute("customerLevelList", customerLevelList);
		model.addAttribute("save", save);
		setCustomerRole(mav);
		return mav;
	}

	/**
	 * 客户修改页面
	 */
	@RequestMapping(value = "edit")
	public ModelAndView edit(QueryCriteria query, Customer customer, Model model, String save) {
		ModelAndView mav = ModelAndViewUtils.createAddFormModelAndView("customer/customer_edit");
		CacheUtils.put("startCode", "");
		CacheUtils.put("endCode", "");
		
		double minQuota = 1000, maxQuota = 1000;
		CustomerContact contact = new CustomerContact();
		List<CustomerLevel> customerLevelList = null;
		customer = customerService.getOne(customer.getId());
		// 处理客户 - 联系人 1对多(老设计缺陷，)
		User user = new User();
		//首次发送密码
		Integer sendTime = 0;
		if(customer.getUsers().size() > 0){
		user = customer.getUsers().get(0);
		sendTime = user.getPassword() == null ?  0 : 1;
		model.addAttribute("user", user);
		model.addAttribute("sendTime", sendTime); //密码
		}

		
		if (customer.getContacts().size() > 0) contact = customer.getContacts().get(0);
		minQuota = customer.getMinQuota();
		maxQuota = customer.getMaxQuota();
		//客户类型
		customerLevelList = customerService.getCustomerLevelList(customer.getId());
		CustomerLevel customerLevel = customerLevelService.getCustomerLevelByCustomer(customer);
		model.addAttribute("customerLevel", customerLevel);
		model.addAttribute("quota", String.valueOf(minQuota).replace(".0", "") + "-" + String.valueOf(maxQuota).replace(".0", "")); //额度
		model.addAttribute("contact", contact);
		model.addAttribute("customer", customer);
		model.addAttribute("customerLevelList", customerLevelList);
		model.addAttribute("save", save);
		
		setCustomerRole(mav);
		return mav;
	}

	@RequestMapping(value = "get/{customerId}")
	public String get(@PathVariable Integer customerId, Model model) {
		Customer customer = customerService.getOne(customerId);
		User loginUser = ShiroUtils.getCurrentUser();
		if (loginUser.isAdmin()) {
			model.addAttribute("roles", roleService.findAll());
		} else {
			model.addAttribute("roles", loginUser.getRoles());
		}
		model.addAttribute("customer", customer);
		model.addAttribute("action", "edit");
		return "customer/customer";
	}

	@RequestMapping(value = "edit/{id}")
	public ModelAndView edit(@PathVariable Integer id, Model model) {
		ModelAndView mav = ModelAndViewUtils.createAddFormModelAndView("customer/customer_edit");
		Customer customer = customerService.getOne(id);
		List<Province> provices = areaService.listProvince();
		List<String> levelList = PublicDataUtils.getLibraryLevels();
		model.addAttribute("provices", provices);
		model.addAttribute("levels", levelList);
		model.addAttribute("customer", customer);
		model.addAttribute("action", "edit");
		setCustomerRole(mav);
		return mav;
	}

	public void setCustomerRole(ModelAndView mav) {
		mav.addObject("roles", roleService.findRolesByLevel(Common.ROLE_SECOND_LEVLE));
	}

	@RequestMapping(value = "save")
	public void save(Customer customer, CutomerLibrary cutomerLibrary, Model model, HttpServletRequest req,
			HttpServletResponse response) throws IOException {
		boolean success = customerService.saveOrUpdateCustomer(customer, cutomerLibrary);
		int customerlevel = customerLevelService.getLevelByCustomer(customer);
		JSONObject json = new JSONObject();
		json.put("customerId", customer.getId());
		// 返回客户代码给页面
		json.put("customerHallCode", customer.getHallCode());
		json.put("customerlevel", customerlevel);
		json.put("userId", customer.getUsers().get(0).getId());
		json.put("success", success);
		System.out.println("json is : " + json);
		this.renderJson(response, json.toJSONString());
	}

	@RequestMapping(value = "assgin")
	@ResponseBody
	public Map<String, String> assignLibraryCode(String startCode, String prevCode, Integer number, Boolean modify) {
		Map<String, String> codeMap = customerService.assignLibraryCode(startCode, prevCode, number, modify);
		return codeMap;
	}

	@RequestMapping(value = "del/{id}", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
		return JsonUtil.createSuccessJson(customerService.deleteById(id));
	}

	@RequestMapping("/download")
	public String download(String fileName, HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {
		String prefix = fileName.substring(0, fileName.lastIndexOf("_"));
		String suffix = fileName.substring(fileName.lastIndexOf("."));
		response.setCharacterEncoding("utf-8");
		response.setContentType("multipart/form-data");
		response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(prefix + suffix, "UTF-8"));
		try {
			String path = Config.getProperty("uploadFile");
			InputStream inputStream = new FileInputStream(
					new File(path + File.separator + "uploads" + File.separator + fileName));
			response.getOutputStream();
			OutputStream os = response.getOutputStream();
			byte[] b = new byte[2048];
			int length;
			while ((length = inputStream.read(b)) > 0) {
				os.write(b, 0, length);
			}
			// 这里主要关闭。
			os.close();
			inputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 返回值要注意，要不然就出现下面这句错误！
		return null;
	}

	/**
	 * 
	 * @method:deleteCustomerLib 删除分配馆
	 * @Description:deleteCustomerLib
	 * @author: HeTao
	 * @date:2016年8月21日 上午11:09:26
	 * @param:@param id
	 * @param:@param redirectAttributes
	 * @return:void
	 */
	@RequestMapping("delLib")
	@ResponseBody
	public boolean deleteCustomerLib(Integer id, RedirectAttributes redirectAttributes) {
		return customerService.deleteLib(id);
	}

}
