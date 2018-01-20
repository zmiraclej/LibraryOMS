package com.flea.modules.system.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
 
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
 
import java.util.Date;
import java.util.HashSet;
import java.util.List;
 
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
 












import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.flea.common.Common;
import com.flea.common.Constants;
import com.flea.common.action.BaseAction;
import com.flea.common.pojo.Page;
import com.flea.common.pojo.Role;
import com.flea.common.pojo.User;
 
import com.flea.common.service.UserService;
import com.flea.common.util.Config;
import com.flea.common.util.FileUtils;
import com.flea.common.util.ImportExcel;
import com.flea.common.util.JsonUtil;
import com.flea.common.util.LngAndLatUtil;
import com.flea.common.util.PasswordHelper;
import com.flea.common.util.ShiroUtils;
import com.flea.common.util.ValidatorRegexUtils;
import com.flea.modules.customer.pojo.CustomerContact;
import com.flea.modules.customer.service.CutomerLibraryService;
import com.flea.modules.system.pojo.Library;
import com.flea.modules.system.pojo.LibraryBook;
import com.flea.modules.system.pojo.LibraryCirculateRel;
import com.flea.modules.system.pojo.LibraryCustomer;
import com.flea.modules.system.pojo.LibraryImportErrorData;
 
import com.flea.modules.system.pojo.LibraryStop;
import com.flea.modules.system.pojo.vo.LibraryCity;
import com.flea.modules.system.pojo.vo.Librarys_Books;
import com.flea.modules.system.service.LibraryCustomerService;
import com.flea.modules.system.service.LibraryImportErrorDataService;
import com.flea.modules.system.service.LibraryService;
import com.flea.modules.system.service.LibraryStopService;
import com.flea.modules.system.service.LibrarysUpdateStatusService;
import com.flea.modules.system.util.DepartmentUtil;
import com.flea.modules.system.util.PublicDataUtils;



@Controller
@RequestMapping("cms/library")
public class LibraryController extends BaseAction {
	
	/**
	 * 日志对象
	 */
	protected Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private LibraryService libraryService;
	
	@Autowired
	private CutomerLibraryService cutomerLibraryService;
	
	@Autowired
	private LibraryCustomerService libraryCustomerService;
	
	@Autowired
	private LibraryStopService libraryStopService;
	
	@Autowired
	private LibrarysUpdateStatusService librarysUpdateStatusService;
	
	@Resource
	private UserService userService;
	
	private Map<String, String> cellMap;

	private ImportExcel  excel;
	
	@RequestMapping("/addLibraryPage.html")
	public ModelAndView addLibraryPage(){
		return new ModelAndView("/library/addLibraryPage");
	}
	
	
	public String setMaxHallCode(String hallCode){
		int number = DepartmentUtil.depcodeToNumber(hallCode);
		number++;
		String maxCode = DepartmentUtil.numberToDepcode(number, 4);
		return maxCode;
	}
	
	
	@RequestMapping("/groupByCity")
	@ResponseBody
	public String LibraryGroupByCity(){
		Map<String,List<LibraryCity>>  map =libraryService.findLibraryWithGroupByCity();
		return JSONArray.toJSONString(map);
	}
	
	
	/**
	 * 到加盟店管理页面列表去
	 * @param pageNum
	 * @param search
	 * @return
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(Integer pageNum,LibraryCustomer library,String search,HttpServletRequest request, HttpServletResponse response, Model model){
//	    Page<Library> page=libraryService.find(new Page<Library>(request,response),search, library);
		Page<LibraryCustomer> page = libraryCustomerService.findLibCustomer(new Page<LibraryCustomer>(request,response), search, library);
		ModelAndView mav = new ModelAndView("/library/library_list");
		User user = ShiroUtils.getCurrentUser();
		Role role = user.getRoles().get(0);
		String le = role.getLevel();
		if (Common.ROLE_FIRST_LEVLE.equals(le)) {
			mav.addObject("customer", le);
		} else {
			mav.addObject("customer", le);
		}
		boolean flag = libraryStopService.isExtistLibrarysAddRole(user.getId());
		mav.addObject("search", search);
		mav.addObject("page", page);
		mav.addObject("flag", flag);
		return mav;
	}
	
	/**
	 * 图书馆审核跳转页面
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/examine")
	public ModelAndView examine(Integer pageNum,LibraryCustomer library,String search,HttpServletRequest request, HttpServletResponse response, Model model){
		Page<LibraryCustomer> page = libraryCustomerService.findLibCustomerExamine(new Page<LibraryCustomer>(request,response), library, search);
		ModelAndView mv = new ModelAndView();
		if(null == library.getLibraryStatus() || "0".equals(library.getLibraryStatus())){
	        	mv.setViewName("library/library_list");
	        }else{
	        	mv.setViewName("library/library_examine_list");
	        }
		mv.addObject("search", search);
		mv.addObject("page", page);
		return mv;
	}
	/**
	 *  
	 * @Description:查询未审核的图书馆信息
	 * @param qc
	 * @return    
	 * BigInteger    返回类型
	 * @throws
	 * @author QL 
	 * @date 2017年3月29日 下午6:46:53
	 */
	@RequestMapping(value="/librarysCount")
	@ResponseBody
	public JSONObject librarysCount(){
		JSONObject jsonObject = new JSONObject();
		User user = ShiroUtils.getCurrentUser();
		Role role = user.getRoles().get(0);
		String level = role.getLevel();
		if (Common.ROLE_FIRST_LEVLE.equals(level)) {
			jsonObject.put("customer", level);
		} else {
			jsonObject.put("customer", level);
		}
		Integer librarysCount = libraryService.getLibrarysCount();
		jsonObject.put("librarysCount", librarysCount);
		return jsonObject;
	}
	 
