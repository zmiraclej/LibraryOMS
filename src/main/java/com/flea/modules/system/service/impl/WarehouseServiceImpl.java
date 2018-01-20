/**  
* @Package com.flea.common.service.impl
* @Description: TODO
* @author bruce
* @date 2015年12月17日 上午10:18:10
* @version V1.0  
*/ 
package com.flea.modules.system.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flea.common.pojo.User;
import com.flea.common.service.impl.BaseServiceImpl;
import com.flea.common.util.ShiroUtils;
import com.flea.modules.system.dao.WarehouseDao;
import com.flea.modules.system.pojo.Warehouse;
import com.flea.modules.system.service.LibraryService;
import com.flea.modules.system.service.WarehouseService;

/**
 * @author bruce
 * @2015年12月17日 上午10:18:10
 */
@Service
public class WarehouseServiceImpl extends BaseServiceImpl<Warehouse, Long> implements WarehouseService {

	@Autowired
	private  WarehouseDao warehouseDao;
	@Autowired
	private  LibraryService libraryService;
	
	public WarehouseServiceImpl() {
		super();
	}

	@Autowired
	public WarehouseServiceImpl(WarehouseDao warehouseDao) {
		super(warehouseDao);
		this.warehouseDao = warehouseDao;
	}

	@Override
	public void saveOne(Warehouse warehouse) {
		User user = ShiroUtils.getCurrentUser();
		warehouse.setLibrary(user.getLibrary());
		super.saveOne(warehouse);
	}

	@Override
	public void updateOne(Warehouse warehouse) {
		User user = ShiroUtils.getCurrentUser();
		warehouse.setLibrary(user.getLibrary());
		warehouseDao.updateOne(warehouse);
	}

	/* (non-Javadoc)
	 * @see com.flea.common.service.WarehouseService#removeById(int)
	 */
	@Override
	public boolean removeById(int id) {
		return 	warehouseDao.removeById(id);
	}

	@Override
	public boolean deleteById(int id) {
		return super.deleteById(id);
	}
	

}
