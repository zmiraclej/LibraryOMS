package com.flea.modules.report.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import org.apache.commons.lang.StringUtils;
import com.flea.common.Common;
import com.flea.common.dao.impl.BaseDaoImpl;
import com.flea.common.pojo.Page;
import com.flea.common.pojo.Role;
import com.flea.common.pojo.User;
import com.flea.common.util.ShiroUtils;
import com.flea.modules.report.dao.CompensateBookDao;
import com.flea.modules.report.pojo.CatalogueReport;
import com.flea.modules.report.pojo.vo.QueryCriteria;
import com.google.gson.Gson;

/**
 * 赔书统计DAO接口
 * @author bruce
 * @version 2016-07-13
 */
@Repository
public class CompensateBookDaoImpl extends BaseDaoImpl<CatalogueReport,Integer> implements CompensateBookDao{

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
	public Page<CatalogueReport> find(Page<CatalogueReport> page, String area, String lib) {
		//获取当前登录的用户
		User user = ShiroUtils.getCurrentUser();
		Integer id = user.getCustomer().getId();
		String sql = "SELECT lb.hallCode,lb.name,lcr.compensateDate AS catalogueDate,lcr.returnCode AS catalogueCode,COUNT(lcrm.recordId) AS count,SUM(lbk.price) AS price,lu.userName FROM librarys AS lb JOIN library_compensate_record AS lcr ON(lb.hallCode=lcr.hallCode) JOIN library_compensate_record_map AS lcrm ON(lcr.id=lcrm.recordId) JOIN librarys_users AS lu ON(lcr.operUser=lu.id) JOIN library_books AS lbk ON(lcrm.libraryBookId=lbk.id) WHERE lb.customerId=? AND lb.areaAddress=? AND lb.libraryLevel=? GROUP BY lcrm.recordId";
		SQLQuery query = this.getSession().createSQLQuery(sql);
		query.setResultTransformer(Transformers.aliasToBean(CatalogueReport.class));
		query.setInteger(0,id);
		query.setString(1,area);
		query.setString(2,lib);
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
		User user = ShiroUtils.getCurrentUser();
		Role role = user.getRoles().get(0);
		String le = role.getLevel();
		int id=0;
		boolean flag = false;
		//拼凑SQL语句
		StringBuffer sbf = new StringBuffer();
		sbf.append("SELECT lb.hallCode,lb.name,lcr.compensateDate AS catalogueDate,lcr.returnCode AS catalogueCode,COUNT(lcrm.recordId) AS count,truncate(SUM(lbk.price),2) AS price,lu.userName FROM librarys AS lb JOIN library_compensate_record AS lcr ON(lb.hallCode=lcr.hallCode) JOIN library_compensate_record_map AS lcrm ON(lcr.id=lcrm.recordId) JOIN librarys_users AS lu ON(lcr.operUser=lu.id) JOIN library_books AS lbk ON(lcrm.libraryBookId=lbk.id) ");
		
		//判断是否是全部搜索	只能管理员有权利全部搜索
		String area = qc.getArea();
		String lib = qc.getLib();
		sbf.append("where 1=1 ");
		
		if (StringUtils.isNotBlank(qc.getLib())) {
			sbf.append(" and lb.libraryLevel='" + qc.getLib() + "'");
		}
		if (StringUtils.isNotBlank(qc.getProvince())) {
			sbf.append(" and lb.provinceCode='" + qc.getProvince() + "' ");
		}
		if (StringUtils.isNotBlank(qc.getCity())) {
			sbf.append(" and lb.cityCode='" + qc.getCity() + "' ");
		}
		
		
		//添加条件	
		if(Common.ROLE_FIRST_LEVLE.equals(le)){
			flag = true;
			if (StringUtils.isNotBlank(qc.getArea())) {
				sbf.append(" and lb.areaCode='" + qc.getArea() + "' ");
			}
		}else{
			if (StringUtils.isNotBlank(qc.getArea())) {
				sbf.append(" and lb.areaAddress='" + qc.getArea() + "' ");
			}
			
			sbf.append("AND lb.customerId=? ");
			id = user.getCustomer().getId();
		}
		sbf.append("AND lb.isEffective=1 ");
		String option = qc.getOption();
		getLibName(option,sbf,qc);
		StringBuffer sumSql = sbf;
		//连接好所有的查询条件并创建sql语句
		String sql = sbf.toString();
		SQLQuery query = this.getSession().createSQLQuery(sql);
		//对应的实体类
		query.setResultTransformer( Transformers.aliasToBean(CatalogueReport.class));
		//设置参数 -----------------------------------------------------------
		if(!flag){
			query.setInteger(0,id);
		}
		//设置分页
		List lis = query.list();
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
		page.setCount(lis.size());
		//分页信息
		query.setFirstResult(page.getFirstResult());
		query.setMaxResults(page.getPageSize());
		//进行查询
		List<CatalogueReport> list = query.list();
		//设置集合总数到最后一个对象里面
		setSumInLastObject(list,sumSql,id,flag);
		page.setList(list);
		//返回
		return page;
	}

