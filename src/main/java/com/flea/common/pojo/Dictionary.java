package com.flea.common.pojo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.flea.modules.customer.pojo.Customer;
@Entity
@Table(name="system_dictionary")
public class Dictionary implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8233851651137761399L;
	
	@Id
	@GenericGenerator(name="generator",strategy="identity")
	@GeneratedValue(generator="generator")
	@Column(name ="id",unique=true,nullable= false)
	private Integer id;
	@Column(name="code",length=30)
	private String code;
	@Column(name="desc",length=30)
	private String desc;
	@Column(name="value",length=10)
	private String value;
	@Column(name="isEffective")
	private String isEffective;
//	@OneToMany(cascade= CascadeType.ALL,fetch = FetchType.LAZY,mappedBy ="dictionary")
//	private Set<Customer> customer = new HashSet<Customer>(0);
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getIsEffective() {
		return isEffective;
	}
	public void setIsEffective(String isEffective) {
		this.isEffective = isEffective;
	}
//	public Set<Customer> getCustomer() {
//		return customer;
//	}
//	public void setCustomer(Set<Customer> customer) {
//		this.customer = customer;
//	}
	
}
