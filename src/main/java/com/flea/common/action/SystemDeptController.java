package com.flea.common.action;

import java.util.List;

import javassist.expr.NewArray;

import org.apache.shiro.crypto.hash.format.Shiro1CryptFormat;
import org.apache.solr.client.solrj.impl.LBHttpSolrClient.Req;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.flea.common.pojo.SystemDept;
import com.flea.common.service.SystemDeptService;
import com.flea.common.util.ShiroUtils;
import com.google.gson.Gson;

@Controller
@RequestMapping("/cms/systemDept")
public class SystemDeptController {
	
	@Autowired
	private SystemDeptService deptService;
	
	/**
	 * @return
	 * 部门管理页面
	 */
	@RequestMapping("/deptPage")
	public ModelAndView deptPage(){
		ModelAndView mav = new ModelAndView("dept/dept");
		List<SystemDept> list = deptService.listSystemDept();
		if(null ==  ShiroUtils.getCurrentUser().getCustomer()){
			mav.addObject("fatherName", "YTSG");
		}else{
			mav.addObject("fatherName", ShiroUtils.getCurrentUser().getCustomer().getName());
		}
		mav.addObject("listDept", list);
		return mav;
	}
	
	/**
	 * @param dept
	 * @return
	 * 添加部门
	 */
	@ResponseBody
	@RequestMapping(value="/addDept",method=RequestMethod.POST)
	public SystemDept addDept(SystemDept dept){
		if(null != ShiroUtils.getCurrentUser().getCustomer()){
			dept.setCustomerId(ShiroUtils.getCurrentUser().getCustomer().getId());
		}
		dept.setIsEffective(1);
		deptService.saveOne(dept);
		return dept;
	}
	
	
	/**
	 * @param dept
	 * @return
	 * 更新部门信息
	 */
	@ResponseBody
	@RequestMapping(value="/updateDept",method=RequestMethod.POST)
	public String updateDept(String id,String name){
		SystemDept one = deptService.getOne(Integer.parseInt(id));
		one.setName(name);
		deptService.updateOne(one);
		return "success";
	}
	
	/**
	 * @param id
	 * @return
	 * 删除部门
	 */
	@ResponseBody
	@RequestMapping(value="/delDept",method=RequestMethod.POST)
	public String delDept(String id){
		if(deptService.checkEmptyDept(Integer.parseInt(id))){
			deptService.deleteById(Integer.parseInt(id));
			return "success";
		}else{
			return "fail";
		}
	}
	
	@ResponseBody
	@RequestMapping(value="/getSubLevel",method=RequestMethod.GET)
	public List<SystemDept> getSublevel(String fatherId){
		List<SystemDept> list = deptService.getlistByFatherId(Integer.parseInt(fatherId));
		return list;
	}
	
	
}
