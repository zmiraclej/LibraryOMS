package com.flea.modules.customer.dao.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.poi.ss.formula.functions.T;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

import com.flea.common.dao.impl.BaseDaoImpl;
import com.flea.common.pojo.User;
import com.flea.common.util.ShiroUtils;
import com.flea.modules.customer.dao.CustomerDao;
import com.flea.modules.customer.dao.CutomerLibraryDao;
import com.flea.modules.customer.pojo.Customer;
import com.flea.modules.customer.pojo.CutomerLibrary;

/**
 * 客户DAO接口
 * @author Bruce
 * @version 2016-04-15
 */
@Repository
public class CustomerDaoImpl extends BaseDaoImpl<Customer,Integer> implements CustomerDao{
	
	/* (non-Javadoc)
	 * @see com.flea.modules.customer.dao.LibraryCodeDao#findMaxLibraryCode()
	 */
	@Override
	public String findMaxLibraryCode() {
		String sql="select MAX(endCode) from system_customer_library";
		SQLQuery query = getSession().createSQLQuery(sql);
		return (String)query.uniqueResult();
	}

	/**
	 * 获取每个区域的code 字段
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String getId(String name, Class<T> t) {
		Table table = (Table)t.getAnnotation(Table.class);
		String sqlName = table.name();
		SQLQuery query = this.getSession().createSQLQuery("SELECT code FROM "+sqlName+" WHERE name='"+name+"'");
		return (String) query.list().get(0);
	}
	/**
	 * 
	 * @method:deleteLib	删除对应馆
	 * @Description:deleteLib
	 * @author: HeTao
	 * @date:2016年8月20日 下午5:07:33
	 * @param:@param id
	 * @return:void
	 */
	@Override
	public boolean deleteLib(Integer id) {
		SQLQuery del = this.getSession().createSQLQuery("delete from system_customer_library where id = "+id);
		int falg = del.executeUpdate();
		return falg==0?false:true;
	}

	/* (non-Javadoc)
	 * @see com.flea.modules.customer.dao.CustomerDao#updateHallCodeById(java.lang.String, java.lang.Integer)
	 */
	@Override
	public boolean updateHallCodeById(String hallCode, Integer id) {
		SQLQuery query = getSession().createSQLQuery("UPDATE system_customer  set hallCode = '"+hallCode+"' where id="+id);
		return 	query.executeUpdate()>1?true:false;
	}

	/* (non-Javadoc)
	 * @see com.flea.modules.customer.dao.CustomerDao#updateModifyInfoById(java.lang.Integer)
	 */
	@Override
	public boolean updateModifyInfoById(Integer id) {
		User user = ShiroUtils.getCurrentUser();
		String sqlString = "UPDATE system_customer set modifyDate =?,modifyUser=? where id=?";
		SQLQuery query = getSession().createSQLQuery(sqlString);
		query.setDate(0, new Date());
		query.setString(1, user.getUserName());
		query.setInteger(2, id);
		return query.executeUpdate()>0?true:false;
	}
	
	@Override
	public boolean isExistCustomer(String customerCode) {
		
		String sqlString = "select COUNT(1) from system_customer where hallCode = '" + customerCode + "'";
		SQLQuery  query = getSession().createSQLQuery(sqlString);
		int customerCount = ((BigInteger)query.uniqueResult()).intValue();
		return customerCount > 0 ? true : false;
	}

}
