package com.flea.modules.ebook.service.impl;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flea.common.pojo.Page;
import com.flea.common.service.impl.BaseServiceImpl;
import com.flea.modules.ebook.dao.EbookCategoryDao;
import com.flea.modules.ebook.pojo.EbookCategory;
import com.flea.modules.ebook.service.EbookCategoryService;

/**
 * 电子书分类Service
 * @author bruce
 * @version 2016-07-04
 */
 @Service
public class EbookCategoryServiceImpl extends BaseServiceImpl<EbookCategory,Integer> implements EbookCategoryService{

	@Autowired
	private EbookCategoryDao ebookCategoryDao;
	
	
	@Autowired
	public  EbookCategoryServiceImpl(EbookCategoryDao ebookCategoryDao) {
		super(ebookCategoryDao);
		this.ebookCategoryDao = ebookCategoryDao;
	}
	
	@Override
	public Page<EbookCategory> find(Page<EbookCategory> page,EbookCategory ebookCategory) {
		DetachedCriteria dc = ebookCategoryDao.createDetachedCriteria();
//		if(StringUtils.isNotEmpty(ebookCategory.getName())){
//			dc.add(Restrictions.eq("name", ebookCategory.getName()));
//		}
		return ebookCategoryDao.find(page, dc);
	}
	
}