	//统计
	private void setSumInLastObject(List<CatalogueReport> list, StringBuffer sumSql, int id, boolean isAdmin) {
		String str = sumSql.toString();
		str += ") as a";
		str = str.replace("SELECT lb.hallCode,lb.name,lcr.compensateDate AS catalogueDate,lcr.returnCode AS catalogueCode,COUNT(lcrm.recordId) AS count,truncate(SUM(lbk.price),2) AS price,lu.userName","SELECT  SUM(a.category),truncate(SUM(a.price),2) FROM(SELECT COUNT(lcrm.recordId) category,SUM(lbk.price) price");
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

	/**
	 * 
	 * @method:getLibName	拼凑动态SQL
	 * @Description:getLibName
	 * @author: HeTao
	 * @date:2016年7月13日 上午11:42:21
	 * @param:@param option
	 * @param:@param sbf
	 * @param:@param qc
	 * @return:void
	 */
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
			sbf.append(" GROUP BY lcr.returnCode,lcr.compensateDate");
			break;
		//馆名
		case "1":
			if(cont.trim().length()>0){
				sbf.append(" AND lb.name LIKE '%"+cont.trim()+"%' ");
			}
			sbf.append(" GROUP BY lcr.returnCode,lcr.compensateDate");
			break;
		//日期
		case "2":
			if(dateFrom.trim().length()>0 && dateTo.trim().length()>0){
				sbf.append(" AND lcr.compensateDate >= '"+dateFrom+"' AND lcr.compensateDate <= '"+dateTo+"' ");
			}else if(dateFrom.trim().length()>0 && dateTo.trim().length()==0){
				sbf.append(" AND lcr.compensateDate >= '"+dateFrom+"' ");
			}else if(dateFrom.trim().length()==0 && dateTo.trim().length()>0){
				sbf.append(" AND lcr.compensateDate <= '"+dateTo+"' ");
			}
			sbf.append(" GROUP BY lcr.returnCode,lcr.compensateDate");
			break;
		//单号
		case "3":
			if(priceFrom.trim().length()>0 && priceTo.trim().length()>0){
				sbf.append(" AND lcr.returnCode >= '"+priceFrom.trim()+"' AND lcr.returnCode <= '"+priceTo.trim()+"' ");
			}else if(priceFrom.trim().length()>0 && priceTo.trim().length()==0){
				sbf.append(" AND lcr.returnCode >= '"+priceFrom.trim()+"' ");
			}else if(priceFrom.trim().length()==0 && priceTo.trim().length()>0){
				sbf.append(" AND lcr.returnCode <= '"+priceTo.trim()+"' ");
			}
			sbf.append(" GROUP BY lcr.returnCode,lcr.compensateDate");
			break;
		//册数
		case "4":
			sbf.append(" GROUP BY lcr.returnCode,lcr.compensateDate");
			if(priceFrom.trim().length()>0 && priceTo.trim().length()>0){
				sbf.append(" HAVING COUNT(lcr.returnCode) >= "+priceFrom.trim()+" AND COUNT(lcr.returnCode) <= "+priceTo.trim()+" ");
			}else if(priceFrom.trim().length()>0 && priceTo.trim().length()==0){
				sbf.append(" HAVING COUNT(lcr.returnCode) >= "+priceFrom.trim()+" ");
			}else if(priceFrom.trim().length()==0 && priceTo.trim().length()>0){
				sbf.append(" HAVING COUNT(lcr.returnCode) <= "+priceTo.trim()+" ");
			}
			break;
		//码洋
		case "5":
			sbf.append(" GROUP BY lcr.returnCode,lcr.compensateDate");
			if(priceFrom.trim().length()>0 && priceTo.trim().length()>0){
				sbf.append(" HAVING SUM(lbk.price) >= "+priceFrom+" AND SUM(lbk.price) <= "+priceTo);
			}else if(priceFrom.trim().length()>0 && priceTo.trim().length()==0){
				sbf.append(" HAVING SUM(lbk.price) >= "+priceFrom);
			}else if(priceFrom.trim().length()==0 && priceTo.trim().length()>0){
				sbf.append(" HAVING SUM(lbk.price) <= "+priceTo);
			}
			break;
		//操作员
		case "6":
			if(cont.trim().length()>0){
				sbf.append(" AND lu.userName LIKE '%"+cont.trim()+"%' ");
			}
			sbf.append(" GROUP BY lcr.returnCode,lcr.compensateDate");
			break;
		}
		
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
		User user = ShiroUtils.getCurrentUser();
		Role role = user.getRoles().get(0);
		String le = role.getLevel();
		int id=0;
		boolean flag = false;
		//GROUP BY lb.customerId
		String str = "SELECT COUNT(lcr.returnCode),SUM(lbk.price) FROM librarys AS lb JOIN library_compensate_record AS lcr ON(lb.hallCode=lcr.hallCode) JOIN library_compensate_record_map AS lcrm ON(lcr.id=lcrm.recordId) JOIN librarys_users AS lu ON(lcr.operUser=lu.id) JOIN library_books AS lbk ON(lcrm.libraryBookId=lbk.id) WHERE lb.customerId=? AND lb.areaAddress=? AND lb.libraryLevel=?";
		if(Common.ROLE_FIRST_LEVLE.equals(le)){
			str = str.replace("lb.customerId=? AND ","");
			flag = true;
			return "0-0";
		}else{
			id = user.getCustomer().getId();
		}
		SQLQuery query = this.getSession().createSQLQuery(str);
		if(flag){
			query.setString(0,string);
			query.setString(1,string2);
		}else{
			query.setInteger(0,id);
			query.setString(1,string);
			query.setString(2,string2);
		}
		List<String> list = query.list();
		if(list.size()==0){
			list.add("0-0");
			return list.get(0);
		}
		list = changeStatus(list);
		return list.get(0);
	}
	
	private List<String> changeStatus(List<String> hall) {
		List<String> list = null;
		list = new ArrayList<String>(); 
		for (int i = 0; i < hall.size(); i++){
			String str = new Gson().toJson(hall.get(i)).replace("[","").replace("]","").replace("\"","");
			String[] arr = str.split(",");
			String aa = "null".equals(arr[1])?"0":arr[1];
			list.add(arr[0]+"-"+aa);
		}
		return list;
	}
	
}
