package com.flea.modules.order.action;

import java.io.*;
import java.text.DateFormat;
import java.util.*;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.*;
import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Metadata;
import nl.siegmann.epublib.epub.EpubReader;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.alibaba.fastjson.JSONObject;
import com.flea.common.pojo.Page;
import com.flea.common.util.*;
import com.flea.common.util.exportExcel.ExportExcelUtils;
import com.flea.modules.customer.pojo.Customer;
import com.flea.modules.customer.service.CustomerService;
import com.flea.modules.ebook.pojo.Ebook;
import com.flea.modules.ebook.service.EbookService;
import com.flea.modules.order.pojo.CustomerAllot;
import com.flea.modules.order.pojo.Orders;
import com.flea.modules.order.pojo.OrdersDetail;
import com.flea.modules.order.service.CustomerAllotService;
import com.flea.modules.order.service.OrdersDetailService;
import com.flea.modules.order.service.OrdersService;

/**
 * 订单
 * 
 * @author Wuhua
 * @version 2016-06-18
 */
@Controller
@RequestMapping(value = "cms/order")
public class OrdersController {
	
	@Autowired
	private EbookService ebookService;
	
	@Autowired
	private OrdersService ordersService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private OrdersDetailService ordersDetailService;
	@Autowired
	private CustomerAllotService customerAllotService;

	/**
	 * 电子书配发 - 订单列表
	 * @param orders 订单
	 * @param search 订单号搜索
	 * @param request
	 * @param response
	 * @param model 视图
	 * @return
	 */
	@RequestMapping(value = { "orderList", "" })
	public ModelAndView orderList(Orders orders, String search, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		Page<Orders> page = ordersService.find(new Page<Orders>(request, response), orders, search);
		model.addAttribute("page", page);
		model.addAttribute("search",search);
		return new ModelAndView("allot/order_list");
	}

	/**
	 * 电子书配发 - 客户列表
	 * @param customer 客户
	 * @param search 客户代码搜索
	 * @param request 
	 * @param response 
	 * @param model 视图
	 * @return
	 */
	@RequestMapping(value = "/customerList")
	public ModelAndView customerList(CustomerAllot customer, String search, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		Page<CustomerAllot> page;
		if (StringUtils.isNotBlank(search)) {
			page = customerAllotService.find(new Page<CustomerAllot>(request, response), search.trim(), customer);
		} else { page = new Page<CustomerAllot>();}
//		
//		if (StringUtils.isNumeric(size)){
//			CookieUtils.setCookie(response, "pageSize", size);
//			page.setPageSize(Integer.parseInt(size));
//		}
		
		model.addAttribute("page", page);
		model.addAttribute("customer", customer);
		model.addAttribute("search", search);
		return new ModelAndView("allot/customer_list");
	}

	/**
	 * 订单详情处理页面
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/orderDetail/{id}")
	public ModelAndView detail(@PathVariable Integer id, String keywords) {
		Orders orders = ordersService.getOne(id);
		List<OrdersDetail> ordersDetails = ordersDetailService.getOrdersDetailListByOrdersID(id, keywords);
		ModelAndView mav = new ModelAndView("/allot/order_detail");
		JSONObject json;
		Set<Map<String, Object>> areaMaps = new HashSet<Map<String, Object>>();
		mav.addObject("orders", orders);
		mav.addObject("ordersDetails", ordersDetails);
		mav.addObject("ordersNum", ordersDetails.size());
		return mav;
	}

	/**
	 * 订单分析
	 * 
	 * @param 订单id
	 * @return
	 */
	@RequestMapping(value = "/analysis/{id}", method = RequestMethod.GET)
	public ModelAndView analysis(@PathVariable Integer id, String fromUrl) {
		ModelAndView mav = new ModelAndView("/allot/order_analysis");
		JSONObject json;
		Set<Map<String, Object>> areaMaps = new HashSet<Map<String, Object>>();
		Map analysisData = ordersService.analysis(id);
		mav.addObject("fromUrl", fromUrl);
		mav.addObject("analysisData", analysisData);
		return mav;
	}

	/**
	 * 订单补充
	 * @param 订单id
	 * @return
	 */
	@RequestMapping(value = "/appended/{id}", method = RequestMethod.GET)
	public ModelAndView appended(@PathVariable Integer id) {
		Orders orders = ordersService.getOne(id);
		ModelAndView mav = new ModelAndView("/allot/order_appended");
		JSONObject json;
		Set<Map<String, Object>> areaMaps = new HashSet<Map<String, Object>>();
		mav.addObject("id", id);
		mav.addObject("orders", orders);
		return mav;
	}

