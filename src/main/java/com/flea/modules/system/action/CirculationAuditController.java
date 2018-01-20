package com.flea.modules.system.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
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
import com.flea.modules.system.pojo.CirculationAudit;
import com.flea.modules.system.pojo.vo.LibraryCirculation;
import com.flea.modules.system.service.CirculationAuditService;
import com.flea.modules.system.service.CirculationService;

/**
 * 跨客户馆际流通审核Controller
 * @author bruce
 * @version 2016-08-22
 */
@Controller
@RequestMapping(value = "system/circulationAudit")
public class CirculationAuditController  {
	
	@Autowired
	private CustomerService customerService;
	@Autowired
	private CirculationAuditService circulationAuditService;
	@Autowired
	private CirculationService circulationService;

	
	
	@RequestMapping(value = {"list", ""})
	public ModelAndView list(CirculationAudit circulationAudit,String search,HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<CirculationAudit> page=circulationAuditService.find(new Page<CirculationAudit>(request,response), circulationAudit,search);
        model.addAttribute("page",page);
        model.addAttribute("circulationAudit",circulationAudit);
		return new ModelAndView("system/providerList");
	}

	@RequestMapping(value = "form")
	public String form(CirculationAudit circulationAudit, Model model) {
		model.addAttribute("circulationAudit", circulationAudit);
		return "system/circulationAuditForm";
	}

	/**
	 * 查询自己的申请
	 * @param circulationAudit
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "self")
	public String selfAudit(CirculationAudit circulationAudit,String search,HttpServletRequest request, HttpServletResponse response,Model model) {
	    User user =  ShiroUtils.getCurrentUser();
		circulationAudit.setUserId(user.getId());
		Page<CirculationAudit> page=circulationAuditService.find(new Page<CirculationAudit>(request,response), circulationAudit,search);
        model.addAttribute("page",page);
        model.addAttribute("search",search);
        model.addAttribute("circulationAudit",circulationAudit);
		return "library/circulate/guanjiliutong_set_myapply";
	}
	
	/**
	 * 申请详情
	 * @param auditId
	 * @param review
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "detail")
	public String selfDetail(Integer auditId,Integer review,Integer operator,Model model) {
		User user =  ShiroUtils.getCurrentUser();
		String level = ShiroUtils.getCurrentUserRoleLevel(user);
		CirculationAudit  audit = circulationAuditService.getOne(auditId);
		List<LibraryCirculation>  selfList = circulationAuditService.findByAuditId(auditId);
		List<LibraryCirculation>  targetList = circulationAuditService.findByTargetIdAndScope(audit.getTargetId(),audit.getTargetScope());
		if(user.getId().equals(audit.getUserId())){
			model.addAttribute("proposer","1");
		}else {
			model.addAttribute("proposer","0");
		}
		model.addAttribute("level", level);
        model.addAttribute("audit",audit);
        model.addAttribute("review",review);
        model.addAttribute("operator", operator);
        model.addAttribute("self",selfList);
        model.addAttribute("target",targetList);
        model.addAttribute("status", review);
		return "library/circulate/guanjiliutong_set_myapply_look";
	}
	
	/**
	 * 发送审核申请
	 * @param action
	 * @param checkedId
	 * @param customerCode
	 * @param scope
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "send")
	@ResponseBody
	public JSONObject send(String action,String checkedId,String customerCode,int scope, Model model) {
		Customer targetCustomer= customerService.findByCustomerCode(customerCode);
		Boolean flag = circulationAuditService.saveAuditApply(targetCustomer,action, checkedId, targetCustomer.getId(), scope);
		return JsonUtil.createSuccessJson(flag);
	}
	
	/**
	 * 审核申请列表
	 * @param circulationAudit
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "review")
	public String review(CirculationAudit circulationAudit,String search,HttpServletRequest request, HttpServletResponse response,Model model) {
	   User user =  ShiroUtils.getCurrentUser();
	   Customer customer = user.getCustomer();
	   if(customer!=null)
	   circulationAudit.setTargetId(customer.getId());
		Page<CirculationAudit> page=circulationAuditService.find(new Page<CirculationAudit>(request,response), circulationAudit,search);
        model.addAttribute("page",page);
        model.addAttribute("search",search);
        model.addAttribute("circulationAudit",circulationAudit);
		return "library/circulate/guanjiliutong_set_toexamine_apply";
	}
	
	
	/**
	 * 更新审核状态
	 * @param auditId
	 * @param status
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	public JSONObject updateStatus(Integer auditId, String status,Model model) {
		Boolean falg =circulationAuditService.updateStatusById(auditId, status);
		return JsonUtil.createSuccessJson(falg);
	}
	
	
	/**
	 * 查询自己的申请
	 * @param circulationAudit
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "audit")
	public String audit(CirculationAudit circulationAudit,String search,HttpServletRequest request, HttpServletResponse response,Model model) {
		circulationAudit.setStatus(Common.FLAG_ENABLE);
		Page<CirculationAudit> page=circulationAuditService.find(new Page<CirculationAudit>(request,response), circulationAudit,search);
        model.addAttribute("page",page);
        model.addAttribute("search",search);
        model.addAttribute("circulationAudit",circulationAudit);
		return "library/circulate/guanjiliutong_audit";
	}
	
	
	@RequestMapping(value = "del")
	@ResponseBody
	public JSONObject delete(Integer id, RedirectAttributes redirectAttributes) {
		return JsonUtil.createSuccessJson(circulationAuditService.deleteById(id));
	}

}
