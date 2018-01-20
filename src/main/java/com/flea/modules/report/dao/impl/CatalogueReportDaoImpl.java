package com.flea.modules.report.dao.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

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
import com.flea.modules.report.dao.CatalogueReportDao;
import com.flea.modules.report.pojo.CatalogueReport;
import com.flea.modules.report.pojo.ReportTotal;
import com.flea.modules.report.pojo.VLibraryBook;
import com.flea.modules.report.pojo.vo.QueryCriteria;
import com.google.gson.Gson;

/**
 * 编目统计DAO接口
 * @author bruce
 * @version 2016-07-11
 */
@Repository
public class CatalogueReportDaoImpl extends BaseDaoImpl<CatalogueReport,Integer> implements CatalogueReportDao{
	
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
	public Page<CatalogueReport> find(Page<CatalogueReport> page,String area,String lib) {
		/*area = "四川省-绵阳市-涪城区";
		lib = "社区书屋";*/
		//获取当前登录的用户
		User user = ShiroUtils.getCurrentUser();
		Role role = user.getRoles().get(0);
		String le = role.getLevel();
		int id;
		if(("0".equals(area)&&"0".equals(lib)) ||Common.ROLE_FIRST_LEVLE.equals(le)){
			 return page;
		}else{
			id = user.getCustomer().getId();
		}
		//查询列表
		String sql = "SELECT lb.hallCode,lb.name,lbs.catalogueDate,lbs.catalogueCode,COUNT(lbs.catalogueCode) AS count,SUM(lbs.price) AS price,lu.userName FROM librarys AS lb JOIN library_books AS lbs ON(lb.hallCode=lbs.belongLibraryHallCode) JOIN librarys_users AS lu ON(lbs.operUser=lu.id) WHERE lb.customerId=? AND lb.isEffective=1 AND lb.areaAddress=? AND lb.libraryLevel=? GROUP BY lbs.catalogueCode";
		//查询合计
		String sumSql = "";
		
		SQLQuery query = this.getSession().createSQLQuery(sql);
		query.setResultTransformer( Transformers.aliasToBean(CatalogueReport.class));
		//设置参数
		query.setInteger(0, id);
		query.setString(1, area);
		query.setString(2,lib.split("-")[0]);
		List lis = query.list();
		page.setCount(lis.size());
		query.setFirstResult(page.getFirstResult());
		query.setMaxResults(page.getPageSize());
		//进行查询
		List<CatalogueReport> list = query.list();
		page.setList(list);
		return page;
	}

