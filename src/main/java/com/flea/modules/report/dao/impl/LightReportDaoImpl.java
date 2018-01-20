package com.flea.modules.report.dao.impl;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.flea.common.Common;
import com.flea.common.dao.impl.BaseDaoImpl;
import com.flea.common.pojo.Page;
import com.flea.common.pojo.Role;
import com.flea.common.pojo.User;
import com.flea.common.util.ShiroUtils;
import com.flea.modules.report.dao.LightReportDao;
import com.flea.modules.report.pojo.LightReport;
import com.flea.modules.report.pojo.vo.QueryCriteria;
import com.google.gson.Gson;

/**
 * 图书馆开放时间统计DAO接口
 * @author 
 * @version 2016-11-22
 */
@Repository()
public class LightReportDaoImpl extends BaseDaoImpl<LightReport,Integer> implements LightReportDao{

	@Override
	public Page<LightReport> find(Page<LightReport> page, QueryCriteria qc) {
		User user = ShiroUtils.getCurrentUser();
		Role role = user.getRoles().get(0);
		String le = role.getLevel();
		int id = 0;
		boolean flag = false;
		//拼凑SQL语句
		StringBuffer sbf = new StringBuffer();
		sbf.append(" SELECT SUM(light_time) AS totalTime,llr.hallCode,name,libraryLevel as lib,conperson as operName, phone ");
		sbf.append(" FROM lib_light_record llr LEFT JOIN librarys ls ON llr.hallCode = ls.hallCode where 1 = 1  ");
		//判断是否是全部搜索	只能管理员有权利全部搜索
		String area = qc.getArea();
		String lib = qc.getLib();
		String hallCode = qc.getHallCode();
		String beginTime = qc.getDateFrom();
		String endTime = qc.getDateTo();
		if ( !"".equals(lib) ) {
			sbf.append(" AND ls.libraryLevel = '"+lib+"' ");
		}
		//条件拼接 馆号
		if("0".equals(qc.getOption())){
			if(StringUtils.isNotBlank(qc.getSearchFrom())){
				sbf.append(" and ls.hallCode>='"+qc.getSearchFrom()+"' ");
			}
			if(StringUtils.isNotBlank(qc.getSearchTo())){
				sbf.append(" and ls.hallCode<='"+qc.getSearchTo()+"' ");
			}
		}
		//条件拼接 日期
		if("2".equals(qc.getOption())){
			if(StringUtils.isNotBlank(qc.getDateFrom())){
				sbf.append(" and llr.light_date >= '"+ qc.getDateFrom() +"' ");
			}
			if(StringUtils.isNotBlank(qc.getDateTo())){
				sbf.append(" and llr.light_date <= '"+ qc.getDateTo() +"' ");
			}
		}
		
		//添加条件	
		if (Common.ROLE_FIRST_LEVLE.equals(le)) {
			flag = true;
			if(!"".equals(qc.getProvince())){
				sbf.append(" and ls.provinceCode='"+qc.getProvince()+"' ");
			}
			if(!"".equals(qc.getCity())){
				sbf.append(" and ls.cityCode='"+qc.getCity()+"' ");
			}
			if(!"".equals(qc.getArea())){
				sbf.append(" AND ls.areaCode = '"+area+"' ");
			}
		} else {
			if (!"".equals(qc.getArea())) {
				sbf.append(" AND ls.areaAddress = '"+area+"' ");
			}
			sbf.append(" AND ls.customerId = ? ");
			id = user.getCustomer().getId();
		}
		sbf.append(" GROUP BY llr.hallCode ");
		//连接好所有的查询条件并创建sql语句
		String sql = sbf.toString();
		SQLQuery query = this.getSession().createSQLQuery(sql);
		//对应的实体类
		query.setResultTransformer( Transformers.aliasToBean(LightReport.class));
		//设置参数 -----------------------------------------------------------
		if(!flag){
			query.setInteger(0,id);
		}
		if(page==null){
			page = new Page<LightReport>();
			//进行查询
			List<LightReport> list = query.list();
			//设置集合总数到最后一个对象里面
			setSumInLastObject(list,sbf,id,flag);
			page.setList(list);
			//返回
			return page;
		}
		//设置分页
		List lis = query.list();
		page.setCount(lis.size());
		//分页信息
		query.setFirstResult(page.getFirstResult());
		query.setMaxResults(page.getPageSize());
		//进行查询
		List<LightReport> list = query.list();
		//设置集合总数到最后一个对象里面
		setSumInLastObject(list,sbf,id,flag);
		page.setList(list);
		//返回
		return page;
	}
	
