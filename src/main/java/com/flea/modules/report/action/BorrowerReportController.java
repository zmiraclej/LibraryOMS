package com.flea.modules.report.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONObject;

import org.springframework.web.servlet.ModelAndView;

import com.flea.common.Common;
import com.flea.common.util.DateUtils;
import com.flea.common.util.JsonUtil;
import com.flea.common.util.ShiroUtils;
import com.flea.common.pojo.Page;
import com.flea.common.pojo.Role;
import com.flea.common.pojo.User;
import com.flea.modules.report.pojo.BorrowerReport;
import com.flea.modules.report.pojo.SearchResult;
import com.flea.modules.report.pojo.vo.QueryCriteria;
import com.flea.modules.report.service.BorrowerReportService;
import com.flea.modules.report.service.VLibraryBookService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 借书统计Controller
 * @author bruce
 * @version 2016-07-14
 */
@Controller
@RequestMapping(value = "report/borrower")
public class BorrowerReportController  {

	@Autowired
	private BorrowerReportService borrowerReportService;
	@Autowired
	private VLibraryBookService vLibraryBookService;
	
	
	@RequestMapping(value = {""})
	public ModelAndView list(QueryCriteria query,HttpServletRequest request, HttpServletResponse response, Model model) {
		 User user = ShiroUtils.getCurrentUser();
			Role role = user.getRoles().get(0);
			String le = role.getLevel();
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
		        query.setArea(map.get("area").get(0));
				query.setLib(map.get("lib").get(0));
				model.addAttribute("isCustomer",true);
			}
	        Page<BorrowerReport> page=new Page<BorrowerReport>();
	     //   page=borrowerReportService.find(new Page<BorrowerReport>(request,response),query);
	    	page.setCont(page.toString().replaceAll("page","pageTo"));
	        model.addAttribute("page",page);
	        model.addAttribute("query",query);
			return new ModelAndView("report/count_borrow");
	}

	@RequestMapping("list")
	@ResponseBody
	public String getQueryInfo(QueryCriteria qc,HttpServletRequest req, HttpServletResponse resp) {
		Page<BorrowerReport> page = borrowerReportService.find(new Page<BorrowerReport>(req,resp), qc);
		page.setCont(page.toString().replaceAll("page","pageTo"));
		Gson gson = new GsonBuilder().setDateFormat("yyyyMMdd").create();
		return gson.toJson(page);
	}
	@RequestMapping("data")
	@ResponseBody
	public String getListByCategory(QueryCriteria query,HttpServletRequest req, HttpServletResponse resp) {
		SearchResult<BorrowerReport> dataList=borrowerReportService.findLibraryDataList(query);
		Gson gson = new Gson();
		return gson.toJson(dataList);
	}
	
	@RequestMapping("exportList")
	public void exprotListPDF(QueryCriteria query,HttpServletRequest request, HttpServletResponse response,HttpSession session) {
		String realPath = session.getServletContext().getRealPath("/");
		realPath = realPath.replace("library\\", "");
		realPath =realPath +"/pdf/"+ShiroUtils.getCurrentUser().getUserName()+DateUtils.getDate("yyyyMMdd")+".pdf";
		borrowerReportService.exportLibraryBookPDF(query, realPath);
        // 取得文件名。
        try {
        	String filename = "借书统计"+DateUtils.getDate("yyyyMMdd")+".pdf";
			InputStream fis = new BufferedInputStream(new FileInputStream(realPath));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			// 清空response
			response.reset();
			// 设置response的Header
			response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes("GBK"), "ISO-8859-1"));
			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/pdf");
			toClient.write(buffer);
			toClient.flush();
			toClient.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@RequestMapping("exportData")
	public void exprotDataPDF(QueryCriteria query,HttpServletRequest request, HttpServletResponse response,HttpSession session) {
		String realPath = session.getServletContext().getRealPath("/");
		realPath = realPath.replace("library\\", "");
		realPath =realPath +"/pdf/"+ShiroUtils.getCurrentUser().getUserName()+DateUtils.getDate("yyyyMMdd")+".pdf";
		borrowerReportService.exportCategoryDataPDF(query, realPath);
        // 取得文件名。
        try {
        	String filename = "借书统计"+DateUtils.getDate("yyyyMMdd")+".pdf";
			InputStream fis = new BufferedInputStream(new FileInputStream(realPath));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			// 清空response
			response.reset();
			// 设置response的Header
			response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes("GBK"),"ISO-8859-1"));
			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/pdf");
			toClient.write(buffer);
			toClient.flush();
			toClient.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@RequestMapping(value = "form")
	public String form(BorrowerReport borrowerReport, Model model) {
		model.addAttribute("borrowerReport", borrowerReport);
		return "report/borrowerReportForm";
	}

	@RequestMapping(value = "save")
	public ModelAndView save(BorrowerReport borrowerReport, Model model) {
		borrowerReportService.saveOne(borrowerReport);
		return new ModelAndView("redirect:list.html");
	}
	
	@RequestMapping(value = "del")
	@ResponseBody
	public JSONObject delete(Integer id, RedirectAttributes redirectAttributes) {
	return JsonUtil.createSuccessJson(borrowerReportService.deleteById(id));
	}

}
