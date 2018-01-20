package com.flea.modules.news.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import net.sf.ehcache.search.expression.And;

import org.hibernate.SQLQuery;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.annotations.Where;
import org.springframework.stereotype.Repository;

import com.flea.common.Common;
import com.flea.common.dao.AreaDao;
import com.flea.common.dao.CityDao;
import com.flea.common.dao.impl.BaseDaoImpl;
import com.flea.common.pojo.Area;
import com.flea.common.pojo.City;
import com.flea.common.pojo.Province;
import com.flea.common.pojo.Role;
import com.flea.common.pojo.User;
import com.flea.common.util.ShiroUtils;
import com.flea.modules.news.dao.NewsDao;
import com.flea.modules.news.pojo.News;
import com.flea.modules.system.pojo.Library;
import com.flea.modules.system.util.PublicDataUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

/**
 * 资讯DAO接口
 * 
 * @author bruce
 * @version 2016-06-08
 */
@Repository
public class NewsDaoImpl extends BaseDaoImpl<News, Integer> implements NewsDao {

	@Resource
	private AreaDao areaDao;
	@Resource
	private CityDao cityDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.flea.modules.news.dao.NewsDao#updateFieldById(java.lang.String,
	 * java.lang.Integer, java.lang.String)
	 */
	@Override
	public int updateFieldById(String filed, Integer id, Integer value) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String sqlString = "update system_news set " + filed + "=" + value
				+ ",modifyDate='" + sdf.format(new Date()) + "' where id=" + id;
		SQLQuery query = getSession().createSQLQuery(sqlString);
		return query.executeUpdate();
	}

	/**
	 * 
	 * @method:queryNewsInfo
	 * @Description:queryNewsInfo 查找一条资讯的详情
	 * @author: HeTao
	 * @date:2016年6月8日 下午5:34:16
	 * @param:@param id
	 * @param:@return
	 * @return:News
	 */
	@Override
	public News queryNewsInfo(Integer id) {
		String hql = "FROM News WHERE id = ?";
		Query query = this.getSession().createQuery(hql);
		query.setInteger(0, id);
		return (News) query.uniqueResult();
	}

	/**
	 * 
	 * @method:updateNewsInfo
	 * @Description:updateNewsInfo 更新一条资讯的记录
	 * @author: HeTao
	 * @date:2016年6月12日 上午9:31:34
	 * @param:@param id
	 * @param:@param news
	 * @param:@return
	 * @return:boolean
	 */
	@Override
	public boolean updateNewsInfo(Integer id, News news) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date1 = news.getStartDate();
		Date date2 = news.getEndDate();
		// 更新地址
		news = setNewsArea(news);
		User user = ShiroUtils.getCurrentUser();
		/**
		 * sql type=4是消息 type = 1 资讯 平台资讯 type = 2 资讯 客户资讯 type = 3 资讯 图书馆资讯
		 */
		String sql = "";
		/*
		 * if ("2".equals(news.getType())) { sql +=
		 * "update system_news set type=?,source=?,title=?,content=?,status=?,area=?,image=?,provinceCode =?,cityCode=?,areaCode=?,objKey=?,objVal=?,modifyDate=now(),modifyUser=?,top=0 where id = ?"
		 * ; }
		 */
		if ("4".equals(news.getType())) {
			sql += "update system_news set type=?,source=?,title=?,content=?,status=?,area=?,image=?,provinceCode =?,cityCode=?,areaCode=?,startDate='"
					+ sdf.format(date1)
					+ "',endDate='"
					+ sdf.format(date2)
					+ "',objKey=?,objVal=?,modifyDate=now(),modifyUser=?,top=0 where id = ?";
		} else {
			sql += "update system_news set type=?,source=?,title=?,content=?,status=?,area=?,image=?,provinceCode =?,cityCode=?,areaCode=?,objKey=?,objVal=?,modifyDate=now(),modifyUser=?,top=0,rejectReason=null where id = ?";
		}

		SQLQuery query = getSession().createSQLQuery(sql);
		// 更新——————设置对应属性中值
		query.setString(0, news.getType());
		query.setString(1, news.getSource());
		query.setString(2, news.getTitle());
		query.setString(3, news.getContent());
		// int state = news.getStatus()==5?5:1;
		query.setInteger(4, news.getStatus());
		query.setString(5, news.getArea());
		query.setString(6, news.getImage());
		query.setString(7, news.getProvinceCode());
		query.setString(8, news.getCityCode());
		query.setString(9, news.getAreaCode());
		query.setString(10, news.getObjKey());
		query.setString(11, news.getObjVal());
		query.setString(12, user.getUserName());
		query.setInteger(13, id);
		return query.executeUpdate() == 1;
	}

	/**
	 * 
	 * @method:deleteNews
	 * @Description:deleteNews 删除一条资讯记录
	 * @author: HeTao
	 * @date:2016年6月12日 上午10:23:58
	 * @param:@param id
	 * @param:@return
	 * @return:boolean
	 */
	@Override
	public boolean deleteNews(Integer id) {
		String sql = "delete from system_news where system_news.id=?";
		SQLQuery delete = getSession().createSQLQuery(sql);
		delete.setInteger(0, id);
		return delete.executeUpdate() == 1;
	}

	/**
	 * 
	 * @method:getSelectMore
	 * @Description:getSelectMore 获取对应的下拉框的更多资料
	 * @author: HeTao
	 * @date:2016年6月12日 上午11:49:58
	 * @param:@param content
	 * @param:@return
	 * @return:String
	 */
	@Override
	public String getSelectMore(String content, User user) {
		Role role = user.getRoles().get(0);
		String le = role.getLevel();
		String sql = "";
		if ("2".equals(content)
				|| ("4".equals(content) && Common.ROLE_SECOND_LEVLE.equals(le))) {
			if (Common.TZPT_OCCUPY_DEPCODE.equals(user.getHallCode())) {
				sql = "select name from system_customer where isEffective =1";
			} else {
				sql = "select name from system_customer where isEffective =1 and id = "
						+ user.getCustomer().getId();
			}
		} else if ("3".equals(content)) {
			if (Common.TZPT_OCCUPY_DEPCODE.equals(user.getHallCode())) {
				sql = "select name from librarys where isEffective =1";
			} else {
				sql = "select name from librarys where isEffective =1 and customerId = "
						+ user.getCustomer().getId();
			}
		} else if ("1".equals(content)
				|| ("4".equals(content) && Common.ROLE_FIRST_LEVLE.equals(le))) {
			return "云图书馆平台";
		}
		SQLQuery query = getSession().createSQLQuery(sql);
		List list = query.list();
		String con = new Gson().toJson(list).replaceAll("\"", "");
		con = con.substring(1, con.length() - 1);
		return con;
	}

	/**
	 * 
	 * @method:setNewsArea
	 * @Description:setNewsArea 设置资讯的位置
	 * @author: HeTao
	 * @date:2016年6月16日 上午10:27:22
	 * @param:@param news
	 * @return:void
	 */
	@Override
	public News setNewsArea(News news) {
		User user = ShiroUtils.getCurrentUser();
		news.setAuthor(user);
		String type = news.getType();
		String param = news.getSource();
		String sql = "";
		// 云图书发的资讯为默认
		if ("1".equals(news.getSource())) {
			news.setArea("All");
			return news;
		}
		// 进行条件筛选地区
		if ("2".equals(type)) {
			// [["绵阳市涪城区绵阳火车站-停车场"]]
			sql = "SELECT sc.address FROM system_customer AS sc  WHERE isEffective =1 and sc.`name` = '"
					+ param + "'";
			SQLQuery qy = getSession().createSQLQuery(sql);
			List<String> list = qy.list();
			if (list != null) {
				String area = new Gson().toJson(list);
				if (area.length() > 4) {
					area = area.substring(2, area.length() - 2);
				}
				news.setArea(area);
			}
			/*
			 * String con = new Gson().toJson(list).replaceAll("\"","");
			 * String[] cn = con.split(",");con = cn[0].replace("[[","");
			 * news.setArea(con);
			 */
			return news;
		} else if ("3".equals(type)) {
			sql = "FROM Library WHERE isEffective =1 and  name = ?";
			Query query = this.getSession().createQuery(sql);
			query.setString(0, param);
			Library lb = (Library) query.uniqueResult();
			// 查出图书馆的地址，，，，两个地址都加上
			news.setArea(lb.getAddress() + "," + lb.getAreaAddress());
		}
		return news;
	}

	/**
	 * 
	 * @method:getNewsUserInfo 获取资讯对应人的信息
	 * @Description:getNewsUserInfo
	 * @author: HeTao
	 * @date:2016年6月21日 下午4:41:01
	 * @param:@param news
	 * @param:@return
	 * @return:News
	 */
	@Override
	public News getNewsUserInfo(News news) {
		Integer id = news.getAuthor().getId();
		String sql = "FROM User WHERE id = ?";
		Query query = this.getSession().createQuery(sql);
		query.setInteger(0, id);
		User user = (User) query.uniqueResult();
		news.setAuthor(user);
		return news;
	}

	/**
	 * 
	 * @method:updateTopById
	 * @Description:updateTopById 置顶
	 * @author: HeTao
	 * @date:2016年6月30日 下午4:07:57
	 * @param:@param id
	 * @param:@param state
	 * @param:@return
	 * @return:boolean
	 */
	@Override
	public boolean updateTopById(Integer id, Integer state) {
		String sql = "update system_news set top=? where id=?";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setInteger(0, state);
		query.setInteger(1, id);
		return query.executeUpdate() == 1;
	}
	/**
	 * 
	 * @method:isExtistNewsExmineRole
	 * @Description:当平台和用户权限为审核时，查看同一客户下所有用户的信息
	 * @author: QL
	 * @date:2017年4月18日 下午13:50:57
	 * @param:@param userId
	 * @return:不能为null
	 */
	@Override
	public boolean isExtistNewsExmineRoleAndExminePlatForm(Integer userId) {
		StringBuffer sqlBuffer = new StringBuffer("  select  s.id  from system_user u LEFT JOIN system_user_role ur on u.id = ur.userId ");
		sqlBuffer.append(" LEFT JOIN system_role r on ur.roleId = r.id ");
        sqlBuffer.append(" LEFT JOIN system_role_menu  m on r.id = m.roleId ");
        sqlBuffer.append(" LEFT JOIN system_menu s on m.menuId = s.id ");
		sqlBuffer.append(" where u.id = ? and s.id = 17");
		SQLQuery query = getSession().createSQLQuery(sqlBuffer.toString());
		query.setInteger(0, userId);
		return query.uniqueResult() != null;
	}
	/**
	 * 
	 * @method:isExtistNewsAddRole
	 * @Description:当平台和用户权限为新增时，只能查看自己的信息
	 * @author: QL
	 * @date:2017年4月18日 下午14:00:57
	 * @param:@param userId
	 * @return:不能为null
	 */
	 
	@Override
	public boolean isExtistNewsAddRoleAndAddPlatForm(Integer userId) {
		StringBuffer sqlBuffer = new StringBuffer(" select  s.id from system_user u LEFT JOIN system_user_role ur on u.id = ur.userId ");
		sqlBuffer.append(" LEFT JOIN system_role r on ur.roleId = r.id ");
		sqlBuffer.append(" LEFT JOIN system_role_menu  m on r.id = m.roleId ");
		sqlBuffer.append(" LEFT JOIN system_menu s on m.menuId = s.id ");
		sqlBuffer.append(" where u.id = ? and s.id = '15' ");
		SQLQuery query = getSession().createSQLQuery(sqlBuffer.toString());
		query.setInteger(0, userId);
		return query.uniqueResult() != null;
	}
	/**
	 * 
	 * @method:isExtistNewsAddRoleListAndAddPlatFormList
	 * @Description:当平台和用户权限为新增时，只能查看自己的信息
	 * @author: QL
	 * @date:2017年4月21日 下午12:00:57
	 * @param:@param userId
	 * @return:不能为null
	 */
	
	@Override
	public boolean isExtistNewsAddRoleListAndAddPlatFormList(Integer userId) {
		StringBuffer sqlBuffer = new StringBuffer(" select  s.id from system_user u LEFT JOIN system_user_role ur on u.id = ur.userId ");
		sqlBuffer.append(" LEFT JOIN system_role r on ur.roleId = r.id ");
		sqlBuffer.append(" LEFT JOIN system_role_menu  m on r.id = m.roleId ");
		sqlBuffer.append(" LEFT JOIN system_menu s on m.menuId = s.id ");
		sqlBuffer.append(" where u.id = ? and s.id = '16' ");
		SQLQuery query = getSession().createSQLQuery(sqlBuffer.toString());
		query.setInteger(0, userId);
		return query.uniqueResult() != null;
	}
	/**
	 * 
	 * @method:isExtistNewsCustomer
	 * @Description:查看同一客户下所有用户的信息
	 * @author: QL
	 * @date:2017年4月18日 下午14:07:57
	 * @param:@param customerId
	 * @return:true 或  false
	 */
	@Override
	public boolean isExtistNewsCustomer(Integer customerId) {
		StringBuffer sqlBuffer = new StringBuffer("select  n.customerId from system_news n LEFT JOIN system_user u on n.customerId = u.customerId where u.customerId = ? and u.customerId is not NULL");
		SQLQuery query = getSession().createSQLQuery(sqlBuffer.toString());
		query.setInteger(0, customerId);
		List<News> list = query.list();
		if(list.size() > 0){
			return true;
		}else{
			return false;
		}
	}
}
