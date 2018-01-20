package com.flea.modules.report.dao.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import com.flea.common.dao.impl.BaseDaoImpl;
import com.flea.common.pojo.Page;
import com.flea.modules.report.dao.BorrowerReportDao;
import com.flea.modules.report.pojo.BorrowerReport;
import com.flea.modules.report.pojo.ReportTotal;
import com.flea.modules.report.pojo.SearchResult;
import com.flea.modules.report.pojo.vo.QueryCriteria;


/**
 * 借书统计DAO接口
 * @author bruce
 * @version 2016-07-14
 */	
@Repository
public class BorrowerReportDaoImpl extends BaseDaoImpl<BorrowerReport,Integer> implements BorrowerReportDao{

	/* (non-Javadoc)
	 * @see com.flea.modules.report.dao.BorrowerReportDao#findBorrowerBookList(com.flea.common.pojo.Page, com.flea.modules.report.pojo.vo.QueryCriteria)
	 */
	@Override
	public SearchResult<BorrowerReport> findBorrowerBookList(
			Page<BorrowerReport> page, QueryCriteria param) {
		SearchResult<BorrowerReport> result = new SearchResult<BorrowerReport>();
		String sqlTotal = "SELECT SUM(b.totalBook) as sumBook,SUM(b.totalPrice) as sumPrice,count(b.hallCode) as sumCount from (";
		String countString ="select count(*) from ( ";
		String frontSQL = "SELECT c.hallCode,c.`name`,c.borrowDate,c.borrowNumber, c.totalBook,c.totalPrice,c.userName  from ( ";
		String sqlString =frontSQL+ "SELECT  a.hallCode,a.`name`,a.borrowDate,a.borrowNumber,COUNT(a.id) totalBook,SUM(a.price) totalPrice,a.userName from";
		StringBuffer buffer = new StringBuffer();
		buffer.append(" (SELECT b.id,b.borrowDate,b.borrowNumber,lb.price,l.hallCode,l.`name`,u.userName from library_borrower_books b ");
		buffer.append(" LEFT JOIN library_books lb on b.libraryBookId=lb.id ");
		buffer.append("  LEFT JOIN librarys_users u ON b.operUser=u.id ");
		buffer.append(" LEFT JOIN librarys l on   lb.stayLibraryHallCode = l.hallCode  where l.isEffective =1 ");
		if(param.getCustomerId()!=null){
			buffer.append(" and  l.customerId="+param.getCustomerId());
		}
		if(StringUtils.isNotBlank(param.getLib())){
			buffer.append(" and  l.libraryLevel='"+param.getLib()+"'");
		}
		if(StringUtils.isNotBlank(param.getProvince())){
			buffer.append(" and l.provinceCode='"+param.getProvince()+"' ");
		}
		if(StringUtils.isNotBlank(param.getCity())){
			buffer.append(" and l.cityCode='"+param.getCity()+"' ");
		}
		if(StringUtils.isNotBlank(param.getArea())){
			if(param.getCustomerId()==null){ //一级用户
				buffer.append(" and l.areaCode='"+param.getArea()+"' ");
			}else {//二级
				buffer.append(" and l.areaAddress='"+param.getArea()+"' ");
			}
		}
		if("7".equals(param.getOption())){
			if(StringUtils.isNotBlank(param.getSearchFrom())){
				buffer.append(" and l.hallCode>='"+param.getSearchFrom()+"' ");
			}
			if(StringUtils.isNotBlank(param.getSearchTo())){
				buffer.append(" and l.hallCode<='"+param.getSearchTo()+"' ");
			}
		}
	
		String fieldString ="  and";
		if("1".equals(param.getOption())&&StringUtils.isNotBlank(param.getKeyWord())){
			buffer.append(" and l.`name` like '%"+param.getKeyWord()+"%'");
		}else if("2".equals(param.getOption())){
			fieldString +=" b.borrowDate";
		}else if("3".equals(param.getOption())){
			fieldString +=" b.borrowNumber";
		}
		if(StringUtils.isNotBlank(param.getOption())&&Integer.parseInt(param.getOption())<4){
			if(StringUtils.isNotBlank(param.getSearchFrom())){
				buffer.append(fieldString +">='"+param.getSearchFrom()+"'");
			}
			if(StringUtils.isNotBlank(param.getSearchTo())){
				buffer.append(fieldString +"<='"+param.getSearchTo()+"'");
			}
		}
		buffer.append(" ) as a  where 1=1 ");
	
		if("6".equals(param.getOption())){
				buffer.append(" and  a.userName ='"+param.getKeyWord()+"'");
		}
		buffer.append(" GROUP BY  a.borrowNumber ) as c where 1=1");
		if("4".equals(param.getOption())){
			fieldString +=" c.totalBook";
			 if(StringUtils.isNotBlank(param.getSearchFrom())){
					buffer.append(fieldString +">="+param.getSearchFrom());
			 }
			if(StringUtils.isNotBlank(param.getSearchTo())){
				buffer.append(fieldString +"<="+param.getSearchTo());
			}
		}else if("5".equals(param.getOption())){
			fieldString +=" c.totalPrice";
			 if(StringUtils.isNotBlank(param.getSearchFrom())){
					buffer.append(fieldString +">="+param.getSearchFrom());
			 }
			if(StringUtils.isNotBlank(param.getSearchTo())){
				buffer.append(fieldString +"<="+param.getSearchTo());
			}
		}
		
		//合计
		SQLQuery query = getSession().createSQLQuery(sqlTotal+sqlString+buffer.toString()+" ) as b");
		query.setResultTransformer(new AliasToBeanResultTransformer(ReportTotal.class));
		ReportTotal total =(ReportTotal) query.uniqueResult();
		result.setTotal(total);
		//分页总数
		query = getSession().createSQLQuery(countString+sqlString+buffer.toString()+" ) as b");
		int count = ((BigInteger)query.uniqueResult()).intValue();
		result.setCount(count);
		page.setCount(count);
		//page = new Page(page.getPageNo(),page.getPageSize(),count);
		//列表
		query = getSession().createSQLQuery(sqlString+buffer.toString());
		System.out.println("first--"+page.getFirstResult());
		query.setFirstResult(page.getFirstResult());
		query.setMaxResults(page.getMaxResults());
		query.setResultTransformer(new AliasToBeanResultTransformer(BorrowerReport.class));
		List<BorrowerReport> list = query.list();
	
		result.setResult(list);
		return result;
	}

