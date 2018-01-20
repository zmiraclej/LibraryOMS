package com.flea.modules.report.service;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.flea.common.pojo.Page;
import com.flea.common.service.BaseService;
import com.flea.modules.report.pojo.CatalogueReport;
import com.flea.modules.report.pojo.vo.QueryCriteria;

/**
 * 盈亏统计Service
 * @author bruce
 * @version 2016-07-13
 */

public interface LossReportService extends BaseService<CatalogueReport,Integer> {

	/**
	 * 
	 * @method:getSum
	 * @Description:getSum	获取集合总计
	 * @author: HeTao
	 * @date:2016年7月15日 上午9:24:42
	 * @param:@param string
	 * @param:@param string2
	 * @param:@return
	 * @return:String
	 */
	String getSum(String string, String string2);

	/**
	 * 
	 * @method:find  查找默认的记录
	 * @Description:find
	 * @author: HeTao
	 * @date:2016年7月15日 上午9:25:49
	 * @param:@param page
	 * @param:@param list
	 * @param:@param list2
	 * @param:@return
	 * @return:Page<CatalogueReport>
	 */
	Page<CatalogueReport> find(Page<CatalogueReport> page, List<String> list, List<String> list2);

	/**
	 * 
	 * @method:find 根据条件获取对应记录
	 * @Description:find
	 * @author: HeTao
	 * @date:2016年7月15日 上午9:27:42
	 * @param:@param page
	 * @param:@param qc
	 * @param:@return
	 * @return:Page<CatalogueReport>
	 */
	Page<CatalogueReport> find(Page<CatalogueReport> page, QueryCriteria qc);

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
