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
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.flea.common.Constants;

/**
 * 馆际流通
 * @author bruce
 * @2016年5月26日 上午10:58:48
 */
@Entity
@Table(name="system_library_circulate")
public class LibraryCirculate {

	@Id
	@GenericGenerator(name="generator",strategy="identity")
	@GeneratedValue(generator="generator")
	@Column(name ="id",unique=true,nullable= false)
	private Integer id;
	@Column(name="range")
	private String range;
	@Column(name="hallCodes")
	private String hallCodes;
	@Column(name="isEffective")
	private short isEffective =Constants.FLAG_ACTIVATION;
	
	@Column(name = "customerId")
	private  Integer  customerId;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public short getIsEffective() {
		return isEffective;
	}
	public void setIsEffective(short isEffective) {
		this.isEffective = isEffective;
	}
	public String getRange() {
		return range;
	}
	public void setRange(String range) {
		this.range = range;
	}
	public String getHallCodes() {
		return hallCodes;
	}
	public void setHallCodes(String hallCodes) {
		this.hallCodes = hallCodes;
	}
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	
}
