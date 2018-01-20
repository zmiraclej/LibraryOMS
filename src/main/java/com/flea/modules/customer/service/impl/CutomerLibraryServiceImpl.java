package com.flea.modules.customer.service.impl;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.flea.common.dao.BaseDao;
import com.flea.common.dao.UserDao;
import com.flea.common.dao.impl.BaseDaoImpl;
import com.flea.common.pojo.Area;
import com.flea.common.pojo.City;
import com.flea.common.pojo.Page;
import com.flea.common.pojo.User;
import com.flea.common.service.impl.BaseServiceImpl;
import com.flea.common.util.DateUtils;
import com.flea.common.util.ShiroUtils;
import com.flea.modules.customer.dao.CustomerDao;
import com.flea.modules.customer.dao.CustomerLevelDao;
import com.flea.modules.customer.dao.CutomerLibraryDao;
import com.flea.modules.customer.dao.LibraryCodeDao;
import com.flea.modules.customer.pojo.CutomerLibrary;
import com.flea.modules.customer.service.CutomerLibraryService;
import com.flea.modules.system.dao.LibraryDao;
import com.flea.modules.system.pojo.Library;
import com.flea.modules.system.util.DepartmentUtil;

/**
 * 分配馆号Service
 * @author Bruce
 * @version 2016-04-15
 */
 @Service
public class CutomerLibraryServiceImpl extends BaseServiceImpl<CutomerLibrary,Integer> implements CutomerLibraryService{

	@Autowired
	private CutomerLibraryDao cutomerLibraryDao;
	
	@Autowired
	private CustomerDao customerDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private LibraryDao libraryDao;

	@Autowired
	private CustomerLevelDao customerLevelDao;

	@Autowired
	private LibraryCodeDao libraryCodeDao;
	
	@Autowired
	public  CutomerLibraryServiceImpl(CutomerLibraryDao cutomerLibraryDao) {
		super(cutomerLibraryDao);
		this.cutomerLibraryDao = cutomerLibraryDao;
	}
	
	@Override
	public Page<CutomerLibrary> find(Page<CutomerLibrary> page,String search,CutomerLibrary cutomerLibrary) {
		return cutomerLibraryDao.findListBySearch(page, search);
	}

	@Override
//	@Transactional(readOnly=false)
	public boolean deleteById(int id) {
//		 CutomerLibrary  cutomerLibrary = cutomerLibraryDao.getOne(id);
//		 List<User> users= userDao.findByHallCode(cutomerLibrary.getStartCode());
//		 List<CutomerLibrary> cutomerLibrarys = cutomerLibraryDao.findByCustomerId(cutomerLibrary.getCustomer().getId());
//		 String hallCode ="";
//		 if(cutomerLibrarys.size()>1){
//			 hallCode = cutomerLibrarys.get(1).getStartCode();
//		 }
//		 for(User user:users){
//			 userDao.updateHallCodeById(hallCode, user.getId());
//		 }
		return cutomerLibraryDao.deleteById(id)>0?true:false;
	}

	/* (non-Javadoc)
	 * @see com.flea.modules.customer.service.CutomerLibraryService#findByCustomerId(java.lang.Integer)
	 */
	@Override
	public JSONObject findLibraryAreasByCustomerId(Integer customerId) {
		List<CutomerLibrary>  cutomerLibraries =cutomerLibraryDao.findByCustomerId(customerId);
		JSONObject json = new JSONObject();
		Set<Map<String,Object>> areaMaps = new HashSet<Map<String,Object>>();
		for(CutomerLibrary customerLibrary:cutomerLibraries){
			Map<String,Object> areaMap = new HashMap<String, Object>();
			Area area = customerLibrary.getArea();
			City city = area.getCity();
			areaMap.put("label", city.getProvince().getName()+"-"+city.getName()+"-"+area.getName());
			areaMap.put("value", area.getCode());
			areaMaps.add(areaMap);
		}
		json.put("areas", areaMaps);
		List<Map<String,Object>> levelMaps = new ArrayList<Map<String,Object>>();
		for(CutomerLibrary customerLibrary:cutomerLibraries) {
			Map<String,Object> levelMap = new HashMap<String, Object>();
			levelMap.put("label", customerLibrary.getLibraryLevel());
			levelMaps.add(levelMap);
		}
		json.put("levels", levelMaps);
		return json;
	}

	
	@Override
	public void saveOne(CutomerLibrary customerLibrary) {
//		String code = customerLibrary.getAssignCode();
//		String startCode =code.split("-")[0];
//		String endCode =code.split("-")[1];
//		customerLibrary.setStartCode(startCode);
//		customerLibrary.setEndCode(endCode);
		Integer customerId = customerLibrary.getCustomer().getId();
		List<CutomerLibrary> list = cutomerLibraryDao.findByCustomerId(customerId);
//		if(list.size()<1){//
//		 List<User> users= userDao.findByCustomerId(customerId);
//		 for(User user:users){
//			 //分级
//			 user.setHallCode(startCode);
//			 userDao.saveOrUpdateOne(user);
//		 }
//	   }
	   cutomerLibraryDao.saveOne(customerLibrary);
	}

