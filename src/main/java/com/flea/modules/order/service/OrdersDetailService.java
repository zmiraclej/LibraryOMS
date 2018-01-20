package com.flea.modules.order.service;

import java.util.List;

import com.flea.common.pojo.Page;
import com.flea.common.service.BaseService;
import com.flea.modules.ebook.pojo.Ebook;
import com.flea.modules.order.pojo.Orders;
import com.flea.modules.order.pojo.OrdersDetail;
import com.flea.modules.system.pojo.Library;

public interface OrdersDetailService extends BaseService<OrdersDetail, Integer>{

   	Page<Orders> find(Page<OrdersDetail> page,OrdersDetail orders,String search);
	
	boolean start(Integer id);
	
	OrdersDetail findByName(String name);
	
	void saveSolrIndex(OrdersDetail orders);
	/**
	 * 新增订单
	 * @param customerID 客户ID
	 */
    public Orders addOrderByCustomerID(Integer customerID, Integer kinds);
    
	/**
	 * 关键字查询 订单详情列表
	 * @param ordersID 订单详情ID
	 * @param keywords 检索关键字
	 */
    public List<OrdersDetail> getOrdersDetailListByOrdersID(Integer ordersID,String keywords);
    
	/**
	 * 批量删除
	 * @param orderID 订单
	 * @param delIds 订单详情ID
	 */
	public void deleteBatch(Integer orderID, Long[] delIds);

	/**
	 * 订单补充内电子书去重检索
	 * @param id 订单id
	 * @param keywords 检索关键字
	 * @param searchRole 查询条件
	 * @return 
	 */
	public List<Ebook> supplementsSearch(Integer id,  Integer customerID, String[] keywords, Integer[] searchRole);
	
	
	/**
	 * 补充订单
	 * @param orderID 订单ID
	 * @param ebookIDs 电子书IDs
	 */
	public void appendedOrders(Integer orderID, Long[] ebookIDs);
	
	
	/**
	 * 新增订单
	 * @param customerID 客户ID
	 * @param ebookIDs 电子书IDs
	 */
	public Orders addedOrders(Integer customerID, Long[] ebookIDs);

	/**
	 * 新增订单内电子书去重检索
	 * @param customerId 客户id
	 * @param keywords 检索关键字
	 * @param searchRole 查询条件
	 * @return 
	 */
	public List<Ebook> addedSearch(Integer customerId, String[] keywords, Integer[] searchRole);
}