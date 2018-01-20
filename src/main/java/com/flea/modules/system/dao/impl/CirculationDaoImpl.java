package com.flea.modules.system.dao.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import com.flea.common.dao.impl.BaseDaoImpl;
import com.flea.common.pojo.Page;
import com.flea.modules.report.pojo.BorrowerReport;
import com.flea.modules.report.pojo.SearchResult;
import com.flea.modules.report.pojo.VLibraryBook;
import com.flea.modules.report.pojo.vo.QueryCriteria;
import com.flea.modules.system.dao.CirculationDao;
import com.flea.modules.system.pojo.Circulation;
import com.flea.modules.system.pojo.vo.LibraryCirculation;
import com.google.gson.Gson;

/**
 * 馆际流通DAO接口
 * @author bruce
 * @version 2016-08-18
 */
@Repository
public class CirculationDaoImpl extends BaseDaoImpl<Circulation,Integer> implements CirculationDao{

	/* (non-Javadoc)
	 * @see com.flea.modules.system.dao.CirculationDao#findLibraryCirculationList(com.flea.common.pojo.Page, com.flea.modules.report.pojo.vo.QueryCriteria)
	 */
	@Override
	public SearchResult<LibraryCirculation> findLibraryCirculationList(
			Page<LibraryCirculation> page,Integer customerId,String search) {
		 SearchResult<LibraryCirculation> result = new SearchResult<LibraryCirculation>();
		 String countString = "SELECT  COUNT(*) FROM( ";
		 StringBuffer sqlString =  new StringBuffer();
		 sqlString.append("SELECT 	a.id,a.`name`,a.hallCode,a.libraryLevel,a.areaAddress,a.conperson,group_concat(a.customerCode,scope) as scopeString FROM ( ");
		 sqlString.append("SELECT l.id,	l.`name`,l.hallCode,l.libraryLevel,l.areaAddress,l.conperson,c.hallCode as customerCode,(case WHEN s.status='1' THEN group_concat(s.scope ORDER BY s.scope separator '+') ELSE NULL END) as scope ");
		 sqlString.append("from  librarys l LEFT JOIN  system_circulation s  ON l.hallCode= s.hallCode ");
		 sqlString.append("LEFT JOIN  system_customer c ON s.customerId = c.id  where l.isEffective=1");
		 if(customerId!=null){
			 sqlString.append(" and l.customerId= "+customerId);
		 }
		 if(StringUtils.isNotBlank(search)){
			 sqlString.append(" and (l.`name` like '%"+search+"%' or l.hallCode like '%"+search+"%')");
		 }
		 sqlString.append(" GROUP BY l.hallCode,c.hallCode) as a  GROUP BY a.hallCode ");
		 SQLQuery  sqlQuery =  getSession().createSQLQuery(countString+sqlString.toString()+") as b ");
		 BigInteger total = (BigInteger)sqlQuery.uniqueResult() ;
		 int count =total.intValue();
//		 if(total==null){
//			 count =((BigInteger)sqlQuery.uniqueResult()).intValue();
//		 }
		 result.setCount(count);
		 page.setCount(count);
		 sqlQuery =  getSession().createSQLQuery(sqlString.toString());
		 sqlQuery.setFirstResult(page.getFirstResult());
		 sqlQuery.setMaxResults(page.getMaxResults());
		 sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(LibraryCirculation.class));
		 List<LibraryCirculation> list = sqlQuery.list();
		 result.setResult(list);
		return result;
	}

	/* (non-Javadoc)
	 * @see com.flea.modules.system.dao.CirculationDao#getMaxScope()
	 */
	@Override
	public int getMaxScope(Integer  customerId) {
		//得到当前登录者的
		/*User user = ShiroUtils.getCurrentUser();
		Integer id = user.getCustomer().getId();*/
		String sql = "SELECT max(li.scope) FROM system_circulation as li"
				+ " WHERE li.customerId = "+customerId;
		SQLQuery query = this.getSessionFactory().getCurrentSession().createSQLQuery(sql);
	//	query.setInteger(0, id);
		List list = query.list();
		String str = new Gson().toJson(list).toString();
		//返回当前用户的最大值
		if("[null]".equals(str))
		{
			return 0;
		}
		else
		{
			return (int)list.get(0);
		}
	}

	/* (non-Javadoc)
	 * @see com.flea.modules.system.dao.CirculationDao#updateLibraryCirculation(java.lang.String, int)
	 */
	@Override
	public boolean updateLibraryCirculation(String allId, int jieshu) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.flea.modules.system.dao.CirculationDao#findByCustomerIdHallCodeScope(java.lang.Integer, java.lang.String, java.lang.String)
	 */
	@Override
	public Circulation findByCustomerIdHallCodeScope(Integer customerId,
			String HallCode, Integer scope) {
		String hqlString = "from Circulation where customerId=? and hallCode=? and scope=?";
		List<Object> values = new ArrayList<Object>();
		values.add(customerId);
		values.add(HallCode);
		values.add(scope);
		return 	getByHQL(hqlString, values);
	}

	/* (non-Javadoc)
	 * @see com.flea.modules.system.dao.CirculationDao#removeByCustomerIdHallCodeScope(java.lang.Integer, java.lang.String, java.lang.Integer)
	 */
	@Override
	public boolean deleteByCustomerIdHallCodeScope(Integer customerId,
			String HallCode, Integer scope) {
		String hqlString = "delete from system_circulation where status='1'";
		if(customerId!=null){
			hqlString+=" and customerId="+customerId;
		}
		if(StringUtils.isNotBlank(HallCode)){
			hqlString+=" and hallCode='"+HallCode+"'";
		}
		if(scope!=null){
			hqlString+=" and scope="+scope;
		}
		SQLQuery query =  this.getSession().createSQLQuery(hqlString);
		return 	query.executeUpdate()>0?true:false;
	}

	/* (non-Javadoc)
	 * @see com.flea.modules.system.dao.CirculationDao#updateScopeByCustomerIdHallCode(java.lang.Integer, java.lang.String, java.lang.Integer)
	 */
	@Override
	public boolean updateScopeByCustomerIdHallCode(Integer customerId,
			String HallCode, Integer scope) {
		String hqlString = "update Circulation set scope=? where customerId=? and hallCode=? and ";
		Query query =  this.getSession().createQuery(hqlString);
		query.setInteger(0, scope);
		query.setInteger(1, customerId);
		query.setString(2, HallCode);
		return 	query.executeUpdate()>0?true:false;
	}

	/* (non-Javadoc)
	 * @see com.flea.modules.system.dao.CirculationDao#findByCustomerIdAndScope(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public List<Circulation> findByCustomerIdAndScope(Integer customerId,
			Integer scope) {
		String hqlString = " from Circulation where 1=1";
		if(customerId!=null){
			hqlString +=" and customerId="+customerId;
		}
		if(scope!=null){
			hqlString +=" and scope="+scope;
		}
		return getListByHQL(hqlString, null);
	}

	/* (non-Javadoc)
	 * @see com.flea.modules.system.dao.CirculationDao#findByAuditId(java.lang.Integer)
	 */
	@Override
	public List<Circulation> findByAuditId(Integer auditId) {
		String hqlString = " from Circulation where audit.id="+auditId;
		return getListByHQL(hqlString, null);
	}
	
}