	/**
	 * 图书馆新增页面
	 * @return
	 */
	@RequestMapping(value="/add",method=RequestMethod.GET)
	public ModelAndView add(){
		ModelAndView mav = new ModelAndView("/library/library_add");
		User user = ShiroUtils.getCurrentUser();
		user = userService.getOne(user.getId());
		JSONObject json =null;
		Set<Map<String,Object>> areaMaps = new HashSet<Map<String,Object>>();
		if(user.getCustomer()!=null){
			 json = cutomerLibraryService.findLibraryAreasByCustomerId(user.getCustomer().getId());
			 areaMaps=(Set<Map<String, Object>>)json.get("areas");
		}
		mav.addObject("areas", areaMaps);
		mav.addObject("action","add");
		mav.addObject("title","添加");
		return mav;
	}
	
	
	/**
	 * 批量导入页面
	 * @return
	 */
	@RequestMapping(value="/batchAdd",method=RequestMethod.GET)
	public ModelAndView batchAdd(Integer pageNum,Library library,String search,HttpServletRequest request, HttpServletResponse response, Model model) {

		
//	    Page<Library> page = libraryService.find(new Page<Library>(request,response), search, library);
	    Page<Library> page = new Page<Library>();
		ModelAndView mav = new ModelAndView("/library/library_batch_add");
		User user =ShiroUtils.getCurrentUser();
		User t_user =userService.getOne(user.getId());
		JSONObject json;
		Set<Map<String,Object>> areaMaps = new HashSet<Map<String,Object>>();
		if(t_user.getCustomer()!=null){
			 json = cutomerLibraryService.findLibraryAreasByCustomerId(t_user.getCustomer().getId());
			 areaMaps=(Set<Map<String, Object>>)json.get("areas");
		}
		mav.addObject("showError", "");
		mav.addObject("search", search);
		mav.addObject("page", page);
		mav.addObject("areas", areaMaps);
		return mav;
		
	}
	
	/**
	 * 添加图书馆
	 * @param library, physicalAddres, response
	 * @return
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public void add(Library library,String physicalAddres,HttpServletResponse response){
		Long libCount = libraryService.findLibraryByName(library.getName());
		if (libCount > 0) return; //重名
		String  levelId = library.getLibraryLevel();
		String  levelString =  levelId.split(",")[0];
		library.setLibraryLevel(levelString);
		library.setLibraryStatus(1);
		//默认设置为0
		library.setLibraryUpdateStatus(0);
		JSONObject json = new JSONObject();
		try {
			//libraryService.saveOne(library);
			libraryService.addLibraryAndUpdateLibraryCode(library);
			json.put("success", true);
			this.renderJson(response, json.toJSONString());
		} catch (Exception e) {
			json.put("success", false);
			this.renderJson(response, json.toJSONString());
		}
	}
	
	/**
	 * 
	 * @Description:图书馆审核
	 * @param id
	 * @param model
	 * @param libraryStatus
	 * @return    
	 * ModelAndView    返回类型
	 * @throws
	 * @author QL 
	 * @date 2017年3月29日 下午5:36:21
	 */
	@RequestMapping("libraryExamine")
	public ModelAndView examine(Integer id,Model model,Integer libraryStatus) {
//		Library library = libraryService.getOne(id);
//	    model.addAttribute("library",library);
//		return new ModelAndView("library/library_examine");
		
		Library library = libraryService.getOne(id);
		ModelAndView mav = new ModelAndView("/library/library_examine");
		if (libraryStatus != null) {
			// 1 是查看界面
			model.addAttribute("libraryStatus", "1");
		} else {
			// 0 是审核界面
			model.addAttribute("libraryStatus", "0");
		}
		JSONObject json;
		Set<Map<String,Object>> areaMaps = new HashSet<Map<String,Object>>();
		User user =ShiroUtils.getCurrentUser();
		User t_user =userService.getOne(user.getId());
		if(t_user.getCustomer()!=null){
			 json = cutomerLibraryService.findLibraryAreasByCustomerId(t_user.getCustomer().getId());
			 areaMaps=(Set<Map<String, Object>>)json.get("areas");
		}
		mav.addObject("areas", areaMaps);
		List<String> levels = PublicDataUtils.getLibraryLevels();
		mav.addObject("levels",levels);
		mav.addObject("action","edit");
		mav.addObject("title","修改");
		mav.addObject("library",library);
		return mav;
		
	}
	
