package com.flea.modules.news.service.impl;

import java.io.IOException;
import java.util.Date;

import javax.annotation.Resource;

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
import com.flea.common.dao.AreaDao;
import com.flea.common.dao.CityDao;
import com.flea.common.pojo.Area;
import com.flea.common.pojo.City;
import com.flea.common.pojo.Page;
import com.flea.common.pojo.Province;
import com.flea.common.pojo.User;
import com.flea.common.service.impl.BaseServiceImpl;
import com.flea.common.util.ShiroUtils;
import com.flea.modules.customer.pojo.Customer;
import com.flea.modules.news.dao.NewsDao;
import com.flea.modules.news.pojo.News;
import com.flea.modules.news.pojo.vo.NewsIndex;
import com.flea.modules.news.service.NewsService;

/**
 * 资讯Service
 * @author bruce
 * @version 2016-06-08
 */
 @Service
public class NewsServiceImpl extends BaseServiceImpl<News,Integer> implements NewsService{

	@Autowired
	private NewsDao newsDao;
	@Resource
	private AreaDao areaDao;
	@Resource
	private CityDao cityDao;
	@Autowired
	public  NewsServiceImpl(NewsDao newsDao) {
		super(newsDao);
		this.newsDao = newsDao;
	}
	
	@Override
	public Page<News> find(Page<News> page,News news,String status,String type) {
		DetachedCriteria dc = newsDao.createDetachedCriteria();
		User user = ShiroUtils.getCurrentUser();
		//获取用户的级别
		String role = ShiroUtils.getCurrentUserRoleLevel(user);
		if(news.getTitle() != null)
		{
			dc.add(Restrictions.like("title",news.getTitle(),MatchMode.ANYWHERE));
		}
		if("3".equals(type)) {
			dc.add(Restrictions.le("type",type));
		}else {
			dc.add(Restrictions.eq("type",type));
		}
		//0:是查看维护列表页面 ：一级用户用户的维护列表
		if("0".equals(status) || status == null){
			//user.getRemark() == 1是超级权限
				if (role.equals(Common.ROLE_FIRST_LEVLE) && 1 == Integer.parseInt(user.getRemark())) {
					//一级用户的维护列表
					dc.add(Restrictions.or(Restrictions.and(Restrictions.eq("customerId", 0), Restrictions.between("status", 2, 3)), Restrictions.eq("status",1), Restrictions.eq("status",2),Restrictions.eq("status",4), Restrictions.eq("status",5)));
					dc.addOrder(Order.desc("createDate"));
					return newsDao.find(page, dc);
					//判断当前平台为新增权限时，平台只看到自己的资讯信息
				}else if(role.equals(Common.ROLE_FIRST_LEVLE) && newsDao.isExtistNewsAddRoleAndAddPlatForm(user.getId())) {
					dc.add(Restrictions.eq("author.id",user.getId()));
					return newsDao.find(page, dc);
				   //判断当前平台为审核权限时，平台查看自己的信息和所有用户资讯信息
				}else if(role.equals(Common.ROLE_FIRST_LEVLE) && newsDao.isExtistNewsExmineRoleAndExminePlatForm(user.getId())){
					dc.add(Restrictions.or(Restrictions.and(Restrictions.eq("status", 2),Restrictions.eq("customerId",0),Restrictions.eq("status", 2))));
						/*	Restrictions.and(Restrictions.eq("customerId", 0), Restrictions.between("status", 1, 3)),
							Restrictions.and(Restrictions.eq("customerId", 0), Restrictions.between("status", 4, 5)),*/
					dc.addOrder(Order.desc("createDate"));
					return newsDao.find(page, dc);	
				}
		}else{
			    //1:是查看审核页面 一级用户的审核列表
			    //一级用户的审核列表
//				dc.add(Restrictions.or(Restrictions.and(Restrictions.eq("customerId",0),Restrictions.eq("status", 2)),Restrictions.eq("status", 2), Restrictions.eq("status",1), Restrictions.eq("status",7)));
				dc.add(Restrictions.or(Restrictions.and(Restrictions.eq("customerId",0),Restrictions.eq("status", 1)),Restrictions.eq("status",7)));
				dc.addOrder(Order.desc("createDate"));
				return newsDao.find(page, dc);	 
		}
		return newsDao.find(page, dc);	
	}
	
