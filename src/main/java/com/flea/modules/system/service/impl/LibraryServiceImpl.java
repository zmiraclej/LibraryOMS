package com.flea.modules.system.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;

import org.hibernate.criterion.DetachedCriteria;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flea.common.Common;
import com.flea.common.Constants;
import com.flea.common.SolrContent;
import com.flea.common.dao.AreaDao;
import com.flea.common.dao.CityDao;
import com.flea.common.dao.UserDao;
import com.flea.common.pojo.Area;
import com.flea.common.pojo.City;
import com.flea.common.pojo.Page;
import com.flea.common.pojo.Province;
import com.flea.common.pojo.User;
import com.flea.common.service.impl.BaseServiceImpl;
import com.flea.common.util.EncryptUtils;
import com.flea.common.util.PasswordHelper;
import com.flea.common.util.ShiroUtils;
import com.flea.modules.customer.dao.CutomerLibraryDao;
import com.flea.modules.customer.dao.LibraryCodeDao;
import com.flea.modules.customer.pojo.Customer;
import com.flea.modules.customer.pojo.CustomerContact;
import com.flea.modules.customer.pojo.CutomerLibrary;
import com.flea.modules.customer.pojo.LibraryCode;
import com.flea.modules.report.dao.LightReportDao;
import com.flea.modules.system.dao.LibraryDao;
import com.flea.modules.system.dao.LibraryImportErrorDataDao;
import com.flea.modules.system.dao.LibraryStopDao;
import com.flea.modules.system.dao.LibraryUsersDao;
import com.flea.modules.system.dao.LibrarysUpdateStatusDao;
import com.flea.modules.system.dao.WarehouseDao;
import com.flea.modules.system.pojo.Library;
import com.flea.modules.system.pojo.LibraryBook;
import com.flea.modules.system.pojo.LibraryCirculateRel;
import com.flea.modules.system.pojo.LibraryImportErrorData;
import com.flea.modules.system.pojo.LibrarysUpdateStatus;
import com.flea.modules.system.pojo.vo.LibraryCity;
import com.flea.modules.system.pojo.vo.LibraryIndex;
import com.flea.modules.system.pojo.vo.Librarys_Books;
import com.flea.modules.system.service.LibraryService;
import com.flea.modules.system.util.DepartmentUtil;

@Service
public class LibraryServiceImpl extends BaseServiceImpl<Library, Integer> implements LibraryService {

	@Resource
	private LibraryDao libraryDao;
	@Resource
	private CutomerLibraryDao cutomerLibraryDao;

	@Resource
	private LibraryUsersDao libraryUsersDao;

	@Resource
	private UserDao userDao;

	@Resource
	private AreaDao areaDao;

	@Resource
	private CityDao cityDao;

	@Resource
	private WarehouseDao warehouseService;

	@Resource
	private LibraryImportErrorDataDao errorDataDao;

	@Resource
	private LibraryCodeDao libraryCodeDao;

	@Resource
	private LibrarysUpdateStatusDao librarysUpdateStatusDao;

	@Autowired
	private LibraryStopDao libraryStopDao;

	@Autowired
	public LibraryServiceImpl(LibraryDao libraryDao) {
		super(libraryDao);
		this.libraryDao = libraryDao;
	}

	/**
	 * @return excel数据导入数据库并返回错误信息
	 */
	@Override
	public Map<String, String> saveErroeExcel(LibraryImportErrorData errorData) {
		errorDataDao.saveOne(errorData);
		return null;

	}

