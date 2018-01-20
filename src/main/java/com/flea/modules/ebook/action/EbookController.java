package com.flea.modules.ebook.action;

import java.io.File;
import java.io.FileInputStream;
 
import java.io.IOException;
import java.io.InputStream;
 
import java.text.DateFormat;
 
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Metadata;
import nl.siegmann.epublib.epub.EpubReader;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
 
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
 
import org.hibernate.NonUniqueResultException;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
 
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
 
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
 
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONObject;
import com.flea.common.pojo.Page;
import com.flea.common.pojo.User;
import com.flea.common.util.Config;
 
import com.flea.common.util.FileUtils;
import com.flea.common.util.FtpUtils;
import com.flea.common.util.ImportExcel;
import com.flea.common.util.JsonUtil;
import com.flea.common.util.OSSUtils;
import com.flea.common.util.ShiroUtils;
import com.flea.common.util.exportService.ExportExcelService;
import com.flea.modules.ebook.pojo.Ebook;
import com.flea.modules.ebook.pojo.EbookCategory;
import com.flea.modules.ebook.pojo.EbookError;
import com.flea.modules.ebook.pojo.vo.FileMeta;
 
import com.flea.modules.ebook.service.EbookCategoryService;
import com.flea.modules.ebook.service.EbookExportErrorDataService;
import com.flea.modules.ebook.service.EbookService;

 
 
/**
 * 电子书Controller
 * 
 * @author bruce
 * @version 2016-06-18
 */
@Controller
@SessionAttributes("status")
@RequestMapping(value = "cms/ebook")
public class EbookController {
	@Autowired
	private ExportExcelService exportExcelService;
	@Autowired
	private EbookExportErrorDataService ebookExportErrorDataService;
	@Autowired
	private EbookService ebookService;
	@Autowired
	private EbookCategoryService ebookCategoryService;
 
	private Map<String, String> cellMap;

	private ImportExcel excel;

	SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");

	LinkedList<EbookError> ebookErrors = new LinkedList<EbookError>();

	OSSUtils ossUtils = new OSSUtils();

	FtpUtils ftpUtils = new FtpUtils();

	InputStream inputStream;
	Logger log = Logger.getLogger(EbookController.class);

	int sheetIndex;

	// private String imgPath = Config.getProperty("imgPathRoot");

	private String imgPath = "";

	LinkedList<FileMeta> fileMetas = new LinkedList<FileMeta>();

	Map<String, Integer> categoryMaps = new HashMap<String, Integer>();

	FileMeta fileMeta = null;
  
	@RequestMapping(value = { "list", "" })
	public ModelAndView list(Ebook ebook, String search,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
		log.info("i am here");
		Page<Ebook> page = ebookService.find(
				new Page<Ebook>(request, response), ebook, search);
		model.addAttribute("page", page);
		model.addAttribute("ebook", ebook);
		model.addAttribute("list", fileMetas);
		return new ModelAndView("ebook/book_list");
	}

	@RequestMapping(value = "form")
	public String form(Ebook ebook, Model model) {
		List<EbookCategory> categories = ebookCategoryService.findAll();
		if (categoryMaps.isEmpty()) {
			for (EbookCategory category : categories) {
				categoryMaps.put(category.getCategoryName(),
						category.getCategoryId());
			}
		}
		fileMetas.clear();
		model.addAttribute("categories", categories);
		model.addAttribute("ebook", ebook);
		model.addAttribute("list", fileMetas);
		return "ebook/book_add";
	}

	@RequestMapping(value = "add")
	public String add(Ebook ebook, Model model) {
		List<EbookCategory> categories = ebookCategoryService.findAll();
		if (categoryMaps.isEmpty()) {
			for (EbookCategory category : categories) {
				categoryMaps.put(category.getCategoryName(),
						category.getCategoryId());
			}
		}
		model.addAttribute("categories", categories);
		model.addAttribute("list", fileMetas);
		return "ebook/book_add";
	}
 
