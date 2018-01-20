package com.flea.modules.system.action;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.flea.common.pojo.Page;
import com.flea.common.service.UserService;
import com.flea.modules.customer.service.CutomerLibraryService;
import com.flea.modules.system.pojo.Library;
import com.flea.modules.system.service.LibraryService;
import com.flea.modules.system.util.PublicDataUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

/**
 * 图书馆模式设置
 * @author bruce
 * @2016年5月25日 上午9:51:37
 */
@Controller
@RequestMapping("cms/libset")
public class LibrarySetupController {
	
	
	@Autowired
	private LibraryService libraryService;
	
	@Autowired
	private CutomerLibraryService cutomerLibraryService;
	
	@Resource
	private UserService userService;
	/**
	 * 图书馆列表
	 * @param pageNum
	 * @param search
	 * @return
	 */
	@RequestMapping(value="/list/{page}")
	public ModelAndView list(@PathVariable String page,Integer pageNum,Library library,String search,HttpServletRequest request, HttpServletResponse response, Model model){
		List<Library> list1 = null;
		Page<Library> pageObj=libraryService.find(new Page<Library>(request,response),search, library);
		list1 = pageObj.getList();
		if(!page.equals("lendout")&&!page.equals("deposit")){
	    	for (int i = 0; i < pageObj.getList().size(); i++) {
			    		boolean flag = true;
			    		try {
			    			flag = list1.get(i).getScope() != 0;
						} catch (Exception e) {
							flag = false;
						}
						if(flag){
							//如果范围状态为不是未启用的就不给添加   范围几。。。
							//list1.get(i).setScopeString("范围"+PublicDataUtils.changeNumber((pageObj.getList().get(i).getScope())));
						}
					}
				}
	    ModelAndView mav = new ModelAndView();
	    if(page.equals("lendout")){
	    	mav.setViewName("/library/borrow_limited_set");
	    } else if(page.equals("deposit")){
	    	mav.setViewName("/library/reader_deposit_set");
	    }else{
			//范围自加一
			int max = libraryService.getMaxScope()+1;
			List<Library> list = new ArrayList<Library>();
			for (int i = 0; i < max; i++) {
				Library li = new Library();
				//把数字更改字符串。。
				li.setName("范围"+PublicDataUtils.changeNumber((i+1)));
				list.add(li);
			}
			mav.addObject("max",list);
			mav.setViewName("/library/circulate");
		}
		mav.addObject("search", search);
		mav.addObject("page", pageObj);
		return mav;
	}
	
	@RequestMapping(value="/lendout",method=RequestMethod.POST)
	public void lendout(Integer maxSum,Integer freeRentDays,String rent,String menuIds,HttpServletResponse response) throws IOException{
		String[] ids = menuIds.split(",");
		libraryService.updateBorrowModelById(maxSum,freeRentDays,rent,ids);
		JSONObject json = new JSONObject();
		json.put("success", true);
		response.setContentType("text/html");  
		response.getWriter().write(json.toJSONString());
	}
	
	@RequestMapping(value="/deposit",method=RequestMethod.POST)
	public void deposit(String reader,String deposit,String menuIds,HttpServletResponse response) throws IOException{
		String[] ids = menuIds.split(",");
		libraryService.updateDepositModelById(reader,deposit,ids);
		JSONObject json = new JSONObject();
		json.put("success", true);
		response.setContentType("text/html");  
		response.getWriter().write(json.toJSONString());
	}
	
	/**
	 * 
	 * @method:circulate
	 * @Description:circulate 更新用户所选中的图书馆范围
	 * @author: HeTao
	 * @date:2016年5月26日 下午4:44:58
	 * @param:@param operation
	 * @param:@param menuIds
	 * @param:@param response
	 * @param:@throws IOException
	 * @return:void
	 */
	@RequestMapping(value="/circulate",method=RequestMethod.POST)
	@ResponseBody
	public String circulate(String checkedId,int scope,HttpServletRequest req) throws IOException{
		boolean flag = libraryService.updateLibraryScope(checkedId, scope);
		if(flag)
		return "location.reload()";
		else
		return "false";
	}
	
	
	
}
