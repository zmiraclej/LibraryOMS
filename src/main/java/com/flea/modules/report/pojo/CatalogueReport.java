package com.flea.modules.report.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;


/**
 * 藏书、编目统计视图
 * @author wq
 *
 */
public class CatalogueReport implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4979435552981022398L;
//	馆号
	private String hallCode;
//	官名
	private String name;
//	日期
	private Date catalogueDate;
//	单号
	private String catalogueCode;
//	册数
	private BigInteger count;
	//	码洋
	private double price;
//	操作员
	private String userName;
//	集合总数
	private String sumNumber;
//	身份证号
	private String idCard;
	
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getSumNumber() {
		return sumNumber;
	}
	public void setSumNumber(String sumNumber) {
		this.sumNumber = sumNumber;
	}
	public Date getCatalogueDate() {
		return catalogueDate;
	}
	public void setCatalogueDate(Date catalogueDate) {
		this.catalogueDate = catalogueDate;
	}
	public String getCatalogueCode() {
		return catalogueCode;
	}
	public void setCatalogueCode(String catalogueCode) {
		this.catalogueCode = catalogueCode;
	}
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
	public double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		if (price == null) {
			price = (double) 0;
		}
		this.price = price;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public BigInteger getCount() {
		return count;
	}
	public void setCount(BigInteger count) {
		this.count = count;
	}
	@Override
	public String toString() {
		return "CatalogueReport [hallCode=" + hallCode + ", name=" + name + ", catalogueDate=" + catalogueDate
				+ ", catalogueCode=" + catalogueCode + ", count=" + count + ", price=" + price + ", userName="
				+ userName + ", sumNumber=" + sumNumber + "]";
	}
	
}
