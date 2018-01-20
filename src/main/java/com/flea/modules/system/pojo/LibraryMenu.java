/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.flea.modules.system.pojo;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import com.google.common.collect.Lists;

/**
 * 
 * @author bruce
 * @2015年12月1日 上午9:58:29
 */
@Entity
@Table(name = "librarys_menus")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class LibraryMenu implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4472103283360941004L;

	@Id
	@GenericGenerator(name="generator",strategy="identity")
	@GeneratedValue(generator="generator")
	@Column(name ="id",unique=true,nullable= false)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="parent_id")
	@NotFound(action = NotFoundAction.IGNORE)
	private LibraryMenu parent;	// 父级菜单
	
	private String parentIds; // 所有父级编号
	private String name; 	// 名称
	private String href; 	// 链接
	private String target; 	// 目标（ mainFrame、_blank、_self、_parent、_top）
	private String icon; 	// 图标
	private Integer sort; 	// 排序
	private String isShow; 	// 是否在菜单中显示（1：显示；0：不显示）
	private String permission; // 权限标识
	
	@OneToMany(mappedBy = "parent", fetch=FetchType.LAZY)
	@OrderBy(value="sort") @Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private List<LibraryMenu> childList = Lists.newArrayList();// 拥有子菜单列表
	
	@ManyToMany(cascade={CascadeType.PERSIST, CascadeType.MERGE},mappedBy="libraryMenuList")
//	@JoinTable(name = "sys_libraryuser_LibraryMenu", joinColumns = { @JoinColumn(name = "LibraryMenu_id") }, inverseJoinColumns = { @JoinColumn(name = "user_id") })
	@Fetch(FetchMode.SUBSELECT)
	private List<LibraryUser> userList = Lists.newArrayList();// 拥有子菜单列表
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	
	public LibraryMenu getParent() {
		return parent;
	}

	public void setParent(LibraryMenu parent) {
		this.parent = parent;
	}

	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}
	
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	public Integer getSort() {
		return sort;
	}
	
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public List<LibraryMenu> getChildList() {
		return childList;
	}

	public void setChildList(List<LibraryMenu> childList) {
		this.childList = childList;
	}
	
	public List<LibraryUser> getUserList() {
		return userList;
	}

	public void setUserList(List<LibraryUser> userList) {
		this.userList = userList;
	}
	
	@Transient
	public static void sortList(List<LibraryMenu> list, List<LibraryMenu> sourcelist, Integer parentId){
		for (int i=0; i<sourcelist.size(); i++){
			LibraryMenu e = sourcelist.get(i);
			if (e.getParent()!=null && e.getParent().getId()!=null
					&& e.getParent().getId().equals(parentId)){
				list.add(e);
				// 判断是否还有子节点, 有则继续获取子节点
				for (int j=0; j<sourcelist.size(); j++){
					LibraryMenu child = sourcelist.get(j);
					if (child.getParent()!=null && child.getParent().getId()!=null
							&& child.getParent().getId().equals(e.getId())){
						sortList(list, sourcelist, e.getId());
						break;
					}
				}
			}
		}
	}

	
	

	@Transient
	public static boolean isRoot(String id){
		return id != null && id.equals("1");
	}


}
