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
import com.flea.common.pojo.Log;
import com.flea.common.service.LogService;

/**
 * 系统日志Controller
 * @author bruce
 * @version 2016-09-01
 */
@Controller
@RequestMapping(value = "common/log")
public class LogController  {

	@Autowired
	private LogService logService;
	
	
	
	@RequestMapping(value = {"list", ""})
	public ModelAndView list(Log log, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<Log> page=logService.find(new Page<Log>(request,response), log);
        model.addAttribute("page",page);
        model.addAttribute("log",log);
		return new ModelAndView("system/providerList");
	}

	@RequestMapping(value = "form")
	public String form(Log log, Model model) {
		model.addAttribute("log", log);
		return "common/logForm";
	}

	@RequestMapping(value = "save")
	public ModelAndView save(Log log, Model model) {
		logService.saveOne(log);
		return new ModelAndView("redirect:list.html");
	}
	
	@RequestMapping(value = "del")
	@ResponseBody
	public JSONObject delete(Integer id, RedirectAttributes redirectAttributes) {
	return JsonUtil.createSuccessJson(logService.deleteById(id));
	}

}
