package com.flea.modules.data.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.flea.modules.data.pojo.vo.DataAuthoInfo;
import com.flea.modules.data.service.DataReportingService;
import com.flea.modules.data.service.impl.DataReportingServiceImpl;
import com.flea.modules.report.pojo.vo.QueryCriteria;

/**
 * 数据管理Controller
 * 
 * @author wuhua
 * @version 2016-07-14
 */
@Controller
@RequestMapping(value = "data/dataReporting")
public class DataReportingController {
	
	@Autowired
	DataReportingService DataReportingServiceImpl;
	/**
	 * 接口1
	 * @接口名称 : 获取上次上传截止时间
	 * @接口标识: finddeadline
	 * @接口说明: 调用明细接口前，获取上次上传的截止时间，避免重复上传
	 * @指标索引号: 无
	 * @频率: 每次调用明细数据接口前
	 */
	@RequestMapping(value = "/finddeadline")
	public ModelAndView finddeadline(QueryCriteria query, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		return new ModelAndView("data/marc_list");
	}

	/**
	 * 接口2  
	 * @接口名称 : 更新本次上传截止时间
	 * @接口标识: updatedeadline
	 * @接口说明: 调用明细接口后，更新本次上传的截止时间 (可将本地记录的添加时间作为截止时间）
	 * @指标索引号: 无
	 * @频率: 每次调用明细数据接口后
	 */
	@RequestMapping(value = "/updatedeadline")
	public ModelAndView updatedeadline(QueryCriteria query, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		return new ModelAndView("data/marc_list");
	}
	
	/**
	 * 接口3
	 * @接口名称 : 接收公共图书馆年流通数据量（万册次）接口
	 * @接口标识: borrowedTotal
	 * @接口说明: 获取成员馆或分馆年流通数据
	 * @指标索引号: C1294
	 * @频率: 一次（按年份区分）
	 */
	@RequestMapping(value = "/borrowedTotal")
	public ModelAndView borrowedTotal(QueryCriteria query, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		return new ModelAndView("data/marc_list");
	}

	/**
	 * 接口4
	 * @接口名称 : 接收公共图书馆年流通册次明细信息接口
	 * @接口标识: borrowedTotal
	 * @接口说明: 接收公共图书馆年流通册次明细信息接口
	 * @指标索引号: C1294
	 * @频率: 10分钟 （评估期数据，每次1000条，增量上传）
	 */
	@RequestMapping(value = "/borrowedDetail")
	public ModelAndView borrowedDetail(QueryCriteria query, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		return new ModelAndView("data/marc_list");
	}

	/**
	 * 接口5
	 * @接口名称 : 接收年读者到馆量（人次）接口
	 * @接口标识: libVisitorsTotal
	 * @接口说明: 接收年读者人均到馆量接口
	 * @指标索引号: C1293
	 * @频率: 一次（按年份区分）
	 */
	@RequestMapping(value = "/libVisitorsTotal")
	public ModelAndView libVisitorsTotal(QueryCriteria query, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		return new ModelAndView("data/marc_list");
	}
	/**
	 * 接口6
	 * @接口名称 : 接收活动总数接口
	 * @接口标识: activityTotal
	 * @接口说明: 接收活动总数接口
	 * @指标索引号: C1304
	 * @频率: 一次（按年份区分）
	 */
	@RequestMapping(value = "/activityTotal")
	public ModelAndView activityTotal(QueryCriteria query, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		return new ModelAndView("data/marc_list");
	}
	
	/**
	 * 接口7
	 * @接口名称 : 接收活动详情接口
	 * @接口标识: activityTotal
	 * @接口说明: 接收活动详情接口
	 * @指标索引号: C1304
	 * @频率: 10分钟（评估期数据，每次200条，增量上传）
	 */
	@RequestMapping(value = "/activityDetail")
	public ModelAndView activityDetail(QueryCriteria query, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		return new ModelAndView("data/marc_list");
	}

