package com.flea.modules.order.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flea.common.Common;
import com.flea.common.pojo.Page;
import com.flea.common.service.impl.BaseServiceImpl;
import com.flea.common.util.OrderUtil;
import com.flea.common.util.ShiroUtils;
import com.flea.modules.customer.dao.CustomerDao;
import com.flea.modules.customer.pojo.Customer;
import com.flea.modules.customer.pojo.CustomerContact;
import com.flea.modules.ebook.dao.EbookDao;
import com.flea.modules.ebook.pojo.Ebook;
import com.flea.modules.news.dao.ActivityDao;
import com.flea.modules.order.dao.OrdersDao;
import com.flea.modules.order.dao.OrdersDetailDao;
import com.flea.modules.order.pojo.Orders;
import com.flea.modules.order.pojo.OrdersDetail;
import com.flea.modules.order.service.OrdersDetailService;
import com.flea.modules.order.service.OrdersService;
import com.flea.modules.system.pojo.Library;

@Service
public class OrdersDetailServiceImpl extends BaseServiceImpl<OrdersDetail, Integer> implements OrdersDetailService {

	@Autowired
	private OrdersDetailDao ordersDetailDao;

	@Autowired
	private CustomerDao custumerDao;

	@Autowired
	private EbookDao ebookDao;
	
	@Autowired
	private OrdersDao ordersDao;

	@Autowired
	public OrdersDetailServiceImpl(OrdersDetailDao ordersDetailDao) {
		super(ordersDetailDao);
		this.ordersDetailDao = ordersDetailDao;
	}

