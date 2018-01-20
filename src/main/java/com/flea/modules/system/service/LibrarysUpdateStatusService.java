package com.flea.modules.system.service;

import com.flea.common.service.BaseService;
import com.flea.modules.system.pojo.LibrarysUpdateStatus;
 
/**
 * 
 * @ClassName: LibrarysUpdateStatusService
 * @Description:图书馆用户新增权限中的维护列表编辑之后的状态
 * @author QL
 * @date 2017年5月8日 上午10:30:35
 */
public interface LibrarysUpdateStatusService extends BaseService<LibrarysUpdateStatus, Integer>{
	/**
	 * 
	 * @Description:当用户的信息状态为修改待审时，平台给驳回了，则用户显示的信息为原来的数据状态为修改驳回，而不是更新之后的数据
	 * @param libraryId
	 * @return    
	 * boolean    返回类型
	 * @throws
	 * @author QL 
	 * @date 2017年5月11日 下午5:16:53
	 */
	public boolean updateLibrarys(Integer libraryId);
}
