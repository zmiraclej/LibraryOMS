//package com.flea.common.pojo;
//
//import java.io.Serializable;
//import java.util.Set;
//
//import javax.persistence.CascadeType;
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.FetchType;
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.JoinTable;
//import javax.persistence.ManyToMany;
//import javax.persistence.ManyToOne;
//import javax.persistence.OneToMany;
//import javax.persistence.OrderBy;
//
//import org.hibernate.annotations.GenericGenerator;
//
//
///**
// * 权限
// * @author admin
// *
// */
//@Entity
//public class Permission extends IdEntity  implements Serializable{
//	private static final long serialVersionUID = 8215563924166196207L;
//	private String name;
//	private String description;//描述
//	private Permission pater;
//	private Set<Permission> childrens;
////	private Set<Role> roles; 
//	private int order;
//
//	public String getName() {
//		return name;
//	}
//	public void setName(String name) {
//		this.name = name;
//	}
//	public String getDescription() {
//		return description;
//	}
//	public void setDescription(String description) {
//		this.description = description;
//	}
//	@ManyToOne(fetch=FetchType.LAZY)
//	public Permission getPater() {
//		return pater;
//	}
//	public void setPater(Permission pater) {
//		this.pater = pater;
//	}
//	@OneToMany(mappedBy="pater")
//	public Set<Permission> getChildrens() {
//		return childrens;
//	}
//	public void setChildrens(Set<Permission> childrens) {
//		this.childrens = childrens;
//	}
//	@Column(name="iorder")
//	@OrderBy("order")
//	public int getOrder() {
//		return order;
//	}
//	public void setOrder(int order) {
//		this.order = order;
//	}
////	@ManyToMany(fetch=FetchType.LAZY,cascade=CascadeType.REMOVE)
////	@JoinTable(name="APP_ROLE_Permission",joinColumns={@JoinColumn(name="PERMISSIONS_ID")},
////	inverseJoinColumns={@JoinColumn(name="APP_ROLE_ROLEID")})
////	public Set<Role> getRoles() {
////		return roles;
////	}
////	public void setRoles(Set<Role> roles) {
////		this.roles = roles;
////	}
//}