	/**
	 * 接口8
	 * @接口名称 : 接收展览总次数
	 * @接口标识: exhibitionTotal
	 * @接口说明: 接收展览总次数
	 * @指标索引号: C1305
	 * @频率: 一次（按年份区分）
	 */
	@RequestMapping(value = "/exhibitionTotal")
	public ModelAndView exhibitionTotal(QueryCriteria query, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		return new ModelAndView("data/marc_list");
	}
	/**
	 * 接口9
	 * @接口名称 : 接收展览详情接口
	 * @接口标识: exhibitionTotal
	 * @接口说明: 接收展览详情接口
	 * @指标索引号: C1305
	 * @频率: 10分钟（评估期数据，每次200条，增量上传）
	 */
	@RequestMapping(value = "/exhibitionDetail")
	public ModelAndView exhibitionDetail(QueryCriteria query, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		return new ModelAndView("data/marc_list");
	}
	/**
	 * 接口10
	 * @接口名称 : 接收公共图书馆年网站访问量（次）接口
	 * @接口标识: annualWebsiteVisits
	 * @接口说明: 获取成员馆年网站访问总次数数据
	 * @指标索引号: C1316
	 * @频率: 一次（按年份区分）
	 */
	@RequestMapping(value = "/annualWebsiteVisits")
	public ModelAndView annualWebsiteVisits(QueryCriteria query, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		return new ModelAndView("data/marc_list");
	}
	/**
	 * 接口11
	 * @接口名称 : 接收普通文献的馆藏量(册件)
	 * @接口标识: annualWebsiteVisits
	 * @接口说明: 获取普通文献的馆藏数量（不含电子文献馆藏)
	 * @指标索引号: C3404
	 * @频率: 一次（按年份区分）
	 */
	@RequestMapping(value = "/genaralCollection")
	public ModelAndView genaralCollection(QueryCriteria query, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		return new ModelAndView("data/marc_list");
	}
	/**
	 * 接口12
	 * @接口名称 : 接收普通文献的馆藏量(册件)
	 * @接口标识: literatureDetail
	 * @接口说明: 获取馆内各类型文献的明细信息
	 * @指标索引号: C3404
	 * @频率: 10分钟（评估期数据，每次1000条，增量上传）
	 */
	@RequestMapping(value = "/literatureDetail")
	public ModelAndView literatureDetail(QueryCriteria query, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		return new ModelAndView("data/marc_list");
	}
	/**
	 * 接口13
	 * @接口名称 : 接收普通文献的馆藏量(册件)
	 * @接口标识: literatureDetail
	 * @接口说明: 获取指定书目的成员馆馆藏描述明细信息。
	 * @指标索引号: C3404
	 * @频率: 10分钟（评估期数据，每次1000条，增量上传）
	 */
	@RequestMapping(value = "/fetchCollections")
	public ModelAndView fetchCollections(QueryCriteria query, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		return new ModelAndView("data/marc_list");
	}
	/**
	 * 接口14
	 * @接口名称 : 接收普通文献的馆藏量(册件)
	 * @接口标识: addliterature
	 * @接口说明: 获取成员馆新增文献入藏量统计数据（不含电子文入藏)
	 * @指标索引号: C3405
	 * @频率: 一次（按年份区分）
	 */
	@RequestMapping(value = "/addliterature")
	public ModelAndView addliterature(QueryCriteria query, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		return new ModelAndView("data/marc_list");
	}
	/**
	 * 接口15
	 * @接口名称 : 接收公共图书馆用户总数接口
	 * @接口标识: userTotal
	 * @接口说明: 获取成员馆读者用户总数
	 * @指标索引号: C1292
	 * @频率: 一次/周
	 */
	@RequestMapping(value = "/userTotal")
	public ModelAndView userTotal(QueryCriteria query, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		return new ModelAndView("data/marc_list");
	}
	/**
	 * 接口16
	 * @接口名称 : 接收公共图书馆读者办证数据接口
	 * @接口标识: userDetail
	 * @接口说明: 接收成员馆中的读者数据
	 * @指标索引号: C1292
	 * @频率: 每周增量
	 */
	@RequestMapping(value = "/userDetail")
	public ModelAndView userDetail(QueryCriteria query, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		return new ModelAndView("data/marc_list");
	}
	
	
	@RequestMapping(value = "/entrance")
	@ResponseBody
	public JSONObject entrance(@RequestBody List<DataAuthoInfo> dataAuthoInfos,Model model) {
		DataReportingServiceImpl.entrance(dataAuthoInfos);
		return null;
		
	}
}
