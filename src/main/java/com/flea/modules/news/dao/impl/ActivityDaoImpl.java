package com.flea.modules.news.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.dao.support.DaoSupport;
import org.springframework.stereotype.Repository;

import com.flea.common.dao.impl.BaseDaoImpl;
import com.flea.common.pojo.User;
import com.flea.common.util.ShiroUtils;
import com.flea.modules.news.dao.ActivityDao;
import com.flea.modules.news.pojo.Activity;
import com.flea.modules.news.pojo.News;
import com.flea.modules.system.pojo.Library;
import com.google.gson.Gson;
@Repository
public class ActivityDaoImpl extends BaseDaoImpl<Activity,Integer> implements ActivityDao {

	
	
	/**
	 * 
	 * @method:setactivityArea 设置活动区域位置
	 * @Description:setactivityArea
	 * @author: HeTao
	 * @date:2016年6月24日 上午11:16:28
	 * @param:@param activity
	 * @param:@return
	 * @return:Activity
	 */
	@Override
	public Activity setActivityUserID(Activity activity) {
		User user = ShiroUtils.getCurrentUser();
		activity.setAuthor(user);
		return activity;
	}

	
	/**
	 * 
	 * @method:updateView
	 * @Description:updateView	进入修改界面  查找一条活动详情
	 * @author: HeTao
	 * @date:2016年6月24日 下午2:38:59
	 * @param:@param id
	 * @param:@return
	 * @return:ModelAndView
	 */
	@Override
	public Activity queryActivityInfo(Integer id) {
		String hql = "FROM Activity WHERE id = ?";
		Query query = this.getSession().createQuery(hql);
		query.setInteger(0,id);
		return (Activity) query.uniqueResult();
	}
	
	/**
	 * 
	 * @method:updateActivityInfo
	 * @Description:updateActivityInfo 更新一条资讯
	 * @author: HeTao
	 * @date:2016年6月24日 下午2:46:46
	 * @param:@param id
	 * @param:@param activity
	 * @param:@param request
	 * @param:@param response
	 * @param:@return
	 * @return:ModelAndView
	 */
	@Override
	public boolean updateActivityInfo(Integer id, Activity activity) {
		User user = ShiroUtils.getCurrentUser();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date1 = activity.getStartDate();
		Date date2 = activity.getEndDate();
		//sql
		String sql = "update system_activity set type=?,source=?,title=?,content=?,status=?,activeAddress=?, modifyUser= ?, startDate='"+sdf.format(date1)+"',endDate='"+sdf.format(date2)+"',image=?,modifyDate=now(),top=0, rejectReason=null where id = ?";
		//如果没有更改照片。。。那就不更新照片地址
		if(activity.getImage() == null || activity.getImage().trim().length() == 0){
			sql = sql.replace("image=?,","");
		}
		SQLQuery query = getSession().createSQLQuery(sql);
		//更新——————设置对应属性中值
		query.setString(0,activity.getType().trim());
		query.setString(1,activity.getSource().trim());
		query.setString(2,activity.getTitle().trim());
		query.setString(3,activity.getContent().trim());
	/*	int state = activity.getStatus()==5?5:1;*/
		query.setInteger(4,activity.getStatus());
		query.setString(5, activity.getActiveAddress());
		query.setString(6, user.getUserName());
		//不更新照片地址
		if(activity.getImage() == null || activity.getImage().trim().length() == 0){
			query.setInteger(7,id);
			return query.executeUpdate()==1;
		}
		//更新照片地址
		query.setString(7,activity.getImage().trim());
		query.setInteger(8, id);
		return query.executeUpdate()==1;
	}


	/**
	 * 
	 * @method:deleteActivity	删除某个活动
	 * @Description:deleteActivity
	 * @author: HeTao
	 * @date:2016年6月24日 下午4:31:07
	 * @param:@param id
	 * @param:@return
	 * @return:boolean
	 */
	@Override
	public boolean deleteActivity(Integer id) {
		String sql = "delete from system_activity where id=?";
		SQLQuery delete = getSession().createSQLQuery(sql);
		delete.setInteger(0, id);
		return delete.executeUpdate() == 1;
	}

