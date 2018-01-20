package com.flea.common.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flea.common.dao.SystemDeptDao;
import com.flea.common.dao.UserDao;
import com.flea.common.pojo.SystemDept;
import com.flea.common.pojo.User;
import com.flea.common.service.SystemDeptService;
import com.flea.common.util.ShiroUtils;
import com.google.gson.Gson;

@Service
@Transactional
public class SystemDeptServiceImpl extends BaseServiceImpl<SystemDept, Long> implements SystemDeptService{

	private SystemDeptDao deptDao;
	@Resource
	private UserDao userDao;
	
	@Autowired
	public SystemDeptServiceImpl(SystemDeptDao deptDao){
		super(deptDao);
		this.deptDao = deptDao;
	}
	
	
	
	@Override
	public boolean checkEmptyDept(int id) {
		// TODO Auto-generated method stub
		StringBuffer buffer = new StringBuffer();
		buffer.append(" select a.id from system_user a where 1=1 ");
		
		buffer.append(" and a.deptId in (");
		buffer.append(" select id from system_dept where FIND_IN_SET(id, getSystemDeptChild('"+id+"')))");
		List<User> list = userDao.getListBySQL(buffer.toString(),null);
		if(list.size() > 0){
			return false;
		}else{
			return true;
		}
	}


	@Override
	public List<SystemDept> listSystemDept() {
		// TODO Auto-generated method stub
		StringBuffer buffer = new StringBuffer();
		buffer.append(" from SystemDept as dept where 1=1 ");
		
		if(null ==  ShiroUtils.getCurrentUser().getCustomer()){
			buffer.append(" and dept.customerId  = '0' ");
		}else{
			buffer.append(" and dept.customerId  = '"+ShiroUtils.getCurrentUser().getCustomer().getId()+"'");
		}
		
		buffer.append(" and level = '1'");
		buffer.append(" and isEffective ='1'");
		List<SystemDept> list = deptDao.getListByHQL(buffer.toString(), null);
		return list;
	}


	@Override
	public List<SystemDept> getlistByFatherId(int fatherId) {
		// TODO Auto-generated method stub
		StringBuffer buffer = new StringBuffer();
		buffer.append(" from SystemDept as dept where 1=1 ");
		buffer.append(" and fatherId = '"+fatherId+"'");
		buffer.append(" and isEffective ='1'");
		List<SystemDept> list = deptDao.getListByHQL(buffer.toString(), null);
		return list;
	}

}
