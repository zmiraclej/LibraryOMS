package com.flea.common.dao.impl;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.transform.ResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.flea.common.Common;
import com.flea.common.dao.BaseDao;
import com.flea.common.pojo.Page;


@SuppressWarnings({ "rawtypes", "unchecked" })
@Transactional
public class BaseDaoImpl<T,ID extends Serializable> implements BaseDao<T,ID> {
	 
    @Autowired
    private SessionFactory sessionFactory;
    protected Class<T> entityClass;
 
    public BaseDaoImpl() {
    	 
    }
 
    protected Class getEntityClass() {
        if (entityClass == null) {
            entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        }
        return entityClass;
    }
 
    /**
     * <保存实体>
     * <完整保存实体>
     * @param t 实体参数
     * @see com.leju.base.util.IBaseDao#save(java.lang.Object)
     */
    @Override
    public void saveOne(T t) {
    	try {
    		this.getSession().save(t);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    
     
    /**
     * <保存或者更新实体>
     * @param t 实体
     * @see com.leju.base.util.IBaseDao#saveOrUpdate(java.lang.Object)
     */
    @Override
    public void saveOrUpdateOne(T t) {
    	Session session = getSession();
    	session.merge(t);
    	session.flush();
    	session.clear();
//    	session.refresh(t);

    	session.update(t);
    	//session.saveOrUpdate(t);
    }
     
    /**
     * <load>
     * <加载实体的load方法>
     * @param id 实体的id
     * @return 查询出来的实体
     * @see com.leju.base.util.IBaseDao#load(java.io.Serializable)
     */
    @Override
    public T load(int id) {
        T load = (T) this.getSession().load(getEntityClass(), id);
        return load;
    }
     
    /**
     * <get>
     * <查找的get方法>
     * @param id 实体的id
     * @return 查询出来的实体
     * @see com.leju.base.util.IBaseDao#get(java.io.Serializable)
     */
    @Override
    public T getOne(int id) {
        T load = (T) this.getSession().get(getEntityClass(), id);
        return load;
    }
     
    /**
     * <contains>
     * @param t 实体
     * @return 是否包含
     * @see com.leju.base.util.IBaseDao#contains(java.lang.Object)
     */
    @Override
    public boolean contains(T t) {
        return this.getSession().contains(t);
    }
 
    /**
     * <delete>
     * <删除表中的t数据>
     * @param t 实体
     */
    @Override
    public void delOne(T t) {
        this.getSession().delete(t);
        this.getSession().flush();
    }
     
    /**
     * <根据ID删除数据>
     * @param Id 实体id
     * @return 是否删除成功
     */
    @Override
    public boolean delById(int id) {
         T t = getOne(id);
         if(t == null){
             return false;
         }
         delOne(t);
        return true;
    }
     
    /**
     * <根据ID删除数据>
     * @param Id 实体id
     * @return 是否删除成功
     * @see com.leju.base.util.IBaseDao#deleteById(java.io.Serializable)
     */
    @Override
    public int deleteById(int id) {
	  Session session =this.getSession();
      String hqlString = "update "+getEntityClass().getName()+" set isEffective=" + Common.FLAG_DELETED + " where id = "+id;
      Query query =session.createQuery(hqlString);
        return  query.executeUpdate();
    }
 
    /**
     * <删除所有>
     * @param entities 实体的Collection集合
     * @see com.leju.base.util.IBaseDao#deleteAll(java.util.Collection)
     */
    @Override
    public void batchDelete(Collection<T> entities) {
        for(Object entity : entities) {
            this.getSession().delete(entity);
        }
    }
 
    /**
     * <执行Hql语句>
     * @param hqlString hql
     * @param values 不定参数数组
     * @see com.leju.base.util.IBaseDao#queryHql(java.lang.String, java.lang.List<Object>)
     */
    @Override
    public void queryHql(String hqlString, List<Object> values) {
        Query query = this.getSession().createQuery(hqlString);
        if (values != null)
        {
            for (int i = 0; i < values.size(); i++)
            {
                query.setParameter(i, values.get(i));
            }
        }
        query.executeUpdate();
    }
 
    /**
     * <执行Sql语句>
     * @param sqlString sql
     * @param values 不定参数数组
     * @see com.leju.base.util.IBaseDao#querySql(java.lang.String, java.lang.List<Object>)
     */
    @Override
    public void querySql(String sqlString, List<Object> values) {
        Query query = this.getSession().createSQLQuery(sqlString);
        if (values != null)
        {
            for (int i = 0; i < values.size(); i++)
            {
                query.setParameter(i, values.get(i));
            }
        }
        query.executeUpdate();
    }
 
    /**
     * <根据HQL语句查找唯一实体>
     * @param hqlString HQL语句
     * @param values 不定参数的Object数组
     * @return 查询实体
     * @see com.leju.base.util.IBaseDao#getByHQL(java.lang.String, java.lang.List<Object>)
     */
    @Override
    public T getByHQL(String hqlString, List<Object> values) {
        Query query = this.getSession().createQuery(hqlString);
        if (values != null)
        {
            for (int i = 0; i < values.size(); i++)
            {
                query.setParameter(i, values.get(i));
            }
        }
        return (T) query.uniqueResult();
    }
 
    /**
     * <根据SQL语句查找唯一实体>
     * @param sqlString SQL语句
     * @param values 不定参数的Object数组
     * @return 查询实体
     * @see com.leju.base.util.IBaseDao#getBySQL(java.lang.String, java.lang.List<Object>)
     */
    @Override
    public T getBySQL(String sqlString, List<Object> values) {
        Query query = this.getSession().createSQLQuery(sqlString);
        if (values != null)
        {
            for (int i = 0; i < values.size(); i++)
            {
                query.setParameter(i, values.get(i));
            }
        }
        return (T) query.uniqueResult();
    }
 
    /**
     * <根据HQL语句，得到对应的list>
     * @param hqlString HQL语句
     * @param values 不定参数的Object数组
     * @return 查询多个实体的List集合
     * @see com.leju.base.util.IBaseDao#getListByHQL(java.lang.String, java.lang.List<Object>)
     */
    @Override
    public List<T> getListByHQL(String hqlString, List<Object> values) {
        Query query = this.getSession().createQuery(hqlString);
        if (values != null)
        {
            for (int i = 0; i < values.size(); i++)
            {
                query.setParameter(i, values.get(i));
            }
        }
        return query.list();
    }
    
    /**
     * <根据HQL语句，得到对应的limit list>
     * @param hqlString HQL语句
     * @param values 不定参数的Object数组
     * @return 查询多个实体的List集合
     * @see com.leju.base.util.IBaseDao#getListByHQL(java.lang.String, java.lang.List<Object>)
     */
    @Override
    public List<T> getListByHQL(String hqlString, Integer limit, List<Object> values) {
        Query query = this.getSession().createQuery(hqlString);
        query.setMaxResults(limit);
        if (values != null)
        {
            for (int i = 0; i < values.size(); i++)
            {
                query.setParameter(i, values.get(i));
            }
        }
        return query.list();
    }
 
    /**
     * <根据SQL语句，得到对应的list>
     * @param sqlString SQL语句
     * @param values 不定参数的Object数组
     * @return 查询多个实体的List集合
     * @see com.leju.base.util.IBaseDao#getListBySQL(java.lang.String, java.lang.List<Object>)
     */
    @Override
    public List<T> getListBySQL(String sqlString, List<Object> values ) {
        Query query = this.getSession().createSQLQuery(sqlString);
        if (values != null)
        {
            for (int i = 0; i < values.size(); i++)
            {
                query.setParameter(i, values.get(i));
            }
        }
        return query.list();
    }
     
    /**
     * 由sql语句得到List
     * @param sql
     * @param map
     * @param values
     * @return List
     * @see com.leju.base.util.IBaseDao#findListBySql(java.lang.String, com.leju.base.hibernate.RowMapper, java.lang.List<Object>)
     */
   
    /**
     * <refresh>
     * @param t 实体
     * @see com.leju.base.util.IBaseDao#refresh(java.lang.Object)
     */
    @Override
    public void refresh(T t) {
        this.getSession().refresh(t);
    }
 
    /**
     * <update>
     * @param t 实体
     * @see com.leju.base.util.IBaseDao#update(java.lang.Object)
     */
    @Override
    public void updateOne(T t) {
    	this.getSession().clear();
        this.getSession().merge(t);
    }
     
    /**
     * <根据HQL得到记录数>
     * @param hql HQL语句
     * @param values 不定参数的Object数组
     * @return 记录总数
     * @see com.leju.base.util.IBaseDao#countByHql(java.lang.String, java.lang.List<Object>)
     */
    @Override
    public Long countByHql(String hql, List<Object> values) {
        Query query = this.getSession().createQuery(hql);
        if(values != null){
            for(int i = 0; i < values.size(); i++) {
                query.setParameter(i, values.get(i));
            }
        }
        return (Long) query.uniqueResult();
    }
 
    /**
     * <HQL分页查询>
     * @param hql HQL语句
     * @param countHql 查询记录条数的HQL语句
     * @param pageNo 下一页
     * @param pageSize 一页总条数
     * @param values 不定Object数组参数
     * @return PageResults的封装类，里面包含了页码的信息以及查询的数据List集合
     * @see com.leju.base.util.IBaseDao#findPageByFetchedHql(java.lang.String, java.lang.String, int, int, java.lang.List<Object>)
     */
   
 
    /**
     * @return the sessionFactory
     */
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
 
    /**
     * @param sessionFactory the sessionFactory to set
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
     
    /**
     * 
     * @return session
     */
    public Session getSession() {
        //需要开启事物，才能得到CurrentSession
        return sessionFactory.getCurrentSession();
    }
     
    /**
     * 
     * 设置每行批处理参数
     * 
     * @param ps
     * @param pos ?占位符索引，从0开始
     * @param data
     * @throws SQLException
     * @see [类、类#方法、类#成员]
     */
    private void setParameter(PreparedStatement ps, int pos, Object data)
        throws SQLException
    {
        if (data == null)
        {
            ps.setNull(pos + 1, Types.VARCHAR);
            return;
        }
        Class dataCls = data.getClass();
        if (String.class.equals(dataCls))
        {
            ps.setString(pos + 1, (String)data);
        }
        else if (boolean.class.equals(dataCls))
        {
            ps.setBoolean(pos + 1, ((Boolean)data));
        }
        else if (int.class.equals(dataCls))
        {
            ps.setInt(pos + 1, (Integer)data);
        }
        else if (double.class.equals(dataCls))
        {
            ps.setDouble(pos + 1, (Double)data);
        }
        else if (Date.class.equals(dataCls))
        {
            Date val = (Date)data;
            ps.setTimestamp(pos + 1, new Timestamp(val.getTime()));
        }
        else if (BigDecimal.class.equals(dataCls))
        {
            ps.setBigDecimal(pos + 1, (BigDecimal)data);
        }
        else
        {
            // 未知类型
            ps.setObject(pos + 1, data);
        }
    }
    
	/** 
	 * 
	 * <批量保存>
	 * @see com.leju.base.hibernate.IBaseDao#batchSave(java.util.Collection)
	 */
	@Override
	public void batchSave(Collection<T> entities) {
		 for(Object entity : entities) {
	            this.getSession().save(entity);
	     }
	}
	
	/** 
	 * 
	 * <批量保存或更新>
	 * @see com.leju.base.hibernate.IBaseDao#batchSaveOrUpdateOne(java.util.Collection)
	 */
	@Override
	public void batchSaveOrUpdateOne(Collection<T> entities) {
		 for(Object entity : entities) {
	            this.getSession().saveOrUpdate(entity);
	        }
	}
	     
	  
	    /** 
	 
	     * 根据HQL语句，获得查找总记录数的HQL语句 
	 
	     * 如： 
	 
	     * select ... from Orgnizationo where o.parent is null 
	 
	     * 经过转换，可以得到： 
	 
	     * select count(*) from Orgnizationo where o.parent is null 
	 
	     * @param hql 
	 
	     * @return 
	 
	     */  
	  
	    private String getCountQuery(String hql){  
	  
	       int index = hql.indexOf("from");  
	  
	       if(index != -1){  
	  
	           return"select count(*) " + hql.substring(index);  
	  
	       }
	       return "0";
	    }

		@Override
		public Object getMaxBySql(String sql) {
			Object obj = this.getSession().createQuery(sql).uniqueResult();
			return obj;
		}

		/* (non-Javadoc)
		 * @see com.flea.common.dao.BaseDao#findAll()
		 */
		@Override
		public List<T> findAll() {
			Session session = getSession();
			Query query = session.createQuery("from  "+getEntityClass().getName());
					//+" where isEffective="+Common.FLAG_ACTIVATION);
			return query.list();
		}

		
		/**
		 * 分页查询
		 * @param page
		 * @return
		 */
		public Page<T> find(Page<T> page) {
			return find(page, createDetachedCriteria());
		}
		

		/**
		 * 使用检索标准对象分页查询
		 * @param page
		 * @param detachedCriteria
		 * @return
		 */
		public Page<T> find(Page<T> page, DetachedCriteria detachedCriteria) {
			return find(page, detachedCriteria, Criteria.DISTINCT_ROOT_ENTITY);
		}
		
		/**
		 * 使用检索标准对象分页查询
		 * @param page
		 * @param detachedCriteria
		 * @param resultTransformer
		 * @return
		 */
		@SuppressWarnings("unchecked")
		public Page<T> find(Page<T> page, DetachedCriteria detachedCriteria, ResultTransformer resultTransformer) {
			// get count
			if (!page.isDisabled() && !page.isNotCount()){
				page.setCount(count(detachedCriteria));
				if (page.getCount() < 1) {
					return page;
				}
			}
			Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());
			criteria.setResultTransformer(resultTransformer);
			// set page
			if (!page.isDisabled()){
		        criteria.setFirstResult(page.getFirstResult());
		        criteria.setMaxResults(page.getMaxResults()); 
			}
			// order by
			if (StringUtils.isNotBlank(page.getOrderBy())){
				for (String order : StringUtils.split(page.getOrderBy(), ",")){
					String[] o = StringUtils.split(order, " ");
					if (o.length==1){
						criteria.addOrder(Order.asc(o[0]));
					}else if (o.length==2){
						if ("DESC".equals(o[1].toUpperCase())){
							criteria.addOrder(Order.desc(o[0]));
						}else{
							criteria.addOrder(Order.asc(o[0]));
						}
					}
				}
			}
			page.setList(criteria.list());
			return page;
		}

		/**
		 * 使用检索标准对象查询
		 * @param detachedCriteria
		 * @return
		 */
		public List<T> find(DetachedCriteria detachedCriteria) {
			return find(detachedCriteria, Criteria.DISTINCT_ROOT_ENTITY);
		}
		
		/**
		 * 使用检索标准对象查询
		 * @param detachedCriteria
		 * @param resultTransformer
		 * @return
		 */
		@SuppressWarnings("unchecked")
		public List<T> find(DetachedCriteria detachedCriteria, ResultTransformer resultTransformer) {
			Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());
			criteria.setResultTransformer(resultTransformer);
			return criteria.list(); 
		}
		
