package com.flea.modules.report.service;


import com.flea.common.pojo.Page;
import com.flea.common.service.BaseService;
import com.flea.modules.report.pojo.BorrowerReport;
import com.flea.modules.report.pojo.SearchResult;
import com.flea.modules.report.pojo.VLibraryBook;
import com.flea.modules.report.pojo.vo.QueryCriteria;

/**
 * 借书统计Service
 * @author bruce
 * @version 2016-07-14
 */

public interface BorrowerReportService extends BaseService<BorrowerReport,Integer> {

   	Page<BorrowerReport> find(Page<BorrowerReport> page,QueryCriteria query);
   	
   	
  	
    void exportLibraryBookPDF(QueryCriteria query,String filePath);
    
    void exportCategoryDataPDF(QueryCriteria query,String filePath);
    
    /**
   	 * 藏书统计数表
   	 * @param query
   	 * @return
   	 */
   	SearchResult<BorrowerReport>  findLibraryDataList(QueryCriteria query);
	
}