	/**
	 * 
	 * @Description:图书馆审核详细页面
	 * @param id
	 * @param status  驳回  
	 * @param reject
	 * @param model
	 * @return    
	 * ModelAndView    返回类型
	 * @throws
	 * @author QL 
	 * @date 2017年3月26日 下午12:47:29
	 */
	@RequestMapping("audit")
	@ResponseBody
	public ModelAndView audit(Integer id,Integer libraryStatus, String reject) {
     	libraryService.audit(id, libraryStatus);
     	Library lib = libraryService.getOne(id);
     	if (libraryStatus == 2) {
     		Object userId=	libraryService.findLibUserByHallCodeAndUserName(lib.getHallCode(),"admin");
		 	if(userId==null){
		 		libraryService.saveLibraryUser(lib.getHallCode());
		 	}else {
		 		libraryService.updateLibraryUserPassword(lib.getHallCode(),lib.getIsEffective());
			}
			org.apache.shiro.session.Session currentSession = SecurityUtils.getSubject().getSession();
			String passwd =(String)currentSession.getAttribute(lib.getHallCode()+"userPassword");
	 		PasswordHelper.sendPassword(lib.getHallCode(), lib.getPhone(), passwd);
	 		if(lib.getIsEffective().equals(Constants.FLAG_ENABLE)) {
	 			libraryService.start(lib.getId());
	 		}
     	}
 		//reject 驳回原因
//	    libraryService.audit(id, libraryStatus, reject);
		return  new ModelAndView("redirect:/cms/library/examine.do");
	}
	