	/**
	 * 订单补充内检索
	 * 
	 * @param id
	 *            订单id
	 * @param keywords
	 *            检索关键字
	 * @param searchRole
	 *            查询条件
	 * @param status 
	 *            状态 1 加入成功            
	 *            
	 * @return
	 */
	@RequestMapping(value = "/appendedSearch/{id}/{customerID}", method = RequestMethod.GET)
	public ModelAndView appendedSearch(@PathVariable Integer id, @PathVariable Integer customerID, String[] keywords, Integer[] searchRole,
			Integer status) {
		Orders orders = ordersService.getOne(id);
		List<Ebook> ebooks = ordersDetailService.supplementsSearch(id, customerID, keywords, searchRole);
		ModelAndView mav = new ModelAndView("/allot/order_appended");
		Map keywordMap = new LinkedHashMap();
		Map roleMap = new LinkedHashMap();
		int index = 0;
		for(int i = 0; i<searchRole.length;i++) {
			//处理空值 缩减空串
			if("" != keywords[i]) {
				keywordMap.put(String.valueOf(index), keywords[i]);
				roleMap.put(String.valueOf(index), searchRole[i]);
				index++;
			}
		}
		mav.addObject("id", id);
		mav.addObject("orders", orders);
		mav.addObject("ebooks", ebooks);
		mav.addObject("status", status);
		mav.addObject("keywordMap", keywordMap);
		mav.addObject("roleMap", roleMap);
		return mav;
	}

	/**
	 * 加入订单  (已有订单)
	 * 
	 * @param id
	 *            订单ID
	 * @param ebookIds
	 *            加入的图书
	 * @return
	 */
	@RequestMapping(value = "appendedOrders/{id}/{customerID}", method = RequestMethod.POST)
	public ModelAndView appendedOrders(@PathVariable Integer id, @PathVariable Integer customerID, Long[] ebookIds, String[] keywords, Integer[] searchRole) {
		ordersDetailService.appendedOrders(id, ebookIds);
		ModelAndView mav = new ModelAndView("/allot/order_appended");
		return appendedSearch(id, customerID, keywords, searchRole, 2);
	}

	/**
	 * 订单新增页面
	 * 
	 * @param id 客户id
	 * @return
	 */
	@RequestMapping(value = "/added/{id}", method = RequestMethod.GET)
	public ModelAndView added(@PathVariable Integer id) {
		Customer customer = customerService.getOne(id);
		ModelAndView mav = new ModelAndView("/allot/order_added");
		JSONObject json;
		Set<Map<String, Object>> areaMaps = new HashSet<Map<String, Object>>();
		mav.addObject("customer", customer);
		return mav;
	}
	
	/**
	 * 订单新增页面检索
	 * @param id 客户id
	 * @param  keywords 检索关键字
	 * @param  searchRole 检索条件
	 * @param  status 状态
	 * @return
	 */
	@RequestMapping(value = "/addedSearch/{id}", method = RequestMethod.GET)
	public ModelAndView addedSearch(@PathVariable Integer id, String[] keywords, Integer[] searchRole, Integer status) {
		List<Ebook> ebooks = ordersDetailService.addedSearch(id, keywords, searchRole);
		Customer customer = customerService.getOne(id);
		ModelAndView mav = new ModelAndView("/allot/order_added");
		
		Map keywordMap = new LinkedHashMap();
		Map roleMap = new LinkedHashMap();
		int index = 0;
		for(int i = 0; i<searchRole.length;i++) {
			//处理空值 缩减空串
			if("" != keywords[i]) {
				keywordMap.put(String.valueOf(index), keywords[i]);
				roleMap.put(String.valueOf(index), searchRole[i]);
				index++;
			}
		}
		mav.addObject("id", id);
		mav.addObject("customer", customer);
		mav.addObject("ebooks", ebooks);
		mav.addObject("status", status);
		mav.addObject("keywordMap", keywordMap);
		mav.addObject("roleMap", roleMap);
		return mav;
	}

	
	/**
	 * 加入订单  (新增订单)
	 * @param id 
	 *            客户ID
	 * @param ebookIds
	 *            加入的图书
	 * @return
	 */
	@RequestMapping(value = "addedOrders/{id}", method = RequestMethod.POST)
	public ModelAndView addedOrders(@PathVariable Integer id, Long[] ebookIds, String[] keywords, Integer[] searchRole) {
		//判断该客户是否有未处理订单
//		List<Orders> UnSubmitOrder= ordersService.getUnSubmitOrdersList(id);
//		if(UnSubmitOrder.size() > 0) {
//			return addedSearch(id, null, null, -2);
//		}
		Orders  orders = ordersDetailService.addedOrders(id, ebookIds);
		return appendedSearch(orders.getId(), id,keywords, searchRole, 1);
	}

	/**
	 * 订单详情批量删除
	 * @param id
	 *            订单ID
	 * @param delIds
	 *            checkbox id值 对应数据库订单详情表id
	 * @return
	 */
	// 对应jsp页面中的deleteBatch()请求
	@RequestMapping(value = "deleteBatch/{id}", method = RequestMethod.POST)
	public ModelAndView deleteBatch(@PathVariable Integer id, Long[] delIds, RedirectAttributes redirectAttributes) {
		if (delIds != null && delIds.length != 0) {
			ordersDetailService.deleteBatch(id, delIds);
		}
		// 重定向到列表页面
		return detail(id, "");
	}

	/**
	 * 订单提交 修改订单状态
	 * @param id
	 *            订单ID
	 * @return
	 */
	@RequestMapping("submitOrders/{id}")
	public ModelAndView submitOrders(@PathVariable Integer id, Orders orders, String search, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		ordersService.submitOrder(id);
		return orderList(orders, search, request, response, model);
	}
}
