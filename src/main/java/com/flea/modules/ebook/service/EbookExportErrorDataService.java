package com.flea.modules.ebook.service;

import com.flea.common.pojo.Page;
import com.flea.common.service.BaseService;
import com.flea.modules.ebook.pojo.EbookError;
/**
 * 
 * @ClassName: EbookExportErrorData
 * @Description: 电子书错误信息的业务层
 * @author QL
 * @date 2017年1月16日 下午6:32:54
 */
public interface EbookExportErrorDataService extends BaseService<EbookError,Integer>{
	Page<EbookError> find(Page<EbookError> page,EbookError ebookError);
}
