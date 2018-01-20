package com.flea.modules.data.dao;

import java.util.List;

import com.flea.common.dao.BaseDao;
import com.flea.modules.data.mapping.BorrowedDetail;
import com.flea.modules.data.mapping.LiteratureDetail;

public interface BorrowedDetailDao extends BaseDao<BorrowedDetail,Long> {
	public List<BorrowedDetail> getBorrowedDetailsBySql(String sqlString, List<Object> values);
}
