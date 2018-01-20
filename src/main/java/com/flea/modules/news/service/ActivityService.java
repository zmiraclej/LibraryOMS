package com.flea.modules.news.service;

import javax.servlet.http.HttpServletRequest;

import com.flea.common.pojo.Page;
import com.flea.common.service.BaseService;
import com.flea.modules.news.pojo.Activity;
import com.flea.modules.news.pojo.News;
import com.flea.modules.system.pojo.vo.User;

public interface ActivityService extends BaseService<Activity, Integer>{
	

	/**
	 * 
	 * @method:setNewsArea 设置活动区域位置
	 * @Description:setNewsArea
	 * @author: HeTao
	 * @param request 
	 * @date:2016年6月24日 上午11:16:28
	 * @param:@param activity
	 * @param:@return
	 * @return:Activity
	 */
	Activity setActivityArea(Activity activity, HttpServletRequest request);

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
	Page<Activity> find(Page<Activity> page, Activity activity, String status);

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
	void updateActivityInfo(Integer id, Activity activity);

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
	boolean audit(Integer id, Integer status);
	
	/**
   	 * create 2017-02-20 by gouxl
   	 * 审核，驳回理由添加
   	 * @param id
   	 * @param status
   	 * @param rejectReason
   	 * @return
   	 */
   	boolean audit(Integer id, Integer status, String rejectReason);

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
     * @Description:用户读友会维护列表
     * @param page
     * @param activity
     * @param status
     * @return    
     * Page<Activity>    返回类型
     * @throws
     * @author QL 
     * @date 2017年4月10日 下午4:17:26
     */
	Page<Activity> findCustomer(Page<Activity> page, Activity activity,String status);
	
	
	
	/**
	 * 
	 * @Description: 读友会平台和用户，当平台和用户都有新增权限的时候，只能查看自己的信息是否存在
	 * @param userId
	 * @return    
	 * boolean    返回类型
	 * @throws
	 * @author QL 
	 * @date 2017年4月19日 下午6:01:23
	 */
	boolean isExtistNewsAddRoleAndAddPlatForm(Integer userId);

}
