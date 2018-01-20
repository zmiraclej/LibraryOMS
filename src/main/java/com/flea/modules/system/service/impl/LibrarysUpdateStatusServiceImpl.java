package com.flea.modules.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flea.common.service.impl.BaseServiceImpl;
import com.flea.modules.system.dao.LibrarysUpdateStatusDao;
import com.flea.modules.system.pojo.LibrarysUpdateStatus;
import com.flea.modules.system.service.LibrarysUpdateStatusService;
/**
 * 
 * @ClassName: LibrarysUpdateStatusServiceImpl
 * @Description:图书馆用户新增权限的业务层
 * @author QL
 * @date 2017年5月8日 上午10:33:50
 */
@Service
public class LibrarysUpdateStatusServiceImpl extends BaseServiceImpl<LibrarysUpdateStatus, Integer> implements LibrarysUpdateStatusService{
	@Autowired
	private LibrarysUpdateStatusDao librarysUpdateStatusDao;

	/**
	 * 图书馆用户新增权限中维护列表点击编辑之后，保存数据库
	 */
	@Override
	public void saveOne(LibrarysUpdateStatus t) {
		librarysUpdateStatusDao.saveOne(t);
	}
    /**
     * 图书馆如果当前为用户新增权限时，用户的维护列表操作状态为修改驳回时，用户的信息显示修改之前的数据
     */
	@Override
	public boolean updateLibrarys(Integer libraryId) {
		return librarysUpdateStatusDao.updateLibrarys(libraryId);
	}
}
