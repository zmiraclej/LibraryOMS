package com.flea.modules.system.dao;

import com.flea.common.dao.BaseDao;
import com.flea.modules.system.pojo.Warehouse;

public interface WarehouseDao extends BaseDao<Warehouse, Long> {
	public boolean removeById(int id);
}