	/* (non-Javadoc)
	 * @see com.flea.modules.customer.service.CutomerLibraryService#findLevelByAreaCode(java.lang.String)
	 */
	@Override
	public JSONArray findLevelByAreaCode(Integer areaCode) {
		User user = ShiroUtils.getCurrentUser();
		JSONArray jsonArray =this.cutomerLibraryDao.findLevelByAreaCode(areaCode, user.getId());
		return jsonArray;
	}

	/* (non-Javadoc)
	 * @see com.flea.modules.customer.service.CutomerLibraryService#findLibraryCodeById()
	 */
	@Override
	public JSONObject findLibraryCodeById(Integer Id) {
		CutomerLibrary cutomerLibrary = cutomerLibraryDao.getOne(Id);
		String startCode =  cutomerLibrary.getStartCode();
		String libraryNumber = cutomerLibrary.getLibraryNumber();
		int number = DepartmentUtil.depcodeToNumber(startCode);
		JSONObject json = new JSONObject();
	
		List<String> codeList = new ArrayList<String>();
		for(int i=0;i<Integer.parseInt(libraryNumber);i++){
			String code = DepartmentUtil.numberToDepcode(number+i, 4);
			Library libaryObj = libraryDao.findByCode(code);
			if(libaryObj==null){
				codeList.add(code);
			}
		}
		int used = Integer.parseInt(libraryNumber) - codeList.size();
		json.put("codeNumber",String.valueOf(used)+"/"+libraryNumber);
		json.put("codes",codeList);
		
		return json;
	}

	/* (non-Javadoc)
	 * @see com.flea.modules.customer.service.CutomerLibraryService#findListByCustomerId(java.lang.Integer)
	 */
	@Override
	public List<CutomerLibrary> findListByCustomerId(Integer customerId) {
		List<CutomerLibrary> libraries =  cutomerLibraryDao.findListByCustomerId(customerId);
		List<CutomerLibrary> list = new ArrayList<CutomerLibrary>();
		for(CutomerLibrary library:libraries) {
			String chargeStart = library.getChargeStartDate();
			if(StringUtils.isNotBlank(chargeStart)&&chargeStart.indexOf("-")>1) {
				library.setChargeStartDate(DateUtils.formatDate(chargeStart, "yyyyMMdd"));
			}
			String chargeEnd = library.getChargeEndDate();
			if(StringUtils.isNotBlank(chargeEnd)&&chargeEnd.indexOf("-")>1) {
				library.setChargeEndDate(DateUtils.formatDate(chargeEnd, "yyyyMMdd"));
			}
			String contactStart = library.getContractStartDate();
			if(StringUtils.isNotBlank(contactStart)&&contactStart.indexOf("-")>1) {
				library.setContractStartDate(DateUtils.formatDate(contactStart, "yyyyMMdd"));
			}
			String contactEnd = library.getContractEndDate();
			if(StringUtils.isNotBlank(contactEnd)&&contactEnd.indexOf("-")>1) {
				library.setContractEndDate(DateUtils.formatDate(contactEnd, "yyyyMMdd"));
			}
			list.add(library);
		}
		return list;
	}

	@Override
	public void updateOne(CutomerLibrary cutomerLibrary) {
		CutomerLibrary  mCutomerLibrary = cutomerLibraryDao.getOne(cutomerLibrary.getLibraryId());
		mCutomerLibrary.setProvince(cutomerLibrary.getProvince());
		mCutomerLibrary.setCity(cutomerLibrary.getCity());
		mCutomerLibrary.setArea(cutomerLibrary.getArea());
		mCutomerLibrary.setLibraryLevel(cutomerLibrary.getLibraryLevel());
		mCutomerLibrary.setChargeStandard(cutomerLibrary.getChargeStandard());
		mCutomerLibrary.setChargeMoney(cutomerLibrary.getChargeMoney());
		mCutomerLibrary.setChargeStartDate(cutomerLibrary.getChargeStartDate());
		mCutomerLibrary.setChargeEndDate(cutomerLibrary.getChargeEndDate());
		mCutomerLibrary.setContractStartDate(cutomerLibrary.getContractStartDate());
		mCutomerLibrary.setContractEndDate(cutomerLibrary.getContractEndDate());
		mCutomerLibrary.setAttachementFile(cutomerLibrary.getAttachementFile());
		cutomerLibraryDao.updateOne(mCutomerLibrary);
	}
	
