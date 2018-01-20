package com.flea.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.flea.common.dao.BaseDao;
import com.flea.common.pojo.Menu;
import com.flea.modules.system.pojo.Region;

/**
 * 菜单管理DAO接口
 * @author Bruce
 * @version 2016-01-12
 */
@Repository
public interface MenuDao extends BaseDao<Menu,Integer> {
	
	
	List<Menu> findByPid(Integer pid);
	
	public List<Menu> findByUserId(Integer userId);
}
