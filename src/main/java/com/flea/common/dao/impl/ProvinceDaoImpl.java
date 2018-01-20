package com.flea.common.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import com.flea.common.dao.ProvinceDao;
import com.flea.common.pojo.Province;
@Repository
public class ProvinceDaoImpl extends BaseDaoImpl<Province, Long> implements ProvinceDao{

	/* (non-Javadoc)
	 * @see com.flea.common.dao.ProvinceDao#findProvinceByCode(java.lang.String)
	 */
	@Override
	public Province findProvinceByCode(String code) {
		List<Object> codes = new ArrayList<Object>();
		codes.add(code);
		return	getByHQL("from Province where code=?", codes);
	}

	/* (non-Javadoc)
	 * @see com.flea.common.dao.ProvinceDao#findByCustomerId(java.lang.Integer)
	 */
	@Override
	public List<Province> findByCustomerId(Integer customerId) {
		String sqlString = " SELECT DISTINCT p.`code`,p.`name`  FROM librarys l "
						+ " LEFT JOIN system_province p ON l.provinceCode=p.`code`"
						+" where l.isEffective = 1 and customerId = ? ";
		SQLQuery query = getSession().createSQLQuery(sqlString);
		query.setInteger(0,customerId);
		query.setResultTransformer(new AliasToBeanResultTransformer(Province.class));
		return query.list();
	}

}
