package com.flea.modules.news.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONObject;
import com.flea.common.Common;
import com.flea.common.dao.AreaDao;
import com.flea.common.dao.CityDao;
import com.flea.common.dao.ProvinceDao;
import com.flea.common.pojo.Area;
import com.flea.common.pojo.City;
import com.flea.common.pojo.Page;
import com.flea.common.pojo.Province;
import com.flea.common.pojo.Role;
import com.flea.common.pojo.User;
import com.flea.common.service.SystemAreasService;
import com.flea.common.service.UserService;
import com.flea.common.util.FileUtils;
import com.flea.common.util.JsonUtil;
import com.flea.common.util.ShiroUtils;
import com.flea.modules.customer.pojo.Customer;
import com.flea.modules.customer.service.CutomerLibraryService;
import com.flea.modules.news.pojo.News;
import com.flea.modules.news.service.NewsService;
import com.flea.modules.system.pojo.Library;
import com.flea.modules.system.service.LibraryService;
import com.flea.modules.system.util.PublicDataUtils;

/**
 * 资讯Controller
 * @author bruce
 * @version 2016-06-08
 */
@Controller
@RequestMapping(value = "news/news")
public class NewsController  {

	@Autowired
	private NewsService newsService;
	@Resource
	private UserService userService;
	@Autowired
	private LibraryService libraryService;
	@Resource
	private AreaDao areaDao;
	@Resource
	private CityDao cityDao;
	@Resource
	private ProvinceDao provinceDao;
	
