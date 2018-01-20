package com.flea.common.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flea.common.dao.UserOperationDao;
import com.flea.common.pojo.UserOperation;
import com.flea.common.service.UserOperationService;

@Service
public class UserOperationImplService extends BaseServiceImpl<UserOperation, Integer> implements UserOperationService {
	
	private UserOperationDao userOperationDao;
	

	
	@Autowired
	public UserOperationImplService(UserOperationDao userOperationDao) {
		super(userOperationDao);
		this.userOperationDao = userOperationDao;
	}
	
	@Override
	public void saveOne(UserOperation userOperation) {
		userOperationDao.saveOne(userOperation);
	}

}
