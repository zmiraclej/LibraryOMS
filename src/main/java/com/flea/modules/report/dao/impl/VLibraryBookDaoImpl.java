package com.flea.modules.report.dao.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import com.flea.common.Common;
import com.flea.common.dao.impl.BaseDaoImpl;
import com.flea.common.pojo.Page;
import com.flea.common.pojo.Role;
import com.flea.common.pojo.User;
import com.flea.common.util.ShiroUtils;
import com.flea.modules.report.dao.VLibraryBookDao;
import com.flea.modules.report.pojo.ReportTotal;
import com.flea.modules.report.pojo.SearchResult;
import com.flea.modules.report.pojo.VLibraryBook;
import com.flea.modules.report.pojo.vo.QueryCriteria;
import com.google.gson.Gson;

/**
 * 藏书统计DAO接口
 * 
 * @author bruce
 * @version 2016-07-09
 */
@Repository
public class VLibraryBookDaoImpl extends BaseDaoImpl<VLibraryBook, Integer> implements VLibraryBookDao {

	/**
	 * 
	 * @method:showTopSelect
	 * @Description:showTopSelect 查找登录者对应的地区、管别、馆号
	 * @author: HeTao
	 * @date:2016年7月9日 上午10:36:33
	 * @param:@return
	 * @return:Map<String,List<String>>
	 */
	@Override
	public Map<String, List<String>> showTopSelect(String area, String lib) {
		// 获取当前登录的用户
		User user = ShiroUtils.getCurrentUser();
		Role role = user.getRoles().get(0);
		String le = role.getLevel();
		int id;

		if (Common.ROLE_FIRST_LEVLE.equals(le)) {
			// 获取权限用户
			return findAdminDefault(area, lib);
		} else {
			id = user.getCustomer().getId();
			// 获取普通用户第一条默认的信息
			return findGeneralDefault(id, area, lib);
		}
	}

	private Map<String, List<String>> findAdminDefault(String are, String li) {
		Map<String, List<String>> map = null;
		map = new HashMap<String, List<String>>();
		List<String> area = new ArrayList<String>();
		List<String> lib = new ArrayList<String>();
		List<String> hall = new ArrayList<String>();
		List<String> sum = new ArrayList<String>();
		// 两个都为空的话，表示超级用户点击统计进入该页面，但是该页面第一次展示我们不提供任何数据，当选择条件的时候，开始展示对应
		if (are == null && li == null) {
			sum.add("0-0");
			// 装进去
			map.put("area", area);
			map.put("lib", lib);
			map.put("hall", hall);
			map.put("sum", sum);
			return map;
		} else {
			sum.add("0-0");
			lib = getLib(0, are, true);
			if (are != null && li != null) {
				hall = getHall(0, are, li, true);
			} else if (lib.size() != 0) {
				hall = getHall(0, are, lib.get(0), true);
			}
			map.put("lib", lib);
			map.put("hall", hall);
			map.put("sum", sum);
			return map;
		}

	}

	private Map<String, List<String>> findGeneralDefault(int id, String userArea, String userLib) {
		boolean flag = false;
		Map<String, List<String>> map = null;
		map = new HashMap<String, List<String>>();
		// 得到区域
		List<String> area = getArea(id, false);
		// 默认 两个参数都没有的时候
		if (userArea == null && userLib == null) {
			if (area.size() == 0) {
				userArea = "";
				area.add("");
			} else {
				userArea = area.get(0);
			}
			flag = true;
		}
		// 得到馆别
		List<String> lib = getLib(id, userArea, false);
		// 当没有参数的时候 或者只有区域值得时候
		if (flag || userLib == null) {
			if (lib.size() == 0) {
				userLib = "";
				lib.add("");
			} else {
				userLib = lib.get(0);
			}
		}
		List<String> hall = getHall(id, userArea, userLib, false);
		// 这个统计只适合编目统计使用！！！！！不能通用
		if (lib != null) {
			// 统计
			List<String> sum = sumAll(id, userArea, lib.get(0).split("-")[0]);
			map.put("sum", sum);
		} else {
			map.put("sum", new ArrayList<String>());
		}
		// 装进去
		map.put("area", area);
		map.put("lib", lib);
		map.put("hall", hall);
		return map;
	}

