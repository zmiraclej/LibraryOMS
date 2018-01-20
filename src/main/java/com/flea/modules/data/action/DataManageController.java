package com.flea.modules.data.action;

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

import org.apache.poi.util.SystemOutLogger;
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
 * 数据管理Controller
 * @author wuhua
 * @version 2016-07-14
 */
@Controller
@RequestMapping(value = "data/manage")
public class DataManageController  {


	//Marc数据导出列表
	@RequestMapping(value = "/marcList")
	public ModelAndView marcList(QueryCriteria query,HttpServletRequest request, HttpServletResponse response, Model model) {

			return new ModelAndView("data/marc_list");
	}
	
	//电子书导出列表
	@RequestMapping(value = "/ebookList")
	public ModelAndView ebookList(QueryCriteria query,HttpServletRequest request, HttpServletResponse response, Model model) {

			return new ModelAndView("data/ebook_list");
	}
	
	//数据上报
	@RequestMapping(value = "/dataReporting")
	public ModelAndView dataReporting(QueryCriteria query,HttpServletRequest request, HttpServletResponse response, Model model) {
			return new ModelAndView("data/data_reporting");
	}

}
