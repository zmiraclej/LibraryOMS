package com.flea.modules.customer.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.SystemOutLogger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.flea.common.Common;
import com.flea.common.dao.AreaDao;
import com.flea.common.dao.BaseDao;
import com.flea.common.dao.CityDao;
import com.flea.common.dao.ProvinceDao;
import com.flea.common.dao.UserDao;
import com.flea.common.pojo.Area;
import com.flea.common.pojo.City;
import com.flea.common.pojo.Page;
import com.flea.common.pojo.Province;
import com.flea.common.pojo.Role;
import com.flea.common.pojo.User;
import com.flea.common.service.impl.BaseServiceImpl;
import com.flea.common.util.CacheUtils;
import com.flea.common.util.Config;
import com.flea.common.util.ShiroUtils;
import com.flea.modules.customer.dao.CustomerContactDao;
import com.flea.modules.customer.dao.CustomerDao;
import com.flea.modules.customer.dao.CustomerLevelDao;
import com.flea.modules.customer.dao.CutomerLibraryDao;
import com.flea.modules.customer.pojo.Customer;
import com.flea.modules.customer.pojo.CustomerContact;
import com.flea.modules.customer.pojo.CustomerLevel;
import com.flea.modules.customer.pojo.CutomerLibrary;
import com.flea.modules.customer.service.CustomerService;
import com.flea.modules.customer.service.CustomerTypeCodeService;
import com.flea.modules.system.dao.LibraryDao;
import com.flea.modules.system.dao.LibraryUsersDao;
import com.flea.modules.system.pojo.Library;
import com.flea.modules.system.service.LibraryService;
import com.flea.modules.system.util.DepartmentUtil;

/**
 * 客户Service
 * 
 * @author Bruce
 * @version 2016-04-15
 */
@Service
public class CustomerServiceImpl extends BaseServiceImpl<Customer, Integer> implements CustomerService {

	@Autowired
	private CustomerDao customerDao;

	@Autowired
	private CustomerContactDao customerContactDao;

	@Autowired
	private CutomerLibraryDao cutomerLibraryDao;

	@Autowired
	private CustomerLevelDao customerLevelDao;

	@Autowired
	private ProvinceDao provinceDao;

	@Autowired
	private CityDao cityDao;
	
	@Autowired
	private AreaDao areaDao;
	
	@Resource
	private LibraryUsersDao libraryUsersDao;
	
	@Autowired
	private CustomerTypeCodeService customerTypeCodeService;
	
	@Autowired
	private LibraryService libraryService;
	@Autowired
	public CustomerServiceImpl(CustomerDao customerDao) {
		super(customerDao);
		this.customerDao = customerDao;
	}