	@Autowired
	private CutomerLibraryService cutomerLibraryService;
	
	
	@RequestMapping(value = {"list", ""})
	public ModelAndView list(News news, HttpServletRequest request, HttpServletResponse response, Model model,
			String status,String title,Integer testState,String type) {
		User user = ShiroUtils.getCurrentUser();
		Role role = user.getRoles().get(0);
		String le = role.getLevel();
		if (Common.ROLE_FIRST_LEVLE.equals(le)) {
			model.addAttribute("isCustomer", le);
		} else {
			model.addAttribute("isCustomer", le);
			return new ModelAndView("redirect:/news/news/customerList.do?status=" + status + "&type=" + news.getType() + "&action"
							+ news.getStatus());
		}
		status = testState == null ? status : String.valueOf(testState);
		Page<News> page = newsService.find(new Page<News>(request,response), news,status,type);
        model.addAttribute("page",page);
        model.addAttribute("news",news);
        ModelAndView  mv = new ModelAndView();
        if("0".equals(status) || status == null){
        	//0:是查看维护列表页面
        	mv.setViewName("news/news_list");
        }else{
        	//1:是查看审核页面
        	mv.setViewName("news/news_examine_list");
        }
        
        List<Map<String,String>> typeMap = PublicDataUtils.getNewsType();
        //给用户和平台一个标识，如果为true有新增权限的维护列表有编辑功能，反而有审核权限的维护列表没有编辑功能
        boolean flag = newsService.isExtistNewsAddRoleAndAddPlatForm(user.getId());
        mv.addObject("flag", flag);
        mv.addObject("types", typeMap);
		return mv;
	}
	/**
	 * 
	 * @Description:用户咨询审核列表
	 * @param nes
	 * @param request
	 * @param response
	 * @param model
	 * @param status
	 * @param title
	 * @param testState
	 * @return    
	 * ModelAndView    返回类型
	 * @throws
	 * @author QL 
	 * @date 2017年4月12日 下午5:36:26
	 */
	@RequestMapping(value = {"customerList", ""})
	public ModelAndView customerList(News news, HttpServletRequest request, HttpServletResponse response, Model model,
			String status,String title,Integer testState,String type) {
		status = testState == null ? status : String.valueOf(testState);
		Page<News> page=newsService.findCustomer(new Page<News>(request,response), news,status,type);
		model.addAttribute("page", page);
		model.addAttribute("news", news);
		ModelAndView  mv = new ModelAndView();
		if("0".equals(status) || status == null){
			// 用户维护列表页面
			mv.setViewName("news/news_customer_list");
		}else{
			// 用户审核页面
			mv.setViewName("news/news_examine_customer_list");
		}
		User user = ShiroUtils.getCurrentUser();
		Role role = user.getRoles().get(0);
		String le = role.getLevel();
		if(Common.ROLE_FIRST_LEVLE.equals(le)){
			model.addAttribute("isCustomer",le);
		}else{
			model.addAttribute("isCustomer",le);
		}
		List<Map<String,String>> typeMap = PublicDataUtils.getNewsType();
		boolean reslut = newsService.isExtistNewsAddRoleAndAddPlatForm(user.getId());
		System.out.println(reslut);
	    mv.addObject("reslut", reslut);
	    mv.addObject("types", typeMap);
		return mv;
	}
	@RequestMapping(value = {"examine/{id}"})
	public ModelAndView examine(@PathVariable Integer id,Model model,Integer status) {
		News news = newsService.getOne(id);
		List<Map<String,String>> typeMap = PublicDataUtils.getNewsType();
		for(Map<String,String> map:typeMap){
			String key = map.get("key");
			if(key.equals(news.getType())){
				news.setType(map.get("val"));
			}
		}
		String objKey = news.getObjKey();
		if(StringUtils.isNotBlank(objKey)){
			switch (objKey) {
			case "1":
				objKey ="地区";
				Area area = areaDao.findAreaByCode(news.getAreaCode());
				City city = cityDao.findCityByCode(news.getCityCode());
				Province province = provinceDao.findProvinceByCode(news.getProvinceCode());
				if(province!=null){
					news.setProvinceCode(province.getName());
				}else {
					news.setProvinceCode("全部");
				}
				if(city!=null){
					news.setCityCode(city.getName());
				}else {
					news.setCityCode("全部");
				}
				if(area!=null){
					news.setAreaCode(area.getName());
				}else {
					news.setAreaCode("全部");
				}
				break;
			case "2":
				objKey ="馆别";
				break;
			case "3":
				objKey ="馆号";
				break;
			case "4":
				objKey ="馆名";
				//获取馆号去查询馆名
				String objVal = news.getObjVal();
				if (!"0".equals(objVal)) {
					Library library = libraryService.findByHallCode(objVal);
					news.setObjVal(library.getName());
				}
				break;
			default:
				objKey ="全部";
				news.setObjVal("全部");
				break;
			}
			news.setObjKey(objKey);
			if(news.getObjVal().equals("0")){
				news.setObjVal("全部");
			}
		}
		if(status != null)
		{
			//1 是查看界面
			model.addAttribute("status","1");
		}
		else
		{
			//0 是审核界面
			model.addAttribute("status","0");
		}
		User user = ShiroUtils.getCurrentUser();
		Role role = user.getRoles().get(0);
		String le = role.getLevel();
		if(Common.ROLE_FIRST_LEVLE.equals(le)){
			model.addAttribute("isCustomer","0");
		}else{
			model.addAttribute("isCustomer","1");
			return new ModelAndView(
					"redirect:/news/news/customerExamine.do?status=" + news.getStatus() + "&id=" + id);
		}
	    model.addAttribute("news",news);
		return new ModelAndView("news/news_examine");
	}
	
	/**
	 * 
	 * @Description:用户咨询审核列表
	 * @param id
	 * @param model
	 * @param status
	 * @return    
	 * ModelAndView    返回类型
	 * @throws
	 * @author QL 
	 * @date 2017年4月12日 下午5:56:56
	 */
	
