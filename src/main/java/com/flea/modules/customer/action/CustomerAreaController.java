/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.flea.modules.customer.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;






import com.flea.common.pojo.Area;
import com.flea.common.pojo.City;
import com.flea.common.pojo.Province;
import com.flea.common.service.SystemAreasService;
import com.flea.modules.customer.pojo.Customer;
import com.flea.modules.customer.pojo.CustomerTypeCode;
import com.flea.modules.customer.service.CustomerTypeCodeService;
import com.google.gson.Gson;

/**
 * 客户分级地区查询
 * @author gouxl
 * @version 2017-05-15
 */
@Controller
@RequestMapping(value = "customer/area")
public class CustomerAreaController {
	
	@Autowired
	private CustomerTypeCodeService customerTypeCodeService;
	
	
	@Resource
	private SystemAreasService areaService;
	
	/**
	 * 获取省级
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getProvinceUnused.html", produces = "text/html;charset=UTF-8")
	public String getCustomerProvince(Integer customerType){
		List<Province> list = customerTypeCodeService.listProvince(customerType);
		Map<String, String> map = new HashMap<String,String>();
		for(Province c:list){
			map.put(c.getCode(), c.getName());	
		}
		return new Gson().toJson(map);
	}
	
	/**
	 * 修改客户信息获取省级
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getEditProvinceUnused.html", produces = "text/html;charset=UTF-8")
	public String getEditCustomerProvince(Integer customerType, String provinceCode){
		List<Province> list = customerTypeCodeService.listEditProvince(customerType, provinceCode);
		Map<String, String> map = new HashMap<String,String>();
		for(Province c:list){
			map.put(c.getCode(), c.getName());	
		}
		return new Gson().toJson(map);
	}
	
	/**
	 * 修改客户信息获取市级
	 * @param provinceCode 省级code
	 * @param customerType 客户类型
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getCityUnused.html", produces = "text/html;charset=UTF-8")
	public String getCity(Integer customerType, String provinceCode){
		List<City> list = customerTypeCodeService.listCity(provinceCode, customerType);
		Map<String, String> map = new HashMap<String,String>();
		for(City c:list){
			map.put(c.getCode(), c.getName());	
		}
		return new Gson().toJson(map);
	}
	
	/**
	 * 获取市级
	 *@param provinceCode 省级code
	 * @param customerType 客户类型
	 * @param cityCode 市级code
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getEditCityUnused.html", produces = "text/html;charset=UTF-8")
	public String getEditCity(Integer customerType, String provinceCode, String cityCode){
		List<City> list = customerTypeCodeService.listEditCity(provinceCode, customerType, cityCode);
		Map<String, String> map = new HashMap<String,String>();
		for(City c:list){
			map.put(c.getCode(), c.getName());	
		}
		return new Gson().toJson(map);
	}
	
	/**
	 * 获取区域
	 * @param code
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getAreaUnused.html", produces = "text/html;charset=UTF-8")
	public String getArea(String cityCode){
		List<Area> list = customerTypeCodeService.listArea(cityCode);
		Map<String, String> map = new HashMap<String,String>();
		for(Area c:list){
			map.put(c.getCode(), c.getName());
		}
		return new Gson().toJson(map);
	}
	
	/**
	 * 客户新增，保存级联关系
	 * @param customer
	 */
	@RequestMapping(value = "/save")
	public void save(Customer customer){
		//customerTypeCodeService.listCity(code, customerType);
		customerTypeCodeService.saveOne(customer);
	}
	
	/**
	 * 修改客户信息，编辑地区存入
	 * @param customer
	 */
	@RequestMapping(value = "/edit")
	public void edit(Customer customer){
		//customerTypeCodeService.listCity(code, customerType);
		customerTypeCodeService.updateOne(customer);
	}
	
	/**
	 * 查询客户地区省市级是否存在
	 * @param customer
	 */
	@RequestMapping(value = "/find")
	public void find(Customer customer){
		//customerTypeCodeService.listCity(code, customerType);
		CustomerTypeCode customerTypeCode = customerTypeCodeService.findCustomerTypeCode(customer);
		if (null == customerTypeCode) {
			System.out.println("可以保存！");
		} else {
			System.out.println("已经存在,不能在插入！返回失败！");
		}
	}
}
