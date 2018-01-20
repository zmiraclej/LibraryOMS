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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="library_compensate_record")
public class LibraryCompensateRecord implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7028410040557473746L;

	
	@Id
	@GenericGenerator(name="generator",strategy="identity")
	@GeneratedValue(generator="generator")
	@Column(name ="id",unique=true,nullable= false)
	private Integer id;
	
	@OneToMany(cascade= CascadeType.ALL,fetch = FetchType.LAZY,mappedBy ="libraryCompensateRecord")
	private List<LibraryCompensateRecordMap> libraryCompensateRecordMap;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hallCode")
	private Library library;
	@Column(name="moneySum")
	private double moneySum;
	@Column(name="compensateDate")
	private Date compensateDate;
	@Column(name="idCard")
	private String idCard;
	@Column(name="compensateName")
	private String compensateName;
	@Column(name="returnCode")
	private String returnCode;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "operUser")
	private LibraryUser libraryUser;
	
	
	public List<LibraryCompensateRecordMap> getLibraryCompensateRecordMap() {
		return libraryCompensateRecordMap;
	}
	public void setLibraryCompensateRecordMap(
			List<LibraryCompensateRecordMap> libraryCompensateRecordMap) {
		this.libraryCompensateRecordMap = libraryCompensateRecordMap;
	}
	public Library getLibrary() {
		return library;
	}
	public void setLibrary(Library library) {
		this.library = library;
	}
	public double getMoneySum() {
		return moneySum;
	}
	public void setMoneySum(double moneySum) {
		this.moneySum = moneySum;
	}
	public Date getCompensateDate() {
		return compensateDate;
	}
	public void setCompensateDate(Date compensateDate) {
		this.compensateDate = compensateDate;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getCompensateName() {
		return compensateName;
	}
	public void setCompensateName(String compensateName) {
		this.compensateName = compensateName;
	}
	public LibraryUser getLibraryUser() {
		return libraryUser;
	}
	public void setLibraryUser(LibraryUser libraryUser) {
		this.libraryUser = libraryUser;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	
}
