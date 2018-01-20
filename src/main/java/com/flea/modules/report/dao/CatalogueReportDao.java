package com.flea.modules.report.dao;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Repository;

import com.flea.common.dao.BaseDao;
import com.flea.common.pojo.Page;
import com.flea.modules.report.pojo.CatalogueReport;
import com.flea.modules.report.pojo.vo.QueryCriteria;

/**
 * 编目统计DAO接口
 * @author bruce
 * @version 2016-07-11
 */
@Repository
public interface CatalogueReportDao extends BaseDao<CatalogueReport,Integer> {
	/**
	 * 
	 * @method:find
	 * @Description:find   获得当前默认列表
	 * @author: HeTao
	 * @date:2016年7月11日 上午11:08:13
	 * @param:@param page
	 * @param:@param catalogueReport
	 * @param:@return
	 * @return:Page<CatalogueReport>
	 */
   	Page<CatalogueReport> find(Page<CatalogueReport> page, String string, String string2);

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
   	 * @method:find
   	 * @Description:find	根据查询条件查询
   	 * @author: HeTao
   	 * @date:2016年7月11日 下午4:41:15
   	 * @param:@param page
   	 * @param:@param qc
   	 * @param:@return
   	 * @return:Page<CatalogueReport>
   	 */
	Page<CatalogueReport> find(Page<CatalogueReport> page, QueryCriteria qc, HttpSession session);
}
