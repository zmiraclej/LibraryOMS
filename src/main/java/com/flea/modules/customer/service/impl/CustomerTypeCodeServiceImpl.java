package com.flea.modules.customer.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flea.common.Common;
import com.flea.common.dao.AreaDao;
import com.flea.common.dao.CityDao;
import com.flea.common.dao.ProvinceDao;
import com.flea.common.pojo.Area;
import com.flea.common.pojo.City;
import com.flea.common.pojo.Province;
import com.flea.common.pojo.User;
import com.flea.common.service.impl.BaseServiceImpl;
import com.flea.common.util.ShiroUtils;
import com.flea.modules.customer.dao.CustomerLevelDao;
import com.flea.modules.customer.dao.CustomerTypeCodeDao;
import com.flea.modules.customer.pojo.Customer;
import com.flea.modules.customer.pojo.CustomerLevel;
import com.flea.modules.customer.pojo.CustomerTypeCode;
import com.flea.modules.customer.service.CustomerTypeCodeService;

/**
 * 客户分级地区查询
 * @author gouxl
 * @version 2016-05-15
 */
 @Service
public class CustomerTypeCodeServiceImpl extends BaseServiceImpl<CustomerTypeCode, Integer> implements CustomerTypeCodeService{

	@Autowired
	private CustomerTypeCodeDao customerTypeCodeDao;
	@Resource
	private ProvinceDao provinceDao;
	@Resource
	private CityDao cityDao;
	@Resource
	private AreaDao areaDao;
	@Resource
	private CustomerLevelDao customerLevelDao;

	@Override
	public List<Province> listProvince(Integer customerType) {
		//1.查询出所有的数据
		List<Province> list = new ArrayList<Province>();
		String hql ="from Province as province order by id";
		list = provinceDao.getListByHQL(hql, null);
		//2.查询客户分级是否存在该省
		List<CustomerTypeCode> listCustomer = new ArrayList<CustomerTypeCode>();
		listCustomer = customerTypeCodeDao.listProvince(customerType);
		//判断客户分级表不为空
		if(listCustomer != null && !listCustomer.isEmpty()){
			for(int i = 0 ; i < list.size() ; i++) {
				for (int j = 0; j < listCustomer.size(); j++) {
					if (list.get(i).getCode().equals(listCustomer.get(j).getLocationCode())) {
						list.remove(i);
					}
				}
			}
		}
		return list;
	}
	
	@Override
	public List<Province> listEditProvince(Integer customerType, String pCode) {
		//1.查询出所有的数据
		List<Province> list = new ArrayList<Province>();
		String hql ="from Province as province order by id";
		list = provinceDao.getListByHQL(hql, null);
		//2.查询客户分级是否存在该省
		List<CustomerTypeCode> listCustomer = new ArrayList<CustomerTypeCode>();
		listCustomer = customerTypeCodeDao.listProvince(customerType);
		//判断客户分级表不为空
		if(listCustomer != null && !listCustomer.isEmpty()){
			for(int i = 0 ; i < list.size() ; i++) {
				for (int j = 0; j < listCustomer.size(); j++) {
					//忽略回传的省级代码
					if (list.get(i).getCode().equals(pCode)) {
						continue;
					} 
					if (list.get(i).getCode().equals(listCustomer.get(j).getLocationCode())) {
						list.remove(i);
					}
				}
			}
		}
		return list;
	}

	@Override
	public List<City> listCity(String provinceCode, Integer customerType) {
		// TODO Auto-generated method stub
		//1.通过省级查询出所有的数据
		List<City> list = new ArrayList<City>();
		StringBuffer buffer = new StringBuffer();
		buffer.append("from City as city ");
		List<Object> values = new ArrayList<Object>();
		if(null != provinceCode){
			buffer.append("where provinceCode = ? order by id");
			values.add(provinceCode.toString());
		}
 		list = cityDao.getListByHQL(buffer.toString(), values);
 		//2.查询客户分级是否存在该市
 		List<CustomerTypeCode> listCustomer = new ArrayList<CustomerTypeCode>();
		listCustomer = customerTypeCodeDao.listCity(customerType);
		//判断客户分级表不为空
		if(listCustomer != null && !listCustomer.isEmpty()){
			for(int i = 0 ; i < list.size() ; i++) {
				for (int j = 0; j < listCustomer.size(); j++) {
					if (list.get(i).getCode().equals(listCustomer.get(j).getLocationCode())) {
						list.remove(i);
					}
				}
			}
		}
		return list;
	}
	
