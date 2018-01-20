package com.flea.modules.ebook.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flea.common.Common;
import com.flea.common.SolrContent;
import com.flea.common.pojo.Page;
import com.flea.common.pojo.User;
import com.flea.common.service.impl.BaseServiceImpl;
import com.flea.common.util.FileUtils;
import com.flea.common.util.ShiroUtils;
import com.flea.modules.customer.pojo.Customer;
import com.flea.modules.ebook.dao.EbookDao;
import com.flea.modules.ebook.pojo.Ebook;
import com.flea.modules.ebook.pojo.EbookError;
import com.flea.modules.ebook.pojo.EbookIndex;
import com.flea.modules.ebook.service.EbookService;
import com.flea.modules.news.pojo.News;

/**
 * 电子书Service
 * 
 * @author bruce
 * @version 2016-06-18
 */
@Service
public class EbookServiceImpl extends BaseServiceImpl<Ebook, Integer> implements
		EbookService {

	@Autowired
	private EbookDao ebookDao;

	@Autowired
	public EbookServiceImpl(EbookDao ebookDao) {
		super(ebookDao);
		this.ebookDao = ebookDao;
	}

	@Override
	public Page<Ebook> find(Page<Ebook> page, Ebook ebook, String search) {
		DetachedCriteria dc = ebookDao.createDetachedCriteria();
		if (StringUtils.isNotBlank(search)) {
			dc.add(Restrictions.or(Restrictions.like("bookName", "%" + search + "%"),Restrictions.like("isbn", "%" + search + "%")));
			
		}
		dc.addOrder(Order.desc("id"));
		return ebookDao.find(page, dc);
	}

	@Override
	public boolean deleteById(int id) {
		int rs = ebookDao.deleteById(id);
		if (rs > 0) {
			removeSolrIndex(id);
			return true;
		}
		return false;
	}

	@Override
	public void saveOne(Ebook t) {
		ebookDao.saveOne(t);
		// saveSolrIndex(t);
	}

	@Resource(name = "ebookSolrContent")
	protected SolrContent ebookSolrContent;

	/**
	 * 添加solr 索引
	 */
	public void saveSolrIndex(Ebook ebook) {
		SolrClient client = ebookSolrContent.createClient();
		EbookIndex index = new EbookIndex();
		index.setId(ebook.getId());
		index.setAuthor(ebook.getAuthor());
		index.setBookName(ebook.getBookName());
		index.setFile(ebook.getFile());
		index.setPublishDate(ebook.getPublishDate());
		if (ebook.getCategoryId() != null) {
			index.setCategoryId(ebook.getCategoryId());
		}
		index.setCategoryName(ebook.getCategoryName());
		index.setPublisher(ebook.getPublisher());
		index.setIsbn(ebook.getIsbn());
		index.setSummary(ebook.getSummary());
		index.setImage(ebook.getImage());
		index.setCreateDate(ebook.getCreateDate());
		try {
			client.addBean(index);
			client.commit();
		} catch (IOException e) {
			logger.error(e);
		} catch (SolrServerException e) {
			logger.error(e);
		} finally {
			ebookSolrContent.colse(client);
		}
	}

	/**
	 * 删除文档数据库
	 * 
	 * @param id
	 */
	public void removeSolrIndex(Integer id) {
		SolrClient client = ebookSolrContent.createClient();
		try {
			logger.info("removeSolrIndex>" + id);
			client.deleteById(String.valueOf(id));
			client.commit();
		} catch (SolrServerException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		} finally {
			ebookSolrContent.colse(client);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.flea.modules.ebook.service.EbookService#start(java.lang.Integer)
	 */
	@Override
	public boolean start(Integer id) {
		List<Ebook> list = ebookDao.findAll();
		for (Ebook ebook : list) {
			if (ebook.getId() > 70) {
				saveSolrIndex(ebook);
			}

		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.flea.modules.ebook.service.EbookService#findByName(java.lang.String)
	 */
	@Override
	public Ebook findByName(String name) {
		return ebookDao.getByHQL("from Ebook where bookName='" + name + "'",null);
	}
    /**
     * 更新方法
     */
	@Override
	public void updateEbook(Ebook ebooks) {
		ebookDao.saveOrUpdateOne(ebooks);
	}

}
