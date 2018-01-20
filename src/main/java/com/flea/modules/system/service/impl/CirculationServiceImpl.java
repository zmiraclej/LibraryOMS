package com.flea.modules.system.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flea.common.Common;
import com.flea.common.pojo.Page;
import com.flea.common.pojo.User;
import com.flea.common.service.impl.BaseServiceImpl;
import com.flea.common.util.ShiroUtils;
import com.flea.modules.report.pojo.SearchResult;
import com.flea.modules.system.dao.CirculationDao;
import com.flea.modules.system.pojo.Circulation;
import com.flea.modules.system.pojo.vo.LibraryCirculation;
import com.flea.modules.system.service.CirculationService;

/**
 * 馆际流通Service
 * @author bruce
 * @version 2016-08-18
 */
 @Service
public class CirculationServiceImpl extends BaseServiceImpl<Circulation,Integer> implements CirculationService{

	@Autowired
	private CirculationDao circulationDao;
	
	
	@Autowired
	public  CirculationServiceImpl(CirculationDao circulationDao) {
		super(circulationDao);
		this.circulationDao = circulationDao;
	}
	
	@Override
	public Page<LibraryCirculation> find(Page<LibraryCirculation> page,Circulation circulation,String search) {
		User user =  ShiroUtils.getCurrentUser();
	    String roleLevel =	ShiroUtils.getCurrentUserRoleLevel(user);
	    Integer  customerId = null;
	    if(roleLevel.equals(Common.ROLE_SECOND_LEVLE)){
	    	customerId = user.getCustomer().getId();
	    }
		SearchResult<LibraryCirculation>  result = circulationDao.findLibraryCirculationList(page, customerId,search);
		page.setList(result.getResult());
		page.setCount(result.getCount());
		return page;
	}

	/* (non-Javadoc)
	 * @see com.flea.modules.system.service.CirculationService#getMaxScope()
	 */
	@Override
	public int getMaxScope(Integer  customerId) {
		return circulationDao.getMaxScope(customerId);
	}

	/* (non-Javadoc)
	 * @see com.flea.modules.system.service.CirculationService#updateLibraryCirculation(java.lang.String, java.lang.String, int)
	 */
	@Override
	public boolean updateLibraryCirculation(String action, String allId,
			int scope) {
		User user =  ShiroUtils.getCurrentUser();
	    String roleLevel =	ShiroUtils.getCurrentUserRoleLevel(user);
		Integer  customerId = 391;
	    if(roleLevel.equals(Common.ROLE_SECOND_LEVLE)){
	    	customerId = user.getCustomer().getId();
	    }
	    String[] arr = allId.split(",");
	    for(String hallCode:arr){
			if("add".equals(action)){
			   Circulation  circulation = circulationDao.findByCustomerIdHallCodeScope(customerId, hallCode, scope);
			   if(circulation == null){//表示是不存在的记录
				   circulation = new Circulation();
				   circulation.setCustomerId(customerId);
				   circulation.setScope(scope);
				   circulation.setHallCode(hallCode);
				   circulation.setCreateDate(new Date());
				   circulationDao.saveOne(circulation);
			   }
			}else if("exit".equals(action)){
				circulationDao.deleteByCustomerIdHallCodeScope(customerId,hallCode, scope);
			}else if("replace".equals(action)){
				circulationDao.deleteByCustomerIdHallCodeScope(null,hallCode, null);
				Circulation circulation = new Circulation();
			    circulation.setCustomerId(customerId);
			    circulation.setScope(scope);
			    circulation.setHallCode(hallCode);
			    circulation.setCreateDate(new Date());
			    circulationDao.saveOne(circulation);
			}
	    }
		return true;
	}

	/* (non-Javadoc)
	 * @see com.flea.modules.system.service.CirculationService#findByCustomerIdAndScope(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public List<Circulation> findByCustomerIdAndScope(Integer customerId,
			Integer scope) {
		return circulationDao.findByCustomerIdAndScope(customerId, scope);
	}
	
}
