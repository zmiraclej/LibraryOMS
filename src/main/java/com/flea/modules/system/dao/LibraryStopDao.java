package com.flea.modules.system.dao;


import com.flea.common.dao.BaseDao;
import com.flea.modules.system.pojo.LibraryStop;
/**
 * 
 * @ClassName: LibraryStopDao
 * @Description:图书馆停用
 * @author QL
 * @date 2017年4月28日 下午4:58:31
 */
public interface LibraryStopDao extends BaseDao<LibraryStop,Integer>{
	 /**
	  * 
	  * @Description:停用状态的修改，当为停用状态，libraryStatus变为6 停用待审
	  * @param id
	  * @return    
	  * int    返回类型
	  * @throws
	  * @author QL 
	  * @date 2017年5月10日 上午11:29:05
	  */
	 public int stopById(Integer id);
	 
	 /**
	  * 
	  * @Description:如果当平台驳回当前的用户信息，libraryStatus变为7 状态改为停用驳回
	  * @param libraryId
	  * @return    
	  * int    返回类型
	  * @throws
	  * @author QL 
	  * @date 2017年5月10日 上午11:30:08
	  */
	 public boolean updateLibraryStopReject(Integer libraryId);
	 
	 /**
	  * 
	  * @Description:如果当平台通过当前的用户信息，libraryStatus变为8状态改为停用
	  * @param libraryId
	  * @return    
	  * int    返回类型
	  * @throws
	  * @author QL 
	  * @date 2017年5月10日 下午2:23:24
	  */
	 public boolean updateLibraryStop(Integer libraryId);
	 
	 
	 /**
	  * 
	  * @Description:如果当平台驳回当前的用户为启用待审，状态为10启用驳回
	  * @param libraryId
	  * @return    
	  * boolean    返回类型
	  * @throws
	  * @author QL 
	  * @date 2017年5月10日 下午3:59:37
	  */
	 public boolean updateLibraryStartReject(Integer libraryId);
	 
	 /**
	  * 
	  * @Description:当用户信息状态为停用时，点击锁，状态为9 启用待审
	  * @param id
	  * @return    
	  * int    返回类型
	  * @throws
	  * @author QL 
	  * @date 2017年5月10日 下午3:10:33
	  */
	 public int startById(Integer id);
	   
	   
	   
	 /**
	   * 
	   * @Description:平台屏蔽用户信息
	   * @param id
	   * @return    
	   * boolean    返回类型
	   * @throws
	   * @author QL 
	   * @date 2017年5月11日 下午7:49:38
	   */
	 public boolean platFormDisplayCustomer(Integer id);
     /**
      * 
      * @Description:平台显示用户信息
      * @param id
      * @return    
      * boolean    返回类型
      * @throws
      * @author QL 
      * @date 2017年5月11日 下午8:19:29
      */
	public boolean platFormLookCustomer(Integer id);
	
	
	/**
	 * 
	 * @Description:图书馆新增权限
	 * @param userId
	 * @return    
	 * boolean    返回类型
	 * @throws
	 * @author QL 
	 * @date 2017年5月12日 上午10:57:47
	 */
	public boolean isExtistLibrarysAddRole(Integer userId);
    
	/**
	 * 
	 * @Description:图书馆审核用户停用待审的信息
	 * @param id
	 * @return    
	 * LibraryStop    返回类型
	 * @throws
	 * @author QL 
	 * @date 2017年5月16日 上午11:56:39
	 */
	 LibraryStop getLibraryStopInfomation(Integer libraryStopId);
     
	 
	 /**
	  * 
	  * @Description:当图书馆平台审核用户为启用状态时，删除用户停用的信息
	  * @param id    
	  * void    返回类型
	  * @throws
	  * @author QL 
	  * @date 2017年5月17日 下午3:51:48
	  */
	 public void deleteByStopId(Integer id);

}
