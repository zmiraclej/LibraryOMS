package com.flea.modules.system.service;


import java.util.List;

import com.flea.common.pojo.Page;
import com.flea.common.service.BaseService;
import com.flea.modules.report.pojo.ReaderReport;
import com.flea.modules.report.pojo.vo.QueryCriteria;
import com.flea.modules.system.pojo.LibraryImportErrorData;

/**
 * 导入图书馆错误信息Service
 * @author bruce
 * @version 2016-12-26
 */

public interface LibraryImportErrorDataService extends BaseService<LibraryImportErrorData,Integer> {

   	Page<LibraryImportErrorData> find(Page<LibraryImportErrorData> page,LibraryImportErrorData libraryImportErrorData);
    
   	/**
   	 * 导出错误信息
   	 * @param fileName
   	 */
   	void exportErrorData(String fileName);
   	
   	/**
   	 * 查询错误信息
   	 * @return
   	 */
   	List<LibraryImportErrorData> listImportErrorData();
   	
   	/**
   	 * 删除临时表当前操作员的错误记录
   	 */
   	void detele();
}