	/**
   	 * 
   	 * @method:find
   	 * @Description:find	根据查询条件查询
   	 * @param:@param page
   	 * @param:@param qc
   	 * @param:@return
   	 * @return:Page<CatalogueReport>
   	 */
	@Override
	public Page<CatalogueReport> find(Page<CatalogueReport> page, QueryCriteria qc) {
		//获取当前登录的用户
		User user = ShiroUtils.getCurrentUser();
		Role role = user.getRoles().get(0);
		String le = role.getLevel();
		int id=0;
		boolean flag = false;
		StringBuffer sbf = new StringBuffer();
		sbf.append("SELECT lb.hallCode,lb.`name`,lbs.catalogueDate,lbs.catalogueCode,COUNT(lbs.catalogueCode) AS count,truncate(SUM(lbs.price),2) AS price,lu.userName FROM librarys AS lb JOIN library_books AS lbs ON(lb.hallCode=lbs.belongLibraryHallCode) JOIN librarys_users AS lu ON(lbs.operUser=lu.id) ");
		
		//判断是否是全部搜索	只能管理员有权利全部搜索
		String area = qc.getArea();
		String lib = qc.getLib();
		sbf.append("where 1=1 ");
		//当搜索地区区域为空时：

		sbf.append("AND lb.isEffective=1 ");
//		if (StringUtils.isNotBlank(qc.getArea())) {
//			if (qc.getCustomerId() == null) { // 一级用户
//				sbf.append(" and lb.areaCode='" + qc.getArea() + "' ");
//			} else {// 二级
//				sbf.append(" and lb.areaAddress='" + qc.getArea() + "' ");
//			}
//		}
		if (StringUtils.isNotBlank(qc.getCity())) {
			sbf.append(" and lb.cityCode='" + qc.getCity() + "' ");
		}
		if (StringUtils.isNotBlank(qc.getProvince())) {
			sbf.append(" and lb.provinceCode='" + qc.getProvince() + "' ");
		}
		if (StringUtils.isNotBlank(qc.getLib())) {
			sbf.append(" and lb.libraryLevel='" + qc.getLib() + "'");
		}
		
		//添加条件	
		if(Common.ROLE_FIRST_LEVLE.equals(le)) {
			flag = true;
			if(StringUtils.isNotBlank(qc.getArea())){
				sbf.append(" AND lb.areaCode = '"+area+"' ");
			}
		} else {
			if(StringUtils.isNotBlank(qc.getArea())){
				sbf.append(" and lb.areaAddress='" + qc.getArea() + "' ");
			}
			sbf.append("AND lb.customerId=? ");
			id = user.getCustomer().getId();
		}
		
		String option = qc.getOption();
		getLibName(option,sbf,qc);
		StringBuffer sumSql = sbf;
		//连接好所有的查询条件并创建sql语句
		String sql = sbf.toString();
		SQLQuery query = this.getSession().createSQLQuery(sql);
		//对应的实体类
		query.setResultTransformer( Transformers.aliasToBean(CatalogueReport.class));
		//设置参数
		if(!flag){
			query.setInteger(0,id);
		}
		//设置分页
		List<CatalogueReport> bookList = query.list();
		if(page==null){
			page = new Page<CatalogueReport>();
			//进行查询
			List<CatalogueReport> list = query.list();
			//设置集合总数到最后一个对象里面
			setSumInLastObject(list,sumSql,id,flag);
			page.setList(list);
			//返回
			return page;
		}
		page.setCount(bookList.size());
		//分页信息

		List<CatalogueReport> resultList = new ArrayList();
		for (int i = page.getFirstResult(); i < page.getFirstResult() + page.getPageSize(); i++) {
			if (i >= bookList.size())
				break;
			resultList.add(bookList.get(i));
		}
		
		BigDecimal price = BigDecimal.valueOf(0);
		BigInteger count = BigInteger.valueOf(0);
		for (int i = 0;i< bookList.size();i++){
			price = price.add(BigDecimal.valueOf(bookList.get(i).getPrice()));
			
			count = count.add(bookList.get(i).getCount());
		}
		//resultList.get(resultList.size()-1).setSumNumber("count"+"," + "price");
		page.setList(resultList);
		ReportTotal reportTotal= new ReportTotal();
		reportTotal.setSumPrice(price.doubleValue());
		reportTotal.setSumCount(count);
		page.setTotal(reportTotal);
		//返回
		return page;
	}
	
	
	/**
   	 * 
   	 * @method:find
   	 * @Description:find	根据查询条件查询
   	 * @param:@param page
   	 * @param:@param qc
   	 * @param:@return
   	 * @return:Page<CatalogueReport>
   	 */

