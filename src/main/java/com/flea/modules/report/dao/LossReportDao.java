package com.flea.modules.report.dao;

import org.springframework.stereotype.Repository;

import com.flea.common.dao.BaseDao;
import com.flea.common.pojo.Page;
import com.flea.modules.report.pojo.CatalogueReport;
import com.flea.modules.report.pojo.vo.QueryCriteria;

/**
 * 盈亏统计DAO接口
 * @author bruce
 * @version 2016-07-13
 */
@Repository
public interface LossReportDao extends BaseDao<CatalogueReport,Integer> {
	
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
	Page<CatalogueReport> find(Page<CatalogueReport> page, String string, String string2);
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
	
}
