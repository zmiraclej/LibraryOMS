package com.flea.modules.ebook.dao;

import org.springframework.stereotype.Repository;

import com.flea.common.dao.BaseDao;
import com.flea.modules.ebook.pojo.Ebook;

/**
 * 电子书DAO接口
 * @author bruce
 * @version 2016-06-18
 */
@Repository
public interface EbookDao extends BaseDao<Ebook,Integer> {
	
}
