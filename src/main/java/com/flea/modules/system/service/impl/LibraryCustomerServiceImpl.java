package com.flea.modules.system.service.impl;


import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.flea.common.Common;
import com.flea.common.pojo.Page;
import com.flea.common.pojo.User;
import com.flea.common.service.impl.BaseServiceImpl;
import com.flea.common.util.ShiroUtils;
import com.flea.modules.customer.pojo.Customer;
import com.flea.modules.system.dao.LibraryCustomerDao;
import com.flea.modules.system.pojo.LibraryCustomer;
import com.flea.modules.system.service.LibraryCustomerService;

@Service
public class LibraryCustomerServiceImpl  extends BaseServiceImpl<LibraryCustomer, Integer> implements LibraryCustomerService{
	
	@Resource
	private LibraryCustomerDao libraryCustomerDao;
	
	@Override
	public Page<LibraryCustomer> findLibCustomer(Page<LibraryCustomer> page,String search, LibraryCustomer library) {
		DetachedCriteria dc = libraryCustomerDao.createDetachedCriteria();
		if(StringUtils.isNotEmpty(search)){
			dc.add(Restrictions.or(Restrictions.like("hallCode", "%"+search+"%"),Restrictions.like("name", "%"+search+"%")));
		}
		//添加馆际流通的权限控制
		User user = ShiroUtils.getCurrentUser();
		String hallCode = user.getHallCode();
		//只查询有效图书馆
		dc.add(Restrictions.eq("isEffective",Common.FLAG_ENABLE));
		//客户新增权限下的维护列表信息
		if(!Common.TZPT_OCCUPY_DEPCODE.equals(user.getHallCode()) && !hallCode.toUpperCase().equals(Common.TZPT_OCCUPY_DEPCODE)){
			dc.add(Restrictions.eq("customerId",user.getCustomer().getId()));
			return libraryCustomerDao.find(page, dc);
		}else{
			//平台审核权限的维护列表信息
			dc.add(Restrictions.or(Restrictions.eq("libraryStatus", 2),Restrictions.eq("libraryStatus", 12)));
			return libraryCustomerDao.find(page, dc);
		}
//		dc.add(Restrictions.ne("isEffective",Common.FLAG_DISABLE));
//		String hallCode = user.getHallCode();
//		if(!hallCode.toUpperCase().equals(Common.TZPT_OCCUPY_DEPCODE)){
//			Customer  customer = user.getCustomer();
//			dc.add(Restrictions.eq("customerId",customer.getId()));
//		}
	}

	@Override
	public Page<LibraryCustomer> findLibCustomerExamine(Page<LibraryCustomer> page, LibraryCustomer library, String search) {
		DetachedCriteria dc = libraryCustomerDao.createDetachedCriteria();
		if(StringUtils.isNotEmpty(search)){
			dc.add(Restrictions.or(Restrictions.like("hallCode", "%"+search+"%"),Restrictions.like("name", "%"+search+"%")));
		}
		//添加馆际流通的权限控制
		User user = ShiroUtils.getCurrentUser();
		String hallCode = user.getHallCode();
		//客户审核权限下列表信息展示
		if(!Common.TZPT_OCCUPY_DEPCODE.equals(user.getHallCode()) && !hallCode.toUpperCase().equals(Common.TZPT_OCCUPY_DEPCODE)){
			dc.add(Restrictions.eq("customerId",user.getCustomer().getId()));
		}
//		dc.add(Restrictions.ne("isEffective",Common.FLAG_DISABLE));
//		String hallCode = user.getHallCode();
//		if(!hallCode.toUpperCase().equals(Common.TZPT_OCCUPY_DEPCODE)){
//			Customer  customer = user.getCustomer();
//			dc.add(Restrictions.eq("customerId",customer.getId()));
//		}
		dc.add(Restrictions.or(Restrictions.eq("libraryStatus", 1),Restrictions.eq("libraryUpdateStatus", 4),Restrictions.eq("libraryUpdateStatus", 6),Restrictions.eq("libraryUpdateStatus", 9)));
		return libraryCustomerDao.find(page, dc);
	}
}
