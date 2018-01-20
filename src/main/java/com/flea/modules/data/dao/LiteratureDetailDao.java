package com.flea.modules.data.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.flea.common.dao.BaseDao;
import com.flea.modules.data.mapping.LiteratureDetail;
public interface LiteratureDetailDao extends BaseDao<LiteratureDetail, Long> {
	public List<LiteratureDetail> getLiteratureDetailsBySql(String sqlString, List<Object> values);
	public List<Object> getObjectBySql(String sql);
}
