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

import com.flea.modules.customer.pojo.Customer;
import com.flea.modules.system.pojo.Library;

@Entity
@Table(name = "system_area")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Area implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1826971558821645937L;

	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	@Id
	private String code;
	@Column(name = "name", length = 30)
	private String name;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cityCode")
	private City city;

//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "area")
//	private Set<Customer> customer = new HashSet<Customer>(0);

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "area")
	private Set<Library> library = new HashSet<Library>(0);

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

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

//	public Set<Customer> getCustomer() {
//		return customer;
//	}
//
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