	@Override
	public Page<Orders> find(Page<OrdersDetail> page, OrdersDetail orders, String search) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean start(Integer id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public OrdersDetail findByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveSolrIndex(OrdersDetail orders) {
		// TODO Auto-generated method stub

	}

	@Override
	public Orders addOrderByCustomerID(Integer customerID, Integer kinds) {
		// TODO Auto-generated method stub
		//获取全球单号
		Long orderNumber = OrderUtil.makeOrder(customerID, ordersDetailDao);
		//保存订单
		List paramList = new ArrayList();
		paramList.add(orderNumber);
		paramList.add(customerID);
		paramList.add(new Date());
		paramList.add(ShiroUtils.getCurrentUser().getId());
		ordersDetailDao.querySql("INSERT INTO  ebook_orders(order_number, customer_id, create_date, operator_id) values (?, ?, ?, ?)", paramList);
		Orders orders = ordersDao.getByHQL("from  Orders where orderNumber = " + orderNumber,null );
		return orders;
	}

	@Override
	public List<OrdersDetail> getOrdersDetailListByOrdersID(Integer ordersID, String keywords) {
		// TODO Auto-generated method stub
		if (null == keywords || "".equals(keywords.trim())) {
			return ordersDetailDao.getListByHQL("from OrdersDetail as ebook_orders_detail where ordersID = " + ordersID,
					null);
		} else {
			keywords = keywords.trim();
			List<Object> values = new ArrayList<Object>();
			values.add(keywords);
			return ordersDetailDao.getListByHQL(
					"from OrdersDetail as ebook_orders_detail where ordersID = " + ordersID + " and bookName like '%"
							+ keywords + "%'",
					// +" or publisher like '%" + keywords + "%'"
					// +" or author like '%" + keywords + "%'",
					null);
		}
	}

	@Override
	public void deleteBatch(Integer orderID, Long[] delIds) {
		String ids = StringUtils.join(delIds, ",");
		ordersDetailDao.querySql("delete from ebook_orders_detail where id in(" + ids + ")", null);
	
	}



	public String getSearchCloumns(Integer i) {
		/*
		 * <option value="0">出版年</option> <option value="1">分类</option> <option
		 * value="2">ISBN</option> <option value="3">书名</option> <option
		 * value="4">著译者</option> <option value="5">出版社</option>
		 */
		String condition = null;
		switch (i) {
		case 0:
			condition = "publishDate";
			break;
		case 1:
			condition = "categoryName";
			break;
		case 2:
			condition = "isbn";
			break;
		case 3:
			condition = "bookName";
			break;
		case 4:
			condition = "author";
			break;
		case 5:
			condition = "publisher";
			break;
		default:
			condition = "bookName";
		}

		return condition;
	}

	public void appendedOrders(Integer orderID, Long[] ebookIDs) {
		if(null == ebookIDs || null == orderID) return;
		StringBuffer sql = new StringBuffer("INSERT IGNORE INTO ebook_orders_detail(order_id, ebook_id) values");
		for (int i = 0; i < ebookIDs.length; i++) {
			if (i == ebookIDs.length - 1) {
				sql.append("(" + orderID + "," + ebookIDs[i] + ")");
			} else {
				sql.append("(" + orderID + "," + ebookIDs[i] + "),");
			}
		}
		ordersDetailDao.querySql(sql.toString(), null);
	}
	
	
	public Orders addedOrders(Integer customerID, Long[] ebookIDs) {
		if(null == ebookIDs || null == customerID) return null;
		//生成订单
		Orders orders = addOrderByCustomerID(customerID, ebookIDs.length);
		//保存订单详情
		StringBuffer sql = new StringBuffer("INSERT IGNORE INTO ebook_orders_detail(order_id, ebook_id) values");
		for (int i = 0; i < ebookIDs.length; i++) {
			if (i == ebookIDs.length - 1) {
				sql.append("(" + orders.getId() + "," + ebookIDs[i] + ")");
			} else {
				sql.append("(" + orders.getId() + "," + ebookIDs[i] + "),");
			}
		}
		ordersDetailDao.querySql(sql.toString(), null);
		return orders;
	}	
	@Override
	public List<Ebook> supplementsSearch(Integer id, Integer customerID, String[] keywords, Integer[] searchRole) {

//		//去掉订单内的重复
//		List<OrdersDetail> listDetail = ordersDetailDao
//				.getListBySQL("select ebook_id from ebook_orders_detail where order_id = " + id, null);
		//去掉改该客户所有订单内重复的书
		List<OrdersDetail> listDetail = ordersDetailDao
				.getListBySQL("select ebook_id from ebook_orders_detail where order_id in (SELECT id from ebook_orders where customer_id = " + customerID + ")", null);
		//去掉客户库存的重复
		List<OrdersDetail> listCustomerEbook = ordersDetailDao.getListBySQL("select ebook_id from r_customer_ebook  where customer_id  = " + customerID, null);
		// 数组合并
		Set excludeBookSet =new HashSet();
		excludeBookSet.addAll(listDetail);
		excludeBookSet.addAll(listCustomerEbook);
		String ids = StringUtils.join(excludeBookSet, ",");
		StringBuffer sql = new StringBuffer("from Ebook where 1=1");
		if (excludeBookSet.size() > 0) {
			// 去重
			sql.append(" and id not in (" + ids + ")");
		}
		if (null != keywords) {
			//6个查询选项组合
			for (int i = 0; i < keywords.length; i++) {
				if (null != keywords[i] && !"".equals(keywords[i].trim())) {
					sql.append(" and " + getSearchCloumns(searchRole[i]) + " like '%" + keywords[i] + "%'");
				}
			}
		}
		sql.append(" order by id desc ");
		return ebookDao.getListByHQL(sql.toString(), 1000, null);
	}
	
	@Override
	public List<Ebook> addedSearch(Integer customerId, String[] keywords, Integer[] searchRole) {
		
		//去掉客户库存的重复
		List<OrdersDetail> listCustomerEbook = ordersDetailDao
				.getListBySQL("select ebook_id from r_customer_ebook  where customer_id  = " + customerId, null);
		//去掉改该客户所有订单内重复的书
		List<OrdersDetail> listDetail = ordersDetailDao
				.getListBySQL("select ebook_id from ebook_orders_detail where order_id in (SELECT id from ebook_orders where customer_id = " + customerId + ")", null);		
		//去掉该客户未提交订单内的重复
//		List<OrdersDetail> listDetail = ordersDetailDao
//				.getListBySQL("select ebook_id from ebook_orders_detail where customer_id = " + customerId, null);
		// 数组合并
		Set excludeBookSet =new HashSet();
		excludeBookSet.addAll(listDetail);
		excludeBookSet.addAll(listCustomerEbook);
		String ids = StringUtils.join(excludeBookSet, ",");
		StringBuffer sql = new StringBuffer("from Ebook where 1=1");
		if (excludeBookSet.size() > 0) {
			// 去重
			sql.append(" and id not in (" + ids + ")");
		}
		if (null != keywords) {
			//6个查询选项组合
			for (int i = 0; i < keywords.length; i++) {
				if (null != keywords[i] && !"".equals(keywords[i].trim())) {
					sql.append(" and " + getSearchCloumns(searchRole[i]) + " like '%" + keywords[i] + "%'");
				}
			}
		}
		return ebookDao.getListByHQL(sql.toString(), 1000, null);
	}
}
