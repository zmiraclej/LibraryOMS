package com.flea.modules.order.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
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
import com.flea.modules.order.service.OrdersService;
import com.flea.modules.system.pojo.Library;

@Service
public class OrdersServiceImpl extends BaseServiceImpl<Orders, Integer> implements OrdersService {

	@Autowired
	private OrdersDao ordersDao;

	@Autowired
	private CustomerDao custumerDao;

	@Autowired
	public OrdersServiceImpl(OrdersDao ordersDao) {
		super(ordersDao);
		this.ordersDao = ordersDao;
	}

	@Override
	public Page<Orders> find(Page<Orders> page, Orders orders, String search) {
		// TODO Auto-generated method stub

		DetachedCriteria criteria = ordersDao.createDetachedCriteria();
		if (StringUtils.isNotBlank(search)) { // 搜索
			search = search.trim();
			if (OrderUtil.validateOrder(search)) {
				criteria.add(Restrictions.eq("orderNumber", Long.parseLong(search)));
			} else {
				return new Page<Orders>();
			}
		}
		criteria.add(Restrictions.eq("status", 0));
		criteria.addOrder(Order.desc("id"));
		return ordersDao.find(page, criteria);
	}

	@Override
	public boolean start(Integer id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Orders findByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveSolrIndex(Orders orders) {
		// TODO Auto-generated method stub

	}

	@Override
	public Integer addOrderByCustomerID(Integer customerID) {


		return null;
	}

	@Override
	public void submitOrder(Integer id) {
		// TODO Auto-generated method stub
		Orders order = this.getOne(id);
		order.setStatus(1);
		Date date = new Date();
		order.setModifyDate(date);
		order.setSubmitDate(date);
		//提交订单
		this.saveOne(order);
		//关联关系
		ordersDao.querySql("INSERT IGNORE INTO  r_customer_ebook(customer_id, ebook_id ) select eo.customer_id ,ebook_id from ebook_orders_detail  eod left JOIN ebook_orders eo ON eod.order_id = eo.id WHERE order_id = " + id, null);
	}

	@Override
	public Map analysis(Integer orderID) {
		// TODO Auto-generated method stub
		Map data = new HashMap();
		// Orders order = this.getOne(orderID);

		List<Orders> listDetail = ordersDao
				.getListBySQL("select ebook_id from ebook_orders_detail where order_id = " + orderID, null);
		if (listDetail.size() != 0) {
			String ids = StringUtils.join(listDetail, ",");
			List yearData = ordersDao
					.getListBySQL("select left(publishDate, 4), count(publishDate) from system_ebook where id in (" + ids + ") group by left(publishDate, 4)", null);
			List categoryData = ordersDao
					.getListBySQL("select categoryName,count(categoryName) from system_ebook where id in (" + ids + ") group by categoryName", null);
			data.put("yearData", yearData);
			data.put("categoryData", categoryData);
			data.put("total", listDetail.size());
		}
		return data;
	}

	public List<Orders> getUnSubmitOrdersList(Integer customerID) {

		return ordersDao.getListByHQL("from Orders where customer = " + customerID + " and status != 1", null);
	}

}
