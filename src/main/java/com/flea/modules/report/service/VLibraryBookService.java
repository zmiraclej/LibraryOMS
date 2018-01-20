package com.flea.modules.report.service;


import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.flea.common.pojo.Page;
import com.flea.common.service.BaseService;
import com.flea.modules.report.pojo.SearchResult;
import com.flea.modules.report.pojo.VLibraryBook;
import com.flea.modules.report.pojo.vo.QueryCriteria;

/**
 * 藏书、编目统计Service
 * @author bruce
 * @version 2016-07-09
 */

public interface VLibraryBookService extends BaseService<VLibraryBook,Integer> {
	/**
	 * 
	 * @method:find
	 * @Description:find 展示列表
	 * @author: HeTao
	 * @date:2016年7月9日 上午10:06:19
	 * @param:@param page
	 * @param:@param vLibraryBook
	 * @param:@return
	 * @return:Page<VLibraryBook>
	 */
   	Page<VLibraryBook> find(Page<VLibraryBook> page,QueryCriteria query);
   	
   	/**
   	 * 
   	 * @method:showTopSelect
   	 * @Description:showTopSelect  查找登录者对应的地区、管别、馆号
   	 * @author: HeTao
   	 * @date:2016年7月9日 上午10:36:33
   	 * @param:@return
   	 * @return:Map<String,List<String>>
   	 */
   	Map<String,List<String>> showTopSelect(String areaString,String lib);
   	
   	/**
   	 * 
   	 * @method:getSelectMore
   	 * @Description:getSelectMore 获取下拉列表更多
   	 * @author: HeTao
   	 * @date:2016年7月9日 下午2:34:55
   	 * @param:@param area
   	 * @param:@param lib
   	 * @param:@return
   	 * @return:String
   	 */
   	String getSelectMore(String area,String lib);
   	
   	
    void exportLibraryBookPDF(QueryCriteria query,String filePath);
    
    void exportCategoryDataPDF(QueryCriteria query,String filePath);
    
    
   	
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

	Page<VLibraryBook> find(Page<VLibraryBook> page, QueryCriteria qc, HttpSession session);
}
