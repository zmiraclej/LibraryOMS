package com.flea.modules.report.pojo;

import java.io.Serializable;
import java.util.Date;

public class LightReport implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3677805382389314949L;

	//地区
	private String area;
	//馆别
	private String lib;
	//馆号
	private String hallCode;
	//开始时间
	private Date dateBegin;
	//结束时间
	private Date dateEnd;
	
	//馆名
	private String name;
	//负责人
	private String operName;
	//负责人电话
	private String phone;
	//合计天数
	private String days;
	//馆总时间
	private Double totalTime;
	
//	开放天数
	private String sumNumber;
	//所有总时间
	private String sumTotalTime;
	
	private Integer totalDays;
	
	
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getLib() {
		return lib;
	}
	public void setLib(String lib) {
		this.lib = lib;
	}
	public String getHallCode() {
		return hallCode;
	}
	public void setHallCode(String hallCode) {
		this.hallCode = hallCode;
	}
	public Date getDateBegin() {
		return dateBegin;
	}
	public void setDateBegin(Date dateBegin) {
		this.dateBegin = dateBegin;
	}
	public Date getDateEnd() {
		return dateEnd;
	}
	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOperName() {
		return operName;
	}
	public void setOperName(String operName) {
		this.operName = operName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getDays() {
		return days;
	}
	public void setDays(String days) {
		this.days = days;
	}
	public Double getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(Double totalTime) {
		this.totalTime = totalTime;
	}
	public String getSumNumber() {
		return sumNumber;
	}
	public void setSumNumber(String sumNumber) {
		this.sumNumber = sumNumber;
	}
	public String getSumTotalTime() {
		return sumTotalTime;
	}
	public void setSumTotalTime(String sumTotalTime) {
		this.sumTotalTime = sumTotalTime;
	}
	public Integer getTotalDays() {
		return totalDays;
	}
	public void setTotalDays(Integer totalDays) {
		this.totalDays = totalDays;
	}
	
	
	
	
}
