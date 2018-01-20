package com.flea.modules.order.service;

import java.util.List;
import java.util.Map;

import com.flea.common.pojo.Page;
import com.flea.common.service.BaseService;
import com.flea.modules.order.pojo.Orders;
import com.flea.modules.system.pojo.Library;

public interface OrdersService extends BaseService<Orders, Integer>{

   	Page<Orders> find(Page<Orders> page,Orders orders,String search);
	
	boolean start(Integer id);
	
	Orders findByName(String name);
	
	void saveSolrIndex(Orders orders);

	/**
	 * 添加客户订单
	 * @param customerID 客户ID
	 */
    public Integer addOrderByCustomerID(Integer customerID);
	/**
	 * 提交订单
	 * @param orderID 订单ID
	 */
    public void submitOrder(Integer orderID);

	/**
	 * 统计分析
	 * @param orderID 订单ID
	 */
	public Map analysis(Integer orderID);
	
	
	/**
	 * 获取客户未提交订单列表
	 * @param orderID 订单ID
	 */
	public List<Orders> getUnSubmitOrdersList(Integer customerID);
    

}