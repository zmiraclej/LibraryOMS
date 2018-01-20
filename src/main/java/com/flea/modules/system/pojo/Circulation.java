package com.flea.modules.system.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import com.flea.common.Common;
import com.flea.common.Constants;

@Entity
@Table(name="system_circulation")
@DynamicInsert @DynamicUpdate
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Circulation implements Serializable{

	private static final long serialVersionUID = -2149410426380599207L;
	
	@Id
	@GenericGenerator(name="generator",strategy="identity")
	@GeneratedValue(generator="generator")
	@Column(name ="id",unique=true,nullable= false)
	private Integer id;
	
	@Column(name="hallCode")
	private String hallCode;
	
	/*1表示正常，0表示未启用，2停用*/
	@Column(length=2)
	private String status = Common.FLAG_ENABLE;

	@Column(name="customerId")
	private Integer customerId;
	
	@Column(name="scope")
	private Integer scope=0;
	
	@Transient
	private String scopeString;
	
	@Column(name="userId")
	private Integer userId;
	
	@Column(name="isEffective")
	private String isEffective =Constants.FLAG_DISABLE;
	
	@Column(name="createDate")
	private Date createDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="auditId")
	private CirculationAudit audit;
	
	public String getScopeString() {
		return scopeString;
	}
	public void setScopeString(String scopeString) {
		this.scopeString = scopeString;
	}
	
	public Integer getScope() {
		return scope;
	}
	public void setScope(Integer scope) {
		this.scope = scope;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getIsEffective() {
		return isEffective;
	}
	public void setIsEffective(String isEffective) {
		this.isEffective = isEffective;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	public String getHallCode() {
		return hallCode;
	}
	public void setHallCode(String hallCode) {
		this.hallCode = hallCode;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public CirculationAudit getAudit() {
		return audit;
	}
	public void setAudit(CirculationAudit audit) {
		this.audit = audit;
	}
	
}
