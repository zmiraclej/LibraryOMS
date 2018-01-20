package com.thinkgem.jeesite.modules.prj.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.thinkgem.jeesite.common.persistence.IdEntity;
import com.thinkgem.jeesite.modules.sys.entity.Office;

/**
 * 
 * @author admin
 *
 */
@Entity
@Table(name="StudentInfo")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Student extends IdEntity<Student> {

	private static final long serialVersionUID = 1L;
	
	public static final Short REGISTER = 0;//提交未审核
	
	public static final Short PASS = 1;//审核
	
	public static final Short DELETE = 0;//删除
	
	/*真实姓名*/
	private String realName;
	
	/*年龄*/
	private Short age;
	/*性别*/
	private Character sex;
	
	private String nation;
	
	private String identificationCard;
	/*学历*/
	private String education;
	
	/*学历*/
	private String school;
	
	/*联系电话*/
	private String phone;
	
	/*家庭地址*/
	private String address;
	
	private String parentName;
	
	private Integer parentAge;
	
	private Integer homePerson;

	private Integer farmerPerson;
	
	private Integer workPerson;
	
	/*是否企业*/
	private Short whetherGift;
	
	private Office office;
	
	private double moeny;
	
	/*企业日期*/
	private Date giftDate = new Date();
	
	/*企业原因*/
	private String giftReason;
	
	private String remark;//备注
	
	/*状态*/
	private Short state;
	
	/*保留*/
	private String column1;//
	
	
	public Student() {
	}


	@Temporal(TemporalType.DATE)
	@Column(name="registerDate",updatable = false)
	public Date getGiftDate() {
		return giftDate;
	}


	public void setGiftDate(Date giftDate) {
		this.giftDate = giftDate;
	}
	

	@Column(length=20)
	public String getRealName() {
		return realName;
	}


	public void setRealName(String realName) {
		this.realName = realName;
	}

	@Column(length=20)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@Column(length=20)
	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}


	public Character getSex() {
		return sex;
	}

	public void setSex(Character sex) {
		this.sex = sex;
	}

	public Short getAge() {
		return age;
	}

	public void setAge(Short age) {
		this.age = age;
	}
	
	@Column(length=18)
	public String getIdentificationCard() {
		return identificationCard;
	}

	public void setIdentificationCard(String identificationCard) {
		this.identificationCard = identificationCard;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(length=18)
	public String getEducation() {
		return education;
	}


	public void setEducation(String education) {
		this.education = education;
	}

	@Column(length=100)
	public String getSchool() {
		return school;
	}


	public void setSchool(String school) {
		this.school = school;
	}

	@Column(length=30)
	public String getParentName() {
		return parentName;
	}


	public void setParentName(String parentName) {
		this.parentName = parentName;
	}


	public Integer getParentAge() {
		return parentAge;
	}


	public void setParentAge(Integer parentAge) {
		this.parentAge = parentAge;
	}


	public Integer getHomePerson() {
		return homePerson;
	}


	public void setHomePerson(Integer homePerson) {
		this.homePerson = homePerson;
	}


	public Integer getFarmerPerson() {
		return farmerPerson;
	}


	public void setFarmerPerson(Integer farmerPerson) {
		this.farmerPerson = farmerPerson;
	}


	public Integer getWorkPerson() {
		return workPerson;
	}


	public void setWorkPerson(Integer workPerson) {
		this.workPerson = workPerson;
	}


	public Short getWhetherGift() {
		return whetherGift;
	}


	public void setWhetherGift(Short whetherGift) {
		this.whetherGift = whetherGift;
	}

	
	@ManyToOne
	@JoinColumn(name="office_id")
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}


	public double getMoeny() {
		return moeny;
	}


	public void setMoeny(double moeny) {
		this.moeny = moeny;
	}


	public String getGiftReason() {
		return giftReason;
	}


	public void setGiftReason(String giftReason) {
		this.giftReason = giftReason;
	}





	public Short getState() {
		return state;
	}


	public void setState(Short state) {
		this.state = state;
	}


	public String getColumn1() {
		return column1;
	}


	public void setColumn1(String column1) {
		this.column1 = column1;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


	@Transient
	public boolean isAdmin() {
	
		return false;
	}

}
