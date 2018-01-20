package com.flea.modules.news.dao;

import org.springframework.stereotype.Repository;

import com.flea.common.dao.BaseDao;
import com.flea.common.pojo.User;
import com.flea.modules.news.pojo.News;

/**
 * 资讯DAO接口
 * @author bruce
 * @version 2016-06-08
 */
@Repository
public interface NewsDao extends BaseDao<News,Integer> {
	
	/**
	 * 通过Id更新整形字段
	 * @param filed
	 * @param id
	 * @param value
	 * @return
	 */
   	int updateFieldById(String filed,Integer id,Integer value);
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
   	String getSelectMore(String content,User user);
   	
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
   	 * @method:isExtistNewsExmineRoleAndExminePlatForm
   	 * @Description:平台和用户是否存在审核权限
   	 * @param userId
   	 * @return    
   	 * boolean    返回类型
   	 * @throws
   	 * @author QL 
   	 * @date 2017年4月18日 下午6:32:01
   	 */
   	boolean  isExtistNewsExmineRoleAndExminePlatForm(Integer userId);
   	
   	/**
   	 * @method:isExtistNewsAddRoleAndAddPlatForm
   	 * @Description:平台和用户是否存在新增权限
   	 * @param userId
   	 * @param name
   	 * @return    
   	 * boolean    返回类型
   	 * @throws
   	 * @author QL 
   	 * @date 2017年4月18日 下午6:45:52
   	 */
   	boolean isExtistNewsAddRoleAndAddPlatForm(Integer userId);
   	
   	
   	/**
   	 * @method:isExtistNewsCustomer
   	 * @Description:查看同一客户下所有用户的信息
   	 * @param customerId
   	 * @return    
   	 * boolean    返回类型
   	 * @throws
   	 * @author QL 
   	 * @date 2017年4月18日 上午11:07:32
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
   	 * @date 2017年4月21日 下午12:34:36
   	 */
   	boolean isExtistNewsAddRoleListAndAddPlatFormList(Integer userId);
   	
}
