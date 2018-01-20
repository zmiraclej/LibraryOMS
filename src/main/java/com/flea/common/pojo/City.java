package com.flea.common.pojo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.flea.modules.system.pojo.Library;
@Entity
@Table(name="system_city")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class City implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4960908833830767L;
	@Column(name ="id",unique=true,nullable= false)
	private Integer id;
	@Id
	private String code;
	@Column(name="name",length=30)
	private String name;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "provinceCode")
	private Province province;
	
	@OneToMany(cascade= CascadeType.ALL,fetch = FetchType.LAZY,mappedBy ="city")
	private Set<Area> area = new HashSet<Area>(0);
	
	
//	@OneToMany(cascade= CascadeType.ALL,fetch = FetchType.LAZY,mappedBy ="city")
//	private Set<Customer> customer = new HashSet<Customer>(0);
//	
	@OneToMany(cascade= CascadeType.ALL,fetch = FetchType.LAZY,mappedBy ="city")
	private Set<Library> library = new HashSet<Library>(0);
	
	@Column(name="cityCode",length=30)
	private String cityCode;
	
	
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Province getProvince() {
		return province;
	}
	public void setProvince(Province province) {
		this.province = province;
	}
	public Set<Area> getArea() {
		return area;
	}
	public void setArea(Set<Area> area) {
		this.area = area;
	}
//	public Set<Customer> getCustomer() {
//		return customer;
//	}
//	public void setCustomer(Set<Customer> customer) {
//		this.customer = customer;
//	}
	public Set<Library> getLibrary() {
		return library;
	}
	public void setLibrary(Set<Library> library) {
		this.library = library;
	}
	
}