	/**
	 * 通过图书馆馆名查找重复
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "/findLibraryByName", method=RequestMethod.GET)
	@ResponseBody
	public JSONObject findLibraryByName(String name) {
		Long libCount = libraryService.findLibraryByName(name);
		JSONObject json = new JSONObject();
		if(libCount > 0){
			json.put("exists", "1");
		} else {
			json.put("exists", "0");
		}
//		json = libraryService.findLibraryByName(name);
		return json;
	}
	
	/**
	 * 批量导入图书馆
	 * @param dept
	 * @return
	 */
	@RequestMapping(value="/batchAdd", method=RequestMethod.POST)
	@ResponseBody
	public ModelAndView batchAdd(String areaCode, String libraryLevel, Library dept, String filename, String excelname, MultipartHttpServletRequest request, HttpServletResponse response, Model model) {
		//areaCode CustomerLibraryId LibraryLevel
		ModelAndView mav = new ModelAndView("/library/library_batch_add");
		JSONObject json = new JSONObject();
		User user = ShiroUtils.getCurrentUser();
		User t_user = userService.getOne(user.getId());
		Set<Map<String,Object>> areaMaps = new HashSet<Map<String,Object>>();
		if(t_user.getCustomer()!=null){
			 json = cutomerLibraryService.findLibraryAreasByCustomerId(t_user.getCustomer().getId());
			 areaMaps=(Set<Map<String, Object>>)json.get("areas");
		}
		mav.addObject("areas", areaMaps);
		mav.addObject("showError", "showError");
		//首先删除临时表errorData的数据
		dataErrorService.detele();
		boolean vlidate =  FileUtils.validateFileSize(request, response);
		if(!vlidate) {
			mav.addObject("error", "上传文件大小不能超过300M.");
		}
		String  levelId = dept.getLibraryLevel();
		String  levelString =  levelId.split(",")[0];
		dept.setLibraryLevel(levelString);
		//查询可用的馆号
//		JSONObject jsonObject = cutomerLibraryService.findLibraryCodeById(dept.getCustomerLibraryId());
		JSONObject jsonObject = cutomerLibraryService.findUnusedLibraryCodeById(dept.getCustomerLibraryId());
		//可用馆号数量
		String number = jsonObject.getString("codeNumber");
		int hallCodeNumber = Integer.parseInt(number.split("/")[1]) - Integer.parseInt(number.split("/")[0]);
		//可用馆号集合
	/*	List<String> listHallCode = new ArrayList<String>();
		String hallCodeArray = jsonObject.getString("codes");
		String str = hallCodeArray.substring(1, hallCodeArray.length() - 1 );
		String[] strs = str.split(",");
		Collections.addAll(listHallCode, strs);*/
	
		String fileName = FileUtils.uploadFile(request, response, "library");
		String libraryPath = Config.getProperty("libraryFile");
		int successNum = 0; //导入条目
		int errorNum = 0; //错误条目
		
		//错误条目list
		List<LibraryImportErrorData> listErrorData = new ArrayList<LibraryImportErrorData>();
		try {
			File file = new File(libraryPath + fileName);
	    	//导入表格集合信息
	    	List<Library> libList = libraryReader(file);
	    	//library循环接收list
	    	if (libList.size() > hallCodeNumber) {
				mav.addObject("error", "导入馆数量超上限！");
				FileUtils.deleteFile(libraryPath + fileName);
				return mav;
	    	}
	    	for (int i = 0; i < libList.size(); i++) {
	    		Library lib = new Library();
	    		lib.setArea(dept.getArea());
	    		lib.setCustomerLibraryId(Integer.parseInt(levelId.split(",")[1]));
	    		lib.setLibraryLevel(levelString);
//	    		lib.setHallCode(listHallCode.get(i).trim());
	    		lib.setName(libList.get(i).getName().trim());
	    		lib.setAddress(libList.get(i).getAddress().trim());
	    		//通过地址获取经纬度
	    		lib.setLngLat(LngAndLatUtil.getLngAndLat(libList.get(i).getAddress()));
	    		lib.setAgreementAccount(libList.get(i).getAgreementAccount());
	    		lib.setAcountName(libList.get(i).getAcountName().trim());
	    		lib.setCreditLines(libList.get(i).getCreditLines());
	    		lib.setContacts(libList.get(i).getContacts());
	    		//图书馆新建状态
	    		lib.setLibraryStatus(1);
	    		//默认设置为0
	    		lib.setLibraryUpdateStatus(0);
	    		//导入的信息不能为空
	    		if ("".equals(lib.getName().trim()) || "".equals(lib.getAddress().trim()) || "".equals(lib.getAcountName().trim()) || "".equals(lib.getAgreementAccount().trim()) ||
	    				"".equals(lib.getContacts().get(0).getContactPerson().trim()) || "".equals(lib.getContacts().get(0).getTel().trim()) ||
	    				"".equals(lib.getContacts().get(0).getPhone().trim()) || "".equals(lib.getContacts().get(0).getChat().trim())) {
	    			LibraryImportErrorData errorData = getErrorData(lib, "数据不完整！");
	    			listErrorData.add(errorData);
	    			libraryService.saveErroeExcel(errorData);
	    			errorNum++;
	    			continue;
	    		} else {
	    			if (lib.getName().trim().length() > 64 || lib.getAddress().trim().length() > 64 || lib.getAgreementAccount().trim().length() > 20 || lib.getAcountName().trim().length() > 20 ||
	    					lib.getContacts().get(0).getContactPerson().trim().length() > 64 || lib.getContacts().get(0).getPhone().trim().length() > 15 ||
	    					lib.getContacts().get(0).getTel().trim().length() > 15 || lib.getContacts().get(0).getChat().trim().length() > 64) {
	    				LibraryImportErrorData errorData = getLengthErrorData(lib, "数据格式错误！");
		    			listErrorData.add(errorData);
		    			libraryService.saveErroeExcel(errorData);
		    			errorNum++;
		    			continue;
	    			}
	    			//验证手机号码进行校验
		    		if(!lib.getContacts().get(0).getPhone().matches(ValidatorRegexUtils.REGEX_phone)){
		    			LibraryImportErrorData errorData = getErrorData(lib, "手机格式错误!");
		    			listErrorData.add(errorData);
		    			libraryService.saveErroeExcel(errorData);
		    			errorNum++;
		    			continue;
		    		}
		    		/*业务需求、暂时去掉邮箱验证
		    		 * if(!lib.getContacts().get(0).getChat().matches(ValidatorRegexUtils.REGEX_EMAIL)){
		    			LibraryImportErrorData errorData = getErrorData(lib, "邮箱格式错误!");
		    			listErrorData.add(errorData);
		    			libraryService.saveErroeExcel(errorData);
		    			errorNum++;
		    			continue;
		    		}*/
		    		//导入的馆名不能重复
		    		Long libCount = libraryService.findLibraryByName(lib.getName());
		    		if (0 < libCount) {
		    			LibraryImportErrorData errorData = getErrorData(lib, "资料重复！");
		    			listErrorData.add(errorData);
		    			libraryService.saveErroeExcel(errorData);
		    			errorNum++;
		    			continue;
		    		}
	    		}
	    		
//	    		libraryService.saveOne(lib);
	    		libraryService.addLibraryAndUpdateLibraryCode(lib);
				
				successNum++;
			}
	    	//删除上传文件
	    	FileUtils.deleteFile(libraryPath + fileName);
	    	
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();			
		}
		
//		batchAddError(listErrorData, errorNum, successNum, request, response, model);
		//错误信息分页
		LibraryImportErrorData LibErrorData = new LibraryImportErrorData();
		LibErrorData.setUser(ShiroUtils.getCurrentUser());
//		Page<LibraryImportErrorData> page = dataErrorService.find(new Page<LibraryImportErrorData>(request, response), LibErrorData);
//		mav.addObject("errorList", listErrorData);
//		Page<LibraryImportErrorData> page = new Page<LibraryImportErrorData>();
		mav.addObject("errorNum", errorNum);
		mav.addObject("successNum", successNum);
//		mav.addObject("page", page);
		return mav;
	
//		return "success";
//		return "redirect:/cms/library/batchAddError.html";
	}
	
	@Autowired
	private LibraryImportErrorDataService dataErrorService;
	
