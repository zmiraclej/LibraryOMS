//package fleasite;
//
//import src.main.java.com.flea.common.SolrContent;
//import src.main.java.com.flea.common.util.FileUtils;
//import src.main.java.com.flea.modules.customer.dao.CutomerLibraryDao;
//import src.main.java.com.flea.modules.customer.service.CutomerLibraryService;
//import src.main.java.com.flea.modules.ebook.pojo.Ebook;
//import src.main.java.com.flea.modules.ebook.service.EbookService;
//import src.main.java.com.flea.modules.news.action.NewsController;
//import src.main.java.com.flea.modules.news.dao.NewsDao;
//import src.main.java.com.flea.modules.ranking.dao.BorrowDao;
//import src.main.java.com.flea.modules.report.dao.CirculatedReportDao;
//import src.main.java.com.flea.modules.report.dao.VLibraryBookDao;
//import src.main.java.com.flea.modules.system.dao.LibraryDao;
//import src.main.java.com.flea.modules.system.pojo.Library;
//import src.main.java.com.flea.modules.system.service.LibraryService;
//import src.main.java.com.flea.modules.system.service.WarehouseService;
//
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = "classpath:spring-context.xml")
//@Transactional
//@TransactionConfiguration(defaultRollback=false,transactionManager="transactionManager")
//public class TestService{
//	/**
//	 * Logger for this class
//	 */
//	private static final Logger log = Logger.getLogger(TestService.class);
//
//	@Resource
//	private LibraryDao libraryDao;
//	
//	@Resource
//	private WarehouseService service;
//	
//	@Resource
//	private LibraryService libaryService;
//	
//	@Resource(name="deptSolrContent")
//	private SolrContent deptSolrContent;
//	
//	@Autowired
//	private EbookService ebookService;
//	
//	@Resource
//	private LibraryDao lbdao;
//	
//	@Autowired
//	private CutomerLibraryService cutomerLibraryService;
//	
//	@Autowired
//	private NewsController newsController;
//	
//	@Autowired
//	private CutomerLibraryDao cutomerLibraryDao;
//	
//	@Resource(name="localBookSolrContent")
//	protected SolrContent goodSolrContent;
//	
//	@Autowired
//	private NewsDao dao;
//	
//	@Autowired
//	private VLibraryBookDao bookDao;
//	
//	@Autowired
//	private BorrowDao borrowDao; 
//	@Autowired
//	private CirculatedReportDao circulatedReportDao;
//	/* @Test
//	public void testFindAll() {
//		List<Library> libraries = libaryService.findAll();
//		for(Library library:libraries){
//			 libaryService.start(library.getId());
//		}
//		// cutomerLibraryDao.findLevelByAreaCode(510704, 16);
//	}*/
//	 
//	@org.junit.Test
//	@Ignore
//	public void tests() {
//		bookDao.showTopSelect("四川省-绵阳市-涪城区","公共图书馆");
//	}
//	
//	
//	
//	@Test
//	@Ignore
//	public void testRead(){
//		
//		System.out.println();
//		File  files = new File("D:/Program Files/apache-tomcat-7.0.65/webapps/ebooks/epub");
//		for(File file:files.listFiles()){
//			String fullName =file.getName();;	
//			Ebook  ebook =ebookService.findByName(fullName.substring(0,fullName.lastIndexOf(".")));
//			if(ebook!=null){
//				fullName = ebook.getFile();
//				String fileName = fullName.substring(fullName.lastIndexOf("/")+1);
//				String prexName = fullName.substring(0,fullName.lastIndexOf("/")+1);
//				System.out.println(fileName);
//				System.out.println(prexName);
//				ebook.setFile(prexName+ebook.getId()+".epub");
//				ebookService.saveOne(ebook);
//				try {
//					FileUtils.moveFile(file, new File("D:/Program Files/apache-tomcat-7.0.65/webapps/ebooks/epubs/"+ebook.getId()+".epub"));
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//	}
//	
//	@Test
//	@Ignore
//	public void testReads(){
//		System.out.println();
//		File  files = new File("D:/Program Files/apache-tomcat-7.0.65/webapps/ebooks/epubs");
//		for(File file:files.listFiles()){
//			String fullName =file.getName();;	
//			Ebook  ebook =ebookService.findByName(fullName.substring(0,fullName.lastIndexOf(".")));
//			if(ebook!=null){
//				fullName = ebook.getFile();
//				String fileName = fullName.substring(fullName.lastIndexOf("/")+1);
//				String prexName = fullName.substring(0,fullName.lastIndexOf("/")+1);
//				System.out.println(fileName);
//				System.out.println(prexName);
//				ebook.setFile(prexName+ebook.getId()+".epub");
//				ebookService.saveOne(ebook);
//				  ebook.getId();
//				try {
//					FileUtils.moveFile(file, new File("D:/Program Files/apache-tomcat-7.0.65/webapps/ebooks/epubs/"+ebook.getId()+".epub"));
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}
//	}
//	
//	/**
//	 * 查找没有持久化完的图书馆
//	 */
//	 @org.junit.Test
//	 @Ignore
//	 public void findBookSum() {
//		 List<Library> list = libraryDao.findAll();
//	
//		 List<Map<String, String>> mapList = new ArrayList<Map<String,String>>();
//		 for(Library library:list){
//			 String libCode =library.getHallCode();
//			 int sum = libraryDao.findLibraryBooksSumByCode(libCode);
//			 int dSum = totalCount(libCode);
//			 if(sum!=dSum){
//				 Map<String,String> map = new HashMap<String, String>();
//				 map.put(libCode, String.valueOf(sum)+","+String.valueOf(dSum));
//				 mapList.add(map);
//			 }
//		 }
//		 System.out.println("----------------------");
//		 for(Map<String, String> maps:mapList){
//			 for(String key:maps.keySet()){
//				 System.out.print("馆号--"+key);
//				 String sum = maps.get(key);
//				 System.out.print("关系数据库--"+sum.split(",")[0]);
//				 System.out.println("文档数据库--"+sum.split(",")[1]);
//			 }
//			
//		 }
//	
//	}
//	 
//	 @Ignore
//	 public  Integer totalCount(String libCode){
//		   SolrClient client = goodSolrContent.createClient();
//			SolrQuery query = new SolrQuery("*");
//			query.addFilterQuery("libCode:"+libCode);
//			query.setParam(GroupParams.GROUP, true);
//			query.setParam(GroupParams.GROUP_FIELD,"localBookId");
//			query.setParam("group.ngroups", true);
//				QueryResponse response;
//				try {
//					response = client.query(query);
//					GroupResponse gr = response.getGroupResponse();
//					for(GroupCommand document:gr.getValues()){
//						Integer sum = document.getNGroups();
//						return sum;
//					}
//				} catch (SolrServerException e) {
//					e.printStackTrace();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//				return 0;
//	}
//	 
//	 /*@org.junit.Test
//	 public void test1() {
//		 libraryDao.findLibarary(null,null);
//	}*/
//
//	@Test
//	@Ignore
//	public void removeLibrary() {
//	SolrClient  client = deptSolrContent.createClient();
//	SolrQuery query = new SolrQuery("*");
//	try {
//		query.setRows(100);
//		QueryResponse  response =  client.query(query);
//		SolrDocumentList list =  response.getResults();
//		for(SolrDocument document:list){
//			String libId =(String)document.getFieldValue("libId");
//			String hallCode =(String)document.getFieldValue("hallCode");
//			System.out.println(hallCode);
//			String hqlString =" from Library where hallCode ='"+hallCode+"' and id="+Integer.parseInt(libId);
////			Library  library =	libaryService.getByHQL(hqlString, null);
////			if("AABC".equals(hallCode)){
////				System.out.println(libId);
////			}
////			if(library==null){
////				System.out.println(libId);
////				libaryService.removeSolrIndex(Integer.parseInt(libId));
////			}
//		}
//	} catch (SolrServerException | IOException e) {
//		e.printStackTrace();
//	}
//	}
//	
//	@org.junit.Test
//	public void test() {
//		//bookDao.showTopSelect("四川省-绵阳市-涪城区","公共图书馆");
//		//catalogueReportDao.find(null,"四川省-绵阳市-游仙区","社会馆");
//		/*QueryCriteria qc = new QueryCriteria();
//		qc.setArea("四川省-绵阳市-涪城区");
//		qc.setLib("社区书屋");
//		qc.setOption("0");
//		clicklikeDao.find(null,qc);*/
//		//readerReportDaoImpl.find(null,"四川省-绵阳市-涪城区","社区书屋");
//		//circulatedReportDao.find(null,"四川省-绵阳市-涪城区","社区书屋");
//		bookDao.getAllLibLevel();
//	}
//	
//}
