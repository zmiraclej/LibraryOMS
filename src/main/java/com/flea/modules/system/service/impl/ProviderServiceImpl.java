package com.flea.modules.system.service.impl;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flea.common.pojo.Page;
import com.flea.common.service.impl.BaseServiceImpl;
import com.flea.common.util.AppUtils;
import com.flea.modules.system.dao.ProviderDao;
import com.flea.modules.system.pojo.Provider;
import com.flea.modules.system.service.ProviderService;

/**
 * 供应商管理Service
 * @author Bruce
 * @version 2016-01-07
 */
 @Service
public class ProviderServiceImpl extends BaseServiceImpl<Provider,Integer> implements ProviderService{

	@Autowired
	private ProviderDao providerDao;
	
	
	@Autowired
	public  ProviderServiceImpl(ProviderDao providerDao) {
		super(providerDao);
		this.providerDao = providerDao;
	}


	/* (non-Javadoc)
	 * @see com.flea.common.service.ProviderService#findProvider$Region(java.lang.Integer)
	 */
	@Override
	public Provider findProvider$Region(Integer id) {
		Provider provider = providerDao.getOne(id);
		String regionStr = AppUtils.RegionAndParentName(provider.getRegion(),"\\");
		provider.setRegionAssist(regionStr);
		return provider;
	}


	/* (non-Javadoc)
	 * @see com.flea.common.service.ProviderService#find(com.flea.common.pojo.Page, com.flea.common.pojo.Provider)
	 */
	@Override
	public Page<Provider> find(Page<Provider> page, Provider provider) {
		DetachedCriteria dc = providerDao.createDetachedCriteria();
		if(StringUtils.isNotEmpty(provider.getName())){
			dc.add(Restrictions.eq("name", provider.getName()));
		}
		return providerDao.find(page, dc);
	}
	
	
}
