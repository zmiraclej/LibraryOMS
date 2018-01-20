package com.flea.modules.data.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.flea.common.dao.impl.BaseDaoImpl;
import com.flea.modules.data.dao.BorrowedDetailDao;
import com.flea.modules.data.mapping.BorrowedDetail;

/**
 * @author zhangjian
 *
 */
@Repository
public class BorrowedDetailDaoImpl extends BaseDaoImpl<BorrowedDetail,Long> implements
		BorrowedDetailDao {

	@Override
	public List<BorrowedDetail> getBorrowedDetailsBySql(String sqlString, List<Object> values) {
		Query query = this.getSession().createSQLQuery(sqlString.toString()).setResultTransformer(Transformers.aliasToBean(BorrowedDetail.class));
		 if (values != null)
	        {
	            for (int i = 0; i < values.size(); i++)
	            {
	            	System.out.println(values.get(i));
	                query.setParameter(i, values.get(i));
	            }
	        }
		 List<BorrowedDetail> list = query.list();
		 return  list;
		 
	}

}