	@RequestMapping(value = {"customerExamine/{id}"})
	public ModelAndView customerExamine(@PathVariable Integer id,Model model,Integer status) {
		News news = newsService.getOne(id);
		List<Map<String,String>> typeMap = PublicDataUtils.getNewsType();
		for(Map<String,String> map:typeMap){
			String key = map.get("key");
			if(key.equals(news.getType())){
				news.setType(map.get("val"));
			}
		}
		String objKey = news.getObjKey();
		if(StringUtils.isNotBlank(objKey)){
			switch (objKey) {
			case "1":
				objKey ="地区";
				Area area = areaDao.findAreaByCode(news.getAreaCode());
				City city = cityDao.findCityByCode(news.getCityCode());
				Province province = provinceDao.findProvinceByCode(news.getProvinceCode());
				if(province!=null){
					news.setProvinceCode(province.getName());
				}else {
					news.setProvinceCode("全部");
				}
				if(city!=null){
					news.setCityCode(city.getName());
				}else {
					news.setCityCode("全部");
				}
				if(area!=null){
					news.setAreaCode(area.getName());
				}else {
					news.setAreaCode("全部");
				}
				break;
			case "2":
				objKey ="馆别";
				break;
			case "3":
				objKey ="馆号";
				break;
			case "4":
				objKey ="馆名";
				//获取馆号去查询馆名
				String objVal = news.getObjVal();
				if (!"0".equals(objVal)) {
					Library library = libraryService.findByHallCode(objVal);
					news.setObjVal(library.getName());
				}
				break;
			default:
				objKey ="全部";
				news.setObjVal("全部");
				break;
			}
			news.setObjKey(objKey);
			if(news.getObjVal().equals("0")){
				news.setObjVal("全部");
			}
		}
		if(status != null)
		{
			//1 是查看界面
			model.addAttribute("status","1");
		}
		else
		{
			//0 是审核界面
			model.addAttribute("status","0");
		}
		User user = ShiroUtils.getCurrentUser();
		Role role = user.getRoles().get(0);
		String le = role.getLevel();
		if(Common.ROLE_FIRST_LEVLE.equals(le)){
			model.addAttribute("isCustomer",le);
		}else{
			model.addAttribute("isCustomer",le);
		}
		model.addAttribute("news",news);
		return new ModelAndView("news/news_examine_customer");
	}
	
	@RequestMapping(value = {"audit/{id}"})
	@ResponseBody
	public ModelAndView audit(@PathVariable Integer id,Integer status, String reject, HttpServletRequest request, HttpServletResponse response, Model model) {
		//驳回消息，存入驳回理由，否则不存
		ModelAndView mv = new ModelAndView("redirect:/news/news/customerList.do?status=1");
		String leve = ShiroUtils.getCurrentUserRoleLevel(ShiroUtils.getCurrentUser());
		if(Common.ROLE_FIRST_LEVLE.equals(leve)){
			if (status==3 ) {
				newsService.audit(id, status, reject);
			} else {
				newsService.audit(id, status);
			}
		}else{
			if (status==3 ) {
				newsService.audit(id, status, reject);
				return  mv;
			} else {
				newsService.audit(id, status);
				return mv;
			}
		}
		return   new ModelAndView("redirect:/news/news/list.do?status=1");
	}
	
	/**
	 * 
	 * @method:testChageState
	 * @Description:testChageState 测试弃审
	 * @author: HeTao
	 * @date:2016年6月27日 上午10:07:47
	 * @param:@param id
	 * @param:@param status
	 * @param:@return
	 * @return:ModelAndView
	 */
	@RequestMapping("testOut")
	@ResponseBody
	public ModelAndView testChageState(Integer id,Integer status) {
		newsService.audit(id, status);
		return new ModelAndView("redirect:/news/news/list.do?status=0&type=3");
	}
	
	@RequestMapping(value = "form")
	public ModelAndView form(News news, Model model,String status,String action) {
		User user = ShiroUtils.getCurrentUser();
		Role role = user.getRoles().get(0);
		String le = role.getLevel();
		List<Map<String,String>> typeMap = PublicDataUtils.getNewsType();
		if(Common.ROLE_FIRST_LEVLE.equals(le)){
			model.addAttribute("isCustomer","0");
			//typeMap.remove(3);
		}else{
			model.addAttribute("isCustomer","1");
		}
		model.addAttribute("action", action);
		model.addAttribute("status", status);
		model.addAttribute("types", typeMap);
		model.addAttribute("news", news);
		return new ModelAndView("news/news_add");
	}
	
