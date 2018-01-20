package com.flea.common.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.flea.common.Common;
import com.flea.common.dao.AreaDao;
import com.flea.common.dao.CityDao;
import com.flea.common.dao.ProvinceDao;
import com.flea.common.pojo.Area;
import com.flea.common.pojo.City;
import com.flea.common.pojo.InitData;
import com.flea.common.pojo.Province;
import com.flea.common.pojo.User;
import com.flea.common.service.SystemAreasService;
import com.flea.common.util.ShiroUtils;
@Service
public class SystemAreasServiceImpl implements SystemAreasService{
	
	@Resource
	private ProvinceDao provinceDao;
	@Resource
	private CityDao cityDao;
	@Resource
	private AreaDao areaDao;
	
	@Override
	public List<Province> listProvince() {
		List<Province> list = new ArrayList<Province>();
		User user =ShiroUtils.getCurrentUser();
		String level = ShiroUtils.getCurrentUserRoleLevel(user);
		if(level.equals(Common.ROLE_FIRST_LEVLE)){
			String hql ="from Province as province order by id";
			list = provinceDao.getListByHQL(hql, null);
		}else {
			list = provinceDao.findByCustomerId(user.getCustomer().getId());
		}
		return list;
	}

	@Override
	public List<City> listCity(String provinceCode) {
		List<City> list = new ArrayList<City>();
		User user =ShiroUtils.getCurrentUser();
		String level = ShiroUtils.getCurrentUserRoleLevel(user);
		if(level.equals(Common.ROLE_FIRST_LEVLE)){
			StringBuffer buffer = new StringBuffer();
			buffer.append("from City as city ");
			List<Object> values = new ArrayList<Object>();
			if(null != provinceCode){
				buffer.append("where provinceCode = ? order by id");
				values.add(provinceCode.toString());
			}
	 		list = cityDao.getListByHQL(buffer.toString(), values);
		}else {
			list = cityDao.findByCustomerId(provinceCode,user.getCustomer().getId());
		}
		return list;
	}

	@Override
	public List<Area> listArea(String cityCode) {
		List<Area> list = new ArrayList<Area>();
		User user =ShiroUtils.getCurrentUser();
		String level = ShiroUtils.getCurrentUserRoleLevel(user);
		if(level.equals(Common.ROLE_FIRST_LEVLE)){
			StringBuffer buffer = new StringBuffer();
			buffer.append("from Area as Area ");
			List<Object> values = new ArrayList<Object>();
			if(null != cityCode){
				buffer.append("where cityCode = ? order by id");
				values.add(cityCode.toString());
			}
			list = areaDao.getListByHQL(buffer.toString(), values);
		}else {
			list = areaDao.findByCustomerId(cityCode,user.getCustomer().getId());
		}
		return list;
	}
	
	
	/**
	 * 地区信息初始化
	 */
	public void initArea(){
		List<Province> listProvince = this.listProvince();
		List<City> listCityS = this.listCity(null);
		for(Province p:listProvince){
			List<City> listCity = this.listCity(p.getCode());
			InitData.province.put(p.getCode(),p.getName());
			InitData.city.put(p.getCode(), listCity);
		}
		for(City c:listCityS){
			List<Area> listAreas = this.listArea(c.getCode());
			InitData.areas.put(c.getCode(), listAreas);
		}
		/*
		List<Province> listProvince = this.listProvince();
		for(Province p:listProvince){
			List<City> listCity = this.listCity(p.getCode());
			Set<City> setCity = new HashSet<City>(listCity);
			p.setCity(setCity);	
			for(City c:listCity){
				List<Area> listArea = this.listArea(c.getCode());
				Set<Area> setArea = new HashSet<Area>(listArea);
				c.setArea(setArea);
			}
		}
		InitData.province = listProvince;
		*/
	}

}
