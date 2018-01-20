package com.flea.modules.system.service;

import com.flea.common.service.BaseService;
import com.flea.modules.system.pojo.LibraryStop;

/**
 * 
 * @ClassName: LibraryStopService
 * @Description:图书馆停用service 启用状态
 * @author QL
 * @date 2017年4月26日 上午11:39:01
 */
public interface LibraryStopService extends BaseService<LibraryStop, Integer>{
	/**
	 * 
	 * @Description:用户的停用的状态
	 * @param id
	 * @return    
	 * boolean    返回类型
	 * @throws
	 * @author QL 
	 * @date 2017年5月10日 下午3:15:18
	 */
	boolean stopById(Integer id);
	/**
	 * 
	 * @Description:用户的启用状态
	 * @param id
	 * @return    
	 * boolean    返回类型
	 * @throws
	 * @author QL 
	 * @date 2017年5月10日 下午3:15:36
	 */
    boolean startById(Integer id);
    
    
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
     * @date 2017年5月11日 下午8:18:05
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
	 LibraryStop  getLibraryStopInfomation(Integer id);


}
