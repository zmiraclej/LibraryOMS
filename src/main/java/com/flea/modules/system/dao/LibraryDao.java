package com.flea.modules.system.dao;

import java.util.List;
import java.util.Map;

import com.flea.common.dao.BaseDao;
import com.flea.common.pojo.Page;
import com.flea.modules.system.pojo.Library;
import com.flea.modules.system.pojo.LibraryBook;
import com.flea.modules.system.pojo.LibraryCirculateRel;
import com.flea.modules.system.pojo.vo.LibraryCity;

public interface LibraryDao extends BaseDao<Library, Long>{

	Map<String,List<LibraryCity>> getLibrarysWithGroupByCity();
	
	Library findByCode(String code);
	
	Boolean saveLibraryUser(String hallCode);
	
	Object findLibUserByHallCodeAndUserName(String hallCode,String userName);
	
   	
   	boolean updateLibraryUserPassword(String hallCode,String userPwd);
	
	 boolean  updateBorrowModelById(Integer maxSum,Integer freeRentDays,String rent,String libId);
	 
	 /**
	  * 
	  * @param Operation
	  * @param libId
	  * @return
	  */
	 boolean  updateDepositModelById(String reader,String deposit,String libId);
	 
	 /**
	  * 馆际流通
	  * @param Operation
	  * @param libId
	  * @return
	  */
	 boolean  updateCirculateById(String Operation,String libId);
	 
	 /**
	  * 
	  * @method:updateLibraryScope
	  * @Description:updateLibraryScope  更新用户所选中的图书馆范围
	  * @author: HeTao
	  * @date:2016年5月26日 下午5:22:25
	  * @param:@param allId
	  * @param:@param jieshu
	  * @param:@return
	  * @return:boolean
	  */
	 boolean updateLibraryScope(String allId, int jieshu);
	 
	 /**
	  * 
	  * @method:getMaxScope
	  * @Description:getMaxScope 返回最大的范围值
	  * @author: HeTao
	  * @date:2016年5月27日 上午10:56:20
	  * @param:@return
	  * @return:int
	  */
	int getMaxScope();

	/**
	 * 
	 * @method:find
	 * @Description:find	馆际流通审核列表
	 * @author: HeTao
	 * @param searchText 
	 * @date:2016年6月20日 上午11:06:27
	 * @param:@param page
	 * @param:@param cil
	 * @param:@param status
	 * @param:@return
	 * @return:Page<LibraryCirculate>
	 */
	Map<String,Object> findLibarary(Page<LibraryCirculateRel> page, String searchText);
	
	/**
	 * 
	 * @method:reviewLibraryInfo
	 * @Description:reviewLibraryInfo	馆际流通审核详情
	 * @author: HeTao
	 * @date:2016年6月22日 下午3:00:09
	 * @param:@param id
	 * @param:@return
	 * @return:List<LibraryBook>
	 */
	List<LibraryBook> reviewLibraryInfo(Integer id);
	
	/**
	 * 
	 * @method:circulateInfo
	 * @Description:circulateInfo	借书详情
	 * @author: HeTao
	 * @date:2016年6月23日 上午10:49:50
	 * @param:@param id
	 * @param:@return
	 * @return:LibraryCirculateRel
	 */
	Map<String, Object> queryCirculate(Integer id);
	
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
	boolean updateCirculate(Integer state,Integer id);
	
	/**
	 * 
	 * @method:getUserInfo
	 * @Description:getUserInfo		电话info
	 * @author: HeTao
	 * @date:2016年6月23日 下午7:29:51
	 * @param:@param hallcode
	 * @param:@return
	 * @return:com.flea.modules.system.pojo.vo.User
	 */
	com.flea.modules.system.pojo.vo.User getUserInfo(String hallcode);
	
	int  findLibraryBooksSumByCode(String libCode);
	
	
	List<String> findLevelByCustomerId(Integer customerId);
	
	/**
	 * 
	 * @Description:获取图书馆未审核几条记录
	 * @param qc
	 * @return    
	 * BigInteger    返回类型
	 * @throws
	 * @author QL 
	 * @date 2017年3月29日 下午6:21:41
	 */
	int getLibrarysCount();
	
}