	@RequestMapping(value = "save")
	public ModelAndView save(News news, Model model,HttpServletRequest request,
			HttpServletResponse response)throws IOException {
		String fileName = FileUtils.uploadImages(request,response,"newsImages");
		
		news.setImage(fileName);
		//设置区域名
		news = newsService.setNewsArea(news);
		User user = ShiroUtils.getCurrentUser();
		Role role = user.getRoles().get(0);
		String le = role.getLevel();
		if(Common.ROLE_FIRST_LEVLE.equals(le)){
			if(news.getStatus() == 1){
				news.setStatus(1);
			}else{
				news.setStatus(5);
			}
		}else{
             if(news.getStatus() == 1){
            	news.setStatus(6);
			}else{
				news.setStatus(10);
			}
		}
		newsService.saveOne(news);
		return new ModelAndView("redirect:/news/news/form.do?status="+news.getStatus()+"&action="+news.getStatus());
	}
	
	@RequestMapping(value = "getAreas")
	@ResponseBody
	public Set<Map<String,Object>> getAreas(News news, Model model,HttpServletRequest request,
			HttpServletResponse response) {
		User user =ShiroUtils.getCurrentUser();
		User t_user =userService.getOne(user.getId());
		JSONObject json;
		Set<Map<String,Object>> areaMaps = new HashSet<Map<String,Object>>();
		if(t_user.getCustomer()!=null){
			 json = cutomerLibraryService.findLibraryAreasByCustomerId(t_user.getCustomer().getId());
			 return (Set<Map<String, Object>>)json.get("areas");
		}
		return null;
	}
	
	/**
	 * 
	 * @method:top
	 * @Description:top	更新置顶
	 * @author: HeTao
	 * @date:2016年6月30日 下午4:19:56
	 * @param:@param id
	 * @param:@param redirectAttributes
	 * @param:@param state
	 * @param:@return
	 * @return:JSONObject
	 */
	@RequestMapping("top")
	@ResponseBody
	public ModelAndView top(Integer id, RedirectAttributes redirectAttributes,Integer state) {
		newsService.updateTopById(id,state);
		return new ModelAndView("redirect:/news/news/list.do?status=0&type=3");
	}
	
	/**
	 * 
	 * @method:delete
	 * @Description:delete 传一个id进来   进行删除某一条资讯
	 * @author: HeTao
	 * @date:2016年6月13日 上午9:35:48
	 * @param:@param id
	 * @param:@return
	 * @return:ModelAndView
	 */
	@RequestMapping(value = "del")
	@ResponseBody
	public ModelAndView delete(Integer id) {
		newsService.deleteNews(id);
		return new ModelAndView("redirect:/news/news/list.html?status=0&type=3");
	}
	
	/**
	 * 
	 * @method:queryNewsInfo
	 * @Description:queryNewsInfo 查找一条资讯的详情
	 * @author: HeTao
	 * @date:2016年6月8日 下午5:34:16
	 * @param:@param id
	 * @param:@return
	 * @return:News
	 */
	@RequestMapping("updateView")
	public ModelAndView updateView(Integer id,Model model,HttpServletRequest request,
			HttpServletResponse response,String status,String action) {
	News newsInfo = newsService.queryNewsInfo(id);
	newsInfo.setId(id);
	String key = newsInfo.getObjKey();
	if("1".equals(key)){
		User user =ShiroUtils.getCurrentUser();
		User t_user =userService.getOne(user.getId());
		JSONObject json;
		Set<Map<String,Object>> areaMaps = new HashSet<Map<String,Object>>();
		if(t_user.getCustomer()!=null){
			 json = cutomerLibraryService.findLibraryAreasByCustomerId(t_user.getCustomer().getId());
			 areaMaps = (Set<Map<String, Object>>)json.get("areas");
			 model.addAttribute("list",areaMaps);
		}
	}
	if("2".equals(key)){
		User user = ShiroUtils.getCurrentUser();
		Customer customer = user.getCustomer();
		Integer customerId = 0;
		if(customer != null){
			customerId = customer.getId();
		}
		List<String> list = libraryService.findLevelByCustomerId(customerId);
		 model.addAttribute("levels",list);
	}
	if("3".equals(key)||"4".equals(key)){
		User user = ShiroUtils.getCurrentUser();
		Customer customer = user.getCustomer();
		Integer customerId = 0;
		if(customer != null){
			customerId = customer.getId();
		}
		List<Map<String,String>> listMap = libraryService.findLibsByCustomerId(customerId);
		 model.addAttribute("list",listMap);
	}
	User user = ShiroUtils.getCurrentUser();
	Role role = user.getRoles().get(0);
	String le = role.getLevel();
	List<Map<String,String>> typeMap = PublicDataUtils.getNewsType();
	if(Common.ROLE_FIRST_LEVLE.equals(le)){
		model.addAttribute("isCustomer","0");
	}else{
		model.addAttribute("isCustomer","1");
	}
	model.addAttribute("action", action);
	model.addAttribute("status", status);
	model.addAttribute("isArea",newsInfo.getObjKey().equals("1")?"1":"0");
	model.addAttribute("province", newsInfo.getProvinceCode());
	model.addAttribute("city", newsInfo.getCityCode());
	model.addAttribute("area", newsInfo.getAreaCode());
	model.addAttribute("types", typeMap);
	model.addAttribute("newsInfo",newsInfo);
	return new ModelAndView("news/news_update");
	}
	
