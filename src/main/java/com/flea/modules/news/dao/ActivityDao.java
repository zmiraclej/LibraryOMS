package com.flea.modules.news.dao;

import com.flea.common.dao.BaseDao;
import com.flea.modules.news.pojo.Activity;
import com.flea.modules.system.pojo.vo.User;

public interface ActivityDao extends BaseDao<Activity,Integer>{

	/**
	 * 
	 * @method:setNewsArea 设置活动区域位置
	 * @Description:setNewsArea
	 * @author: HeTao
	 * @date:2016年6月24日 上午11:16:28
	 * @param:@param activity
	 * @param:@return
	 * @return:Activity
	 */
	Activity setActivityUserID(Activity activity);
	
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
	Activity queryActivityInfo(Integer id);
	
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
	boolean updateActivityInfo(Integer id, Activity activity);
	
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
	boolean deleteActivity(Integer id);

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
	boolean updateTopById(Integer id, Integer state);
	
	/**
	 * 
	 * @Description:读友会平台和用户，当平台和用户都有审核权限的时候，查看同一平台或同一客户下的所有用户信息是否存在
	 * @param userId
	 * @return    
	 * boolean    返回类型
	 * @throws
	 * @author QL 
	 * @date 2017年4月18日 下午4:59:35
	 */
	boolean isExtistNewsExmineRoleAndExminePlatForm(Integer userId);
	
	/**
	 * 
	 * @Description: 读友会平台和用户，当平台和用户都有新增权限的时候，只能查看自己的信息是否存在
	 * @param userId
	 * @return    
	 * boolean    返回类型
	 * @throws
	 * @author QL 
	 * @date 2017年4月18日 下午5:01:23
	 */
	boolean isExtistNewsAddRoleAndAddPlatForm(Integer userId);
	
	/**
	 * 
	 * @Description:同一客户下查看所有用户信息是否存在
	 * @param customerId
	 * @return    
	 * boolean    返回类型
	 * @throws
	 * @author QL 
	 * @date 2017年4月18日 下午5:03:25
	 */
	
	boolean isExtistNewsCustomer(Integer customerId);

   /**
   	 * 
   	 * @Description:用户和平台维护列表
   	 * @param userId
   	 * @return    
   	 * boolean    返回类型
   	 * @throws
   	 * @author QL 
   	 * @date 2017年4月21日 下午12:44:36
   	 */
   	boolean isExtistNewsAddRoleListAndAddPlatFormList(Integer userId);
}
