/**  
* @Package com.flea.modules.customer.pojo
* @Description: TODO
* @author bruce
* @date 2016年4月15日 下午3:47:44
* @version V1.0  
*/ 
package com.flea.modules.customer.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.flea.common.Constants;
import com.flea.common.pojo.Area;
import com.flea.common.pojo.City;
import com.flea.common.pojo.Province;

/**
 * @author bruce
 * @2016年4月15日 下午3:47:44
 */
@Entity
@Table(name="system_customer_library")
public class CutomerLibrary {

	@Id
	@GenericGenerator(name="generator",strategy="identity")
	@GeneratedValue(generator="generator")
	@Column(name ="id",unique=true,nullable= false)
	private Integer libraryId;
	
	@Column(name="libraryLevel",length=10)
	private String libraryLevel;
	
	@Column(name="libraryNumber",length=3)
	private String libraryNumber;
	
	@Column(name="assignCode",length=20)
	private String assignCode;
	
	@Column(name="startCode",length=20)
	private String startCode;
	
	@Column(name="endCode",length=20)
	private String endCode;
	
	@Column(name="chargeStandard",length=40)
	private String chargeStandard;
	
	@Column(name="chargeMoney",length=20)
	private String chargeMoney;
	
	@Column(name="chargeStartDate")
	private String chargeStartDate;
	
	@Column(name="chargeEndDate")
	private String chargeEndDate;
	
	@Column(name="contractStartDate")
	private String contractStartDate;
	
	@Column(name="contractEndDate")
	private String contractEndDate;
	
	@Column(name="attachement")
	private String attachement;
	
	/**附件文件名*/
	@Column(name="attachementFile")
	private String attachementFile;

	@Column(name="createTime")
	private Date createTime;
	
	@Column(name="isEffective")
	private short isEffective =Constants.FLAG_ACTIVATION;
	
	@Column(name="orderNo",length=32)
	private String orderNo;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="customerId")
	private Customer customer;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cityCode")
	private City city;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "provinceCode")
	private Province province;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "areaCode")
	private Area area;
	
	@Column(name="usedCodeNumber",length=20)
	private Integer usedCodeNumber =0;
	
//	@OneToMany(cascade= CascadeType.ALL,fetch = FetchType.LAZY,mappedBy ="cutomerLibrary")
//	private List<LibraryCode> libraryCodes  = new ArrayList<LibraryCode>();
	
	
	public CutomerLibrary() {
	}
	
	public CutomerLibrary(Integer id, String libraryLevel) {
		super();
		this.libraryId = id;
		this.libraryLevel = libraryLevel;
	}
	
	
	public Integer getLibraryId() {
		return libraryId;
	}

	public void setLibraryId(Integer libraryId) {
		this.libraryId = libraryId;
	}

	public String getLibraryLevel() {
		return libraryLevel;
	}
	public void setLibraryLevel(String libraryLevel) {
		this.libraryLevel = libraryLevel;
	}
	public String getLibraryNumber() {
		return libraryNumber;
	}
	public void setLibraryNumber(String libraryNumber) {
		this.libraryNumber = libraryNumber;
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
	public String getAssignCode() {
		return assignCode;
	}
	public void setAssignCode(String assignCode) {
		this.assignCode = assignCode;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public String getChargeStandard() {
		return chargeStandard;
	}
	public void setChargeStandard(String chargeStandard) {
		this.chargeStandard = chargeStandard;
	}
	public String getChargeMoney() {
		return chargeMoney;
	}
	public void setChargeMoney(String chargeMoney) {
		this.chargeMoney = chargeMoney;
	}
	public String getAttachement() {
		return attachement;
	}
	public void setAttachement(String attachement) {
		this.attachement = attachement;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getChargeStartDate() {
		return chargeStartDate;
	}
	public void setChargeStartDate(String chargeStartDate) {
		this.chargeStartDate = chargeStartDate;
	}
	public String getChargeEndDate() {
		return chargeEndDate;
	}
	public void setChargeEndDate(String chargeEndDate) {
		this.chargeEndDate = chargeEndDate;
	}
	public String getContractStartDate() {
		return contractStartDate;
	}
	public void setContractStartDate(String contractStartDate) {
		this.contractStartDate = contractStartDate;
	}
	public String getContractEndDate() {
		return contractEndDate;
	}
	public void setContractEndDate(String contractEndDate) {
		this.contractEndDate = contractEndDate;
	}
	public String getAttachementFile() {
		return attachementFile;
	}
	public void setAttachementFile(String attachementFile) {
		this.attachementFile = attachementFile;
	}
//	public List<LibraryCode> getLibraryCodes() {
//		return libraryCodes;
//	}
//	public void setLibraryCodes(List<LibraryCode> libraryCodes) {
//		this.libraryCodes = libraryCodes;
//	}
	public String getStartCode() {
		return startCode;
	}
	public void setStartCode(String startCode) {
		this.startCode = startCode;
	}
	public String getEndCode() {
		return endCode;
	}
	public void setEndCode(String endCode) {
		this.endCode = endCode;
	}

	public Integer getUsedCodeNumber() {
		return usedCodeNumber;
	}

	public void setUsedCodeNumber(Integer usedCodeNumber) {
		this.usedCodeNumber = usedCodeNumber;
	}
}
