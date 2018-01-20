package com.flea.common.service;

import java.util.List;

import com.flea.common.pojo.SystemDept;

public interface SystemDeptService extends BaseService<SystemDept, Long>{
	
	
	/**
	 * @param id
	 * @return
	 * 检测是否为空部门
	 */
	public boolean checkEmptyDept(int id);
	
	
	/**
	 * @return
	 * 得到第一级别部门
	 */
	public List<SystemDept> listSystemDept();
	
	
	/**
	 * @return
	 * 根据父id得到子部门列表
	 */
	public List<SystemDept> getlistByFatherId(int fatherId);
	
}