	/* (non-Javadoc)
	 * @see com.flea.modules.report.dao.BorrowerReportDao#findBorrowerDataList(com.flea.modules.report.pojo.vo.QueryCriteria)
	 */
	@Override
	public SearchResult<BorrowerReport> findBorrowerDataList(QueryCriteria param) {
		SearchResult<BorrowerReport> result = new SearchResult<BorrowerReport>();
		String frontSQL = "SELECT d.category,d.totalBook,d.totalPrice,d.percent  FROM ( ";
		String sqlTotal = "SELECT SUM(d.totalIsbn) as sumIsbn,SUM(d.totalBook) as sumBook,SUM(d.totalPrice) as sumPrice from (";//合计
		String sql =" SELECT concat(a.categoryCode,' ',a.categoryName) category,COUNT(a.categoryCode) totalBook,COUNT(DISTINCT a.isbn) totalIsbn,SUM(a.price) totalPrice,(1/100) as percent FROM ";
		StringBuffer buffer = new StringBuffer();
		buffer.append(" (SELECT  SUBSTRING(lb.classificationNumber,1,1) categoryCode,c.categoryName,lb.isbn,lb.price FROM library_borrower_books b ");
		buffer.append(" LEFT JOIN library_books lb on b.libraryBookId=lb.id ");
		buffer.append(" LEFT JOIN  categorys c ON categoryCode= c.categoryCode ");
		buffer.append("LEFT JOIN librarys l ON lb.stayLibraryHallCode=l.hallCode  where l.isEffective =1");
		if(param.getCustomerId()!=null){
			buffer.append(" and  l.customerId="+param.getCustomerId());
		}
		if(StringUtils.isNotBlank(param.getLib())){
			buffer.append(" and  l.libraryLevel='"+param.getLib()+"'");
		}
		if(StringUtils.isNotBlank(param.getProvince())){
			buffer.append(" and l.provinceCode='"+param.getProvince()+"' ");
		}
		if(StringUtils.isNotBlank(param.getCity())){
			buffer.append(" and l.cityCode='"+param.getCity()+"' ");
		}
		if(StringUtils.isNotBlank(param.getArea())){
			if(param.getCustomerId()==null){ //一级用户
				buffer.append(" and l.areaCode='"+param.getArea()+"' ");
			}else {//二级
				buffer.append(" and l.areaAddress='"+param.getArea()+"' ");
			}
		}
		if("7".equals(param.getOption())){
			if(StringUtils.isNotBlank(param.getSearchFrom())){
				buffer.append(" and l.hallCode>='"+param.getSearchFrom()+"' ");
			}
			if(StringUtils.isNotBlank(param.getSearchTo())){
				buffer.append(" and l.hallCode<='"+param.getSearchTo()+"' ");
			}
		}
		buffer.append(" )  as a  GROUP BY a.categoryCode ) as d  where 1=1");
		
		String fieldString ="  and";
		if("4".equals(param.getOption())){
			fieldString +=" d.totalBook";
			 if(StringUtils.isNotBlank(param.getSearchFrom())){
					buffer.append(fieldString +">="+param.getSearchFrom());
			 }
			if(StringUtils.isNotBlank(param.getSearchTo())){
				buffer.append(fieldString +"<="+param.getSearchTo());
			}
		}else if("5".equals(param.getOption())){
			fieldString +=" d.totalPrice";
			 if(StringUtils.isNotBlank(param.getSearchFrom())){
					buffer.append(fieldString +">="+param.getSearchFrom());
			 }
			if(StringUtils.isNotBlank(param.getSearchTo())){
				buffer.append(fieldString +"<="+param.getSearchTo());
			}
		}
		
		SQLQuery query = getSession().createSQLQuery("select  sum(e.totalBook)  from ("+frontSQL+sql+buffer.toString()+ " ) as e");
		Object  totalCount  =query.uniqueResult();
		int total;//查询借阅总数，算百分比
		if(totalCount!=null){
			total =((BigDecimal)totalCount).intValue();//查询借阅总数，算百分比
		}else{
			total =0;
		}
		String sqlString = "SELECT concat(a.categoryCode,' ',a.categoryName) category,COUNT(a.categoryCode) totalBook,COUNT(DISTINCT a.isbn) totalIsbn,SUM(a.price) totalPrice,(COUNT(a.categoryCode)/"+total+")*100 as percent FROM";
		 query =  getSession().createSQLQuery(sqlTotal+sqlString+buffer.toString());
		query.setResultTransformer(new AliasToBeanResultTransformer(ReportTotal.class));
		ReportTotal reportTotal =(ReportTotal) query.uniqueResult();//查询合计
		result.setTotal(reportTotal);
		
	    query = getSession().createSQLQuery(frontSQL+sqlString+buffer.toString());
		query.setResultTransformer(new AliasToBeanResultTransformer(BorrowerReport.class));
		List<BorrowerReport> list = query.list();//记录
		result.setResult(list);
		return result;
	}
	
}
