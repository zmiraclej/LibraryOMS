/**  
* @Package com.flea.modules.customer.pojo
* @Description: TODO
* @author bruce
* @date 2016年4月15日 下午3:47:44
* @version V1.0  
*/ 
package com.flea.modules.customer.pojo;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.flea.modules.system.pojo.Library;

/**
 * @author bruce
 * @2016年4月15日 下午3:47:44
 */

@Entity
@Table(name="system_customer_hallCodes")
public class CustomerHallCode {

	@Id
	@GenericGenerator(name="generator",strategy="identity")
	@GeneratedValue(generator="generator")
	@Column(name ="id",unique=true,nullable= false)
	private Integer id;
	@Column(name="code")
	private String code;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="customerId")
	private Customer customer;
	@JoinColumn(name="status")
	private Integer status;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="libraryId")
	private Library library;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public Library getLibrary() {
		return library;
	}
	public void setLibrary(Library library) {
		this.library = library;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
}
