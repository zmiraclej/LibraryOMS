package com.flea.common.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flea.common.Common;
import com.flea.modules.customer.pojo.Customer;
import com.flea.modules.system.pojo.Library;



@Entity
@Table(name="system_user")
@DynamicInsert @DynamicUpdate
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1679500750117569491L;
	
	@Id
	@GenericGenerator(name="generator",strategy="identity")
	@GeneratedValue(generator="generator")
	@Column(name ="id",unique=true,nullable= false)
	private Integer id;
	@Column(name="userName",length =30)
	private String userName;
	@Column(name="realName",length =30)
	private String realName;
	@Column(name="phone",length =30)
	private String phone;
	@Column(name="tel",length =30)
	private String tel;
	@Column(name="chat",length =30)
	private String chat;
	@Column(name="hallCode",length =30)
	private String hallCode;
	
	/*年龄*/
	private Short age;
	/*性别*/
	private Integer sex;
	@Column(length=18,unique=true)
	private String identificationCard;
	@Column
	private Date regDate;
	@Column(name="password",length =64)
	private String password;
	@Column(name="isEffective",length =10)
	private short isEffective = Common.FLAG_ACTIVATION;
	@Column(name="idCard",length =18)
	private String idCard;
	
	@Column(name="depName",length =18)
	private String depName;
	
	@Column(name="duty",length =18)
	private String duty;
	
	@Column(name="jobNumber",length =18)
	private String jobNumber;
	
	@Column(name="remark",length =40)
	private String remark;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="customerId")
	private Customer customer;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name="system_user_role",joinColumns={@JoinColumn(name="userId")},
		inverseJoinColumns={@JoinColumn(name="roleId")})
	@Fetch(FetchMode.SUBSELECT)
	private List<Role> roles;
	
//	@OneToMany(cascade= CascadeType.ALL,fetch = FetchType.LAZY,mappedBy ="user")
//	private Set<Customer> customer = new HashSet<Customer>(0);
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "libraryId")
	private Library library;
	@Transient
	private String validCode;
	@Transient
	private String roleIds;
	
	@Column(name="modifyUser",length =30)
	private String modifyUser;
	
	@Column(name="modifyDate")
	private Date modifyDate;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public short getIsEffective() {
		return isEffective;
	}
	public void setIsEffective(short isEffective) {
		this.isEffective = isEffective;
	}
	
	public List<Role> getRoles() {
		return roles;
	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	public String getValidCode() {
		return validCode;
	}
	public void setValidCode(String validCode) {
		this.validCode = validCode;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public Library getLibrary() {
		return library;
	}
	public void setLibrary(Library library) {
		this.library = library;
	}
	
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Date getRegDate() {
		return regDate;
	}
	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}
	@Transient
	public boolean isAdmin(){
		return isAdmin(this.id);
	}
	
	@Transient
	public static boolean isAdmin(Integer id){
		return id != null && id==1;
	}
	public Short getAge() {
		return age;
	}
	public void setAge(Short age) {
		this.age = age;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public String getIdentificationCard() {
		return identificationCard;
	}
	public void setIdentificationCard(String identificationCard) {
		this.identificationCard = identificationCard;
	}
	
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getChat() {
		return chat;
	}
	public void setChat(String chat) {
		this.chat = chat;
	}
	public String getDepName() {
		return depName;
	}
	public void setDepName(String depName) {
		this.depName = depName;
	}
	public String getDuty() {
		return duty;
	}
	public void setDuty(String duty) {
		this.duty = duty;
	}
	public String getJobNumber() {
		return jobNumber;
	}
	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}
	@Transient
	public String getRoleIds() {
		return roleIds;
	}
	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}
	
	public void addRole(Role role) {
		if(roles==null) {
			roles = new ArrayList<Role>();
		}
		roles.add(role);
	}
	
	/**
	 * 如果此 User 包含 Role集合中所有Role  则返回 true。 
	 * @param role 将检查是否包含在此 Role集合 中的 Role 集合
	 * @return 包含返回true 不包含方法false
	 */
	public boolean cuserUserRoleAll(Collection<Role> role){
		return roles.containsAll(role);//重写了Role equals方法
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getHallCode() {
		return hallCode;
	}
	public void setHallCode(String hallCode) {
		this.hallCode = hallCode;
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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
