package com.flea.common.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flea.common.Common;
import com.flea.common.Constants;
import com.flea.common.dao.MenuDao;
import com.flea.common.dao.RoleDao;
import com.flea.common.dao.UserDao;
import com.flea.common.pojo.Page;
import com.flea.common.pojo.Role;
import com.flea.common.pojo.User;
import com.flea.common.service.UserService;
import com.flea.common.util.EncryptUtils;
import com.flea.common.util.ShiroUtils;
import com.flea.modules.report.pojo.ReportTotal;
import com.flea.modules.report.pojo.SearchResult;
import com.flea.modules.system.util.DepartmentUtil;
@Service
public class UserServiceImpl extends BaseServiceImpl<User, Integer> implements UserService{
	
	private UserDao userDao;
	
	@Resource
	private RoleDao roleDao;

	
	@Autowired
	public UserServiceImpl(UserDao userDao) {
		super(userDao);
		this.userDao = userDao;
	}


	@Resource
	private MenuDao menuDao;
	@Override
	public void insertUser(User newUser){
		setUserRoles(newUser);
		if (StringUtils.isNotBlank(newUser.getPassword())) {
			newUser.setPassword(DigestUtils.md5Hex(newUser.getPassword()));
		}
		String password = Common.USER_DEFAULT_PASSWORD;
		password = DigestUtils.md5Hex(password);
		newUser.setPassword(password);
//		List<Menu> menus = menuDao.findAll();
//		newUser.setMenus(menus);
//		newUser.setCmsUser(true);
//		boolean s = super.insert(newUser);
		this.saveOne(newUser);
	}
	/**
	 * 获取页面提交的所有权限id将提交id放入user
	 * @param user
	 * @param request
	 */
	private void setUserRoles(User user) {
		String[] roleids = user.getRoleIds().split(",");
		if (roleids!=null) {
			List<Role> roleSet = new ArrayList<Role>();
			for (int i = 0; i < roleids.length; i++) {
				Role role = new Role();
				role.setId(Integer.valueOf(roleids[i]));
				roleSet.add(role);
			}
			user.setRoles(roleSet);
		}
	}
	@Override
	public Page<User> findPagingList(Page<User> page,String search,String hallCode) {
		DetachedCriteria dc = userDao.createDetachedCriteria();
		if(StringUtils.isNotEmpty(search)){
			dc.add(Restrictions.or(Restrictions.like("jobNumber", "%"+search+"%"),Restrictions.like("userName", "%"+search+"%")));
		}
//		if(StringUtils.isNotBlank(hallCode)){
//			dc.add(Restrictions.eq("hallCode", hallCode));
//		}
		User user = ShiroUtils.getCurrentUser();
		String level = ShiroUtils.getCurrentUserRoleLevel(user);
		dc.add(Restrictions.eq("hallCode", user.getHallCode()));
//		dc.createAlias("roles", "r").add(Restrictions.eq("r.level",level));
		
//		dc.add(Restrictions.eq("isEffective", Common.FLAG_ACTIVATION));
		return userDao.find(page, dc);
	}

	@Override
	public boolean sotp(String id) {
		if (userDao.changeStatusToSotpOrUndelete("status", Constants.FLAG_STOP,"userId", id)>=1) {
			return true;
		}
		return false;
	}

	@Override
	public boolean start(String id) {
		if (userDao.changeStatusToSotpOrUndelete("status", Constants.FLAG_ACTIVATION,"userId", id)>=1) {
			return true;
		}
		return false;
	}


	//@Override
//	public boolean valiUserName(String userName,String userid,Integer depid) {
//		if (StringUtils.isBlank(userName))return false;
//		User loginUser = ShiroUtils.getCurrentUser();
//		if(depid==null&&loginUser.isAdmin()){
//			return true;
//		}
//		if(!loginUser.isAdmin()){
//			depid = loginUser.getLibrary().getId();
//		}
//		Search search = new Search(User.class);
//		search.addFilterEqual("userName",userName);
//		search.addFilterEqual("appDepartment.depid", depid);
//		if (userid!=null&&!"".equals(userid)) {
//			search.addFilterNotEqual("userId", userid);
//		}
//		if (userDao.count(search)>0)return false;
//		return true;
//	}

