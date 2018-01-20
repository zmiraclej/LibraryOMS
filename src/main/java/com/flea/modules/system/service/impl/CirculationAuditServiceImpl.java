package com.flea.modules.system.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flea.common.Common;
import com.flea.common.pojo.Page;
import com.flea.common.pojo.User;
import com.flea.common.service.impl.BaseServiceImpl;
import com.flea.common.util.ShiroUtils;
import com.flea.modules.customer.dao.CustomerContactDao;
import com.flea.modules.customer.pojo.Customer;
import com.flea.modules.customer.pojo.CustomerContact;
import com.flea.modules.system.dao.CirculationAuditDao;
import com.flea.modules.system.dao.CirculationDao;
import com.flea.modules.system.pojo.Circulation;
import com.flea.modules.system.pojo.CirculationAudit;
import com.flea.modules.system.pojo.vo.CirculationVo;
import com.flea.modules.system.pojo.vo.LibraryCirculation;
import com.flea.modules.system.service.CirculationAuditService;

/**
 * 跨客户馆际流通审核Service
 * @author bruce
 * @version 2016-08-22
 */
 @Service
public class CirculationAuditServiceImpl extends BaseServiceImpl<CirculationAudit,Integer> implements CirculationAuditService{

	@Autowired
	private CirculationAuditDao circulationAuditDao;
	@Autowired
	private CirculationDao circulationDao;
	@Autowired
	private  CustomerContactDao  contactDao;
	
	@Autowired
	public  CirculationAuditServiceImpl(CirculationAuditDao circulationAuditDao) {
		super(circulationAuditDao);
		this.circulationAuditDao = circulationAuditDao;
	}
	
	@Override
	public Page<CirculationAudit> find(Page<CirculationAudit> page,CirculationAudit circulationAudit,String search) {
		DetachedCriteria dc = circulationAuditDao.createDetachedCriteria();
		if(circulationAudit.getUserId()!=null){//我的申请
			dc.add(Restrictions.eq("userId", circulationAudit.getUserId()));
		}
		if(circulationAudit.getTargetId()!=null){ //审核申请
			dc.add(Restrictions.eq("targetId", circulationAudit.getTargetId()));
		}
		if(!"0".equals(circulationAudit.getStatus())){
			Collection<String> list = new ArrayList<String>();
			list.add(Common.FLAG_ENABLE);
			list.add(Common.FLAG_AUDITED);
			dc.add(Restrictions.in("status", list));
		}
		if(StringUtils.isNotBlank(search)){
			dc.add(Restrictions.or(Restrictions.like("oneself","%"+search+"%"),Restrictions.like("target","%"+search+"%"),Restrictions.like("targetContact","%"+search+"%"),Restrictions.like("targetUser","%"+search+"%")));
		}
		dc.addOrder(Order.asc("status"));
		return circulationAuditDao.find(page, dc);
	}

	/* (non-Javadoc)
	 * @see com.flea.modules.system.service.CirculationAuditService#saveAuditApply(java.lang.String, java.lang.String, java.lang.String, int)
	 */
	@Override
	public boolean saveAuditApply(Customer targetCustomer,String action, String checkedId,
		Integer customerId, int scope) {
		User user =  ShiroUtils.getCurrentUser();
		CirculationAudit  audit = new CirculationAudit();
		audit.setAction(action);
		audit.setUserId(user.getId());
		audit.setUserName(user.getUserName());
		Customer userCustomer = user.getCustomer();
		if(userCustomer!=null){
			List<CustomerContact> contacts = contactDao.findByCustomerId(userCustomer.getId());
			if(contacts.size()>0){
				CustomerContact contact = contacts.get(0);
				audit.setUserContact(contact.getContactPerson()+"-"+contact.getPhone()+"-"+contact.getTel());
			}
			audit.setCustomerId(userCustomer.getId());
			audit.setOneself(userCustomer.getHallCode()+"-"+userCustomer.getName());
		}
		audit.setCreateDate(new Date());
		audit.setTarget(targetCustomer.getHallCode()+"-"+targetCustomer.getName());
		audit.setTargetId(targetCustomer.getId());
		audit.setTargetScope(scope);
		List<CustomerContact>  contacts =targetCustomer.getContacts();
		if(contacts.size()>0){
			CustomerContact contact = contacts.get(0);
			audit.setTargetContact(contact.getContactPerson()+"-"+contact.getPhone()+"-"+contact.getChat());
		}
		List<User>  users =targetCustomer.getUsers();
		if(users.size()>0){
			User Operator = users.get(0);
			audit.setTargetUser(Operator.getUserName());
		}
		circulationAuditDao.saveOne(audit);//审核单
	    String[] arr = checkedId.split(",");
	    for(String hallCode:arr){
	    	Circulation  flag = circulationDao.findByCustomerIdHallCodeScope(customerId, hallCode, scope);
	    	if(flag!=null&&action.equals(Common.FLAG_ENABLE))continue;
			Circulation circulation = new Circulation();
			circulation.setCustomerId(customerId);
			circulation.setHallCode(hallCode);
			circulation.setScope(scope);
			if(action.equals(Common.FLAG_ENABLE)){
				circulation.setStatus(Common.FLAG_DISABLE);
			}else {
				circulation.setStatus(Common.FLAG_STOPED);
			}
			circulation.setAudit(audit);
			circulation.setCreateDate(new Date());
			circulation.setUserId(user.getId());
			circulationDao.saveOne(circulation);
	    }
		return true;
	}

