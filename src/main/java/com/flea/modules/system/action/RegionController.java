package com.flea.modules.system.action;

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
import com.flea.common.util.JsonUtil;
import com.flea.modules.system.pojo.Region;
import com.flea.modules.system.service.RegionService;

/**
 * 区域管理Controller
 * @author Bruce
 * @version 2016-01-06
 */
@Controller
@RequestMapping(value = "common/region")
public class RegionController  {

	@Autowired
	private RegionService regionService;
	
	
	
	@RequestMapping(value = {"list", ""})
	public ModelAndView list(Region region, HttpServletRequest request, HttpServletResponse response, Model model) {
        List<Region> list = regionService.findAll();
        model.addAttribute("list", list);
		return new ModelAndView("system/regionList");
	}

	@RequestMapping(value="/getRreaNodes")
	@ResponseBody
	public List<Map<String, Object>> getGrea(Integer pid){
		if ("0".equals(pid)||"".equals(pid)) {
			pid=null;
		}
		List<Map<String, Object>> nodes = regionService.findMapByPid(pid);
		return nodes;
	}

	@RequestMapping(value = "save",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject save(Region region, Model model) {
		JSONObject json = new JSONObject();
		Boolean isSuccess=true;
		try {
			regionService.saveOne(region);
		} catch (Exception e) {
			isSuccess = false;
		}
		json.put("success", isSuccess);
		return json;
	}
	
	@RequestMapping(value = "delArea")
	@ResponseBody
	public JSONObject delete(Integer id, RedirectAttributes redirectAttributes) {
	return JsonUtil.createSuccessJson(regionService.deleteById(id));
	}

}
