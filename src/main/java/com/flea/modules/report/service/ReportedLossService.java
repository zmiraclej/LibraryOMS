package com.flea.modules.report.service;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.flea.common.pojo.Page;
import com.flea.common.service.BaseService;
import com.flea.modules.report.pojo.CatalogueReport;
import com.flea.modules.report.pojo.vo.QueryCriteria;

/**
 * 报损统计Service
 * @author bruce
 * @version 2016-07-15
 */

public interface ReportedLossService extends BaseService<CatalogueReport,Integer> {
	/**
	 * 
	 * @method:find 返回默认的数据列表
	 * @Description:find
	 * @author: HeTao
	 * @date:2016年7月15日 下午2:50:08
	 * @param:@param page
	 * @param:@param list
	 * @param:@param list2
	 * @param:@return
	 * @return:Page<CatalogueReport>
	 */
	Page<CatalogueReport> find(Page<CatalogueReport> page, List<String> list, List<String> list2);

	/**
	 * 
	 * @method:find 根据条件进行查找对应的数据
	 * @Description:find
	 * @author: HeTao
	 * @date:2016年7月15日 下午2:50:42
	 * @param:@param page
	 * @param:@param qc
	 * @param:@return
	 * @return:Page<CatalogueReport>
	 */
	Page<CatalogueReport> find(Page<CatalogueReport> page, QueryCriteria qc);

	/**
	 * 
	 * @method:getSum 得到统计结果
	 * @Description:getSum
	 * @author: HeTao
	 * @date:2016年7月15日 下午2:51:10
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