	public Page<CatalogueReport> find(Page<CatalogueReport> page, QueryCriteria param,HttpSession session) {
		//获取当前登录的用户
		User user = ShiroUtils.getCurrentUser();
		Role role = user.getRoles().get(0);
		String le = role.getLevel();
		int id=0;
		boolean flag = false;
		StringBuffer sbf = new StringBuffer();
		sbf.append("SELECT lb.hallCode,lb.`name`,lbs.catalogueDate,lbs.catalogueCode,COUNT(lbs.catalogueCode) AS count,truncate(SUM(lbs.price),2) AS price,lu.userName FROM librarys AS lb JOIN library_books AS lbs ON(lb.hallCode=lbs.belongLibraryHallCode) JOIN librarys_users AS lu ON(lbs.operUser=lu.id) ");
		
		//判断是否是全部搜索	只能管理员有权利全部搜索
		String area = param.getArea();
		String lib = param.getLib();
		sbf.append("where 1=1 ");
		//当搜索地区区域为空时：

		sbf.append("AND lb.isEffective=1 ");
		/*if (StringUtils.isNotBlank(param.getArea())) {
			if (user.getCustomer().getId() == null) { // 一级用户
				sbf.append(" and lb.areaCode='" + param.getArea() + "' ");
			} else {// 二级
				sbf.append(" and lb.areaAddress='" + param.getArea() + "' ");
			}
		}*/
		if (StringUtils.isNotBlank(param.getCity())) {
			sbf.append(" and lb.cityCode='" + param.getCity() + "' ");
		}
		if (StringUtils.isNotBlank(param.getProvince())) {
			sbf.append(" and lb.provinceCode='" + param.getProvince() + "' ");
		}
		if (StringUtils.isNotBlank(param.getLib())) {
			sbf.append(" and lb.libraryLevel='" + param.getLib() + "'");
		}
		
		//添加条件	
		if(Common.ROLE_FIRST_LEVLE.equals(le)) {
			flag = true;
			if (StringUtils.isNotBlank(param.getArea())) {
				sbf.append(" and lb.areaCode='" + param.getArea() + "' ");
			}
			
		} else {
			if (StringUtils.isNotBlank(param.getArea())) {
				sbf.append(" and lb.areaAddress='" + param.getArea() + "' ");
			}
			sbf.append("AND lb.customerId=? ");
			
			id = user.getCustomer().getId();
		}
		
		String option = param.getOption();
		getLibName(option,sbf,param);
		StringBuffer sumSql = sbf;
		//连接好所有的查询条件并创建sql语句
		String sql = sbf.toString();
		SQLQuery query = this.getSession().createSQLQuery(sql);
		//对应的实体类
		query.setResultTransformer( Transformers.aliasToBean(CatalogueReport.class));
		//设置参数
		if(!flag){
			query.setInteger(0,id);
		}
		//设置分页
		//缓存
		List<CatalogueReport> bookList = ( List<CatalogueReport>)session.getAttribute("bookList");;
		QueryCriteria qca = (QueryCriteria) session.getAttribute("param");
		if (null != qca && param.equals(qca) && null != bookList) {
		} else {
		bookList = query.list();
		session.setAttribute("param", param);
		session.setAttribute("bookList", bookList);
		}
		if(page==null){
			page = new Page<CatalogueReport>();
			//进行查询
			List<CatalogueReport> list = query.list();
			//设置集合总数到最后一个对象里面
			setSumInLastObject(list,sumSql,id,flag);
			page.setList(list);
			//返回
			return page;
		}
		page.setCount(bookList.size());
		//分页信息

		List<CatalogueReport> resultList = new ArrayList();
		for (int i = page.getFirstResult(); i < page.getFirstResult() + page.getPageSize(); i++) {
			if (i >= bookList.size())
				break;
			resultList.add(bookList.get(i));
		}
		
		BigDecimal price = BigDecimal.valueOf(0);
		BigInteger count = BigInteger.valueOf(0);
		for (int i = 0;i< bookList.size();i++){
			price = price.add(BigDecimal.valueOf(bookList.get(i).getPrice()));
			count = count.add(bookList.get(i).getCount());
		}
		//resultList.get(resultList.size()-1).setSumNumber("count"+"," + "price");
		page.setList(resultList);
		ReportTotal reportTotal= new ReportTotal();
		reportTotal.setSumPrice(price.doubleValue());
		reportTotal.setSumCount(count);
		page.setTotal(reportTotal);
		//返回
		return page;
	}
	
	//设置集合总数到最后一个对象里面
	private void setSumInLastObject(List<CatalogueReport> list, StringBuffer sumSql,int id,boolean isAdmin) {
		String str = sumSql.toString();
		str += ") as a";
		str = str.replace("SELECT lb.hallCode,lb.`name`,lbs.catalogueDate,lbs.catalogueCode,COUNT(lbs.catalogueCode) AS count,truncate(SUM(lbs.price),2) AS price,lu.userName","SELECT  SUM(a.category),truncate(SUM(a.price),2) FROM(SELECT COUNT(lbs.catalogueCode) category,SUM(lbs.price) price");
		SQLQuery query = this.getSession().createSQLQuery(str);
		if(!isAdmin){
			query.setInteger(0,id);
		}
		List<String> lis = query.list();
		String cont = new Gson().toJson(lis);
		if(list.size()>0 && cont.trim().length()>4)
		{
			cont = cont.substring(2,cont.length()-2);
			
			list.get(list.size()-1).setSumNumber(cont);
		}
	}

