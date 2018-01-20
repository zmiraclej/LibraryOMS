/**  
* @Package fleasite
* @Description: TODO
* @author bruce
* @date 2016年6月17日 上午10:22:11
* @version V1.0  
*/ 
package fleasite;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.xml.namespace.QName;

import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Date;
import nl.siegmann.epublib.domain.Metadata;
import nl.siegmann.epublib.epub.EpubReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.flea.common.util.FileUtils;
import com.flea.modules.ebook.pojo.Ebook;
import com.flea.modules.ebook.service.EbookService;
import com.flea.modules.ebook.util.HttpUtil;

/**
 * @author bruce
 * @2016年6月17日 上午10:22:11
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = "classpath:spring-context.xml")
//@Transactional
//@TransactionConfiguration(defaultRollback=false,transactionManager="transactionManager")
public class TestEpublib {
	 
	@Resource
	private EbookService ebookService;
	protected Log log = LogFactory.getLog(this.getClass());
	
	String imgPath ="c://img/";
	
	
	@Test
	public void testUnzip() {
		String file ="D:\\Program Files\\apache-tomcat-7.0.65\\webapps\\ebooks\\demo1.zip";
		String path ="D:\\Program Files\\apache-tomcat-7.0.65\\webapps\\ebooks\\";
		try {
			//FileUtils.unZipFiles(file, path);
		
			FileUtils.unZipFiles(file, path+"temp/");//解压
		//	ZipUtil.unzipEpub(file, path);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void testRead() {
		File file = new File("C://epub//");
		if(file.isDirectory()){
			for(File  book:file.listFiles()){
				  String fileName=book.getName();
				  String prefix=fileName.substring(fileName.lastIndexOf(".")+1);
				  if("epub".equals(prefix))
				readSaveBook(book);
			}
		}
	}
	
	public void readBook(File file,String isbn){
		Ebook  ebook = new Ebook();
		String url = "http://api.douban.com/v2/book/isbn/:"+isbn+"?apikey=0ee1696aa05594e12ca43c3fe100bea3";
		String jsonString = HttpUtil.http_get(url);//json 字符串
		JSONObject json = JSONObject.parseObject(jsonString);
		String author_info="",catalog="",summary ="";
		if(json.containsKey("author_intro")){//作者信息
			 author_info = json.getString("author_intro");
		}
		if(json.containsKey("catalog")){
			 catalog = json.getString("catalog");//目录
		} 
		if(json.containsKey("summary")){
			 summary = json.getString("summary");//内容简介
		}
		
		String tiltle = json.get("title").toString();
		String publisher = json.get("publisher").toString();
		
		
		
	}
	
	protected String saveImge(InputStream input,String imgeName){
		System.out.println("---download img to--"+imgPath+imgeName+".jpg");
		File file = new File(imgPath+imgeName+".jpg");
		log.info("saveImge>imgePath:"+file.getPath());
		int len =0;
		FileOutputStream output = null;
		try {
			byte[] buffer = new byte[1024];
			output = new FileOutputStream(file);
			while((len=input.read(buffer))!=-1){
				output.write(buffer, 0, len);
			}
		} catch (FileNotFoundException e) {
			log.error(e);
			return "";
		} catch (IOException e) {
			log.error(e);
			return "";
		}finally{
			try {
				if (input!=null) {
						input.close();
				}
				if (output!=null) {
					output.close();;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "pic/"+imgeName+".jpg";
	}
	
	public void readSaveBook(File file){
		EpubReader  epReader = new EpubReader();
		try {
			Ebook  ebook = new Ebook();
			Book book = epReader.readEpub(new FileInputStream(file));
			ebook.setFile("ebooks/"+file.getName());
			String title = book.getTitle();
			System.out.println("title---"+title);
			ebook.setBookName(title);
			 Metadata metadata = book.getMetadata();
			 List<String> publishers = metadata.getPublishers();
			 for(String publisher:publishers){
				 System.out.println("出版社--"+publisher);
				 ebook.setPublisher(publisher);
			 }
			 
			 List<String> descriptions = metadata.getDescriptions();
			 for(String description:descriptions){
				 System.out.println("简介--"+description);
				 ebook.setSummary(description);
			 }
			 List<String>    subStrings =   metadata.getSubjects();
			 for(String subString:subStrings){
				 System.out.println("类型--"+subString);
			 }
			 List<Date>  dates =  metadata.getDates();
			 for(Date date:dates){
				 System.out.println("出版日期--"+date.getValue());
				 ebook.setPublishDate(date.getValue());
			 }
			 List<Author>   authors =  metadata.getContributors();
			 for(Author author:authors){
				 System.out.println("作者--"+author.getFirstname());
				 ebook.setAuthor(author.getFirstname());
			 }
			 Map<QName, String> map =  metadata.getOtherProperties();
			 for(QName key:map.keySet()){
				  Object object = map.get(key.toString());
				  System.out.println(object);
			 }
			 //ebookService.saveOne(ebook);
			 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 电子书持久化文档数据库
	 */
	 @Test
	public void testFindAll() {
		 ebookService.start(8);
	}
}