	/**
	 * 获取未使用馆号
	 * @param sclId 批次ID
	 */
	public JSONObject findUnusedLibraryCodeById(Integer sclId){
		JSONObject json = new JSONObject();
		List<Object> params = new ArrayList<Object>();
		params.add(sclId);
		params.add(ShiroUtils.getCurrentUser().getCustomer().getId());
		CutomerLibrary  cutomerLibrary = cutomerLibraryDao.getOne(sclId);
		Integer usedCodeNumber = cutomerLibrary.getUsedCodeNumber();
		//老设计缺陷  : 可建馆数量设计为string 
		Integer totalCodeNumber = Integer.parseInt(cutomerLibrary.getLibraryNumber());
		json.put("codeNumber",usedCodeNumber +"/" + totalCodeNumber);
		return json;
	}
	
   	/**
   	 * 客户增加图书馆批次并预分配馆号 需要事务处理
   	 * @param id  system_customer_library 批次表ID
   	 * @return
   	 */
	public void addCustomerLibraryAndUpdateLibraryCode(CutomerLibrary customerLibrary) {
		this.saveOne(customerLibrary);
		//this.allotLibraryCode(customerLibrary);
	}
	/**
   	 * 根据批次给客户预分配馆号
   	 * @param id  system_customer_library 批次表ID
   	 * @return
   	 */		
	public void allotLibraryCode(CutomerLibrary customerLibrary) {
		
		String libraryLevel =  customerLibrary.getLibraryLevel();
		int number = Integer.parseInt(customerLibrary.getLibraryNumber());
		int customerId = customerLibrary.getCustomer().getId();
		int customerLibraryId = customerLibrary.getLibraryId();
		List params = new ArrayList();
		List<String> list = null;
		//一级：AAAA 公共图书馆I
		if (libraryLevel.equals("公共图书馆I")) {
			params.add(customerId);
			params.add(customerLibraryId);
			params.add(number);
			libraryCodeDao.querySql("Update lib_code set customer_id = ?, status = 2, scl_id = ?  where level = 1 and status = 0 limit ? ", params);
		}
		//二级：AAAB\BAAA 公共图书馆II \高校馆I 
		if (libraryLevel.equals("公共图书馆II") || libraryLevel.equals("高校馆I")) {
			params.add(customerId);
			params.add(customerLibraryId);
			params.add(number);
			libraryCodeDao.querySql("Update lib_code set customer_id = ?, status = 2, scl_id = ? where level = 2 and status = 0 limit ? ", params);
		}
		//三级 AABB\ABAB\BAAB\AABA\ABAA   公共图书馆III\高校馆II\中小学馆I
		if (libraryLevel.equals("公共图书馆III") || libraryLevel.equals("高校馆II") || libraryLevel.equals("中小学馆I")) {
			params.add(customerId);
			params.add(customerLibraryId);
			params.add(number);
			libraryCodeDao.querySql("Update lib_code set customer_id = ?, status = 2, scl_id = ? where level = 3 and status = 0 limit ? ", params);
		}
		//四级：AABC\BAAC\BCAA\ABCA\ABAC\CABA \ABCD  高校馆III\中小学馆II
		if (libraryLevel.equals("高校馆III") || libraryLevel.equals("中小学馆II")) {
			params.add(customerId);
			params.add(customerLibraryId);
			params.add(number);
			libraryCodeDao.querySql("Update lib_code set customer_id = ?, status = 2, scl_id = ? where level = 4 and status = 0 limit ? ", params);
		}
		//五级：其余  中小学馆III、农家书屋、社区书屋、社会馆
		if (libraryLevel.equals("中小学馆III") || libraryLevel.equals("农家书屋") || libraryLevel.equals("社区书屋") || libraryLevel.equals("社会馆")) {
			params.add(customerId);
			params.add(customerLibraryId);
			params.add(number);
			libraryCodeDao.querySql("Update lib_code set customer_id = ?, status = 2, scl_id = ? where level = 5 and status = 0 limit ? ", params);			
		}
	}
}
