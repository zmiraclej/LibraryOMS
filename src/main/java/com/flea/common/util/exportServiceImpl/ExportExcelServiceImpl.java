package com.flea.common.util.exportServiceImpl;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.flea.common.service.impl.BaseServiceImpl;
import com.flea.common.util.ShiroUtils;
import com.flea.common.util.exportDao.ExportExcelDao;
import com.flea.common.util.exportExcel.ExportExcelUtils;
import com.flea.common.util.exportService.ExportExcelService;
import com.flea.modules.ebook.pojo.EbookError;
import com.flea.modules.ebook.pojo.vo.FileMeta;
import com.flea.modules.system.pojo.LibraryImportErrorData;

/**
 * 
 * @ClassName: ExportExcelServiceImpl
 * @Description: excel实现类
 * @author QL
 * @date 2017年1月19日 上午11:25:20
 */
@Service
public class ExportExcelServiceImpl extends BaseServiceImpl<EbookError,Integer>implements ExportExcelService {
	@Resource
	private ExportExcelDao exportExcelDao;

	@Override
	public void exportExcel(String hql, String[] titles,
			ServletOutputStream outputStream) {
		List<EbookError> list = exportExcelDao.exportExcel(hql);
		// 创建一个workbook 对应一个excel应用文件
		XSSFWorkbook workBook = new XSSFWorkbook();
		// 在workbook中添加一个sheet,对应Excel文件中的sheet
		XSSFSheet sheet = workBook.createSheet("导出excel");
		ExportExcelUtils exportUtil = new ExportExcelUtils(workBook, sheet);
		XSSFCellStyle headStyle = exportUtil.getHeadStyle();
		XSSFCellStyle bodyStyle = exportUtil.getBodyStyle();
		// 构建表头
		XSSFRow headRow = sheet.createRow(0);
		XSSFCell cell = null;
		for (int i = 0; i < titles.length; i++) {
			cell = headRow.createCell(i);
			cell.setCellStyle(headStyle);
			cell.setCellValue(titles[i]);
		}
		// 构建表体数据
		if (list != null && list.size() > 0) {
			for (int j = 0; j < list.size(); j++) {
				XSSFRow bodyRow = sheet.createRow(j + 1);
				EbookError ebookError = list.get(j);
				cell = bodyRow.createCell(0);
				cell.setCellStyle(bodyStyle);
				cell.setCellValue(ebookError.getId());

				cell = bodyRow.createCell(1);
				cell.setCellStyle(bodyStyle);
				if (StringUtils.isNotBlank(ebookError.getFileName())) {
					cell.setCellValue(ebookError.getFileName());
				}

				cell = bodyRow.createCell(2);
				cell.setCellStyle(bodyStyle);
				if (StringUtils.isNotBlank(ebookError.getFileType())) {
					cell.setCellValue(ebookError.getFileType());
				}

				cell = bodyRow.createCell(3);
				cell.setCellStyle(bodyStyle);
				if (StringUtils.isNotBlank(ebookError.getFileSize())) {
					cell.setCellValue(ebookError.getFileSize());
				}
				cell = bodyRow.createCell(4);
				cell.setCellStyle(bodyStyle);
				if (StringUtils.isNotBlank(ebookError.getRemark())) {
					cell.setCellValue(ebookError.getRemark());
				}
				cell = bodyRow.createCell(5);
				cell.setCellStyle(bodyStyle);
				if (StringUtils.isNotBlank(ebookError.getUploadTime())) {
					cell.setCellValue(ebookError.getUploadTime());
				}
			}
		}
		try {
			workBook.write(outputStream);
			outputStream.flush();
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	
	@Override
	public void detele(){
		//获取当前用户id去删除
		exportExcelDao.deleteById(ShiroUtils.getCurrentUser().getId());
	}

}
