package com.flea.common.dao.impl;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;



















import com.flea.common.Common;
import com.flea.common.dao.UserDao;
import com.flea.common.pojo.Page;
import com.flea.common.pojo.User;
import com.flea.common.util.DateUtils;
import com.flea.common.util.EncryptUtils;
import com.flea.common.util.ShiroUtils;
import com.flea.modules.report.pojo.SearchResult;
@Repository
public class UserDaoImpl extends BaseDaoImpl<User, Long> implements UserDao{

	/* (non-Javadoc)
	 * @see com.flea.common.dao.UserDao#valiUserName(java.lang.String, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public List<User> findUserByName$LibraryId(String userName, Integer userid, String hallCode) {
		String hqlString ="SELECT u.id from system_user u where u.userName='"+userName+"' and  u.hallCode='"+hallCode+"' and  u.isEffective="+Common.FLAG_ACTIVATION;
		if(userid !=null){
			hqlString+=" and u.id!="+userid;
		}
		return getListBySQL(hqlString, null);
	}

	/* (non-Javadoc)
	 * @see com.flea.common.dao.UserDao#findUsesByHallCode(java.lang.String)
	 */
	@Override
	public List<User> findByHallCode(String hallCode) {
		String hqlString ="SELECT u.id from system_user u where u.hallCode='"+hallCode+"' and  u.isEffective="+Common.FLAG_ACTIVATION;
	    SQLQuery query = getSession().createSQLQuery(hqlString);
	    query.setResultTransformer(new AliasToBeanResultTransformer(User.class));
		return query.list();
	}

	/* (non-Javadoc)
	 * @see com.flea.common.dao.UserDao#findByCustomerId(java.lang.String)
	 */
	@Override
	public List<User> findByCustomerId(Integer customerId) {
		String hqlString ="from User where customer.id=? and isEffective="+Common.FLAG_ACTIVATION;
		List<Object> values = new ArrayList<Object>();
		values.add(customerId);
		return getListByHQL(hqlString, values);
	}

	/* (non-Javadoc)
	 * @see com.flea.common.dao.UserDao#updateHallCodeById(java.lang.Integer)
	 */
	@Override
	public boolean updateHallCodeById(String hallCode,Integer userId) {
		String sqlString ="UPDATE  system_user set hallCode ='"+hallCode+"' where id="+userId;
		 SQLQuery query = getSession().createSQLQuery(sqlString);
		return query.executeUpdate()>1?true:false;
	}
	
	/**
	 * 
	 * @method:changePsw	更改新密码
	 * @Description:changePsw
	 * @author: HeTao
	 * @date:2016年8月12日 下午2:55:14
	 * @param:@param oldpsw
	 * @param:@param newpsw
	 * @param:@return
	 * @return:ModelAndView
	 */
	@Override
	public boolean changePsw(String oldpsw, String newpsw) {
		//编译成对应的MD5
		oldpsw = EncryptUtils.encryptMD5(oldpsw);
		newpsw = EncryptUtils.encryptMD5(newpsw);
		//获取当前登录用户的ID
		User user = ShiroUtils.getCurrentUser();
		Integer id = user.getId();
		String sql = "SELECT su.id FROM system_user AS su WHERE su.id=? AND su.`password`=?"; 
		SQLQuery query = this.getSession().createSQLQuery(sql);
		query.setInteger(0,id);
		query.setString(1,oldpsw);
		List list = query.list();
		if(list.size() == 0){
			return false;
		}else{
			//更改密码
			String sqlString ="UPDATE system_user AS su SET su.`password`=? WHERE su.id=?";
			SQLQuery queryT = this.getSession().createSQLQuery(sqlString);
			queryT.setString(0,newpsw);
			queryT.setInteger(1,id);
			return queryT.executeUpdate()==0?false:true;
		}
	}

	/* (non-Javadoc)
	 * @see com.flea.common.dao.UserDao#changePaswd(java.lang.Integer, java.lang.String)
	 */
	@Override
	public boolean changePaswd(Integer userId, String password) {
		String sqlString ="UPDATE  system_user set password ='"+password+"' where id="+userId;
		SQLQuery query = getSession().createSQLQuery(sqlString);
		return query.executeUpdate()>0?true:false;
	}
	
	@Override
    public int deleteById(int id) {
		String modifyUser = ShiroUtils.getCurrentUser().getUserName();
		String modifyDate =DateUtils.formatDateTime(new Date());
	    String hqlString = "update system_user set isEffective = 0, modifyUser = '"+modifyUser+"',modifyDate = '"+modifyDate+"'  where id = "+id;
	    SQLQuery query = getSession().createSQLQuery(hqlString);
	    return  query.executeUpdate();
		
	}
	
	@Override
    public int startUseById(int id) {
		String modifyUser = ShiroUtils.getCurrentUser().getUserName();
		String modifyDate =DateUtils.formatDateTime(new Date());
	    String hqlString = "update system_user set isEffective = 1, modifyUser = '"+modifyUser+"',modifyDate = '"+modifyDate+"'  where id = "+id;
	    SQLQuery query = getSession().createSQLQuery(hqlString);
	    return  query.executeUpdate();
		
	}

}
