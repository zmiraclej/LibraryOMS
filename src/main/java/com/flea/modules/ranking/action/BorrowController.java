package com.flea.modules.ranking.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.servlet.ModelAndView;
import com.flea.common.util.JsonUtil;
import com.flea.common.util.ShiroUtils;
import com.flea.common.Common;
import com.flea.common.pojo.Page;
import com.flea.common.pojo.Role;
import com.flea.common.pojo.User;
import com.flea.modules.ranking.pojo.Clicklike;
import com.flea.modules.ranking.service.BorrowService;
import com.flea.modules.report.pojo.vo.QueryCriteria;
import com.flea.modules.report.service.VLibraryBookService;
import com.flea.modules.report.util.ExprotPDF;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 点赞排行Controller
 * @author bruce
 * @version 2016-07-21
 */
@Controller
@RequestMapping(value = "ranking/borrow")
public class BorrowController  {

	@Autowired
	private BorrowService borrowService;
	@Autowired
	private VLibraryBookService vLibraryBookService;
	
	
	/**
	 * 
	 * @method:list	查询默认列表
	 * @Description:list
	 * @author: HeTao
	 * @date:2016年7月20日 上午9:54:00
	 * @param:@param clicklike
	 * @param:@param request
	 * @param:@param response
	 * @param:@param model
	 * @param:@return
	 * @return:ModelAndView
	 */
	@RequestMapping("list")
	public ModelAndView list(Clicklike clicklike, HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String, List<String>> map = vLibraryBookService.showTopSelect(null,null);
        model.addAttribute("area",map.get("area"));
        model.addAttribute("hall",map.get("hall"));
        User user = ShiroUtils.getCurrentUser();
		Role role = user.getRoles().get(0);
		String le = role.getLevel();
		if(Common.ROLE_FIRST_LEVLE.equals(le)){
			model.addAttribute("sum","0-0");
			model.addAttribute("lib",vLibraryBookService.getAllLibLevel());
			model.addAttribute("isCustomer",false);
		}else{
			model.addAttribute("lib",vLibraryBookService.getAllLibLevel());
			model.addAttribute("isCustomer",true);
			//获取对应的的统计
			//String sum = borrowService.getSum(map.get("area").get(0),map.get("lib").get(0));
        	model.addAttribute("sum","0-0");
		}
        Page<Clicklike> page= new Page<Clicklike>();
        borrowService.find(new Page<Clicklike>(request,response),map.get("area"),map.get("lib"));
        model.addAttribute("page",page);
		return new ModelAndView("report/ranking_borrow");
	}

	/**
	 * 
	 * @method:getQueryInfo	根据条件获取对应记录
	 * @Description:getQueryInfo
	 * @author: HeTao
	 * @date:2016年7月20日 上午9:53:57
	 * @param:@param qc
	 * @param:@param req
	 * @param:@param resp
	 * @param:@return
	 * @return:String
	 */
	@RequestMapping("queryInfo")
	@ResponseBody
	public String getQueryInfo(QueryCriteria qc,HttpServletRequest req, HttpServletResponse resp) {
		Page<Clicklike> page = borrowService.find(new Page<Clicklike>(req,resp), qc);
		page.setCont(page.toString().replaceAll("page","pageTo"));
		Gson gson = new GsonBuilder().setDateFormat("yyyyMMdd").create();
		return gson.toJson(page);
	}

	/**
	 * @method:exprotListPDF	导出清单
	 * @Description:exprotListPDF
	 * @author: HeTao
	 * @date:2016年7月25日 下午3:57:39
	 * @param:@param qc
	 * @param:@param request
	 * @param:@param response
	 * @return:void
	 */
	@RequestMapping("exportList")
	public void exprotListPDF(QueryCriteria qc,HttpServletRequest request, HttpServletResponse response) {
		//获取文件名 并且生成文件
		String realPath = borrowService.exprotListPDF(qc,request);
		if(!"".equals(realPath)){
			//把文件输出
			ExprotPDF.getInstance().exprotPDFList(realPath,response,"借书排行榜统计清单");
		}
	}
	
}
