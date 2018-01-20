package com.flea.modules.system.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.flea.common.dao.BaseDao;
import com.flea.modules.system.pojo.CirculationAudit;
import com.flea.modules.system.pojo.vo.CirculationVo;
import com.flea.modules.system.pojo.vo.LibraryCirculation;

/**
 * 跨客户馆际流通审核DAO接口
 * @author bruce
 * @version 2016-08-22
 */
@Repository
public interface CirculationAuditDao extends BaseDao<CirculationAudit,Integer> {
	
 	/**
   	 * 计算申请和审核总数
   	 * @return
   	 */
   	Map<String,Integer> getAduitCount(Integer userId,Integer customerId);
   	
	
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
