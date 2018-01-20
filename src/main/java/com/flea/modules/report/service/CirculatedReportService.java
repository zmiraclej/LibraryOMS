package com.flea.modules.report.service;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.flea.common.pojo.Page;
import com.flea.common.service.BaseService;
import com.flea.modules.report.pojo.CatalogueReport;
import com.flea.modules.report.pojo.Circulated;
import com.flea.modules.report.pojo.vo.QueryCriteria;

/**
 * 流通统计Service
 * @author bruce
 * @version 2016-07-16
 */

public interface CirculatedReportService extends BaseService<CatalogueReport,Integer> {

	/**
	 * 
	 * @method:find
	 * @Description:find	获取当前默认列表
	 * @author: HeTao
	 * @date:2016年7月13日 上午9:41:43
	 * @param:@param page
	 * @param:@param list
	 * @param:@param list2
	 * @param:@return
	 * @return:Page<CatalogueReport>
	 */
	Page<Circulated> find(Page<Circulated> page, List<String> list, List<String> list2);

	/**
	 * 
	 * @method:getQueryInfo
	 * @Description:getQueryInfo 根据查询条件查询
	 * @author: HeTao
	 * @date:2016年7月13日 上午11:23:12
	 * @param:@param qc
	 * @param:@param req
	 * @param:@param resp
	 * @param:@return
	 * @return:String
	 */
	Page<Circulated> find(Page<Circulated> page, QueryCriteria qc);

	/**
	 * 
	 * @method:getSum
	 * @Description:getSum	获取默认统计数值
	 * @author: HeTao
	 * @date:2016年7月13日 下午2:49:30
	 * @param:@param string
	 * @param:@param string2
	 * @param:@return
	 * @return:String
	 */
	String getSum();
	/**
	 * 
	 * @method:exprotListPDF	导出清单
	 * @Description:exprotListPDF
	 * @author: HeTao
	 * @date:2016年7月25日 下午3:57:39
	 * @param:@param qc
	 * @param:@param request
	 * @param:@param response
	 * @return:void
	 */
	String exprotListPDF(QueryCriteria qc, HttpServletRequest request);
	
}
