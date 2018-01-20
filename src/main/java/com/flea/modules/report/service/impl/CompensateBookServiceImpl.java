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
import com.flea.modules.report.dao.CompensateBookDao;
import com.flea.modules.report.pojo.CatalogueReport;
import com.flea.modules.report.pojo.vo.QueryCriteria;
import com.flea.modules.report.service.CompensateBookService;
import com.flea.modules.report.util.ExprotPDF;

/**
 * 赔书统计Service
 * @author bruce
 * @version 2016-07-13
 */
 @Service
public class CompensateBookServiceImpl extends BaseServiceImpl<CatalogueReport,Integer> implements CompensateBookService{

	@Autowired
	private CompensateBookDao compensateBookDao;
	
	@Autowired
	private ProvinceDao provinceDao;
	@Autowired
	private CityDao cityDao;
	@Autowired
	private AreaDao areaDao;
	
	
	@Autowired
	public  CompensateBookServiceImpl(CompensateBookDao compensateBookDao) {
		super(compensateBookDao);
		this.compensateBookDao = compensateBookDao;
	}

	
	/**
	 * 
	 * @method:find
	 * @Description:find 获取当前默认列表
	 * @author: HeTao
	 * @date:2016年7月13日 上午9:43:53
	 * @param:@param page
	 * @param:@param string
	 * @param:@param string2
	 * @param:@return
	 * @return:Page<CatalogueReport>
	 */
	@Override
	public Page<CatalogueReport> find(Page<CatalogueReport> page, List<String> list, List<String> list2) {
		User user = ShiroUtils.getCurrentUser();
		Role role = user.getRoles().get(0);
		String le = role.getLevel();
		if(Common.ROLE_FIRST_LEVLE.equals(le)){
			 return page;
		}else{
			return compensateBookDao.find(page,list.get(0),list2.get(0));
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
	public Page<CatalogueReport> find(Page<CatalogueReport> page, QueryCriteria qc) {
		return compensateBookDao.find(page,qc);
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
	public String getSum(String string, String string2) {
		return compensateBookDao.getSum(string,string2);
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
		Page<CatalogueReport> result = compensateBookDao.find(null,qc);
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
        
    	return ExprotPDF.getInstance().exportLibraryBookPDF(qc, request, result.getList(),"赔书统计清单");
	}
	
}
