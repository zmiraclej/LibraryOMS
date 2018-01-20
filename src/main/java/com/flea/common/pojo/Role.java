package com.flea.common.pojo;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;

import com.flea.common.Common;
@Entity
@Table(name="system_role")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Role implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8992163907752928380L;
	@Id
	@GenericGenerator(name="generator",strategy="identity")
	@GeneratedValue(generator="generator")
	@Column(name ="id",unique=true,nullable= false)
	private Integer id;
	@Column(name="description",length =30)
	private String description;
	@Column(name="name",length =30)
	private String name;
	@Column(name="isEffective",length =10)
	private short isEffective =Common.FLAG_ACTIVATION;
	
	@Column(name="authority",length =30)
	private String authority;
	
	@Column(name="level",length =30)
	private String level;
	
	 @ManyToMany(fetch = FetchType.LAZY)
	    @JoinTable(name = "system_role_menu", 
	     joinColumns = { @JoinColumn(name = "roleId", updatable = false) }, 
	     inverseJoinColumns = { @JoinColumn(name = "menuId", updatable = false) })
	@Fetch(FetchMode.SUBSELECT)
	private Set<Menu> menus;
	 
	 @ManyToMany(fetch = FetchType.LAZY)
		@JoinTable(name="system_user_role",joinColumns={@JoinColumn(name="roleId")},
			inverseJoinColumns={@JoinColumn(name="userId")})
	@Fetch(FetchMode.SUBSELECT)
	private List<User> users;
	 
	@Column(name="owner",length =30)
	private Integer owner;
	
	@Column(name="customerId",length =30) 
	private Integer customerId;
	
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
	public short getIsEffective() {
		return isEffective;
	}
	public void setIsEffective(short isEffective) {
		this.isEffective = isEffective;
	}
	public String getAuthority() {
		return authority;
	}
	public void setAuthority(String authority) {
		this.authority = authority;
	}
	
	public Set<Menu> getMenus() {
		return menus;
	}
	public void setMenus(Set<Menu> menus) {
		this.menus = menus;
	}
	
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getOwner() {
		return owner;
	}
	public void setOwner(Integer owner) {
		this.owner = owner;
	}
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	
}
