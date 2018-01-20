package com.flea.modules.ebook.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flea.common.pojo.Page;
import com.flea.common.service.impl.BaseServiceImpl;
import com.flea.common.util.ShiroUtils;
import com.flea.modules.ebook.dao.EbookCategoryDao;
import com.flea.modules.ebook.dao.EbookErrorDataDao;
import com.flea.modules.ebook.pojo.EbookError;
import com.flea.modules.ebook.service.EbookExportErrorDataService;
import com.flea.modules.system.pojo.LibraryImportErrorData;
/**
 * 
 * @ClassName: EbookExportErrorDataServiceImpl
 * @Description: 实现类
 * @author QL
 * @date 2017年1月16日 下午6:37:26
 */
@Service
public class EbookExportErrorDataServiceImpl extends BaseServiceImpl<EbookError,Integer> implements EbookExportErrorDataService{
    @Resource
    EbookErrorDataDao ebookErrorDataDao;
    @Autowired
	public  EbookExportErrorDataServiceImpl(EbookErrorDataDao ebookErrorDataDao) {
		super(ebookErrorDataDao);
		this.ebookErrorDataDao = ebookErrorDataDao;
	}
	@Override
	public Page<EbookError> find(Page<EbookError> page, EbookError ebookError) {
		DetachedCriteria dc = ebookErrorDataDao.createDetachedCriteria();
		Page<EbookError> errors = ebookErrorDataDao.find(page, dc);
		return errors;
	}
	/**
	 * @return
	 * 错误信息列表
	 */
	public List<EbookError> listExportErrorData(){
		StringBuffer buffer = new StringBuffer();
		buffer.append(" from EbookErrorData ");
		List<EbookError> list = ebookErrorDataDao.getListByHQL(buffer.toString(), null);
		return list;
	}
	/**
	 * 保存错误信息
	 */
	@Override
	public void saveOne(EbookError t) {
		ebookErrorDataDao.saveOne(t);
	}


}
