package com.flea.modules.system.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.flea.common.pojo.User;
import com.flea.common.util.JsonUtil;
import com.flea.common.util.ShiroUtils;
import com.flea.modules.system.pojo.Warehouse;
import com.flea.modules.system.service.WarehouseService;
import com.google.common.collect.Lists;

@Controller
@RequestMapping(value="/warehouse")
public class WarehouseController {
	
	@Autowired
	private WarehouseService warehouseService;
	
	@RequestMapping(value="/form",method = RequestMethod.GET)	
	public ModelAndView form(Model model){
		model.addAttribute("action", "add");
		return new ModelAndView("library/add_edit");
	}
	
	@RequestMapping(value="/edit/{id}")	
	public ModelAndView edit(@PathVariable Integer id, Model model){
		model.addAttribute("action", "edit");
		Warehouse warehouse = warehouseService.getOne(id);
		model.addAttribute("wh", warehouse);
		return new ModelAndView("library/add_edit");
	}
	
	@RequestMapping(value="/list",method = RequestMethod.GET)	
	public ModelAndView list(Model model){
		List<Object> params = Lists.newArrayList();
		User user = ShiroUtils.getCurrentUser();
		params.add(user.getLibrary().getId());
		List<Warehouse> list = warehouseService.getListByHQL("from Warehouse where library.id=?", params);
		model.addAttribute("list", list);
		return new ModelAndView("library/list");
	}
	
	@RequestMapping(value="/save",method = RequestMethod.POST)
	public ModelAndView save(Warehouse warehouse){
		if(warehouse.getId()==null){
			warehouseService.saveOne(warehouse);
		}else {
			warehouseService.updateOne(warehouse);
		}
		return new ModelAndView("redirect:list.html");
	}

	@ResponseBody
	@RequestMapping(value="del/{id}",method=RequestMethod.POST)
	public JSONObject del(@PathVariable int id) {
		return JsonUtil.createSuccessJson(warehouseService.deleteById(id));
	}
	
}
