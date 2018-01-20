package com.flea.modules.system.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import com.flea.common.pojo.User;
/**
 * 
 * @ClassName: LibraryStop
 * @Description:图书馆停用
 * @author QL
 * @date 2017年4月26日 上午10:18:48
 */
@Entity
@Table(name="librarys_stop")
public class LibraryStop implements Serializable{
	/**
	 * @Fields serialVersionUID : 序列号
	 */ 
	private static final long serialVersionUID = 1L;
	
	@Id
	@GenericGenerator(name="generator",strategy="identity")
	@GeneratedValue(generator="generator")
	@Column(name ="id",unique=true,nullable= false)
	private Integer id;
	
	@Column(name ="stopReasion")
	private String stopReasion;
	
	@Column(name="noticeContent")
	private String noticeContent;
	
	@Column(name="startDate")
	private Date startDate;
	
	@Column(name="endDate")
	private Date endDate;
	
	@Column(name="dealAddress")
	private String dealAddress;
	
	@Column(name="conperson")
	private String conperson;
	
	@Column(name="telephone")
	private String telephone;
	
	@Column(name="phone")
	private String phone;
	
	
	@Column(name="userId")
	private Integer userId;
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStopReasion() {
		return stopReasion;
	}

	public void setStopReasion(String stopReasion) {
		this.stopReasion = stopReasion;
	}

	public String getNoticeContent() {
		return noticeContent;
	}

	public void setNoticeContent(String noticeContent) {
		this.noticeContent = noticeContent;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getDealAddress() {
		return dealAddress;
	}

	public void setDealAddress(String dealAddress) {
		this.dealAddress = dealAddress;
	}

	public String getConperson() {
		return conperson;
	}

	public void setConperson(String conperson) {
		this.conperson = conperson;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "LibraryStop [id=" + id + ", stopReasion=" + stopReasion
				+ ", noticeContent=" + noticeContent + ", startDate="
				+ startDate + ", endDate=" + endDate + ", dealAddress="
				+ dealAddress + ", conperson=" + conperson + ", telephone="
				+ telephone + ", phone=" + phone + ", userId=" + userId + "]";
	}

}
