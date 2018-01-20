package com.flea.common.dao;

import java.util.List;

import com.flea.common.pojo.Area;
import com.flea.common.pojo.Province;

public interface ProvinceDao extends BaseDao<Province, Long>{

	
	Province findProvinceByCode(String code);
	
	List<Province> findByCustomerId(Integer customerId);
}
