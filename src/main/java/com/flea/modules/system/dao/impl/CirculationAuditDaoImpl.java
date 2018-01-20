package com.flea.modules.system.dao.impl;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import com.flea.common.dao.impl.BaseDaoImpl;
import com.flea.modules.system.dao.CirculationAuditDao;
import com.flea.modules.system.pojo.CirculationAudit;
import com.flea.modules.system.pojo.vo.CirculationVo;
import com.flea.modules.system.pojo.vo.LibraryCirculation;

/**
 * 跨客户馆际流通审核DAO接口
 * @author bruce
 * @version 2016-08-22
 */
@Repository
public class CirculationAuditDaoImpl extends BaseDaoImpl<CirculationAudit,Integer> implements CirculationAuditDao{

	/* (non-Javadoc)
	 * @see com.flea.modules.system.dao.CirculationAuditDao#findByAuditId(java.lang.Integer)
	 */
	@Override
	public List<LibraryCirculation> findByAuditId(Integer auditId) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(" SELECT l.`name`,l.hallCode,l.libraryLevel,l.areaAddress,CONCAT(l.conperson,',',l.phone) as conperson,CONCAT(s.hallCode,c.scope) as scopeString ");
		buffer.append(" FROM system_circulation c LEFT JOIN system_circulation_audit a on c.auditId=a.id");
		buffer.append(" LEFT JOIN librarys l on c.hallCode=l.hallCode ");
		buffer.append(" LEFT JOIN system_customer s on c.customerId =s.id");
		buffer.append(" WHERE a.id="+auditId+";");
		SQLQuery query = getSession().createSQLQuery(buffer.toString());
		query.setResultTransformer(new AliasToBeanResultTransformer(LibraryCirculation.class));
		return query.list();
	}

	/* (non-Javadoc)
	 * @see com.flea.modules.system.dao.CirculationAuditDao#findByTargetIdAndScope(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public List<LibraryCirculation> findByTargetIdAndScope(Integer targetId,
			Integer targetScope) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(" SELECT l.id,l.`name`,l.hallCode,l.libraryLevel,l.areaAddress,CONCAT(l.conperson,',',l.phone) as conperson,CONCAT('范围',c.scope) as scopeString");
		buffer.append(" FROM system_circulation c LEFT JOIN librarys l on c.hallCode=l.hallCode");
		buffer.append(" WHERE c.status='1' and c.customerId ="+targetId+" and c.scope="+targetScope);
		SQLQuery query = getSession().createSQLQuery(buffer.toString());
		query.setResultTransformer(new AliasToBeanResultTransformer(LibraryCirculation.class));
		return query.list();
	}

	/* (non-Javadoc)
	 * @see com.flea.modules.system.dao.CirculationAuditDao#findByTargetId(java.lang.Integer)
	 */
	@Override
	public List<CirculationVo> findByTargetId(Integer targetId) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(" SELECT a.id, a.createDate,a.action,a.userName, CONCAT(u.hallCode,'-',a.customerName) operator, CONCAT(co.contactPerson,' ',co.tel,'	',co.phone) contactPerson,a.`status` from system_circulation_audit a");
		buffer.append(" LEFT JOIN system_customer c ON a.targetId = c.id");
		buffer.append(" LEFT JOIN  system_customer_contact co on a.customerId= co.customerId");
		buffer.append(" LEFT JOIN system_user u on a.userId=u.id");
		buffer.append(" WHERE  a.targetId ="+targetId);
		SQLQuery query = getSession().createSQLQuery(buffer.toString());
		query.setResultTransformer(new AliasToBeanResultTransformer(CirculationVo.class));
		return query.list();
	}

	/* (non-Javadoc)
	 * @see com.flea.modules.system.dao.CirculationAuditDao#updateStatusById(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Boolean updateStatusById(Integer auditId, String status) {
		String sqString ="UPDATE system_circulation_audit set `status`= '"+status+"' where id ="+auditId;
		 SQLQuery query = getSession().createSQLQuery(sqString);
		return query.executeUpdate()>0?true:false;
	}

	/* (non-Javadoc)
	 * @see com.flea.modules.system.dao.CirculationAuditDao#getAduitCount(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Map<String, Integer> getAduitCount(Integer userId, Integer customerId) {
		Map<String, Integer>  map = new HashMap<String, Integer>();
		String sql ="SELECT COUNT(*) from system_circulation_audit where userId="+userId;
		SQLQuery query = getSession().createSQLQuery(sql);
		int myApply = ((BigInteger)query.uniqueResult()).intValue();
		sql ="SELECT COUNT(*) from system_circulation_audit where targetId="+customerId; 
		query = getSession().createSQLQuery(sql);
		int myAudit = ((BigInteger)query.uniqueResult()).intValue();
		map.put("applySum", myApply);
		map.put("auditSum", myAudit);
		return map;
	}
	
}
