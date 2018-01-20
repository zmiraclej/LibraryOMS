package com.flea.common.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.transform.ResultTransformer;

import com.flea.common.pojo.Page;

public interface BaseDao<T, ID extends Serializable> {

	/**
	 * <保存实体> <完整保存实体>
	 * 
	 * @param t
	 *            实体参数
	 */
	public abstract void saveOne(T t);

	/**
	 * <批量保存实体> <完整保存实体>
	 * 
	 * @param t
	 *            实体参数
	 */
	public abstract void batchSave(Collection<T> entities);

	/**
	 * <保存或者更新实体>
	 * 
	 * @param t
	 *            实体
	 */
	public abstract void saveOrUpdateOne(T t);

	/**
	 * <批量保存或者更新实体>
	 * 
	 * @param t
	 *            实体
	 */
	public abstract void batchSaveOrUpdateOne(Collection<T> entities);

	/**
	 * <load> <加载实体的load方法>
	 * 
	 * @param id
	 *            实体的id
	 * @return 查询出来的实体
	 */
	public abstract T load(int id);

	/**
	 * <get> <查找的get方法>
	 * 
	 * @param id
	 *            实体的id
	 * @return 查询出来的实体
	 */
	public abstract T getOne(int id);

	/**
	 * 查找所有
	 * 
	 * @return
	 */
	public abstract List<T> findAll();

	/**
	 * <contains>
	 * 
	 * @param t
	 *            实体
	 * @return 是否包含
	 */
	public abstract boolean contains(T t);
	
	/**
     * <delete>
     * <删除表中的t数据>
     * @param t 实体
     */
    public abstract void delOne(T t);
 
    /**
     * <根据ID删除数据>
     * @param Id 实体id
     * @return 是否删除成功
     */
    public abstract boolean delById(int id);

	/**
	 * <根据ID删除数据>
	 * 
	 * @param Id
	 *            实体id
	 * @return 是否删除成功
	 */
	public abstract int deleteById(int id);

	/**
	 * <删除所有>
	 * 
	 * @param entities
	 *            实体的Collection集合
	 */
	public abstract void batchDelete(Collection<T> entities);

	/**
	 * <执行Hql语句>
	 * 
	 * @param hqlString
	 *            hql
	 * @param values
	 *            不定参数数组
	 */
	public abstract void queryHql(String hqlString, List<Object> values);

	/**
	 * <执行Sql语句>
	 * 
	 * @param sqlString
	 *            sql
	 * @param values
	 *            不定参数数组
	 */
	public abstract void querySql(String sqlString, List<Object> values);

	/**
	 * <根据HQL语句查找唯一实体>
	 * 
	 * @param hqlString
	 *            HQL语句
	 * @param values
	 *            不定参数的Object数组
	 * @return 查询实体
	 */
	public abstract T getByHQL(String hqlString, List<Object> values);

	/**
	 * <根据SQL语句查找唯一实体>
	 * 
	 * @param sqlString
	 *            SQL语句
	 * @param values
	 *            不定参数的Object数组
	 * @return 查询实体
	 */
	public abstract T getBySQL(String sqlString, List<Object> values);

	/**
	 * <根据HQL语句，得到对应的list>
	 * 
	 * @param hqlString
	 *            HQL语句
	 * @param values
	 *            不定参数的Object数组
	 * @return 查询多个实体的List集合
	 */
	public abstract List<T> getListByHQL(String hqlString, List<Object> values);

	/**
	 * <根据SQL语句，得到对应的list>
	 * 
	 * @param sqlString
	 *            HQL语句
	 * @param values
	 *            不定参数的Object数组
	 * @return 查询多个实体的List集合
	 */
	public abstract List<T> getListBySQL(String sqlString, List<Object> values);

	/**
	 * <refresh>
	 * 
	 * @param t
	 *            实体
	 */
	public abstract void refresh(T t);

	/**
	 * <update>
	 * 
	 * @param t
	 *            实体
	 */
	public abstract void updateOne(T t);

	/**
	 * <根据HQL得到记录数>
	 * 
	 * @param hql
	 *            HQL语句
	 * @param values
	 *            不定参数的Object数组
	 * @return 记录总数
	 */
	public abstract Long countByHql(String hql, List<Object> values);

	/**
	 * 改变状态 不能改变状态为 Common.FLAG_DELETED 的值
	 * 
	 * @param statusKey
	 *            状态名
	 * @param statusValue
	 *            状态值
	 * @param idKey
	 *            id名
	 * @param idValue
	 *            Id值
	 * @return
	 */
	int changeStatusToSotpOrUndelete(String statusKey, Object statusValue, String idKey, Object idValue);

	/**
	 * @param sql
	 * @return 字段max值
	 */
	public abstract Object getMaxBySql(String sql);

	public DetachedCriteria createDetachedCriteria(Criterion... criterions);

	public Page<T> find(Page<T> page, DetachedCriteria detachedCriteria);

	/**
	 * 使用检索标准对象分页查询
	 * 
	 * @param page
	 * @param detachedCriteria
	 * @param resultTransformer
	 * @return
	 */
	public Page<T> find(Page<T> page, DetachedCriteria detachedCriteria, ResultTransformer resultTransformer);
	 /**
	 * SQL 万能查询
	 * @param sqlString SQL语句
	 * @param values    SQL参数
	 * @return
	 */
	public Object getOneBySQL(String sqlString, List<Object> values);

	/**
	 * <根据HQL语句，得到对应的limit list>
	 * 
	 * @param hqlString
	 *            HQL语句
	 * @param limit
	 *            limit数量
	 * @param values
	 *            不定参数的Object数组
	 * @return 查询多个实体的List集合
	 * @see com.leju.base.util.IBaseDao#getListByHQL(java.lang.String,
	 *      java.lang.List<Object>)
	 */
	public List<T> getListByHQL(String hqlString, Integer limit, List<Object> values);
}
