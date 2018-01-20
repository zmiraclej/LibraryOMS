package com.flea.modules.customer.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
/**
 * 馆号库存表对象
 */
@Entity
@Table(name="lib_code")
public class LibraryCode {
	@Id
	@GenericGenerator(name = "generator", strategy = "identity")
	@GeneratedValue(generator = "generator")
	@Column(name ="id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name="library_code")
	private String libraryCode;
	
	@Column(name="use_date")
	private Date useDate; //使用时间
	
	@Column(name ="level", nullable = false)
	private Integer level; // 馆号级别(类别) 五大类
	
	@Column(name ="status", nullable = false)
	private Integer status; //状态，是否使用：0 未使用(默认) ； 1 使用, 2 预分配
	
	@Column(name ="customer_id")
	private Integer customerId; // system_customer 的 客户id
	
	@Column(name ="scl_id")
	private Integer sclId; //批次表system_customer_library的 批次id
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getLibraryCode() {
		return libraryCode;
	}
	public void setLibraryCode(String libraryCode) {
		this.libraryCode = libraryCode;
	}
	public Date getUseDate() {
		return useDate;
	}
	public void setUseDate(Date useDate) {
		this.useDate = useDate;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	public Integer getSclId() {
		return sclId;
	}
	public void setSclId(Integer sclId) {
		this.sclId = sclId;
	}
	
	

	
}
