/**
 * Copyright &copy; 2014-2015.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.flea.modules.ebook.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * 电子书类
 * @author Bruce Tie
 * @date 2014-11-3 下午4:10:40 
 * @function
 */

@Entity
@Table(name="system_ecategory")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EbookCategory implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GenericGenerator(name="generator",strategy="identity")
	@GeneratedValue(generator="generator")
	@Column(name ="categoryId",unique=true,nullable= false)
	private Integer categoryId;
	
	private String categoryName;//类别名称

	@Column(name="iorder")
	@OrderBy
	private int order;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="parentId")
	@NotFound(action = NotFoundAction.IGNORE)
	private EbookCategory category;
	
	public Integer getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}
	
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	
	public EbookCategory getCategory() {
		return category;
	}
	public void setCategory(EbookCategory category) {
		this.category = category;
	}
	
}
