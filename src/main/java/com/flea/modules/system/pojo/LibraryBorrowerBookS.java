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

import org.hibernate.annotations.GenericGenerator;

import com.flea.common.pojo.Dictionary;
@Entity
@Table(name="library_borrower_books")
public class LibraryBorrowerBookS implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -472608219113726229L;
	
	@Id
	@GenericGenerator(name="generator",strategy="identity")
	@GeneratedValue(generator="generator")
	@Column(name ="id",unique=true,nullable= false)
	private Integer id;
	@Column(name="idCard")
	private String idCard;
	@Column(name="name")
	private String name;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "libraryBookId")
	private LibraryBook libraryBook;
	@Column(name="borrowDate")
	private Date borrowDate;
	@Column(name="returnDate")
	private Date returnDate;
	@Column(name="borrowNumber")
	private String borrowNumber;
	@Column(name="returnNumber")
	private String returnNumber;
	
	@Column(name="borrowDays")
	private int borrowDays;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hallCode")
	private Library library;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "operUser")
	private LibraryUser libraryUser;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "borrowState")
	private Dictionary dictionary;
	
	@Column(name="barNumber")
	private String barNumber;
	@Column(name="isPraiseD")
	private String isPraiseD;
	
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
	public LibraryBook getLibraryBook() {
		return libraryBook;
	}
	public void setLibraryBook(LibraryBook libraryBook) {
		this.libraryBook = libraryBook;
	}
	public Date getBorrowDate() {
		return borrowDate;
	}
	public void setBorrowDate(Date borrowDate) {
		this.borrowDate = borrowDate;
	}
	public String getBorrowNumber() {
		return borrowNumber;
	}
	public void setBorrowNumber(String borrowNumber) {
		this.borrowNumber = borrowNumber;
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
	public String getBarNumber() {
		return barNumber;
	}
	public void setBarNumber(String barNumber) {
		this.barNumber = barNumber;
	}
	public Date getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}
	public int getBorrowDays() {
		return borrowDays;
	}
	public void setBorrowDays(int borrowDays) {
		this.borrowDays = borrowDays;
	}
	public LibraryUser getLibraryUser() {
		return libraryUser;
	}
	public void setLibraryUser(LibraryUser libraryUser) {
		this.libraryUser = libraryUser;
	}
	public String getIsPraiseD() {
		return isPraiseD;
	}
	public void setIsPraiseD(String isPraiseD) {
		this.isPraiseD = isPraiseD;
	}
	public String getReturnNumber() {
		return returnNumber;
	}
	public void setReturnNumber(String returnNumber) {
		this.returnNumber = returnNumber;
	}
	
	
	
}
