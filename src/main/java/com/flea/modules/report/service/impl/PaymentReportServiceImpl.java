package com.flea.modules.report.service.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import com.flea.common.pojo.Role;
import com.flea.common.pojo.User;
import com.flea.common.service.impl.BaseServiceImpl;
import com.flea.common.util.ShiroUtils;
import com.flea.modules.report.dao.PaymentReportDao;
import com.flea.modules.report.pojo.CatalogueReport;
import com.flea.modules.report.pojo.vo.QueryCriteria;
import com.flea.modules.report.service.PaymentReportService;
import com.flea.modules.report.util.ExprotPDF;

/**
 * 收款统计Service
 * @author bruce
 * @version 2016-07-16
 */
 @Service
public class PaymentReportServiceImpl extends BaseServiceImpl<CatalogueReport,Integer> implements PaymentReportService{
	 
	 @Autowired
	 private PaymentReportDao paymentReportDao;
	 @Autowired
	 private ProvinceDao provinceDao;
	 @Autowired
	 private CityDao cityDao;
	 @Autowired
	 private AreaDao areaDao;
		
		
	@Autowired
	public  PaymentReportServiceImpl(PaymentReportDao paymentReportDao) {
		super(paymentReportDao);
		this.paymentReportDao = paymentReportDao;
	}
	 
	 /**
	 * @method:list
	 * @Description:list	查询默认列表
	 * @author: HeTao
	 * @date:2016年7月16日 上午9:32:52
	 * @param:@param catalogueReport
	 * @param:@param request
	 * @param:@param response
	 * @param:@param model
	 * @param:@return
	 * @return:ModelAndView
	 */
	@Override
	public Page<CatalogueReport> find(Page<CatalogueReport> page, List<String> list, List<String> list2) {
		User user = ShiroUtils.getCurrentUser();
		Role role = user.getRoles().get(0);
		String le = role.getLevel();
		if(Common.ROLE_FIRST_LEVLE.equals(le)){
			 return page;
		}else{
			return paymentReportDao.find(page,list.get(0),list2.get(0));
		}
	}
	/**
	 * 
	 * @method:getQueryInfo		根据条件获取对应记录
	 * @Description:getQueryInfo
	 * @author: HeTao
	 * @date:2016年7月16日 上午9:33:31
	 * @param:@param qc
	 * @param:@param req
	 * @param:@param resp
	 * @param:@return
	 * @return:String
	 */
	@Override
	public Page<CatalogueReport> find(Page<CatalogueReport> page, QueryCriteria qc) {
		return paymentReportDao.find(page,qc);
	}
	/**
	 * 
	 * @method:getSum	结果统计
	 * @Description:getSum
	 * @author: HeTao
	 * @date:2016年7月16日 上午9:34:19
	 * @param:@param string
	 * @param:@param string2
	 * @param:@return
	 * @return:String
	 */
	@Override
	public String getSum(String string, String string2) {
		return paymentReportDao.getSum(string,string2);
	}

	@Override
	public String exprotListPDF(QueryCriteria qc, HttpServletRequest request) {
		Page<CatalogueReport> result = paymentReportDao.find(null,qc);
    	if(result.getList().size() == 0 ){
    		return "";
    	}
    	// 根据qc查询地区
		String areaString = "";
		if (StringUtils.isNotBlank(qc.getProvince())) {
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
    	return ExprotPDF.getInstance().exportPayPDF(qc, request, result.getList(),"收款统计清单");
	}

}
