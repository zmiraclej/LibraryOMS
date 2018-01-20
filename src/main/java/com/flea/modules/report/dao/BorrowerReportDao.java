package com.flea.modules.report.dao;

import org.springframework.stereotype.Repository;

import com.flea.common.dao.BaseDao;
import com.flea.common.pojo.Page;
import com.flea.modules.report.pojo.BorrowerReport;
import com.flea.modules.report.pojo.SearchResult;
import com.flea.modules.report.pojo.VLibraryBook;
import com.flea.modules.report.pojo.vo.QueryCriteria;

/**
 * 借书统计DAO接口
 * @author bruce
 * @version 2016-07-14
 */
@Repository
public interface BorrowerReportDao extends BaseDao<BorrowerReport,Integer> {
	
   	/**
   	 * 借书统计列表
   	 * @param page
   	 * @param query
   	 * @return
   	 */
   	SearchResult<BorrowerReport>  findBorrowerBookList(Page<BorrowerReport> page,QueryCriteria query);
   	
   	/**
   	 * 借书统计数表
   	 * @param query
   	 * @return
   	 */
   	SearchResult<BorrowerReport>  findBorrowerDataList(QueryCriteria query);
}
