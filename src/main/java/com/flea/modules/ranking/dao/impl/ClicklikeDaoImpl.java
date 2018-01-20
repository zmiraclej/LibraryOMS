package com.flea.modules.ranking.dao.impl;

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
import com.flea.modules.ranking.dao.ClicklikeDao;
import com.flea.modules.ranking.pojo.Clicklike;
import com.flea.modules.ranking.pojo.vo.ClicklikeVo;
import com.flea.modules.report.pojo.CatalogueReport;
import com.flea.modules.report.pojo.vo.QueryCriteria;
import com.google.gson.Gson;

/**
 * 点赞排行DAO接口
 * @author bruce
 * @version 2016-07-20
 */
@Repository
public class ClicklikeDaoImpl extends BaseDaoImpl<Clicklike,Integer> implements ClicklikeDao{

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
	public Page<Clicklike> find(Page<Clicklike> page, String area, String lib) {
		//获取当前登录的用户
		User user = ShiroUtils.getCurrentUser();
		Integer id = user.getCustomer().getId();
		String sql = "SELECT t.* FROM ( SELECT CONVERT((@rowNum\\:=@rowNum+1.0),signed) AS ranking,a.* FROM (Select (@rowNum \\:=0) ) AS s,(SELECT lbk.isbn,lbk.properTitle AS bookName,lbk.press,lbk.price,lbk.author,DATE_FORMAT(lbb.borrowDate,'%Y')AS date,lbk.classificationNumber AS typeNumber,COUNT(lbb.isPraiseD) AS click FROM librarys AS lb JOIN library_borrower_books AS lbb ON(lb.hallCode=lbb.hallCode) JOIN library_books AS lbk ON(lbb.libraryBookId=lbk.id) WHERE lb.isEffective = 1 and lb.customerId=? AND lb.areaAddress=?  AND lb.libraryLevel=?  GROUP BY lbk.isbn ORDER BY COUNT(lbb.isPraiseD) DESC) AS a)t ";
		SQLQuery query = this.getSession().createSQLQuery(sql);
		query.setResultTransformer(Transformers.aliasToBean(Clicklike.class));
		query.setInteger(0,id);
		query.setString(1,area);
		query.setString(2,lib);
		List lis = query.list();
		page.setCount(lis.size());
		query.setFirstResult(page.getFirstResult());
		query.setMaxResults(page.getPageSize());
		//进行查询
		List<Clicklike> list = query.list();
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
	public Page<Clicklike> find(Page<Clicklike> page, QueryCriteria qc) {
		User user = ShiroUtils.getCurrentUser();
		Role role = user.getRoles().get(0);
		String le = role.getLevel();
		int id=0;
		boolean flag = false;
		//拼凑SQL语句
		StringBuffer sbf = new StringBuffer();
		sbf.append("SELECT t.* FROM ( SELECT CONVERT((@rowNum\\:=@rowNum+1.0),signed) AS ranking,a.*,a.click AS clickt FROM (Select (@rowNum \\:=0) ) AS s,(SELECT lbk.isbn,lbk.properTitle AS bookName,lbk.press,lbk.price,lbk.author,DATE_FORMAT(lbb.borrowDate,'%Y')AS date,lbk.classificationNumber AS typeNumber,COUNT(lbb.isPraiseD) AS click FROM librarys AS lb JOIN library_borrower_books AS lbb ON(lb.hallCode=lbb.hallCode) JOIN library_books AS lbk ON(lbb.libraryBookId=lbk.id)  ");
		
		//判断是否是全部搜索	只能管理员有权利全部搜索
		String area = qc.getArea();
		String lib = qc.getLib();
		sbf.append("WHERE isPraiseD IS not NULL and lb.isEffective = 1 ");
		//当搜索地区区域为空时：
		if(!"".equals(area)){
			sbf.append(" and  lb.areaAddress LIKE '%"+area+"%' ");
			sbf.append(" AND lb.libraryLevel LIKE '%"+lib+"%' ");
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
		query.setResultTransformer(Transformers.aliasToBean(Clicklike.class));
		//设置参数 -----------------------------------------------------------
		if(!flag){
			query.setInteger(0,id);
		}
		if(page==null){
			page = new Page<Clicklike>();
			//进行查询
			List<Clicklike> list = query.list();
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
		List<Clicklike> list = query.list();
		//设置集合总数到最后一个对象里面
		setSumInLastObject(list,sumSql,id,flag);
		page.setList(list);
		//返回
		return page;
	}

	private void setSumInLastObject(List<Clicklike> list, StringBuffer sumSql, int id, boolean isAdmin) {
		String str = sumSql.toString();
		//str += ") as a";
		//str = str.replace("CONVERT((@rowNum\\:=@rowNum+1.0),signed) AS ranking,a.*","COUNT(a.isbn),SUM(a.click)");
		str = str.replace("t.*"," COUNT(t.ranking),SUM(t.clickt) ");
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
	private void getLibName(String option, StringBuffer sbf, QueryCriteria qc) {
		//对特定的条件进行拼凑
		String dateFrom = qc.getDateFrom();
		String dateTo = qc.getDateTo();
		String priceFrom = qc.getSearchFrom();
		String priceTo = qc.getSearchTo();
		String cont = qc.getKeyWord();
		switch (option) {
		//馆号
		case "9":
			if(priceFrom.trim().length()>0 && priceTo.trim().length()>0){
				sbf.append(" AND lb.hallCode >= '"+priceFrom.trim()+"' AND lb.hallCode <= '"+priceTo.trim()+"' ");
			}else if(priceFrom.trim().length()>0 && priceTo.trim().length()==0){
				sbf.append(" AND lb.hallCode >= '"+priceFrom.trim()+"' ");
			}else if(priceFrom.trim().length()==0 && priceTo.trim().length()>0){
				sbf.append(" AND lb.hallCode <= '"+priceTo.trim()+"' ");
			}
			sbf.append(" GROUP BY lbk.isbn ORDER BY COUNT(lbb.isPraiseD) DESC) AS a)t ");
			break;
		//名次 
		case "0":
			sbf.append(" GROUP BY lbk.isbn ORDER BY COUNT(lbb.isPraiseD) DESC) AS a)t ");
			if(priceFrom.trim().length()>0 && priceTo.trim().length()>0){
				sbf.append(" WHERE t.ranking >= "+priceFrom.trim()+" AND t.ranking <= "+priceTo.trim()+" ");
			}else if(priceFrom.trim().length()>0 && priceTo.trim().length()==0){
				sbf.append(" WHERE t.ranking >= "+priceFrom.trim()+" ");
			}else if(priceFrom.trim().length()==0 && priceTo.trim().length()>0){
				sbf.append(" WHERE t.ranking <= "+priceTo.trim()+" ");
			}
			break;
		//ISBN
		case "1":
			if(priceFrom.trim().length()>0 && priceTo.trim().length()>0){
				sbf.append(" AND lbk.isbn >= '"+priceFrom.trim()+"' AND lbk.isbn <= '"+priceTo.trim()+"' ");
			}else if(priceFrom.trim().length()>0 && priceTo.trim().length()==0){
				sbf.append(" AND lbk.isbn >= '"+priceFrom.trim()+"' ");
			}else if(priceFrom.trim().length()==0 && priceTo.trim().length()>0){
				sbf.append(" AND lbk.isbn <= '"+priceTo.trim()+"' ");
			}
			sbf.append(" GROUP BY lbk.isbn ORDER BY COUNT(lbb.isPraiseD) DESC) AS a)t ");
			break;
		//书名  one
		case "2":
			if(cont.trim().length()>0){
				sbf.append(" AND lbk.properTitle LIKE '%"+cont.trim()+"%' ");
			}
			sbf.append(" GROUP BY lbk.isbn ORDER BY COUNT(lbb.isPraiseD) DESC) AS a)t ");
			break;
		//出版社 one
		case "3":
			if(cont.trim().length()>0){
				sbf.append(" AND lbk.press LIKE '%"+cont.trim()+"%' ");
			}
			sbf.append(" GROUP BY lbk.isbn ORDER BY COUNT(lbb.isPraiseD) DESC) AS a)t ");
			break;
		//定价
		case "4":
			if(priceFrom.trim().length()>0 && priceTo.trim().length()>0){
				sbf.append(" AND lbk.price >= "+priceFrom.trim()+" AND lbk.price <= "+priceTo.trim()+" ");
			}else if(priceFrom.trim().length()>0 && priceTo.trim().length()==0){
				sbf.append(" AND lbk.price >= "+priceFrom.trim()+" ");
			}else if(priceFrom.trim().length()==0 && priceTo.trim().length()>0){
				sbf.append(" AND lbk.price <= "+priceTo.trim()+" ");
			}
			sbf.append(" GROUP BY lbk.isbn ORDER BY COUNT(lbb.isPraiseD) DESC) AS a)t ");
			break;
		//著者 one
		case "5":
			if(cont.trim().length()>0){
				sbf.append(" AND lbk.author LIKE '%"+cont.trim()+"%' ");
			}
			sbf.append(" GROUP BY lbk.isbn ORDER BY COUNT(lbb.isPraiseD) DESC) AS a)t ");
			break;
		//年份 
		case "6":
			if(priceFrom.trim().length()>0 && priceTo.trim().length()>0){
				sbf.append(" AND DATE_FORMAT(lbb.borrowDate,'%Y') >= '"+priceFrom.trim()+"' AND DATE_FORMAT(lbb.borrowDate,'%Y') <= '"+priceTo.trim()+"' ");
			}else if(priceFrom.trim().length()>0 && priceTo.trim().length()==0){
				sbf.append(" AND DATE_FORMAT(lbb.borrowDate,'%Y') >= '"+priceFrom.trim()+"' ");
			}else if(priceFrom.trim().length()==0 && priceTo.trim().length()>0){
				sbf.append(" AND DATE_FORMAT(lbb.borrowDate,'%Y') <= '"+priceTo.trim()+"' ");
			}
			sbf.append(" GROUP BY lbk.isbn ORDER BY COUNT(lbb.isPraiseD) DESC) AS a)t ");
			break;
		//分类号 
		case "7":
			if(priceFrom.trim().length()>0 && priceTo.trim().length()>0){
				sbf.append(" AND lbk.classificationNumber >= '"+priceFrom.trim()+"' AND lbk.classificationNumber <= '"+priceTo.trim()+"' ");
			}else if(priceFrom.trim().length()>0 && priceTo.trim().length()==0){
				sbf.append(" AND lbk.classificationNumber >= '"+priceFrom.trim()+"' ");
			}else if(priceFrom.trim().length()==0 && priceTo.trim().length()>0){
				sbf.append(" AND lbk.classificationNumber <= '"+priceTo.trim()+"' ");
			}
			sbf.append(" GROUP BY lbk.isbn ORDER BY COUNT(lbb.isPraiseD) DESC) AS a)t ");
			break;
		//赞数
		case "8":
			sbf.append(" GROUP BY lbk.isbn ");
			if(priceFrom.trim().length()>0 && priceTo.trim().length()>0){
				sbf.append(" HAVING COUNT(lbb.isPraiseD) >= "+priceFrom.trim()+" AND COUNT(lbb.isPraiseD) <= "+priceTo.trim()+" ");
			}else if(priceFrom.trim().length()>0 && priceTo.trim().length()==0){
				sbf.append(" HAVING COUNT(lbb.isPraiseD) >= "+priceFrom.trim()+" ");
			}else if(priceFrom.trim().length()==0 && priceTo.trim().length()>0){
				sbf.append(" HAVING COUNT(lbb.isPraiseD) <= "+priceTo.trim()+" ");
			}
			sbf.append(" ORDER BY COUNT(lbb.isPraiseD) DESC) AS a)t ");
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
	public String getSum(String string, String string2) {
		User user = ShiroUtils.getCurrentUser();
		Role role = user.getRoles().get(0);
		String le = role.getLevel();
		int id=0;
		boolean flag = false;
		//GROUP BY lb.customerId
		String str = "SELECT COUNT(a.isbn),SUM(a.click) FROM (Select (@rowNum \\:=0) ) AS s,(SELECT lbk.isbn,lbk.properTitle AS bookName,lbk.press,lbk.price,lbk.author,DATE_FORMAT(lbb.borrowDate,'%Y')AS date,lbk.classificationNumber AS typeNumber,COUNT(lbb.isPraiseD) AS click FROM librarys AS lb JOIN library_borrower_books AS lbb ON(lb.hallCode=lbb.hallCode) JOIN library_books AS lbk ON(lbb.libraryBookId=lbk.id) WHERE lb.isEffective = 1 and lb.customerId=? AND lb.areaAddress=? AND lb.libraryLevel=? GROUP BY lbk.isbn ORDER BY COUNT(lbb.isPraiseD) DESC) AS a";
		if(Common.ROLE_FIRST_LEVLE.equals(le)){
			str = str.replace("lb.customerId=? AND ","");
			flag = true;
			return "00-00";
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
			list.add("00-00");
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
