package com.flea.modules.report.dao.impl;


import java.util.ArrayList;
import java.util.List;

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
import com.flea.modules.report.dao.CirculatedReportDao;
import com.flea.modules.report.pojo.CatalogueReport;
import com.flea.modules.report.pojo.Circulated;
import com.flea.modules.report.pojo.vo.QueryCriteria;
import com.google.gson.Gson;

/**
 * 流通统计DAO接口
 * @author bruce
 * @version 2016-07-16
 */
@Repository
public class CirculatedReportDaoImpl extends BaseDaoImpl<CatalogueReport,Integer> implements CirculatedReportDao{
	
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
	public Page<Circulated> find(Page<Circulated> page, String area, String lib) {
		//获取当前登录的用户
		User user = ShiroUtils.getCurrentUser();
		Integer id = user.getCustomer().getId();
		//查询各个馆的累计流出和各个馆的累计流入  没有条件都是默认的
		String sql = "";
		sql += "SELECT a.*,b.* FROM";
		//																																																																																											CONVERT(,DECIMAL(15,2))
		sql += " (SELECT lb.hallCode AS hallCodeT,COUNT(CASE WHEN lb.hallCode=lc.outHallCode THEN lc.outHallCode END) AS totalNowOut,CONVERT(SUM(CASE WHEN lb.hallCode=lc.outHallCode AND lc.id=lcm.circulateId AND lcm.libraryBookId = lbk.id THEN lbk.price END),DECIMAL(15,2)) AS totalOutMoney ,COUNT(CASE WHEN lb.hallCode=inHallCode THEN lc.inHallCode END) AS totalNowin,CONVERT(SUM(CASE WHEN lb.hallCode=lc.inHallCode AND lc.id=lcm.circulateId AND lcm.libraryBookId = lbk.id THEN lbk.price END),DECIMAL(15,2)) AS totalNowInMoney  FROM librarys AS lb JOIN library_circulate AS lc ON(lb.hallCode=lc.outHallCode OR lb.hallCode=lc.inHallCode) JOIN library_circulate_map AS lcm ON(lc.id=lcm.circulateId) JOIN library_books AS lbk ON(lcm.libraryBookId=lbk.id) WHERE "
				+ "lb.customerId="+id+" AND lb.areaAddress='"+area+"' AND lb.libraryLevel='"+lib+"' AND lc.outState=29 AND lc.inState=28 GROUP BY lb.hallCode) b RIGHT JOIN";
		//																																																																																																																							CONVERT(,DECIMAL(15,2))
		sql += "(SELECT lb.hallCode,lb.`name`,COUNT(CASE WHEN lb.hallCode=lc.outHallCode THEN lc.outHallCode END) AS nowOut,CONVERT(SUM(CASE WHEN lb.hallCode=lc.outHallCode AND lc.id=lcm.circulateId AND lcm.libraryBookId = lbk.id THEN lbk.price END),DECIMAL(15,2)) AS nowOutMoney ,COUNT(CASE WHEN lb.hallCode=inHallCode THEN lc.inHallCode END) AS nowIn,CONVERT(SUM(CASE WHEN lb.hallCode=lc.inHallCode AND lc.id=lcm.circulateId AND lcm.libraryBookId = lbk.id THEN lbk.price END),DECIMAL(15,2)) AS nowInMoney FROM librarys AS lb JOIN library_circulate AS lc ON(lb.hallCode=lc.outHallCode OR lb.hallCode=lc.inHallCode) JOIN library_circulate_map AS lcm ON(lc.id=lcm.circulateId) JOIN library_books AS lbk ON(lcm.libraryBookId=lbk.id) WHERE "
				+ "lb.customerId="+id+" AND lb.areaAddress='"+area+"' AND lb.libraryLevel='"+lib+"' AND lc.outState=29 AND lc.inState=28 GROUP BY lb.hallCode) a ON a.hallCode = b.hallCodeT";
		SQLQuery query = this.getSession().createSQLQuery(sql);
		query.setResultTransformer(Transformers.aliasToBean(Circulated.class));
		List lis = query.list();
		page.setCount(lis.size());
		query.setFirstResult(page.getFirstResult());
		query.setMaxResults(page.getPageSize());
		//进行查询
		List<Circulated> list = query.list();
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
	public Page<Circulated> find(Page<Circulated> page, QueryCriteria qc) {
		User user = ShiroUtils.getCurrentUser();
		Role role = user.getRoles().get(0);
		String le = role.getLevel();
		int id=0;
		boolean flag = false;
		//拼凑SQL语句
		StringBuffer sbf = new StringBuffer();
		//区域
		String area = qc.getArea();
		//馆名
		String lib = qc.getLib();
		//馆号
		String libNum = qc.getHallCode();
		
		sbf.append("SELECT a.*,b.* FROM ");
		//第一个条件  查询累积的数量与总价 																																																																																																																										CONVERT(,DECIMAL(15,2))					
		sbf.append("(SELECT lb.hallCode AS hallCodeT,lc.outDate,COUNT(CASE WHEN lb.hallCode=lc.outHallCode THEN lc.outHallCode END) AS totalNowOut,CONVERT(SUM(CASE WHEN lb.hallCode=lc.outHallCode AND lc.id=lcm.circulateId AND lcm.libraryBookId = lbk.id THEN lbk.price END),DECIMAL(15,2)) AS totalOutMoney ,COUNT(CASE WHEN lb.hallCode=inHallCode THEN lc.inHallCode END) AS totalNowin,CONVERT(SUM(CASE WHEN lb.hallCode=lc.inHallCode AND lc.id=lcm.circulateId AND lcm.libraryBookId = lbk.id THEN lbk.price END),DECIMAL(15,2)) AS totalNowInMoney  FROM librarys AS lb JOIN library_circulate AS lc ON(lb.hallCode=lc.outHallCode OR lb.hallCode=lc.inHallCode) JOIN library_circulate_map AS lcm ON(lc.id=lcm.circulateId) JOIN library_books AS lbk ON(lcm.libraryBookId=lbk.id) WHERE "
				+" lc.outState=29 AND lc.inState=28 ");
		
		//添加筛选条件
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
			 flag=true;
			 if (StringUtils.isNotBlank(qc.getArea())) {
				 sbf.append(" and lb.areaCode='" + qc.getArea() + "' ");
			 }
			 
		}else{
			id = user.getCustomer().getId();
			if(StringUtils.isNotBlank(qc.getArea())){
				sbf.append(" and lb.areaAddress='" + qc.getArea() + "' ");
			}
			sbf.append(" AND lb.customerId= "+id );
		}
		
		sbf.append(" AND lb.isEffective = 1 ");
		sbf.append(" GROUP BY lb.hallCode) b RIGHT JOIN ( ");
		
		//第二个条件 查询当前流入\总计当前流出\总计																																																																																																															CONVERT(,DECIMAL(15,2))
		sbf.append("SELECT lb.hallCode, lb.`name`, COUNT(CASE WHEN lb.hallCode=lc.outHallCode THEN lc.outHallCode END) AS nowOut,CONVERT(SUM(CASE WHEN lb.hallCode=lc.outHallCode AND lc.id=lcm.circulateId AND lcm.libraryBookId = lbk.id THEN lbk.price END),DECIMAL(15,2)) AS nowOutMoney ,COUNT(CASE WHEN lb.hallCode=inHallCode THEN lc.inHallCode END) AS nowIn,CONVERT(SUM(CASE WHEN lb.hallCode=lc.inHallCode AND lc.id=lcm.circulateId AND lcm.libraryBookId = lbk.id THEN lbk.price END),DECIMAL(15,2)) AS nowInMoney  FROM librarys AS lb JOIN library_circulate AS lc ON(lb.hallCode=lc.outHallCode OR lb.hallCode=lc.inHallCode) JOIN library_circulate_map AS lcm ON(lc.id=lcm.circulateId) JOIN library_books AS lbk ON(lcm.libraryBookId=lbk.id) WHERE lc.outState=29 AND lc.inState=28 ");
		
		if (Common.ROLE_FIRST_LEVLE.equals(le)) {
			//平台条件，省市区、馆别
			if (StringUtils.isNotBlank(qc.getProvince())) {
				sbf.append(" and lb.provinceCode='" + qc.getProvince() + "' ");
			}
			if (StringUtils.isNotBlank(qc.getCity())) {
				sbf.append(" and lb.cityCode='" + qc.getCity() + "' ");
			}
			if (!"".equals(area)) {
				sbf.append(" AND lb.areaCode = '"+area+"' ");
			}
			if (!"".equals(lib)) {
				sbf.append(" AND lb.libraryLevel LIKE '%"+lib+"%' ");
			}
		} else {
			//客户查询
			if (!"".equals(area)) {
				sbf.append(" AND lb.areaAddress LIKE '%"+area+"%' ");
			}
			if (!"".equals(lib)) {
				sbf.append(" AND lb.libraryLevel LIKE '%"+lib+"%' ");
			}
		}
		
		//添加条件	
		if(Common.ROLE_FIRST_LEVLE.equals(le)){
			flag = true;
		}else{
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
		query.setResultTransformer( Transformers.aliasToBean(Circulated.class));
		//设置参数 -----------------------------------------------------------
		if(!flag){
			query.setInteger(0,id);
		}
		if(page==null){
			page = new Page<Circulated>();
			//进行查询
			List<Circulated> list = query.list();
			//设置集合总数到最后一个对象里面
			setSumInLastObject(list,sql,id,flag);
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
		List<Circulated> list = query.list();
		//设置集合总数到最后一个对象里面
		setSumInLastObject(list,sql,id,flag);
		page.setList(list);
		//返回
		return page;
	}
	
	//统计
	private void setSumInLastObject(List<Circulated> list,String sql, int id, boolean isAdmin) {
		sql = sql.replace("a.*,b.*","CONVERT(SUM(a.nowOut),DECIMAL(15,2)) AS reportNowOut,CONVERT(SUM(a.nowOutMoney),DECIMAL(15,2)) AS reportOutMoney ,CONVERT(SUM(a.nowIn),DECIMAL(15,2)) AS reportNowIn,CONVERT(SUM(a.nowInMoney),DECIMAL(15,2)) AS reportNowInMoney,CONVERT(SUM(b.totalNowOut),DECIMAL(15,2)) AS reportOldOut,CONVERT(SUM(b.totalOutMoney),DECIMAL(15,2)) AS reportOldMoney,CONVERT(SUM(b.totalNowin),DECIMAL(15,2)) AS reportOldIn,CONVERT(SUM(b.totalNowInMoney),DECIMAL(15,2)) AS reportOldInMoney ");
		SQLQuery query = this.getSession().createSQLQuery(sql);
		if(!isAdmin){
			query.setInteger(0,id);
		}
		query.setResultTransformer(Transformers.aliasToBean(Circulated.class));
		List<Circulated> qci = query.list();
		int size = list.size();
		if(size != 0){
			Circulated qc = qci.get(0);
			Circulated qp = list.get(size-1);
			qp.setReportNowIn(qc.getReportNowIn());
			qp.setReportNowInMoney(qc.getReportNowInMoney());
			qp.setReportNowOut(qc.getReportNowOut());
			qp.setReportOldIn(qc.getReportOldIn());
			qp.setReportOldInMoney(qc.getReportOldInMoney());
			qp.setReportOldMoney(qc.getReportOldMoney());
			qp.setReportOldOut(qc.getReportOldOut());
			qp.setReportOutMoney(qc.getReportOutMoney());
			list.remove(size-1);
			list.add(qp);
		}
	}
	
	//更改sql   形成动态sql
	private void getLibName(String option2, StringBuffer sbf, QueryCriteria qc) {
		//对特定的条件进行拼凑
		String dateFrom = qc.getDateFrom();
		String dateTo = qc.getDateTo();
		String priceFrom = qc.getSearchFrom();
		String priceTo = qc.getSearchTo();
		String cont = qc.getKeyWord();
		//条件筛选值
		String option = qc.getOption();
		switch(option){
		//馆号
		case "0":
			if(priceFrom.trim().length()>0 && priceTo.trim().length()>0){
				sbf.append(" AND lb.hallCode >= '"+priceFrom.trim()+"' AND lb.hallCode <= '"+priceTo.trim()+"' ");
			}else if(priceFrom.trim().length()>0 && priceTo.trim().length()==0){
				sbf.append(" AND lb.hallCode >= '"+priceFrom.trim()+"' ");
			}else if(priceFrom.trim().length()==0 && priceTo.trim().length()>0){
				sbf.append(" AND lb.hallCode <= '"+priceTo.trim()+"' ");
			}
			sbf.append("GROUP BY lb.hallCode )a ON a.hallCode = b.hallCodeT");
			break;
		//馆名
		case "1":
			if(cont.trim().length()>0){
				sbf.append(" AND lb.name LIKE '%"+cont.trim()+"%' ");
			}
			sbf.append("GROUP BY lb.hallCode )a ON a.hallCode = b.hallCodeT");
			break;
		//日期
		case "2":
			if(dateFrom.trim().length()>0 && dateTo.trim().length()>0){
				sbf.append(" AND lc.outDate >= '"+dateFrom+"' AND lc.outDate <= '"+dateTo+"' ");
			}else if(dateFrom.trim().length()>0 && dateTo.trim().length()==0){
				sbf.append(" AND lc.outDate >= '"+dateFrom+"' ");
			}else if(dateFrom.trim().length()==0 && dateTo.trim().length()>0){
				sbf.append(" AND lc.outDate <= '"+dateTo+"' ");
			}
			sbf.append("GROUP BY lb.hallCode )a ON a.hallCode = b.hallCodeT");
			break;
		//当前流出
		case "3":
			sbf.append("GROUP BY lb.hallCode ");
			if(priceFrom.trim().length()>0 && priceTo.trim().length()>0){
				sbf.append(" HAVING nowOut >= '"+priceFrom.trim()+"' AND nowOut <= '"+priceTo.trim()+"' ");
			}else if(priceFrom.trim().length()>0 && priceTo.trim().length()==0){
				sbf.append(" HAVING nowOut >= '"+priceFrom.trim()+"' ");
			}else if(priceFrom.trim().length()==0 && priceTo.trim().length()>0){
				sbf.append(" HAVING nowOut <= '"+priceTo.trim()+"' ");
			}
			sbf.append(" )a ON a.hallCode = b.hallCodeT ");
			break;
		//当前流入	
		case "4":
			sbf.append("GROUP BY lb.hallCode ");
			if(priceFrom.trim().length()>0 && priceTo.trim().length()>0){
				sbf.append(" HAVING nowIn >= '"+priceFrom.trim()+"' AND nowIn <= '"+priceTo.trim()+"' ");
			}else if(priceFrom.trim().length()>0 && priceTo.trim().length()==0){
				sbf.append(" HAVING nowIn >= '"+priceFrom.trim()+"' ");
			}else if(priceFrom.trim().length()==0 && priceTo.trim().length()>0){
				sbf.append(" HAVING nowIn <= '"+priceTo.trim()+"' ");
			}
			sbf.append(" )a ON a.hallCode = b.hallCodeT");
			break;
		//累计流出
		case "5":
			sbf.append("GROUP BY lb.hallCode )a ON a.hallCode = b.hallCodeT ");
			if(priceFrom.trim().length()>0 && priceTo.trim().length()>0){
				sbf.append(" WHERE b.totalNowOut >= '"+priceFrom.trim()+"' AND b.totalNowOut <= '"+priceTo.trim()+"' ");
			}else if(priceFrom.trim().length()>0 && priceTo.trim().length()==0){
				sbf.append(" WHERE b.totalNowOut >= '"+priceFrom.trim()+"' ");
			}else if(priceFrom.trim().length()==0 && priceTo.trim().length()>0){
				sbf.append(" WHERE b.totalNowOut <= '"+priceTo.trim()+"' ");
			}
			break;
		//累计流入	
		case "6":
			sbf.append("GROUP BY lb.hallCode )a ON a.hallCode = b.hallCodeT");
			if(priceFrom.trim().length()>0 && priceTo.trim().length()>0){
				sbf.append(" WHERE b.totalNowIn >= '"+priceFrom.trim()+"' AND b.totalNowIn <= '"+priceTo.trim()+"' ");
			}else if(priceFrom.trim().length()>0 && priceTo.trim().length()==0){
				sbf.append(" WHERE b.totalNowIn >= '"+priceFrom.trim()+"' ");
			}else if(priceFrom.trim().length()==0 && priceTo.trim().length()>0){
				sbf.append(" WHERE b.totalNowIn <= '"+priceTo.trim()+"' ");
			}
			break;
		}
	}
	/**
	    * 
	    * @method:getSum
	    * @Description:getSum    获取默认统计数值
	    * @author: HeTao
	    * @date:2016年7月13日 下午2:49:30
	    * @param:@param string
	    * @param:@param string2
	    * @param:@return
	    * @return:String
	    */
	@Override
	public String getSum() {
		String str = "{'nowOut':0,'nowIn':0,'nowOutMoney':0,'nowInMoney':0,'totalNowOut':0,'totalNowin':0,'totalOutMoney':0,'totalNowInMoney':0}";
		return str;
	}
	private Circulated changeStatus(Circulated ct) {
		ct.setTotalNowin(ct.getNowIn());
		ct.setTotalNowInMoney(ct.getNowInMoney());
		ct.setTotalNowOut(ct.getNowOut());
		ct.setTotalOutMoney(ct.getNowOutMoney());
		return ct;
	}
	
}
