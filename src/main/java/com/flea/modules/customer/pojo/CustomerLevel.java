package com.flea.modules.customer.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.ws.soap.Addressing;

import org.hibernate.annotations.GenericGenerator;
/**
 * 客户分级对象
 * @author Wuhua
 */
@Entity
@Table(name="system_customer_level")
public class CustomerLevel  implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GenericGenerator(name = "generator", strategy = "identity")
	@GeneratedValue(generator = "generator")
	@Column(name ="id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name ="name", nullable = false)
	private String name;
	@Column(name ="full_name", nullable = false)
	private String fullName;
	@Column(name ="short_name", nullable = true)
	private String shortName; 	
	@Column(name ="code", nullable = false)
	private String code;
	@Column(name ="level", nullable = false)
	private Integer level;
	@Column(name ="isUsed", nullable = false)
	private Integer isUsed;
	@Column(name ="customerID", nullable = true)
	private Integer customerID;
	@Column(name ="type", nullable = false)
	private Integer type;
	
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
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public Integer getIsUsed() {
		return isUsed;
	}
	public void setIsUsed(Integer isUsed) {
		this.isUsed = isUsed;
	}
	public Integer getCustomerID() {
		return customerID;
	}
	public void setCustomerID(Integer customerID) {
		this.customerID = customerID;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
	
}
