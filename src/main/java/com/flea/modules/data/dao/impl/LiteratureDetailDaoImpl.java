package com.flea.modules.data.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.flea.common.dao.impl.BaseDaoImpl;
import com.flea.modules.data.dao.LiteratureDetailDao;
import com.flea.modules.data.mapping.LiteratureDetail;
/**
 * @author zhangjian
 *
 */
@Repository
public class LiteratureDetailDaoImpl extends BaseDaoImpl<LiteratureDetail,Long> implements
		LiteratureDetailDao {

	@Override
	public List<LiteratureDetail> getLiteratureDetailsBySql(String sqlString, List<Object> values) {
		Query query = this.getSession().createSQLQuery(sqlString.toString()).setResultTransformer(Transformers.aliasToBean(LiteratureDetail.class));
		 if (values != null)
	        {
	            for (int i = 0; i < values.size(); i++)
	            {
	                query.setParameter(i, values.get(i));
	            }
	        }
		 List<LiteratureDetail> list = query.list();
		 return  list;
		 
	}
	
	public List<Object> getObjectBySql(String sql){
		Session session = getSession();
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		List<Object> list = sqlQuery.list();
		return list;
	}
	

}