	/* (non-Javadoc)
	 * @see com.flea.modules.system.service.CirculationAuditService#findByAuditId(java.lang.Integer)
	 */
	@Override
	public List<LibraryCirculation> findByAuditId(Integer auditId) {
		return circulationAuditDao.findByAuditId(auditId);
	}

	/* (non-Javadoc)
	 * @see com.flea.modules.system.service.CirculationAuditService#findByTargetIdAndScope(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public List<LibraryCirculation> findByTargetIdAndScope(Integer targetId,
			Integer targetScope) {
		return circulationAuditDao.findByTargetIdAndScope(targetId, targetScope);
	}

	/* (non-Javadoc)
	 * @see com.flea.modules.system.service.CirculationAuditService#findByTargetId(java.lang.Integer)
	 */
	@Override
	public List<CirculationVo> findByTargetId(Integer targetId) {
		return circulationAuditDao.findByTargetId(targetId);
	}

	/* (non-Javadoc)
	 * @see com.flea.modules.system.service.CirculationAuditService#updateStatusById(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Boolean updateStatusById(Integer auditId, String status) {
		User user = ShiroUtils.getCurrentUser();
		String role = ShiroUtils.getCurrentUserRoleLevel(user);
		CirculationAudit audit = circulationAuditDao.getOne(auditId);
		if(!Common.FLAG_ENABLE.equals(status)){
			List<Circulation>  list = this.circulationDao.findByAuditId(auditId);
			for(Circulation circulation:list){
				if(Common.FLAG_AUDITED.equals(status)){// status==3
					if(audit.getAction().equals(Common.FLAG_ENABLE)){//加入
						circulation.setStatus(Common.FLAG_ENABLE);//生效 1
					}
				}else {//拒绝status==2
					circulation.setStatus(status);
				}
				circulationDao.saveOne(circulation);
				if(!audit.getAction().equals(Common.FLAG_ENABLE)){//退出
					circulationDao.deleteByCustomerIdHallCodeScope(circulation.getCustomerId(), circulation.getHallCode(), circulation.getScope());
				}
			}
			return circulationAuditDao.updateStatusById(auditId, status);
		}
		return circulationAuditDao.updateStatusById(auditId, status);
	}

	/* (non-Javadoc)
	 * @see com.flea.modules.system.service.CirculationAuditService#getAduitCount()
	 */
	@Override
	public Map<String, Integer> getAduitCount() {
		User user = ShiroUtils.getCurrentUser();
		Customer customer = user.getCustomer();
		Integer  customerId =0;
		if(customer!=null){
			customerId = customer.getId();
		}
		return 	circulationAuditDao.getAduitCount(user.getId(), customerId);
	}

	/* (non-Javadoc)
	 * @see com.flea.modules.system.service.CirculationAuditService#findAudit(com.flea.common.pojo.Page, com.flea.modules.system.pojo.CirculationAudit)
	 */
	@Override
	public Page<CirculationAudit> findAudit(Page<CirculationAudit> page,
			CirculationAudit circulationAudit) {

		return null;
	}
}