	private void getLibName(String option, StringBuffer sbf, QueryCriteria qc) {
		//对特定的条件进行拼凑
		String dateFrom = qc.getDateFrom();
		String dateTo = qc.getDateTo();
		String priceFrom = qc.getSearchFrom();
		String priceTo = qc.getSearchTo();
		String cont = qc.getKeyWord();
		switch (option) {
		//馆号
		case "0":
			if(priceFrom.trim().length()>0 && priceTo.trim().length()>0){
				sbf.append(" AND lb.hallCode >= '"+priceFrom.trim()+"' AND lb.hallCode <= '"+priceTo.trim()+"' ");
			}else if(priceFrom.trim().length()>0 && priceTo.trim().length()==0){
				sbf.append(" AND lb.hallCode >= '"+priceFrom.trim()+"' ");
			}else if(priceFrom.trim().length()==0 && priceTo.trim().length()>0){
				sbf.append(" AND lb.hallCode <= '"+priceTo.trim()+"' ");
			}
//			sbf.append(" GROUP BY lbs.catalogueCode,lbs.catalogueDate");
			sbf.append(" GROUP BY lbs.belongLibraryHallCode,lbs.catalogueCode,lbs.catalogueDate,lbs.operUser ");
			break;
		//馆名
		case "1":
			if(cont.trim().length()>0){
				sbf.append(" AND lb.name LIKE '%"+cont.trim()+"%' ");
			}
//			sbf.append(" GROUP BY lbs.catalogueCode,lbs.catalogueDate");
			sbf.append(" GROUP BY lbs.belongLibraryHallCode,lbs.catalogueCode,lbs.catalogueDate,lbs.operUser ");
			break;
		//日期
		case "2":
			if(dateFrom.trim().length()>0 && dateTo.trim().length()>0){
				sbf.append(" AND lbs.catalogueDate >= '"+dateFrom+"' AND lbs.catalogueDate <= '"+dateTo+"' ");
			}else if(dateFrom.trim().length()>0 && dateTo.trim().length()==0){
				sbf.append(" AND lbs.catalogueDate >= '"+dateFrom+"' ");
			}else if(dateFrom.trim().length()==0 && dateTo.trim().length()>0){
				sbf.append(" AND lbs.catalogueDate <= '"+dateTo+"' ");
			}
//			sbf.append(" GROUP BY lbs.catalogueCode,lbs.catalogueDate");
			sbf.append(" GROUP BY lbs.belongLibraryHallCode,lbs.catalogueCode,lbs.catalogueDate,lbs.operUser ");
			break;
		//单号
		case "3":
			if(priceFrom.trim().length()>0 && priceTo.trim().length()>0){
				sbf.append(" AND lbs.catalogueCode >= '"+priceFrom.trim()+"' AND lbs.catalogueCode <= '"+priceTo.trim()+"' ");
			}else if(priceFrom.trim().length()>0 && priceTo.trim().length()==0){
				sbf.append(" AND lbs.catalogueCode >= '"+priceFrom.trim()+"' ");
			}else if(priceFrom.trim().length()==0 && priceTo.trim().length()>0){
				sbf.append(" AND lbs.catalogueCode <= '"+priceTo.trim()+"' ");
			}
//			sbf.append(" GROUP BY lbs.catalogueCode,lbs.catalogueDate");
			sbf.append(" GROUP BY lbs.belongLibraryHallCode,lbs.catalogueCode,lbs.catalogueDate,lbs.operUser ");
			break;
		//册数
		case "4":
//			sbf.append(" GROUP BY lbs.catalogueCode,lbs.catalogueDate");
			sbf.append(" GROUP BY lbs.belongLibraryHallCode,lbs.catalogueCode,lbs.catalogueDate,lbs.operUser ");
			if(priceFrom.trim().length()>0 && priceTo.trim().length()>0){
				sbf.append(" HAVING COUNT(lbs.catalogueCode) >= "+priceFrom.trim()+" AND COUNT(lbs.catalogueCode) <= "+priceTo.trim()+" ");
			}else if(priceFrom.trim().length()>0 && priceTo.trim().length()==0){
				sbf.append(" HAVING COUNT(lbs.catalogueCode) >= "+priceFrom.trim()+" ");
			}else if(priceFrom.trim().length()==0 && priceTo.trim().length()>0){
				sbf.append(" HAVING COUNT(lbs.catalogueCode) <= "+priceTo.trim()+" ");
			}
			break;
		//码洋
		case "5":
//			sbf.append(" GROUP BY lbs.catalogueCode,lbs.catalogueDate");
			sbf.append(" GROUP BY lbs.belongLibraryHallCode,lbs.catalogueCode,lbs.catalogueDate,lbs.operUser ");
			if(priceFrom.trim().length()>0 && priceTo.trim().length()>0){
				sbf.append(" HAVING SUM(lbs.price) >= "+priceFrom+" AND SUM(lbs.price) <= "+priceTo);
			}else if(priceFrom.trim().length()>0 && priceTo.trim().length()==0){
				sbf.append(" HAVING SUM(lbs.price) >= "+priceFrom);
			}else if(priceFrom.trim().length()==0 && priceTo.trim().length()>0){
				sbf.append(" HAVING SUM(lbs.price) <= "+priceTo);
			}
			break;
		//操作员
		case "6":
			if(cont.trim().length()>0){
				sbf.append(" AND lu.userName LIKE '%"+cont.trim()+"%' ");
			}
//			sbf.append(" GROUP BY lbs.catalogueCode,lbs.catalogueDate");
			sbf.append(" GROUP BY lbs.belongLibraryHallCode,lbs.catalogueCode,lbs.catalogueDate,lbs.operUser ");
			break;
		}
		
	}
}