	/**
	 * 
	 * @method:findCustomer
	 * @Description:用户咨讯维护列表
	 * @author: QL
	 * @date:2017年4月12日 下午17:30:24
	 * @param: page
	 * @param: news
	 * @param: status
	 * @return:Page<News>
	 */
	@Override
	public Page<News> findCustomer(Page<News> page, News news, String status,String type) {
		DetachedCriteria dc = newsDao.createDetachedCriteria();
		User user = ShiroUtils.getCurrentUser();
		//获取用户的角色级别
		String role = ShiroUtils.getCurrentUserRoleLevel(user);
		if(news.getTitle() != null)
		{
			dc.add(Restrictions.like("title",news.getTitle(),MatchMode.ANYWHERE));
		}
		if("3".equals(type)) {
			dc.add(Restrictions.le("type",type));
		}else {
			dc.add(Restrictions.eq("type",type));
		}
		//0:是查看维护列表页面 二级用户的维护列表页面
		if("0".equals(status) || status == null){
			//判断当前二级用户为新增权限时，二级用户只看到自己的资讯信息
		    if(role.equals(Common.ROLE_SECOND_LEVLE) && newsDao.isExtistNewsAddRoleAndAddPlatForm(user.getId()) && newsDao.isExtistNewsAddRoleListAndAddPlatFormList(user.getId())) {
				dc.add(Restrictions.or(Restrictions.and(Restrictions.eq("author.id",user.getId()), Restrictions.eq("status", 4)),Restrictions.eq("author.id",user.getId())));
				return newsDao.find(page, dc);
				//判断当前二级用户为审核权限时，客户至查看自己咨询信息和自己的用户所有资讯信息
			}else{
//				dc.add(Restrictions.or(Restrictions.and(Restrictions.eq("customerId", user.getCustomer().getId()), Restrictions.between("status", 2, 3)), 
//						Restrictions.and(Restrictions.eq("customerId", user.getCustomer().getId()), Restrictions.between("status", 6, 7)),
//						Restrictions.and(Restrictions.eq("customerId", user.getCustomer().getId()), Restrictions.between("status", 8, 9)),
//						Restrictions.and(Restrictions.eq("customerId", user.getCustomer().getId()), Restrictions.eq("status",10))));
				dc.add(Restrictions.or(Restrictions.and(Restrictions.eq("customerId", user.getCustomer().getId()), Restrictions.eq("status", 2)), 
					   Restrictions.and(Restrictions.eq("customerId", user.getCustomer().getId()), Restrictions.eq("status", 7))));
				dc.addOrder(Order.desc("createDate"));
				return newsDao.find(page, dc);
			}
		}else{
			    //1代表审核页面 二级用户的审核页面
//			    dc.add(Restrictions.or(Restrictions.and(Restrictions.eq("customerId", user.getCustomer().getId()), Restrictions.eq("status", 2)), (Restrictions.and(Restrictions.eq("customerId", user.getCustomer().getId()), Restrictions.eq("status", 6))), Restrictions.and(Restrictions.eq("customerId", user.getCustomer().getId()), Restrictions.eq("status", 7))));
			    dc.add(Restrictions.or(Restrictions.and(Restrictions.eq("customerId", user.getCustomer().getId()), Restrictions.eq("status", 6))));
				dc.addOrder(Order.desc("createDate"));
				return newsDao.find(page, dc);	 
		}
	}
	@Override
	public void saveOne(News news) {
		System.out.println(news.getStartDate());
		User user = ShiroUtils.getCurrentUser();
		Customer customer = user.getCustomer();
		if(customer!=null) {
			news.setCustomerId(customer.getId());
		} else {
			news.setCustomerId(0);
		}
		news.setAuthor(user);
		news.setCreateDate(new Date());
		if(!news.getType().equals("4")){
			news.setModifyDate(new Date());
			news.setModifyUser(user.getUserName());
			newsDao.saveOne(news);
			return;
		}
		if(news.getObjKey().equals("1")&&!news.getObjVal().equals("0")){
			Area area = areaDao.findAreaByCode(news.getObjVal());
			City city =area.getCity();
			city = cityDao.findCityByCode(city.getCode());
			Province province = city.getProvince();
			news.setAreaCode(news.getObjVal());
			news.setCityCode(city.getCode());
			news.setProvinceCode(province.getCode());
		}
		news.setModifyDate(new Date());
		news.setModifyUser(user.getUserName());
		newsDao.saveOne(news);
	}
	
	@Resource(name="newSolrContent")
	protected SolrContent newSolrContent;
	
	/**
	 * 添加solr 索引
	 */
	public void saveSolrIndex(News news) {
		SolrClient client = newSolrContent.createClient();
		NewsIndex index = new NewsIndex();
		index.setId(news.getId());
		index.setTitle(news.getTitle());
		index.setType(news.getType());
		index.setSource(news.getSource());
		index.setStatus(news.getStatus());
		index.setImage(news.getImage());
		index.setContent(news.getContent());
		index.setCreateDate(news.getCreateDate());
		index.setModifyDate(news.getModifyDate());
		index.setTop(news.getTop());
		try {
			client.addBean(index);
			client.commit();
		} catch (IOException e) {
			logger.error(e);
		} catch (SolrServerException e) {
			logger.error(e);
		}finally{
			newSolrContent.colse(client);
		}
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
		return newsDao.queryNewsInfo(id);
	}

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
	@Override
	public boolean updateTopById(Integer id,Integer state) {
		SolrClient client = newSolrContent.createClient();
		News news = newsDao.getOne(id);
		news.setTop(state);
		saveSolr(news);
		return newsDao.updateTopById(id, state);
	}

