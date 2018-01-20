package com.flea.modules.system.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import com.google.common.collect.Lists;

@Entity
@Table(name="librarys_users")
@Inheritance(strategy=InheritanceType.JOINED)
public class LibraryUser implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1082363849717697618L;
	
	@Id
	@GenericGenerator(name="generator",strategy="identity")
	@GeneratedValue(generator="generator")
	@Column(name ="id",unique=true,nullable= false)
	private Integer id;
	@Column(name ="userName")
	private String userName;
	@Column(name ="password")
	private String password;
	@Column(name ="identification")
	private String identification;
	@Column(name ="isManager")
	private String isManager;
	@Column(name ="isEffective")
	private String isEffective="1";
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "libraryHallCode")
	private Library library;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "librarys_user_menu", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "menu_id") })
	@Fetch(FetchMode.SUBSELECT)
	private List<LibraryMenu> libraryMenuList = Lists.newArrayList(); // 拥有菜单列表
	
	@OneToMany(cascade= CascadeType.ALL,fetch = FetchType.LAZY,mappedBy ="libraryUser")
	private transient Set<LibraryCompensateRecord> libraryCompensateRecord = new HashSet<LibraryCompensateRecord>(0);
	
	@OneToMany(cascade= CascadeType.ALL,fetch = FetchType.LAZY,mappedBy ="libraryUser")
	private transient Set<LibraryBorrowerDepositRecord> libraryBorrowerDepositRecord = new HashSet<LibraryBorrowerDepositRecord>(0);
	
	
	
	
	private String validCode;
	
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

	public String getIsManager() {
		return isManager;
	}

	public void setIsManager(String isManager) {
		this.isManager = isManager;
	}

	public String getIsEffective() {
		return isEffective;
	}

	public void setIsEffective(String isEffective) {
		this.isEffective = isEffective;
	}

	public Library getLibrary() {
		return library;
	}

	public void setLibrary(Library library) {
		this.library = library;
	}

	public String getValidCode() {
		return validCode;
	}

	public void setValidCode(String validCode) {
		this.validCode = validCode;
	}
	
	
	public List<LibraryMenu> getLibraryMenuList() {
		return libraryMenuList;
	}

	public void setLibraryMenuList(List<LibraryMenu> libraryMenuList) {
		this.libraryMenuList = libraryMenuList;
	}

	public void addLibraryMenu(LibraryMenu menu) {
		if(libraryMenuList==null) {
			libraryMenuList = new ArrayList<LibraryMenu>();
		}
		libraryMenuList.add(menu);
	}
	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}

	public Set<LibraryCompensateRecord> getLibraryCompensateRecord() {
		return libraryCompensateRecord;
	}

	public void setLibraryCompensateRecord(
			Set<LibraryCompensateRecord> libraryCompensateRecord) {
		this.libraryCompensateRecord = libraryCompensateRecord;
	}

	public Set<LibraryBorrowerDepositRecord> getLibraryBorrowerDepositRecord() {
		return libraryBorrowerDepositRecord;
	}

	public void setLibraryBorrowerDepositRecord(
			Set<LibraryBorrowerDepositRecord> libraryBorrowerDepositRecord) {
		this.libraryBorrowerDepositRecord = libraryBorrowerDepositRecord;
	}
	
	
	
}
