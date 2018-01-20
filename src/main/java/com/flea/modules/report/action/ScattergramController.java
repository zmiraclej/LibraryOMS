package com.flea.modules.report.action;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.flea.common.Common;
import com.flea.common.pojo.User;
import com.flea.common.util.RedisUtil;
import com.flea.common.util.ShiroUtils;
import com.flea.modules.report.pojo.vo.QueryCriteria;
import com.flea.modules.report.pojo.vo.ScattergramQueryCriteria;
import com.flea.modules.report.service.ScattergramService;
import com.flea.modules.report.service.VLibraryBookService;
import com.google.gson.Gson;

/**
 * 全景分布图 Controller
 * @author DaiBo
 * @version 2017-05-15
 */

@Controller
@RequestMapping(value = "report/scattergram")
public class ScattergramController {
	
	@Autowired
	private VLibraryBookService vLibraryBookService;

    @Autowired
    private ScattergramService scattergramService;
    
	@RequestMapping("")
	public ModelAndView list(QueryCriteria query, Model model, HttpServletRequest request, HttpServletResponse response){
        User user = ShiroUtils.getCurrentUser();
        String le = ShiroUtils.getCurrentUserRoleLevel(user);
        if(Common.ROLE_FIRST_LEVLE.equals(le)){
            model.addAttribute("area",null);
            model.addAttribute("lib",vLibraryBookService.getAllLibLevel());
            model.addAttribute("hall",null);
            model.addAttribute("isCustomer",false);
            query.setArea("区");
            query.setLib("");
        }else{
            Map<String, List<String>> map = vLibraryBookService.showTopSelect(query.getArea(),query.getLib());
            model.addAttribute("area",map.get("area"));
            model.addAttribute("lib",vLibraryBookService.getAllLibLevel());
            model.addAttribute("hall",new ArrayList<String>());
            model.addAttribute("isCustomer",true);
            query.setArea(map.get("area").get(0));
            query.setLib(map.get("lib").get(0));
        }
        model.addAttribute("query",query);
		return new ModelAndView("report/count_scattergram");
	}

	@RequestMapping(value = "/list")
	@ResponseBody
	public String list(ScattergramQueryCriteria params) {
		User currentUser = ShiroUtils.getCurrentUser();
		RedisUtil redisUtil = RedisUtil.getRedisUtil();
		String json = null;
		//定义key
		String key = "report_scattergram_list_"+currentUser.getId();
		// 先查找缓存中是否存在数据
		if (redisUtil.exists(key)) {
			// 缓存存在数据,直接返回
			json = redisUtil.get(key);
		} else {
			// 初始化,将数据加入缓存
			Gson gson = new Gson();
			json = gson.toJson(scattergramService.list(params));
			// 加入缓存,过期时间7天,7天之后更新redis缓存
			redisUtil.setex(key, json, 60*60*24*7);
		}
		return json;
	}

	@RequestMapping(value = "/listlight")
	@ResponseBody
	public String listLightInfo(ScattergramQueryCriteria params) {
		User currentUser = ShiroUtils.getCurrentUser();
		RedisUtil redisUtil = RedisUtil.getRedisUtil();
		String json = null;
		//定义key
		String key = "report_scattergram_listlight_"+currentUser.getId();
		// 先查找缓存中是否存在数据
		if (redisUtil.exists(key)) {
			// 缓存存在数据
			json = redisUtil.get(key);
		} else {
			// 初始化,将数据加入缓存
			Gson gson = new Gson();
			json = gson.toJson(scattergramService.listLight(params));
			// 加入缓存,过期时间7天,7天之后更新redis缓存
			redisUtil.setex(key, json, 60*60*24*7);
		}
		return json;
	}

	//打印参数到控制台.
	public static void testReflect(Object model) throws Exception{
		for (Field field : model.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			System.out.println(field.getName() + ":" + field.get(model) );
		}
	}

}