	/**
	 * 
	 * @method:queryNewsInfo
	 * @Description:queryNewsInfo 更新一条资讯的详情
	 * @author: HeTao
	 * @date:2016年6月8日 下午5:34:16
	 * @param:@param id
	 * @param:@return
	 * @return:News
	 * @throws IOException 
	 */
	@RequestMapping("updateinfo")
	public ModelAndView updateInfo(Integer id,News news,HttpServletRequest request,
			HttpServletResponse response,String status) {
		String leve = ShiroUtils.getCurrentUserRoleLevel(ShiroUtils.getCurrentUser());
//		if(status.trim().length()>0 || news.getStatus() != null)
//		{
//			news.setStatus(Integer.parseInt(status));
//		}
//		else
//		{   
//			news.setStatus(1);
//		}
		if(Common.ROLE_FIRST_LEVLE.equals(leve)){
			if(news.getStatus() == 1){
				news.setStatus(1);
			}else{
				news.setStatus(5);
			}
		}else{
             if(news.getStatus() == 1){
            	news.setStatus(6);
			}else{
				news.setStatus(10);
			}
		}
		//得到上传图片的名字
		String fileName;
		fileName = FileUtils.uploadImages(request, response,"newsImages");
	//	ossUtils.upFile(request.getSession().getServletContext().getRealPath("/")+"newsImages/"+fileName,"newsImages"+"/"+fileName);
		if(StringUtils.isNotBlank(fileName))
		news.setImage(fileName);
		newsService.updateNewsInfo(id, news);
		return new ModelAndView("redirect:/news/news/updateView.html?status="+news.getStatus()+"&id="+id+"&action="+news.getStatus());
	}
	
	/**
	 * 
	 * @method:getSelectMore
	 * @Description:getSelectMore 获取更多的对应的Select
	 * @author: HeTao
	 * @date:2016年6月12日 上午11:23:21
	 * @param:@return
	 * @return:String
	 */
	@RequestMapping("getMore")
	@ResponseBody
	public String getSelectMore(String content) {
		return newsService.getSelectMore(content);
	}

	
	@Resource
	private SystemAreasService areaService;
	@RequestMapping(value = "area")
	@ResponseBody
	public JSONObject getArea(String province,String city,String area) {
		JSONObject  json = new JSONObject();
		json.put("provice", province);
		json.put("city", city);
		json.put("area", area);
		List<City> areaList = areaService.listCity(province);
		Map<String, String> cityMap = new HashMap<String,String>();
		for(City c:areaList){
			cityMap.put(c.getCode(), c.getName());	
		}
		List<Area> list = areaService.listArea(city);
		Map<String, String> areaMap = new HashMap<String,String>();
		for(Area c:list){
			areaMap.put(c.getCode(), c.getName());
		}
		json.put("citylist", cityMap);
		json.put("arealist", areaMap);
		return json;
	}
}