	/**
	 * @return
	 * 错误信息分页列表
	 */
	@RequestMapping(value="listErrorData",method = RequestMethod.POST)
	public ModelAndView listErrorData(HttpServletRequest request, HttpServletResponse response, HttpSession session){
		System.out.println(request.getParameter("pageNo")+"#####################");
		LibraryImportErrorData libraryImportErrorData = new LibraryImportErrorData();
		libraryImportErrorData.setUser(ShiroUtils.getCurrentUser());
		//Page<LibraryImportErrorData> page = dataErrorService.find(new Page<LibraryImportErrorData>());
		Page<LibraryImportErrorData> page = dataErrorService.find(new Page<LibraryImportErrorData>(request, response), libraryImportErrorData);
		//查询所有错误条目
		List<LibraryImportErrorData> listErrorData = dataErrorService.listImportErrorData();
		//Page<LibraryImportErrorData> page = new Page<LibraryImportErrorData>();
		ModelAndView mav = new ModelAndView("/library/library_error");
		mav.addObject("errorList", listErrorData);
		mav.addObject("page", page);
		return mav;
	}
	
	/**
	 * 存储错误
	 * @param lib
	 * @param msg
	 * @return 得到错误信息并设置
	 */
	private LibraryImportErrorData getErrorData(Library lib, String msg) {
		LibraryImportErrorData errorData = new LibraryImportErrorData();
		errorData.setName(lib.getName());
		errorData.setAddress(lib.getAddress());
		errorData.setAgreementAccount(lib.getAgreementAccount());
		errorData.setAcountName(lib.getAcountName());
		errorData.setCreditLines(lib.getCreditLines());
		errorData.setConperson(lib.getContacts().get(0).getContactPerson());
		errorData.setTel(lib.getContacts().get(0).getTel());
		errorData.setPhone(lib.getContacts().get(0).getPhone());
		errorData.setEmail(lib.getContacts().get(0).getChat());
		errorData.setMsg(msg);
		errorData.setUser(ShiroUtils.getCurrentUser());
		return errorData;
	}
	
	/**
	 * 错误信息--传入字段长度超过数据库设置长度
	 * @param lib
	 * @param msg
	 * @return
	 */
	private LibraryImportErrorData getLengthErrorData(Library lib, String msg) {
		LibraryImportErrorData errorData = new LibraryImportErrorData();
		if (lib.getName().trim().length() > 64) {
			errorData.setName(lib.getName().trim().substring(0, 64));
		} else {
			errorData.setName(lib.getName());
		}
		if ( lib.getAddress().trim().length() > 64) {
			errorData.setAddress(lib.getAddress().trim().substring(0, 64));
		} else {
			errorData.setAddress(lib.getAddress());
		}
		if (lib.getAgreementAccount().trim().length() > 20) {
			errorData.setAgreementAccount(lib.getAgreementAccount().trim().substring(0, 20));
		} else {
			errorData.setAgreementAccount(lib.getAgreementAccount());
		}
		if (lib.getAcountName().trim().length() > 20) {
			errorData.setAcountName(lib.getAcountName().trim().substring(0, 20));
		} else {
			errorData.setAcountName(lib.getAcountName());
		}
		errorData.setCreditLines(lib.getCreditLines());
		if (lib.getContacts().get(0).getContactPerson().trim().length() > 64) {
			errorData.setConperson(lib.getContacts().get(0).getContactPerson().trim().substring(0, 64));
		} else {
			errorData.setConperson(lib.getContacts().get(0).getContactPerson());
		}
		if (lib.getContacts().get(0).getTel().trim().length() > 15) {
			errorData.setTel(lib.getContacts().get(0).getTel().trim().substring(0, 15));
		} else {
			errorData.setTel(lib.getContacts().get(0).getTel());
		}
		if (lib.getContacts().get(0).getPhone().trim().length() > 15) {
			errorData.setPhone(lib.getContacts().get(0).getPhone().trim().substring(0, 15));
		} else {
			errorData.setPhone(lib.getContacts().get(0).getPhone());
		}
		if (lib.getContacts().get(0).getChat().trim().length() > 64) {
			errorData.setEmail(lib.getContacts().get(0).getChat().trim().substring(0, 64));
		} else {
			errorData.setEmail(lib.getContacts().get(0).getChat());
		}
		errorData.setMsg(msg);
		errorData.setUser(ShiroUtils.getCurrentUser());
		return errorData;
	}
	