		/**
		 * 使用检索标准对象查询记录数
		 * @param detachedCriteria
		 * @return
		 */
		@SuppressWarnings("rawtypes")
		public long count(DetachedCriteria detachedCriteria) {
			Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());
			long totalCount = 0;
			try {
				// Get orders
				Field field = CriteriaImpl.class.getDeclaredField("orderEntries");
				field.setAccessible(true);
				List orderEntrys = (List)field.get(criteria);
				// Remove orders
				field.set(criteria, new ArrayList());
				// Get count
				criteria.setProjection(Projections.rowCount());
				totalCount = Long.valueOf(criteria.uniqueResult().toString());
				// Clean count
				criteria.setProjection(null);
				// Restore orders
				field.set(criteria, orderEntrys);
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			return totalCount;
		}

		/**
		 * 创建与会话无关的检索标准对象
		 * @param criterions Restrictions.eq("name", value);
		 * @return 
		 */
		public DetachedCriteria createDetachedCriteria(Criterion... criterions) {
			DetachedCriteria dc = DetachedCriteria.forClass(getEntityClass());
			for (Criterion c : criterions) {
				dc.add(c);
			}
			return dc;
		}

		/* (non-Javadoc)
		 * @see com.flea.common.dao.BaseDao#changeStatusToSotpOrUndelete(java.lang.String, java.lang.Object, java.lang.String, java.lang.Object)
		 */
		@Override
		public int changeStatusToSotpOrUndelete(String statusKey,
				Object statusValue, String idKey, Object idValue) {
			Session session = getSession();
			Query query = session.createQuery("update "+getEntityClass().getName()+" set "+statusKey+"=? where "+idKey+"=? and "+statusKey+"!=?");
			query.setParameter(0, statusValue);
			query.setParameter(1, idValue);
			query.setParameter(2, Common.FLAG_DELETED);
			return query.executeUpdate();
		}
		