	@Override
	public boolean update(User user) {
		setUserRoles(user);
		User oldUser = userDao.getOne(user.getId());
		oldUser.setAge(user.getAge());
		oldUser.setDepName(user.getDepName());
		oldUser.setUserName(user.getUserName());
		oldUser.setSex(user.getSex());
		oldUser.setDuty(user.getDuty());
		oldUser.setJobNumber(user.getJobNumber());
		oldUser.setPhone(user.getPhone());
		oldUser.setTel(user.getTel());
		oldUser.setChat(user.getChat());
		oldUser.setRoles(user.getRoles());
		User loginUser = ShiroUtils.getCurrentUser();
		oldUser.setModifyUser(loginUser.getUserName());
		oldUser.setModifyDate(new Date());
		userDao.saveOrUpdateOne(oldUser);
		return true;
	}
	@Override
	public boolean insertDeptManager(User user) {
//		user.setUserName(Common.USER_ADMIN_NAME);
//		user.setPassword(DigestUtils.md5Hex(DepartmentUtil.createUserPassword(Common.USER_PASSWORD_LENGTH)));
//		user.setCmsUser(true);
		super.saveOne(user);
		return true;
	}

	@Override
	public User findByUserName$HallCode(String userName,String hallCode) {
		// TODO Auto-generated method stub
		StringBuffer buffer = new StringBuffer();
		buffer.append("from User as user");
		buffer.append(" where userName = ?");
		buffer.append(" and  hallCode = ?");
		buffer.append(" and  isEffective = "+ Common.FLAG_ACTIVATION);
		List<Object> list = new ArrayList<Object>();
		list.add(userName);
		list.add(hallCode);
		User user = userDao.getByHQL(buffer.toString(), list);
		return user;
	}

	@Override
	public String resetPaswd(String id) {
		String password = DepartmentUtil.createUserPassword(Constants.USER_PASSWORD_LENGTH);
		userDao.changePaswd(Integer.valueOf(id), DigestUtils.md5Hex(password));
		return password;
	}
	/* (non-Javadoc)
	 * @see com.flea.common.service.UserService#valiUserName(java.lang.String, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public boolean valiUserName(String userName, Integer userid, String hallCode) {
		if (StringUtils.isBlank(userName))return false;
		List<User> users = userDao.findUserByName$LibraryId(userName, userid, hallCode);
		if(users.size()>0)return false;
		return true;
	}
	/* (non-Javadoc)
	 * @see com.flea.common.service.UserService#checkByHallCode(java.lang.String)
	 */
	@Override
	public boolean checkByHallCode(String hallCode) {
		List<User> users = userDao.findByHallCode(hallCode);
		if(users.size()>0){
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @method:changePsw	更改新密码
	 * @Description:changePsw
	 * @author: HeTao
	 * @date:2016年8月12日 下午2:55:14
	 * @param:@param oldpsw
	 * @param:@param newpsw
	 * @param:@return
	 * @return:ModelAndView
	 */
	@Override
	public boolean changePsw(String oldpsw, String newpsw) {
		return userDao.changePsw(oldpsw,newpsw);
	}
	/* (non-Javadoc)
	 * @see com.flea.common.service.UserService#setPaswd(java.lang.Integer, java.lang.String)
	 */
	@Override
	public Boolean setPaswd(Integer id, String password) {
		return userDao.changePaswd(id, DigestUtils.md5Hex(password));
	}
	@Override
	public void saveOne(User newUser) {
		List<Role> list =roleDao.findAll();
		newUser.setRoles(list);
		User loginUser = ShiroUtils.getCurrentUser();
		String defaultPwd = Common.USER_DEFAULT_PASSWORD;
		defaultPwd = EncryptUtils.encryptMD5(defaultPwd);
		newUser.setLibrary(loginUser.getLibrary());
		newUser.setHallCode(loginUser.getHallCode());
		newUser.setCustomer(loginUser.getCustomer());
		newUser.setPassword(defaultPwd);
		newUser.setRemark("0");
		newUser.setModifyUser(loginUser.getUserName());
		newUser.setModifyDate(new Date());
		userDao.saveOne(newUser);
	}
	@Override
	public boolean deleteById(Integer id) {
		// TODO Auto-generated method stub
		return dao.deleteById(id)>0?true:false;
	}
	
	@Override
	public boolean startUseById(Integer id) {
		// TODO Auto-generated method stub
		return userDao.startUseById(id)>0?true:false;
	}
	
}
