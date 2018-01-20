package com.flea.modules.report.dao;

import java.math.BigInteger;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.flea.common.dao.BaseDao;
import com.flea.common.pojo.Page;
import com.flea.modules.report.pojo.CatalogueReport;
import com.flea.modules.report.pojo.LightReport;
import com.flea.modules.report.pojo.vo.QueryCriteria;

/**
 * 图书馆开发时间统计DAO接口
 * @author 
 * @version 2016-11-22
 */
@Repository
public interface LightReportDao extends BaseDao<LightReport, Integer> {
	/**
	 * 
	 * @method:getQueryInfo		根据条件获取对应记录
	 * @Description:getQueryInfo
	 * @param:@param qc
	 * @param:@param req
	 * @param:@param resp
	 * @param:@return
	 * @return:String
	 */
	Page<LightReport> find(Page<LightReport> page, QueryCriteria qc);
	
	/**
	 * 查询所有馆开放总天数
	 * @param qc
	 * @return
	 */
	BigInteger findDays(QueryCriteria qc);
	
	Map<String,String> findLightByCode(String code);
	
}
