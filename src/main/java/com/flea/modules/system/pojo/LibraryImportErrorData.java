package com.flea.modules.system.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.flea.common.pojo.User;
import com.flea.modules.system.pojo.LibraryUser;


@Entity
@Table(name="library_import_temp_errordata")
public class LibraryImportErrorData implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1918483515913688594L;

	@Id
	@GenericGenerator(name="generator", strategy="identity")
	@GeneratedValue(generator="generator")
	@Column(name ="id", unique=true, nullable= false)
	private Integer id;
	
	@Column(name ="name")
	private String name;
	
	@Column(name="address")
	private String address;
	
	@Column(name="agreementAccount")
	private String agreementAccount;//协议账户
	
	@Column(name="acountName")
	private String acountName;//户名
	
	@Column(name="creditLines")
	private double creditLines;//授信额度
	
	@Column(name="conperson")
	private String conperson;//联系人
	
	@Column(name="tel")
	private String tel;//联系电话
	
	@Column(name="phone")
	private String phone;//联系手机
	
	@Column(name="email")
	private String email;//邮箱
	
	@Column(name ="msg")
	private String msg;
	
	@ManyToOne
	@JoinColumn(name ="systemUser")
	private User user;

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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getConperson() {
		return conperson;
	}

	public void setConperson(String conperson) {
		this.conperson = conperson;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	
	
	
}
