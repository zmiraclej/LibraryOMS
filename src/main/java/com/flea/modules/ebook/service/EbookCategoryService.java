package com.flea.modules.ebook.service;


import com.flea.common.pojo.Page;
import com.flea.common.service.BaseService;
import com.flea.modules.ebook.pojo.EbookCategory;

/**
 * 电子书分类Service
 * @author bruce
 * @version 2016-07-04
 */

public interface EbookCategoryService extends BaseService<EbookCategory,Integer> {

   	Page<EbookCategory> find(Page<EbookCategory> page,EbookCategory ebookCategory);
	
}
