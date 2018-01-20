/**  
* @Package com.flea.common.pojo
* @Description: TODO
* @author bruce
* @date 2016年1月7日 下午2:28:43
* @version V1.0  
*/ 
package com.flea.modules.system.pojo;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.flea.common.pojo.IdEntity;


/**
 * @author bruce
 * @2016年1月7日 下午2:28:43
 */

@Entity
@Table(name="system_provider")
public class Provider  extends IdEntity {
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "regionId")
	private Region region;		// 归属区域
	@Transient
	private String regionAssist;
	private String name; 	// 机构名称
	private String master; 	// 法人代表
	private String registCapital ;//注册资本
	private Date registDate; // 成立时间
	private String address; // 联系地址
	private String code; 	// 组织机构代码
	private String phote; 	// 营业执照
	private String zipCode; // 邮政编码
	private String phone; 	// 电话
	private String fax; 	// 传真
	private String email; 	// 邮箱
	
	public Region getRegion() {
		return region;
	}
	public void setRegion(Region region) {
		this.region = region;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMaster() {
		return master;
	}
	public void setMaster(String master) {
		this.master = master;
	}
	public String getRegistCapital() {
		return registCapital;
	}
	public void setRegistCapital(String registCapital) {
		this.registCapital = registCapital;
	}
	public Date getRegistDate() {
		return registDate;
	}
	public void setRegistDate(Date registDate) {
		this.registDate = registDate;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getPhote() {
		return phote;
	}
	public void setPhote(String phote) {
		this.phote = phote;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRegionAssist() {
		return regionAssist;
	}
	public void setRegionAssist(String regionAssist) {
		this.regionAssist = regionAssist;
	}
}
