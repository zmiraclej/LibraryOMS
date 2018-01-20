package com.flea.modules.report.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.flea.common.Common;
import com.flea.common.dao.impl.BaseDaoImpl;
import com.flea.common.pojo.Page;
import com.flea.common.pojo.Role;
import com.flea.common.pojo.User;
import com.flea.common.util.ShiroUtils;
import com.flea.modules.report.dao.ReaderReportDao;
import com.flea.modules.report.pojo.CatalogueReport;
import com.flea.modules.report.pojo.ReaderReport;
import com.flea.modules.report.pojo.vo.QueryCriteria;
import com.google.gson.Gson;

/**
 * 读者统计DAO接口
 * @author bruce
 * @version 2016-07-16
 */
@Repository()
public class ReaderReportDaoImpl extends BaseDaoImpl<CatalogueReport,Integer> implements ReaderReportDao{
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
	public Page<CatalogueReport> find(Page<CatalogueReport> page, String area, String lib) {
		//获取当前登录的用户
		User user = ShiroUtils.getCurrentUser();
		Integer id = user.getCustomer().getId();
		String sql = "SELECT lb.hallCode,lr.createDate AS catalogueDate,lr.`name`,lr.borrowCard AS catalogueCode,lr.idCard ,COUNT(lbb.idCard) AS count,SUM(lbk.price) AS price FROM librarys AS lb JOIN library_reader AS lr ON(lb.hallCode=lr.hallCode) JOIN library_borrower_books AS lbb ON(lr.idCard=lbb.idCard) JOIN library_books AS lbk ON(lbb.libraryBookId=lbk.id) WHERE lb.customerId=? AND lb.areaAddress=? AND lb.libraryLevel=? GROUP BY lbb.idCard";
		SQLQuery query = this.getSession().createSQLQuery(sql);
		query.setResultTransformer(Transformers.aliasToBean(CatalogueReport.class));
		query.setInteger(0,id);
		query.setString(1,area);
		query.setString(2,lib);
		List lis = query.list();
		//page.setCount(lis.size());
		query.setFirstResult(page.getFirstResult());
		query.setMaxResults(page.getPageSize());
		//进行查询
		List<CatalogueReport> list = query.list();
		page.setList(list);
		return page;
	}
	/**
	 * 
	 * @method:getQueryInfo		根据条件获取对应记录
	 * @Description:getQueryInfo
	 * @author: HeTao
	 * @date:2016年7月16日 上午9:33:31 
	 * update 2016-12-09 by gouxl
	 * @param:@param qc
	 * @param:@param req
	 * @param:@param resp
	 * @param:@return
	 * @return:String
	 */
	@Override
	public Page<ReaderReport> find(Page<ReaderReport> page, QueryCriteria qc) {
		User user = ShiroUtils.getCurrentUser();
		Role role = user.getRoles().get(0);
		String le = role.getLevel();
		int id = 0;
		boolean flag = false;
		//拼凑SQL语句
		StringBuffer sbf = new StringBuffer();
		
		//判断是否是全部搜索	只能管理员有权利全部搜索
		String area = qc.getArea();
		String lib = qc.getLib();
		String option = qc.getOption();
		String dateFrom = qc.getDateFrom();
		String dateTo = qc.getDateTo();
		String searchFrom = qc.getSearchFrom();
		String searchTo = qc.getSearchTo();
		String cont = qc.getKeyWord();
		//添加条件	,拼接平台与客户查询的sql
		if(Common.ROLE_FIRST_LEVLE.equals(le)){
			flag = true;
			sbf.append("SELECT b.hallCode, b.createDate, b.cardName, b.idCard, b.totalBorrowSum, IFNULL(lpd.platform_deposit, 0) AS deposit  ");
			sbf.append(" FROM borrower b LEFT JOIN librarys lib ON b.hallCode = lib.hallCode LEFT JOIN lib_platform_deposit lpd ON b.idCard = lpd.id_card ");
			sbf.append(" where 1 = 1 AND lib.isEffective=1 ");
			if(!"".equals(qc.getProvince())){
				sbf.append(" AND lib.provinceCode='"+qc.getProvince()+"' ");
			}
			if(!"".equals(qc.getCity())){
				sbf.append(" AND lib.cityCode='"+qc.getCity()+"' ");
			}
			if(!"".equals(qc.getArea())){
				sbf.append(" AND lib.areaCode = '"+area+"' ");
			}
			if ( !"".equals(lib) ) {
				sbf.append(" AND lib.libraryLevel = '"+lib+"' ");
			}
			getLibName(option, sbf, qc);
		} else {
			//判断协议显示读者押金
			int amt = user.getCustomer().getAgreement();
			id = user.getCustomer().getId();
			if (amt == 3) {
				sbf.append(" SELECT b.hallCode, b.createDate, b.idCard, b.cardName, SUM(lau.total_sum) AS totalSum,IFNULL(lcd.user_deposit, 0) AS deposit ");
				sbf.append(" FROM lib_activate_user lau  LEFT JOIN borrower b ON b.idCard = lau.idcard ");
				sbf.append(" LEFT JOIN lib_customer_deposit lcd ON lau.idcard=lcd.id_card ");
				sbf.append(" AND lcd.customer_id = " + id );
				sbf.append(" LEFT JOIN librarys lib ON lau.library_code = lib.hallCode ");
				sbf.append(" WHERE lib.customerId = " + id );
				//押金金额
				if(searchFrom.trim().length() > 0 && searchTo.trim().length() > 0){
					sbf.append(" AND lcd.user_deposit >= "+searchFrom+" AND lcd.user_deposit <= "+searchTo);
				}else if(searchFrom.trim().length()>0 && searchTo.trim().length()==0){
					sbf.append(" AND lcd.user_deposit >= "+searchFrom);
				}else if(searchFrom.trim().length()==0 && searchTo.trim().length()>0){
					sbf.append(" AND lcd.user_deposit <= "+searchTo);
				}
			} else {
				sbf.append(" SELECT b.hallCode, b.createDate, b.idCard, b.cardName, SUM(lau.total_sum) AS totalSum, IFNULL(lpd.platform_deposit, 0) AS deposit ");
				sbf.append(" FROM lib_activate_user lau  LEFT JOIN borrower b ON b.idCard = lau.idcard ");
				sbf.append(" LEFT JOIN lib_platform_deposit lpd ON lau.idcard = lpd.id_card ");
				sbf.append(" LEFT JOIN librarys lib ON lau.library_code = lib.hallCode ");
				sbf.append(" WHERE lib.customerId = " + id );
				//押金金额
				if(searchFrom.trim().length() > 0 && searchTo.trim().length() > 0){
					sbf.append("  AND lpd.platform_deposit >= "+searchFrom+" AND lpd.platform_deposit <= "+searchTo);
				}else if(searchFrom.trim().length() > 0 && searchTo.trim().length() == 0){
					sbf.append(" AND lpd.platform_deposit >= "+searchFrom);
				}else if(searchFrom.trim().length() == 0 && searchTo.trim().length() > 0){
					sbf.append(" AND lpd.platform_deposit <= "+searchTo);
				}
			}
			//地区与馆别
			if(!"".equals(qc.getArea())){
				sbf.append(" AND lib.areaAddress = '"+area+"' ");
			}
			if ( !"".equals(lib) ) {
				sbf.append(" AND lib.libraryLevel = '"+lib+"' ");
			}
			
			//馆号
			if(searchFrom.trim().length() > 0 && searchTo.trim().length() > 0){
				sbf.append(" AND b.hallCode >= '"+searchFrom.trim()+"' AND b.hallCode <= '"+searchTo.trim()+"' ");
			}
			if(searchFrom.trim().length() > 0 && searchTo.trim().length() == 0){
				sbf.append(" AND b.hallCode >= '"+ searchFrom.trim() +"' ");
			}
			if(searchFrom.trim().length() == 0 && searchTo.trim().length() > 0){
				sbf.append(" AND b.hallCode <= '"+ searchTo.trim() +"' ");
			}
			//日期
			if(dateFrom.trim().length() > 0 && dateTo.trim().length() > 0){
				sbf.append(" AND b.createDate >= '"+dateFrom+"' AND b.createDate <= '"+dateTo+"' ");
			}else if(dateFrom.trim().length()>0 && dateTo.trim().length()==0){
				sbf.append(" AND b.createDate >= '"+dateFrom+"' ");
			}else if(dateFrom.trim().length()==0 && dateTo.trim().length()>0){
				sbf.append(" AND b.createDate <= '"+dateTo+"' ");
			}
			//读者姓名
			if(cont.trim().length() > 0){
				sbf.append(" AND b.cardName LIKE '%"+cont.trim()+"%' ");
			}
			//身份证号
			if(cont.trim().length() > 0){
				sbf.append(" AND b.idCard = '"+cont.trim()+"' ");
			}
			//借阅次数
			if(searchFrom.trim().length() > 0 && searchTo.trim().length() > 0){
				sbf.append(" AND lau.total_sum >= "+searchFrom+" AND lau.total_sum <= "+searchTo);
			}else if(searchFrom.trim().length() > 0 && searchTo.trim().length() == 0 ){
				sbf.append(" AND lau.total_sum >= "+searchFrom );
			}else if(searchFrom.trim().length() == 0 && searchTo.trim().length() > 0 ){
				sbf.append(" AND lau.total_sum <= "+searchTo);
			}
//			if("".equals(area) && "".equals(lib)){
//				sbf.append(" AND lb.customerId=? ");
//			}else{
//				sbf.append(" AND lb.customerId=? ");
//			}
			
			
			sbf.append(" AND lib.isEffective=1 GROUP BY lau.idcard ");
		}
		
		
		StringBuffer sumSql = sbf;
		//连接好所有的查询条件并创建sql语句
		String sql = sbf.toString();
		SQLQuery query = this.getSession().createSQLQuery(sql);
		//对应的实体类
		query.setResultTransformer( Transformers.aliasToBean(ReaderReport.class));
		
		//设置参数 -----------------------------------------------------------
//		if(!flag){
//			query.setInteger(0,id);
//		}
		if(page==null){
			page = new Page<ReaderReport>();
			//进行查询
			List<ReaderReport> list = query.list();
			//设置集合总数到最后一个对象里面
			setSumInLastObject(list,sumSql,id,flag);
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
		List<ReaderReport> list = query.list();
		//设置集合总数到最后一个对象里面
		setSumInLastObject(list,sumSql,id,flag);
		page.setList(list);
		//返回
		return page;
	}
	
	//统计
	private void setSumInLastObject(List<ReaderReport> list, StringBuffer sumSql, int id, boolean isAdmin) {
		sumSql.insert(0,"SELECT count(*) as sumNumber,sum(b.deposit) FROM ( ");
		String str = sumSql.toString();
		str += ") AS b ";
//		str = str.replace("a.*","a.count");
		SQLQuery query = this.getSession().createSQLQuery(str);
//		if(!isAdmin){
//			query.setInteger(0,id);
//		}
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
	 * update 2016-12-09 by gouxl
	 * @param:@param option
	 * @param:@param sbf
	 * @param:@param qc
	 * @return:void
	 */
	private void getLibName(String option, StringBuffer sbf, QueryCriteria qc) {
		//对特定的条件进行拼凑
		String dateFrom = qc.getDateFrom();
		String dateTo = qc.getDateTo();
		String searchFrom = qc.getSearchFrom();
		String searchTo = qc.getSearchTo();
		String cont = qc.getKeyWord();
		switch (option) {
		//馆号
		case "0":
			if(searchFrom.trim().length() > 0 && searchTo.trim().length() > 0){
				sbf.append(" AND b.hallCode >= '"+searchFrom.trim()+"' AND b.hallCode <= '"+searchTo.trim()+"' ");
			}
			if(searchFrom.trim().length() > 0 && searchTo.trim().length() == 0){
				sbf.append(" AND b.hallCode >= '"+ searchFrom.trim() +"' ");
			}
			if(searchFrom.trim().length() == 0 && searchTo.trim().length() > 0){
				sbf.append(" AND b.hallCode <= '"+ searchTo.trim() +"' ");
			}
			break;
		//日期
		case "1":
			if(dateFrom.trim().length() > 0 && dateTo.trim().length() > 0){
				sbf.append(" AND b.createDate >= '"+dateFrom+"' AND b.createDate <= '"+dateTo+"' ");
			}else if(dateFrom.trim().length()>0 && dateTo.trim().length()==0){
				sbf.append(" AND b.createDate >= '"+dateFrom+"' ");
			}else if(dateFrom.trim().length()==0 && dateTo.trim().length()>0){
				sbf.append(" AND b.createDate <= '"+dateTo+"' ");
			}
			break;
		//读者姓名
		case "2":
			if(cont.trim().length() > 0){
				sbf.append(" AND b.cardName LIKE '%"+cont.trim()+"%' ");
			}
			break;
		//借阅证号
//		case "3":
//			break;
		//身份证号
		case "4":
			if(cont.trim().length() > 0){
				sbf.append(" AND b.idCard = '"+cont.trim()+"' ");
			}
			break;
		//借阅次数
		case "6":
			if(searchFrom.trim().length() > 0 && searchTo.trim().length() > 0){
				sbf.append(" AND b.totalBorrowSum >= "+searchFrom+" AND b.totalBorrowSum <= "+searchTo);
			}else if(searchFrom.trim().length() > 0 && searchTo.trim().length() == 0 ){
				sbf.append(" AND b.totalBorrowSum >= "+searchFrom );
			}else if(searchFrom.trim().length()==0 && searchTo.trim().length() > 0 ){
				sbf.append(" AND b.totalBorrowSum <= "+searchTo);
			}
			break;
		//押金金额
		case "7":
			if(searchFrom.trim().length() > 0 && searchTo.trim().length() > 0){
				sbf.append("  AND lpd.platform_deposit >= "+searchFrom+" AND lpd.platform_deposit <= "+searchTo);
			}else if(searchFrom.trim().length()>0 && searchTo.trim().length()==0){
				sbf.append(" AND lpd.platform_deposit >= "+searchFrom);
			}else if(searchFrom.trim().length()==0 && searchTo.trim().length()>0){
				sbf.append(" AND lpd.platform_deposit <= "+searchTo);
			}
			break;
		}
		
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
		User user = ShiroUtils.getCurrentUser();
		Role role = user.getRoles().get(0);
		String le = role.getLevel();
		int id=0;
		boolean flag = false;
		//GROUP BY lb.customerId
		String str = "SELECT COUNT(lbb.idCard) AS count,SUM(lbk.price) AS price FROM librarys AS lb JOIN library_reader AS lr ON(lb.hallCode=lr.hallCode) JOIN library_borrower_books AS lbb ON(lr.idCard=lbb.idCard) JOIN library_books AS lbk ON(lbb.libraryBookId=lbk.id) WHERE lb.customerId=? AND lb.areaAddress=? AND lb.libraryLevel=?";
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
