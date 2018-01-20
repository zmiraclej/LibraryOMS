package com.flea.modules.system.pojo;

import java.io.Serializable;
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

import org.hibernate.annotations.GenericGenerator;

import com.flea.common.pojo.Dictionary;

@Entity
@Table(name="library_circulate")
public class LibraryCirculateRel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3236589240778617813L;
	
	
	@Id
	@GenericGenerator(name="generator",strategy="identity")
	@GeneratedValue(generator="generator")
	@Column(name ="id",unique=true,nullable= false)
	private Integer id;
	
	@Column(name="outHallCode")
	private String outHallCode;
	
	
	@Column(name="inHallCode")
	private String inHallCode;
	
	@Column(name="outDate")
	private Date outDate;
	@Column(name="inCode")
	private String inCode;
	@Column(name="outCode")
	private String outCode;
	
	@Column(name="auditDate")
	private Date auditDate;
	@Column(name="auditPerson")
	private String auditPerson;
	
	@Column(name="isEffective",columnDefinition="String default 1")
	private String isEffective;
	
	@Column(name="delDate")
	private Date delDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "signPerson")
	private LibraryUser signPerson;
	@Column(name="signDate")
	private Date signDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "operUser")
	private LibraryUser operUser;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "outState")
	private Dictionary dictionaryOut;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "inState")
	private Dictionary dictionaryIn;

	@OneToMany(cascade= CascadeType.ALL,fetch = FetchType.LAZY,mappedBy ="libraryCirculate")
	private List<LibraryCirculateMap> libraryCirculateMap;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOutHallCode() {
		return outHallCode;
	}

	public void setOutHallCode(String outHallCode) {
		this.outHallCode = outHallCode;
	}

	public String getInHallCode() {
		return inHallCode;
	}

	public void setInHallCode(String inHallCode) {
		this.inHallCode = inHallCode;
	}

	public Date getOutDate() {
		return outDate;
	}

	public void setOutDate(Date outDate) {
		this.outDate = outDate;
	}


	public Date getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}

	public Date getDelDate() {
		return delDate;
	}

	public void setDelDate(Date delDate) {
		this.delDate = delDate;
	}

	public String getAuditPerson() {
		return auditPerson;
	}

	public void setAuditPerson(String auditPerson) {
		this.auditPerson = auditPerson;
	}

	public LibraryUser getSignPerson() {
		return signPerson;
	}

	public void setSignPerson(LibraryUser signPerson) {
		this.signPerson = signPerson;
	}

	public Date getSignDate() {
		return signDate;
	}

	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}

	

	public LibraryUser getOperUser() {
		return operUser;
	}

	public void setOperUser(LibraryUser operUser) {
		this.operUser = operUser;
	}

	public String getInCode() {
		return inCode;
	}

	public void setInCode(String inCode) {
		this.inCode = inCode;
	}

	public String getOutCode() {
		return outCode;
	}

	public void setOutCode(String outCode) {
		this.outCode = outCode;
	}

	public Dictionary getDictionaryOut() {
		return dictionaryOut;
	}

	public void setDictionaryOut(Dictionary dictionaryOut) {
		this.dictionaryOut = dictionaryOut;
	}

	public Dictionary getDictionaryIn() {
		return dictionaryIn;
	}

	public void setDictionaryIn(Dictionary dictionaryIn) {
		this.dictionaryIn = dictionaryIn;
	}

	public List<LibraryCirculateMap> getLibraryCirculateMap() {
		return libraryCirculateMap;
	}

	public void setLibraryCirculateMap(List<LibraryCirculateMap> libraryCirculateMap) {
		this.libraryCirculateMap = libraryCirculateMap;
	}

	public String getIsEffective() {
		return isEffective;
	}

	public void setIsEffective(String isEffective) {
		this.isEffective = isEffective;
	}
	
	
	
}
