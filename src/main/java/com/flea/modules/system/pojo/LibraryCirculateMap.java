package com.flea.modules.system.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="library_circulate_map")
public class LibraryCirculateMap implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 716811832354554650L;
	
	
	@Id
	@GenericGenerator(name="generator",strategy="identity")
	@GeneratedValue(generator="generator")
	@Column(name ="id",unique=true,nullable= false)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "circulateId")
	private LibraryCirculateRel libraryCirculate;
	
	@Column(name ="libraryBookId")
	private Integer libraryBookId;
	@Column(name ="price")
	private double price;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public LibraryCirculateRel getLibraryCirculate() {
		return libraryCirculate;
	}
	public void setLibraryCirculate(LibraryCirculateRel libraryCirculate) {
		this.libraryCirculate = libraryCirculate;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public Integer getLibraryBookId() {
		return libraryBookId;
	}
	public void setLibraryBookId(Integer libraryBookId) {
		this.libraryBookId = libraryBookId;
	}
	
	
}
