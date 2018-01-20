package com.flea.modules.report.dao;

import org.springframework.stereotype.Repository;

import com.flea.common.dao.BaseDao;
import com.flea.common.pojo.Page;
import com.flea.modules.report.pojo.CatalogueReport;
import com.flea.modules.report.pojo.vo.QueryCriteria;

/**
 * 报损统计DAO接口
 * @author bruce
 * @version 2016-07-15
 */
@Repository
public interface ReportedLossDao extends BaseDao<CatalogueReport,Integer> {

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
	Page<CatalogueReport> find(Page<CatalogueReport> page, String string, String string2);

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
	
}
