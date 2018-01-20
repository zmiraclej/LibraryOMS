package com.flea.common.dao;

import java.util.List;

import com.flea.common.pojo.Page;
import com.flea.common.pojo.User;
import com.flea.modules.report.pojo.BorrowerReport;
import com.flea.modules.report.pojo.SearchResult;

public interface UserDao extends BaseDao<User, Long> {

	List<User> findUserByName$LibraryId(String userName, Integer userid, String hallCode);
	
	
	List<User> findByHallCode(String hallCode);
	 
	List<User> findByCustomerId(Integer customerId);
	
	boolean  updateHallCodeById(String hallCode,Integer userId);
	
	boolean  changePaswd(Integer userId,String password);
	
	/**
	 * 删除用户
	 */
	int deleteById(int id);
	
	/**
	 * 启用用户
	 */
	int startUseById(int id);

	/**
	 * 
	 * @method:changePsw	更改新密码
	 * @Description:changePsw
	 * @author: HeTao
	 * @date:2016年8月12日 下午2:55:14
	 * @param:@param oldpsw
	 * @param:@param newpsw
	 * @param:@return
	 * @return:ModelAndView
	 */
	boolean changePsw(String oldpsw, String newpsw);
	
}
