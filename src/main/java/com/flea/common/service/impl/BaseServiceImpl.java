/**  
* @Package com.flea.common.service.impl
* @Description: TODO
* @author bruce
* @date 2015年12月17日 下午2:47:25
* @version V1.0  
*/ 
package com.flea.common.service.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flea.common.dao.BaseDao;
import com.flea.common.pojo.Page;
import com.flea.common.service.BaseService;

/**
 * @author bruce
 * @2015年12月17日 下午2:47:25
 */

@Service
public class BaseServiceImpl<T,ID extends Serializable>  implements BaseService<T, ID> {
	
	
	protected final transient Log logger = LogFactory.getLog(BaseServiceImpl.class);
	
	protected BaseDao<T, Serializable> dao;

	public BaseServiceImpl() {
	}
	

	public BaseServiceImpl(BaseDao dao) {
		this.dao = dao;
	}

	/* (non-Javadoc)
	 * @see com.flea.common.service.BaseService#saveOne(java.lang.Object)
	 */
	@Override
	public void saveOne(T t) {
		dao.saveOne(t);
		
	}

	/* (non-Javadoc)
	 * @see com.flea.common.service.BaseService#batchSave(java.util.Collection)
	 */
	@Override
	public void batchSave(Collection<T> entities) {
		dao.batchSave(entities);
	}

	/* (non-Javadoc)
	 * @see com.flea.common.service.BaseService#saveOrUpdateOne(java.lang.Object)
	 */
	@Override
	public void saveOrUpdateOne(T t) {
		dao.saveOrUpdateOne(t);
	}

	/* (non-Javadoc)
	 * @see com.flea.common.service.BaseService#batchSaveOrUpdateOne(java.util.Collection)
	 */
	@Override
	public void batchSaveOrUpdateOne(Collection<T> entities) {
		dao.batchSaveOrUpdateOne(entities);
	}

	/* (non-Javadoc)
	 * @see com.flea.common.service.BaseService#load(int)
	 */
	@Override
	public T load(int id) {
		return dao.load(id);
	}

	/* (non-Javadoc)
	 * @see com.flea.common.service.BaseService#getOne(int)
	 */
	@Override
	public T getOne(int id) {
		return dao.getOne(id);
	}

	/* (non-Javadoc)
	 * @see com.flea.common.service.BaseService#contains(java.lang.Object)
	 */
	@Override
	public boolean contains(T t) {
		return dao.contains(t);
	}



	/* (non-Javadoc)
	 * @see com.flea.common.service.BaseService#deleteById(int)
	 */
	@Override
	public boolean deleteById(int id) {
		return dao.deleteById(id)>0?true:false;
	}

	/* (non-Javadoc)
	 * @see com.flea.common.service.BaseService#batchDelete(java.util.Collection)
	 */
	@Override
	public void batchDelete(Collection<T> entities) {
		dao.batchDelete(entities);
	}

	/* (non-Javadoc)
	 * @see com.flea.common.service.BaseService#queryHql(java.lang.String, java.util.List)
	 */
	@Override
	public void queryHql(String hqlString, List<Object> values) {
		dao.queryHql(hqlString, values);
	}

	/* (non-Javadoc)
	 * @see com.flea.common.service.BaseService#querySql(java.lang.String, java.util.List)
	 */
	@Override
	public void querySql(String sqlString, List<Object> values) {
		dao.querySql(sqlString, values);
	}

	/* (non-Javadoc)
	 * @see com.flea.common.service.BaseService#getByHQL(java.lang.String, java.util.List)
	 */
	@Override
	public T getByHQL(String hqlString, List<Object> values) {
		return dao.getByHQL(hqlString, values);
	}

	/* (non-Javadoc)
	 * @see com.flea.common.service.BaseService#getBySQL(java.lang.String, java.util.List)
	 */
	@Override
	public T getBySQL(String sqlString, List<Object> values) {
		return dao.getBySQL(sqlString, values);
	}

	/* (non-Javadoc)
	 * @see com.flea.common.service.BaseService#getListByHQL(java.lang.String, java.util.List)
	 */
	@Override
	public List<T> getListByHQL(String hqlString, List<Object> values) {
		return dao.getListByHQL(hqlString, values);
	}

	/* (non-Javadoc)
	 * @see com.flea.common.service.BaseService#getListBySQL(java.lang.String, java.util.List)
	 */
	@Override
	public List<T> getListBySQL(String sqlString, List<Object> values) {
		return dao.getListBySQL(sqlString, values);
	}

	/* (non-Javadoc)
	 * @see com.flea.common.service.BaseService#refresh(java.lang.Object)
	 */
	@Override
	public void refresh(T t) {
		dao.refresh(t);
	}

	/* (non-Javadoc)
	 * @see com.flea.common.service.BaseService#updateOne(java.lang.Object)
	 */
	@Override
	public void updateOne(T t) {
		dao.updateOne(t);
	}

	/* (non-Javadoc)
	 * @see com.flea.common.service.BaseService#countByHql(java.lang.String, java.util.List)
	 */
	@Override
	public Long countByHql(String hql, List<Object> values) {
		return dao.countByHql(hql, values);
	}

	/* (non-Javadoc)
	 * @see com.flea.common.service.BaseService#findAll()
	 */
	@Override
	public List<T> findAll() {
		return dao.findAll();
	}


}
