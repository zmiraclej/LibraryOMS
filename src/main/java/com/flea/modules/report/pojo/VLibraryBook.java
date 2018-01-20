package com.flea.modules.report.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;


/**
 * 藏书统计视图
 * @author bruce
 *
 */
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class VLibraryBook implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4390911312699624203L;
	
	private String stayLibraryHallCode;

	private String libName;
	
	private String  category;
	
	private BigInteger totalIsbn;
	//藏书统计总册数
	private BigInteger totalBook;
	//藏书统计总册数-所属
	private BigInteger totalBookBelong;
	//藏书统计总价
	private Double totalPrice;
	//藏书统计总价所属
	private Double totalPriceBelong;
	
	private BigInteger totalIn;
	
	private BigDecimal  percent;
	
	

	public BigInteger getTotalBookBelong() {
		return totalBookBelong;
	}

	public void setTotalBookBelong(BigInteger totalBookBelong) {
		this.totalBookBelong = totalBookBelong;
	}

	public Double getTotalPriceBelong() {
		return totalPriceBelong;
	}

	public void setTotalPriceBelong(Double totalPriceBelong) {
		this.totalPriceBelong = totalPriceBelong;
	}

	public String getStayLibraryHallCode() {
		return stayLibraryHallCode;
	}

	public void setStayLibraryHallCode(String stayLibraryHallCode) {
		this.stayLibraryHallCode = stayLibraryHallCode;
	}

	public String getLibName() {
		return libName;
	}

	public void setLibName(String libName) {
		this.libName = libName;
	}

	public BigInteger getTotalIsbn() {
		return totalIsbn;
	}

	public void setTotalIsbn(BigInteger totalIsbn) {
		this.totalIsbn = totalIsbn;
	}

	public BigInteger getTotalBook() {
		return totalBook;
	}

	public void setTotalBook(BigInteger totalBook) {
		this.totalBook = totalBook;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public BigInteger getTotalIn() {
		return totalIn;
	}

	public void setTotalIn(BigInteger totalIn) {
		this.totalIn = totalIn;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public BigDecimal getPercent() {
		return percent;
	}

	public void setPercent(BigDecimal percent) {
		this.percent = percent;
	}
	
}
