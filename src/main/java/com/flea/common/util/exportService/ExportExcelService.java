package com.flea.common.util.exportService;

import java.util.List;

import javax.servlet.ServletOutputStream;

import com.flea.common.service.BaseService;
import com.flea.modules.ebook.pojo.EbookError;
/**
 * 
 * @ClassName: ExportExcelService
 * @Description: 导出excelService
 * @author QL
 * @date 2017年1月19日 上午11:24:26
 */
public interface ExportExcelService extends BaseService<EbookError,Integer>{
	 public void exportExcel(String hql,String [] titles,ServletOutputStream outputStream);
	 	/**
	   	 * 删除临时表当前操作员的错误记录
	   	 */
	   	void detele();
	 
}
