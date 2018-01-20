package com.flea.modules.system.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.solr.client.solrj.beans.Field;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;

import com.flea.common.Common;
import com.flea.common.Constants;
import com.flea.common.pojo.Area;
import com.flea.common.pojo.City;
import com.flea.common.pojo.Province;
import com.flea.modules.customer.pojo.CustomerContact;
/**
 * 
 * @ClassName: LibraryUpdateStatus
 * @Description:图书馆用户新增权限中维护列表编辑的修改之后的状态
 * @author QL
 * @date 2017年5月8日 上午10:11:57
 */
@Entity
@Table(name="librarys_update_status")
@DynamicInsert @DynamicUpdate
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class LibrarysUpdateStatus implements Serializable{
	
	
	/**
	 * @Fields serialVersionUID : 生成序列号
	 */ 
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name="generator",strategy="identity")
	@GeneratedValue(generator="generator")
	@Column(name ="id",unique=true,nullable= false)
	private Integer id;
	
	//图书馆名称
	@Column(name="name")
	private String name;
	
	//联系人
	@Column(name="conperson")
	private String conperson;
	
	//手机号码
	@Column(name="phone")
	private String phone;
	
	//固定电话
	@Column(name="fixphone")
	private String fixphone;
	
	//邮箱
	@Column(name="email")
	private String email;
	
	//地址
	@Column(name="address")
	private String address;
	
	//经纬度
	@Column(name="lngLat")
	private String lngLat;
	
	//协议账户
	@Column(name="agreementAccount")
	private String agreementAccount; 
	
	//户名
	@Column(name="acountName")
	private String acountName;
	
	//授信额度
	@Column(name="creditLines")
	private double creditLines;
	
	//授信额度
	@Column(name="libraryId")
    private Integer libraryId;

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

	public String getConperson() {
		return conperson;
	}

	public void setConperson(String conperson) {
		this.conperson = conperson;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFixphone() {
		return fixphone;
	}

	public void setFixphone(String fixphone) {
		this.fixphone = fixphone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLngLat() {
		return lngLat;
	}

	public void setLngLat(String lngLat) {
		this.lngLat = lngLat;
	}

	public String getAgreementAccount() {
		return agreementAccount;
	}

	public void setAgreementAccount(String agreementAccount) {
		this.agreementAccount = agreementAccount;
	}

	public String getAcountName() {
		return acountName;
	}

	public void setAcountName(String acountName) {
		this.acountName = acountName;
	}

	public double getCreditLines() {
		return creditLines;
	}

	public void setCreditLines(double creditLines) {
		this.creditLines = creditLines;
	}

	public Integer getLibraryId() {
		return libraryId;
	}

	public void setLibraryId(Integer libraryId) {
		this.libraryId = libraryId;
	}
 
}
