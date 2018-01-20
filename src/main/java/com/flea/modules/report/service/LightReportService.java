package com.flea.modules.report.service;


import javax.servlet.http.HttpServletRequest;

import com.flea.common.pojo.Page;
import com.flea.common.service.BaseService;
import com.flea.modules.report.pojo.CatalogueReport;
import com.flea.modules.report.pojo.LightReport;
import com.flea.modules.report.pojo.vo.QueryCriteria;

/**
 * 图书馆开放时间统计Service
 * @author 
 * @version 2016-11-22
 */

public interface LightReportService extends BaseService<LightReport,Integer> {


	/**
	 * 
	 * @method:getQueryInfo		根据条件获取对应记录
	 * @Description:getQueryInfo
	 * @param:@param qc
	 * @param:@param req
	 * @param:@param resp
	 * @param:@return
	 * @return:String
	 */
	Page<LightReport> find(Page<LightReport> page, QueryCriteria qc);

	/**
	 * 
	 * @method:exprotListPDF	导出清单
	 * @Description:exprotListPDF
	 * @param:@param qc
	 * @param:@param request
	 * @param:@param response
	 * @return:void
	 */
	String exprotListPDF(QueryCriteria qc, HttpServletRequest request);
	
}
