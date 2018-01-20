package com.flea.modules.data.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.flea.common.dao.impl.BaseDaoImpl;
import com.flea.modules.data.dao.ActivityDetailDao;
import com.flea.modules.data.mapping.ActivityDetail;

/**
 * @author zhangjian
 *
 */
@Repository
public class ActivityDetailDaoImpl extends BaseDaoImpl<ActivityDetail, Long> implements ActivityDetailDao{

	@Override
	public List<ActivityDetail> getActivityDetailBySql(String sqlString,
			List<Object> values) {
		Query query = this.getSession().createSQLQuery(sqlString.toString()).setResultTransformer(Transformers.aliasToBean(ActivityDetail.class));
		 if (values != null)
	        {
	            for (int i = 0; i < values.size(); i++)
	            {
	            	System.out.println(values.get(i));
	                query.setParameter(i, values.get(i));
	            }
	        }
		 List<ActivityDetail> list = query.list();
		 return  list;
		 
	}
	

}
