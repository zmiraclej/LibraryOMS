package com.flea.modules.report.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 藏书、编目统计视图
 * @author wq
 *
 */
public class ReaderReport implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4979435552981022398L;
//	馆号
	private String hallCode;
//	姓名
	private String cardName;
//	身份证号
	private String idCard;
//	日期
	private Date createDate;

	//平台借书总数
	private Integer totalBorrowSum;
	//客户查询借书总数
	private BigDecimal totalSum;

	//	押金
	private Double deposit;
//	集合总数
	private String sumNumber;
	public String getHallCode() {
		return hallCode;
	}
	public void setHallCode(String hallCode) {
		this.hallCode = hallCode;
	}
	public String getCardName() {
		return cardName;
	}
	public void setCardName(String cardName) {
		this.cardName = cardName;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public Integer getTotalBorrowSum() {
		return totalBorrowSum;
	}
	public void setTotalBorrowSum(Integer totalBorrowSum) {
		this.totalBorrowSum = totalBorrowSum;
	}
	public Double getDeposit() {
		return deposit;
	}
	public void setDeposit(Double deposit) {
		this.deposit = deposit;
	}
	public String getSumNumber() {
		return sumNumber;
	}
	public void setSumNumber(String sumNumber) {
		this.sumNumber = sumNumber;
	}
	public BigDecimal getTotalSum() {
		return totalSum;
	}
	public void setTotalSum(BigDecimal totalSum) {
		this.totalSum = totalSum;
	}

	
	
}
