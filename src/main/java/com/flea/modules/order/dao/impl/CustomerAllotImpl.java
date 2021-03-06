package com.flea.modules.order.dao.impl;

import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.transform.ResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.flea.common.dao.impl.BaseDaoImpl;
import com.flea.common.pojo.Page;
import com.flea.modules.order.dao.CustomerAllotDao;
import com.flea.modules.order.pojo.CustomerAllot;



@Repository
public class CustomerAllotImpl extends BaseDaoImpl<CustomerAllot, Integer> implements CustomerAllotDao {



}