		 /**
		 * SQL 分页查询
		 * @param page
		 * @param sqlString
		 * @param resultClass
		 * @param parameter
		 * @return
		 */
	    @SuppressWarnings("unchecked")
		public <E> Page<E> findBySql(Page<E> page, String sqlString,Class<?> resultClass){
			// get count
	    	if (!page.isDisabled() && !page.isNotCount()){
		        String countSqlString = "select count(*) " ;
//		        page.setCount(Long.valueOf(createSqlQuery(countSqlString, parameter).uniqueResult().toString()));
		        Query query = getSession().createSQLQuery(sqlString);
		        List<Object> list = query.list();
		        if (list.size() > 0){
		        	page.setCount(Long.valueOf(list.get(0).toString()));
		        }else{
		        	page.setCount(list.size());
		        }
				if (page.getCount() < 1) {
					return page;
				}
	    	}
	    	// order by
	    	String sql = sqlString;
			if (StringUtils.isNotBlank(page.getOrderBy())){
				sql += " order by " + page.getOrderBy();
			}
	          SQLQuery query = getSession().createSQLQuery(sqlString);
			// set page
	        if (!page.isDisabled()){
		        query.setFirstResult(page.getFirstResult());
		        query.setMaxResults(page.getMaxResults()); 
	        }
	        page.setList(query.list());
			return page;
	    }
	    @Override
	    public Object getOneBySQL(String sqlString, List<Object> values) {
	    	Query query = this.getSession().createSQLQuery(sqlString);
	        if (values != null)
	        {
	            for (int i = 0; i < values.size(); i++)
	            {
	                query.setParameter(i, values.get(i));
	            }
	        }
	    	return query.uniqueResult();
	    }
}
