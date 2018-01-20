package com.flea.common.service;

import java.util.List;

import com.flea.common.pojo.Area;
import com.flea.common.pojo.City;
import com.flea.common.pojo.Province;

public interface SystemAreasService {
	//查询省份列表
	public List<Province> listProvince();
	//查询城市列表
	public List<City> listCity(String provinceCode);
	//查询地区列表
	public List<Area> listArea(String cityCode);
	
}
