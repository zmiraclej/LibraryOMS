package com.flea.common.pojo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.flea.modules.customer.pojo.Customer;
import com.flea.modules.system.pojo.Library;


@Entity
@Table(name="system_province")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Province implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 523860002307257203L;
	
	@Column(name ="id",unique=true,nullable= false)
	private Integer id;
	@Id
	private String code;
	@Column(name="name",length=30)
	private String name;
	
	@OneToMany(cascade= CascadeType.ALL,fetch = FetchType.LAZY,mappedBy ="province")
	private Set<City> city = new HashSet<City>(0);
	
//	@OneToMany(cascade= CascadeType.ALL,fetch = FetchType.LAZY,mappedBy ="province")
//	private Set<Customer> customer = new HashSet<Customer>(0);
	
	@OneToMany(cascade= CascadeType.ALL,fetch = FetchType.LAZY,mappedBy ="province")
	private Set<Library> library = new HashSet<Library>(0);
	
	@Column(name="codeCN",length=30)
	private String codeCN;
	
	
	public String getCodeCN() {
		return codeCN;
	}
	public void setCodeCN(String codeCN) {
		this.codeCN = codeCN;
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
	public Set<City> getCity() {
		return city;
	}
	public void setCity(Set<City> city) {
		this.city = city;
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
