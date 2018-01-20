package com.flea.modules.system.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flea.common.service.impl.BaseServiceImpl;
import com.flea.modules.system.dao.RegionDao;
import com.flea.modules.system.pojo.Region;
import com.flea.modules.system.service.RegionService;

/**
 * 区域管理Service
 * @author Bruce
 * @version 2016-01-06
 */

@Service
public class RegionServiceImpl extends BaseServiceImpl<Region,Integer> implements RegionService{

	@Autowired
	private RegionDao regionDao;
	
	
	@Autowired
	public  RegionServiceImpl(RegionDao regionDao) {
		super(regionDao);
		this.regionDao = regionDao;
	}


	/* (non-Javadoc)
	 * @see com.flea.common.service.RegionService#findByPid(java.lang.Integer)
	 */
	@Override
	public List<Map<String,Object>> findMapByPid(Integer pid) {
		List<Region> regions = regionDao.findByPid(pid);
		List<Map<String,Object>> nodes = new ArrayList<Map<String,Object>>();
		for(Region region:regions){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("pid", pid);
			map.put("id",region.getId());
			map.put("type",region.getType()+"");
			map.put("name",region.getName());
			List<Region> seeAreas = regionDao.findByPid(pid);
			if (seeAreas.size()>0) {
				map.put("isParent", "true");
			}
			nodes.add(map);
		}
		return nodes;
	}
	
	
}