	@Override
	public void saveOne(Library library) {
		//
		User user = ShiroUtils.getCurrentUser();
		library.setCreateTime(new Date());
		library.setModifyDate(new Date());
		library.setModifyUser(user.getUserName());
		Integer customerLibraryId = library.getCustomerLibraryId();
		CutomerLibrary customerLibrary = cutomerLibraryDao.getOne(customerLibraryId);
		Customer customer = customerLibrary.getCustomer();
		library.setCustomerId(customer.getId());
		//设置省市区
		Area area = library.getArea();
		area = areaDao.findAreaByCode(area.getCode());
		City city = area.getCity();
		city = cityDao.findCityByCode(city.getCode());
		Province province = city.getProvince();
		library.setCity(city);
		library.setProvince(province);
		library.setAreaAddress(province.getName() + "-" + city.getName() + "-" + area.getName());
		//设置联系人
		List<CustomerContact> contacts = library.getContacts();
		List<CustomerContact> contactsList = new ArrayList<CustomerContact>();
		String phone = "";
		for (CustomerContact contact : contacts) {
			contact.setLibrary(library);
			contactsList.add(contact);
		}
		library.setContacts(contactsList);
		CustomerContact contact = contacts.get(0);
		if (StringUtils.isNotBlank(contact.getPhone())) {
			phone = contact.getPhone();
		} else if (StringUtils.isNotBlank(contact.getTel()) && StringUtils.isBlank(phone)) {
			phone = contact.getTel();
		}
		library.setConperson(contact.getContactPerson());
		library.setPhone(phone);
		// 未审核状态
		library.setLibraryStatus(1);
//		library.setIsEffective("2");
		libraryDao.saveOne(library);
		cutomerLibraryDao.updateUsedCodeById(customerLibraryId);
	}

	@Override
	public void updateLibrary(Library newLibrary, boolean sendpwd) {
		newLibrary.setCreateTime(new Date());
		Library library = libraryDao.getOne(newLibrary.getId());
		
		// 当前判断更新的内容是否包含
		// 1.联系人，座机电话，手机号，邮箱可修改，提交后不用审核
		// 2.其余内容可修改，提交后需审核
		if (library.getLibraryStatus() == 2 || library.getLibraryStatus() == 12) {
			if (!newLibrary.getName().equals(library.getName()) || !newLibrary.getAddress().equals(library.getAddress())
					|| !newLibrary.getAgreementAccount().equals(library.getAgreementAccount())
					|| !newLibrary.getAcountName().equals(library.getAcountName())
					|| newLibrary.getCreditLines() != library.getCreditLines()
							&& newLibrary.getContacts().get(0).getPhone().equals(library.getPhone())
							&& newLibrary.getContacts().get(0).getChat().equals(library.getEmail())) {
				LibrarysUpdateStatus librarysUpdateStatus = new LibrarysUpdateStatus();
				List<CustomerContact> contacts = newLibrary.getContacts();
				List<CustomerContact> contactsList = new ArrayList<CustomerContact>();
				for (CustomerContact contact : contacts) {
					contact.setLibrary(library);
					contactsList.add(contact);
				}
				CustomerContact contact = contacts.get(0);
				librarysUpdateStatus.setLibraryId(newLibrary.getId());
				librarysUpdateStatus.setName(library.getName()); // 馆名
				librarysUpdateStatus.setAddress(library.getAddress()); // 地址
				librarysUpdateStatus.setLngLat(library.getLngLat()); // 经纬度
				librarysUpdateStatus.setConperson(library.getConperson());// 联系人
				librarysUpdateStatus.setPhone(library.getPhone());// 手机号码
				librarysUpdateStatus.setFixphone(contact.getTel());// 固定电话
				librarysUpdateStatus.setEmail(contact.getChat());// 邮箱
				librarysUpdateStatus.setAgreementAccount(library.getAgreementAccount()); // 协议账号
				librarysUpdateStatus.setAcountName(library.getAcountName()); // 账户名
				librarysUpdateStatus.setCreditLines(library.getCreditLines()); // 授信额度
				//保存librarysUpdateStatus表中
				librarysUpdateStatusDao.saveOne(librarysUpdateStatus);
				library.setLibraryUpdateStatus(4);
			}
		} 
			//平台给用户驳回的状态为3的时候，用户可以编辑，编辑完提交之后为待审状态为1
			if(library.getLibraryStatus() == 3){
				library.setLibraryStatus(1);
			}
			List<CustomerContact> contacts = newLibrary.getContacts();
			List<CustomerContact> contactsList = new ArrayList<CustomerContact>();
			for (CustomerContact contact : contacts) {
				contact.setLibrary(library);
				contactsList.add(contact);
			}
			User user = ShiroUtils.getCurrentUser();
			library.setModifyDate(new Date());
			library.setModifyUser(user.getUserName());
//			library.setIsEffective(newLibrary.getIsEffective());
			library.setName(newLibrary.getName()); // 馆名
			library.setAddress(newLibrary.getAddress()); // 地址
			library.setLngLat(newLibrary.getLngLat()); // 经纬度
			library.setAgreementAccount(newLibrary.getAgreementAccount()); // 协议账号
			library.setAcountName(newLibrary.getAcountName()); // 账户名
			library.setCreditLines(newLibrary.getCreditLines()); // 授信额度
			library.setContacts(contactsList);
			String phone = "";
			CustomerContact contact = contacts.get(0);
			library.setConperson(contact.getContactPerson());
			if (StringUtils.isNotBlank(contact.getPhone())) {
				phone = contact.getPhone();
			} else if (StringUtils.isNotBlank(contact.getTel()) && StringUtils.isBlank(phone)) {
				phone = contact.getTel();
			}
			library.setPhone(phone);
			org.apache.shiro.session.Session currentSession = SecurityUtils.getSubject().getSession();
			Object userId = libraryDao.findLibUserByHallCodeAndUserName(library.getHallCode(), "admin");
			if (userId == null) {
				libraryDao.saveLibraryUser(library.getHallCode());
				
			}
			if (sendpwd || "2".equals(library.getIsEffective())) {
				String userPwd;
				if (library.getIsEffective().equals(Constants.FLAG_ENABLE)) {
					userPwd = PasswordHelper.getStringRandom(6);// 随机生成密码
				} else {
					userPwd = DepartmentUtil.reverseCode(library.getHallCode());// 反转
				}
				currentSession.setAttribute(library.getHallCode() + "userPassword", userPwd);
				userPwd = EncryptUtils.encryptMD5(userPwd);
				libraryDao.updateLibraryUserPassword(library.getHallCode(), userPwd);
			}
			// 保存文本数据库
			saveSolrIndex(library);
			libraryDao.updateOne(library);
		}