	@RequestMapping(value = "save")
	public ModelAndView save(Ebook ebook, Model model) {
		ebookService.saveOne(ebook);
		return new ModelAndView("redirect:list.html");
	}

	/**
	 * 导入电子书
	 * 
	 * @param filename
	 *            压缩包名
	 * @param excelname
	 *            索引文件名
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "upload", method = RequestMethod.POST)
	public String upload(String filename, String excelname,
			String autoCategoryName, String isbn,
			MultipartHttpServletRequest request, HttpServletResponse response,
			Model model) {
		HttpSession session = request.getSession();
		//设置session时间 -1表示永不过期
		session.setMaxInactiveInterval(-1);
		
		// 首先删除临时表ebookError的数据
		exportExcelService.detele();
		// 验证文件的大小
		boolean vlidate = FileUtils.validateFileSize(request, response);
		if (!vlidate) {
			model.addAttribute("error", "上传文件大小不能超过2G");
			return "error";
		}
		fileMetas.clear();
		// Cate this.ebookCategoryService.getOne(categoryId);
		// 文件上传
		FileUtils.uploadMutilpleFile(request, response, "ebooks");
		// D:/Program Files/apache-tomcat-7.0.65/webapps/ebooks/;
		String ebooksPath = Config.getProperty("ebooksFile");
		try {
			// excel = new ImportExcel(ebooksPath + excelname, 0);
			// .xls文件存放路径
			File file = new File(ebooksPath + excelname);
			FileInputStream input = new FileInputStream(file);
			// 兼容.xls和.xlsx文件格式
			excel = new ImportExcel(ebooksPath + excelname, input, 0, 0);
			cellMap = excel.getCell(0);
			Integer userId = ShiroUtils.getCurrentUser().getId();
			long current = System.currentTimeMillis();
			// 重命名文件名格式为temp_userID11210000091
			String targetFolder = ebooksPath + "temp//" + "temp_"
					+ String.valueOf(userId) + current;
			File temFile = new File(targetFolder);
			// File temFile = new File(ebooksPath + "temp/");
			if (!temFile.exists())
				temFile.mkdir();
			// zip压缩文件调用解压的方法
			FileUtils.unZipFiles(ebooksPath + filename, targetFolder);// 解压
			// File mFile = new File(ebooksPath + "temp\\");
			// ftp上传linux注意斜杠与反斜杠的问题
			File mFile = new File(targetFolder);
			log.info(mFile);
			// 解压之后的文件移动
			moveFile(mFile, ebooksPath + "/epub/", autoCategoryName, isbn);// 移动
			// 删除目录以及目录下所有文件 （temp下的文件夹以及所有文件）
			FileUtils.deleteDirectory(targetFolder);
			// 删除.zip压缩文件
			FileUtils.delFile(ebooksPath + filename);
			// 删除.xls和.xlsx格式的文件
			FileUtils.delFile(ebooksPath + excelname);
			//修改session时间为1个小时
			session.setMaxInactiveInterval(2*60);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			session.setMaxInactiveInterval(2*60);

		}
		// model.addAttribute("fileUrl", request.getContextPath() + "/upload/"
		// +"");
		return "redirect:/cms/ebook/add.html";
	}

	/**
	 * 
	 * 讲解压到临时目录的文件移动到ebook与pic
	 * 
	 * @param mFile
	 * @param ebooksPath
	 * @throws IOException
	 */
	public void moveFile(File mFile, String ebooksPath,
			String autoCategoryName, String isbn) throws IOException {
		String fileName = "", row = "", suffix = "";
		// 遍历temp下的文件夹的所有文件
		if (mFile.isDirectory()) {
			File[] files = mFile.listFiles();
			for (File mpf : files) {
				moveFile(mpf, ebooksPath, autoCategoryName, isbn);
			}
			mFile.delete();
		} else {
			try {
				fileName = mFile.getName().replaceAll("'", "");
				// 2015中国艺术品拍卖年鉴.玉器.epub 截取epub
				suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
				fileName = fileName.substring(0, fileName.lastIndexOf("."));
				// 从数据库中找到ebook文件名称
				Ebook ebook = ebookService.findByName(fileName);
				// 如果文件为.epub格式的
				if ("epub".equals(suffix)) { // 导入epub
					
					// row = cellMap.get(fileName);
					String str = mFile.getName();
					str = str.substring(0, str.lastIndexOf("."));
					row = cellMap.get(str);
					fileMeta = new FileMeta();
					fileMeta.setFileName(mFile.getName());
					// 文件大小的算法
					String fileSize = (double) (mFile.length() / 1024) / 1024
							+ "";
					fileSize = String.format("%.2f", Double.valueOf(fileSize));
					fileMeta.setFileSize(fileSize + "M");
					// 如果当前数据库中没有ebook数据，则插入到数据库中，否则文件已存在
					if (ebook == null) {
						if (StringUtils.isNotBlank(row)) {// Excel表格里面有
							// 通过读取excel表中的信息存入数据库中
							Integer bookId = saveEbookInfo(
									Integer.parseInt(row), fileSize,
									autoCategoryName);
							fileMeta.setBookId(String.valueOf(bookId));
							fileMeta.setRemark(String.valueOf(bookId));
							moveFiles(mFile, ebooksPath, bookId, isbn);
						} else { // Excel表格里面没有
							Integer bookId = readEpubFile(mFile, fileSize);
							fileMeta.setBookId(String.valueOf(bookId));
							fileMeta.setRemark(String.valueOf(bookId));
							moveFiles(mFile, ebooksPath, bookId, isbn);
						}
						// fileMetas.add(fileMeta);
					} else {
						fileMeta.setRemark("1");
						fileMetas.add(fileMeta);
						EbookError error = new EbookError();
						error.setFileName(fileMeta.getFileName());
						error.setFileType(fileMeta.getFileType());
						error.setFileSize(fileMeta.getFileSize());
						error.setRemark("重复上传");
						error.setUploadTime(ebook.getCreateDate()+"");
						error.setUser(ShiroUtils.getCurrentUser());
						ebookExportErrorDataService.saveOne(error);
					}
				}
			}catch(NonUniqueResultException e){
				fileMeta.setRemark("3");
				fileMetas.add(fileMeta);
				EbookError error = new EbookError();
				error.setFileName(fileName+".epub");
				error.setFileType(fileMeta.getFileType());
				error.setFileSize(fileMeta.getFileSize());
				error.setRemark("存在多本重复上传");
				error.setUser(ShiroUtils.getCurrentUser());
				ebookExportErrorDataService.saveOne(error);
			} catch (IOException e) {
				log.error(e.getMessage(), e);
				fileMeta.setRemark("2");
				fileMetas.add(fileMeta);
				EbookError error = new EbookError();
				error.setFileName(fileMeta.getFileName());
				error.setFileType(fileMeta.getFileType());
				error.setFileSize(fileMeta.getFileSize());
				error.setRemark("文件格式错误");
				error.setUser(ShiroUtils.getCurrentUser());
				ebookExportErrorDataService.saveOne(error);
			}
		}
	}

