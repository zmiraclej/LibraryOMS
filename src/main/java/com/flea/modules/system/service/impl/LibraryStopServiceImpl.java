package com.flea.modules.system.service.impl;

 
import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flea.common.SolrContent;
import com.flea.common.service.impl.BaseServiceImpl;
import com.flea.modules.report.dao.LightReportDao;
import com.flea.modules.system.dao.LibraryStopDao;
import com.flea.modules.system.pojo.Library;
import com.flea.modules.system.pojo.LibraryStop;
import com.flea.modules.system.pojo.vo.LibraryIndex;
import com.flea.modules.system.service.LibraryStopService;
/**
 * 
 * @ClassName: LibraryStopServiceImpl
 * @Description:图书馆用户和平台交互的各种状态(这里用一句话描述这个类的作用)
 * @author QL
 * @date 2017年5月11日 下午7:53:48
 */
@Service
public class LibraryStopServiceImpl  extends BaseServiceImpl<LibraryStop, Integer> implements LibraryStopService{
	@Autowired
	private LibraryStopDao libraryStopDao;
	
	@Override
	public void saveOne(LibraryStop t) {
		libraryStopDao.saveOne(t);
	}
	/**
	 * 
	 * @Description:用户的停用的状态
	 * @param id
	 * @return    
	 * boolean    返回类型
	 * @throws
	 * @author QL 
	 * @date 2017年5月10日 下午3:15:18
	 */
	@Override
	public boolean stopById(Integer id) {
		return libraryStopDao.stopById(id)>0?true:false;
	}
	
	/**
	 * 
	 * @Description:用户的启用状态
	 * @param id
	 * @return    
	 * boolean    返回类型
	 * @throws
	 * @author QL 
	 * @date 2017年5月10日 下午3:15:36
	 */
	@Override
	public boolean startById(Integer id) {
		return libraryStopDao.startById(id)>0?true:false;
	}
	/**
     * 
     * @Description:平台屏蔽用户信息
     * @param id
     * @return    
     * boolean    返回类型
     * @throws
     * @author QL 
     * @date 2017年5月11日 下午7:49:38
     */
	@Override
	public boolean platFormDisplayCustomer(Integer id) {
		removeSolrIndex(id);
		return libraryStopDao.platFormDisplayCustomer(id);
	}
	
	/**
     * 
     * @Description:平台显示用户信息
     * @param id
     * @return    
     * boolean    返回类型
     * @throws
     * @author QL 
     * @date 2017年5月11日 下午7:49:38
     */
	@Override
	public boolean platFormLookCustomer(Integer id) {
		return libraryStopDao.platFormLookCustomer(id);
	}
	
	
	/**
	 * 
	 * @Description:图书馆新增权限
	 * @param userId
	 * @return    
	 * boolean    返回类型
	 * @throws
	 * @author QL 
	 * @date 2017年5月12日 上午10:57:47
	 */
	@Override
	public boolean isExtistLibrarysAddRole(Integer userId) {
		return libraryStopDao.isExtistLibrarysAddRole(userId);
	}
	/**
	 * 
	 * @Description:图书馆审核用户停用待审的信息
	 * @param id
	 * @return    
	 * LibraryStop    返回类型
	 * @throws
	 * @author QL 
	 * @date 2017年5月16日 上午11:56:39
	 */
	@Override
	public LibraryStop getLibraryStopInfomation(Integer libraryStopId) {
		return libraryStopDao.getLibraryStopInfomation(libraryStopId);
	}
	
	@Resource(name = "deptSolrContent")
	protected SolrContent deptSolrContent;
	/**
	 * 
	 * @Description:图书馆屏蔽用户信息，app看不到用户的信息
	 * @param id
	 * @return    
	 * @author QL 
	 * @date 2017年6月7日 上午10:56:39
	 */
	public void removeSolrIndex(Integer id) {
		SolrClient client = deptSolrContent.createClient();
		try {
			logger.info("removeSolrIndex>" + id);
			client.deleteById(String.valueOf(id));
			client.commit();
		} catch (SolrServerException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		} finally {
			deptSolrContent.colse(client);
		}
	}
	
	
	 
}