	@Override
	public Library checkName(String userName) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(" from Library as library ");
		List<Object> values = new ArrayList<Object>();
		if (null != userName) {
			buffer.append(" where userName = ?");
			values.add(userName);
		}
		Library library = libraryDao.getByHQL(buffer.toString(), values);
		return library;
	}

	@Override
	public String getMaxHallCode() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(" select max(a.hallCode) from Library as a ");
		Object obj = libraryDao.getMaxBySql(buffer.toString());
		return obj.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.flea.common.service.UserService#findLibrarysGroupByCity()
	 */
	@Override
	public Map<String, List<LibraryCity>> findLibraryWithGroupByCity() {
		return libraryDao.getLibrarysWithGroupByCity();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.flea.modules.system.service.LibraryService#findPagingList(com.flea.
	 * common.pojo.Page, java.lang.String)
	 */
	@Override
	public Page<Library> findPagingList(Page<Library> page, String searchStr) {
		DetachedCriteria dc = libraryDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(searchStr)) {
			dc.add(Restrictions.eq("name", searchStr));
		}
		User user = ShiroUtils.getCurrentUser();
		String hallCode = user.getHallCode();
		if (!hallCode.toUpperCase().equals(Common.TZPT_OCCUPY_DEPCODE)) {
			dc.add(Restrictions.eq("hallCode", hallCode));
		}
		// dc.add(Restrictions.eq("isEffective", Common.FLAG_ACTIVATION));
		dc.addOrder(Order.desc("createTime"));
		return libraryDao.find(page, dc);

	}

	@Override
	public boolean sotp(Integer id) {
		libraryDao.changeStatusToSotpOrUndelete("isEffective", Common.FLAG_DISABLE, "id", id);
		removeSolrIndex(id);
		return true;
	}

	@Override
	public boolean start(Integer id) {
		// libraryDao.changeStatusToSotpOrUndelete("isEffective",
		// Common.FLAG_ENABLE, "id", id);
		Library dept = libraryDao.getOne(id);
		// saveSolrIndex(dept);
		return true;
	}

	@Override
	/**
	 * 
	 * @Description:审核通过或者不通过
	 * @param id
	 * @param status
	 * @return boolean 返回类型
	 * @throws @author
	 *             QL
	 * @date 2017年3月26日 下午12:51:40
	 */
	public boolean audit(Integer id, Integer libraryStatus) {
		Library library = dao.getOne(id);
		User user = ShiroUtils.getCurrentUser();
		// 平台对用户审核的各种状态,并且返回给用户的信息状态不同
		// 当用户状态是4为修改待审时，
		if (libraryStatus == 4) {
			//设置修改驳回为5,并且保存到librarysUpdateStatus表中
			library.setLibraryUpdateStatus(5);
			return librarysUpdateStatusDao.updateLibrarys(id);
		}
		// 当用户状态是6为停用待审
		if (libraryStatus == 6) {
			//删除librarysStop表中的用户停用信息
			libraryStopDao.deleteByStopId(id);
			//更新librarys表中的用户信息状态为7，停用驳回状态
			return libraryStopDao.updateLibraryStopReject(id);
		}

		// 当用户状态为13是通过并且操作状态为6是停用待审
		if (libraryStatus == 13 && library.getLibraryUpdateStatus() == 6) {
			//更新用户信息状态8为停用
			return libraryStopDao.updateLibraryStop(id);
		}

		// 当用户操作状态为9是启用待审
		if (libraryStatus == 9) {
			//用户信息更新状态为10是启用驳回
			return libraryStopDao.updateLibraryStartReject(id);
		}

		// 通过
		if (libraryStatus == 2) {
			//如果当前用户信息状态为2是通过时，操作状态为启用待审时
			if (library.getLibraryUpdateStatus() == 9) {
				//删除librarysStop表中的用户停用信息
				libraryStopDao.deleteByStopId(id);
			}
			//用户信息操作状态为11是无操作状态
			library.setLibraryUpdateStatus(11);
//			 图书馆为1的时候是有效的
//			library.setIsEffective("1");
		}
		library.setModifyDate(new Date());
		library.setLibraryStatus(libraryStatus);
		library.setModifyUser(user.getUserName());
		dao.updateOne(library);
		// 更新用户图书馆的状态，条件是通过library获取图书馆名字
		libraryUsersDao.updateLibraryUser(library.getHallCode());
		// 图书馆审核状态为2代表通过
		if (libraryStatus == 2) {
			// 保存文本数据库
			saveSolrIndex(library);
		}
		return true;
	}

	/**
	 * 
	 * @Description:审核，驳回理由添加
	 * @param id
	 * @param status
	 * @param rejectReason
	 * @return boolean 返回类型
	 * @throws @author
	 *             QL
	 * @date 2017年3月26日 下午12:52:14
	 */
	@Override
	public boolean audit(Integer id, Integer libraryStatus, String rejectReason) {
		Library library = dao.getOne(id);
		User user = ShiroUtils.getCurrentUser();
		// 平台对用户审核的各种状态,并且返回给用户的信息状态不同
		// 当用户状态是4为修改待审时，
		if (libraryStatus == 4) {
			// 设置修改驳回为5,并且保存到librarysUpdateStatus表中
			library.setLibraryUpdateStatus(5);
			return librarysUpdateStatusDao.updateLibrarys(id);
		}
		// 当用户状态是6为停用待审
		if (libraryStatus == 6) {
			// 删除librarysStop表中的用户停用信息
			libraryStopDao.deleteByStopId(id);
			// 更新librarys表中的用户信息状态为7，停用驳回状态
			return libraryStopDao.updateLibraryStopReject(id);
		}

		// 当用户状态为13是通过并且操作状态为6是停用待审
		if (libraryStatus == 13 && library.getLibraryUpdateStatus() == 6) {
			// 更新用户信息状态8为停用
			return libraryStopDao.updateLibraryStop(id);
		}

		// 当用户操作状态为9是启用待审
		if (libraryStatus == 9) {
			// 用户信息更新状态为10是启用驳回
			return libraryStopDao.updateLibraryStartReject(id);
		}

		library.setLibraryStatus(libraryStatus);
		library.setModifyDate(new Date());
		library.setModifyUser(user.getUserName());
		// library.setRejectReason(rejectReason == "" ? null:rejectReason);
		dao.updateOne(library);
		if (library.getLibraryStatus() == 2) {
			saveSolrIndex(library);
		}
		if (library.getLibraryStatus() == 4) {
			removeSolrIndex(id);
		}
		return true;
	}

	@Resource
	private LightReportDao lightDao;

	@Resource(name = "deptSolrContent")
	protected SolrContent deptSolrContent;

	public void removeSolrIndex(Integer id) {
		SolrClient client = deptSolrContent.createClient();
		try {
			logger.info("removeSolrIndex>" + id);
			client.deleteById(String.valueOf(id));
			client.commit();
		} catch (SolrServerException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		} finally {
			deptSolrContent.colse(client);
		}
	}

	/**
	 * 添加solr 索引
	 */
	public void saveSolrIndex(Library dept) {

		Map<String, String> map = lightDao.findLightByCode(dept.getHallCode());

		SolrClient client = deptSolrContent.createClient();
		LibraryIndex index = new LibraryIndex();
		index.setLibId(dept.getId());
		index.setCreateTime(dept.getCreateTime());
		index.setHallCode(dept.getHallCode());
		index.setLngLat(dept.getLngLat());
		if (!map.isEmpty()) {
			index.setLightTime(map.get("light"));
			index.setPhone(map.get("phone"));
		}
		String lngLat = dept.getLngLat();
		String lng = "", lat = "";
		if (StringUtils.isNotBlank(lngLat)) {
			lng = lngLat.split(",")[0];
			lat = lngLat.split(",")[1];
			index.setLatlng(lat + "," + lng);
		}
		index.setName(dept.getName());
		index.setConperson(dept.getConperson());
		index.setPhone(dept.getPhone());
		index.setLevel(dept.getLibraryLevel());
		index.setCityCode(dept.getCity().getCode());
		index.setAreaCode(dept.getArea().getCode());
		index.setProvinceCode(dept.getProvince().getCode());
		index.setAddress(dept.getAddress());

		try {
			client.addBean(index);
			client.commit();
		} catch (IOException e) {
			logger.error(e);
		} catch (SolrServerException e) {
			logger.error(e);
		} finally {
			deptSolrContent.colse(client);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.flea.modules.system.service.LibraryService#find(com.flea.common.pojo.
	 * Page, java.lang.String, com.flea.modules.system.pojo.Library)
	 */
	@Override
	public Page<Library> find(Page<Library> page, String search, Library library) {
		DetachedCriteria dc = libraryDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(search)) {
			dc.add(Restrictions.or(Restrictions.like("hallCode", "%" + search + "%"),
					Restrictions.like("name", "%" + search + "%")));
		}
		// 添加馆际流通的权限控制
		User user = ShiroUtils.getCurrentUser();
		if (!Common.TZPT_OCCUPY_DEPCODE.equals(user.getHallCode())) {
			dc.add(Restrictions.eq("customerId", user.getCustomer().getId()));
		}
		dc.add(Restrictions.ne("isEffective", Common.FLAG_DISABLE));
		String hallCode = user.getHallCode();
		if (!hallCode.toUpperCase().equals(Common.TZPT_OCCUPY_DEPCODE)) {
			Customer customer = user.getCustomer();
			dc.add(Restrictions.eq("customerId", customer.getId()));
		}
		return libraryDao.find(page, dc);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.flea.modules.system.service.LibraryService#checkDeptCode(java.lang.
	 * String)
	 */
	@Override
	public boolean checkDeptCode(String deptCode) {
		Library dept = libraryDao.findByCode(deptCode);
		if (dept == null) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.flea.modules.system.service.LibraryService#updateBorrowModelById(java
	 * .lang.String, java.lang.String[])
	 */
	@Override
	public void updateBorrowModelById(Integer maxSum, Integer freeRentDays, String rent, String[] libIds) {
		for (String libId : libIds) {
			libraryDao.updateBorrowModelById(maxSum, freeRentDays, rent, libId);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.flea.modules.system.service.LibraryService#updateDepositModelById(
	 * java.lang.String, java.lang.String[])
	 */
	@Override
	public void updateDepositModelById(String reader, String deposit, String[] libIds) {
		for (String libId : libIds) {
			libraryDao.updateDepositModelById(reader, deposit, libId);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.flea.modules.system.service.LibraryService#updateCirculateModelById(
	 * java.lang.String, java.lang.String[])
	 */
	@Override
	public void updateCirculateModelById(String Operation, String[] libIds) {
		for (String libId : libIds) {
			libraryDao.updateCirculateById(Operation, libId);
		}
	}

	/**
	 * 更新用户所选中的图书馆范围
	 */
	@Override
	public boolean updateLibraryScope(String allId, int jieshu) {
		return libraryDao.updateLibraryScope(allId, jieshu);
	}

	/**
	 * 返回最大的范围数
	 */
	@Override
	public int getMaxScope() {
		return libraryDao.getMaxScope();
	}

	/**
	 * 
	 * @method:find
	 * @Description:find 馆际流通审核列表
	 * @author: HeTao
	 * @date:2016年6月20日 上午11:06:27
	 * @param:@param page
	 * @param:@param cil
	 * @param:@param status
	 * @param:@return
	 * @return:Page<LibraryCirculate>
	 */
	@Override
	public Page<LibraryCirculateRel> find(Page<LibraryCirculateRel> page, String searchText) {
		Map<String, Object> map = libraryDao.findLibarary(page, searchText);
		List<LibraryCirculateRel> lis = (List<LibraryCirculateRel>) map.get("list");
		List<Librarys_Books> lbs = (List<Librarys_Books>) map.get("val");
		for (int i = 0; i < lis.size(); i++) {
			LibraryCirculateRel lb = lis.get(i);
			Librarys_Books lbooks = lbs.get(i);
			lb.setOutHallCode(lbooks.getOutLib());
			lb.setInHallCode(lbooks.getInLib());
			String name = lbooks.getUserName();
			lb.setAuditPerson(name.substring(1, name.length() - 1));
		}
		page.setList(lis);

		return page;
	}

	/**
	 * 
	 * @method:reviewLibraryInfo
	 * @Description:reviewLibraryInfo 馆际流通审核详情
	 * @author: HeTao
	 * @date:2016年6月22日 下午3:00:09
	 * @param:@param id
	 * @param:@return
	 * @return:List<LibraryBook>
	 */
	@Override
	public List<LibraryBook> reviewLibraryInfo(Integer id) {
		return libraryDao.reviewLibraryInfo(id);
	}

	/**
	 * 
	 * @method:circulateInfo
	 * @Description:circulateInfo 借书详情
	 * @author: HeTao
	 * @date:2016年6月23日 上午10:49:50
	 * @param:@param id
	 * @param:@return
	 * @return:LibraryCirculateRel
	 */
	@Override
	public Map<String, Object> queryCirculate(Integer id) {
		Map<String, Object> map = null;
		map = new HashMap<String, Object>();
		Map<String, Object> maps = libraryDao.queryCirculate(id);
		LibraryCirculateRel lb = (LibraryCirculateRel) maps.get("lb");
		Librarys_Books book = (Librarys_Books) maps.get("book");
		com.flea.modules.system.pojo.vo.User user1 = libraryDao.getUserInfo(lb.getInHallCode());
		com.flea.modules.system.pojo.vo.User user2 = libraryDao.getUserInfo(lb.getOutHallCode());
		map.put("one", lb);
		map.put("two", user1);
		map.put("three", user2);
		map.put("book", book);
		return map;
	}

	/**
	 * 
	 * @method:updateCirculate
	 * @Description:updateCirculate 改变审核订单状态
	 * @author: HeTao
	 * @date:2016年6月23日 下午2:53:45
	 * @param:@param state
	 * @param:@param id
	 * @param:@return
	 * @return:boolean
	 */
	@Override
	public boolean updateCirculate(Integer state, Integer id) {
		return libraryDao.updateCirculate(state, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.flea.modules.system.service.LibraryService#saveLibraryUser(java.lang.
	 * String)
	 */
	@Override
	public Boolean saveLibraryUser(String hallCode) {
		return libraryDao.saveLibraryUser(hallCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.flea.modules.system.service.LibraryService#
	 * findLibUserByHallCodeAndUserName(java.lang.String, java.lang.String)
	 */
	@Override
	public Object findLibUserByHallCodeAndUserName(String hallCode, String userName) {
		return libraryDao.findLibUserByHallCodeAndUserName(hallCode, userName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.flea.modules.system.service.LibraryService#updateLibraryUserPassword(
	 * java.lang.String)
	 */
	@Override
	public boolean updateLibraryUserPassword(String hallCode, String isEffective) {
		org.apache.shiro.session.Session currentSession = SecurityUtils.getSubject().getSession();
		String userPwd;
		if (isEffective.equals(Constants.FLAG_ENABLE)) {
			userPwd = PasswordHelper.getStringRandom(6);// 随机生成密码
		} else {
			userPwd = DepartmentUtil.reverseCode(hallCode);
		}
		currentSession.setAttribute(hallCode + "userPassword", userPwd);
		userPwd = EncryptUtils.encryptMD5(userPwd);
		return libraryDao.updateLibraryUserPassword(hallCode, userPwd);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.flea.modules.system.service.LibraryService#findCodesByCustomerId(java
	 * .lang.Integer)
	 */
	@Override
	public List<Map<String, String>> findLibsByCustomerId(Integer customerId) {
		String sqlString = " FROM Library where  isEffective=1 and customerId=" + customerId;
		List<Library> list = libraryDao.getListByHQL(sqlString, null);
		List<Map<String, String>> libs = new ArrayList<Map<String, String>>();
		for (Library lib : list) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("code", lib.getHallCode());
			map.put("name", lib.getName());
			libs.add(map);
		}
		return libs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.flea.modules.system.service.LibraryService#findLevelByCustomerId(java
	 * .lang.Integer)
	 */
	@Override
	public List<String> findLevelByCustomerId(Integer customerId) {
		return libraryDao.findLevelByCustomerId(customerId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.flea.modules.system.service.LibraryService#findByHallCode(java.lang.
	 * String)
	 */
	@Override
	public Library findByHallCode(String hallCode) {
		String hql = "from Library where hallCode =?";
		List<Object> values = new ArrayList<Object>();
		values.add(hallCode);
		return libraryDao.getByHQL(hql, values);
	}

	@Override
	public Long findLibraryByName(String name) {
		// TODO Auto-generated method stub
		String hql = "select count(id) from Library where name = ? ";
		List<Object> values = new ArrayList<Object>();
		values.add(name);
		return libraryDao.countByHql(hql, values);

	}

	@Override
	public int getLibrarysCount() {
		int librarysCount = libraryDao.getLibrarysCount();
		return librarysCount;
	}

	@Override
	public void addLibraryAndUpdateLibraryCode(Library library) {

		LibraryCode libraryCode = this.allotLibraryCode(library);
		this.saveOne(library);
		// 占用该馆号
		libraryCode.setCustomerId(library.getCustomerId());
		libraryCodeDao.updateOne(libraryCode);
	}
	@Override
	public void autoAddLibraryAndUpdateLibraryCode(Library library) {

		LibraryCode libraryCode = this.allotLibraryCode(library);
		//自动建馆不用审核直接通过
		this.saveLibrary(library);
		// 占用该馆号
		libraryCode.setCustomerId(library.getCustomerId());
		libraryCodeDao.updateOne(libraryCode);
		//保存到文本数据库
		saveSolrIndex(library);
	}
	
	public void saveLibrary(Library library) {
		//
		User user = ShiroUtils.getCurrentUser();
		library.setCreateTime(new Date());
		library.setModifyDate(new Date());
		library.setModifyUser(user.getUserName());
		Integer customerLibraryId = library.getCustomerLibraryId();
		CutomerLibrary customerLibrary = cutomerLibraryDao.getOne(customerLibraryId);
		Customer customer = customerLibrary.getCustomer();
		library.setCustomerId(customer.getId());
		//设置省市区
		Area area = library.getArea();
		area = areaDao.findAreaByCode(area.getCode());
		City city = area.getCity();
		city = cityDao.findCityByCode(city.getCode());
		Province province = city.getProvince();
		library.setCity(city);
		library.setProvince(province);
		library.setAreaAddress(province.getName() + "-" + city.getName() + "-" + area.getName());
		//设置联系人
		List<CustomerContact> contacts = library.getContacts();
		List<CustomerContact> contactsList = new ArrayList<CustomerContact>();
		String phone = "";
		for (CustomerContact contact : contacts) {
			contact.setLibrary(library);
			contactsList.add(contact);
		}
		library.setContacts(contactsList);
		CustomerContact contact = contacts.get(0);
		if (StringUtils.isNotBlank(contact.getPhone())) {
			phone = contact.getPhone();
		} else if (StringUtils.isNotBlank(contact.getTel()) && StringUtils.isBlank(phone)) {
			phone = contact.getTel();
		}
		library.setConperson(contact.getContactPerson());
		library.setPhone(phone);
		// 未审核状态
		library.setLibraryStatus(2);
//		library.setIsEffective("1");
		libraryDao.saveOne(library);
		cutomerLibraryDao.updateUsedCodeById(customerLibraryId);
	}	
	
	
	// 分配馆号
	@Override
	public LibraryCode allotLibraryCode(Library library) {

		String libraryLevel = library.getLibraryLevel();// 图书馆类型
		Integer customerLibraryId = library.getCustomerLibraryId(); // 批次号
		Integer CustomerId = library.getCustomerId();
		
		// 1 根据图书馆类型分配馆号
		Integer level = getLibraryCodeLevel(library);
		LibraryCode libraryCode = null;
		synchronized (this) {
			while (null == libraryCode) {
				libraryCode = libraryCodeDao.getUnUsedLibraryCodeByLevel(level);
				level += 1;// 馆号分配完时，取下一等级，直至取到
			}
			// 占用该馆号
			Date date = new Date();
			libraryCode.setUseDate(date);
			libraryCode.setSclId(customerLibraryId);
			libraryCode.setStatus(1);
			// 更新批次表 设置可建馆数量
			// 分配给图书馆
			library.setHallCode(libraryCode.getLibraryCode());
			return libraryCode;
		}
	}
	//根据图书馆类型分配馆号
	public Integer getLibraryCodeLevel(Library library) {
		Integer level = 5;
		String libraryLevel = library.getLibraryLevel();
		// 一级：AAAA 公共图书馆I
		if (libraryLevel.equals("公共图书馆I")) {
			level = 1;
		}
		// 二级：AAAB\BAAA 公共图书馆II \高校馆I
		if (libraryLevel.equals("公共图书馆II") || libraryLevel.equals("高校馆I")) {
			level = 2;
		}
		// 三级 AABB\ABAB\BAAB\AABA\ABAA 公共图书馆III\高校馆II\中小学馆I
		if (libraryLevel.equals("公共图书馆III") || libraryLevel.equals("高校馆II") || libraryLevel.equals("中小学馆I")) {
			level = 3;
		}
		// 四级：AABC\BAAC\BCAA\ABCA\ABAC\CABA \ABCD 高校馆III\中小学馆II\公共馆II
		if (libraryLevel.equals("高校馆III") || libraryLevel.equals("中小学馆II") || libraryLevel.equals("公共馆IV")) {
			level = 4;
		}
		// 五级：其余 中小学馆III、农家书屋、社区书屋、社会馆,体验馆
		if (libraryLevel.equals("中小学馆III") || libraryLevel.equals("农家书屋") || libraryLevel.equals("社区书屋")
				|| libraryLevel.equals("社会馆") || libraryLevel.equals("体验馆")) {
			level = 5;
		}
		return level;
	}
}
