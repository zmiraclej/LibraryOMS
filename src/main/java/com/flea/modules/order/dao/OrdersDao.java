package com.flea.modules.order.dao;

import org.springframework.stereotype.Repository;

import com.flea.common.dao.BaseDao;
import com.flea.modules.order.pojo.Orders;

@Repository
public interface OrdersDao extends BaseDao<Orders,Integer> {

}
