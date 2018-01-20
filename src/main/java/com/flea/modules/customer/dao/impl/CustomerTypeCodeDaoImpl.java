package com.flea.modules.customer.dao.impl;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import com.flea.common.dao.impl.BaseDaoImpl;
import com.flea.modules.customer.dao.CustomerTypeCodeDao;
import com.flea.modules.customer.pojo.CustomerTypeCode;

@Repository
public class CustomerTypeCodeDaoImpl extends BaseDaoImpl<CustomerTypeCode, Integer> implements CustomerTypeCodeDao {

	@Override
	public List<CustomerTypeCode> listProvince(Integer customerType) {
		// TODO Auto-generated method stub 
		//level=2 表示省级
		String sql = "SELECT a.location_code as locationCode, a.code_name as codeName FROM r_customer_type_code a LEFT JOIN system_customer_level b ON a.customer_level_id = b.id WHERE b.level = 2 AND a.customer_level_id = ? ";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setInteger(0, customerType);
		query.setResultTransformer(new AliasToBeanResultTransformer(CustomerTypeCode.class));
		List<CustomerTypeCode> list = query.list();
		return list;
	}

	@Override
	public List<CustomerTypeCode> listCity(Integer customerType) {
		// TODO Auto-generated method stub
		//level=3 表示市级
		String sql = "SELECT a.location_code as locationCode, a.code_name as codeName FROM r_customer_type_code a LEFT JOIN system_customer_level b ON a.customer_level_id = b.id WHERE b.level = 3 AND a.customer_level_id = ? ";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setInteger(0, customerType);
		query.setResultTransformer(new AliasToBeanResultTransformer(CustomerTypeCode.class));
		List<CustomerTypeCode> list = query.list();
		return list;
	}

	@Override
	public CustomerTypeCode findOne(Integer customerId) {
		String sql = "SELECT id, customerId, customer_level_id as customerLevel, location_code as locationCode, code_name as codeName FROM r_customer_type_code WHERE customerId = ? ";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setInteger(0, customerId);
		query.setResultTransformer(new AliasToBeanResultTransformer(CustomerTypeCode.class));
		CustomerTypeCode ctc = (CustomerTypeCode)query.uniqueResult();
		return ctc;
	}

	@Override
	public CustomerTypeCode findCustomerTypeCode(String provinceCode, Integer customerLevelId) {
		String sql = "SELECT location_code as locationCode FROM r_customer_type_code WHERE location_code = ? and customer_level_id = ? ";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setString(0, provinceCode);
		query.setInteger(1, customerLevelId);
		query.setResultTransformer(new AliasToBeanResultTransformer(CustomerTypeCode.class));
		CustomerTypeCode ctc = (CustomerTypeCode)query.uniqueResult();
		return ctc;
	}

}
