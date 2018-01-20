package com.flea.modules.ebook.dao.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.flea.common.dao.impl.BaseDaoImpl;
import com.flea.modules.ebook.dao.EbookDao;
import com.flea.modules.ebook.pojo.Ebook;

/**
 * 电子书DAO接口
 * @author bruce
 * @version 2016-06-18
 */
@Repository
public class EbookDaoImpl extends BaseDaoImpl<Ebook,Integer> implements EbookDao{

	@Override
	public int deleteById(int id) {
		Query query = getSession().createQuery("delete from Ebook where id=?");
		query.setInteger(0, id);
		return query.executeUpdate();
	}
	
	
}
