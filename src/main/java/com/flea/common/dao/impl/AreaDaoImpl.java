package com.flea.common.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import com.flea.common.dao.AreaDao;
import com.flea.common.pojo.Area;
import com.flea.common.pojo.City;
import com.flea.common.pojo.Province;
@Repository
public class AreaDaoImpl extends BaseDaoImpl<Area, Long> implements AreaDao{

	/* (non-Javadoc)
	 * @see com.flea.common.dao.AreaDao#findAreaByCode(java.lang.String)
	 */
	@Override
	public Area findAreaByCode(String code) {
		List<Object> codes = new ArrayList<Object>();
		codes.add(code);
		return	getByHQL("from Area where code=?", codes);
	}

	/* (non-Javadoc)
	 * @see com.flea.common.dao.AreaDao#findByCity(java.lang.String)
	 */
	@Override
	public List<Area> findByCity(String city) {
		List<Object> codes = new ArrayList<Object>();
		codes.add(city);
		return	getListByHQL("from Area where city.code=?", codes);
	}

	/* (non-Javadoc)
	 * @see com.flea.common.dao.AreaDao#findByCustomerId(java.lang.Integer)
	 */
	@Override
	public List<Area> findByCustomerId(String cityCode,Integer customerId) {
		String sqlString = "SELECT DISTINCT c.`code`,c.`name`  FROM librarys l  "
				+ " LEFT JOIN system_area c ON l.areaCode = c.`code`"
				+ " where l.isEffective = 1 and l.cityCode = ?  and l.customerId = ?";
		SQLQuery query = getSession().createSQLQuery(sqlString);
		query.setString(0, cityCode);
		query.setInteger(1,customerId);
		query.setResultTransformer(new AliasToBeanResultTransformer(Area.class));
		return query.list();
	}


}
