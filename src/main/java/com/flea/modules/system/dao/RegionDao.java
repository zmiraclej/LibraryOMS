package com.flea.modules.system.dao;


import java.util.List;

import com.flea.common.dao.BaseDao;
import com.flea.modules.system.pojo.Region;

/**
 * 区域管理DAO接口
 * @author Bruce
 * @version 2016-01-06
 */

public interface RegionDao extends BaseDao<Region,Integer> {
	
	List<Region> findByPid(Integer pid);
}
