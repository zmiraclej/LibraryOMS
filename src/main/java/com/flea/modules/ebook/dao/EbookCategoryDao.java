package com.flea.modules.ebook.dao;

import org.springframework.stereotype.Repository;

import com.flea.common.dao.BaseDao;
import com.flea.modules.ebook.pojo.EbookCategory;

/**
 * 电子书分类DAO接口
 * @author bruce
 * @version 2016-07-04
 */
@Repository
public interface EbookCategoryDao extends BaseDao<EbookCategory,Integer> {
	
}
