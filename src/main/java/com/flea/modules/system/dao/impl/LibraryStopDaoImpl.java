package com.flea.modules.system.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.flea.common.dao.impl.BaseDaoImpl;
import com.flea.modules.system.dao.LibraryStopDao;
import com.flea.modules.system.pojo.LibraryStop;
/**
 * 
 * @ClassName: LibraryStopDaoImpl
 * @Description:图书馆停用
 * @author QL
 * @date 2017年4月28日 下午5:43:01
 */
@Repository
public class LibraryStopDaoImpl  extends BaseDaoImpl<LibraryStop, Integer> implements LibraryStopDao{
	 /**
	  * 
	  * @Description:停用状态的修改，当为停用状态，libraryStatus变为6 停用待审
	  * @param id
	  * @return    
	  * int    返回类型
	  * @throws
	  * @author QL 
	  * @date 2017年5月10日 上午11:29:05
	  */
	@Override
	public int stopById(Integer id) {
		 StringBuffer hqlString = new StringBuffer("update librarys set isStop = 1, libraryUpdateStatus = 6 where id = " + id);
		 SQLQuery query = getSession().createSQLQuery(hqlString.toString());
		 return  query.executeUpdate();
	}
	/**
	  * 
	  * @Description:如果当平台驳回当前的用户信息，libraryStatus变为7 状态改为停用驳回
	  * @param libraryId
	  * @return    
	  * int    返回类型
	  * @throws
	  * @author QL 
	  * @date 2017年5月10日 上午11:30:08
	  */
	@Override
	public boolean updateLibraryStopReject(Integer libraryId) {
		 StringBuffer hqlString = new StringBuffer("update librarys set isStop = 1, libraryUpdateStatus = 7 where id = " + libraryId);
		 SQLQuery query = getSession().createSQLQuery(hqlString.toString());
		 return  query.executeUpdate()>0?true:false;
	}
	 /**
	  * 
	  * @Description:如果当平台通过当前的用户信息，libraryStatus变为8状态改为停用
	  * @param libraryId
	  * @return    
	  * int    返回类型
	  * @throws
	  * @author QL 
	  * @date 2017年5月10日 下午2:23:24
	  */
	@Override
	public boolean updateLibraryStop(Integer libraryId) {
		StringBuffer hqlString = new StringBuffer("update librarys set isStop = 1, libraryStatus = 8, libraryUpdateStatus = 13 where id = " + libraryId);
		SQLQuery query = getSession().createSQLQuery(hqlString.toString());
	    return query.executeUpdate()>0?true:false;
	}
	 /**
	  * 
	  * @Description:当用户信息状态为停用时，点击锁，状态为9 启用待审
	  * @param id
	  * @return    
	  * int    返回类型
	  * @throws
	  * @author QL 
	  * @date 2017年5月10日 下午3:10:33
	  */
	@Override
	public int startById(Integer id) {
		 StringBuffer hqlString = new StringBuffer("update librarys set isStop = 0, libraryUpdateStatus = 9 where id = " + id);
		 SQLQuery query = getSession().createSQLQuery(hqlString.toString());
		 return  query.executeUpdate();
	}
	
	/**
	  * 
	  * @Description:如果当平台驳回当前的用户为启用待审，状态为10启用驳回
	  * @param libraryId
	  * @return    
	  * boolean    返回类型
	  * @throws
	  * @author QL 
	  * @date 2017年5月10日 下午3:59:37
	  */
	@Override
	public boolean updateLibraryStartReject(Integer id) {
		 StringBuffer hqlString = new StringBuffer("update librarys set isStop = 0, libraryUpdateStatus = 10 where id = " + id);
		 SQLQuery query = getSession().createSQLQuery(hqlString.toString());
		 return query.executeUpdate()>0?true:false;
	}
	
	 /**
	   * 
	   * @Description:平台屏蔽用户信息
	   * @param id
	   * @return    
	   * boolean    返回类型
	   * @throws
	   * @author QL 
	   * @date 2017年5月11日 下午7:49:38
	   */
	@Override
	public boolean platFormDisplayCustomer(Integer id) {
		 StringBuffer hqlString = new StringBuffer("update librarys set libraryStatus=12 where id = " + id);
		 SQLQuery query = getSession().createSQLQuery(hqlString.toString());
		 return query.executeUpdate()>0?true:false;
	}
	
	 /**
	   * 
	   * @Description:平台显示用户信息
	   * @param id
	   * @return    
	   * boolean    返回类型
	   * @throws
	   * @author QL 
	   * @date 2017年5月11日 下午7:49:38
	   */
	@Override
	public boolean platFormLookCustomer(Integer id) {
		 StringBuffer hqlString = new StringBuffer("update librarys set libraryStatus=2 where id = " + id);
		 SQLQuery query = getSession().createSQLQuery(hqlString.toString());
		 return query.executeUpdate()>0?true:false;
	}
	
	/**
	 * 
	 * @Description:图书馆新增权限
	 * @param userId
	 * @return    
	 * boolean    返回类型
	 * @throws
	 * @author QL 
	 * @date 2017年5月12日 上午10:57:47
	 */
	@Override
	public boolean isExtistLibrarysAddRole(Integer userId) {
		StringBuffer sqlBuffer = new StringBuffer(" select  s.id from system_user u LEFT JOIN system_user_role ur on u.id = ur.userId ");
		sqlBuffer.append(" LEFT JOIN system_role r on ur.roleId = r.id ");
		sqlBuffer.append(" LEFT JOIN system_role_menu  m on r.id = m.roleId ");
		sqlBuffer.append(" LEFT JOIN system_menu s on m.menuId = s.id ");
		sqlBuffer.append(" where u.id = ? and s.id = '3' ");
		SQLQuery query = getSession().createSQLQuery(sqlBuffer.toString());
		query.setInteger(0, userId);
		return query.uniqueResult() != null;
	}
	
	
	/**
	 * 
	 * @Description:图书馆审核用户停用待审的信息
	 * @param id
	 * @return    
	 * LibraryStop    返回类型
	 * @throws
	 * @author QL 
	 * @date 2017年5月16日 上午11:56:39
	 */
	@Override
	public LibraryStop getLibraryStopInfomation(Integer libraryStopId) {
		StringBuffer sqlString =new StringBuffer("select s.id, s.stopReasion, s.noticeContent, s.startDate, s.endDate, s.dealAddress, s.conperson, s.phone, s.telephone, s.userId ");
		sqlString.append(" from librarys l, librarys_stop s  where  l.id = s.userId  and   s.userId = ?");
	 	Query query = getSession().createSQLQuery(sqlString.toString()).setResultTransformer(Transformers.aliasToBean(LibraryStop.class));
	 	query.setInteger(0, libraryStopId);
	 	return  (LibraryStop) query.uniqueResult();
	}
	
	
	/**
	  * 
	  * @Description:当图书馆平台审核用户为启用状态时，删除用户停用的信息
	  * @param id    
	  * void    返回类型
	  * @throws
	  * @author QL 
	  * @date 2017年5月17日 下午3:51:48
	  */
	@Override
	public void deleteByStopId(Integer id) {
		 StringBuffer hqlString = new StringBuffer("delete t2 from librarys as t1 left join librarys_stop as t2 on t1.id=t2.userId  where t2.userId = ? ");
		 SQLQuery query = getSession().createSQLQuery(hqlString.toString());
		 query.setInteger(0, id);
		 query.executeUpdate();
	}

}
