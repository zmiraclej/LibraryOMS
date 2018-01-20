package com.flea.modules.news.service;


import com.flea.common.pojo.Page;
import com.flea.common.service.BaseService;
import com.flea.modules.news.pojo.News;

/**
 * 资讯Service
 * @author bruce
 * @version 2016-06-08
 */

public interface NewsService extends BaseService<News,Integer> {

   	Page<News> find(Page<News> page,News news,String status,String type);
   	
   	/**
   	 * 
   	 * @method:updateTopById
   	 * @Description:updateTopById	置顶
   	 * @author: HeTao
   	 * @date:2016年6月30日 下午4:07:57
   	 * @param:@param id
   	 * @param:@param state
   	 * @param:@return
   	 * @return:boolean
   	 */
   	boolean  updateTopById(Integer id, Integer state);
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
   	News queryNewsInfo(Integer id);
   	
   	Boolean audit(Integer id,Integer status);
   	
   	/**
   	 * create 2017-02-20 by gouxl
   	 * 资讯审核，驳回理由添加
   	 * @param id
   	 * @param status
   	 * @param rejectReason
   	 * @return
   	 */
   	Boolean audit(Integer id, Integer status, String rejectReason);
   	
   	/**
   	 * 
   	 * @method:updateNewsInfo 
   	 * @Description:updateNewsInfo 更新一条记录
   	 * @author: HeTao
   	 * @date:2016年6月12日 上午9:31:34
   	 * @param:@param id
   	 * @param:@param news
   	 * @param:@return
   	 * @return:boolean
   	 */
   	boolean updateNewsInfo(Integer id,News news);
   	
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
   	boolean deleteNews(Integer id);
   	
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
   	String getSelectMore(String content);
   	
   	/**
   	 * 
   	 * @method:setNewsArea
   	 * @Description:setNewsArea	设置资讯的位置
   	 * @author: HeTao
   	 * @date:2016年6月16日 上午10:27:22
   	 * @param:@param news
   	 * @return:void
   	 */
   	News setNewsArea(News news);
   	
   	/**
   	 * 
   	 * @method:getNewsUserInfo	获取资讯对应人的信息
   	 * @Description:getNewsUserInfo
   	 * @author: HeTao
   	 * @date:2016年6月21日 下午4:41:01
   	 * @param:@param news
   	 * @param:@return
   	 * @return:News
   	 */
   	News getNewsUserInfo(News news);
    /**
     * 
     * @Description:用户咨询审核
     * @param page
     * @param news
     * @param status
     * @return    
     * Page<News>    返回类型
     * @throws
     * @author QL 
     * @date 2017年4月12日 下午5:38:54
     */
	Page<News> findCustomer(Page<News> page, News news, String status,String type);
	
	
	
	/**
   	 * @method:isExtistNewsAddRoleAndAddPlatForm
   	 * @Description:平台和用户是否存在审核权限
   	 * @param userId
   	 * @return    
   	 * boolean    返回类型
   	 * @throws
   	 * @author QL 
   	 * @date 2017年4月19日 下午6:32:01
   	 */
   	boolean  isExtistNewsAddRoleAndAddPlatForm(Integer userId);
	
}
