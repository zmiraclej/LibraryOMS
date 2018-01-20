package com.flea.modules.data.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.flea.common.dao.impl.BaseDaoImpl;
import com.flea.modules.data.dao.FetchCollectionsDao;
import com.flea.modules.data.mapping.FetchCollections;
/**
 * @author zhangjian
 *
 */
@Repository
public class FetchCollectionsDaoImpl extends
		BaseDaoImpl<FetchCollections, Long> implements FetchCollectionsDao {

	@Override
	public List<FetchCollections> getFetchCollectionsBySql(String sqlString,
			List<Object> values) {
		Query query = this.getSession().createSQLQuery(sqlString.toString()).setResultTransformer(Transformers.aliasToBean(FetchCollections.class));
		 if (values != null)
	        {
	            for (int i = 0; i < values.size(); i++)
	            {
	            	System.out.println(values.get(i));
	                query.setParameter(i, values.get(i));
	            }
	        }
		 List<FetchCollections> list = query.list();
		 return  list;
		 
	}
	

}
