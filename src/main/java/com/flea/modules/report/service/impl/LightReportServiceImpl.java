package com.flea.modules.report.service.impl;

import java.math.BigInteger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flea.common.dao.AreaDao;
import com.flea.common.dao.CityDao;
import com.flea.common.dao.ProvinceDao;
import com.flea.common.pojo.Area;
import com.flea.common.pojo.City;
import com.flea.common.pojo.Page;
import com.flea.common.pojo.Province;
import com.flea.common.service.impl.BaseServiceImpl;
import com.flea.modules.report.pojo.LightReport;
import com.flea.modules.report.pojo.ReportTotal;
import com.flea.modules.report.pojo.vo.QueryCriteria;
import com.flea.modules.report.service.LightReportService;
import com.flea.modules.report.util.ExprotPDF;
import com.flea.modules.report.dao.LightReportDao;

/**
 * 图书馆开放时间统计Service
 * @author 
 * @version 2016-11-22
 */
 @Service
public class LightReportServiceImpl extends BaseServiceImpl<LightReport,Integer> implements LightReportService{

	@Autowired
	private LightReportDao lightReportDao;
	@Autowired
	private ProvinceDao provinceDao;
	@Autowired
	private CityDao cityDao;
	@Autowired
	private AreaDao areaDao;
	
	
	@Autowired
	public  LightReportServiceImpl(LightReportDao lightReportDao) {
		super(lightReportDao);
		this.lightReportDao = lightReportDao;
	}

	@Override
	public Page<LightReport> find(Page<LightReport> page, QueryCriteria qc) {
		Page<LightReport> page1 = lightReportDao.find(page, qc);
		BigInteger days = lightReportDao.findDays(qc);
		ReportTotal total = new ReportTotal();
		total.setSumCount(days);
		page1.setTotal(total);
		return page1;
	}

	@Override
	public String exprotListPDF(QueryCriteria qc, HttpServletRequest request) {
		Page<LightReport> result = lightReportDao.find(null, qc);
		BigInteger days = lightReportDao.findDays(qc);
		//这里如果是超级权限，出入的地区代码，将实体qc更改，传入PDF导出
		String areaString = "";
        if(!"".equals(qc.getProvince()) && !"undefined".equals(qc.getProvince()) ){
            Province province = provinceDao.findProvinceByCode(qc.getProvince());
         	City city = cityDao.findCityByCode(qc.getCity());
         	Area area = areaDao.findAreaByCode(qc.getArea());
         	if(province != null){
         		areaString += province.getName();
         	}
         	if(city != null){
         		areaString += "-"+city.getName();
         	}
         	if(area != null){
         		areaString += "-"+area.getName();
         	}
        } else {
         	areaString = qc.getArea();
		}
		qc.setArea(areaString);
    	if(result.getList().size() == 0 ){
    		return "";
    	}
    	//文件标题名
    	return ExprotPDF.getInstance().exportLightPDF(days, qc, request, result.getList(),"开放时间统计清单");
	}


	

}