	/**
	 * 下载示例文件
	 * @param response
	 */
	@RequestMapping(value="/exportData", method = RequestMethod.GET)
	public void exportData(HttpServletRequest request, HttpServletResponse response){
		
		try {
			String path = request.getSession().getServletContext().getRealPath("/document/");
//			String fileName = path+"/云图书馆_图书馆批量新增_示例文件.xls";
			String fileName = path+"/example.xls";
			File file = new File(fileName);
	        // 取得文件名。
	        String filename = file.getName();
	        InputStream fis = new BufferedInputStream(new FileInputStream(fileName));
	        byte[] buffer = new byte[fis.available()];
	        fis.read(buffer);
	        fis.close();
	        // 清空response
	        response.reset();
	        response.setCharacterEncoding("UTF-8");
	        // 设置response的Header
//	        response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
	        response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes("GB2312"), "ISO8859-1"));
	        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
	        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
	        toClient.write(buffer);
	        toClient.flush();
	        toClient.close();
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e.getMessage());
			e.printStackTrace();
			System.out.println(e.getMessage());
		}finally{
			
		}
		
	}
	
	/**
	 * 导出错误条目
	 */
	@RequestMapping(value="/exportErrorData", method = RequestMethod.GET)
	public void exportErrorData(HttpServletResponse response){
		try {
			String libraryPath = Config.getProperty("fileExport");
			String fileName = libraryPath +ShiroUtils.getCurrentUser().getCustomer().getId() + ShiroUtils.getCurrentUser().getId() +".xls";
			dataErrorService.exportErrorData(fileName);
			File file = new File(fileName);
	        // 取得文件名。
	        String filename = file.getName();
	        InputStream fis = new BufferedInputStream(new FileInputStream(fileName));
	        byte[] buffer = new byte[fis.available()];
	        fis.read(buffer);
	        fis.close();
	        // 清空response
	        response.reset();
	        // 设置response的Header
	        response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes("GB2312"), "ISO8859-1"));
	        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
	        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
	        toClient.write(buffer);
	        toClient.flush();
	        toClient.close();
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e.getMessage());
			e.printStackTrace();
			System.out.println(e.getMessage());
		}finally{
			
		}
		
	}
	
	/**
	 * 读取excel文件
	 * @param file
	 * @return
	 */
	public List<Library> libraryReader(File file) {
	    List<Library> list = new ArrayList<Library>();
		Library library = null;
		try {
			Workbook hssfWorkbook = null;
			InputStream is = new FileInputStream(file);
			if (file.getName().contains(".xlsx")) {
				hssfWorkbook = new XSSFWorkbook(is);
			} else {
				hssfWorkbook = new HSSFWorkbook(is);
			}
			Sheet sheet = hssfWorkbook.getSheetAt(0);
			for (int numSheet = 1; numSheet <= sheet.getPhysicalNumberOfRows(); numSheet++) {
				Row hssfRow = sheet.getRow(numSheet);
				if (hssfRow == null) {
					continue;
				}
				// 循环行Row
				library = new Library();
				//馆名、地址、协议账号、户名、授信额度、联系人、电话、手机、邮箱
				if (null != hssfRow.getCell(0)) {
					library.setName(getValue(hssfRow.getCell(0)));
				} else {
					library.setName("");
				}
				if (null != hssfRow.getCell(1)) {
					library.setAddress(getValue(hssfRow.getCell(1)));
				} else {
					library.setAddress("");
				}
				if (null != hssfRow.getCell(2)) {
					library.setAgreementAccount(getValue(hssfRow.getCell(2)));
				} else {
					library.setAgreementAccount("");
				}
				if (null != hssfRow.getCell(3)) {
					library.setAcountName(getValue(hssfRow.getCell(3)));
				} else {
					library.setAcountName("");
				}
				if (null != hssfRow.getCell(4)) {
					library.setCreditLines(new BigDecimal(getValue(hssfRow.getCell(4))).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
				} else {
					library.setCreditLines(0.00);
				}
				List<CustomerContact> contactsList = new ArrayList<CustomerContact>();
				CustomerContact contacts = new CustomerContact();
				if (null != hssfRow.getCell(5)) {
					contacts.setContactPerson(getValue(hssfRow.getCell(5)));
				} else {
					contacts.setContactPerson("");
				}
				if (null != hssfRow.getCell(6)) {
					contacts.setTel(getValue(hssfRow.getCell(6)));
				} else {
					contacts.setTel("");
				}
				if (null != hssfRow.getCell(7)) {
					String str = String.valueOf(getValue(hssfRow.getCell(7)));
					contacts.setPhone(str);
				} else {
					contacts.setPhone("");
				}
				if (null != hssfRow.getCell(8)) {
					contacts.setChat(getValue(hssfRow.getCell(8)));
				} else {
					contacts.setChat("");
				}
				contactsList.add(contacts);
				library.setContacts(contactsList);
				list.add(library);
				
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 得到Excel表中的值
	 * @param hssfCell Excel中的每一个格子
	 * @return Excel中每一个格子中的值
	 */
	@SuppressWarnings("static-access")
	private String getValue(Cell hssfCell) {
		if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
			// 返回布尔类型的值
			return String.valueOf(hssfCell.getBooleanCellValue());
		} else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
			// 返回数值类型的值
			DecimalFormat df = new DecimalFormat("0");
			String whatYourWant = df.format(hssfCell.getNumericCellValue());
			if (whatYourWant.indexOf(".") > 0) {
				String s = String.valueOf(whatYourWant).replaceAll("0+?$", "");
				s = s.replaceAll("[.]$", "");
				return s;
			} else {
				return whatYourWant;
			}
		} else {
			// 返回字符串类型的值
			return String.valueOf(hssfCell.getStringCellValue());
		}
	}
	
	
	/**
	 * 返回地图页面
	 * @param dept
	 * @return
	 */
	@RequestMapping(value="/map",method=RequestMethod.GET)
	public ModelAndView map(){
		ModelAndView mav = new ModelAndView("/library/map");
		return mav;
	}
	
	/**
	 * 删除加盟店
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/del/{id}",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject del(@PathVariable Integer id){
		JSONObject json = JsonUtil.createSuccessJson(libraryService.deleteById(id));
		return json;
	}
	/**
	 * 停用加盟店
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/stop/{id}",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject stop(@PathVariable Integer id){
		return JsonUtil.createSuccessJson(libraryService.sotp(id));
	}
	/**
	 * 启用加盟店
	 * @param id
	 * @return 
	 * @return
	 */
	@RequestMapping(value="/start/{id}",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject start(@PathVariable Integer id){
		return JsonUtil.createSuccessJson(libraryService.start(id));
	}
	/**
	 * 到编辑页面去
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/edit/{id}",method=RequestMethod.GET)
	public ModelAndView edit(@PathVariable Integer id){
		Library library = libraryService.getOne(id);
		ModelAndView mav = new ModelAndView("/library/library_edit");
		JSONObject json;
		Set<Map<String,Object>> areaMaps = new HashSet<Map<String,Object>>();
		User user =ShiroUtils.getCurrentUser();
		User t_user =userService.getOne(user.getId());
		if(t_user.getCustomer()!=null){
			 json = cutomerLibraryService.findLibraryAreasByCustomerId(t_user.getCustomer().getId());
			 areaMaps=(Set<Map<String, Object>>)json.get("areas");
		}
		mav.addObject("areas", areaMaps);
		List<String> levels = PublicDataUtils.getLibraryLevels();
		mav.addObject("levels",levels);
		mav.addObject("action","edit");
		mav.addObject("title","修改");
		mav.addObject("library",library);
		return mav;
	}
	/**
	 * 编辑联盟店
	 * @param dept
	 * @param areaName
	 * @return
	 */
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public ModelAndView edit(Library dept,String areaName,String physicalAddres,boolean sendpwd ){
//		dept.setLibraryStatus(1);
		libraryService.updateLibrary(dept,sendpwd);
		if(sendpwd) {
			org.apache.shiro.session.Session currentSession = SecurityUtils.getSubject().getSession();
			String passwd =(String)currentSession.getAttribute(dept.getHallCode()+"userPassword");
	 		PasswordHelper.sendPassword(dept.getHallCode(),dept.getContacts().get(0).getPhone(), passwd);
		}
		if(dept.getIsEffective().equals(Constants.FLAG_ENABLE)) {
 			libraryService.start(dept.getId());
 		}
		return new ModelAndView("redirect:/cms/library/add.do");
	}
	/**
	 * 
	 * @method:reviewLibrary
	 * @Description:reviewLibrary	馆际流通审核列表界面
	 * @author: HeTao
	 * @date:2016年6月17日 下午2:55:39
	 * @param:@return
	 * @return:ModelAndView
	 */
	@RequestMapping(value="/reviewList")
	public ModelAndView reviewLibraryList(HttpServletRequest request, HttpServletResponse response,String searchText) {
		Page<LibraryCirculateRel> page = libraryService.find(new Page<LibraryCirculateRel>(request, response),searchText);
		ModelAndView mav = new ModelAndView();
		mav.addObject("search", searchText);
		mav.addObject("page", page);
		mav.setViewName("library/gjlt_examine_list");
		return mav;
	}
	
	/**
	 * 
	 * @method:reviewLibraryInfo
	 * @Description:reviewLibraryInfo	馆际流通审核界面
	 * @author: HeTao
	 * @date:2016年6月17日 下午3:06:36
	 * @param:@return
	 * @return:ModelAndView
	 */
	@RequestMapping(value="/review")
	public ModelAndView reviewLibraryInfo(Integer id) {
		
		Map<String,Object> maps = libraryService.queryCirculate(id);
		//借书的简介
		LibraryCirculateRel lc = (LibraryCirculateRel) maps.get("one");
		com.flea.modules.system.pojo.vo.User inLib =  (com.flea.modules.system.pojo.vo.User) maps.get("two");
		com.flea.modules.system.pojo.vo.User outLib =  (com.flea.modules.system.pojo.vo.User) maps.get("three");
		Librarys_Books book = (Librarys_Books) maps.get("book");
		//借书的详情
		List<LibraryBook> listbook = libraryService.reviewLibraryInfo(id);
		ModelAndView mav = new ModelAndView();
		mav.addObject("listbook",listbook);
		mav.addObject("lc",lc);
		mav.addObject("inLib",inLib);
		mav.addObject("outLib",outLib);
		mav.addObject("book",book);
		mav.setViewName("library/gjlt_examine");
		return mav;
	}
	
	/**
	 * 
	 * @method:selectReview
	 * @Description:selectReview	订单通过还是否决
	 * @author: HeTao
	 * @date:2016年6月23日 下午2:41:57
	 * @param:@param state
	 * @param:@param id
	 * @param:@return
	 * @return:ModelAndView
	 */
	@RequestMapping(value="/selectReview")
	public ModelAndView selectReview(Integer state,Integer id) {
		libraryService.updateCirculate(state, id);
		return new ModelAndView("redirect:/cms/library/reviewList.do");
	}
	
	
	/**
	 * 查找客户的馆
	 * @return
	 */
	@RequestMapping(value="codes")
	@ResponseBody
	public List<Map<String,String>> findCustomerHallCode() {
		User user = ShiroUtils.getCurrentUser();
		if(user.getCustomer()==null) {
			return null;
		}
		return  libraryService.findLibsByCustomerId(user.getCustomer().getId());
	}
	
	/**
	 * 查找客户的馆
	 * @return
	 */
	@RequestMapping(value="levels")
	@ResponseBody
	public List<String> findCustomerLevel() {
		User user = ShiroUtils.getCurrentUser();
		Role role = user.getRoles().get(0);
		String le = role.getLevel();
		Integer customerId =0;
		if(Common.ROLE_SECOND_LEVLE.equals(le)){
			customerId =user.getCustomer().getId();
		}
		return  libraryService.findLevelByCustomerId(customerId);
	}
	/**
	 * 
	 * @Description:跳转图书馆停用页面
	 * @return    
	 * ModelAndView    返回类型
	 * @throws
	 * @author QL 
	 * @date 2017年4月26日 上午11:47:18
	 */
	@RequestMapping(value="toStop/{id}")
	public ModelAndView toStop(@PathVariable Integer id, HttpServletRequest request) {
		Library library = libraryService.getOne(id);
		ModelAndView mv = new ModelAndView("library/library_stop");
		mv.addObject("library", library);
		return mv;
	}
	/**
	 * 
	 * @Description:图书馆停用
	 * @param id
	 * @param request
	 * @return    
	 * ModelAndView    返回类型
	 * @throws
	 * @author QL 
	 * @date 2017年4月25日 下午3:04:42
	 */
	
	@RequestMapping(value="addStop")
	public ModelAndView addStop(LibraryStop libraryStop, HttpServletRequest request, HttpServletResponse response) {
		// Tue May 02 2017 00:00:00 GMT+0800 (中国标准时间)转换date类型
		String start = request.getParameter("startDate").replace("GMT", "").replaceAll("\\(.*\\)", "");
		String end = request.getParameter("endDate").replace("GMT", "").replaceAll("\\(.*\\)", "");
		String libraryStopUserId = request.getParameter("id");
		boolean bool = libraryStopService.stopById(Integer.parseInt(libraryStopUserId));
		// 将字符串转化为date类型，格式yyyy-MM-dd
		if(bool){
		try {
			libraryStop.setStartDate(new Date(start));
			libraryStop.setEndDate(new Date(end));
		} catch (Exception e) {
			e.printStackTrace();
		}
		}
		libraryStop.setUserId(Integer.parseInt(libraryStopUserId));
	    libraryStopService.saveOne(libraryStop);
		ModelAndView mv = new ModelAndView("redirect:/cms/library/list.do");
		return mv;
	}
	/**
	 * 
	 * @Description:图书馆启用状态
	 * @param id
	 * @param request
	 * @return    
	 * ModelAndView    返回类型
	 * @throws
	 * @author QL 
	 * @date 2017年5月11日 下午7:45:07
	 */
	@RequestMapping(value="start/{id}")
	public ModelAndView start(@PathVariable Integer id, HttpServletRequest request) {
	   ModelAndView mv = new ModelAndView("redirect:/cms/library/list.do");
       libraryStopService.startById(id);
	   return mv;
	}
	/**
	 * 
	 * @Description:平台屏蔽用户信息
	 * @param id
	 * @param request
	 * @return    
	 * ModelAndView    返回类型
	 * @throws
	 * @author QL 
	 * @date 2017年5月11日 下午7:46:39
	 */
	@RequestMapping(value="lookLibrary/{id}")
	public ModelAndView lookLibrary(@PathVariable Integer id, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("redirect:/cms/library/list.do");
		libraryStopService.platFormDisplayCustomer(id);
		return mv;
	}
	/**
	 * 
	 * @Description:平台正常显示用户信息
	 * @param id
	 * @param request
	 * @return    
	 * ModelAndView    返回类型
	 * @throws
	 * @author QL 
	 * @date 2017年5月11日 下午8:16:32
	 */
	@RequestMapping(value="displayLibrary/{id}")
	public ModelAndView displayLibrary(@PathVariable Integer id, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("redirect:/cms/library/list.do");
		libraryStopService.platFormLookCustomer(id);
		return mv;
	}
	
	@RequestMapping("libraryStopExamine")
	public ModelAndView libraryStopExamine(Integer id,Model model,Integer libraryStatus, HttpServletRequest request ) {
		Library library = libraryService.getOne(id);
		LibraryStop libraryStop = libraryStopService.getLibraryStopInfomation(id);
		ModelAndView mav = new ModelAndView("/library/library_examine_stop");
		if (libraryStatus != null) {
			// 1 是查看界面
			model.addAttribute("libraryStatus", "1");
		} else {
			// 0 是审核界面
			model.addAttribute("libraryStatus", "0");
		}
		mav.addObject("libraryStop",libraryStop);
		mav.addObject("library",library);
		return mav;
		
	}
}
