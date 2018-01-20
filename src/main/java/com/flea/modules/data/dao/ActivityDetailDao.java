package com.flea.modules.data.dao;

import java.util.List;

import com.flea.common.dao.BaseDao;
import com.flea.modules.data.mapping.ActivityDetail;

public interface ActivityDetailDao extends BaseDao<ActivityDetail,Long>{
	public List<ActivityDetail> getActivityDetailBySql(String sqlString, List<Object> values);
}
