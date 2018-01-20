package com.flea.modules.news.action;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
 * 消息Controller
 * @author bruce
 * @version 2016-06-08
 */
@Controller
@RequestMapping(value = "news/notice")
public class NoticeController  {

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
		status = testState==null?status:String.valueOf(testState);
		Page<News> page=newsService.find(new Page<News>(request,response), news,status,type);
        model.addAttribute("page",page);
        model.addAttribute("news",news);
        ModelAndView  mv = new ModelAndView();
        if("0".equals(status) || status == null){
        	mv.setViewName("news/notice_list");
        }else{
        	mv.setViewName("news/notice_examine_list");
        }
        
        User user = ShiroUtils.getCurrentUser();
		Role role = user.getRoles().get(0);
		String le = role.getLevel();
		if(Common.ROLE_FIRST_LEVLE.equals(le)){
			model.addAttribute("isCustomer","0");
		}else{
			model.addAttribute("isCustomer","1");
		}
        List<Map<String,String>> typeMap = PublicDataUtils.getNoticeType();
        mv.addObject("types", typeMap);
		return mv;
	}
	
	@RequestMapping(value = {"examine/{id}"})
	public ModelAndView examine(@PathVariable Integer id,Model model,Integer status) {
		News news = newsService.getOne(id);
		List<Map<String,String>> typeMap = PublicDataUtils.getNoticeType();
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
		}
	    model.addAttribute("news",news);
		return new ModelAndView("news/notice_examine");
	}
	
	@RequestMapping(value = {"audit/{id}"})
	@ResponseBody
	public JSONObject audit(@PathVariable Integer id,Integer status, String reject, HttpServletRequest request, HttpServletResponse response, Model model) {
//		return  JsonUtil.createSuccessJson(newsService.audit(id, status));
		//驳回消息，存入驳回理由，否则不存
		if (status==3 ) {
			return  JsonUtil.createSuccessJson(newsService.audit(id, status, reject));
		} else {
			return  JsonUtil.createSuccessJson(newsService.audit(id, status));
		}
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
		return new ModelAndView("redirect:/news/notice/list.do?status=1&type=4");
	}
	
	@RequestMapping(value = "form")
	public ModelAndView form(News news, Model model,String status,String action) {
		User user = ShiroUtils.getCurrentUser();
		Role role = user.getRoles().get(0);
		String le = role.getLevel();
		List<Map<String,String>> typeMap = PublicDataUtils.getNoticeType();
		if(Common.ROLE_FIRST_LEVLE.equals(le)){
			model.addAttribute("isCustomer","0");
			//typeMap.remove(3);
		}else{
			model.addAttribute("isCustomer","1");
		}
		String source = newsService.getSelectMore("4");
		model.addAttribute("action", action);
		model.addAttribute("source", source);
		model.addAttribute("status", status);
		model.addAttribute("types", typeMap);
		model.addAttribute("news", news);
		return new ModelAndView("news/notice_add");
	}
	
	@RequestMapping(value = "save")
	public ModelAndView save(News news, Model model,HttpServletRequest request,
			HttpServletResponse response)throws IOException {
		news.setImage("");
		//传入设置消息开始结束时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String start = request.getParameter("start");
		String end = request.getParameter("end");
		try {
			Date startDate = sdf.parse(start);
			Date endDate = sdf.parse(end);
			news.setStartDate(startDate);
			news.setEndDate(endDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		//设置区域名
		news = newsService.setNewsArea(news);
		if(news.getStatus() == null){
			news.setStatus(1);
		}
		newsService.saveOne(news);
		return new ModelAndView("redirect:/news/notice/form.do?status=1&action="+news.getStatus());
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
	 * 2016-12-21 update by gouxl
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
		return new ModelAndView("redirect:/news/notice/list.do?status=0&type=4");
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
		return new ModelAndView("redirect:/news/notice/list.html?status=0&type=4");
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
		List<Map<String,String>> typeMap = PublicDataUtils.getNoticeType();
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
		return new ModelAndView("news/notice_update");
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
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String start = request.getParameter("start");
		String end = request.getParameter("end");
		try {
			Date startDate = sdf.parse(start);
			Date endDate = sdf.parse(end);
			news.setStartDate(startDate);
			news.setEndDate(endDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(status.trim().length()>0 || news.getStatus() != null)
		{
			news.setStatus(Integer.parseInt(status));
		}
		else
		{
			news.setStatus(1);
		}
		newsService.updateNewsInfo(id, news);
		return new ModelAndView("redirect:/news/notice/updateView.html?status=1&id="+id+"&action="+news.getStatus());
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


