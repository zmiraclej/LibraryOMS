package com.flea.common.util.exportDaoImpl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.flea.common.dao.impl.BaseDaoImpl;
import com.flea.common.util.exportDao.ExportExcelDao;
import com.flea.modules.ebook.pojo.EbookError;

/**
 * 
 * @ClassName: ExportExcelDaoImpl
 * @Description:实现类
 * @author QL
 * @date 2017年1月19日 上午11:27:47
 */
@Repository  
public class ExportExcelDaoImpl extends BaseDaoImpl<EbookError, Long>implements ExportExcelDao {

	@Resource
	private SessionFactory factory;

	/**
	 * 以EbookError表为例导出Excel
	 */
	@SuppressWarnings("unchecked")
	public List<EbookError> exportExcel(String hql) {
		Session session = factory.getCurrentSession();
		List<EbookError> list = session.createQuery(hql).list();
		return list;
	}
	
	
	@Override
	public int deleteById(int id) {
		String hql="delete from EbookError where user.id = "+id;
		Query query =getSession().createQuery(hql);
		return query.executeUpdate();
	}

}
