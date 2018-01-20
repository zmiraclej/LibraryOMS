package com.flea.modules.report.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flea.common.Common;
import com.flea.common.dao.AreaDao;
import com.flea.common.dao.CityDao;
import com.flea.common.dao.ProvinceDao;
import com.flea.common.pojo.Area;
import com.flea.common.pojo.City;
import com.flea.common.pojo.Page;
import com.flea.common.pojo.Province;
import com.flea.common.pojo.User;
import com.flea.common.service.impl.BaseServiceImpl;
import com.flea.common.util.ShiroUtils;
import com.flea.common.util.pdf.PdfHelper;
import com.flea.modules.report.dao.CatalogueReportDao;
import com.flea.modules.report.pojo.CatalogueReport;
import com.flea.modules.report.pojo.vo.QueryCriteria;
import com.flea.modules.report.service.CatalogueReportService;
import com.flea.modules.report.util.ExprotPDF;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * 编目统计Service
 * @author bruce
 * @version 2016-07-11
 */
 @Service
public class CatalogueReportServiceImpl extends BaseServiceImpl<CatalogueReport,Integer> implements CatalogueReportService{
	 
	private final int headColNumber = 4;
	//表格的设置
    private final int spacing = 0;
    //表格的设置
    private  final int padding = 2; 
	@Autowired
	private CatalogueReportDao catalogueReportDao;
	
	@Autowired
	private ProvinceDao provinceDao;
	@Autowired
	private CityDao cityDao;
	@Autowired
	private AreaDao areaDao;
	
	
	@Autowired
	public  CatalogueReportServiceImpl(CatalogueReportDao catalogueReportDao) {
		super(catalogueReportDao);
		this.catalogueReportDao = catalogueReportDao;
	}
	
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
	@Override
	public Page<CatalogueReport> find(Page<CatalogueReport> page, List<String> list, List<String> list2) {
		if(list.size() != 0 || list2.size()!=0)
		{
			catalogueReportDao.find(page,list.get(0),list2.get(0));
		}else if(list.size() == 0 || list2.size()==0){
			catalogueReportDao.find(page,"0","0");
		}
		return page;
	}

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
	@Override
	public Page<CatalogueReport> find(Page<CatalogueReport> page, QueryCriteria qc) {
		return catalogueReportDao.find(page,qc);
	}
	
	
	
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
	@Override
	public Page<CatalogueReport> find(Page<CatalogueReport> page, QueryCriteria qc, HttpSession session) {
		return catalogueReportDao.find(page,qc, session);
	}
	
	/**
   	 * 
   	 * @method:find
   	 * @Description:find	导出清单
   	 * @author: HeTao
   	 * @date:2016年7月11日 下午4:41:15
   	 * @param:@param page
   	 * @param:@param qc
   	 * @param:@return
   	 * @return:Page<CatalogueReport>
   	 */
	@Override
	public String exprotListPDF(QueryCriteria qc, HttpServletRequest request) {
    	Page<CatalogueReport> result = catalogueReportDao.find(null,qc);
    	if(result.getList().size() == 0 ){
    		return "";
    	}
    	/*
    	 * 这里如果是超级权限，传入的地区代码，将实体qc更改，传入PDF导出
    	 *  根据qc查询地区
    	 */
		String areaString = "";
		if (StringUtils.isNotBlank(qc.getProvince()) && !"undefined".equals(qc.getProvince())) {
			Province province = provinceDao.findProvinceByCode(qc.getProvince());
			City city = cityDao.findCityByCode(qc.getCity());
			Area area = areaDao.findAreaByCode(qc.getArea());
			areaString = province.getName();
			if (city != null) {
				areaString += "-" + city.getName();
			}
			if (area != null) {
				areaString += "-" + area.getName();
			}
		} else {
			areaString = qc.getArea();
		}
		qc.setArea(areaString);
    	//文件标题名
    	return ExprotPDF.getInstance().exportLibraryBookPDF(qc,request,result.getList(),"入藏统计清单");
	}

}
