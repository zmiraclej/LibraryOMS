package com.flea.modules.data.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;

/**
 * @author Wuhua
 * @2016年4月15日 下午3:47:44
 * 数据上报POJO
 */
@Entity
@Table(name="system_data_report")
public class DataReport implements Serializable{
	@Id
	@GenericGenerator(name="generator",strategy="identity")
	@GeneratedValue(generator="generator")
	@Column(name ="id", unique=true, nullable=false)
	private Integer id;
	@Column(name ="library_code", unique=true, nullable= true)
	private String libraryCode; //图书馆编码(国家)
	@Column(name ="unicode", nullable= true)
	private String unicode;   //图书馆授权码
	@Formula("(select ls.hallCode from librarys ls where ls.id = library_id)")
	private String hallCode; //图书馆code
	@Column(name ="customer_id", nullable=false)
	private Integer customerId; //客户ID
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
	public String getUnicode() {
		return unicode;
	}
	public void setUnicode(String unicode) {
		this.unicode = unicode;
	}
	public String getHallCode() {
		return hallCode;
	}
	public void setHallCode(String hallCode) {
		this.hallCode = hallCode;
	}
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	
	
	
	
}
