package com.flea.modules.system.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import com.flea.common.Common;

@Entity
@Table(name="system_circulation_audit")
@DynamicInsert @DynamicUpdate
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class CirculationAudit implements Serializable{

	private static final long serialVersionUID = -2149410426380599207L;
	
	
	@Id
	@GenericGenerator(name="generator",strategy="identity")
	@GeneratedValue(generator="generator")
	@Column(name ="id",unique=true,nullable= false)
	private Integer id;
	
	@Column(name="action",length=2)
	private String action;
	
	@Column(name="targetId")
	private Integer targetId;
	
	@Column(name="target",length=80)
	private String target;
	
	@Column(name="targetContact",length=100)
	private String targetContact;
	
	@Column(name="targetUser",length=40)
	private String targetUser;
	
	@Column(name="targetScope",length=2)
	private Integer targetScope;
	
	/*0表示未审核,1表示审核通过,2已拒绝,3生效*/
	@Column(name="status",length=2)
	private String status = Common.FLAG_DISABLE;
	
	@Column(name="userId")
	private Integer userId;
	
	@Column(name="userName")
	private String userName;
	
	@Column(name="customerId")
	private Integer customerId;
	
	@Column(name="oneself",length=80)
	private String oneself;
	
	@Column(name="userContact",length=80)
	private String userContact;
	
	@Column(name="createDate")
	private Date createDate;
	
	@OneToMany(mappedBy="audit")
	private List<Circulation> circulations = new ArrayList<Circulation>();
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public Integer getTargetId() {
		return targetId;
	}
	public void setTargetId(Integer targetId) {
		this.targetId = targetId;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getTargetContact() {
		return targetContact;
	}
	public void setTargetContact(String targetContact) {
		this.targetContact = targetContact;
	}
	public String getTargetUser() {
		return targetUser;
	}
	public void setTargetUser(String targetUser) {
		this.targetUser = targetUser;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public  Circulation addCirculation(Circulation circulation) {
		getCirculations().add(circulation);
		circulation.setAudit(this);
		return circulation;
	}
	
	public List<Circulation> getCirculations() {
		return circulations;
	}
	public void setCirculations(List<Circulation> circulations) {
		this.circulations = circulations;
	}
	public Integer getTargetScope() {
		return targetScope;
	}
	public void setTargetScope(Integer targetScope) {
		this.targetScope = targetScope;
	}
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	public String getOneself() {
		return oneself;
	}
	public void setOneself(String oneself) {
		this.oneself = oneself;
	}
	public String getUserContact() {
		return userContact;
	}
	public void setUserContact(String userContact) {
		this.userContact = userContact;
	}
	
}
