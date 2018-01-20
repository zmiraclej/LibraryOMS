package com.flea.modules.report.action;

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
import com.flea.common.pojo.Page;
import com.flea.common.pojo.Role;
import com.flea.common.pojo.User;
import com.flea.common.util.ShiroUtils;
import com.flea.modules.report.pojo.CatalogueReport;
import com.flea.modules.report.pojo.Circulated;
import com.flea.modules.report.pojo.vo.QueryCriteria;
import com.flea.modules.report.service.CirculatedReportService;
import com.flea.modules.report.service.VLibraryBookService;
import com.flea.modules.report.util.ExprotPDF;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 流通统计Controller
 * @author bruce
 * @version 2016-07-16
 */
@Controller
@RequestMapping(value = "report/circulatedReport")
public class CirculatedReportController  {

	@Autowired
	private CirculatedReportService circulatedReportService;
	@Autowired
	private VLibraryBookService vLibraryBookService;
	
	/**
	 * 
	 * @method:list
	 * @Description:list 查询默认列表
	 * @author: HeTao
	 * @date:2016年7月13日 上午11:22:54
	 * @param:@param catalogueReport
	 * @param:@param request
	 * @param:@param response
	 * @param:@param model
	 * @param:@return
	 * @return:ModelAndView
	 */
	@RequestMapping(value = {"list", ""})
	public ModelAndView list(Circulated catalogueReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String, List<String>> map = vLibraryBookService.showTopSelect(null,null);
        model.addAttribute("area",map.get("area"));
        model.addAttribute("hall",map.get("hall"));
        User user = ShiroUtils.getCurrentUser();
		Role role = user.getRoles().get(0);
		String le = role.getLevel();
		if(Common.ROLE_FIRST_LEVLE.equals(le)){
			model.addAttribute("sum",new Circulated());
			model.addAttribute("lib",vLibraryBookService.getAllLibLevel());
			model.addAttribute("isCustomer",false);
		}else{
			model.addAttribute("isCustomer",true);
			//获取对应的的统计
			String sum = circulatedReportService.getSum();
			model.addAttribute("lib",vLibraryBookService.getAllLibLevel());
			//{"nowOut":0,"nowIn":0,"nowOutMoney":0,"nowInMoney":0,"totalNowOut":0,"totalNowin":0,"totalOutMoney":0,"totalNowInMoney":0}
        	model.addAttribute("sum",new Gson().fromJson(sum,Circulated.class));
		}
        Page<Circulated> page= new Page<Circulated>();
        //circulatedReportService.find(new Page<Circulated>(request,response),map.get("area"),map.get("lib"));
        model.addAttribute("page",page);
		return new ModelAndView("report/count_circulation");
	}

	/**
	 * 
	 * @method:getQueryInfo	根据条件获取对应记录
	 * @Description:getQueryInfo
	 * @author: HeTao
	 * @date:2016年7月15日 上午9:28:32
	 * @param:@param qc
	 * @param:@param req
	 * @param:@param resp
	 * @param:@return
	 * @return:String
	 */
	@RequestMapping("queryInfo")
	@ResponseBody
	public String getQueryInfo(QueryCriteria qc,HttpServletRequest req, HttpServletResponse resp) {
		Page<Circulated> page = circulatedReportService.find(new Page<Circulated>(req,resp), qc);
		page.setCont(page.toString().replaceAll("page","pageTo"));
		Gson gson = new GsonBuilder().setDateFormat("yyyyMMdd").create();
		return gson.toJson(page);
	}
	/**
	 * 
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
		String realPath = circulatedReportService.exprotListPDF(qc,request);
		if(!"".equals(realPath)){
			//把文件输出	文件名
			ExprotPDF.getInstance().exprotPDFList(realPath,response,"流通统计清单");
		}
	}
}
