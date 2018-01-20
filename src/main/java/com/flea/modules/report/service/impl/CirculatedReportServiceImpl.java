package com.flea.modules.report.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.flea.common.Common;
import com.flea.common.dao.AreaDao;
import com.flea.common.dao.CityDao;
import com.flea.common.dao.ProvinceDao;
import com.flea.common.pojo.Area;
import com.flea.common.pojo.City;
import com.flea.common.pojo.Page;
import com.flea.common.pojo.Province;
import com.flea.common.pojo.Role;
import com.flea.common.pojo.User;
import com.flea.common.service.impl.BaseServiceImpl;
import com.flea.common.util.ShiroUtils;
import com.flea.modules.report.service.CirculatedReportService;
import com.flea.modules.report.util.ExprotPDF;
import com.flea.modules.report.dao.CirculatedReportDao;
import com.flea.modules.report.pojo.CatalogueReport;
import com.flea.modules.report.pojo.Circulated;
import com.flea.modules.report.pojo.vo.QueryCriteria;

/**
 * 流通统计Service
 * @author bruce
 * @version 2016-07-16
 */
 @Service
public class CirculatedReportServiceImpl extends BaseServiceImpl<CatalogueReport,Integer> implements CirculatedReportService{

	@Autowired
	private CirculatedReportDao circulatedReportDao;
	@Autowired
	private ProvinceDao provinceDao;
	@Autowired
	private CityDao cityDao;
	@Autowired
	private AreaDao areaDao;
	
	
	@Autowired
	public  CirculatedReportServiceImpl(CirculatedReportDao circulatedReportDao) {
		super(circulatedReportDao);
		this.circulatedReportDao = circulatedReportDao;
	}

	/**
	 * 
	 * @method:find
	 * @Description:find	获取当前默认列表
	 * @author: HeTao
	 * @date:2016年7月13日 上午9:41:43
	 * @param:@param page
	 * @param:@param list
	 * @param:@param list2
	 * @param:@return
	 * @return:Page<CatalogueReport>
	 */
	@Override
	public Page<Circulated> find(Page<Circulated> page, List<String> list, List<String> list2) {
		User user = ShiroUtils.getCurrentUser();
		Role role = user.getRoles().get(0);
		String le = role.getLevel();
		if(Common.ROLE_FIRST_LEVLE.equals(le)){
			 return page;
		}else{
			return circulatedReportDao.find(page,list.get(0),list2.get(0));
		}
	}

	/**
	 * 
	 * @method:getQueryInfo
	 * @Description:getQueryInfo 根据查询条件查询
	 * @author: HeTao
	 * @date:2016年7月13日 上午11:23:12
	 * @param:@param qc
	 * @param:@param req
	 * @param:@param resp
	 * @param:@return
	 * @return:String
	 */
	@Override
	public Page<Circulated> find(Page<Circulated> page, QueryCriteria qc) {
		return circulatedReportDao.find(page, qc);
	}

	/**
	 * 
	 * @method:getSum
	 * @Description:getSum	获取默认统计数值
	 * @author: HeTao
	 * @date:2016年7月13日 下午2:49:30
	 * @param:@param string
	 * @param:@param string2
	 * @param:@return
	 * @return:String
	 */
	@Override
	public String getSum() {
		return circulatedReportDao.getSum();
	}

	/**
	 * 
	 * @method:exprotListPDF	导出清单
	 * @Description:exprotListPDF
	 * @author: HeTao
	 * @date:2016年7月25日 下午3:57:39
	 * @param:@param qc
	 * @param:@param request
	 * @param:@param response
	 * @return:void
	 */
	@Override
	public String exprotListPDF(QueryCriteria qc, HttpServletRequest request) {
		Page<Circulated> result = circulatedReportDao.find(null,qc);
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
    	return ExprotPDF.getInstance().exportCirulatePDF(qc, request, result.getList(), "流通统计清单");
	}
	
	
}
