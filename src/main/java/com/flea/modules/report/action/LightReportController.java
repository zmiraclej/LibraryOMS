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
import com.flea.modules.report.pojo.LightReport;
import com.flea.modules.report.pojo.vo.QueryCriteria;
import com.flea.modules.report.service.LightReportService;
import com.flea.modules.report.service.ReaderReportService;
import com.flea.modules.report.service.VLibraryBookService;
import com.flea.modules.report.util.ExprotPDF;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 图书馆开放时间统计Controller
 * @author 
 * @version 2016-11-22
 */
@Controller
@RequestMapping(value = "report/lightReport")
public class LightReportController  {

	@Autowired
	private LightReportService lightReportService;
	@Autowired
	private ReaderReportService readerReportService;
	@Autowired
	private VLibraryBookService vLibraryBookService;
	
	/**
	 * 返回查询条件选择
	 * @param catalogueReport
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"list", ""})
	public ModelAndView list(CatalogueReport catalogueReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String, List<String>> map = vLibraryBookService.showTopSelect(null,null);
        model.addAttribute("area",map.get("area"));
        User user = ShiroUtils.getCurrentUser();
		Role role = user.getRoles().get(0);
		String le = role.getLevel();
		if(Common.ROLE_FIRST_LEVLE.equals(le)){
			model.addAttribute("sum","0-0:00");
			model.addAttribute("lib",vLibraryBookService.getAllLibLevel());
			model.addAttribute("isCustomer",false);
		}else{
			model.addAttribute("isCustomer",true);
			model.addAttribute("lib",vLibraryBookService.getAllLibLevel());
			//获取对应的的统计
			//String sum = readerReportService.getSum(map.get("area").get(0),map.get("lib").get(0));
        	model.addAttribute("sum","0-0:00");
		}
        Page<CatalogueReport> page= new Page<CatalogueReport>();
        //readerReportService.find(new Page<CatalogueReport>(request,response),map.get("area"),map.get("lib"));
        model.addAttribute("page",page);
		return new ModelAndView("report/count_light");
	}

	
	/**
	 * 
	 * @method:getQueryInfo		根据条件获取对应记录
	 * @Description:getQueryInfo
	 * @author: HeTao
	 * @date:2016年7月16日 上午9:33:31
	 * @param:@param qc
	 * @param:@param req
	 * @param:@param resp
	 * @param:@return
	 * @return:String
	 */
	@RequestMapping("queryInfo")
	@ResponseBody
	public String getQueryInfo(QueryCriteria qc,HttpServletRequest req, HttpServletResponse resp) {
		Page<LightReport> page = lightReportService.find(new Page<LightReport>(req,resp), qc);
//		Page<CatalogueReport> page1 = readerReportService.find(new Page<CatalogueReport>(req,resp), qc);
		page.setCont(page.toString().replaceAll("page","pageTo"));
		Gson gson = new GsonBuilder().setDateFormat("yyyyMMdd").create();
		return gson.toJson(page);
	}
	
	/**
	 * 
	 * @method:exprotListPDF	导出pdf清单
	 * @Description:exprotListPDF
	 * @param:@param qc
	 * @param:@param request
	 * @param:@param response
	 * @return:void
	 */
	@RequestMapping("exportList")
	public void exprotListPDF(QueryCriteria qc,HttpServletRequest request, HttpServletResponse response) {
		//获取文件名 并且生成文件
		String realPath = lightReportService.exprotListPDF(qc, request);
		if (!"".equals(realPath)) {
			//把文件输出
			ExprotPDF.getInstance().exprotPDFList(realPath, response, "开放时长统计清单");
		}
	}
}