	/**
	 * 
	 * @method:top
	 * @Description:top	更新置顶
	 * @author: HeTao
	 * @date:2016年6月30日 下午4:19:56
	 * @param:@param id
	 * @param:@param redirectAttributes
	 * @param:@param state
	 * @param:@return
	 * @return:JSONObject
	 */
	@Override
	public boolean updateTopById(Integer id, Integer state) {
		String sql = "update system_activity set top=? where id=?";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setInteger(0,state);
		query.setInteger(1,id);
		return query.executeUpdate() == 1;
	}
	/**
	 * 
	 * @method:isExtistNewsExmineRoleAndExminePlatForm
	 * @Description:当平台和用户权限为审核时，查看同一客户下所有用户的信息
	 * @author: QL
	 * @date:2017年4月18日 下午16:58:57
	 * @param:@param userId
	 * @return:不能为null
	 */
	@Override
	public boolean isExtistNewsExmineRoleAndExminePlatForm(Integer userId) {
		StringBuffer sqlBuffer = new StringBuffer("  select  s.id  from system_user u LEFT JOIN system_user_role ur on u.id = ur.userId ");
		sqlBuffer.append(" LEFT JOIN system_role r on ur.roleId = r.id ");
        sqlBuffer.append(" LEFT JOIN system_role_menu  m on r.id = m.roleId ");
        sqlBuffer.append(" LEFT JOIN system_menu s on m.menuId = s.id ");
		sqlBuffer.append(" where u.id = ? and s.id = 20");
		SQLQuery query = getSession().createSQLQuery(sqlBuffer.toString());
		query.setInteger(0, userId);
		return query.uniqueResult() != null;
	}
	/**
	 * 
	 * @method:isExtistNewsAddRoleAndAddPlatForm
	 * @Description:当平台和用户权限为新增时，只能查看自己的信息
	 * @author: QL
	 * @date:2017年4月18日 下午17:00:57
	 * @param:@param userId
	 * @return:不能为null
	 */
	 
	@Override
	public boolean isExtistNewsAddRoleAndAddPlatForm(Integer userId) {
		StringBuffer sqlBuffer = new StringBuffer(" select  s.id from system_user u LEFT JOIN system_user_role ur on u.id = ur.userId ");
		sqlBuffer.append(" LEFT JOIN system_role r on ur.roleId = r.id ");
		sqlBuffer.append(" LEFT JOIN system_role_menu  m on r.id = m.roleId ");
		sqlBuffer.append(" LEFT JOIN system_menu s on m.menuId = s.id ");
		sqlBuffer.append(" where u.id = ? and s.id = '18' ");
		SQLQuery query = getSession().createSQLQuery(sqlBuffer.toString());
		query.setInteger(0, userId);
		return query.uniqueResult() != null;
	}
	/**
	 * 
	 * @method:isExtistNewsCustomer
	 * @Description:查看同一客户下所有用户的信息
	 * @author: QL
	 * @date:2017年4月18日 下午17:37:57
	 * @param:@param customerId
	 * @return:true 或  false
	 */
	@Override
	public boolean isExtistNewsCustomer(Integer customerId) {
		StringBuffer sqlBuffer = new StringBuffer("select  a.customerId from system_activity a LEFT JOIN system_user u on a.customerId = u.customerId where u.customerId = ? and u.customerId is not NULL");
		SQLQuery query = getSession().createSQLQuery(sqlBuffer.toString());
		query.setInteger(0, customerId);
		List<News> list = query.list();
		if(list.size() > 0){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 
	 * @method:isExtistNewsAddRoleListAndAddPlatFormList
	 * @Description:平台和用户维护列表
	 * @author: QL
	 * @date:2017年4月18日 下午17:37:57
	 * @param:@param customerId
	 * @return:true 或  false
	 */
	@Override
	public boolean isExtistNewsAddRoleListAndAddPlatFormList(Integer userId) {
		StringBuffer sqlBuffer = new StringBuffer(" select  s.id from system_user u LEFT JOIN system_user_role ur on u.id = ur.userId ");
		sqlBuffer.append(" LEFT JOIN system_role r on ur.roleId = r.id ");
		sqlBuffer.append(" LEFT JOIN system_role_menu  m on r.id = m.roleId ");
		sqlBuffer.append(" LEFT JOIN system_menu s on m.menuId = s.id ");
		sqlBuffer.append(" where u.id = ? and s.id = '19' ");
		SQLQuery query = getSession().createSQLQuery(sqlBuffer.toString());
		query.setInteger(0, userId);
		return query.uniqueResult() != null;
	}

}