package com.flea.modules.order.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import com.flea.common.Common;
import com.flea.common.Constants;
import com.flea.common.pojo.User;
import com.flea.modules.customer.pojo.CustomerContact;
import com.flea.modules.customer.pojo.CutomerLibrary;
/**
 * @author Wuhua
 * @2016年4月15日 下午3:47:44
 * 电子书配发客户
 */
@Entity
@Table(name="system_customer")
public class CustomerAllot implements Serializable{

	private static final long serialVersionUID = -2149410422380599207L;
	
	@Id
	@GenericGenerator(name="generator",strategy="identity")
	@GeneratedValue(generator="generator")
	@Column(name ="id",unique=true,nullable= false)
	private Integer id;
	
	@Column(name="name")
	private String name;
	@Column(name="customerCode")
	private String customerCode;
	@Column(name="address")
	private String address;
	@Column(name="postcode")
	private String postcode;
	
	@Column(name="phone")
	private String phone;
	
	@Column(name="createTime")
	private Date createTime;
	
	/*押金*/
	@Column(name="deposit")
	private  int deposit=0;
	
	/*协议*/
	@Column(name="agreement")
	private int agreement=3;
	
	/*最小额度*/
	@Column(name="minQuota")
	private double minQuota;
	
	/*最大额度*/
	@Column(name="maxQuota")
	private double maxQuota;
	
	@Transient
	private String quota;
	
	/*信用额度*/
	@Column(name="creditQuota")
	private double creditQuota;
	
	
	/*读者限制*/
	@Column(name="readerLimit")
	private int readerLimit;
	
	/*限借数量*/
	@Column(name="borrowNum")
	private int borrowNum;
	
	/*限借天数*/
	@Column(name="borrowDays")
	private int borrowDays;
	
	/*逾期罚金*/
	@Column(name="rent")
	private double rent;
	
	@Column(name="isEffective")
	private short isEffective =Constants.FLAG_ACTIVATION;
	/*联系人*/
	@OneToMany(cascade= CascadeType.ALL,fetch = FetchType.LAZY,mappedBy ="customer")
	private List<CustomerContact> contacts  = new ArrayList<CustomerContact>();
	/*管理员*/
	@OneToMany(cascade=CascadeType.ALL,fetch = FetchType.LAZY,mappedBy ="customer")
	@Where(clause="isEffective="+Common.FLAG_ENABLE)
	private List<User> users  = new ArrayList<User>();
	/*分配馆号*/
	@OneToMany(cascade= CascadeType.ALL,fetch = FetchType.LAZY,mappedBy ="customer")
	private List<CutomerLibrary> libras  = new ArrayList<CutomerLibrary>();
	
	@Column(name="hallCode")
	private String hallCode;
	
	@Transient
	private boolean sendpwd;
	
	@Column(name="modifyUser",length =30)
	private String modifyUser;
	
	@Column(name="modifyDate")
	private Date modifyDate;

	@Formula("(select count(*) from r_customer_ebook rce  where rce.customer_id = id)")
	public Integer allotNum;
	
	public List<CutomerLibrary> getLibras() {
		return libras;
	}
	public void setLibras(List<CutomerLibrary> libras) {
		this.libras = libras;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public short getIsEffective() {
		return isEffective;
	}
	public void setIsEffective(short isEffective) {
		this.isEffective = isEffective;
	}
	public String getCustomerCode() {
		return customerCode;
	}
	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}
	
	public List<CustomerContact> getContacts() {
		return contacts;
	}
	public void setContacts(List<CustomerContact> contacts) {
		this.contacts = contacts;
	}
	public String getHallCode() {
		return hallCode;
	}
	public void setHallCode(String hallCode) {
		this.hallCode = hallCode;
	}
	public boolean isSendpwd() {
		return sendpwd;
	}
	public void setSendpwd(boolean sendpwd) {
		this.sendpwd = sendpwd;
	}
	public int getDeposit() {
		return deposit;
	}
	public void setDeposit(int deposit) {
		this.deposit = deposit;
	}
	public int getAgreement() {
		return agreement;
	}
	public void setAgreement(int agreement) {
		this.agreement = agreement;
	}
	public double getMinQuota() {
		return minQuota;
	}
	public void setMinQuota(double minQuota) {
		this.minQuota = minQuota;
	}
	public double getMaxQuota() {
		return maxQuota;
	}
	public void setMaxQuota(double maxQuota) {
		this.maxQuota = maxQuota;
	}
	public int getReaderLimit() {
		return readerLimit;
	}
	public void setReaderLimit(int readerLimit) {
		this.readerLimit = readerLimit;
	}
	public int getBorrowNum() {
		return borrowNum;
	}
	public void setBorrowNum(int borrowNum) {
		this.borrowNum = borrowNum;
	}
	public int getBorrowDays() {
		return borrowDays;
	}
	public void setBorrowDays(int borrowDays) {
		this.borrowDays = borrowDays;
	}
	public double getRent() {
		return rent;
	}
	public void setRent(double rent) {
		this.rent = rent;
	}
	public double getCreditQuota() {
		return creditQuota;
	}
	public void setCreditQuota(double creditQuota) {
		this.creditQuota = creditQuota;
	}
	public String getQuota() {
		return quota;
	}
	public void setQuota(String quota) {
		this.quota = quota;
	}
	public String getModifyUser() {
		return modifyUser;
	}
	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}
	public Date getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	public Integer getAllotNum() {
		return allotNum;
	}
	public void setAllotNum(Integer allotNum) {
		this.allotNum = allotNum;
	}
	
}
