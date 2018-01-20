package com.flea.common.dao;

import java.util.List;

import com.flea.common.pojo.Area;
import com.flea.common.pojo.City;
import com.flea.common.pojo.Province;

public interface AreaDao extends BaseDao<Area, Long>{
	
	Area  findAreaByCode(String code);
	
	List<Area>  findByCity(String city);
	
	List<Area> findByCustomerId(String cityCode,Integer customerId);

}
