package com.flea.common.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import com.flea.common.dao.CityDao;
import com.flea.common.pojo.City;
import com.flea.common.pojo.Province;
@Repository
public class CityDaoImpl extends BaseDaoImpl<City, Long> implements CityDao{

	/* (non-Javadoc)
	 * @see com.flea.common.dao.CityDao#findCityByCode(java.lang.String)
	 */
	@Override
	public City findCityByCode(String code) {
		List<Object> codes = new ArrayList<Object>();
		codes.add(code);
		return	getByHQL("from City where code=?", codes);
	}

	/* (non-Javadoc)
	 * @see com.flea.common.dao.CityDao#findByProvince(java.lang.String)
	 */
	@Override
	public List<City> findByProvince(String province) {
		List<Object> codes = new ArrayList<Object>();
		codes.add(province);
		return	getListByHQL("from City where province.code=?", codes);
	}

	/* (non-Javadoc)
	 * @see com.flea.common.dao.CityDao#findByCustomerId(java.lang.Integer)
	 */
	@Override
	public List<City> findByCustomerId(String provinceCode,Integer customerId) {
		String sqlString = "SELECT DISTINCT c.`code`,c.`name`  FROM librarys l  "
				+ " LEFT JOIN system_city c ON l.cityCode = c.`code` "
				+ " where l.isEffective = 1 and l.provinceCode = ? and l.customerId = ?";
		SQLQuery query = getSession().createSQLQuery(sqlString);
		query.setString(0, provinceCode);
		query.setInteger(1,customerId);
		query.setResultTransformer(new AliasToBeanResultTransformer(City.class));
		return query.list();
	}

}
