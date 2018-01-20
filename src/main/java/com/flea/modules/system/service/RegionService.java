package com.flea.modules.system.service;

import java.util.List;
import java.util.Map;

import com.flea.common.service.BaseService;
import com.flea.modules.system.pojo.Region;

/**
 * 区域管理Service
 * @author Bruce
 * @version 2016-01-06
 */

public interface RegionService extends BaseService<Region,Integer> {

	List<Map<String,Object>> findMapByPid(Integer pid);
}
