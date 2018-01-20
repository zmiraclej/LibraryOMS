package com.flea.common.dao;

import java.util.List;

import com.flea.common.pojo.Area;
import com.flea.common.pojo.City;
import com.flea.common.pojo.Province;

public interface CityDao extends BaseDao<City, Long>{

	City  findCityByCode(String code);
	
	List<City>  findByProvince(String province);
	
	List<City> findByCustomerId(String provinceCode,Integer customerId);
}
