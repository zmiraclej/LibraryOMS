package com.flea.modules.system.pojo;

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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.solr.client.solrj.beans.Field;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;

import com.flea.common.Common;
import com.flea.common.Constants;
import com.flea.common.pojo.Area;
import com.flea.common.pojo.City;
import com.flea.common.pojo.Province;
import com.flea.modules.customer.pojo.CustomerContact;

@Entity
@Table(name="librarys")
@DynamicInsert @DynamicUpdate
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class LibraryCustomer implements Serializable{

	private static final long serialVersionUID = -2149410426380599207L;
	
	@Id
	@GenericGenerator(name="generator",strategy="identity")
	@GeneratedValue(generator="generator")
	@Column(name ="id",unique=true,nullable= false)
	private Integer id;
	
	@Column(name="name")
	private String name;
	@Column(name="hallCode")
	private String hallCode;
	@Column(name="conperson")
	private String conperson;
	@Column(name="phone")
	private String phone;
	@Column(name="fixphone")
	private String fixphone;
	@Column(name="email")
	private String email;
	@Column(name="address")
	private String address;
	@Column(name="lngLat")
	private String lngLat;
	@Column(name="createTime")
	private Date createTime;
	@Column(name="isEffective")
	//isEffective=0,无效馆，1 正常馆，2是测试馆：不通过jms向solr发送数据
	//private String isEffective =Constants.FLAG_STOPED;
	private String isEffective =Constants.FLAG_ENABLE;
	@Column(name="userName")
	private String userName;
	@Column(name="password")
	private String password;
	@Column(name="remark")
	private String remark;
	@Column(name="areaAddress")
	private String areaAddress;
	@Column(name="libraryLevel")
	private String libraryLevel;
	
	@Column(name="borrowModel")
	private String borrowModel=Common.BORROWER_MODEL;
	/*读者限制,0无限制,1限注册*/
	@Column(name="readerLimited",length=2)
	private String readerLimited="1";
	
	@Column(name="needDeposit")
	private String needDeposit="1";
	
	@Column(name="labelDir")
	private String labelDir="1";
	
	/*借阅限制*/
	@Column(name="rent")
	private double rent;
	@Column(name="freeRentDate")
	private int freeRentDate;
	@Column(name="maxSum")
	private int maxSum;
	
	/*删除标志,1表示正常，0表示删除，2停用*/
	private String status = Common.FLAG_ENABLE;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cityCode")
	private City city;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "provinceCode")
	private Province province;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "areaCode")
	private Area area;

	@Column(name="customerLibraryId")
	private Integer customerLibraryId;
	
	@Column(name="customerId")
	private Integer customerId;
	
	@Formula("(select se.hallCode from system_customer se where se.id = customerId)") 
	private String customerHallCode;
	
	@Formula("(select se.name from system_customer se where se.id = customerId)") 
	private String customerName;
	
	@OneToMany(cascade= CascadeType.ALL,fetch = FetchType.LAZY,mappedBy ="library")
	private List<CustomerContact> contacts  = new ArrayList<CustomerContact>();
	
	@Column(name="scope")
	private Integer scope=0;
	
	@Column(name="agreementAccount")
	private String agreementAccount;//协议账户
	
	@Column(name="acountName")
	private String acountName;//户名
	
	@Column(name="creditLines")
	private double creditLines;//授信额度
	
	@Column(name="useStoreroom")
	private int useStoreroom =0;
	
	@Column(name="basenum")
	private int basenum=0;
	
	@Column(name="storeroom")
	private String storeroom="2";

	@Column(name="classRange")
	private int classRange=0;
	
	@Column(name="modifyUser",length =30)
	private String modifyUser;
	
	@Column(name="modifyDate")
	private Date modifyDate;
	
	@Column(name="libraryStatus")
	@Field("libraryStatus")
	private Integer libraryStatus=1;//1待审,2审核通过,3驳回,4弃审,5未提交
	
	//是否停用
	@Column(name="isStop")
	private Integer isStop;
	
	@Column(name="libraryUpdateStatus")
	private Integer libraryUpdateStatus;//4:修改待审 5:修改驳回  6:停用待审  7:停用驳回   8:停用  9:启用待审 10:启用驳回 11:无 12:屏蔽
	
	