	@Override
	public List<City> listEditCity(String provinceCode, Integer customerType, String cityCode) {
		// TODO Auto-generated method stub
		//1.通过省级查询出所有的数据
		List<City> list = new ArrayList<City>();
		StringBuffer buffer = new StringBuffer();
		buffer.append("from City as city ");
		List<Object> values = new ArrayList<Object>();
		if(null != provinceCode){
			buffer.append("where provinceCode = ? order by id");
			values.add(provinceCode.toString());
		}
 		list = cityDao.getListByHQL(buffer.toString(), values);
 		//2.查询客户分级是否存在该市
 		List<CustomerTypeCode> listCustomer = new ArrayList<CustomerTypeCode>();
		listCustomer = customerTypeCodeDao.listCity(customerType);
		//判断客户分级表不为空
		if(listCustomer != null && !listCustomer.isEmpty()){
			for(int i = 0 ; i < list.size() ; i++) {
				for (int j = 0; j < listCustomer.size(); j++) {
					//忽略回传市级代码
					if (list.get(i).getCode().equals(cityCode)) {
						continue;
					}
					if (list.get(i).getCode().equals(listCustomer.get(j).getLocationCode())) {
						list.remove(i);
					}
				}
			}
		}
		return list;
	}

	@Override
	public List<Area> listArea(String cityCode) {
		List<Area> list = new ArrayList<Area>();
		StringBuffer buffer = new StringBuffer();
		buffer.append("from Area as Area ");
		List<Object> values = new ArrayList<Object>();
		if(null != cityCode){
			buffer.append("where cityCode = ? order by id");
			values.add(cityCode.toString());
		}
		list = areaDao.getListByHQL(buffer.toString(), values);
		return list;
	}

	@Override
	public void saveOne(Customer customer) {
		//判断客户级别是否省市级
		CustomerLevel customerLevel = customerLevelDao.getOne(customer.getLevelId());
		//不是省市级客户，不保存级联关系
		if (2 != customerLevel.getLevel() && 3 != customerLevel.getLevel()) {
			return;
		}
		CustomerTypeCode customerTypeCode = new CustomerTypeCode();
		customerTypeCode.setCustomerId(customer.getId());
		customerTypeCode.setCustomerLevel(customer.getLevelId());
		if (2 == customerLevel.getLevel()) {
			//省级
			//首先查询省级是否已经存入
			CustomerTypeCode ctc = customerTypeCodeDao.findCustomerTypeCode(customer.getProvinceCode(), customer.getLevelId());
			if (null != ctc) {
				return;
			}
			customerTypeCode.setLocationCode(customer.getProvinceCode());
			Province p = provinceDao.findProvinceByCode(customer.getProvinceCode());
			customerTypeCode.setCodeName(p.getName());
		}
		if (3 == customerLevel.getLevel()) {
			//市级级
			//首先查询市级是否已经存入
			CustomerTypeCode ctc = customerTypeCodeDao.findCustomerTypeCode(customer.getCityCode(), customer.getLevelId());
			if (null != ctc) {
				return;
			}
			customerTypeCode.setLocationCode(customer.getCityCode());
			City city = cityDao.findCityByCode(customer.getCityCode());
			customerTypeCode.setCodeName(city.getName());
		}
		customerTypeCodeDao.saveOne(customerTypeCode);
	}

	@Override
	public void updateOne(Customer customer) {
		//判断客户级别是否省市级
		CustomerLevel customerLevel = customerLevelDao.getOne(customer.getLevelId());
		//查询当前实体是否存在
		CustomerTypeCode customerTypeCode = customerTypeCodeDao.findOne(customer.getId());
		if (null == customerTypeCode) {
			//保存实体
			this.saveOne(customer);
		} else {
			//级联关系存在，并且不是省市级客户，删除客户地区级联关系
			if (2 != customerLevel.getLevel() && 3 != customerLevel.getLevel()) {
				customerTypeCodeDao.delById(customerTypeCode.getId());
			} else {
				//修改
				customerTypeCode.setCustomerId(customer.getId());
				customerTypeCode.setCustomerLevel(customer.getLevelId());
				if (2 == customerLevel.getLevel()) {
					//省级
					customerTypeCode.setLocationCode(customer.getProvinceCode());
					Province p = provinceDao.findProvinceByCode(customer.getProvinceCode());
					customerTypeCode.setCodeName(p.getName());
				}
				if (3 == customerLevel.getLevel()) {
					//市级级
					customerTypeCode.setLocationCode(customer.getCityCode());
					City c = cityDao.findCityByCode(customer.getCityCode());
					customerTypeCode.setCodeName(c.getName());
				}
				customerTypeCodeDao.updateOne(customerTypeCode);
			}
		}
	}

	@Override
	public CustomerTypeCode findCustomerTypeCode(Customer customer) {
		// TODO Auto-generated method stub
		//判断客户级别是否省市级
		CustomerLevel customerLevel = customerLevelDao.getOne(customer.getLevelId());
		CustomerTypeCode customerTypeCode = null;
		if (2 == customerLevel.getLevel()) {
			//首先查询省级是否已经存入
			customerTypeCode = customerTypeCodeDao.findCustomerTypeCode(customer.getProvinceCode(), customer.getLevelId());
		}
		if (3 == customerLevel.getLevel()) {
			//首先查询市级是否已经存入
			customerTypeCode = customerTypeCodeDao.findCustomerTypeCode(customer.getCityCode(), customer.getLevelId());
		}
		return customerTypeCode;
	}
}