package com.flea.modules.order.dao;

import org.springframework.stereotype.Repository;

import com.flea.common.dao.BaseDao;
import com.flea.modules.order.pojo.OrdersDetail;

@Repository
public interface OrdersDetailDao extends BaseDao<OrdersDetail,Integer> {

}