//	private String scopeString;
//	
//	public String getScopeString() {
//		return scopeString;
//	}
//	public void setScopeString(String scopeString) {
//		this.scopeString = scopeString;
//	}
	
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
	public City getCity() {
		return city;
	}
	public void setCity(City city) {
		this.city = city;
	}
	public Province getProvince() {
		return province;
	}
	public void setProvince(Province province) {
		this.province = province;
	}
	public Area getArea() {
		return area;
	}
	public void setArea(Area area) {
		this.area = area;
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
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
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
	public String getLibraryLevel() {
		return libraryLevel;
	}
	public void setLibraryLevel(String libraryLevel) {
		this.libraryLevel = libraryLevel;
	}
	public List<CustomerContact> getContacts() {
		return contacts;
	}
	public void setContacts(List<CustomerContact> contacts) {
		this.contacts = contacts;
	}
	public Integer getCustomerLibraryId() {
		return customerLibraryId;
	}
	public void setCustomerLibraryId(Integer customerLibraryId) {
		this.customerLibraryId = customerLibraryId;
	}
	public String getAreaAddress() {
		return areaAddress;
	}
	public void setAreaAddress(String areaAddress) {
		this.areaAddress = areaAddress;
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
	public String getBorrowModel() {
		return borrowModel;
	}
	public void setBorrowModel(String borrowModel) {
		this.borrowModel = borrowModel;
	}
	public String getNeedDeposit() {
		return needDeposit;
	}
	public void setNeedDeposit(String needDeposit) {
		this.needDeposit = needDeposit;
	}
	public String getLabelDir() {
		return labelDir;
	}
	public void setLabelDir(String labelDir) {
		this.labelDir = labelDir;
	}
	public String getReaderLimited() {
		return readerLimited;
	}
	public void setReaderLimited(String readerLimited) {
		this.readerLimited = readerLimited;
	}
	public double getRent() {
		return rent;
	}
	public void setRent(double rent) {
		this.rent = rent;
	}
	public int getFreeRentDate() {
		return freeRentDate;
	}
	public void setFreeRentDate(int freeRentDate) {
		this.freeRentDate = freeRentDate;
	}
	public int getMaxSum() {
		return maxSum;
	}
	public void setMaxSum(int maxSum) {
		this.maxSum = maxSum;
	}
	public String getAgreementAccount() {
		return agreementAccount;
	}
	public void setAgreementAccount(String agreementAccount) {
		this.agreementAccount = agreementAccount;
	}
	public String getAcountName() {
		return acountName;
	}
	public void setAcountName(String acountName) {
		this.acountName = acountName;
	}
	public double getCreditLines() {
		return creditLines;
	}
	public void setCreditLines(double creditLines) {
		this.creditLines = creditLines;
	}
	public int getUseStoreroom() {
		return useStoreroom;
	}
	public void setUseStoreroom(int useStoreroom) {
		this.useStoreroom = useStoreroom;
	}
	public int getBasenum() {
		return basenum;
	}
	public void setBasenum(int basenum) {
		this.basenum = basenum;
	}
	public String getStoreroom() {
		return storeroom;
	}
	public void setStoreroom(String storeroom) {
		this.storeroom = storeroom;
	}
	public int getClassRange() {
		return classRange;
	}
	public void setClassRange(int classRange) {
		this.classRange = classRange;
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
	public String getCustomerHallCode() {
		return customerHallCode;
	}
	public void setCustomerHallCode(String customerHallCode) {
		this.customerHallCode = customerHallCode;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public Integer getLibraryStatus() {
		return libraryStatus;
	}
	public void setLibraryStatus(Integer libraryStatus) {
		this.libraryStatus = libraryStatus;
	}
	public Integer getIsStop() {
		return isStop;
	}
	public void setIsStop(Integer isStop) {
		this.isStop = isStop;
	}
	public Integer getLibraryUpdateStatus() {
		return libraryUpdateStatus;
	}
	public void setLibraryUpdateStatus(Integer libraryUpdateStatus) {
		this.libraryUpdateStatus = libraryUpdateStatus;
	}
	
}
