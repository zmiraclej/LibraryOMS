package com.flea.modules.ebook.dao.impl;


import org.springframework.stereotype.Repository;

import com.flea.common.dao.impl.BaseDaoImpl;
import com.flea.modules.ebook.dao.EbookErrorDataDao;
import com.flea.modules.ebook.pojo.EbookError;
/**
 * 
 * @ClassName: EbookErrorDaoImpl
 * @Description: 电子书的错误信息实现类
 * @author QL
 * @date 2017年1月16日 下午6:31:18
 */
@Repository
public class EbookErrorDaoImpl extends BaseDaoImpl<EbookError, Integer> implements EbookErrorDataDao{

}
