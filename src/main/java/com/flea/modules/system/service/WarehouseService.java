/**  
* @Package com.flea.common.service
* @Description: TODO
* @author bruce
* @date 2015年12月17日 上午10:17:37
* @version V1.0  
*/ 
package com.flea.modules.system.service;


import com.flea.common.service.BaseService;
import com.flea.modules.system.pojo.Warehouse;

/**
 * @author bruce
 * @2015年12月17日 上午10:17:37
 */
public interface WarehouseService  extends BaseService<Warehouse, Long> {

	boolean removeById(int id);
}
