/**  
* @Package com.flea.modules.report.pojo
* @Description: TODO
* @author bruce
* @date 2016年7月14日 上午10:46:29
* @version V1.0  
*/ 
package com.flea.modules.report.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * 借书统计
 * @author bruce
 * @2016年7月14日 上午10:46:29
 */
public class BorrowerReport implements Serializable{

	private static final long serialVersionUID = 1L;

	private String hallCode;
	
	private String name;
	
	private String category;
	
	private Date borrowDate;
	
	private String borrowNumber;
	
	private BigInteger totalIsbn;
	
	private BigInteger totalBook;
	
	private Double totalPrice;
	
	private String userName;
	
	private BigDecimal  percent;

	public String getHallCode() {
		return hallCode;
	}

	public void setHallCode(String hallCode) {
		this.hallCode = hallCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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
	
}