	/**
	 * 讲解压到临时目录的文件移动到ebook与pic
	 * 
	 * @param mFile
	 * @param ebooksPath
	 * @throws IOException
	 */
	public void moveFiles(File file, String ebooksPath, Integer bookId,
			String isbn) throws IOException {
		Ebook ebook = ebookService.getOne(bookId);
		String fullName = file.getName();
		String fileName = fullName.substring(fullName.lastIndexOf("/") + 1);
		String ebookName = ebook.getFile();
		ebookName = ebookName.substring(ebookName.lastIndexOf("/") + 1);
		String sourcePath = Config.getProperty("ebooksFile") + "temp/";
		// File epubFile = new File(sourcePath + fileName);
		try {
			// 重命名epub文件上传到ftp服务器
			ftpUtils.upload(file, ebookName, ebook.getFile());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
		
		fileName = fileName.substring(0, fileName.lastIndexOf("."));
		String imgName = ebook.getImage().substring(ebook.getImage().lastIndexOf("/") + 1);
		file = new File(file + File.separator);
		String path = file.getAbsolutePath();
		
		//获取file路径分隔符"/"或者"\"
		String str = File.separator;
		/*
		 * 获取本地图片，上传到OSS服务器 调用电子书图片上传方法,捕获异常
		 */
		try {
			InputStream inputStream = new FileInputStream(path.substring(0, path.lastIndexOf(str))+str+fileName+ ".jpg");
			ossUtils.upEbookFile(inputStream, imgName);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		// 不向文档数据库发数据
		// ebookService.saveSolrIndex(ebook);
	}

	/**
	 * 取excel行值,保存到电子书
	 * 
	 * @param row
	 *            行号
	 * @return
	 */
	private Integer saveEbookInfo(int row, String fileSize,String autoCategoryName) {
		Ebook ebook = new Ebook();
		String value = "";
		String tmp = "";
		Row bookRow = excel.getRow(row);
		for (int i = 0; i < 8; i++) {
			Cell cell = bookRow.getCell(i);
			try {
				if (cell != null) {
					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_FORMULA:
						value += cell.getRichStringCellValue().toString()
								+ "\"";
						break;
					case Cell.CELL_TYPE_NUMERIC:
						if (i == 4) { // 出版日期
							tmp = String.format("%.0f",
									cell.getNumericCellValue())
									+ "\"";
							if (!tmp.startsWith("2")) {// 格式错误
								double d = cell.getNumericCellValue();
								Date date = HSSFDateUtil.getJavaDate(d);
								DateFormat df1 = DateFormat.getDateInstance();// 日期格式，精确到日
								value += df1.format(date) + "\"";
								break;
							} else {// 正常
								if (StringUtils.isNotBlank(tmp)
										&& tmp.length() > 4) {
									value += tmp.substring(0, 4) + "\"";
								}
								break;
							}
						} else {
							value += String.format("%.0f",
									cell.getNumericCellValue())
									+ "\"";
							break;
						}
					case Cell.CELL_TYPE_STRING:
						value += cell.getStringCellValue() + "\"";
						break;
					default:
						value += " \"";
						break;
					}
				} else {
					value += " \"";
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
		String[] val = value.split("\"");
		String bookName = val[0];
		if (StringUtils.isNotBlank(bookName)) {
			ebook.setBookName(bookName);
		}
		String isbn = val[1];
		String author = val[2];
		String publisher = val[3];
		String publishDate = val[4];
		String type = val[5];
		String summary = val[6];
		String price = val[7];
		Integer categoryId = 0;
		// 如果类型的ID为0 那么为自动分类
		if ("-1".equals(autoCategoryName)) {
			categoryId = categoryMaps.get(type);
		} else {
			// 如果id不为0，设置成小说 艺术 科幻类型等等
			categoryId = categoryMaps.get(autoCategoryName);
			type = autoCategoryName;
		}
		ebook.setIsbn(isbn);
		ebook.setAuthor(author);
		ebook.setPublisher(publisher);
		ebook.setPublishDate(publishDate);
		ebook.setSummary(summary);
		ebook.setPrice(price);
		long fileName = System.currentTimeMillis();
		ebook.setImage("pic/" + fileName + ".jpg");
		ebook.setFileSize(fileSize);
		ebook.setFile("ebooks/epub/" + FtpUtils.getSubCatalogByIsbn(fileName + "") + fileName + ".epub");
		if (categoryId == null) {
			ebook.setCategoryId(0);
		} else {
			ebook.setCategoryId(categoryId);
		}
		ebook.setCategoryName(type);
		ebook.setCreateDate(new Date());
		// 添加一个操作员id
		ebook.setUser(ShiroUtils.getCurrentUser());
		ebookService.saveOne(ebook);
		return ebook.getId();
	}

	/**
	 * 直接读取Epub图书信息
	 * 
	 * @param file
	 * @return
	 */
	public Integer readEpubFile(File file, String fileSize) {
		EpubReader epReader = new EpubReader();
		try {
			Ebook ebook = new Ebook();
			Book book = epReader.readEpub(new FileInputStream(file));
			ebook.setFile("ebooks/" + file.getName());
			String title = book.getTitle();
			ebook.setBookName(title);
			Metadata metadata = book.getMetadata();
			List<String> publishers = metadata.getPublishers();
			for (String publisher : publishers) {
				ebook.setPublisher(publisher);
			}
			List<String> descriptions = metadata.getDescriptions();
			for (String description : descriptions) {
				ebook.setSummary(description);
			}
			List<String> subStrings = metadata.getSubjects();
			for (String subString : subStrings) {
				Integer categoryId = categoryMaps.get(subString);
				ebook.setCategoryId(categoryId);
				ebook.setCategoryName(subString);
			}
			List<nl.siegmann.epublib.domain.Date> dates = metadata.getDates();
			for (nl.siegmann.epublib.domain.Date date : dates) {
				ebook.setPublishDate(date.getValue());
			}
			List<Author> authors = metadata.getContributors();
			for (Author author : authors) {
				ebook.setAuthor(author.getFirstname());
			}

			long fileName = System.currentTimeMillis();
			ebook.setImage("pic/" + fileName + ".jpg");
			ebook.setFileSize(fileSize);
			ebook.setFile("ebooks/epub/"
					+ FtpUtils.getSubCatalogByIsbn(fileName + "") + fileName
					+ ".epub");
			ebook.setCategoryId(0);
			ebook.setCreateDate(new Date());
			ebook.setUser(ShiroUtils.getCurrentUser());
			ebookService.saveOne(ebook);
			return ebook.getId();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return 0;
	}

	@RequestMapping(value = "del/{id}", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject delete(@PathVariable Integer id,
			RedirectAttributes redirectAttributes) {
		// String ebooksPath = Config.getProperty("uploadFile")+"ebooks/epus/";
		// Ebook ebook = ebookService.getOne(id);
		// FileUtils.deleteFile(ebooksPath+ebook.getBookName()+".epub");
		return JsonUtil.createSuccessJson(ebookService.deleteById(id));
	}

	/**
	 * 
	 * @Title: exportExcel
	 * @Description: 导出电子书错误信息
	 * @param response
	 * @param ebookError
	 * @return String    返回类型
	 * @author QL 
	 * @date 2017年3月2日 上午9:47:40 
	 * @throws
	 */
	@RequestMapping(value = "export")
	public void exportExcel(HttpServletResponse response, EbookError ebookError) {
		response.setContentType("application/binary;charset=ISO8859_1");
		try {
			
			ServletOutputStream outputStream = response.getOutputStream();
			
			String fileName = new String(("导出excel").getBytes(), "utf-8");
			
			response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xlsx");// 组装附件名称和格式
			
			//通过ShiroUtils获取操作员ID
			User currentUser = ShiroUtils.getCurrentUser();
			
			//从数据库里查询全部错误信息
			String hql = "from EbookError where systemUser ='" + currentUser.getId() + "'";
			
			System.out.println(hql);
			
			String[] titles = { "序号", "文件名", "文件类型", "文件大小", "备注", "上传时间" };
			
			exportExcelService.exportExcel(hql, titles, outputStream);
			
		} catch (IOException e) {
			//输出错误信息
			log.error(e.getMessage(), e);
		}
	
	}
	
	/**
	 * 
	 * @Title: toEdit
	 * @Description: 跳转到电子书编译页面
	 * @param id
	 * @return String    返回类型
	 * @author QL 
	 * @date 2017年3月2日 上午9:47:01 
	 * @throws
	 */
    @RequestMapping(value = "toEditEbook")
	public ModelAndView toEdit(Integer id,String errorMessage) {
		ModelAndView mv = new ModelAndView("ebook/book_edit");
		// 通过properties文件获取imageURL
		String imagePath = Config.getProperty("image.url");

		// 获取ebookID
		Ebook ebook = ebookService.getOne(id);

		// 查询所有分类表中的分类信息
		List<EbookCategory> categories = ebookCategoryService.findAll();
		if (categoryMaps.isEmpty()) {

			// 遍历分类信息，获取分类的ID和分类的名称
			for (EbookCategory category : categories) {
				categoryMaps.put(category.getCategoryName(),
						category.getCategoryId());
			}
		}

		// 绑定对象，返回到前端
		mv.addObject("categories", categories);
		mv.addObject("imagePath", imagePath);
		mv.addObject("ebooks", ebook);
		mv.addObject("errorMessage", errorMessage);
		return  mv;
	}
	/**
	 * @Title: editEbook
	 * @Description: 电子书单条数据的更新
	 * @param ebook 电子书实体对象
	 * @param id 电子书的ID
	 * @param request 
	 * @param response
	 * @return ModelAndView   返回到电子书维护列表页面
	 * @author QL 
	 * @throws UnsupportedEncodingException 
	 * @date 2017年3月2日 上午9:45:37 
	 */
	@RequestMapping(value="editEbook")
	public ModelAndView editEbook(Ebook ebook,Integer id,HttpServletRequest request,HttpServletResponse response){
		// 通过获取id，获取一条数据
		Ebook ebooks = ebookService.getOne(id);
		// 文件上传，存入数据库的file路径
		try {
			FileUtils.uploadFileImageAndEpub(request, response, ebooks);
		} catch (Exception ex) {
			return toEdit(id, "连接超时");
		}
		//设置对象属性的值
		//设置作者
		ebooks.setAuthor(ebook.getAuthor());
		if (StringUtils.isNotBlank(ebook.getBookName()) || ebook.getBookName().length() < 250) {
			ebooks.setBookName(ebook.getBookName().substring(0,ebook.getBookName().length()));
		}
		// 通过ebookCategoryService的方法查找ebook里的CategoryId
		try {
			EbookCategory ebookCategory = ebookCategoryService.getOne(ebook.getCategoryId());
			// 通过ebookCategory对象获取CategoryId设置Ebook里ebooks.setCategoryId
			ebooks.setCategoryId(ebookCategory.getCategoryId());
			// 通过id获取categoryName
			if (StringUtils.isNotBlank(ebookCategory.getCategoryName())) {
				ebooks.setCategoryName(ebookCategory.getCategoryName());
			}
		} catch (Exception e) {
			return toEdit(id, "书名已存在,请重新输入");
		}
	 
		//设置isbn编号
        if(ebook.getIsbn().length()<250){
        	ebooks.setIsbn(ebook.getIsbn().substring(0,ebook.getIsbn().length()));
        } 
        
        //设置出版日期
		ebooks.setPublishDate(ebook.getPublishDate());
         
		//设置出版社
		ebooks.setPublisher(ebook.getPublisher());
		 
		//设置修改的日期
		ebooks.setModifyDate(new Date());
		
		//保存操作员修改信息
		ebooks.setUser(ShiroUtils.getCurrentUser());
 
		// 保存到数据库
		ebookService.saveOrUpdateOne(ebooks);
		
		//重定向维护列表
		return new ModelAndView("redirect:/cms/ebook/list.do");
	}

	 
}
