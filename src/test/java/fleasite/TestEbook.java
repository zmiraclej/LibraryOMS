package fleasite;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.GroupCommand;
import org.apache.solr.client.solrj.response.GroupResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.GroupParams;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.flea.common.SolrContent;
import com.flea.modules.customer.dao.CutomerLibraryDao;
import com.flea.modules.customer.service.CutomerLibraryService;
import com.flea.modules.ebook.pojo.EbookCategory;
import com.flea.modules.ebook.service.EbookCategoryService;
import com.flea.modules.news.action.NewsController;
import com.flea.modules.news.dao.NewsDao;
import com.flea.modules.system.dao.LibraryDao;
import com.flea.modules.system.pojo.Library;
import com.flea.modules.system.service.LibraryService;
import com.flea.modules.system.service.WarehouseService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-context.xml")
@Transactional
@TransactionConfiguration(defaultRollback=false,transactionManager="transactionManager")
public class TestEbook{
	/**
	 * Logger for this class
	 */
	private static final Logger log = Logger.getLogger(TestEbook.class);
	
	@Resource
	private EbookCategoryService  eBookCategoryService;
	
	@Resource
	private LibraryService  libraryService;
	

	 @org.junit.Test
	 public void testSave() {
		 String categoryString ="小说、经营、成功励志、人文社科、文学、计算机、历史、生活、动漫绘本、传记、原创文学、两性情感、自然科学、少儿、旅游、政治军事、艺术、法律、教育、外语、进口原版";
		 int i=1;
		 for(String name:categoryString.split("、")){
			 EbookCategory  category = new EbookCategory();
			 category.setCategoryName(name);
			 category.setOrder(i);
			 i++;
			 eBookCategoryService.saveOne(category);
		 }
		
	}
	 
	 /**
	 * 
	 */
	@Test
	public void testFindByHallCode() {
		Library library = libraryService.findByHallCode("ATTU");
		System.out.println(library.getHallCode());
	}
}
