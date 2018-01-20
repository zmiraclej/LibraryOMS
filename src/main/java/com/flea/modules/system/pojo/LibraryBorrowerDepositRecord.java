package com.flea.modules.system.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.flea.common.pojo.Dictionary;
@Entity
@Table(name="library_borrower_deposit_record")
public class LibraryBorrowerDepositRecord implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4398932839283644638L;
	
	
	@Id
	@GenericGenerator(name="generator",strategy="identity")
	@GeneratedValue(generator="generator")
	@Column(name ="id",unique=true,nullable= false)
	private Integer id;
	@Column(name="idCard")
	private String idCard;
	@Column(name="name")
	private String name;
	@Column(name="deposit")
	private double deposit;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hallCode")
	private Library library;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "operUser")
	private LibraryUser libraryUser;
	
	@Column(name="operDate")
	private Date operDate;
	
	@Column(name="operCode")
	private String operCode;
	
	private int bookSum;
	
	private double priceSum;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "operation")
	private Dictionary dictionary;
	
	@Transient
	private Integer status;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getDeposit() {
		return deposit;
	}
	public void setDeposit(double deposit) {
		this.deposit = deposit;
	}

	public Date getOperDate() {
		return operDate;
	}
	public void setOperDate(Date operDate) {
		this.operDate = operDate;
	}
	public String getOperCode() {
		return operCode;
	}
	public void setOperCode(String operCode) {
		this.operCode = operCode;
	}
	public Dictionary getDictionary() {
		return dictionary;
	}
	public void setDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}
	public Library getLibrary() {
		return library;
	}
	public void setLibrary(Library library) {
		this.library = library;
	}
	public LibraryUser getLibraryUser() {
		return libraryUser;
	}
	public void setLibraryUser(LibraryUser libraryUser) {
		this.libraryUser = libraryUser;
	}
	public int getBookSum() {
		return bookSum;
	}
	public void setBookSum(int bookSum) {
		this.bookSum = bookSum;
	}
	public double getPriceSum() {
		return priceSum;
	}
	public void setPriceSum(double priceSum) {
		this.priceSum = priceSum;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
}
