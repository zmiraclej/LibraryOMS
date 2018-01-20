package com.flea.modules.news.service.impl;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flea.common.Common;
import com.flea.common.SolrContent;
import com.flea.common.pojo.Page;
import com.flea.common.pojo.User;
import com.flea.common.service.impl.BaseServiceImpl;
import com.flea.common.util.ShiroUtils;
import com.flea.modules.news.dao.ActivityDao;
import com.flea.modules.news.pojo.Activity;
import com.flea.modules.news.pojo.News;
import com.flea.modules.news.service.ActivityService;
@Service
public class ActivityServiceImpl extends BaseServiceImpl<Activity, Integer> implements ActivityService {
	
	private ActivityDao dao;
	@Autowired
	public void setDao(ActivityDao dao) {
		this.dao = dao;
	}
	//进来就给超类  的dao  进行赋值
	@Autowired
	public ActivityServiceImpl(ActivityDao dao) {
		super(dao);
		this.dao = dao;
	}
	@Resource(name="activitySolrContent")
	protected SolrContent activitySolrContent;
	/**
	 * 
	 * @method:setActivityArea 设置活动区域位置
	 * @Description:setNewsArea
	 * @author: HeTao
	 * @date:2016年6月24日 上午11:16:28
	 * @param:@param activity
	 * @param:@return
	 * @return:Activity
	 */
	@Override
	public Activity setActivityArea(Activity activity,HttpServletRequest req) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String start = req.getParameter("start");
		String end = req.getParameter("end");
		try {
			Date startDate = sdf.parse(start);
			System.out.println(startDate);
			Date endDate = sdf.parse(end);
			activity.setStartDate(startDate);
			activity.setEndDate(endDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		User user = ShiroUtils.getCurrentUser();
		activity.setCreateDate(new Date());
		activity.setModifyDate(new Date());
		activity.setModifyUser(user.getUserName());
		activity = dao.setActivityUserID(activity);
		if(activity.getStatus() == null){activity.setStatus(1);}
		return activity;
	}
	/**
	 * 
	 * @method:find
	 * @Description:find 查找对应的活动列表
	 * @author: HeTao
	 * @date:2016年6月24日 上午11:47:24
	 * @param:@param page
	 * @param:@param news
	 * @param:@param status
	 * @param:@return
	 * @return:Page<News>
	 */
	@Override
	public Page<Activity> find(Page<Activity> page, Activity activity, String status) {
		User user = ShiroUtils.getCurrentUser();
		String role = ShiroUtils.getCurrentUserRoleLevel(user);
		//使用Criteria进行查询 
		DetachedCriteria dc = dao.createDetachedCriteria();
		if(activity.getTitle() != null)
		{
			//进行模糊查询    对于一条活动的标题
			dc.add(Restrictions.like("title",activity.getTitle(),MatchMode.ANYWHERE));
		}
		//如果登陆的不是系统管理员 那么看到的就只有自己发的
		String hallcode = user.getHallCode();
		//一级用户的维护列表
		if("0".equals(status) || status == null){
			if (role.equals(Common.ROLE_FIRST_LEVLE) && 1 == Integer.parseInt(user.getRemark()) ) {
				dc.add(Restrictions.or(Restrictions.and(Restrictions.eq("customerId", 0), Restrictions.between("status", 2, 3)), Restrictions.eq("status",1), Restrictions.eq("status",2), Restrictions.eq("status",3), Restrictions.eq("status",4), Restrictions.eq("status",5)));
				dc.addOrder(Order.desc("createDate"));
				return dao.find(page, dc);	
				//判断当前平台为新增权限时，只看到自己的读友会信息
			}else if(role.equals(Common.ROLE_FIRST_LEVLE) && dao.isExtistNewsAddRoleAndAddPlatForm(user.getId())) {
				dc.add(Restrictions.eq("author.id",user.getId()));
				return dao.find(page, dc);
			   //判断当前平台为审核权限时，平台查看自己的读友会信息以及所有用户读友会信息
			}else if(role.equals(Common.ROLE_FIRST_LEVLE) && dao.isExtistNewsExmineRoleAndExminePlatForm(user.getId())){
//				dc.add(Restrictions.or(Restrictions.and(Restrictions.eq("customerId", 0), Restrictions.between("status", 2, 3)), Restrictions.eq("status",1), Restrictions.eq("status",4), Restrictions.eq("status",5)));
				dc.add(Restrictions.or(Restrictions.and(Restrictions.eq("customerId",0),Restrictions.eq("status", 2)),Restrictions.eq("status", 2)));
				dc.addOrder(Order.desc("createDate"));
				return dao.find(page, dc);	
			}
		}else{
			    //1:是查看审核页面  
				//一级用户的审核列表
//				dc.add(Restrictions.or(Restrictions.and(Restrictions.eq("customerId", 0), Restrictions.eq("status", 2)), Restrictions.eq("status",1), Restrictions.eq("status", 2), Restrictions.eq("status",7)));
				dc.add(Restrictions.or(Restrictions.and(Restrictions.eq("customerId",0),Restrictions.eq("status", 1)),Restrictions.eq("status",7)));
				dc.addOrder(Order.desc("createDate"));
				return dao.find(page, dc);
		}
		 
		return dao.find(page, dc);
	}
	/**
	 * 
	 * @method:findCustomer
	 * @Description:用户读友会维护列表
	 * @author: QL
	 * @date:2017年4月10日 下午16:25:24
	 * @param: page
	 * @param: activity
	 * @param: status
	 * @return:Page<News>
	 */
	@Override
	public Page<Activity> findCustomer(Page<Activity> page, Activity activity, String status) {
		User user = ShiroUtils.getCurrentUser();
		String role = ShiroUtils.getCurrentUserRoleLevel(user);
		//使用Criteria进行查询 
		DetachedCriteria dc = dao.createDetachedCriteria();
		if(activity.getTitle() != null)
		{
			//进行模糊查询    对于一条活动的标题
			dc.add(Restrictions.like("title",activity.getTitle(),MatchMode.ANYWHERE));
		}
		//0:是查看维护列表页面 ：二级用户的维护列表
		if("0".equals(status) || status == null){
			     //判断客户是二级用户
		        //判断当前二级用户为新增权限时，只看到自己的读友会信息
		       if(role.equals(Common.ROLE_SECOND_LEVLE) && dao.isExtistNewsAddRoleAndAddPlatForm(user.getId()) && dao.isExtistNewsAddRoleListAndAddPlatFormList(user.getId())) {  
//		    	dc.add(Restrictions.eq("author.id",user.getId()));
		    	dc.add(Restrictions.or(Restrictions.and(Restrictions.eq("author.id",user.getId()), Restrictions.eq("status", 4)),Restrictions.eq("author.id",user.getId())));
				return dao.find(page, dc);
				//判断当前用户为审核权限时，二级客户查看自己读友会信息以及二级客户下所有用户读友会信息
		       }else{
//		    	dc.add(Restrictions.or(Restrictions.and(Restrictions.eq("customerId", user.getCustomer().getId()), Restrictions.between("status", 2, 3)), 
//					   Restrictions.and(Restrictions.eq("customerId", user.getCustomer().getId()), Restrictions.between("status", 6, 7)),
//					   Restrictions.and(Restrictions.eq("customerId", user.getCustomer().getId()), Restrictions.between("status", 8, 9)),
//					   Restrictions.and(Restrictions.eq("customerId", user.getCustomer().getId()), Restrictions.eq("status",10))));
		    	dc.add(Restrictions.or(Restrictions.and(Restrictions.eq("customerId", user.getCustomer().getId()), Restrictions.eq("status", 2)), 
					   Restrictions.and(Restrictions.eq("customerId", user.getCustomer().getId()), Restrictions.eq("status", 7))));
				 
		    	dc.addOrder(Order.desc("createDate"));
				return dao.find(page, dc);   
		       }
		} else {
			    // 1代表查看审核页面 二级用户的审核列表
//			   dc.add(Restrictions.or(Restrictions.and(Restrictions.eq("customerId", user.getCustomer().getId()), Restrictions.eq("status", 2)),
//					                 (Restrictions.and(Restrictions.eq("customerId", user.getCustomer().getId()), Restrictions.eq("status", 6))), 
//					                  Restrictions.and(Restrictions.eq("customerId", user.getCustomer().getId()), Restrictions.eq("status", 7))));	
			   dc.add(Restrictions.or(Restrictions.and(Restrictions.eq("customerId", user.getCustomer().getId()), Restrictions.eq("status", 6))));
			   dc.addOrder(Order.desc("createDate"));
			   return dao.find(page, dc);
		}
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
		// TODO Auto-generated method stub
		return dao.queryActivityInfo(id);
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
	public void updateActivityInfo(Integer id, Activity activity) {
		//saveSolrIndex(activity);
		boolean flag = dao.updateActivityInfo(id, activity);
	}
	
	/**
	 * 
	 * @method:audit
	 * @Description:audit	审核通过或者不通过
	 * @author: HeTao
	 * @date:2016年6月24日 下午3:35:37
	 * @param:@param id
	 * @param:@param status
	 * @param:@param request
	 * @param:@param response
	 * @param:@param model
	 * @param:@return
	 * @return:ModelAndView
	 */
	@Override
	public boolean audit(Integer id, Integer status) {
		Activity activity = dao.getOne(id);
		activity.setStatus(status);
		activity.setRejectReason(null);
		activity.setModifyDate(new Date());
		User user = ShiroUtils.getCurrentUser();
		String role = ShiroUtils.getCurrentUserRoleLevel(user);
		activity.setModifyUser(user.getUserName());
		if (Common.ROLE_FIRST_LEVLE.equals(role)) {
	 
				if(status == 2){
				// 审核通过，向文档数据库发数据
					saveSolrIndex(activity);
				}
				if(status == 3){
					activity.setStatus(3);
				}
				// 删除文档数据库
				if (status == 4) {
					deletSolrIndex(id);
				}
			 
			dao.updateOne(activity);
		} else {
				if(status == 2){
				// 消息不存入文档数据库，
				activity.setStatus(7);
				dao.updateOne(activity);
				}
				if(status == 3){
				activity.setStatus(8);
				dao.updateOne(activity);
				}
				// 删除文档数据库
				if (status == 4) {
				activity.setStatus(9);
				dao.updateOne(activity);
				}
		}
		//删除文档数据库
		if (status == 4) {
			deletSolrIndex(id);
		}
		return true;
	}
	
	/**
	 * create 2017-02-20 by gouxl
   	 * 审核，驳回理由添加
	 */
	@Override
	public boolean audit(Integer id, Integer status, String rejectReason) {
		Activity activity = dao.getOne(id);
		activity.setStatus(status);
		activity.setRejectReason(rejectReason == "" ? null:rejectReason);
		activity.setModifyDate(new Date());
		User user = ShiroUtils.getCurrentUser();
		activity.setModifyUser(user.getUserName());
		dao.updateOne(activity);
		String role = ShiroUtils.getCurrentUserRoleLevel(user);
		if (Common.ROLE_FIRST_LEVLE.equals(role)) {
		 
				if(status == 2){
				// 审核通过，向文档数据库发数据
					saveSolrIndex(activity);
				}
				if(status == 3){
					activity.setStatus(3);
				}
				// 删除文档数据库
				if (status == 4) {
					deletSolrIndex(id);
				 
			}
			dao.updateOne(activity);
		} else {
				if(status == 2){
				// 消息不存入文档数据库，
				activity.setStatus(7);
				dao.updateOne(activity);
				}
				if(status == 3){
				activity.setStatus(8);
				dao.updateOne(activity);
				}
				// 删除文档数据库
				if (status == 4) {
				deletSolrIndex(id);
				}
		}
		return true;
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
		deletSolrIndex(id);
		return dao.deleteActivity(id);
	}
	
	/**
	 * 删除solr 索引
	 */
	private void deletSolrIndex(Integer id) {
		SolrClient client = activitySolrContent.createClient();
		try {
			client.deleteByQuery("id:"+id);
			client.commit();
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}finally{
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 添加solr 索引   向solr里面添加文档数据
	 */
	public void saveSolrIndex(Activity a) {
		SolrClient client = activitySolrContent.createClient();
		try {
			client.addBean(a);
			client.commit();
		} catch (IOException | SolrServerException e) {
			e.printStackTrace();
		}finally {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
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
		Activity ac = dao.getOne(id);
		ac.setTop(state);
		saveSolrIndex(ac);
		return dao.updateTopById(id, state);
	}
	/**
	 * 
	 * @Description: 读友会平台和用户，当平台和用户都有新增权限的时候，只能查看自己的信息是否存在
	 * @param userId
	 * @return    
	 * boolean    返回类型
	 * @throws
	 * @author QL 
	 * @date 2017年4月19日 下午6:31:23
	 */
	@Override
	public boolean isExtistNewsAddRoleAndAddPlatForm(Integer userId) {
		return dao.isExtistNewsAddRoleAndAddPlatForm(userId);
	}
	
}
