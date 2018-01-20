package com.flea.modules.report.pojo.vo;

import java.io.Serializable;

public class QueryCriteria implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2386705131402752732L;
	// 地区
	private String area;
	// 馆别
	private String lib;
	// 馆号
	private String hallCode;
	// 条件
	private String option;
	// 从多久开始
	private String dateFrom;
	// 到多久结束
	private String dateTo;
	// 条件开始
	private String searchFrom;
	// 条件结束
	private String searchTo;
	// 关键字
	private String keyWord;

	private Integer type;

	private Integer customerId;

	private String province;

	private String city;

	private String district;

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

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	public String getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(String dateFrom) {
		this.dateFrom = dateFrom;
	}

	public String getDateTo() {
		return dateTo;
	}

	public void setDateTo(String dateTo) {
		this.dateTo = dateTo;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getSearchFrom() {
		return searchFrom;
	}

	public void setSearchFrom(String searchFrom) {
		this.searchFrom = searchFrom;
	}

	public String getSearchTo() {
		return searchTo;
	}

	public void setSearchTo(String searchTo) {
		this.searchTo = searchTo;
	}

	public String getHallCode() {
		return hallCode;
	}

	public void setHallCode(String hallCode) {
		this.hallCode = hallCode;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public boolean equals(QueryCriteria qc) {

		if (this.area.equals(qc.getArea()) 
				&& ( ( null == this.lib && null == qc.getLib()) ||  this.lib.equals(qc.getLib()) )
				&& ( ( null == this.hallCode && null == qc.getHallCode()) || this.hallCode.equals(qc.getHallCode()) )
				&& ( ( null == this.option && null == qc.getOption()) || this.option.equals(qc.getOption()))
				&& ( ( null == this.dateFrom && null == qc.getDateFrom()) || this.dateFrom.equals(qc.getDateFrom()) )
				&& ( ( null == this.dateTo && null == qc.getDateTo()) || this.dateTo.equals(qc.getDateTo()) )
				&& ( ( null == this.searchFrom && null == qc.getSearchFrom()) || this.searchFrom.equals(qc.getSearchFrom()) ) 
				&& ( ( null == this.searchTo && null == qc.getSearchTo()) || this.searchTo.equals(qc.getSearchTo()) )
				&& ( ( null == this.keyWord && null == qc.getKeyWord()) || this.keyWord.equals(qc.getKeyWord()) )
				&& ( ( null == this.type && null == qc.getType()) || this.type.equals(qc.getType()) )
				&& ( ( null == this.customerId && null == qc.getCustomerId()) || this.customerId.equals(qc.getCustomerId()) )
				&& ( ( null == this.province && null == qc.getProvince()) || this.province.equals(qc.getProvince())) 
				&& ( ( null == this.city && null == qc.getCity()) ||  this.city.equals(qc.getCity()) )
				&& ( ( null == this.district && null == qc.getDistrict()) || this.district.equals(qc.getDistrict()) )
				) {
			return true;
		} else {
			return false;
		}

	}
}