	@Override
	public BigInteger findDays(QueryCriteria qc) {
		// TODO Auto-generated method stub
		User user = ShiroUtils.getCurrentUser();
		Role role = user.getRoles().get(0);
		String le = role.getLevel();
		int id = 0;
		boolean flag = false;
		//拼凑SQL语句
		StringBuffer sbf = new StringBuffer();
		sbf.append(" SELECT COUNT(*) as totalDays FROM lib_light_record llr LEFT JOIN librarys ls ON llr.hallCode = ls.hallCode ");
		sbf.append(" WHERE 1 = 1 ");  
		
		//判断是否是全部搜索	只能管理员有权利全部搜索
		String area = qc.getArea();
		String lib = qc.getLib();
		String hallCode = qc.getHallCode();
		String beginTime = qc.getDateFrom();
		String endTime = qc.getDateTo();
		if ( !"".equals(lib) ) {
			sbf.append(" AND ls.libraryLevel = '"+lib+"' ");
		}
		//条件拼接 馆号
		if("0".equals(qc.getOption())){
			if(StringUtils.isNotBlank(qc.getSearchFrom())){
				sbf.append(" and ls.hallCode>='"+qc.getSearchFrom()+"' ");
			}
			if(StringUtils.isNotBlank(qc.getSearchTo())){
				sbf.append(" and ls.hallCode<='"+qc.getSearchTo()+"' ");
			}
		}
		//条件拼接 日期
		if("2".equals(qc.getOption())){
			if(StringUtils.isNotBlank(qc.getDateFrom())){
				sbf.append(" and llr.light_date >= '"+ qc.getDateFrom() +"' ");
			}
			if(StringUtils.isNotBlank(qc.getDateTo())){
				sbf.append(" and llr.light_date <= '"+ qc.getDateTo() +"' ");
			}
		}
		//添加条件	
		if (Common.ROLE_FIRST_LEVLE.equals(le)) {
			flag = true;
			if(!"".equals(qc.getProvince())){
				sbf.append(" and ls.provinceCode='"+qc.getProvince()+"' ");
			}
			if(!"".equals(qc.getCity())){
				sbf.append(" and ls.cityCode='"+qc.getCity()+"' ");
			}
			if(!"".equals(qc.getArea())){
				sbf.append(" AND ls.areaCode = '"+area+"' ");
			}
		} else {
			if (!"".equals(qc.getArea())) {
				sbf.append(" AND ls.areaAddress = '"+area+"' ");
			}
			sbf.append(" AND ls.customerId = ? ");
			id = user.getCustomer().getId();
		}
		//连接好所有的查询条件并创建sql语句
		String sql = sbf.toString();
		SQLQuery query = this.getSession().createSQLQuery(sql);
		//对应的实体类
		//query.setResultTransformer( Transformers.aliasToBean(LightReport.class));
		//设置参数 -----------------------------------------------------------
		if(!flag){
			query.setInteger(0,id);
		}
		//进行查询
		BigInteger num = (BigInteger)query.uniqueResult();
		//返回
		return num;
	}

	//时间总数
	private void setSumInLastObject(List<LightReport> list, StringBuffer sumSql, int id, boolean isAdmin) {
		sumSql.insert(0,"SELECT count(*) as sumNumber, SUM(b.totalTime) as sumTotalTime FROM ( ");
		String str = sumSql.toString();
		str += ") AS b ";
		SQLQuery query = this.getSession().createSQLQuery(str);
		if(!isAdmin){
			query.setInteger(0,id);
		}
		@SuppressWarnings("unchecked")
		List<String> lis = query.list();
		String cont = new Gson().toJson(lis);
		if(list.size()>0 && cont.trim().length()>4)
		{
			cont = cont.substring(2,cont.length()-2);
			list.get(list.size()-1).setSumNumber(cont);
		}
	}

	/* (non-Javadoc)
	 * @see com.flea.modules.report.dao.LightReportDao#findLightByCode(java.lang.String)
	 */
	@Override
	public Map<String, String> findLightByCode(String code) {
		String sqlString = "SELECT light_option,phone from lib_light WHERE library_code=?";
		SQLQuery query = getSession().createSQLQuery(sqlString);
		query.setString(0, code);
		Object object =	query.uniqueResult();
		Map<String,String> map = new HashMap<String, String>();
		if(object != null) {
			Object[] obs = (Object[])object;
			String light =	(String)obs[0];
			String phone =	(String)obs[1];
			map.put("light", light);
			map.put("phone", phone);
		}
		return map;
	}
	
}