	@Override
	public Page<Customer> find(Page<Customer> page, String search, Customer customer) {
		DetachedCriteria dc = customerDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(customer.getName())) {
			dc.add(Restrictions.eq("name", customer.getName()));
		}
		if (StringUtils.isNotBlank(search)) {
			dc.add(Restrictions.or(Restrictions.like("name", "%" + search + "%"),
					Restrictions.like("hallCode", "%" + search + "%")));
		}
		dc.add(Restrictions.eq("isEffective", Common.FLAG_ACTIVATION));
		return customerDao.find(page, dc);
	}

	@Override
	public void saveOne(Customer customer) {
		List<CustomerContact> contacts = customer.getContacts();
		List<CustomerContact> contactList = new ArrayList<CustomerContact>();
		for (CustomerContact contact : contacts) {
			contact.setCustomer(customer);
			contactList.add(contact);
		}
		List<User> users = customer.getUsers();
		List<User> userList = new ArrayList<User>();
		for (User user : users) {
			setUserRoles(user, user);
			if (StringUtils.isNotBlank(user.getPassword())) {
				user.setPassword(DigestUtils.md5Hex(user.getPassword()));
			}
			user.setCustomer(customer);
			user.setRemark("1");
			userList.add(user);
		}
		customer.setContacts(contactList);
		;
		customer.setUsers(userList);
		customerDao.saveOne(customer);
	}

	@Autowired
	private UserDao userDao;

	@Override
	public void updateOne(Customer customer) {
		int customerId = customer.getId();
		List<CustomerContact> customerContacts = customerContactDao.findByCustomerId(customerId);
		List<CustomerContact> contacts = customer.getContacts();
		List<CustomerContact> contactList = new ArrayList<CustomerContact>();
		for (CustomerContact contact : contacts) {
			if (customerContacts.contains(contact)) {
				customerContacts.remove(contact);
			}
			contact.setCustomer(customer);
			contactList.add(contact);
		}
		for (CustomerContact contact : customerContacts) {
			customerContactDao.deleteById(contact.getId());
		}
		// 设置第一个用户到list，
		List<User> customerUsers = userDao.findByCustomerId(customerId);
		List<User> users = customer.getUsers();
		System.out.println(" users.size()" + users.size());
		List<User> userList = new ArrayList<User>();
		for (User user : users) {
			System.out.println("user: " + user.getId());
			System.out.println("user: " + user.getRoleIds());
			System.out.println(" StringUtils.isNotBlank(user.getRoleIds()) " + StringUtils.isNotBlank(user.getRoleIds()));
			if (StringUtils.isNotBlank(user.getRoleIds()) && null != user.getId()) {
				User mUser = userDao.getOne(user.getId());
				setUserRoles(user, mUser);
				if (StringUtils.isNotBlank(user.getPassword())) {
					mUser.setPassword(user.getPassword());
				}
				if (StringUtils.isNotBlank(user.getHallCode())) {
					mUser.setHallCode(user.getHallCode());
				}
				mUser.setUserName(user.getUserName());
				mUser.setTel(user.getTel());
				mUser.setChat(user.getChat());
				mUser.setPhone(user.getPhone());
				mUser.setCustomer(customer);
				userList.add(mUser);
			} else {
				for (User newUser : users) {
					setUserRoles(newUser, newUser);
					if (StringUtils.isNotBlank(newUser.getPassword())) {
						newUser.setPassword(DigestUtils.md5Hex(newUser.getPassword()));
					}
					newUser.setCustomer(customer);
					newUser.setRemark("1");
					userList.add(newUser);
				}
			}
			if (customerUsers.contains(user)) {
				customerUsers.remove(user);
			}
		}
		// 删除所有用户
		// for(User user:customerUsers){
		// userDao.deleteById(user.getId());
		// }
		customer.setContacts(contactList);
		// 将第一个用户重新添加
		customer.setUsers(userList);
		customerDao.updateOne(customer);
	}

	private void setUserRoles(User user, User mUser) {
		String[] roleids = user.getRoleIds().split(",");
		if (roleids != null) {
			List<Role> roleSet = new ArrayList<Role>();
			for (int i = 0; i < roleids.length; i++) {
				Role role = new Role();
				role.setId(Integer.valueOf(roleids[i]));
				roleSet.add(role);
			}
			mUser.setRoles(roleSet);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.flea.modules.customer.service.LibraryCodeService#assignLibraryCode(
	 * java.lang.Integer)
	 */
	@Override
	public Map<String, String> assignLibraryCode(String startCode, String prevCode, Integer number, Boolean modify) {
		Map<String, String> map = new HashMap<String, String>();
		String code = (String) CacheUtils.get("endCode");
		if (StringUtils.isNotBlank(code) && !modify) {// 第二个
			startCode = (String) CacheUtils.get("endCode");
			int asciiNumber = DepartmentUtil.depcodeToNumber(startCode);
			++asciiNumber;
			startCode = DepartmentUtil.numberToDepcode(asciiNumber, 4);
		} else if (StringUtils.isBlank(startCode) && !modify) {// 第一个
			startCode = customerDao.findMaxLibraryCode();
			if (StringUtils.isBlank(startCode))
				startCode = Common.BEGIN_HALL_CODE;
			int asciiNumber = DepartmentUtil.depcodeToNumber(startCode);
			asciiNumber = asciiNumber + 1;
			startCode = DepartmentUtil.numberToDepcode(asciiNumber, 4);
		} else if (StringUtils.isNotBlank(prevCode) && modify) {
			int asciiNumber = DepartmentUtil.depcodeToNumber(prevCode);
			++asciiNumber;
			startCode = DepartmentUtil.numberToDepcode(asciiNumber, 4);
		}
		if (StringUtils.isBlank(startCode))
			startCode = "AABB";
		int asciiNumber = DepartmentUtil.depcodeToNumber(startCode);
		number = asciiNumber + number - 1;
		String endCode = DepartmentUtil.numberToDepcode(number, 4);
		map.put("startCode", startCode);
		map.put("endCode", endCode);
		CacheUtils.put("startCode", startCode);
		CacheUtils.put("endCode", endCode);
		return map;
	}



	// 保存客户添加的图书馆
	private String save(Customer customer, CutomerLibrary cl) {
		if (cl != null) {
			cl.setCustomer(customer);
			String code = cl.getAssignCode();
			String[] arr = code.split("-");
			cl.setStartCode(arr[0]);
			cl.setEndCode(arr[1]);
			cl.setCreateTime(new Date());
			// 判断是否是修改还是添加客户图书馆
			if (cl.getAttachement().trim().length() != 0) {
				int id = Integer.parseInt(cl.getAttachement());
				cl.setLibraryId(id);
				cutomerLibraryDao.updateOne(cl);
				return cl.getLibraryId() + "," + false;
			} else {
				cutomerLibraryDao.saveOne(cl);
				return cl.getLibraryId() + "," + true;
			}
		} else {
			return 0 + "," + true;
		}

	}

	/**
	 * 
	 * @method:deleteLib 删除对应馆
	 * @Description:deleteLib
	 * @author: HeTao
	 * @date:2016年8月20日 下午5:07:33
	 * @param:@param id
	 * @return:void
	 */
	@Override
	public boolean deleteLib(Integer id) {
		return customerDao.deleteLib(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.flea.modules.customer.service.CustomerService#findByCustomerCode(java
	 * .lang.String)
	 */
	@Override
	public Customer findByCustomerCode(String hallCode) {
		String hqlString = " from Customer where hallCode='" + hallCode + "'";
		return getByHQL(hqlString, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.flea.modules.customer.service.CustomerService#updateHallCodeById(java
	 * .lang.String, java.lang.Integer)
	 */
	@Override
	public boolean updateHallCodeById(String hallCode, Integer id) {
		return customerDao.updateHallCodeById(hallCode, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.flea.modules.customer.service.CustomerService#updateModifyInfoById(
	 * java.lang.Integer)
	 */
	@Override
	public boolean updateModifyInfoById(Integer id) {
		return customerDao.updateModifyInfoById(id);
	}

	/**
	 * 
	 * @method:makeCustomerCode 处理1-3级客户，设置客户代码
	 * @Description: 根据客户分级，设置客户代码
	 */
	@Override
	public String makeCustomerCode(Customer customer) {
		String customerCode = "";
		Integer levelId = customer.getLevelId();
		CustomerLevel customerLevel = customerLevelDao.getOne(levelId);
		Integer level = customerLevel.getLevel();
		if (level == 1) {
			// 一级客户： 10文化部 20教育部 30院校 40全国 50体验
			customerCode = customerLevel.getShortName();
		} else if (level == 2) {
			// 二级客户: 11川文 21川教 31川校 41川 51川体
			// 获取二级客户省代码
			String provinceCode = customer.getProvinceCode();
			Province province = provinceDao.findProvinceByCode(provinceCode);
			String codeCN = province.getCodeCN();
			customerCode = codeCN + customerLevel.getShortName();
		} else if (level == 3) {
			// 三级客户市级 市级客户 ： 12川文A 22川教A 32川校A 42川A 52川体A
			String provinceCode = customer.getProvinceCode();
			Province province = provinceDao.findProvinceByCode(provinceCode);
			String codeCN = province.getCodeCN();
			String cityCodes = customer.getCityCode();
			City city = cityDao.findCityByCode(cityCodes);
			String cityCN = city.getCityCode();
			customerCode = codeCN + customerLevel.getShortName() + cityCN;
		} 
		// LevelId: 1,7,11,17,21 一级客户
		return customerCode;
	}

	/**
	 * 
	 * @method:makeCustomerCode 处理4级客户，自动建馆再设置客户代码
	 * @Description: 处理4级客户，自动建馆再设置客户代码
	 */
	@Override
	public void makeCustomerCodeLevelFour(Customer customer) {
		String customerCode = "";
		Integer levelId = customer.getLevelId();
		// 四级客户区县级 (四位字母)
		// 自动建批次建馆
		Library library= autoCreateLibrary(customer, levelId);
		customerCode = library.getHallCode();

//		else {
//			//需求变更待确定！
//			// 修改时， 取第一个公馆馆号
//			List params = new ArrayList();
//			params.add(customer.getId());
//			BaseDao baseDaos = customerDao;
//			if (customer.getId() > 0) { // 生产环境为944后的客户才处理 修改客户时，历史馆不做处理，生产环境 customerId 小于944的历史数据不做建馆处理
//				//customerDao.getFirstLibraryCodeByCustomer();
//				customerCode = (String) baseDaos.getBySQL(
//						"SELECT hallCode from librarys where customerId = ? and libraryLevel like '%公共图书馆%' ORDER BY id limit 1", params);
//				if (null == customerCode) {
//					// 没有则取其他馆
//					customerCode = (String) baseDaos.getBySQL(
//							"SELECT hallCode from librarys where customerId = ?  ORDER BY id limit 1", params);
//				}
//			} else {
//				customerCode = customer.getHallCode();
//			}
//		}
		customer.setHallCode(customerCode);
	}	
	
	/**
	 * 自动建馆
	 */
	public Library autoCreateLibrary(Customer customer, Integer levelId) {
		/* 
		 * customerLevel 自动建馆对应
		 * id  客户类型           编号                                 可建馆范围
		 * 4           省级图书馆       1A      公共I 公共II 公共IV
		 * 5           市级图书馆       1B      公共II 公共IV
		 * 6           区县级图书馆   13                   公共III 公共IV 农家         社区
		 * 10       中小学馆             23                    中小学I 中小学II   中小学III
		 * 14       一类高校馆        3A      高校I
		 * 15      二类高校馆         3B      高校II
		 * 16      三类高校馆         3C      高校III
		 * 20     社会馆                   4A      社会
		 * 24    体验馆                    5A      体验
		 */
		CutomerLibrary cutomerLibrary = null;
		switch (levelId) {
		case 4:
			cutomerLibrary = autoCreateCustomerLibrary(customer, "公共图书馆I");
			break;
		case 5:
			cutomerLibrary = autoCreateCustomerLibrary(customer, "公共图书馆II");
			break;
		case 6:
			cutomerLibrary = autoCreateCustomerLibrary(customer, "公共图书馆III");
			break;
		case 10:
			cutomerLibrary = autoCreateCustomerLibrary(customer, "中小学馆I");
			break;
		case 14:
			cutomerLibrary = autoCreateCustomerLibrary(customer, "高校馆I");
			break;
		case 15:
			cutomerLibrary = autoCreateCustomerLibrary(customer, "高校馆II");
			break;
		case 16:
			cutomerLibrary = autoCreateCustomerLibrary(customer, "高校馆III");
			break;
		case 20:
			cutomerLibrary = autoCreateCustomerLibrary(customer, "社会馆");
			break;
		case 24:
			cutomerLibrary = autoCreateCustomerLibrary(customer, "体验馆");
			break;
		default:
			break;
		}
		//创建图书馆
		Library library = new Library();
		//设定批次
		library.setCustomerLibraryId(cutomerLibrary.getLibraryId());
		//设置基本资料
		library.setCustomerId(customer.getId());
		library.setName(customer.getName());
		library.setAddress(customer.getAddress());
		library.setAcountName("0");
		library.setAgreementAccount("0");
		library.setEmail("");
		library.setFixphone("");
		library.setPhone(customer.getPhone());
		library.setLibraryLevel(cutomerLibrary.getLibraryLevel());
		//设置经纬度
		library.setLngLat("104.0657573993,30.6573286723");//初始化天府广场
		//设置联系人
		List<CustomerContact> contacts = customer.getContacts();
		library.setContacts(contacts);
		//设置省市区
		Area area = areaDao.findAreaByCode(customer.getAreaCode());
		City city = area.getCity();
		city = cityDao.findCityByCode(city.getCode());
		Province province = city.getProvince();
		library.setArea(area);
		library.setCity(city);
		library.setProvince(province);
		libraryService.autoAddLibraryAndUpdateLibraryCode(library);
		//自动建馆成功，修改图书馆管理员的登录账号信息
		libraryUsersDao.updateLibraryUser(library.getHallCode());
		
		return library;
	}

	/**
	 * 自动建批次
	 */
	public CutomerLibrary autoCreateCustomerLibrary(Customer customer, String libraryLevel) {
		CutomerLibrary cutomerLibrary = new CutomerLibrary();
		cutomerLibrary.setCreateTime(new Date());
		cutomerLibrary.setLibraryLevel(libraryLevel);
		cutomerLibrary.setCustomer(customer);
		//自动设置省市区
		Province province = provinceDao.findProvinceByCode(customer.getProvinceCode());
		City city = cityDao.findCityByCode(customer.getCityCode());
		Area area = areaDao.findAreaByCode(customer.getAreaCode());
		cutomerLibrary.setProvince(province);
		cutomerLibrary.setCity(city);
		cutomerLibrary.setArea(area);
		//设置批次的限制建馆数为1
		cutomerLibrary.setLibraryNumber("1");
//		cutomerLibrary.setUsedCodeNumber(1);
		cutomerLibraryDao.saveOne(cutomerLibrary);
		return cutomerLibrary;
	}
	/**
	 * 获取未占用客户分类列表
	 * 
	 * @return
	 */
	public List<CustomerLevel> getCustomerLevelList() {
		// 客户类型可用列表 剔除已使用的国家级类型
		List<CustomerLevel> list = customerLevelDao.getUnsedCustomerLevelList();
		return list;
	}

	/**
	 * 获取未占用客户分类列表 ,排除客户自己
	 * 
	 * @return
	 */
	public List<CustomerLevel> getCustomerLevelList(int customerID) {
		// 客户类型可用列表 剔除已使用的国家级类型
		List<CustomerLevel> list = customerLevelDao.getUnsedCustomerLevelList(customerID);
		return list;
	}

	public boolean saveOrUpdateCustomer(Customer customer, CutomerLibrary cutomerLibrary) {
		boolean success = false;
		// 设置权限 
		setRole(customer);
		// 设置额度
		setQuota(customer);
		// 操作员
		setModifyUser(customer);
		// 判断客户新增或修改
		if (null == customer.getId()) {
			// 新增
			success = saveCustomer(customer, cutomerLibrary);
		} else {
			// 修改
			success = updateCustomer(customer, cutomerLibrary);
		}
		// 同时更新后台system_user
		updateUserListByCustomer(customer);
		return success;
	}
	
	//新增客户
	public boolean saveCustomer(Customer customer, CutomerLibrary cutomerLibrary) {
		boolean success = false;
		int levelId = customer.getLevelId();
		CustomerLevel customerLevel = customerLevelDao.getOne(levelId);
		int levels = customerLevel.getLevel();
		if (levels == 1 && customerLevel.getIsUsed() == 1) {
			//一级已被占用客户等级 此空处理， 防止页面注入客户levelId
			return false;
		} else if (levels == 1) {
			// 一级用户 事务处理(未做)
			String customerCode = this.makeCustomerCode(customer);
			customer.setHallCode(customerCode);
			this.saveOne(customer);
			// 1级客户保存后。需占用system_customer_level
			customerLevel.setIsUsed(1);
			customerLevel.setCustomerID(customer.getId());
			customerLevelDao.updateOne(customerLevel);
			success = true;
		} else if(levels == 2 || levels == 3) {
			// 2，3级客户
			String customerCode = this.makeCustomerCode(customer);
			boolean isExist = customerDao.isExistCustomer(customerCode);
			if (isExist) return false;			
			customer.setHallCode(customerCode);
			this.saveOne(customer);
			//2,3级 分级加入省市区记录
			customerTypeCodeService.saveOne(customer);
			success = true;
		} else { 
			//4级客户需要自动建馆
			//4级客户先建立，然后建馆批次表，自动建馆。再更新馆号
			this.saveOne(customer);
			this.makeCustomerCodeLevelFour(customer);
			this.updateOne(customer);
			success = true;
		}
		return success;
	}

	//更新客户
	public boolean updateCustomer(Customer customer, CutomerLibrary cutomerLibrary) {
		boolean success = false;
		int levelId = customer.getLevelId();
		CustomerLevel customerLevel = customerLevelDao.getOne(levelId);
		int levels = customerLevel.getLevel();
		// 更新客户对应的客户代码 
		if(levels < 4) {
			//处理1-3级
			String customerCode = this.makeCustomerCode(customer);
			customer.setHallCode(customerCode);
		}
		this.updateOne(customer);
		// 修改为非一级用户释放占用system_customer_level
		if (levels != 1) {
			List params = new ArrayList();
			params.add(customer.getId());
			customerLevelDao.querySql("UPDATE system_customer_level set isUsed = 0, customerID = null WHERE customerID = ? ", params);
		} else {
			// 修改为一级时，占用system_customer_level
			List params = new ArrayList();
			params.add(customer.getId());
			params.add(customer.getLevelId());
			customerLevelDao.querySql("UPDATE system_customer_level set isUsed = 1, customerID = ?  WHERE id = ? ",
					params);
		}
		//更客户地区关联关系表
		customerTypeCodeService.updateOne(customer);
		return success;
	}
	
	/**
	 * 根据客户更新后台用户账号
	 * @param customer
	 */
	@Override
	public void updateUserListByCustomer(Customer customer) {
		if (null != customer.getId()) {
			List<User> userList = userDao.findByCustomerId(customer.getId());
			for (User userOne : userList) {
				userDao.updateHallCodeById(customer.getHallCode(), userOne.getId());
			}
		}
	}
	// 设置权限 
	public void setRole(Customer customer) {
		String roleId = Config.getProperty("cRoleId");
		customer.getUsers().get(0).setRoleIds(roleId);		
	}
	// 设置额度 
	public void setQuota(Customer customer) {
		double minQuota = 0, maxQuota = 1000;
		minQuota = Double.parseDouble(customer.getQuota().split("-")[0]);
		maxQuota =  Double.parseDouble(customer.getQuota().split("-")[1]);
		customer.setMinQuota(minQuota);
		customer.setMaxQuota(maxQuota);
	}
	//设置操作员
	public void setModifyUser(Customer customer) {
		User user = ShiroUtils.getCurrentUser();
		customer.setModifyUser(user.getUserName());
		customer.setModifyDate(new Date());		
	}
	/**
	 * 客户可建馆区间
	 * @param customer
	 */
	public List<String> getVisibleLibraryRange(Customer customer) {
		Integer range = 0;
		Integer levelId = customer.getLevelId();
		CustomerLevel customerLevel =  customerLevelDao.getOne(levelId);
		String code = customerLevel.getCode();
		List<String> visibleLibraryList = new ArrayList<String>();
		if(code.equals("1A")){ //省级图书馆
			visibleLibraryList.add("公共图书馆I");
			visibleLibraryList.add("公共图书馆II");
			visibleLibraryList.add("公共图书馆IV");
		} else if(code.equals("1B")){//市级图书馆
			visibleLibraryList.add("公共图书馆II");
			visibleLibraryList.add("公共图书馆IV");
		} else if(code.equals("13")){//区县级
			visibleLibraryList.add("公共图书馆III");
			visibleLibraryList.add("公共图书馆IV");
			visibleLibraryList.add("社区书屋");
			visibleLibraryList.add("农家书屋");
		} else if(code.equals("23")){
			visibleLibraryList.add("中小学馆I");
			visibleLibraryList.add("中小学馆II");
			visibleLibraryList.add("中小学馆III");			
		} else if(code.equals("3A")){
			visibleLibraryList.add("高校馆I");	
		} else if(code.equals("3B")){
			visibleLibraryList.add("高校馆II");	
		} else if(code.equals("3C")){
			visibleLibraryList.add("高校馆III");	
		}else if(code.equals("4A")){
			visibleLibraryList.add("社会馆");
		}else if(code.equals("5A")){
			visibleLibraryList.add("体验馆");
		}
		return visibleLibraryList;
	}
	

}