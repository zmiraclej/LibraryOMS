package com.flea.modules.system.dao;

import java.util.List;

import org.apache.zookeeper.Op.Check;
import org.springframework.stereotype.Repository;

import com.flea.common.dao.BaseDao;
import com.flea.common.pojo.Page;
import com.flea.modules.report.pojo.SearchResult;
import com.flea.modules.report.pojo.VLibraryBook;
import com.flea.modules.report.pojo.vo.QueryCriteria;
import com.flea.modules.system.pojo.Circulation;
import com.flea.modules.system.pojo.vo.LibraryCirculation;

/**
 * 馆际流通DAO接口
 * @author bruce
 * @version 2016-08-18
 */
@Repository
public interface CirculationDao extends BaseDao<Circulation,Integer> {
	
 	/**
 	 * 馆际流通列表
 	 * @param page
 	 * @param customerId
 	 * @return
 	 */
   	SearchResult<LibraryCirculation>  findLibraryCirculationList(Page<LibraryCirculation> page,Integer customerId,String search);
   	
	int getMaxScope(Integer  customerId);
	
	boolean updateLibraryCirculation(String allId,int jieshu);
	
	
	Circulation  findByCustomerIdHallCodeScope(Integer  customerId,String HallCode,Integer scope);
	
	boolean  deleteByCustomerIdHallCodeScope(Integer  customerId,String HallCode,Integer scope);
	
	boolean  updateScopeByCustomerIdHallCode(Integer  customerId,String HallCode,Integer scope);
   	
	List<Circulation> findByCustomerIdAndScope(Integer customerId,Integer scope);
	
	List<Circulation> findByAuditId(Integer auditId);
}
