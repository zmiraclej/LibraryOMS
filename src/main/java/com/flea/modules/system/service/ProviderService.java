package com.flea.modules.system.service;



import com.flea.common.pojo.Page;
import com.flea.common.service.BaseService;
import com.flea.modules.system.pojo.Provider;

/**
 * 供应商管理Service
 * @author Bruce
 * @version 2016-01-07
 */

public interface ProviderService extends BaseService<Provider,Integer> {

	Provider findProvider$Region(Integer id);
	
	Page<Provider> find(Page<Provider> page,Provider provider);
	
}
