package com.flea.modules.system.service;

 
import java.util.List;
import java.util.Map;

 
import com.flea.common.pojo.Page;
import com.flea.common.service.BaseService;
import com.flea.modules.customer.pojo.CutomerLibrary;
import com.flea.modules.customer.pojo.LibraryCode;
import com.flea.modules.system.pojo.Library;
import com.flea.modules.system.pojo.LibraryBook;
import com.flea.modules.system.pojo.LibraryCirculateRel;
import com.flea.modules.system.pojo.LibraryCustomer;
import com.flea.modules.system.pojo.LibraryImportErrorData;
import com.flea.modules.system.pojo.vo.LibraryCity;

public interface LibraryService  extends BaseService<Library, Integer>{
	
	/**
	 * @return
	 * excel数据导入数据库并返回错误信息
	 */
	public Map<String, String> saveErroeExcel(LibraryImportErrorData errorData);
	
   	Page<Library> find(Page<Library> page,String search, Library library);
   	
   	
   	Boolean saveLibraryUser(String hallCode);
   	
	Object findLibUserByHallCodeAndUserName(String hallCode,String userName);
	
   	boolean updateLibraryUserPassword(String hallCode,String isEffective);

   	/**
   	 * 通过馆名查询是否重复
   	 * @param name
   	 * @return
   	 */
   	public Long findLibraryByName(String name);
	
	//更新馆
	public void updateLibrary(Library library,boolean sendpwd);
	//检查账号是否存在
	public Library checkName(String userName);
	
	public String getMaxHallCode();
	
	Map<String,List<LibraryCity>> findLibraryWithGroupByCity();
	
	Page<Library> findPagingList(Page<Library> page,String searchStr);
	 
	 boolean checkDeptCode(String deptCode);
	 
	 /**
	  * 更新借阅模式
	  * @param Operation
	  * @param libIds
	  */
	 void  updateBorrowModelById(Integer maxSum,Integer freeRentDays,String rent,String[] libIds);
	 
	 /**
	  * 押金模式设置
	  * @param Operation
	  * @param libIds
	  */
	 void  updateDepositModelById(String reader,String deposit,String[] libIds);
	 
	 /**
	  * 馆际流通设置
	  * @param Operation
	  * @param libIds
	  */
	 void  updateCirculateModelById(String Operation,String[] libIds);
	 
	 
	 void removeSolrIndex(Integer id);
	 
	 
		/**
		 * 停用平台
		 * @param id
		 * @return
		 */
		boolean sotp(Integer id);

		/**
		 * 启用平台
		 * @param id
		 * @return
		 */
		boolean start(Integer id);
		
	/**
	 * 
	 * @method:updateLibraryScope
	 * @Description:updateLibraryScope 更新用户所选中的图书馆范围
	 * @author: HeTao
	 * @date:2016年5月26日 下午5:21:10
	 * @param:@param allId
	 * @param:@param jieshu
	 * @param:@return
	 * @return:boolean
	 */
	boolean updateLibraryScope(String allId,int jieshu);
	
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
	 * @Description:find	馆际流通审核
	 * @author: HeTao
	 * @param searchText 
	 * @date:2016年6月20日 上午11:06:27
	 * @param:@param page
	 * @param:@param cil
	 * @param:@param status
	 * @param:@return
	 * @return:Page<LibraryCirculate>
	 */
	Page<LibraryCirculateRel> find(Page<LibraryCirculateRel> page, String searchText);
	
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
	
	List<Map<String,String>> findLibsByCustomerId(Integer customerId);
	
	List<String> findLevelByCustomerId(Integer customerId);
	
	Library  findByHallCode(String hallCode);

	/**
	 * 
	 * @Description:审核通过或者不通过
	 * @param id
	 * @param status
	 * @return    
	 * boolean    返回类型
	 * @throws
	 * @author QL 
	 * @date 2017年3月26日 下午12:51:40
	 */
	boolean audit(Integer id, Integer status);
	/**
	 * 
	 * @Description:审核，驳回理由添加
	 * @param id
	 * @param status
	 * @param rejectReason
	 * @return    
	 * boolean    返回类型
	 * @throws
	 * @author QL 
	 * @date 2017年3月26日 下午12:52:14
	 */
   	boolean audit(Integer id, Integer status, String rejectReason);
   	
   /**
    * 
    * @Description:显示未审核的图书馆信息
    * @param qc
    * @return    
    * Integer    返回类型
    * @throws
    * @author QL 
    * @date 2017年3月29日 下午6:39:52
    */
   	int getLibrarysCount();
   	
   	/**
   	 * 增加图书馆 分配馆号并更新图书馆基库
   	 * @param library 
   	 * @return
   	 */
   	public void addLibraryAndUpdateLibraryCode(Library library);

   	/**
   	 * 自动增加图书馆 分配馆号并更新图书馆基库
   	 * @param library 
   	 * @return
   	 */
   	public void autoAddLibraryAndUpdateLibraryCode(Library library);
   	
   	/**
   	 * 根据图书馆类型分配馆号
   	 * @param library
   	 * @return
   	 */
	public LibraryCode allotLibraryCode(Library library);
}