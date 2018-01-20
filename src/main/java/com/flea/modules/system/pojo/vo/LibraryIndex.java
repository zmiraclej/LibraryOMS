package com.flea.modules.system.pojo.vo;

import java.util.Date;

import org.apache.solr.client.solrj.beans.Field;



public class LibraryIndex {

	@Field
	private Integer libId;
	@Field
	private String name;
	@Field
	private String hallCode;
	@Field
	private String conperson;
	@Field
	private String phone;
	@Field
	private String fixphone;
	@Field
	private String email;
	@Field
	private String address;
	@Field
	private String level;
	@Field
	private Date createTime;
	@Field
	private String isEffective;
	@Field
	private String cityCode;
	@Field
	private String provinceCode;
	@Field
	private String areaCode;
	@Field
	private String lngLat;
	@Field
	private String lightTime;
	@Field
	private String tel;
	@Field
	private String latlng;
	@Field
	private String remark;
	
	public Integer getLibId() {
		return libId;
	}
	public void setLibId(Integer libId) {
		this.libId = libId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHallCode() {
		return hallCode;
	}
	public void setHallCode(String hallCode) {
		this.hallCode = hallCode;
	}
	public String getConperson() {
		return conperson;
	}
	public void setConperson(String conperson) {
		this.conperson = conperson;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getFixphone() {
		return fixphone;
	}
	public void setFixphone(String fixphone) {
		this.fixphone = fixphone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getIsEffective() {
		return isEffective;
	}
	public void setIsEffective(String isEffective) {
		this.isEffective = isEffective;
	}
	
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public String getProvinceCode() {
		return provinceCode;
	}
	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getLngLat() {
		return lngLat;
	}
	public void setLngLat(String lngLat) {
		this.lngLat = lngLat;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getLatlng() {
		return latlng;
	}
	public void setLatlng(String latlng) {
		this.latlng = latlng;
	}
	public String getLightTime() {
		return lightTime;
	}
	public void setLightTime(String lightTime) {
		this.lightTime = lightTime;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	
}	
