package com.flea.common.util.exportDao;

import java.util.List;

import com.flea.common.dao.BaseDao;
import com.flea.modules.ebook.pojo.EbookError;
import com.flea.modules.system.pojo.LibraryImportErrorData;

/**
 * 
 * @ClassName: ExportExcelDao
 * @Description: excelDao
 * @author QL
 * @date 2017年1月19日 上午11:26:31
 */
public interface ExportExcelDao extends BaseDao<EbookError, Long>{
	public List<EbookError> exportExcel(String hql);
	
	int deleteById(int id);
}
