package com.flea.common.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.flea.modules.customer.pojo.Customer;
/**
 * @author liuyi
 * 2017-03-23
 * 系统部门表
 */
@Entity
@Table(name="system_dept")
public class SystemDept implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5727588019628078835L;
	
	
	@Id
	@GenericGenerator(name="generator",strategy="identity")
	@GeneratedValue(generator="generator")
	@Column(name ="id",unique=true,nullable= false)
	private Integer id;
	@Column(name ="name")
	private String name;
	@Column(name ="isEffective")
	private int isEffective;
	@Column(name ="father_dept_id")
	private int fatherId;
	@Column(name ="level")
	private int level;
	@Column(name ="sort")
	private int sort;
	
	@Column(name ="customer_id")
	private int customerId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIsEffective() {
		return isEffective;
	}

	public void setIsEffective(int isEffective) {
		this.isEffective = isEffective;
	}

	public int getFatherId() {
		return fatherId;
	}

	public void setFatherId(int fatherId) {
		this.fatherId = fatherId;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	
	
}
