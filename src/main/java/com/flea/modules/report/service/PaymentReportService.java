package com.flea.modules.report.service;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.flea.common.pojo.Page;
import com.flea.common.service.BaseService;
import com.flea.modules.report.pojo.CatalogueReport;
import com.flea.modules.report.pojo.vo.QueryCriteria;

/**
 * 收款统计Service
 * @author bruce
 * @version 2016-07-16
 */

public interface PaymentReportService extends BaseService<CatalogueReport,Integer> {

	/**
	 * 
	 * @method:list
	 * @Description:list	查询默认列表
	 * @author: HeTao
	 * @date:2016年7月16日 上午9:32:52
	 * @param:@param catalogueReport
	 * @param:@param request
	 * @param:@param response
	 * @param:@param model
	 * @param:@return
	 * @return:ModelAndView
	 */
	Page<CatalogueReport> find(Page<CatalogueReport> page, List<String> list, List<String> list2);

	/**
	 * 
	 * @method:getQueryInfo		根据条件获取对应记录
	 * @Description:getQueryInfo
	 * @author: HeTao
	 * @date:2016年7月16日 上午9:33:31
	 * @param:@param qc
	 * @param:@param req
	 * @param:@param resp
	 * @param:@return
	 * @return:String
	 */
	Page<CatalogueReport> find(Page<CatalogueReport> page, QueryCriteria qc);

	/**
	 * 
	 * @method:getSum	结果统计
	 * @Description:getSum
	 * @author: HeTao
	 * @date:2016年7月16日 上午9:34:19
	 * @param:@param string
	 * @param:@param string2
	 * @param:@return
	 * @return:String
	 */
	String getSum(String string, String string2);
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
