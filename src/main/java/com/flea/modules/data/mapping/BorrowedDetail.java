package com.flea.modules.data.mapping;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BorrowedDetail {
	private Integer id;
	private BigDecimal borrowtype;
	private String  idCard;
	private String  name;
	private Date  borrowDate;
	private Date  returnDate;
	private Integer  operUser;
	private Integer  returnUser;
	private String  barNumber;
	private String  properTitle;
	private String  outname;
	private String  inname;
	private Date  theoryReturnDate;
	private Integer reborrownum = 0;
	
	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public BigDecimal getBorrowtype() {
		return borrowtype;
	}


	public void setBorrowtype(BigDecimal borrowtype) {
		this.borrowtype = borrowtype;
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


	public String getBorrowDate() {
		if (borrowDate!=null) {
			return new SimpleDateFormat("yyyy-MM-dd").format(borrowDate);
		}
		return "";
	}


	public void setBorrowDate(Date borrowDate) {
		this.borrowDate = borrowDate;
	}


	public String getReturnDate() {
		if (returnDate!=null) {
			return new SimpleDateFormat("yyyy-MM-dd").format(returnDate);
		}
		return "";
	}


	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}


	public Integer getOperUser() {
		return operUser;
	}


	public void setOperUser(Integer operUser) {
		this.operUser = operUser;
	}


	public Integer getReturnUser() {
		return returnUser;
	}


	public void setReturnUser(Integer returnUser) {
		this.returnUser = returnUser;
	}


	public String getBarNumber() {
		return barNumber;
	}


	public void setBarNumber(String barNumber) {
		this.barNumber = barNumber;
	}

	public String getProperTitle() {
		if (properTitle!=null) {
			return properTitle.replaceAll(":", "\\:").replaceAll("'", "\\'");
		}
		return null;
	}


	public void setProperTitle(String properTitle) {
		this.properTitle = properTitle;
	}


	public String getOutname() {
		return outname;
	}


	public void setOutname(String outname) {
		this.outname = outname;
	}


	public String getInname() {
		return inname;
	}


	public void setInname(String inname) {
		this.inname = inname;
	}


	public String getTheoryReturnDate() {
		if (theoryReturnDate!=null) {
			return new SimpleDateFormat("yyyy-MM-dd").format(theoryReturnDate);
		}
		return "";
	}


	public void setTheoryReturnDate(Date theoryReturnDate) {
		this.theoryReturnDate = theoryReturnDate;
	}


	public Integer getReborrownum() {
		return reborrownum;
	}


	public void setReborrownum(Integer reborrownum) {
		this.reborrownum = reborrownum;
	}


	@Override
	public String toString() {
		return "BorrowedDetail [BorrowID=" + id + ", borrowtype=" + borrowtype
				+ ", readercode=" + idCard + ", readername=" + name + ", borrowDate="
				+ borrowDate + ", realreturndate=" + returnDate + ", bstaffcode="
				+ operUser + ", rstaffcode=" + returnUser + ", barcode="
				+ barNumber + ", booktitle=" + properTitle + ", bbpname="
				+ outname + ", brpname=" + inname + ", returndate="
				+ theoryReturnDate + ", reborrownum=" + reborrownum + "]";
	}

	
}
