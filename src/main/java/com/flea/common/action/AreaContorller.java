package com.flea.common.action;
  
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.flea.common.pojo.Area;
import com.flea.common.pojo.City;
import com.flea.common.pojo.Province;
import com.flea.common.service.SystemAreasService;
import com.google.gson.Gson;

@Controller
@RequestMapping("/area")
public class AreaContorller {
	
	/*
	@ResponseBody
	@RequestMapping(value="/getAreaJson.html")
	public String getAreaJson(){
		System.out.println(new Gson().toJson(InitData.province));
		return new Gson().toJson(InitData.province);
	}*/
	
	@Resource
	private SystemAreasService areaService;
	
	@ResponseBody
	@RequestMapping(value="/getProvince.html",produces = "text/html;charset=UTF-8")
	public String getProvince(){
		List<Province> list = areaService.listProvince();
		Map<String, String> map = new HashMap<String,String>();
		for(Province c:list){
			map.put(c.getCode(), c.getName());	
		}
		return new Gson().toJson(map);
	}
	
	/**
	 * 
	 * 获取地区，客户分级未占用的省
	 * */
	@ResponseBody
	@RequestMapping(value="/getProvinceUnused.html",produces = "text/html;charset=UTF-8")
	public String getProvinceUnused(){
		List<Province> list = areaService.listProvince();
		Map<String, String> map = new HashMap<String,String>();
		for(Province c:list){
			map.put(c.getCode(), c.getName());	
		}
		return new Gson().toJson(map);
	}	
	
	@ResponseBody
	@RequestMapping(value="/getCity.html/{code}",produces = "text/html;charset=UTF-8")
	public String getCity(@PathVariable("code") String code){
		List<City> list = areaService.listCity(code);
		Map<String, String> map = new HashMap<String,String>();
		for(City c:list){
			map.put(c.getCode(), c.getName());	
		}
		return new Gson().toJson(map);
	}
	
	@ResponseBody
	@RequestMapping(value="/getArea.html/{code}",produces = "text/html;charset=UTF-8")
	public String getArea(@PathVariable("code") String code){
		List<Area> list = areaService.listArea(code);
		Map<String, String> map = new HashMap<String,String>();
		for(Area c:list){
			map.put(c.getCode(), c.getName());
		}
		return new Gson().toJson(map);
	}
	
}
