package com.flea.modules.customer.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.flea.common.Common;
import com.flea.common.dao.impl.BaseDaoImpl;
import com.flea.modules.customer.dao.CustomerContactDao;
import com.flea.modules.customer.pojo.CustomerContact;

/**
 * 联系人DAO接口
 * @author bruce
 * @version 2016-08-01
 */
@Repository
public class CustomerContactDaoImpl extends BaseDaoImpl<CustomerContact,Integer> implements CustomerContactDao{

	/* (non-Javadoc)
	 * @see com.flea.modules.customer.dao.CustomerContactDao#findByCustomerId(java.lang.Integer)
	 */
	@Override
	public List<CustomerContact> findByCustomerId(Integer customerId) {
		String hqlString ="from CustomerContact where customer.id=?";
		List<Object> values = new ArrayList<Object>();
		values.add(customerId);
		return getListByHQL(hqlString, values);
	}

	@Override
	public int deleteById(int id) {
		String hqlString  ="delete from CustomerContact where id=?";
		Query query = getSession().createQuery(hqlString);
		query.setInteger(0, id);
		return query.executeUpdate();
	}
	
	
	
}
