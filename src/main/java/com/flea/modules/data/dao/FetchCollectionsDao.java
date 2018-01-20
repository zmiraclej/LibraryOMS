package com.flea.modules.data.dao;

import java.util.List;

import com.flea.common.dao.BaseDao;
import com.flea.modules.data.mapping.BorrowedDetail;
import com.flea.modules.data.mapping.FetchCollections;

public interface FetchCollectionsDao extends BaseDao<FetchCollections,Long>{
	public List<FetchCollections> getFetchCollectionsBySql(String sqlString, List<Object> values);
}