	private List<String> sumAll(int id, String userArea, String userLib) {
		// 默认一个馆别
		String sql = "SELECT COUNT(lbs.catalogueCode),SUM(lbs.price) FROM librarys AS lb JOIN library_books AS lbs ON(lb.hallCode=lbs.belongLibraryHallCode) JOIN librarys_users AS lu ON(lbs.operUser=lu.id) WHERE lb.customerId=? AND lb.areaAddress=? AND lb.libraryLevel=? GROUP BY lb.customerId";
		SQLQuery query = this.getSession().createSQLQuery(sql);
		query.setInteger(0, id);
		query.setString(1, userArea);
		query.setString(2, userLib);
		List<String> list = query.list();
		if (list.size() == 0) {
			list.add("0-0");
			return list;
		}
		list = changeStatus(list);
		return list;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	private List<String> getHall(int id, String userArea, String userLib, boolean isAll) {
		String sqlHallName = "SELECT hallCode,name FROM librarys WHERE customerId=? AND areaAddress=? AND libraryLevel=?";
		// 如果是一级用户 就不用id去查找
		if (isAll) {
			sqlHallName = sqlHallName.replace("customerId=? AND", "");
		}
		// 获取当前用户某个区域的某个馆别的某个图书馆
		SQLQuery queryHallName = this.getSession().createSQLQuery(sqlHallName);
		if (!isAll) {
			queryHallName.setInteger(0, id);
			queryHallName.setString(1, userArea);
			queryHallName.setString(2, userLib);
		} else {
			queryHallName.setString(0, userArea);
			queryHallName.setString(1, userLib);
		}
		List<String> list = queryHallName.list();
		list = changeStatus(list);
		return list;
	}

	@SuppressWarnings("unchecked")
	private List<String> getLib(int id, String userArea, boolean isAll) {
		String sqlLibType = "SELECT libraryLevel FROM librarys WHERE customerId=? AND areaAddress=? GROUP BY libraryLevel";
		// 如果是一级用户 就不用id去查找
		if (isAll) {
			sqlLibType = sqlLibType.replace("customerId=? AND", "");
		}
		// 获取当前用户某个区域的馆别
		SQLQuery queryLevel = this.getSession().createSQLQuery(sqlLibType);
		if (!isAll) {
			queryLevel.setInteger(0, id);
			queryLevel.setString(1, userArea);
		} else {
			queryLevel.setString(0, userArea);
		}
		List<String> list = queryLevel.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	private List<String> getArea(int id, boolean isAll) {
		String sqlArea = "SELECT areaAddress FROM librarys WHERE isEffective = 1 and customerId=? GROUP BY areaAddress";
		// 如果是一级用户 就不用id去查找
		if (isAll) {
			sqlArea = sqlArea.replace("customerId=?", "");
		}
		// 获取当前用户有哪些主管区域
		SQLQuery queryAddress = this.getSession().createSQLQuery(sqlArea);
		if (!isAll) {
			queryAddress.setInteger(0, id);
		}
		return queryAddress.list();

	}

	// 最后一个馆号 变成字符串 AABE-丰谷镇二社区书屋
	private List<String> changeStatus(List<String> hall) {
		List<String> list = null;
		list = new ArrayList<String>();
		for (int i = 0; i < hall.size(); i++) {
			String str = new Gson().toJson(hall.get(i)).replace("[", "").replace("]", "").replace("\"", "");
			String[] arr = str.split(",");
			String aa = "null".equals(arr[1]) ? "0" : arr[1];
			list.add(arr[0] + "-" + aa);
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.flea.modules.report.dao.VLibraryBookDao#findLibraryBookList(java.lang
	 * .Integer, java.lang.String)
	 */
	@Override
	public SearchResult<VLibraryBook> findLibraryBookList(Page<VLibraryBook> page, QueryCriteria param) {

		SearchResult<VLibraryBook> result = new SearchResult<VLibraryBook>();
		String sqlTotal = "SELECT SUM(tb.totalBook) as sumBook,SUM(tb.totalPrice) as sumPrice,SUM(tb.totalIsbn) as sumIsbn,SUM(tb.totalBookBelong) as sumTotalBelong,SUM(tb.totalPriceBelong) as sumPriceBelong from(";
		String sqlString1 = "SELECT a.totalBook,a.totalPrice,a.libName,a.stayLibraryHallCode,a.totalIsbn,a.totalIn,b.totalBookBelong,b.totalPriceBelong FROM ";
		String sqlString = "(SELECT l.id AS id,COUNT(lb.id) as totalBook,COUNT(distinct lb.isbn) as totalIsbn,stayLibraryHallCode,l.`name` as libName,SUM(price) as totalPrice, "
				+ "COUNT(CASE WHEN lb.bookState =1 THEN lb.id END) as totalIn  ";
		StringBuffer buffer = new StringBuffer();
		buffer.append("  "
				+ " FROM  library_books lb  LEFT JOIN librarys l ON lb.stayLibraryHallCode=l.hallCode where l.isEffective =1 ");
		StringBuffer bufferBelong = new StringBuffer();
		bufferBelong.append(" (SELECT l.id AS id,COUNT(lb.id) AS totalBookBelong,SUM(price) as totalPriceBelong FROM library_books lb LEFT JOIN librarys l ON lb.belongLibraryHallCode = l.hallCode WHERE l.isEffective = 1 ");
		StringBuffer bufferBelongPrice = new StringBuffer();
//		bufferBelongPrice.append(" (SELECT l.id AS id,SUM(price) as totalPriceBelong FROM library_books lb LEFT JOIN librarys l ON lb.belongLibraryHallCode = l.hallCode  WHERE l.isEffective = 1 ");
		
		if (param.getCustomerId() != null) {
			buffer.append(" and  l.customerId=" + param.getCustomerId());
			bufferBelong.append(" and  l.customerId=" + param.getCustomerId());
//			bufferBelongPrice.append(" and  l.customerId=" + param.getCustomerId());
		}
		if (StringUtils.isNotBlank(param.getLib())) {
			buffer.append(" and  l.libraryLevel='" + param.getLib() + "'");
			bufferBelong.append(" and  l.libraryLevel='" + param.getLib() + "'");
//			bufferBelongPrice.append(" and  l.libraryLevel='" + param.getLib() + "'");
		}
		if (StringUtils.isNotBlank(param.getProvince())) {
			buffer.append(" and l.provinceCode='" + param.getProvince() + "' ");
			bufferBelong.append(" and l.provinceCode='" + param.getProvince() + "' ");
//			bufferBelongPrice.append(" and l.provinceCode='" + param.getProvince() + "' ");
		}
		if (StringUtils.isNotBlank(param.getCity())) {
			buffer.append(" and l.cityCode='" + param.getCity() + "' ");
			bufferBelong.append(" and l.cityCode='" + param.getCity() + "' ");
//			bufferBelongPrice.append(" and l.cityCode='" + param.getCity() + "' ");
		}
		if (StringUtils.isNotBlank(param.getArea())) {
			if (param.getCustomerId() == null) { // 一级用户
				buffer.append(" and l.areaCode='" + param.getArea() + "' ");
				bufferBelong.append(" and l.areaCode='" + param.getArea() + "' ");
//				bufferBelongPrice.append(" and l.areaCode='" + param.getArea() + "' ");
			} else {// 二级
				buffer.append(" and l.areaAddress='" + param.getArea() + "' ");
				bufferBelong.append(" and l.areaAddress='" + param.getArea() + "' ");
//				bufferBelongPrice.append(" and l.areaAddress='" + param.getArea() + "' ");
			}
		}
		if ("6".equals(param.getOption())) {
			if (StringUtils.isNotBlank(param.getSearchFrom())) {
				buffer.append(" and l.hallCode>='" + param.getSearchFrom() + "' ");
				bufferBelong.append(" and l.hallCode>='" + param.getSearchFrom() + "' ");
//				bufferBelongPrice.append(" and l.hallCode>='" + param.getSearchFrom() + "' ");
			}
			if (StringUtils.isNotBlank(param.getSearchTo())) {
				buffer.append(" and l.hallCode<='" + param.getSearchTo() + "' ");
				bufferBelong.append(" and l.hallCode<='" + param.getSearchTo() + "' ");
//				bufferBelongPrice.append(" and l.hallCode<='" + param.getSearchTo() + "' ");
			}
		}
		buffer.append(" GROUP BY stayLibraryHallCode) as a ");// 总册数,总码洋
		bufferBelong.append(" GROUP BY belongLibraryHallCode) as b ");
//		bufferBelongPrice.append(" GROUP BY belongLibraryHallCode) as c ");

		String countString = "select count(*) from ( ";
		String whereString = " where 1=1";
		String fieldString = " and ";
		if ("1".equals(param.getOption()) && StringUtils.isNotBlank(param.getKeyWord())) {
			whereString += " and a.libName like '%" + param.getKeyWord() + "%'";
		} else if ("2".equals(param.getOption())) {
			fieldString += " a.totalIsbn";
		} else if ("3".equals(param.getOption())) {
			fieldString += " a.totalBook";
		} else if ("4".equals(param.getOption())) {
			fieldString += " a.totalPrice";
		} else if ("5".equals(param.getOption())) {
			fieldString += " a.totalIn";
		}
		if (!"6".equals(param.getOption()) && StringUtils.isNotBlank(param.getSearchFrom())) {
			whereString += fieldString + ">=" + param.getSearchFrom();
		}
		if (!"6".equals(param.getOption()) && StringUtils.isNotBlank(param.getSearchTo())) {
			whereString += fieldString + "<=" + param.getSearchTo();
		}
		//查询listsql 拼接
		String str= sqlString1+
				sqlString + buffer.toString() +" left join "+ bufferBelong.toString() 
				+ " on a.id = b.id "+whereString;
		SQLQuery query = getSession().createSQLQuery(sqlTotal + str+") as tb");
		query.setResultTransformer(new AliasToBeanResultTransformer(ReportTotal.class));
		ReportTotal total = (ReportTotal) query.uniqueResult();
		result.setTotal(total);
		query = getSession().createSQLQuery(countString + str+ ") as cb");
		int count = ((BigInteger) query.uniqueResult()).intValue();
		page.setCount(count);
		result.setCount(count);// 分页总数
		// String limit = " limit
		// "+page.getFirstResult()+","+page.getPageSize();
		query = getSession().createSQLQuery(str);
		query.setFirstResult(page.getFirstResult());
		query.setMaxResults(page.getMaxResults());
		query.setResultTransformer(new AliasToBeanResultTransformer(VLibraryBook.class));
		List<VLibraryBook> libraryBooks = query.list();// 记录

		result.setResult(libraryBooks);
		return result;
	}

	@Override
	public SearchResult<VLibraryBook> findLibraryBookList(Page<VLibraryBook> page, QueryCriteria param,
			HttpSession session) {

		SearchResult<VLibraryBook> result = new SearchResult<VLibraryBook>();
		SQLQuery query;

		String sqlTotal = "SELECT SUM(tb.totalBook) as sumBook,SUM(tb.totalPrice) as sumPrice,SUM(tb.totalIsbn) as sumIsbn,SUM(tb.totalBookBelong) as sumTotalBelong,SUM(tb.totalPriceBelong) as sumPriceBelong from(";
		String sqlString1 = "SELECT a.totalBook,a.totalPrice,a.libName,a.stayLibraryHallCode,a.totalIsbn,a.totalIn,b.totalBookBelong,b.totalPriceBelong FROM ";
		String sqlString = "(SELECT l.id AS id,COUNT(lb.id) as totalBook,COUNT(distinct lb.isbn) as totalIsbn,stayLibraryHallCode,l.`name` as libName,SUM(price) as totalPrice, "
				+ "COUNT(CASE WHEN lb.bookState =1 THEN lb.id END) as totalIn  ";
		StringBuffer buffer = new StringBuffer();
		buffer.append("  "
				+ " FROM  library_books lb  LEFT JOIN librarys l ON lb.stayLibraryHallCode=l.hallCode where l.isEffective =1 ");
		StringBuffer bufferBelong = new StringBuffer();
		bufferBelong.append(" (SELECT l.id AS id,COUNT(lb.id) AS totalBookBelong,SUM(price) as totalPriceBelong FROM library_books lb LEFT JOIN librarys l ON lb.belongLibraryHallCode = l.hallCode WHERE l.isEffective = 1 ");
		StringBuffer bufferBelongPrice = new StringBuffer();
//		bufferBelongPrice.append(" (SELECT l.id AS id,SUM(price) as totalPriceBelong FROM library_books lb LEFT JOIN librarys l ON lb.belongLibraryHallCode = l.hallCode  WHERE l.isEffective = 1 ");
		
		if (param.getCustomerId() != null) {
			buffer.append(" and  l.customerId=" + param.getCustomerId());
			bufferBelong.append(" and  l.customerId=" + param.getCustomerId());
//			bufferBelongPrice.append(" and  l.customerId=" + param.getCustomerId());
		}
		if (StringUtils.isNotBlank(param.getLib())) {
			buffer.append(" and  l.libraryLevel='" + param.getLib() + "'");
			bufferBelong.append(" and  l.libraryLevel='" + param.getLib() + "'");
//			bufferBelongPrice.append(" and  l.libraryLevel='" + param.getLib() + "'");
		}
		if (StringUtils.isNotBlank(param.getProvince())) {
			buffer.append(" and l.provinceCode='" + param.getProvince() + "' ");
			bufferBelong.append(" and l.provinceCode='" + param.getProvince() + "' ");
//			bufferBelongPrice.append(" and l.provinceCode='" + param.getProvince() + "' ");
		}
		if (StringUtils.isNotBlank(param.getCity())) {
			buffer.append(" and l.cityCode='" + param.getCity() + "' ");
			bufferBelong.append(" and l.cityCode='" + param.getCity() + "' ");
//			bufferBelongPrice.append(" and l.cityCode='" + param.getCity() + "' ");
		}
		if (StringUtils.isNotBlank(param.getArea())) {
			if (param.getCustomerId() == null) { // 一级用户
				buffer.append(" and l.areaCode='" + param.getArea() + "' ");
				bufferBelong.append(" and l.areaCode='" + param.getArea() + "' ");
//				bufferBelongPrice.append(" and l.areaCode='" + param.getArea() + "' ");
			} else {// 二级
				buffer.append(" and l.areaAddress='" + param.getArea() + "' ");
				bufferBelong.append(" and l.areaAddress='" + param.getArea() + "' ");
//				bufferBelongPrice.append(" and l.areaAddress='" + param.getArea() + "' ");
			}
		}
		if ("6".equals(param.getOption())) {
			if (StringUtils.isNotBlank(param.getSearchFrom())) {
				buffer.append(" and l.hallCode>='" + param.getSearchFrom() + "' ");
				bufferBelong.append(" and l.hallCode>='" + param.getSearchFrom() + "' ");
//				bufferBelongPrice.append(" and l.hallCode>='" + param.getSearchFrom() + "' ");
			}
			if (StringUtils.isNotBlank(param.getSearchTo())) {
				buffer.append(" and l.hallCode<='" + param.getSearchTo() + "' ");
				bufferBelong.append(" and l.hallCode<='" + param.getSearchTo() + "' ");
//				bufferBelongPrice.append(" and l.hallCode<='" + param.getSearchTo() + "' ");
			}
		}
		buffer.append(" GROUP BY stayLibraryHallCode) as a ");// 总册数,总码洋
		bufferBelong.append(" GROUP BY belongLibraryHallCode) as b ");
//		bufferBelongPrice.append(" GROUP BY belongLibraryHallCode) as c ");
		
		String countString = "select count(*) from ( ";
		String whereString = " where 1=1";
		String fieldString = " and ";
		if ("1".equals(param.getOption()) && StringUtils.isNotBlank(param.getKeyWord())) {
			whereString += " and a.libName like '%" + param.getKeyWord() + "%'";
		} else if ("2".equals(param.getOption())) {
			fieldString += " a.totalIsbn";
		} else if ("3".equals(param.getOption())) {
			fieldString += " a.totalBook";
		} else if ("4".equals(param.getOption())) {
			fieldString += " a.totalPrice";
		} else if ("5".equals(param.getOption())) {
			fieldString += " a.totalIn";
		}
		if (!"6".equals(param.getOption()) && StringUtils.isNotBlank(param.getSearchFrom())) {
			whereString += fieldString + ">=" + param.getSearchFrom();
		}
		if (!"6".equals(param.getOption()) && StringUtils.isNotBlank(param.getSearchTo())) {
			whereString += fieldString + "<=" + param.getSearchTo();
		}
		QueryCriteria qca = (QueryCriteria) session.getAttribute("vlibBook");
		ReportTotal total = (ReportTotal) session.getAttribute("total");
		
		int count = 0;
		List<VLibraryBook> libraryBooks = null ;
		if (null != qca && param.equals(qca) && null != total) {
			libraryBooks = (List<VLibraryBook>)session.getAttribute("libraryBooks");
			count = (int)session.getAttribute("count");
			System.out.println("exists");
		} else {
			//拼接查询sql
			String str= sqlString1+
					sqlString + buffer.toString() +" left join "+ bufferBelong.toString() 
					+ " on a.id = b.id "+whereString;
			session.setAttribute("vlibBook", param);
			query = getSession().createSQLQuery(sqlTotal + str+") as tb");
			query.setResultTransformer(new AliasToBeanResultTransformer(ReportTotal.class));
			total = (ReportTotal) query.uniqueResult();
			session.setAttribute("total", total);
			query = getSession().createSQLQuery(countString + str+ ") as cb");
			count = ((BigInteger) query.uniqueResult()).intValue();
			session.setAttribute("count", count);
			
//			query = getSession().createSQLQuery(sqlString + buffer.toString() + whereString);
			query = getSession().createSQLQuery(str);
			query.setResultTransformer(new AliasToBeanResultTransformer(VLibraryBook.class));
			libraryBooks = query.list();// 记录
			session.setAttribute("libraryBooks", libraryBooks);
		}

		result.setTotal(total);
		page.setCount(count);
		result.setCount(count);// 分页总数
		// String limit = " limit
		// "+page.getFirstResult()+","+page.getPageSize();

		//List<VLibraryBook> libraryBooks2 = query.list();
		List<VLibraryBook> resultBooks = new ArrayList();
		for (int i = page.getFirstResult(); i< page.getFirstResult() + page.getPageSize() ;i++) {
			if(i>=libraryBooks.size()) break;
			resultBooks.add(libraryBooks.get(i));
		}
		
		result.setResult(resultBooks);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.flea.modules.report.dao.VLibraryBookDao#findLibraryDataList()
	 */
	@Override
	public SearchResult<VLibraryBook> findLibraryDataList(QueryCriteria param) {
		SearchResult<VLibraryBook> result = new SearchResult<VLibraryBook>();
		String sqlTotal = "SELECT SUM(d.totalIsbn) as sumIsbn,SUM(d.totalBook) as sumBook,SUM(d.totalPrice) as sumPrice from (";// 合计

		StringBuffer buffer = new StringBuffer();
		// 种数
		buffer.append(
				" (SELECT concat(l.categoryCode,' ',l.categoryName) category ,COUNT(l.c) totalIsbn,l.categoryCode FROM ( 	SELECT c.categoryCode,c.categoryName,a.isbn,a.stayLibraryHallCode,a.c FROM ");

		buffer.append(
				" (SELECT SUBSTRING(lb.classificationNumber,1,1) classNumber, lb.isbn,COUNT(DISTINCT lb.isbn) as c,lb.stayLibraryHallCode from library_books lb LEFT JOIN librarys l  ON lb.stayLibraryHallCode=l.hallCode where l.isEffective =1");
		if (param.getCustomerId() != null) {
			buffer.append(" and  l.customerId=" + param.getCustomerId());
		}
		if (StringUtils.isNotBlank(param.getLib())) {
			buffer.append(" and  l.libraryLevel='" + param.getLib() + "'");
		}
		if (StringUtils.isNotBlank(param.getProvince())) {
			buffer.append(" and l.provinceCode='" + param.getProvince() + "' ");
		}
		if (StringUtils.isNotBlank(param.getCity())) {
			buffer.append(" and l.cityCode='" + param.getCity() + "' ");
		}
		if (StringUtils.isNotBlank(param.getArea())) {
			if (param.getCustomerId() == null) { // 一级用户
				buffer.append(" and l.areaCode='" + param.getArea() + "' ");
			} else {// 二级
				buffer.append(" and l.areaAddress='" + param.getArea() + "' ");
			}
		}
		if ("6".equals(param.getOption()) && StringUtils.isNotBlank(param.getKeyWord())) {
			buffer.append(" and l.hallCode='" + param.getKeyWord() + "' ");
		}
		buffer.append(
				" GROUP BY lb.isbn) as a ,categorys c  where a.classNumber=c.categoryCode) as l  WHERE l.categoryCode!='' GROUP BY l.categoryCode) AS a ");
		buffer.append(" LEFT JOIN   ");
		// 册数
		buffer.append(
				"(SELECT  COUNT(*) AS totalBook,SUM(price) AS totalPrice,SUBSTRING(classificationNumber,1,1) classnumber from library_books lb  LEFT JOIN  librarys l ON lb.stayLibraryHallCode=l.hallCode where 1=1");
		if (param.getCustomerId() != null) {
			buffer.append(" and  l.customerId=" + param.getCustomerId());
		}
		if (StringUtils.isNotBlank(param.getLib())) {
			buffer.append(" and l.libraryLevel='" + param.getLib() + "'");
		}
		if (StringUtils.isNotBlank(param.getProvince())) {
			buffer.append(" and l.provinceCode='" + param.getProvince() + "' ");
		}
		if (StringUtils.isNotBlank(param.getCity())) {
			buffer.append(" and l.cityCode='" + param.getCity() + "' ");
		}
		if (StringUtils.isNotBlank(param.getArea())) {
			if (param.getCustomerId() == null) { // 一级用户
				buffer.append(" and l.areaCode='" + param.getArea() + "' ");
			} else {// 二级
				buffer.append(" and l.areaAddress='" + param.getArea() + "' ");
			}
		}
		if ("6".equals(param.getOption())) {
			if (StringUtils.isNotBlank(param.getSearchFrom())) {
				buffer.append(" and l.hallCode>='" + param.getSearchFrom() + "' ");
			}
			if (StringUtils.isNotBlank(param.getSearchTo())) {
				buffer.append(" and l.hallCode<='" + param.getSearchTo() + "' ");
			}
		}
		buffer.append(" GROUP BY classnumber) AS b  ON a.categoryCode=b.classnumber ");
		String whereString = " where 1=1";
		String fieldString = " and ";
		if ("1".equals(param.getOption()) && StringUtils.isNotBlank(param.getKeyWord())) {
			whereString += " and t.libName like '%" + param.getKeyWord() + "%'";
		} else if ("2".equals(param.getOption())) {
			fieldString += " a.totalIsbn";
		} else if ("3".equals(param.getOption())) {
			fieldString += " b.totalBook";
		} else if ("4".equals(param.getOption())) {
			fieldString += " b.totalPrice";
		} else if ("5".equals(param.getOption())) {
			fieldString += " (b.totalBook/391)*100";
		}

		if (!"6".equals(param.getOption()) && StringUtils.isNotBlank(param.getSearchFrom())) {
			whereString += fieldString + ">=" + param.getSearchFrom();
		}
		if (!"6".equals(param.getOption()) && StringUtils.isNotBlank(param.getSearchTo())) {
			whereString += fieldString + "<=" + param.getSearchTo();
		}
		String countString = "SELECT  COUNT(*) AS totalCount from library_books";// 查询百分比总数
		SQLQuery query = getSession().createSQLQuery(countString);
		Object totalCount = query.uniqueResult();
		int total;// 查询借阅总数，算百分比
		if (totalCount != null) {
			total = ((BigInteger) totalCount).intValue();// 查询借阅总数，算百分比
		} else {
			total = 0;
		}
		String sqlString = "SELECT  a.category,a.totalIsbn,b.totalBook,b.totalPrice,(b.totalBook/" + total
				+ ")*100 as percent  FROM ";
		query = getSession().createSQLQuery(sqlString + buffer.toString() + whereString);
		query.setResultTransformer(new AliasToBeanResultTransformer(VLibraryBook.class));
		List<VLibraryBook> libraryBooks = query.list();// 记录
		query = getSession().createSQLQuery(sqlTotal + sqlString + buffer.toString() + ") as d");
		query.setResultTransformer(new AliasToBeanResultTransformer(ReportTotal.class));
		ReportTotal reportTotal = (ReportTotal) query.uniqueResult();
		result.setTotal(reportTotal);
		result.setResult(libraryBooks);
		return result;
	}

	/**
	 * 
	 * @method:getAllLibLevel 获取所有的全部馆别
	 * @Description:getAllLibLevel
	 * @author: HeTao
	 * @date:2016年8月12日 上午10:13:46
	 * @param:@return
	 * @return:String
	 */
	@Override
	public List<String> getAllLibLevel() {
		User user = ShiroUtils.getCurrentUser();
		Role role = user.getRoles().get(0);
		String le = role.getLevel();
		if (Common.ROLE_FIRST_LEVLE.equals(le)) {
			// String sql = "SELECT lb.libraryLevel FROM librarys AS lb GROUP BY
			// lb.libraryLevel";
			// SQLQuery query = this.getSession().createSQLQuery(sql);
			List<String> list = new ArrayList<String>();
//			list.add("公共图书馆");
//			list.add("高校馆");
//			list.add("中小学馆");
//			list.add("社区书屋");
//			list.add("农家书屋");
//			list.add("社会馆");
			list.add("公共图书馆I");
			list.add("公共图书馆II");
			list.add("公共图书馆III");
			list.add("公共图书馆IV");
			list.add("社区书屋");
			list.add("农家书屋");
			list.add("中小学馆I");
			list.add("中小学馆II");
			list.add("中小学馆III");			
			list.add("高校馆I");	
			list.add("高校馆II");	
			list.add("高校馆III");
			list.add("社会馆");
			list.add("体验馆");
			return list;
		} else {
			// 如果是用户的话 根据用户所有的图书馆的馆别进行分类，然后返回一个字符串数组
			String sql = "SELECT lb.libraryLevel FROM librarys AS lb WHERE lb.customerId=? GROUP BY lb.libraryLevel ";
			SQLQuery query = this.getSession().createSQLQuery(sql);
			Integer id = user.getCustomer().getId();
			if (id == null) {
				query.setInteger(0, 0);
			} else {
				query.setInteger(0, id);
			}
			List<String> list = query.list();
			return list;
		}

	}
}
