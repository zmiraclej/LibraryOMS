package com.flea.modules.report.service;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.flea.common.pojo.Page;
import com.flea.common.service.BaseService;
import com.flea.modules.report.pojo.CatalogueReport;
import com.flea.modules.report.pojo.vo.QueryCriteria;

/**
 * 编目统计Service
 * @author bruce
 * @version 2016-07-11
 */

public interface CatalogueReportService extends BaseService<CatalogueReport,Integer> {

	/**
	 * 
	 * @method:find
	 * @Description:find   获得当前默认列表
	 * @author: HeTao
	 * @param list2 
	 * @param list 
	 * @date:2016年7月11日 上午11:08:13
	 * @param:@param page
	 * @param:@param catalogueReport
	 * @param:@return
	 * @return:Page<CatalogueReport>
	 */
   	Page<CatalogueReport> find(Page<CatalogueReport> page, List<String> list, List<String> list2);

   	/**
   	 * 
   	 * @method:find
   	 * @Description:find	根据查询条件查询
   	 * @author: HeTao
   	 * @date:2016年7月11日 下午4:41:15
   	 * @param:@param page
   	 * @param:@param qc
   	 * @param:@return
   	 * @return:Page<CatalogueReport>
   	 */
	Page<CatalogueReport> find(Page<CatalogueReport> page, QueryCriteria qc);

	/**
	 * 
	 * @method:exprotListPDF
	 * @Description:exprotListPDF
	 * @author: HeTao
	 * @date:2016年7月25日 下午3:58:36
	 * @param:@param qc
	 * @param:@param request
	 * @param:@return
	 * @return:String
	 */
	String exprotListPDF(QueryCriteria qc, HttpServletRequest request);

	Page<CatalogueReport> find(Page<CatalogueReport> page, QueryCriteria qc, HttpSession session);
	
	
}
