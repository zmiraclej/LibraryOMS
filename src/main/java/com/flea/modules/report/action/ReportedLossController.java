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
import com.flea.modules.report.pojo.vo.QueryCriteria;
import com.flea.modules.report.service.ReportedLossService;
import com.flea.modules.report.service.VLibraryBookService;
import com.flea.modules.report.util.ExprotPDF;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 报损统计Controller
 * @author bruce
 * @version 2016-07-15
 */
@Controller
@RequestMapping(value = "report/reportedLoss")
public class ReportedLossController  {

	@Autowired
	private ReportedLossService reportedLossService;
	@Autowired
	private VLibraryBookService vLibraryBookService;
	
	/**
	 * 
	 * @method:list 如果是普通用户就返回有数据的默认列表，如果是超级管理员，就返回空白的列表
	 * @Description:list
	 * @author: HeTao
	 * @date:2016年7月15日 下午2:46:10
	 * @param:@param lossReport
	 * @param:@param request
	 * @param:@param response
	 * @param:@param model
	 * @param:@return
	 * @return:ModelAndView
	 */
	@RequestMapping(value = {"list", ""})
	public ModelAndView list(CatalogueReport lossReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String, List<String>> map = vLibraryBookService.showTopSelect(null,null);
		model.addAttribute("area",map.get("area"));
        model.addAttribute("lib",map.get("lib"));
        model.addAttribute("hall",map.get("hall"));
        User user = ShiroUtils.getCurrentUser();
		Role role = user.getRoles().get(0);
		String le = role.getLevel();
		if(Common.ROLE_FIRST_LEVLE.equals(le)){
			model.addAttribute("sum","0-0.00");
			model.addAttribute("lib",vLibraryBookService.getAllLibLevel());
			model.addAttribute("isCustomer",false);
		}else{
			model.addAttribute("isCustomer",true);
			model.addAttribute("lib",vLibraryBookService.getAllLibLevel());
			//String sum = reportedLossService.getSum(map.get("area").get(0),map.get("lib").get(0));
        	model.addAttribute("sum",0.00);
		}
		Page<CatalogueReport> page= new Page<CatalogueReport>();
		//reportedLossService.find(new Page<CatalogueReport>(request,response),map.get("area"),map.get("lib"));
        model.addAttribute("page",page);
		return new ModelAndView("report/count_reportedloss");
	}


	/**
	 * 
	 * @method:getQueryInfo 根据查找条件进行查找
	 * @Description:getQueryInfo
	 * @author: HeTao
	 * @date:2016年7月15日 下午2:45:52
	 * @param:@param qc
	 * @param:@param req
	 * @param:@param resp
	 * @param:@return
	 * @return:String
	 */
	@RequestMapping("queryInfo")
	@ResponseBody
	public String getQueryInfo(QueryCriteria qc,HttpServletRequest req, HttpServletResponse resp) {
		Page<CatalogueReport> page = reportedLossService.find(new Page<CatalogueReport>(req,resp), qc);
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
		String realPath = reportedLossService.exprotListPDF(qc,request);
		if(!"".equals(realPath)){
			//把文件输出
			ExprotPDF.getInstance().exprotPDFList(realPath,response,"剔旧统计清单");
		}
	}
}
