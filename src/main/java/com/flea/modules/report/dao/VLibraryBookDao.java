package com.flea.modules.report.dao;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Repository;

import com.flea.common.dao.BaseDao;
import com.flea.common.pojo.Page;
import com.flea.modules.report.pojo.SearchResult;
import com.flea.modules.report.pojo.VLibraryBook;
import com.flea.modules.report.pojo.vo.QueryCriteria;

/**
 * 藏书统计DAO接口
 * @author bruce
 * @version 2016-07-09
 */
@Repository
public interface VLibraryBookDao extends BaseDao<VLibraryBook,Integer> {
	/**
   	 * 
   	 * @method:showTopSelect
   	 * @Description:showTopSelect  查找登录者对应的地区、管别、馆号
   	 * @author: HeTao
   	 * @date:2016年7月9日 上午10:36:33
   	 * @param:@return
   	 * @return:Map<String,List<String>>
   	 */
   	Map<String,List<String>> showTopSelect(String area,String lib);
   	
   	
   	/**
   	 * 藏书统计列表
   	 * @param page
   	 * @param query
   	 * @return
   	 */
   	SearchResult<VLibraryBook>  findLibraryBookList(Page<VLibraryBook> page,QueryCriteria query);
   	
   	/**
   	 * 藏书统计数表
   	 * @param query
   	 * @return
   	 */
   	SearchResult<VLibraryBook>  findLibraryDataList(QueryCriteria query);

   	/**
   	 * 
   	 * @method:getAllLibLevel	获取所有的全部馆别
   	 * @Description:getAllLibLevel
   	 * @author: HeTao
   	 * @date:2016年8月12日 上午10:13:46
   	 * @param:@return
   	 * @return:String
   	 */
   	List<String> getAllLibLevel();


	SearchResult<VLibraryBook> findLibraryBookList(Page<VLibraryBook> page, QueryCriteria param, HttpSession session);
   	
   	
   	
}
