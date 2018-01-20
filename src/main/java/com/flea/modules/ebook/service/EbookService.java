package com.flea.modules.ebook.service;


import com.flea.common.pojo.Page;
import com.flea.common.service.BaseService;
import com.flea.modules.ebook.pojo.Ebook;
import com.flea.modules.news.pojo.News;

/**
 * 
 * @ClassName: EbookService
 * @Description: ebookService层
 * @author QL
 * @date 2017年2月23日 下午5:58:38
 */

public interface EbookService extends BaseService<Ebook,Integer> {

   	Page<Ebook> find(Page<Ebook> page,Ebook ebook,String search);
	
   	
	boolean start(Integer id);
	
	Ebook  findByName(String name);
	
	void saveSolrIndex(Ebook ebook);
	/**
	 * 
	 * @Title: updateEbook
	 * @Description: 电子书更新
	 * @param ebooks    设定文件
	 * @author QL
	 * @throws
	 */
	void updateEbook(Ebook ebooks);
	
	
	
	
}