	/* (non-Javadoc)
	 * @see com.flea.modules.news.service.NewsService#audit(java.lang.Integer)
	 */
	@Override
	public Boolean audit(Integer id,Integer status) {
		//通过id查询用户的咨询信息
		News news = newsDao.getOne(id);
		//设置用户的状态
		news.setStatus(status);
		//驳回的内容
		news.setRejectReason(null);
		User user = ShiroUtils.getCurrentUser();
		//用户的角色级别
		String role = ShiroUtils.getCurrentUserRoleLevel(user);
		news.setModifyDate(new Date());
		news.setModifyUser(user.getUserName());
		if (Common.ROLE_FIRST_LEVLE.equals(role)) {
			// 消息不存入文档数据库，
			if (!"4".equals(news.getType())) {
				if(status == 2){
				// 审核通过，向文档数据库发数据
					saveSolrIndex(news);
				}
			}
			// 删除文档数据库
			if (status == 4) {
				deletSolrIndex(id);
			}
		} else {
			if(status == 2){
			// 消息不存入文档数据库，
				news.setStatus(7);
			}
			if(status == 3){
				news.setStatus(8);
			}
			// 删除文档数据库
			if (status == 4) {
				news.setStatus(9);
				deletSolrIndex(id);
			}
		}
		newsDao.updateOne(news);
		return true;
	}
	
	@Override
	public Boolean audit(Integer id, Integer status, String rejectReason) {
		//通过id查询用户的咨询信息
		News news = newsDao.getOne(id);
		news.setStatus(status);
		//驳回内容
		news.setRejectReason(rejectReason == "" ? null:rejectReason);
		User user = ShiroUtils.getCurrentUser();
		//用户的角色级别
		String role = ShiroUtils.getCurrentUserRoleLevel(user);
		news.setModifyDate(new Date());
		news.setModifyUser(user.getUserName());
		System.out.println("rejectReason");
		if (Common.ROLE_FIRST_LEVLE.equals(role)) {
			if (!"4".equals(news.getType())) {
				if(status == 2){
				// 审核通过，向文档数据库发数据
					saveSolrIndex(news);
				}
			}
		} else {
			//2:是平台审核通过用户一条资讯信息
			if(status == 2){
				// 消息不存入文档数据库，
				//7:用户状态为一级待审
				news.setStatus(7);
			}
			//3:平台驳回用户一条资讯信息
			if(status == 3){
				//8:用户被驳回状态
				news.setStatus(8);
			}
		}
		// 删除文档数据库
		if (status == 4) {
			deletSolrIndex(id);
		}
		newsDao.updateOne(news);
		return true;
	}
	
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
	@Override
	public boolean updateNewsInfo(Integer id, News news) {
		//saveSolr(news);
		return newsDao.updateNewsInfo(id, news);
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
		deletSolrIndex(id);
		return newsDao.deleteNews(id);
	}

	/**
	 * 删除solr 索引
	 */
	private void deletSolrIndex(Integer id) {
		SolrClient client = newSolrContent.createClient();
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
	public void saveSolr(News news) {
		SolrClient client = newSolrContent.createClient();
		NewsIndex index = new NewsIndex();
		index.setId(news.getId());
		index.setTitle(news.getTitle());
		index.setType(news.getType());
		index.setSource(news.getSource());
		index.setStatus(news.getStatus());
		index.setImage(news.getImage());
		index.setContent(news.getContent());
		index.setCreateDate(news.getCreateDate());
		index.setModifyDate(news.getModifyDate());
		index.setTop(news.getTop());
		try {
			client.addBean(index);
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
   	 * @method:getSelectMore
   	 * @Description:getSelectMore 获取对应的下拉框的更多资料
   	 * @author: HeTao
   	 * @date:2016年6月12日 上午11:49:58
   	 * @param:@param content
   	 * @param:@return
   	 * @return:String
   	 */
	@Override
	public String getSelectMore(String content) {
		User user = ShiroUtils.getCurrentUser();
		return newsDao.getSelectMore(content,user);
	}

	/**
   	 * 
   	 * @method:setNewsArea
   	 * @Description:setNewsArea	设置资讯的位置
   	 * @author: HeTao
   	 * @date:2016年6月16日 上午10:27:22
   	 * @param:@param news
   	 * @return:void
   	 */
	@Override
	public News setNewsArea(News news) {
		return newsDao.setNewsArea(news);
	}

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
	@Override
	public News getNewsUserInfo(News news) {
		return newsDao.getNewsUserInfo(news);
	}

	@Override
	public boolean isExtistNewsAddRoleAndAddPlatForm(Integer userId) {
		return newsDao.isExtistNewsAddRoleAndAddPlatForm(userId);
	}

}
