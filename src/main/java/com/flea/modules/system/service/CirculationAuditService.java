package com.flea.modules.system.service;


import java.util.List;
import java.util.Map;

import com.flea.common.pojo.Page;
import com.flea.common.service.BaseService;
import com.flea.modules.customer.pojo.Customer;
import com.flea.modules.system.pojo.CirculationAudit;
import com.flea.modules.system.pojo.vo.CirculationVo;
import com.flea.modules.system.pojo.vo.LibraryCirculation;

/**
 * 跨客户馆际流通审核Service
 * @author bruce
 * @version 2016-08-22
 */

public interface CirculationAuditService extends BaseService<CirculationAudit,Integer> {

   	Page<CirculationAudit> find(Page<CirculationAudit> page,CirculationAudit circulationAudit,String search);
   	
   	
 	Page<CirculationAudit> findAudit(Page<CirculationAudit> page,CirculationAudit circulationAudit);
 	
   	/**
   	 * 计算申请和审核总数
   	 * @return
   	 */
   	Map<String,Integer> getAduitCount();
   	
   	
   	/**
   	 * 保存发送申请
   	 * @param circulationAudit
   	 * @param action
   	 * @param checkedId
   	 * @param customerId
   	 * @param scope
   	 * @return
   	 */
   	boolean  saveAuditApply(Customer targetCustomer,String action,String checkedId,Integer customerId,int scope);
   	
   	/**
	 * 查询申请加入流通的馆及范围
	 * @param auditId
	 * @return
	 */
   	List<LibraryCirculation>  findByAuditId(Integer auditId);
   	
	/**
	 * 查询对象范围
	 * @param targetId
	 * @param targetScope
	 * @return
	 */
	List<LibraryCirculation>  findByTargetIdAndScope(Integer targetId,Integer targetScope);
	
	/**
	 * 查询我的审核申请
	 * @param targetId
	 * @return
	 */
	List<CirculationVo> findByTargetId(Integer targetId);
	
	/**
	 * 更改审核状态
	 * @param auditId
	 * @param status
	 * @return
	 */
	Boolean  updateStatusById(Integer auditId,String status);
	
	
}
