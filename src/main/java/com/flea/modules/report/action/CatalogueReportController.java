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
import com.flea.modules.report.service.CatalogueReportService;
import com.flea.modules.report.service.VLibraryBookService;
import com.flea.modules.report.util.ExprotPDF;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 编目统计Controller
 * @author bruce
 * @version 2016-07-11
 */
@Controller
@RequestMapping(value = "report/catalogueReport")
public class CatalogueReportController  {

	@Autowired
	private CatalogueReportService catalogueReportService;
	
	@Autowired
	private VLibraryBookService vLibraryBookService;
	
	
	
	@RequestMapping(value = {"list", ""})
	public ModelAndView list(CatalogueReport catalogueReport,QueryCriteria query, HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String, List<String>> map = vLibraryBookService.showTopSelect(query.getArea(),query.getLib());
        model.addAttribute("area",map.get("area"));
        model.addAttribute("lib",map.get("lib"));
        model.addAttribute("hall",map.get("hall"));
        model.addAttribute("sum","0-0.00");
        User user = ShiroUtils.getCurrentUser();
		Role role = user.getRoles().get(0);
		String le = role.getLevel();
		if(Common.ROLE_FIRST_LEVLE.equals(le)){
			model.addAttribute("sum","0-0.00");
			model.addAttribute("lib",vLibraryBookService.getAllLibLevel());
			model.addAttribute("isCustomer",false);
		}else{
			model.addAttribute("lib",vLibraryBookService.getAllLibLevel());
			model.addAttribute("isCustomer",true);
		}
        Page<CatalogueReport> page= new Page<CatalogueReport>();
        //catalogueReportService.find(new Page<CatalogueReport>(request,response),map.get("area"),map.get("lib"));
        model.addAttribute("page",page);
       return new ModelAndView("report/count_catalogue");
	}

	/**
   	 * 
   	 * @method:find
   	 * @Description:find	根据查询条件查询
   	 * @author: HeTao
   	 * @date:2016年7月11日 下午4:41:15
   	 * @param:@param page
   	 * @param:@param qc
   	 * @param:@return
   	 * @return:Page<CatalogueReport>
   	 */
	@RequestMapping("queryInfo")
	@ResponseBody
	public String getQueryInfo(QueryCriteria qc,HttpServletRequest req, HttpServletResponse resp) {
		Page<CatalogueReport> page = catalogueReportService.find(new Page<CatalogueReport>(req,resp), qc, req.getSession());

		
		page.setCont(page.toString().replaceAll("page","pageTo"));
		Gson gson = new GsonBuilder().setDateFormat("yyyyMMdd").create();
		String str = gson.toJson(page);
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
		String realPath = catalogueReportService.exprotListPDF(qc,request);
		if(!"".equals(realPath)){
			//把文件输出	文件名
			ExprotPDF.getInstance().exprotPDFList(realPath,response,"入藏统计清单");
		}
	}
	
}
