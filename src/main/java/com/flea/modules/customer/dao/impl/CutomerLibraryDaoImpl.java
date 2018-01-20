package com.flea.modules.customer.dao.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.flea.common.Common;
import com.flea.common.dao.impl.BaseDaoImpl;
import com.flea.common.pojo.Page;
import com.flea.modules.customer.dao.CutomerLibraryDao;
import com.flea.modules.customer.pojo.Customer;
import com.flea.modules.customer.pojo.CutomerLibrary;

/**
 * 分配馆号DAO接口
 * @author Bruce
 * @version 2016-04-15
 */
@Repository
public class CutomerLibraryDaoImpl extends BaseDaoImpl<CutomerLibrary,Integer> implements CutomerLibraryDao{

	/* (non-Javadoc)
	 * @see com.flea.modules.customer.dao.CutomerLibraryDao#findListBySearch(com.flea.common.pojo.Page, java.lang.String)
	 */
	@Override
	public Page<CutomerLibrary> findListBySearch(Page<CutomerLibrary> page, String search) {
		String hql = "from CutomerLibrary c  where isEffective ="+Common.FLAG_ACTIVATION;
		if(StringUtils.isNotBlank(search)){
			hql += "and c.customer.name like '%"+search+"%'"
					+ "or c.customer.address like '%"+search+"%'";
		}
		Query query=  getSession().createQuery(hql);
		SQLQuery  query1 = getSession().createSQLQuery("");
		List<CutomerLibrary> list = query.list();
		page.setCount(list.size());
		query.setFirstResult(page.getFirstResult());
		query.setMaxResults(page.getMaxResults());
		page.setList(query.list());
		return page;
	}

	/* (non-Javadoc)
	 * @see com.flea.modules.customer.dao.CutomerLibraryDao#findByCustomerId(java.lang.Integer)
	 */
	@Override
	public List<CutomerLibrary> findByCustomerId(Integer customerId) {
		String hqlString = "from CutomerLibrary where customer.id="+customerId +" order by createTime asc";
		return getListByHQL(hqlString, null);
	}

	/* (non-Javadoc)
	 * @see com.flea.modules.customer.dao.CutomerLibraryDao#findLevelByAreaCode(java.lang.String, java.lang.Integer)
	 */
	@Override
	public JSONArray findLevelByAreaCode(Integer area, Integer userId) {
		String sql ="SELECT cl.id,cl.libraryLevel,cl.libraryNumber from system_customer_library cl LEFT JOIN "
				+ "system_customer c  on cl.customerId = c.id  JOIN system_user u  " 
				+ "on c.id=u.customerId JOIN system_area a  on cl.areaCode=a.`code`"
				+" WHERE   cl.areaCode ="+area+"  and u.id="+userId;
		 SQLQuery query = getSession().createSQLQuery(sql);
		  List<Object> list=query.list();
		  JSONArray jsonArray = new JSONArray();
		  int count =0;
		  Set<String> set = new HashSet<String>();
		  for(Object object:list){
			  JSONObject json = new JSONObject();
			  Object[] objects =(Object[]) object;
			  Integer id= (Integer)objects[0];
			  String level= (String)objects[1];
			  String number=(String)objects[2];
			  count+=Integer.parseInt(number);
			  json.put("id", id);
			  json.put("level", level);
			  jsonArray.add(json);
		  }
//		  JSONObject cJson = new JSONObject();
//		  cJson.put("count", count);
//		  jsonArray.add(cJson);
		return jsonArray;
	}

	/* (non-Javadoc)
	 * @see com.flea.modules.customer.dao.CutomerLibraryDao#updateUsedCodeById(java.lang.Integer)
	 */
	@Override
	public void updateUsedCodeById(Integer id) {
		SQLQuery query = getSession().createSQLQuery("UPDATE system_customer_library  set usedCodeNumber = usedCodeNumber+1 where id="+id);
		query.executeUpdate();
	}

	/* (non-Javadoc)
	 * @see com.flea.modules.customer.dao.CutomerLibraryDao#findListByCustomerId(java.lang.Integer)
	 */
	@Override
	public List<CutomerLibrary> findListByCustomerId(Integer customerId) {
		 String hql ="from CutomerLibrary where customer.id="+customerId;
		return getListByHQL(hql, null);
	}

	@Override
	public int deleteById(int id) {
		String hql="delete from CutomerLibrary where id="+id;
		Query query =getSession().createQuery(hql);
		return query.executeUpdate();
	}

	@Override
	public String getAllCustomerLibrary(Customer customer) {
		String hql ="from CutomerLibrary where customer.id="+customer.getId();
		return null;
	}
}
