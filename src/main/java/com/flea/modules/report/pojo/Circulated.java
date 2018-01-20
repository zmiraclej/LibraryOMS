package com.flea.modules.report.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class Circulated implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6464861553437559129L;
	
	private String hallCode;
	private String hallCodeT;
	private String name;
	private Date outDate;
	private BigInteger nowOut;
	private BigInteger nowIn;
	private BigDecimal nowOutMoney;
	private BigDecimal nowInMoney;
	
	private BigInteger totalNowOut;
	private BigInteger totalNowin;
	private BigDecimal totalOutMoney;
	private BigDecimal totalNowInMoney;
	
	private BigDecimal reportNowOut;
	private BigDecimal reportNowIn;
	private BigDecimal reportOutMoney;
	private BigDecimal reportNowInMoney;
	
	private BigDecimal reportOldOut;
	private BigDecimal reportOldIn;
	private BigDecimal reportOldMoney;
	private BigDecimal reportOldInMoney;
	
	
	public String getHallCodeT() {
		return hallCodeT;
	}
	public void setHallCodeT(String hallCodeT) {
		this.hallCodeT = hallCodeT;
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
	public BigInteger getNowOut() {
		return nowOut;
	}
	public void setNowOut(BigInteger nowOut) {
		if(nowOut == null){
			this.nowOut = BigInteger.valueOf(0);
		}else
		this.nowOut = nowOut;
	}
	public BigInteger getNowIn() {
		return nowIn;
	}
	public void setNowIn(BigInteger nowIn) {
		if(nowIn == null){
			this.nowIn = BigInteger.valueOf(0);
		}else{
			this.nowIn = nowIn;
		}
	}
	public BigInteger getTotalNowOut() {
		return totalNowOut;
	}
	public void setTotalNowOut(BigInteger totalNowOut) {
		if(totalNowOut == null){
			this.totalNowOut = BigInteger.valueOf(0);
		}else
		this.totalNowOut = totalNowOut;
	}
	public BigInteger getTotalNowin() {
		return totalNowin;
	}
	public void setTotalNowin(BigInteger totalNowin) {
		if(totalNowin == null){
			this.totalNowin = BigInteger.valueOf(0);
		}else
		this.totalNowin = totalNowin;
	}
	public BigDecimal getReportNowOut() {
		return reportNowOut;
	}
	public void setReportNowOut(BigDecimal reportNowOut) {
		if(reportNowOut == null){
			this.reportNowOut = BigDecimal.valueOf(0);
		}else
		this.reportNowOut = reportNowOut;
	}
	public BigDecimal getReportNowIn() {
		return reportNowIn;
	}
	public void setReportNowIn(BigDecimal reportNowIn) {
		if(reportNowIn == null){
			this.reportNowIn = BigDecimal.valueOf(0);
		}else
		this.reportNowIn = reportNowIn;
	}
	public BigDecimal getReportOldOut() {
		return reportOldOut;
	}
	public void setReportOldOut(BigDecimal reportOldOut) {
		if(reportOldOut == null){
			this.reportOldOut = BigDecimal.valueOf(0);
		}else
		this.reportOldOut = reportOldOut;
	}
	public BigDecimal getReportOldIn() {
		return reportOldIn;
	}
	public void setReportOldIn(BigDecimal reportOldIn) {
		if(reportOldIn == null){
			this.reportOldIn = BigDecimal.valueOf(0);
		}else
		this.reportOldIn = reportOldIn;
	}
	
	public BigDecimal getReportOutMoney() {
		return reportOutMoney;
	}
	public void setReportOutMoney(BigDecimal reportOutMoney) {
		if(reportOutMoney == null){
			this.reportOutMoney = BigDecimal.valueOf(0);
		}else
		this.reportOutMoney = reportOutMoney;
	}
	public BigDecimal getReportNowInMoney() {
		return reportNowInMoney;
	}
	public void setReportNowInMoney(BigDecimal reportNowInMoney) {
		if(reportNowInMoney == null){
			this.reportNowInMoney = BigDecimal.valueOf(0);
		}else
		this.reportNowInMoney = reportNowInMoney;
	}
	public BigDecimal getReportOldMoney() {
		return reportOldMoney;
	}
	public void setReportOldMoney(BigDecimal reportOldMoney) {
		if(reportOldMoney == null){
			this.reportOldMoney = BigDecimal.valueOf(0);
		}else
		this.reportOldMoney = reportOldMoney;
	}
	public BigDecimal getReportOldInMoney() {
		return reportOldInMoney;
	}
	public void setReportOldInMoney(BigDecimal reportOldInMoney) {
		if(reportOldInMoney == null){
			this.reportOldInMoney = BigDecimal.valueOf(0);
		}else
		this.reportOldInMoney = reportOldInMoney;
	}
	
	public BigDecimal getNowOutMoney() {
		return nowOutMoney;
	}
	public void setNowOutMoney(BigDecimal nowOutMoney) {
		if(nowOutMoney == null){
			this.nowOutMoney = BigDecimal.valueOf(0);
		}else
		this.nowOutMoney = nowOutMoney;
	}
	public BigDecimal getNowInMoney() {
		return nowInMoney;
	}
	public void setNowInMoney(BigDecimal nowInMoney) {
		if(nowInMoney == null){
			this.nowInMoney = BigDecimal.valueOf(0);
		}else
		this.nowInMoney = nowInMoney;
	}
	public BigDecimal getTotalOutMoney() {
		return totalOutMoney;
	}
	public void setTotalOutMoney(BigDecimal totalOutMoney) {
		if(totalOutMoney == null){
			this.totalOutMoney = BigDecimal.valueOf(0);
		}else
		this.totalOutMoney = totalOutMoney;
	}
	public BigDecimal getTotalNowInMoney() {
		return totalNowInMoney;
	}
	public void setTotalNowInMoney(BigDecimal totalNowInMoney) {
		if(totalNowInMoney == null){
			this.totalNowInMoney = BigDecimal.valueOf(0);
		}else
		this.totalNowInMoney = totalNowInMoney;
	}
	public Date getOutDate() {
		return outDate;
	}
	public void setOutDate(Date outDate) {
		this.outDate = outDate;
	}
	@Override
	public String toString() {
		return "Circulated [hallCode=" + hallCode + ", name=" + name + ", nowOut=" + nowOut + ", nowIn=" + nowIn
				+ ", nowOutMoney=" + nowOutMoney + ", nowInMoney=" + nowInMoney + ", totalNowOut=" + totalNowOut
				+ ", totalNowin=" + totalNowin + ", totalOutMoney=" + totalOutMoney + ", totalNowInMoney="
				+ totalNowInMoney + ", reportNowOut=" + reportNowOut + ", reportNowIn=" + reportNowIn
				+ ", reportOutMoney=" + reportOutMoney + ", reportNowInMoney=" + reportNowInMoney + ", reportOldOut="
				+ reportOldOut + ", reportOldIn=" + reportOldIn + ", reportOldMoney=" + reportOldMoney
				+ ", reportOldInMoney=" + reportOldInMoney + "]";
	}
	
}
