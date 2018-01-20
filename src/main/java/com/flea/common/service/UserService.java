package com.flea.common.service;

import java.util.List;
import java.util.Map;

import com.flea.common.pojo.Page;
import com.flea.common.pojo.User;

public interface UserService extends BaseService<User,Integer> {
	public  void insertUser(User User);
	
	public boolean update(User user);

	public boolean sotp(String id);


	public boolean start(String id);


	 Page<User> findPagingList(Page<User> page,String name,String hallCode);

	public boolean valiUserName(String userName, Integer userid, String hallCode);
	

	boolean insertDeptManager(User user);


	public String resetPaswd(String id);
	
	public User findByUserName$HallCode(String userName,String hallCode);
	
	 boolean checkByHallCode(String hallCode);
	 
	public Boolean setPaswd(Integer id,String password);
	
	/**
	 * 删除用户记录
	 * @param id
	 * @return
	 */
	abstract boolean deleteById(Integer id);
	
	/**
	 * 启用用户
	 * @param id
	 * @return
	 */
	abstract boolean startUseById(Integer id);

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
	public boolean changePsw(String oldpsw, String newpsw);
	
	

}
