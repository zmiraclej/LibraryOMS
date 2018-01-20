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

@Entity
@Table(name="library_compensate_record_map")
public class LibraryCompensateRecordMap implements Serializable{
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7039176169533371311L;
	
	@Id
	@GenericGenerator(name="generator",strategy="identity")
	@GeneratedValue(generator="generator")
	@Column(name ="id",unique=true,nullable= false)
	private Integer id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "recordId")
	private LibraryCompensateRecord libraryCompensateRecord; 
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "libraryBookId")
	private LibraryBook libraryBook;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hallCode")
	private Library library;
	@Column(name="barNumber")
	private String barNumber;
	public LibraryCompensateRecord getLibraryCompensateRecord() {
		return libraryCompensateRecord;
	}
	public void setLibraryCompensateRecord(
			LibraryCompensateRecord libraryCompensateRecord) {
		this.libraryCompensateRecord = libraryCompensateRecord;
	}
	public LibraryBook getLibraryBook() {
		return libraryBook;
	}
	public void setLibraryBook(LibraryBook libraryBook) {
		this.libraryBook = libraryBook;
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
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
}
